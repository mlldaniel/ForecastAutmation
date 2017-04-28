/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DB.PreferenceSettings;
import DB.TopStockDescriptionList;
import DB.*;
import Library.TableColumnAdjuster;
import java.awt.Component;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import static main.ForcastUi.consoleLog;
import rss.ForecastData;

/**
 *
 * @author Daniel
 */
public class Tab {
    static int currentTab = 0;
    static int selectedRow = -1;
    
    private String tabName;
    private int tabNumber;
    private boolean generateToXML;
    
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTable jTable;
    
    public TableManager tableManager;
    public List<Item> itemList;
    List<CVSTable> cvsTableList;
    
    static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static String workingDirectory  = System.getProperty("user.dir");
    
    public CategorieSlugList categorieSlugList= null;
    private List<String> packageList = new ArrayList();// For Form UI
    private PackageHeaderList packageHeaderList= null;
    public BottomTextRotationList bottomTextRotationList = null;
    public BottomTextRotationANYList bottomTextRotationANYList = null;
    private TopStockDescriptionList tsdData = null;
    private SubpackageInventoryList spkgInventoryList = null;
    
    private PreferenceSettings preferenceSettings = null;
    private Dictionary timeFrameDictionary = null;
    private Dictionary positionTypeDictionary = null;
    
    private PostFormList postFormList = null;
    private KeywordList keywordList = null;
    private KeywordList keyword2List = null;
    
    String bottomTextRotationANYListDir ="";
    String bottomTextRotationListDir = "";
    String categorieSlugListDir = "";
    String packageHeaderListDir = "";
    public String topStockDescriptionListDir = "";

    String positionTypeDictionaryDir = "";
    String timeFrameDictionaryDir = "";

    String postFormListDir = "";
    
    public String keywordListDir = "";
    public String keyword2ListDir = "";
    
    public String keywordListTestDir = "";
    
    
    //output
    String keywordHistoryDir = "";
    String outputXMLDir = "";
    
    public String linkForecastDataDir = "";
    public LinkForecastDataManager linkForecastDataManager;
    public String csvDirectory="";
    
