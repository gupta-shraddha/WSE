package Crawler;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import IndexerParser.Indexer;
import LinksInfo.UrlMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.Thread.currentThread;
import static org.jcp.xml.dsig.internal.dom.Utils.readBytesFromStream;

/**
 * Created by Zaryab on 11/4/2016.
 */
public class CrawlerMultiThread implements Runnable {
    //-----------getter/setter---------
    public static int getMaxDepth() {
        return maxDepth;
    }
    public static void setMaxDepth(int maxDepth) {
        CrawlerMultiThread.maxDepth = maxDepth;
    }
    public static int getMaxDoc() {
        return maxDoc;
    }
    public static void setMaxDoc(int maxDoc) {
        CrawlerMultiThread.maxDoc = maxDoc;
    }
    public static boolean isLevDomain() {
        return levDomain;
    }
    public static void setLevDomain(boolean levDomain) {
        CrawlerMultiThread.levDomain = levDomain;
    }

    protected static int maxDepth =3 ;
    protected static int maxDoc=100;
    protected static boolean levDomain ;
    private Connection conn;
    static volatile int docCount = 0;


    public CrawlerMultiThread(int depth,int doc,boolean leavedomain)
    {
        conn = DBConnection.getConnection(); //each instance of thread will get its own connection.
        setMaxDoc(depth);
        setMaxDoc(doc);
        setLevDomain(leavedomain);

    }
    @Override
    public void run()// each thread has to pop from queue and then parse the HTML and push external links to the queue ............
    {
        UrlMetaData currWebLink= null;
        //docCount starts from 0
        //Sequence starts from 1
        //in case stream doesnt
        do {
            if(docCount<maxDoc) {
                boolean siteInQueue = false;
                currWebLink = new UrlMetaData();
                try {
                    siteInQueue = currWebLink.PopFromProcessQueue(conn); // if queue has a site it returns true & initialize currWebLink
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        conn.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    DecrementSequence(conn); // reduces sequences by 1
                    continue;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (!siteInQueue) {
                    currWebLink = null;
                    continue; // iterate
                }

                URL url = currWebLink.getUrl();
                if (url.equals(null)) {
                    continue;
                }
                System.out.println("Fetched: " + url.toString() + " from Queue by " + currentThread().getName());
                String rawHtml = null;
                Document docXHtml = null;
                try {
                    rawHtml = getRawHtml(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                if (rawHtml == null) {
                    continue;
                }
                try {
                    docXHtml = getXhtmlDoc(rawHtml);
                } catch (Exception e) {
                    System.out.println(" JTIDY unable to Convert to XHTML");
                    continue;
                }
                System.out.println("Documents data fetched from queue :"+currWebLink.getDocNum() + " / " + docCount + ": Documents stored in DB \t");

                if (fetchImages(currWebLink, currWebLink.getUrl(), docXHtml)&& docCount<maxDoc) {
                    System.out.println("Images fetched from " + currWebLink.getUrl() + " and Stored in Images Table by " + currentThread().getName()); // later put this is log.
                } else {
                    continue;
                }

                if (insert2DocTable(conn, currWebLink) && docCount<maxDoc) {
                    docCount++;
                    Indexer indexer = new Indexer(rawHtml.toLowerCase(), currWebLink.getDocId());
                    System.out.println("Successfully Indexed  : " + url.toString() + " : " + indexer.startIndexing() + " by " + currentThread().getName());
                } else {
                    continue;
                }
                if (fetchExternalLinks(currWebLink, currWebLink.getUrl(), docXHtml)) {
                    System.out.println("External Link fetched from " + currWebLink.getUrl() + " and Stored in Links Table by " + currentThread().getName()); // later put this is log.
                } else {
                    continue;
                }


                try {
                    currentThread().sleep(300); // helps in thread join while exiting run()
                } catch (InterruptedException e) {
                    continue;
                }
            }

        }while((docCount<maxDoc) && currWebLink !=null && currWebLink.getDocDepthlvl()<=maxDepth);
        try {
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static Document getXhtmlDoc(String html)
    {
        Tidy tidy= newTidy();
        Writer out = new StringWriter();
        PrintWriter dummyOut = new PrintWriter(out);
        tidy.setErrout(dummyOut);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = tidy.parseDOM(inputStream, null);
        return doc;
    }

    public String getRawHtml(URL url ) throws IOException
    {
        StringBuffer bf= null;
        try {
            bf = new StringBuffer();
            BufferedReader in= new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine=null;
            while ((inputLine = in.readLine()) != null) {
                bf.append(inputLine + "\n");
            }
            in.close();
        } catch (IOException e) {
            System.out.println("unable to open stream on "+url.toString()+" by "+currentThread().getName());
            return null;
        }
        return bf.toString().trim();
    }
    public boolean insert2DocTable(Connection conn,UrlMetaData currWebLink)
    {
        boolean flag=false;
        try {

            String Query="INSERT INTO "+ DBSetup.T_Documents + "(" + DBSetup.Col_docId + "," + DBSetup.Col_url + ") VALUES (?,?) " +
            "ON CONFLICT(" + DBSetup.Col_docId + ") DO UPDATE SET " + DBSetup.Col_crawledOnDate + " = EXCLUDED." + DBSetup.Col_crawledOnDate+";";
            PreparedStatement stmt = conn.prepareStatement(Query);
            stmt.setInt(1,currWebLink.getDocId());
            stmt.setString(2,currWebLink.getUrl().toString());
            stmt.execute();
            conn.commit();
            flag=true;
        } catch (SQLException e) {
            e.printStackTrace();
            try
            {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            DecrementSequence(conn);
            flag=false;
        }
        return flag;
    }
    public boolean fetchImages(UrlMetaData currWebLink,URL url, Document docXHtml)
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = null;
        try {
            nodes = (NodeList)xpath.compile("//img").evaluate(docXHtml, XPathConstants.NODESET);//all links in nodes
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
            return false;
        }
        return insert2ImagesTable(nodes,url,currWebLink); //push external links to queue and LINKS table

    }
    private boolean insert2ImagesTable(NodeList nodes ,URL currUrl,UrlMetaData webLink)
    {
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                Node n = nodes.item(i);
                String src = n.getAttributes().getNamedItem("src").getNodeValue();
                String alt =n.getAttributes().getNamedItem("alt") != null?n.getAttributes().getNamedItem("alt").getNodeValue():"" ;
                URL imgSrc = new URL(currUrl, src);
                if(imgSrc == null){
                    continue;
                }
                byte[] arr = null;
                try(InputStream is = imgSrc.openStream()){
                    arr = readBytesFromStream(is);
                }catch(IOException ex){
                    System.out.println("Unable to open image from "+imgSrc.toString()+" by "+currentThread().getName());
                    continue;
                }
                String imgType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(arr));
                if(imgType != null && !imgType.contains("image")) {
                    continue;
                }// if content is not of type image/png jpeg etc..... skip

                PreparedStatement stmt =  conn.prepareStatement("INSERT INTO " + DBSetup.T_Images + " VALUES(?,?,?,?,?,?)");
                stmt.setInt(1,webLink.getDocId()); //doc_Id
                stmt.setInt(2,i); //Index of Image on Page
                //default position -1 will be updated while indexing
                stmt.setString(3,imgSrc.toString()); //link of image
                stmt.setString(4,alt); //Alt Text
                stmt.setString(5,imgType);
                stmt.setBytes(6,arr); //store image in binary format
                stmt.execute();
                conn.commit();
            } catch (IOException|NullPointerException e) {
                continue;
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                continue;
            }
        }
        return true;
    }
    public boolean fetchExternalLinks(UrlMetaData currWebLink,URL url, Document docXHtml)
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = null;
        try {
            nodes = (NodeList)xpath.compile("//a/@href").evaluate(docXHtml, XPathConstants.NODESET);//all links in nodes
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
            return false;
        }
        return pushToQueue_LinksTable(nodes,url,currWebLink); //push external links to queue and LINKS table

    }
    private boolean pushToQueue_LinksTable(NodeList nodes ,URL currUrl,UrlMetaData webLink)
    {
        try
        {
            for(int i = 0; i<nodes.getLength();i++)
            {
                Node n = nodes.item(i);
                String link = n.getNodeValue(); //Get the href value from <a> nodes
                URL url = null;
                try {
                    url = webLink.formatURL(link.toLowerCase(),currUrl); // node url plus currentUrl for context
                    if(webLink.scrutinizeUrl(url))
                    {
                        //Check if the URL should be ignored
                        UrlMetaData childSite = new UrlMetaData(url, webLink.getDocDepthlvl() + 1);
                        // set the depth level of the external link w.r.t to current webLink object
                        if(childSite.getDocDepthlvl() <= maxDepth&&(childSite.checkDomain(webLink) || levDomain ))
                        {   //see if the link is not at the depth level restriction //Check Domain
                            childSite.PushToProcessQueue(conn); //Save website to queue
                            try { //
                                insert2LinksTable(conn,webLink.getDocId(),childSite.getDocId());
                            } catch (SQLException e)
                            {
                                conn.rollback();
                            }
                        }
                    }
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException|NullPointerException e){
        return false;
        } //Happens rarely because of a Bug in XPath/Jtidy
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void insert2LinksTable(Connection conn,int from_doc ,int to_doc) throws SQLException
    {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO "
                + DBSetup.T_Links + "(" + DBSetup.Col_fromDocId + "," + DBSetup.Col_toDocId + ") "
                + "VALUES (?,?) ON CONFLICT DO NOTHING ");
        stmt.setInt(1, from_doc);
        stmt.setInt(2, to_doc);
        stmt.execute();
        conn.commit();

    }

    private void DecrementSequence(Connection conn){
        try{
            conn.createStatement().execute("ALTER SEQUENCE docs_Crawled INCREMENT BY -1");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static Tidy newTidy() {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setXHTML(true);
        tidy.setQuiet(true);
        tidy.setMakeClean(true);
        tidy.setSmartIndent(true);
        return tidy;
    }

}
