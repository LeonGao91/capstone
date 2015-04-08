package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * This class represents a repeat of a validation test which contain margin data
 * of all lanes of that repeat. Statistics accross lanes are computed here.
 * 
 * @author Yan, Lucy 04/04/2015
 * @version 1.0
 *
 */
public class TestRepeat {
	public static boolean VALID = true;
	public static boolean INVALID = false;
	private TestLane[] lanes; // margins of all lanes within this repeat
	private Stats basicStats; // basic statistics
	private int size; // number of lanes of this repeat
	private boolean status; // whether this repeat is an outlier

	/**
	 * Constructor with no argument.
	 */
	public TestRepeat() {
	}

	/**
	 * Constructor with one int array argument.
	 *
	 * @param dataInput
	 *            an array storing all data of one repeat.
	 */
	public TestRepeat(int[] dataInput) {
		size = dataInput.length;
		lanes = new TestLane[size];
		for (int i=0; i< dataInput.length; i++){
			lanes[i] = new TestLane(dataInput[i]);
		}
		status = TestRepeat.VALID;
		basicStats = new Stats();
		findStats();
	}
	
	/**
	 * Constructor with one TestLane array argument.
	 *
	 * @param lanes
	 *            an array of TestLane representing all lanes of one repeat.
	 */
	public TestRepeat(TestLane[] lanes){
		size = lanes.length;
		lanes = lanes;
		status = TestRepeat.VALID;
		basicStats = new Stats();
		findStats();
	}
	
	/**
	 * Calculate stats within one repeat.
	 */
	private void findStats() {
		DescriptiveStatistics ds = new DescriptiveStatistics();
		// Traverse each lane
		for (int i = 0; i < lanes.length; i++) {
			// Only include valid values (NaN values were converted as -1)
			if (lanes[i].isValid()) {
				ds.addValue(lanes[i].getMargin());
			}
		}
		// Store computed stats
		basicStats.setMean(ds.getMean());
		basicStats.setMin(ds.getMin());
		basicStats.setMax(ds.getMax());
		basicStats.setMedian(ds.getPercentile(50));
		basicStats.setSigma(ds.getStandardDeviation());
	}

	/**
	 * Get number of lanes of the test
	 * 
	 * @return number of lanes
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Get basic stats
	 * 
	 * @return Stats object storing basic stats
	 */
	public Stats getBasicStats() {
		return basicStats;
	}

	/**
	 * Check whether this repeat is not an outlier
	 * 
	 * @return true if it is not an outlier
	 */
	public boolean isNotOutlier() {
		return status;
	}

	/**
	 * Set the outlier status of this repeat
	 * 
	 * @param status
	 *            VALID if it is not outlier, INVALID if it is outlier
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public TestLane getLaneByIndex(int index){
		return lanes[index];
	}
	
	public TestLane[] getLanes(){
		return lanes;
	}

}
