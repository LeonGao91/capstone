package dataAnalyticsModel;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a direction of a validation test. Statistics across
 * system and repeat are computed here.
 * 
 * @author Yan, Lucy 04/08/2015
 * @version 1.0
 *
 */
public class TestDirection {
	public static boolean CHECKOUTLIER = true; //remove outlier
	public static boolean NOTCHECKOUTLIER = false; // not remove outlier
	private TestSystem[] systems; // test systems
	private Stats noOutlierStats; // statistics after removing outliers
	private Stats basicStats; // statistics before removing outliers
	private Stats wcStats; //statistics for worst case
	private int nearWcCount; //number of lanes close to worst case
	private int size; // number of systems
	private int outlierCount; //number of outliers in this direction
	private int minLaneNo; //index of minimum lane
	private double[] outlierThresholds; //thresholds for detecting outliers
	private double byLaneThreshold; //threshold for determining near worst case lane
	private double[] meanEachLane; //mean of each lane across systems and repeats
//	private double[] medianEachLane; //median of each lane across systems and repeats
	

	/**
	 * Constructor with no argument.
	 */
	public TestDirection() {
	}

//	/**
//	 * Constructor with one int array argument.
//	 *
//	 * @param dataInput
//	 *            a three-dimensional array storing all data of one direction.
//	 */
//	public TestDirection(int[][][] dataInput) {
//		size = dataInput.length;
//		systems = new TestSystem[size];
//		basicStats = new Stats();
//		noOutlierStats = new Stats();
//		// Construct each system
//		for (int i = 0; i < size; i++) {
//			systems[i] = new TestSystem(dataInput[i]);
//		}
//		initializeThresholds();
//		findStats(NOTCHECKOUTLIER);
//		findOutlier();
//		findStats(CHECKOUTLIER);
//		findWorstCase();
//	}
	
	/**
	 * Constructor with one TestSystem array argument.
	 *
	 * @param systems
	 *            an array TestSystem representing all systems of one direction.
	 */
	public TestDirection(TestSystem[] systems) {
		size = systems.length;
		this.systems = systems;
		basicStats = new Stats();
		noOutlierStats = new Stats();
		initializeThresholds();
		findStats(NOTCHECKOUTLIER);
		findOutlier();
		findStats(CHECKOUTLIER);
	}
	
//	/**
//	 * Constructor to clone a direction.
//	 * 
//	 * @param direction
//	 *            a TestDirection object
//	 */
//	public TestDirection(TestDirection direction){
//		size = direction.getSize();
//		this.systems = new TestSystem[size];
//		for (int i = 0; i < size; i++){
//			systems[i] = new TestSystem(direction.getSystemByIndex(i));
//		}
//		basicStats = new Stats();
//		noOutlierStats = new Stats();
//		initializeThresholds();
//		findStats(NOTCHECKOUTLIER);
//		findOutlier();
//		findStats(CHECKOUTLIER);
//		findWorstCase();
//	}

	/**
	 * Initial thresholds with default numbers.
	 */
	private void initializeThresholds() {
		outlierThresholds = new double[6];
		for (int i = 0; i < 6; i++) {
			outlierThresholds[i] = 6;
		}
		byLaneThreshold = 0.5;
	}

	/**
	 * Initial thresholds from external file.
	 * 
	 * @param filePath
	 *            the path to the thresholds configuration file
	 */
	public void initializeThresholds(String filePath) {
		// ToDo
	}
	
