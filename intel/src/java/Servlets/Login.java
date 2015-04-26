/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servlets;

import Models.UserTransaction;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import dataAnalyticsModel.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author leon
 */
@WebServlet(name = "login", urlPatterns = {"/userLogin"})
public class Login extends HttpServlet {

    private TestSummary summary;
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
        
        String userName = request.getParameter("id");
        String pw = request.getParameter("pw");
        UserTransaction t = new UserTransaction();
        
        //validate user and password
        if(t.validateUser(userName, pw)){
            //Set session for the valid user
            request.getSession().setAttribute("validUser", userName);
            request.getSession().setAttribute("folders", t.getFolder(userName));
            xstream.alias("testSummary", TestSummary.class);
            xstream.alias("testProduct", TestProduct.class);
            xstream.alias("testBrief", TestBrief.class);
            summary = (TestSummary) xstream.fromXML(getSummary(userName));
            request.getSession().setAttribute("summary", summary);
            response.sendRedirect("index.jsp");
        }
        else {
            //Send the user back to Login page with message
            request.setAttribute("validate", "false");
            RequestDispatcher view = request.getRequestDispatcher("login.jsp");
            view.forward(request, response);
        }
    }

    private String getSummary(String id) {
        String xml = "";
        File f = new File("/Users/leon/Desktop/Capstone/capstone/intel/summary.xml");
        try {
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                xml = xml.concat(s.nextLine());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
        }
        return xml;
    }

}
