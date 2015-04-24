package dataAnalyticsModel;

public class TestBrief {
	private String pass_fail;
	private String test_date;
	private double health;
	private double trust;
	
	public TestBrief(){
		
	}
	
	public TestBrief(String pass_fail, String test_date, double health,
			double trust) {
		this.pass_fail = pass_fail;
		this.test_date = test_date;
		this.health = health;
		this.trust = trust;
	}

	public void setPass_fail(String pass_fail) {
		this.pass_fail = pass_fail;
	}

	public void setTest_date(String test_date) {
		this.test_date = test_date;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public void setTrust(double trust) {
		this.trust = trust;
	}
	
	
	
	
}
