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

    public static synchronized String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String validateOrigin(String origin) {
        String result = "";
        if (origin.contains("Anh")) {
            result = "Anh Quốc";
        }
        if (origin.contains("Bản") || origin.contains("Nhật")) {
            result = "Nhật Bản";
        }
        if (origin.contains("Pháp") || origin.contains("Paris")) {
            result = "Pháp";
        }
        if (origin.contains("Mĩ") || origin.contains("Mỹ") || origin.contains("U.S.A")) {
            result = "Mỹ";
        }
        if (origin.contains("Hàn Quốc")) {
            result = "Hàn Quốc";
        }
        if (origin.contains("Thái Lan")) {
            result = "Thái Lan";
        }
        return result;
    }

    public static String validateBrand(String brand) {
        String result = "";
        if (brand.contains(":")) {
            result = brand.replace(":", "").trim();
        } else {
            result = brand;
        }
        return result;
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

}
