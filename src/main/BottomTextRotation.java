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
public class BottomTextRotation {
    // Long & Short 
    // Any
    private String positionType;
    private String packageType;
    private String subpackage;
    private String text;
    
    public BottomTextRotation(String positionType, String packageType, String subpackage, String text){
        this.positionType = positionType;
        this.packageType = packageType;
        this.subpackage = subpackage;
        this.text = text;
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
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
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
    
    
}
