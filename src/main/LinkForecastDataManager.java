/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DB.Dictionary;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rss.ForecastData;


/**
 *
 * @author Daniel
 */
public class LinkForecastDataManager{
    public List<ForecastData> linkForecastDataList;
    public String dbLocation;//no change
    public String tableName;//no change
    public String csvTableName;
    public String csvRowName;
    
    Dictionary timeFrameDictionary;
    Dictionary positionTypeDictionary;
    String csvDirectory;
    DateFormat dateFormat;
    public LinkForecastDataManager(String fileLoc){
        linkForecastDataList = new ArrayList();
        this.dbLocation = fileLoc;
        tableName = "mainTable";
        csvTableName = "csvTable";
        csvRowName = "csvRow";
        csvDirectory="";
    }
    
    public void insertForecastDataList(List<ForecastData> fdList){
        linkForecastDataList = fdList;
    }
    //Insert exept Title and Link
    public void insertItemList(List<Item> itemList){
        for(Item item: itemList){
            linkForecastDataList.add(new ForecastData(item));
        }
    }
    
    public void insertRSSEntries(List<rss.RSSEntry> entries){
        if(linkForecastDataList.size() == entries.size())
            System.out.println("GOOD");
        System.out.println("Entries : "+ entries.size());
        System.out.println("linkForecastDataList : "+ linkForecastDataList.size());
        for(int i =0; i < linkForecastDataList.size(); i++){
            linkForecastDataList.get(i).setRssEntry(entries.get(i));
        }
        
    }
    
