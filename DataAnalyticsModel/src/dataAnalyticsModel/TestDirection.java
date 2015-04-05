package dataAnalyticsModel;

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
	private TestSystem[] systems; // test systems
	private Stats noOutlierStats; // statistics after removing outliers
	private Stats basicStats; // statistics before removing outliers
	private int size; // number of systems

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
		findStats(Test.NOTCHECKOUTLIER);
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
				if (checkOutlier == Test.NOTCHECKOUTLIER || (checkOutlier == Test.CHECKOUTLIER && systems[i].getRepeatByIndex(j).isNotOutlier())){
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
}
