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
	private TestMargins margins; //All lane margins
	private Stats basicStats; // basic statistics
	private Stats noOutlierStats; // statistics after removing outliers
	private int byLaneNumber;
	private int acrossLaneNumber;
	private String repeatID;


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
		basicStats = margins.getAcrossLanesStats();
		noOutlierStats = margins.getAcrossNoOutlierLaneStats();
		byLaneNumber = margins.getByLaneSize();
		acrossLaneNumber = margins.getAcrossLaneSize();
		repeatID = "";
	}
	
	/**
	 * Constructor with two basic arguments.
	 * This constructor should be the most often used.
	 *
	 * @param lanes
	 *            an array of TestLane representing all lanes of one repeat.
	 */
	public TestRepeat(TestMargins margins, String repeatID){
		this.margins = margins;
		basicStats = margins.getAcrossLanesStats();
		noOutlierStats = margins.getAcrossNoOutlierLaneStats();
		byLaneNumber = margins.getByLaneSize();
		acrossLaneNumber = margins.getAcrossLaneSize();
		this.repeatID = repeatID;
		//System.out.println(repeatID);
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
	
	public boolean findSystemOutliers(double benchmark, double threshold, StringBuilder messages){
		return margins.checkSystemOutliers(benchmark, threshold, messages);
	}
	
	public boolean findLaneOutliers(double meanThreshold, double medianThreshold, StringBuilder messages){
		return margins.checkLaneOutliers(meanThreshold, medianThreshold, messages);
	}
	
	public void markOutlierByLane(int laneIndex){
		margins.markOutlierByLane(laneIndex);
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
	
	public boolean checkValidity(){
		return margins.checkValidity();
	}

	public String getRepeatID() {
		return repeatID;
	}

	public void setRepeatID(String repeatID) {
		this.repeatID = repeatID;
	}
	
	public String getLaneIDByIndex(int index){
		return margins.getLaneIDByIndex(index);
	}

}
