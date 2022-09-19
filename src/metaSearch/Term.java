package metaSearch;

import DBPostgres.DBConnection;
import DBPostgres.DBSetup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by gupta on 06.02.2017.
 */
public class Term {

    private static final double b = 0.4;

    private String term;
    private int df;
    private int cf;

    public Term(String term, int df) {
        this.term = term;
        this.df = df;
    }

    public void setCf(int cf) {
        this.cf = cf;
    }


    public double getT(int cw, double avg_cw) {
        return (df/(df+50+150*(cw/avg_cw)));
    }

    public double getI(int c, double cf) {
        return (Math.log((c+0.5)/cf)/Math.log(c+1.0));
    }

    public double getScore(int c, int cw, double avg_cw) {
        return b+(1-b)*getT(cw,avg_cw)*getI(c,cf);
    }
    public double getScore(int c, double T) {
        return b+(1-b)*T*getI(c,cf);
    }

    public void save(int engineId) {
        Connection con = DBConnection.getConnection();
        try{
            PreparedStatement stmt = con.prepareStatement("INSERT INTO " + DBSetup.T_MetaSearch_terms + " VALUES (" +
                    "?,?,?) ON CONFLICT DO NOTHING");

            stmt.setInt(1,engineId);
            stmt.setInt(2,term.hashCode());
            stmt.setInt(3,df);
            stmt.execute();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) { }
        }

    }

}
