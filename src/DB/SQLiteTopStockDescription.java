package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import main.Item;
import main.RowData;


/**
 *
 * @author Daniel
 */
public class SQLiteTopStockDescription {
    String dbLocation;
    String dbName;
    String tableName;
    
    public SQLiteTopStockDescription(String dbLocation,String dbName, String tableName){
        this.dbLocation = dbLocation;
        this.dbName = dbName;
        this.tableName = tableName;
    }
    public void createTableIfNotExists(){
        String sql = "CREATE TABLE IF NOT EXISTS "+tableName+" " +
                     "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                     " SHORTNAME           TEXT    UNIQUE, " + 
                     " LONGNAME            TEXT, " + 
                     " CONTENTTEXT         TEXT    NOT NULL)"; 
        doQuery(sql,dbLocation+ dbName);
        System.out.println("Table : "+ tableName+" created successfully");
    }
    public void insertRow(TopStockDescription topStockDescription){        
        String sql = "INSERT INTO "+tableName+" (ID,shortName,longName,contentText) " 
                + "VALUES ( null, '"+topStockDescription.getShortName()+"', '"
                + topStockDescription.getLongName()+"' , '"
                + topStockDescription.getContentText()+"');"; 
        System.out.println("Recording: "+sql);
        doQuery(sql, dbLocation+ dbName);
        System.out.println("Records created successfully");
    }
    public void updateRow(TopStockDescription topStockDescription){
        String sql = "UPDATE "+tableName+" set CONTENTTEXT = '"+topStockDescription.getContentText()+"'"
                + "where SHORTNAME = '"+topStockDescription.getShortName()+"';";
//<editor-fold defaultstate="collapsed" desc="comment">

//String sql = "INSERT INTO "+tableName+" (ID,shortName,longName,contentText) "
//                + "VALUES ( null, '"+topStockDescription.getShortName()+"', '"
//                + topStockDescription.getLongName()+"' , '"
//                + topStockDescription.getContentText()+"');";
//</editor-fold>
        doQuery(sql, dbLocation+ dbName);

        
        //System.out.println("Records created successfully");
    }
    public TopStockDescription selectRowWith(String columnName, String name){
        TopStockDescription topStockDescription= null;
        String sql = "SELECT * FROM "+ tableName+ " WHERE "+columnName+"='"+name+"';";
        topStockDescription = doQueryAndGet(sql,dbLocation+ dbName);
        
        return topStockDescription;
    }
    public void updateTopStockDescriptionList(List<Item> itemList){
        TopStockDescription tempTSD=null;
        for(Item item : itemList){
            if(item.getPackageContent().equalsIgnoreCase("Gold & Commodity Forecast")){
                if(item.getSubpackage()!=null)
                    if(item.getSubpackage().equalsIgnoreCase("Gold"))
                        continue;
                    
            }else if(item.getPackageContent().equalsIgnoreCase("Currency Forecast")){
                continue;
            }
            if(item.getPositionType().equalsIgnoreCase("Long & Short")){
                RowData topStock = item.getCvsTable().getHighest(1);
                RowData topStock2 = item.getCvsTable2().getHighest(1);

                //Check if it exist in DB
                tempTSD = selectRowWith("SHORTNAME",topStock.getSymbol()); 
                if(tempTSD == null){//If not Insert New Row
                    tempTSD = new TopStockDescription(topStock.getSymbol(),"",item.getTopStockDescription(),true);
                    insertRow(tempTSD);
                }else{// If there is, Update
                    tempTSD = new TopStockDescription(topStock.getSymbol(),"",item.getTopStockDescription(),false);
                    updateRow(tempTSD);
                }

                //Check if it exist in DB
                tempTSD = selectRowWith("SHORTNAME",topStock2.getSymbol()); 
                if(tempTSD == null){//If not Insert New Row
                    tempTSD = new TopStockDescription(topStock2.getSymbol(),"",item.getTopStockDescription(),true);
                    insertRow(tempTSD);
                }else{// If there is, Update
                    tempTSD = new TopStockDescription(topStock2.getSymbol(),"",item.getTopStockDescription(),false);
                    updateRow(tempTSD);
                }

            }else{
                RowData topStock = item.getCvsTable().getHighest(1);

                //Check if it exist in DB
                tempTSD = selectRowWith("SHORTNAME",topStock.getSymbol()); 
                if(tempTSD == null){//If not Insert New Row
                    tempTSD = new TopStockDescription(topStock.getSymbol(),"",item.getTopStockDescription(),true);
                    insertRow(tempTSD);
                }else{// If there is, Update
                    tempTSD = new TopStockDescription(topStock.getSymbol(),"",item.getTopStockDescription(),false);
                    updateRow(tempTSD);
                }
            }
            
        }
    }
    public void insertRows(List<TopStockDescription> topStockDescriptionList){
        for(TopStockDescription tsd : topStockDescriptionList){
            insertRow(tsd);
        }
    }
    
    private static void doQuery(String sql,String dbName){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+dbName);
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
    private static TopStockDescription doQueryAndGet(String sql,String dbName){
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
    
//<editor-fold defaultstate="collapsed" desc="comment">
//    public static void main(String args[]){
//        SQLiteTopStockDescription db = new SQLiteTopStockDescription("AutomationDb","TopStockDescription");
//        //db.createTable();
//        //TopStockDescription topStockDescription = new TopStockDescription("DDD","DANIEL HWANG", "CONTENT");
//        //db.insertRow(topStockDescription);
//        TopStockDescription topStockDescription = db.selectRowWith("ID","1");
//
//        if(topStockDescription != null)
//            topStockDescription.print();
//    }
//</editor-fold>
}
