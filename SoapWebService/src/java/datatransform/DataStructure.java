package datatransform;

/**
 *
 * @author wangjerome
 */
public interface DataStructure {
    
    /**
     * parameter number, aka, the dimension of the array
     * @return 
     */
    public int getParamNum();
    
    /**
     * translate a string into an integer
     * @param columnNum
     * @param value
     * @return 
     */
    public int translate(int columnNum, String value);
    
    public int getSize(int columnNum);
    
    public Object getData();
    
    public void setData(Object data, int value, int[] params);
    
    public String toString(Object data);
}
