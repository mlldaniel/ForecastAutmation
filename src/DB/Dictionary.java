/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import main.ForcastUi;
import static main.ForcastUi.fileExtCheck;

/**
 *
 * @author Daniel
 */
public class Dictionary {
    List<Meaning> meaningList;
    public Dictionary(String fileName){
        this.meaningList = readFromFile(fileName);
    }
    private List<Meaning> readFromFile(String fileName){    
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy =",";
        
        List<Meaning> tempMeaningList = new ArrayList();
        
        try{
            if(!fileExtCheck(fileName)){
                //throw new IOException("BAD EXTENSION FILE UPLOAD");
                return null;
            }           
            
            ForcastUi.consoleLog("Opening filename: "+fileName);
            //br = new BufferedReader(new FileReader(fileName));
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
            
            while((line = br.readLine()) != null){
                String[] row = line.split(cvsSplitBy);
                
                if(row[0].equalsIgnoreCase("key")){
                    continue;
                }
                Meaning tempMeaning = new Meaning();
                tempMeaning.setKey(row[0]);//.replaceAll("\"", "").trim();
                tempMeaning.setValue(row[1]);
                
                tempMeaningList.add(tempMeaning);
            }
            
            
        }catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();ForcastUi.consoleLog(e.getMessage());
        } catch (Exception e){
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,fileName);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();ForcastUi.consoleLog(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();ForcastUi.consoleLog(e.getMessage());
                }
                
            }
            
        }
        
        return (tempMeaningList);
    }
    public String findValue(String key){
        for(Meaning row: meaningList){
            if(row.getKey().equalsIgnoreCase(key))
                return row.getValue();
        }
        return key;
    }
    public String findKey(String value){
        for(Meaning row: meaningList){
            if(row.getValue().equalsIgnoreCase(value))
                return row.getKey();
        }
        return value;
    }
}

class Meaning{
    private String key;
    private String value;
    public Meaning(){
        key ="";
        value = "";
    }
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}