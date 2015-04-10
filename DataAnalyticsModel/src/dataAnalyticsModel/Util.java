package dataAnalyticsModel;

public class Util {
	public static double getArrayMean(double[][] input){
		double total = 0;
		double number = input.length * input[0].length;
		for (int i = 0; i < input.length; i++){
			for (int j = 0; j < input[0].length; j++){
				total = total + input[i][j];
			}
		}
		return total/number;
	}
	
	public static double getArrayMin(double[][] input){
		double min = Double.MIN_VALUE;
		for (int i = 0; i < input.length; i++){
			for (int j = 0; j < input[0].length; j++){
				min = min < input[i][j] ? min : input[i][j];
			}
		}
		return min;
	}
	
	public static double getArrayMax(double[] input) {
		double max = Double.MAX_VALUE;
		for (int i = 0; i < input.length; i++) {
			max = max < input[i] ? input[i] : max;
		}
		return max;
	}
	
	public static double getArrayMean(double[] input) {
		double total = 0;
		double number = input.length;
		for (int i = 0; i < input.length; i++) {
			total = total + input[i];
		}
		return total/number;
	}
}


