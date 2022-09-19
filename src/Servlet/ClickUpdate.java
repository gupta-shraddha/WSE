package Servlet;

import Miscellaneous.adInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by gupta on 03.02.2017.
 */
@WebServlet(name = "ClickUpdate")
public class ClickUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        String url=request.getParameter("url");
        try {
            adInfo ad = new adInfo();
            ad.adClickUpdate(id);
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }finally{
            out.close();
        }
        response.sendRedirect(url);
    }
}
