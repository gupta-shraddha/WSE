package Algos_Compute;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zaryab on 1/14/2017.
 */
public class Jaccard {

    int n;
    double avg;
    double median;
    double fq;
    double tq;
    private static List<Jaccard> jr=new ArrayList<>();
    private static int[] parameter= new int[]{1,4,16,32};
    public Jaccard(int n,double avg,double median,double fq,double tq)
    {
        this.n = n;
        this.avg = avg;
        this.median = median;
        this.fq = fq;
        this.tq = tq;
    }
    public Jaccard()
    {
        //insert into Jaccard select d1,d2,I/U from docpairs group by d1,d2
        //select d1 ,d2, I, U from  View...
        //I--> select count() from shingle T1, shingle T2. where t1.docid= doc1 and t2.docid=doc2  and t1.shinglHash= t2.shingHash
        //U-->select count() from shingle where shingle.docID = doc1 or shingle.docId=doc2 .... Intersection
        Connection conn= DBConnection.getConnection();
        String jaccardQuery="INSERT INTO "+ DBSetup.T_Jaccard+" (doc1,doc2,jaccard)"
                + "SELECT doc1,doc2, (CAST (I AS double precision)/ U) AS jaccard "
                + "FROM (SELECT doc1,doc2, "
                + "(SELECT COUNT("+DBSetup.Col_shingleHash+") FROM "+DBSetup.T_Shingles+" WHERE "+DBSetup.T_Shingles+"."+DBSetup.Col_docId+" = doc1 "
                + "OR "+DBSetup.T_Shingles+"."+DBSetup.Col_docId+" = doc2) AS U, "
                + " (SELECT COUNT(ds1."+DBSetup.Col_shingleHash+") FROM "+DBSetup.T_Shingles+" ds1, "+DBSetup.T_Shingles+" ds2 "
                + " WHERE ds1."+DBSetup.Col_docId+" = doc1 AND ds2."+DBSetup.Col_docId+" = doc2 AND ds1."+DBSetup.Col_shingleHash+" = ds2."+DBSetup.Col_shingleHash+") AS I "
                + "FROM "+DBSetup.View_DocumentsPairs+" GROUP BY doc1,doc2) AS foo ON CONFLICT ON CONSTRAINT Jaccard_PK DO Update Set jaccard=EXCLUDED.jaccard;";
        try {
            conn.setAutoCommit(true);
            Statement stmt = conn.createStatement();
            stmt.execute(jaccardQuery);
            conn.setAutoCommit(false);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("-------Jaccard Computed---------");
        for(int i :parameter)
        {
            minConfigComputation(i);
        }
    }

    public static void minConfigComputation(int i)
    {
        Connection conn= DBConnection.getConnection();
        try {
            String sql="TRUNCATE min_cnfg_shingles;INSERT INTO min_cnfg_shingles SELECT  s.doc_id, p1.shingle_hash AS minShingles  FROM shingles s,(SELECT doc_id, shingle_hash,row_number() OVER (PARTITION BY doc_id ORDER BY shingle_hash )AS size FROM  shingles) p1 WHERE s.doc_id = p1.doc_id  AND p1.size <= "+i+" GROUP BY p1.shingle_hash,s.doc_id ORDER BY s.doc_id ";
            conn.setAutoCommit(true);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.setAutoCommit(false);
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateJaccard(i);
        System.out.println("-----MinCnfJaccard "+i+"----------- computed");
    }

    public static void updateJaccard(int i) {
        Connection conn= DBConnection.getConnection();
        String jaccardQuery="Update "+ DBSetup.T_Jaccard+" Set jaccard"+i+"=jk.jaccard From "
                + "(SELECT doc1,doc2, (CAST (I AS double precision)/ U) AS jaccard "
                + "FROM (SELECT doc1,doc2, "
                + "(SELECT COUNT("+DBSetup.Col_shingleHash+") FROM "+DBSetup.T_MinCnfgShingles+" WHERE "+DBSetup.T_MinCnfgShingles+"."+DBSetup.Col_docId+" = doc1 "
                + "OR "+DBSetup.T_MinCnfgShingles+"."+DBSetup.Col_docId+" = doc2) AS U, "
                + " (SELECT COUNT(ds1."+DBSetup.Col_shingleHash+") FROM "+DBSetup.T_MinCnfgShingles+" ds1, "+DBSetup.T_MinCnfgShingles+" ds2 "
                + " WHERE ds1."+DBSetup.Col_docId+" = doc1 AND ds2."+DBSetup.Col_docId+" = doc2 AND ds1."+DBSetup.Col_shingleHash+" = ds2."+DBSetup.Col_shingleHash+") AS I "
                + "FROM "+DBSetup.View_DocumentsPairs+" GROUP BY doc1,doc2) AS foo )as jk where jk.doc1="+DBSetup.T_Jaccard+".doc1 AND jk.doc2="+DBSetup.T_Jaccard+".doc2 AND "+DBSetup.T_Jaccard+".jaccard="+DBSetup.T_Jaccard+".jaccard ";
        try {
            conn.setAutoCommit(true);
            Statement stmt = conn.createStatement();
            stmt.execute(jaccardQuery);
            conn.setAutoCommit(false);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String []args)
    {
        new Jaccard();
//        for (int i:parameter)
//        {
//         makepdf( i);
//        }

    }

    private static void makepdf(int i) {
        Connection conn=DBConnection.getConnection();
        String sql="SELECT '"+i+"' as n, AVG(Abs_error) AS avg, " +
                " percentile_cont(0.5) WITHIN GROUP (ORDER BY Abs_error) As median," +
                " percentile_cont(0.25) WITHIN GROUP (ORDER BY Abs_error)AS first_quartile," +
                " percentile_cont(0.75) WITHIN GROUP (ORDER BY  Abs_error) AS third_quartile " +
                " FROM( SELECT abs(j.jaccard - j.jaccard"+i+") AS Abs_error FROM jaccard j  ) as foo ";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                jr.add(new Jaccard(rs.getInt(1),rs.getDouble(2),rs.getDouble(3),rs.getDouble(4),rs.getDouble(5)));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
