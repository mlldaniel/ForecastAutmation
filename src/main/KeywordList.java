/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DB.ErrorMessages;
import DB.TopStockDescriptionList;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;



/**
 *
 * @author Daniel
 */
public class KeywordList {
    private List<Keyword> keywordList;
    private List<String> suggestedKeywordList;
    public KeywordList(String fileName){
        //keywordList = readFromCSV(fileName);
        keywordList = readFromExcel(fileName);
        suggestedKeywordList = new ArrayList();
    }
    
    public void printKeywordList(String name){
        ForcastUi.consoleLog("Printing List of "+name);
        keywordList.stream().forEach(a-> ForcastUi.consoleLog("Keyword: "+ a.getKeyword()));
    }

    private boolean checkIfExist(String keyword){
        if (keywordList.stream().anyMatch((cat) -> (cat.getKeyword().equalsIgnoreCase(keyword)))) {
            return true;
        }
        return false;// No Categorie Matches
    }
    
    public Date returnDateIfExist(String keyword){
        Optional <Keyword> returnObj = keywordList.stream().filter(e -> e.getKeyword().equalsIgnoreCase(keyword)).findFirst();
        if(returnObj.isPresent())
            return returnObj.get().getDate();
        else
            return null;
    }
    public String[] chooseKeywordList(String packageName, String subpackageName, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        //Filter Package Name and Subpackage Name
        Stream <Keyword> keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName));
        if(keywordStream.count()>0){// If specific Keyword exist
            //Filter recently used Keyword
            keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName))
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()));
            
            List<String> keywordList = new ArrayList();
            keywordStream.forEach(keyword-> keywordList.add(keyword.getKeyword()));
            //Update the after using it
            return keywordList.toArray(new String[0]);
        }else{// IF that package Name doens't Exist
            
            keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY")) // ANY Package
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()));
            
            
            List<String> keywordList = new ArrayList();
            keywordStream.forEach(keyword-> keywordList.add(keyword.getKeyword()));
            //Update the after using it
            return keywordList.toArray(new String[0]);
        }
    }
    /*public String[] chooseKeywordList2(String packageName, String subpackageName, String positionType, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        //Filter Package Name and Subpackage Name
        List<Keyword> tempKeywordList = filterPackageNameOrANY(keywordList,packageName);
        tempKeywordList = filterSubpackageNameOrANY(tempKeywordList,subpackageName);
        tempKeywordList = filterPositionTypeOrANY(tempKeywordList,positionType);
        
    }*/
    
    private List<Keyword> filterPackageNameOrANY(List<Keyword> inputList, String packageName){
        List<Keyword> KeywordList = inputList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                .collect(Collectors.toList());
        
        if(KeywordList.size()>0)
            return KeywordList;
        else
            return inputList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY"))
                .collect(Collectors.toList());
    }
    private List<Keyword> filterSubpackageNameOrANY(List<Keyword> inputList, String subpackageName){
        List<Keyword> KeywordList = inputList.stream()
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName))
                .collect(Collectors.toList());
        
        if(KeywordList.size()>0)
            return KeywordList;
        else
            return inputList.stream()
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase("ANY"))
                .collect(Collectors.toList());
    }
    private List<Keyword> filterPositionTypeOrANY(List<Keyword> inputList, String positionType){
        List<Keyword> KeywordList = inputList.stream()
                .filter(cat -> cat.getPositionType().equalsIgnoreCase(positionType))
                .collect(Collectors.toList());
        
        if(KeywordList.size()>0)
            return KeywordList;
        else
            return inputList.stream()
                .filter(cat -> cat.getPositionType().equalsIgnoreCase("ANY"))
                .collect(Collectors.toList());
    }
    private List<Keyword> filterPositionTypeANDANY(List<Keyword> inputList, String positionType){
        List<Keyword> KeywordList = inputList.stream()
                .filter(cat -> (cat.getPositionType().equalsIgnoreCase(positionType)||(cat.getPositionType().equalsIgnoreCase("ANY"))))
                .collect(Collectors.toList());
        
        return KeywordList;
    }
    
    public List<Object[]> chooseKeywordListSpecificxxx(String packageName, String subpackageName, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        //Filter Package Name and Subpackage Name
        Stream <Keyword> keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName));
        if(keywordStream.count()>0){
            //Filter recently used Keyword
            keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName))
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()));
            
            List<Object[]> keywordList = new ArrayList();
            keywordStream.forEach(keyword-> keywordList
                    .add(new Object[]{
                        (Object)keyword.getKeyword(),
                        (Object)ForcastUi.dateToString(keyword.getDate()),
                        (Object)Integer.toString(keyword.getPriority())
                    }));
            
            //Update the after using it
            return keywordList;
        }else{// IF There is no specific just return empty List
            
            List<Object[]> keywordList = new ArrayList();
            
            return keywordList;
        }
    }
    public List<Object[]> chooseKeywordListSpecific(String packageName, String subpackageName, String positionType, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        List<Keyword> tempKeywordList = filterPackageNameOrANY(keywordList,packageName);
        tempKeywordList = filterSubpackageNameOrANY(tempKeywordList,subpackageName);
        tempKeywordList = filterPositionTypeOrANY(tempKeywordList,positionType);
        
        if(tempKeywordList.size()>0){
            if(tempKeywordList.get(0).getPackageName().equalsIgnoreCase("ANY")
                    &&tempKeywordList.get(0).getSubpackageName().equalsIgnoreCase("ANY")
                    &&tempKeywordList.get(0).getPositionType().equalsIgnoreCase("ANY")){
                
                List<Object[]> returnKeywordList = new ArrayList();
                return returnKeywordList;
            }else if(tempKeywordList.get(0).getPackageName().equalsIgnoreCase("ANY")
                    &&tempKeywordList.get(0).getSubpackageName().equalsIgnoreCase("ANY")
                    &&!tempKeywordList.get(0).getPositionType().equalsIgnoreCase("ANY")){
                
                List<Object[]> returnKeywordList = new ArrayList();
                return returnKeywordList;
            }
            //Sort
            Stream <Keyword> keywordStream = tempKeywordList.stream()
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()));
            
            List<Object[]> returnKeywordList = new ArrayList();
            keywordStream.forEach(keyword-> returnKeywordList
                        .add(new Object[]{
                            (Object)keyword.getKeyword(),
                            (Object)ForcastUi.dateToString(keyword.getDate()),
                            (Object)Integer.toString(keyword.getPriority())
                        }));
            return returnKeywordList;
        }else{
            List<Object[]> returnKeywordList = new ArrayList();
            return returnKeywordList;
        }
    }
    public List<Object[]> chooseKeywordListANYxxx(String packageName, String subpackageName, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        //Filter Package Name and Subpackage Name
        Stream <Keyword> keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY"))
                .filter(e -> e.getDate().before(dateLimit));
        
        if(keywordStream.count()<1){
            keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY"))
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()));
            
            List<Object[]> keywordList = new ArrayList();
            keywordStream.forEach(keyword-> keywordList
                    .add(new Object[]{
                        (Object)keyword.getKeyword(),
                        (Object)ForcastUi.dateToString(keyword.getDate()),
                        (Object)Integer.toString(keyword.getPriority())
                    }));
            return keywordList;
        }else{
            keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY"))
                .filter(e -> e.getDate().before(dateLimit))
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()));
            List<Object[]> keywordList = new ArrayList();
            keywordStream.forEach(keyword-> keywordList
                    .add(new Object[]{
                        (Object)keyword.getKeyword(),
                        (Object)ForcastUi.dateToString(keyword.getDate()),
                        (Object)Integer.toString(keyword.getPriority())
                    }));
            
            //Update the after using it
            return keywordList;
        }
        
    }
    public List<Object[]> chooseKeywordListANY(String packageName, String subpackageName, String positionType, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        List<Keyword> tempKeywordList = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY"))
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase("ANY"))
                .collect(Collectors.toList());
        tempKeywordList = filterPositionTypeANDANY(tempKeywordList,positionType);
                //.filter(cat -> cat.getPositionType().equalsIgnoreCase("ANY"))
                
        
        if(tempKeywordList.size()>0){
            //Sort
            Stream <Keyword> keywordStream = tempKeywordList.stream()
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()));
            
            List<Object[]> returnKeywordList = new ArrayList();
            keywordStream.forEach(keyword-> returnKeywordList
                        .add(new Object[]{
                            (Object)keyword.getKeyword(),
                            (Object)ForcastUi.dateToString(keyword.getDate()),
                            (Object)Integer.toString(keyword.getPriority())
                        }));
            return returnKeywordList;
        }else{
            List<Object[]> returnKeywordList = new ArrayList();
            return returnKeywordList;
        }
    }
    
    public Keyword chooseKeywordxxx(String packageName, String subpackageName, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        //Filter Package Name and Subpackage Name
        //ForcastUi.consoleLog("      Choosing Keyword");
        Stream <Keyword> keywordStream = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName));
        
        
        //특정한게 있으면
        if(keywordStream.count()>0){
            //Filter recently used Keyword
            Optional<Keyword> keyword = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName))
                .filter(e -> e.getDate().before(dateLimit)) // filter Date Limit
                .filter(i -> !i.isUsed()) // It is used?
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()))
                .findFirst();
            
            //if does not exist before that day just choose from recent
            if(!keyword.isPresent())
                keyword = keywordList.stream()
                    .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                    .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName))
                    .filter(i -> !i.isUsed()) // It is used?
                    .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()))
                    .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                    .findFirst();
            
            //Still doesn find any
            if(!keyword.isPresent())
                keyword = keywordList.stream()
                    .filter(cat -> cat.getPackageName().equalsIgnoreCase(packageName))
                    .filter(cat -> cat.getSubpackageName().equalsIgnoreCase(subpackageName))
                    .findAny();
            
            //update used
            //updateDateForThatKeyword(keyword.get().getKeyword(),new Date());
            updateKeywordUsed(keyword.get().getKeyword(), true);
            
            //Store in Suggested KeywordList
            suggestedKeywordList.add(keyword.get().getKeyword());
            //Update the after using it
            return keyword.get();
        }else{// IF that package Name doens't Exist
            
            Optional<Keyword> keyword = keywordList.stream()
                .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY")) // ANY Package
                .filter(e -> e.getDate().before(dateLimit)) // filter Date Limit
                .filter(i -> !i.isUsed()) // It is used?
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()))
                .findFirst();
            
            //if does not exist before that day just choose from recent
            if(!keyword.isPresent())
                keyword = keywordList.stream()
                    .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY")) // ANY Package
                    .filter(i -> !i.isUsed()) // It is used?
                    .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                    .findFirst();
            
            //If nothing found just pick ANY 
            if(!keyword.isPresent())
                keyword = keywordList.stream()
                    .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY")) // ANY Package
                    .findAny();
            
            //update used
            //updateDateForThatKeyword(keyword.get().getKeyword(),new Date());
            updateKeywordUsed(keyword.get().getKeyword(), true);
            
            //Store in Suggested KeywordList
            suggestedKeywordList.add(keyword.get().getKeyword());
            //Update the after using it
            return keyword.get();
        }
        
    }
    public Keyword chooseKeyword(String packageName, String subpackageName, String positionType, Date dateLimit){//add(Calendar.DAY_OF_MONTH, 7);
        List<Keyword> tempKeywordList = filterPackageNameOrANY(keywordList,packageName);
        tempKeywordList = filterSubpackageNameOrANY(tempKeywordList,subpackageName);
        tempKeywordList = filterPositionTypeANDANY(tempKeywordList,positionType);
        
        if(tempKeywordList.size() > 0){
            if((tempKeywordList.get(0).getPackageName().equalsIgnoreCase("ANY")
                    &&tempKeywordList.get(0).getSubpackageName().equalsIgnoreCase("ANY")
                    &&tempKeywordList.get(0).getPositionType().equalsIgnoreCase("ANY"))
                    
                    ||(tempKeywordList.get(0).getPackageName().equalsIgnoreCase("ANY")
                    &&tempKeywordList.get(0).getSubpackageName().equalsIgnoreCase("ANY")
                    &&tempKeywordList.get(0).getPositionType().equalsIgnoreCase(positionType))){
                
                Optional<Keyword> keyword = tempKeywordList.stream()
                    .filter(e -> e.getDate().before(dateLimit)) // filter Date Limit
                    .filter(i -> !i.isUsed()) // It is used?
                    .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                    .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()))
                    .findFirst();
            
                //if does not exist before that day just choose from recent
                if(!keyword.isPresent())
                    keyword = tempKeywordList.stream()
                        .filter(i -> !i.isUsed()) // It is used?
                        .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                        .findFirst();

                //If nothing found just pick ANY 
                if(!keyword.isPresent())
                    keyword = tempKeywordList.stream()
                        .filter(cat -> cat.getPackageName().equalsIgnoreCase("ANY")) // ANY Package
                        .findAny();

                //update used
                //updateDateForThatKeyword(keyword.get().getKeyword(),new Date());
                updateKeywordUsed(keyword.get().getKeyword(), true);

                //Store in Suggested KeywordList
                suggestedKeywordList.add(keyword.get().getKeyword());
                //Update the after using it
                return keyword.get();
            }else{
                Optional<Keyword> keyword = tempKeywordList.stream()
                    .filter(e -> e.getDate().before(dateLimit)) // filter Date Limit
                    .filter(i -> !i.isUsed()) // It is used?
                    .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                    .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()))
                    .findFirst();
                
                tempKeywordList.stream()
                        .filter(i -> !i.isUsed()) // It is used?
                        .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()))
                        .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                        .forEach(e -> System.out.println("Keyword " + e.getKeyword()));
                
                //if does not exist before that day just choose from recent
                if(!keyword.isPresent())
                    keyword = tempKeywordList.stream()
                        .filter(i -> !i.isUsed()) // It is used?
                        .sorted((e1,e2) -> e1.getPriority().compareTo(e2.getPriority()))
                        .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                        .findFirst();
                
                //Still doesn find any
                if(!keyword.isPresent())
                    keyword = tempKeywordList.stream().findAny();
                
                //update used
                //updateDateForThatKeyword(keyword.get().getKeyword(),new Date());
                updateKeywordUsed(keyword.get().getKeyword(), true);

                //Store in Suggested KeywordList
                suggestedKeywordList.add(keyword.get().getKeyword());
                //Update the after using it
                return keyword.get();
            }
        }else{
            return new Keyword("NONE","NO Package","No Subpackage", "No PositionType", "Wrong Keyword", "01/01/2000", "9");
        }
        
        
    }
    private void updateKeywordUsed(String keyword,boolean used){
        keywordList.stream()
                .filter(key -> key.getKeyword().equalsIgnoreCase(keyword))
                .forEach(key-> key.setUsed(used));
    }
    private void updateDateForThatKeyword(String keyword,Date date){
        keywordList.stream()
                .filter(key -> key.getKeyword().equalsIgnoreCase(keyword))
                .forEach(key-> key.setDate(date));
    }
    public void refactorKeywordList(List<String> effected){
        List<String> toRestore = new ArrayList();
        // if There is in Effected but not on suggested
        suggestedKeywordList.stream().forEach((suggestedKeyword) -> {
            
            boolean doesNotExist = effected.stream().filter(key -> key.equalsIgnoreCase(suggestedKeyword)).count() == 0;
            if(doesNotExist)
                toRestore.add(suggestedKeyword);

        });
        
        //Restore Date to original
        toRestore.stream().forEach(keyword ->{
            keywordList.stream()
                    .filter(key -> key.getKeyword().equalsIgnoreCase(keyword))
                    .forEach(item -> item.restore());
        });
        
        //Set All effected keyword to used;
        effected.stream().forEach(keyword ->{
            keywordList.stream()
                    .filter(key -> key.getKeyword().equalsIgnoreCase(keyword))
                    .forEach(item -> item.setDate(new Date()));
        });
        //writeUsedKeywordToCSV(fileName);
//        keywordList.stream()
//                .filter(key->key.isUsed()).forEach(ddd -> System.out.println(ddd.getPackageName()+"- keyword: "+ ddd.getKeyword()+" - date: "+ddd.getDate()));
        
    }
    
    public void writeToExcel(String fileName){
        final int GROUP = 0;
        final int PACKAGENAME = 1;
        final int SUBPACKAGENAME = 2;
        final int POSITIONTYPE = 3;
        final int KEYWORD = 4;
        final int DATE = 5;
        final int PRIORITY = 6;
        
        
        try{    
            System.out.println("Writing XML: "+fileName);
            ForcastUi.consoleLog("Writing XML: "+fileName);
            
            FileInputStream fileIn =  new FileInputStream(fileName);
            
            Workbook wb = WorkbookFactory.create(fileIn);
            Sheet sheet = wb.getSheetAt(0);
            
            keywordList.stream().filter(key -> key.isUsed()).forEach(keyword->{
                //같은거 찾아서 교체하기 or Don't
                int rowInt=1;
                while(true){
                    Row row = sheet.getRow(rowInt);
                    
                    if(row == null)
                        break;
                    
                    Cell cellKeyword = row.getCell(KEYWORD);
                    String shortName = cellKeyword.getRichStringCellValue().getString();
                    if(shortName.equalsIgnoreCase(keyword.getKeyword())){//If Match, Update Date
                        Cell cellDate = row.getCell(DATE);
                        cellDate.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cellDate.setCellValue(new Date());
                        //cellDate.setCellValue(ForcastUi.dateToString(new Date()));
                        //cellDate.setCellType(Cell.CELL_TYPE_STRING);
                        //cellDate.setCellValue(ForcastUi.dateToString(new Date()));
                        //break;
                    }
                    rowInt++;
                }
            });
            
            
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();
            fileIn.close();
            
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
    }
    public void writeToCSV(String fileName){
        
        try{
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(fileName,false), "UTF-8"));
            String [] nextLine = new String[] {"Group","Category","Subpackage","Keyword","Recent","Priority"};
            writer.writeNext(nextLine);
            keywordList.stream().forEach(keyword ->{
                writer.writeNext(keyword.getRow());
            });
            
            writer.flush();
            writer.close();
            
        }catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,fileName);
            e.printStackTrace();
        } catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            e.printStackTrace();
        } catch (Exception e){
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            e.printStackTrace();
        }
