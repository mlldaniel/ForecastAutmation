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
import java.util.Random;
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
public class BottomTextRotationANYList {
    List<BottomTextRotationANY> sentencesList;
    public BottomTextRotationANYList(){
        this.sentencesList = new ArrayList();
    }
    public BottomTextRotationANYList(List<BottomTextRotationANY> sentencesList){
        this.sentencesList = sentencesList;
    }
    public static BottomTextRotationANYList readFromFile(String fileName){
        List<BottomTextRotationANY> sentencesListReturn = new ArrayList();
        
        try{
            ForcastUi.consoleLog("Opening filename: "+fileName);
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("sentences");
            int size = nList.getLength();
            for (int temp = 0; temp < size; temp++) {
                Element sentencesNode = (Element) nList.item(temp);
                NodeList sentenceNodeList = sentencesNode.getElementsByTagName("sentence");
                
                BottomTextRotationANY tempSentenceList = new BottomTextRotationANY();
                
                //System.out.println("<sentences>");
                for(int i = 0; i < sentenceNodeList.getLength(); i++){
                    String packageName1="";
                    String packageName2="";
                    String subpackage="";
                    String senTemp="";
                    
                    //System.out.print("<sentence>");
                    Node sentenceNode = sentenceNodeList.item(i);
                    senTemp =  sentenceNode.getTextContent();
                    packageName1 = ((Element)sentenceNode).getAttribute("packageName1");
                    packageName2 = ((Element)sentenceNode).getAttribute("packageName2");
                    subpackage = ((Element)sentenceNode).getAttribute("subpackage");
                    
                    packageName1 = packageName1.isEmpty() ? "ANY" : packageName1;
                    packageName2 = packageName2.isEmpty() ? "ANY" : packageName2;
                    subpackage = subpackage.isEmpty() ? "ANY" : subpackage;
                    
                    tempSentenceList.addSentence(senTemp,packageName1,packageName2,subpackage);
                    
//                    if(packageNameTemp.equalsIgnoreCase("")){
//                        senTemp =  sentenceNode.getTextContent();
//                        tempSentenceList.addSentence(senTemp);
//                    }else{
//                        senTemp =  sentenceNode.getTextContent();
//                        tempSentenceList.addSentence(senTemp,packageNameTemp);
//                    }
                    //System.out.println(senTemp+"</sentence>");
                    
                }
                //System.out.println("</sentences>");
                sentencesListReturn.add(tempSentenceList);
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
        return new BottomTextRotationANYList(sentencesListReturn);
    }
    public String getRandomCombination(String packageName1, String packageName2, String subpackage){
        StringBuffer buf = new StringBuffer();
        //
        System.nanoTime();
        long randSeed = System.nanoTime();
        for(int i = 0; i < sentencesList.size(); i++){
            //returnBotText +=" "+ sentencesList.get(i).getRandomSentence(randSeed, packageName2);
            buf.append(" ").append(sentencesList.get(i).getRandomSentence(randSeed, packageName1, packageName2, subpackage));
        }
        String returnBotText=buf.toString();
        return returnBotText;
    }
}

// FIrst / Second / .... Sentence
class BottomTextRotationANY{
    List<BotSentence> sentence;
    
//    List<String> sentenceN;
//    List<String> packageName1;
//    List<String> packageName2;
//    List<String> subpackage;
    public BottomTextRotationANY(){
        sentence = new ArrayList();
        
//        sentenceN = new ArrayList();
//        packageName1 = new ArrayList();
//        packageName2 = new ArrayList();
//        subpackage = new ArrayList();
    }
    public void addSentence(String sen){
        sentence.add(new BotSentence(sen,"ANY","ANY","ANY"));
//        sentenceN.add(sen);
//        packageName1.add("ANY");
//        packageName2.add("ANY");
//        subpackage.add("ANY");
    }
    public void addSentence(String sen, String packageName1, String packageName2, String subpackage){
        sentence.add(new BotSentence(sen,packageName1,packageName2,subpackage));
//        this.sentenceN.add(sen);
//        this.packageName1.add(packageName1);
//        this.packageName2.add(packageName2);
//        this.subpackage.add(subpackage);
    }
    public int size(){
        return sentence.size();
    }
    public String getSentence(int n){
        return sentence.get(n).getSentenceN();
    }
    public String getPackageName1(int n){
        return sentence.get(n).getPackageName1();
    }
    public String getPackageName2(int n){
        return sentence.get(n).getPackageName2();
    }
    public String getSubpackage(int n){
        return sentence.get(n).getSubpackage();
    }
//    public List<String> getSentetenceWherePackageType(String packageName2){
//        List<String> sentences = new ArrayList();
//        for(int i = 0; i<this.packageName2.size(); i++){
//            if(this.packageName2.get(i).equalsIgnoreCase(packageName2)){
//                sentences.add(sentenceN.get(i));
//            }
//        }
//        if(sentences.isEmpty()){
//            for(int i = 0; i<this.packageName2.size(); i++){
//                if(this.packageName2.get(i).equalsIgnoreCase("ANY")){
//                    sentences.add(sentenceN.get(i));
//                }
//            }
//        }
//        
//        return sentences;
//    }
    
//    public String getRandomSentence(long randSeed, String packageType){
//        Random rand;
//        rand = new Random();
//        rand.setSeed(randSeed);
//        
//        List<String> sent = getSentetenceWherePackageType(packageType);
//        
//        int size = sent.size();
//        return sent.get(rand.nextInt(size));
//    }
    public String getRandomSentence(long randSeed, String packageName1, String packageName2, String subpackage){
        Random rand;
        rand = new Random();
        rand.setSeed(randSeed);
        
        List<BotSentence> sentenceList= new ArrayList();
        //Copy to btr Graded List
        for(BotSentence btr : sentence){
            sentenceList.add(new BotSentence(btr));
        }
        sentenceList = getMatchPackageName1OrANY(sentenceList, packageName1);
        sentenceList = getMatchPackageName2OrANY(sentenceList, packageName2);
        sentenceList = getMatchSubpackageOrANY(sentenceList, subpackage);
        //List<String> sent = getSentetenceWherePackageType(packageType);
        sentenceList = pickMostMatches(sentenceList);
        int size = sentenceList.size();
        
        return sentenceList.get(rand.nextInt(size)).getSentenceN();
    }
    public List<BotSentence> getMatchPackageName1OrANY(List<BotSentence> inputList, String param){
        List<BotSentence> returnObj = new ArrayList();
        
        for(BotSentence sen : inputList){
            if(sen.getPackageName1().equalsIgnoreCase(param)){
                BotSentence addSen = new BotSentence(sen);
                addSen.incrementPoints();
                returnObj.add(addSen);
            }else if(sen.getPackageName1().equalsIgnoreCase("ANY")){
                BotSentence addSen = new BotSentence(sen);
                returnObj.add(addSen);
            }
        }
        
        return returnObj;
    }
    public List<BotSentence> getMatchPackageName2OrANY(List<BotSentence> inputList, String param){
        List<BotSentence> returnObj = new ArrayList();
        
        for(BotSentence sen : inputList){
            if(sen.getPackageName2().equalsIgnoreCase(param)){
                BotSentence addSen = new BotSentence(sen);
                addSen.incrementPoints();
                returnObj.add(addSen);
            }else if(sen.getPackageName2().equalsIgnoreCase("ANY")){
                BotSentence addSen = new BotSentence(sen);
                returnObj.add(addSen);
            }
        }
        
        return returnObj;
    }
    public List<BotSentence> getMatchSubpackageOrANY(List<BotSentence> inputList, String param){
        List<BotSentence> returnObj = new ArrayList();
        
        for(BotSentence sen : inputList){
            if(sen.getSubpackage().equalsIgnoreCase(param)){
                BotSentence addSen = new BotSentence(sen);
                addSen.incrementPoints();
                returnObj.add(addSen);
            }else if(sen.getSubpackage().equalsIgnoreCase("ANY")){
                BotSentence addSen = new BotSentence(sen);
                returnObj.add(addSen);
            }
        }
        
        return returnObj;
    }
    public List<BotSentence> pickMostMatches(List<BotSentence> inputList){
        List<BotSentence> btrList = new ArrayList();
        int highest = 0;
        for(BotSentence btr : inputList){
            if(highest < btr.getPoints())
                highest = btr.getPoints();
        }
        
        for(int i = 0 ; i < inputList.size() ; i++){
            if(highest == inputList.get(i).getPoints())
                btrList.add(inputList.get(i));
        }
        
        return btrList;
    }
}


class BotSentence{
    private String sentenceN;
    private String packageName1;
    private String packageName2;
    private String subpackage;
    
    private int points;
    public BotSentence(String sentenceN, String packageName1, String packageName2, String subpackage){
        this.sentenceN = sentenceN;
        this.packageName1 = packageName1;
        this.packageName2 = packageName2;
        this.subpackage = subpackage;
        this.points = 0;
    }
    
    public BotSentence(BotSentence sent){
        this.sentenceN = sent.getSentenceN();
        this.packageName1 = sent.getPackageName1();
        this.packageName2 = sent.getPackageName2();
        this.subpackage = sent.getSubpackage();
        this.points = sent.getPoints();
    }

    /**
     * @return the sentenceN
     */
    public String getSentenceN() {
        return sentenceN;
    }

    /**
     * @return the packageName1
     */
    public String getPackageName1() {
        return packageName1;
    }

    /**
     * @return the packageName2
     */
    public String getPackageName2() {
        return packageName2;
    }

    /**
     * @return the subpackage
     */
    public String getSubpackage() {
        return subpackage;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }
    public void incrementPoints(){
        points++;
    }
    
}