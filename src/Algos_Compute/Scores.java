package Algos_Compute;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;

import java.sql.*;

/**
 * Created by Zaryab on 11/27/2016.
 */
public class Scores {
    public void Scores()
    {

    }
    public static void computeIDF(Connection con) throws SQLException {
        con.setAutoCommit(true);
        String sql="SELECT "+ DBSetup.Func_CalcIDF+"() ";
        Statement stmt = con.createStatement();
        stmt.execute(sql);
        con.setAutoCommit(false);
        System.out.println("IDF computed");

    }

    public static void computeTFIDF(Connection con) throws SQLException {
        con.setAutoCommit(true);
        String sql="SELECT "+ DBSetup.Func_CalcTFIDF+"()";
        Statement stmt = con.createStatement();
        stmt.execute(sql);
        con.setAutoCommit(false);
        System.out.println("TFIDF computed");
    }
    public static void computeBM25(Connection con) throws SQLException {
        con.setAutoCommit(true);
        String sql="SELECT "+ DBSetup.Func_CalcBM25+"()";
        Statement stmt = con.createStatement();
        stmt.execute(sql);
        con.setAutoCommit(false);
        System.out.println("BM25 computed");
    }
    public static void computeBM25Pagerank(Connection con) throws SQLException {
        con.setAutoCommit(true);
        Statement stmt = con.createStatement();
        String sql="SELECT max(f.bm25) as bm25Weight , max(d.pagerank) as prWeight from features f, documents d";
        stmt.execute(sql);
        ResultSet rs = stmt.getResultSet();
        if(rs.next())
        {
            double bm25 = 1/rs.getDouble("bm25Weight");
            double pgRank = 1/rs.getDouble("prWeight");
            String updateQuery="UPDATE " + DBSetup.T_Features + " SET " + DBSetup.Col_bm25_pagerank +" = "+ "? *" + DBSetup.Col_bm25  + " + ? *" +  DBSetup.Col_pageRank;
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setDouble(1,bm25);
            ps.setDouble(2,pgRank);
            ps.execute();
            ps.close();
        }
        stmt.close();
        con.setAutoCommit(false);
        System.out.println("BM25_Pagerank computed");
    }
    public static void main(String []args) throws SQLException {
        try {
            computeBM25Pagerank(DBConnection.getConnection());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
