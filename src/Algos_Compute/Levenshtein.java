package Algos_Compute;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import IndexerParser.QueryParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zaryab on 12/12/2016.
 */
public class Levenshtein
{
    private static List<String> qTerms;
    private static String Language;
    static Connection conn=null;
    public Levenshtein()
    {
        CreateFuzzyExtension();
    }

    public static int lDistance(String s1, String s2){
        int len0 = s1.length() + 1;
        int len1 = s2.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s1
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s2
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s2
            newcost[0] = j;

            // transformation cost for each letter in s1
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    public static HashMap<String,String> correctTypos(String query,String lang)
    {
        //currently providing signel suggstion for multiple terms .
        //to do multiple suggrestions.
        Language=lang;
        qTerms = Language.equalsIgnoreCase("en")? new QueryParser(query,lang).getUnStemmedTerms():new QueryParser(query,lang).getUnStemmedTerms();
        //suggestion Limit 1
        HashMap<String,String> Suggestions =  new HashMap<>();
        for(String s :qTerms)
        {
            Suggestions.put(s, getSuggestions(s));
        }

        return Suggestions;
    }
    public static String getSuggestions(String term)
    {

        conn= DBConnection.getConnection();
        String matched="";
        String sql = "";
        if (Language.equalsIgnoreCase("en")) {
            sql = "SELECT DISTINCT " + DBSetup.Col_term + ", levenshtein (" + DBSetup.Col_term + ", '" + term + "') AS distance "
                    + " FROM " + DBSetup.T_Features
                    + " WHERE levenshtein (" + DBSetup.Col_term + ", '" + term + "') between 0 AND ?"// distance limit
                    + " AND " + DBSetup.Col_language + " ='en' "
                    + " ORDER BY distance "
                    + " LIMIT ?"; //# of results to retrieve
        }
        else {
            sql = "SELECT DISTINCT " + DBSetup.Col_term + ", levenshtein (" + DBSetup.Col_term + ", '" + term + "') AS distance "
                    + " FROM " + DBSetup.T_Features
                    + " WHERE levenshtein (" + DBSetup.Col_term + ", '" + term + "') between 0 AND ?"// distance limit
                    + " AND " + DBSetup.Col_language + " ='de' "
                    + " ORDER BY distance "
                    + " LIMIT ?"; //# of results to retrieve
        }
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, 3); //distance
            ps.setInt(2, 5); //top 5 results
            ResultSet rs = ps.executeQuery();
            int min_Distance=5;
            while (rs.next()) {
                if(rs.getInt("distance")<min_Distance)
                {
                    min_Distance=rs.getInt("distance");
                    matched=(rs.getString("term"));
                }
            }
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return matched;
    }


    public void CreateFuzzyExtension()
    {
        System.out.println("Fuzzy Extension Exists:"+DBSetup.fuzzyExtension());
    }
    public static void main(String []args)
    {
        System.out.println("Fuzzy Extension Exists:"+DBSetup.fuzzyExtension());
        correctTypos("informatik michel hello","en");
//wrote systtemz michel project hello
    }
}
