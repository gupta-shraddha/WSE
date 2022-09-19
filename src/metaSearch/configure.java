package metaSearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;

/**
 * Created by gupta on 29.01.2017.
 */
public class configure {
    
        public int id;
        public String url;
        public String queryKeyword;
        public String kKeyword;
        public boolean activated;

        public configure() {
            id = 0;
        }
        public static List<configure> getactiveEngine(){
            List<configure> items = new ArrayList<>();
            Connection con = DBConnection.getConnection();
            int total=0;
            try {
                PreparedStatement stmt = con.prepareStatement(" SELECT "+DBSetup.Col_id+", "
                        +DBSetup.Col_url+", "
                        +DBSetup.Col_active
                        + " FROM " + DBSetup.T_MetaSearch + " WHERE " + DBSetup.Col_active + "=TRUE ");
                ResultSet rs=stmt.executeQuery();
                while(rs.next()){
                    configure item = new configure();
                    item.id=rs.getInt(DBSetup.Col_id);
                    item.url=rs.getString(DBSetup.Col_url);
                    items.add(item);
                }
                con.commit();
            }catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    con.close();
                } catch (SQLException ignored) {}
            }
            return items;
        }
    public static void saveEngine(String url,Boolean active){
        Connection con = DBConnection.getConnection();
        try{
            PreparedStatement stmt = con.prepareStatement("INSERT INTO " + DBSetup.T_MetaSearch
                    + "("+DBSetup.Col_url+"," +DBSetup.Col_active+")"+"VALUES "+
                    "(?,?)");
            System.out.println(stmt.toString());

            stmt.setString(1,url);
            stmt.setBoolean(2,active);
            stmt.execute();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (SQLException ignored) {}
        }
    }
    public static List<configure> getsavedEngine(){
        List<configure> items = new ArrayList<>();
        Connection con = DBConnection.getConnection();
        try{
            PreparedStatement stmt = con.prepareStatement(" SELECT "+DBSetup.Col_id+", "
                    +DBSetup.Col_url+", "
                    +DBSetup.Col_active
                    + " FROM " + DBSetup.T_MetaSearch);
            System.out.println(stmt.toString());
            ResultSet rs=stmt.executeQuery();
            while(rs.next()){
                configure item = new configure();
                item.id=rs.getInt(DBSetup.Col_id);
                item.url=rs.getString(DBSetup.Col_url);
                item.activated=rs.getBoolean(DBSetup.Col_active);
                items.add(item);
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (SQLException ignored) {}
        }
        return items;
    }
    public static void updateEngine(Integer id,String url,Boolean active){
        Connection con = DBConnection.getConnection();
        try{
            PreparedStatement stmt = con.prepareStatement("UPDATE " + DBSetup.T_MetaSearch
                    + "("+DBSetup.Col_url+"," +DBSetup.Col_active+")"+"VALUES "+
                    "(?,?)"+ "WHERE "+DBSetup.Col_id+"="+id);
            System.out.println(stmt.toString());

            stmt.setString(1,url);
            stmt.setBoolean(2,active);
            stmt.execute();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (SQLException ignored) {}
        }
    }

        public void update() {
            Connection con = DBConnection.getConnection();
            try {
                PreparedStatement stmt = con.prepareStatement("UPDATE " + DBSetup.T_MetaSearch
                        + " SET " + DBSetup.Col_url + " = ?, " +
                        "" + DBSetup.Col_active + " =? WHERE " + DBSetup.Col_id + "=?");

                stmt.setString(1, url);
                stmt.setBoolean(2, activated);
                stmt.setInt(3, id);
                stmt.execute();
                con.commit();

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    con.close();
                } catch (SQLException ignored) {}
            }
        }

        public void delete() {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement stmt = con.prepareStatement("DELETE FROM " + DBSetup.T_MetaSearch
                        + " WHERE " + DBSetup.Col_id + "=?");

                stmt.setInt(1, id);
                stmt.execute();
                con.commit();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

}
