package main;


import DB.Dictionary;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static rss.TestRSSWriter.stringToDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */
public class Item {

    /**
     * @return the modifiedPackageHeader
     */
    public PackageHeader getModifiedPackageHeader() {
        return modifiedPackageHeader;
    }

    /**
     * @param modifiedPackageHeader the modifiedPackageHeader to set
     */
    public void setModifiedPackageHeader(PackageHeader modifiedPackageHeader) {
        this.modifiedPackageHeader = modifiedPackageHeader;
    }
    private String fileName;
    private String forecastDay;
    private String targetDay;
    private String timeFrame;
    private String positionType;
    private String topN;
    private String packageContent;
    private String subpackage;
    
    private String keyword1;
    private String keyword1Date;
    private String keyword2;
    private String topStockDescription;
    
    private String topStockDescriptionName;
    
    private CVSTable cvsTable;
    private CVSTable cvsTable2;
    
    private PackageHeader modifiedPackageHeader=null;
    
    public static String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    Item(Item item){
        
    }
    Item(){
        this.fileName = null;
        this.forecastDay = null;
        this.targetDay = null;
        this.positionType = null;
        this.positionType = null;
        this.topN = null;
        this.packageContent = null;
        this.subpackage = null;
        this.keyword1 = null;
        this.keyword1Date = null;
        this.keyword2 = null;
        this.topStockDescription = null;
        this.topStockDescriptionName = null;
                
        this.cvsTable = null;
        this.cvsTable2 = null;
    }
    public static Item emptyItemConstructor(){
        Item item = new Item();
        
        item.fileName = "";
        item.forecastDay = "";
        item.targetDay = "";
        item.positionType = "";
        item.positionType = "";
        item.topN = "";
        item.packageContent = "";
        item.subpackage = "";
        item.keyword1 = "";
        item.keyword1Date = "";
        item.keyword2 = "";
        item.topStockDescription = "";
        
        item.topStockDescriptionName ="";
                
        item.cvsTable = null;
        item.cvsTable2 = null;
        
        return item;
    }
    
    //Used My Table Manager without cvsTable
    Item(String[] content){
        this.fileName = content[0];
        this.forecastDay = content[1];
        this.targetDay = content[2];
        this.timeFrame = content[3];
        this.positionType = content[4];
        this.topN = content[5];
        this.packageContent = content[6];
        this.subpackage = content[7];
        this.keyword1 = content[8];
        this.keyword1Date = content[9];
        this.keyword2 = content[10];
        this.topStockDescription = content[11];
        
        this.topStockDescriptionName = content[12];
        
        this.cvsTable = new CVSTable();
        this.cvsTable2 = new CVSTable();
    }
    
    public void setCvsTable(CVSTable inputTable){
        this.cvsTable = inputTable;
    }
    public void setCvsTableBoth(CVSTable inputTable1, CVSTable inputTable2){
        this.cvsTable = inputTable1;
        this.setCvsTable2(inputTable2);
    }
    
    private static String whiteSpaceValidation(String inputString){
        return inputString.trim();
    }
    
    
    public void printAll(){
        System.out.println("++++++++++++++++++++++++++++++");
        System.out.print("File Name : "+ getFileName()+" / ");
        System.out.print("forecastDay : "+ getForecastDay()+" / ");
        System.out.print("targetDay : "+ getTargetDay()+" / ");
        System.out.print("timeFrame : "+ getTimeFrame()+" / ");
        System.out.print("positionType : "+ getPositionType()+" / ");
        System.out.print("topN : "+ getTopN()+" / ");
        System.out.print("packageContent : "+ getPackageContent()+" / ");
        System.out.print("keyword : "+ getKeyword1()+" / ");
        System.out.print("keyword2 : "+ getKeyword2()+" / ");
        System.out.println("topStockDescription : "+ getTopStockDescription());
        getCvsTable().printAll();
        System.out.println("++++++++++++++++++++++++++++++");
    }
    
