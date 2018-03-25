package com.sandeep.app.barcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BarCodeValidator {
    
    private int BARCODE_LENGTH ;

    private int MIN_MELTING_TEMPERATURE;
    private int MAX_MELTING_TEMPERATURE;

    private int SIMILARITY;
    private int COMPLEMENTARITY;
    
    private int MIN_GC_PERCENTAGE;
    private int MAX_GC_PERCENTAGE;
    
    public BarCodeValidator(BarcodeRequest request) {
        this.BARCODE_LENGTH = request.getBarcodeLength();
        this.MIN_MELTING_TEMPERATURE = request.getMinTemp();
        this.MAX_MELTING_TEMPERATURE = request.getMaxTemp();
        this.SIMILARITY = request.getSimilarity();
        this.COMPLEMENTARITY = request.getComplementarity();
        this.MIN_GC_PERCENTAGE = request.getMinGC();
        this.MAX_GC_PERCENTAGE = request.getMaxGC();
    }

    public boolean validate(String generated_barcode,Set<String> returnSet ) {

        boolean flag = false;
        boolean isGC;
        boolean isTest3Passed;
        boolean isTest4Passed;

        float meltingTemp = 0;

        isGC = isQualifiedGAndCBasis(String.valueOf(generated_barcode));
        if (isGC) {
            isTest3Passed = isTest3Passed(String.valueOf(generated_barcode));
            if (isTest3Passed) {
                isTest4Passed = isTest4Passed(String.valueOf(generated_barcode));
                if (isTest4Passed) {

                    meltingTemp = calculateMeltingTemperature(generated_barcode);

                    if (isInMeltingTempRange(meltingTemp)) {
                        
                        if (isSimilarityAndComplPassed(returnSet, generated_barcode)) {
                            flag = true;
                        }
                    }
                }
            }
        }
        
        return flag;
    }
    
    private boolean isSimilarityAndComplPassed(Set<String> set, String generated_barcode) {
        boolean returnFlag = false;
        float similarityValue = 0, complmentarityValue = 0;
        String compString= new StringBuilder(generated_barcode).reverse().toString();
        if(set.isEmpty()) {
            returnFlag = true;
        }
        for (String code : set) {
            similarityValue = getSimilarityScore(generated_barcode.toCharArray(), code.toCharArray(), BARCODE_LENGTH);

            if (similarityValue <=SIMILARITY  ) {
                complmentarityValue = getComplimentaryScore(code.toCharArray(), compString.toCharArray(),
                        BARCODE_LENGTH);
                if (complmentarityValue <=COMPLEMENTARITY  ) {
                    returnFlag = true;
                }else {
                    returnFlag = false;
                }
            }else {
                returnFlag = false;
            }

        }
        return returnFlag;
    }

    private boolean isQualifiedGAndCBasis(String generatedCode) {

        boolean returnFlag = false;
        int gcCount = getGCCount(generatedCode);
        double percentage = getGCPercentage(gcCount);

        if (MIN_GC_PERCENTAGE <= percentage && percentage <= MAX_GC_PERCENTAGE) {
            returnFlag = true;
        }
        return returnFlag;
    }

    private double getGCPercentage(int gcCount) {

        return (double) (((gcCount * 1.0d) / BARCODE_LENGTH) * 100.0d);

    }

  /*  private double getGCPercentage(String generatedCode) {

        int gcCount = getGCCount(generatedCode);
        return getGCPercentage(gcCount);

    }
*/
    public static double calculateMeltingTemperature2(String generatedCode,int length) {
        int gcCount = getGCCount(generatedCode);
        return 64.9 + 41 * ((gcCount - 16.4) / length);
    }

    private boolean isTest3Passed(String code) {
        for (String diCode : test3Combinations) {
            if (charCount(code, diCode) > 0) {
                return false;
            }

        }
        return true;
    }

    private boolean isTest4Passed(String code) {

        boolean returnFlag = true;
        if (code.contains(longRunBasis[0]) || code.contains(longRunBasis[1]) || code.contains(longRunBasis[2])
                || code.contains(longRunBasis[3])) {
            returnFlag = false;
        }
        return returnFlag;
    }

    private boolean isInMeltingTempRange(float meltingTemp) {

        return ((meltingTemp >= MIN_MELTING_TEMPERATURE) && (meltingTemp <= MAX_MELTING_TEMPERATURE));
    }

    private float getSimilarityScore(char[] s1, char[] s2, int length) {
        int i = 0;
        int count = 0;
        while (i < length) {
            if (s1[i] == s2[i]) {
                ++count;
            }
            i++;

        }
        return ((count * 100f / length));

    }

    private float getComplimentaryScore(char[] s1, char[] s2, int length) {
        int i = 0;
        int count = 0;
        while (i < length) {
            switch (s1[i]) {
                case 'A':
                    if (s2[i] == 'T') {
                        ++count;
                    }
                    break;
                case 'T':
                    if (s2[i] == 'A') {
                        ++count;
                    }
                    break;
                case 'G':
                    if (s2[i] == 'C') {
                        ++count;
                    }
                    break;
                case 'C':
                    if (s2[i] == 'G') {
                        ++count;
                    }
                    break;

                default:
                    break;
            }
            i++;

        }
        return ((count * 100f / length));
    }

   
    private static float calculateMeltingTemperature(int gcCount, int atCount) {
        // return 64.9f + 41 * ((gcCount - 16.4f) / BARCODE_LENGTH);
        return 4 * gcCount + 2 * atCount;
    }

    public static float calculateMeltingTemperature(String barCode) {
        return calculateMeltingTemperature(getGCCount(barCode), getATCount(barCode));
    }

    public static int getGCCount(String generatedCode) {
        return charCount(generatedCode, "G") + charCount(generatedCode, "C");
    }

    private static int getATCount(String generatedCode) {
        return charCount(generatedCode, "A") + charCount(generatedCode, "T");
    }

    public static int charCount(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    private final String[] longRunBasis = new String[] { "AAAA", "TTTT", "GGGG", "CCCC" };
    private List<String> test3Combinations = new ArrayList<String>();
    {
        test3Combinations.add("ATATAT");
        test3Combinations.add("AGAGAG");
        test3Combinations.add("ACACAC");

        test3Combinations.add("TATATA");
        test3Combinations.add("TGTGTG");
        test3Combinations.add("TCTCTC");

        test3Combinations.add("GAGAGA");
        test3Combinations.add("GTGTGT");
        test3Combinations.add("GCGCGC");

        test3Combinations.add("CACACA");
        test3Combinations.add("CGCGCG");
        test3Combinations.add("CTCTCT");
    }

}
