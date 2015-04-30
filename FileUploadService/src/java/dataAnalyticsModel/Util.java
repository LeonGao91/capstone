package dataAnalyticsModel;

import java.util.Map;

/**
 * This class serves as an utility class which has methods to manipulate arrays
 * and maps.
 *
 * @author Yan 04/29/2015
 * @version 2.0
 */
public class Util {

    /**
     * Get mean value of a two-dimensional array.
     *
     * @param input two-dimensional double array
     * @return mean of all numbers
     */
    public static double getArrayMean(double[][] input) {
        double total = 0;
        double number = input.length * input[0].length;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                total = total + input[i][j];
            }
        }
        return total / number;
    }

    /**
     * Get min value of a two-dimensional array.
     *
     * @param input two-dimensional double array
     * @return min of all numbers
     */
    public static double getArrayMin(double[][] input) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < input.length; i++) { // TODO
            for (int j = 0; j < input[0].length; j++) { // TODO
                min = min < input[i][j] ? min : input[i][j];
            }
        }
        return min;
    }

    /**
     * Change matrix main diagonal to 0s.
     *
     * @param input two-dimensional double array
     * @return two-dimensional double array whose main diagonal is all 0s
     */
    public static double[][] removeEye(double[][] input) {
        for (int i = 0; i < input.length; i++) { // TODO
            input[i][i] = 0;
        }
        return input;
    }

    /**
     * Get max value of an array.
     *
     * @param input a double array
     * @return max of all numbers
     */
    public static double getArrayMax(double[] input) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < input.length; i++) { // TODO
            max = max < input[i] ? input[i] : max;
        }
        return max;
    }

    /**
     * Get mean value of an array.
     *
     * @param input a double array.
     * @return mean of all numbers
     */
    public static double getArrayMean(double[] input) {
        double total = 0;
        double number = input.length; // TODO
        for (int i = 0; i < input.length; i++) { // TODO
            total = total + input[i];
        }
        return total / number;
    }

    /**
     * Get a string presentation of a two-dimensional array
     *
     * @param input two-dimensional double array
     * @return a string presentation of the array
     */
    public static String arrayToString(double[][] input) {
        String forReturn = "";
        for (int i = 0; i < input.length; i++) { // TODO
            for (int j = 0; j < input[0].length; j++) { // TODO
                forReturn = forReturn + input[i][j] + "  ";
            }
            forReturn = forReturn + "\n";
        }
        return forReturn;
    }

    /**
     * Get a string presentation of an array
     *
     * @param input a double array
     * @return a string presentation of the array
     */
    public static String arrayToString(double[] input) {
        String forReturn = "";
        for (int i = 0; i < input.length; i++) { // TODO
            forReturn = forReturn + input[i] + "  ";
        }
        return forReturn;
    }

    /**
     * Get mean of all values of a map
     *
     * @param map a map with String as key and Integer as value
     * @return mean of all values
     */
    public static double mapMeanValue(Map<String, Integer> map) {
        double mean = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mean = mean + entry.getValue();
        }
        return mean / map.size();
    }

    /**
     * Get sum of all values of a map
     *
     * @param map a map with String as key and Integer as value
     * @return sum of all values
     */
    public static double mapSumValue(Map<String, Double> map) {
        double sum = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            sum = sum + entry.getValue();
        }
        return sum;
    }
}
