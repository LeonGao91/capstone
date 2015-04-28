package dataAnalyticsModel;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * This class represents a validation test composing directions, systems,
 * repeats and lanes. Methods include detecting outliers, worst cases and
 * computing their statistics.
 *
 *
 * @author Yan 04/04/2015
 * @version 1.0
 *
 */
public class Test {

    public static boolean CHECKOUTLIER = true; //remove outlier
    public static boolean NOTCHECKOUTLIER = false; // not remove outlier
    //Health check items
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
    //Trust check items
    public static String SYSTEMCOUNT = "System Count"; // TODO
    public static String REPEATCOUNT = "Repeat Count"; // TODO
    public static String MEANCHECK = "Mean Check";
    public static String MINCHECK = "Min Check";
    public static String SIGMAMEANCHECKTRUST = "Sigma of Mean Check";
    public static String SIGMAMINCHECKTRUST = "Sigma of Min Check";
    public static String TOTALSIGMACHECK = "Total Sigma Check";
    public static String OUTLIERTRUST = "Outlier Count for Trust";
    public static String HIGHTOLOWCORRCHECKTRUST = "High to Low Correlation Check for Trust";
    public static String WINDOWCORRCHECKTRUST = "Window Correlation Check for Trust";
    public static String LANE2LANECORRCHECKTRUST = "Lane2Lane Correlation Check for Trust";

    private double health;
    private double trust;
    private String conclusion;
    private String customerID;
    private String productID;

    private TestDirection[] directions;
    private TestDirection[] pairedDirections;
    private int size; // number of directions
    private double outlierCount; // number of outliers
    private LinkedHashMap<String, Double> healthDetail;
    private LinkedHashMap<String, Double> trustDetail;
    private Map<String, Integer> systems_repeats;

    //Thresholds
    private double[] thresholds;
    private double[] highThresholds;
    private double[] repeatNoiseThresholds;
    private double sigmaThreshold;
    private double sigmaThreshold2;
    private double corrThreshold;
    private double lane2LaneCorrThresholds;
    private double[] outlierThresholds; // xml configuration, default 6
    private double[] pairedOutlierThresholds; // xml configuration, default 12
    private double byLaneThreshold; /// xml configuration, default 0.5
    //Benchmarks
    private double[] eyeChartIntelMinBenchmark; // xml configuration size 4 -12, 12, 40, -40
    private double[] eyeChartIntelMeanBenchmark; // xml confituration size 4 -12, 12, 40, -40
    private double healthBenchmark; // xml configuration, default 80
    private double trustBenchmark; // xml configuration, default 80
    //Check results
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
    private PearsonsCorrelation pearsons = new PearsonsCorrelation();
    private StringBuilder messages;
    private boolean outliers[];
    private double nearWcCount;

    /**
     * Constructor with no argument.
     */
    public Test() {
    }

//	/**
//	 * Constructor with one int array argument.
//	 *
//	 * @param dataInput
//	 *            a three-dimensional array storing all data of one direction.
//	 */
//
//	public Test(int[][][][] dataInput) {
//		size = dataInput.length; 
//		directions = new TestDirection[size];
//		//Construct each direction
//		for (int i = 0; i < size; i++) {
//			directions[i] = new TestDirection(dataInput[i]);
//		}
//		outlierCount = 0;
//		pairDirections();
//		initializeThresholds();
//		basicChecks();
//	}
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
        systems_repeats = new LinkedHashMap<>();
        messages = new StringBuilder();
        initialize("Config.xml");
        pairDirections();
        basicChecks();
    }

    /**
     * Constructor with three basic arguments. This constructor should be the
     * most often used.
     *
     * @param directions an array TestDirection representing all directions of
     * one test.
     */
    public Test(TestDirection[] directions, String customerID, String productID) {
        size = directions.length;
        this.directions = directions;
        outlierCount = 0;
        health = 0;
        trust = 0;
        healthDetail = new LinkedHashMap<>();
        trustDetail = new LinkedHashMap<>();
        systems_repeats = new LinkedHashMap<>();
        messages = new StringBuilder();
        this.customerID = customerID;
        this.productID = productID;
        initialize("Config.xml");
        pairDirections();
        checkOutlier();
        findNoOutlierStats();
        basicChecks();
        System.out.println(customerID + productID);
    }

