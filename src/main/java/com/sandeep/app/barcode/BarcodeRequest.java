package com.sandeep.app.barcode;

public class BarcodeRequest {

    private int noOfBarcodes = 10000;
    private int requestedBarCodes = 10000;
    private int barcodeLength = 27;

    private int minTemp = 40;
    private int maxTemp = 80;

    private int similarity = 70;
    private int complementarity = 70;

    private int minGC = 40;
    private int maxGC = 80;
    
    private String parsecKey;
    private String parsecValue;
    
    private String userName = "System";
    
    private long totalProcessingTime=0;
    private long totalParsecTime=0;

    public int getNoOfBarcodes() {
        return noOfBarcodes;
    }

    public void setNoOfBarcodes(int noOfBarcodes) {
        this.noOfBarcodes = noOfBarcodes;
    }

    public int getBarcodeLength() {
        return barcodeLength;
    }

    public void setBarcodeLength(int barcodeLength) {
        this.barcodeLength = barcodeLength;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    public int getComplementarity() {
        return complementarity;
    }

    public void setComplementarity(int complementarity) {
        this.complementarity = complementarity;
    }

    public int getMinGC() {
        return minGC;
    }

    public void setMinGC(int minGC) {
        this.minGC = minGC;
    }

    public int getMaxGC() {
        return maxGC;
    }

    public void setMaxGC(int maxGC) {
        this.maxGC = maxGC;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTotalProcessingTime() {
        return totalProcessingTime;
    }

    public void setTotalProcessingTime(long totalProcessingTime) {
        this.totalProcessingTime = totalProcessingTime;
    }

    public long getTotalParsecTime() {
        return totalParsecTime;
    }

    public void setTotalParsecTime(long totalParsecTime) {
        this.totalParsecTime = totalParsecTime;
    }

    public String getParsecKey() {
        return parsecKey;
    }

    public void setParsecKey(String parsecKey) {
        this.parsecKey = parsecKey;
    }

    public String getParsecValue() {
        return parsecValue;
    }

    public void setParsecValue(String parsecValue) {
        this.parsecValue = parsecValue;
    }

    public int getRequestedBarCodes() {
        return requestedBarCodes;
    }

    public void setRequestedBarCodes(int requestedBarCodes) {
        this.requestedBarCodes = requestedBarCodes;
    }
    
    
    
}
