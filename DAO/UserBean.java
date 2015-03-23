public class UserBean {
	private int userId; // primary key
	private String emailAddr;
	private String fName;
	private String lName;
	private String password;

	// get methods
	public int getUserId() {
		return userId;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public String getFName() {
		return fName;
	}

	public String getLName() {
		return lName;
	}

	public String getPassword() {
		return password;
	}

	// set methods
	public void setUserId(int i) {
		userId = i;
	}

	public void setEmailAddr(String s) {
		emailAddr = s;
	}

	public void setFName(String s) {
		fName = s;
	}

	public void setLName(String s) {
		lName = s;
	}

	public void setPassword(String s) {
		password = s;
	}
}