    public void dbUpdateToDB(Tab tab){
        timeFrameDictionary = tab.getTimeFrameDictionary();
        positionTypeDictionary = tab.getPositionTypeDictionary();
        
        csvDirectory = tab.csvDirectory + "/";
        dateFormat = tab.getPreferenceSettings().getDateFormat();
        createTableIfNotExists();
        for(ForecastData item : linkForecastDataList){
            insertItem(item);//, tab.getPackageHeaderList());
            //insertRow(item, tab.getPackageHeaderList());
        }
    }
    public void emptyDataList(){
        linkForecastDataList.clear();
    }
    private void insertRow(ForecastData linkForecastData, PackageHeaderList packageHeaderManager){
        Item item = linkForecastData.getItem();
        List<RowData> topData = item.getTop3(positionTypeDictionary);
        
        PackageHeader tempPackageHeader= packageHeaderManager.getPackageHeaderWhereNameIs(item.getPackageContent(), item.getSubpackage());
        
        String sql = "INSERT INTO "+tableName+" (ID,title,link,positionType,packageName,subpackageName,csvFilePath,timeFrame,forecastDate,targetDate,"
                + "topReturn1,topStockName1,topReturn2,topStockName2,topReturn3,topStockName3,avgReturn1,avgReturn2,snp500Return,marketPremium1,marketPremium2,accuracy,totalNumber) " 
                + "VALUES ( null, '"+linkForecastData.getRssEntry().getTitle()+"', '"
                + linkForecastData.getRssEntry().getLink()+"' , '"
                + item.getPositionType()+"' , '"
                + tempPackageHeader.getPackageName2()+"' , '"
                + tempPackageHeader.getSubpackage()+"' , '"
                + item.getCSVPath(csvDirectory)+"' , '"
                + item.getTimeFrame()+"' , '"
                + item.getForecastDay(dateFormat)+"' , '"
                + item.getTargetDay(dateFormat)+"' , '"
                + topData.get(0).getReturnzString()+"' , '" //1
                + topData.get(0).getSymbol()+"' , '" //1
                + topData.get(1).getReturnzString()+"' , '" //1
                + topData.get(1).getSymbol()+"' , '" //1
                + topData.get(2).getReturnzString()+"' , '" //1
                + topData.get(2).getSymbol()+"' , '" //1
                + item.getAvgReturn1()+"' , '"
                + item.getAvgReturn2(positionTypeDictionary)+"' , '"
                + item.getSNP500Return()+"' , '"
                + item.getMarketPremium1()+"' , '"
                + item.getMarketPremium2(positionTypeDictionary)+"' , '"
                + item.getAccuracy(positionTypeDictionary)+"' , '"
                + item.getTotalNumber(positionTypeDictionary)+"');"; 
        System.out.println("Recording: "+sql);
        doQuery(sql, dbLocation);
        System.out.println("Records created successfully");
    }
    private void insertItem(ForecastData linkForecastData){//, PackageHeaderList packageHeaderManager){
        int result= -1;
        //Insert in Main Table
        int mainTableId = insertMainTable(linkForecastData);//, packageHeaderManager);
        //Insert in CSV and Row Table
        if(mainTableId != -1)
            result = insertCsvTable(mainTableId, linkForecastData);
        if(result == 0)
            System.out.println("No problem for this forcast ");
        
    }
    private int insertCsvTable(Integer mainTableId, ForecastData linkForecastData){
        Item item = linkForecastData.getItem();
        boolean longNShort = false;
        Date forecastDate = item.getCvsTable().getUpdateDate();
        Date targetDate = item.getCvsTable().getTargetDate();
        String forecastDateStr = ForcastUi.dateToString(forecastDate);
        String targetDateStr = ForcastUi.dateToString(targetDate);
        
        List<CVSTable> csvTableList = new ArrayList();
        if(item.getCvsTable2().getCvsTable()!= null){//Long & Short
            longNShort = true;
            csvTableList.add(item.getCvsTable());
            csvTableList.add(item.getCvsTable2());
        }else{//Not Long&SHORT
            csvTableList.add(item.getCvsTable());
        }
        
        Integer positionNumber = 0;
        for(CVSTable csvTable : csvTableList){
            positionNumber++;
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet generatedKeys;
            int csvTableId = -1;
            
            try{// Insert CSV Table 
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
                System.out.println("Open database success, will write CSVTable Position: " + (positionNumber == 1 ? "Long" : "Short"));
                
                stmt = conn.prepareStatement("INSERT INTO " + csvTableName 
                    + " (Id,positionNumber,forecastDate,targetDate,snp500Return,iKnowFirstAvgReturn,mainTableId)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)");
                
                //stmt.setString(1, "null");//Id
                stmt.setString(2, positionNumber.toString());//positionNumber
                stmt.setString(3, forecastDateStr);//forecastDate
                stmt.setString(4, targetDateStr);//targetDate
                stmt.setString(5, item.getSNP500Return());//snp500Return
                stmt.setString(6, (positionNumber==1 ? item.getAvgReturn1() : item.getAvgReturn2()));//iKnowFirstAvgReturn for Table 1 or 2
                stmt.setString(7, mainTableId.toString());//mainTableId
                stmt.executeUpdate();
                
                generatedKeys = stmt.getGeneratedKeys();
                csvTableId = generatedKeys.getInt(1);
                
            }catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                //System.exit(0);
            }finally {
                try {
                   if (stmt != null) { stmt.close(); }
                }
                catch (Exception e) {
                   // log this error
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
                try {
                   if (conn != null) { conn.close(); }
                }
                catch (Exception e) {
                   // log this error
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
                System.out.println("MainTable created successfully Adding CSV Rows");
            }
            
            
            if(csvTableId == -1){
                System.out.println("Failed to add CSV Rows");
                return -1;
            }
            //If CSV Table sucessfully Recorded Insert ROWs
            String csvTableIdStr = String.valueOf(csvTableId);
            for(RowData row: csvTable.getCvsTable()){
                String symbol = row.getSymbol();
                String prediction = String.valueOf(row.getPrediction());
                String returnValue = row.getReturnzString();
                String accuracy = String.valueOf(row.getAccuracy());
                
                try {
                    Class.forName("org.sqlite.JDBC");
                    conn = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
                    System.out.println("Open database success, will write CSV Rows");

                    stmt = conn.prepareStatement("INSERT INTO " + csvRowName
                            +" (Id,symbol,prediction,returnValue,accuracy,csvTableId) "
                                    +" VALUES (?, ?, ?, ?, ?, ?)");
                    
                    //stmt.setString(1, "null");//Id
                    stmt.setString(2, symbol);//symbol
                    stmt.setString(3, prediction);//prediction
                    stmt.setString(4, returnValue);//returnValue
                    stmt.setString(5, accuracy);//accuracy
                    stmt.setString(6, csvTableIdStr);//iKnowFirstAvgReturn for Table 1 or 2
                    stmt.executeUpdate();
                    
                } catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    //System.exit(0);
                }finally {
                    try {
                       if (stmt != null) { stmt.close(); }
                    }
                    catch (Exception e) {
                       // log this error
                       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    }
                    try {
                       if (conn != null) { conn.close(); }
                    }
                    catch (Exception e) {
                       // log this error
                       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    }
                    System.out.println("CsvRow adding Success");
                }
//                String csvRowSql = "INSERT INTO "+csvRowName+" (Id,symbol,prediction,returnValue,accuracy,csvTableId) " 
//                    + "VALUES ( null, '" // If 1 any if 2 Short (of Long & short)
//                    + symbol+"' , '"
//                    + prediction+"' , '"
//                    + returnValue+"' , '"
//                    + accuracy+"' , '"
//                    + String.valueOf(csvTableId)+"' );"; 
//                insertQueryReturnInt(csvRowSql, dbLocation);
            }
            
            
        }
        
        return 0;
        
    }
    private int insertCsvTable2(int mainTableId, ForecastData linkForecastData){
        Item item = linkForecastData.getItem();
        boolean longNShort = false;
        Date forecastDate = item.getCvsTable().getUpdateDate();
        Date targetDate = item.getCvsTable().getTargetDate();
        String forecastDateStr = ForcastUi.dateToString(forecastDate);
        String targetDateStr = ForcastUi.dateToString(targetDate);
        
        
        if(item.getCvsTable2().getCvsTable()!= null)
            longNShort = true;
        
        
        //First Table (If it is long and short it will be Long Part)
        String sql1 = "INSERT INTO "+csvTableName+" (Id,positionNumber,forecastDate,targetDate,snp500Return,iKnowFirstAvgReturn,mainTableId) " 
                + "VALUES ( null, '"+1+"', '" // If 1 any if 2 Short (of Long & short)
                + forecastDateStr+"' , '"
                + targetDateStr+"' , '"
                + item.getSNP500Return()+"' , '"
                + item.getAvgReturn1()+"' , '"
                + String.valueOf(mainTableId)+"' );"; 

        int csvTableId = insertQueryReturnInt(sql1, dbLocation);
        
       
        if(csvTableId!= -1){
            CVSTable csvTable1 = item.getCvsTable();
            for(RowData row: csvTable1.getCvsTable()){
                String symbol = row.getSymbol();
                String prediction = String.valueOf(row.getPrediction());
                String returnValue = row.getReturnzString();
                String accuracy = String.valueOf(row.getAccuracy());
                
                
                String csvRowSql = "INSERT INTO "+csvRowName+" (Id,symbol,prediction,returnValue,accuracy,csvTableId) " 
                    + "VALUES ( null, '" // If 1 any if 2 Short (of Long & short)
                    + symbol+"' , '"
                    + prediction+"' , '"
                    + returnValue+"' , '"
                    + accuracy+"' , '"
                    + String.valueOf(csvTableId)+"' );"; 
                insertQueryReturnInt(csvRowSql, dbLocation);
            }
            
            
        }else{
            return -1;
        }
        
        //If Long & Short insert the short part
        if(longNShort){
            String sql2= "INSERT INTO "+csvTableName+" (Id,positionNumber,forecastDate,targetDate,snp500Return,iKnowFirstAvgReturn,mainTableId) " 
                + "VALUES ( null, '"+2+"', '" // If 1 any if 2 Short (of Long & short)
                + forecastDateStr+"' , '"
                + targetDateStr+"' , '"
                + item.getSNP500Return()+"' , '"
                + item.getAvgReturn2()+"' , '"
                + String.valueOf(mainTableId)+"' );"; 

            csvTableId = insertQueryReturnInt(sql2, dbLocation);

            if(csvTableId!= -1){
                CVSTable csvTable2 = item.getCvsTable2();
                for(RowData row: csvTable2.getCvsTable()){
                    String symbol = row.getSymbol();
                    String prediction = String.valueOf(row.getPrediction());
                    String returnValue = String.valueOf(row.getReturnz());
                    String accuracy = String.valueOf(row.getAccuracy());


                    String csvRowSql = "INSERT INTO "+csvRowName+" (Id,symbol,prediction,returnValue,accuracy,csvTableId) " 
                        + "VALUES ( null,  '" // If 1 any if 2 Short (of Long & short)
                        + symbol+"' , '"
                        + prediction+"' , '"
                        + returnValue+"' , '"
                        + accuracy+"' , '"
                        + String.valueOf(csvTableId)+"' );"; 
                    insertQueryReturnInt(csvRowSql, dbLocation);
                }
            }else{
                return -1;
            }
            
        }
        
        return 0;
        
    }
    private int insertMainTable(ForecastData linkForecastData){//, PackageHeaderList packageHeaderManager){
        Item item = linkForecastData.getItem();
        String title =linkForecastData.getRssEntry().getTitle();
        String link = linkForecastData.getRssEntry().getLink();
        PackageHeader tempPackageHeader= item.getModifiedPackageHeader();//packageHeaderManager.getPackageHeaderWhereNameIs(item.getPackageContent(), item.getSubpackage());
        
        int lastIndexOfFolder = csvDirectory.lastIndexOf("csv_export");
        String csvDirModified = csvDirectory.substring(lastIndexOfFolder);
        
//        String sql = "INSERT INTO "+tableName+" (Id,title,link,positionType,packageNameEng,packageName,subpackageName,csvFilePath,timeFrame,forecastDate,targetDate) " 
//                + "VALUES ( null, '"+linkForecastData.getRssEntry().getTitle()+"', '"
//                + linkForecastData.getRssEntry().getLink()+"' , '"
//                + item.getPositionType()+"' , '"
//                + tempPackageHeader.getPackageName()+"' , '"
//                + tempPackageHeader.getPackageName2()+"' , '"
//                + tempPackageHeader.getSubpackage()+"' , '"
//                + item.getCSVPath(csvDirModified)+"' , '"
//                + item.getTimeFrame()+"' , '"
//                + item.getForecastDay(dateFormat)+"' , '"
//                + item.getTargetDay(dateFormat)+"' );"; 
        
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys;
        int resultId = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
            //conn.setAutoCommit(false);
            System.out.println("Open database success, will write forecast "+ tableName+" title: "+ linkForecastData.getRssEntry().getTitle());
            
            stmt = conn.prepareStatement("INSERT INTO " + tableName 
                    + " (Id,title,link,positionType,packageNameEng,packageName,subpackageName,csvFilePath,timeFrame,forecastDate,targetDate)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            //stmt.setString(1, "null");//Id
            stmt.setString(2, linkForecastData.getRssEntry().getTitle());//title
            stmt.setString(3, linkForecastData.getRssEntry().getLink());//link
            stmt.setString(4, item.getPositionType());//positionType
            stmt.setString(5, tempPackageHeader.getPackageName());//packageNameEng
            stmt.setString(6, tempPackageHeader.getPackageName2());//packageName
            stmt.setString(7, tempPackageHeader.getSubpackage());//subpackageName
            stmt.setString(8, item.getCSVPath(csvDirModified));//csvFilePath
            stmt.setString(9, item.getTimeFrame());//timeFrame
            stmt.setString(10, item.getForecastDay(dateFormat));//forecastDate
            stmt.setString(11, item.getTargetDay(dateFormat));//targetDate
            stmt.executeUpdate();
            
            generatedKeys = stmt.getGeneratedKeys();
            resultId = generatedKeys.getInt(1);
            
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }finally {
            try {
               if (stmt != null) { stmt.close(); }
            }
            catch (Exception e) {
               // log this error
                System.out.println(e.toString());
            }
            try {
               if (conn != null) { conn.close(); }
            }
            catch (Exception e) {
               // log this error
               System.out.println(e.toString());
            }
            System.out.println("MainTable created successfully");
        }
        
        return resultId;
        //return insertQueryReturnInt(sql, dbLocation);
    }
    
    
    
