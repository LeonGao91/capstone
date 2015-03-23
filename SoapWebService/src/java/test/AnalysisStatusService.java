/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;


@WebService(serviceName = "AnalysisStatusService")
public class AnalysisStatusService {


    /**
     * get product names based on the customer id
     */
    @WebMethod(operationName = "getProductNamesByCustomerID")
    public java.util.ArrayList<String> getProductNamesByCustomerID(@WebParam(name = "customerID") String customerID) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * get platform id based on product id and customer
     */
    @WebMethod(operationName = "getPlatformIDByProductCustomer")
    public java.util.ArrayList<String> getPlatformIDByProductCustomer(@WebParam(name = "productName") String productName, @WebParam(name = "customerName") String customerName) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * get timestamps based on customer id, product id and platform id
     */
    @WebMethod(operationName = "getTimestampByCustomerProductPlatform")
    public java.util.ArrayList<String> getTimestampByCustomerProductPlatform(@WebParam(name = "customerID") String customerID, @WebParam(name = "productID") String productID, @WebParam(name = "platformID") String platformID) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * check if a file name already exist in the storage system
     */
    @WebMethod(operationName = "checkNameAvailibility")
    public Boolean checkNameAvailibility(@WebParam(name = "fileName") String fileName) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * set the raw file path based on customer id, product id, platform id and timestamp
     */
    @WebMethod(operationName = "updateRawFileWithAddress")
    public Boolean updateRawFileWithAddress(@WebParam(name = "customerID") String customerID, @WebParam(name = "productID") String productID, @WebParam(name = "platformID") String platformID, @WebParam(name = "timestamp") String timestamp, @WebParam(name = "filePath") String filePath) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * update a customer's test file with the file status
     */
    @WebMethod(operationName = "updateTestStatus")
    @Oneway
    public void updateTestStatus(@WebParam(name = "customerID") String customerID, @WebParam(name = "productID") String productID, @WebParam(name = "platformID") String platformID, @WebParam(name = "timestamp") String timestamp, @WebParam(name = "fileStatus") Integer fileStatus) {
    }

    /**
     * get a customer's test status, basically include no found, analyzing, analyzed
     */
    @WebMethod(operationName = "getFileTestStatus")
    public Integer getFileTestStatus(@WebParam(name = "customerID") String customerID, @WebParam(name = "productID") String productID, @WebParam(name = "platformID") String platformID, @WebParam(name = "timeStamp") String timeStamp) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * return true if a customer has unfinished job
     */
    @WebMethod(operationName = "checkCustomerHavingUnfinishedJob")
    public Boolean checkCustomerHavingUnfinishedJob(@WebParam(name = "customerID") String customerID) {
        //TODO write your implementation code here:
        return null;
    }

    /**
     * return the result file of a customer's test
     */
    @WebMethod(operationName = "getTestResultFilePath")
    public String getTestResultFilePath(@WebParam(name = "customerID") String customerID, @WebParam(name = "productID") String productID, @WebParam(name = "platformID") String platformID, @WebParam(name = "timeStamp") String timeStamp) {
        //TODO write your implementation code here:
        return null;
    }

}
