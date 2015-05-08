package dataAnalyticsModel;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.xml.sax.SAXException;

/**
 * This class represents a validation test composing directions, systems,
 * repeats and lanes. Methods include detecting outliers, worst cases and
 * computing their statistics.
 *
 *
 * @author Lucy, Yan 04/29/2015
 * @version 2.0
 *
 */
public class Test {

    public static boolean CHECKOUTLIER = true; //remove outlier
    public static boolean NOTCHECKOUTLIER = false; // not remove outlier
    //health check items
    public static String MEANMINCHECK = "Mean of Min Check";
    public static String MINMINCHECK = "Min of Min Check";
    public static String HIGHMEANMINCHECK = "Mean of Min Check (High)";
    public static String HIGHMINMINCHECK = "Min of Min Check (High)";
    public static String SIGMAMEANCHECK = "Sigma of Mean Check";
    public static String SIGMAMINCHECK = "Sigma of Min Check";
    public static String HIGHMEANMEANCHECK = "Mean of Mean Check (High)";
    public static String HIGHMINMEANCHECK = "Min of Mean Check (High)";
    public static String OUTLIERHEALTH = "Outlier Count for Health";
    public static String WINDOWMEANCHECK = "Window Mean Check";
    public static String REPEATNOISECHECK = "Repeat Noise Check";
    public static String HIGHTOLOWCORRCHECKHEALTH = "High to Low Correlation Check for Health";
    public static String WINDOWCORRCHECKHEALTH = "Window Correlation Check for Health";
    public static String NEARWCCOUNT = "Near Worst Case Lane Count";
    public static String LANE2LANECORRCHECKHEALTH = "Lane2Lane Correlation Check for Health";
    //trust check items
    public static String SYSTEMCOUNT = "System Count"; // total system count
    public static String REPEATCOUNT = "Repeat Count"; // average system repeat count
    public static String MEANCHECK = "Mean Check";
    public static String MINCHECK = "Min Check";
    public static String SIGMAMEANCHECKTRUST = "Sigma of Mean Check";
    public static String SIGMAMINCHECKTRUST = "Sigma of Min Check";
    public static String TOTALSIGMACHECK = "Total Sigma Check";
    public static String OUTLIERTRUST = "Outlier Count for Trust";
    public static String HIGHTOLOWCORRCHECKTRUST = "High to Low Correlation Check for Trust";
    public static String WINDOWCORRCHECKTRUST = "Window Correlation Check for Trust";
    public static String LANE2LANECORRCHECKTRUST = "Lane2Lane Correlation Check for Trust";

    private double health; //health score
    private double trust; //trust score
    private String conclusion; //"pass" or "fail"
    private String customerID; //customer ID
    private String productID; //product ID
    private TestDirection[] directions; //all directions
    private TestDirection[] pairedDirections; //direction pairs
    private int size; // number of directions
    private double outlierCount; // number of outliers
    private LinkedHashMap<String, Double> healthDetail; //details of computing health score
    private LinkedHashMap<String, Double> trustDetail; //detals of computing trust score
    private Map<String, Integer> systems_repeats; //systems and number of repeats
    //thresholds
    private double[] thresholds;
    private double[] highThresholds;
    private double[] repeatNoiseThresholds;
    private double sigmaThreshold;
    private double sigmaThreshold2;
    private double corrThreshold;
    private double lane2LaneCorrThresholds;
    private double[] outlierThresholds;
    private double[] pairedOutlierThresholds;
    private double byLaneThreshold;
    //benchmarks
    private double[] eyeChartIntelMinBenchmark;
    private double[] eyeChartIntelMeanBenchmark;
    private double passHealthBenchmark;
    private double passTrustBenchmark;
    private double failHealthBenchmark; 
    private double failTrustBenchmark;
    private int maxNearWcCount; 
    private int desiredRepeatCount; 
    //check results
    private boolean basicMeanCheck = true;
    private boolean outlierMeanCheck = true;
    private boolean basicMinCheck = true;
    private boolean outlierMinCheck = true;
    private boolean basicMeanCheckH = true; // high threshold
    private boolean basicMinCheckH = true; // high threshold
    private boolean basicSigmaMeanCheck = true;
    private boolean outlierSigmaMeanCheck = true;
    private boolean basicSigmaMinCheck = true;
    private boolean outlierSigmaMinCheck = true;
    private boolean outlierSigmaMinCheckT2 = true; // compare to sigmaThreshold2
    private boolean outlierMeanCheckH2 = true; // mean check 3
    private boolean outlierMeanCheckH3 = true; // mean check 4
    private boolean windowCheck = true;
    private boolean outlierWindowCheck = true;
    private boolean outlierNoiseCheck = true;
    private boolean udCorr = true; //up-down correlation
    private boolean winCorr = true; //window correlation
    private boolean lane2LaneCorr = true; //lane2lane correlation
    private PearsonsCorrelation pearsons;
    private StringBuilder messages;
    private boolean outliers[];
    private double nearWcCount;

