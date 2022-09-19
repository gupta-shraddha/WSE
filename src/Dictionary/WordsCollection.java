package Dictionary;

import java.io.*;
import java.util.ArrayList;

public class WordsCollection {
	ArrayList<String> EnglishWords = new ArrayList<String>();
	private static WordsCollection wc = new WordsCollection( );
	private WordsCollection()
	{
		int k = 0;
		String sCurrentLine;

		try {

			//BufferedReader br = new BufferedReader(new FileReader("English58k.txt"));
			BufferedReader br = new BufferedReader(new FileReader("English58k.txt"));
			//
			while ((sCurrentLine = br.readLine()) != null) {
				EnglishWords.add(sCurrentLine);
			}
		}
		catch (FileNotFoundException e) {
			this.getClass().getClassLoader().getResource("");
			System.out.println("File not Found");
			System.out.println(new File("English58k.txt").getAbsolutePath());
		}

		catch (IOException e)
		{

		}

	}
    public static WordsCollection getInstance() {
        if (wc == null) {
            wc = new WordsCollection();
        }
        return wc;
    }
    public ArrayList<String> getEnglishWords()
    {
        return EnglishWords;
    }

    private final static String[] stopWordsDeutsch = {"einer", "eine", "eines", "einem", "einen", "der", "die", "das", "dass", "da?", "du", "er", "sie", "es", "was", "wer", "wie", "wir", "und", "oder", "ohne", "mit", "am", "im", "in", "aus", "auf", "ist", "sein", "war", "wird", "ihr", "ihre", "ihres", "ihnen", "ihrer", "als", "f?r", "von", "mit", "dich", "dir", "mich", "mir", "mein", "sein", "kein", "durch", "wegen", "wird", "sich", "bei", "beim", "noch", "den", "dem", "zu", "zur", "zum", "auf", "ein", "auch", "werden", "an", "des", "sein", "sind", "vor", "nicht", "sehr", "um", "unsere", "ohne", "so", "da", "nur", "diese", "dieser", "diesem", "dieses", "nach", "?ber", "mehr", "hat", "bis", "uns", "unser", "unserer", "unserem", "unsers", "euch", "euers", "euer", "eurem", "ihr", "ihres", "ihrer", "ihrem", "alle", "vom" };

    public static String[] getDeutchStopWords() {
        return stopWordsDeutsch;
    }
}
