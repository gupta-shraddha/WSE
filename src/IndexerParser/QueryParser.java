package IndexerParser;

import DBPostgres.DBSetup;
import Dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;


public class QueryParser {
    private String query;
    private String Language;
    private List<String> stemmedTerms;
    private List<String> unStemmedTerms; // for conjunctive we need unstemmed text
    private List<String> quotedKeywords;
    private List<String> synonymTerms;
    private List<String> siteQuery;
    //-----------------------get methods
    public List<String> getStemmedTerms() {
        return stemmedTerms;
    }
    public List<String> getUnStemmedTerms() {
        return unStemmedTerms;
    }
    public List<String> getQuotedKeywords() {
        return quotedKeywords;
    }
    public List<String> getSynonymTerms() {
        return synonymTerms;
    }
    public List<String> getSiteQuery() {
        return siteQuery;
    }
    //--------------------------------

    public QueryParser(String Query,String lang)
    {
        Language=lang;
        query=Query;
        stemmedTerms = new ArrayList<String>();
        unStemmedTerms = new ArrayList<String>();
        quotedKeywords = new ArrayList<String>();
        synonymTerms= new ArrayList<String>();
        siteQuery = new ArrayList<String>();

        String[] queryTerms = query.split(" ");// abc,jkl,"xyz",site:www.abc.com
        Stemmer stem = new Stemmer();
        for(String s:queryTerms)
        {
            if(s.startsWith("site:"))
            {
                siteQuery.add(s.substring(5));
            }
            else if (s.startsWith("\"") && s.endsWith("\"")) // "hello"
            {
                quotedKeywords.add(s.substring(1,s.length()-1));
                unStemmedTerms.add(s.substring(1,s.length()-1));
            }
            else if(s.startsWith("~")){
                synonymTerms.add(s.substring(1));
                unStemmedTerms.add(s.substring(1));
            }
            else {
                unStemmedTerms.add(s); //just in case until stemmer is not ruining terms
                if(Language=="en") {
                    char[] splitChar = s.trim().toCharArray();
                    stem.add(splitChar, splitChar.length);
                    stem.stem();
                    stemmedTerms.add(stem.toString());
                }
            }
        }
    }
    public int getTermsCount()
    {
        return getStemmedTerms().size()+getUnStemmedTerms().size()+getQuotedKeywords().size()+ getSynonymTerms().size();//getSiteQuery().size() not to be counted as term;
    }
    public String getWhereClause() {return getSite_Quoted_Synonym_Predicates() ;}

    public String getSite_Quoted_Synonym_Predicates() // append where claus to search for quoted keywords or specific site: search
    {
        String quotedClause = " ";
        if(getQuotedKeywords().size()>0)
        {
            for (String term : getQuotedKeywords())
            {
                quotedClause += "AND f."+DBSetup.Col_docId + " IN (SELECT " + DBSetup.Col_docId + " FROM " + DBSetup.T_Features +
                        " WHERE " + DBSetup.Col_termId + "=" + term.hashCode() + ") ";
                // if its like "information system" then search in content
            }
        }
        String synonymClause = " ";
        if(getSynonymTerms().size()>0) {
            Dictionary d=new Dictionary();
            if (Language.equalsIgnoreCase("de")) {
                d.GermanDict();
                for (String s : getSynonymTerms()) {
                    List<Integer> hashCodes = new ArrayList<>();
                    for (String syn : d.getDeuSynonyms(s)) {
                        hashCodes.add(syn.hashCode());
                    }
                    if(hashCodes.size()!=0) {
                        synonymClause += "AND f.term_Id IN (" + hashCodes.toString().replaceAll("\\[|\\]", "") + ") ";
                    }
                }
            }
            else {
                d.EnglishDict();
                for (String s : getSynonymTerms()) {
                    List<Integer> hashCodes = new ArrayList<>();
                    for (String syn : d.getEngSynonyms(s)) {
                        hashCodes.add(syn.hashCode());
                    }
                    synonymClause += "AND f.term_Id IN (" + hashCodes.toString().replaceAll("\\[|\\]", "") + ") ";
                }

            }
        }
        String siteClause = " ";
        if(getSiteQuery().size()>0)
        {
            for (String site : getSiteQuery())
            {
                siteClause += "AND d."+ DBSetup.Col_url + " LIKE '%" + site + "%' ";
            }
        }
        return quotedClause+synonymClause+siteClause;
    }
}
