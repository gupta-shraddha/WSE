package Servlet;

import Algos_Compute.Levenshtein;
import DBPostgres.DBSetup;
import Miscellaneous.QueryResultItems;
import Miscellaneous.adInfo;
import SearchEngine.CommandLine.ProcessQuery;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Display extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* output page. */
            try {
                String query=request.getParameter("query");
                int noOfDoc=20;
                noOfDoc=  Integer.parseInt(StringUtils.isEmpty(request.getParameter("k"))?"10":request.getParameter("k"));

                int scoreMethod=Integer.parseInt(StringUtils.isEmpty(request.getParameter("score"))?"1":request.getParameter("score"));

                String language = StringUtils.isEmpty(request.getParameter("language"))?"en":request.getParameter("language");

                int image = Integer.parseInt(StringUtils.isEmpty(request.getParameter("image"))?"0":(request.getParameter("image")));

                String currentUrl= request.getRequestURI();
                //System.out.println( request.getScheme().toString()+"://" + request.getServerName().toString() +":"+ request.getServerPort() + request.getRequestURI().toString() + "?" + request.getQueryString().toString());

                List<adInfo> ads= new ArrayList<adInfo>();
                ads=adInfo.fetchAds(query);

                Levenshtein lv= new Levenshtein();
                HashMap<String,String> sug= lv.correctTypos(query,language);

                String queryWords="";
                for(Map.Entry<String,String> pos: sug.entrySet())
                {
                    queryWords=queryWords+pos.getValue()+" ";
                }
                queryWords=queryWords.trim();

                ProcessQuery pq=null;
                boolean queryType=false; // disjunctive
                if(scoreMethod==1) pq = new ProcessQuery(DBSetup.Col_tfIdf);
                if(scoreMethod==2) pq = new ProcessQuery(DBSetup.Col_bm25);
                if(scoreMethod==3) pq = new ProcessQuery(DBSetup.Col_bm25_pagerank);
                boolean hasImage = false;
                if(image==1) {
                    hasImage = true;
                }
                pq.search(query.toLowerCase(),noOfDoc,queryType,language,hasImage);
                List<QueryResultItems> results= pq.getResultItems();


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
                        ".img-style{width:100px;height:100px;}\n"+
                        "</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Search Results here: </h1>");

                out.println("<h2>Optimal Suggestions:"+ "<a href=" + "\"" + request.getScheme().toString()+"://" + request.getServerName().toString() +":"+ request.getServerPort() + request.getRequestURI().toString() + "?"+"query="+queryWords+"&score="+scoreMethod+"&k="+noOfDoc+"&image="+image+"&language="+language+"\""+ ">"+queryWords+"</a>"+"</h2>");
                if(ads.size()!=0) {
                    out.println("<table>");
                    for (adInfo Ad : ads) {
                        out.println(" <tr>\n");
                        out.println("<td>\n");
                        out.println("AD:");
                        out.println("</td>\n" + "    <td>");
                        out.println("<a href=/Servlet.ClickUpdate?id=" + Ad.getId() + "&url=" + Ad.getUrl() + ">" + Ad.getUrl() + "</a>" + "\n" + "</td>  <td>");
                        out.println(Ad.getDescription());
                        out.println("</td>\n");
                        if (Ad.getImage() != null) {
                            out.println("<td> <img class='img-style'" +
                                    "src=" + "\"" + Ad.getImage() +
                                    "\" alt=" + "\"" + "ad_image" + "\">" + "    </td>");
                        }
                        out.println("</tr");

                    }
                    out.println("</table>");
                }
                out.println("<table>");
                out.println("  <tr>\n" +
                        "    <th>Rank</th>\n" +
                        "    <th>Link</th>\n" +
                        "    <th>Snippets</th>\n" );
                if (hasImage)
                {
                    out.println("<th>Image</th>" );
                }
                out.println("  </tr>");
                int r=0;
                for(QueryResultItems item:results){
                    out.println(" <tr>\n");
                    out.println("<td>\n");
                    out.println(++r);
                    out.println("</td>\n" +"    <td>");
                    out.println("<a href="+ "\"" + item.getUrl() + "\">" +item.getUrl() +"</a>"+ "\n" + "</td>  <td>");
                    out.println(item.getSnippet());
                    out.println("</td>\n" );
                    if(hasImage) {
                        String url = "data:" + item.getType() + ";base64," + Base64.encode(item.getImg());
                        out.println("<td> <img class='img-style' src=" + "\"" + url + "\" alt=" + "\"" + item.getAlt() + "\">" + "    </td>");
                    }
                    out.println("</tr");

                }
                out.println("</table>");
                out.println("</body>");
                out.println("</html>");
            }catch(Exception e) {
                System.err.println(e.getMessage());
            }finally{
                out.close();
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

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
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
