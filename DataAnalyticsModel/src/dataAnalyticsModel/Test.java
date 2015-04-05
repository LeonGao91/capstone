package dataAnalyticsModel;

/**
 * This class represents a validation test composing directions, systems,
 * repeats and lanes. Methods include detecting outliers, worst cases and
 * computing their statistics.
 * 
 * 
 * @author Yan 04/04/2015
 * @version 1.0
 *
 */
public class Test {
	public static boolean CHECKOUTLIER = true;
	public static boolean NOTCHECKOUTLIER = false;
	private double threshold1 = 6; // for detecting system outlier
	private double threshold2 = 6; // for detecting system outlier
	private double threshold3 = 6; // for detecting lane2lane outlier
	private double threshold4 = 6; // for detecting lane2lane outlier
	private TestDirection[] directions;
	private int size; // number of directions
	private int outlierCount; // number of outliers

	/**
	 * Constructor with no argument.
	 */
	public Test() {
	}

	/**
	 * Constructor with one int array argument.
	 *
	 * @param dataInput
	 *            a three-dimensional array storing all data of one direction.
	 */

	public Test(int[][][][] dataInput) {
		size = dataInput.length;
		directions = new TestDirection[size];
		//Construct each direction
		for (int i = 0; i < size; i++) {
			directions[i] = new TestDirection(dataInput[i]);
		}
		outlierCount = 0;
		findOutlier();
	}

	/**
	 * Find outliers based on basic statistics.
	 */
	private void findOutlier() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < directions[i].getSize(); j++) {
				for (int k = 0; k < directions[i].getSystemByIndex(j).getSize(); k++) {
					// System outlier 1
					if (Math.abs(getDirectionByIndex(i).getBasicStats().getMean() - getRepeatByIndexes(i, j, k).getBasicStats().getMin()) > threshold1) {
						getRepeatByIndexes(i, j, k).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					// System outlier 2
					if (Math.abs(getSystemByIndexes(i, j).getBasicStats().getMean() - getRepeatByIndexes(i, j, k).getBasicStats().getMin()) > threshold2) {
						getRepeatByIndexes(i, j, k).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					// Lane2lane outlier 1
					if (Math.abs(getRepeatByIndexes(i, j, k).getBasicStats().getMedian() - getRepeatByIndexes(i, j, k).getBasicStats().getMin()) > threshold3) {
						getRepeatByIndexes(i, j, k).setStatus(TestRepeat.INVALID);
						// Message
						outlierCount++;
					}
					// Lane2lane outlier 2?

				}
				// Compute statistics withour outliers
				getSystemByIndexes(i, j).findStats(CHECKOUTLIER); 
			}
			// Compute statistics withour outliers
			getDirectionByIndex(i).findStats(CHECKOUTLIER); 
		}
		// Output outlier message
		if (outlierCount > 1) {
			// Message
		}

	}

	/**
	 * Get certain repeat according to indexes
	 * 
	 * @param directionIndex
	 *            direction index
	 * @param systemIndex
	 *            system index
	 * @param repeatIndex
	 *            repeat index
	 * @return TestRepeat object
	 */
	private TestRepeat getRepeatByIndexes(int directionIndex, int systemIndex,
			int repeatIndex) {
		return directions[directionIndex].getSystemByIndex(systemIndex).getRepeatByIndex(
				repeatIndex);
	}

	/**
	 * Get certain system according to indexes
	 * 
	 * @param directionIndex
	 *            direction index
	 * @param systemIndex
	 *            system index
	 * @return TestSystem object
	 */
	private TestSystem getSystemByIndexes(int directionIndex, int systemIndex) {
		return directions[directionIndex].getSystemByIndex(systemIndex);
	}

	/**
	 * Get certain direction according to index
	 * 
	 * @param index
	 *            direction index
	 * @return TestDirection object
	 */
	public TestDirection getDirectionByIndex(int index) {
		return directions[index];
	}

	/**
	 * Get threshold1 value
	 * 
	 * @return threshold1 value
	 */
	public double getThreshold1() {
		return threshold1;
	}

	/**
	 * Set threshold1 value
	 * 
	 * @param threshold1
	 *            new threshold1 value
	 */
	public void setThreshold1(double threshold1) {
		this.threshold1 = threshold1;
	}

	/**
	 * Get threshold2 value
	 * 
	 * @return threshold2 value
	 */
	public double getThreshold2() {
		return threshold2;
	}

	/**
	 * Set threshold2 value
	 * 
	 * @param threshold2
	 *            new threshold1 value
	 */
	public void setThreshold2(double threshold2) {
		this.threshold2 = threshold2;
	}

	/**
	 * Get threshold3 value
	 * 
	 * @return threshold3 value
	 */
	public double getThreshold3() {
		return threshold3;
	}

	/**
	 * Set threshold3 value
	 * 
	 * @param threshold3
	 *            new threshold1 value
	 */
	public void setThreshold3(double threshold3) {
		this.threshold3 = threshold3;
	}

	/**
	 * Get threshold4 value
	 * 
	 * @return threshold4 value
	 */
	public double getThreshold4() {
		return threshold4;
	}

	/**
	 * Set threshold4 value
	 * 
	 * @param threshold4
	 *            new threshold1 value
	 */
	public void setThreshold4(double threshold4) {
		this.threshold4 = threshold4;
	}

	/**
	 * Get number of directions of the test
	 * 
	 * @return number of directions
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Get the number of outliers of the test
	 * 
	 * @return number of outliers
	 */
	public int getOutlierCount() {
		return outlierCount;
	}

	
}
