package main;

import DB.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.util.Vector;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
/**
 *
 * @author Daniel
 */
public class TableManager extends JFrame {
    public static final int FILENAME = 0;
    public static final int FORECASTDAY = 1;
    public static final int TARGETDAY = 2;
    public static final int TIMEFRAME = 3;
    public static final int POSITIONTYPE = 4;
    public static final int TOPN = 5;
    public static final int PACKAGE = 6;
    public static final int SUBPACKAGE = 7;
    public static final int KEYWORD1 = 8;
    public static final int KEYWORD1DATE = 9;
    public static final int KEYWORD2 = 10;
    public static final int TOPSTOCKDESCRIPTION = 11;
    public static final int TOPSTOCKDESCRIPTIONNAME = 12;//HIDDEN
    
    
    private DefaultTableModel tableModel;
    //private List<String> topSDName=null;
    
    private static JFileChooser myJFileChooser = new JFileChooser(new File("."));
    
    TableManager(DefaultTableModel tableModel){
        this.tableModel = tableModel;
    }
    TableManager(JTable jTable ){
        tableModel = (DefaultTableModel) jTable.getModel();
        //topSDName = new ArrayList();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="comment">
    /* Order of Data
    String fileName;
    String forecastDay;
    String targetDay;
    String timeFrame;
    String positionType;
    String topN;
    String packageContent;
    String keyword;
    String keyword2;
    String topStockDescription;
    */
    
    /*    public void addRow(String fileName, Item guess){
    String updateDate = guess.getForecastDay();//ForcastUi.dateToString(tempTable.getUpdateDate());
    String targetDate = guess.getTargetDay();//ForcastUi.dateToString(tempTable.getTargetDate());
    Date updateDateDF = ForcastUi.stringToDate(updateDate);
    Date targetDateDF = ForcastUi.stringToDate(targetDate);
    //Guessing Some info
    String timeFrame = ForcastUi.datesToTimeFrame(updateDateDF, targetDateDF);
    String topN = ForcastUi.guessTopN(fileName, guess.getTopN());
    String positionType = ForcastUi.guessPositionType(fileName, guess.getPositionType());
    String packageName = ForcastUi.guessPackageName(fileName);
    
    String topStockSymbol = ForcastUi.guessTopStock(guess);
    String topStockDescription = ForcastUi.getTopStockDescription(topStockSymbol);
    //System.out.println("TOP STOCK ::::: " + topStockSymbol);
    //String packageContent = ForcastUi.guessPackage(fileName);
    
    tableModel.addRow(new Object[]{fileName, updateDate ,targetDate,timeFrame,positionType,topN,packageName,"-","-",topStockDescription,"",topStockSymbol});
    //topSDName.add(topStockSymbol);
    }*/
//</editor-fold>
    public void addRow(String fileName, Item guess, TopStockDescriptionList tdsData, List<String> packageList, KeywordList keywordList,KeywordList keyword2List, PackageHeaderList packageHeaderList, int keyword1DateLimit, int keyword2DateLimit){
        ForcastUi.consoleLog("  Guessing CSV filename : "+fileName);
        
        String updateDate = guess.getForecastDay();//ForcastUi.dateToString(tempTable.getUpdateDate());
        ForcastUi.consoleLog("   Forecast date : "+updateDate);
        String targetDate = guess.getTargetDay();//ForcastUi.dateToString(tempTable.getTargetDate());
        ForcastUi.consoleLog("   Target date : "+targetDate);
        Date updateDateDF = ForcastUi.stringToDate(updateDate);
        Date targetDateDF = ForcastUi.stringToDate(targetDate);
        //Guessing Some info
        String timeFrame = ForcastUi.datesToTimeFrame(updateDateDF, targetDateDF);
        ForcastUi.consoleLog("   timeFrame  : "+timeFrame);
        String topN = guessTopN(fileName, guess.getTopN());
        ForcastUi.consoleLog("   topN  : "+topN);
        String positionType = guessPositionType(fileName, guess.getPositionType());
        ForcastUi.consoleLog("   positionType  : "+positionType);
        String packageName = guessPackageName(fileName, packageList);
        ForcastUi.consoleLog("   packageName  : "+packageName);
        
        // New Part
                
        String topStockSymbol = guessTopStock(guess);
        String topStockDescription = getTopStockDescription(tdsData, topStockSymbol);
        
        Optional<PackageHeader> packageHeaderOptional = packageHeaderList.getPackageHeaderList().stream()
                .filter(e -> e.getPackageName().equalsIgnoreCase(packageName)).findAny();
        String subpackage="ANY";
        if(packageHeaderOptional.isPresent())
            subpackage = packageHeaderOptional.get().getSubpackage().isEmpty() ? "ANY" : packageHeaderOptional.get().getSubpackage();
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -keyword1DateLimit);
        Keyword keyword1 = keywordList.chooseKeyword(packageName, subpackage, positionType, cal.getTime());
        ForcastUi.consoleLog("   keyword1  : "+keyword1.getKeyword());
        ForcastUi.consoleLog("   keyword1 Date : "+keyword1.getDate().toString());
        
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH, -keyword2DateLimit);
        Keyword keyword2 = keyword2List.chooseKeyword(packageName, subpackage, positionType, cal2.getTime());
        ForcastUi.consoleLog("   keyword2  : "+keyword2.getKeyword());
        
