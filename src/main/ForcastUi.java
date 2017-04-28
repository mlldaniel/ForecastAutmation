package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DB.ErrorMessages;
import DB.TopStockDescription;
import DB.KeywordHistoryTracker;
import DB.PreferenceSettings;
import DB.TopStockDescriptionList;
import Library.TableColumnAdjuster;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import static java.lang.Math.toIntExact;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import rss.TestRSSWriterVarImprovement;



/**
 *
 * @author Daniel
 */
public class ForcastUi extends javax.swing.JFrame{

    /**
     * Creates new form ForcastUi
     */
    //DefaultTableModel tableModel;
    TableManager tableManager;
    static List<Item> itemList = new ArrayList();
    List<CVSTable> cvsTableList;
    
    static boolean updateEnabled = true;
    static boolean editGeneral = true;
    static boolean opened = false;
    static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    
    //CategorieSlugList categorieSlugList= new CategorieSlugList();
    //static List<String> packageList = new ArrayList();// For Form UI
    //PackageHeaderList packageHeaderList= null;
    //BottomTextRotationList bottomTextRotationList = null;
    //BottomTextRotationANYList bottomTextRotationANYList = null;
    //static SubpackageInventoryList spkgInventoryList = null;
//<editor-fold defaultstate="collapsed" desc="comment">
    /*
    { "-","52 Week High Stocks", "52 Week Low Stocks", "Aggressive Stocks Forecast", "Automative Stock Forecast",
    "Bank Forecast Stock","Basic Industry Forecast", "Biotech Stock Forecast", "Brazil Stock Forecast", "Canadian Stock Forecast",
    "Chemicals Stock Forecast", "Chinese Stock Forecast", "Computer Industry", "Conservative Stock Forecast", "Consumer Stocks",
    "Currency Forecast", "Dividend Stocks Forecast", "Energy Stocks Forecast", "ETF's Forecast", "European Stock Forecast",
    "French Stock Market", "Fundamental", "German Stock Forecast", "Gold & Commodity Forecast", "Healthcare", "Hedge Fund Stocks",
    "Home Builders", "Hong Kong Stock Forecast", "Indices Forecast", "Insider Trades", "Insurance Companies Forecast",
    "Interest Rates Forecast","International Stocks","Israeli Stocks","Medicine Stocks","Mega Cap Forecast","Microsoft Stock Forecast",
    "MLP Stocks","Oil Forecast","Options","Pharma Stocks Forecast","S&P 100 Stocks","Small Cap Forecast","Stock Forecast & S&P 500 Forecast",
    "Stocks Under $10","Stocks Under $5","Tech Stocks Forecast","Transportation Stocks","UK Stock Forecast","US Sector Indices Forecast",
    "Volatility Forecast"};
    */
//</editor-fold>
    //Preference
    
    static List<Tab> tabList = null;
    //static int selectedTab = 0;
    static List<PreferenceSettings> preferenceSettingsList = null;
    static PreferenceSettings generalSetting = null;
//<editor-fold defaultstate="collapsed" desc="comment">
    /*static String[] timeFrameDropDownList = new String[] { "-","3 Days", "7 Days", "14 Days", "1 Month", "3 Months", "1 Year" };
    static String[] positionTypeDropDownList = new String[] { "-","Long", "Short", "Long & Short", "Commodities", "Currencies","XAU" };
    static String[] fundamentalSubpackage = new String[] {"","Low P/E Stocks","High P/E Stocks"
    ,"Low PEG Stocks","High PEG Stocks"
    ,"Low price-to-book ratio Stocks","High price-to-book ratio Stocks","Low price-to-sales ratio Stocks"
    ,"High price-to-sales ratio Stocks","Low High Short Ratio Stocks","High Short Ratio Stocks"};
    static String[] goldNCommonditiesSubpackage = new String[] {"","Gold","Commodities"};
    */
//</editor-fold>
    String choosenDirectory=null;
    static int selectedRow = -1;
    static boolean exported = false;
    //static String dbName ="AutomationDB";
    //static SQLiteTopStockDescription sqliteTSD = null;
    //static TopStockDescriptionList tsdData;
    
    static String workingDirectory  = "";
    
    public static String slash = File.separator;    
    static String preferenceSettingDir = (slash+"resources"+slash+"PreferenceSettings.xml");
    static String keywordHistoryDir = (slash+"resources"+slash+"Keyword_History.csv");
    
    public final int KEYWORD1 = 0;
    public final int KEYWORD2 = 1;
    public int genNumber = -1;
    
    public static String OS = System.getProperty("os.name").toLowerCase();
    
    public ForcastUi(javax.swing.JEditorPane jEditorPane){
            jEditorPane1 = jEditorPane;
    }
    public ForcastUi() {
        workingDirectory = System.getProperty("user.dir");
        
        preferenceSettingsList = PreferenceSettings.readFromFileXml(workingDirectory+preferenceSettingDir);
        generalSetting = preferenceSettingsList.get(0);//Edit Here
        
        //Creating UI
        initComponents();
        disableForm();
        saveLoadButtonsEnabled(false);
        
        // Other Tabs
        tabList = new ArrayList();
        for(int i = 0; i < preferenceSettingsList.size(); i++){
            tabList.add(new Tab(preferenceSettingsList.get(i),i));
            jTabbedPaneTab.add(tabList.get(i).getTabName() , tabList.get(i).makePanel());
        }
        jLabel1.setText(jLabel1.getText()+" - "+OS);
        if(isMac()){
            InputMap im = (InputMap) UIManager.get("TextField.focusInputMap");
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.META_DOWN_MASK), DefaultEditorKit.cutAction);
        }
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                String def = "Are you sure to close?";
                String ask = "";
                if(!exported)
                    ask += def +" It has not been Exported in this session!";
                else
                    ask = def;
                
                if (JOptionPane.showConfirmDialog(null, 
                    ask, "Really Closing?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                        System.exit(0);
                    }
            }
        });
    }
    
        public static boolean isWindows() {
            return (OS.contains("win"));
	}
	public static boolean isMac() {
            return (OS.contains("mac"));
	}
    javax.swing.JScrollPane jPaneTest;
    javax.swing.JTable jTableTest;
    public static Date stringToDate(String stringDateFormat){
	//dateFormat
	Date convertedDateFormat = null;
	try{
            convertedDateFormat = dateFormat.parse(stringDateFormat);
	}catch (ParseException e) {
            e.printStackTrace();
	}
	return convertedDateFormat;
    }
    public static String dateToString(Date dateDateFormat){
        String stringDateFormat = dateFormat.format(dateDateFormat);
        return stringDateFormat;
    }
    public static String datesToTimeFrame(Date startDate, Date endDate){

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        int select = toIntExact(diffDays);
        
        switch(select){
            case 3:
                return generalSetting.getTimeFrameDropDownList()[1];
            case 7:
                return generalSetting.getTimeFrameDropDownList()[2];
            case 14:
                return generalSetting.getTimeFrameDropDownList()[3];
            case 28:
            case 29:
            case 30:
            case 31:
                return generalSetting.getTimeFrameDropDownList()[4];
            case 89:
            case 90:
            case 91:
            case 92:
                return generalSetting.getTimeFrameDropDownList()[5];
            case 365:
            case 366:
                return generalSetting.getTimeFrameDropDownList()[6];
            default:
                return generalSetting.getTimeFrameDropDownList()[0];
        }
    }
    