    /**
     * Constructor with no argument.
     */
    public Test() {
    }

    /**
     * Constructor with one TestDirection array argument.
     *
     * @param directions an array TestDirection representing all directions of
     * one test.
     */
    public Test(TestDirection[] directions) {
        System.out.println("construct test");
        size = directions.length;
        this.directions = directions;
        outlierCount = 0;
        health = 0;
        trust = 0;
        healthDetail = new LinkedHashMap<>();
        trustDetail = new LinkedHashMap<>();
        messages = new StringBuilder();
        pearsons = new PearsonsCorrelation();
        initialize("");
        pairDirections();
        basicChecks();
    }

    /**
     * Constructor with three basic arguments. This constructor should be the
     * most often used.
     *
     * @param directions an array TestDirection representing all directions of
     * one test.
     * @param customerID customer ID
     * @param productID product ID
     * @param path customer file path
     */
    public Test(TestDirection[] directions, String customerID, String productID, String path) {
        size = directions.length;
        this.directions = directions;
        outlierCount = 0;
        health = 0;
        trust = 0;
        healthDetail = new LinkedHashMap<>();
        trustDetail = new LinkedHashMap<>();
        messages = new StringBuilder();
        pearsons = new PearsonsCorrelation();
        this.customerID = customerID;
        this.productID = productID;
        //initialize benchmarks and thresholds
        initialize(path);
        //pair directions
        pairDirections();
        //check outlers
        checkOutlier();
        //compute statistics excluding outliers
        findNoOutlierStats();
        //check health and trust
        basicChecks();
        System.out.println(">>>>> Created test for " + productID + " of customer " + customerID + " with " + size + " directions");
    }

    /**
     * Read xml configuration file and customer historical tests records to
     * initialize the benchmarks and thresholds.
     *
     * @param path customer file path
     */
    private void initialize(String path) {
        // convert the xmlString to a Document object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        String filePath = "Config.xml";
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(filePath));
            doc.getDocumentElement().normalize();
            thresholds = new double[4];
            highThresholds = new double[4];
            repeatNoiseThresholds = new double[4];
            //thresholds
            for (int i = 1; i <= 4; i++) {
                thresholds[i - 1] = Double.parseDouble(doc.getElementsByTagName("T" + i).item(0).getTextContent());
                highThresholds[i - 1] = Double.parseDouble(doc.getElementsByTagName("T" + i + "H").item(0).getTextContent());
                repeatNoiseThresholds[i - 1] = Double.parseDouble(doc.getElementsByTagName("T" + i + "R").item(0).getTextContent());
            }
            sigmaThreshold = Double.parseDouble(doc.getElementsByTagName("TS").item(0).getTextContent());
            sigmaThreshold2 = Double.parseDouble(doc.getElementsByTagName("TS2").item(0).getTextContent());
            corrThreshold = Double.parseDouble(doc.getElementsByTagName("TC").item(0).getTextContent());
            lane2LaneCorrThresholds = Double.parseDouble(doc.getElementsByTagName("TL").item(0).getTextContent());
            outlierThresholds = new double[6];
            pairedOutlierThresholds = new double[6];
            for (int i = 1; i <= 6; i++) {
                outlierThresholds[i - 1] = Double.parseDouble(doc.getElementsByTagName("T" + i + "O").item(0).getTextContent());
                pairedOutlierThresholds[i - 1] = Double.parseDouble(doc.getElementsByTagName("T" + i + "PO").item(0).getTextContent());
            }
            byLaneThreshold = Double.parseDouble(doc.getElementsByTagName("TBL").item(0).getTextContent());
            // benchmarks
            eyeChartIntelMinBenchmark = new double[4];
            eyeChartIntelMeanBenchmark = new double[4];
            for (int i = 1; i <= 4; i++) {
                eyeChartIntelMinBenchmark[i - 1] = Double.parseDouble(doc.getElementsByTagName("B" + i + "EyeMin").item(0).getTextContent());
                eyeChartIntelMeanBenchmark[i - 1] = Double.parseDouble(doc.getElementsByTagName("B" + i + "EyeMean").item(0).getTextContent());
            }
            passHealthBenchmark = Double.parseDouble(doc.getElementsByTagName("PH").item(0).getTextContent());
            passTrustBenchmark = Double.parseDouble(doc.getElementsByTagName("PT").item(0).getTextContent());
            failHealthBenchmark = Double.parseDouble(doc.getElementsByTagName("FH").item(0).getTextContent());
            failTrustBenchmark = Double.parseDouble(doc.getElementsByTagName("FT").item(0).getTextContent());

