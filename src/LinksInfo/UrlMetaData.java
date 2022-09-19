package LinksInfo;

import DBPostgres.DBSetup;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Zaryab on 11/4/2016.
 */
public class UrlMetaData {

    //------------getter/setters--------
    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getDocNum() {
        return docNum;
    }

    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    public String getUrlDomain() {
        return urlDomain;
    }

    public void setUrlDomain(String urlDomain) {
        this.urlDomain = urlDomain;
    }

    public URL getUrl() {
        return Url;
    }

    public void setUrl(URL url) {
        Url = url;
    }

    public int getDocDepthlvl() {
        return docDepthlvl;
    }

    public void setDocDepthlvl(int docDepthlvl) {
        this.docDepthlvl = docDepthlvl;
    }

    //------------------------------------------
    private int docId = 0;
    private int docNum = 0; //which # doc is this w.r.t CrawlRequest maxDoc allowed
    private String urlDomain = null;
    private URL Url = null;
    private int docDepthlvl = -1;

    public UrlMetaData(URL url ,int depth)
    {
        setUrl(url);
        setDocDepthlvl(depth);
        setDocId(url.hashCode()); // hash value generated from the link. unique for every link
        setUrlDomain(url.getHost());
        if(getUrlDomain().startsWith("www."))
        {
            setUrlDomain(getUrlDomain().substring(4));
        }
    }
    public UrlMetaData()
    {

    }
    public void PushToProcessQueue(Connection conn)
    {//Saves the Links ProcessQueue
        String query ="Insert into " + DBSetup.T_CrawlQueue + " ("
        + DBSetup.Col_docId + ", "
        + DBSetup.Col_depth + ", "
        + DBSetup.Col_url + ")"
        + " values (?,?,?)" +
                "On CONFLICT DO NOTHING " // nothing means don't insert
        + " RETURNING "+DBSetup.Col_docId+";";
        try
        {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, getDocId()); //save hashcode as ID
            stmt.setInt(2, getDocDepthlvl());
            stmt.setString(3, getUrl().toString());
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            if (rs.next())
            {
                docId = rs.getInt(1);
            }
            conn.commit();
        }
        catch(SQLException e)
        {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
   }
    public boolean PopFromProcessQueue(Connection conn)throws SQLException, MalformedURLException
    {
        synchronized (UrlMetaData.class) { // multiple thread not popping the same weblink from queue

            String Query = "UPDATE " + DBSetup.T_CrawlQueue +
                    " SET " + DBSetup.Col_visited + "= TRUE WHERE " + DBSetup.Col_docId +
                    " = (SELECT min(" + DBSetup.Col_docId + ") FROM " + DBSetup.T_CrawlQueue + " WHERE " + DBSetup.Col_visited + " = FALSE " +
                    "GROUP BY " + DBSetup.Col_depth + " ORDER BY " + DBSetup.Col_depth + " LIMIT 1)" +
                    "RETURNING " + DBSetup.Col_docId + " , " + DBSetup.Col_depth + " , " + DBSetup.Col_url + " , nextval('docs_Crawled') AS num ";

            PreparedStatement stmt = conn.prepareStatement(Query);
            stmt.execute();
            conn.commit();

            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                setDocId(rs.getInt(1));
                setDocDepthlvl(rs.getInt(2));
                setUrl(new URL(rs.getString(3)));
                setDocNum(rs.getInt(4));
                setUrlDomain(getUrl().getHost());
                if (getUrlDomain().startsWith("www.")) {
                    setUrlDomain(getUrlDomain().substring(4));
                }
                return true;
            } else return false;
        }
    }
    public boolean checkDomain(UrlMetaData web)
    {
        return this.urlDomain.equalsIgnoreCase(web.getUrlDomain());
    }

    public URL formatURL(String link,URL _url) throws MalformedURLException
    {
        String fragment = null;
        URI uri = null;
        URL url = new URL(_url, link); //Create the basic URL object

        try {
            uri = url.toURI(); //Translate it to a URI
            uri = uri.normalize(); //Normalize URI
            fragment = uri.getRawFragment(); //get fragment
        } catch (URISyntaxException e) {}

        if(uri != null)
            link = uri.toString();//Translate URI back to string for cleaning
        //Remove Fragment
        if( fragment != null && fragment.length()>0) //if URL has a fragment, remove it
            link = link.substring(0,link.indexOf("#"+fragment));
        //Remove last slash
        link = link.substring(0, link.length() - (link.endsWith("/") ? 1 : 0));
        //Create URL
        URL ret = new URL(link);
        return ret;
    }
    public boolean scrutinizeUrl(URL url)
    {
        boolean flag = false;
        if( url.getProtocol().equalsIgnoreCase("http")|| url.getProtocol().equalsIgnoreCase("https"))
        { flag= true;}
        else{ return false; }
      String file = url.getPath();
        String[] extension={"html","shtml","php","htm"};
        String[] tokens = file.split("/(?=[^/]+$)");
        if(tokens.length >= 1)
        {
            String fileName = tokens[tokens.length-1];
            String[] fileParts = fileName.split("\\.(?=[^\\.]+$)");
            if(fileParts.length >= 2) {
                String fileExt = fileParts[tokens.length-1];

                if(!(fileExt.equalsIgnoreCase(extension[0]) || fileExt.equalsIgnoreCase(extension[1])|| fileExt.equalsIgnoreCase(extension[2])
                        || fileExt.equalsIgnoreCase(extension[3])))
                    flag = false;
            }

        }
        return flag;
    }

}
