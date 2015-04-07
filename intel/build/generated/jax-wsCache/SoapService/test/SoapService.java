
package test;

import javax.jws.Oneway;
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
 * JAX-WS RI 2.2.10-b140803.1500
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SoapService", targetNamespace = "http://test/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SoapService {


    /**
     * 
     * @param fileName
     * @return
     *     returns java.lang.Integer
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getResult", targetNamespace = "http://test/", className = "test.GetResult")
    @ResponseWrapper(localName = "getResultResponse", targetNamespace = "http://test/", className = "test.GetResultResponse")
    @Action(input = "http://test/SoapService/getResultRequest", output = "http://test/SoapService/getResultResponse")
    public Integer getResult(
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @param path
     */
    @WebMethod
    @Oneway
    @RequestWrapper(localName = "getFilePath", targetNamespace = "http://test/", className = "test.GetFilePath")
    @Action(input = "http://test/SoapService/getFilePath")
    public void getFilePath(
        @WebParam(name = "path", targetNamespace = "")
        String path);

}
