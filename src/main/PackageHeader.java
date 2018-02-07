/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import DB.ErrorMessages;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class PackageHeader implements Cloneable {

    private String packageName;
    private String packageExplain;
    private List<String> ulList;
    private String packageNameImage;

    private boolean recommendedPositions;
    private String packageName2;
    private String subpackage;
    //String 

    PackageHeader() {
        packageName = "";
        packageExplain = "";
        ulList = new ArrayList();
        packageNameImage = "";
        recommendedPositions = false;
        packageName2 = "";
        subpackage = "";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the packageExplain
     */
    public String getPackageExplain() {
        return packageExplain;
    }

    /**
     * @param packageExplain the packageExplain to set
     */
    public void setPackageExplain(String packageExplain) {
        this.packageExplain = packageExplain;
    }

    /**
     * @return the ulList
     */
    public List<String> getUlList() {
        return ulList;
    }

    /**
     * @param ulList the ulList to set
     */
    public void setUlList(List<String> ulList) {
        this.ulList = ulList;
    }

    public void addUlList(String li) {
        if (this.ulList == null) {
            this.ulList = new ArrayList();
        }
        this.ulList.add(li);
    }

    /**
     * @return the packageNameImage
     */
    public String getPackageNameImage() {
        return packageNameImage;
    }

    /**
     * @param packageNameImage the packageNameImage to set
     */
    public void setPackageNameImage(String packageNameImage) {
        this.packageNameImage = packageNameImage;
    }

    /**
     * @return the recommendedPositions
     */
    public boolean isRecommendedPositions() {
        return recommendedPositions;
    }

    /**
     * @param recommendedPositions the recommendedPositions to set
     */
    public void setRecommendedPositions(boolean recommendedPositions) {
        this.recommendedPositions = recommendedPositions;
    }

    /**
     * @return the packageName2
     */
    public String getPackageName2() {
        return packageName2;
    }

    /**
     * @param packageName2 the packageName2 to set
     */
    public void setPackageName2(String packageName2) {
        this.packageName2 = packageName2;
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
