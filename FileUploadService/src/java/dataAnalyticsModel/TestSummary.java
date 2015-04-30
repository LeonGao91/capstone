package dataAnalyticsModel;

import java.util.LinkedHashMap;

/**
 * This class represents a customer summary. It is mainly used to convert and
 * parse xml. It contains a list of products that belong to the customer.
 *
 * @author Yan 04/24/2015
 * @version 1.0
 *
 */
public class TestSummary {

    private LinkedHashMap<String, TestProduct> products; // a list of test
    // product objects

    /**
     * Constructor with no argument
     */
    public TestSummary() {
        products = new LinkedHashMap<>();
    }

    /**
     * Constructor with all argument
     *
     * @param products a list of test product objects
     */
    public TestSummary(LinkedHashMap<String, TestProduct> products) {
        this.products = products;
    }

    /**
     * Check whether certain product ID exists
     *
     * @param productID product ID
     * @return true if product exists in the summary, false otherwise
     */
    public boolean contains(String productID) {
        return products.containsKey(productID);
    }

    /**
     * Get product by ID
     *
     * @param productID product ID
     * @return a test product object that matches the ID
     */
    public TestProduct getProduct(String productID) {
        return products.get(productID);
    }

    /**
     * Update product information by product ID
     *
     * @param productID product ID
     * @param product test product object with up-to-date test information
     */
    public void updateProduct(String productID, TestProduct product) {
        products.put(productID, product);
    }
}
