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
	private double[] medianEachLane; //median of each lane across systems and repeats
	

	/**
	 * Constructor with no argument.
	 */
	public TestDirection() {
	}

	/**
	 * Constructor with one int array argument.
	 *
	 * @param dataInput
	 *            a three-dimensional array storing all data of one direction.
	 */
	public TestDirection(int[][][] dataInput) {
		size = dataInput.length;
		systems = new TestSystem[size];
		basicStats = new Stats();
		noOutlierStats = new Stats();
		// Construct each system
		for (int i = 0; i < size; i++) {
			systems[i] = new TestSystem(dataInput[i]);
		}
		initializeThresholds();
		findStats(NOTCHECKOUTLIER);
		findOutlier();
		findStats(CHECKOUTLIER);
		findWorstCase();
	}
	
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
		findWorstCase();
	}
	
	/**
	 * Constructor to clone a direction.
	 * 
	 * @param direction
	 *            a TestDirection object
	 */
	public TestDirection(TestDirection direction){
		size = direction.getSize();
		this.systems = new TestSystem[size];
		for (int i = 0; i < size; i++){
			systems[i] = new TestSystem(direction.getSystemByIndex(i));
		}
		basicStats = new Stats();
		noOutlierStats = new Stats();
		initializeThresholds();
		findStats(NOTCHECKOUTLIER);
		findOutlier();
		findStats(CHECKOUTLIER);
		findWorstCase();
	}

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

		for (int i = 0; i < systems.length; i++) {
			// Assign an input Stats object of each system according to outlier indicator
			Stats tempStats = checkOutlier ? systems[i].getNoOutlierStats()
					: systems[i].getBasicStats();
			ssSigmaMin.addValue(tempStats.getSigmaMin());
			ssSigmaMean.addValue(tempStats.getSigmaMean());
			ssMeanMin.addValue(tempStats.getMeanMin());
			ssMeanMean.addValue(tempStats.getMean());
			for (int j = 0; j < systems[i].getSize(); j++) {
				//Ignore outliers or remove ourliers
				if (checkOutlier == NOTCHECKOUTLIER || (checkOutlier == CHECKOUTLIER && systems[i].getRepeatByIndex(j).isNotOutlier())){
					ssAllMin.addValue(systems[i].getRepeatByIndex(j).getBasicStats().getMin());
					ssAllMean.addValue(systems[i].getRepeatByIndex(j).getBasicStats().getMean());
				}
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
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < getSystemByIndex(1).getSize(); j++) {
					// System outlier 1
					if (Math.abs(basicStats.getMean() - getRepeatByIndexes(i, j).getBasicStats().getMin()) > outlierThresholds[1]) {
						getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					// System outlier 2
					if (Math.abs(getSystemByIndex(i).getBasicStats().getMean() - getRepeatByIndexes(i, j).getBasicStats().getMin()) > outlierThresholds[2]) {
						getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					// Lane2lane outlier 1
					if (Math.abs(getRepeatByIndexes(i, j).getBasicStats().getMedian() - getRepeatByIndexes(i, j).getBasicStats().getMin()) > outlierThresholds[3]) {
						getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					
					if (Math.abs(getRepeatByIndexes(i, j).getBasicStats().getMean() - getRepeatByIndexes(i, j).getBasicStats().getMin()) > outlierThresholds[4]) {
						getRepeatByIndexes(i, j).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					findMeanMedianEachLane();
					// ToDo: Lane2lane outlier 3&4?
					
				}
				// Compute statistics without outliers
				getSystemByIndex(i).findStats(CHECKOUTLIER); 
			}
			// Compute statistics withour outliers
			findStats(CHECKOUTLIER);
		// Output outlier message
		if (outlierCount > 1) 
		{
			// Message
		}

	}
	
	/**
	 * Compute mean and median across systems and repeats for each line.
	 */
	private void findMeanMedianEachLane(){
		int numberOfLanes = getRepeatByIndexes(1,1).getSize();
		meanEachLane = new double[numberOfLanes];
		medianEachLane = new double[numberOfLanes];
		//Used for get statistics
		DescriptiveStatistics[] ds = new DescriptiveStatistics[numberOfLanes];
		for (int i = 0; i < numberOfLanes; i++){
			ds[i] = new DescriptiveStatistics();
		}
		//Loop to add margins
		for (int i = 0; i < size; i++){
			for (int j = 0; j < systems[1].getSize(); j++){
				for (int k = 0; k < numberOfLanes; k++){
					if (getLaneByIndexes(i, j, k).isValid()){
					ds[k].addValue(getLaneByIndexes(i, j, k).getMargin());
					}
				}
			}
		}
		//Get statistics and store
		for (int i = 0; i < numberOfLanes; i++){
			meanEachLane[i] = ds[i].getMean();
			medianEachLane[i] = ds[i].getPercentile(50);
		}
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
	
	/**
	 * Get certain lane according to indexes
	 * 
	 * @param systemIndex
	 *            system index
	 * @param repeatIndex
	 *            repeat index
	 * @param laneIndex
	 *            lane index 
	 * @return TestSystem object
	 */
	private TestLane getLaneByIndexes(int systemIndex, int repeatIndex, int laneIndex) {
		return systems[systemIndex].getRepeatByIndex(repeatIndex).getLaneByIndex(laneIndex);
	}
	
	/**
	 * Find the worst case lane and number of lanes close to the worst lane
	 */
	private void findWorstCase(){
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
		findWorstCaseStats();
	}
	
	/**
	 * Find statistics for worst case lane
	 */
	private void findWorstCaseStats(){
		SummaryStatistics wc = new SummaryStatistics();
		//Add valid margins
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < getSystemByIndex(1).getSize(); j++) {
				wc.addValue(getLaneByIndexes(i, j, minLaneNo).getMargin());
			}
			//Store statistics to Stat object
			wcStats.setMean(wc.getMean());
			wcStats.setMin(wc.getMin());
		}
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
	public double[][] getLane2LaneCorrceof(){
		double[][] byLaneMeanAcrossRepeat = new double[getRepeatByIndexes(1,1).getSize()][size];
		//Compute across repeat statistics
		SummaryStatistics[][] acrossRepeat = new SummaryStatistics[getRepeatByIndexes(1,1).getSize()][size];
		for (int i = 0; i < getRepeatByIndexes(1,1).getSize(); i++){
			for (int j = 0; j < size; j++){
				acrossRepeat[i][j] = new SummaryStatistics();
			}
		}
		//add margin values
		for (int i = 0; i < size; i++){
			for (int j = 0; j < systems[1].getSize(); j++){
				for (int k = 0; k < getRepeatByIndexes(1,1).getSize(); k++){
					if (getLaneByIndexes(i, j, k).isValid()){
						acrossRepeat[k][i].addValue(getLaneByIndexes(i, j, k).getMargin());
					}
				}
			}
		}
		//get statistics
		for (int i = 0; i < getRepeatByIndexes(1,1).getSize(); i++){
			for (int j = 0; j < size; j++){
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
	 * @param direction
	 *            another TestDirection to add
	 * @return a third TestDirection object of the sum of the two TestDirection
	 *         objects
	 */
	public TestDirection add(TestDirection direction){
		int margin;
		TestLane[] newLanes;
		TestRepeat[] newRepeats;
		TestSystem[] newTestSystems;
		TestDirection newDirection;
		newTestSystems = new TestSystem[size];
		//Loop for adding margins of the two directions and constructing new objects
		for (int i = 0; i < size; i++){
			newRepeats = new TestRepeat[systems[1].getSize()];
			for (int j = 0; j < systems[1].getSize(); j++){
				newLanes = new TestLane[getRepeatByIndexes(i,j).getSize()];
				for (int k = 0; k < getRepeatByIndexes(i,j).getSize(); k++){
					margin = direction.getLaneByIndexes(i, j, k).getMargin()+ this.getLaneByIndexes(i, j, k).getMargin();
					newLanes[k] = new TestLane(margin);
					newLanes[k].setAttributs(getLaneByIndexes(i, j, k).getAttributes());
					newLanes[k].setState(direction.getLaneByIndexes(i, j, k).isValid() || this.getLaneByIndexes(i, j, k).isValid());
				}
				newRepeats[j] = new TestRepeat(newLanes);
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
