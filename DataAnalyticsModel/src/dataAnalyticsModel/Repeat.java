package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class Repeat {
    private int[] margins; // 64 lanes * 2 ranks * 2 channels = 256 margin values
    private double mean; // mean of margins in one repeat
    private int min; // min of margins in one repeat
    private int max; // max of margins in one repeat
    //private int median;
    private double sigma; // standard deviation of margins in one repeat
    private int size; // number of values in margins

    /**
     * Constructor with no argument.
     */
    public Repeat() {
    }

    /**
     * Constructor with one int array argument.
     *
     * @param dataInput an array storing all data of one repeat.
     */
    public Repeat(int[] dataInput) {
        margins = dataInput;
        size = dataInput.length;
        findStats();
    }
    
    /**
     * Calculate stats within one repeat.
     */
    private void findStats(){
        SummaryStatistics ss = new SummaryStatistics();
        
        for (int i = 0; i < margins.length; i++){
        		if (margins[i] != -1) { // add only valid values
        	        ss.addValue(margins[i]);
        		}
        }
        mean = ss.getMean();
        min = (int) ss.getMin();
        max = (int) ss.getMax();
        sigma = ss.getStandardDeviation();
    }

    // get methods   
	public double getMean() {
		return mean;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public double getSigma() {
		return sigma;
	}

	public int getSize() {
		return size;
	}
}
