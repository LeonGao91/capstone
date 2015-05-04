package dataAnalyticsModel;

import java.util.Map;

/**
 * This class represents a brief of test result. It is used to construct and
 * parse xml. Data include test health and trust scores, test date, test
 * conclusion and the file name of detailed test result.
 *
 * @author Yan 04/24/2015
 * @version 1.0
 *
 */
public class TestBrief {

    private String pass_fail; // test conclusion
    private String test_date; // test date
    private double health; // health score
    private double trust; // trust score
    private Map<String, Integer> systems_repeats; // summary of all systems and repeats
    private String result_file; // detailed test result file name

    /**
     * Constructor with no argument
     */
    public TestBrief() {
    }

    /**
     * Constructor with all arguments
     *
     * @param pass_fail test conclusion
     * @param test_date test date
     * @param health health score
     * @param trust trust score
     * @param result_file etailed test result file name
     */
    public TestBrief(String pass_fail, String test_date, double health,
            double trust, String result_file) {
        this.pass_fail = pass_fail;
        this.test_date = test_date;
        this.health = health;
        this.trust = trust;
        this.result_file = result_file;
    }

    /**
     * Set test conclusion.
     *
     * @param pass_fail "pass" or "fail"
     */
    public void setPass_fail(String pass_fail) {
        this.pass_fail = pass_fail;
    }

    /**
     * Set test date
     *
     * @param test_date test date: MMddyyhhmmss
     */
    public void setTest_date(String test_date) {
        this.test_date = test_date;
    }

    /**
     * Set health score
     *
     * @param health health score
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Set trust score
     *
     * @param trust trust score
     */
    public void setTrust(double trust) {
        this.trust = trust;
    }

    /**
     * Set file name of detailed test result.
     *
     * @param fileName detailed test result file name
     */
    public void setResult_file(String fileName) {
        this.result_file = fileName;
    }

    /**
     * Get test conclusion
     *
     * @return "pass" or "fail"
     */
    public String getPass_fail() {
        return pass_fail;
    }

    /**
     * Get test date
     *
     * @return test date: MMddyyhhmmss
     */
    public String getTest_date() {
        return test_date;
    }

    /**
     * Get health score
     *
     * @return health score
     */
    public double getHealth() {
        return health;
    }

    /**
     * Get trust score
     *
     * @return trust score
     */
    public double getTrust() {
        return trust;
    }

    /**
     * Get file name of detailed test result.
     *
     * @return detailed test result file name
     */
    public String getResult_file() {
        return result_file;
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
     * Set summary of systems and repeats information
     *
     * @param systems_repeats a map of all systems and number of repeats
     */
    public void setSystems_repeats(Map<String, Integer> systems_repeats) {
        this.systems_repeats = systems_repeats;
    }
}
