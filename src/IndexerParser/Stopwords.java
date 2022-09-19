package IndexerParser;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Zaryab on 10/27/2016.
 */
public class Stopwords {
    ArrayList<String> sws = new ArrayList<String>();
    private static Stopwords sw = new Stopwords( );
    private Stopwords()
    {
        int k = 0;
        String sCurrentLine;

        try {
            BufferedReader br = new BufferedReader(new FileReader("StopwordsEnglish.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                sws.add(sCurrentLine);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not Found");
            System.out.println(new File("StopwordsEnglish.txt").getAbsolutePath());
            }
            catch (IOException e)
            {

            }

    }
    public static Stopwords getInstance() {
        if (sw == null) {
            sw = new Stopwords();
        }
        return sw;
    }
    public ArrayList<String> getEngStopWords()
    {
        return sws;
    }


}
