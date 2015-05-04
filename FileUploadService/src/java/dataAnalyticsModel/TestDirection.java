package dataAnalyticsModel;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a direction of a validation test. Statistics across
 * system and repeat are computed here.
 *
 * @author Yan, Lucy 04/29/2015
 * @version 2.0
 *
 */
public class TestDirection {

    private TestSystem[] systems; // test systems
    private Stats noOutlierStats; // statistics after removing outliers
    private Stats basicStats; // statistics before removing outliers
    private Stats wcStats; //statistics for worst case
    private int nearWcCount; //number of lanes close to worst case
    private int size; // number of systems
    private int minLaneNo; //index of minimum lane
    private double[] meanEachLane; //mean of each lane across systems and repeats
    private double minAllLane; //min of meanEachLane
    private double meanAllLane; //mean of meanEachLane
    private double medianAllLane; //median of meanEachLane
    private String directionID;
    private SummaryStatistics[] byLaneValues;

    /**
     * Constructor with no argument.
     */
    public TestDirection() {
        basicStats = new Stats();
        noOutlierStats = new Stats();
        wcStats = new Stats();
        nearWcCount = 0;
        directionID = "";
    }

    /**
     * Constructor with one TestSystem array argument.
     *
     * @param systems an array TestSystem representing all systems of one
     * direction.
     */
    public TestDirection(TestSystem[] systems) {
        size = systems.length;
        this.systems = systems;
        basicStats = new Stats();
        noOutlierStats = new Stats();
        wcStats = new Stats();
        nearWcCount = 0;
        directionID = "";
        findStats(Test.NOTCHECKOUTLIER);
        findByLaneMean();
    }

    /**
     * Constructor with one TestSystem array argument.
     *
     * @param systems an array TestSystem representing all systems of one
     * direction.
     * @param directionID direction ID
     */
    public TestDirection(TestSystem[] systems, String directionID) {
        size = systems.length;
        this.systems = systems;
        basicStats = new Stats();
        noOutlierStats = new Stats();
        wcStats = new Stats();
        nearWcCount = 0;
        this.directionID = directionID;
        findStats(Test.NOTCHECKOUTLIER);
        findByLaneMean();
        System.out.println("Loading" + directionID);
    }