        if(subpackage.equalsIgnoreCase("ANY"))
            subpackage ="";
        
        ForcastUi.consoleLog("   subpackage : "+subpackage);
        //System.out.println("TOP STOCK ::::: " + topStockSymbol);
        //String packageContent = ForcastUi.guessPackage(fileName);
        
        tableModel.addRow(new Object[]{fileName, updateDate ,targetDate,timeFrame,positionType,topN,packageName,subpackage, keyword1.getKeyword(), ForcastUi.dateToString(keyword1.getDate()),keyword2.getKeyword(),topStockDescription,topStockSymbol});    
        ForcastUi.consoleLog("Guessing CSV Dones\n");
        //topSDName.add(topStockSymbol);
    }
    public void addRowGeneral(String fileName, Item guess){
        String updateDate = guess.getForecastDay();//ForcastUi.dateToString(tempTable.getUpdateDate());
        String targetDate = guess.getTargetDay();//ForcastUi.dateToString(tempTable.getTargetDate());
        Date updateDateDF = ForcastUi.stringToDate(updateDate);
        Date targetDateDF = ForcastUi.stringToDate(targetDate);
        //Guessing Some info
        String timeFrame = ForcastUi.datesToTimeFrame(updateDateDF, targetDateDF);
        String topN = guessTopN(fileName, guess.getTopN());
        String positionType = guessPositionType(fileName, guess.getPositionType());
        //String packageName = guessPackageName(fileName);
        
        //String topStockSymbol = guessTopStock(guess);
        //String topStockDescription = getTopStockDescription(tdsData, topStockSymbol);
        //System.out.println("TOP STOCK ::::: " + topStockSymbol);
        //String packageContent = ForcastUi.guessPackage(fileName);
        
        tableModel.addRow(new Object[]{fileName, updateDate ,targetDate,timeFrame,positionType,topN});    
        //topSDName.add(topStockSymbol);
    }
    public void addRow(Item addItem){
        String fileName = addItem.getFileName();
        String forecastDay = addItem.getForecastDay();
        String targetDay = addItem.getTargetDay();
        String timeFrame = addItem.getTimeFrame();
        String positionType = addItem.getPositionType();
        String topN = addItem.getTopN();
        String packageContent = addItem.getPackageContent();
        String subpackage = addItem.getSubpackage();
        String keyword = addItem.getKeyword1();
        String keyword1Date = addItem.getKeyword1Date();
        String keyword2 = addItem.getKeyword2();
        String topStockDescription = addItem.getTopStockDescription();
        String topStockDescriptionName = addItem.getTopStockDescriptionName();
        
        tableModel.addRow(new Object[]{fileName, forecastDay, targetDay, timeFrame, positionType, 
                        topN, packageContent,subpackage, keyword, keyword1Date, keyword2, topStockDescription, topStockDescriptionName});
    }
    public void addRowIfNotExist(Vector<Object> ob){
        for(int row = 0; row < tableModel.getRowCount(); row++){
            if(((String)ob.get(0)).equalsIgnoreCase(this.getFileNameAt(row)))
                return;
        }
        tableModel.addRow(ob);
    }
    public Item getRow(int row){
        return new Item(new String[] {(String)tableModel.getValueAt(row,0), 
            (String)tableModel.getValueAt(row,1), (String)tableModel.getValueAt(row,2),
            (String)tableModel.getValueAt(row,3), (String)tableModel.getValueAt(row,4),
            (String)tableModel.getValueAt(row,5), (String)tableModel.getValueAt(row,6),
            (String)tableModel.getValueAt(row,7), (String)tableModel.getValueAt(row,8),
            (String)tableModel.getValueAt(row,9), (String)tableModel.getValueAt(row,10),
            (String)tableModel.getValueAt(row, 11), (String)tableModel.getValueAt(row, 12)});
        
        /*
        return new String[]{(String)tableModel.getValueAt(row,0), 
            (String)tableModel.getValueAt(row,1), (String)tableModel.getValueAt(row,2),
            (String)tableModel.getValueAt(row,3), (String)tableModel.getValueAt(row,4),
            (String)tableModel.getValueAt(row,4), (String)tableModel.getValueAt(row,5),
            (String)tableModel.getValueAt(row,6), (String)tableModel.getValueAt(row,7)};
        */
    }
    
