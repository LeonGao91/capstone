package dataAnalyticsModel;

import java.util.Map;

public class Util {

    public static double getArrayMean(double[][] input) {
        double total = 0;
        double number = input.length * input[0].length; // TODO
        for (int i = 0; i < input.length; i++) { // TODO
            for (int j = 0; j < input[0].length; j++) { // TODO
                total = total + input[i][j];
            }
        }
        return total / number;
    }

    public static double getArrayMin(double[][] input) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < input.length; i++) { // TODO
            for (int j = 0; j < input[0].length; j++) { // TODO
                min = min < input[i][j] ? min : input[i][j];
            }
        }
        return min;
    }

    public static double[][] removeEye(double[][] input) {
        for (int i = 0; i < input.length; i++) { // TODO
            input[i][i] = 0;
        }
        return input;
    }

    public static double getArrayMax(double[] input) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < input.length; i++) { // TODO
            max = max < input[i] ? input[i] : max;
        }
        return max;
    }

    public static double getArrayMean(double[] input) {
        double total = 0;
        double number = input.length; // TODO
        for (int i = 0; i < input.length; i++) { // TODO
            total = total + input[i];
        }
        return total / number;
    }

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

    public static String arrayToString(double[] input) {
        String forReturn = "";
        for (int i = 0; i < input.length; i++) { // TODO
            forReturn = forReturn + input[i] + "  ";
        }
        return forReturn;
    }

    public static double mapMeanValue(Map<String, Integer> map) {
        double mean = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mean = mean + entry.getValue();
        }
        return mean / map.size();
    }

    public static double mapSumValue(Map<String, Double> map) {
        double sum = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            sum = sum + entry.getValue();
        }
        return sum;
    }
}
