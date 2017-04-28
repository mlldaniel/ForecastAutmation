package main;


import DB.ErrorMessages;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import static main.ForcastUi.fileExtCheck;
import static main.ForcastUi.stringToDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */
public class CVSTable {
    private String fileName;

    private Date updateDate;
    private Date targetDate;
    private List<RowData> cvsTable;
    private RowData iKnowFirstAvg;
    private RowData snp500;
    
    private String positionType;
    
    public static String slash = File.separator; 
    /*
    public CVSTable(List<RowData> cvsTable, RowData iKnowFirstAvg, RowData snp500){
        this.cvsTable = cvsTable;
        this.iKnowFirstAvg = iKnowFirstAvg;
        this.snp500 = snp500;
        sortByReturn();
    }*/
    public CVSTable(){
        this.fileName = null;
        this.updateDate = null;
        this.targetDate = null;
        this.cvsTable = null;
        this.iKnowFirstAvg = null;
        this.snp500 = null;
        this.positionType = null;
    }
    public CVSTable(String fileName,Date updateDate, Date targetDate, List<RowData> cvsTable, RowData iKnowFirstAvg, RowData snp500){
        this.fileName = fileName;
        this.updateDate = updateDate;
        this.targetDate = targetDate;
        this.cvsTable = cvsTable;
        this.iKnowFirstAvg = iKnowFirstAvg;
        this.snp500 = snp500;
        this.positionType = null;
    }
    public CVSTable(String fileName,Date updateDate, Date targetDate){
        this.fileName = fileName;
        this.updateDate = updateDate;
        this.targetDate = targetDate;
    }
    
    public void printAll(){
        System.out.println("Update Date : "+updateDate + " Target Date : "+targetDate);
        this.getCvsTable().stream().forEach((row) -> {
            System.out.println("Symbol = "+ row.getSymbol() +" , Prediction: "+row.getPrediction()+ " , Return = " + row.getReturnz()+ " , Accuracy: "+row.getAccuracy());
        });
        
        if(getiKnowFirstAvg() != null)
            System.out.println("Symbol = "+ getiKnowFirstAvg().getSymbol() +" , Prediction: "+getiKnowFirstAvg().getPrediction()+ " , Return = " + getiKnowFirstAvg().getReturnz()+ " , Accuracy: "+getiKnowFirstAvg().getAccuracy());
        if(getSnp500() != null)
            System.out.println("Symbol = "+ getSnp500().getSymbol() +" , Prediction: "+getSnp500().getPrediction()+ " , Return = " + getSnp500().getReturnz()+ " , Accuracy: "+getSnp500().getAccuracy());
        //System.out.println("I Know First AVG = "+getIKnowFirstAvg());
        //System.out.println("S&P500 = "+getSNP500());
        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
    
    }
    
    public int countTotalRow(){
        return cvsTable.size();
    }
    public int countAccurate(){
        int count = 0;
        count = cvsTable.stream().filter((row) -> (row.getAccuracy() == 1)).map((_item) -> 1).reduce(count, Integer::sum);
        return count;
    }
    
    public void setPositionType(String positionType){
        this.positionType = positionType;
    }
    
    public Date getUpdateDate(){
        return updateDate;
    }
    
    public Date getTargetDate(){
        return targetDate;
    }
    
    public void sortCVSTableByReturn(boolean isShort){
        List<RowData> tempCVSTable =  getCvsTable();
        for(RowData row : tempCVSTable){
            row.setReturnz(row.getPrediction()*row.getReturnz());
        }
        
        Collections.sort(tempCVSTable);
        
        setCvsTable(tempCVSTable);
    }
    
    public List<RowData> getcvsTable(){
        return this.getCvsTable();
    }
    public RowData getIKnowFirstAvg(){
        if(iKnowFirstAvg == null)
            return new RowData("IKNOWFIRST_AVG",0.0);
        return iKnowFirstAvg;
    }
    public RowData getSNP500(){
        if(snp500 == null)
            return new RowData("S&P500",0.0);
        return getSnp500();
    }
    public String getSNP500State(){
        if(snp500 == null)
            return "0";
        if(snp500.getReturnz() > 0 )
            return "positive";
        else
            return "negative";
    }
    
    public RowData getLowest(int th){
        int size = getCvsTable().size();
        return getCvsTable().get(size-th);
    }
    
