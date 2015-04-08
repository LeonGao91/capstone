package dataAnalyticsModel;

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
	private int NearWcCount;
	private int size; // number of systems
	private int outlierCount;
	private int[] outlierThresholds;
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
	}

	public void initializeThresholds(){
		outlierThresholds = new int[6];
		for (int i = 0; i < 6; i++){
			outlierThresholds[i] = 6;
		}
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
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < getSystemByIndex(1).getSize(); k++) {
					// System outlier 1
					if (Math.abs(basicStats.getMean() - getRepeatByIndexes(j, k).getBasicStats().getMin()) > outlierThresholds[1]) {
						getRepeatByIndexes(j, k).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					// System outlier 2
					if (Math.abs(getSystemByIndex(j).getBasicStats().getMean() - getRepeatByIndexes(j, k).getBasicStats().getMin()) > outlierThresholds[2]) {
						getRepeatByIndexes(j, k).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					// Lane2lane outlier 1
					if (Math.abs(getRepeatByIndexes(j, k).getBasicStats().getMedian() - getRepeatByIndexes(j, k).getBasicStats().getMin()) > outlierThresholds[3]) {
						getRepeatByIndexes(j, k).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					
					if (Math.abs(getRepeatByIndexes(j, k).getBasicStats().getMean() - getRepeatByIndexes(j, k).getBasicStats().getMin()) > outlierThresholds[4]) {
						getRepeatByIndexes(j, k).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					findMeanMedianEachLane();
					// ToDo: Lane2lane outlier 3&4?
					
				}
				// Compute statistics withour outliers
				getSystemByIndex(j).findStats(CHECKOUTLIER); 
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
					margin = getLaneByIndexes(i, j, k).getMargin();
					ds[k].addValue(margin);
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
		// TODO Auto-generated method stub
		return systems[systemIndex].getRepeatByIndex(repeatIndex).getLaneByIndex(laneIndex);
	}
	
	private void findWorstCase(){
		//ToDo
	}
	
	private void findWorstCaseStats(){
		//ToDo
	}
	
	public double[] getAllMarginMean(){
		//ToDo
		return null;
	}
	
	public double[][] getLane2LaneCorrceof(){
		//ToDo
		return null;
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
	public TestDirection add(TestDirection newDirection){
		int margin;
		for (int i = 0; i < size; i++){
			for (int j = 0; j < systems[1].getSize(); j++){
				for (int k = 0; k < getRepeatByIndexes(i,j).getSize(); k++){
					margin = newDirection.getLaneByIndexes(i, j, k).getMargin()+ this.getLaneByIndexes(i, j, k).getMargin();
					newDirection.getLaneByIndexes(i, j, k).setMargin(margin);
				}
			}
		}
		return newDirection;
	}
}
