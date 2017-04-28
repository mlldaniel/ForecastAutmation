/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import DB.ErrorMessages;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author Daniel
 */
public class BottomTextRotationList {
    private List<BottomTextRotation> bottomTextRotationList;
    
    public BottomTextRotationList(){
        bottomTextRotationList = null;
    }
    public BottomTextRotationList(List<BottomTextRotation> bottomTextRotationList){
        this.bottomTextRotationList = bottomTextRotationList;
    }
    
    public List<BottomTextRotationGraded> getMatchPositionTypeOrANY(List<BottomTextRotationGraded> inList, String positionType){
        inList = inList.stream()
                .filter(e -> e.getPositionType().equalsIgnoreCase(positionType) || e.getPositionType().equalsIgnoreCase("ANY"))
                .collect(Collectors.toList());
        
        for(BottomTextRotationGraded btrg: inList){
            if(btrg.getPositionType().equalsIgnoreCase(positionType))
                btrg.increasePointsBy(1);
        }
        
        
        //inList= inList.stream().forEach(item -> item);
//        for(int i = 0 ; i<inList.size() ; ){
//            if(inList.get(i).getPositionType().equalsIgnoreCase(positionType)){
//                inList.get(i).increasePointsBy(1);
//            }
//            if((!inList.get(i).getPositionType().equalsIgnoreCase(positionType)
//                    && !inList.get(i).getPositionType().equalsIgnoreCase("ANY"))){
//                inList.remove(i);
//                i = 0;
//                continue;
//            }
//            i++;
//            
//        }
        
        return inList;
    }
    public List<BottomTextRotationGraded> getMatchPackageTypeOrANY(List<BottomTextRotationGraded> inList, String packageType){
        
        for(int i = 0 ; i<inList.size() ; ){
            if(inList.get(i).getPackageType().equalsIgnoreCase(packageType)){
                inList.get(i).increasePointsBy(1);
            }else if(!inList.get(i).getPackageType().equalsIgnoreCase("ANY")){
                inList.remove(i);
                i = 0;
                continue;
            }
            i++;
        }

        return inList;
    }
    public List<BottomTextRotationGraded> getMatchSubpackageOrANY(List<BottomTextRotationGraded> inList, String subpackage){
        
        for(int i = 0 ; i<inList.size() ;){
            if(inList.get(i).getSubpackage().equalsIgnoreCase(subpackage)){
                inList.get(i).increasePointsBy(1);
                i++;
            }else if(inList.get(i).getSubpackage().equalsIgnoreCase("ANY")){
                i++;
            }else {
                inList.remove(i);
                i = 0;
            }
            
        }
        
        return inList;
    }
    
    public BottomTextRotationList getListWhere(String positionType, String packageType, String subpackage){
        
        List<BottomTextRotationGraded> btrGradedList = new ArrayList();
        
        //Copy to btr Graded List
        for(BottomTextRotation btr : bottomTextRotationList){
            btrGradedList.add(new BottomTextRotationGraded(btr,0));
        }
        
        //POSITION TYPE - START
        btrGradedList = getMatchPositionTypeOrANY(btrGradedList, positionType);
        //POSITION TYPE - END
        
        //Package Type - START
        btrGradedList = getMatchPackageTypeOrANY(btrGradedList, packageType);
        //Package Type - END
        
        //Subpackage- START
        btrGradedList = getMatchSubpackageOrANY(btrGradedList, subpackage);
        //Subpackage - END 
        
        List<BottomTextRotation> btrList = null;
        //Get the Highest Score Only
        btrList = pickMostMatches(btrGradedList);
        
        return new BottomTextRotationList(btrList);
    }
    
    public List<BottomTextRotation> pickMostMatches(List<BottomTextRotationGraded> btrGradedList){
        List<BottomTextRotation> btrList = new ArrayList();
        int highest = 0;
        for(BottomTextRotationGraded btr : btrGradedList){
            if(highest < btr.getPoints())
                highest = btr.getPoints();
        }
        
        for(int i = 0 ; i < btrGradedList.size() ; i++){
            if(highest == btrGradedList.get(i).getPoints())
                btrList.add(btrGradedList.get(i));
        }
        
        return btrList;
    }
    
    public BottomTextRotation pickOne(String textANY){
        
        Random rand;
        int size = bottomTextRotationList.size();
        
        switch(size){
            case 0:
                System.out.println("There is no Match ! I Don't Think this is possible");
                return null;
            case 1:
                BottomTextRotation btr = bottomTextRotationList.get(0);
                /*
                if(btr.getPackageType().equalsIgnoreCase("ANY") 
                        && btr.getPositionType().equalsIgnoreCase("ANY") 
                        && btr.getSubpackage().equalsIgnoreCase("ANY")){
                    
                    String modifiedANY = btr.getText().replace("###ANYTEXT###", textANY);
                    btr.setText(modifiedANY);
                }*/
                if(btr.getText().contains("###ANYTEXT###")){
                    String modifiedANY = btr.getText().replace("###ANYTEXT###", textANY);
                    btr.setText(modifiedANY);
                }
                return btr;
            default:
                rand = new Random();
                rand.setSeed(System.nanoTime());
                return bottomTextRotationList.get(rand.nextInt(size));
        }

    }
    
    public static BottomTextRotationList readFromFile(String fileName){
        
        List<BottomTextRotation>bottomTextRotationList = new ArrayList();
        
        //BottomTextRotation bottomTextRotation = null;
        String positionType = "";
        String packageType = "";
        String subpackage = "";
        String text = "";
        
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
                    
                    text = eElement.getElementsByTagName("text")
                                    .item(0)
                                    .getTextContent();
//                    System.out.println("Position TYpe: " + positionType);
//                    System.out.println("packageType: " + packageType);
//                    System.out.println("subpackage: " + subpackage);
//                    System.out.println("text: " + text);
//                    
                    
                    bottomTextRotationList.add(new BottomTextRotation(positionType,packageType,subpackage,text));
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
        
        return new BottomTextRotationList(bottomTextRotationList);
    }

    /**
     * @return the bottomTextRotationList
     */
    public List<BottomTextRotation> getBottomTextRotationList() {
        return bottomTextRotationList;
    }

    /**
     * @param bottomTextRotationList the bottomTextRotationList to set
     */
    public void setBottomTextRotationList(List<BottomTextRotation> bottomTextRotationList) {
        this.bottomTextRotationList = bottomTextRotationList;
    }
    
}