	/**
	 * Calculate statistics within this direction.
	 * 
	 * @param checkOutlier
	 *            indicate whether to remove outliers
	 */
	public void findStats(boolean checkOutlier) {
		SummaryStatistics ssSigmaMin = new SummaryStatistics();
		SummaryStatistics ssSigmaMean = new SummaryStatistics();
		SummaryStatistics ssMeanMin = new SummaryStatistics();
		SummaryStatistics ssMeanMean = new SummaryStatistics();
		SummaryStatistics ssAllMin = new SummaryStatistics();
		SummaryStatistics ssAllMean = new SummaryStatistics();
		// Assign an output Stats object according to outlier indicator
		Stats outputStats = checkOutlier ? noOutlierStats : basicStats;
		Stats tempSystemStats;
		Stats tempRepeatStats;

		for (int i = 0; i < systems.length; i++) {
			// Assign an input Stats object of each system according to outlier indicator
			 tempSystemStats= checkOutlier ? systems[i].getNoOutlierStats()
					: systems[i].getBasicStats();
			ssSigmaMin.addValue(tempSystemStats.getSigmaMin());
			ssSigmaMean.addValue(tempSystemStats.getSigmaMean());
			ssMeanMin.addValue(tempSystemStats.getMeanMin());
			ssMeanMean.addValue(tempSystemStats.getMean());
			for (int j = 0; j < systems[i].getSize(); j++) {
				tempRepeatStats= checkOutlier ? systems[i].getRepeatByIndex(j).getNoOutlierStats()
						: systems[i].getRepeatByIndex(j).getBasicStats();
				//if (checkOutlier == NOTCHECKOUTLIER || (checkOutlier == CHECKOUTLIER && systems[i].getRepeatByIndex(j).isNotOutlier())){
					ssAllMin.addValue(tempRepeatStats.getMin());
					ssAllMean.addValue(tempRepeatStats.getMean());
				//}
			}
			// Store computed stats
			outputStats.setRepeatNoise1(ssSigmaMin.getMean());
			outputStats.setP2pNoise2(ssSigmaMean.getMean());
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
	 */
	private void findOutlier() {
		TestRepeat tempRepeat;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < getSystemByIndex(0).getSize(); j++) {
				// System outlier 1
				tempRepeat = getRepeatByIndexes(i, j);
				outlierCount = outlierCount
						+ tempRepeat.findSystemOutliers(basicStats.getMean(),
								outlierThresholds[0]);
				// if (Math.abs(basicStats.getMean() - getRepeatByIndexes(i,
				// j).getBasicStats().getMin()) > outlierThresholds[0]) {
				// getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
				// // Message
				// outlierCount++;
				// }
				// System outlier 2
				outlierCount = outlierCount
						+ tempRepeat.findSystemOutliers(getSystemByIndex(i)
								.getBasicStats().getMean(),
								outlierThresholds[1]);
				// if (Math.abs(getSystemByIndex(i).getBasicStats().getMean() -
				// getRepeatByIndexes(i, j).getBasicStats().getMin()) >
				// outlierThresholds[1]) {
				// getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
				// // Message
				// outlierCount++;
				// }
				// Lane2lane outlier 1
				outlierCount = outlierCount
						+ tempRepeat.findLaneOutliers(outlierThresholds[2],
								outlierThresholds[4]);
				// if (Math.abs(getRepeatByIndexes(i,
				// j).getBasicStats().getMedian() - getRepeatByIndexes(i,
				// j).getBasicStats().getMin()) > outlierThresholds[2]) {
				// getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
				// // Message
				// outlierCount++;
				// }
				//
				// if (Math.abs(getRepeatByIndexes(i,
				// j).getBasicStats().getMean() - getRepeatByIndexes(i,
				// j).getBasicStats().getMin()) > outlierThresholds[3]) {
				// getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
				// // Message
				// outlierCount++;
				// }

			}
			// Compute statistics without outliers
			getSystemByIndex(i).findStats(CHECKOUTLIER);
		}
		// Lane2lane outlier 3&4?
		findByLaneMean();
		boolean meanOutlier = false;
		boolean medianOutlier = false;
		DescriptiveStatistics ds = new DescriptiveStatistics();
		for (int i = 0; i < meanEachLane.length; i++) {
			ds.addValue(meanEachLane[i]);
		}
		meanOutlier = (ds.getMean() - ds.getMin()) > outlierThresholds[4];
		medianOutlier = (ds.getPercentile(50) - ds.getMin()) > outlierThresholds[5];
		if (meanOutlier && medianOutlier) {
			outlierCount = outlierCount + 2;
		} else if (meanOutlier || medianOutlier) {
			outlierCount++;
			markOutlierByLane(minLaneNo);
		}
		// Compute statistics withour outliers
		for (int i = 0; i < size; i++){
			for (int j = 0; j < systems[0].getSize(); j++){
				getRepeatByIndexes(i, j).findStats(CHECKOUTLIER);
			}
			systems[i].findStats(CHECKOUTLIER);
		}
		findStats(CHECKOUTLIER);
		// Output outlier message
		if (outlierCount > 1) {
			// Message
		}
	}
	
	/**
	 * Compute mean and median across systems and repeats for each line.
	 */
	private void findByLaneMean(){
		int numberOfLanes = getRepeatByIndexes(0,0).getByLaneNumber();
		meanEachLane = new double[numberOfLanes];
//		medianEachLane = new double[numberOfLanes];
		TestRepeat tempRepeat;
		//Used for get statistics
		SummaryStatistics[] ss = new SummaryStatistics[numberOfLanes];
		//Loop to add margins
		for (int i = 0; i < size; i++){
			ss[i] = new SummaryStatistics();
			for (int j = 0; j < systems[1].getSize(); j++){
				tempRepeat = getRepeatByIndexes(i, j);
				tempRepeat.addValuesByLane(ss);
			}
		}
		//Get statistics and store
		for (int i = 0; i < numberOfLanes; i++){
			meanEachLane[i] = ss[i].getMean();
//			medianEachLane[i] = ds[i].getPercentile(50);
		}
		findWorstCase(ss);
	}
	
	private void markOutlierByLane(int laneIndex){
		//ToDo
	}
	
