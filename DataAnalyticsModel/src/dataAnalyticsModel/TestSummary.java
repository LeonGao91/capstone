package dataAnalyticsModel;

import java.util.LinkedList;

public class TestSummary {
	private LinkedList<TestProduct> products;

	public TestSummary(){
	}
	
	public TestSummary(LinkedList<TestProduct> products) {
		this.products = products;
	}

	public void setProducts(LinkedList<TestProduct> products) {
		this.products = products;
	}
	
	
}
