package DB;

import java.sql.*;

public class SQLite
{
    public static void doQuery(String sql,String dbName){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }
    public static TopStockDescription doQueryAndGet(String sql,String dbName){
        TopStockDescription topStockDescription = null;
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            //stmt.executeUpdate(sql);
            
            if(rs.next()){
                topStockDescription = new TopStockDescription(rs.getString("shortName"),
                        rs.getString("longName"),
                        rs.getString("contentText"),
                        false);
            }
            
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        
        //System.out.println("Records created successfully");
        return topStockDescription;
    }
}