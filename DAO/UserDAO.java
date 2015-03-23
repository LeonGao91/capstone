import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import databean.UserBean;

public class UserDAO {
	private List<Connection> connectionPool = new ArrayList<Connection>();

	private String jdbcDriver;
	private String jdbcURL;
	private String tableName;

	public UserDAO(String jdbcDriver, String jdbcURL, String tableName) throws MyDAOException {
		this.jdbcDriver = jdbcDriver;
		this.jdbcURL = jdbcURL;
		this.tableName = tableName;

		if (!tableExists()) {
			createTable();
		}
	}

	private synchronized Connection getConnection() throws MyDAOException {
		if (connectionPool.size() > 0) {
			return connectionPool.remove(connectionPool.size() - 1);
		}

		try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			throw new MyDAOException(e);
		}

		try {
			return DriverManager.getConnection(jdbcURL);
		} catch (SQLException e) {
			throw new MyDAOException(e);
		}
	}

	private synchronized void releaseConnection(Connection con) {
		connectionPool.add(con);
	}

	// create a new user record in the "user" table
	public synchronized void create(UserBean user) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO " + tableName
					+ " (emailAddr,fName,lName,password) VALUES (?,?,?,?)");

			pstmt.setString(1, user.getEmailAddr());
			pstmt.setString(2, user.getFName());
			pstmt.setString(3, user.getLName());
			pstmt.setString(4, user.getPassword());

			int count = pstmt.executeUpdate();
			if (count != 1) {
				throw new SQLException("Insert updated " + count + " rows");
			}

			pstmt.close();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
			rs.next();
			user.setUserId(rs.getInt("LAST_INSERT_ID()"));
			// the first automatically generated value that was set for an AUTO_INCREMENT column
			// by the most recently executed INSERT statement to affect such a column

			releaseConnection(con);
		} catch (Exception e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	// take a user's email address and return a UserBean containing information about that user
	public UserBean read(String emailAddr) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM "
					+ tableName + " WHERE emailAddr=?");
			pstmt.setString(1, emailAddr); // set value for emailAddr
			ResultSet rs = pstmt.executeQuery();

			UserBean user;
			if (!rs.next()) {
				user = null; // no such user
			} else {
				// get values from the result table and create a new UserBean
				user = new UserBean();
				user.setUserId(rs.getInt("userId"));
				user.setEmailAddr(rs.getString("emailAddr"));
				user.setFName(rs.getString("fName"));
				user.setLName(rs.getString("lName"));
				user.setPassword(rs.getString("password"));
			}

			rs.close();
			pstmt.close();
			releaseConnection(con);

			return user;
		} catch (Exception e) {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e2) { /* ignore */
			}
			throw new MyDAOException(e);
		}
	}

	// return all the users in the database as an array of UserBean
	public UserBean[] getUsers() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + tableName);
			ResultSet rs = pstmt.executeQuery();

			List<UserBean> list = new ArrayList<UserBean>();
			while (rs.next()) {
				UserBean user = new UserBean();
				user.setUserId(rs.getInt("userId"));
				user.setEmailAddr(rs.getString("emailAddr"));
				user.setFName(rs.getString("fName"));
				user.setLName(rs.getString("lName"));
				user.setPassword(rs.getString("password"));
				list.add(user);
			}

			rs.close();
			pstmt.close();
			releaseConnection(con);

			return list.toArray(new UserBean[list.size()]);
		} catch (Exception e) {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	// for users to change password
	public synchronized void setPassword(int userId, String newPassword) throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("UPDATE "
					+ tableName + " SET password=? WHERE userId=?");
			pstmt.setString(1, newPassword);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
			pstmt.close();
			releaseConnection(con);
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	private boolean tableExists() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet rs = metaData.getTables(null, null, tableName, null);

			boolean answer = rs.next();

			rs.close();
			releaseConnection(con);

			return answer;
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	// return number of rows
	public int size() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(userId) FROM " + tableName);

			rs.next();
			int count = rs.getInt("COUNT(userId)");

			stmt.close();
			releaseConnection(con);

			return count;
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) {
			}
			throw new MyDAOException(e);
		}
	}

	// create a new "user" table
	private void createTable() throws MyDAOException {
		Connection con = null;
		try {
			con = getConnection();
			Statement stmt = con.createStatement();
			stmt.executeUpdate("CREATE TABLE " + tableName
					+ " (userId INT NOT NULL AUTO_INCREMENT, emailAddr VARCHAR(255), fName VARCHAR(50),"
					+ " lName VARCHAR(50), password VARCHAR(255), PRIMARY KEY(userId))");
			stmt.close();

			releaseConnection(con);
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e2) { /* ignore */
			}
			throw new MyDAOException(e);
		}
	}
}
