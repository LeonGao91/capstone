package dataAnalyticsModel;

import java.util.HashMap;

/**
 * This class represents a lane of a validation test which contain the margin of
 * one lanes of one repeat as well as additional lane information, such as which
 * channel or rank this lane margin value belongs to.
 * 
 * @author Yan 04/07/2015
 * @version 1.0
 *
 */

public class TestLane {
	public static boolean VALID = true;
	public static boolean NOTVALID = false;
	private int margin; // margin value of the lane
	private HashMap<String, String> attributes; // lane attributes
	private boolean state;

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
		state = this.VALID;
	}


	/**
	 * Constructor with two arguments: margin value and lane state.
	 * @param dataInput an array storing all data of one repeat.
	 * @param isValid a boolean indicating whether this lane is valid.
	 */

	public TestLane(int dataInput, boolean isValid) {
		margin = dataInput;
		attributes = new HashMap<>();
		state = isValid;
	}

	
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
		attributes.put(attriName, attriName);
	}

	/**
	 * Get the attribute value according to attribute name
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
	 * data)
	 * 
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		return state;
	}

	/**
	 * Get the margin value
	 * 
	 * @return the margin value
	 */
	public int getMargin() {
		return margin;
	}
	
	public void setMargin(int margin){
		this.margin = margin;
	}
	
	public void setState(boolean state){
		this.state = state;
	}

}
