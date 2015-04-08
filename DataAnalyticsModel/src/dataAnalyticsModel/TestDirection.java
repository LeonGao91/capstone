package dataAnalyticsModel;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a direction of a validation test. Statistics across
 * system and repeat are computed here.
 * 
 * @author Yan, Lucy 04/04/2015
 * @version 1.0
 *
 */
public class TestDirection {
	public static boolean CHECKOUTLIER = true;
	public static boolean NOTCHECKOUTLIER = false;
	private TestSystem[] systems; // test systems
	private Stats noOutlierStats; // statistics after removing outliers
	private Stats basicStats; // statistics before removing outliers
	private Stats wcStats;
	private int nearWcCount;
	private int size; // number of systems
	private int outlierCount;
	private int minLaneNo;
	private double[] outlierThresholds;
	private double byLaneThreshold;
	private double[] meanEachLane;
	private double[] medianEachLane;
	

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
		findStats(NOTCHECKOUTLIER);
		findOutlier();
		findStats(CHECKOUTLIER);
		findWorstCase();
	}
	
	public TestDirection(TestDirection direction){
		size = direction.getSize();
		this.systems = new TestSystem[size];
		for (int i = 0; i < size; i++){
			systems[i] = new TestSystem(direction.getSystemByIndex(i));
		}
		basicStats = new Stats();
		noOutlierStats = new Stats();
		findStats(NOTCHECKOUTLIER);
		findOutlier();
		findStats(CHECKOUTLIER);
		findWorstCase();
	}

	public void initializeThresholds(){
		outlierThresholds = new double[6];
		for (int i = 0; i < 6; i++){
			outlierThresholds[i] = 6;
		}
		byLaneThreshold = 0.5;
	}
	
	public void initializeThresholds(String filePath){
		//ToDo
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
			// Assign an input Stats object of each system according to outlier
			// indicator
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
				// Compute statistics withour outliers
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
		DescriptiveStatistics[] ds = new DescriptiveStatistics[numberOfLanes];
		for (int i = 0; i < numberOfLanes; i++){
			ds[i] = new DescriptiveStatistics();
		}
		int margin;
		for (int i = 0; i < size; i++){
			for (int j = 0; j < systems[1].getSize(); j++){
				for (int k = 0; k < numberOfLanes; k++){
					if (getLaneByIndexes(i, j, k).isValid()){
					margin = getLaneByIndexes(i, j, k).getMargin();
					ds[k].addValue(margin);
					}
				}
			}
		}
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
		// TODO Auto-generated method stub
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
	
	private void findWorstCase(){
		double worstCase = Integer.MAX_VALUE;
		minLaneNo = 0;
		for (int i = 0; i < meanEachLane.length; i++){
			worstCase = worstCase < meanEachLane[i] ? worstCase : meanEachLane[i];
			minLaneNo = i;
		}
		for (int i = 0; i < meanEachLane.length; i++){
			if (meanEachLane[i] - worstCase > byLaneThreshold){
				nearWcCount++;
			}
		}
		findWorstCaseStats();
		//ToDo
	}
	
	private void findWorstCaseStats(){
		SummaryStatistics wc = new SummaryStatistics();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < getSystemByIndex(1).getSize(); j++) {
				wc.addValue(getLaneByIndexes(i, j, minLaneNo).getMargin());
			}
			wcStats.setMean(wc.getMean());
			wcStats.setMin(wc.getMin());
		}
	}
	
	public double[] getAllMarginMean(){
		double[] allMarginMean = new double[size * systems[1].getSize()];
		for (int i = 0; i < size; i++){
			for (int j = 0; j < systems[1].getSize(); j++){
				allMarginMean[i] = getRepeatByIndexes(i,j).getBasicStats().getMean();
			}
		}
		return allMarginMean;
	}
	
	public double[][] getLane2LaneCorrceof(){
		double[][] byLaneMeanAcrossRepeat = new double[getRepeatByIndexes(1,1).getSize()][size];
		SummaryStatistics[][] acrossRepeat = new SummaryStatistics[getRepeatByIndexes(1,1).getSize()][size];
		for (int i = 0; i < getRepeatByIndexes(1,1).getSize(); i++){
			for (int j = 0; j < size; j++){
				acrossRepeat[i][j] = new SummaryStatistics();
			}
		}
		for (int i = 0; i < size; i++){
			for (int j = 0; j < systems[1].getSize(); j++){
				for (int k = 0; k < getRepeatByIndexes(1,1).getSize(); k++){
					if (getLaneByIndexes(i, j, k).isValid()){
						acrossRepeat[k][i].addValue(getLaneByIndexes(i, j, k).getMargin());
					}
				}
			}
		}
		for (int i = 0; i < getRepeatByIndexes(1,1).getSize(); i++){
			for (int j = 0; j < size; j++){
				byLaneMeanAcrossRepeat[i][j] = acrossRepeat[i][j].getMean();
			}
		}
		PearsonsCorrelation pc = new PearsonsCorrelation(byLaneMeanAcrossRepeat);
		return pc.getCorrelationMatrix().getData();
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
	
	//Wrong implementation, need change
	public TestDirection add(TestDirection direction){
		int margin;
		TestLane[] newLanes;
		TestRepeat[] newRepeats;
		TestSystem[] newTestSystems;
		TestDirection newDirection;
		newTestSystems = new TestSystem[size];
		for (int i = 0; i < size; i++){
			newRepeats = new TestRepeat[systems[1].getSize()];
			for (int j = 0; j < systems[1].getSize(); j++){
				newLanes = new TestLane[getRepeatByIndexes(i,j).getSize()];
				for (int k = 0; k < getRepeatByIndexes(i,j).getSize(); k++){
					margin = direction.getLaneByIndexes(i, j, k).getMargin()+ this.getLaneByIndexes(i, j, k).getMargin();
					newLanes[k] = new TestLane(margin);
					newLanes[k].setAttributs(getLaneByIndexes(i, j, k).getAttributes());
				}
				newRepeats[j] = new TestRepeat(newLanes);
			}
			newTestSystems[i] = new TestSystem(newRepeats);
		}
		newDirection = new TestDirection(newTestSystems);
		return newDirection;
	}

	public TestSystem[] getSystems() {
		return systems;
	}

	public Stats getWcStats() {
		return wcStats;
	}

	public int getNearWcCount() {
		return nearWcCount;
	}

	public int getOutlierCount() {
		return outlierCount;
	}
	
	
}
