/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Daniel
 */
public class PostForm {
    private String positionType;
    private String packageType;
    private String subpackage;
    private String title;
    private String mainText;
    private String excerptText;

    public PostForm(String positionType, String packageType, String subpackage, String title, String mainText, String excerptText) {
        this.positionType = positionType;
        this.packageType = packageType;
        this.subpackage = subpackage;
        this.title = title;
        this.mainText = mainText;
        this.excerptText = excerptText;
    }
    public static String deleteRowWhere(String inputText, String tagName){
        inputText = inputText.replaceAll("(<[$]"+tagName+">)[^&]*(</[$]"+tagName+">)", "");
        return inputText;
    }
    public static String removeAllDollarTag(String inputText){
        inputText = inputText.replaceAll("<[$][^>]+>", "");
        inputText = inputText.replaceAll("<[/][$][^>]+>", "");
        return inputText;
//        mainText = mainText.replaceAll("<[$][a-zA-Z]>", "");
//        mainText = mainText.replaceAll("</[$][a-zA-Z]>", "");
    }
    public void deletePackageExplain(){
        mainText = mainText.replaceAll("(<[$]packageExplain>)[^&]*(</[$]packageExplain>)", "");
        //mainText.replace(title, mainText)
    }
    public void deleteUlList(){
        mainText = mainText.replaceAll("(<[$]ulList>)[^&]*(</[$]ulList>)", "");
        //mainText.replace(title, mainText)
    }
    public void deletePackageNameImage(){
        //mainText = mainText.replaceAll("(?i)(<[$]packageNameImage.*?>)(.+?)()", "");
        mainText = mainText.replaceAll("(<[$]packageNameImage>)[^&]*(</[$]packageNameImage>)", "");
        //mainText.replace(title, mainText)
    }
    public void deletePackageName2(){
        mainText = mainText.replaceAll("(<[$]packageName2>)[^&]*(</[$]packageName2>)", "");
    }
    public void deleteRecommendedPosition(){
        mainText = mainText.replaceAll("(<[$]recommendedPosition>)[^&]*(</[$]recommendedPosition>)", "");
    }
//    public void deleteForecastLength(){
//        mainText = mainText.replaceAll("(<[$]forecastLength>)[^&]*(</[$]forecastLength>)", "");
//    }
    public void deleteIKnowFirstAverage(){
        mainText = mainText.replaceAll("(<[$]iknowfirstAverage>)[^&]*(</[$]iknowfirstAverage>)", "");
    }
    public void deleteMainImage(){
        mainText = mainText.replaceAll("(<[$]mainImage>)[^&]*(</[$]mainImage>)", "");
    }
    public void deleteBottomText(){
        mainText = mainText.replaceAll("(<[$]bottomText>)[^&]*(</[$]bottomText>)", "");
    }
    public void removeAllDollarTag(){
        mainText = mainText.replaceAll("<[$][^>]+>", "");
        mainText = mainText.replaceAll("<[/][$][^>]+>", "");
//        mainText = mainText.replaceAll("<[$][a-zA-Z]>", "");
//        mainText = mainText.replaceAll("</[$][a-zA-Z]>", "");
    }
    /**
     * @return the positionType
     */
    public String getPositionType() {
        return positionType;
    }

    /**
     * @param positionType the positionType to set
     */
    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    /**
     * @return the packageType
     */
    public String getPackageType() {
        return packageType;
    }

    /**
     * @param packageType the packageType to set
     */
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    /**
     * @return the subpackage
     */
    public String getSubpackage() {
        return subpackage;
    }

    /**
     * @param subpackage the subpackage to set
     */
    public void setSubpackage(String subpackage) {
        this.subpackage = subpackage;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the mainText
     */
    public String getMainText() {
        return mainText;
    }

    /**
     * @param mainText the mainText to set
     */
    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    /**
     * @return the excerptText
     */
    public String getExcerptText() {
        return excerptText;
    }

    /**
     * @param excerptText the excerptText to set
     */
    public void setExcerptText(String excerptText) {
        this.excerptText = excerptText;
    }
    
    
}
