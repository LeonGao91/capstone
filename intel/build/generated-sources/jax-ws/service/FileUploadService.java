
package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebService(name = "FileUploadService", targetNamespace = "http://service/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface FileUploadService {


    /**
     * 
     * @param file
     * @param customerID
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getDetail", targetNamespace = "http://service/", className = "service.GetDetail")
    @ResponseWrapper(localName = "getDetailResponse", targetNamespace = "http://service/", className = "service.GetDetailResponse")
    @Action(input = "http://service/FileUploadService/getDetailRequest", output = "http://service/FileUploadService/getDetailResponse")
    public String getDetail(
        @WebParam(name = "customerID", targetNamespace = "")
        String customerID,
        @WebParam(name = "file", targetNamespace = "")
        String file);

    /**
     * 
     * @param customerID
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSummary", targetNamespace = "http://service/", className = "service.GetSummary")
    @ResponseWrapper(localName = "getSummaryResponse", targetNamespace = "http://service/", className = "service.GetSummaryResponse")
    @Action(input = "http://service/FileUploadService/getSummaryRequest", output = "http://service/FileUploadService/getSummaryResponse")
    public String getSummary(
        @WebParam(name = "customerID", targetNamespace = "")
        String customerID);

}
