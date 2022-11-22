package com.weizhuan.work;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    public static ArrayList<ArrayList<String>> splitIntoSeveralPieces(List<String> insertList) {
        StringBuilder sb = new StringBuilder();
        for (String s : insertList) {
            sb.append(s);
        }
        int len = sb.toString().length();
        if (len <= 350) {
            System.out.println("tomcat 文章字数小于350，内容中间插入1张图");
            return splitInto2Pieces(insertList);
        } else if (len > 350 && len <= 700) {
            System.out.println("tomcat 文章字数大于350并且小于700，内容中间插入2张图");
            return splitInto3Pieces(insertList);
        } else {
            System.out.println("tomcat 文章字数大于700，内容中间插入3张图");
            return splitInto4Pieces(insertList);
        }
    }

    public static ArrayList<ArrayList<String>> splitInto2Pieces(List<String> insertList) {
        StringBuilder sb = new StringBuilder();
        for (String s: insertList) {
            sb.append(s);
        }
        int len = sb.length();
        int one = len / 10 * 4;
        ArrayList<ArrayList<String>> re = new ArrayList<>();
        boolean oneFlag = true;
        ArrayList<String> tmp = new ArrayList<>();
        StringBuilder sb1 = new StringBuilder();
        for (String s: insertList) {
            tmp.add(s);
            sb1.append(s);
            int len1 = sb1.length();
            if (len1 > one && oneFlag) {
                re.add(tmp);
                tmp = new ArrayList<>();
                oneFlag = false;
            }
        }
        re.add(tmp);
        return re;
    }

    public static ArrayList<ArrayList<String>> splitInto3Pieces(List<String> insertList) {
        StringBuilder sb = new StringBuilder();
        for (String s: insertList) {
            sb.append(s);
        }
        int len = sb.length();
        int one = len / 10 * 3;
        int two = len / 10 * 7;
        ArrayList<ArrayList<String>> re = new ArrayList<>();
        boolean oneFlag = true;
        boolean twoFlag = true;
        boolean threeFlag = true;
        ArrayList<String> tmp = new ArrayList<>();
        StringBuilder sb1 = new StringBuilder();
        for (String s: insertList) {
            tmp.add(s);
            sb1.append(s);
            int len1 = sb1.length();
            if (len1 > one && oneFlag) {
                re.add(tmp);
                tmp = new ArrayList<>();
                oneFlag = false;
            } else if (len1 > two && twoFlag) {
                re.add(tmp);
                tmp = new ArrayList<>();
                twoFlag = false;
            }
        }
        re.add(tmp);
        return re;
    }

    public static ArrayList<ArrayList<String>> splitInto4Pieces(List<String> insertList) {
        StringBuilder sb = new StringBuilder();
        for (String s: insertList) {
            sb.append(s);
        }
        int len = sb.length();
        int oneFourth = len / 10 * 1;
        int twoFourth = len / 10 * 4;
        int threeFourth = len / 10 * 7;
        ArrayList<ArrayList<String>> re = new ArrayList<>();
        boolean oneFlag = true;
        boolean twoFlag = true;
        boolean threeFlag = true;
        ArrayList<String> tmp = new ArrayList<>();
        StringBuilder sb1 = new StringBuilder();
        for (String s: insertList) {
            tmp.add(s);
            sb1.append(s);
            int len1 = sb1.length();
            if (len1 > oneFourth && oneFlag) {
                re.add(tmp);
                tmp = new ArrayList<>();
                oneFlag = false;
            } else if (len1 > twoFourth && twoFlag) {
                re.add(tmp);
                tmp = new ArrayList<>();
                twoFlag = false;
            } else if (len1 > threeFourth && threeFlag) {
                re.add(tmp);
                tmp = new ArrayList<>();
                threeFlag = false;
            }
        }
        re.add(tmp);
        return re;
    }
}