//<editor-fold defaultstate="collapsed" desc="comment">
//    public static String guessTopN(String fileName, String finalGuess){
//        fileName = fileName.toLowerCase();
//        if(fileName.contains("top_10")){
//            return "10";
//        }else if(fileName.contains("top_20")){
//            return "20";
//        }
//
//        return finalGuess;
//    }
//    public static String guessPositionType(String fileName, String finalGuess){
//        fileName = fileName.toLowerCase();
//        if(fileName.contains("long")){
//            if(fileName.contains("short"))
//                return "Long & Short";
//            else
//                return "Long";
//        }else if(fileName.contains("short")){
//            return "Short";
//        }else if(fileName.contains("commodities")){
//            return "Commodities";
//        }else if(fileName.contains("currencies")){
//            return "Currencies";
//        }else{
//            return finalGuess;
//        }
//
//    }
//    public static String guessPackageName(String fileName){
//
//        String modified = fileName.toLowerCase().replace("sp500", "s&p500");
//        int cutHere = modified.lastIndexOf("_");
//        modified = modified.substring(0,cutHere);
//        modified = modified.replaceAll("_"," ");
//        modified = modified.replace("ikforecast ","");
//
//        int max = 0;
//        int maxIndex = 0;
//        int []dis = new int [packageList.size()];
//
//        for(int i = 1 ; i < packageList.size() ; i++ ){
//            dis[i] = LongestCommonSubsequence.lcs(modified, packageList.get(i).toLowerCase().replace("forecast", ""));
//            //System.out.println("Comparing : "+ modified +" VS "+ packageList.get(i).toLowerCase().replace("forecast", "") +" = " +dis[i]);
//            if(dis[i] > max){
//                max = dis[i];
//                maxIndex = i;
//            }
//        }
//        return packageList.get(maxIndex);
//        /*
//        int cutHere = modified.lastIndexOf("_");
//        modified = modified.substring(0,cutHere);
//        modified = modified.replaceAll("_"," ");
//        modified = modified.replace("ikforecast ","");
//
//        int []dis = new int [packageList.size()];
//        int min = 1000;
//        int minIndex = 0;
//        for(int i = 1 ; i < packageList.size() ; i++ ){
//            dis[i] = LevenshteinDistance.distance(modified, packageList.get(i).toLowerCase().replace("forecast", ""));
//            System.out.println("Comparing : "+ modified +" VS "+ packageList.get(i).toLowerCase().replace("forecast", "") +" = " +dis[i]);
//            if(dis[i] < min){
//                min = dis[i];
//                minIndex = i;
//            }
//        }
//
//        return packageList.get(minIndex);
//        */
//    }
//    public static String guessTopStock(Item guess){
//        String returnTopStockSymbol = "";
//        RowData topStockSymbol = null;
//
//        if(guess.getPositionType().equalsIgnoreCase("Long")){
//            topStockSymbol = guess.getCvsTable().getHighest(1);
//        }else if(guess.getPositionType().equalsIgnoreCase("Short")){
//            topStockSymbol = guess.getCvsTable().getHighest(1);
//        }else if(guess.getPositionType().equalsIgnoreCase("Long & Short")){
//            if(guess.getCvsTable().getHighest(1).getReturnz() >= guess.getCvsTable2().getHighest(1).getReturnz())
//                topStockSymbol = guess.getCvsTable().getHighest(1);
//            else
//                topStockSymbol = guess.getCvsTable2().getHighest(1);
//        }
//
//        returnTopStockSymbol = topStockSymbol.getSymbol();
//        return returnTopStockSymbol;
//    }
//    public static String getTopStockDescription(String symbol){
//        TopStockDescription tSD =  tsdData.selectRowByShortName(symbol); //sqliteTSD.selectRowWith("SHORTNAME", symbol);
//        if(tSD != null)
//            return tSD.getContentText();
//        else
//            return "";
//    }
//
//</editor-fold>
    
    //Check if it is CSV file
    public static boolean fileExtCheck(String fileName){
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        
        return ext.equalsIgnoreCase("csv");
    }
    
    
    public void updateSubpackageItemTop(){
        if(editGeneral){
            //updateSubpackageItem();
        }else{
            updateSubpackageItemFromTab(Tab.currentTab);
        }
    }
