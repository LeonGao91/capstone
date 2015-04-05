package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class Direction {
    private System[] systems; // 5 systems
    private double repeatNoise1; // mean of all std of all min
    private double repeatNoise2; // mean of all std of all mean
    private double p2pNoise1; // std of all mean of all min
    private double p2pNoise2; // std of all mean of all mean
    private int minMin; // min of all min across lanes
    private double minMean; // min of all mean across lanes
    private double meanMin; // mean of all min across lanes
    private double meanMean; // mean of all mean across lanes
    private double sigmaMin; // std of all min across lanes
    private double sigmaMean; // std of all mean across lanes
    private int size; // number of systems

    /**
     * Constructor with no argument.
     */
    public Direction(){
    }
    
    /**
     * Constructor with one int array argument.
     *
     * @param dataInput a three-dimensional array storing all data of one
     * direction.
     */
    public Direction(int[][][] dataInput) {
        size = dataInput.length;
        systems = new System[size];
        for (int i = 0; i < size; i++) {
            systems[i] = new System(dataInput[i]);
        }
        findStats();
    }
    
    /**
     * Calculate stats of one direction.
     */
    private void findStats(){
        SummaryStatistics ssSigmaMin = new SummaryStatistics();
        SummaryStatistics ssSigmaMean = new SummaryStatistics();
        SummaryStatistics ssMeanMin = new SummaryStatistics();
        SummaryStatistics ssMeanMean = new SummaryStatistics();
        SummaryStatistics ssAllMin = new SummaryStatistics();
        SummaryStatistics ssAllMean = new SummaryStatistics();
        
        for (int i = 0; i < systems.length; i++) {
        		ssSigmaMin.addValue(systems[i].getSigmaMin());
        		ssSigmaMean.addValue(systems[i].getSigmaMean());
        		ssMeanMin.addValue(systems[i].getMeanMin());
        		ssMeanMean.addValue(systems[i].getMeanMean());
        		for (int j = 0; j<systems[i].getSize(); j++){
        			ssAllMin.addValue(systems[i].getRepeat(j).getMin());
        			ssAllMean.addValue(systems[i].getRepeat(j).getMean());
        		}
        }
        repeatNoise1=ssSigmaMin.getMean();
        repeatNoise2 = ssSigmaMean.getMean();
        p2pNoise1 = ssMeanMin.getStandardDeviation();
        p2pNoise2 = ssMeanMean.getStandardDeviation();
        minMin = (int) ssAllMin.getMin();
        meanMean = ssAllMean.getMean();
        minMean = ssAllMean.getMin();
        meanMin = ssAllMin.getMean();
        sigmaMin = ssAllMin.getStandardDeviation();
        sigmaMean = ssAllMean.getStandardDeviation();
    }

	public System[] getSystems() {
		return systems;
	}

	public double getRepeatNoise1() {
		return repeatNoise1;
	}

	public double getRepeatNoise2() {
		return repeatNoise2;
	}

	public double getP2pNoise1() {
		return p2pNoise1;
	}

	public double getP2pNoise2() {
		return p2pNoise2;
	}

	public int getMinMin() {
		return minMin;
	}

	public double getMinMean() {
		return minMean;
	}

	public double getMeanMin() {
		return meanMin;
	}

	public double getMeanMean() {
		return meanMean;
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
}
