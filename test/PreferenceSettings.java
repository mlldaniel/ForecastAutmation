/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import main.BottomTextRotation;
import main.BottomTextRotationList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


class GeneralSettings{
    static List<String> languageList = new ArrayList();
    static private String defaultDirectory = "";
    public void addLanguage(String lang){
        this.languageList.add(lang);
    }
    private String getLanguage(int i){
        return languageList.get(i);
    }
    public List<String> getLanguageList(){
        return languageList;
    }
    public int countLanguages(){
        return languageList.size();
    }
    
    /**
     * @return the recentOpen
     */
    public String getDefaultDirectory() {
        return defaultDirectory;
    }
    public void setDefaultDirectory(String dir){
        defaultDirectory = dir;
    }
}
/**
 *
 * @author Daniel
 */
public class PreferenceSettings extends GeneralSettings {
    private String language = null;
    private String[] timeFrameDropDownList = null;
    private String[] positionTypeDropDownList = null;
    private String[] fundamentalSubpackage = null;
    private String[] goldNCommonditiesSubpackage = null;
    
    
    public PreferenceSettings(){
        this.language = "eng";
        this.timeFrameDropDownList = new String[] { "-","3 Days", "7 Days", "14 Days", "1 Month", "3 Months", "1 Year" };
        this.positionTypeDropDownList = new String[] { "-","Long", "Short", "Long & Short", "Commodities", "Currencies","XAU" };
        this.fundamentalSubpackage = new String[] {"","Low P/E Stocks","High P/E Stocks"
                ,"Low PEG Stocks","High PEG Stocks"
                ,"Low price-to-book ratio Stocks","High price-to-book ratio Stocks","Low price-to-sales ratio Stocks"
                ,"High price-to-sales ratio Stocks","Low High Short Ratio Stocks","High Short Ratio Stocks"};
        this.goldNCommonditiesSubpackage = new String[] {"","Gold","Commodities"};
        
        //this.defaultDirectory = "C:\\Users\\Daniel\\OneDrive\\I Know First\\forecast automation\\csvtables";
    }
    public PreferenceSettings(String lang,String[] timeFrameDropDownList, String[] positionTypeDropDownList, String[] fundamentalSubpackage, String[] goldNCommonditiesSubpackage){
        this.language = lang;
        this.timeFrameDropDownList = timeFrameDropDownList;
        this.positionTypeDropDownList = positionTypeDropDownList;
        this.fundamentalSubpackage = fundamentalSubpackage;
        this.goldNCommonditiesSubpackage = goldNCommonditiesSubpackage;
    }
    
    static public List<PreferenceSettings> readFromFileXml(String fileName){
        List<PreferenceSettings> prefList = new ArrayList();
        String defaultDirectory = "";
        
        try{
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            // <directory> </directory>
            Node directoryNode = doc.getElementsByTagName("directory").item(0);
            Element itemsElement = (Element) directoryNode;
            NodeList itemNodeList = itemsElement.getElementsByTagName("defaultDirectory");
            defaultDirectory = itemNodeList.item(0).getTextContent();
            
            //<language> </language>
            NodeList languageList = doc.getElementsByTagName("language");
            for(int j = 0; j <languageList.getLength(); j++){
                List<String> tempTimeFrame = new ArrayList();
                List<String> tempPositionType = new ArrayList();
                List<String> tempfundamentalSubpackage = new ArrayList();
                List<String> tempGoldNCommonditiesSubpackage = new ArrayList();
                String tempLanguage = "";
                
                Node languageItem = languageList.item(j);
                NamedNodeMap attrList = languageItem.getAttributes();
                Node codeAttr = attrList.item(0);
                //<code = "x">
                tempLanguage = codeAttr.getTextContent();
                
                
                Element languageElementList = (Element) languageItem;
                
                // Time FRAME
                Node timeFrameNode = languageElementList.getElementsByTagName("timeFrame").item(0);
                
                itemsElement = (Element) timeFrameNode;
                itemNodeList = itemsElement.getElementsByTagName("item");

                for(int i = 0; i < itemNodeList.getLength(); i++){
                    //System.out.print("<sentence>");
                    String senTemp = itemNodeList.item(i).getTextContent();
                    //System.out.println(senTemp+"</sentence>");
                    tempTimeFrame.add(senTemp);
                }
                
                //Position Type
                Node positionType = languageElementList.getElementsByTagName("positionType").item(0);
                itemsElement = (Element) positionType;
                itemNodeList = itemsElement.getElementsByTagName("item");

                for(int i = 0; i < itemNodeList.getLength(); i++){
                    //System.out.print("<sentence>");
                    String senTemp = itemNodeList.item(i).getTextContent();
                    //System.out.println(senTemp+"</sentence>");
                    tempPositionType.add(senTemp);
                }

                //Subpackages
                NodeList subpackageList = languageElementList.getElementsByTagName("subpackage");

                for(int i = 0; i < subpackageList.getLength(); i++){
                    Element subpackageNode = (Element) subpackageList.item(i);
                    Node packageName = subpackageNode.getElementsByTagName("packageName").item(0);
                    NodeList itemList = subpackageNode.getElementsByTagName("item");

                    if(packageName.getTextContent().equalsIgnoreCase("Fundamental")){
                        for(int k = 0; k<itemList.getLength(); k++){
                            String senTemp = itemList.item(k).getTextContent();
                            tempfundamentalSubpackage.add(senTemp);
                        }
                    }else if(packageName.getTextContent().equalsIgnoreCase("Gold & Commodity Forecast")){
                        for(int k = 0; k<itemList.getLength(); k++){
                            String senTemp = itemList.item(k).getTextContent();
                            tempGoldNCommonditiesSubpackage.add(senTemp);
                        }
                    }
                }
                
                
                prefList.add(new PreferenceSettings(tempLanguage, tempTimeFrame.toArray(new String[0]), tempPositionType.toArray(new String[0]),
                        tempfundamentalSubpackage.toArray(new String[0]), tempGoldNCommonditiesSubpackage.toArray(new String[0])));
                prefList.get(j).addLanguage(tempLanguage);
            }
            
            
            if(!prefList.isEmpty()){
                prefList.get(0).setDefaultDirectory(defaultDirectory);
            }
            
            
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return prefList;
    }
    
    public static BottomTextRotationList readFromFile(String fileName){
        
        List<BottomTextRotation>bottomTextRotationList = new ArrayList();
        
        //BottomTextRotation bottomTextRotation = null;
        String positionType = "";
        String packageType = "";
        String subpackage = "";
        String text = "";
        
        try{
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return new BottomTextRotationList(bottomTextRotationList);
    }

    /**
     * @return the timeFrameDropDownList
     */
    public String[] getTimeFrameDropDownList() {
        return timeFrameDropDownList;
    }

    /**
     * @return the positionTypeDropDownList
     */
    public String[] getPositionTypeDropDownList() {
        return positionTypeDropDownList;
    }

    /**
     * @return the fundamentalSubpackage
     */
    public String[] getFundamentalSubpackage() {
        return fundamentalSubpackage;
    }

    /**
     * @return the goldNCommonditiesSubpackage
     */
    public String[] getGoldNCommonditiesSubpackage() {
        return goldNCommonditiesSubpackage;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    
}