//<editor-fold defaultstate="collapsed" desc="comment">
//    public void updateSubpackageItem(){
//        if(selectedRow == -1)
//            return;
//
//
//        selectedRow = jTable1.getSelectedRow();
//        Item aRow = tableManager.getRow(selectedRow);
//        DefaultComboBoxModel model = new DefaultComboBoxModel(spkgInventoryList.getSubpackageList(aRow.getPackageContent()).toArray());
////
////        switch(aRow.getPackageContent()){
////            case "Gold & Commodity Forecast":
////                model = new DefaultComboBoxModel( generalSetting.getGoldNCommonditiesSubpackage() );
////                break;
////            case "Fundamental":
////                model = new DefaultComboBoxModel( generalSetting.getFundamentalSubpackage() );
////                break;
////            default:
////                model = new DefaultComboBoxModel( new String[]{""} );
////        }
//        jComboBoxSubpackage.setModel(model);
//    }
//</editor-fold>
    
    public static void updateSubpackageItemFromTab(int tabNumber){
        if(Tab.selectedRow == -1)
            return;
        Tab tab = tabList.get(tabNumber);
        Tab.selectedRow = tab.getjTable().getSelectedRow();
        Item aRow = tab.getTableManager().getRow(Tab.selectedRow);
        
        DefaultComboBoxModel model = new DefaultComboBoxModel(tab.getSpkgInventoryList().getSubpackageList(aRow.getPackageContent()).toArray());

        jComboBoxSubpackage.setModel(model);
    }
    public void updateTableTop(){
        if(updateEnabled)
            if(editGeneral){
                updateTableGeneral();
            }else{
                updateTableFromTab(Tab.currentTab);
            }
    }
    public void updateTableGeneral(){
        if(selectedRow == -1)
            return;
        
        Item itemTemp = Item.emptyItemConstructor();
        itemTemp.setFileName(jLabelFileName.getText());
        itemTemp.setForecastDay(dateToString(jXDatePickerForecastDay.getDate()));
        itemTemp.setTargetDay(dateToString(jXDatePickerTargetDay.getDate()));
        itemTemp.setTimeFrame((String)jComboBoxTimeFrame.getSelectedItem());
        itemTemp.setPositionType((String)jComboBoxPositionType.getSelectedItem());
        itemTemp.setTopN(jTextFieldTopN.getText());
        //itemTemp.setKeyword(jTextFieldKeyword.getText());
        //itemTemp.setKeyword2(jTextFieldKeyword2.getText());
        //itemTemp.setPackageContent(jListPackage.getSelectedValue());
        //itemTemp.setTopStockDescription(jTextAreaTopStockDescription.getText());
        //itemTemp.setSubpackage(jTextFieldSubpackage.getText());
        //itemTemp.setSubpackage((String)jComboBoxSubpackage.getSelectedItem());
        //itemTemp.setTopStockDescriptionName(jTextFieldTopStockDescriptionShortName.getText());
        
        tableManager.setRow(itemTemp,selectedRow);
        updateTableFromGeneral(itemTemp, selectedRow);
        
    }
    public void updateTableFromGeneral(Item inputItem, int selectedRow){
        tabList.stream().forEach((tab) -> {
            tab.updateTableOrderFromGeneral(inputItem,selectedRow);
        });
    }
    public void updateTableFromTab(int tabNumber){
        if(selectedRow == -1)
            return;
        Tab tab = tabList.get(tabNumber);
        if(tab != null){
            String keyword1 = jTextFieldKeyword.getText().trim();
            Date keyword1Date = tab.getKeywordList().returnDateIfExist(keyword1);
            String keyword1DateStr = "";
            if(keyword1Date != null)
                keyword1DateStr = ForcastUi.dateToString(keyword1Date);
            Item itemTemp = new Item();
            itemTemp.setFileName(jLabelFileName.getText());
            itemTemp.setForecastDay(dateToString(jXDatePickerForecastDay.getDate()));
            itemTemp.setTargetDay(dateToString(jXDatePickerTargetDay.getDate()));
            itemTemp.setTimeFrame((String)jComboBoxTimeFrame.getSelectedItem());
            itemTemp.setPositionType((String)jComboBoxPositionType.getSelectedItem());
            itemTemp.setTopN(jTextFieldTopN.getText());
            itemTemp.setPackageContent(jListPackage.getSelectedValue());
            itemTemp.setSubpackage((String)jComboBoxSubpackage.getSelectedItem());
            itemTemp.setKeyword1(keyword1);
            itemTemp.setKeyword1Date(keyword1DateStr);
            itemTemp.setKeyword2(jTextFieldKeyword2.getText().trim());
            itemTemp.setTopStockDescription(jTextAreaTopStockDescription.getText());
            
            itemTemp.setTopStockDescriptionName(jTextFieldTopStockDescriptionShortName.getText());
            
            TableManager currentTableManager = tab.getTableManager();
            currentTableManager.setRow(itemTemp,selectedRow);
        }
        
        
    }
    public static void updateEnable(boolean allow){
        updateEnabled = allow;
    }
    public static void enableForm(){
        jLabelFileName.setEnabled(true);
        jXDatePickerForecastDay.setEnabled(true);
        jXDatePickerTargetDay.setEnabled(true);
        jComboBoxTimeFrame.setEnabled(true);
        jComboBoxPositionType.setEnabled(true);
        jTextFieldTopN.setEnabled(true);
        jTextFieldKeyword.setEnabled(true);
        jTextFieldKeyword2.setEnabled(true);
        jListPackage.setEnabled(true);
        jTextAreaTopStockDescription.setEnabled(true);
        jComboBoxSubpackage.setEnabled(true);
        jTextFieldTopStockDescriptionShortName.setEnabled(true);
        jButtonPickTopStockDescription.setEnabled(true);
        
        jButtonGenerateKeyword1.setEnabled(true);
        jButtonGenerateKeyword2.setEnabled(true);
    }// RN
    public static void enableFormGeneral(){
        jLabelFileName.setEnabled(true);
        jXDatePickerForecastDay.setEnabled(true);
        jXDatePickerTargetDay.setEnabled(true);
        jComboBoxTimeFrame.setEnabled(true);
        jComboBoxPositionType.setEnabled(true);
        jTextFieldTopN.setEnabled(true);
        
        jTextFieldKeyword.setEnabled(false);
        jTextFieldKeyword2.setEnabled(false);
        jListPackage.setEnabled(false);
        jTextAreaTopStockDescription.setEnabled(false);
        jComboBoxSubpackage.setEnabled(false);
        jTextFieldTopStockDescriptionShortName.setEnabled(false);
        jButtonPickTopStockDescription.setEnabled(false);
    }// RN
    public static void disableForm(){
        jLabelFileName.setEnabled(false);
        jXDatePickerForecastDay.setEnabled(false);
        jXDatePickerTargetDay.setEnabled(false);
        jComboBoxTimeFrame.setEnabled(false);
        jComboBoxPositionType.setEnabled(false);
        jTextFieldTopN.setEnabled(false);
        jTextFieldKeyword.setEnabled(false);
        jTextFieldKeyword2.setEnabled(false);
        jListPackage.setEnabled(false);
        jTextAreaTopStockDescription.setEnabled(false);
        jComboBoxSubpackage.setEnabled(false);
        jTextFieldTopStockDescriptionShortName.setEnabled(false);
        jButtonPickTopStockDescription.setEnabled(false);
        
        jButtonGenerateKeyword1.setEnabled(false);
        jButtonGenerateKeyword2.setEnabled(false);
    }// RN
    private void saveLoadButtonsEnabled(boolean allow){
        jButtonSaveTable.setEnabled(allow);
        jButtonLoadTable.setEnabled(allow);
    }

    public void clearTableNCVS(){
        
        if(tableManager != null){
            tableManager.clearAll();
            cvsTableList.clear();
            selectedRow = -1;
            disableForm();
        }
        
    }
    public void clearTablesNCVSs(){
        
        tabList.stream().forEach((tab) -> {
            tab.clearTableNCVS();
        });
    }
    public void clearTableNCVS(int i){
        tabList.get(i).clearTableNCVS();
    }
    public static void clearTableNCVS(TableManager tableManager, List<CVSTable> cvsTableList){
        
        if(tableManager != null){
            tableManager.clearAll();
            cvsTableList.clear();
            selectedRow = -1;
            disableForm();
        }
    }
    
    public static void tableRowSelected(javax.swing.JTable jTable, TableManager tableManager){// Load data from Table to Form
        updateEnable(false);
        
        selectedRow = jTable.getSelectedRow();
        Tab.selectedRow = selectedRow;
        Item aRow = tableManager.getRow(selectedRow);
        //String tSDName = tableManager.getRowTSDName(selectedRow);

        //Set the Form Below
        jLabelFileName.setText(aRow.getFileName());
        jXDatePickerForecastDay.setDate(stringToDate(aRow.getForecastDay()));
        jXDatePickerTargetDay.setDate(stringToDate(aRow.getTargetDay()));
        jComboBoxTimeFrame.setSelectedItem(aRow.getTimeFrame());
        jComboBoxPositionType.setSelectedItem(aRow.getPositionType());
        jTextFieldTopN.setText(aRow.getTopN());
        jListPackage.setSelectedValue((Object)aRow.getPackageContent(), true);
        jComboBoxSubpackage.setSelectedItem(aRow.getSubpackage());
        jTextFieldKeyword.setText(aRow.getKeyword1());
        jTextFieldKeyword1Date.setText(aRow.getKeyword1Date());
        jTextFieldKeyword2.setText(aRow.getKeyword2());
        jTextAreaTopStockDescription.setText(aRow.getTopStockDescription());
        
        jTextFieldTopStockDescriptionShortName.setText(aRow.getTopStockDescriptionName());
        //jTextFieldTopStockDescriptionShortName.setText(aRow.getTopStockDescriptionName());
        
        updateEnable(true);
        
    }// RN
    public static void tableRowSelectedGeneral(javax.swing.JTable jTable, TableManager tableManager){
        updateEnable(false);
        
        selectedRow = jTable.getSelectedRow();
        Tab.selectedRow = selectedRow;
        Item aRow = tableManager.getRow(selectedRow);
        //String tSDName = tableManager.getRowTSDName(selectedRow);

        //Set the Form Below
        jLabelFileName.setText(aRow.getFileName());
        jXDatePickerForecastDay.setDate(stringToDate(aRow.getForecastDay()));
        jXDatePickerTargetDay.setDate(stringToDate(aRow.getTargetDay()));
        jComboBoxTimeFrame.setSelectedItem(aRow.getTimeFrame());
        jComboBoxPositionType.setSelectedItem(aRow.getPositionType());
        jTextFieldTopN.setText(aRow.getTopN());
//        jTextFieldKeyword.setText(aRow.getKeyword());
//        jTextFieldKeyword2.setText(aRow.getKeyword2());
//        jListPackage.setSelectedValue((Object)aRow.getPackageContent(), true);
//        jTextAreaTopStockDescription.setText(aRow.getTopStockDescription());
//        jComboBoxSubpackage.setSelectedItem(aRow.getSubpackage());
//        jTextFieldTopStockDescriptionShortName.setText(aRow.getTopStockDescriptionName());
        //jTextFieldTopStockDescriptionShortName.setText(aRow.getTopStockDescriptionName());
        updateEnable(true);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jToggleButton1 = new javax.swing.JToggleButton();
        jDialogKeywordsGen = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabbedPaneKeywordsGen = new javax.swing.JTabbedPane();
        jPanelSpecificKeywords = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableKeywordsSpecific = new javax.swing.JTable();
        jPanelANYKeywords = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableKeywordsANY = new javax.swing.JTable();
        jButtonKeywordsSelected = new javax.swing.JButton();
        jTextFieldKeyword1Date = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanelRoot = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanelDirectorySelection = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldFileDirectory = new javax.swing.JTextField();
        jButtonOpenImage = new javax.swing.JButton();
        jButtonOpen = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelSelected = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelFileName = new javax.swing.JLabel();
        jXDatePickerForecastDay = new org.jdesktop.swingx.JXDatePicker();
        jXDatePickerTargetDay = new org.jdesktop.swingx.JXDatePicker();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabelKeyword1 = new javax.swing.JLabel();
        jTextFieldTopN = new javax.swing.JTextField();
        jTextFieldKeyword = new javax.swing.JTextField();
        jLabelTopStockDescription = new javax.swing.JLabel();
        jButtonSaveTable = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxPositionType = new javax.swing.JComboBox<>();
        jLabelTimeFrame = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListPackage = new javax.swing.JList<>();
        jLabelKeyword2 = new javax.swing.JLabel();
        jTextFieldKeyword2 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaTopStockDescription = new javax.swing.JTextArea();
        jComboBoxTimeFrame = new javax.swing.JComboBox<>();
        jLabelSubpackage = new javax.swing.JLabel();
        jComboBoxSubpackage = new javax.swing.JComboBox<>();
        jTextFieldTopStockDescriptionShortName = new javax.swing.JTextField();
        jButtonLoadTable = new javax.swing.JButton();
        jButtonPickTopStockDescription = new javax.swing.JButton();
        jLabelViewCSV = new javax.swing.JLabel();
        jButtonGenerateKeyword1 = new javax.swing.JButton();
        jButtonGenerateKeyword2 = new javax.swing.JButton();
        ToXMLFile = new javax.swing.JButton();
        jPanelTopMenu = new javax.swing.JPanel();
        jButtonCardToggle = new javax.swing.JButton();
        jCheckBoxGenerateToXML = new javax.swing.JCheckBox();
        jLabelCurrentTable = new javax.swing.JLabel();
        jPanelCards = new javax.swing.JPanel();
        jScrollPaneGeneral = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTabbedPaneTab = new javax.swing.JTabbedPane();

        jToggleButton1.setText("jToggleButton1");

        jTableKeywordsSpecific.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Keyword", "Date Last Used", "Priority"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableKeywordsSpecific.setColumnSelectionAllowed(true);
        jScrollPane5.setViewportView(jTableKeywordsSpecific);
        jTableKeywordsSpecific.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout jPanelSpecificKeywordsLayout = new javax.swing.GroupLayout(jPanelSpecificKeywords);
        jPanelSpecificKeywords.setLayout(jPanelSpecificKeywordsLayout);
        jPanelSpecificKeywordsLayout.setHorizontalGroup(
            jPanelSpecificKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanelSpecificKeywordsLayout.setVerticalGroup(
            jPanelSpecificKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSpecificKeywordsLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPaneKeywordsGen.addTab("Specific Keyword(s)", jPanelSpecificKeywords);

        jTableKeywordsANY.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Keyword", "Date Last Used", "Priority"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableKeywordsANY.setColumnSelectionAllowed(true);
        jScrollPane6.setViewportView(jTableKeywordsANY);
        jTableKeywordsANY.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout jPanelANYKeywordsLayout = new javax.swing.GroupLayout(jPanelANYKeywords);
        jPanelANYKeywords.setLayout(jPanelANYKeywordsLayout);
        jPanelANYKeywordsLayout.setHorizontalGroup(
            jPanelANYKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
        );
        jPanelANYKeywordsLayout.setVerticalGroup(
            jPanelANYKeywordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelANYKeywordsLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPaneKeywordsGen.addTab("ANY Keywords", jPanelANYKeywords);

        jScrollPane1.setViewportView(jTabbedPaneKeywordsGen);

        jButtonKeywordsSelected.setFont(new java.awt.Font("굴림", 0, 36)); // NOI18N
        jButtonKeywordsSelected.setText("Select");
        jButtonKeywordsSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKeywordsSelectedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogKeywordsGenLayout = new javax.swing.GroupLayout(jDialogKeywordsGen.getContentPane());
        jDialogKeywordsGen.getContentPane().setLayout(jDialogKeywordsGenLayout);
        jDialogKeywordsGenLayout.setHorizontalGroup(
            jDialogKeywordsGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogKeywordsGenLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jButtonKeywordsSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogKeywordsGenLayout.setVerticalGroup(
            jDialogKeywordsGenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogKeywordsGenLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonKeywordsSelected, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
        );

        jDialogKeywordsGen.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        jDialogKeywordsGen.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                //System.out.println("OUT");
                jDialogKeywordsGen.setVisible(false);
            }
        });
        jDialogKeywordsGen.pack();

        jTextFieldKeyword1Date.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jLabel1.setFont(new java.awt.Font("굴림", 1, 18)); // NOI18N
        jLabel1.setText("Forcast Automation Form");

        jPanelDirectorySelection.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("File Directory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 47;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 7, 0);
        jPanelDirectorySelection.add(jLabel2, gridBagConstraints);

        jTextFieldFileDirectory.setText(generalSetting.getDefaultDirectory());
        jTextFieldFileDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFileDirectoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 639;
        gridBagConstraints.ipady = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 0);
        jPanelDirectorySelection.add(jTextFieldFileDirectory, gridBagConstraints);

        jButtonOpenImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/open.png"))); // NOI18N
        jButtonOpenImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenImageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        jPanelDirectorySelection.add(jButtonOpenImage, gridBagConstraints);

        jButtonOpen.setText("Open");
        jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 78;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 0, 0);
        jPanelDirectorySelection.add(jButtonOpen, gridBagConstraints);

        jButtonClear.setText("Clear");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 17, 0, 0);
        jPanelDirectorySelection.add(jButtonClear, gridBagConstraints);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelSelected.setFont(new java.awt.Font("굴림", 3, 18)); // NOI18N
        jLabelSelected.setText("Selected Row");
        jPanel1.add(jLabelSelected, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 12, 140, -1));

        jLabel3.setText("File Name");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 42, 140, 31));

        jLabel4.setText("Forecast Day");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 82, 140, 31));

        jLabel5.setText("Target Day");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 122, 140, 31));
        jPanel1.add(jLabelFileName, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 42, 270, 31));

        jXDatePickerForecastDay.setFormats(dateFormat);
        jXDatePickerForecastDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePickerForecastDayActionPerformed(evt);
            }
        });
        jPanel1.add(jXDatePickerForecastDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 270, 31));

        jXDatePickerTargetDay.setFormats(dateFormat);
        jXDatePickerTargetDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePickerTargetDayActionPerformed(evt);
            }
        });
        jPanel1.add(jXDatePickerTargetDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, 270, 31));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(435, 15, 8, 81));

        jLabel6.setText("Top N");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 140, 31));

        jLabel7.setText("Package");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 40, 140, 31));

        jLabelKeyword1.setText("Keyword1");
        jPanel1.add(jLabelKeyword1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 70, 31));

        jTextFieldTopN.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                updateAtChange(evt);
            }
        });
        jPanel1.add(jTextFieldTopN, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 270, 31));

        jTextFieldKeyword.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                updateAtChange(evt);
            }
        });
        jPanel1.add(jTextFieldKeyword, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 270, 31));

        jLabelTopStockDescription.setText("Top Stock Description");
        jPanel1.add(jLabelTopStockDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 40, 200, 31));

        jButtonSaveTable.setFont(new java.awt.Font("굴림", 0, 24)); // NOI18N
        jButtonSaveTable.setText("Save");
        jButtonSaveTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveTableActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSaveTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 80, 120, 130));

        jLabel10.setText("Position Type");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 140, 31));

        jComboBoxPositionType.setModel(new javax.swing.DefaultComboBoxModel<>(generalSetting.getPositionTypeDropDownList()));
        jComboBoxPositionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPositionTypeActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxPositionType, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 270, 31));

        jLabelTimeFrame.setText("Time Frame");
        jPanel1.add(jLabelTimeFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 140, 31));

        jListPackage.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListPackageValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jListPackage);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, 280, 230));

        jLabelKeyword2.setText("Keyword2");
        jPanel1.add(jLabelKeyword2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 70, 30));

        jTextFieldKeyword2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                updateAtChange(evt);
            }
        });
        jPanel1.add(jTextFieldKeyword2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 270, 31));

        jTextAreaTopStockDescription.setLineWrap(true);
        jTextAreaTopStockDescription.setWrapStyleWord(true);
        jTextAreaTopStockDescription.setColumns(20);
        jTextAreaTopStockDescription.setRows(5);
        jTextAreaTopStockDescription.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                updateAtChange(evt);
            }
        });
        jScrollPane3.setViewportView(jTextAreaTopStockDescription);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 120, 260, 190));

        jComboBoxTimeFrame.setModel(new javax.swing.DefaultComboBoxModel<>(generalSetting.getTimeFrameDropDownList()));
        jComboBoxTimeFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTimeFrameActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxTimeFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 270, 31));

        jLabelSubpackage.setText("Subpackage");
        jPanel1.add(jLabelSubpackage, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 320, 130, 30));

        jComboBoxSubpackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubpackageActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxSubpackage, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 320, 420, 30));

        jTextFieldTopStockDescriptionShortName.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextFieldTopStockDescriptionShortNameCaretUpdate(evt);
            }
        });
        jPanel1.add(jTextFieldTopStockDescriptionShortName, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 80, 260, 32));

        jButtonLoadTable.setFont(new java.awt.Font("굴림", 0, 24)); // NOI18N
        jButtonLoadTable.setText("Load");
        jButtonLoadTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadTableActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonLoadTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 220, 120, 130));

        jButtonPickTopStockDescription.setText("Pick TSD");
        jButtonPickTopStockDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPickTopStockDescriptionActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonPickTopStockDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 80, 110, 270));

        jLabelViewCSV.setFont(new java.awt.Font("굴림", 0, 18)); // NOI18N
        jLabelViewCSV.setText("Hover Here To View CSV File");
        jPanel1.add(jLabelViewCSV, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 8, 250, 60));
        jLabelViewCSV.setToolTipText("wow \n"
            + "Inte\n"
            + "res\n"
            + "ting");

        jButtonGenerateKeyword1.setText("GEN");
        jButtonGenerateKeyword1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateKeyword1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonGenerateKeyword1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 70, 30));

        jButtonGenerateKeyword2.setText("GEN");
        jButtonGenerateKeyword2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateKeyword2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonGenerateKeyword2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 70, 30));

        ToXMLFile.setText("To XML File");
        ToXMLFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToXMLFileActionPerformed(evt);
            }
        });

        jButtonCardToggle.setText("Edit Specific Table");
        jButtonCardToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCardToggleActionPerformed(evt);
            }
        });

        jCheckBoxGenerateToXML.setText("Generate To XML");
        jCheckBoxGenerateToXML.setEnabled(false);
        jCheckBoxGenerateToXML.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxGenerateToXMLStateChanged(evt);
            }
        });

        jLabelCurrentTable.setFont(new java.awt.Font("굴림", 0, 24)); // NOI18N
        jLabelCurrentTable.setText("General Table (Not Actually Generated To XML)");

        javax.swing.GroupLayout jPanelTopMenuLayout = new javax.swing.GroupLayout(jPanelTopMenu);
        jPanelTopMenu.setLayout(jPanelTopMenuLayout);
        jPanelTopMenuLayout.setHorizontalGroup(
            jPanelTopMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTopMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxGenerateToXML)
                .addGap(18, 18, 18)
                .addComponent(jButtonCardToggle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelCurrentTable, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelTopMenuLayout.setVerticalGroup(
            jPanelTopMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTopMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTopMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTopMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCardToggle)
                        .addComponent(jCheckBoxGenerateToXML))
                    .addComponent(jLabelCurrentTable))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelTopMenuLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonCardToggle, jLabelCurrentTable});

        jPanelCards.setPreferredSize(new java.awt.Dimension(1235, 300));
        jPanelCards.setLayout(new java.awt.CardLayout());

        jScrollPaneGeneral.setPreferredSize(new java.awt.Dimension(452, 300));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Forecast day", "Target Day", "Time Frame", "Position Type", "Top N", "Package", "Subpackage","Keyword1", "Keyword1Date", "Keyword2", "Top Stock Description",  "Top Stock Description Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jScrollPaneGeneral.setViewportView(jTable1);
        for(int i = jTable1.getColumnCount() - 1; i>5; i--){
            TableColumn colToDelete = jTable1.getColumnModel().getColumn(i);
            jTable1.removeColumn(colToDelete);
        }
        jTable1.validate();

        jPanelCards.add(jScrollPaneGeneral, "card3");
        CardLayout cl = (CardLayout)jPanelCards.getLayout();
        cl.show(jPanelCards, "card3");

        jTabbedPaneTab.setPreferredSize(new java.awt.Dimension(1235, 300));
        jTabbedPaneTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneTabStateChanged(evt);
            }
        });
        jPanelCards.add(jTabbedPaneTab, "card2");

        javax.swing.GroupLayout jPanelRootLayout = new javax.swing.GroupLayout(jPanelRoot);
        jPanelRoot.setLayout(jPanelRootLayout);
        jPanelRootLayout.setHorizontalGroup(
            jPanelRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRootLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ToXMLFile, javax.swing.GroupLayout.PREFERRED_SIZE, 1243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanelCards, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelDirectorySelection, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jPanelTopMenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelRootLayout.setVerticalGroup(
            jPanelRootLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRootLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(9, 9, 9)
                .addComponent(jPanelDirectorySelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTopMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ToXMLFile, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanelRoot);

        getContentPane().add(jScrollPane4, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Open Button (Almost Like First Step)
    private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenActionPerformed
        
        choosenDirectory = jTextFieldFileDirectory.getText();
        if(jTextFieldFileDirectory.getText().length()==0){
            System.out.println("Insert the Directory");
            ForcastUi.consoleLog("Insert the Directory");
            return;
        }
        ForcastUi.consoleLog("Opening CSV Files in path: " + choosenDirectory);
        
        opened = true;
        if(editGeneral == false)
            saveLoadButtonsEnabled(true);
        //For Each Language
        final File folder = new File(choosenDirectory);
        
        for(Tab tab: tabList){
            tab.clearTableNCVS();
            tab.setTableManager(new TableManager(tab.getjTable()));
            TableManager tableManager = tab.getTableManager();
            ForcastUi.consoleLog("In TabName: "+ tab.getTabName());
            for(final File fileEntry : folder.listFiles()){
                if(fileEntry.isFile() && fileExtCheck(fileEntry.getName())){ // If it is a file and csv file
                    
                    //Open CVSfile from the directory But only Date
                    String fileName = fileEntry.getName();// get file name
                    ForcastUi.consoleLog("  Opening CSV Files Filename: "+ fileName );
                    //Only getting Position type and Top N
                    Item guess = CVSTable.openCSVfileToGuess(choosenDirectory, fileName);

                    //Short According to Position
                    if(guess.getPositionType().equalsIgnoreCase("Long"))
                        guess.getCvsTable().sortCVSTableByReturn(false);
                    else if(guess.getPositionType().equalsIgnoreCase("Short"))
                        guess.getCvsTable().sortCVSTableByReturn(true);
                    else if(guess.getPositionType().equalsIgnoreCase("Long & Short")){
                        guess.getCvsTable().sortCVSTableByReturn(false);
                        guess.getCvsTable2().sortCVSTableByReturn(true);
                    }
                    //tableManager.addRow(fileName, guess);
                    System.out.println("Tab: " + tab.getTabName()+ " "+fileName);
                    int keyword1DateLimit = tab.getPreferenceSettings().getKeyword1DateLimit();
                    int keyword2DateLimit = tab.getPreferenceSettings().getKeyword2DateLimit();
                    
                    tableManager.addRow(fileName, guess, tab.getTsdData(), tab.getPackageList(), tab.getKeywordList(), tab.getKeyword2List(), tab.getPackageHeaderList(), keyword1DateLimit, keyword2DateLimit);
                    tab.csvDirectory = choosenDirectory;
                }
            }
            //tab.resizeToFit();
            ForcastUi.consoleLog("Done for this tab: "+ tab.getTabName()+"\n");
            
        }

        //For General Table
        clearTableNCVS();// Clear the Original Table
        //saveLoadButtonsEnabled(true);
        
        choosenDirectory = jTextFieldFileDirectory.getText();
        if(jTextFieldFileDirectory.getText().length()==0){
            System.out.println("Insert the Directory");
            return;
        }
        
        //Get from jTable & Create new object
        tableManager = new TableManager(jTable1);
        cvsTableList = new ArrayList();
        
        //final File folder = new File(choosenDirectory);
        for(final File fileEntry : folder.listFiles()){
            if(fileEntry.isFile() && fileExtCheck(fileEntry.getName())){ // If it is a file and csv file
                
                //Open CVSfile from the directory But only Date
                String fileName = fileEntry.getName();// get file name
                
                //Only getting Position type and Top N
                Item guess = CVSTable.openCSVfileToGuess(choosenDirectory, fileName);
                
                //Short According to Position
                if(guess.getPositionType().equalsIgnoreCase("Long"))
                    guess.getCvsTable().sortCVSTableByReturn(false);
                else if(guess.getPositionType().equalsIgnoreCase("Short"))
                    guess.getCvsTable().sortCVSTableByReturn(true);
                else if(guess.getPositionType().equalsIgnoreCase("Long & Short")){
                    guess.getCvsTable().sortCVSTableByReturn(false);
                    guess.getCvsTable2().sortCVSTableByReturn(true);
                }
                //tableManager.addRow(fileName, guess);
                itemList.add(guess);
                tableManager.addRowGeneral(fileName, guess);
            }
        }
    }//GEN-LAST:event_jButtonOpenActionPerformed
    // Open ICON Button
    private void jButtonOpenImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenImageActionPerformed
        JFileChooser chooser;
        String choosertitle ="Choose the Directory of the File";
        
        chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //    
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
          /*System.out.println("getCurrentDirectory(): " 
             +  chooser.getCurrentDirectory());*/ // Curent Directory App is running
            choosenDirectory = chooser.getSelectedFile().getAbsolutePath();
            jTextFieldFileDirectory.setText(choosenDirectory);
            
        }
        else {
          System.out.println("No Selection ");
        }
    }//GEN-LAST:event_jButtonOpenImageActionPerformed
    // Clear Button
    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        clearTableNCVS();
        for(Tab tab: tabList)
            tab.clearTableNCVS();
        saveLoadButtonsEnabled(false);
        opened = false;
    }//GEN-LAST:event_jButtonClearActionPerformed
    
    public static void GenerateXML(Tab tab, String choosenDirectory){
        //In order to override the first read cvsTable which only has two dates
        tab.cvsTableList.clear();

        final File folder = new File(choosenDirectory);
        //int count = 0;
        if(tab.itemList != null)
            tab.itemList.clear();
        tab.itemList = new ArrayList();
        int solved = 0;
        
        List<String> keywordHistory= new ArrayList();
                
        for(final File fileEntry : folder.listFiles()){
            // If it is not a correct CSV file
            if(!(fileEntry.isFile() && fileExtCheck(fileEntry.getName()))){ 
                continue;
            }    
            
            String fileName = fileEntry.getName();// get file name
            CVSTable tempCSVTable = null;
            List<CVSTable> tempCSVTableList = null;
            
            //Find a matching Table Row
            for(int i = 0; i < tab.tableManager.getRowCount(); i++){
                //Check until the file(csv) match with the row fileName
                Item itemTemp = tab.tableManager.getRow(i);
                // if the table file name  is === to opened filename Check until Match 
                if(!itemTemp.getFileName().equalsIgnoreCase(fileName)){
                    continue;
                }
                switch(tab.tableManager.getPositionTypeAt(i)){
                    case "Long & Short":
                        //Open CSV file with Long And Short function
                        tempCSVTableList = CVSTable.openCVSfileLongAndShort(choosenDirectory , fileName);
                        if(tempCSVTableList == null){
                            System.out.println("ERR: The CSV file you are looking for doesn't exist");
                            continue;
                        }
                        tempCSVTableList.get(0).setPositionType("Long & Short(Long)");
                        tempCSVTableList.get(0).setFileName(fileName);
                        tempCSVTableList.get(0).sortCVSTableByReturn(false);

                        tempCSVTableList.get(1).setPositionType("Long & Short(Short)");
                        tempCSVTableList.get(1).setFileName(fileName);
                        tempCSVTableList.get(1).sortCVSTableByReturn(true);

                        // Store CVS Table Info
                        itemTemp.setCvsTableBoth(tempCSVTableList.get(0),tempCSVTableList.get(1)); 
                        
                        tempCSVTableList.get(0).printAll();
                        tempCSVTableList.get(1).printAll();
                        break;
                    case "Short":
                        tempCSVTable = CVSTable.openCVSfile(choosenDirectory , fileName);
                        if(tempCSVTable == null){
                            System.out.println("ERR: The CSV file you are looking for doesn't exist");
                            continue;
                        }

                        tempCSVTable.setPositionType(tab.tableManager.getPositionTypeAt(i));
                        tempCSVTable.setFileName(fileName);
                        tempCSVTable.sortCVSTableByReturn(true);

                        itemTemp.setCvsTable(tempCSVTable);

                        tempCSVTable.printAll();
                        break;
                    default:
                        tempCSVTable = CVSTable.openCVSfile(choosenDirectory , fileName);
                        if(tempCSVTable == null){
                            System.out.println("ERR: The CSV file you are looking for doesn't exist");
                            continue;
                        }

                        tempCSVTable.setPositionType(tab.tableManager.getPositionTypeAt(i));
                        tempCSVTable.setFileName(fileName);
                        tempCSVTable.sortCVSTableByReturn(false);

                        itemTemp.setCvsTable(tempCSVTable);

                        tempCSVTable.printAll();
                        break;
                }
                keywordHistory.add(itemTemp.getKeyword1());
                
                
                tab.itemList.add(tab.returnTranslatedVersion(itemTemp));
                solved++;              
                break;//Found so no need to find again!
            }
            
        
        }
        if(solved != tab.tableManager.getRowCount()){
            System.out.println("The table row doesn't match with the file(csv) in the Folder \n"
                    + "The file has been moved or changed while program was running or other problem.");
        }
        // CVSTables Complete!!!!
        //CVSTale => Items <= Table Model
        
        
        //List<Item> itemList = tableModelAndCVSToItemList();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy_HH-MM-ss");
        SimpleDateFormat dateformat2 = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        
        String fileName ="WP-Forecast_" + dateformat.format(date) +".xml";
        //String fileNameAndLocation = workingDirectory + slash+"output"+slash+"("+ tab.getTabName()+")"+ fileName;
        
        if(tab.itemList != null){
            //Update Link Forecast Data db
            //tab.updateForecastDataManager1();
            
            //TestRSSWriter.StartWriting(tab, fileNameAndLocation);
            TestRSSWriterVarImprovement.StartWriting(tab, workingDirectory+ tab.outputXMLDir+fileName);
            //sqliteTSD.updateTopStockDescriptionList(itemList);
            List<TopStockDescription> tsdDataToUpdate;
            tsdDataToUpdate = tab.getTsdData().getChangedTopStockDescriptionList(tab.itemList);
            TopStockDescriptionList.writeToFileExcel(workingDirectory + tab.topStockDescriptionListDir, tsdDataToUpdate);
            
            KeywordHistoryTracker.writeCsvFile(workingDirectory + tab.keywordHistoryDir, keywordHistory, dateformat2.format(date));
            
            tab.getKeywordList().refactorKeywordList(keywordHistory);
            //tab.getKeywordList().writeToCSV(workingDirectory+tab.keywordListTestDir);
            tab.getKeywordList().writeToExcel(workingDirectory+tab.keywordListDir);
            JOptionPane.showMessageDialog(null, workingDirectory+ tab.outputXMLDir+fileName+"\nXml file successfully created");
            exported = true;
        }else{
            System.out.println("The ItemList Is NULL!");
        }
    }
    
    private void ToXMLFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToXMLFileActionPerformed
        for(Tab tab: tabList){
            if(tab.isGenerateToXML()){
                ForcastUi.consoleLog("Creating XML for: "+tab.getTabName());
                GenerateXML(tab, choosenDirectory);
                ForcastUi.consoleLog(" ");
            }
        }
    }//GEN-LAST:event_ToXMLFileActionPerformed

    private void jTextFieldFileDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFileDirectoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFileDirectoryActionPerformed

    private void jButtonPickTopStockDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPickTopStockDescriptionActionPerformed
        // prompt the user to enter their name
        TopStockDescriptionList tsdData = tabList.get(Tab.currentTab).getTsdData();
        if(tsdData.isEmpty())
            return;
        
        String shortName = JOptionPane.showInputDialog(null, "Enter Top Stock Symbol:");
        TopStockDescription tsd = tsdData.selectRowByShortName(shortName.trim());

        if(tsd !=null){
            jTextFieldTopStockDescriptionShortName.setText(tsd.getShortName());
            jTextAreaTopStockDescription.setText(tsd.getContentText());
        }else{
            jTextFieldTopStockDescriptionShortName.setText(shortName.trim().toUpperCase());
            jTextAreaTopStockDescription.setText("");
        }
        
        updateTableTop();
    }//GEN-LAST:event_jButtonPickTopStockDescriptionActionPerformed

    private void jButtonLoadTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadTableActionPerformed
        //clearTableNCVS();
        
        if((Tab.currentTab != -1)&&(!editGeneral)){
            Tab tab = tabList.get(Tab.currentTab);
            TableManager tableManager = tab.tableManager;
            
            DefaultTableModel loaded = TableManager.loadTable(tableManager, this, tabList.get(Tab.currentTab).categorieSlugList);
            DefaultTableModel before = tableManager.getTableModel();
            
            int rowCount = before.getRowCount();
            int colcount = before.getColumnCount();
            Vector<Vector<Object>> copy = new Vector<Vector<Object>>(rowCount);
            
            for(int row = 0;row < rowCount;row++) { // Check Whether it is 
                Vector<Object> newRow = new Vector<Object>(colcount);
                for(int col = 0;col < colcount;col++) {
                    newRow.add(before.getValueAt(row, col));
                }
                copy.add(newRow);
                
            }
            
            if(loaded != null){
                tab.clearTableNCVS();
                tableManager.setTableModel(loaded);
            }
            
            for(Vector<Object> aRow: copy){
                tableManager.addRowIfNotExist(aRow);
            }
        }
        
        
        //TableManager tableManager, JFrame jFrame
    }//GEN-LAST:event_jButtonLoadTableActionPerformed

    private void jTextFieldTopStockDescriptionShortNameCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextFieldTopStockDescriptionShortNameCaretUpdate
        updateTableTop();
    }//GEN-LAST:event_jTextFieldTopStockDescriptionShortNameCaretUpdate

    private void jComboBoxSubpackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubpackageActionPerformed
        updateTableTop();
    }//GEN-LAST:event_jComboBoxSubpackageActionPerformed

    private void jComboBoxTimeFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTimeFrameActionPerformed
        updateTableTop();
    }//GEN-LAST:event_jComboBoxTimeFrameActionPerformed

    private void updateAtChange(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_updateAtChange
        updateTableTop();
    }//GEN-LAST:event_updateAtChange

    private void jListPackageValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListPackageValueChanged
        updateTableTop();
        updateSubpackageItemTop();
    }//GEN-LAST:event_jListPackageValueChanged

    private void jComboBoxPositionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPositionTypeActionPerformed
        updateTableTop();
    }//GEN-LAST:event_jComboBoxPositionTypeActionPerformed

    //Save button
    private void jButtonSaveTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveTableActionPerformed
        if((Tab.currentTab != -1)&&(!editGeneral)){
            Tab tab = tabList.get(Tab.currentTab);
            tab.tableManager.saveTable();
        }
    }//GEN-LAST:event_jButtonSaveTableActionPerformed

    private void jXDatePickerTargetDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerTargetDayActionPerformed
        updateTableTop();
    }//GEN-LAST:event_jXDatePickerTargetDayActionPerformed

    private void jXDatePickerForecastDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePickerForecastDayActionPerformed
        updateTableTop();
    }//GEN-LAST:event_jXDatePickerForecastDayActionPerformed

    private void jTabbedPaneTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneTabStateChanged
        int tabNumber = jTabbedPaneTab.getSelectedIndex();
        Tab.currentTab = tabNumber;
        //disableForm();
        selectedRow = -1; // General Table
        Tab tab = tabList.get(tabNumber);
        Tab.selectedRow = tab.getjTable().getSelectedRow();
        jCheckBoxGenerateToXML.setSelected(tab.isGenerateToXML());
        updateForm(tabNumber);
        
        if(Tab.selectedRow > -1){
            enableForm();
            updateForm(Tab.currentTab);
            tableRowSelected(tabList.get(tabNumber).getjTable(), tabList.get(tabNumber).getTableManager());
            
        }else{
            disableForm();
        }
    }//GEN-LAST:event_jTabbedPaneTabStateChanged

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        //updateSubpackageItem();
        if(editGeneral){
            enableFormGeneral();
            tableRowSelectedGeneral(jTable1, tableManager);
            updateViewCSVFile();
        }
    }//GEN-LAST:event_jTable1MousePressed
    
    public void updateViewCSVFile(){
        if(selectedRow>=0){
            Item selectedItem = itemList.get(selectedRow);
            if(selectedItem.getCvsTable()!=null && selectedItem.getCvsTable2()!=null)
                jLabelViewCSV.setToolTipText("<html>"+selectedItem.getCvsTable().printView() + selectedItem.getCvsTable2().printView()+"</html>");
            else if(selectedItem.getCvsTable()!=null){
                jLabelViewCSV.setToolTipText("<html>"+selectedItem.getCvsTable().printView()+"</html>");
            }
        }
    }
    
    public void updateForm(int currentTab){
        List<String> packageList = tabList.get(currentTab).getPackageList();
        
        jListPackage.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() { return packageList.size(); }
            public String getElementAt(int i) { return packageList.get(i); }
        });
        
        //updateSubpackageItemTop();
    }
    private void jButtonCardToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCardToggleActionPerformed
        disableForm();
        selectedRow = -1;
        Tab.selectedRow = -1;

        CardLayout cl = (CardLayout)jPanelCards.getLayout();
        if(editGeneral){// to Tab
            jCheckBoxGenerateToXML.setEnabled(true);
            cl.show(jPanelCards, "card2");
            jButtonCardToggle.setText("Edit General Table");
            jLabelCurrentTable.setText("Specific Tables (Generated To XML if Enabled)");
            editGeneral = false;
            updateForm(Tab.currentTab);
            
            if(opened)
                saveLoadButtonsEnabled(true);
            /*
            jComboBoxTimeFrame.setModel(new javax.swing.DefaultComboBoxModel<>
                (tabList.get(Tab.currentTab).getPreferenceSettings().getTimeFrameDropDownList()));
            jComboBoxPositionType.setModel(new javax.swing.DefaultComboBoxModel<>
                (tabList.get(Tab.currentTab).getPreferenceSettings().getPositionTypeDropDownList()));
            */
        }else{
            jCheckBoxGenerateToXML.setEnabled(false);
            cl.show(jPanelCards, "card3");// to General
            jButtonCardToggle.setText("Edit Specific Table");
            jLabelCurrentTable.setText("General Table (Not Actually Generated To XML)");
            editGeneral = true;
            
            if(opened)
                saveLoadButtonsEnabled(false);
            /*
            jComboBoxTimeFrame.setModel(new javax.swing.DefaultComboBoxModel<>
                (generalSetting.getTimeFrameDropDownList()));
            jComboBoxPositionType.setModel(new javax.swing.DefaultComboBoxModel<>
                (generalSetting.getPositionTypeDropDownList()));
            */
        }
    }//GEN-LAST:event_jButtonCardToggleActionPerformed

    private void jCheckBoxGenerateToXMLStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxGenerateToXMLStateChanged
        tabList.get(Tab.currentTab).setGenerateToXML(jCheckBoxGenerateToXML.isSelected());
    }//GEN-LAST:event_jCheckBoxGenerateToXMLStateChanged

    private void jButtonGenerateKeyword1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateKeyword1ActionPerformed
        updateTableTop();
        //int option = JOptionPane.showConfirmDialog(null, jDialogKeyword1Gen, "edit", JOptionPane.OK_CANCEL_OPTION);
        genNumber = KEYWORD1;
        Tab curTab = tabList.get(Tab.currentTab);
        Item tempItem = curTab.getTableManager().getRow(Tab.selectedRow);
        String packageName = tempItem.getPackageContent();
        String subpackageName = tempItem.getSubpackage();
        String positionType = tempItem.getPositionType();
        if(subpackageName.isEmpty())
            subpackageName = "ANY";
        
        Calendar car = Calendar.getInstance();
        car.add(Calendar.DAY_OF_MONTH, -curTab.getPreferenceSettings().getKeyword1DateLimit());
        List<Object[]> keywordSpecificList = curTab.getKeywordList().chooseKeywordListSpecific(packageName, subpackageName, positionType, car.getTime());
        List<Object[]> KeywordANYList = curTab.getKeywordList().chooseKeywordListANY(packageName, subpackageName, positionType, car.getTime());
        
        //Table Setting!
        DefaultTableModel modelKeyword1Specific = (DefaultTableModel) jTableKeywordsSpecific.getModel();
        modelKeyword1Specific.setRowCount(0);
        
        keywordSpecificList.stream().forEach(item ->modelKeyword1Specific.addRow(item));
        //modelKeyword1Specific.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});
        
        
        
        DefaultTableModel modelKeyword1ANY = (DefaultTableModel) jTableKeywordsANY.getModel();
        modelKeyword1ANY.setRowCount(0);
        KeywordANYList.stream().forEach(item ->modelKeyword1ANY.addRow(item));
        
        int width = jTableKeywordsSpecific.getWidth();
        TableColumnModel tcm1 = jTableKeywordsSpecific.getColumnModel();
        tcm1.getColumn(0).setPreferredWidth(width-300);
        tcm1.getColumn(1).setPreferredWidth(80);
        tcm1.getColumn(2).setPreferredWidth(40);
        
        TableColumnModel tcm2 = jTableKeywordsANY.getColumnModel();
        tcm2.getColumn(0).setPreferredWidth(width-300);
        tcm2.getColumn(1).setPreferredWidth(80);
        tcm2.getColumn(2).setPreferredWidth(40);
        
        jTabbedPaneKeywordsGen.setSelectedIndex(0);
        jDialogKeywordsGen.setVisible(true);
       
    }//GEN-LAST:event_jButtonGenerateKeyword1ActionPerformed

    private void jButtonKeywordsSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKeywordsSelectedActionPerformed
        int selectedTab =  jTabbedPaneKeywordsGen.getSelectedIndex();
        int selectedRow = 0;
        String keyword ="";
        String keywordDate = "";
        if(selectedTab == 0){
            selectedRow = jTableKeywordsSpecific.getSelectedRow();
            if(selectedRow >-1){
                keyword = (String)jTableKeywordsSpecific.getValueAt(selectedRow, 0);
                keywordDate = (String)jTableKeywordsSpecific.getValueAt(selectedRow, 1);
            }
        }else if(selectedTab ==1){
            selectedRow = jTableKeywordsANY.getSelectedRow();
            if(selectedRow >-1){
                keyword = (String)jTableKeywordsANY.getValueAt(selectedRow, 0);
                keywordDate = (String)jTableKeywordsANY.getValueAt(selectedRow, 1);
            }
        }
        //check if already used
        
        if(!keyword.isEmpty()){
            if(genNumber == KEYWORD1){
                jTextFieldKeyword.setText(keyword);
                jTextFieldKeyword1Date.setText(keywordDate);
            }else if(genNumber == KEYWORD2){
                jTextFieldKeyword2.setText(keyword);
            }
        }
        
        jDialogKeywordsGen.setVisible(false);
    }//GEN-LAST:event_jButtonKeywordsSelectedActionPerformed

    private void jButtonGenerateKeyword2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateKeyword2ActionPerformed
        updateTableTop();
        //int option = JOptionPane.showConfirmDialog(null, jDialogKeyword1Gen, "edit", JOptionPane.OK_CANCEL_OPTION);
        genNumber = KEYWORD2;
        Tab curTab = tabList.get(Tab.currentTab);
        Item tempItem = curTab.getTableManager().getRow(Tab.selectedRow);
        String packageName = tempItem.getPackageContent();
        String subpackageName = tempItem.getSubpackage();
        String positionType = tempItem.getPositionType();
        if(subpackageName.isEmpty())
            subpackageName = "ANY";
        
        Calendar car = Calendar.getInstance();
        car.add(Calendar.DAY_OF_MONTH, -curTab.getPreferenceSettings().getKeyword2DateLimit());
        List<Object[]> keywordSpecificList = curTab.getKeyword2List().chooseKeywordListSpecific(packageName, subpackageName, positionType, car.getTime());
        List<Object[]> KeywordANYList = curTab.getKeyword2List().chooseKeywordListANY(packageName, subpackageName, positionType, car.getTime());
        
        //Table Setting!
        DefaultTableModel modelKeywordSpecific = (DefaultTableModel) jTableKeywordsSpecific.getModel();
        modelKeywordSpecific.setRowCount(0);
        
        keywordSpecificList.stream().forEach(item ->modelKeywordSpecific.addRow(item));
        //modelKeyword1Specific.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});
        
        
        
        DefaultTableModel modelKeyword1ANY = (DefaultTableModel) jTableKeywordsANY.getModel();
        modelKeyword1ANY.setRowCount(0);
        KeywordANYList.stream().forEach(item ->modelKeyword1ANY.addRow(item));
        
        
        int width = jTableKeywordsSpecific.getWidth();
        TableColumnModel tcm1 = jTableKeywordsSpecific.getColumnModel();
        tcm1.getColumn(0).setPreferredWidth(width-300);
        tcm1.getColumn(1).setPreferredWidth(80);
        tcm1.getColumn(2).setPreferredWidth(40);
        
        TableColumnModel tcm2 = jTableKeywordsANY.getColumnModel();
        tcm2.getColumn(0).setPreferredWidth(width-300);
        tcm2.getColumn(1).setPreferredWidth(80);
        tcm2.getColumn(2).setPreferredWidth(40);
        
        jTabbedPaneKeywordsGen.setSelectedIndex(0);
        jDialogKeywordsGen.setVisible(true);
    }//GEN-LAST:event_jButtonGenerateKeyword2ActionPerformed

        
             
    /**
     * @param args the command line arguments
     */
    public static void main() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ForcastUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ForcastUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ForcastUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForcastUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ForcastUi().setVisible(true);
            }
        });
        
        consoleLog("Opening ForecastUi");
    }
    public static void consoleLog(String str){
        Document doc = jEditorPane1.getDocument();
        try{
            doc.insertString(doc.getLength(), str+"\n", null);
        }catch(Exception e){
            ErrorMessages.printErrorMsg(ErrorMessages.CONSOLE,"str");
        }
        jEditorPane1.setCaretPosition(doc.getLength());
    }
    // Manually added swing
    public static javax.swing.JEditorPane jEditorPane1;
    //private List<javax.swing.JTable> jTable
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ToXMLFile;
    private javax.swing.JButton jButtonCardToggle;
    private javax.swing.JButton jButtonClear;
    private static javax.swing.JButton jButtonGenerateKeyword1;
    private static javax.swing.JButton jButtonGenerateKeyword2;
    private javax.swing.JButton jButtonKeywordsSelected;
    private javax.swing.JButton jButtonLoadTable;
    private javax.swing.JButton jButtonOpen;
    private javax.swing.JButton jButtonOpenImage;
    private static javax.swing.JButton jButtonPickTopStockDescription;
    private javax.swing.JButton jButtonSaveTable;
    private javax.swing.JCheckBox jCheckBoxGenerateToXML;
    private static javax.swing.JComboBox<String> jComboBoxPositionType;
    private static javax.swing.JComboBox<String> jComboBoxSubpackage;
    private static javax.swing.JComboBox<String> jComboBoxTimeFrame;
    private javax.swing.JDialog jDialogKeywordsGen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelCurrentTable;
    private static javax.swing.JLabel jLabelFileName;
    private javax.swing.JLabel jLabelKeyword1;
    private javax.swing.JLabel jLabelKeyword2;
    private javax.swing.JLabel jLabelSelected;
    private javax.swing.JLabel jLabelSubpackage;
    private javax.swing.JLabel jLabelTimeFrame;
    private javax.swing.JLabel jLabelTopStockDescription;
    public static javax.swing.JLabel jLabelViewCSV;
    private static javax.swing.JList<String> jListPackage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelANYKeywords;
    private javax.swing.JPanel jPanelCards;
    private javax.swing.JPanel jPanelDirectorySelection;
    private javax.swing.JPanel jPanelRoot;
    private javax.swing.JPanel jPanelSpecificKeywords;
    private javax.swing.JPanel jPanelTopMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPaneGeneral;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPaneKeywordsGen;
    private javax.swing.JTabbedPane jTabbedPaneTab;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableKeywordsANY;
    private javax.swing.JTable jTableKeywordsSpecific;
    private static javax.swing.JTextArea jTextAreaTopStockDescription;
    private javax.swing.JTextField jTextFieldFileDirectory;
    private static javax.swing.JTextField jTextFieldKeyword;
    private static javax.swing.JTextField jTextFieldKeyword1Date;
    private static javax.swing.JTextField jTextFieldKeyword2;
    private static javax.swing.JTextField jTextFieldTopN;
    private static javax.swing.JTextField jTextFieldTopStockDescriptionShortName;
    private javax.swing.JToggleButton jToggleButton1;
    private static org.jdesktop.swingx.JXDatePicker jXDatePickerForecastDay;
    private static org.jdesktop.swingx.JXDatePicker jXDatePickerTargetDay;
    // End of variables declaration//GEN-END:variables


}
