package Servlet;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import metaSearch.configure;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by gupta on 03.02.2017.
 */
@WebServlet(name = "Config")
public class Config extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<String> strList = new ArrayList<>();
            String url = request.getParameter("url");
            Boolean act = Boolean.parseBoolean(request.getParameter("active"));
            strList.add(url);

            configure.saveEngine(url,act);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            out.close();
        }
        response.sendRedirect("configure.jsp");
    }

}
