package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a repeat of a validation test which contain margin data
 * of all lanes of that repeat. Statistics across lanes are computed here.
 * 
 * @author Yan, Lucy 04/08/2015
 * @version 1.0
 *
 */
public class TestRepeat {
	public static boolean VALID = true; //this lane is not an outlier
	public static boolean INVALID = false; //this lane is an outlier
	//private TestLane[] lanes; // margins of all lanes within this repeat
	private TestMargins margins; //All lane margins
	private Stats basicStats; // basic statistics
	private Stats noOutlierStats; // statistics after removing outliers
	//private int size; // number of lanes of this repeat
	//private boolean status; // whether this repeat is an outlier
	private int byLaneNumber;
	private int acrossLaneNumber;


	/**
	 * Constructor with no argument.
	 */
	public TestRepeat() {
	}
	
	/**
	 * Constructor with one TestLane array argument.
	 *
	 * @param lanes
	 *            an array of TestLane representing all lanes of one repeat.
	 */
	public TestRepeat(TestMargins margins){
		this.margins = margins;
		//status = TestRepeat.VALID;
		basicStats = margins.getAcrossLanesStats();
		noOutlierStats = margins.getAcrossNoOutlierLaneStats();
		byLaneNumber = margins.getByLaneSize();
		acrossLaneNumber = margins.getAcrossLaneSize();
//		findStats();
	}

//	/**
//	 * Constructor with one int array argument.
//	 *
//	 * @param dataInput
//	 *            an array storing all data of one repeat.
//	 */
//	public TestRepeat(int[] dataInput) {
//		size = dataInput.length;
//		lanes = new TestLane[size];
//		for (int i=0; i< dataInput.length; i++){
//			lanes[i] = new TestLane(dataInput[i]);
//		}
//		status = TestRepeat.VALID;
//		basicStats = new Stats();
//		findStats();
//	}
	
//	/**
//	 * Constructor with one TestLane array argument.
//	 *
//	 * @param lanes
//	 *            an array of TestLane representing all lanes of one repeat.
//	 */
//	public TestRepeat(TestLane[] lanes){
//		size = lanes.length;
//		this.lanes = lanes;
//		status = TestRepeat.VALID;
//		basicStats = new Stats();
//		findStats();
//	}
	
//	/**
//	 * Constructor to clone a repeat.
//	 * 
//	 * @param repeat
//	 *            a TestRepeat object
//	 */
//	public TestRepeat(TestRepeat repeat) {
//		size = repeat.getSize();
//		this.lanes = new TestLane[size];
//		for (int i = 0; i < size; i++) {
//			lanes[i] = new TestLane(repeat.getLaneByIndex(i));
//		}
//		status = TestRepeat.VALID;
//		basicStats = new Stats();
//		findStats();
//	}
	
//	/**
//	 * Calculate stats within one repeat.
//	 */
//	private void findStats() {
//	}

//	/**
//	 * Get number of lanes of the test
//	 * 
//	 * @return number of lanes
//	 */
//	public int getSize() {
//		return size;
//	}

	/**
	 * Get basic stats
	 * 
	 * @return Stats object storing basic stats
	 */
	public Stats getBasicStats() {
		return basicStats;
	}
	
	/**
	 * Get stats after removing outliers
	 * 
	 * @return Stats object storing basic stats
	 */
	public Stats getNoOutlierStats() {
		return noOutlierStats;
	}

//	/**
//	 * Check whether this repeat is not an outlier
//	 * 
//	 * @return true if it is not an outlier
//	 */
//	public boolean isNotOutlier() {
//		return status;
//	}
	
	public int findSystemOutliers(double benchmark, double threshold){
		return margins.checkSystemOutliers(benchmark, threshold);
	}
	
	public int findLaneOutliers(double meanThreshold, double medianThreshold){
		return margins.checkLaneOutliers(meanThreshold, medianThreshold);
	}

	public int getByLaneNumber() {
		return byLaneNumber;
	}

	public int getAcrossLaneNumber() {
		return acrossLaneNumber;
	}
	
	public TestMargins getMargins() {
		return margins;
	}

	public void addValuesByLane(SummaryStatistics[] ss){
		margins.addValuesByLane(ss);
	}
	
	public void addValuesByLane2Lane(SummaryStatistics[] ss){
		margins.addValuesByLane2Lane(ss);
	}
	
	public TestRepeat add(TestRepeat tr){
		TestMargins newTestMargins = this.margins.add(tr.getMargins());
		return new TestRepeat(newTestMargins);
	}
	
	public void findStats(boolean checkOutlier){
		margins.findAcrossLaneStats(checkOutlier);
	}

	public String toString(){
		return margins.toString();
	}
//	/**
//	 * Set the outlier status of this repeat
//	 * 
//	 * @param status
//	 *            VALID if it is not outlier, INVALID if it is outlier
//	 */
//	public void setStatus(boolean status) {
//		this.status = status;
//	}
	
//	/**
//	 * Get certain lane within the repeat by index
//	 * 
//	 * @param index
//	 *            the index of the lane
//	 * @return a TestLane object corresponds to the index
//	 */
//	public TestLane getLaneByIndex(int index) {
//		return lanes[index];
//	}
	
//	/**
//	 * Get the lanes of this repeat
//	 * @return all the TestLane objects of this repeat
//	 */
//	public TestLane[] getLanes(){
//		return lanes;
//	}

}