            maxNearWcCount = Integer.parseInt(doc.getElementsByTagName("MaxWC").item(0).getTextContent());
            desiredRepeatCount = Integer.parseInt(doc.getElementsByTagName("RepeatCount").item(0).getTextContent());

            // healthDetail
            healthDetail.put(MEANMINCHECK, Double.parseDouble(doc.getElementsByTagName("MEANMINCHECK").item(0).getTextContent()));
            healthDetail.put(MINMINCHECK, Double.parseDouble(doc.getElementsByTagName("MINMINCHECK").item(0).getTextContent()));
            healthDetail.put(HIGHMEANMINCHECK, Double.parseDouble(doc.getElementsByTagName("HIGHMEANMINCHECK").item(0).getTextContent()));
            healthDetail.put(HIGHMINMINCHECK, Double.parseDouble(doc.getElementsByTagName("HIGHMINMINCHECK").item(0).getTextContent()));
            healthDetail.put(SIGMAMEANCHECK, Double.parseDouble(doc.getElementsByTagName("SIGMAMEANCHECK").item(0).getTextContent()));
            healthDetail.put(SIGMAMINCHECK, Double.parseDouble(doc.getElementsByTagName("SIGMAMINCHECK").item(0).getTextContent()));
            healthDetail.put(HIGHMEANMEANCHECK, Double.parseDouble(doc.getElementsByTagName("HIGHMEANMEANCHECK").item(0).getTextContent()));
            healthDetail.put(HIGHMINMEANCHECK, Double.parseDouble(doc.getElementsByTagName("HIGHMINMEANCHECK").item(0).getTextContent()));
            healthDetail.put(OUTLIERHEALTH, Double.parseDouble(doc.getElementsByTagName("OUTLIERHEALTH").item(0).getTextContent()));
            healthDetail.put(WINDOWMEANCHECK, Double.parseDouble(doc.getElementsByTagName("WINDOWMEANCHECK").item(0).getTextContent()));
            healthDetail.put(REPEATNOISECHECK, Double.parseDouble(doc.getElementsByTagName("REPEATNOISECHECK").item(0).getTextContent()));
            healthDetail.put(HIGHTOLOWCORRCHECKHEALTH, Double.parseDouble(doc.getElementsByTagName("HIGHTOLOWCORRCHECKHEALTH").item(0).getTextContent()));
            healthDetail.put(WINDOWCORRCHECKHEALTH, Double.parseDouble(doc.getElementsByTagName("WINDOWCORRCHECKHEALTH").item(0).getTextContent()));
            healthDetail.put(NEARWCCOUNT, Double.parseDouble(doc.getElementsByTagName("NEARWCCOUNT").item(0).getTextContent()));
            healthDetail.put(LANE2LANECORRCHECKHEALTH, Double.parseDouble(doc.getElementsByTagName("LANE2LANECORRCHECKHEALTH").item(0).getTextContent()));
            // trustDetail
            trustDetail.put(SYSTEMCOUNT, Double.parseDouble(doc.getElementsByTagName("SYSTEMCOUNT").item(0).getTextContent()));
            trustDetail.put(REPEATCOUNT, Double.parseDouble(doc.getElementsByTagName("REPEATCOUNT").item(0).getTextContent()));
            trustDetail.put(MEANCHECK, Double.parseDouble(doc.getElementsByTagName("MEANCHECK").item(0).getTextContent()));
            trustDetail.put(MINCHECK, Double.parseDouble(doc.getElementsByTagName("MINCHECK").item(0).getTextContent()));
            trustDetail.put(SIGMAMEANCHECKTRUST, Double.parseDouble(doc.getElementsByTagName("SIGMAMEANCHECKTRUST").item(0).getTextContent()));
            trustDetail.put(SIGMAMINCHECKTRUST, Double.parseDouble(doc.getElementsByTagName("SIGMAMINCHECKTRUST").item(0).getTextContent()));
            trustDetail.put(TOTALSIGMACHECK, Double.parseDouble(doc.getElementsByTagName("TOTALSIGMACHECK").item(0).getTextContent()));
            trustDetail.put(OUTLIERTRUST, Double.parseDouble(doc.getElementsByTagName("OUTLIERTRUST").item(0).getTextContent()));
            trustDetail.put(HIGHTOLOWCORRCHECKTRUST, Double.parseDouble(doc.getElementsByTagName("HIGHTOLOWCORRCHECKTRUST").item(0).getTextContent()));
            trustDetail.put(WINDOWCORRCHECKTRUST, Double.parseDouble(doc.getElementsByTagName("WINDOWCORRCHECKTRUST").item(0).getTextContent()));
            trustDetail.put(LANE2LANECORRCHECKTRUST, Double.parseDouble(doc.getElementsByTagName("LANE2LANECORRCHECKTRUST").item(0).getTextContent()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.getMessage();
        }
        //set all outlier indicators as false
        outliers = new boolean[5];
        for (int i = 0; i < 5; i++) {
            outliers[i] = false;
        }
        //get system and repeat counts
        TestSystem tempSystem;
        String systemID;
        systems_repeats = new LinkedHashMap<>();
        for (int i = 0; i < getDirectionByIndex(0).getSize(); i++) {
            tempSystem = getSystemByIndexes(0, i);
            systemID = tempSystem.getSystemID();
            systems_repeats.put(systemID, tempSystem.getSize());
        }
    }

