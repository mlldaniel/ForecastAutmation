/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import static main.ForcastUi.fileExtCheck;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.ForcastUi;
import main.Item;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Daniel
 */
public class TopStockDescriptionList {
    private List<TopStockDescription> topStockDescriptionList;
    public TopStockDescriptionList(){
        topStockDescriptionList = new ArrayList();
    }
    public TopStockDescriptionList(List<TopStockDescription> topStockDescriptionList){
        this.topStockDescriptionList = topStockDescriptionList;
    }
    public static List<TopStockDescription> readFromCSVFile(String fileName){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy =",";
        
        List<TopStockDescription> tempTSD = new ArrayList();
        
        String longName= "";
        String shortName = "";
        String explanation = "";
        
        
        try{
            if(!fileExtCheck(fileName)){
                //throw new IOException("BAD EXTENSION FILE UPLOAD");
                return null;
            }           
            
            System.out.println("Opening filename: "+fileName);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
            
            while((line = br.readLine()) != null){
                String[] row = line.split(cvsSplitBy);
                
                if(row[0].equalsIgnoreCase("Name")){
                    continue;
                }
                longName = row[0];//.replaceAll("\"", "").trim();
                shortName = row[1];//.replaceAll("\"", "").trim();
                explanation = row[2];//.replaceAll("\"", "").trim();
                
                tempTSD.add(new TopStockDescription(shortName,longName,explanation,false));
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
        
        return (tempTSD);
    }
    public static FileInputStream openExcelFileOrCreate(String fileName) throws Exception{

        File file = new File(fileName);
        if(file.isFile() && file.exists())
        {
           System.out.println(fileName+" file open successfully.");
        }
        else
        {
           System.out.println("File doesnt exist : "+fileName);
           FileOutputStream out = new FileOutputStream(new File(fileName));
           XSSFWorkbook workbook = new XSSFWorkbook();
           workbook.write(out);
           out.close();
           file = new File(fileName);
        }

        return new FileInputStream(file);

    }
    public static List<TopStockDescription> readFromFileExcel(String fileName){

        List<TopStockDescription> tempTSD = new ArrayList();
        String longName= "";
        String shortName = "";
        String explanation = "";
        
        try{
            ForcastUi.consoleLog("Opening filename: "+fileName);
            
            FileInputStream fIP =  openExcelFileOrCreate(fileName);
            //Get the workbook instance for XLSX file 
            XSSFWorkbook workbook = new XSSFWorkbook(fIP);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator < Row > rowIterator = spreadsheet.iterator();
            XSSFRow row;
            while (rowIterator.hasNext()){
                row = (XSSFRow) rowIterator.next();
                Iterator < Cell > cellIterator = row.cellIterator();
                
                //한줄 읽어오기
                Cell cell = cellIterator.next();
                longName =  cell.getStringCellValue();
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    shortName =  cell.getStringCellValue();
                    if(shortName.isEmpty())
                        continue;
                }else
                    continue;
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    explanation =  cell.getStringCellValue();
                    if(shortName.isEmpty())
                        continue;
                }else
                    continue;
                
                tempTSD.add(new TopStockDescription(shortName.trim(),longName.trim(),explanation.trim(),false));
            }
            fIP.close();
            
        }catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        }catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) { 
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(ex.getMessage());
            Logger.getLogger(TopStockDescriptionList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tempTSD;
    }
    public void printAll(){
        for(TopStockDescription tsd: getTopStockDescriptionList()){
            System.out.println("Short Name: "+ tsd.getShortName());
            System.out.println("Long Name: "+ tsd.getLongName());
            System.out.println("Text: "+ tsd.getContentText()+ "\n");
        }
    }
    
    public TopStockDescription selectRowByShortName(String shortName){
        for(TopStockDescription tsd : getTopStockDescriptionList()){
            if(tsd.getShortName().equalsIgnoreCase(shortName))
                return tsd;
        }
        return null;
    }
    public int getRowNumberByShortName(String shortName){
        for(int i = 0; i < getTopStockDescriptionList().size(); i++){
            if(getTopStockDescriptionList().get(i).getShortName().equalsIgnoreCase(shortName))
                return i;
        }
        return -1;
    }
    public void setTSDWhere(int i, String contentText){
        getTopStockDescriptionList().get(i).setContentText(contentText);
    }
    public TopStockDescription getTSDWhere(int i){
        return getTopStockDescriptionList().get(i);
    }
    public boolean isEqualTo(int i, String contentText){
        if(getTopStockDescriptionList().get(i).getContentText().equalsIgnoreCase(contentText))
            return true;
        else
            return false;
    }
    public void addTSD(String shortName, String contentText){
        getTopStockDescriptionList().add(new TopStockDescription(shortName,"",contentText,true));
    }
    
    public List<TopStockDescription> getChangedTopStockDescriptionList(List<Item> itemList){
        List<TopStockDescription> tempTSDList = new ArrayList();
        
        for(Item item : itemList){
            int tsdID = this.getRowNumberByShortName(item.getTopStockDescriptionName());
            
            if(tsdID == -1){ //IF it doesn't exist Add new one
                tempTSDList.add(new TopStockDescription(item.getTopStockDescriptionName(), 
                                "", item.getTopStockDescription(), true));
                //this.addTSD(item.getTopStockDescriptionName(),item.getTopStockDescription());
            }else if(!this.isEqualTo(tsdID, item.getTopStockDescription())){
                // If need Update
                this.setTSDWhere(tsdID, item.getTopStockDescription());
                tempTSDList.add(this.getTSDWhere(tsdID));
            }
                
            
        }
        return tempTSDList;
    }
    public void updateTopStockDescriptionList(List<Item> itemList){
        for(Item item : itemList){
            int tsdID = this.getRowNumberByShortName(item.getTopStockDescriptionName());
            
            if(tsdID == -1) //IF it doesn't exist Add new one
                this.addTSD(item.getTopStockDescriptionName(),item.getTopStockDescription());
            else if(!this.isEqualTo(tsdID, item.getTopStockDescription())){
                // If need Update
                this.setTSDWhere(tsdID, item.getTopStockDescription());
            }
                
            
        }
    }
    
    public static void writeToFileExcel(String fileName, List<TopStockDescription> tsdData){
        int SHORTNAME = 1;
        int LONGNAME = 0;
        int TEXT = 2;
        
        try{    
            FileInputStream fileIn =  new FileInputStream(fileName);
            
            Workbook wb = WorkbookFactory.create(fileIn);
            Sheet sheet = wb.getSheetAt(0);
            
            for(TopStockDescription tsd : tsdData){
                if(tsd.getContentText().isEmpty())
                    continue;
                //같은거 찾아서 교체하기 or Create
                int rowInt=1;
                while(true){
                    Row row = sheet.getRow(rowInt);
                    
                    
                    if(row == null){// THE END OF THE ROW not found
                        //Create
                        row = sheet.createRow(rowInt);
                        Cell cellShortName = row.createCell(SHORTNAME);
                        cellShortName.setCellType(Cell.CELL_TYPE_STRING);
                        cellShortName.setCellValue(tsd.getShortName());
                        
                        String longName = tsd.getShortName();
                        int endIndex = tsd.getContentText().indexOf(tsd.getShortName());
                        if(endIndex>0 && endIndex < tsd.getContentText().length())
                            longName = tsd.getContentText().substring(0, endIndex-1).trim();
                        
                        Cell cellLongName = row.createCell(LONGNAME);
                        cellLongName.setCellType(Cell.CELL_TYPE_STRING);
                        cellLongName.setCellValue(longName);
                        
                        Cell cellText = row.createCell(TEXT);
                        cellText.setCellType(Cell.CELL_TYPE_STRING);
                        cellText.setCellValue(tsd.getContentText());
                        break;
                    }
                    Cell cellShortName = row.getCell(SHORTNAME);
                    String shortName = cellShortName.getRichStringCellValue().getString();
                    if(shortName.equalsIgnoreCase(tsd.getShortName())){//If Match, Update
                        //Cell cellLongName = row.getCell(LONGNAME);
                        //cellLongName.setCellType(Cell.CELL_TYPE_STRING);
                        //cellLongName.setCellValue(tsd.getLongName());
                        
                        Cell cellText = row.getCell(TEXT);
                        cellText.setCellType(Cell.CELL_TYPE_STRING);
                        cellText.setCellValue(tsd.getContentText());
                        break;
                    }
                    rowInt++;
                }
            }
            
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();
            fileIn.close();
            
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) { 
            Logger.getLogger(TopStockDescriptionList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public boolean isEmpty(){
        return topStockDescriptionList.isEmpty() ? true:false;
    }
    /**
     * @return the topStockDescriptionList
     */
    public List<TopStockDescription> getTopStockDescriptionList() {
        return topStockDescriptionList;
    }
}
