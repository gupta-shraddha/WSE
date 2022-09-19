package DBPostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection
{
    private static final String connectionString = "jdbc:postgresql://localhost:5432/";
    private static final String user = "postgres";
    private static final String password = "admin";
    private static final String defaultDbName="WSE";
    private static final String postgreSqlJDBC="org.postgresql.Driver";

    public static Connection getConnection()
    {
        return getCon(connectionString+defaultDbName, false); //by default autocommit off
    }
    public static Connection getConnection(boolean autocommit) //specify autocommit property
    {
        return getCon(connectionString+defaultDbName, autocommit); //specific DB
    }
    public static Connection getConnection(String dbName)//specific DB
    {
        return getCon(connectionString+dbName, false);
    }
    public static Connection getConnection(String dbName,boolean autocommit) //specific DB & Autocommit
    {
        return getCon(connectionString+dbName, autocommit);
    }

    private static Connection getCon(String connectionString, boolean autocommit) {
        Connection conn = null;
        try {
            Class.forName(postgreSqlJDBC);
            conn = DriverManager.getConnection(connectionString,user,password);
            conn.setAutoCommit(autocommit);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
}