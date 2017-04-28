/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Daniel
 */
public class SubpackageInventoryList{
    List<SubpackageInventory> SubpackageInventoryList;
    public SubpackageInventoryList(List<PackageHeader> pkgHeaderList){
        this.SubpackageInventoryList = new ArrayList();
        for(PackageHeader pkgHeader : pkgHeaderList){
            if(!pkgHeader.getSubpackage().isEmpty())
                addSubpackage(pkgHeader.getPackageName(),pkgHeader.getSubpackage());
        }
    }
    public List<String> getSubpackageList(String packageName){
        for(SubpackageInventory si: SubpackageInventoryList){
            if(si.getPackageName().equalsIgnoreCase(packageName)){
                return si.getSubpackageNameList();
            }
        }
        //없으면 
        return new ArrayList();
    }
    public void addSubpackage(String packageName, String subpackageName){
        for(SubpackageInventory si: SubpackageInventoryList){
            if(si.getPackageName().equalsIgnoreCase(packageName)){
                si.addSupackage(subpackageName);
                return;
            }
        }
        // 못찾으면
        SubpackageInventoryList.add(new SubpackageInventory(packageName,subpackageName));
    }
}

class SubpackageInventory {
    private String packageName;
    private List<String> subpackageNameList;
    
    public SubpackageInventory(String packageName){
        this.packageName = packageName;
        this.subpackageNameList = new ArrayList();
    }
    public SubpackageInventory(String packageName, String subpackageName){
        this.packageName = packageName;
        this.subpackageNameList = new ArrayList();
        this.subpackageNameList.add(subpackageName);
    }
    public void addSupackage(String subpackageName){
        this.getSubpackageNameList().add(subpackageName);
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
     * @return the subpackageNameList
     */
    public List<String> getSubpackageNameList() {
        return subpackageNameList;
    }

    /**
     * @param subpackageNameList the subpackageNameList to set
     */
    public void setSubpackageNameList(List<String> subpackageNameList) {
        this.subpackageNameList = subpackageNameList;
    }
}