//    public String getRowTSDName(int row){
//        return topSDName.get(row);
//    }
    public int getRowCount(){
        return tableModel.getRowCount();
    }
    public int getColumnCount(){
        return tableModel.getColumnCount();
    }
    
    public boolean checkFileNameExist(String name){
        for(int i = 0; i < tableModel.getRowCount(); i++){
            if(tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
    public String getFileNameAt(int row){
        return (String)tableModel.getValueAt(row,FILENAME);
    }
    public String getPositionTypeAt(int row){
        return (String)tableModel.getValueAt(row,4);
    }
    
    public DefaultTableModel getTableModel(){
        return tableModel;
    }
    public void setTableModel(DefaultTableModel loaded){
        for(int i = 0; i < loaded.getRowCount(); i++){
            TableManager tempTableManager = new TableManager(loaded);
            Item tempItem = tempTableManager.getRow(i);
            
            this.addRow(tempItem);
            
        }
        
        //tableModel.setDataVector(data, data);
    }
    
    public void setRow(Item aRow,int row){
        if((tableModel.getValueAt(row, FILENAME)==null) || (tableModel.getValueAt(row, FILENAME).toString().equalsIgnoreCase("")) )
            tableModel.setValueAt(aRow.getFileName(), row, FILENAME); // FileName not overwritten anymore
        tableModel.setValueAt(aRow.getForecastDay(), row, FORECASTDAY);
        tableModel.setValueAt(aRow.getTargetDay(), row, TARGETDAY);
        tableModel.setValueAt(aRow.getTimeFrame(), row, TIMEFRAME);
        tableModel.setValueAt(aRow.getPositionType(), row, POSITIONTYPE);
        tableModel.setValueAt(aRow.getTopN(), row, TOPN);
        tableModel.setValueAt(aRow.getPackageContent(), row, PACKAGE);
        tableModel.setValueAt(aRow.getSubpackage(), row, SUBPACKAGE);
        tableModel.setValueAt(aRow.getKeyword1(), row, KEYWORD1);
        tableModel.setValueAt(aRow.getKeyword1Date(), row, KEYWORD1DATE);
        tableModel.setValueAt(aRow.getKeyword2(), row, KEYWORD2);
        tableModel.setValueAt(aRow.getTopStockDescription(), row, TOPSTOCKDESCRIPTION);
        
        tableModel.setValueAt(aRow.getTopStockDescriptionName(), row, TOPSTOCKDESCRIPTIONNAME);
    }
    public void setGeneralRow(Item aRow,int row){
        tableModel.setValueAt(aRow.getForecastDay(), row, 1);
        tableModel.setValueAt(aRow.getTargetDay(), row, 2);
        tableModel.setValueAt(aRow.getTimeFrame(), row, 3);
        tableModel.setValueAt(aRow.getPositionType(), row, 4);
        tableModel.setValueAt(aRow.getTopN(), row, 5);
    }
    public void clearAll(){
        tableModel.setRowCount(0);
        System.out.println("Cleared!");
    }
    
    public void saveTable(){
        if (myJFileChooser.showSaveDialog(this) ==
                JFileChooser.APPROVE_OPTION ) {
            saveTable(myJFileChooser.getSelectedFile());
        }
    }
    private void saveTable(File file) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(file));
                out.writeObject(tableModel.getDataVector());
                out.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static DefaultTableModel loadTable(TableManager tableManager, JFrame jFrame, CategorieSlugList categorieSlugList){
        
        if (myJFileChooser.showOpenDialog(jFrame) == JFileChooser.APPROVE_OPTION ){
            
            DefaultTableModel tempTableModel;            
            tempTableModel = loadTable(myJFileChooser.getSelectedFile(), tableManager);
            
            int rowCount = tempTableModel.getRowCount();
            for(int i = 0; i<rowCount; i++){
                if(!categorieSlugList.checkIfExist(tempTableModel.getValueAt(i, 6).toString()))
                    tempTableModel.setValueAt((Object)"-", i, 6);
            }
            
            return tempTableModel;
        }else
            return null;
    }
     
    private static DefaultTableModel loadTable(File file, TableManager tableManager) {
        int columnCount = tableManager.getColumnCount();
        DefaultTableModel tempTableModel = new DefaultTableModel(0,columnCount);
        
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            Vector rowData = (Vector)in.readObject();
            Iterator itr = rowData.iterator();
            while(itr.hasNext()) {
                Vector aRow = (Vector)itr.next();
                if(tableManager.checkFileNameExist(aRow.get(0).toString()))
                    tempTableModel.addRow(aRow);
            }
            in.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return tempTableModel;
    }
    
    public List<String> getAllFileName(){
        List<String> names = new ArrayList();
        for(int i = 0 ; i < tableModel.getRowCount(); i++){
            names.add((String)tableModel.getValueAt(i, 0));
        }
        return names;
    }
    
    private String guessTopN(String fileName, String finalGuess){
        fileName = fileName.toLowerCase();
        if(fileName.contains("top_10")){
            return "10";
        }else if(fileName.contains("top_20")){
            return "20";
        }

        return finalGuess;
    }
    private String guessPositionType(String fileName, String finalGuess){
        fileName = fileName.toLowerCase();
        if(fileName.contains("long")){
            if(fileName.contains("short"))
                return "Long & Short";
            else
                return "Long";
        }else if(fileName.contains("short")){
            return "Short";
        }else if(fileName.contains("commodities")){
            return "Commodities";
        }else if(fileName.contains("currencies")){
            return "Currencies";
        }else if(fileName.contains("XAU")){
            return "XAU";
        }else{
            return finalGuess;
        }
        
    }
    private String guessPackageName(String fileName, List<String> packageList){

        String modified = fileName.toLowerCase().replace("sp500", "s&p500");
        int cutHere = modified.lastIndexOf("_");
        modified = modified.substring(0,cutHere);
        modified = modified.replaceAll("_"," ");
        modified = modified.replace("ikforecast ","");
        
        int max = 0;
        int maxIndex = 0;
        int []dis = new int [packageList.size()];
        
        for(int i = 1 ; i < packageList.size() ; i++ ){
            dis[i] = LongestCommonSubsequence.lcs(modified, packageList.get(i).toLowerCase().replace("forecast", ""));
            //System.out.println("Comparing : "+ modified +" VS "+ packageList.get(i).toLowerCase().replace("forecast", "") +" = " +dis[i]);
            if(dis[i] > max){
                max = dis[i];
                maxIndex = i;
            }
        }
        return packageList.get(maxIndex);
    }
    private String guessTopStock(Item guess){
        String returnTopStockSymbol = "";
        RowData topStockSymbol = null;
        
        if(guess.getPositionType().equalsIgnoreCase("Long")){
            topStockSymbol = guess.getCvsTable().getHighest(1);
        }else if(guess.getPositionType().equalsIgnoreCase("Short")){
            topStockSymbol = guess.getCvsTable().getHighest(1);
        }else if(guess.getPositionType().equalsIgnoreCase("Long & Short")){
            if(guess.getCvsTable().getHighest(1).getReturnz() >= guess.getCvsTable2().getHighest(1).getReturnz())
                topStockSymbol = guess.getCvsTable().getHighest(1);
            else
                topStockSymbol = guess.getCvsTable2().getHighest(1);
        }
        
        returnTopStockSymbol = topStockSymbol.getSymbol();
        return returnTopStockSymbol;
    }
    private String getTopStockDescription(TopStockDescriptionList tsdData,String symbol){
        TopStockDescription tSD =  tsdData.selectRowByShortName(symbol); //sqliteTSD.selectRowWith("SHORTNAME", symbol);
        if(tSD != null)
            return tSD.getContentText();
        else
            return "";
    }
}