    /**
     * Check outliers of the test.
     */
    private void checkOutlier() {
        //obvious scan
        boolean outlierSystem;
        String message;
        //traverse each direction, system and repeat
        for (int i = 0; i < getDirectionByIndex(0).getSize(); i++) {
            int laneNum;
            outlierSystem = false;
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < getSystemByIndexes(j, i).getSize(); k++) {
                    outlierSystem = outlierSystem && getRepeatByIndexes(j, i, k).checkValidity();
                }
            }
            if (outlierSystem) {
                message = "Remeasure " + getSystemByIndexes(0, i).getSystemID() + ": all zeros;";
                messages.append(message);
                outliers[0] = true;
                //mark all lane margins of outlier system in all directions as outliers
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < getSystemByIndexes(j, i).getSize(); k++) {
                        laneNum = getRepeatByIndexes(j, i, k).getByLaneSize();
                        for (int l = 0; l < laneNum; l++) {
                            getRepeatByIndexes(j, i, k).markOutlierByLane(l);
                        }
                    }
                }
            }
        }
        //check direction outlier
        for (int i = 0; i < size; i++) {
            getDirectionByIndex(i).findWorstCase(byLaneThreshold);
            getDirectionByIndex(i).findOutlier(outlierThresholds, outliers, messages);
        }
        //check paired direction outlier
        for (int i = 0; i < size / 2; i++) {
            getPairedDirectionByIndex(i).findWorstCase(byLaneThreshold);
            getPairedDirectionByIndex(i).findOutlier(pairedOutlierThresholds, outliers, messages);
        }
    }

    /**
     * Compute statistics excluding outliers.
     */
    private void findNoOutlierStats() {
        //traverse each direction, system and repeat
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < getDirectionByIndex(i).getSize(); j++) {
                for (int k = 0; k < getSystemByIndexes(i, j).getSize(); k++) {
                    getRepeatByIndexes(i, j, k).findStats(CHECKOUTLIER);
                }
                getSystemByIndexes(i, j).findStats(CHECKOUTLIER);
            }
            getDirectionByIndex(i).findStats(CHECKOUTLIER);
        }
        //traverse each repeat of each paired directions
        for (int i = 0; i < size / 2; i++) {
            for (int j = 0; j < getPairedDirectionByIndex(i).getSize(); j++) {
                for (int k = 0; k < getPairedSystemByIndexes(i, j).getSize(); k++) {
                    getPairedRepeatByIndexes(i, j, k).findStats(CHECKOUTLIER);
                }
                getPairedSystemByIndexes(i, j).findStats(CHECKOUTLIER);
            }
            getPairedDirectionByIndex(i).findStats(CHECKOUTLIER);
        }
    }

    /**
     * Check and compute scores.
     */
    private void basicChecks() {
        for (int i = 0; i < size / 2; i++) {
            // basic mean check
            basicMeanCheck = basicMeanCheck && getDirectionByIndex(2 * i).getBasicStats().getMeanMin() > thresholds[i]
                    && getDirectionByIndex(2 * i + 1).getBasicStats().getMeanMin() > thresholds[i];

            outlierMeanCheck = outlierMeanCheck && getDirectionByIndex(2 * i).getNoOutlierStats().getMeanMin() > thresholds[i]
                    && getDirectionByIndex(2 * i + 1).getNoOutlierStats().getMeanMin() > thresholds[i];
            // basic min check
            basicMinCheck = basicMinCheck && getDirectionByIndex(2 * i).getBasicStats().getMin() > thresholds[i]
                    && getDirectionByIndex(2 * i + 1).getBasicStats().getMin() > thresholds[i];
            outlierMinCheck = outlierMinCheck && getDirectionByIndex(2 * i).getNoOutlierStats().getMin() > thresholds[i]
                    && getDirectionByIndex(2 * i + 1).getNoOutlierStats().getMin() > thresholds[i];
            // mean check 2 (high threshold)
            basicMeanCheckH = basicMeanCheckH && getDirectionByIndex(2 * i).getBasicStats().getMeanMin() > highThresholds[i]
                    && getDirectionByIndex(2 * i + 1).getBasicStats().getMeanMin() > highThresholds[i];
            // min check 2 (high threshold)	
            basicMinCheckH = basicMinCheckH && getDirectionByIndex(2 * i).getBasicStats().getMin() > highThresholds[i]
                    && getDirectionByIndex(2 * i + 1).getBasicStats().getMin() > highThresholds[i];
            // mean check 3
            outlierMeanCheckH2 = outlierMeanCheckH2 && getDirectionByIndex(2 * i).getNoOutlierStats().getMean() > highThresholds[i]
                    && getDirectionByIndex(2 * i + 1).getNoOutlierStats().getMean() > highThresholds[i];
            // mean check 4
            outlierMeanCheckH3 = outlierMeanCheckH3 && getDirectionByIndex(2 * i).getNoOutlierStats().getMinMean() > highThresholds[i]
                    && getDirectionByIndex(2 * i + 1).getNoOutlierStats().getMinMean() > highThresholds[i];
            // window check
            windowCheck = windowCheck && getPairedDirectionByIndex(i).getBasicStats().getMeanMin() > 2 * thresholds[i];
            outlierWindowCheck = outlierWindowCheck && getPairedDirectionByIndex(i).getNoOutlierStats().getMeanMin() > 2 * thresholds[i];
            // noise check
            outlierNoiseCheck = outlierNoiseCheck && getDirectionByIndex(2 * i).getNoOutlierStats().getRepeatNoise1() > repeatNoiseThresholds[i]
                    && getDirectionByIndex(2 * i + 1).getNoOutlierStats().getRepeatNoise1() > repeatNoiseThresholds[i];
            if (getDirectionByIndex(i).getSize() * getSystemByIndexes(i, 0).getSize() > 1) {
                //high to low correlation check
                udCorr = pearsons.correlation(getDirectionByIndex(i).getAllMarginMean(), getDirectionByIndex(i + 1).getAllMarginMean()) > corrThreshold;
            }
            // lane2lane check
            if (getDirectionByIndex(i).getSize() > 1) {
                lane2LaneCorr = getDirectionByIndex(2 * i).getLane2LaneCorr() > lane2LaneCorrThresholds && getDirectionByIndex(2 * i + 1).getLane2LaneCorr() > lane2LaneCorrThresholds;
            }
        }

        for (int i = 0; i < size; i++) {
            // sigma mean check
            basicSigmaMeanCheck = basicSigmaMeanCheck && getDirectionByIndex(i).getBasicStats().getSigmaMean() < sigmaThreshold;
            outlierSigmaMeanCheck = outlierSigmaMeanCheck && getDirectionByIndex(i).getNoOutlierStats().getSigmaMean() < sigmaThreshold;
            // sigma min check
            basicSigmaMinCheck = basicSigmaMinCheck && getDirectionByIndex(i).getBasicStats().getSigmaMin() < sigmaThreshold;
            outlierSigmaMinCheck = outlierSigmaMinCheck && getDirectionByIndex(i).getNoOutlierStats().getSigmaMin() < sigmaThreshold;
            // sigma check 2
            outlierSigmaMinCheckT2 = outlierSigmaMinCheckT2 && getDirectionByIndex(i).getNoOutlierStats().getSigmaMin() < sigmaThreshold2;
            // near worst case count
            nearWcCount = Math.max(nearWcCount, getDirectionByIndex(i).getNearWcCount());
        }
        
        nearWcCount = nearWcCount > maxNearWcCount ? maxNearWcCount : nearWcCount;
        // window correlation check
        if (getDirectionByIndex(0).getSize() * getSystemByIndexes(0, 0).getSize() > 1) {
            for (int i = 0; i < size / 4; i++) {
                winCorr = winCorr && pearsons.correlation(getPairedDirectionByIndex(2 * i).getAllMarginMean(), getPairedDirectionByIndex(2 * i + 1).getAllMarginMean()) > corrThreshold;
            }
        }

        //Get detail scores
        // mean check 1
        if (!basicMeanCheck && !outlierMeanCheck) {
            healthDetail.put(MEANMINCHECK, 0.0);
        }
        if (outlierMeanCheck != basicMeanCheck) {
            trustDetail.put(MEANCHECK, 0.0);
        }
        // mean check 2 (high threshold)
        if (!basicMeanCheckH) {
            healthDetail.put(HIGHMEANMINCHECK, 0.0);
        }
        // min check 1
        if (!basicMinCheck && !outlierMinCheck) {
            healthDetail.put(MINMINCHECK, 0.0);
        }
        if (outlierMinCheck != basicMinCheck) {
            trustDetail.put(MINCHECK, 0.0);
        }
        // min check 2 (high threshold)
        if (!basicMinCheckH) {
            healthDetail.put(HIGHMINMINCHECK, 0.0);
        }
        // sigma check (mean)
        if (!basicSigmaMeanCheck && !outlierSigmaMeanCheck) {
            healthDetail.put(SIGMAMEANCHECK, 0.0);
        }
        if (basicSigmaMeanCheck != outlierSigmaMeanCheck) {
            trustDetail.put(SIGMAMEANCHECKTRUST, 0.0);
        }
        // sigma check (min)
        if (!basicSigmaMinCheck && !outlierSigmaMinCheck) {
            healthDetail.put(SIGMAMINCHECK, 0.0);
        }
        if (basicSigmaMinCheck != outlierSigmaMinCheck) {
            trustDetail.put(SIGMAMINCHECKTRUST, 0.0);
        }
        // sigma check 2
        if (!outlierSigmaMinCheckT2) {
            trustDetail.put(TOTALSIGMACHECK, trustDetail.get(TOTALSIGMACHECK));
        } else {
            trustDetail.put(TOTALSIGMACHECK, 0.0);
        }
        // mean check 3 (high threshold)
        if (!outlierMeanCheckH2) {
            healthDetail.put(HIGHMEANMEANCHECK, 0.0);
        }
        // mean check 4 (high threshold)
        if (!outlierMeanCheckH3) {
            healthDetail.put(HIGHMINMEANCHECK, 0.0);
        }
        // outlier count
        for (int i = 0; i < outliers.length; i++) {
            if (outliers[i]) {
                outlierCount++;
            }
        }
        healthDetail.put(OUTLIERHEALTH, healthDetail.get(OUTLIERHEALTH) * outlierCount);
        trustDetail.put(OUTLIERTRUST, trustDetail.get(OUTLIERTRUST) * outlierCount);
        // window check
        if (!windowCheck && !outlierWindowCheck) {
            healthDetail.put(WINDOWMEANCHECK, 0.0);
        }
        // repeatability noise
        if (!outlierNoiseCheck) {
            healthDetail.put(REPEATNOISECHECK, 0.0);
        }
        //high to low correlation check
        if (!udCorr) {
            healthDetail.put(HIGHTOLOWCORRCHECKHEALTH, 0.0);
            trustDetail.put(HIGHTOLOWCORRCHECKTRUST, 0.0);
        }
        // window correlation check
        if (!winCorr) {
            healthDetail.put(WINDOWCORRCHECKHEALTH, 0.0);
            trustDetail.put(WINDOWCORRCHECKTRUST, 0.0);
        }
        //lane2lane chack
        if (!lane2LaneCorr) {
            healthDetail.put(LANE2LANECORRCHECKHEALTH, 0.0);
            trustDetail.put(LANE2LANECORRCHECKTRUST, 0.0);
        }
        //near worst case count
        healthDetail.put(NEARWCCOUNT, healthDetail.get(NEARWCCOUNT) * nearWcCount);
        //check system and repeat count
        trustDetail.put(SYSTEMCOUNT, (double) systems_repeats.size());
        double averageRepeat = Util.mapMeanValue(systems_repeats);
        trustDetail.put(REPEATCOUNT, Double.compare(averageRepeat, Double.NaN) == 0 ? 0 : averageRepeat);
        String message = "";
        for (Map.Entry<String, Integer> entry : systems_repeats.entrySet()) {
            if (entry.getValue() < desiredRepeatCount) {
                message = message + "System \"" + entry.getKey() + "\" needs more test;";
            }
        }
        messages.insert(0, message);
        health = Util.mapSumValue(healthDetail);
        trust = Util.mapSumValue(trustDetail);
        // check health and trust scores with benchmarks
        if (health >= passHealthBenchmark && trust >= passTrustBenchmark) {
            conclusion = "pass";
        } else if (health < failHealthBenchmark && trust > failTrustBenchmark) {
            conclusion = "fail";
        } else {
            conclusion = "not ready";
        }
        if (health < passHealthBenchmark && trust < passTrustBenchmark) {
            messages.insert(0, "Low health score and low trust score;");
        } else if (health < passHealthBenchmark) {
            messages.insert(0, "Low health score;");
        } else if (trust < passTrustBenchmark) {
            messages.insert(0, "Low trust score;");
        }
    }

    /**
     * Pair up/down, right/left directions together.
     */
    private void pairDirections() {
        pairedDirections = new TestDirection[size / 2];
        String directionID;
        int[] pairs = new int[8];
        for (int i = 0; i < 8; i++) {
            pairs[i] = -1;
        }
        for (int i = 0; i < size; i++) {
            directionID = getDirectionByIndex(i).getDirectionID();
            switch (directionID) {
                case "rxDqLeft":
                    pairs[0] = i;
                    break;
                case "rxDqRight":
                    pairs[1] = i;
                    break;
                case "rxVrefHigh":
                    pairs[2] = i;
                    break;
                case "rxVrefLow":
                    pairs[3] = i;
                    break;
                case "txDqLeft":
                    pairs[4] = i;
                    break;
                case "txDqRight":
                    pairs[5] = i;
                    break;
                case "txVrefHigh":
                    pairs[6] = i;
                    break;
                case "txVrefLow":
                    pairs[7] = i;
            }
        }
        int temp = 0;
        for (int i = 0; i < 4; i++) {
            if (pairs[2 * i] != -1 && pairs[2 * i + 1] != -1) {
                pairedDirections[temp] = getDirectionByIndex(pairs[2 * i]).add(getDirectionByIndex(pairs[2 * i + 1]));
                temp++;

            }

        }
    }

    /**
     * Get certain direction according to index.
     *
     * @param index direction index
     * @return TestDirection object
     */
    public TestDirection getDirectionByIndex(int index) {
        return directions[index];
    }

    /**
     * Get certain direction pair according to index.
     *
     * @param index direction pair index
     * @return TestDirection object
     */
    public TestDirection getPairedDirectionByIndex(int index) {
        return pairedDirections[index];
    }

    /**
     * Get certain system in direction according to indexes.
     *
     * @param directionIndex direction index
     * @param systemIndex system index
     * @return TestSystem object
     */
    public TestSystem getSystemByIndexes(int directionIndex, int systemIndex) {
        return getDirectionByIndex(directionIndex).getSystemByIndex(systemIndex);
    }

    /**
     * Get certain system in direction pair according to indexes.
     *
     * @param directionIndex direction pair index
     * @param systemIndex system index
     * @return TestSystem object
     */
    public TestSystem getPairedSystemByIndexes(int directionIndex, int systemIndex) {
        return getPairedDirectionByIndex(directionIndex).getSystemByIndex(systemIndex);
    }

    /**
     * Get certain repeat in direction according to indexes.
     *
     * @param directionIndex direction index
     * @param systemIndex system index
     * @param repeatIndex repeat index
     * @return SystemRepeat object
     */
    public TestRepeat getRepeatByIndexes(int directionIndex, int systemIndex, int repeatIndex) {
        return getDirectionByIndex(directionIndex).getSystemByIndex(systemIndex).getRepeatByIndex(repeatIndex);
    }

    /**
     * Get certain repeat in direction pair according to indexes
     *
     * @param directionIndex direction pair index
     * @param systemIndex system index
     * @param repeatIndex repeat index
     * @return SystemRepeat object
     */
    public TestRepeat getPairedRepeatByIndexes(int directionIndex, int systemIndex, int repeatIndex) {
        return getPairedDirectionByIndex(directionIndex).getSystemByIndex(systemIndex).getRepeatByIndex(repeatIndex);
    }

    /**
     * Get number of directions of the test.
     *
     * @return number of directions
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the number of outliers of the test.
     *
     * @return number of outliers
     */
    public double getOutlierCount() {
        return outlierCount;
    }

    /**
     * Get health score.
     *
     * @return health score
     */
    public double getHealth() {
        return health;
    }

    /**
     * Get trust score.
     *
     * @return trust score
     */
    public double getTrust() {
        return trust;
    }

    /**
     * Get health detail scores.
     *
     * @return a map of health detail scores
     */
    public Map<String, Double> getHealthDetail() {
        return healthDetail;
    }

    /**
     * Get trust detail scores.
     *
     * @return a map of trust detail scores
     */
    public Map<String, Double> getTrustDetail() {
        return trustDetail;
    }

    /**
     * Get output messages.
     *
     * @return warning messages about the test
     */
    public String getTestMessages() {
        return messages.toString();
    }

    /**
     * Get test conclusion.
     *
     * @return "pass" or "fail"
     */
    public String getConclusion() {
        return conclusion;
    }

    /**
     * Get customer ID.
     *
     * @return customer ID
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * Get product ID.
     *
     * @return product ID
     */
    public String getProductID() {
        return productID;
    }

    /**
     * Get system and repeat count records.
     *
     * @return a map of all systems and repeat counts
     */
    public Map<String, Integer> getSystems_repeats() {
        return systems_repeats;
    }

    /**
     * Get Intel eye chart min value benchmarks.
     *
     * @return an array of Intel min value benchmarks
     */
    public double[] getEyeChartIntelMinBenchmark() {
        return eyeChartIntelMinBenchmark;
    }

    /**
     * Get Intel eye chart mean value benchmarks.
     *
     * @return an array of Intel mean value benchmarks
     */
    public double[] getEyeChartIntelMeanBenchmark() {
        return eyeChartIntelMeanBenchmark;
    }

    /**
     * Get a string representation of a test.
     *
     * @return information of a test including statistics
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String message;
        for (int i = 0; i < size; i++) {
            message = getDirectionByIndex(i) + "\n";
            sb.append(message);
            sb.append(getDirectionByIndex(i).toString());
        }
        for (int i = 0; i < size / 2; i++) {
            message = getPairedDirectionByIndex(i) + "\n";
            sb.append(message);
            sb.append(getPairedDirectionByIndex(i).toString());
        }
        sb.append(messages.toString());
        return sb.toString();
    }
}
