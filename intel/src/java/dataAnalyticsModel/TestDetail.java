package dataAnalyticsModel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents detailed test results. It is mainly used to convert and
 * parse xml. Data contain test health and trust scores and detailed
 * information, eye charts data and warning messages.
 * 
 * @author Yan 04/24/2015
 * @version 1.0
 *
 */

public class TestDetail {
	private double health;  //health score
	private double trust;  //trust score
	private Map<String, Double> health_detail; //a map of detailed health items scores
	private Map<String, Double> trust_detail; // a map of detailed trust items and scores
	private Map<String, EyeChart> eyes; //a map of all eye charts
	private String messages; //warning messages
    
	/**
	 * Constructor with no argument
	 */
	public TestDetail() {
		health_detail = new LinkedHashMap<>();
		trust_detail = new LinkedHashMap<>();
		eyes = new LinkedHashMap<>();
                
	}

	/**
	 * Constructor with all arguments
	 * @param health health score
	 * @param trust trust score
	 * @param health_detail a map of detailed health items scores
	 * @param trust_detail a map of detailed trust items and scores
	 * @param eyes a map of all eye charts
	 * @param messages warning messages
	 */
	public TestDetail(double health, double trust,
			Map<String, Double> health_detail,
			Map<String, Double> trust_detail, Map<String, EyeChart> eyes,
			String messages) {
		this.health = health;
		this.trust = trust;
		this.health_detail = health_detail;
		this.trust_detail = trust_detail;
		this.eyes = eyes;
		this.messages = messages;
	}

	/**
	 * Set health score
	 * @param health health score
	 */
	public void setHealth(double health) {
		this.health = health;
	}

	/**
	 * Set trust score
	 * @param trust trust score
	 */
	public void setTrust(double trust) {
		this.trust = trust;
	}

	/**
	 * Set health detail scores map
	 * @param health_detail a map of detailed health items scores
	 */
	public void setHealth_detail(Map<String, Double> health_detail) {
		this.health_detail = health_detail;
	}

	/**
	 * Set trust detail scores map
	 * @param trust_detail a map of detailed trust items scores
	 */
	public void setTrust_detail(Map<String, Double> trust_detail) {
		this.trust_detail = trust_detail;
	}

	/**
	 * Set eye charts map
	 * @param eyes a map of eye charts
	 */
	public void setEyes(Map<String, EyeChart> eyes) {
		this.eyes = eyes;
	}

	/**
	 * Set warning messages
	 * @param messages a String of warning messages
	 */
	public void setMessages(String messages) {
		this.messages = messages;
	}

	/**
	 * Get health score
	 * @return health score
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * Get trust score
	 * @return trust score
	 */
	public double getTrust() {
		return trust;
	}

	/**
	 * Get health detail scores map
	 * @return a map of detailed health items scores
	 */
	public Map<String, Double> getHealth_detail() {
		return health_detail;
	}
        
        /**
	 * Get health detail scores set for iteration
	 * @return a map of detailed health items scores
	 */
	public Set<String> getHealth_detail_keySet() {
		return health_detail.keySet();
	}

	/**
	 * Get trust detail scores map
	 * @return a map of detailed trust items scores
	 */
	public Map<String, Double> getTrust_detail() {
		return trust_detail;
	}

        /**
	 * Get Trust detail scores set for iteration
	 * @return a map of detailed health items scores
	 */
	public Set<String> getTrust_detail_keySet() {
		return trust_detail.keySet();
	}
        
	/**
	 * Get eye charts map
	 * @return a map of eye charts
	 */
	public Map<String, EyeChart> getEyes() {
		return eyes;
	}

	/**
	 * Get warning messages
	 * @return warning messages
	 */
	public String[] getMessages() {
            //use ";" as line breaker
            String[] m = messages.split(";");
            for(int i = 0; i < m.length; i++) {
                String s = m[i];
                s = s.replaceAll("#", "&nbsp;&nbsp;");
                System.out.println(s);
                m[i] = s;
            }
            return m;
	}

}
