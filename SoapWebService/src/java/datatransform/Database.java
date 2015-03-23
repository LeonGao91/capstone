/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datatransform;

/**
 *
 * @author wangjerome
 */
public interface Database {
    public void putData(long[] dims, String name, Object data) throws Exception;
    
    public long[] getDataDim(String name) throws Exception;
    
    public void getData(String name, Object data) throws Exception;
    
    public void close() throws Exception;
}