	/**
	 * Get certain repeat according to indexes
	 * 
	 * @param systemIndex
	 *            system index
	 * @param repeatIndex
	 *            repeat index
	 * @return TestRepeat object
	 */
	private TestRepeat getRepeatByIndexes(int systemIndex, int repeatIndex) {
		return systems[systemIndex].getRepeatByIndex(repeatIndex);
	}
	
//	/**
//	 * Get certain lane according to indexes
//	 * 
//	 * @param systemIndex
//	 *            system index
//	 * @param repeatIndex
//	 *            repeat index
//	 * @param laneIndex
//	 *            lane index 
//	 * @return TestSystem object
//	 */
//	private TestLane getLaneByIndexes(int systemIndex, int repeatIndex, int laneIndex) {
//		return systems[systemIndex].getRepeatByIndex(repeatIndex).getLaneByIndex(laneIndex);
//	}
	
	/**
	 * Find the worst case lane and number of lanes close to the worst lane
	 */
	private void findWorstCase(SummaryStatistics[] ss){
		double worstCase = Integer.MAX_VALUE;
		minLaneNo = 0;
		//find worst case lane from by lane means
		for (int i = 0; i < meanEachLane.length; i++){
			worstCase = worstCase < meanEachLane[i] ? worstCase : meanEachLane[i];
			minLaneNo = i;
		}
		//count number of lanes that are close to worst lane 
		for (int i = 0; i < meanEachLane.length; i++){
			if (meanEachLane[i] - worstCase > byLaneThreshold){
				nearWcCount++;
			}
		}
		//get worst case lane statistics
		wcStats.setMean(ss[minLaneNo].getMean());
		wcStats.setMin(ss[minLaneNo].getMin());
	}
	
	/**
	 * Get mean values across lanes
	 * 
	 * @return a double array of all mean values across lanes
	 */
	public double[] getAllMarginMean() {
		double[] allMarginMean = new double[size * systems[1].getSize()];
		//Loop to get the mean across lanes of each repeat
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < systems[1].getSize(); j++) {
				allMarginMean[i] = getRepeatByIndexes(i, j).getBasicStats()
						.getMean();
			}
		}
		return allMarginMean;
	}
	
	/**
	 * Get lane2Lane correlation matrix.
	 * @return a two-dimensional array representing the correlation matrix
	 */
	private double[][] getLane2LaneCorrceof(){
		TestRepeat tempRepeat;
		int laneNum = getRepeatByIndexes(1,1).getAcrossLaneNumber();
		double[][] byLaneMeanAcrossRepeat = new double[size][laneNum];
		SummaryStatistics[][] acrossRepeat = new SummaryStatistics[size][laneNum];
		//add margin values
		for (int i = 0; i < size; i++){
			for (int j = 0; j < laneNum; j++){
				acrossRepeat[i][j] = new SummaryStatistics();
			}
			for (int j = 0; j < laneNum; j++){
				tempRepeat = getRepeatByIndexes(i, j);
				tempRepeat.addValuesByLane2Lane(acrossRepeat[i]);
			}
		}
		//get statistics
		for (int i = 0; i < size; i++){
			for (int j = 0; j < laneNum; j++){
				byLaneMeanAcrossRepeat[i][j] = acrossRepeat[i][j].getMean();
			}
		}
		//get correlation matrix
		PearsonsCorrelation pc = new PearsonsCorrelation(byLaneMeanAcrossRepeat);
		return pc.getCorrelationMatrix().getData();
	}
	
	public double getLane2LaneCorr(){
		double[][] correlationMatrix = getLane2LaneCorrceof();
		double[] maxCorrelation = new double[correlationMatrix.length];
		for (int i = 0; i < correlationMatrix.length; i++){
			maxCorrelation[i] = Util.getArrayMax(correlationMatrix[i]);
		}
		return Util.getArrayMean(maxCorrelation);
		
	}
	
	/**
	 * Add one direction to current direction and return a new TestDirection
	 * object
	 * 
	 * @param direction
	 *            another TestDirection to add
	 * @return a third TestDirection object of the sum of the two TestDirection
	 *         objects
	 */
	public TestDirection add(TestDirection direction){
		TestRepeat[] newRepeats;
		TestSystem[] newTestSystems;
		TestDirection newDirection;
		newTestSystems = new TestSystem[size];
		//Loop for adding margins of the two directions and constructing new objects
		for (int i = 0; i < size; i++){
			newRepeats = new TestRepeat[systems[1].getSize()];
			for (int j = 0; j < systems[1].getSize(); j++){
				newRepeats[j]= this.getRepeatByIndexes(i, j).add(direction.getRepeatByIndexes(i, j));
			}
			newTestSystems[i] = new TestSystem(newRepeats);
		}
		newDirection = new TestDirection(newTestSystems);
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
	 * @param index
	 *            system index
	 * @return TestSystem object
	 */
	public TestSystem getSystemByIndex(int index) {
		return systems[index];
	}
	
	/**
	 * Get all systems of the direction
	 * 
	 * @return a TestSystem array
	 */
	public TestSystem[] getSystems() {
		return systems;
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
	 * Get ourlier count
	 * 
	 * @return number of outliers in this direction
	 */
	public int getOutlierCount() {
		return outlierCount;
	}
}
