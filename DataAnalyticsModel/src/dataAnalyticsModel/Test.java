package dataAnalyticsModel;

public class Test {
    private Direction[] directions;

    /**
     * Constructor with no argument.
     */
    public Test() {
    }

    /**
     * Constructor with one int array argument.
     *
     * @param dataInput a three-dimensional array storing all data of one
     * direction.
     */

    public Test(int[][][][] dataInput) {
        int size = dataInput.length;
        directions = new Direction[size];
        for (int i = 0; i < size; i++) {
            directions[i] = new Direction(dataInput[i]);
        }
    }
}