    private int insertForecastData(String sql,String dbName){
        Connection c = null;
        Statement stmt = null;
        ResultSet generatedKeys;
        int resultId = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            
            stmt = c.createStatement();
            stmt.execute(sql);
            generatedKeys = stmt.getGeneratedKeys();
            resultId = generatedKeys.getInt(1);
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }
        System.out.println("Records created successfully");
        
        return resultId;
        
    }
    
    private int insertQueryReturnInt(String sql,String dbName){
        Connection c = null;
        Statement stmt = null;
        ResultSet generatedKeys;
        int resultId = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");
            
            stmt = c.createStatement();
            stmt.execute(sql);
            generatedKeys = stmt.getGeneratedKeys();
            resultId = generatedKeys.getInt(1);
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }
        //System.out.println("Records created successfully");
        
        return resultId;
        
    }
    private void createTableIfNotExists(){
        //Create Main Table
        String sqlMainTable = "CREATE TABLE IF NOT EXISTS "+tableName+" " +
                     "(Id INTEGER PRIMARY KEY     AUTOINCREMENT," +
                     " title           TEXT, " + 
                     " link        TEXT, " + 
                     " positionType        TEXT,"
                + " packageNameEng   TEXT,"
                + " packageName   TEXT,"
                + " subpackageName   TEXT,"
                + " csvFilePath    TEXT ,"
                + " timeFrame    TEXT,"
                + " forecastDate   TEXT,"
                + " targetDate   TEXT,"
                + " UNIQUE (csvFilePath) ON CONFLICT REPLACE)"; 
        //Create CsvTable
        String sqlCsvTable = "CREATE TABLE IF NOT EXISTS "+csvTableName+" " +
                     "(Id INTEGER PRIMARY KEY     AUTOINCREMENT," +
                     " positionNumber    INTEGER, " + 
                     " forecastDate        TEXT, " + 
                     " targetDate        TEXT,"
                + " snp500Return   TEXT,"
                + " iKnowFirstAvgReturn   TEXT,"
                + " mainTableId   INTEGER"
                + " )"; 
        //Create CsvRow
        String sqlCsvRow = "CREATE TABLE IF NOT EXISTS "+csvRowName+" " +
                     "(Id INTEGER PRIMARY KEY     AUTOINCREMENT," +
                     " symbol    TEXT, " + 
                     " prediction        TEXT, " + 
                     " returnValue        TEXT,"
                + " accuracy   TEXT,"
                + " csvTableId   INTEGER"
                + " )"; 
        
//        String sql = "CREATE TABLE IF NOT EXISTS "+tableName+" " +
//                     "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
//                     " title           TEXT, " + 
//                     " link        TEXT, " + 
//                     " positionType        TEXT,"
//                + " packageName   TEXT,"
//                + " subpackageName   TEXT,"
//                + " csvFilePath    TEXT,"
//                + " timeFrame    TEXT,"
//                + " forecastDate   TEXT,"
//                + " targetDate   TEXT,"
//                + " topReturn1   TEXT,"
//                + " topStockName1   TEXT,"
//                + " topReturn2   TEXT,"
//                + " topStockName2   TEXT,"
//                + " topReturn3   TEXT,"
//                + " topStockName3   TEXT,"
//                + " avgReturn1   TEXT,"
//                + " avgReturn2   TEXT,"
//                + " snp500Return   TEXT,"
//                + " marketPremium1    TEXT,"
//                + " marketPremium2    TEXT,"
//                + " accuracy    TEXT,"
//                + " totalNumber    TEXT"
//                + " )"; 
        doQuery(sqlMainTable,dbLocation);
        doQuery(sqlCsvTable,dbLocation);
        doQuery(sqlCsvRow,dbLocation);
        System.out.println("Table : "+ tableName+" created successfully");
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
}



/*

    String title;
    String link;
    String positionType;
    String packageName;
    String csvFilePath;
    
    String timeFrame;
    String forecastDate;
    String targetDate;
    
    Double getTopReturn;
    String getTopStockName;
    
    Double getAvgReturn1;
    Double getAvgReturn2;
    
    Double snp500Return;
    
    Double marketPremium1;
    Double marketPremium2;
    
    String accuracy;
*/