    static String slash =  File.separator;
    public Tab(PreferenceSettings preferenceSettings, int tabNumber){
        
        
        this.tabName = preferenceSettings.getLanguage();
        this.tabNumber = tabNumber;
        this.generateToXML = false;
        
        consoleLog("Opening: "+ tabName);
        
        //preferenceSettingDir = slash+"resources"+slash+tabName+slash+"PreferenceSettings.xml";
        bottomTextRotationANYListDir = slash+"resources"+slash+tabName+slash+"BottomTextRotationANY.xml";
        bottomTextRotationListDir = slash+"resources"+slash+tabName+slash+"BottomTextRotation.xml";
        categorieSlugListDir = slash+"resources"+slash+tabName+slash+"Categories_slugs.csv";
        packageHeaderListDir = slash+"resources"+slash+tabName+slash+"Package Header Text Database2.txt";
        topStockDescriptionListDir = slash+"resources"+slash+tabName+slash+"I Know First Company Names.xlsx";
        
        positionTypeDictionaryDir = slash+"resources"+slash+tabName+slash+"dic"+slash+"positionType.csv";
        timeFrameDictionaryDir = slash+"resources"+slash+tabName+slash+"dic"+slash+"timeFrame.csv";
        
        postFormListDir = slash+"resources"+slash+tabName+slash+"PostFormFile.xml";
        
        keywordListDir = slash+"resources"+slash+tabName+slash+"keywords"+slash+"Keyword1List.xlsx";
        keyword2ListDir = slash+"resources"+slash+tabName+slash+"keywords"+slash+"Keyword2List.xlsx";
        //keywordListDir = slash+"resources"+slash+tabName+slash+"keywords"+slash+"Keyword1List.csv";
        //keywordListTestDir = slash+"resources"+slash+tabName+slash+"keywords"+slash+"(test)keywords2.csv";
        //Output 
        keywordHistoryDir = slash+"output"+slash+ tabName +slash+"keywordHistory"+slash+"Keyword_History.csv";
        outputXMLDir =slash+"output"+slash+tabName+slash;
        
        this.preferenceSettings = preferenceSettings;
        positionTypeDictionary = new Dictionary(workingDirectory + positionTypeDictionaryDir);
        timeFrameDictionary = new Dictionary(workingDirectory + timeFrameDictionaryDir);
//        preferenceSettings.readFromFileXml(workingDirectory+preferenceSettingDir);
        
        bottomTextRotationANYList = BottomTextRotationANYList.readFromFile(workingDirectory + bottomTextRotationANYListDir);
        bottomTextRotationList = BottomTextRotationList.readFromFile(workingDirectory + bottomTextRotationListDir);
        
        categorieSlugList = CategorieSlugList.readFromCVS(workingDirectory + categorieSlugListDir);
        packageList = categorieSlugList.getNameAll();
        packageHeaderList = new PackageHeaderList();
        packageHeaderList.readFromFile(workingDirectory + packageHeaderListDir, packageList);
        spkgInventoryList = new SubpackageInventoryList(getPackageHeaderList().getPackageHeaderList());
        packageList.add("-");
        
        tsdData = new TopStockDescriptionList(TopStockDescriptionList.readFromFileExcel(workingDirectory + topStockDescriptionListDir));
        
        postFormList = new PostFormList(workingDirectory + postFormListDir);
        
        //keywordList = new KeywordList(workingDirectory + keywordListDir);
        keywordList = new KeywordList(workingDirectory + keywordListDir);
        keyword2List = new KeywordList(workingDirectory + keyword2ListDir);
        
        //keywordList.printKeywordList("Keyword 1");
        //keyword2List.printKeywordList("Keyword 2");
        
        tableManager = null;
        itemList = new ArrayList();
        cvsTableList = new ArrayList();
        
        linkForecastDataDir = slash+"output"+slash+tabName+slash+"LinkForecastData"+ slash + "sqlitedb.db";
        linkForecastDataManager = new LinkForecastDataManager(workingDirectory + linkForecastDataDir);
        
        ForcastUi.consoleLog("All Resources Opened for this Language \n==================================================");
    }
    public Component makePanel(){
        jTable = new javax.swing.JTable();
        jScrollPane = new javax.swing.JScrollPane();
        
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Forecast day", "Target Day", "Time Frame", "Position Type", "Top N", "Package","Subpackage", "Keyword1", "Date Last Used", "Keyword2", "Top Stock Description", "Top Stock Description Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableMousePressed(evt);
            }
        });
        jScrollPane.setViewportView(jTable);
        TableColumn[] colToDeleteList= 
            new TableColumn[]{jTable.getColumnModel().getColumn(TableManager.TOPSTOCKDESCRIPTIONNAME)
                            ,jTable.getColumnModel().getColumn(TableManager.FORECASTDAY)
                            ,jTable.getColumnModel().getColumn(TableManager.TARGETDAY)
                            ,jTable.getColumnModel().getColumn(TableManager.TIMEFRAME)
                            ,jTable.getColumnModel().getColumn(TableManager.TOPN)};
        for(TableColumn colToDelete : colToDeleteList)
            jTable.removeColumn(colToDelete);
        jTable.validate();
        
        
        tableManager = new TableManager(jTable);
        return jScrollPane;
    }
