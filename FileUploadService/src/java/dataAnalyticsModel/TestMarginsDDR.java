package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents all lanes within one repeat for DDR platform. There are
 * three layers: channel, repeat, lane. The definition of "across lane" is total
 * lane number. The definition of "by lane" is total channel number * total rank
 * number * total lane number.
 *
 * @author Yan 04/29/2015
 * @version 2.0
 *
 */
public class TestMarginsDDR extends TestMargins {

    private TestLane[][][] lanes; // Margins organized by channel, rank and lane layers
    private int channelNum; // number of channels
    private int rankNum; // number of ranks
    private int laneNum; // number of lanes
    private double[][] acrossLaneMeans; //lane means for all channels and ranks
    private double[][] acrossLaneMins; //lane mins for all channels and ranks
    private double[][] acrossLaneMedians; //lane medians for all channels and ranks
    private double[][] acrossLaneNoOutlierMeans; //lane means for all channels and ranks after removing outliers
    private double[][] acrossLaneNoOutlierMins; //lane mins for all channels and ranks after removing outliers
    private double[][] acrossLaneNoOutlierMedians; //lane medians for all channels and ranks after removing outliers

    /**
     * Constructor with no argument
     */
    public TestMarginsDDR() {
        super();
    }

    /**
     * Constructor with one three-dimensional array of TestLanes
     *
     * @param lanes a three-dimensional array of TestLanes representing all
     * lanes within one repeat for DDR platform.
     */
    public TestMarginsDDR(TestLane[][][] lanes) {
        this.lanes = lanes;
        channelNum = lanes.length;
        rankNum = lanes[0].length;
        laneNum = lanes[0][0].length;
        acrossLaneMeans = new double[channelNum][rankNum];
        acrossLaneMins = new double[channelNum][rankNum];
        acrossLaneMedians = new double[channelNum][rankNum];
        acrossLaneNoOutlierMeans = new double[channelNum][rankNum];
        acrossLaneNoOutlierMins = new double[channelNum][rankNum];
        acrossLaneNoOutlierMedians = new double[channelNum][rankNum];
        acrossLaneSize = laneNum;
        byLaneSize = channelNum * rankNum * laneNum;
        acrossLanesStats = new Stats();
        acrossLanesNoOutlierStats = new Stats();
        findAcrossLaneStats(Test.NOTCHECKOUTLIER);
    }

