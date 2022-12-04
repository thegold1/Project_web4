package com.weizhuan.work.util;

import com.weizhuan.work.simhash.SimhashUtil;
import org.xm.Similarity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// author tanghao1
public class TxtUtils {

    public static String WEI_ZHUAN_PREFIX = "D:\\weizhuan_web\\";
    public static String PAPER_PATH_CONFIG = WEI_ZHUAN_PREFIX + "config\\paper_path_config.txt";
    public static String mReadPaperPath = new String(WEI_ZHUAN_PREFIX+"test");
    public static String mCharSet = new String("");
    public static String SMALL_NUMBER = "0123456789";
    public static String BIG_NUMBER = "一二三四五六七八九十";
    public static String CHAR_PATTERN = "！？；：，。";
    public static String SENSITIVE_WORDS_PATH = WEI_ZHUAN_PREFIX + "config\\sensitive_words.txt";
    public static List<String> SENSITIVE_WORDS = readTxt(SENSITIVE_WORDS_PATH);
    public static String PAPER_MAX_MIN_CONFIG = WEI_ZHUAN_PREFIX + "config\\paper_max_min_config.txt";
    public static String TITLE_SENSITIVE_WORDS_PATH = WEI_ZHUAN_PREFIX + "config\\title_sensitive_words.txt";
    public static List<String> TITLE_SENSITIVE_WORDS = readTxt(TITLE_SENSITIVE_WORDS_PATH);
    public static int MAX_LEN = 900;
    public static int MIN_LEN = 450;
    public static String PAPER_REPLACEMENT_CONFIG = WEI_ZHUAN_PREFIX + "config\\paper_replacement.txt";
    public static String PAPER_HEAD_5_CHAR_REPLACEMENT_CONFIG = WEI_ZHUAN_PREFIX + "config\\head_5_char.txt";
    public static HashMap<String, String> mReplaceMap = new HashMap<String, String>();
    public static HashMap<String, String> mHead_5_ReplaceMap = new HashMap<String, String>();
    public static String MARK_PATTERN = "；：？！，。、";
    public static String MERGE_MARK_PATTERN = ".；：？！，。、“”),";
    //  第一步 第一、  等等
    public static String NUM_MARk_PATTERN = "，。！？,、.)步";

    public static boolean isTitleContainSenWords(String s) {
        for (String ss: TITLE_SENSITIVE_WORDS) {
            if (s.contains(ss)) {
                System.out.println("tomcat 文章标题："+s+" 包含关键词:"+ss + "，文章将被删除");
                return true;
            }
        }
        return false;
    }

    public static void readPaperPath(String path) {
        try {
            File file = new File(path);
            ArrayList<String> ls = new ArrayList<String>();

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            if ("".equals(TxtUtils.mCharSet)) {
                TxtUtils.mCharSet = TxtUtils.getFileCharset(new File(path));
            }

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), TxtUtils.mCharSet);

            BufferedReader br = new BufferedReader(reader);

            String line = "";
            line = br.readLine();
            if (line != null) {
                ls.add(line);
//                MAX_LEN = Integer.parseInt(line);
                mReadPaperPath = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String removeStringCommonWord(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        String re = s;
        do {
            s = re;
            re = removeCommonWord(re);
        } while (!re.equals(s));
        return re;
    }

    public static String removeCommonWord(String s) {
        String common = "";
        int len = -1;
        for (int i = 0; i <= s.length() / 2; i++) {
            for (int j = i + 5; j <= s.length() / 2; j++) {
                String tmp = s.substring(i, j);
                String other = s.substring(j, s.length());
                if (other.contains(tmp) && tmp.length() > len) {
                    common = tmp;
                    len = tmp.length();
                }
//                System.out.println("tomcat tmp:"+tmp);
//                System.out.println("tomcat other:"+other);
            }
        }
        if (len < 5) {
            return s;
        }
//        System.out.println("tomcat common:"+common);
//        System.out.println("tomcat len:"+len);
        int index = s.indexOf(common);
        String t1 = s.substring(0,index+common.length());
        String t2 = s.substring(index+common.length(),s.length());
//        System.out.println("tomcat t1:"+t1);
//        System.out.println("tomcat t2:"+t2);
        t2 = t2.replace(common,"");
//        System.out.println("tomcat later t2:"+t2);
        String re = t1 + t2;
//        System.out.println("tomcat re:"+re);
        return re;
    }

    // 去除文本中的空格，不区分英文和中文空格
    public static String removeSpace(String value) {
        String afterValue = value.replace(" ", "");
        return afterValue;
    }

    // 超过30个字母,or超过30个数字,or超过20个符号,删除
    public static boolean isExceptionCharOverLimit(String s) {
        int a = 0;
        int b = 0;
        int c = 0;
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ('A' <= chars[i] && 'Z' >= chars[i]
                    || 'a' <= chars[i] && 'z' >= chars[i]) {
                a++;
            } else if (SMALL_NUMBER.contains(new String(new char[]{chars[i]}))
                    || BIG_NUMBER.contains(new String(new char[]{chars[i]}))) {
                b++;
            } else if (CHAR_PATTERN.contains(new String(new char[]{chars[i]}))) {
                c++;
            }
        }
        if (a >= 30 || b >= 30 || c >= 20) {
            return true;
        } else {
            return false;
        }
    }

