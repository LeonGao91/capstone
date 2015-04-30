package dataAnalyticsModel;

/**
 * This class represents all statistics might be used for different test
 * dimensions (direction, system or repeat). Use this class to get or set
 * statistics.
 *
 * Fields: mean, min, max, median sigma: standard deviation meanMin: mean of min
 * values sigmaMin: standard deviation of min values sigmaMean: standard
 * deviation of mean values repeatNoise1: mean of standard deviations of min
 * values repeatNoise2: mean of standard deviations of mean values
 * p2pNoise1:standard deviation of means of min values p2pNoise2: standard
 * deviation of means of mean values minMean: min of mean values
 *
 * @author Yan 04/05/2015
 * @version 1.0
 */
public class Stats {

    private double mean;
    private double min;
    private double max;
    private double median;
    private double sigma; // standard deviation
    private double meanMin; // mean of min values
    private double sigmaMin; // standard deviation of min values
    private double sigmaMean; // standard deviation of mean values
    private double repeatNoise1; // mean of standard deviations of min values
    private double repeatNoise2; // mean of standard deviations of mean values
    private double p2pNoise1; // standard deviation of means of min values
    private double p2pNoise2; // standard deviation of means of mean values
    private double minMean; // min of mean values

    /**
     * Get mean value
     *
     * @return stored mean value
     */
    public double getMean() {
        return mean;
    }

    /**
     * Set mean value
     *
     * @param mean computed mean value
     */
    public void setMean(double mean) {
        this.mean = mean;
    }

    /**
     * Get min value
     *
     * @return stored min value
     */
    public double getMin() {
        return min;
    }

    /**
     * Set min value
     *
     * @param min computed min value
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Get max value
     *
     * @return stored max value
     */
    public double getMax() {
        return max;
    }

    /**
     * Set max value
     *
     * @param max computed max value
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * Get median value
     *
     * @return median stored value
     */
    public double getMedian() {
        return median;
    }

    /**
     * Set median value
     *
     * @param median computed median value
     */
    public void setMedian(double median) {
        this.median = median;
    }

    /**
     * Get standard deviation
     *
     * @return stored standard deviation
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set standard deviation
     *
     * @param sigma computed standard deviation
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Get meanMin Value
     *
     * @return stored meanMin value
     */
    public double getMeanMin() {
        return meanMin;
    }

    /**
     * Set meanMin value
     *
     * @param meanMin computed meanMin value
     */
    public void setMeanMin(double meanMin) {
        this.meanMin = meanMin;
    }

    /**
     * Get sigmaMin Value
     *
     * @return stored sigmaMin value
     */
    public double getSigmaMin() {
        return sigmaMin;
    }

    /**
     * Set sigmaMin value
     *
     * @param sigmaMin computed sigmaMin value
     */
    public void setSigmaMin(double sigmaMin) {
        this.sigmaMin = sigmaMin;
    }

    /**
     * Get sigmaMean Value
     *
     * @return stored sigmaMean value
     */
    public double getSigmaMean() {
        return sigmaMean;
    }

    /**
     * Set sigmaMean value
     *
     * @param sigmaMean computed sigmaMean value
     */
    public void setSigmaMean(double sigmaMean) {
        this.sigmaMean = sigmaMean;
    }

    /**
     * Get repeatNoise1 Value
     *
     * @return stored repeatNoise1 value
     */
    public double getRepeatNoise1() {
        return repeatNoise1;
    }

    /**
     * Set repeatNoise1 value
     *
     * @param repeatNoise1 computed repeatNoise1 value
     */
    public void setRepeatNoise1(double repeatNoise1) {
        this.repeatNoise1 = repeatNoise1;
    }

    /**
     * Get repeatNoise2 Value
     *
     * @return stored repeatNoise2 value
     */
    public double getRepeatNoise2() {
        return repeatNoise2;
    }

    /**
     * Set repeatNoise2 value
     *
     * @param repeatNoise2 computed repeatNoise2 value
     */
    public void setRepeatNoise2(double repeatNoise2) {
        this.repeatNoise2 = repeatNoise2;
    }

    /**
     * Get p2pNoise1 Value
     *
     * @return stored p2pNoise1 value
     */
    public double getP2pNoise1() {
        return p2pNoise1;
    }

    /**
     * Set p2pNoise1 value
     *
     * @param p2pNoise1 computed p2pNoise1 value
     */
    public void setP2pNoise1(double p2pNoise1) {
        this.p2pNoise1 = p2pNoise1;
    }

    /**
     * Get p2pNoise2 Value
     *
     * @return stored p2pNoise2 value
     */
    public double getP2pNoise2() {
        return p2pNoise2;
    }

    /**
     * Set p2pNoise2 value
     *
     * @param p2pNoise2 computed p2pNoise2 value
     */
    public void setP2pNoise2(double p2pNoise2) {
        this.p2pNoise2 = p2pNoise2;
    }

    /**
     * Get minMean Value
     *
     * @return stored minMean value
     */
    public double getMinMean() {
        return minMean;
    }

    /**
     * Set minMean value
     *
     * @param minMean computed minMean value
     */
    public void setMinMean(double minMean) {
        this.minMean = minMean;
    }

    public String toString() {
        String forReturn = "";
        if (mean != 0) {
            forReturn = forReturn + "mean: " + mean + "\n";
        }
        if (min != 0) {
            forReturn = forReturn + "min: " + min + "\n";
        }
        if (median != 0) {
            forReturn = forReturn + "median: " + median + "\n";
        }
        if (sigma != 0) {
            forReturn = forReturn + "sigma: " + sigma + "\n";
        }
        if (meanMin != 0) {
            forReturn = forReturn + "meanMin: " + meanMin + "\n";
        }
        if (minMean != 0) {
            forReturn = forReturn + "minMean: " + minMean + "\n";
        }
        if (sigmaMin != 0) {
            forReturn = forReturn + "sigmaMin: " + sigmaMin + "\n";
        }
        if (sigmaMean != 0) {
            forReturn = forReturn + "sigmaMean: " + sigmaMean + "\n";
        }
        if (repeatNoise1 != 0) {
            forReturn = forReturn + "repeatNoise1: " + repeatNoise1 + "\n";
        }
        if (repeatNoise2 != 0) {
            forReturn = forReturn + "repeatNoise2: " + repeatNoise2 + "\n";
        }
        if (p2pNoise1 != 0) {
            forReturn = forReturn + "p2pNoise1: " + p2pNoise1 + "\n";
        }
        if (p2pNoise2 != 0) {
            forReturn = forReturn + "p2pNoise2: " + p2pNoise2 + "\n";
        }
        return forReturn;
    }

}
