package dataAnalyticsModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This class represents a tool to export test results to user test summary file
 * and test result file.
 * 
 * @author Yan 04/24/2015
 * @version 1.0
 *
 */
public class ResultExporter {
	/**
	 * Output test results to customer summary file and test result file in
	 * system assigned directories.
	 * 
	 * @param test test object containing all test data and results.
	 */
	public static void output(Test test, String path) {
		//Output to test result file located in customerID/result/ directory.
		TestDetail testDetail = new TestDetail();
		testDetail.setHealth(test.getHealth());
		testDetail.setHealth_detail(test.getHealthDetail());
		testDetail.setTrust(test.getTrust());
		testDetail.setTrust_detail(test.getTrustDetail());
		testDetail.setMessages(test.getTestMessages());
		EyeChart eyeChart;
		LinkedHashMap<String, EyeChart> eyes = new LinkedHashMap<>();
		String eyeName;
		double[] min;
		double[] mean;
		TestDirection tempDirection = new TestDirection();
		String directionID;
		//read and write eye charts data
		//[0] timing left
		//[1] timing right
		//[2] voltage high
		//[3] voltage low
		for (int i = 0; i < test.getSize(); i++) {
			tempDirection = test.getDirectionByIndex(i);
			directionID = tempDirection.getDirectionID();
			eyeName = directionID.substring(0, 2);

			if (eyes.containsKey(eyeName)) {
				min = eyes.get(eyeName).getMin();
				mean = eyes.get(eyeName).getMean();
			} else {
				eyeChart = new EyeChart();
				min = new double[4];
				mean = new double[4];
				eyeChart.setMin(min);
				eyeChart.setMean(mean);
				eyeChart.setIntel_min(test.getEyeChartIntelMinBenchmark());
				eyeChart.setIntel_mean(test.getEyeChartIntelMeanBenchmark()) ;
				eyes.put(eyeName, eyeChart);
			}
			if (directionID.toLowerCase().contains("left")) {
				min[0] = -tempDirection.getBasicStats().getMin();
				mean[0] = -tempDirection.getBasicStats().getMinMean();
			} else if (directionID.toLowerCase().contains("right")) {
				min[1] = tempDirection.getBasicStats().getMin();
				mean[1] = tempDirection.getBasicStats().getMinMean();
			} else if (directionID.toLowerCase().contains("high")) {
				min[2] = tempDirection.getBasicStats().getMin();
				mean[2] = tempDirection.getBasicStats().getMinMean();
			} else if (directionID.toLowerCase().contains("low")) {
				min[3] = -tempDirection.getBasicStats().getMin();
				mean[3] = -tempDirection.getBasicStats().getMinMean();
			}
		}
		testDetail.setEyes(eyes);
		//Convert to xml object
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("testDetail", TestDetail.class);
		xstream.alias("eyeChart", EyeChart.class);
		String xml = xstream.toXML(testDetail);
		//Get test date time
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		String timeStamp = dateFormat.format(cal.getTime());
		String resultDirName = path + "/" + test.getCustomerID() + "/result";
		String resultFileName = test.getProductID() + "_" + timeStamp + ".xml";
		//Write to xml file
		File dir = new File(resultDirName);
		if (!dir.exists()) {
			System.out.println("Folder created");
			dir.mkdir();
		}
		File file = new File(dir, resultFileName);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n");
			output.write(xml);
			output.close();
			System.out.println("File has been written");
		} catch (Exception e) {
			System.out.println("Could not create file");
		}

		//Output to customer summary file located in customerID/ directory.
		TestSummary testSummary;
		TestProduct testProduct;
		TestBrief testBrief;
		xstream = new XStream(new DomDriver());
		xstream.alias("testSummary", TestSummary.class);
		xstream.alias("testProduct", TestProduct.class);
		xstream.alias("testBrief", TestBrief.class);
		String summaryDirName = path + "/" + test.getCustomerID();
		String summaryFileName = "summary.xml";
		dir = new File(summaryDirName);
		if (!dir.exists()) {
			System.out.println("Folder created");
			dir.mkdir();
		}
		file = new File(dir, summaryFileName);
		//read existing file if summary exists
		if (file.exists()) { 
			System.out.println("Summary exists");
			try {
				BufferedReader input = new BufferedReader(new FileReader(file));
				StringBuilder sb = new StringBuilder();
				String line = input.readLine();
				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = input.readLine();
				}
				xml = sb.toString();
				System.out.println("read: " + xml);
				input.close();
				System.out.println("XML file has been read");
			} catch (Exception e) {
				System.out.println("Could find file");
			}
			//Convert to summary object
			testSummary = (TestSummary) xstream.fromXML(xml);
			//Get product if exists
			if (testSummary.contains(test.getProductID())) { 
				System.out.println("Product exists");
				testProduct = testSummary.getProduct(test.getProductID());
			} else { //Create new product if doesn't exist
				System.out.println("Product doesnot exist");
				testProduct = new TestProduct();
			}
		} else {  //Create new summary if doesn't exist
			System.out.println("Summary doesnot exist");
			testSummary = new TestSummary();
			testProduct = new TestProduct();
		}
		//Construct new test brief and add to product
		testBrief = new TestBrief();
		testBrief.setHealth(test.getHealth());
		testBrief.setPass_fail(test.getConclusion());
		testBrief.setTrust(test.getTrust());
		testBrief.setTest_date(timeStamp);
		testBrief.setResult_file(resultFileName);
		testProduct.addTest(testBrief);
		testProduct.setLast_test_date(timeStamp);
		testProduct.setPass_fail(test.getConclusion());
		testProduct.updateSystemsRepeats(test.getSystems_repeats());
		//Update test summary
		testSummary.updateProduct(test.getProductID(), testProduct);
		//Convert to xml object
		xml = xstream.toXML(testSummary);
		//Write to xml file
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n");
			output.write(xml);
			output.close();
			System.out.println("File has been written");
		} catch (Exception e) {
			System.out.println("Could not create file");
		}
	}
}
