/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author wangjerome
 */
@WebService(serviceName = "FileAnalysisService")
public class FileAnalysisService {


    /**
     * analyze a file with particular file, and put the result into result path
     */
    @WebMethod(operationName = "analyzeFile")
    public Boolean analyzeFile(@WebParam(name = "customerID") String customerID, @WebParam(name = "productID") String productID, @WebParam(name = "platformID") String platformID, @WebParam(name = "timeStamp") String timeStamp, @WebParam(name = "filePath") String filePath, @WebParam(name = "resultPath") String resultPath) {
        //TODO write your implementation code here:
        return null;
    }
}
