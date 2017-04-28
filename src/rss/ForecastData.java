/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rss;
import main.Item;

/**
 *
 * @author danielhwang
 */
public class ForecastData{
    RSSEntry rssEntry;
    Item item;
    
    public ForecastData(Item item, RSSEntry rssEntry){
        this.item = item;
        this.rssEntry = rssEntry;
    }
    public ForecastData(Item item){
        this.item = item;
        this.rssEntry = new RSSEntry();
    }
    public RSSEntry getRssEntry() {
        return rssEntry;
    }

    public void setRssEntry(RSSEntry rssEntry) {
        this.rssEntry = rssEntry;
    }

    public Item getItem() {
        return item;
    }

    
    
    

}