/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DB.ErrorMessages;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Daniel
 */
public class PostFormList{
    List<PostForm> postFormList;
    
    public PostFormList(String fileName){
        
        this.postFormList = readFromFile(fileName);
    }
    private PostFormList(List<PostForm> pfList){
        this.postFormList = pfList;
    }
    public PostFormList getForm(String positionType, String packageType, String subpackage){
        List<PostFormGraded> postFormGradedList = new ArrayList();
        
        //Copy
        for(PostForm pf: postFormList){
            postFormGradedList.add(new PostFormGraded(pf));
        }
        
        //POSITION TYPE - START
        postFormGradedList = getMatchPostionType(postFormGradedList, positionType);
        //POSITION TYPE - END
        
        //Package Type - START
        postFormGradedList = getMatchPackageType(postFormGradedList, packageType);
        //Package Type - END
        
        //Subpackage- START
        postFormGradedList = getMatchSubpackage(postFormGradedList, subpackage);
        //Subpackage - END 
        
        List<PostForm> pfgList = null;
        //Get the Highest Score Only
        pfgList = pickMostMatches(postFormGradedList);
        
        return new PostFormList(pfgList);
    }
    private List<PostFormGraded> getMatchPostionType(List<PostFormGraded> pfgList, String positionType){
        for(int i = 0; i< pfgList.size(); i++){
            
            if(pfgList.get(i).getPositionType().equalsIgnoreCase(positionType)){
                pfgList.get(i).increasePointsBy(1);
            }
            //ANy 도 아니고 해당하는것 도 안니면 remove
            if(!(pfgList.get(i).getPositionType().equalsIgnoreCase(positionType)
                    || pfgList.get(i).getPositionType().equalsIgnoreCase("ANY"))){
                pfgList.remove(i);
                i = 0;
            }
        }
        return pfgList;
    }
    private List<PostFormGraded> getMatchPackageType(List<PostFormGraded> pfgList, String packageType){
        for(int i = 0; i< pfgList.size(); i++){
            
            if(pfgList.get(i).getPackageType().equalsIgnoreCase(packageType)){
                pfgList.get(i).increasePointsBy(1);
            }
            //ANy 도 아니고 해당하는것 도 안니면 remove
            if(!(pfgList.get(i).getPackageType().equalsIgnoreCase(packageType)
                    || pfgList.get(i).getPackageType().equalsIgnoreCase("ANY"))){
                pfgList.remove(i);
                i = 0;
            }
        }
        return pfgList;
    }
    private List<PostFormGraded> getMatchSubpackage(List<PostFormGraded> pfgList, String subpackage){
        for(int i = 0; i< pfgList.size(); i++){
            
            if(pfgList.get(i).getSubpackage().equalsIgnoreCase(subpackage)){
                pfgList.get(i).increasePointsBy(1);
            }
            //ANy 도 아니고 해당하는것 도 안니면 remove
            if(!(pfgList.get(i).getSubpackage().equalsIgnoreCase(subpackage)
                    || pfgList.get(i).getSubpackage().equalsIgnoreCase("ANY"))){
                pfgList.remove(i);
                i = 0;
            }
        }
        return pfgList;
    }
    public List<PostForm> pickMostMatches(List<PostFormGraded> pfgGradedList){
        List<PostForm> pfList = new ArrayList();
        int highest = 0;
        for(PostFormGraded pfg : pfgGradedList){
            if(highest < pfg.getPoints())
                highest = pfg.getPoints();
        }
        
        for(int i = 0 ; i < pfgGradedList.size() ; i++){
            if(highest == pfgGradedList.get(i).getPoints())
                pfList.add(pfgGradedList.get(i));
        }
        
        return pfList;
    }
    public PostForm pickOne(){
        if(postFormList.isEmpty()){
            System.out.println("Form not choosen!");
            return null;
        }
        return postFormList.get(0);
    }
    private List<PostForm> readFromFile(String fileName){
        List<PostForm> tempPostForm = new ArrayList();
        
        String positionType;
        String packageType;
        String subpackage;
        String title;
        String mainText;
        String excerptText;
        
        try{
            ForcastUi.consoleLog("Opening filename: "+fileName);
            
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("item");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    
                    positionType = eElement.getElementsByTagName("positionType")
                                    .item(0)
                                    .getTextContent();
                    
                    packageType = eElement.getElementsByTagName("packageType")
                                    .item(0)
                                    .getTextContent();
                    
                    subpackage = eElement.getElementsByTagName("subpackage")
                                    .item(0)
                                    .getTextContent();                    
                    
                    title =  eElement.getElementsByTagName("title")
                                    .item(0)
                                    .getTextContent(); 
                    
                    mainText = eElement.getElementsByTagName("mainText")
                                    .item(0)
                                    .getTextContent();
                    
                    excerptText = eElement.getElementsByTagName("excerptText")
                                    .item(0)
                                    .getTextContent();
                    
                    tempPostForm.add(new PostForm(positionType,packageType,subpackage,title,mainText,excerptText));
                }
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
        }
        
        return tempPostForm;
    }
    
}

class PostFormGraded extends PostForm{
    int points;
    public int getPoints(){
        return points;
    }
    public PostFormGraded(PostForm pf){
        super(pf.getPositionType(), pf.getPackageType(), pf.getSubpackage(), pf.getTitle(), pf.getMainText(), pf.getExcerptText());
        points = 0;
    }
    public void increasePointsBy(int i){
        points = points +1;
    }
}
