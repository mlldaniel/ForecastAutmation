package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */
public class RowData implements Comparable<RowData> {
    private String symbol;
    private Integer prediction;
    private Double returnz;
    private Integer accuracy;
    
    RowData(String symbol, Integer prediction, String returnz, Integer accuracy){
        this.symbol = symbol;
        this.prediction = prediction;
        this.returnz = Double.parseDouble(returnz);
        this.accuracy = accuracy;
    }
    RowData(String symbol, String returnz){
        this.symbol = symbol;
        this.prediction = null;
        this.returnz = Double.parseDouble(returnz);
        this.accuracy = null;
    }
    RowData(String symbol, Double returnz){
        this.symbol = symbol;
        this.prediction = null;
        this.returnz = returnz;
        this.accuracy = null;
    }
    
    public String getSymbol(){
        return symbol;
    }
    public Double getReturnz(){
        return returnz;
    }
    public String getReturnzString(){
        return String.format("%.2f",returnz);
    }
    public Double getReturnzInPos(){
        return Math.abs(returnz);
    }
    public Integer getPrediction(){
        return prediction;
    }
    public Integer getAccuracy(){
        return accuracy;
    }
    
    public RowData getThis(){
        return this;
    }
    
    public int compareTo(RowData item){
        //Always Descending Order !!
        return Double.compare(item.getReturnz(),this.getReturnz());
        /*
        if(returnz == temp.getReturnz())
            return 0;
        else if(returnz>temp.getReturnz())
            return -1;
        else
            return 1;
        */
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @param prediction the prediction to set
     */
    public void setPrediction(Integer prediction) {
        this.prediction = prediction;
    }

    /**
     * @param returnz the returnz to set
     */
    public void setReturnz(Double returnz) {
        this.returnz = returnz;
    }

    /**
     * @param accuracy the accuracy to set
     */
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }
    
}
