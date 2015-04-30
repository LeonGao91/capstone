package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a repeat of a validation test which contain margin data
 * of all lanes of that repeat. It is used as a wrapper to accommodate all lower
 * lever data structures.
 *
 * @author Yan, Lucy 04/29/2015
 * @version 2.0
 *
 */
public class TestRepeat {

    public static boolean VALID = true; //this lane is not an outlier
    public static boolean INVALID = false; //this lane is an outlier
    private TestMargins margins; //All lane margins
    private Stats basicStats; // basic statistics
    private Stats noOutlierStats; // statistics after removing outliers
    private int byLaneNumber;
    private int acrossLaneNumber;
    private String repeatID;

    /**
     * Constructor with no argument.
     */
    public TestRepeat() {
    }

    /**
     * Constructor with one TestLane array argument.
     *
     * @param margins a TestMargins object representing all lanes of one repeat.
     */
    public TestRepeat(TestMargins margins) {
        this.margins = margins;
        basicStats = margins.getAcrossLanesStats();
        noOutlierStats = margins.getAcrossNoOutlierLaneStats();
        byLaneNumber = margins.getByLaneSize();
        acrossLaneNumber = margins.getAcrossLaneSize();
        repeatID = "";
    }

    /**
     * Constructor with two basic arguments. This constructor should be the most
     * often used.
     *
     * @param margins a TestMargins object representing all lanes of one repeat.
     * @param repeatID repeat ID
     */
    public TestRepeat(TestMargins margins, String repeatID) {
        this.margins = margins;
        basicStats = margins.getAcrossLanesStats();
        noOutlierStats = margins.getAcrossNoOutlierLaneStats();
        byLaneNumber = margins.getByLaneSize();
        acrossLaneNumber = margins.getAcrossLaneSize();
        this.repeatID = repeatID;
    }

    /**
     * Get basic stats.
     *
     * @return Stats object storing basic stats
     */
    public Stats getBasicStats() {
        return basicStats;
    }

    /**
     * Get stats after removing outliers.
     *
     * @return Stats object storing basic stats
     */
    public Stats getNoOutlierStats() {
        return noOutlierStats;
    }

    /**
     * Find outliers based on system threshold.
     *
     * @param benchmark benchmark to compare lane statistics with
     * @param threshold threshold to consider outlier
     * @param messages StringBuilder to output outlier messages
     * @return true if outlier found, false otherwise
     */
    public boolean findSystemOutliers(double benchmark, double threshold, StringBuilder messages) {
        return margins.checkSystemOutliers(benchmark, threshold, messages);
    }

    /**
     * Check find based on lane statistics threshold.
     *
     * @param meanThreshold mean statistics threshold to consider outlier
     * @param medianThreshold median statistics threshold to consider outlier
     * @param messages StringBuilder to output outlier messages
     * @return true if outlier found, false otherwise
     */
    public boolean findLaneOutliers(double meanThreshold, double medianThreshold, StringBuilder messages) {
        return margins.checkLaneOutliers(meanThreshold, medianThreshold, messages);
    }

    /**
     * Mark whole lane as outlier by lane index.
     *
     * @param laneIndex "by lane" index
     */
    public void markOutlierByLane(int laneIndex) {
        margins.markOutlierByLane(laneIndex);
    }

    /**
     * Get number of lanes for by lane computation.
     *
     * @return by lane size
     */
    public int getByLaneSize() {
        return byLaneNumber;
    }

    /**
     * Get number of lanes for across lane computation.
     *
     * @return across lane size
     */
    public int getAcrossLaneSize() {
        return acrossLaneNumber;
    }

    /**
     * Get all margins of the repeat
     *
     * @return a TestMargins object representing all margins of the repeat
     */
    public TestMargins getMargins() {
        return margins;
    }

    /**
     * Add margins to corresponding SummaryStatistics object in "by lane"
     * manner. The definition of "by lane" is determined by the test type.
     *
     * @param ss a SummaryStatistics object to compute statistics
     */
    public void addValuesByLane(SummaryStatistics[] ss) {
        margins.addValuesByLane(ss);
    }

    /**
     * Add margins to corresponding SummaryStatistics object in "across lane"
     * manner. The definition of "across lane" is determined by the test type.
     *
     * @param ss a SummaryStatistics array to compute statistics for each lane
     */
    public void addValuesAcrossLane(SummaryStatistics[] ss) {
        margins.addValuesAcrossLane(ss);
    }

    /**
     * Add another TestRepeat object to current one and get a new TestRepeat
     * object.
     *
     * @param tr another TestRepeat to add
     * @return a new TestRepeat object representing the addition result
     */
    public TestRepeat add(TestRepeat tr) {
        TestMargins newTestMargins = this.margins.add(tr.getMargins());
        return new TestRepeat(newTestMargins);
    }

    /**
     * Find across lane statistics.
     *
     * @param checkOutlier if true, exclude outliers, if false, include outliers
     */
    public void findStats(boolean checkOutlier) {
        margins.findAcrossLaneStats(checkOutlier);
    }

    /**
     * Method to check whether all lanes are invalid.
     *
     * @return true if all invalid, false otherwise
     */
    public boolean checkValidity() {
        return margins.checkValidity();
    }

    /**
     * Get the repeat ID.
     *
     * @return repeat ID
     */
    public String getRepeatID() {
        return repeatID;
    }

    /**
     * Set the repeat ID.
     *
     * @param repeatID repeat ID
     */
    public void setRepeatID(String repeatID) {
        this.repeatID = repeatID;
    }

    /**
     * Method to get lane id by lane index
     *
     * @param index "by lane" index
     * @return channelID+rankID+laneID
     */
    public String getLaneIDByIndex(int index) {
        return margins.getLaneIDByIndex(index);
    }

    /**
     * Get a string representation of all margins within a repeat.
     *
     * @return information of all margins within a repeat
     */
    @Override
    public String toString() {
        return margins.toString();
    }
}
