package Servlet;

import metaSearch.configure;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.System.out;

/**
 * Created by gupta on 03.02.2017.
 */
@WebServlet(name = "ConfigDelete")
public class ConfigDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String value = request.getParameter("delete");
            System.out.println(value);
            configure conf = new configure();
            //conf.delete();
        }catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            out.close();
        }
        response.sendRedirect("configure.jsp");
    }
}
