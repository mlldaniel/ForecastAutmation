/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Keyword{
    private String group; //0
    private String packageName; //1
    private String subpackageName; //2
    private String positionType; //3
    private String keyword; //4
    private Date date; //5
    private final Integer priority; //6
    
    private final Date backupDate;
    private boolean used;

    public Keyword(String group, String packageName, String subpackageName, String positionType, String keyword, String date, String priority) {
        this.group = group;
        this.packageName = packageName;
        this.subpackageName = subpackageName;
        this.positionType = positionType;
        this.keyword = keyword;
        this.date = ForcastUi.stringToDate(date);
        this.backupDate = ForcastUi.stringToDate(date);
        Double d = Double.parseDouble(priority);
        this.priority = d.intValue();
        
        this.used = false;
    }
    public String[] getRow(){
        return new String[]{group, packageName, subpackageName, getPositionType(), keyword, ForcastUi.dateToString(date), Integer.toString(priority)};
    }
    public void restore(){
        setUsed(false);
        date = backupDate;
    }
    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @return the subpackageName
     */
    public String getSubpackageName() {
        return subpackageName;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.getDate();
        setUsed(true);
        this.date = date;
    }

    /**
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @return the used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * @return the backupDate
     */
    public Date getBackupDate() {
        return backupDate;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * @return the positionType
     */
    public String getPositionType() {
        return positionType;
    }
}
