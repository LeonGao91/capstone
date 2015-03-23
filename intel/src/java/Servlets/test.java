package Servlets;

import ServiceClients.DataWebService_Service;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import test.SoapService_Service;
import test.SoapService;

/**
 * 
 * @author leon
 */
@WebServlet("/testing")
public class test extends HttpServlet {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/SoapWebService/SoapService.wsdl")
    private SoapService_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/home/DataWebService.wsdl")
    private DataWebService_Service service;
    
    @Override
    public void init() {
    }

    /**
     * Getting the average number of all numbers by getting the web service
     * @param request 
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("average", getResult(request.getParameter("file_name")));
        RequestDispatcher view = request.getRequestDispatcher("result.jsp");
        view.forward(request, response);
    }

    private String hello(java.lang.String name) {
        ServiceClients.DataWebService port = service.getDataWebServicePort();
        return port.hello(name);
    }

    /**
     * Get result from SOAP web service
     * @param fileName
     * @return 
     */
    private Integer getResult(java.lang.String fileName) {
        SoapService port = service_1.getSoapServicePort();
        return port.getResult(fileName);
    }
    
}
