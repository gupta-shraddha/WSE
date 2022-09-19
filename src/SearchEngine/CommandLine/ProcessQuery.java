package SearchEngine.CommandLine;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import IndexerParser.QueryParser;
import Miscellaneous.QueryResultItems;
import Miscellaneous.Snippets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Zaryab on 11/13/2016.
 */
public class ProcessQuery {
    private String sqlQuery;
    private String queryKW;
    private List<QueryResultItems> resultItems;
    private final String TFIDF = DBSetup.Col_tfIdf;
    private final String BM25 = DBSetup.Col_bm25;
    private final String BM25_Pagerank = DBSetup.Col_bm25_pagerank;
    private final String V_TFIDF = DBSetup.View_Features_tfidf;
    private final String V_BM25 = DBSetup.View_Features_bm25;
    private final String V_BM25_Pagerank = DBSetup.View_bm25_pagerank;

    private String ScoreStandard = null;
    private String View_ScoreStandard = null;
    //--------------------------

    public String getSql() {
        return sqlQuery;
    }

    public void setSql(String sql) {
        this.sqlQuery = sql;
    }

    public void setResultItems(List<QueryResultItems> resultItems) {
        this.resultItems = resultItems;
    }

    public List<QueryResultItems> getResultItems() {
        return resultItems;
    }

    public ProcessQuery() {
        ScoreStandard = BM25_Pagerank;
        View_ScoreStandard=V_BM25_Pagerank;
    }
    public ProcessQuery(String Standard) {
        ScoreStandard = Standard;
        if(ScoreStandard.equals(TFIDF))
            View_ScoreStandard=V_TFIDF;
        else if(ScoreStandard.equals(BM25))
            View_ScoreStandard=V_BM25;
        else if(ScoreStandard.equals(BM25_Pagerank))
            View_ScoreStandard=V_BM25_Pagerank;
    }

    public void search(String query, int resultSize, boolean queryType) {
        performSearch(DBConnection.getConnection(), query, resultSize, queryType,"en",false);
    }
    public void search(String query, int resultSize, boolean queryType,String language) {
        performSearch(DBConnection.getConnection(), query, resultSize, queryType,language,false);
    }
    public void search(String query, int resultSize, boolean queryType,String language,boolean hasImage) {
        performSearch(DBConnection.getConnection(), query, resultSize, queryType,language,hasImage);
    }
    public void search(String db, String query, int resultSize, boolean queryType) {
        performSearch(DBConnection.getConnection(db), query, resultSize, queryType ,"en",false);
    }
    public void search(String db, String query, int resultSize, boolean queryType,String language) {
        performSearch(DBConnection.getConnection(db), query, resultSize, queryType ,language,false);
    }
    public void search(String db, String query, int resultSize, boolean queryType,String language,boolean hasImage) {
        performSearch(DBConnection.getConnection(db), query, resultSize, queryType ,language,hasImage);
    }


