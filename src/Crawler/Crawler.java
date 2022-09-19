package Crawler;

import Algos_Compute.Jaccard;
import Algos_Compute.Pagerank;
import Algos_Compute.Scores;
import Algos_Compute.Shingles;
import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import LinksInfo.UrlMetaData;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Crawler {
    private static String Url; // was using it for single url written using keyboard
    private static int maxDepth ;
    private static int maxDoc;
    private static boolean levDomain ;
    private static List<String> Urls; // for set of urls
    //--------------------getter/setter--------------


    public String getUrl() { return Url; }
    public void setUrl(String Url) { Crawler.Url = Url; }
    public int getMaxDepth() {return maxDepth;}
    public void setMaxDepth(int maxDepth) {    Crawler.maxDepth = maxDepth;}
    public int getMaxDoc() { return maxDoc;}
    public void setMaxDoc(int maxDoc) {        Crawler.maxDoc = maxDoc;}
    public boolean isLevDomain() {return levDomain;}
    public void setLevDomain(boolean levDomain) {        Crawler.levDomain = levDomain;}
    public List<String> getUrls() {     return Urls;}
    public void setUrls(List<String> urls) { Urls = urls;}

    //----------------------------------------------
    public static void main(String []args) throws SQLException // for console test
    {
//        Urls.add(args[0]);
//        maxDepth = Integer.parseInt(args[1]);
//        maxDoc = Integer.parseInt(args[2]);
//        levDomain = Boolean.parseBoolean(args[3]);

        //new Crawler().Crawl();
    }
    public void Crawl() throws SQLException {
        Connection conn = DBConnection.getConnection();
        pushReqQueue(conn); // Root links pushed in request Queue..... once its done fully

        boolean flag = checkReqQueue(conn); // checks whether all roots in Request_queue visited or not

        if(flag==false) {
            boolean seqGenereted = beginCrawler(conn); // initially no sequence
            //sequence generatioon helps in Queue crawling from the very position
            if (seqGenereted == false) // new sequence
            {
                defineInitialDepth(conn);// initial depth set to 0 for all roots and pushed to process queue.
            }
            StartMultiThreadedProcessing();

            if (getMaxDepth() >= 5) { // LATER: make it dynamic if for a link depth exceededs 10 update Visited = true.
                for (String s : getUrls()) {
                    updateReqQueue(conn, s);
                    System.out.println(s + " crawled on " + new Date());

                }
            }

        }
        Scores.computeIDF(conn);
        Scores.computeTFIDF(conn);
        Scores.computeBM25(conn);
        new Pagerank();
        Scores.computeBM25Pagerank(conn);
        DBSetup.createIndexes();
        new Shingles();
        new Jaccard();
    }

    public void StartMultiThreadedProcessing()
    {
        //Runtime.getRuntime().availableProcessors() gives available cores
        List<Thread> ThreadsList = new ArrayList<Thread>();
        int i=0;
        while(i<1) // default names threads: Thread-0, Thread-1 and Thread-2.....
        {
            Thread processThread = new Thread(new CrawlerMultiThread(maxDepth,maxDoc,levDomain));
            ThreadsList.add(processThread);
            processThread.start();
            try
            {
                Thread.sleep(1000); //next thread starts after 1 second
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }

        for (Thread thread : ThreadsList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public void defineInitialDepth(Connection conn) // define the depth of root links and store them into process queue
    {
        for (String s : getUrls()) { // get all URL from the list<string> sent to Crawler by main method
            try {
                UrlMetaData link = new UrlMetaData(new URL(s), 0); // 0: root link
                link.PushToProcessQueue(conn);//set of urls in List into Queue.
                conn.commit();
            } catch (SQLException | MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
    }
    public boolean checkReqQueue(Connection conn ) throws SQLException
    {
        boolean flag = true; // true means there are links that are not visited
        String query = " Select " + DBSetup.Col_visited+" From "+ DBSetup.T_RootsQueue +
                 " Where "+DBSetup.Col_url+" =? ";
        for (int i=1;i<getUrls().size();i++)
        {
            query += " UNION Select " + DBSetup.Col_visited+" From "+ DBSetup.T_RootsQueue +
                    " Where "+DBSetup.Col_url+" = ? ";
        }

        PreparedStatement ps = conn.prepareStatement(query+";");
        for (int j=0; j<getUrls().size();j++) {
        ps.setString(j+1,getUrls().get(j));
        }
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            if(rs.getBoolean("visited")==false);
            {
                flag= false;
                return flag;
            }
        }
        ps.close();


        return flag;
    }
    public void updateReqQueue(Connection conn, String url) throws SQLException {
        String query = "Update " + DBSetup.T_RootsQueue + " Set "
                + DBSetup.Col_visited+" = "+true+" Where "
                + DBSetup.Col_url +" =?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, url);
        ps.execute();
        ps.close();
        conn.commit();

    }

    public void pushReqQueue(Connection conn) throws SQLException {
        String query = "INSERT INTO " + DBSetup.T_RootsQueue + " ("
                + DBSetup.Col_docId + ", "
                + DBSetup.Col_depth + ", "
                + DBSetup.Col_leaveDomain + ", "
                + DBSetup.Col_maxDoc + ", "
                + DBSetup.Col_url + ")"
                + " values (?,?,?,?,?) ON CONFLICT  DO NOTHING";
        for(String s:getUrls()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, s.hashCode());
            ps.setInt(2, getMaxDepth());
            ps.setBoolean(3, isLevDomain());
            ps.setInt(4, getMaxDoc());
            ps.setString(5, s);
            ps.execute();
            ps.close();
        }
        conn.commit();
    }
    public boolean beginCrawler(Connection conn) throws SQLException
    {
        int i=1;
        boolean flag= false;
        try {
            Statement st = conn.createStatement();
            st.execute("CREATE SEQUENCE docs_Crawled START "+i+ "Increment by 1");// Automatically generates unique Integers
        } catch (SQLException e)
        {
            flag = true; // sequence already exists
        }
        conn.commit();
        return flag;
    }
    public void endCrawler(Connection con) throws SQLException
    {
        try {
            Statement stmt = con.createStatement();
            stmt.execute("DROP SEQUENCE docs_Crawled ");
            // stmt.execute("TRUNCATE docs_Crawled RESTART IDENTITY ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.commit();
        con.close();
    }


}