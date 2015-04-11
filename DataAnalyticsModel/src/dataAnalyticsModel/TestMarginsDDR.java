package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This class represents all lanes within one repeat for DDR platform. There are
 * three layers: channel, repeat, lane
 * 
 * @author Yan 04/09/2015
 * @version 1.0
 *
 */
public class TestMarginsDDR extends TestMargins {
	private TestLane[][][] lanes; // Margins organized by channel, rank and lane layers
	private int channelNum; // number of channels
	private int rankNum; // number of ranks
	private int laneNum; // number of lanes
	private double[][] acrossLaneMeans;
	private double[][] acrossLaneMins;
	private double[][] acrossLaneMedians;
	private double[][] acrossLaneNoOutlierMeans;
	private double[][] acrossLaneNoOutlierMins;
	private double[][] acrossLaneNoOutlierMedians;


	/**
	 * Constructor with no argument
	 */
	public TestMarginsDDR() {
		super();
	}

	/**
	 * Constructor with one three-dimensional array of TestLanes
	 * 
	 * @param lanes
	 *            a three-dimensional array of TestLanes representing all lanes
	 *            within one repeat for DDR platform.
	 */
	public TestMarginsDDR(TestLane[][][] lanes) {
		this.lanes = lanes;
		channelNum = lanes.length;
		rankNum = lanes[0].length;
		laneNum = lanes[0][0].length;
		acrossLaneMeans = new double[channelNum][rankNum];
		acrossLaneMins = new double[channelNum][rankNum];
		acrossLaneMedians = new double[channelNum][rankNum];
		acrossLaneNoOutlierMeans = new double[channelNum][rankNum];
		acrossLaneNoOutlierMins = new double[channelNum][rankNum];
		acrossLaneNoOutlierMedians = new double[channelNum][rankNum];
		//System.out.println("" + channelNum + " " + rankNum + " " + laneNum);
		acrossLaneSize = laneNum;
		byLaneSize = channelNum * rankNum * laneNum;
		acrossLanesStats = new Stats();
		acrossLanesNoOutlierStats = new Stats();
		findAcrossLaneStats(TestDirection.NOTCHECKOUTLIER);
	}

	/**
	 * Find basic statistics across lane.
	 */
	public void findAcrossLaneStats(boolean checkOutlier) {
		Stats outputStats = checkOutlier? acrossLanesNoOutlierStats: acrossLanesStats;
		double means[][] = checkOutlier? acrossLaneNoOutlierMeans: acrossLaneMeans;
		double mins[][] = checkOutlier? acrossLaneNoOutlierMins: acrossLaneMins;
		double medians[][] = checkOutlier? acrossLaneNoOutlierMedians: acrossLaneMedians;
		DescriptiveStatistics[][] ds = new DescriptiveStatistics[channelNum][rankNum];
		// Traverse each lane
		for (int i = 0; i < channelNum; i++) {
			for (int j = 0; j < rankNum; j++) {
				//System.out.println("" + i + " " + j);
				ds[i][j] = new DescriptiveStatistics();
				for (int k = 0; k < laneNum; k++) {
					//System.out.println("" + i + " " + j + " " + k);
					// Only include valid values
					if (lanes[i][j][k].isValid()) {
						ds[i][j].addValue(lanes[i][j][k].getMargin());
					}
				}
			}
		}
		//System.out.println("0 0 mean: " + ds[0][0].getMean());
		for (int i = 0; i < channelNum; i++) {
			for (int j = 0; j < rankNum; j++) {
				//System.out.println("" + i + " " + j);
				means[i][j] = ds[i][j].getMean();
				mins[i][j] = ds[i][j].getMin();
				medians[i][j] = ds[i][j].getPercentile(50);
			}
		}
		outputStats.setMean(Util.getArrayMean(acrossLaneMeans));
		outputStats.setMin(Util.getArrayMin(acrossLaneMins));
		// Store computed stats
//		acrossLaneStats.setMean(ds.getMean());
//		acrossLaneStats.setMin(ds.getMin());
//		acrossLaneStats.setMax(ds.getMax());
//		acrossLaneStats.setMedian(ds.getPercentile(50));
//		acrossLaneStats.setSigma(ds.getStandardDeviation());
	}
	
	
	public int checkSystemOutliers(double benchmark, double threshold){
		int outlierCount = 0;
		for (int i = 0; i < channelNum; i++) {
			for (int j = 0; j < rankNum; j++) {
				if (Math.abs(acrossLaneMeans[i][j] - benchmark) > threshold){
					outlierCount++;
					for (int k = 0; k < laneNum; k++){
						lanes[i][j][k].setState(TestLane.INVALID);
					}
				}
			}
		}
//		findAcrossLaneStats(TestDirection.CHECKOUTLIER);
		return outlierCount;
	}
	
	public int checkLaneOutliers(double meanThreshold, double medianThreshold){
		int outlierCount = 0;
		boolean meanOutlier = false;
		boolean medianOutlier = false;
		for (int i = 0; i < channelNum; i++) {
			for (int j = 0; j < rankNum; j++) {
				meanOutlier = (acrossLaneMeans[i][j] - acrossLaneMins[i][j]) > meanThreshold;
				medianOutlier = (acrossLaneMedians[i][j] - acrossLaneMins[i][j]) > medianThreshold;
				if ( meanOutlier || medianOutlier){
					for (int k = 0; k < laneNum; k++){
						lanes[i][j][k].setState(TestLane.INVALID);
					}
				}
				if (meanOutlier && medianOutlier){
					outlierCount = outlierCount + 2;
				}else if (meanOutlier || medianOutlier){
					outlierCount++;
				}
			}
		}
//		findAcrossLaneStats(TestDirection.CHECKOUTLIER);
		return outlierCount;
	}

