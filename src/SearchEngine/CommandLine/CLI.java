package SearchEngine.CommandLine;

import Miscellaneous.QueryResultItems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Zaryab on 11/13/2016.
 */
public class CLI
{
    public static void main(String[] args)
    {
      //console
        System.out.println("Please enter Query");
        String Query=null;
        boolean QueryType=false;
        int K=0;
        BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
        try {
            Query=bf.readLine();
            Query.trim(); //trim() solves query  _a_b_c_ by removing spaces on edges
            System.out.println("Please enter true for Conjunctive or false for Disjunctive");
            String qtype= bf.readLine();
            if (qtype.equalsIgnoreCase("true") || qtype.equalsIgnoreCase("false")) {
               QueryType=Boolean.valueOf(qtype);
            }
            else{
                throw new IllegalArgumentException(qtype + " is not a boolean.");
            }
            System.out.println("Please enter ResultSize");
                K= Integer.parseInt(bf.readLine());


        } catch (IOException e) {
            System.out.println("stop messing around enter correct parameters i have no time for validations");
            e.printStackTrace();
        }
//        commandLine
//        QueryType = Boolean.parseBoolean(args[0]); //true conjunctive false disjunctive;
//        K = Integer.parseInt(args[1]); // result set
//        for (int i = 2; i < args.length; i++)
//        {
//            Query += args[i]+",";
//        }

        if(Query!=null && K>0 && (QueryType==true ||QueryType==false )) {
            String qtyp=QueryType==true?"Conjunctive":"Disjunctive";
            System.out.println("Query: "+Query+"\t\tType: "+qtyp+"\t\tResult size: "+K);
            ProcessQuery pq = new ProcessQuery();
            pq.search(Query, K, QueryType,"en",false);
            System.out.println(pq.getSql());
            List<QueryResultItems> qri =pq.getResultItems();
            int r=0;
            if(qri.size()>0) {
                for (QueryResultItems ResultItem : qri) {
                    System.out.println("Rank: " + ++r + "\t\tScore: " + ResultItem.getScore() + "\t\tURL: " + ResultItem.getUrl());
                }
            }
            else
                {
                    System.out.println("Search returned 0 items");

                }



        }

    }
}