//    public void resizeToFit(){
//        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        TableColumnAdjuster tca = new TableColumnAdjuster(jTable);
//        tca.adjustColumns();
//    }
    
    public void jTableMousePressed(java.awt.event.MouseEvent evt) {
        ForcastUi.enableForm();
        ForcastUi.tableRowSelected(jTable, tableManager);
        updateViewCSVFile();
    }
    public void updateViewCSVFile(){
        if(selectedRow>=0){
            Item selectedItem = ForcastUi.itemList.get(selectedRow);
            if(selectedItem.getCvsTable()!=null && selectedItem.getCvsTable2()!=null)
                ForcastUi.jLabelViewCSV.setToolTipText("<html>"+selectedItem.getCvsTable().printView() + selectedItem.getCvsTable2().printView()+"</html>");
            else if(selectedItem.getCvsTable()!=null)
                ForcastUi.jLabelViewCSV.setToolTipText("<html>"+selectedItem.getCvsTable().printView()+"</html>");
        }
    }
    public void clearTableNCVS(){
        if(tableManager != null){
            tableManager.clearAll();
            cvsTableList.clear();
            selectedRow = -1;
            ForcastUi.disableForm();
        }
        
    }
    public String positionTypeKeyIndexToValue(int index ){
        return preferenceSettings.getPositionTypeDropDownList()[index];
    }
    
    public Item returnTranslatedVersion(Item item){
        String timeFrame = item.getTimeFrame();
        String positionType = item.getPositionType();
        
        timeFrame = getTimeFrameDictionary().findValue(timeFrame);
        positionType = getPositionTypeDictionary().findValue(positionType);
        
        item.setTimeFrame(timeFrame);
        item.setPositionType(positionType);
        return item;
    }
    public void updateTableOrderFromGeneral(Item inputItem, int selectedRow){
        tableManager.setGeneralRow(inputItem,selectedRow);
    }
    /**
     * @return the tabName
     */
    public String getTabName() {
        return tabName;
    }

    /**
     * @param tabName the tabName to set
     */
    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    /**
     * @return the tabNumber
     */
    public int getTabNumber() {
        return tabNumber;
    }

    /**
     * @param tabNumber the tabNumber to set
     */
    public void setTabNumber(int tabNumber) {
        this.tabNumber = tabNumber;
    }

    /**
     * @return the jTable
     */
    public javax.swing.JTable getjTable() {
        return jTable;
    }

    /**
     * @return the tableManager
     */
    public TableManager getTableManager() {
        return tableManager;
    }

    /**
     * @param tableManager the tableManager to set
     */
    public void setTableManager(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    /**
     * @return the preferenceSettings
     */
    public PreferenceSettings getPreferenceSettings() {
        return preferenceSettings;
    }
    /**
     * @return the generateTable
     */
    public boolean isGenerateToXML() {
        return generateToXML;
    }

    /**
     * @param generateTable the generateTable to set
     */
    public void setGenerateToXML(boolean generateToXML) {
        this.generateToXML = generateToXML;
    }

    /**
     * @return the tsdData
     */
    public TopStockDescriptionList getTsdData() {
        return tsdData;
    }

    /**
     * @return the postFormList
     */
    public PostFormList getPostFormList() {
        return postFormList;
    }

    public List<String> getPackageList() {
        return packageList;
    }

    /**
     * @return the spkgInventoryList
     */
    public SubpackageInventoryList getSpkgInventoryList() {
        return spkgInventoryList;
    }

    /**
     * @param spkgInventoryList the spkgInventoryList to set
     */
    public void setSpkgInventoryList(SubpackageInventoryList spkgInventoryList) {
        this.spkgInventoryList = spkgInventoryList;
    }

    /**
     * @return the keywordList
     */
    public KeywordList getKeywordList() {
        return keywordList;
    }

    /**
     * @return the packageHeaderList
     */
    public PackageHeaderList getPackageHeaderList() {
        return packageHeaderList;
    }

    /**
     * @return the keyword2List
     */
    public KeywordList getKeyword2List() {
        return keyword2List;
    }
    
    
//    public void updateForecastDataManager1(){
//        linkForecastDataManager.insertItemList(itemList);
//    }  
//    public void updateForecastDataManager2(List<rss.RSSEntry> entries){
//        linkForecastDataManager.insertRSSEntries(entries);
//    }
    
    public void updateForecastDBList(List<ForecastData> forecastDBList) {
        linkForecastDataManager.insertForecastDataList(forecastDBList);
    }
    
    public void storeForecastDataList(){
        linkForecastDataManager.dbUpdateToDB(this);
    }
    public void emptyForecastDataList(){
        linkForecastDataManager.emptyDataList();
    }

    /**
     * @return the timeFrameDictionary
     */
    public Dictionary getTimeFrameDictionary() {
        return timeFrameDictionary;
    }

    /**
     * @return the positionTypeDictionary
     */
    public Dictionary getPositionTypeDictionary() {
        return positionTypeDictionary;
    }

    

}
