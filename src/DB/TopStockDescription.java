package DB;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */
public class TopStockDescription {
    private String shortName;
    private String longName;
    private String contentText;
    
    private boolean changed;
    
    TopStockDescription(String shortName, String longName,String contentText, boolean changed){
        this.shortName = shortName;
        this.longName = longName;
        this.contentText = contentText;
        this.changed = changed;
    }
    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        setChanged(true);
        this.shortName = shortName;
    }

    /**
     * @return the longName
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @param longName the longName to set
     */
    public void setLongName(String longName) {
        setChanged(true);
        this.longName = longName;
    }

    /**
     * @return the contentText
     */
    public String getContentText() {
        return contentText;
    }

    /**
     * @param contentText the contentText to set
     */
    public void setContentText(String contentText) {
        setChanged(true);
        this.contentText = contentText;
    }
    
    public void print(){
        
        System.out.println("Short Name : "+shortName+ " Long Name : "+longName + " Content Text : " + contentText);
    }

    /**
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
