/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DB.ErrorMessages;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class CategorieSlugList {
    private List<CategorieSlug> list;
    public CategorieSlugList(){
        list = null;
    }
    public CategorieSlugList(List<CategorieSlug> list){
        this.list = list;
    }
    /**
     * @return the list
     */
    public List<CategorieSlug> getList() {
        return list;
    }
    
    public String getMatchSlug(String name){
        for(CategorieSlug item : list){
            if(item.getName().equalsIgnoreCase(name))
                return item.getSlug();
        } 
       ErrorMessages.printErrorMsg(2, name);
        return "check_the_slug_name";
    }
    
    public boolean checkIfExist(String packageName){
        if (list.stream().anyMatch((cat) -> (cat.getName().equalsIgnoreCase(packageName)))) {
            return true;
        }
        return false;// No Categorie Matches
    }
    public List<String> getNameAll(){
        List <String> nameList = new ArrayList();
        for(CategorieSlug item : list){
            nameList.add(item.getName());
        }
        return nameList;
    }
    public List<String> getSlugAll(){
        List <String> nameList = new ArrayList();
        for(CategorieSlug item : list){
            nameList.add(item.getSlug());
        }
        return nameList;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<CategorieSlug> list) {
        this.list = list;
    }
    
    public static CategorieSlugList readFromCVS(String fileName){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy =",";
        
        List<CategorieSlug> list = new ArrayList();
        try{
            if(!fileExtCheck(fileName)){
                //throw new IOException("BAD EXTENSION FILE UPLOAD");
                return null;
            }     
            System.out.println("Opening filename :"+fileName);
            ForcastUi.consoleLog("Opening filename: "+fileName);
            
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8")); 
            
            //Read the First Line
            line = br.readLine();
            String[] row = line.split(cvsSplitBy);
            
            if(row[0].equalsIgnoreCase("Name") && row[1].equalsIgnoreCase("Slug")){
                System.out.println("Correct Start!");
            }else{
                System.out.println("incorrect start of file for Categories Slug List");
                throw new IOException();
                
            }
            
            while((line = br.readLine()) != null){
                row = line.split(cvsSplitBy);
                list.add(new CategorieSlug(row[0],row[1]));
                
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
            
        }
        return new CategorieSlugList(list);
    }
    
    public static boolean fileExtCheck(String fileName){
        int lastIndexOfDot = fileName.lastIndexOf(".");
        String ext = fileName.substring(lastIndexOfDot + 1, fileName.length());
        
        if(ext.equalsIgnoreCase("csv"))
            return true;
        else
            return false;
    }
}
