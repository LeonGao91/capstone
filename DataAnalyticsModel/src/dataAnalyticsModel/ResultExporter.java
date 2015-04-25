package dataAnalyticsModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ResultExporter {
	public static void output(Test test){
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
		/*read / write
		 *[0]timing left
		 *[1]timing right
		 *[2]voltage high
		 *[3]voltage low
		 */
		for(int i = 0; i < test.getSize(); i++){
			//System.out.println("generating eyeChart");
			tempDirection = test.getDirectionByIndex(i);
			directionID = tempDirection.getDirectionID();
			eyeName = directionID.substring(0, 2);
			
			if(eyes.containsKey(eyeName)){
				min = eyes.get(eyeName).getMin();
				mean = eyes.get(eyeName).getMean();
			}else{
				eyeChart = new EyeChart();
				min = new double[4];
				mean = new double[4];
				eyeChart.setMin(min);
				eyeChart.setMean(mean);
				//eyeChart.setIntel_min(test.getIntelMinBenchmark);
				//eyeChart.setIntem_mean(test.getIntelMeanBenchmark);
				eyes.put(eyeName, eyeChart);
			}
			if (directionID.toLowerCase().contains("left")){
				min[0] = - tempDirection.getBasicStats().getMin();
				mean[0] = - tempDirection.getBasicStats().getMinMean();
			}else if(directionID.toLowerCase().contains("right")) {
				min[1] = tempDirection.getBasicStats().getMin();
				mean[1] = tempDirection.getBasicStats().getMinMean();
			}else if(directionID.toLowerCase().contains("high")){
				min[2] = tempDirection.getBasicStats().getMin();
				mean[2] = tempDirection.getBasicStats().getMinMean();
			}else if(directionID.toLowerCase().contains("low")){
				min[3] = - tempDirection.getBasicStats().getMin();
				mean[3] = - tempDirection.getBasicStats().getMinMean();
			}
		}
		testDetail.setEyes(eyes);
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("testDetail", TestDetail.class);
		xstream.alias("eyeChart", EyeChart.class);
		String xml = xstream.toXML(testDetail);
		//System.out.println(xml);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		String timeStamp = dateFormat.format(cal.getTime()); 
		//String dirName = test.getCustomerID();
		//String fileName = test.getProductID + "_" + timeStamp;
		String dirName = "testClient/result";
		String fileName = "testProduct" +  "_" + timeStamp + ".xml";
		File dir = new File (dirName);
		if (!dir.exists()){
			System.out.println("Folder created");
			dir.mkdir();
		}
		File file = new File (dir, fileName);
		try{
	        BufferedWriter output = new BufferedWriter(new FileWriter(file));
	        output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n");
	        output.write(xml);
	        output.close();
	        System.out.println("File has been written");
	    }catch(Exception e){
	        System.out.println("Could not create file");
	    }
		
		TestSummary testSummary = new TestSummary();
		xstream = new XStream(new DomDriver()); 
		xstream.alias("testSummary", TestSummary.class);
		xstream.alias("testProduct", TestProduct.class);
		dirName = "testClient";
		fileName = "summary.xml";
		dir = new File (dirName);
		if (!dir.exists()){
			System.out.println("Folder created");
			dir.mkdir();
		}
		file = new File (dir, fileName);
		if (file.exists()){
			//read from xml
			testSummary = (TestSummary)xstream.fromXML(xml);
			//add new record
			//write to xml
		}else{
			//form xml
			try{
		        BufferedWriter output = new BufferedWriter(new FileWriter(file));
		        output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n");
		        output.write(xml);
		        output.close();
		        System.out.println("File has been written");
		    }catch(Exception e){
		        System.out.println("Could not create file");
		    }
		}
	}
}
