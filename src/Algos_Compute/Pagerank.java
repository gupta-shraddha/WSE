package Algos_Compute;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.SparseMatrix;
import org.la4j.vector.dense.BasicVector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zaryab on 11/27/2016.
 */
public class Pagerank {

    public static void computePageRank()
    {
        System.out.println("--------------Computing page rank-----------------");
        //---------------get all docs ----------------
        Connection conn= DBConnection.getConnection();
        Map<Integer,Integer> doc_Ids= new HashMap<Integer, Integer>();
        try {
            String sql="SELECT DISTINCT "+ DBSetup.Col_docId+" FROM "+DBSetup.T_Documents;
            PreparedStatement ps= conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int pos=0;
            while (rs.next())
            {
                doc_Ids.put(rs.getInt("doc_id"),pos++);  // key Document ;value DocPosition will be used in matrix
                //can also be done by rank() || rownumber() over partition by doc ID......
            }
            ps.close();
        }
        catch (SQLException e){             e.printStackTrace();}
        //--------------get EdgesInfo-------------
        int[][] edgeInfo = null;
        try {
            String sql="SELECT * FROM "+DBSetup.T_Links+" where to_docid IN (SELECT DISTINCT "+ DBSetup.Col_docId+" FROM "+DBSetup.T_Documents+") AND from_docid IN (SELECT DISTINCT "+ DBSetup.Col_docId+" FROM "+DBSetup.T_Documents+")";
            PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE); // we can traverse RS ...
            ResultSet rs = ps.executeQuery();
            rs.last();
            edgeInfo = new int[rs.getRow()][2];  //total rows [rs.getRow()].using rs.Last()... [2] for each link the from & to
            if(!rs.isBeforeFirst())
                rs.beforeFirst();
            int i = 0;
            while (rs.next())
            {
                edgeInfo[i][0] = rs.getInt("from_docid");
                edgeInfo[i][1] = rs.getInt("to_docid");
                i++;
            }
            ps.close();
        } catch (Exception e) {            e.printStackTrace();}

        //---------Generate Adjacency matrix ----- from EdgesInfo--
        int N= doc_Ids.size();
        Matrix adjMatrix = SparseMatrix.zero(N, N);   // N x N matrix initialized with 0 for all rowcolumns
        for(int edge [] :edgeInfo)
        {
            Integer from_posInMatrix = doc_Ids.get(edge[0]); //position from Hashmap
            Integer to_posInMatrix = doc_Ids.get(edge[1]);

            if((from_posInMatrix >-1 && to_posInMatrix >-1)&&(from_posInMatrix != null && to_posInMatrix != null))
            {
                // Respective edge information in the Matrix set to 1
                adjMatrix.set(from_posInMatrix, to_posInMatrix, 1);
            }
        }
        System.out.println("Adjacency Matrix Generated");
        //----------------apply the formula on each row to compute Transaction probability matrix ----------
        int index=0;
        double alpha = 0.1;
        while(index<N)
        {
            Vector mx_row = adjMatrix.getRow(index);
            double out_Degree = mx_row.sum(); // Sum p|p -> q
            if (out_Degree == 0) // adding default probability if outdegree ==0  // dangling nodes
            {
                mx_row = mx_row.add(1.0/N);

            }
            else
            {
                double randomJump=alpha/N;
                // PR(P)/out -->                        mx_row.divide(out_Degree)
                // (1-alpha)* PR(P)/out -->             mx_row.divide(out_Degree).multiply((1 - alpha))
                //(1-alpha)*PR(P)/out + alpha/N -->     mx_row.divide(out_Degree).multiply((1 - alpha)).add(alpha/N) --------------DMAS
                mx_row = mx_row.divide(out_Degree).multiply((1 - alpha)).add(randomJump);
            }
            adjMatrix.setRow(index++, mx_row);
        }
        //----------- compute Rank using Vector .. k iterations using the previous result.
        //https://en.wikipedia.org/wiki/Examples_of_Markov_chains
        Vector iRank = BasicVector.zero(N);
        iRank.set(0,1F); //initially probability matrix [1,0,0,0,0,0,0,0,0,0,0,0,0,.....................N]   1xN
        int powerIteration=1;
        double diff=1f;
        Vector nRank=null;
        while (powerIteration<=N)
        {
            nRank = iRank.multiply(adjMatrix); // Matrix multiplication [1xN] . [NXN] = [1xN]
            diff=calculateDistance(iRank.subtract(nRank)); // sqrt of (Sum of Square)
            if(diff<.0001)   //if difference between current and previous state is very minor or converging to 0 // 0.0001 too early
            {
                System.out.println("Converged at Iteration number:"+powerIteration);
                powerIteration=N;
            }
            iRank=nRank;
            powerIteration++;
        }
        System.out.println("Ranks Computed");
        //--------------------Rank Update in DB -----------------
        updateDB(conn,iRank,doc_Ids);
        System.out.println("Ranks Updated in DB");
        System.out.println("----------------------------------------------");

    }
    public static double calculateDistance(Vector v)
    {
        double Sum = 0.0;
        for(double d : v) {
            Sum = Sum + Math.pow(d,2);
        }
        return Math.sqrt(Sum);
    }
    public static void updateDB(Connection conn,Vector finalRank,Map<Integer,Integer> doc_Ids)
    {
        String sqld="UPDATE "+DBSetup.T_Documents+" set "+DBSetup.Col_pageRank+" = ? WHERE "+DBSetup.Col_docId+" = ?;";
        String sqlf="UPDATE "+DBSetup.T_Features+" set "+DBSetup.Col_pageRank+" = ? WHERE "+DBSetup.Col_docId+" = ?;";

        try {
            PreparedStatement stDoc = conn.prepareStatement(sqld);
            PreparedStatement stFea = conn.prepareStatement(sqlf);

            final int batchSize = 50;
            int i = 0;
            for (Map.Entry<Integer, Integer> pos : doc_Ids.entrySet())
            {

                stDoc.setDouble(1,finalRank.get(pos.getValue()));
                stDoc.setInt(2, pos.getKey());
                stDoc.addBatch();
                stFea.setDouble(1,finalRank.get(pos.getValue()));
                stFea.setInt(2, pos.getKey());
                stFea.addBatch();

                if(++i == batchSize)
                {
                    stDoc.executeBatch();
                    stFea.executeBatch();
                    i=0;
                }
            }
            stDoc.executeBatch();
            stFea.executeBatch();
            conn.commit();
            stDoc.close();
            stFea.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Pagerank()
    {
        computePageRank();
    }

    public static void main(String[] args)
    {
        computePageRank();
        try {
            Scores.computeBM25Pagerank(DBConnection.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