    public double getTopStock(){
        return getCvsTable().getHighest(1).getReturnz();
    }
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    public Date stringToDate(String stringDateFormat){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //dateFormat
        Date convertedDateFormat = null;
        try{
            convertedDateFormat = dateFormat.parse(stringDateFormat);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateFormat;
    }
    public String dateToString(Date date, DateFormat dateFormat){
        String stringDateFormat = dateFormat.format(date);
        return stringDateFormat;
    }
    /**
     * @return the forecastDay
     */
    public String getForecastDay() {
        return forecastDay;
    }
    public String getForecastDay(DateFormat dateFormat) {
        String convertedForecastDay = dateToString(stringToDate(forecastDay),dateFormat);
        return convertedForecastDay;
    }
    public String getForecastDateDay(){
        Calendar forcastCal = Calendar.getInstance();
        forcastCal.setTime(stringToDate(forecastDay));
        int forcastDay = forcastCal.get(Calendar.DATE);
        return Integer.toString(forcastDay);
    }
    public String getForecastDateMonth(){
        Calendar forcastCal = Calendar.getInstance();
        forcastCal.setTime(stringToDate(forecastDay));
        int forcastMonth = forcastCal.get(Calendar.MONTH);
        return month[forcastMonth];
    }
    public String getForecastDateYear(){
        Calendar forcastCal = Calendar.getInstance();
        forcastCal.setTime(stringToDate(forecastDay));
        int forcastYear = forcastCal.get(Calendar.YEAR);
        return Integer.toString(forcastYear);
    }
    /**
     * @return the targetDay
     */
    public String getTargetDay() {
        return targetDay;
    }
    public String getTargetDay(DateFormat dateFormat) {
        String convertedForecastDay = dateToString(stringToDate(targetDay),dateFormat);
        return convertedForecastDay;
    }
    public String getTargetDateDay(){
        Calendar forcastCal = Calendar.getInstance();
        forcastCal.setTime(stringToDate(targetDay));
        int forcastDay = forcastCal.get(Calendar.DATE);
        return Integer.toString(forcastDay);
    }
    public String getTargetDateMonth(){
        Calendar forcastCal = Calendar.getInstance();
        forcastCal.setTime(stringToDate(targetDay));
        int forcastMonth = forcastCal.get(Calendar.MONTH);
        return month[forcastMonth];
    }
    public String getTargetDateYear(){
        Calendar forcastCal = Calendar.getInstance();
        forcastCal.setTime(stringToDate(targetDay));
        int forcastYear = forcastCal.get(Calendar.YEAR);
        return Integer.toString(forcastYear);
    }
    /**
     * @return the timeFrame
     */
    public String getTimeFrame() {
        return timeFrame;
    }

    /**
     * @return the positionType
     */
    public String getPositionType() {
        return positionType;
    }

    /**
     * @return the topN
     */
    public String getTopN() {
        return topN;
    }

    /**
     * @return the packageContent
     */
    public String getPackageContent() {
        return packageContent;
    }

    /**
     * @return the keyword1
     */
    public String getKeyword1() {
        return keyword1;
    }

    /**
     * @return the keyword2
     */
    public String getKeyword2() {
        if(this.keyword2.equalsIgnoreCase(""))
            return "";
        else
            return keyword2;
    }

    /**
     * @return the topStockDescription
     */
    public String getTopStockDescription() {
        return topStockDescription;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param forecastDay the forecastDay to set
     */
    public void setForecastDay(String forecastDay) {
        this.forecastDay = forecastDay;
    }

    /**
     * @param targetDay the targetDay to set
     */
    public void setTargetDay(String targetDay) {
        this.targetDay = targetDay;
    }

    /**
     * @param timeFrame the timeFrame to set
     */
    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    /**
     * @param positionType the positionType to set
     */
    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    /**
     * @param topN the topN to set
     */
    public void setTopN(String topN) {
        this.topN = topN;
    }

    /**
     * @param packageContent the packageContent to set
     */
    public void setPackageContent(String packageContent) {
        this.packageContent = packageContent;
    }

    /**
     * @param keyword1 the keyword1 to set
     */
    public void setKeyword1(String keyword1) {
        keyword1 = whiteSpaceValidation(keyword1);
        this.keyword1 = keyword1;
    }

    /**
     * @param keyword2 the keyword2 to set
     */
    public void setKeyword2(String keyword2) {
        keyword2 = whiteSpaceValidation(keyword2);
        this.keyword2 = keyword2;
    }

    /**
     * @param topStockDescription the topStockDescription to set
     */
    public void setTopStockDescription(String topStockDescription) {
        topStockDescription = whiteSpaceValidation(topStockDescription);
        this.topStockDescription = topStockDescription;
    }

    /**
     * @return the cvsTable
     */
    public CVSTable getCvsTable() {
        return cvsTable;
    }

    /**
     * @return the cvsTable2
     */
    public CVSTable getCvsTable2() {
//        if(cvsTable2 == null){
//            return new CVSTable();
//        }
        return cvsTable2;
    }

    /**
     * @param cvsTable2 the cvsTable2 to set
     */
    public void setCvsTable2(CVSTable cvsTable2) {
        this.cvsTable2 = cvsTable2;
    }

    /**
     * @return the Subpackage
     */
    public String getSubpackage() {
        if(subpackage == null)
            return "";
        else
            return subpackage;
    }

    /**
     * @param Subpackage the Subpackage to set
     */
    public void setSubpackage(String Subpackage) {
        this.subpackage = Subpackage;
    }

    /**
     * @return the topStockDescriptionName
     */
    public String getTopStockDescriptionName() {
        return topStockDescriptionName;
    }

    /**
     * @param topStockDescriptionName the topStockDescriptionName to set
     */
    public void setTopStockDescriptionName(String topStockDescriptionName) {
        topStockDescriptionName = whiteSpaceValidation(topStockDescriptionName);
        this.topStockDescriptionName = topStockDescriptionName;
    }

    /**
     * @return the keyword1Date
     */
    public String getKeyword1Date() {
        return keyword1Date;
    }

    /**
     * @param keyword1Date the keyword1Date to set
     */
    public void setKeyword1Date(String keyword1Date) {
        this.keyword1Date = keyword1Date;
    }
    
    //////////////////////////////
    public boolean matchPositionTypeKey(Dictionary positionTypeDictionary, String value, String key){
        if(positionTypeDictionary.findKey(value).equalsIgnoreCase(key))
            return true;
        else
            return false;
    }
    public String getCSVPath(String csvDirectory){
        String csvPath ="";
        String os = System.getProperty("os.name");
        if(os.startsWith("Windows"))
            csvPath = csvDirectory.replace("\\", "/") + fileName;
        else
            csvPath = csvDirectory + fileName;
        
        if(csvPath.contains("Dropbox")){
            int firstOc = csvPath.indexOf("Dropbox");
            csvPath = csvPath.substring(firstOc);
            System.out.println("firstOc : "+ firstOc+ " "+csvPath);

        }
        return csvPath;
    }
    public String getAccuracy(Dictionary positionTypeDictionary){
        //if(positionTypeDictionary.findKey(positionType).equalsIgnoreCase("Long & Short"))
        if(matchPositionTypeKey(positionTypeDictionary,positionType,"Long & Short")){
            int accurate = cvsTable.countAccurate()+cvsTable2.countAccurate();
            return Integer.toString(accurate);
        }else{
            return Integer.toString(cvsTable.countAccurate());
        }
    }
    public String getTotalNumber(Dictionary positionTypeDictionary){
        if(matchPositionTypeKey(positionTypeDictionary,positionType,"Long & Short")){
            int total = cvsTable.countTotalRow()+cvsTable2.countTotalRow();
            return Integer.toString(total);
        }else{
            return Integer.toString(cvsTable.countTotalRow());
        }
    }
    public String getMarketPremium1(){
        return String.format("%.2f",cvsTable.getIKnowFirstAvg().getReturnz()-cvsTable.getSNP500().getReturnz());
    }
    public String getMarketPremium2(Dictionary positionTypeDictionary){
        if(matchPositionTypeKey(positionTypeDictionary,positionType,"Long & Short"))
            return String.format("%.2f",cvsTable2.getIKnowFirstAvg().getReturnz()-cvsTable2.getSNP500().getReturnz());
        else
            return "";
    }
    public String getSNP500Return(){
        return String.format("%.2f",cvsTable.getSNP500().getReturnz());
    }
    public String getAvgReturn1(){
        return String.format("%.2f",getCvsTable().getIKnowFirstAvg().getReturnz());
    }
    public String getAvgReturn2(){
        if(cvsTable2.getCvsTable() != null)
            return String.format("%.2f",getCvsTable2().getIKnowFirstAvg().getReturnz());
        else
            return "";
    }
    public String getAvgReturn2(Dictionary positionTypeDictionary){
        if(matchPositionTypeKey(positionTypeDictionary,positionType,"Long & Short"))
            return String.format("%.2f",getCvsTable2().getIKnowFirstAvg().getReturnz());
        else
            return "";
    }
    public List<RowData> getTop3(Dictionary positionTypeDictionary){
        List<RowData> returnList = new ArrayList();
        if(matchPositionTypeKey(positionTypeDictionary,positionType,"Long & Short")){
            
            returnList.add(cvsTable.getHighest(1));
            returnList.add(cvsTable.getHighest(2));
            returnList.add(cvsTable.getHighest(3));
            
            returnList.add(cvsTable2.getHighest(1));
            returnList.add(cvsTable2.getHighest(2));
            returnList.add(cvsTable2.getHighest(3));
            
            Collections.sort(returnList);
            
            System.out.println("Long & Short");
            for(RowData row: returnList){
                System.out.println("Row : "+row.getSymbol()+ " Value : "+row.getReturnz());
            }

        }else{//Not Long & Short
            
            returnList.add(cvsTable.getHighest(1));
            returnList.add(cvsTable.getHighest(2));
            returnList.add(cvsTable.getHighest(3));
            
            System.out.println("Other");
            for(RowData row: returnList){
                System.out.println("Row : "+row.getSymbol()+ " Value : "+row.getReturnz());
            }
        }
        
        return returnList;
        
    }
    public String getTopReturn(Dictionary positionTypeDictionary){//String.format("%.2f",cvsTable2.getHighest(1).getReturnz())
        if(matchPositionTypeKey(positionTypeDictionary,positionType,"Long & Short")){
            double longReturn = cvsTable.getHighest(1).getReturnz();
            double shortReturn = cvsTable2.getHighest(1).getReturnz();
            if(longReturn >= shortReturn)
                return String.format("%.2f",longReturn);
            else
                return String.format("%.2f",shortReturn);
        }else{
            double anyReturn = cvsTable.getHighest(1).getReturnz();
            return String.format("%.2f",anyReturn); 
        }
    }
    public String getTopStockName(Dictionary positionTypeDictionary){//String.format("%.2f",cvsTable2.getHighest(1).getReturnz())
        if(matchPositionTypeKey(positionTypeDictionary,positionType,"Long & Short")){
            double longReturn = cvsTable.getHighest(1).getReturnz();
            double shortReturn = cvsTable2.getHighest(1).getReturnz();
            if(longReturn >= shortReturn)
                return cvsTable.getHighest(1).getSymbol();
            else
                return cvsTable2.getHighest(1).getSymbol();
        }else{
            cvsTable.getHighest(1).getReturnz();
            return cvsTable.getHighest(1).getSymbol();
        }
    }
}
