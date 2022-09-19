package IndexerParser;

import Dictionary.WordsCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageLanguageClassifier {

	private static final float Probability = 0.3f;

	public static String identifyLanguage(HashMap<String, Integer> termFrequency)
	{
		List<String> englishList = WordsCollection.getInstance().getEnglishWords(); // get all english words
		int N = 0,n = 0;  //N -->Total Words , n --> Total English Words

		for(Map.Entry<String, Integer> entry : termFrequency.entrySet())
		{
			String key = entry.getKey();
			int count = entry.getValue();
			N +=count;
			if (englishList.contains(key))
			{
				n += count;
			}
		}
		return (float)n/(float)N > Probability?"en":"de";
	}
	public static String identifyQueryLanguage(List<String> qTerms)
	{
		List<String> englishList = WordsCollection.getInstance().getEnglishWords(); // get all english words
		int En =0,De = 0;
		int count=1;
		for(String s :qTerms)
		{
			if (englishList.contains(s.toLowerCase()))
			{
				En+=count;
			}else
			{
				De+=count;
			}
		}
		return En>=De?"en":"de";

	}
	public static void main(String[] args)
	{
		String s="folk michel goal dessloch kiril leggeymseyer michel system \"House\"";
		QueryParser qp= new QueryParser(s.toLowerCase(),"en");
		System.out.println(identifyQueryLanguage(qp.getUnStemmedTerms()));
	}
}