    // 判断txt文本有多少字符
    // String path = "C:\\Users\\Administrator\\Desktop\\test_demo.txt";
    public static int getTxtLen(String path) {
        try {
            File file = new File(path);

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fw = new FileReader(file.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fw);
            StringBuilder sb = new StringBuilder("");
            String line = "";
            line = br.readLine();
            if (line != null) {
                sb.append(line);
                sb.append("\n");
            }
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if (line != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
            br.close();
            return sb.toString().length();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String replace(String s) {
        for (String key : mReplaceMap.keySet()) {
            s = s.replace(key, mReplaceMap.get(key));
        }
        return s;
    }

    public static String head5CharReplace(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        String s1 = "";
        String s2 = "";
        if (s.length() > 5) {
            s1 = s.substring(0, 5);
            s2 = s.substring(5, s.length());
        } else {
            s1 = s;
        }
        for (String key : mHead_5_ReplaceMap.keySet()) {
            s1 = s1.replace(key, mHead_5_ReplaceMap.get(key));
        }
        return s1 + s2;
    }

    public static void configReplaceMap(String s) {
        if ("".equals(s)) {
            return;
        }
        String[] ss = s.split(">");
        if (ss.length >= 2) {
            mReplaceMap.put(ss[0],ss[1]);
        }
    }

    public static void configHead5CharReplaceMap(String s) {
        if ("".equals(s)) {
            return;
        }
        String[] ss = s.split(">");
        if (ss.length >= 2) {
            mHead_5_ReplaceMap.put(ss[0], ss[1]);
        }
    }

    public static void readReplaceConfig(String path) {
        try {
            File file = new File(path);

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            if ("".equals(TxtUtils.mCharSet)) {
                TxtUtils.mCharSet = TxtUtils.getFileCharset(new File(path));
            }

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), TxtUtils.mCharSet);

            BufferedReader br = new BufferedReader(reader);

            String line = "";
            line = br.readLine();
            if (line != null) {
                configReplaceMap(line);
            }
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if (line != null) {
                    configReplaceMap(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readHead5CharReplaceConfig(String path) {
        try {
            File file = new File(path);

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            if ("".equals(TxtUtils.mCharSet)) {
                TxtUtils.mCharSet = TxtUtils.getFileCharset(new File(path));
            }

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), TxtUtils.mCharSet);

            BufferedReader br = new BufferedReader(reader);

            String line = "";
            line = br.readLine();
            if (line != null) {
                configHead5CharReplaceMap(line);
            }
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if (line != null) {
                    configHead5CharReplaceMap(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void readPaperPath(String path) {
//        try {
//            File file = new File(path);
//            ArrayList<String> ls = new ArrayList<String>();
//
//            // if file doesn't exist, then create it
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//
//            if ("".equals(TxtUtils.mCharSet)) {
//                TxtUtils.mCharSet = TxtUtils.getFileCharset(new File(path));
//            }
//
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(file), TxtUtils.mCharSet);
//
//            BufferedReader br = new BufferedReader(reader);
//
//            String line = "";
//            line = br.readLine();
//            if (line != null) {
//                ls.add(line);
////                MAX_LEN = Integer.parseInt(line);
//                mReadPaperPath = line;
////                SimhashUtil.SHENG_CHENG_PATH = line;
//                String[] ss = mReadPaperPath.split("\\\\");
////                String[] ss = line.split("")
//                for (int i = 0; i < ss.length; i++) {
//                    if ("".equals(ss[i])) {
//                        continue;
//                    }
//                    if (i != ss.length - 1) {
//                        SimhashUtil.SHENG_CHENG_PATH_word2 += ss[i] +"\\";
//                    } else {
//                        SimhashUtil.SHENG_CHENG_PATH_word2 += ss[i];
//                    }
//                }
//
////                System.out.println("tomcat path:"+SimhashUtil.SHENG_CHENG_PATH_word);
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void readConfig(String path) {
        try {
            File file = new File(path);
            ArrayList<String> ls = new ArrayList<String>();

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            if ("".equals(TxtUtils.mCharSet)) {
                TxtUtils.mCharSet = TxtUtils.getFileCharset(new File(path));
            }

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), TxtUtils.mCharSet);

            BufferedReader br = new BufferedReader(reader);

            String line = "";
            line = br.readLine();
            if (line != null) {
                ls.add(line);
                MAX_LEN = Integer.parseInt(line);
            }
            if (line != null) {
                line = br.readLine();
                MIN_LEN = Integer.parseInt(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readTxt(String path) {
        try {
            File file = new File(path);
            ArrayList<String> ls = new ArrayList<String>();

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            if ("".equals(TxtUtils.mCharSet)) {
                TxtUtils.mCharSet = TxtUtils.getFileCharset(new File(path));
            }

            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), TxtUtils.mCharSet);
//                    new FileInputStream(file), "GBK");

//            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言


//            FileReader fw = new FileReader(file.getAbsoluteFile());
//            FileReader fw = new FileReader(file.getAbsoluteFile());
//            BufferedReader br = new BufferedReader(fw);
            BufferedReader br = new BufferedReader(reader);

            StringBuilder sb = new StringBuilder("");
            String line = "";
            line = br.readLine();
            if (line != null) {
                ls.add(line);
                sb.append(line);
                sb.append("\n");
            }
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
//                System.out.println("tomcat line:"+line);
                if (line != null) {
                    ls.add(line);
                    sb.append(line);
                    sb.append("\n");
                }
            }

            br.close();
            return ls;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dbc2sbcCase(String s) {
        if (s == null) {
            return null;
        }
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '.') {
                if (i + 1 < c.length
                        && i - 1 >= 0
                        && SMALL_NUMBER.contains(new String(new char[] {c[i - 1]}))
                        && SMALL_NUMBER.contains(new String(new char[] {c[i + 1]}))) {

                } else {
                    c[i] = '。';
                }
            } else if (c[i] == ',') {
                if (i + 1 < c.length
                        && i - 1 >= 0
                        && SMALL_NUMBER.contains(new String(new char[] {c[i - 1]}))
                        && SMALL_NUMBER.contains(new String(new char[] {c[i + 1]}))) {

                } else {
                    c[i] = '，';
                }
                c[i] = '，';
            } else if (c[i] == '!') {
                c[i] = '！';
            } else if (c[i] == '?') {
                c[i] = '？';
            } else if (c[i] == ':') {
                if (i + 1 < c.length
                        && i - 1 >= 0
                        && SMALL_NUMBER.contains(new String(new char[] {c[i - 1]}))
                        && SMALL_NUMBER.contains(new String(new char[] {c[i + 1]}))) {

                } else {
                    c[i] = '：';
                }
            } else if (c[i] == ';') {
                c[i] = '；';
            }
        }
        return new String(c);
    }

    public static boolean isStringContainSenWords(String s) {
        for (String ss: SENSITIVE_WORDS) {
            if (s.contains(ss)) {
                return true;
            }
        }
        return false;
    }

    public static String removeSpecificWords(String s) {
        for (String ss: SENSITIVE_WORDS) {
            s = s.replace(ss,"");
        }
        return s;
    }

    public static String[] splitSentence(String s) {
        if (s == null || "".equals(s)) {
            return new String[]{s};
        }
        String[] ls = s.split("。");
        for (int i = 0; i < ls.length; i++) {
            char[] chars = ls[i].toCharArray();
            if (ls[i].length() >= 2) {
                char c = ls[i].charAt(ls[i].length() - 1);
                if (MARK_PATTERN.contains(new String(new char[] {c}))) {
                    if ('？' == chars[chars.length - 1]
                        || '！' == chars[chars.length - 1]
                        || '：' == chars[chars.length - 1]
                        || ':' == chars[chars.length - 1]) {

                    } else {
                        chars[chars.length - 1] = '。';
                        ls[i] = String.valueOf(chars);
                    }
                }  else {
                    ls[i] += "。";
                }
            }
        }
        return ls;
    }

    // 判断两句话是否相等
    public static boolean isStringEqual(String lastSentence, String currentSentence) {
        if (lastSentence == null || currentSentence == null) {
            return false;
        }

        if (lastSentence.length() >= 10 && currentSentence.length() >= 10) {
            // 0.2 就是阈值
            double editDistanceResult = Similarity.editDistanceSimilarity(lastSentence, currentSentence);
            return editDistanceResult > 0.2;
        } else {
            return lastSentence.equals(currentSentence);
        }
    }

    public static boolean boldStringEqual(String lastSentence, String currentSentence) {
        if ("".equals(lastSentence)
                || "".equals(currentSentence)
                || lastSentence.length() != currentSentence.length()) {
            return false;
        }
        int index1 = bigNumIndex(lastSentence);
        int index2 = smallNumIndex(lastSentence);
        String s1 = "";
        String s2 = "";
        if (index1 != -1) {
            if (index1 + 1 <= lastSentence.length() - 1) {
                s1 = lastSentence.substring(index1 + 1, lastSentence.length() - 1);
                s2 = currentSentence.substring(index1 + 1, lastSentence.length() - 1);
            }
        } else if (index2 != -1) {
            if (index2 + 1 <= lastSentence.length() - 1) {
                s1 = lastSentence.substring(index2 + 1, lastSentence.length() - 1);
                s2 = currentSentence.substring(index2 + 1, lastSentence.length() - 1);
            }
        } else {
            return false;
        }
        if ("".equals(s1) || "".equals(s2)) {
            return false;
        }
//        return s1.equals(s2);
        return isStringEqual(s1, s2);
    }

    // 判断这一行最后一个符号是不是逗号
    public static boolean isLastWordComma(String s) {
        if (s == null || "".equals(s)) {
            return false;
        }
        char[] chars = s.toCharArray();
        char c = chars[chars.length - 1];
        if (c == '，') {
            return true;
        } else {
            return false;
        }
    }

    public static String removeDuplicateWord2(String s) {
        s = removeDuplicateWord(s, "，");
        s = removeDuplicateWord(s, "、");
        return s;
    }

    // 去掉这一行文字里面重复的话
    // case: "我的，我的，你的。"--->"我的，你的"
    public static String removeDuplicateWord(String s, String splitString) {
        String[] ss = s.split(splitString);
        for (int i = ss.length - 1; i >= 1; i--) {
            if (ss[i].length() >= 10 && ss[i - 1].length() >= 10) {
                String s1 = ss[i];
                String s2 = ss[i - 1];
                double editDistanceResult = Similarity.editDistanceSimilarity(s1, s2);
                if (editDistanceResult > 0.2) {
                    ss[i] = "";
                }
            } else if (ss[i].equals(ss[i - 1])) {
                ss[i] = "";
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= ss.length - 1; i++) {
            if (!"".equals(ss[i])) {
                ss[i] = removeStringCommonWord(ss[i]);
            }
            if (i == ss.length - 1) {
                if (!ss[i].equals("")) {
                    sb.append(ss[i]);
                }
            } else {
                if (!ss[i].equals("")) {
                    sb.append(ss[i]).append("，");
                }
            }
        }
        if (!"".equals(sb.toString())
                && sb.toString().length() > 1
                && sb.toString().charAt(sb.length() - 1) == '，') {
            sb.setCharAt(sb.length() - 1,'。');
        }
        return sb.toString();
    }

    // 删除多余的符号
    // case "我的，，。魔法书"-->"我的，魔法书"
    public static String removeRedundantSymbol(String s) {
//        String s =  "我的，，。魔法书";
//        String pattern = "；：？！，。、“”";
        char[] chars = s.toCharArray();
        boolean flag = false;
        for (int i = 0; i < chars.length; i++) {
            if (MERGE_MARK_PATTERN.contains(new String(new char[] {chars[i]} ))) {
                if (flag) {
                    chars[i] = ' ';
                } else {
                    flag = true;
                }
            } else {
                flag = false;
            }
        }
        String re = String.valueOf(chars);
        re = re.replace(" ","");
        return re;
    }

    // 将一行中文第2个字符句号变成英文句号
    // case "1。XXX"-->"1.XXX"
    public static String changeChFullStop2En(String s) {
        if (s == null || "".equals(s)) {
            return "";
        }
        char[] chars = s.toCharArray();
//        if (chars.length >= 2 && chars[1] == '。') {
//            chars[1] = '.';
//        }
        for (int i = 0; i < 5 && i < chars.length ; i++) {
            if (chars[i] == '。') {
                chars[i] = '.';
            }
        }
        return String.valueOf(chars);
    }

    // 删除相似的段落
    // 1.AAAA XXXXX 2.AAAA XXXXX 3.BBBB XXXXX ----> 1.AAAA XXXXX  3.BBBB XXXXX
    public static ArrayList<String> delSimilarPara(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return null;
        }
        boolean flag = false;
        ArrayList<String> re = new ArrayList<String>();
        List<Integer> numList = new ArrayList<Integer>();
        for (int i = 0; i < ls.size(); i++) {
            String s = ls.get(i);
            char[] chars = s.toCharArray();
            if (chars.length == 0) {
                continue;
            }
            if (isStringContainsNumber(s)) {
                numList.add(i);
            }
        }
        int left = -1;
        int right = -1;
        for (int i = 0; i <= numList.size() - 2; i++) {
            if (flag == true) {
                break;
            }
            String s1 = ls.get(numList.get(i));
            String s2 = "";

            for (int j = i + 1; j <= numList.size() - 1; j++) {
                s2 = ls.get(numList.get(j));
                if (boldStringEqual(s1, s2)) {
                    if (j == numList.size() - 1) {
                        left = numList.get(j);
                        right = (left + 1) < ls.size() + 1 ? (left + 1) : ls.size() + 1;
                    } else {
                        left = numList.get(j);
                        right = left + 1;
                    }
                    flag = true;
                    break;
                }
            }
        }
        for (int i = 0; i < ls.size(); i++) {
            if (i >= left && i <= right) {

            } else {
                re.add(ls.get(i));
            }
        }
        if (flag) {
            return delSimilarPara(re);
        } else {
            return re;
        }
    }

    public static ArrayList<String> addBoldMark(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return null;
        }
        boolean flag = false;
        List<Integer> numList = new ArrayList<Integer>();
        List<Integer> normalList = new ArrayList<Integer>();
        for (int i = 0; i < ls.size(); i++) {
            String s = ls.get(i);
            char[] chars = s.toCharArray();
            if (chars.length == 0) {
                continue;
            }
            if (isStringContainsNumber(s)) {
                numList.add(i);
            } else {
                normalList.add(i);
            }
        }
        int dollarCount = 0;
        for (int i = 0; i < numList.size(); i++) {
            int index = numList.get(i);
            String s = ls.get(index);

            int nextIndex = -1;
            if (i <= numList.size() - 2) {
                nextIndex = numList.get(i + 1);
            } else {
                nextIndex = 100;
            }
            char[] chars = s.toCharArray();
            if (chars.length <= 20 &&
                    (chars[chars.length - 1] == '？'
                     || chars[chars.length - 1] == '！')) {
                s += "$";
                dollarCount++;
            } else if (nextIndex - index == 1) {
                // 相邻 如果大于 35字 部分加粗
                if (s.length() >= 35 && s.contains("，")) {
                    String s1 = s.substring(0, s.indexOf("，") + 1);
                    s1 += "$";
                    String s2 = s.substring(s.indexOf("，") + 1);
                    s = s1 + s2;
                    dollarCount++;
                }
            } else {
                // 不相邻一定加粗
                s += "$";
                dollarCount++;
            }
            ls.set(index, s);
        }
        for (int i = 0; i < normalList.size(); i++) {
            int index = normalList.get(i);
            String s = ls.get(index);
            char[] chars = s.toCharArray();
            if (chars.length <= 25 &&
                    (chars[chars.length - 1] == '？'
                            || chars[chars.length - 1] == '：'
                            || chars[chars.length - 1] == '：')) {
                s += "$";
                dollarCount++;
            }
            ls.set(index, s);
        }

        // 如果全文内，加粗项小于等于2
        // 如果单行，小于20个字，单行进行加粗
        // 单行，超过100字单行，针对前1个符号+内容，进行加粗
        // 针对全文内，结尾为！？：内容进行加粗
        if (dollarCount <= 2) {
            for (int i = 0; i < ls.size(); i++) {
                String s = ls.get(i);
                if (s.contains("$")) {
                    continue;
                }
                if (s.length() <= 20) {
                    s += "$";
                } else {
                    if (s.length() >= 100) {
                        char[] chars = s.toCharArray();
                        for (int j = 0; j < chars.length; j++) {
                            if (isStringContainsChar(CHAR_PATTERN, chars[j])) {
                                String s1 = s.substring(0, j + 1);
                                s1 += "$";
                                String s2 = s.substring(j + 1, s.length());
                                s = s1 + s2;
                                break;
                            }
                        }
                    } else {
                        String pattern = "！？：";
                        char[] chars = s.toCharArray();
                        for (int j = 0; j < chars.length; j++) {
                            if (isStringContainsChar(pattern, chars[j])) {
                                String s1 = s.substring(0, j + 1);
                                s1 += "$";
                                String s2 = s.substring(j + 1, s.length());
                                s = s1 + s2;
                                break;
                            }
                        }
                    }
                }
                ls.set(i, s);
            }
        }
        return ls;
    }

    // 遍历带“$”的一行字，如果超过50个字，以“，”来分段，同时给第二段加上“。”
    public static ArrayList<String> splitSentenceWithMark(ArrayList<String> ls) {
        if (ls == null) {
            return new ArrayList<String>();
        }
        ArrayList<String> re = new ArrayList();
        for (int i = 0;i < ls.size(); i++) {
            boolean flag = false;
            String s = ls.get(i);
            if (s.length() > 0 && isStringContainsNumber(s) && s.contains("$") && s.length() > 50) {
                for (int j = 0; j < CHAR_PATTERN.length(); j++) {
                    char c = CHAR_PATTERN.charAt(j);
                    if (s.contains(new String(""+c))) {
                        int index = s.indexOf(c);
                        String tmp = s.substring(0,index) + "$";
                        String tmp2 = s.substring(index+1,s.length()-1);
                        char[] chars = tmp2.toCharArray();
                        if (chars.length > 0) {
                            char cc = chars[chars.length - 1];
                            if (CHAR_PATTERN.contains(new String(new char[cc]))) {
                                chars[chars.length - 1] = '。';
                                tmp2 = new String(chars);
                            } else {
                                tmp2 = new String(chars) + "。";
                            }
                        }
                        re.add(tmp);
                        re.add(tmp2);
                        flag = true;
                        break;
                    }
                }
                if (flag) {

                } else {
                    re.add(s);
                }

            } else {
                re.add(s);
            }
        }
        return re;
    }

    static String[] smalls = new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
    static String[] bigs = new String[] {"一","二","三","四","五","六","七","八","九","十",
    "十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
    static String SMALL_PATTERNS = "0123456789";
    static String BIG_PATTERS = "零一二三四五六七八九十";
    static String NUM_PATTERS = "零一二三四五六七八九十0123456789";

    public static boolean isStringContainsChar(String pattern, char c) {
        if (pattern == null || "".equals(pattern)) {
            return false;
        }
        return pattern.contains(new String(new char[] {c}));
    }

    // 删除副标题只有1个数字以及符号
    // 1.XXX -> XXX
    // 一.XXX -> XXX
    public static ArrayList<String> removeOnlyOneBigNumAndSmallNum(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return null;
        }
        int smallCount = 0;
        int smallIndex = -1;
        int bigCount = 0;
        int bigIndex = -1;
        for (int i = 0; i < ls.size(); i++) {
            String s = ls.get(i);
            char[] chars = s.toCharArray();
            if (chars.length == 0) {
                continue;
            }
            if (isStringContainBigNumber(s)) {
                bigIndex = i;
                bigCount++;
            } else if (isStringContainSmallNumber(s)) {
                smallIndex = i;
                smallCount++;
            }
        }
        if (smallCount == 1) {
            String s = ls.get(smallIndex);
            char[] chars = s.toCharArray();
            for (int i = 0; i < 5 && i < chars.length; i++) {
                if (isStringContainsChar(SMALL_PATTERNS, chars[i])
                        || isStringContainsChar(NUM_MARk_PATTERN, chars[i])) {
                    chars[i] = ' ';
                }
            }
            s = new String(chars);
            s = s.replace(" ","");
            ls.set(smallIndex, s);
        }
        if (bigCount == 1) {
            String s = ls.get(bigIndex);
            char[] chars = s.toCharArray();
            for (int i = 0; i < 5 && i < chars.length; i++) {
                if (isStringContainsChar(BIG_PATTERS, chars[i])
                        || isStringContainsChar(NUM_MARk_PATTERN, chars[i])) {
                    chars[i] = ' ';
                }
            }
            s = new String(chars);
            s = s.replace(" ","");
            ls.set(bigIndex, s);
        }
        return ls;
    }

    // 删除数字带空格
    // 删除空格
    public static ArrayList<String> delNumWithSpace(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return null;
        }
        ArrayList<String> re = new ArrayList<String>();
        for (int i = 0; i < ls.size(); i++) {
            String originString = ls.get(i);
            char[] chars = originString.toCharArray();
            if (chars.length == 0) {
                continue;
            }
            boolean isSpace = false;
            for (int j = chars.length - 1; j >= 0; j--) {
                if (' ' == chars[j]) {
                    isSpace = true;
                } else if (isStringContainsChar(BIG_PATTERS, chars[j])
                        || isStringContainsChar(SMALL_PATTERNS, chars[j])) {
                    if (isSpace) {
                        chars[j] = ' ';
                    }
                } else {
                    isSpace = false;
                }
            }
            String processString = String.valueOf(chars);
            processString = processString.replace(" ", "");
            re.add(processString);
        }
        return re;
    }

    public static ArrayList<String> processWithEOD(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return null;
        }
        String tmp = ls.get(ls.size() - 1);
        if (tmp.contains("<eod>")) {
            return ls;
        }
        ArrayList<String> re = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (String s : ls) {
            sb.append(s);
        }
        int len = sb.toString().length();
        if (len < 650) {
            return ls;
        }
        int target = len * 80 / 100;
        sb = new StringBuilder();
        for (String s : ls) {
            re.add(s);
            sb.append(s);
            if (sb.toString().length() >= target) {
                break;
            }
        }
        return re;
    }

    // 将多行文字首字符 顺序化
    // 2.xxx 3.xxx 4.xxx --> 1.xxx 2.xxx 3.xxx
    // 二.xxx 三.xxx 四.xxx --> 一.xxx 二.xxx 三.xxx
    public static ArrayList<String> makeNumOrder2(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return null;
        }
        ArrayList<String> re = new ArrayList<String>();
        int bigIndex = 0;
        int smallIndex = 0;
        for (int i = 0; i < ls.size(); i++) {
            String originString = ls.get(i);
            char[] chars = originString.toCharArray();
            if (chars.length == 0) {
                continue;
            }
            String tmp = "";
            boolean isContainBigNum = false;
            boolean isContainSmallNum = false;
            boolean isContainChar = false;
            boolean isDataChange = false;
            for (int j = 0; j < chars.length - 1 && j <=4; j++) {
                if (isStringContainsChar(BIG_PATTERS, chars[j])) {
                    if (isContainSmallNum) {
                        break;
                    }
                    if (bigIndex <= 19) {
                        chars[j] = 'b';
                        tmp += "b";
                        isContainBigNum = true;
                        continue;
                    }
                } else if (isStringContainsChar(SMALL_PATTERNS, chars[j])) {
                    if (isContainBigNum) {
                        break;
                    }
                    if (smallIndex <= 19) {
                        chars[j] = 's';
                        tmp += "s";
                        isContainSmallNum = true;
                        continue;
                    }
                } else if (isStringContainsChar(NUM_MARk_PATTERN, chars[j])) {
                    isContainChar = true;
                    break;
                } else if (isContainBigNum || isContainSmallNum) {
                    isContainBigNum = false;
                    isContainSmallNum = false;
                    break;
                }
            }

            String processString = String.valueOf(chars);
            if (!"".equals(tmp)) {
                String replace = "";
                if (isContainSmallNum && isContainChar) {
                    replace = smalls[smallIndex];
                    smallIndex++;
                } else if (isContainBigNum && isContainChar) {
                    replace = bigs[bigIndex];
                    bigIndex++;
                    smallIndex = 0;
                }
                if (!"".equals(replace)) {
                    processString = processString.replace(tmp, replace);
                    isDataChange = true;
                }
            }
            if (isDataChange) {
                re.add(processString);
            } else {
                re.add(originString);
            }
        }
        return re;
    }

    // 将多行文字首字符 顺序化
    // 2.xxx 3.xxx 4.xxx --> 1.xxx 2.xxx 3.xxx
    // 二.xxx 三.xxx 四.xxx --> 一.xxx 二.xxx 三.xxx
    public static ArrayList<String> makeNumOrder(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return null;
        }
        ArrayList<String> re = new ArrayList<String>();
        String big = "一二三四五六七八九十";
        int bigIndex = 0;
        String small = "123456789";
//        String charPattern = "，。、:：,)步";
        int smallIndex = 0;
        for (int i = 0; i < ls.size(); i++) {
            String s = ls.get(i);
            boolean isNum = false;
            char[] chars = s.toCharArray();
            if (chars.length == 0) {
                continue;
            }
            for (int j = 0; j < chars.length - 1 && j <=3; j++){
                if (big.contains(new String(new char[] {chars[j]}))
                        && chars.length > j + 1
                        && NUM_MARk_PATTERN.contains(new String(new char[]{chars[j + 1]}))) {
                    if (bigIndex <= 9) {
                        chars[j] = big.charAt(bigIndex);
                        bigIndex++;
                        isNum = true;
                        smallIndex = 0;
                    }
                } else if (small.contains(new String(new char[] {chars[j]}))
                        && chars.length > j + 1
                        && NUM_MARk_PATTERN.contains(new String(new char[]{chars[j + 1]}))) {
                    if (smallIndex <= 8) {
                        chars[j] = small.charAt(smallIndex);
                        smallIndex++;
                        isNum = true;
                    }
                }
            }
//            String ss = String.valueOf(chars) + (isNum ? "$" : "");
            String ss = String.valueOf(chars);
            re.add(ss);
        }
        return re;
    }

    //判断编码格式方法
    public static String getFileCharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    //句子是否以逗号结尾
    public static boolean isStringEndWithComma(String s) {
        if (s == null || "".equals(s)) {
            return false;
        }
        char[] chars = s.toCharArray();
        if (chars[chars.length - 1] == '，') {
            return true;
        } else {
            return false;
        }
    }

    public static String dealLastString(String s) {
        if ("".equals(s)) {
            return "";
        }
        String charsPattern = "。！？";
        char[] chars = s.toCharArray();
        if (chars.length < 5) {
            System.out.println("tomcat 内容少于5个字，删除最后一行");
            return "";
        } else if ((chars[chars.length - 1] == '，' ||chars[chars.length - 1] == ',')
                && chars.length > 30
                ||charsPattern.contains(new String(new char[] {chars[chars.length - 1]}))) {
            chars[chars.length - 1] = '。';
            return new String(chars);
        } else {
            System.out.println("tomcat 内容不符合要求，删除最后一行");
            return "";
        }
    }

    public static String dealLastStringSecond(String s) {
        if ("".equals(s)) {
            return "";
        }
        char[] chars = s.toCharArray();
//        String charPattern = "，。！？,、.)步";
        int count = 0;
        for (char c: chars) {
            if (count == 3) {
                break;
            }
            if (BIG_NUMBER.contains(new String(new char[]{c}))) {
                return "";
            } else if (SMALL_NUMBER.contains(new String(new char[]{c})) && chars.length < 10) {
                return "";
            } else {
                if (CHAR_PATTERN.contains(new String(new char[]{chars[chars.length - 1]}))) {
                    chars[chars.length - 1] = ' ';
                    return new String(chars).trim();
                } else if (count == 3){
                    return new String(chars);
                }
                count++;
            }
        }
        return new String(chars);
    }

    // 1.XXXX. --> 1.XXXX
    public static String deleteStringLastChar(String s) {
        if (s == null || "".equals(s)) {
            return "";
        }
        char[] chars = s.toCharArray();
        if (isStringContainsNumber(s) && CHAR_PATTERN.contains(new String(new char[]{chars[chars.length - 1]}))) {
            chars[chars.length - 1] = ' ';
        }
        return new String(chars).trim();
    }

    // 判断某行前三个字符是否包含数字，且数字后面是不是符号
    public static boolean isStringContainsNumber(String s) {
        if ("".equals(s)) {
            return false;
        }
        char[] chars = s.toCharArray();
//        String charPattern = "，。！？,、.)步";
        int count = 0;
        for (char c: chars) {
            if (count == 3) {
                break;
            }
            if (isCharBigNum(c) || isCharSmallNum(c)) {
                if (count <= chars.length - 2 && NUM_MARk_PATTERN.contains(new String(new char[]{chars[count+1]}))) {
                    return true;
                }
            }
            count++;
        }
        return false;
    }

    public static boolean isStringContainBigNumber(String s) {
        if ("".equals(s)) {
            return false;
        }
        char[] chars = s.toCharArray();
        String charPattern = "，。！？,、.)步";
        int count = 0;
        for (char c: chars) {
            if (count == 3) {
                break;
            }
            if (isCharBigNum(c)) {
                if (count <= chars.length - 2 && charPattern.contains(new String(new char[]{chars[count+1]}))) {
                    return true;
                }
            }
            count++;
        }
        return false;
    }

    public static boolean isStringContainSmallNumber(String s) {
        if ("".equals(s)) {
            return false;
        }
        char[] chars = s.toCharArray();
        String charPattern = "，。！？,、.)步";
        int count = 0;
        for (char c: chars) {
            if (count == 3) {
                break;
            }
            if (isCharSmallNum(c)) {
                if (count <= chars.length - 2 && charPattern.contains(new String(new char[]{chars[count+1]}))) {
                    return true;
                }
            }
            count++;
        }
        return false;
    }

    public static int bigNumIndex(String s) {
        if ("".equals(s)) {
            return -1;
        }
        char[] chars = s.toCharArray();
        int count = 0;
        for (char c: chars) {
            if (count == 3) {
                break;
            }
            if (isCharBigNum(c)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    public static int smallNumIndex(String s) {
        if ("".equals(s)) {
            return -1;
        }
        char[] chars = s.toCharArray();
        int count = 0;
        for (char c: chars) {
            if (count == 3) {
                break;
            }
            if (isCharSmallNum(c)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    public static boolean isCharBigNum(char c) {
        return BIG_NUMBER.contains(new String(new char[]{c}));
    }

    public static boolean isCharSmallNum(char c) {
        return SMALL_NUMBER.contains(new String(new char[]{c}));
    }

    // 多行合并,如果连续的两行文字都不超过50，且前五个字符不包含数字，那么合并
    public static ArrayList<String> mergeSentences(ArrayList<String> ls) {
        if (ls == null || ls.size() == 0) {
            return ls;
        }
        String charPattern = "，。！？";
        ArrayList<String> re = new ArrayList<String>();
        String s1 = "";
        String s2 = "";
        String s3 = "";
        for (int i = 0; i <= ls.size() - 2; i +=1) {
            s1 = ls.get(i);
            s2 = ls.get(i+1);
            char[] chars1 = s1.toCharArray();
            char[] chars2 = s2.toCharArray();

            if (!isStringContainsNumber(s1)
                    && !isStringContainsNumber(s2)
                    && s1.length() < 50
                    && s2.length() < 50
                    && s1.length() >= 1
                    && s2.length() >= 1) {
                if (charPattern.contains(new String(new char[]{chars1[chars1.length - 1]}))){
                    chars1[chars1.length - 1] = '，';
                    s3 = new String(chars1) + new String(chars2);
                } else {
                    s3 = new String(chars1) + ","+ new String(chars2);
                }
                if (i == ls.size() - 2) {
                    re.add(s3);
                } else {
                    ls.set(i+1, s3);
                    s3 = new String();
                }
            } else {
                if (i == ls.size() - 2) {
                    re.add(s1);
                    re.add(s2);
                } else {
                    re.add(s1);
                }
            }
        }
        return re;
    }
}
