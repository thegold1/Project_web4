package com.weizhuan.work.util;

import com.weizhuan.work.PicUtils;
import com.weizhuan.work.util.TxtUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.ArrayList;

public class WordUtils {

    public static String PAPER_WORD_SAVE_PATH = TxtUtils.WEI_ZHUAN_PREFIX + "config\\paper_word_save_path.txt";
    public static String mPrefix = TxtUtils.WEI_ZHUAN_PREFIX + "test2\\";

    public static void readWordSaveConfig(String path) {
        try {
            File file = new File(path);
//            ArrayList<String> ls = new ArrayList<String>();

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }


            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), TxtUtils.mCharSet);

            BufferedReader br = new BufferedReader(reader);

            String line = "";
            line = br.readLine();
            mPrefix = line;
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String makePath(String path) {
        if ("".equals(path)) {
            return "";
        }
        String[] tmp = path.split("\\\\");
        String s = mPrefix + tmp[tmp.length - 1];
        return s;
    }

    public static void createFile(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                writerErrorFileContent(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void wordChaseData(String path , ArrayList<String> ls, boolean isFirstTime) {
        try {
            path = path.replace("txt","doc");
            path = makePath(path);

            PicUtils.mSaveTxtPathMap.put(Thread.currentThread().getName(), path);

            if (isFirstTime) {
                File myObj = new File(path);
                myObj.delete();
            }

            createFile(path);
            FileInputStream fileInputStream = new FileInputStream(path);//读取目标文件路径
            XWPFDocument docx = new XWPFDocument(fileInputStream);
            FileOutputStream out = new FileOutputStream(path);

            for (int i = 0; i < ls.size(); i++) {
                String s = ls.get(i);
                XWPFParagraph par = docx.createParagraph();
                XWPFRun run = par.createRun();
                if (s.contains("$")) {
                    String[] ss = s.split("\\$");
                    int count = 1;
                    for (String s1: ss) {
                        if (count == 1) {
                            run.setText(s1);
                            run.setFontSize(13);
                            run.setBold(true);
                        } else {
                            run = par.insertNewRun(1); //insert new run for the '@' as the replacement for the 'a'
                            run.setText(s1, 0);
                            run.setFontSize(13);
//                            run.setColor("DC143C");
                        }
                        count++;
                    }
                } else {
                    run.setText(s);
                    run.setFontSize(13);
                }
//                run.setCapitalized(true);
            }

            docx.write(out);
            docx.close();
            out.close();
            fileInputStream.close();
            System.out.println("sucess!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writerErrorFileContent(String path) {
        XWPFDocument document = new XWPFDocument();
        OutputStream stream = null;
        BufferedOutputStream bufferStream = null;
        try {
            stream = new FileOutputStream(path);
            bufferStream = new BufferedOutputStream(stream, 1024);
            document.write(stream);
            stream.close();
            bufferStream.close();
        } catch (Exception ex) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferStream != null) {
                try {
                    bufferStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
