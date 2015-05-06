package dataAnalyticsModel;

import java.util.HashMap;
import java.util.Map;

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
    
    private HashMap<String, Double> statistics;

    public Stats() {
        statistics = new HashMap<>();
    }
    
    
    /**
     * Get mean value
     *
     * @return stored mean value
     */
    public double getMean() {
        return statistics.get("mean");
    }

    /**
     * Set mean value
     *
     * @param mean computed mean value
     */
    public void setMean(double mean) {
        statistics.put("mean", mean);
    }

    /**
     * Get min value
     *
     * @return stored min value
     */
    public double getMin() {
        return statistics.get("min");
    }

    /**
     * Set min value
     *
     * @param min computed min value
     */
    public void setMin(double min) {
        statistics.put("min", min);
    }

    /**
     * Get max value
     *
     * @return stored max value
     */
    public double getMax() {
        return statistics.get("max");
    }

    /**
     * Set max value
     *
     * @param max computed max value
     */
    public void setMax(double max) {
        statistics.put("max", max);
    }

    /**
     * Get median value
     *
     * @return median stored value
     */
    public double getMedian() {
        return statistics.get("median");
    }

    /**
     * Set median value
     *
     * @param median computed median value
     */
    public void setMedian(double median) {
        statistics.put("median",median);
    }

    /**
     * Get standard deviation
     *
     * @return stored standard deviation
     */
    public double getSigma() {
        return statistics.get("sigma");
    }

    /**
     * Set standard deviation
     *
     * @param sigma computed standard deviation
     */
    public void setSigma(double sigma) {
       statistics.put("sigma", sigma);
    }

    /**
     * Get meanMin Value
     *
     * @return stored meanMin value
     */
    public double getMeanMin() {
        return statistics.get("meanMin");
    }

    /**
     * Set meanMin value
     *
     * @param meanMin computed meanMin value
     */
    public void setMeanMin(double meanMin) {
        statistics.put("meanMin", meanMin);
    }

    /**
     * Get sigmaMin Value
     *
     * @return stored sigmaMin value
     */
    public double getSigmaMin() {
        return statistics.get("sigmaMin");
    }

    /**
     * Set sigmaMin value
     *
     * @param sigmaMin computed sigmaMin value
     */
    public void setSigmaMin(double sigmaMin) {
        statistics.put("sigmaMin", sigmaMin);
    }

    /**
     * Get sigmaMean Value
     *
     * @return stored sigmaMean value
     */
    public double getSigmaMean() {
        return statistics.get("sigmaMean");
    }

    /**
     * Set sigmaMean value
     *
     * @param sigmaMean computed sigmaMean value
     */
    public void setSigmaMean(double sigmaMean) {
        statistics.put("sigmaMean", sigmaMean);
    }

    /**
     * Get repeatNoise1 Value
     *
     * @return stored repeatNoise1 value
     */
    public double getRepeatNoise1() {
        return statistics.get("repeatNoise1");
    }

    /**
     * Set repeatNoise1 value
     *
     * @param repeatNoise1 computed repeatNoise1 value
     */
    public void setRepeatNoise1(double repeatNoise1) {
        statistics.put("repeatNoise1", repeatNoise1);
    }

    /**
     * Get repeatNoise2 Value
     *
     * @return stored repeatNoise2 value
     */
    public double getRepeatNoise2() {
        return statistics.get("repeatNoise2");
    }

    /**
     * Set repeatNoise2 value
     *
     * @param repeatNoise2 computed repeatNoise2 value
     */
    public void setRepeatNoise2(double repeatNoise2) {
        statistics.put("repeatNoise2", repeatNoise2);
    }

    /**
     * Get p2pNoise1 Value
     *
     * @return stored p2pNoise1 value
     */
    public double getP2pNoise1() {
        return statistics.get("p2pNoise1");
    }

    /**
     * Set p2pNoise1 value
     *
     * @param p2pNoise1 computed p2pNoise1 value
     */
    public void setP2pNoise1(double p2pNoise1) {
        statistics.put("p2pNoise1", p2pNoise1);
    }

    /**
     * Get p2pNoise2 Value
     *
     * @return stored p2pNoise2 value
     */
    public double getP2pNoise2() {
        return statistics.get("p2pNoise2");
    }

    /**
     * Set p2pNoise2 value
     *
     * @param p2pNoise2 computed p2pNoise2 value
     */
    public void setP2pNoise2(double p2pNoise2) {
        statistics.put("p2pNoise2", p2pNoise2);
    }

    /**
     * Get minMean Value
     *
     * @return stored minMean value
     */
    public double getMinMean() {
        return statistics.get("minMean");
    }

    /**
     * Set minMean value
     *
     * @param minMean computed minMean value
     */
    public void setMinMean(double minMean) {
        statistics.put("minMean", minMean);
    }

    /**
     * Get a string representation of all statistics data.
     *
     * @return all statistics data
     */
    @Override
    public String toString() {
        StringBuilder forReturn = new StringBuilder();
        String temp;
        for (Map.Entry<String, Double> entry : statistics.entrySet()) {
            temp = entry.getKey() + ": " + entry.getValue() + "\n";
            forReturn.append(temp);
        }
        return forReturn.toString();
    }

}
