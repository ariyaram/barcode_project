package com.sandeep.app.barcode;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.sandeep.app.barcode.utils.FileUtil;
import com.sandeep.app.barcode.utils.ParsecUtl;
import com.sandeep.app.barcode.utils.RandomUtil;

public class BarCodeApplication {

    private int noOfCodesGenerated = 100;
    private int barcodeLength = 27;

    private int minTemp = 40;
    private int maxTemp = 80;

    private int similarity = 70;
    private int complementarity = 70;
    
    private int minGC=40;
    private int maxGC=80;
    
    private String defaultOutputDir = System.getProperty("user.home")+ System.getProperty("file.separator") + "Generated_Barcodes";
    
    private Set<String> finalSet = new HashSet<String>();
    private Set<String> parsecRejectedSet = new HashSet<String>();

    public static void main(String[] args) throws Exception {
        
        BarCodeApplication project = new BarCodeApplication();
        long start = System.currentTimeMillis();
        BarcodeRequest request = null;
        if(args != null && args.length >0) {
            request = FileUtil.convertFile(args[0]);
           
        }else {
            request = new BarcodeRequest();
            request.setNoOfBarcodes(project.noOfCodesGenerated);
            request.setRequestedBarCodes(project.noOfCodesGenerated);
            request.setBarcodeLength(project.barcodeLength);
            request.setMinGC(project.minGC);
            request.setMaxGC(project.maxGC);
            request.setMinTemp(project.minTemp);
            request.setMaxTemp(project.maxTemp);
            request.setSimilarity(project.similarity);
            request.setComplementarity(project.complementarity);
            request.setUserName("System");
            request.setParsecKey("Homo_sapiens");
            request.setParsecValue("hg19_9606_Homo_sapiens_masked");
        }
        
       
        project.startProcess(request);
        
       
        if(project.parsecRejectedSet != null && !project.parsecRejectedSet.isEmpty()) {
            FileUtil.writeIntoFile(project.defaultOutputDir, project.parsecRejectedSet, request,"ParsecRejected");
        }
        
        if(project.finalSet != null && !project.finalSet.isEmpty()) {
            
            if(project.finalSet.size() > request.getRequestedBarCodes()) {
                project.finalSet = ImmutableSet.copyOf(Iterables.limit(project.finalSet, request.getRequestedBarCodes()));
            }
            long endTime =((System.currentTimeMillis()-start));
            request.setTotalProcessingTime(endTime);
            
            FileUtil.writeIntoFile(project.defaultOutputDir, project.finalSet, request,"FinalSet");
        }
       
    }

    private void startProcess(BarcodeRequest request) throws JSONException, IOException {
        int generatedSize = 0;
        
        do {
            process(request);
            generatedSize = finalSet.size();
            if(generatedSize >= request.getRequestedBarCodes()) {
                break;
            }
            request.setNoOfBarcodes(request.getRequestedBarCodes()-generatedSize);
        }while(generatedSize >= request.getRequestedBarCodes());
       
    }
    private void process(BarcodeRequest request) throws JSONException, IOException {

      
        int noOfCodes =( (Double) (request.getNoOfBarcodes()*1.25)).intValue();
        int bagCount = 0;

        String generated_barcode;
        Set<String> returnSet = new HashSet<String>();
        BarCodeValidator validator = new BarCodeValidator(request);
        Set<String> completeSet = new HashSet<>();
        completeSet.addAll(finalSet);
        while (bagCount < noOfCodes) {
            generated_barcode = RandomUtil.random(barcodeLength);
            
            if (validator.validate(generated_barcode,completeSet)) {
                System.out.println(generated_barcode+" "+bagCount);
                bagCount++;
                returnSet.add(generated_barcode);
                completeSet.add(generated_barcode);
            }

        }
        long start = System.currentTimeMillis();
        finalSet.addAll(ParsecUtl.prepareAndSend(request.getParsecValue(), returnSet));
        long endTime =((System.currentTimeMillis()-start));
        request.setTotalParsecTime(request.getTotalParsecTime()+endTime);
        returnSet.removeAll(finalSet);
        parsecRejectedSet.addAll(returnSet);
    }
}
