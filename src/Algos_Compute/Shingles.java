package Algos_Compute;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zaryab on 1/14/2017.
 */
public class Shingles {

    private int shingleSize=4;
    public void setShingleSize(int shingleSize) {
        this.shingleSize = shingleSize;
    }


    public Shingles()
    {
        processShingles();
    }
    public void processShingles()
    {

        Connection conn = DBConnection.getConnection();
        String query="SELECT "+ DBSetup.Col_docId + ","+ DBSetup.Col_content +" FROM " + DBSetup.T_Documents;
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                computeShingles(rs.getInt("doc_id"),rs.getString("content"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
        }
        System.out.println("------Shingling Completed-----");

    }
    public void computeShingles(int docID, String content)
    {
        Connection conn= DBConnection.getConnection();
        List<String> shingleList= new ArrayList<>(shingleSize);
        try {
            String[]contentArray=content.split("\\s+");
            PreparedStatement ps = conn.prepareStatement("INSERT INTO " + DBSetup.T_Shingles + "(" + DBSetup.Col_docId + ","
                    + DBSetup.Col_shingle + "," + DBSetup.Col_shingleHash+ ") VALUES(?,?,"+DBSetup.Func_md5_to_bigint +"(?)) ON CONFLICT DO NOTHING");
            for(int i=0;i<=contentArray.length-4;i++)
            {
                for ( int j=0; j<=3;j++)
                {
                    shingleList.add(contentArray[i+j]);
                }
                if(shingleList.size()>=shingleSize) //1 shingle = 4 words  ... will not consider the final shingle if Total content not divisible by 4
                {
                    String shingle = shingleList.toString().replaceAll("\\[|\\]", "").replaceAll(",","").trim();
                    try {
                        ps.setInt(1, docID);
                        ps.setString(2, shingle);
                        ps.setString(3, shingle );
                        ps.addBatch();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    shingleList.clear();
                }
            }

            try {
                ps.executeBatch();
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String []Args)
    {
        new Shingles();
    }

}