//    private String group; //0
//    private String packageName; //1
//    private String subpackageName; //2
//    private String keyword; //3
//    private Date date; //4
//    private final Integer priority; //5

            
        
    }
    private static FileInputStream openExcelFileOrCreate(String fileName) throws Exception{

        File file = new File(fileName);
        if(file.isFile() && file.exists())
        {
           System.out.println(fileName+" file open successfully.");
           ForcastUi.consoleLog(fileName+" file open successfully.");
        }
        else
        {
           System.out.println("File doesnt exist : "+fileName);
           ForcastUi.consoleLog("File doesnt exist : "+fileName);
//           FileOutputStream out = new FileOutputStream(new File(fileName));
//           XSSFWorkbook workbook = new XSSFWorkbook();
//           workbook.write(out);
//           out.close();
//           file = new File(fileName);
//new InputStreamReader(new FileInputStream(fileName),"UTF-8")
        }

        return new FileInputStream(file);

    } 
    private List<Keyword> readFromExcel(String fileName){
        List<Keyword> keywordList = new ArrayList();
        
        try{
            ForcastUi.consoleLog("Opening filename: "+fileName);
            FileInputStream fIP =  openExcelFileOrCreate(fileName);
            //Get the workbook instance for XLSX file 
            XSSFWorkbook workbook = new XSSFWorkbook(fIP);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator < Row > rowIterator = spreadsheet.iterator();
            XSSFRow row;
            
            boolean firstLine= true;
            while (rowIterator.hasNext()){
                String group=""; //0
                String packageName=""; //1
                String subpackageName=""; //2
                String positionType=""; //3
                String keyword=""; //4
                String date="01/01/2000"; //5
                String priority="1"; //6 
                
                row = (XSSFRow) rowIterator.next();
                Iterator < Cell > cellIterator = row.cellIterator();
                
                //한줄 읽어오기
                Cell cell = cellIterator.next();
                group =  cell.getStringCellValue();
                
                if(firstLine){
                    firstLine = false;
                    continue;
                }
                
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    packageName =  getCellValue(cell);
                }
                
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    subpackageName =  getCellValue(cell);
                }
                
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    positionType =  getCellValue(cell);
                }
                
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    keyword =  getCellValue(cell);
                }
                
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    date =  getCellDate(cell);                 
                }
                
                if(cellIterator.hasNext()){
                    cell = cellIterator.next();
                    priority = getCellValue(cell);         
                }       
                //System.out.println("Keyword "+ keyword+"Date: "+date);
                keywordList.add(new Keyword(group, packageName, subpackageName, positionType, keyword, date, priority));//.add(new TopStockDescription(shortName.trim(),longName.trim(),explanation.trim(),false));
            }
            fIP.close();
            
        }catch (FileNotFoundException e) {
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        }catch (IOException e) {
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) { 
            ForcastUi.consoleLog(ex.getMessage());
            Logger.getLogger(TopStockDescriptionList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return keywordList;
    }
    public static final int CELL_TYPE_NUMERIC = 0;
    public static final int CELL_TYPE_STRING = 1;
    public static final int CELL_TYPE_FORMULA = 2;
    public static final int CELL_TYPE_BLANK = 3;
    public static final int CELL_TYPE_BOOLEAN = 4;
    public static final int CELL_TYPE_ERROR = 5;
    private String getCellDate(Cell cell){
        switch(cell.getCellType()){
            case CELL_TYPE_NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    return ForcastUi.dateToString(cell.getDateCellValue());
                }else{
                    return "01/01/2000";
                }
            case CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case CELL_TYPE_BLANK:
                return "";
            case CELL_TYPE_BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case CELL_TYPE_ERROR:
                return Byte.toString(cell.getErrorCellValue());
            default:
                return "01/01/2000";
        }
    }
    private String getCellValue(Cell cell){
        switch(cell.getCellType()){
            case CELL_TYPE_NUMERIC:
                return Double.toString(cell.getNumericCellValue());
            case CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case CELL_TYPE_BLANK:
                return "";
            case CELL_TYPE_BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case CELL_TYPE_ERROR:
                return Byte.toString(cell.getErrorCellValue());
            default:
                return "";
        }
    }
    private List<Keyword> readFromCSV(String fileName){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy =",";
        
        List<Keyword> keywordList = new ArrayList();
        try{
            if(!main.ForcastUi.fileExtCheck(fileName)){
                ForcastUi.consoleLog("File : "+fileName +" ext error");
                return keywordList;
            }     
            //System.out.println("Opening filename :"+fileName);
            ForcastUi.consoleLog("Opening filename: "+fileName);
            
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8")); 
            
            //Read the First Line
            line = br.readLine();
            String[] row = line.split(cvsSplitBy);
            
            String test = row[0];
            test = row[1];
            if(row[1].equalsIgnoreCase("Category")){
                System.out.println("Correct Start!");
            }else{
                System.out.println("incorrect start of file for Categories Slug List");
                throw new IOException();
                
            }
            
            while((line = br.readLine()) != null){
                row = line.split(cvsSplitBy);
                keywordList.add(new Keyword(row[0],row[1],row[2],row[3],row[4],row[5], row[6]));
                
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
                    ForcastUi.consoleLog(e.getMessage());
                    e.printStackTrace();
                }
                
            }
            
            return (keywordList);
        }
        
    }
}

