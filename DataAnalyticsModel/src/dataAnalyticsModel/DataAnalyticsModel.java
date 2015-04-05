package dataAnalyticsModel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class DataAnalyticsModel {
	 /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SummaryStatistics ss = new SummaryStatistics();
        ss.addValue(1);
        ss.addValue(2);
        ss.addValue(3);
        ss.addValue(4);
        ss.addValue(5);
//        System.out.println((int)ss.getMax());
//        System.out.println((int)ss.getMin());
//        System.out.println(ss.getMean());
//        System.out.println(ss.getStandardDeviation());
    }
}