    /**
     * Calculate statistics within this direction.
     *
     * @param checkOutlier indicate whether to remove outliers
     */
    public final void findStats(boolean checkOutlier) {
        //initialize SummaryStatistics objects to compute statistics
        SummaryStatistics ssSigmaMin = new SummaryStatistics();
        SummaryStatistics ssSigmaMean = new SummaryStatistics();
        SummaryStatistics ssMeanMin = new SummaryStatistics();
        SummaryStatistics ssMeanMean = new SummaryStatistics();
        SummaryStatistics ssAllMin = new SummaryStatistics();
        SummaryStatistics ssAllMean = new SummaryStatistics();
        int maxRepeat = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxRepeat = getSystemByIndex(i).getSize() > maxRepeat ? getSystemByIndex(i).getSize() : maxRepeat;
        }
        SummaryStatistics[] mins = new SummaryStatistics[maxRepeat];
        SummaryStatistics[] means = new SummaryStatistics[maxRepeat];
        //assign an output Stats object according to outlier indicator
        Stats outputStats = checkOutlier ? noOutlierStats : basicStats;
        Stats tempSystemStats;
        Stats tempRepeatStats;
        //get repeat noise
        for (int i = 0; i < maxRepeat; i++) {
            mins[i] = new SummaryStatistics();
            means[i] = new SummaryStatistics();
            for (int j = 0; j < size; j++) {
                if (getSystemByIndex(j).getSize() > i) {
                    tempRepeatStats = checkOutlier ? getRepeatByIndexes(j, i).getNoOutlierStats() : getRepeatByIndexes(j, i).getBasicStats();
                    if (Double.compare(tempRepeatStats.getMin(), Double.NaN) != 0) {
                        mins[i].addValue(tempRepeatStats.getMin());
                    }
                    if (Double.compare(tempRepeatStats.getMean(), Double.NaN) != 0) {
                        means[i].addValue(tempRepeatStats.getMean());
                    }
                }
            }
        }
        for (int i = 0; i < getSystemByIndex(0).getSize(); i++) {
            if (Double.compare(mins[i].getStandardDeviation(), Double.NaN) != 0) {
                ssSigmaMin.addValue(mins[i].getStandardDeviation());
            }
            if (Double.compare(means[i].getStandardDeviation(), Double.NaN) != 0) {
                ssSigmaMean.addValue(means[i].getStandardDeviation());
            }
        }
        //get other stats
        for (int i = 0; i < size; i++) {
            // assign an input Stats object of each system according to outlier indicator
            tempSystemStats = checkOutlier ? getSystemByIndex(i).getNoOutlierStats() : getSystemByIndex(i).getBasicStats();
            //exclude NA statistics from each system
            if (Double.compare(tempSystemStats.getMeanMin(), Double.NaN) != 0) {
                ssMeanMin.addValue(tempSystemStats.getMeanMin());
            }
            //exclude NA statistics from each system
            if (Double.compare(tempSystemStats.getMean(), Double.NaN) != 0) {
                ssMeanMean.addValue(tempSystemStats.getMean());
            }
            for (int j = 0; j < getSystemByIndex(i).getSize(); j++) {
                tempRepeatStats = checkOutlier ? getRepeatByIndexes(i, j).getNoOutlierStats() : getRepeatByIndexes(i, j).getBasicStats();
                //exclude NA statistics from each system
                if (Double.compare(tempRepeatStats.getMin(), Double.NaN) != 0) {
                    ssAllMin.addValue(tempRepeatStats.getMin());
                }
                //exclude NA statistics from each system
                if (Double.compare(tempRepeatStats.getMean(), Double.NaN) != 0) {
                    ssAllMean.addValue(tempRepeatStats.getMean());
                }
            }
            // store computed stats
            outputStats.setRepeatNoise1(ssSigmaMin.getMean());
            outputStats.setRepeatNoise2(ssSigmaMean.getMean());
            outputStats.setP2pNoise1(ssMeanMin.getStandardDeviation());
            outputStats.setP2pNoise2(ssMeanMean.getStandardDeviation());
            outputStats.setMin(ssAllMin.getMin());
            outputStats.setMean(ssAllMean.getMean());
            outputStats.setMinMean(ssAllMean.getMin());
            outputStats.setMeanMin(ssAllMin.getMean());
            outputStats.setSigmaMin(ssAllMin.getStandardDeviation());
            outputStats.setSigmaMean(ssAllMean.getStandardDeviation());
        }
    }

    /**
     * Find outliers based on basic statistics.
     *
     * @param thresholds an array of thresholds to determine outliers
     * @param outliers an array of boolean to indicate whether certain type of
     * outlier exists
     * @param messages outlier messages
     */
    public void findOutlier(double[] thresholds, boolean[] outliers,
            StringBuilder messages) {
        TestRepeat tempRepeat;
        TestSystem tempSystem;
        StringBuilder sb = new StringBuilder();
        StringBuilder tempMessages = new StringBuilder();
        String message;
        //traverse all repeats of all systems
        for (int i = 0; i < size; i++) {
            tempSystem = getSystemByIndex(i);
            for (int j = 0; j < tempSystem.getSize(); j++) {
                tempRepeat = getRepeatByIndexes(i, j);
                // threshold based outlier detection - 1
                outliers[1] = outliers[1] || tempRepeat.findSystemOutliers(basicStats.getMean(), thresholds[0], sb);
                // threshold based outlier detection - 2
                outliers[2] = outliers[2] || tempRepeat.findSystemOutliers(tempSystem.getBasicStats().getMean(), thresholds[1], sb);
                // lane2lane outlier - 1
                outliers[3] = outliers[3] || tempRepeat.findLaneOutliers(thresholds[2], thresholds[3], sb);
                if (sb.length() != 0) {
                    message = "#Lane outlier found in System \"" + tempSystem.getSystemID() + "\" Repeat \"" + tempRepeat.getRepeatID() + "\";";
                    tempMessages.append(message);
                    tempMessages.append(sb);
                    sb = new StringBuilder();
                }
            }
        }
        // lane2lane outlier - 2
        boolean meanOutlier;
        boolean medianOutlier;
        meanOutlier = (meanAllLane - minAllLane) > thresholds[4];
        medianOutlier = (medianAllLane - minAllLane) > thresholds[5];
        if (meanOutlier || medianOutlier) {
            outliers[4] = true;
            message = "#" + getRepeatByIndexes(0, 0).getLaneIDByIndex(minLaneNo)
                    + " Outlier in All Systems;";
            tempMessages.append(message);
            markOutlierByLane(minLaneNo);
        }
        if (tempMessages.length() != 0) {
            message = directionID + ":;";
            messages.append(message);
            messages.append(tempMessages);
        }
    }

    /**
     * Compute mean and median across systems and repeats for each line.
     */
    private void findByLaneMean() {
        int numberOfLanes = getRepeatByIndexes(0, 0).getByLaneSize();
        meanEachLane = new double[numberOfLanes];
        TestRepeat tempRepeat;
        //used for get statistics
        byLaneValues = new SummaryStatistics[numberOfLanes];
        //loop to add margins
        for (int i = 0; i < numberOfLanes; i++) {
            byLaneValues[i] = new SummaryStatistics();
        }
        //System.err.println(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < getSystemByIndex(i).getSize(); j++) {
                tempRepeat = getRepeatByIndexes(i, j);
                tempRepeat.addValuesByLane(byLaneValues);
            }
        }
        //get statistics and store
        for (int i = 0; i < numberOfLanes; i++) {
            meanEachLane[i] = byLaneValues[i].getMean();
        }
        //compute mean, min and median of by lane means
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (int i = 0; i < meanEachLane.length; i++) {
            ds.addValue(meanEachLane[i]);
        }
        minAllLane = ds.getMin();
        meanAllLane = ds.getMean();
        medianAllLane = ds.getPercentile(50);
    }

    /**
     * Find the worst case lane and number of lanes close to the worst lane.
     *
     * @param byLaneThreshold threshold to count near worst case lanes
     */
    public void findWorstCase(double byLaneThreshold) {
        double worstCase = Integer.MAX_VALUE;
        minLaneNo = 0;
        //find worst case lane from by lane means
        for (int i = 0; i < meanEachLane.length; i++) {
            if (worstCase > meanEachLane[i]) {
                worstCase = meanEachLane[i];
                minLaneNo = i;
            }
        }
        //count number of lanes that are close to worst lane 
        for (int i = 0; i < meanEachLane.length; i++) {
            if (meanEachLane[i] - worstCase < byLaneThreshold) {
                nearWcCount++;
            }
        }
        //get worst case lane statistics
        wcStats.setMean(byLaneValues[minLaneNo].getMean());
        wcStats.setMin(byLaneValues[minLaneNo].getMin());
    }

    /**
     * Mark certain lane in all systems and repeats as outlier.
     *
     * @param laneIndex "by lane" index
     */
    private void markOutlierByLane(int laneIndex) {
        TestRepeat tempRepeat;
        //traverse each repeat
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < getSystemByIndex(i).getSize(); j++) {
                tempRepeat = getRepeatByIndexes(i, j);
                tempRepeat.markOutlierByLane(laneIndex);
            }
        }
    }

    /**
     * Get certain repeat according to indexes
     *
     * @param systemIndex system index
     * @param repeatIndex repeat index
     * @return TestRepeat object
     */
    private TestRepeat getRepeatByIndexes(int systemIndex, int repeatIndex) {
        return getSystemByIndex(systemIndex).getRepeatByIndex(repeatIndex);
    }

    /**
     * Get mean values across lanes of each system's each repeat and form an
     * array. Used for computing correlations.
     *
     * @return a double array of all mean values across lanes
     */
    public double[] getAllMarginMean() {
        int maxRepeat = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxRepeat = getSystemByIndex(i).getSize() > maxRepeat ? getSystemByIndex(i).getSize() : maxRepeat;
        }
        double[] allMarginMean = new double[size * maxRepeat];
        //Loop to get the mean across lanes of each repeat
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < getSystemByIndex(i).getSize(); j++) {
                allMarginMean[i * maxRepeat + j] = getRepeatByIndexes(i, j).getBasicStats()
                        .getMean();
            }
        }
        return allMarginMean;
    }

    /**
     * Compute lane2lane correlation.
     *
     * @return lane2lane correlation
     */
    public double getLane2LaneCorr() {
        //remove eye for the correlation matrix
        double[][] correlationMatrix = Util.removeEye(getLane2LaneCorrceof());
        double[] maxCorrelation = new double[correlationMatrix.length];
        for (int i = 0; i < correlationMatrix.length; i++) {
            //get max value of each row
            maxCorrelation[i] = Util.getArrayMax(correlationMatrix[i]);
        }
        //get mean of max values
        return Util.getArrayMean(maxCorrelation);
    }

    /**
     * Get lane2Lane correlation matrix.
     *
     * @return a two-dimensional array representing the correlation matrix
     */
    private double[][] getLane2LaneCorrceof() {
        TestRepeat tempRepeat;
        int laneNum = getRepeatByIndexes(0, 0).getAcrossLaneSize();
        double[][] byLaneMeanAcrossRepeat = new double[size][laneNum];
        SummaryStatistics[][] acrossRepeat = new SummaryStatistics[size][laneNum];
        //traverse each repeat
        for (int i = 0; i < size; i++) {
            //initialize SummaryStatistics objects
            for (int j = 0; j < laneNum; j++) {
                acrossRepeat[i][j] = new SummaryStatistics();
            }
            //add margin values
            for (int j = 0; j < getSystemByIndex(i).getSize(); j++) {
                tempRepeat = getRepeatByIndexes(i, j);
                tempRepeat.addValuesAcrossLane(acrossRepeat[i]);
            }
        }
        //get statistics
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < laneNum; j++) {
                byLaneMeanAcrossRepeat[i][j] = acrossRepeat[i][j].getMean();
            }
        }
        //get correlation matrix
        PearsonsCorrelation pc = new PearsonsCorrelation(byLaneMeanAcrossRepeat);
        return pc.getCorrelationMatrix().getData();
    }

    /**
     * Add one direction to current direction and return a new TestDirection
     * object
     *
     * @param direction another TestDirection to add
     * @return a new TestDirection object of the sum of the two TestDirection
     * objects
     */
    public TestDirection add(TestDirection direction) {
        TestRepeat[] newRepeats;
        TestSystem[] newTestSystems;
        TestDirection newDirection;
        newTestSystems = new TestSystem[size];
        //Loop for adding margins of the two directions and constructing new objects
        for (int i = 0; i < size; i++) {
            newRepeats = new TestRepeat[getSystemByIndex(i).getSize()];
            for (int j = 0; j < getSystemByIndex(i).getSize(); j++) {
                newRepeats[j] = this.getRepeatByIndexes(i, j).add(direction.getRepeatByIndexes(i, j));
            }
            newTestSystems[i] = new TestSystem(newRepeats);
        }
        String newID = this.directionID + "&" + direction.getDirectionID();
        newDirection = new TestDirection(newTestSystems, newID);
        return newDirection;
    }

    /**
     * Get stats after removing outliers
     *
     * @return Stats object storing stats after removing outliers
     */
    public Stats getNoOutlierStats() {
        return noOutlierStats;
    }

    /**
     * Get stats before removing outliers
     *
     * @return Stats object storing stats before removing outliers
     */
    public Stats getBasicStats() {
        return basicStats;
    }

    /**
     * Get worst case statistics
     *
     * @return a worst case Stat object
     */
    public Stats getWcStats() {
        return wcStats;
    }

    /**
     * Get number of systems of the test
     *
     * @return number of systems
     */
    public int getSize() {
        return size;
    }

    /**
     * Get certain system by system index
     *
     * @param index system index
     * @return TestSystem object
     */
    public TestSystem getSystemByIndex(int index) {
        return systems[index];
    }

    /**
     * Get near worst case lane count
     *
     * @return number of lanes close to worst case lane
     */
    public int getNearWcCount() {
        return nearWcCount;
    }

    /**
     * Get direction ID.
     *
     * @return direction ID
     */
    public String getDirectionID() {
        return directionID;
    }

    /**
     * Set direction ID.
     *
     * @param directionID direction ID
     */
    public void setDirectionID(String directionID) {
        this.directionID = directionID;
    }

    /**
     * Get all lane min value.
     *
     * @return all lane min value
     */
    public double getMinAllLane() {
        return minAllLane;
    }

    /**
     * Get all lane mean value.
     *
     * @return all lane mean value
     */
    public double getMeanAllLane() {
        return meanAllLane;
    }

    /**
     * Get all lane median value.
     *
     * @return all lane median value
     */
    public double getMedianAllLane() {
        return medianAllLane;
    }

    /**
     * Get the "by lane" index of min lane.
     *
     * @return min lane index
     */
    public int getMinLaneNo() {
        return minLaneNo;
    }

    /**
     * Get a string representation of a direction.
     *
     * @return information of a direction including statistics
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String message;
        for (int i = 0; i < size; i++) {
            message = getSystemByIndex(i).getSystemID() + " \n";
            sb.append(message);
            sb.append(getSystemByIndex(i).toString());
        }
        sb.append("direction basic stats: \n");
        sb.append(basicStats.toString());
        sb.append("direction no outlier stats: \n");
        sb.append(noOutlierStats.toString());
        sb.append("direction worst case stats: \n");
        sb.append(wcStats.toString());
        return sb.toString();
    }
}
