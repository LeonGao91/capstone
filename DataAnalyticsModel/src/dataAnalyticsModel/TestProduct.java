package dataAnalyticsModel;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class TestProduct {
	private String name;
	private String last_test_date;
	private String pass_fail;
	private Map<String, Integer> systems_repeats;
	private LinkedList<TestBrief> tests;

	
	public TestProduct(String name, String last_test_date, String pass_fail,
			Map<String, Integer> systems_repeats, LinkedList<TestBrief> tests) {
		this.name = name;
		this.last_test_date = last_test_date;
		this.pass_fail = pass_fail;
		this.systems_repeats = systems_repeats;
		this.tests = tests;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLast_test_date(String last_test_date) {
		this.last_test_date = last_test_date;
	}

	public void setPass_fail(String pass_fail) {
		this.pass_fail = pass_fail;
	}

	public void setSystems_repeats(Map<String, Integer> systems_repeats) {
		this.systems_repeats = systems_repeats;
	}

	public void setTests(LinkedList<TestBrief> tests) {
		this.tests = tests;
	}
	
}
