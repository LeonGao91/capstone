package test;

import datatransform.DataStructureFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

/**
 * Soap service to upload file and analysis data
 * @author wangjerome
 */
@WebService(serviceName = "SoapService")
public class SoapService {
    
    //uploaded file status, including updated and done
    Map<String, String> fileStatus = new HashMap<String, String>();
    
    //uploaded file result if it is done
    Map<String, Integer> results = new HashMap<String, Integer>();

    //hdf5 file to input the file, not needed in further development
    private static final String hdf5 = "/Users/wangjerome/Desktop/H5_CreateFile.h5";

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getFilePath")
    @Oneway
    public void getFilePath(@WebParam(name = "path") String path) {
        
        //print out file path
        System.out.println("service: " + path); 
        
        //get the file name, discard the directories
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        
        //update file status
        fileStatus.put(fileName, "uploaded");
        
        //transform the data into hdf5, no needed in futher development
        datatransform.DataTransform dt = new datatransform.DataTransform();
        int result = 0;
        try {
            result = dt.transform(path, hdf5, "dset", DataStructureFactory.getDataStructure());
        } catch (HDF5LibraryException ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SoapService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //updata file status
        fileStatus.put(fileName, "done");
        
        //update result
        results.put(fileName, result);
        
    }

    /**
     * Web service operation, return result of a file test
     */
    @WebMethod(operationName = "getResult")
    public Integer getResult(@WebParam(name = "fileName") String fileName) {
        return results.get(fileName);
    }
}
