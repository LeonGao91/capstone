package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a all lanes within one repeat. The specific structure
 * depends on the test platform and should extend this abstract class.
 *
 * @author Yan 04/29/2015
 * @version 2.0
 *
 */
public abstract class TestMargins {

    protected Stats acrossLanesStats; // across lane statistics including outliers
    protected Stats acrossLanesNoOutlierStats; // statistics excluding outliers
    protected int acrossLaneSize; //channel num * rank num * lane num
    protected int byLaneSize;  //lane num

    /**
     * Constructor with no argument.
     */
    public TestMargins() {
        acrossLanesStats = new Stats();
    }

    /**
     * Get across lane statistics.
     *
     * @return a Stats object
     */
    public Stats getAcrossLanesStats() {
        return acrossLanesStats;
    }

    /**
     * Get across lane statistics.
     *
     * @return a Stats object
     */
    public Stats getAcrossNoOutlierLaneStats() {
        return acrossLanesNoOutlierStats;
    }

    /**
     * Get number of lanes for across lane computation.
     *
     * @return across lane size
     */
    public int getAcrossLaneSize() {
        return acrossLaneSize;
    }

    /**
     * Get number of lanes for by lane computation.
     *
     * @return by lane size
     */
    public int getByLaneSize() {
        return byLaneSize;
    }

    /**
     * Find across lane statistics.
     *
     * @param checkOutlier if true, exclude outliers, if false, include outliers
     */
    public abstract void findAcrossLaneStats(boolean checkOutlier);

    /**
     * Check outliers based on system threshold.
     *
     * @param benchmark benchmark to compare lane statistics with
     * @param threshold threshold to consider outlier
     * @param messages StringBuilder to output outlier messages
     * @return true if outlier found, false otherwise
     */
    public abstract boolean checkSystemOutliers(double benchmark, double threshold, StringBuilder messages);

    /**
     * Check outliers based on lane statistics threshold.
     *
     * @param meanThreshold mean statistics threshold to consider outlier
     * @param medianThreshold median statistics threshold to consider outlier
     * @param messages StringBuilder to output outlier messages
     * @return true if outlier found, false otherwise
     */
    public abstract boolean checkLaneOutliers(double meanThreshold, double medianThreshold, StringBuilder messages);

    /**
     * Add margins to corresponding SummaryStatistics object in "by lane"
     * manner. The definition of "by lane" is determined by the test type.
     *
     * @param ss a SummaryStatistics object to compute statistics
     */
    public abstract void addValuesByLane(SummaryStatistics[] ss);

    /**
     * Add margins to corresponding SummaryStatistics object in "across lane"
     * manner. The definition of "across lane" is determined by the test type.
     *
     * @param ss a SummaryStatistics array to compute statistics for each lane
     */
    public abstract void addValuesAcrossLane(SummaryStatistics[] ss);

    /**
     * Add another TestMargins object to current one and get a new TestMargins
     * object.
     *
     * @param tm another TestMargins to add
     * @return a new TestMargins object representing the addition result
     */
    public abstract TestMargins add(TestMargins tm);

    /**
     * Mark whole lane as outlier by lane index.
     *
     * @param laneIndex "by lane" index
     */
    public abstract void markOutlierByLane(int laneIndex);

    /**
     * Check whether all lanes are invalid.
     *
     * @return true if all invalid, false otherwise
     */
    public abstract boolean checkValidity();

    /**
     * Get lane id by lane index
     *
     * @param index "by lane" index
     * @return lane ID
     */
    public abstract String getLaneIDByIndex(int index);

}
