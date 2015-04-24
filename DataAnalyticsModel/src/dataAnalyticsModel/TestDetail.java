package dataAnalyticsModel;

import java.util.Map;

public class TestDetail {
	private double health;
	private double trust;
	private Map<String, Double> health_detail;
	private Map<String, Double> trust_detail;
	private Map<String, EyeChart> eyes;
	private String messages;
	
	public TestDetail(){
		
	}
	
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

	public void setHealth(double health) {
		this.health = health;
	}

	public void setTrust(double trust) {
		this.trust = trust;
	}

	public void setHealth_detail(Map<String, Double> health_detail) {
		this.health_detail = health_detail;
	}

	public void setTrust_detail(Map<String, Double> trust_detail) {
		this.trust_detail = trust_detail;
	}

	public void setEyes(Map<String, EyeChart> eyes) {
		this.eyes = eyes;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

}
