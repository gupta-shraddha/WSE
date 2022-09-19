import Crawler.Crawler;
import DBPostgres.DBSetup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WSE_Main {
    public static void main(String[] args) throws IOException, SQLException
    {
        DBSetup db= new DBSetup();
        db.SetupDB();
        Crawler cr=new Crawler();


        List<String> urls= new ArrayList<>();
        urls.add("http://soccerisma.com");
        urls.add("http://dbis.informatik.uni-kl.de");
        cr.setUrls(urls);
        cr.setLevDomain(false);
        cr.setMaxDepth(3);
        cr.setMaxDoc(100*urls.size());
        cr.Crawl();
    }
    private static String []Urls=null;
    public static String getHtmlUrl() {
        String HtmlLink = null;
        boolean flag=false;
        BufferedReader input;
        try {
            do {
                input = new BufferedReader(new InputStreamReader(System.in));
                HtmlLink = input.readLine();

                if (isValidURL(HtmlLink)) {
                    System.out.println(HtmlLink);
                    flag=true;
                } else {
                    System.out.println("Please enter correct URL (http://)");
                }
            }while(flag!=true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            return HtmlLink;
        }
    }

    public static boolean isValidURL(String url) {

        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }



}

