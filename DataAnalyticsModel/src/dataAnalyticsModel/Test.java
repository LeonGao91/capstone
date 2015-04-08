package dataAnalyticsModel;

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
	double health;
	double trust;
	
	private TestDirection[] directions;
	private TestDirection[] pairedDirections;
	private int size; // number of directions
	private int outlierCount; // number of outliers
	
	private double[] thresholds;
	private double[] highThresholds;
	private double sigmaThreshold;
	private double sigmaThreshold2;
	
	private boolean basicMeanCheck = true;
	private boolean outlierMeanCheck = true;
	private boolean basicMinCheck = true;
	private boolean outlierMinCheck = true;
	
	private boolean basicMeanCheckHT = true; // high thresh
	private boolean basicMinCheckHT = true; // high thresh
	
	private boolean basicSigmaMeanCheck = true;
	private boolean outlierSigmaMeanCheck = true;
	private boolean basicSigmaMinCheck = true;
	private boolean outlierSigmaMinCheck = true;
	
	private boolean outlierSigmaMinCheckT2 = true;
	
	private boolean outlierMeanCheckHT2 = true; // mean check 3
	private boolean outlierMeanCheckHT3 = true; // mean check 4
	
	/**
	 * Constructor with no argument.
	 */
	public Test() {
	}

	/**
	 * Constructor with one int array argument.
	 *
	 * @param dataInput
	 *            a three-dimensional array storing all data of one direction.
	 */

	public Test(int[][][][] dataInput) {
		size = dataInput.length;
		directions = new TestDirection[size];
		//Construct each direction
		for (int i = 0; i < size; i++) {
			directions[i] = new TestDirection(dataInput[i]);
		}
		outlierCount = 0;
		pairDirections();
		basicChecks();
	}
	
	public void initializeThresholds(){
		thresholds = new double[4];
		highThresholds = new double[4];
		
		for (int i = 0; i < 4; i++){
			thresholds[i] = 6;
			highThresholds[i] = 10;
		}
		sigmaThreshold = 2;
		sigmaThreshold = 0.2;
	}
	
	public void initializeThresholds(String filePath){
		//ToDo
	}

	private void basicChecks() {
		for (int i = 0; i < thresholds[i]; i++) {
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
			// basic mean check high threshold
			basicMeanCheckHT = basicMeanCheckHT && directions[2 * i].getBasicStats().getMeanMin() > highThresholds[i]
					&& directions[2 * i + 1].getBasicStats().getMeanMin() > highThresholds[i];			
			basicMinCheckHT = basicMinCheckHT && directions[2 * i].getBasicStats().getMin() > highThresholds[i]
					&& directions[2 * i + 1].getBasicStats().getMin() > highThresholds[i];
			// basic sigma mean check
			basicSigmaMeanCheck = basicSigmaMeanCheck && directions[2 * i].getBasicStats().getSigmaMean() > sigmaThreshold
					&& directions[2 * i + 1].getBasicStats().getSigmaMean() > sigmaThreshold;
			outlierSigmaMeanCheck = outlierSigmaMeanCheck && directions[2 * i].getNoOutlierStats().getSigmaMean() > sigmaThreshold
					&& directions[2 * i + 1].getNoOutlierStats().getSigmaMean() > sigmaThreshold;
			// basic sigma min check
			basicSigmaMinCheck = basicSigmaMinCheck && directions[2 * i].getBasicStats().getSigmaMin() > sigmaThreshold
					&& directions[2 * i + 1].getBasicStats().getSigmaMin() > sigmaThreshold;
			outlierSigmaMinCheck = outlierSigmaMinCheck && directions[2 * i].getNoOutlierStats().getSigmaMin() > sigmaThreshold
					&& directions[2 * i + 1].getNoOutlierStats().getSigmaMin() > sigmaThreshold;
			// basic sigma min check threshold 2
			outlierSigmaMinCheckT2 = outlierSigmaMinCheckT2 && directions[2 * i].getNoOutlierStats().getSigmaMin() > sigmaThreshold2
					&& directions[2 * i + 1].getNoOutlierStats().getSigmaMin() > sigmaThreshold2;
			// mean check 3
			outlierMeanCheckHT2 = outlierMeanCheckHT2 && directions[2 * i].getNoOutlierStats().getMean() > highThresholds[i]
					&& directions[2 * i + 1].getNoOutlierStats().getMean() > highThresholds[i];
			// mean check 4
			outlierMeanCheckHT3 = outlierMeanCheckHT3 && directions[2 * i].getNoOutlierStats().getMinMean() > highThresholds[i]
					&& directions[2 * i + 1].getNoOutlierStats().getMinMean() > highThresholds[i];

			// outlier count
			outlierCount = directions[2 * i].getOutlierCount() + directions[2 * i + 1].getOutlierCount();
					
		}
		// mean check 1
		health += 20 * (basicMeanCheck || outlierMeanCheck ? 1:0);
		if (outlierMeanCheck == basicMeanCheck) {
			trust += 5;
		}		
		// mean check 2 (high threshold)
		health += 5 * (basicMeanCheckHT ? 1:0);
		
		// min check 1
		health += 20 * (basicMinCheck || outlierMinCheck ? 1:0);
		if (outlierMinCheck == basicMinCheck) {
			trust += 5;
		}		
		// min check 2 (high threshold)
		health += 5 * (basicMinCheckHT ? 1:0);
		
		// sigma check (mean)
		health += 5 * (basicSigmaMeanCheck || outlierSigmaMeanCheck ? 1:0);
		if (outlierMeanCheck == basicMeanCheck) {
			trust += 5;
		}
		// sigma check (min)
		health += 5 * (basicSigmaMinCheck || outlierSigmaMinCheck ? 1:0);
		if (outlierMinCheck == basicMinCheck) {
			trust += 5;
		}
		// sigma check 2
		trust -= 2 * (outlierSigmaMinCheckT2 ? 1:0);
		
		// mean check 3 (high threshold)
		health += 5 * (outlierMeanCheckHT2 ? 1:0);
		// mean check 4 (high threshold)
		health += 5 * (outlierMeanCheckHT3 ? 1:0);
		
		// outlier count
		health -= outlierCount;
		trust -= outlierCount;
		
		// window
	}

	/**
	 * Constructor with one Testdirection array argument.
	 *
	 * @param directions
	 *            an array TestDirection representing all directions of one test.
	 */
	public Test(TestDirection[] directions) {
		size = directions.length;
		this.directions = directions;
		outlierCount = 0;
		pairDirections();
	}
	
	
	
	
	/**
	 * Pair up/down, right/left directions together
	 */
	private void pairDirections(){
		pairedDirections = new TestDirection[size/2];
		for (int i = 0; i < pairedDirections.length; i++){
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
	public int getOutlierCount() {
		return outlierCount;
	}

	
}
