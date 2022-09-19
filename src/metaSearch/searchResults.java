package metaSearch;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.naming.directory.SearchResult;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by gupta on 06.02.2017.
 */
public class searchResults  {
    private int id;
    private String url;
    private String seUrl;
    private int cw ;
    private int c;
    private double avg_cw;
    private double score;
    private double minScore;
    private double maxScore;

    private HashMap<String,Term> terms;

    private JSONObject json;



    public searchResults(int id, String url, int cw,String seUrl) {
        this.id = id;
        this.url = url;
        this.cw = cw;
        this.seUrl=seUrl;
        terms = new HashMap<>();

    }

    public searchResults(int id, String url,String seUrl) {
        this(id,url,-1,seUrl);
    }

    public int getCw() {
        return cw;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void setAvg_cw(double avg_cw) {
        this.avg_cw = avg_cw;
    }

    public void calculateScore()
    {
        score = 0.0;
        minScore = 0.0;
        maxScore = 0.0;
        for (Map.Entry<String, Term> stringTermEntry : terms.entrySet()) {
            score += stringTermEntry.getValue().getScore(c,cw,avg_cw);
            minScore += stringTermEntry.getValue().getScore(c,0.0);
            maxScore += stringTermEntry.getValue().getScore(c,1.0);
        }
    }

    public Set<String> getTerms()
    {
        Set<String> ret = new HashSet<>();
        for (Map.Entry<String, Term> stringTermEntry : terms.entrySet()) {
            ret.add(stringTermEntry.getKey());
        }
        return ret;
    }

    public void setTermCf(String term,int cf){
        Term t = terms.get(term);
        if(t != null)
            t.setCf(cf);
    }

    public void scanResults() {

        try {
            Scanner scanner = new Scanner(new URL(seUrl).openStream(), "UTF-8").useDelimiter("\\A");
            String out;
            if (scanner.hasNext()) {
                out = scanner.next();

                json = (JSONObject) JSONValue.parse(out);
                JSONArray stat = (JSONArray) json.get("stat");
                Iterator<JSONObject> iterator = stat.iterator();
                while (iterator.hasNext()) {
                    JSONObject obj = iterator.next();
                    String term = (String) obj.get("term");
                    int df = ((Long)obj.get("df")).intValue();
                    Term value = new Term(term, df);
                    terms.put(term, value);
                    value.save(id);
                }
                cw = ((Long)json.get("cw")).intValue();
                save();
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        Connection con = DBConnection.getConnection();
        try{
            PreparedStatement stmt = con.prepareStatement("UPDATE " + DBSetup.T_MetaSearch  +" SET "+DBSetup.Col_cw+" = ?" +
                    " WHERE " + DBSetup.Col_id + "=?" );

            stmt.setInt(1,cw);
            stmt.setInt(2,id);
            stmt.execute();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) { }
        }

    }

    public double getNormScore() {
        return (score-minScore)/(maxScore-minScore);
    }

    public List<ResultMeta> getResults(){
        List<ResultMeta> ret = new ArrayList<>();
        try {
            JSONArray resultList = (JSONArray) json.get("resultList");
            Iterator<JSONObject> iter = resultList.iterator();
            while (iter.hasNext()) {
                JSONObject obj = iter.next();
                int rank = ((Long) obj.get("rank")).intValue();
                String url1 = (String) obj.get("url");
                double score = (Double) obj.get("score");
                ResultMeta e = new ResultMeta(rank, url1, score);
                e.setColScore(getNormScore());
                e.setEngineURL(seUrl);
                ret.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }



}
