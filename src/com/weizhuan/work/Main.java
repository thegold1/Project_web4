package com.weizhuan.work;

import com.weizhuan.work.simhash.SimhashUtil;
import com.weizhuan.work.util.TxtUtils;
import com.weizhuan.work.util.WordUtils;
import com.weizhuan.work.util.XiaguoReadExcelToTxt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

// this awesome project was created by th
public class Main {

    public static int count = 1;

    public static void startProcess(String path) throws Exception {
        try {
            TxtUtils.mReadPaperPath = path;
            SimhashUtil.SHENG_CHENG_PATH = path;
            SimhashUtil.SHENG_CHENG_PATH_word2 = path;
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() throws Exception {
        XiaguoReadExcelToTxt.readFirstLastPartSwitchPath(XiaguoReadExcelToTxt.FIRST_LAST_PART_SWITCH_PATH);
        XiaguoReadExcelToTxt.readFirstPartPath(XiaguoReadExcelToTxt.FIRST_PART_PATH);
        XiaguoReadExcelToTxt.readLastPartPath(XiaguoReadExcelToTxt.LAST_PART_PATH);
//        TxtUtils.readPaperPath(TxtUtils.PAPER_PATH_CONFIG);
        TxtUtils.readConfig(TxtUtils.PAPER_MAX_MIN_CONFIG);
        TxtUtils.readReplaceConfig(TxtUtils.PAPER_REPLACEMENT_CONFIG);
        TxtUtils.readHead5CharReplaceConfig(TxtUtils.PAPER_HEAD_5_CHAR_REPLACEMENT_CONFIG);
        WordUtils.readWordSaveConfig(WordUtils.PAPER_WORD_SAVE_PATH);
        PicUtils.findPicDirList();
        PicUtils.readInsertPicPath(PicUtils.READ_PIC_PATH);

        if (SimhashUtil.mSwitch) {
            SimhashUtil.startRemoveDuplicate();
        }

//        File file = new File(TxtUtils.mReadPaperPath);
//        File[] files = file.listFiles();
//        int len = files.length;
//        for (int j = 0; j < len; j++) {
//            String file_path = files[j].toString();
//            System.out.println("tomcat 一共："+len+"篇，当前计数:"+count+"，标题:"+file_path);
//            count++;
//            dealPaper(file_path);
//
//        }
//        System.out.println("tomcat 优化完毕");
    }

    public static void dealPaper(String path) {

        ArrayList<String> ls = TxtUtils.readTxt(path);


        // 去除空格和数字带空格
        ls = TxtUtils.delNumWithSpace(ls);

        // 不以<eod> 结尾的文章，且文章超过600，需要干掉最后25%的内容
        ls = TxtUtils.processWithEOD(ls);

        // 删除多行之间的重复
        for (int i = 0; i< ls.size() - 1; i++) {
            String s1 = ls.get(i);
            String s2 = ls.get(i + 1);
            boolean b = TxtUtils.isStringEqual(s1, s2);
            if (b) {
                ls.remove(i);
                i--;
            }
        }
        // 如果最后一行以逗号结尾，且少于50字，需要删除，否则增加句号。
        String tmp = TxtUtils.dealLastString(ls.get(ls.size() - 1));
        if ("".equals(tmp)) {
            ls.remove(ls.size() - 1);
            if (ls.size() > 0) {
                String tmp2 = TxtUtils.dealLastStringSecond(ls.get(ls.size() - 1));
                if ("".equals(tmp2)) {
                    ls.remove(ls.size() - 1);
                } else {
                    ls.set(ls.size() - 1, tmp2);
                }
            }
        } else {
            ls.set(ls.size() - 1, tmp);
        }
        if (isPaperValid(ls)) {

        } else {
            File myObj = new File(path);
            myObj.delete();
            return;
        }

        if (TxtUtils.isTitleContainSenWords(path)) {
            File myObj = new File(path);
            myObj.delete();
            return;
        }

        ArrayList<String> re = new ArrayList<String>();
        if (ls.size() == 0) {
            return;
        }
//        for (String s: ls) {
//            System.out.println("tomcat before data:"+s);
//        }
        for (int i = 0; i < ls.size(); i++) {
            String s = ls.get(i);
            // 1 清除文本空格
//            s = TxtUtils.removeSpace(s);
            // 2 超过30个字母,or超过30个数字,or超过20个符号,删除
            if (TxtUtils.isExceptionCharOverLimit(s)) {
                ls.remove(i);
                i--;
                continue;
            }
            // 该行只有2个字符，删除
            if("".equals(s) || s.length() <= 2) {
                ls.remove(i);
                i--;
                continue;
            }

            // 3 半角转全角
            s = TxtUtils.dbc2sbcCase(s);
            // 4 错误符号处理 多个符号只保存一个
            s = TxtUtils.removeRedundantSymbol(s);
            // 5 删除包含的敏感词的行
            if (TxtUtils.isStringContainSenWords(s)) {
                ls.remove(i);
                i--;
                continue;
            }
            // 6 删除句子里面重复的话
            s = TxtUtils.removeDuplicateWord2(s);
            // 7 1。xxx --> 2.xxx
            s = TxtUtils.changeChFullStop2En(s);
            // 8 全文替换
            s = TxtUtils.replace(s);
            // 9 前五个字符 替换，有可能会引入新的符号，所以删除多余符号
            s = TxtUtils.head5CharReplace(s);
            s = TxtUtils.removeRedundantSymbol(s);
            // 10 按照句号来分段
            String[] ss = TxtUtils.splitSentence(s);
            //
            for (String data: ss) {
                // 11 一行文字不超过20字，且开头包数字,干掉最后一个符号 1.XXXX. --> 1.XXXX
                data = TxtUtils.deleteStringLastChar(data);
                data = TxtUtils.removeRedundantSymbol(data);
                re.add(data);
            }
            ls.set(i,s);
        }
        re = TxtUtils.delSimilarPara(re);
        re = TxtUtils.makeNumOrder2(re);
        re = TxtUtils.removeOnlyOneBigNumAndSmallNum(re);
        re = TxtUtils.mergeSentences(re);
        for (int i = 0; i < re.size(); i++) {
            String s = re.get(i);
            s = TxtUtils.removeRedundantSymbol(s);
            re.set(i, s);
        }
        re = TxtUtils.addBoldMark(re);
        re = TxtUtils.splitSentenceWithMark(re);
//        for (String s: re) {
//            System.out.println("tomcat data:"+s);
//        }
//        PicUtils.clear();
        if (isPaperValid(re)) {
            ArrayList<String> data = new ArrayList<String>();

            boolean isFirstWord = true;
            // 插入第一部分word
            if (XiaguoReadExcelToTxt.isFirstPart) {
                for (int i = 0; i < XiaguoReadExcelToTxt.mFirstPartList.size(); i++) {
                    String excelOrPicPath = XiaguoReadExcelToTxt.mFirstPartList.get(i);
                    System.out.println("tomcat excelOrPicPath:"+excelOrPicPath);
                    if (excelOrPicPath.contains("xls") || excelOrPicPath.contains("xlsx")) {
                        XiaguoReadExcelToTxt.mSaveExcelPath = XiaguoReadExcelToTxt.mFirstPartList.get(i);
                        ArrayList<String> tmp1 = XiaguoReadExcelToTxt.readExcelToTxt();
                        for (String s : tmp1) {
                            data.add(s);
                        }
                        if (isFirstWord) {
                            WordUtils.wordChaseData(path, tmp1, true);
                            isFirstWord = false;
                        } else {
                            WordUtils.wordChaseData(path, tmp1, false);
                        }
                    } else {
                        PicUtils.randomSelectOnePicFromDir(excelOrPicPath);
                        if (isFirstWord) {
                            PicUtils.insertPic(path, true);
                            isFirstWord = false;
                        } else {
                            PicUtils.insertPic(path, false);
                        }
                    }
                }
            }

            for (String s : re) {
                data.add(s);
            }

            // 插入第二部分word
            ArrayList<ArrayList<String>> re1 = CommonUtils.splitIntoSeveralPieces(re);
            System.out.println("tomcat re1 size:"+re1.size());

            int count = 1;
            for (ArrayList<String> re2 : re1) {
                if (count == 1 && !XiaguoReadExcelToTxt.isFirstPart && isFirstWord) {
                    WordUtils.wordChaseData(path, re2, true);
                    isFirstWord = true;
                } else {
                    WordUtils.wordChaseData(path, re2, false);
                }
                if (XiaguoReadExcelToTxt.isOtherPic && count < re1.size()) {
                    PicUtils.matchPicPath(re2);
                    PicUtils.insertPic(path, false);
                }
                count++;
            }

            // 插入第三部分word
            if (XiaguoReadExcelToTxt.isLastWordPart) {
                for (int i = 0; i < XiaguoReadExcelToTxt.mLastPartList.size(); i++) {
                    String excelOrPicPath = XiaguoReadExcelToTxt.mLastPartList.get(i);
                    System.out.println("tomcat excelOrPicPath:"+excelOrPicPath);
                    if (excelOrPicPath.contains("xls") || excelOrPicPath.contains("xlsx")) {
                        XiaguoReadExcelToTxt.mSaveExcelPath = XiaguoReadExcelToTxt.mLastPartList.get(i);
                        ArrayList<String> tmp1 = XiaguoReadExcelToTxt.readExcelToTxt();
                        for (String s : tmp1) {
                            data.add(s);
                        }
                        WordUtils.wordChaseData(path, tmp1, false);
                    } else {
                        PicUtils.randomSelectOnePicFromDir(excelOrPicPath);
                        PicUtils.insertPic(path, false);
                    }
                }
            }

            // 插入txt
            insertListToPath(path, data);
        } else {
            File myObj = new File(path);
            myObj.delete();
        }
    }

    public static boolean isPaperValid(List<String> insertList) {
        if (insertList == null || insertList.size() == 0) {
            return false;
        }
        boolean delete = false;
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (String s: insertList) {
            sb.append(s);
            if (s.length() <= 15) {
                count++;
                if (count >= 5) {
                    System.out.println("tomcat 连续5行文字小于15个字");
                    delete = true;
                    break;
                }
            } else if (s.length() >= 260){
                System.out.println("tomcat 单行文字超过260 data:"+s);
                delete = true;
            } else {
                count = 0;
            }
        }

        for (String s: insertList) {
            if (delete == true) {
                break;
            }
            char[] cc = s.toCharArray();
            int leftBracketNum = 0;
            int rightBracketNum = 0;
            for (char c: cc) {
                if (c == '【') {
                    leftBracketNum++;
                } else if (c == '】') {
                    rightBracketNum++;
                }
                if (leftBracketNum >= 2 && rightBracketNum >= 2) {
                    System.out.println("tomcat 【】计数大于2");
                    delete = true;
                    break;
                }
            }
        }

        if (delete) {
            return false;
        }
        String tmp = sb.toString();
        if (tmp.length() >= TxtUtils.MAX_LEN) {
            System.out.println("tomcat 整篇文章大于"+TxtUtils.MAX_LEN+"字 data:"+tmp);
            return false;
        } else if (tmp.length() <= TxtUtils.MIN_LEN) {
            System.out.println("tomcat 整篇文章小于"+TxtUtils.MIN_LEN+"字 data:"+tmp);
            return false;
        }
        return true;
    }

    public static void insertListToPath(String path, List<String> insertList) {
        if (insertList == null || "".equals(path)) {
            return;
        }
        if (insertList.size() == 0) {
            insertList.add("");
        }
        try {
            File file = new File(path);

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            for (String s : insertList) {
//                bw.write(s);
//                bw.write("\r\n");
//            }
//            bw.close();
//            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"GBK");
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),TxtUtils.mCharSet);
            for (String s: insertList) {
                if (s.contains("$")) {
//                    s = s.substring(0, s.indexOf("$"));
                    s = s.replace("$","");
                }
                out.write(s);
                out.write("\r\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
