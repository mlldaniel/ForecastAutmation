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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.*;
import org.jsoup.nodes.Document;
/**
 *
 * @author Daniel
 */
public class PackageHeaderList {
    private List<PackageHeader> packageHeaderList=null;
    
    private static final int PACKAGENAME = 0;
    private static final int PACKAGEEXPLAIN = 1;
    private static final int ULLIST = 2;
    private static final int OTHER = 3;
    private static final int WAITING = 4;
    
    PackageHeaderList(){
        this.packageHeaderList = null;
    }
//    private int checkPackages(String line, List<String> packageNameList){
//        for(int i = 0; i < packageNameList.size(); i++){
//            if(line.equalsIgnoreCase(packageNameList.get(i))){
//                return i;
//            }
//        }
//        return -1;
//    }    
    private boolean isPackageName(String line, List<String> packageNameList){
        
        for(String item : packageNameList){
            if(line.equalsIgnoreCase(item)){
                return true;
            }
        }
        return false;
    }
    public PackageHeader getPackageHeaderWhereNameIs(String name){
        for(PackageHeader item : getPackageHeaderList()){
            if(name.equalsIgnoreCase(item.getPackageName())){
                return item;
            }
        }
        PackageHeader pkg = new PackageHeader();
        return pkg;
    }
    public PackageHeader getPackageHeaderWhereNameIs(String packageName, String subpackageName){
        if(!subpackageName.isEmpty())
            for(PackageHeader item : packageHeaderList){
                if(packageName.equalsIgnoreCase(item.getPackageName())
                        && subpackageName.equalsIgnoreCase((item.getSubpackage()))){
                    return item;
                }
            }
        else
            for(PackageHeader item : packageHeaderList){
                if(packageName.equalsIgnoreCase(item.getPackageName())){
                    return item;
                }
            }
        
        PackageHeader pkg = new PackageHeader();
        return pkg;
    }
    
    public void readFromFile(String fileName, List<String> packageNames){
        BufferedReader br = null;
        String line = "";
        
        packageHeaderList = new ArrayList();
        int currentStage = PACKAGENAME;
        Document doc;
        
        //boolean [] checkList = new boolean[packageNames.size()];
        try{     
            ForcastUi.consoleLog("Opening filename: "+fileName);
            System.out.println("Opening filename :"+fileName);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),StandardCharsets.UTF_8));
            
            PackageHeader packageHeader = null;
            String temp = "";
            
            while((line = br.readLine()) != null){
//<editor-fold defaultstate="collapsed" desc="comment">

/*
String newline = System.getProperty("line.separator");
boolean hasNewline = line.contains(newline);
if(hasNewline){ // if it is just new line \n
continue;
}
*/

//</editor-fold>
                if(line.contains("###START###") || line.equalsIgnoreCase(""))
                    continue;
                //Read the First Line
                
                if(isPackageName(html2text(line),packageNames)){ 
                    if(packageHeader != null){// 이전에 packageHeader 를 저장
                        getPackageHeaderList().add(packageHeader);
                    }
//                    int check = checkPackages(html2text(line),packageNames);
//                    if(check!=-1)
//                        checkList[check] = true;
                    
                    //.clear everything Out
                    temp = "";
                    packageHeader = new PackageHeader();
                    packageHeader.setPackageName(html2text(line));
                    
                    currentStage = PACKAGEEXPLAIN;
                    //System.out.println(html2text(line));
                    
                }else{
                    
                    switch(currentStage){
                        case PACKAGENAME:
                            break;
                        case PACKAGEEXPLAIN://1
                            doc = Jsoup.parse(line);
                            
                            if(line.contains("<ul")){//2
                                packageHeader.setPackageExplain(temp);
                                currentStage = ULLIST;
                                break;
                            }else if(doc.select("img").size()>0){// If no List included       3
                                packageHeader.setPackageExplain(temp);
                                packageHeader.setPackageNameImage(doc.select("img").first().toString());
                                currentStage = OTHER;
                            }else if(doc.text().contains("Package Name:")){  //  4
                                String packageName = doc.text().substring(doc.text().lastIndexOf(": ")+2);
                                packageHeader.setPackageName2(packageName);
                                packageHeader.setPackageExplain(temp);
                                currentStage = OTHER;
                            }else if(doc.text().contains("Recommended Positions:")){ //5
                                packageHeader.setRecommendedPositions(true);
                                packageHeader.setPackageExplain(temp);
                                currentStage = OTHER;
                            }else{ //1
                                
                                temp+= line;
                            }                         
                                
                            break;
                        case ULLIST:
                            
                            int liSize = Jsoup.parse(line).select("li").size();
                            if(line.equalsIgnoreCase("</ul>")){
                                currentStage = OTHER;
                                break;
                            }else if(liSize > 0){
                                packageHeader.addUlList(html2text(line));
                                //System.out.println(html2text(line));
                            }else{
                                System.out.println("Something is wrong because after starting ul is not ended");
                                return;
                            }     
                            break;
                        case OTHER:
                            //System.out.println(html2text(line));
                            doc = Jsoup.parse(line);
                            if(doc.select("img").size()>0){ //3
                                packageHeader.setPackageNameImage(doc.select("img").first().toString());
                            }else if(doc.text().contains("Package Name:")){  //  4
                                String packageName = doc.text().substring(doc.text().lastIndexOf(":")+1);
                                packageHeader.setPackageName2(packageName.trim());
                            }else if(doc.text().contains("Recommended Positions:")){ //5
                                packageHeader.setRecommendedPositions(true);
                            }else if(doc.text().contains("Subpackage")){
                                String subpackage = doc.text().substring(doc.text().lastIndexOf(":")+1);
                                packageHeader.setSubpackage(subpackage.trim());
                                currentStage = WAITING;
                            }
                            break;
                        case WAITING:
                            //Don't Do anything
                            break;
//<editor-fold defaultstate="collapsed" desc="comment">
                            /*
                            case OTHER:
                            doc = Jsoup.parse(line);
                            if(doc.select("strong").size()>0){
                            if(doc.select("strong").first().toString().contains("Recommended Positions"))
                            packageHeader.setRecommendedPositions(true);
                            }
                            break;*/
//</editor-fold>                case WAITING:
                        default:
                            System.out.println(line);
                            System.out.println("What a default? Current Stage :" + currentStage);
                                    
                    }
                    //System.out.println("Something is Wrong The first line but not a Package Name");
                }
                
                
            }
            
//            for(int i = 0 ; i<53 ; i++)
//                if(!checkList[i])
//                    System.out.println("This "+packageNames.get(i)+" # "+i+" Exist :"+checkList[i]);
            
            getPackageHeaderList().add(packageHeader);
            System.out.println("Done Reading Package Header List");


            
            
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
    }
    
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    /**
     * @return the packageHeaderList
     */
    public List<PackageHeader> getPackageHeaderList() {
        return packageHeaderList;
    }
}
