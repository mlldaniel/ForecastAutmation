/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DB.ErrorMessages;
import java.io.File;

/**
 *
 * @author Daniel
 */
public class ForecastAutomation {
    public static javax.swing.JEditorPane consolePane;
    static String workingDirectory  = System.getProperty("user.dir");
    public static void main(String args[]){
        Console console = new Console();
        console.main();
        
        checkLibraryExist();
        
        ForcastUi mainUi = new ForcastUi(console.getJEditorPane1());
        mainUi.main();
        
        
        
    }
    public static void checkLibraryExist(){
        String libList[] = {
        "AbsoluteLayout.jar",
        "dom4j-1.6.jar",
        "jsoup-1.9.2.jar", 
        "poi-3.9.jar", 
        "poi-ooxml-3.9.jar",
        "poi-ooxml-schemas-3.9.jar",
        "sqlite-jdbc-3.14.2.jar",
        "swingx-all-1.6.4.jar", 
        "xmlbeans-2.3.0.jar"};
        //consoleLog("Checking Libraries");
        for(String filename : libList){
            File file = new File("lib"+File.separator+filename);
            if(file.exists()) { 
                //consoleLog("Library: " + file.getAbsolutePath()+" Exist");
                System.out.println("GOOD"+ File.separator+"lib"+File.separator+filename);
            }else{
                //consoleLog("Library: " + file.getAbsolutePath()+" Does not Exist");
                System.out.println("Bad: "+ File.separator+"lib"+File.separator+filename);
                ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,"lib"+File.separator+filename);
            }
        }
    }

}
