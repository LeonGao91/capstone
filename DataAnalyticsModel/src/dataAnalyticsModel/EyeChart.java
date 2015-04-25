package dataAnalyticsModel;

public class EyeChart {
	double[] min;
	double[] mean;
	double[] Intel_min;
	double[] Intel_mean;
	
	public EyeChart(){
		min = new double[4];
		mean = new double[4];
		Intel_min = new double[4];
		Intel_mean = new double[4];
	}
	
	public EyeChart(String chartName, double[] min, double[] mean,
			double[] intel_min, double[] intel_mean) {
		this.min = min;
		this.mean = mean;
		Intel_min = intel_min;
		Intel_mean = intel_mean;
	}


	public void setMin(double[] min) {
		this.min = min;
	}

	public void setMean(double[] mean) {
		this.mean = mean;
	}

	public void setIntel_min(double[] intel_min) {
		Intel_min = intel_min;
	}

	public void setIntel_mean(double[] intel_mean) {
		Intel_mean = intel_mean;
	}

	public double[] getMin() {
		return min;
	}

	public double[] getMean() {
		return mean;
	}
	
	
}
