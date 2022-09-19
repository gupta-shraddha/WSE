package Servlet;

import metaSearch.ResultMeta;
import metaSearch.configure;
import metaSearch.metaSearchProcess;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gupta on 01.02.2017.
 */
@WebServlet(name = "MetaDisplay")
public class MetaDisplay extends HttpServlet {

    protected void processMetaRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        //response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* output page. */
            try {
                String query=request.getParameter("query");
                int noOfDoc=  Integer.parseInt(StringUtils.isEmpty(request.getParameter("k"))?"10":request.getParameter("k"));
                int scoreMethod=Integer.parseInt(StringUtils.isEmpty(request.getParameter("score"))?"1":request.getParameter("score"));
                List<String> terms = new ArrayList<>();
                String[] splittedTerms = query.split(" ");
                for(String s: splittedTerms){
                    terms.add(s);
                }
                metaSearchProcess msp = new metaSearchProcess(terms);
                List<ResultMeta> rm= msp.getResults(query,noOfDoc,scoreMethod);
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Results Servlet.Display</title>");
                out.println("<style>\n" +
                        "table {\n" +
                        "    font-family: arial, sans-serif;\n" +
                        "    border-collapse: collapse;\n" +
                        "    width: 100%;\n" +
                        "}\n" +
                        "\n" +
                        "td, th {\n" +
                        "    border: 1px solid #dddddd;\n" +
                        "    text-align: left;\n" +
                        "    padding: 8px;\n" +
                        "}\n" +
                        "\n" +
                        "tr:nth-child(even) {\n" +
                        "    background-color: #dddddd;\n" +
                        "}\n" +
                        "</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Search Results here: </h1>");
                out.println("<table>");
                out.println("  <tr>\n" +
                        "    <th>Rank</th>\n" +
                        "    <th>Link</th>\n" +
                        "<th>From Engine</th> " );
                out.println("  </tr>");
                int i=0;
                for(ResultMeta item:rm){
                    int r=item.getEngineURL().lastIndexOf("json") ;
                    out.println(" <tr>\n");
                    out.println("<td>" + item.getRank() + "</td>");
                    out.println("<td>  "+"<a href="+ "\"" + item.getUrl() + "\">" +item.getUrl() +"</a>"+ "\n" + "</td>");
                    out.println("<td>" + item.getEngineURL().substring(0,r) + "</td>");
                    out.println("</tr");
                    i++;
                    if(i>noOfDoc-1){
                        break;
                    }
                }
                out.println("</table>");
                out.println("</body>");
                out.println("</html>");
            }catch(Exception e) {
                System.err.println(e.getMessage());
            }finally{
                out.close();
            }
            //RequestDispatcher rd = request.getRequestDispatcher(jsonurl);
            //rd.forward(request,response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processMetaRequest(request, response);
    }
}
