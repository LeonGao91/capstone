/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servlets;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import dataAnalyticsModel.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import service.FileUploadService_Service;

/**
 *
 * @author leon
 */
@WebServlet(name = "Detail", urlPatterns = {"/Detail"})
public class Detail extends HttpServlet {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/128.237.207.25_8080/FileUploadService/FileUploadService.wsdl")
    private FileUploadService_Service service;

    private TestDetail detail;
    private XStream xstream = new XStream(new DomDriver());

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        String file = request.getParameter("file");
        String product = request.getParameter("product");
        int testNum = Integer.parseInt(request.getParameter("test"));
        TestSummary summary = (TestSummary) request.getSession().getAttribute("summary");
        xstream.alias("testDetail", TestDetail.class);
        xstream.alias("eyeChart", EyeChart.class);
        detail = (TestDetail) xstream.fromXML(getDetail((String) request.getSession().getAttribute("folders"), file));
        request.getSession().setAttribute("detail", detail);
        request.getSession().setAttribute("currentProduct", summary.getProduct(product));
        request.getSession().setAttribute("currentTest", summary.getProduct(product).getTests().get(testNum));
        System.out.println(product);
        request.getSession().setAttribute("productName", product);
        response.sendRedirect("result.jsp");

    }

//    private String getDetail(String file) {
//        String xml = "";
//        File f = new File("/Users/leon/Desktop/Capstone/capstone/intel/" + file);
//        try {
//            Scanner s = new Scanner(f);
//            while(s.hasNextLine()){
//                xml = xml.concat(s.nextLine());
//            }
//        } catch (FileNotFoundException ex) {
//            System.out.println("file not found");
//        }
//        return xml;
//    }

    private String getDetail(java.lang.String customerID, java.lang.String file) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        service.FileUploadService port = service.getFileUploadServicePort();
        return port.getDetail(customerID, file);
    }

}
