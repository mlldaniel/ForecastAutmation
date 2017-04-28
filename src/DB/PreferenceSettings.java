package DB;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import main.ForcastUi;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rss.RSSHeader;

/**
 *
 * @author Daniel
 */
public class PreferenceSettings {
    private static String[] timeFrameDropDownList = null;
    private static String[] positionTypeDropDownList = null;
    private static String defaultDirectory = null;
    private static int keyword1DateLimit = 7;
    private static int keyword2DateLimit = 7;

    /**
     * @return the keyword1DateLimit
     */
    public static int getKeyword1DateLimit() {
        return keyword1DateLimit;
    }

    /**
     * @param aKeyword1DateLimit the keyword1DateLimit to set
     */
    public static void setKeyword1DateLimit(int aKeyword1DateLimit) {
        keyword1DateLimit = aKeyword1DateLimit;
    }

    /**
     * @return the keyword2DateLimit
     */
    public static int getKeyword2DateLimit() {
        return keyword2DateLimit;
    }

    /**
     * @param aKeyword2DateLimit the keyword2DateLimit to set
     */
    public static void setKeyword2DateLimit(int aKeyword2DateLimit) {
        keyword2DateLimit = aKeyword2DateLimit;
    }
    private String language = "";
    private String linkFormat = "";
    private DateFormat dateFormat=null;
    private String mainImageWidth = "959";
    private String mainImageHeight = "819";
    private RSSHeader rssHeader = new RSSHeader();
    
    private String imageExt;
    
    
    
