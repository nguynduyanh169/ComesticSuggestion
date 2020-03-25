/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anhnd.comestic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author anhnd
 */
public class TextUtils {
    public static String refineHtml(String src) {
        src = removeMiscTags(src);

        XMLChecker xmlSyntaxChecker = new XMLChecker();
        src = xmlSyntaxChecker.check(src);

        return src;
    }

    public static String removeMiscTags(String src) {
        String result = src;

        String expression = "<script.*?</script>";
        result = result.replaceAll(expression, "");

        expression = "<!--.*?-->";
        result = result.replaceAll(expression, "");

        expression = "&nbsp;?";
        result = result.replaceAll(expression, "");

        return result;
    }

    private static List<char[]> getListChar(String input) {
        ArrayList<char[]> listChar = new ArrayList<>();
        for (int i = 0; i < input.length() - 1; i++) {
            char[] chars = new char[2];
            chars[0] = input.charAt(i);
            chars[1] = input.charAt(i + 1);
            listChar.add(chars);
        }
        return listChar;
    }

    public static double getMatchingPercentage(String string1, String string2) {
        List<char[]> listChar1 = getListChar(string1);
        List<char[]> listChar2 = getListChar(string2);
        
        List<char[]> copy = new ArrayList<>(listChar2);
        int matches = 0;
        for (int i = listChar1.size(); --i >= 0;) {
            char[] listchar = listChar1.get(i);
            for (int j = copy.size(); --j >= 0;) {
                char[] toMatch = copy.get(j);
                if (listchar[0] == toMatch[0] && listchar[1] == toMatch[1]) {
                    copy.remove(j);
                    matches += 2;
                    break;
                }
            }
        }
        return (double) matches / (listChar1.size() + listChar2.size());
    }
    public static synchronized String getUUID(){
        return UUID.randomUUID().toString();
    }
    
    public static String validateOrigin(String origin) {
        String result = "";
        if(origin.contains("Anh")) {
            result = "Anh Quốc";
        }
        if(origin.contains("Bản") || origin.contains("Nhật")){
            result = "Nhật Bản";
        }
        if(origin.contains("Pháp") || origin.contains("Paris")){
            result = "Pháp";
        }
        if(origin.contains("Mĩ") || origin.contains("Mỹ") || origin.contains("U.S.A")){
            result = "Mỹ";
        }
        if(origin.contains("Hàn Quốc")){
            result = "Hàn Quốc";
        }
        if(origin.contains("Thái Lan")){
            result = "Thái Lan";
        }
        return result;
    }
    
    public static String validateBrand(String brand){
        String result = "";
        if(brand.contains(":")){
            result = brand.replace(":", "").trim();
        }else{
            result = brand;
        }
        return result;
    }
    
}
