package dataAnalyticsModel;

import java.io.File;
import java.util.HashMap;

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
	public static String SYSTEMCOUNT = "System Count";
	public static String REPEATCOUNT = "Repeat Count";
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
	
	private TestDirection[] directions;
	private TestDirection[] pairedDirections;
	private int size; // number of directions
	private double outlierCount; // number of outliers
	private HashMap<String, Double> healthItems;
	private HashMap<String, Double> trustItems;
	
	//Thresholds
	private double[] thresholds;
	private double[] highThresholds;
	private double[] repeatNoiseThresholds;
	private double sigmaThreshold;
	private double sigmaThreshold2;
	private double corrThreshold;
	private double lane2LaneCorrThresholds;
	private double[] outlierThresholds = new double[6]; //TODO xml configuration, default 6
	
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
	private StringBuffer messages;
	private boolean outliers[];
	
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
	 * @param directions
	 *            an array TestDirection representing all directions of one test.
	 */
	public Test(TestDirection[] directions) {
		System.out.println("construct test");
		size = directions.length; 
		this.directions = directions;
		outlierCount = 0;
		health = 0;
		trust = 0;
		healthItems = new HashMap<>();
		trustItems = new HashMap<>();
		messages = new StringBuffer();
		initializeOutliers();
		initializeThresholds("/Users/yuecao/Dropbox/capstone/DataAnalyticsModel/src/Thresholds.txt");
		pairDirections();  //TODO
		basicChecks();
	}
	
	public void initializeThresholds(){
		thresholds = new double[4];
		highThresholds = new double[4];
		repeatNoiseThresholds = new double[4];
		
		for (int i = 0; i < 4; i++){
			thresholds[i] = 6;
			highThresholds[i] = 10;
			repeatNoiseThresholds[i] = 0.5;
		}
		sigmaThreshold = 2;
		sigmaThreshold2 = 0.2;
		corrThreshold = 0.8;
		lane2LaneCorrThresholds = 0.8;
	}
	
	private void initializeOutliers(){
		outliers = new boolean[5];
		for (int i = 0; i < 5; i++){
			outliers[i] = false;
		}
	}
	
	/**
	 * Read an xml file and initialize the thresholds
	 * @param filePath
	 */
	public void initializeThresholds(String filePath){
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
		
		NodeList nl;
        for (int i = 1; i <= 4; i++) {
    			nl = doc.getElementsByTagName("T" + i);
    			thresholds[i - 1] = Double.parseDouble(nl.item(0).getTextContent());

    			nl = doc.getElementsByTagName("T" + i + "H");
    			highThresholds[i - 1] = Double.parseDouble(nl.item(0).getTextContent());
    			
    			nl = doc.getElementsByTagName("T" + i + "R");
    			repeatNoiseThresholds[i - 1] = Double.parseDouble(nl.item(0).getTextContent());
        }

        nl = doc.getElementsByTagName("TS");
		sigmaThreshold = Double.parseDouble(nl.item(0).getTextContent());
		
		nl = doc.getElementsByTagName("TS2");
		sigmaThreshold2 = Double.parseDouble(nl.item(0).getTextContent());
		
		nl = doc.getElementsByTagName("TC");
		corrThreshold = Double.parseDouble(nl.item(0).getTextContent());
		
		nl = doc.getElementsByTagName("TL");
		lane2LaneCorrThresholds = Double.parseDouble(nl.item(0).getTextContent()); 
	}

	private void checkOutlier(){
		//Obvious scan
		boolean outlierSystem;
		for (int i = 0; i < getDirectionByIndex(0).getSize(); i++){
			outlierSystem = false;
			for (int j =0; j < size; j++){
				for (int k = 0; k < getSystemByIndexes(j,i).getSize(); k++){
					outlierSystem = outlierSystem && getRepeatByIndexes(j, i, k).checkValidity();
				}
			}
			if (outlierSystem){
				messages.append("Remeasure " + getSystemByIndexes(0,i).getSystemID() + ": all zeros\n");
				outliers[0] = true;
			}
		}
		//Check threshold-based outliers
		TestRepeat tempRepeat;
		TestSystem tempSystem;
		TestDirection tempDirection;
		StringBuffer sb = new StringBuffer();
		String message = "";
		for (int i = 0; i < size; i++) {
			tempDirection = getDirectionByIndex(i);
			for (int j = 0; j < tempDirection.getSize(); j++){
				tempSystem = getSystemByIndexes(i, j);
				for (int k = 0; k < tempSystem.getSize(); k++){
					tempRepeat = getRepeatByIndexes(i, j, k);
					// Threshold based outlier detection - 1
					message = tempRepeat.findSystemOutliers(tempDirection.getBasicStats().getMean(),
								outlierThresholds[0]);
					if (!message.equals("") && sb.indexOf(message) == -1){
						outliers[1] = true;
						sb.append(", " + message); 
					}
					// Threshold based outlier detection - 2
					message = tempRepeat.findSystemOutliers(tempSystem.getBasicStats().getMean(), outlierThresholds[1]);
					if (!message.equals("") && (sb.indexOf(message) == -1)){
						outliers[2] = true;
						sb.append(", " + message); 
					}					
					// Lane2lane outlier - 1
					message = tempRepeat.findLaneOutliers(outlierThresholds[2],	outlierThresholds[3]);
					if (!message.equals("") && (sb.indexOf(message) == -1)){
						outliers[3] = true;
						sb.append(", " + message); 
					}
					if (sb.length() == 0){
						sb.insert(0, "  Lane outlier found in " + tempSystem.getSystemID() + " " + tempRepeat.getRepeatID() + ":\n");
					}
				}
			}
			// Lane2lane outlier - 2
			boolean meanOutlier = false;
			boolean medianOutlier = false;
			meanOutlier = (tempDirection.getMeanAllLane() - tempDirection.getMinAllLane()) > outlierThresholds[4];
			medianOutlier = (tempDirection.getMedianAllLane() - tempDirection.getMinAllLane()) > outlierThresholds[5];
			if (meanOutlier || medianOutlier) {
				outliers[4] = true;
				sb.append("  " + getRepeatByIndexes(i, 0, 0).getLaneIDByIndex(tempDirection.getMinLaneNo()) + " Outlier in All Systems\n");
				tempDirection.markOutlierByLane(tempDirection.getMinLaneNo());
			}
			if (sb.length() != 0){
				messages.append(tempDirection.getDirectionID() + ":\n");
				messages.append(sb);
			}
			sb = new StringBuffer();
		}
		//Find no outlier stats
		for (int i = 0; i < size; i++){
			for (int j = 0; j < getDirectionByIndex(i).getSize(); j++){
				for (int k = 0; k < getSystemByIndexes(i, j).getSize(); k++){
					getRepeatByIndexes(i, j, k).findStats(TestDirection.CHECKOUTLIER);
				}
				getSystemByIndexes(i, j).findStats(TestDirection.CHECKOUTLIER);
			}
			getDirectionByIndex(i).findStats(TestDirection.CHECKOUTLIER);
		}
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

			// basic mean & min check high threshold
			basicMeanCheckH = basicMeanCheckH && directions[2 * i].getBasicStats().getMeanMin() > highThresholds[i]
					&& directions[2 * i + 1].getBasicStats().getMeanMin() > highThresholds[i];			
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
			
			//high to low correlation check
			udCorr = udCorr && pearsons.correlation(directions[i].getAllMarginMean(), directions[i + 1].getAllMarginMean())> corrThreshold;
		}
		
		for (int i = 0; i < size; i++) { 
			// basic sigma mean check
			basicSigmaMeanCheck = basicSigmaMeanCheck && directions[i].getBasicStats().getSigmaMean() < sigmaThreshold;
			outlierSigmaMeanCheck = outlierSigmaMeanCheck && directions[i].getNoOutlierStats().getSigmaMean() < sigmaThreshold;
			// basic sigma min check
			basicSigmaMinCheck = basicSigmaMinCheck && directions[i].getBasicStats().getSigmaMin() < sigmaThreshold;
			outlierSigmaMinCheck = outlierSigmaMinCheck && directions[i].getNoOutlierStats().getSigmaMin() < sigmaThreshold;
			// basic sigma min check threshold 2
			outlierSigmaMinCheckT2 = outlierSigmaMinCheckT2 && directions[i].getNoOutlierStats().getSigmaMin() < sigmaThreshold2;
			// outlier count
			outlierCount += directions[i].getOutlierCount();
			// lane2lane check
			lane2LaneCorr = lane2LaneCorr && directions[i].getLane2LaneCorr() > lane2LaneCorrThresholds;
		}
	
		//window correlation check
		for (int i = 0; i + 1 < size / 2; i++) { 
			winCorr = winCorr && pearsons.correlation(pairedDirections[i].getAllMarginMean(), pairedDirections[i + 1].getAllMarginMean()) > corrThreshold;
		}
		
		// mean check 1
		if (basicMeanCheck || outlierMeanCheck) {
			healthItems.put(MEANMINCHECK, 20.0);
		}

		if (outlierMeanCheck == basicMeanCheck) {
			trustItems.put(MEANCHECK, 5.0);
		}	
		// mean check 2 (high threshold)
		if (basicMeanCheckH) {
			healthItems.put(HIGHMEANMINCHECK, 5.0);
		}
		
		// min check 1
		if (basicMinCheck || outlierMinCheck) {
			healthItems.put(MINMINCHECK, 20.0);
		}
		if (outlierMinCheck == basicMinCheck) {
			trustItems.put(MINCHECK, 5.0);
		}		
		// min check 2 (high threshold)
		if (basicMinCheckH) {
			healthItems.put(HIGHMINMINCHECK, 5.0);
		}		
		
		// sigma check (mean)
		if (basicSigmaMeanCheck || outlierSigmaMeanCheck) {
			healthItems.put(SIGMAMEANCHECK, 5.0);
		}
		if (outlierMeanCheck == basicMeanCheck) {
			trustItems.put(SIGMAMEANCHECKTRUST, 5.0);
		}
		// sigma check (min)
		if (basicSigmaMinCheck || outlierSigmaMinCheck) {
			healthItems.put(SIGMAMINCHECK, 5.0);
		}		
		if (outlierMinCheck == basicMinCheck) {
			trustItems.put(SIGMAMINCHECKTRUST, 5.0);
		}
		// sigma check 2
		if (outlierSigmaMinCheckT2) {
			trustItems.put(TOTALSIGMACHECK, -2.0);
		}
		
		// mean check 3 (high threshold)
		if (outlierMeanCheckH2) {
			healthItems.put(HIGHMEANMEANCHECK, 5.0);
		}		
		// mean check 4 (high threshold)
		if (outlierMeanCheckH3) {
			healthItems.put(HIGHMINMEANCHECK, 5.0);
		}
				
		// outlier count
		healthItems.put(OUTLIERHEALTH, -outlierCount);
		trustItems.put(OUTLIERTRUST, -outlierCount);
		
		// window check
		if (windowCheck || outlierWindowCheck) {
			healthItems.put(WINDOWMEANCHECK, 5.0);
		}

		// repeatability noise
		if (outlierNoiseCheck) {
			healthItems.put(REPEATNOISECHECK, 5.0);
		}
		
		if (udCorr) {
			healthItems.put(HIGHTOLOWCORRCHECKHEALTH, 1.5);
			trustItems.put(HIGHTOLOWCORRCHECKTRUST, 2.5);
		}
		
		if (lane2LaneCorr) {
			healthItems.put(LANE2LANECORRCHECKHEALTH, 1.5);
			trustItems.put(LANE2LANECORRCHECKTRUST, 2.5);
		}
		
		if (winCorr) {
			healthItems.put(WINDOWCORRCHECKHEALTH, 3.0);
			trustItems.put(WINDOWCORRCHECKTRUST, 5.0);
		}
		
		
		for (double value : healthItems.values()) {
			health += value;
		}
		
		for (double value : trustItems.values()) {
			trust += value;
		}
	}

	/**
	 * Pair up/down, right/left directions together
	 */
	private void pairDirections(){
		pairedDirections = new TestDirection[size/2];
		for (int i = 0; i < size/2; i++){ 
			pairedDirections[i] = directions[i*2].add(directions[i*2+1]);
		}
	}

	/**
	 * Get certain direction according to index
	 * 
	 * @param index
	 *            direction index
	 * @return TestDirection object
	 */
	public TestDirection getDirectionByIndex(int index) {
		return directions[index];
	}
	
	public TestSystem getSystemByIndexes(int directionIndex, int systemIndex) {
		return directions[directionIndex].getSystemByIndex(systemIndex);
	}

	public TestRepeat getRepeatByIndexes(int directionIndex, int systemIndex, int repeatIndex) {
		return directions[directionIndex].getSystemByIndex(systemIndex).getRepeatByIndex(repeatIndex);
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
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++){
			sb.append("direction" + i + " \n");
			sb.append(directions[i].toString());
		}
		for (int i = 0; i < size/2; i++){
			sb.append("pairedDirection" + i + " \n");
			sb.append(pairedDirections[i].toString());
		}
		return sb.toString();
	}
	
	public String toXML(){
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
		
	}
}
