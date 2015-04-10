package dataAnalyticsModel;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a lane of a validation test which contain the margin of
 * one lanes of one repeat as well as additional lane information, such as which
 * channel or rank this lane margin value belongs to.
 * 
 * @author Yan 04/08/2015
 * @version 1.0
 *
 */

public class TestLane {
	public static boolean VALID = true; //the margin value is valid
	public static boolean INVALID = false; //the margin value is invalid
	private int margin; // margin value of the lane
	private HashMap<String, String> attributes; // lane attributes
	private boolean state; //true for valid, false otherwise

	/**
	 * Constructor with no argument.
	 */
	public TestLane() {
	}
	
	/**
	 * Constructor with one margin value argument.
	 *
	 * @param dataInput
	 *            an integer of margin value.
	 */
	public TestLane(int dataInput) {
		margin = dataInput;
		attributes = new HashMap<>();
		state = true;
	}
	
	/**
	 * Constructor to clone a lane.
	 * 
	 * @param lane
	 *            a TestLane object
	 */
	public TestLane(TestLane lane){
		this.margin = lane.getMargin();
		this.attributes = lane.getAttributes();
		this.state = lane.isValid();
	}


	/**
	 * Constructor with two arguments: margin value and lane state.
	 * 
	 * @param dataInput
	 *            an array storing all data of one repeat.
	 * @param isValid
	 *            a boolean indicating whether this lane is valid.
	 */

	public TestLane(int dataInput, boolean isValid) {
		margin = dataInput;
		attributes = new HashMap<>();
		state = isValid;
	}
	
	/**
	 * Get the attributes of the lane.
	 * 
	 * @return a HashMap of attribute names and values
	 */
	public HashMap<String, String> getAttributes(){
		return attributes;
	}
	
	/**
	 * Add one attribute pair of the lane. Such as channel no. and rank no.
	 * 
	 * @param attriName
	 *            the name of the attribute
	 * @param attriValue
	 *            the value of the attribute
	 */
	public void setLaneAttribute(String attriName, String attriValue) {
		attributes.put(attriName, attriValue);
	}

	/**
	 * Get the attribute value according to attribute name.
	 * 
	 * @param attriName
	 *            the name of the attribute
	 * @return the value of the attribute
	 */
	public String getLaneAttribute(String attriName) {
		return attributes.get(attriName);
	}

	/**
	 * Get whether the lane margin is a valid value (invalid: NA in original
	 * data).
	 * 
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		return state;
	}

	/**
	 * Get the margin value.
	 * 
	 * @return the margin value
	 */
	public int getMargin() {
		return margin;
	}
	
	/**
	 * Set the margin value of the lane.
	 * 
	 * @param margin
	 *            margin value
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}
	
	/**
	 * Set the state of the lane.
	 * 
	 * @param state
	 *            true if the lane is valid, false otherwise
	 */
	public void setState(boolean state) {
		this.state = state;
	}
	
	/**
	 * Get String of all attributes and values.
	 *  
	 * @return a String representing lane attributes
	 */
	public String printAttributes(){
		String forReturn = "";
		for (Map.Entry<String, String> entry: attributes.entrySet()){
			forReturn = forReturn + entry.getKey() + ": " + entry.getValue() + "\n";
		}
		return forReturn;
	}
	
	/**
	 * Set the attributes of the lane
	 * 
	 * @param attributes
	 *            an HashMap of attributes names and values
	 */
	public void setAttributs(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * Get a String of the margin value 
	 */
	public String toString(){
		return "" + margin;
	}
	
	/**
	 * Tester of class.
	 * 
	 * @param args no args
	 */
	public static void main(String[] args){
		TestLane lane = new TestLane(5);
		System.out.println(lane);
		System.out.println(lane.isValid());
		lane.setLaneAttribute("channel", "0");
		lane.setLaneAttribute("rank", "0");
		System.out.println(lane.printAttributes());
	}

}