    /**
     * Find across lane statistics.
     *
     * @param checkOutlier if true, exclude outliers, if false, include outliers
     */
    @Override
    public final void findAcrossLaneStats(boolean checkOutlier) {
        //match output objects
        Stats outputStats = checkOutlier ? acrossLanesNoOutlierStats : acrossLanesStats;
        double means[][] = checkOutlier ? acrossLaneNoOutlierMeans : acrossLaneMeans;
        double mins[][] = checkOutlier ? acrossLaneNoOutlierMins : acrossLaneMins;
        double medians[][] = checkOutlier ? acrossLaneNoOutlierMedians : acrossLaneMedians;
        SummaryStatistics ss = new SummaryStatistics();
        DescriptiveStatistics[][] ds = new DescriptiveStatistics[channelNum][rankNum];
        // traverse each lane
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < rankNum; j++) {
                ds[i][j] = new DescriptiveStatistics();
                for (int k = 0; k < laneNum; k++) {
                    // only include valid values
                    if (getLaneByIndexes(i, j, k).isValid()) {
                        ds[i][j].addValue(getLaneByIndexes(i, j, k).getMargin());
                        ss.addValue(getLaneByIndexes(i, j, k).getMargin());
                    }
                }
            }
        }
        // store computed stats
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < rankNum; j++) {
                means[i][j] = ds[i][j].getMean();
                mins[i][j] = ds[i][j].getMin();
                medians[i][j] = ds[i][j].getPercentile(50);
            }
        }
        outputStats.setMean(ss.getMean());
        outputStats.setMin(ss.getMin());
    }

    /**
     * Check outliers based on system threshold.
     *
     * @param benchmark benchmark to compare lane statistics with
     * @param threshold threshold to consider outlier
     * @param messages StringBuilder to output outlier messages
     * @return true if outlier found, false otherwise
     */
    @Override
    public boolean checkSystemOutliers(double benchmark, double threshold, StringBuilder messages) {
        String message = "";
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < rankNum; j++) {
                if (Math.abs(acrossLaneMeans[i][j] - benchmark) > threshold) {
                    message = getLaneByIndexes(i, j, 0).getLaneAttribute(TestLane.CHANNELID) + getLaneByIndexes(i, j, 0).getLaneAttribute(TestLane.RANKID);
                    if (messages.indexOf(message) == -1) {
                        message = "####" + message + ";";
                        messages.append(message);
                    }
                    //mark outlier lanes as invalid
                    for (int k = 0; k < laneNum; k++) {
                        getLaneByIndexes(i, j, k).setState(TestLane.INVALID);
                    }
                }
            }
        }
        return !message.isEmpty();
    }

    /**
     * Check outliers based on lane statistics threshold.
     *
     * @param meanThreshold mean statistics threshold to consider outlier
     * @param medianThreshold median statistics threshold to consider outlier
     * @param messages StringBuilder to output outlier messages
     * @return true if outlier found, false otherwise
     */
    @Override
    public boolean checkLaneOutliers(double meanThreshold, double medianThreshold, StringBuilder messages) {
        String message = "";
        boolean meanOutlier;
        boolean medianOutlier;
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < rankNum; j++) {
                meanOutlier = (acrossLaneMeans[i][j] - acrossLaneMins[i][j]) > meanThreshold;
                medianOutlier = (acrossLaneMedians[i][j] - acrossLaneMins[i][j]) > medianThreshold;
                if (meanOutlier || medianOutlier) {
                    message = getLaneByIndexes(i, j, 0).getLaneAttribute(TestLane.CHANNELID) + " " + getLaneByIndexes(i, j, 0).getLaneAttribute(TestLane.RANKID);
                    if (messages.indexOf(message) == -1) {
                        message = "####" + message + ";";
                        messages.append(message);
                    }
                    for (int k = 0; k < laneNum; k++) {
                        getLaneByIndexes(i, j, k).setState(TestLane.INVALID);
                    }
                }
            }
        }
        return !message.isEmpty();
    }

    /**
     * Method to add margins to corresponding SummaryStatistics object in "by
     * lane" manner.
     *
     * @param ss a SummaryStatistics object to compute statistics
     */
    @Override
    public void addValuesByLane(SummaryStatistics[] ss) {
        if (ss.length != byLaneSize) {
            System.out.println("Sizes Don't match");
        } else {
            int index;
            for (int i = 0; i < channelNum; i++) {
                for (int j = 0; j < rankNum; j++) {
                    for (int k = 0; k < laneNum; k++) {
                        // only include valid values
                        if (getLaneByIndexes(i, j, k).isValid()) {
                            index = i * rankNum * laneNum + j * laneNum + k;
                            ss[index].addValue(getLaneByIndexes(i, j, k).getMargin());
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to add margins to corresponding SummaryStatistics object in
     * "across lane" manner.
     *
     * @param ss a SummaryStatistics array to compute statistics for each lane
     */
    @Override
    public void addValuesAcrossLane(SummaryStatistics[] ss) {
        if (ss.length != acrossLaneSize) {
            System.out.println("Sizes don't match");
        } else {
            for (int i = 0; i < channelNum; i++) {
                for (int j = 0; j < rankNum; j++) {
                    for (int k = 0; k < laneNum; k++) {
                        // only include valid values
                        if (getLaneByIndexes(i, j, k).isValid()) {
                            ss[k].addValue(getLaneByIndexes(i, j, k).getMargin());
                        }
                    }
                }
            }
        }
    }

    /**
     * Add another TestMargins object to current one and get a new TestMargins
     * object
     *
     * @param tm another TestMargins to add
     * @return a new TestMargins object representing the sum
     */
    @Override
    public TestMargins add(TestMargins tm) {
        //check if it is DDR margins
        if (!(tm instanceof TestMarginsDDR)) {
            System.out.println("Not TestMargin of DDR. Cannot add.");
            return null;
        }
        //check if total channel, rank, lane numbers match 
        TestMarginsDDR tmd = (TestMarginsDDR) tm;
        if (channelNum != tmd.getChannelNum() || rankNum != tmd.rankNum
                || laneNum != tmd.laneNum) {
            System.out.println("Incompatible margin size. Cannot add.");
            return null;
        }
        TestLane[][][] newLanes = new TestLane[channelNum][rankNum][laneNum];
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < rankNum; j++) {
                for (int k = 0; k < laneNum; k++) {
                    // add if both valid; double valid one if one invalid; mark invalid otherwise 
                    if (getLaneByIndexes(i, j, k).isValid()
                            && tmd.getLaneByIndexes(i, j, k).isValid()) {
                        newLanes[i][j][k] = new TestLane(
                                getLaneByIndexes(i, j, k).getMargin()
                                + tmd.getLaneByIndexes(i, j, k)
                                .getMargin());
                    } else if (!getLaneByIndexes(i, j, k).isValid()
                            && !tmd.getLaneByIndexes(i, j, k).isValid()) {
                        newLanes[i][j][k] = new TestLane(0, TestLane.INVALID);
                    } else if (!getLaneByIndexes(i, j, k).isValid()) {
                        newLanes[i][j][k] = new TestLane(2 * tmd
                                .getLaneByIndexes(i, j, k).getMargin());
                    } else {
                        newLanes[i][j][k] = new TestLane(
                                2 * getLaneByIndexes(i, j, k).getMargin());
                    }
                }
            }
        }
        TestMarginsDDR newMargins = new TestMarginsDDR(newLanes);
        return newMargins;
    }

    /**
     * Method to mark whole lane as outlier by lane index.
     *
     * @param laneIndex "by lane" index
     */
    @Override
    public void markOutlierByLane(int laneIndex) {
        int lane = laneIndex % this.laneNum;
        laneIndex = laneIndex / this.laneNum;
        int rank = laneIndex % this.rankNum;
        laneIndex = laneIndex / this.rankNum;
        int channel = laneIndex % this.channelNum;
        getLaneByIndexes(channel, rank, lane).setState(TestLane.INVALID);
    }

    /**
     * Method to check whether all lanes are invalid.
     *
     * @return true if all invalid, false otherwise
     */
    @Override
    public boolean checkValidity() {
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < rankNum; j++) {
                for (int k = 0; k < laneNum; k++) {
                    if (getLaneByIndexes(i, j, k).getMargin() != 0) {
                        //one valid
                        return false;
                    }
                }
            }
        }
        //all invalid
        return true;
    }

    /**
     * Get certain lane by channel, rank, lane indexed
     *
     * @param channel channel index
     * @param rank rank index
     * @param lane lane index
     * @return a TestLane object
     */
    public TestLane getLaneByIndexes(int channel, int rank, int lane) {
        return lanes[channel][rank][lane];
    }

    /**
     * Get total number of channels
     *
     * @return total number of channels
     */
    public int getChannelNum() {
        return channelNum;
    }

    /**
     * Get total number of ranks
     *
     * @return total number of ranks
     */
    public int getRankNum() {
        return rankNum;
    }

    /**
     * Get total number of lanes
     *
     * @return total number of lanes
     */
    public int getLaneNum() {
        return laneNum;
    }

    /**
     * Method to get lane id by lane index
     *
     * @param index "by lane" index
     * @return channelID+rankID+laneID
     */
    @Override
    public String getLaneIDByIndex(int index) {
        int lane = index % this.laneNum;
        index = index / this.laneNum;
        int rank = index % this.rankNum;
        index = index / this.rankNum;
        int channel = index % this.channelNum;
        TestLane tempLane = getLaneByIndexes(channel, rank, lane);
        return tempLane.getLaneAttribute(TestLane.CHANNELID) + tempLane.getLaneAttribute(TestLane.RANKID) + tempLane.getLaneAttribute(TestLane.LANEID);
    }

    /**
     * Get a string representation of all margins within a repeat.
     *
     * @return information of all margins within a repeat
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TestLane tempLane;
        String temp;
        for (int i = 0; i < channelNum; i++) {
            for (int j = 0; j < rankNum; j++) {
                temp = getLaneByIndexes(i, j, 0).getLaneAttribute(TestLane.CHANNELID) + getLaneByIndexes(i, j, 0).getLaneAttribute(TestLane.RANKID) + "\n";
                sb.append(temp);
                for (int k = 0; k < laneNum; k++) {
                    tempLane = getLaneByIndexes(i, j, k);
                    temp = tempLane.getMargin() + "-" + getLaneByIndexes(i, j, k).isValid() + " ";
                    sb.append(temp);
                }
                sb.append("\n");
            }
        }
        sb.append("repeat basic stats: \n");
        sb.append(acrossLanesStats.toString());
        sb.append("repeat no outlier stats: \n");
        sb.append(acrossLanesNoOutlierStats.toString());
        return sb.toString();
    }
}