	/**
	 * Add margins to a DescriptiveStatistics object by lane. 
	 * In the case of DDR, "by lane" is defined as channel.rank.lane
	 * 
	 * @param ds
	 *            a DescriptiveStatistics object to compute statistics
	 */
	public void addValuesByLane(SummaryStatistics[] ss) {
		if (ss.length != byLaneSize) {
			System.out.println("Doesn't match");
		} else {
			int index = 0;
			for (int i = 0; i < channelNum; i++) {
				for (int j = 0; j < rankNum; j++) {
					for (int k = 0; k < laneNum; k++) {
						// Only include valid values
						if (lanes[i][j][k].isValid()) {
							index = i * rankNum * laneNum + j * laneNum + k;
							//System.out.println("index: " + index);
							//System.out.println("i: " + i + " j: " + j + " k: " + k);
							ss[index].addValue(lanes[i][j][k].getMargin());
						}
					}
				}
			}
		}
	}

//	/**
//	 * Add certain lane margins to a SummaryStatistics object.
//	 * In the case of DDR, "lane" is defined as channel.rank.lane
//	 * 
//	 * @param ss
//	 *            a SummaryStatistics to compute statistics
//	 * @param index
//	 *            the index of the lane to be added
//	 */
//	public void addValuesbyLaneIndex(SummaryStatistics ss, int index) {
//		if (index < 0 || index > laneNum) {
//			System.out.println("Wrong index");
//		} else {
//			for (int i = 0; i < channelNum; i++) {
//				for (int j = 0; j < rankNum; j++) {
//					// Only include valid values
//					if (lanes[i][j][index].isValid()) {
//						ss.addValue(lanes[i][j][index].getMargin());
//					}
//				}
//			}
//		}
//	}

	/**
	 * Add lane margins to corresponding SummaryStatistics object of an array.
	 * 
	 * @param ss
	 *            a SummaryStatistics array to compute statistics for each lane
	 */
	public void addValuesByLane2Lane(SummaryStatistics[] ss) {
		if (ss.length != acrossLaneSize) {
			System.out.println("Wrong matrix size");
		} else {
			for (int i = 0; i < channelNum; i++) {
				for (int j = 0; j < rankNum; j++) {
					for (int k = 0; k < laneNum; k++) {
						// Only include valid values
						if (lanes[i][j][k].isValid()) {
							ss[k].addValue(lanes[i][j][k].getMargin());
						}
					}
				}
			}
		}
	}

	/**
	 * Add another TestMargins object to current one and get a new TestMargins object
	 * 
	 * @param tm
	 *            another TestMargins to add
	 * @return a new TestMargins object representing the sum
	 */
	public TestMargins add(TestMargins tm) {
		//Should check before casting
		TestMarginsDDR tmd = (TestMarginsDDR) tm;
		if (channelNum != tmd.getChannelNum() || rankNum != tmd.rankNum
				|| laneNum != tmd.laneNum) {
			System.out.println("Incompatible TestMargin");
		}
		TestLane[][][] newLanes = new TestLane[channelNum][rankNum][laneNum];
		for (int i = 0; i < channelNum; i++) {
			for (int j = 0; j < rankNum; j++) {
				for (int k = 0; k < laneNum; k++) {
					// Add if both valid; double valid one if one invalid; mark invalid otherwise 
					if (lanes[i][j][k].isValid()
							&& tmd.getLaneByIndexes(i, j, k).isValid()) {
						newLanes[i][j][k] = new TestLane(
								lanes[i][j][k].getMargin()
										+ tmd.getLaneByIndexes(i, j, k)
												.getMargin());
					} else if (!lanes[i][j][k].isValid()
							&& !tmd.getLaneByIndexes(i, j, k).isValid()) {
						newLanes[i][j][k] = new TestLane(0, TestLane.INVALID);
					} else if (!lanes[i][j][k].isValid()) {
						newLanes[i][j][k] = new TestLane(2 * tmd
								.getLaneByIndexes(i, j, k).getMargin());
					} else {
						newLanes[i][j][k] = new TestLane(
								2 * lanes[i][j][k].getMargin());
					}
				}
			}
		}
		TestMarginsDDR newMargins = new TestMarginsDDR(newLanes);
		return newMargins;
	}

	/**
	 * Get certain lane by channel, rank, lane indexed
	 * @param channel channel index
	 * @param rank rank index
	 * @param lane lane index
	 * @return a TestLane object
	 */
	public TestLane getLaneByIndexes(int channel, int rank, int lane) {
		return lanes[channel][rank][lane];
	}

	/**
	 * Get total number of channels
	 * @return total number of channels
	 */
	public int getChannelNum() {
		return channelNum;
	}

	/**
	 * Get total number of ranks
	 * @return total number of ranks
	 */
	public int getRankNum() {
		return rankNum;
	}
	
	/**
	 * Get total number of lanes
	 * @return total number of lanes
	 */
	public int getLaneNum() {
		return laneNum;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < channelNum; i++){
			for (int j = 0; j < rankNum; j++){
				sb.append("channel" + i + "rank" + j + ": ");
				for (int k = 0; k < laneNum; k++){
					sb.append(getLaneByIndexes(i, j, k).getMargin() + " ");
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}
