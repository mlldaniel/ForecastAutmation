/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import main.ForcastUi;
/**
 *
 * @author Daniel
 */
public class KeywordHistoryTracker {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    
    //CSV file header
    private static final String FILE_HEADER = "Date, Keywords";
    
//    private static int getLastId(String fileName){
//        File varTmpDir = new File(fileName);
//        boolean exists = varTmpDir.exists();
//        if(exists){
//            return 2;
//        }else
//            return 1;
//    }
    public static void writeCsvFile(String fileName, List <String> keywordList, String today) {
        
        //id = getLastId(fileName);
//        FileWriter fileWriter = null;
            Writer fileWriter=null;
        try {
            ForcastUi.consoleLog("Writing filename: "+fileName);
            
            File file = new File(fileName);
            
            boolean fileHeader = false;
            if(!file.exists())
                fileHeader = true;
            
            //Writer fileWriter = new FileWriter(fileName, true);
            fileWriter = new OutputStreamWriter(new FileOutputStream(fileName,true), "UTF-8");
            
            //Write the CSV file header
            if(fileHeader)
                fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append(today);
            
            
            //Write a new student object list to the CSV file
            for (String keyword : keywordList) {
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(keyword);
                
                //id++;
            }
            System.out.println("CSV file was created successfully !!!");
            ForcastUi.consoleLog("Done Writing filename: "+fileName);
            
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

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }
}
