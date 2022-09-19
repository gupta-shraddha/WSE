package IndexerParser;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import Dictionary.WordsCollection;
import org.postgresql.util.PSQLException;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zaryab on 11/5/2016.
 */
public class Indexer {

    private static int docId=0;
    private static String htmlString=null;
    private static String textContent=null;
    private static String Language ="";


    public Indexer(String html, int id) // url hashcode
    {
        docId= id;
        htmlString=html;
    }
    public static void main(String args[])
    {
        System.out.println("------Metadata------Title,Desciption,Keywords");
        String sample= new SampleHTML().getStringHTML().toLowerCase();
        System.out.println(Arrays.toString(getMetaData(sample)));
        System.out.println("------Content------");
        String c=getHtmlTextContent(removeScriptStyles(sample));

        System.out.println(c+"\n"+c.length());
        System.out.println("------Language------");

        List<String> terms = getTerms(c);
        HashMap<String, Integer> tfreq_U=tfreq(terms); //frequency of Unstemmed to detect language
        Language=PageLanguageClassifier.identifyLanguage(tfreq_U); // from list calculate frequency
        System.out.println(Language);
        System.out.println("------Terms------");
        System.out.println(terms.toString());
        System.out.println("------Stopwords Removed------");
        terms= removeStopWords(terms,Language);
        System.out.println(terms.toString());
        if(Language=="en")
        {System.out.println("------Stemming Applied------");  //useless
        terms= removeStemm(terms);}
        System.out.println(terms.toString());
        System.out.println("------Frequency Counted------");
        HashMap<String, Integer> tfreq=tfreq(terms);
        System.out.println(tfreq.toString());

    }
    public boolean startIndexing()
    {
        String[]metadata=getMetaData(htmlString.toLowerCase());
        textContent= removeScriptStyles(htmlString.toLowerCase()); // content without scripts noscript and styles tags and every other
        textContent = getHtmlTextContent(textContent); // used for generating the snippets
        List<String> terms = getTerms(textContent); //store all content in list
        HashMap<String, Integer> tfreq_U=tfreq(terms); //frequency of Unstemmed termes
        Language=PageLanguageClassifier.identifyLanguage(tfreq_U); // from Unstemmed terms frequency, get probability to detect Language

        terms =removeStopWords(terms,Language);// form list remove stopwords
        if(Language=="en")                  //apply stem only for english.
            terms= removeStemm(terms);
        HashMap<String, Integer> tfreq_s=tfreq(terms); // from list calculate frequency of stemmed terms
        Connection conn = DBConnection.getConnection(true); // for isolation
        insertContentData(metadata,textContent,conn);
        insertFeatureData(tfreq_s,conn);
        ;
        try {
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
        //for images will use docHTML with XPath

     }

    private static String [] getMetaData(String Xhtml)
    {   String []metadata= new String[3];
        //<title>need this </title>
        Pattern ptn = Pattern.compile("<\\s*title\\s*>(.*)<\\s*/title\\s*>");
        Matcher m = ptn.matcher(Xhtml);
        if (m.find()) {
            metadata[0] = m.group(1);
        }

        //<meta name="description" Content= "Need this" />
        ptn = Pattern.compile("<\\s*meta\\s*name\\s*=\\s*\"description\"\\s*content\\s*=\\s*\"(.*)\"\\s*/>");
        m = ptn.matcher(Xhtml);
        if (m.find()) {
            metadata[1] = m.group(1);
        }
        //"<meta name="keywords"  content="need this" />
        ptn = Pattern.compile("<\\s*meta\\s*name\\s*=\\s*\"keywords\"\\s*content\\s*=\\s*\"(.*)\"\\s*/>");
        m = ptn.matcher(Xhtml);
        if (m.find()) {
            metadata[2] = m.group(1);
        }
        return metadata;
    }
    private static String getHtmlTextContent(String html) //only cleaning one time to get TEXT only rest for images will use XPATH
    {
        String multiplenewlines = "(\\n{1,2})(\\s*\\n)+";
        html = html.replaceAll(multiplenewlines, "$1").replaceAll("\\.", " ").replaceAll("&nbsp", "");
        html = html.replaceAll("[-+.^:,]", " ").replaceAll("\\s+", " ");
        if(docId !=0)
        {html=ImagePointers(html.trim());}
        return removeUnsualText(html);

    }
      private static List<String> getTerms(String content)
    {
        List<String> terms = new ArrayList<String>(Arrays.asList(content.split("\\s+|\\W+")));
        terms.removeIf(item -> item == null || "".equals(item)); // to remove " " items from list
        return terms;
    }

    private static List<String> removeStopWords(List<String> terms,String Lang)
    {
        switch (Lang) {
            case "de":
                List<String> swTermsDE = Arrays.asList(WordsCollection.getInstance().getDeutchStopWords());
                terms.removeAll(swTermsDE);
                break;
            case "en":
                List<String> swTermsEN=Stopwords.getInstance().getEngStopWords();
                terms.removeAll(swTermsEN);
                break;
            default:
                return terms;
        }
        return terms;
    }
    private static List<String> removeStemm(List<String>para)//useless without dictionary converts manchester to manchesti . heavy to heavi.
    {
        List<String> fnl= new ArrayList<String>();
        for (String s : para) {
            Stemmer st= new Stemmer();
            char [] splitChar=s.trim().toCharArray();
            st.add(splitChar, splitChar.length);
            st.stem();
            fnl.add(st.toString());
        }
        return fnl;
    }
    private static HashMap<String,Integer> tfreq(List<String>terms)
    {
        HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
        for (String key : terms) {
            int value = Collections.frequency(terms, key);

            freqMap.put(key, value);

        }
        return freqMap;
    }

    private static String removeUnsualText(String content) {
        content = content.replaceAll("[^A-Za-z]", " ").replaceAll("[+_,;:&#$!~%^*{}|.-]","").replaceAll("\\s+"," ");
        return content;
    }
    public static String removeScriptStyles(String html)
    {
        //<script src=""  > ...........</script> || <script> ...................</script> removes both cases ..
        Pattern p = Pattern.compile("<(noscript|script|style)[^>]*>.*?[^<]*</(script|noscript|style)>");
        Matcher m = p.matcher(html);
        html = m.replaceAll(" ");
        //<img> left rest deleted ..
        if(docId!=0){
        p= Pattern.compile("(?!<\\s*img[^>]*>)<[^>]*>");
        m = p.matcher(html);
        html = m.replaceAll(" ");
        }
        else
            {
                p= Pattern.compile("<[^>]*>");//(?!<\s*img[^>]*>)
                m = p.matcher(html);
                html = m.replaceAll(" ");
            }
        return html;
    }
//-----------------------------update to DB
    //canbedone... consider +16-16 from position and store it in image description column,
    private static String ImagePointers(String html)
    {
        int difference = 0,i=0,pos=0;
        Connection conn=DBConnection.getConnection();
        Matcher m = Pattern.compile("<\\s*img[^>]*>").matcher(html);
        while (m.find()) {
            pos = m.start()-difference; // -difference --> position after <img...> are deleted
            try {
                PreparedStatement stmt = conn.prepareStatement("UPDATE " + DBSetup.T_Images + " SET " + DBSetup.Col_position + " = ? WHERE "+DBSetup.Col_docId+ "= ? AND " + DBSetup.Col_index + " = ?");
                stmt.setInt(1,pos);
                stmt.setInt(2,docId);
                stmt.setInt(3,i);
                stmt.execute();
                conn.commit();
            } catch (SQLException e) {
                continue;
            }finally{
                i++;
                difference += m.end()-m.start();
            }
        }
        m.reset();
        html = m.replaceAll(" ");
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return html;
    }
    private void insertContentData(String []metadata,String data,Connection conn) {
        if (conn==null)
            conn = DBConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("Update " + DBSetup.T_Documents + " SET "
                    + DBSetup.Col_length + " = ?, "
                    + DBSetup.Col_content + " = ?, "
                    + DBSetup.Col_pageTitle+ " = ?, "
                    + DBSetup.Col_pageDescription+ " = ?, "
                    + DBSetup.Col_pageKeywords+ " =?, "
                    + DBSetup.Col_language+ " =? "
                    + " WHERE " + DBSetup.Col_docId + " = ? ;");
            ps.setInt(1,data.length());
            ps.setString(2,data);
            ps.setString(3,metadata[0]);
            ps.setString(4,metadata[1]);
            ps.setString(5,metadata[2]);
            ps.setString(6,Language);
            ps.setInt(7,docId);
            ps.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private boolean insertFeatureData(HashMap<String, Integer> tfreq, Connection conn)
    {
        //Insertion based on Groups of features
        if (conn == null)
        {
            conn = DBConnection.getConnection();
        }
        try
        {
            if (conn.isClosed())
                conn = DBConnection.getConnection();
            final int batchSize = 30;
            int i = 0;
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + DBSetup.T_Features + " where " + DBSetup.Col_docId + " = '" + docId + "';");
            ps.execute(); //emplty existing features to avoid redundant features

            ps = conn.prepareStatement( "INSERT into " + DBSetup.T_Features + " ( "
                    + DBSetup.Col_docId + ", "
                    + DBSetup.Col_term + ", "
                    + DBSetup.Col_termFrequency + ", "
                    + DBSetup.Col_language+ ", "
                    + DBSetup.Col_termId +")"
                    + " values (" + docId + ", ?, ?,?,?)"
                    + " ON CONFLICT DO NOTHING;");

            for(Map.Entry<String, Integer> entry : tfreq.entrySet())
            {
                String key = entry.getKey();
                int value = entry.getValue();
                ps.setString(1, key);
                ps.setInt(2, value);
                ps.setString(3,Language);
                ps.setInt(4, key.hashCode());
                ps.addBatch();

                if (++i==batchSize)
                {
                    ps.executeBatch();
                    i=0; // if i<= batch size and for loop ends then executebatch after the Foreach
                }
            }
            ps.executeBatch();
            ps.close();
            return true;
        } catch (PSQLException e) {
            e.printStackTrace();
        } catch (BatchUpdateException e) {
            e.getNextException().printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
//    Map<String, Integer> result = new LinkedHashMap<>();
//        unsortMap.entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByKey())
//        .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));