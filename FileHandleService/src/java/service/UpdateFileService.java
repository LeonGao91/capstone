/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author wangjerome
 */
@WebService(serviceName = "UpdateFileService")
public class UpdateFileService {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "uploadFile")
    @Oneway
    public void uploadFile(@WebParam(name = "path") String path) {
        System.out.println(path);
    }
}
