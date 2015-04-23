package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents a all lanes within one repeat. The specific structure
 * depends on the test platform and should extend this abstruct class.
 * 
 * @author Yan 04/09/2015
 * @version 1.0
 *
 */

public abstract class TestMargins {
	protected Stats acrossLanesStats; // across lane statistics
	protected Stats acrossLanesNoOutlierStats; // statistics after removing outliers
	protected int acrossLaneSize;
	protected int byLaneSize;

	/**
	 * Constructor with no argument.
	 */
	public TestMargins() {
		acrossLanesStats = new Stats();
	}
	
	

	/**
	 * Get across lane statistics.
	 * 
	 * @return a Stats object
	 */
	public Stats getAcrossLanesStats() {
		return acrossLanesStats;
	}
	
	/**
	 * Get across lane statistics.
	 * 
	 * @return a Stats object
	 */
	public Stats getAcrossNoOutlierLaneStats() {
		return acrossLanesNoOutlierStats;
	}

	/**
	 * Get number of lanes for across lane computation
	 * 
	 * @return across lane size
	 */
	public int getAcrossLaneSize() {
		return acrossLaneSize;
	}

	/**
	 * Get number of lanes for by lane computation
	 * 
	 * @return by lane size
	 */
	public int getByLaneSize() {
		return byLaneSize;
	}

	/**
	 * Method to compute across lane statistics
	 */
	public abstract void findAcrossLaneStats(boolean checkOutlier);
	
	public abstract String checkSystemOutliers(double benchmark, double threshold);
	
	public abstract String checkLaneOutliers(double meanThreshold, double medianThreshold);

	/**
	 * Method to add margins to a DescriptiveStatistics object by lane. The
	 * definition of "by lane" is determined by the test type.
	 * 
	 * @param ds
	 *            a DescriptiveStatistics object to compute statistics
	 */
	public abstract void addValuesByLane(SummaryStatistics[] ss);

//	/**
//	 * Method to add certain lane margins to a SummaryStatistics object.
//	 * 
//	 * @param ss
//	 *            a SummaryStatistics to compute statistics
//	 * @param index
//	 *            the index of the lane to be added
//	 */
//	public abstract void addValuesbyLaneIndex(SummaryStatistics ss, int index);

	/**
	 * Method to add lane margins to corresponding SummaryStatistics object.
	 * 
	 * @param ss
	 *            a SummaryStatistics array to compute statistics for each lane
	 */
	public abstract void addValuesByLane2Lane(SummaryStatistics[] ss);

	/**
	 * Method to add another TestMargins object to current one and get a new
	 * TestMargins object.
	 * 
	 * @param tm
	 *            another TestMargins to add
	 * @return a new TestMargins object represending the addition result
	 */
	public abstract TestMargins add(TestMargins tm);
	
	
	public abstract void markOutlierByLane(int laneIndex);
	
	public abstract boolean checkValidity();
	
	public abstract String getLaneIDByIndex(int index);

}
