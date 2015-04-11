package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a system of the validation test. Statistics across
 * repeats are computed here.
 * 
 * @author Yan, Lucy 04/08/2015
 * @version 1.0
 *
 */
public class TestSystem {
	public static boolean CHECKOUTLIER = true;
	public static boolean NOTCHECKOUTLIER = false;
	private TestRepeat[] repeats; // all repeats of one system
	private Stats noOutlierStats;
	private Stats basicStats;
	private int size; // number of repeats

	/**
	 * Constructor with no argument.
	 */
	public TestSystem() {
	}

//	/**
//	 * Constructor with one int array argument.
//	 *
//	 * @param dataInput
//	 *            a two-dimensional array storing all data of one system.
//	 */
//	public TestSystem(int[][] dataInput) {
//		size = dataInput.length;
//		repeats = new TestRepeat[size];
//		basicStats = new Stats();
//		noOutlierStats = new Stats();
//		for (int i = 0; i < size; i++) {
//			repeats[i] = new TestRepeat(dataInput[i]);
//		}
//		findStats(TestDirection.NOTCHECKOUTLIER);
//	}
	
	/**
	 * Constructor with one TestRepeat array argument.
	 *
	 * @param repeats
	 *            an array of TestRepeat representing all repeats of one system.
	 */
	public TestSystem(TestRepeat[] repeats){
		size = repeats.length;
		this.repeats = repeats;
		basicStats = new Stats();
		noOutlierStats = new Stats();
		findStats(TestDirection.NOTCHECKOUTLIER);
	}
	
	/**
	 * Calculate statistics within this system.
	 * 
	 * @param checkOutlier
	 *            indicate whether to remove outliers
	 */
	public void findStats(boolean checkOutlier) {
		SummaryStatistics ssMin = new SummaryStatistics();
		SummaryStatistics ssMean = new SummaryStatistics();
		// Assign an output Stats object according to outlier indicator
		Stats outputStats = checkOutlier ? noOutlierStats : basicStats;
		Stats tempRepeatStats;

		for (int i = 0; i < repeats.length; i++) {
			// Ignore outliers or remove ourliers
			tempRepeatStats = checkOutlier ? repeats[i].getNoOutlierStats() : repeats[i].getBasicStats();
//			if (checkOutlier == TestDirection.NOTCHECKOUTLIER || (checkOutlier == TestDirection.CHECKOUTLIER && repeats[i].isNotOutlier())) {
				ssMin.addValue(tempRepeatStats.getMin());
				ssMean.addValue(tempRepeatStats.getMean());
//			}
			// Store computed stats
			outputStats.setSigmaMin(ssMin.getStandardDeviation());
			outputStats.setMeanMin(ssMin.getMean());
			outputStats.setSigmaMean(ssMean.getStandardDeviation());
			outputStats.setMean(ssMean.getMean());
			outputStats.setMin(ssMin.getMin());
		}
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
	 * @param index
	 *            repeat index
	 * @return TestRepeat object
	 */
	public TestRepeat getRepeatByIndex(int index) {
		return repeats[index];
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
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++){
			sb.append("repeat" + i + ": \n");
			sb.append(repeats[i].toString());
		}
		sb.append("system basic stats: \n");
		sb.append(basicStats.toString());
		sb.append("system no outlier stats: \n");
		sb.append(noOutlierStats.toString());
		return sb.toString();
	}

}
