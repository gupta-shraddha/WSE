package Servlet;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import Miscellaneous.adInfo;
import org.apache.commons.lang3.StringUtils;
import sun.rmi.runtime.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static java.lang.System.out;


/**
 * Created by gupta on 01.02.2017.
 */
//@WebServlet(name = "AdSave")
public class AdSave extends HttpServlet {

    protected void processRegister(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            try {
                String url= request.getParameter("ad_url");
                String username=request.getParameter("cust");
                double budget=Double.parseDouble(request.getParameter("budget"));
                double costPerClick = 0.2;
                String description=request.getParameter("desc");
                String clickURL;
                String image=request.getParameter("ad_img_url");
                String ngrams=request.getParameter("ngrams");
                boolean registration=false;
                //To register a ad into database
                if(image!=null) {
                    registration = adInfo.register(username, ngrams, url, description, budget, costPerClick,image);
                }
                else{
                    registration = adInfo.register(username, ngrams, url, description, budget, costPerClick);
                }
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Registration Status</title>");
                    out.println("<style>\n" +
                            "</style>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<CENTER>");
                    if (registration==true) {
                        out.println("<h1>Your Ad has been registered.</h1>");
                    }
                    else{
                        out.println("<h1>Some Error Occurred.</h1>");
                    }
                    out.println("</CENTER>");
                    out.println("</body>");
                    out.println("</html>");

            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                out.close();
            }
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRegister(request,response);
    }
}
