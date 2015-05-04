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
     * @param path output path
     */
    public static void output(Test test, String path) {
        //create file paths and file names
        String summaryDirName = path + "/" + test.getCustomerID();
        System.out.println(summaryDirName);
        String summaryFileName = "summary.xml";
        File summaryDir = new File(summaryDirName);
        if (!summaryDir.exists()) {
            System.out.println("Summary folder not found, create new one");
            summaryDir.mkdir();
        }
        File summaryFile = new File(summaryDir, summaryFileName);
        //get test date time
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        String timeStamp = dateFormat.format(cal.getTime());
        String resultDirName = path + "/" + test.getCustomerID() + "/result";
        System.out.println(resultDirName);
        String resultFileName = test.getProductID() + "_" + timeStamp + ".xml";
        //write to xml file
        File resultDir = new File(resultDirName);
        if (!resultDir.exists()) {
            System.out.println("Result folder not found, create a new one");
            resultDir.mkdir();
        }
        File resultFile = new File(resultDir, resultFileName);

        //output to test result file located in customerID/result/ directory.
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
        TestDirection tempDirection;
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
                eyeChart.setIntel_mean(test.getEyeChartIntelMeanBenchmark());
                eyes.put(eyeName, eyeChart);
            }
            if (directionID.toLowerCase().contains("left")) {
                min[0] = tempDirection.getBasicStats().getMin();
                mean[0] = tempDirection.getBasicStats().getMinMean();
            } else if (directionID.toLowerCase().contains("right")) {
                min[1] = tempDirection.getBasicStats().getMin();
                mean[1] = tempDirection.getBasicStats().getMinMean();
            } else if (directionID.toLowerCase().contains("high")) {
                min[2] = tempDirection.getBasicStats().getMin();
                mean[2] = tempDirection.getBasicStats().getMinMean();
            } else if (directionID.toLowerCase().contains("low")) {
                min[3] = tempDirection.getBasicStats().getMin();
                mean[3] = tempDirection.getBasicStats().getMinMean();
            }
        }
        testDetail.setEyes(eyes);
        //convert to xml object
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("testDetail", TestDetail.class);
        xstream.alias("eyeChart", EyeChart.class);
        String xml = xstream.toXML(testDetail);
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(resultFile));
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
        //read existing file if summary exists
        if (summaryFile.exists()) {
            System.out.println("Summary exists");
            try {
                BufferedReader input = new BufferedReader(new FileReader(summaryFile));
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
            } catch (Exception e) {
                System.out.println("Could find file");
            }
            //convert to summary object
            testSummary = (TestSummary) xstream.fromXML(xml);
            //get product if exists
            if (testSummary.contains(test.getProductID())) {
                System.out.println("Product exists");
                testProduct = testSummary.getProduct(test.getProductID());
            } else { 
                //create new product if doesn't exist
                System.out.println("Product doesnot exist");
                testProduct = new TestProduct();
            }
        } else { 
            //create new summary if doesn't exist
            System.out.println("Summary doesnot exist");
            testSummary = new TestSummary();
            testProduct = new TestProduct();
        }
        //construct new test brief and add to product
        testBrief = new TestBrief();
        testBrief.setHealth(test.getHealth());
        testBrief.setPass_fail(test.getConclusion());
        testBrief.setTrust(test.getTrust());
        testBrief.setTest_date(timeStamp);
        testBrief.setResult_file(resultFileName);
        testBrief.setSystems_repeats(test.getSystems_repeats());
        testProduct.addTest(testBrief);
        testProduct.setLast_test_date(timeStamp);
        testProduct.setPass_fail(test.getConclusion());
        testProduct.setSystems_repeats(test.getSystems_repeats());
        //update test summary
        testSummary.updateProduct(test.getProductID(), testProduct);
        //convert to xml object
        xml = xstream.toXML(testSummary);
        //write to xml file
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(summaryFile));
            output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n");
            output.write(xml);
            output.close();
            System.out.println("File has been written");
        } catch (Exception e) {
            System.out.println("Could not create file");
        }
    }
}
