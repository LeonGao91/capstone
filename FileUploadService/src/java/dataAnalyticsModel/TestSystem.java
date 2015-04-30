package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a system of the validation test. Statistics across
 * repeats are computed here.
 *
 * @author Yan, Lucy 04/29/2015
 * @version 2.0
 *
 */
public class TestSystem {

    public static boolean CHECKOUTLIER = true;
    public static boolean NOTCHECKOUTLIER = false;
    private TestRepeat[] repeats; // all repeats of one system
    private Stats noOutlierStats; //syste statistics excluding outliers
    private Stats basicStats; //system statistics including outliers
    private int size; // number of repeats
    private String systemID;

    /**
     * Constructor with no argument.
     */
    public TestSystem() {
    }

    /**
     * Constructor with one TestRepeat array argument.
     *
     * @param repeats an array of TestRepeat representing all repeats of one
     * system.
     */
    public TestSystem(TestRepeat[] repeats) {
        size = repeats.length;
        this.repeats = repeats;
        basicStats = new Stats();
        noOutlierStats = new Stats();
        systemID = "";
        findStats(Test.NOTCHECKOUTLIER);
    }

    /**
     * Constructor with two basic arguments. This constructor should be the most
     * often used.
     *
     * @param repeats an array of TestRepeat representing all repeats of one
     * system.
     * @param systemID system ID
     */
    public TestSystem(TestRepeat[] repeats, String systemID) {
        size = repeats.length;
        this.repeats = repeats;
        basicStats = new Stats();
        noOutlierStats = new Stats();
        this.systemID = systemID;
        findStats(Test.NOTCHECKOUTLIER);
    }

    /**
     * Calculate statistics within this system.
     *
     * @param checkOutlier indicate whether to remove outliers
     */
    public final void findStats(boolean checkOutlier) {
        SummaryStatistics ssMin = new SummaryStatistics();
        SummaryStatistics ssMean = new SummaryStatistics();
        // assign an output Stats object according to outlier indicator
        Stats outputStats = checkOutlier ? noOutlierStats : basicStats;
        Stats tempRepeatStats;

        for (int i = 0; i < size; i++) {
            // ignore outliers or remove ourliers
            tempRepeatStats = checkOutlier ? getRepeatByIndex(i).getNoOutlierStats() : getRepeatByIndex(i).getBasicStats();
            //exclude NA statistics from each repeat
            if (Double.compare(tempRepeatStats.getMin(), Double.NaN) != 0) {
                ssMin.addValue(tempRepeatStats.getMin());
            }
            //exclude NA statistics from each repeat
            if (Double.compare(tempRepeatStats.getMean(), Double.NaN) != 0) {
                ssMean.addValue(tempRepeatStats.getMean());
            }
        }
        // store computed stats
        outputStats.setSigmaMin(ssMin.getStandardDeviation());
        outputStats.setMeanMin(ssMin.getMean());
        outputStats.setSigmaMean(ssMean.getStandardDeviation());
        outputStats.setMean(ssMean.getMean());
        outputStats.setMin(ssMin.getMin());
    }

    /**
     * Check whether the whole system is invalid.
     *
     * @return true if all invalid, false otherwise
     */
    public boolean checkValidity() {
        boolean outlier = false;
        for (int i = 0; i < size; i++) {
            outlier = outlier && getRepeatByIndex(i).checkValidity();
        }
        return outlier;
    }

    /**
     * Get number of repeats of the test
     *
     * @return number of repeats
     */
    public int getSize() {
        return size;
    }

    /**
     * Get certain repeat by repeat index
     *
     * @param index repeat index
     * @return TestRepeat object
     */
    public TestRepeat getRepeatByIndex(int index) {
        return repeats[index];
    }

    /**
     * Get stats excluding outliers.
     *
     * @return Stats object storing stats after removing outliers
     */
    public Stats getNoOutlierStats() {
        return noOutlierStats;
    }

    /**
     * Get stats including outliers
     *
     * @return Stats object storing stats before removing outliers
     */
    public Stats getBasicStats() {
        return basicStats;
    }

    /**
     * Get system ID.
     *
     * @return system ID
     */
    public String getSystemID() {
        return systemID;
    }

    /**
     * Get system ID.
     *
     * @param systemID system ID
     */
    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }

    /**
     * Get a string representation of the system.
     *
     * @return information of the system including statistics
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (int i = 0; i < size; i++) {
            temp = getRepeatByIndex(i) + ": \n";
            sb.append(temp);
            sb.append(getRepeatByIndex(i).toString());
        }
        sb.append("system basic stats: \n");
        sb.append(basicStats.toString());
        sb.append("system no outlier stats: \n");
        sb.append(noOutlierStats.toString());
        return sb.toString();
    }

}