//	public void initializeThresholds(){
//		thresholds = new double[4];
//		highThresholds = new double[4];
//		repeatNoiseThresholds = new double[4];
//		
//		for (int i = 0; i < 4; i++){
//			thresholds[i] = 6;
//			highThresholds[i] = 10;
//			repeatNoiseThresholds[i] = 0.5;
//		}
//		sigmaThreshold = 2;
//		sigmaThreshold2 = 0.2;
//		corrThreshold = 0.8;
//		lane2LaneCorrThresholds = 0.8;
//	}
//	
    /**
     * Read an xml file and initialize the thresholds
     *
     * @param filePath
     */
    public void initialize(String filePath) {
        // convert the xmlString to a Document object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        thresholds = new double[4];
        highThresholds = new double[4];
        repeatNoiseThresholds = new double[4];

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

        System.out.println("Thresholds initialized");
        System.out.print(Arrays.toString(thresholds));
        System.out.print(Arrays.toString(highThresholds));
        System.out.print(Arrays.toString(repeatNoiseThresholds));
        System.out.print(Arrays.toString(outlierThresholds));
        System.out.println(Arrays.toString(pairedOutlierThresholds));
        System.out.println(sigmaThreshold + ", " + sigmaThreshold2 + ", " + corrThreshold + ", " + lane2LaneCorrThresholds + ", " + byLaneThreshold);

        // benchmarks
        eyeChartIntelMinBenchmark = new double[4];
        eyeChartIntelMeanBenchmark = new double[4];

        for (int i = 1; i <= 4; i++) {
            eyeChartIntelMinBenchmark[i - 1] = Double.parseDouble(doc.getElementsByTagName("B" + i + "EyeMin").item(0).getTextContent());
            eyeChartIntelMeanBenchmark[i - 1] = Double.parseDouble(doc.getElementsByTagName("B" + i + "EyeMean").item(0).getTextContent());
        }
        healthBenchmark = Double.parseDouble(doc.getElementsByTagName("BH").item(0).getTextContent());
        trustBenchmark = Double.parseDouble(doc.getElementsByTagName("BT").item(0).getTextContent());

        System.out.println("Benchmarks initialized");
        System.out.print(Arrays.toString(eyeChartIntelMinBenchmark));
        System.out.println(Arrays.toString(eyeChartIntelMeanBenchmark));
        System.out.println(healthBenchmark + ", " + trustBenchmark);

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

        System.out.println("Coefficients initialized");

        outliers = new boolean[5];
        for (int i = 0; i < 5; i++) {
            outliers[i] = false;
        }

        TestSystem tempSystem;
        String systemID;
        for (int i = 0; i < getDirectionByIndex(0).getSize(); i++) {
            tempSystem = getSystemByIndexes(0, i);
            systemID = tempSystem.getSystemID();
            systems_repeats.put(systemID, tempSystem.getSize());
        }
    }

    private void checkOutlier() {
        //Obvious scan
        boolean outlierSystem;
        for (int i = 0; i < getDirectionByIndex(0).getSize(); i++) {
            outlierSystem = false;
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < getSystemByIndexes(j, i).getSize(); k++) {
                    outlierSystem = outlierSystem && getRepeatByIndexes(j, i, k).checkValidity();
                }
            }
            if (outlierSystem) {
                messages.append("Remeasure " + getSystemByIndexes(0, i).getSystemID() + ": all zeros\n");
                outliers[0] = true;
            }
        }
        System.out.println("Obvious scan done");

        for (int i = 0; i < size; i++) {
            getDirectionByIndex(i).findWorstCase(byLaneThreshold);
            getDirectionByIndex(i).findOutlier(outlierThresholds, outliers, messages);
        }
        System.out.println("Direction outlier checked");
        for (int i = 0; i < size / 2; i++) {
            getPairedDirectionByIndex(i).findWorstCase(byLaneThreshold);
            getPairedDirectionByIndex(i).findOutlier(pairedOutlierThresholds, outliers, messages);
        }
        System.out.println("Paried direction outlier checked");
    }

    private void findNoOutlierStats() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < getDirectionByIndex(i).getSize(); j++) {
                for (int k = 0; k < getSystemByIndexes(i, j).getSize(); k++) {
                    getRepeatByIndexes(i, j, k).findStats(CHECKOUTLIER);
                }
                getSystemByIndexes(i, j).findStats(CHECKOUTLIER);
            }
            getDirectionByIndex(i).findStats(CHECKOUTLIER);
        }
        for (int i = 0; i < size / 2; i++) {
            for (int j = 0; j < getPairedDirectionByIndex(i).getSize(); j++) {
                for (int k = 0; k < getPairedSystemByIndexes(i, j).getSize(); k++) {
                    getPairedRepeatByIndexes(i, j, k).findStats(CHECKOUTLIER);
                }
                getPairedSystemByIndexes(i, j).findStats(CHECKOUTLIER);
            }
            getPairedDirectionByIndex(i).findStats(CHECKOUTLIER);
        }
        System.out.println("No outlier stats done");
    }

    private void basicChecks() {
        for (int i = 0; i < size / 2; i++) {
            // basic mean check
            basicMeanCheck = basicMeanCheck && directions[2 * i].getBasicStats().getMeanMin() > thresholds[i]
                    && directions[2 * i + 1].getBasicStats().getMeanMin() > thresholds[i];
            outlierMeanCheck = outlierMeanCheck && directions[2 * i].getNoOutlierStats().getMeanMin() > thresholds[i]
                    && directions[2 * i + 1].getNoOutlierStats().getMeanMin() > thresholds[i];

            // basic min check
            basicMinCheck = basicMinCheck && directions[2 * i].getBasicStats().getMin() > thresholds[i]
                    && directions[2 * i + 1].getBasicStats().getMin() > thresholds[i];
            outlierMeanCheck = outlierMeanCheck && directions[2 * i].getNoOutlierStats().getMin() > thresholds[i]
                    && directions[2 * i + 1].getNoOutlierStats().getMin() > thresholds[i];

            // mean check 2 (high threshold)
            basicMeanCheckH = basicMeanCheckH && directions[2 * i].getBasicStats().getMeanMin() > highThresholds[i]
                    && directions[2 * i + 1].getBasicStats().getMeanMin() > highThresholds[i];
            // min check 2 (high threshold)	
            basicMinCheckH = basicMinCheckH && directions[2 * i].getBasicStats().getMin() > highThresholds[i]
                    && directions[2 * i + 1].getBasicStats().getMin() > highThresholds[i];

            // mean check 3
            outlierMeanCheckH2 = outlierMeanCheckH2 && directions[2 * i].getNoOutlierStats().getMean() > highThresholds[i]
                    && directions[2 * i + 1].getNoOutlierStats().getMean() > highThresholds[i];
            // mean check 4
            outlierMeanCheckH3 = outlierMeanCheckH3 && directions[2 * i].getNoOutlierStats().getMinMean() > highThresholds[i]
                    && directions[2 * i + 1].getNoOutlierStats().getMinMean() > highThresholds[i];

            // window check
            windowCheck = windowCheck && pairedDirections[i].getBasicStats().getMeanMin() > 2 * thresholds[i];
            outlierWindowCheck = outlierWindowCheck && pairedDirections[i].getNoOutlierStats().getMeanMin() > 2 * thresholds[i];

            // noise check
            outlierNoiseCheck = outlierNoiseCheck && directions[2 * i].getNoOutlierStats().getRepeatNoise1() > repeatNoiseThresholds[i]
                    && directions[2 * i + 1].getNoOutlierStats().getRepeatNoise1() > repeatNoiseThresholds[i];

            if (directions[i].getSize() * getSystemByIndexes(i, 0).getSize() > 1) {
                //high to low correlation check
                udCorr = pearsons.correlation(directions[i].getAllMarginMean(), directions[i + 1].getAllMarginMean()) > corrThreshold;
                if (udCorr) {
                    health += healthDetail.get(HIGHTOLOWCORRCHECKHEALTH);
                    trust += trustDetail.get(HIGHTOLOWCORRCHECKTRUST);
                }
            }

            // lane2lane check
            if (directions[i].getSize() > 1) {
                lane2LaneCorr = directions[2 * i].getLane2LaneCorr() > lane2LaneCorrThresholds && directions[2 * i + 1].getLane2LaneCorr() > lane2LaneCorrThresholds;
                if (lane2LaneCorr) {
                    health += healthDetail.get(LANE2LANECORRCHECKHEALTH);
                    trust += trustDetail.get(LANE2LANECORRCHECKTRUST);
                }
            }

        }

        for (int i = 0; i < size; i++) {
            // sigma mean check
            basicSigmaMeanCheck = basicSigmaMeanCheck && directions[i].getBasicStats().getSigmaMean() < sigmaThreshold;
            outlierSigmaMeanCheck = outlierSigmaMeanCheck && directions[i].getNoOutlierStats().getSigmaMean() < sigmaThreshold;
            // sigma min check
            basicSigmaMinCheck = basicSigmaMinCheck && directions[i].getBasicStats().getSigmaMin() < sigmaThreshold;
            outlierSigmaMinCheck = outlierSigmaMinCheck && directions[i].getNoOutlierStats().getSigmaMin() < sigmaThreshold;
            // sigma check 2
            outlierSigmaMinCheckT2 = outlierSigmaMinCheckT2 && directions[i].getNoOutlierStats().getSigmaMin() < sigmaThreshold2;
            // near worst case count
            nearWcCount = Math.max(nearWcCount, directions[i].getNearWcCount());
        }

        // mean check 1
        if (basicMeanCheck || outlierMeanCheck) {
            health += healthDetail.get(MEANMINCHECK);
        }
        if (outlierMeanCheck == basicMeanCheck) {
            trust += trustDetail.get(MEANCHECK);
        }
        // mean check 2 (high threshold)
        if (basicMeanCheckH) {
            health += healthDetail.get(HIGHMEANMINCHECK);
        }

        // min check 1
        if (basicMinCheck || outlierMinCheck) {
            health += healthDetail.get(MINMINCHECK);
        }
        if (outlierMinCheck == basicMinCheck) {
            trust += trustDetail.get(MINCHECK);
        }
        // min check 2 (high threshold)
        if (basicMinCheckH) {
            health += healthDetail.get(HIGHMINMINCHECK);
        }

        // sigma check (mean)
        if (basicSigmaMeanCheck || outlierSigmaMeanCheck) {
            health += healthDetail.get(SIGMAMEANCHECK);
        }
        if (outlierMeanCheck == basicMeanCheck) {
            trust += trustDetail.get(SIGMAMEANCHECKTRUST);
        }
        // sigma check (min)
        if (basicSigmaMinCheck || outlierSigmaMinCheck) {
            health += healthDetail.get(SIGMAMINCHECK);
        }
        if (outlierMinCheck == basicMinCheck) {
            trust += trustDetail.get(SIGMAMINCHECKTRUST);
        }
        // sigma check 2
        if (outlierSigmaMinCheckT2) {
            trust -= trustDetail.get(TOTALSIGMACHECK);
        }

        // mean check 3 (high threshold)
        if (outlierMeanCheckH2) {
            health += healthDetail.get(HIGHMEANMEANCHECK);
        }
        // mean check 4 (high threshold)
        if (outlierMeanCheckH3) {
            health += healthDetail.get(HIGHMINMEANCHECK);
        }

        // outlier count
        for (int i = 0; i < outliers.length; i++) {
            if (outliers[i]) {
                outlierCount++;
            }
        }
        health += healthDetail.get(OUTLIERHEALTH) * outlierCount;
        trust += trustDetail.get(OUTLIERTRUST) * outlierCount;

        // window check
        if (windowCheck || outlierWindowCheck) {
            health += healthDetail.get(WINDOWMEANCHECK);
        }

        // repeatability noise
        if (outlierNoiseCheck) {
            health += healthDetail.get(REPEATNOISECHECK);
        }

        // window correlation check
        if (getDirectionByIndex(0).getSize() * getSystemByIndexes(0, 0).getSize() > 1) {
            for (int i = 0; i + 1 < size / 2; i++) {
                winCorr = winCorr && pearsons.correlation(pairedDirections[i].getAllMarginMean(), pairedDirections[i + 1].getAllMarginMean()) > corrThreshold;
            }
            if (winCorr) {
                health += healthDetail.get(WINDOWCORRCHECKHEALTH);
                trust += trustDetail.get(WINDOWCORRCHECKTRUST);
            }
        }

        // near worst case count
        health += healthDetail.get(NEARWCCOUNT) * nearWcCount;

        // check health and trust scores with benchmarks
        if (health >= healthBenchmark && trust >= trustBenchmark) {
            conclusion = "pass";
        } else {
            conclusion = "fail";
            if (health < healthBenchmark && trust < trustBenchmark) {
                messages.insert(0, "Low health score and low trust score.");
            } else if (health < healthBenchmark) {
                messages.insert(0, "Low health score.");
            } else {
                messages.insert(0, "Low trust score.");
            }
        }
    }

    /**
     * Pair up/down, right/left directions together
     */
    private void pairDirections() {
        pairedDirections = new TestDirection[size / 2];
        for (int i = 0; i < size / 2; i++) {
            pairedDirections[i] = directions[i * 2].add(directions[i * 2 + 1]);
        }
    }

    /**
     * Get certain direction according to index
     *
     * @param index direction index
     * @return TestDirection object
     */
    public TestDirection getDirectionByIndex(int index) {
        return directions[index];
    }

    public TestDirection getPairedDirectionByIndex(int index) {
        return pairedDirections[index];
    }

    public TestSystem getSystemByIndexes(int directionIndex, int systemIndex) {
        return directions[directionIndex].getSystemByIndex(systemIndex);
    }

    public TestSystem getPairedSystemByIndexes(int directionIndex, int systemIndex) {
        return pairedDirections[directionIndex].getSystemByIndex(systemIndex);
    }

    public TestRepeat getRepeatByIndexes(int directionIndex, int systemIndex, int repeatIndex) {
        return directions[directionIndex].getSystemByIndex(systemIndex).getRepeatByIndex(repeatIndex);
    }

    public TestRepeat getPairedRepeatByIndexes(int directionIndex, int systemIndex, int repeatIndex) {
        return pairedDirections[directionIndex].getSystemByIndex(systemIndex).getRepeatByIndex(repeatIndex);
    }

    /**
     * Get number of directions of the test
     *
     * @return number of directions
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the number of outliers of the test
     *
     * @return number of outliers
     */
    public double getOutlierCount() {
        return outlierCount;
    }

    public double getHealth() {
        return health;
    }

    public double getTrust() {
        return trust;
    }

    public Map<String, Double> getHealthDetail() {
        return healthDetail;
    }

    public Map<String, Double> getTrustDetail() {
        return trustDetail;
    }

    public String getTestMessages() {
        return messages.toString();
    }

    public String getConclusion() {
        return conclusion;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getProductID() {
        return productID;
    }

    public Map<String, Integer> getSystems_repeats() {
        return systems_repeats;
    }

    public double[] getEyeChartIntelMinBenchmark() {
        return eyeChartIntelMinBenchmark;
    }

    public double[] getEyeChartIntelMeanBenchmark() {
        return eyeChartIntelMeanBenchmark;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("direction" + i + " \n");
            sb.append(directions[i].toString());
        }
        for (int i = 0; i < size / 2; i++) {
            sb.append("pairedDirection" + i + " \n");
            sb.append(pairedDirections[i].toString());
        }
        sb.append(messages.toString());
        return sb.toString();
    }
}
