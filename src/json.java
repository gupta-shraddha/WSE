
import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import IndexerParser.QueryParser;
import Miscellaneous.QueryResultItems;
import SearchEngine.CommandLine.ProcessQuery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

/**
 * Created by gupta on 14.12.2016.
 */

public class json extends HttpServlet { static Map<String,Long> accessControl= new HashMap<>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try {
            PrintWriter out = response.getWriter();
            String query=request.getParameter("query");

            int noOfDoc=20;
            if(!request.getParameter("k").equals("")) {
                noOfDoc = Integer.parseInt(request.getParameter("k"));
            }
            int scoreMethod=Integer.parseInt(request.getParameter("score"));
            ProcessQuery pq=null;
            boolean queryType=false; // disjunctive
            if(scoreMethod==1){
                pq= new ProcessQuery(DBSetup.Col_tfIdf);
            }
            else if(scoreMethod==2) {
                pq= new ProcessQuery(DBSetup.Col_bm25);
            }
            else if(scoreMethod==3) {
                pq= new ProcessQuery(DBSetup.Col_bm25_pagerank);
            }
            int k= 20;

            pq.search(query.toLowerCase(),noOfDoc,queryType);
            List<QueryResultItems> results= pq.getResultItems();
            JSONObject responseJson=null;
            try {
                responseJson = CreateJSON(results,k,query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            out.println(responseJson.toJSONString().replaceAll("\\\\",""));

        } finally {
            out.close();
        }

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        if(!request.isSecure() || request.isSecure()) { // handle later

            try {
                long time = currentTimeMillis();
                String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (ip == null)
                    ip = request.getHeader("Remote_Addr");

                if (accessControl.containsKey(ip))
                {
                    long lastAccessTime = accessControl.get(ip);
                    if (time - lastAccessTime < 1000) {
                        response.getWriter().print("Cant place multiple request in 1 seconds");
                        return;
                    } else {
                        accessControl.remove(ip); // frees the accessControl list
                    }
                }
                // if request size in last 1 sec in more then 10 then skip this request
                if (accessControl.size() >= 10) {
                    response.getWriter().print("Too much access from one IP");
                    return;
                }
                accessControl.put(ip, time);
            } catch (Exception e) {
                e.printStackTrace();
            }

            processRequest(request, response);

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    private JSONObject CreateJSON(List<QueryResultItems>Results,int k,String querySearch) throws SQLException {

        JSONObject jsonDoc= new JSONObject();
        JSONArray resultList = new JSONArray();

        for(QueryResultItems item:Results)
        {
            JSONObject resultObject = new JSONObject();
            resultObject.put("rank", item.getRank());
            resultObject.put("url", item.getUrl());
            resultObject.put("score", item.getScore());
            resultList.add(resultObject);
        }

        jsonDoc.put("resultList",resultList);
        //------------------------------------------------------------------------
        JSONObject queryObj = new JSONObject();
        queryObj.put("k", k);
        queryObj.put("query", querySearch);
        jsonDoc.put("query",queryObj);
        //------------------------------------------------------------------------
        JSONArray stat = getStat(querySearch);
        jsonDoc.put("stat",stat);
        //----------------------------------------------------------------------
        int i=getCw();
        jsonDoc.put("cw" ,i);

        return jsonDoc;
    }
    public JSONArray getStat(String query) {
        Connection conn = DBConnection.getConnection();
        JSONArray statArray = new JSONArray();
        QueryParser qp = new QueryParser(query,"en");
        try {
            List<String> terms =qp.getUnStemmedTerms();
            terms.addAll(qp.getQuotedKeywords());
            for (String s :terms)
            {
                int df = 0;
                String sql="SELECT cast("+ DBSetup.Funct_DocTermCount+"("+s.hashCode()+") as int) AS Count";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                JSONObject object = new JSONObject();
                object.put("df", df = rs.next()?rs.getInt("Count"):0);
                object.put("term", s);
                statArray.add(object);
            }
            conn.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return statArray;
    }
    private int getCw () { // confusing query
        Connection conn = DBConnection.getConnection();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT sum("+DBSetup.Col_termFrequency+") as Count from Features AS Count");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getInt("Count");
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                conn.close();
            } catch
                    (SQLException e) {}
        }
        return -1;
    }

}
