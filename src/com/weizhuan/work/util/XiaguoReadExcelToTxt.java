package com.weizhuan.work.util;

//import bean.ExcelMode;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.weizhuan.work.bean.ExcelMode;
import com.weizhuan.work.simhash.SimhashUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XiaguoReadExcelToTxt {

    public static String mSaveExcelPath = "/Users/dxm/Desktop/软件开发/Test/测试.xlsx";
    public static String mSaveTxtPath = "/Users/dxm/Desktop/软件开发/Test/存数据.txt";
    public static String mSavePicPath = "";

    public static int mColumnSize = 300;
    public static int prefix = -2;

    public static int mRowSize = 300;

    public static boolean isFirstTime = true;

    public static String FIRST_PART_PATH = TxtUtils.WEI_ZHUAN_PREFIX + "config\\first_part_config.txt";
    public static String LAST_PART_PATH = TxtUtils.WEI_ZHUAN_PREFIX + "config\\last_part_config.txt";
    public static String FIRST_LAST_PART_SWITCH_PATH = TxtUtils.WEI_ZHUAN_PREFIX + "config\\first_last_part_switch_config.txt";
    public static ArrayList<String> mFirstPartList = new ArrayList<>();
    public static ArrayList<String> mLastPartList = new ArrayList<>();
    public static boolean isFirstPart = false;
    public static boolean isFirstPic = false;
    public static boolean isLastWordPart = false;
    public static boolean isOtherPic = false;


    public static ArrayList<String> readExcelToTxt() {
        String excelData = simpleRead();
        ArrayList<String> ls = func(excelData);
        return ls;
    }

    public static void readFirstLastPartSwitchPath(String path) {
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
            if (line != null && !"".equals(line)) {
                isFirstPart = line.contains("1") ? true : false;
            }
//            if (line != null) {
//                line = br.readLine();
//                if (line != null && !"".equals(line)) {
//                    isFirstPic = line.contains("1")  ? true : false;
//                }
//            }
            if (line != null) {
                line = br.readLine();
                if (line != null && !"".equals(line)) {
                    isLastWordPart = line.contains("1")  ? true : false;
                }
            }
            if (line != null) {
                line = br.readLine();
                if (line != null && !"".equals(line)) {
                    isOtherPic = line.contains("1") ? true : false;
                }
            }
            if (line != null) {
                line = br.readLine();
                if (line != null && !"".equals(line)) {
                    SimhashUtil.mSwitch = line.contains("1") ? true : false;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFirstPartPath(String path) {
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
//                mReadPaperPath = line;
                mFirstPartList.add(line);
            }
            line = br.readLine();
            while (line != null) {
//                mSaveDataNameList.add(line);
//                line = br.readLine(); // 一次读入一行数据
                mFirstPartList.add(line);
                line = br.readLine();
            }
//            if (line != null) {
//                line = br.readLine();
//                mFirstPartList.add(line);
////                MIN_LEN = Integer.parseInt(line);
//            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readLastPartPath(String path) {
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
//                mReadPaperPath = line;
                mLastPartList.add(line);
            }
            line = br.readLine();
            while (line != null) {
//                mSaveDataNameList.add(line);
//                line = br.readLine(); // 一次读入一行数据
                mLastPartList.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String mMark = "";
    public static int mMarkIndex = -1;
    public static int mRandomStep = 1;

    public static String simpleRead() {
        // 读取 excel 表格的路径
        if(mSaveExcelPath == null || "".equals(mSaveExcelPath)) {
            return "";
        }
        try {
            // sheetNo --> 读取哪一个 表单
            // headLineMun --> 从哪一行开始读取( 不包括定义的这一行，比如 headLineMun为2 ，那么取出来的数据是从 第三行的数据开始读取 )
            // clazz --> 将读取的数据，转化成对应的实体，需要 extends BaseRowModel
            Sheet sheet = new Sheet(1, 0, ExcelMode.class);

            // 这里 取出来的是 ExcelModel实体 的集合
            List<Object> readList = EasyExcelFactory.read(new FileInputStream(mSaveExcelPath), (Sheet) sheet);
            // 存 ExcelMode 实体的 集合
            List<ExcelMode> list = new ArrayList<ExcelMode>();
            for (Object obj : readList) {
                list.add((ExcelMode) obj);
            }
            // 取出数据
            StringBuilder result = new StringBuilder();

            ExcelMode[] excelMode = new ExcelMode[mRowSize];
            for (int i = 0; i < mRowSize && i < list.size(); i++) {
                excelMode[i] = list.get(i);
            }
            for (int i = 0; i < mColumnSize; i++) {
                String[] columnData = new String[mRowSize];
                String data = "";
                for (int j = 0; j < mRowSize && j < list.size(); j++) {
                    columnData[j] = excelMode[j].getNext();
                }
                int maxRange = -1;

                // 判断是否是之前已经选取的数据 如：*2
                // 如果是的话 跳过找maxRange的过程
                if (mMarkIndex >= 0 && mMarkIndex < mRowSize && columnData[mMarkIndex] != null
                        && mMark.equals(columnData[mMarkIndex])) {

                } else {
                    mMarkIndex = -1;
                    mMark = "";
                    mRandomStep = 1;
                    for (int step = 2; step <= 10; step++) {
                        String mark = "*" + step;
                        // 1*30-2=28
                        int markIndex = (step - 1) * 30 + prefix;
                        if (columnData[markIndex] != null && mark.equals(columnData[markIndex])) {
                            maxRange = step;
                        }
                    }

                    // maxRange 是 2、3、4、5、6、7、8、9、10
                    // 随机取一个数 框定一个取数据的范围
                    if (maxRange != -1) {
                        // randomNum 是 1、2、3、4、5、6、7、8、9、10
                        int randomNum = produceRandomInt(maxRange) + 1;
//                     int randomNum = 3;
                        // randomNum 是 2、3、4...
                        if (randomNum != 1) {
                            mMark = "*" + randomNum;
                            mMarkIndex = (randomNum - 1) * 30 + prefix;
                            mRandomStep = randomNum;
                        } else if (randomNum == 1) {
                            mMark = "*" + randomNum;
                            mMarkIndex = 27;
                            mRandomStep = 1;
                        }
                    }
                }

                // 根据mRandomStep，计算produceDataIndex，然后拿数据
                if (mRandomStep != 1) {
                    int count = 0;
                    while (data == null || "".equals(data) || "*".equals(data)) {
                        int produceDataIndex = produceRandomInt(29) + (mRandomStep - 1) * 30 - 1;
                        data = columnData[produceDataIndex];
                        if (count >= 1000) {
                            break;
                        }
                        count++;
                    }
                    if (data != null && !data.equals("*")) {
                        if ("\n".equals(data)) {
                            result.append("\r\n");
                        } else {
                            result.append(data);
                        }
                    }
                } else {
                    int count = 0;
                    while (data == null || "".equals(data) || "*".equals(data)) {
                        int produceDataIndex = produceRandomInt(27);
                        data = columnData[produceDataIndex];
                        if (count >= 1000) {
                            break;
                        }
                        count++;
                    }
                    if (data != null && !data.equals("*")) {
                        if ("\n".equals(data)) {
                            result.append("\r\n");
                        } else {
                            result.append(data);
                        }
                    }
                }
            }
            result.append("\r\n");
            return result.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void createFile() {
        try {
            File file = new File(mSaveTxtPath);
            if (!file.exists()) {
                file.createNewFile();
                writerErrorFileContent("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveDataToTxt(String data) {
        try {
            /* 写入Txt文件 */
            File saveDataName = new File(mSaveTxtPath);
            saveDataName.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(saveDataName));
//            out.write("我会写入文件啦\r\n"); // \r\n即为换行
            out.write(data); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> func(String data) {
        if ("".equals(data)) {
            return new ArrayList<String>();
        }
        ArrayList<String> re = new ArrayList<>();
        String[] lines = data.split("\r\n");
        for (int i = 0; i < lines.length; i++) {
            re.add(lines[i]);
        }
        return re;
    }

    public static void wordChaseData(String data) {
        try {
            createFile();
            FileInputStream fileInputStream = new FileInputStream(mSaveTxtPath);//读取目标文件路径
            XWPFDocument docx = new XWPFDocument(fileInputStream);

            FileOutputStream out = new FileOutputStream(mSaveTxtPath);

            String[] lines = data.split("\r\n");
            for (int i = 0; i < lines.length - 1; i++) {
                XWPFParagraph par = docx.createParagraph();
                XWPFRun run = par.createRun();
                if (lines[i].contains("#")) {
                    lines[i] = lines[i].substring(0, lines[i].indexOf("#"));
                    run.setBold(true);
                }
                System.out.println("tomcat line" + i + " :" + lines[i]);
                run.setText(lines[i]);
                run.setFontSize(13);
                run.setCapitalized(true);
            }

            docx.write(out);
            out.close();
            System.out.println("sucess!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertPic() {
        try {
            createFile();
            FileInputStream fileInputStream = new FileInputStream(mSaveTxtPath);//读取目标文件路径
            XWPFDocument docx = new XWPFDocument(fileInputStream);
            XWPFParagraph par = docx.createParagraph();
            XWPFRun run = par.createRun();
//            InputStream pic = new FileInputStream("/Users/dxm/Desktop/软件开发/word/pic.jpeg");//插入图片路径1
            InputStream pic = new FileInputStream(mSavePicPath);//插入图片路径1
            byte[] picbytes = IOUtils.toByteArray(pic);
            run.addPicture(new ByteArrayInputStream(picbytes), Document.PICTURE_TYPE_GIF, "", 6061200, 3858000);// 宽  高
            FileOutputStream out = new FileOutputStream(mSaveTxtPath);

            docx.write(out);
            out.close();
            pic.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //写入txt 内容不被覆盖 追加写入
    public static boolean filechaseWrite(String Content) {
        boolean flag = false;
        try {
            FileWriter fw = new FileWriter(mSaveTxtPath, true);
            fw.write(Content);
            fw.flush();
            fw.close();
            flag = true;
        } catch (Exception e) {
            //
            e.printStackTrace();
        }
        return flag;
    }

    private static void writerErrorFileContent(String content) {
        XWPFDocument document = new XWPFDocument();
        OutputStream stream = null;
        BufferedOutputStream bufferStream = null;
        try {
            stream = new FileOutputStream(mSaveTxtPath);
            bufferStream = new BufferedOutputStream(stream, 1024);
//            // 创建一个段落
//            XWPFParagraph p1 = document.createParagraph();
//            // 设置居中
//            p1.setAlignment(ParagraphAlignment.CENTER);
//            XWPFRun r1 = p1.createRun();
//            // 是否加粗
//            r1.setBold(true);
//            // 与下一行的距离
//            r1.setTextPosition(30);
//            // 字体大小
//            r1.setFontSize(18);// 字体大小
            // 增加换行
//            r1.addCarriageReturn();
            // 创建第二个段落
//            XWPFParagraph p2 = document.createParagraph();
//            XWPFRun r2 = p2.createRun();
//            r2.setText(content);
//            r2.addCarriageReturn();
            // 设置字体
//            r2.setFontFamily("仿宋");
//            r2.setFontSize(14);// 字体大小
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


    private static int produceRandomInt(int num) {
        Random r = new Random();
        int i = r.nextInt(num);
        return i;
    }
}
