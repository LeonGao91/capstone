package dataAnalyticsModel;

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
		double[] min = new double[4];
		double[] mean = new double[4];
		TestDirection tempDirection = new TestDirection();
		for(int i = 0; i < test.getSize() / 4; i++){
			eyeChart = new EyeChart();
			for(int j = 0; j < 4; j++){
				tempDirection = test.getDirectionByIndex(4 * i + j);
				if (tempDirection.getDirectionID().contains("left") || tempDirection.getDirectionID().contains("low")){
					min[j] = - (tempDirection.getBasicStats().getMin());
					mean[j] = - (tempDirection.getBasicStats().getMinMean());
				}else{
					min[j] = tempDirection.getBasicStats().getMin();
					mean[j] = tempDirection.getBasicStats().getMinMean();
				}
			}
			eyeName = tempDirection.getDirectionID().substring(0, 2);
			eyeChart.setMin(min);
			eyeChart.setMean(mean);
			eyes.put(eyeName, eyeChart);
			eyeChart = new EyeChart();
		}
		testDetail.setEyes(eyes);
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("testDetail", TestDetail.class);
		xstream.alias("eyeChart", EyeChart.class);
		String xml = xstream.toXML(testDetail);
		System.out.println(xml);
		
		
		TestSummary testSummary = new TestSummary();
		
	}
}
