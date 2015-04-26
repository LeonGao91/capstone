package dataAnalyticsModel;

/**
 * This class represents an eye chart. It is used to construct and parse xml.
 * Data shown in the chart are test mean min and min, as well as Intel benchmark
 * mean min and min.
 * 
 * @author Yan 04/24/2015
 * @version 1.0
 *
 */
public class EyeChart {
	double[] min; // min margins
	double[] mean; // mean min margins
	double[] Intel_min; // Intel benchmark min margins
	double[] Intel_mean; // Intel benchmark mean min margins

	/**
	 * Constructor with no argument
	 */
	public EyeChart() {
		min = new double[4];
		mean = new double[4];
		Intel_min = new double[4];
		Intel_mean = new double[4];
	}

	/**
	 * Constructor with all arguments
	 * 
	 * @param chartName
	 *            name of the chart
	 * @param min
	 *            an double array of min margins
	 * @param mean
	 *            an double array of mean min margins
	 * @param intel_min
	 *            an double array of Intel benchmark min margins
	 * @param intel_mean
	 *            an double array of Intel benchmark mean min margins
	 */
	public EyeChart(String chartName, double[] min, double[] mean,
			double[] intel_min, double[] intel_mean) {
		this.min = min;
		this.mean = mean;
		Intel_min = intel_min;
		Intel_mean = intel_mean;
	}

	/**
	 * Set the min margins
	 * 
	 * @param min
	 *            an double array of min margins
	 */
	public void setMin(double[] min) {
		this.min = min;
	}

	/**
	 * Set the mean min margins
	 * 
	 * @param mean
	 *            an double array of mean min margins
	 */
	public void setMean(double[] mean) {
		this.mean = mean;
	}

	/**
	 * Set the Intel benchmark min margins
	 * 
	 * @param intel_min
	 *            an double array of Intel benchmark min margins
	 */
	public void setIntel_min(double[] intel_min) {
		Intel_min = intel_min;
	}

	/**
	 * Set the Intel benchmark mean min margins
	 * 
	 * @param intel_mean
	 *            an double array of Intel benchmark mean min margins
	 */
	public void setIntel_mean(double[] intel_mean) {
		Intel_mean = intel_mean;
	}

	/**
	 * Get the min margins
	 * 
	 * @return an double array of min margins
	 */
	public double[] getMin() {
		return min;
	}

	/**
	 * Get the mean min margins
	 * 
	 * @return an double array of mean min margins
	 */
	public double[] getMean() {
		return mean;
	}

	/**
	 * Get the Intel benchmark min margins
	 * 
	 * @return an double array of Intel benchmark min margins
	 */
	public double[] getIntel_min() {
		return Intel_min;
	}

	/**
	 * Get the Intel benchmark mean min margins
	 * 
	 * @return an double array of Intel benchmark mean min margins
	 */
	public double[] getIntel_mean() {
		return Intel_mean;
	}

}