    public RowData getHighest(int th){
        if(countTotalRow() < th)
            return new RowData("DUMMY", 0, "0.0", 0);
        return getCvsTable().get(th-1);
    }
    public List<String> getSymboleWhereAccuracyOne(){
        
        List<String> list = new ArrayList();
        for (Iterator<RowData> iter = cvsTable.listIterator(); iter.hasNext(); ) {
            RowData aRow  = iter.next();
            if (aRow.getAccuracy()==1) {
                list.add(aRow.getSymbol());
            }
        }
        return list;
    }
    public List<String> getSymboleWhereAccuracyOneLowerCase(){
        
        List<String> list = new ArrayList();
        for (Iterator<RowData> iter = cvsTable.listIterator(); iter.hasNext(); ) {
            RowData aRow  = iter.next();
            if (aRow.getAccuracy()==1) {
                list.add(aRow.getSymbol().toLowerCase());
            }
        }
        return list;
    }
    public List<String> getSymbolList(){
        List<String> list = new ArrayList();
        for(RowData row : cvsTable){
            list.add(row.getSymbol());
        }
        return list;
    }
    public List<String> getSymbolListLowerCase(){
        List<String> list = new ArrayList();
        for(RowData row : cvsTable){
            list.add(row.getSymbol().toLowerCase());
        }
        return list;
    }
    public RowData getRowWhereSymbolIs(String symbol){
        RowData result = null;
        for(RowData row : getCvsTable()){
            if(row.getSymbol().equalsIgnoreCase(symbol))
                result = row;
        }
        return result;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @param targetDate the targetDate to set
     */
    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    /**
     * @return the cvsTable
     */
    public List<RowData> getCvsTable() {
        return cvsTable;
    }

    /**
     * @param cvsTable the cvsTable to set
     */
    public void setCvsTable(List<RowData> cvsTable) {
        this.cvsTable = cvsTable;
    }

    /**
     * @return the iKnowFirstAvg
     */
    public RowData getiKnowFirstAvg() {
        return iKnowFirstAvg;
    }

    /**
     * @param iKnowFirstAvg the iKnowFirstAvg to set
     */
    public void setiKnowFirstAvg(RowData iKnowFirstAvg) {
        this.iKnowFirstAvg = iKnowFirstAvg;
    }

    /**
     * @return the snp500
     */
    public RowData getSnp500() {
        return snp500;
    }

    /**
     * @param snp500 the snp500 to set
     */
    public void setSnp500(RowData snp500) {
        this.snp500 = snp500;
    }

    /**
     * @return the positionType
     */
    public String getPositionType() {
        return positionType;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static Item openCSVfileToGuess(String directory, String fileName){
        BufferedReader br = null;
        String line;
        String cvsSplitBy =",";
        
        Item returnObject=new Item();
        returnObject.setTopN("-");
        returnObject.setPositionType("-");
        
        Date updateDate = null;
        Date targetDate = null;
        List<RowData> cvsValues = new ArrayList();
        List<RowData> cvsValues2 = new ArrayList();
        RowData iKnowFirstAvg = null;
        RowData snp500 = null;
        
        int rowCount = 0;
        int predictionCount = 0;
        boolean longNShort = false;
        try{
            if(!fileExtCheck(fileName)){
                //throw new IOException("BAD EXTENSION FILE UPLOAD");
                return null;
            }           
            
            //System.out.println("Opening filename :"+directory+ slash +fileName);
            //ForcastUi.consoleLog("Opening filename :"+directory+slash+fileName);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(directory+slash+fileName),"UTF-8")); // new InputStreamReader(new FileInputStream(directory+"\\"+fileName),"UTF-8")
            
            //========================================
            // Reading Two Dates
            //========================================
            
            //Read the First Line
            
            line = br.readLine();
            String[] row = line.split(cvsSplitBy);
            if(row[0].equalsIgnoreCase("Update Date")){
                updateDate = stringToDate(row[1]);
                returnObject.setForecastDay(row[1]);
            }else if(row[0].equalsIgnoreCase("Target Date")){
                targetDate = stringToDate(row[1]);
                returnObject.setTargetDay(row[1]);
            }
            
            //Read the Second Line
            line = br.readLine();      
            row = line.split(cvsSplitBy);
            
            if(row[0].equalsIgnoreCase("Update Date")){
                updateDate = stringToDate(row[1]);
                returnObject.setForecastDay(row[1]);
            }else if(row[0].equalsIgnoreCase("Target Date")){
                targetDate = stringToDate(row[1]);
                returnObject.setTargetDay(row[1]);
            }
            
            //========================================
            // Symbol Prediction Retrun Accuracy
            //========================================
            while((line = br.readLine()) != null){
                row = line.split(cvsSplitBy);
                
                //Check if It is ,,, (empty lines) Have to go for next Table
                if(row.length == 0){
                    break;
                }
                //Check if it is the title part
                if(row[0].equalsIgnoreCase("Symbol") )
                    continue;
                
                // Normal data
                RowData tempData;
                if(row.length == 4)
                    tempData = new RowData(row[0],Integer.parseInt(row[1]),row[2],Integer.parseInt(row[3]));
                else
                    tempData = new RowData(row[0],null,row[2],null);            
                
                //Check if it is IKNOW FIRST AVG OR SNP OR other Symbol
                switch(tempData.getSymbol()){
                    case "IKNOWFIRST_AVG":
                        iKnowFirstAvg = tempData.getThis();
                        break;
                    case "S&P500":
                        snp500 = tempData.getThis();
                        break;
                    default:
                        if(tempData.getPrediction() == null){
                            continue;
                        }else if(tempData.getPrediction() == 1){
                            predictionCount ++;
                        }else if(tempData.getPrediction() == -1){
                            predictionCount --;
                        }
                        cvsValues.add(tempData);
                        rowCount++;
                }
//                //Check if it is IKNOW FIRST AVG OR SNP OR other
//                if(tempData.getSymbol().equalsIgnoreCase("IKNOWFIRST_AVG")){
//                    iKnowFirstAvg = tempData.getThis();
//                }else if(tempData.getSymbol().equalsIgnoreCase("S&P500")){
//                    snp500 = tempData.getThis();
//                }else{
//                    
//                    cvsValues.add(tempData);
//                    if(tempData.getPrediction() == 1){
//                        predictionCount ++;
//                    }else if(tempData.getPrediction() == -1){
//                        predictionCount --;
//                    }
//                    rowCount++;
//                }
                

            }
            returnObject.setTopN(Integer.toString(rowCount));
            if(predictionCount > 0)
                returnObject.setPositionType("Long");
            else
                returnObject.setPositionType("Short");
            
            returnObject.setCvsTable(new CVSTable(fileName, updateDate, targetDate, cvsValues, iKnowFirstAvg, snp500));
            
            
            while((line = br.readLine()) != null){
                row = line.split(cvsSplitBy);
                
                //Check if It is ,,, (empty lines) Have to go for next Table
                if(row.length == 0){
                    continue;
                }
                
                // Normal data
                RowData tempData;
                
                //Check if data is is like AAA,,22, because it will be count as 3 when splited
                if(row.length == 4)
                    tempData = new RowData(row[0],Integer.parseInt(row[1]),row[2],Integer.parseInt(row[3]));
                else
                    tempData = new RowData(row[0],null,row[2],null);
                
                //Check if it is IKNOW FIRST AVG OR SNP OR other
                if(tempData.getSymbol().equalsIgnoreCase("IKNOWFIRST_AVG")){
                    iKnowFirstAvg = tempData.getThis();
                    longNShort=true;    
                }else if(tempData.getSymbol().equalsIgnoreCase("S&P500")){
                    snp500 = tempData.getThis();
                    longNShort=true;
                }else{
                    if(tempData.getPrediction()==null || tempData.getAccuracy() ==null)
                        continue;
                    cvsValues2.add(tempData);
                }
                
            }
            
            if(longNShort){
                returnObject.setPositionType("Long & Short");
                returnObject.setCvsTable2(new CVSTable(fileName, updateDate, targetDate, cvsValues2, iKnowFirstAvg, snp500) );
            }
            

            
        }catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            
        }
        
        return returnObject;
        
    }
    
    public static CVSTable openCVSfile(String directory, String fileName){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy =",";
        
        
        Date updateDate = null;
        Date targetDate = null;
        List<RowData> cvsValues = new ArrayList();
        
        RowData iKnowFirstAvg = null;
        RowData snp500 = null;
        
        CVSTable returnObject=null;
        
        try{
            if(!fileExtCheck(fileName)){
                //throw new IOException("BAD EXTENSION FILE UPLOAD");
                return null;
            }           
            
            System.out.println("Opening filename :"+directory+slash+fileName);
            ForcastUi.consoleLog("Opening filename :"+directory+slash+fileName);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(directory+slash+fileName),"UTF-8"));
            
            //========================================
            // Reading Two Dates
            //========================================
            
            //Read the First Line
            line = br.readLine();
            String[] row = line.split(cvsSplitBy);
            
            if(row[0].equalsIgnoreCase("Update Date")){
                updateDate = stringToDate(row[1]);
            }else if(row[0].equalsIgnoreCase("Target Date")){
                targetDate = stringToDate(row[1]);
            }
            
            //Read the Second Line
            line = br.readLine();
            row = line.split(cvsSplitBy);
            
            if(row[0].equalsIgnoreCase("Update Date")){
                updateDate = stringToDate(row[1]);
            }else if(row[0].equalsIgnoreCase("Target Date")){
                targetDate = stringToDate(row[1]);
            }
            
            
            //========================================
            // Symbol Prediction Retrun Accuracy
            //========================================
            
            
            while((line = br.readLine()) != null){
                row = line.split(cvsSplitBy);
                
                //
                
                //Check if it is the title part
                if(row[0].toUpperCase().equals("symbol".toUpperCase()) )
                    continue;
                
                // Normal data
                RowData tempData;
                if(row.length == 4)
                    tempData = new RowData(row[0],Integer.parseInt(row[1]),row[2],Integer.parseInt(row[3]));
                else
                    tempData = new RowData(row[0],null,row[2],null);
                
                //Check if it is IKNOW FIRST AVG OR SNP OR other
                if(tempData.getSymbol().equalsIgnoreCase("IKNOWFIRST_AVG")){
                    iKnowFirstAvg = tempData.getThis();
                }else if(tempData.getSymbol().equalsIgnoreCase("S&P500")){
                    snp500 = tempData.getThis();
                }else{
                    if(tempData.getPrediction()==null || tempData.getAccuracy() ==null)
                        continue;
                    cvsValues.add(tempData);
                }
                //System.out.println("Symbol = "+ row[0] + " , Return = " + row[1]);
                
            }
            
            returnObject = new CVSTable(fileName,updateDate, targetDate, cvsValues, iKnowFirstAvg, snp500);
            
        }catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            
        }
        
        return returnObject;
    }
    
    public static List<CVSTable> openCVSfileLongAndShort(String directory, String fileName){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy =",";
        
        
        Date updateDate = null;
        Date targetDate = null;
        List<RowData> cvsValues = new ArrayList();
        List<RowData> cvsValues2 = new ArrayList();
        RowData iKnowFirstAvg = null;
        RowData snp500 = null;
        
        List<CVSTable> returnObject=null;
        
        try{
            if(!fileExtCheck(fileName)){
                //throw new IOException("BAD EXTENSION FILE UPLOAD");
                return null;
            }           
            
            System.out.println("Opening filename :"+directory+slash+fileName);
            ForcastUi.consoleLog("Opening filename :"+directory+slash+fileName);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(directory+slash+fileName),"UTF-8"));
            
            //========================================
            // Reading Two Dates
            //========================================
            
            //Read the First Line
            line = br.readLine();
            String[] row = line.split(cvsSplitBy);
            
            if(row[0].equalsIgnoreCase("Update Date")){
                updateDate = stringToDate(row[1]);
            }else if(row[0].equalsIgnoreCase("Target Date")){
                targetDate = stringToDate(row[1]);
            }
            
            //Read the Second Line
            line = br.readLine();
            row = line.split(cvsSplitBy);
            
            if(row[0].equalsIgnoreCase("Update Date")){
                updateDate = stringToDate(row[1]);
            }else if(row[0].equalsIgnoreCase("Target Date")){
                targetDate = stringToDate(row[1]);
            }
            
            
            //========================================
            // Symbol Prediction Retrun Accuracy
            //========================================
                        
            while((line = br.readLine()) != null){
                row = line.split(cvsSplitBy);
                
                //Check if It is ,,, (empty lines) Have to go for next Table
                if(row.length == 0){
                    break;
                }
                
                //Check if it is the title part
                if(row[0].equalsIgnoreCase("symbol"))
                    continue;
                
                // Normal data
                RowData tempData;
                
                //Check if data is is like AAA,,22, because it will be count as 3 when splited
                if(row.length == 4)
                    tempData = new RowData(row[0],Integer.parseInt(row[1]),row[2],Integer.parseInt(row[3]));
                else
                    tempData = new RowData(row[0],null,row[2],null);
                
                //Check if it is IKNOW FIRST AVG OR SNP OR other
                if(tempData.getSymbol().equalsIgnoreCase("IKNOWFIRST_AVG")){
                    iKnowFirstAvg = tempData.getThis();
                }else if(tempData.getSymbol().equalsIgnoreCase("S&P500")){
                    snp500 = tempData.getThis();
                }else{
                    if(tempData.getPrediction()==null || tempData.getAccuracy() ==null)
                        continue;
                    cvsValues.add(tempData);
                }
                //System.out.println("Symbol = "+ row[0] + " , Return = " + row[1]);
                
            }
            returnObject = new ArrayList();
            returnObject.add(new CVSTable(fileName, updateDate, targetDate, cvsValues, iKnowFirstAvg, snp500));
            //cvsValues.clear();
            
            while((line = br.readLine()) != null){
                row = line.split(cvsSplitBy);
                
                //Check if It is ,,, (empty lines) Have to go for next Table
                if(row.length == 0){
                    continue;
                }
                // Normal data
                RowData tempData;
                
                //Check if data is is like AAA,,22, because it will be count as 3 when splited
                if(row.length == 4)
                    tempData = new RowData(row[0],Integer.parseInt(row[1]),row[2],Integer.parseInt(row[3]));
                else
                    tempData = new RowData(row[0],null,row[2],null);
                
                //Check if it is IKNOW FIRST AVG OR SNP OR other
                if(tempData.getSymbol().equalsIgnoreCase("IKNOWFIRST_AVG")){
                    iKnowFirstAvg = tempData.getThis();
                }else if(tempData.getSymbol().equalsIgnoreCase("S&P500")){
                    snp500 = tempData.getThis();
                }else{
                    if(tempData.getPrediction()==null || tempData.getAccuracy() ==null)
                        continue;
                    cvsValues2.add(tempData);
                }
            }
            
            returnObject.add(new CVSTable(fileName, updateDate, targetDate, cvsValues2, iKnowFirstAvg, snp500));
            
        }catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            
        }
        
        return returnObject;  
    }

    public String printView(){
        StringBuilder sb = new StringBuilder();
        //sb.append("<html>");
        sb.append("Forecast Day : "+updateDate+"<br>");
        sb.append("Traget Day : "+targetDate+"<br>");
        sb.append("<table>");
        sb.append("<tr><th>Symbol</th><th>Prediction</th><th>Return (%)</th><th>Accuracy</th></tr>");
        
        for(RowData row: cvsTable){
            sb.append("<tr>");
            
            sb.append("<td>");
            sb.append(row.getSymbol());
            sb.append("</td>");
            
            sb.append("<td>");
            if(row.getPrediction() != null)
                sb.append(", "+row.getPrediction());
            sb.append("</td>");
            
            sb.append("<td>");
            if(row.getReturnz() != null)
                sb.append(", "+row.getReturnz());
            sb.append("</td>");
            
            sb.append("<td>");
            if(row.getAccuracy() !=null)
                sb.append(", "+row.getAccuracy());
            sb.append("</td>");
            
            sb.append("</tr>");
        }
        
        if(iKnowFirstAvg != null){
            sb.append("<tr>");
            
            sb.append("<td>");
            sb.append("I Know First Avg: ");
            sb.append("</td>");
            
            sb.append("<td>");
            if(iKnowFirstAvg.getPrediction() != null)
                sb.append(", "+iKnowFirstAvg.getPrediction());
            sb.append("</td>");
            
            sb.append("<td>");
            if(iKnowFirstAvg.getReturnz() != null)
                sb.append(", "+iKnowFirstAvg.getReturnz());
            sb.append("</td>");
            
            sb.append("<td>");
            if(iKnowFirstAvg.getAccuracy() !=null)
                sb.append(", "+iKnowFirstAvg.getAccuracy());
            
            sb.append("</td>");
            
            sb.append("</tr>");
        }
        
        
        if(snp500 != null){
            sb.append("<tr>");
            
            sb.append("<td>");
            sb.append("S&P 500: ");
            sb.append("</td>");
            
            sb.append("<td>");
            if(snp500.getPrediction() != null)
                sb.append(", "+snp500.getPrediction());
            sb.append("</td>");
            
            sb.append("<td>");
            if(snp500.getReturnz() != null)
                sb.append(", "+snp500.getReturnz());
            sb.append("</td>");
            
            sb.append("<td>");
            if(snp500.getAccuracy() !=null)
                sb.append(", "+snp500.getAccuracy());
            sb.append("</td>");
            
            sb.append("</tr>");
        }
        sb.append("</table>");
        //sb.append("</html>");
        
        return sb.toString();
    }
}
