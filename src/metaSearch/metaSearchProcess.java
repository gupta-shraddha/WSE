package metaSearch;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import metaSearch.ResultMeta;
import metaSearch.Term;
import metaSearch.configure;
import metaSearch.searchResults;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.naming.directory.SearchResult;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class metaSearchProcess {
    private static final int k = 10;
    private static final double b = 0.4;
    List<String> terms;
    //String jsonurl="";
    List<searchResults> engines;

    public metaSearchProcess(List<String> terms) {
        this.terms = terms;
    }

    private void queryAllSearchengines(String query,Integer noOfDoc,Integer scoreMethod){
        ArrayList<searchResults> engines = new ArrayList<>();
        String jsonurl="";
        String strnew = query.replaceAll("\\s?[, ]\\s?","+");
        Connection con = DBConnection.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(" SELECT "+ DBSetup.Col_id+", "
                    +DBSetup.Col_url+", "
                    +DBSetup.Col_active +","
                    +DBSetup.Col_cw
                    + " FROM " + DBSetup.T_MetaSearch + " WHERE " + DBSetup.Col_active + "=TRUE ");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String url = rs.getString(DBSetup.Col_url);
                int id = rs.getInt(DBSetup.Col_id);
                int cw = rs.getInt(DBSetup.Col_cw);
                String[] firstpart = url.split("\\/");
                String lastPart = firstpart[firstpart.length - 1];
                String seUrl = url.replace(lastPart, "json?query=" + strnew + "&k=" + noOfDoc + "&score=" + scoreMethod);
                searchResults tmp;
                if(cw > 0)
                    tmp = new searchResults(id,query,cw,seUrl);
                else
                    tmp = new searchResults(id,query,seUrl);
                engines.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (SQLException ignored) {}
        }

        this.engines = engines;
    }



    public boolean getSearchengines(String query,Integer noOfDoc,Integer scoreMethod) {
        ArrayList<searchResults> engines = new ArrayList<>();
        Connection con = DBConnection.getConnection();

        String strnew = query.replaceAll("\\s?[, ]\\s?","+");
        try {
            String searchengineFunction = getSearchengineFunction();
            ResultSet rs = con.createStatement().executeQuery(searchengineFunction);
            while (rs.next()) {
                String url = rs.getString(DBSetup.Col_url);
                int id = rs.getInt(DBSetup.Col_id);
                int cw = rs.getInt(DBSetup.Col_cw);
                String[] firstpart = url.split("\\/");
                String lastPart = firstpart[firstpart.length - 1];
                String seUrl = url.replace(lastPart, "json?query=" + strnew + "&k=" + noOfDoc + "&score=" + scoreMethod);
                searchResults tmp;
                if (cw > 0)
                    tmp = new searchResults(id, query, cw,seUrl);
                else
                    tmp = new searchResults(id, query,seUrl);
                engines.add(tmp);
            }

            rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM " + DBSetup.T_MetaSearch);
            //if (rs.next()) calcChosen(rs.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
        if (engines.size() > 0) {
            this.engines = engines;
            return true;
        } else return false;
    }

    private void calculateScores(String query,Integer noOfDoc,Integer scoreMethod) {
        if (!getSearchengines(query,noOfDoc,scoreMethod)) {
            queryAllSearchengines(query,noOfDoc,scoreMethod);
        }
        for (searchResults engine : engines) {
            engine.setC(engines.size());
            engine.scanResults();
        }
        HashMap<String, Integer> terms = new HashMap<>();
        double avg_cw = 0.0;
        for (searchResults engine : engines) {
            for (String s : engine.getTerms()) {
                if (terms.containsKey(s)) {
                    int i = terms.get(s) + 1;
                    terms.put(s, i);
                } else
                    terms.put(s, 1);
            }
            avg_cw += engine.getCw();
        }
        avg_cw = avg_cw / engines.size();
        for (searchResults engine : engines) {
            engine.setAvg_cw(avg_cw);
            for (Map.Entry<String, Integer> entry : terms.entrySet()) {
                engine.setTermCf(entry.getKey(), entry.getValue());
            }
            engine.calculateScore();
        }

        //Collections.sort(engines);


    }



    public List<ResultMeta> getResults(String query,Integer noOfDoc,Integer scoreMethod) {
        calculateScores(query,noOfDoc,scoreMethod);
        ArrayList<ResultMeta> results = new ArrayList<>();
        for (searchResults engine : engines) {
            results.addAll(engine.getResults());
        }
        Collections.sort(results, ResultMeta.normalizedComparator());
        int i = 1;
        for (ResultMeta result : results) {
            result.rank = i;
            i++;
        }

        return results;
    }

    private String getSearchengineFunction() {

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String sep = "";
        for (String s : terms) {
            sb.append(sep).append(s.hashCode());
            sep = ",";
        }
        sb.append(")");
        String termHashes = sb.toString();

        String ret = "SELECT * FROM " +
                "(SELECT s."+DBSetup.Col_id+", s."+DBSetup.Col_url+", s."+DBSetup.Col_active+", s."+DBSetup.Col_cw+", SUM(score) AS score, " +
                "COUNT("+DBSetup.Col_hash+") AS terms "
                + "FROM "+DBSetup.T_MetaSearch+" s, "
                +        "("
                +                "SELECT s_s."+DBSetup.Col_id+", s_t."+DBSetup.Col_hash+", ("+b+" + (1-"+b+")* "
                +        "("+DBSetup.Col_df+"/("+DBSetup.Col_df+"+50+150*((1.0*"+DBSetup.Col_cw+")/"+DBSetup.Func_avg_cw+"())))* "
                +                "(log(("+DBSetup.Func_active_engine+"()+0.5)/"+DBSetup.Func_cf+"(s_t."+DBSetup.Col_hash+"))/log("+DBSetup.Func_active_engine+"()+1.0)) "
                +        ") AS score "
                + "FROM "+DBSetup.T_MetaSearch+" s_s, "+DBSetup.T_MetaSearch_terms+" s_t "
                + "WHERE s_s."+DBSetup.Col_id+" = s_t."+DBSetup.Col_id + " AND s_t." + DBSetup.Col_hash + " IN " +termHashes
                + ") AS scores " +
                "WHERE scores.score > " +b
                + " GROUP BY s."+DBSetup.Col_id +","+ DBSetup.Col_url+","+DBSetup.Col_active+","+DBSetup.Col_cw
                + " ORDER BY score) AS fin "
                + " WHERE score > "+b+"*terms"    ;
        return ret;
    }
/*
    public static void main(String[] args) {
        List<String> query = new ArrayList<>();
        query.add("tu");
        query.add("kaiserslautern");
        Metasearch ms = new Metasearch(query, 5);
        List<MetaSearchResultItem> results = ms.getResults();

    }*/
}