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


	private TestDirection[] directions;
	private TestDirection[] pairedDirections;
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
		pairDirections();
	}

	
	/**
	 * Constructor with one Testdirection array argument.
	 *
	 * @param directions
	 *            an array TestDirection representing all directions of one test.
	 */
	public Test(TestDirection[] directions) {
		size = directions.length;
		this.directions = directions;
		outlierCount = 0;
		pairDirections();
	}
	
	private void pairDirections(){
		for (int i = 0; i < size/2; i++){
			pairedDirections[i*2] = directions[i].add(directions[i*2+1]);
		}
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
