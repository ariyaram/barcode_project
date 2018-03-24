package com.sandeep.app.barcode.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sandeep.app.barcode.BarCodeValidator;
import com.sandeep.app.barcode.BarcodeRequest;


public class FileUtil {

    private static String separator = ",";
    
    private static final Map<String,String> parsec_species ;
    
    static {
        parsec_species= new HashMap<>();
        parsec_species.put("Danio_rerio","danRer7_7955_Danio_rerio_masked");
        parsec_species.put("Rattus_norvegicus","rn4_10116_Rattus_norvegicus_masked");
        parsec_species.put("Homo_sapiens","hg19_9606_Homo_sapiens_masked");
        parsec_species.put("Gallus_gallus","galGal3_9031_Gallus_gallus_masked");
        parsec_species.put("Mus_musculus","mm9_10090_Mus_musculus_masked");
        parsec_species.put("Drosophila_melanogaster","dm3_7227_Drosophila_melanogaster_masked");
        parsec_species.put("Saccharomyces_cerevisiae","sacCer3_4932_Saccharomyces_cerevisiae");
        parsec_species.put("Caenorhabditis_elegans","ce10_6239_Caenorhabditis_elegans_masked");
    }
    public static BarcodeRequest convertFile(String csvFile) throws Exception {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        BarcodeRequest request = null;
        try {

            br = new BufferedReader(new FileReader(csvFile));
            int count = 0;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                if (count == 1) {
                    String[] inputDataArr = line.split(cvsSplitBy);
                    request = convertFileIntoObject(inputDataArr);

                }
                if (count >= 2) {
                    throw new Exception("File format error");
                }
                count++;
            }

        } catch (FileNotFoundException e) {
            throw new Exception("File Not Found.");
        } catch (IOException e) {
            throw new Exception("File Reading error");
        } catch (Exception e) {
            throw new Exception("Input format error");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (request == null) {
            throw new Exception("Invalid file input");
        }
        return request;

    }

    private static BarcodeRequest convertFileIntoObject(String[] inputDataArr) throws Exception {
        BarcodeRequest request = new BarcodeRequest();

        if (inputDataArr == null || inputDataArr.length != 10) {
            throw new Exception("Invalid file input");
        }
        if (isEmpty(inputDataArr[0]) || isEmpty(inputDataArr[1]) || isEmpty(inputDataArr[8]) || parsec_species.get(inputDataArr[8]) == null) {
            throw new Exception("Missing required input.");
        }

        request.setNoOfBarcodes(Integer.parseInt(inputDataArr[1]));
        request.setBarcodeLength(Integer.parseInt(inputDataArr[0]));
        request.setParsecValue(parsec_species.get(inputDataArr[8]));
        request.setParsecKey(inputDataArr[8]);

        if (!isEmpty(inputDataArr[2])) {
            request.setMinGC(Integer.parseInt(inputDataArr[2]));
        }

        if (!isEmpty(inputDataArr[3])) {
            request.setMaxGC(Integer.parseInt(inputDataArr[3]));
        }

        if (!isEmpty(inputDataArr[4])) {
            request.setMinTemp(Integer.parseInt(inputDataArr[4]));
        }

        if (!isEmpty(inputDataArr[5])) {
            request.setMaxTemp(Integer.parseInt(inputDataArr[5]));
        }

        if (!isEmpty(inputDataArr[6])) {
            request.setSimilarity(Integer.parseInt(inputDataArr[6]));
        }

        if (!isEmpty(inputDataArr[7])) {
            request.setComplementarity(Integer.parseInt(inputDataArr[7]));
        }

        if (!isEmpty(inputDataArr[9])) {
            request.setUserName(inputDataArr[9]);
        }

        return request;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static void writeIntoFile(String outputDir, Set<String> resultSet, BarcodeRequest request,
            String fileNameStartWith) throws IOException {

        File opDir = new File(outputDir);
        if (!opDir.exists()) {
            opDir.mkdir();
        }
        DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
        String fileName = request.getUserName() + "_" + fileNameStartWith + "_" + request.getBarcodeLength()
                + "-Length_" + df.format(Calendar.getInstance().getTime()) + ".csv";
        writeIntoOutputFile(resultSet, opDir, fileName,request);
    }

    private static void writeIntoOutputFile(Set<String> returnSet, File outputDir, String fileName,BarcodeRequest request) throws IOException {

        String barcode;
        if (returnSet != null && !returnSet.isEmpty()) {

            
            File file = new File(outputDir, fileName);
            try (BufferedWriter br = new BufferedWriter(new FileWriter(file));) {
                
                writeInputIntoOutPut(request,br);
                Iterator<String> barCodeIte = returnSet.iterator();
                br.write("FileOutput\n");
                br.write("BarCode,Rev Compl,GC Count,Melting Temp,Melting Temp2\n");
                while (barCodeIte.hasNext()) {
                    barcode = barCodeIte.next();
                    br.write(barcode +separator
                            +generateCompleemntarityCode(barcode)+separator
                            +BarCodeValidator.getGCCount(barcode)+separator
                            +BarCodeValidator.calculateMeltingTemperature(barcode)+separator
                            +roundingDouble(BarCodeValidator.calculateMeltingTemperature2(barcode,request.getBarcodeLength()),2)
                            + "\n");
                }
            }
        }

    }
    
    private static void  writeInputIntoOutPut(BarcodeRequest request,BufferedWriter br) throws IOException {
        
        br.write("FileInput\n");
        br.write("Barcode_length, No_of_barcodes,Min GC %,Max GC %,Min TM %,Max TM%, Similarity,Complementarity,parsec species type,userName\n");
       
        br.write(request.getBarcodeLength()+separator);
        br.write(request.getNoOfBarcodes()+separator);
        br.write(request.getMinGC()+separator);
        br.write(request.getMaxGC()+separator);
        br.write(request.getMinTemp()+separator);
        br.write(request.getMaxTemp()+separator);
        br.write(request.getSimilarity()+separator);
        br.write(request.getComplementarity()+separator);
        br.write(request.getParsecKey()+separator);
        br.write(request.getUserName()+"\n");
        br.write("\n");
        br.write("\n");
        br.write("\n");
        br.write("Total Proc Time,"+request.getTotalProcessingTime()+"ms\n");
        br.write("Total Parsec Proc Time,"+request.getTotalParsecTime()+"ms\n");
        
        br.write("\n");
        br.write("\n");
        br.write("\n");
    }
    
    public static String roundingDouble(Double str, int precession) {
        String returnVal = "0";
        BigDecimal bd = new BigDecimal(str);
        bd = bd.setScale(precession, BigDecimal.ROUND_HALF_DOWN);
        returnVal = bd.doubleValue() + "";
        return returnVal;
    }
    
    private static String generateCompleemntarityCode(String code1) {
        code1 = code1.replaceAll("A", "W");
        code1 = code1.replaceAll("T", "X");
        code1 = code1.replaceAll("G", "Y");
        code1 = code1.replaceAll("C", "Z");

        code1 = code1.replaceAll("W", "T");
        code1 = code1.replaceAll("X", "A");
        code1 = code1.replaceAll("Y", "C");
        code1 = code1.replaceAll("Z", "G");
        return code1;
}

}
