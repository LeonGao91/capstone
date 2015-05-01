package dataAnalyticsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a test product. It is mainly used to convert and parse
 * xml Data include product last test date, test conclusions, all systems and
 * repeats and brief information of all tests.
 * 
 * @author Yan 04/24/2015
 * @version 1.0
 *
 */
public class TestProduct {
	private String last_test_date; // last test date
	private String pass_fail; // last test conclusion
	private Map<String, Integer> systems_repeats; // summary of all systems and
													// repeats
	private LinkedList<TestBrief> tests; // all tests brief information

	/**
	 * Constructor with no argument
	 */
	public TestProduct() {
		systems_repeats = new LinkedHashMap<>();
		tests = new LinkedList<>();
	}

	/**
	 * Constructor with all arguments
	 * 
	 * @param last_test_date
	 *            last test date
	 * @param pass_fail
	 *            last test conclusion
	 * @param systems_repeats
	 *            summary of all systems and repeats
	 * @param tests
	 *            all tests brief information
	 */
	public TestProduct(String last_test_date, String pass_fail,
			Map<String, Integer> systems_repeats, LinkedList<TestBrief> tests) {
		this.last_test_date = last_test_date;
		this.pass_fail = pass_fail;
		this.systems_repeats = systems_repeats;
		this.tests = tests;
	}

	/**
	 * Set last test date
	 * 
	 * @param last_test_date
	 *            last test date MMddyyhhmmss
	 */
	public void setLast_test_date(String last_test_date) {
		this.last_test_date = last_test_date;
	}

	/**
	 * Set last test conclusion
	 * 
	 * @param pass_fail
	 *            "pass" or "fail"
	 */
	public void setPass_fail(String pass_fail) {
		this.pass_fail = pass_fail;
	}

	/**
	 * Set summary of systems and repeats information
	 * 
	 * @param systems_repeats
	 *            a map of all systems and number of repeats
	 */
	public void setSystems_repeats(Map<String, Integer> systems_repeats) {
		this.systems_repeats = systems_repeats;
	}

	/**
	 * Set all tests of the product
	 * 
	 * @param tests
	 *            a list of all test brief object
	 */
	public void setTests(LinkedList<TestBrief> tests) {
		this.tests = tests;
	}

	/**
	 * Add new test to the product
	 * 
	 * @param testBrief
	 *            new test brief object
	 */
	public void addTest(TestBrief testBrief) {
		this.tests.add(testBrief);
	}

	/**
	 * Add new test's systems and repeats information to product
	 * 
	 * @param systems_repeats
	 *            new test systems and repeats information
	 */
	public void updateSystemsRepeats(Map<String, Integer> systems_repeats) {
		String systemID;
		int repeat;
		for (Map.Entry<String, Integer> entry : systems_repeats.entrySet()) {
			systemID = entry.getKey();
			repeat = entry.getValue();
			// If system exists, add repeats
			if (this.systems_repeats.containsKey(systemID)) {
				this.systems_repeats.put(systemID,
						this.systems_repeats.get(systemID) + repeat);
			} else { // If not exist, add new record
				this.systems_repeats.put(systemID, repeat);
			}
		}
	}

	/**
	 * Get last test date
	 * 
	 * @return last test date MMddyyhhmmss
	 */
	public String getLast_test_date() {
		return last_test_date;
	}

	/**
	 * Get last test conclusion
	 * 
	 * @return last test conclusion "pass" or "fail"
	 */
	public String getPass_fail() {
		return pass_fail;
	}

	/**
	 * Get systems and repeats information
	 * 
	 * @return a map of all systems and number of repeats
	 */
	public Map<String, Integer> getSystems_repeats() {
		return systems_repeats;
	}

	/**
	 * Get all test information
	 * 
	 * @return a list of all test brief object
	 */
	public LinkedList<TestBrief> getTests() {
		return tests;
	}
        
        /**
         * get all tests
         * @return all test
         */
        public LinkedList<TestBrief> getElements() {
            return tests;
        }
        
        /**
         * Get last test file
         * @return Last test
         */
        public String getLastTest() {
            if(tests.size() <= 0) {
                return "";
            }
            TestBrief last = tests.get(tests.size() - 1);
            return last.getResult_file();
        }
        
        /**
         * Get time series data as a string
         */
        public String getTimeSeriesData() throws ParseException {
            StringBuilder sb = new StringBuilder();
            for(TestBrief test : tests) {
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
                Date d = date.parse(test.getTest_date());
                date.applyPattern("HH:mm:ss - MM/dd/yyyy");
                sb.append("['").append(date.format(d)).append("', ")
                        .append(test.getHealth()).append(", ").append(test.getTrust()).append("],");
            }
            sb.setLength(sb.length() - 1);
            return sb.toString();
        }
        
        /**
         * Get time series data as a string
         */
        public String getCombinationData() throws ParseException {
            StringBuilder sb = new StringBuilder();
            for(TestBrief test : tests) {
                sb.append("{ x:")
                        .append(test.getHealth()).append(", ").append(" y:").append(test.getTrust()).append("},");
            }
            sb.setLength(sb.length() - 1);
            return sb.toString();
        }
}
