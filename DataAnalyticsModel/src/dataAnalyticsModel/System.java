package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class System {
    private Repeat[] repeats; // 5 repeats
    private double meanMin; // mean of 5 min values generated from 5 repeats
    private double meanMean; // mean of 5 mean values generated from 5 repeats
    private int min; // min of 5 min values generated from 5 repeats
    private double mean; // same as meanMean?
    //private int median;
    private double sigmaMin; // sd of 5 min values generated from 5 repeats
    private double sigmaMean; // sd of 5 mean values generated from 5 repeats
    private int size; // numer of repeats

    /**
     * Constructor with no argument.
     */
    public System() {
    }

    /**
     * Constructor with one int array argument.
     *
     * @param dataInput a two-dimensional array storing all data of one system.
     */
    public System(int[][] dataInput) {
        size = dataInput.length;
        repeats = new Repeat[size];
        for (int i = 0; i < size; i++) {
            repeats[i] = new Repeat(dataInput[i]);
        }
        findStats();
    }
    
    /**
     * Calculate stats of one system which contains 5 repeats.
     */
    private void findStats(){
        SummaryStatistics ssMin = new SummaryStatistics();
        SummaryStatistics ssMean = new SummaryStatistics();
        
        for (int i = 0; i < repeats.length; i++) {
        		ssMin.addValue(repeats[i].getMin());
        		ssMin.addValue(repeats[i].getMean());
        }
        
        sigmaMin = ssMin.getStandardDeviation();
        meanMin = ssMin.getMean();
        sigmaMean = ssMean.getStandardDeviation();
        meanMean = ssMean.getMean();
        min = (int) ssMin.getMin();
        mean = ssMean.getMean();
    }
    
    public double getMeanMin() {
		return meanMin;
	}
    
    public double getMeanMean() {
		return meanMean;
	}

	public int getMin() {
		return min;
	}

	public double getMean() {
		return mean;
	}

	public double getSigmaMin() {
		return sigmaMin;
	}
	
	public double getSigmaMean() {
		return sigmaMean;
	}

	public int getSize() {
		return size;
	}

	public Repeat getRepeat(int index) {
		return repeats[index];
	}
}
