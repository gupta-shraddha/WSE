package SearchEngine.CommandLine;

import Miscellaneous.QueryResultItems;

public class CLIDB {
    public static void main(String[] args)
        {
            String databaseName = args[0];
            boolean queryType = Boolean.parseBoolean(args[1]);
            int k = Integer.parseInt(args[2]);
            String queryKeywords = "";
            for (int i = 3; i < args.length; i++)
            {
                queryKeywords += args[i] + " ";
            }
            ProcessQuery pq = new ProcessQuery();
            pq.search(databaseName,queryKeywords, k, queryType);
            System.out.println(pq.getSql());
            for (QueryResultItems ResultItem : pq.getResultItems())
            {
                System.out.println("Rank: "+ResultItem.getRank()+"\t\tScore: "+ ResultItem.getScore()+"\t\tURL: "+ ResultItem.getUrl() );
            }
        }

}


