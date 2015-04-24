package dataAnalyticsModel;

public class EyeChart {
	String chartName;
	double[] min;
	double[] mean;
	double[] Intel_min;
	double[] Intel_mean;
	
	public EyeChart(){
		
	}
	
	public EyeChart(String chartName, double[] min, double[] mean,
			double[] intel_min, double[] intel_mean) {
		this.chartName = chartName;
		this.min = min;
		this.mean = mean;
		Intel_min = intel_min;
		Intel_mean = intel_mean;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
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
}