    private void performSearch(Connection conn, String query, int resultSize, boolean queryType, String language,boolean hasImage) {
        queryKW=query;
        QueryParser qp = new QueryParser(query.toLowerCase(),language); // seperates site: quote stemm unstemm
        List<String> queryTerms_S = qp.getStemmedTerms(); //
        List<String> queryTerms_U = qp.getUnStemmedTerms();//fix this
        if(queryTerms_S.size()==0)
        { //for german words consider Unstemmed words :D
            queryTerms_S=queryTerms_U;
        }
        if (qp.getTermsCount() > 0) {
            ResultSet resultSet = null;
            String site_Quote_Syn = qp.getWhereClause(); // brings site: and " ____ " in a where clause.
            resultSet = queryType ? Conjunctive(conn, queryTerms_S, resultSize, site_Quote_Syn,language,hasImage) : Disjunctive(conn, queryTerms_S, resultSize, site_Quote_Syn,language,hasImage);
            if (resultSet != null) {
                resultTemplate(resultSet, resultSize,queryTerms_U,hasImage);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resultTemplate(ResultSet rs, int resultSize,List<String> queryTerms_U,boolean hasImage) {
        List<QueryResultItems> resultList = new ArrayList<QueryResultItems>();
        try
        {
            if(!hasImage)
            {int i = 0;
                while (rs.next() && i < resultSize)
                {
                    QueryResultItems result = new QueryResultItems();
                    result.setUrl(rs.getString("url"));
                    result.setScore(rs.getDouble("score"));
                    result.setSnippet(Snippets.createSnippet(rs.getString("content"), queryTerms_U));
                    result.setTitle(rs.getString("title"));
                    result.setRank(++i);
                    resultList.add(result);
                }
                setResultItems(resultList);
            }
            else
            {   int i = 0;
                Connection conn= DBConnection.getConnection();
                while(rs.next())
                {
                    PreparedStatement ps = conn.prepareStatement("Select * from " + DBSetup.T_Images + " WHERE doc_id = " + rs.getString("doc_id"));
                    ResultSet rsImage = ps.executeQuery();
                    while (rsImage.next()) {
                        QueryResultItems resultImagesItem = new QueryResultItems();
                        resultImagesItem.setUrl(rs.getString("url"));
                        resultImagesItem.setScore(rs.getDouble("score"));
                        resultImagesItem.setPageContent(rs.getString("content"));
                        resultImagesItem.setSnippet(Snippets.createSnippet(rs.getString("content"), queryTerms_U));
                        resultImagesItem.setTitle(rs.getString("title"));

                        resultImagesItem.setImg(rsImage.getBytes("image"));
                        resultImagesItem.setPosition(rsImage.getInt("position"));
                        resultImagesItem.setPageIndex(rsImage.getInt("index"));
                        resultImagesItem.setType(rsImage.getString("type"));
                        resultImagesItem.setAlt(rsImage.getString("alt"));
                        resultImagesItem.setSrc(rsImage.getString("src"));
                        resultImagesItem.calculateImgScore(queryTerms_U);
                        if(resultImagesItem.getImgScore()<=0) // if document has term but image vicinity doesn't ..ignore the document.
                        {
                            continue;
                        }
                        else
                        {
                            resultImagesItem.setRank(++i);
                            resultList.add(resultImagesItem);
                        }
                    }
                    Collections.sort(resultList, Comparator.comparingDouble(QueryResultItems::getImgScore));
                }
                Collections.reverse(resultList); // highest image score on 1st position.
                if(resultList.size()>resultSize)
                {
                    resultList.subList(resultSize, resultList.size()).clear(); // scrutinize the list}
                }
                else
                {
                    while (resultList.size() > resultSize) {
                        resultList.remove(resultList.size() - 1);
                    }
                }
                setResultItems(resultList); //add to total result
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet Conjunctive(Connection con, List<String> terms, int resultSize, String site_Quote_Syn, String language,boolean hasImage)
    {
        int termCount = terms.size();
        String keywords = " ";
        String sql="";
        if (termCount > 0)
        {
            String termID = "";
            for (String s : terms)
            {
                termID += s.hashCode() + ",";
            }
            keywords = " AND f."+DBSetup.Col_termId + " in (" + termID.substring(0, termID.length() - 1) + ") ";
        }
        try {
            if(!hasImage)
            {
                sql = "SELECT d." + DBSetup.Col_docId + ", SUM(f." + ScoreStandard + ") as score, d." + DBSetup.Col_url + ", d." + DBSetup.Col_content + ", d." + DBSetup.Col_pageTitle + " from " + DBSetup.T_Features + " f, " + DBSetup.T_Documents + " d " +
                        " WHERE f." + DBSetup.Col_docId + " = d." + DBSetup.Col_docId + " " + keywords + " " + site_Quote_Syn + " and d." + DBSetup.Col_language + "= ?" +
                        " GROUP BY d." + DBSetup.Col_docId +" HAVING count(f." + DBSetup.Col_termId + ") = ? " +
                        " Order BY score desc LIMIT ?";
            }else
            {   // bring all those documents that has the following terms and that document must have the image also ..
                // Change to    //Inner Join (select distinct doc_id from images )img on  d.doc_id = img.doc_id // consider score based on individual image
                sql="Select d." + DBSetup.Col_docId + ", SUM(f." + ScoreStandard + ") as score, d." + DBSetup.Col_url + ",d." + DBSetup.Col_content + ", d." + DBSetup.Col_pageTitle + " from " + DBSetup.T_Features + " f ,  " + DBSetup.T_Documents + " d " +
                        "Inner Join (select distinct doc_id from " + DBSetup.T_Images+" )img on  d." + DBSetup.Col_docId +" = img." + DBSetup.Col_docId +
                        " WHERE f." + DBSetup.Col_docId + " = d." + DBSetup.Col_docId + " " + keywords + " " + site_Quote_Syn + " AND " + "d." + DBSetup.Col_language + "= ?" +
                        " GROUP BY d." + DBSetup.Col_docId +" " +" HAVING count(f." + DBSetup.Col_termId + ") = ? " +
                        " Order BY score desc ";


            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, language);
            ps.setInt(2, termCount);
            if(!hasImage)
                ps.setInt(3, resultSize);
            setSql(ps.toString());
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResultSet Disjunctive(Connection con, List<String> terms, int resultSize, String site_Quote_Syn,String language, boolean hasImage)
    {
        int termCount = terms.size();
        String keywords = " ";
        String sql="";
        if (termCount > 0)
        {
            String termID = "";
            for (String s : terms) {
                termID += s.hashCode() + ",";
            }
            keywords = " AND f."+DBSetup.Col_termId + " in (" + termID.substring(0, termID.length() - 1) + ") ";
        }
        try
        {
            // ~syn will give zero result don't try syn here
            if(!hasImage)
            {
                sql = "SELECT d." + DBSetup.Col_docId + ", SUM(f." + ScoreStandard + ") as score, d." + DBSetup.Col_url + ", d." + DBSetup.Col_content + ", d." + DBSetup.Col_pageTitle + " from " + DBSetup.T_Features + " f ,  " + DBSetup.T_Documents + " d " +
                        "WHERE f." + DBSetup.Col_docId + " = d." + DBSetup.Col_docId + " " + keywords + " " + site_Quote_Syn + " AND " + "d." + DBSetup.Col_language + "= ?" +
                        " GROUP by d." + DBSetup.Col_docId + " ORDER by score desc LIMIT ?";
            }else
            {   // bring all those documents that has the following terms and that document must have the image also ..
                //Change to    //Inner Join (select distinct doc_id from images )img on  d.doc_id = img.doc_id // consider score based on individual image
                sql="Select d." + DBSetup.Col_docId + ", SUM(f." + ScoreStandard + ") as score, d." + DBSetup.Col_url + ",d." + DBSetup.Col_content + ", d." + DBSetup.Col_pageTitle + " from " + DBSetup.T_Features + " f ,  " + DBSetup.T_Documents + " d " +
                        "Inner Join (select distinct doc_id from " + DBSetup.T_Images+" )img on  d." + DBSetup.Col_docId +" = img." + DBSetup.Col_docId +
                        " WHERE f." + DBSetup.Col_docId + " = d." + DBSetup.Col_docId + " " + keywords + " " + site_Quote_Syn + " AND " + "d." + DBSetup.Col_language + "= ?" +
                        " Group by d." + DBSetup.Col_docId + " Order by score desc ";
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, language);
            if(!hasImage)
                ps.setInt(2, resultSize);
            setSql(ps.toString());
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