    public PreferenceSettings(){
//        timeFrameDropDownList = new String[] { "-","3 Days", "7 Days", "14 Days", "1 Month", "3 Months", "1 Year" };
//        positionTypeDropDownList = new String[] { "-","Long", "Short", "Long & Short", "Commodities", "Currencies","XAU" };
//        defaultDirectory = "C:\\Users\\Daniel\\OneDrive\\I Know First\\forecast automation\\csvtables";
    }
    public PreferenceSettings(RSSHeader rssheader, String language, String linkFormat, String dateFormat, String mainImageWidth, String mainImageHeight, String imageExtention){
//        timeFrameDropDownList = new String[] { "-","3 Days", "7 Days", "14 Days", "1 Month", "3 Months", "1 Year" };
//        positionTypeDropDownList = new String[] { "-","Long", "Short", "Long & Short", "Commodities", "Currencies","XAU" };
//        defaultDirectory = "C:\\Users\\Daniel\\OneDrive\\I Know First\\forecast automation\\csvtables";
        this.rssHeader = rssheader;
        this.language = language;
        this.linkFormat = linkFormat;
        this.dateFormat = new SimpleDateFormat(dateFormat);
        this.mainImageWidth = mainImageWidth;
        this.mainImageHeight = mainImageHeight;
        this.imageExt = imageExtention;
        
    }
    static public List<PreferenceSettings> readFromFileXml(String fileName){
        List<PreferenceSettings> returnObject = new ArrayList();
        List<String> tempTimeFrame = new ArrayList();
        List<String> tempPositionType = new ArrayList();
        String defaultDir = "";
        String linkFormat="";
        String dateFormat = "";
        
        
        try{
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            Node directoryNode = doc.getElementsByTagName("directory").item(0);
            Element itemsElement = (Element) directoryNode;
            NodeList itemNodeList = itemsElement.getElementsByTagName("defaultDirectory");
            defaultDir = itemNodeList.item(0).getTextContent();
            //System.out.println(senTemp+"</sentence>");
            
            //NodeList nList = doc.getElementsByTagName("timeFrame");
            // Time FRAME
            Node timeFrameNode = doc.getElementsByTagName("timeFrame").item(0);
            itemsElement = (Element) timeFrameNode;
            itemNodeList = itemsElement.getElementsByTagName("item");

            for(int i = 0; i < itemNodeList.getLength(); i++){
                //System.out.print("<sentence>");
                String senTemp = itemNodeList.item(i).getTextContent();
                //System.out.println(senTemp+"</sentence>");
                tempTimeFrame.add(senTemp);
            }
            
            //Position Type
            Node positionType = doc.getElementsByTagName("positionType").item(0);
            itemsElement = (Element) positionType;
            itemNodeList = itemsElement.getElementsByTagName("item");
            
            for(int i = 0; i < itemNodeList.getLength(); i++){
                //System.out.print("<sentence>");
                String senTemp = itemNodeList.item(i).getTextContent();
                //System.out.println(senTemp+"</sentence>");
                tempPositionType.add(senTemp);
            }
            
            //Keyword 1/2 Date Limit
            Node keyword1DateLimitNode = doc.getElementsByTagName("keyword1DateLimit").item(0);
            Node keyword2DateLimitNode = doc.getElementsByTagName("keyword2DateLimit").item(0);
            keyword1DateLimit = Integer.parseInt(keyword1DateLimitNode.getTextContent());
            keyword2DateLimit = Integer.parseInt(keyword2DateLimitNode.getTextContent());
            
            timeFrameDropDownList = tempTimeFrame.toArray(new String[0]);
            positionTypeDropDownList = tempPositionType.toArray(new String[0]);
            defaultDirectory = defaultDir;
            
            //////////////////////////////
            //   Languages Settings    //
            /////////////////////////////
            //language
            
            NodeList languageList = doc.getElementsByTagName("Language");
            
            //NodeList languageList = doc.getElementsByTagName("rssheader");
            
            for(int i = 0; i < languageList.getLength(); i++){
                String mainImageFileType ="jpeg";
                String mainImageWidth = "0";
                String mainImageHeight = "0";
                RSSHeader rssHeader = new RSSHeader();
                String language = "";
                String text="";
                
                Node languageItem = languageList.item(i);
                Element languageElements = (Element) languageItem;
                language = languageElements.getAttribute("name");
                
                Node linkFormatNode = languageElements.getElementsByTagName("linkFormat").item(0);
                linkFormat = linkFormatNode.getTextContent();
                
                Node dateFormatNode = languageElements.getElementsByTagName("dateFormat").item(0);
                dateFormat = dateFormatNode.getTextContent();
                
                //Image
                Node mainImageFileTypeNode = languageElements.getElementsByTagName("fileType").item(0);
                mainImageFileType = mainImageFileTypeNode.getTextContent();
                
                Node mainImageWidthNode = languageElements.getElementsByTagName("width").item(0);
                mainImageWidth = mainImageWidthNode.getTextContent();
                
                Node mainImageHeightNode = languageElements.getElementsByTagName("height").item(0);
                mainImageHeight = mainImageHeightNode.getTextContent();
                
                Node rssHeaderNode = languageElements.getElementsByTagName("rssheader").item(0);
                Element rssHeaderItems = (Element) rssHeaderNode;
                
                //Rss Header
                //title
                text = rssHeaderItems.getElementsByTagName("title").item(0).getTextContent();
                rssHeader.setTitle(text);
                
                //link
                text = rssHeaderItems.getElementsByTagName("link").item(0).getTextContent();
                rssHeader.setLink(text);
                
                //description
                text = rssHeaderItems.getElementsByTagName("description").item(0).getTextContent();
                rssHeader.setDescription(text);
                
                //language
                text = rssHeaderItems.getElementsByTagName("language").item(0).getTextContent();
                rssHeader.setLanguage(text);
                
                //wp:wxr_version
                text = rssHeaderItems.getElementsByTagName("wp:wxr_version").item(0).getTextContent();
                rssHeader.setWpWxrVersion(text);
                
                //wp:base_site_url
                text = rssHeaderItems.getElementsByTagName("wp:base_site_url").item(0).getTextContent();
                rssHeader.setWpBaseSiteUrl(text);
                
                //wp:base_blog_url
                text = rssHeaderItems.getElementsByTagName("wp:base_blog_url").item(0).getTextContent();
                rssHeader.setWpBaseBlogUrl(text);
                
                
                
                returnObject.add(new PreferenceSettings(rssHeader,language,linkFormat,dateFormat, mainImageWidth, mainImageHeight,mainImageFileType));
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
        
        return returnObject;
    }
    
    public String getLanguage(){
        return language;
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
     * @return the defaultDirectory
     */
    public String getDefaultDirectory() {
        return defaultDirectory;
    }

    /**
     * @return the rssHeader
     */
    public RSSHeader getRssHeader() {
        return rssHeader;
    }
    
    
    public String getLinkFormat() {
        return linkFormat;
    }
    
    public String getLinkFormat(String domain) {
        return (domain+linkFormat);
    }

    /**
     * @return the dateFormat
     */
    public DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * @return the mainImageWidth
     */
    public String getMainImageWidth() {
        return mainImageWidth;
    }

    /**
     * @param mainImageWidth the mainImageWidth to set
     */
    public void setMainImageWidth(String mainImageWidth) {
        this.mainImageWidth = mainImageWidth;
    }

    /**
     * @return the mainImageHeight
     */
    public String getMainImageHeight() {
        return mainImageHeight;
    }

    /**
     * @param mainImageHeight the mainImageHeight to set
     */
    public void setMainImageHeight(String mainImageHeight) {
        this.mainImageHeight = mainImageHeight;
    }

    public String getImageExt() {
        return imageExt;
    }


    
}

