package com.weizhuan.work;

import com.weizhuan.work.util.TxtUtils;
import com.weizhuan.work.util.WordUtils;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.*;

public class PicUtils {

//    public static String mSaveTxtPath = "";
    public static Map<String, String> mSaveTxtPathMap = new HashMap<>();
//    public static String mSavePicPath = "";
    public static Map<String, String> mSavePicPathMap = new HashMap<>();
//    public static ArrayList<String> mDirList = new ArrayList<>();
    public static Map<String, ArrayList<String>> mDirListMap = new HashMap<>();
//    public static String mSelectedDir = new String();
    public static Map<String, String> mSelectedDirMap = new HashMap<>();
//    static List<String> mHaveUsedPic = new ArrayList<>();
    public static Map<String, ArrayList<String>> mHaveUsedPicMap= new HashMap<>();
//    public static String mFirstPicPath = "";

    public static String mInsertPicPath = "";

    public static String READ_PIC_PATH = TxtUtils.WEI_ZHUAN_PREFIX + "config\\pic_path.txt";
    public static void readInsertPicPath (String path) {
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
                mInsertPicPath = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void clear() {
        mSaveTxtPathMap.put(Thread.currentThread().getName(), "");
        mSavePicPathMap.put(Thread.currentThread().getName(), "");
        mDirListMap.put(Thread.currentThread().getName(), new ArrayList<>());
        mSelectedDirMap.put(Thread.currentThread().getName(), "");
        mHaveUsedPicMap.put(Thread.currentThread().getName(), new ArrayList<>());
    }

    public synchronized static void createFile() {
        try {
            String path = mSaveTxtPathMap.get(Thread.currentThread().getName());
            if (path == null) {
                path = "";
                mSaveTxtPathMap.put(Thread.currentThread().getName(), path);
            }
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                writerErrorFileContent("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized static void writerErrorFileContent(String content) {
        XWPFDocument document = new XWPFDocument();
        OutputStream stream = null;
        BufferedOutputStream bufferStream = null;
        try {
            String path = mSaveTxtPathMap.get(Thread.currentThread().getName());
            if (path == null) {
                path = "";
                mSaveTxtPathMap.put(Thread.currentThread().getName(), path);
            }
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

    public synchronized static String makePath(String path) {
        if ("".equals(path)) {
            return "";
        }
        String[] tmp = path.split("\\\\");
        String s = WordUtils.mPrefix + tmp[tmp.length - 1];
        return s;
    }

    public static synchronized void insertPic(String path, boolean isFirstTime) {
        try {
            path = path.replace("txt","doc");
            path = makePath(path);

            mSaveTxtPathMap.put(Thread.currentThread().getName(), path);

            if (isFirstTime) {
                File myObj = new File(path);
                myObj.delete();
            }
            createFile();
            String path1 = mSaveTxtPathMap.get(Thread.currentThread().getName());
            if (path == null) {
                path1 = "";
                mSaveTxtPathMap.put(Thread.currentThread().getName(), path1);
            }
            FileInputStream fileInputStream = new FileInputStream(path1);//读取目标文件路径
//            System.out.println("tomcat word path:"+path);
            XWPFDocument docx = new XWPFDocument(fileInputStream);
            XWPFParagraph par = docx.createParagraph();
            XWPFRun run = par.createRun();
//            InputStream pic = new FileInputStream("/Users/dxm/Desktop/软件开发/word/pic.jpeg");//插入图片路径1
            String picPath = mSavePicPathMap.get(Thread.currentThread().getName());
            if (picPath == null) {
                picPath = "";
                mSavePicPathMap.put(Thread.currentThread().getName(), picPath);
            }


            System.out.println("tomcat word path:"+path+"   mSavePicPath:"+picPath);
            InputStream pic = new FileInputStream(picPath);//插入图片路径1
            byte[] picbytes = IOUtils.toByteArray(pic);
            run.addPicture(new ByteArrayInputStream(picbytes), Document.PICTURE_TYPE_GIF, "", 6061200, 3858000);// 宽  高
            FileOutputStream out = new FileOutputStream(path1);

            docx.write(out);
            out.close();
            pic.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void findPicDirList() {
        ArrayList<String> dirList = mDirListMap.get(Thread.currentThread().getName());
        if (dirList == null) {
            dirList = new ArrayList<>();
        }
        dirList.clear();

        String path = TxtUtils.WEI_ZHUAN_PREFIX + "pic";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println("tomcat file:"+files[i].toString());
            String path1 = files[i].toString();
            dirList.add(path1);
        }
        mDirListMap.put(Thread.currentThread().getName(), dirList);
    }

    public static synchronized void matchPicPath(ArrayList<String> dataList) {
//        findPicDirList();
        StringBuilder sb = new StringBuilder();
        for (String s: dataList) {
            sb.append(s);
        }
        String data = sb.toString();
        String splitRes = Utils.splitSentence(data);
        // 分词数据解析
        ArrayList<String> words = Utils.parseSplitSentence(splitRes);

        Map<String,Integer> map = new HashMap<String,Integer>();
        map = saveDataIntoMap(words);
        System.out.println("tomcat --------------------------------------");
        List<Map.Entry<String, Integer>> infoIds = sort(map);
        matchPicDir(infoIds);
    }

    public static synchronized void matchPicDir(List<Map.Entry<String, Integer>> infoIds) {
//        boolean flag = false;
//        for (int i = 0; i < infoIds.size() && !flag; i++) {
//            Map.Entry<String, Integer> id = infoIds.get(i);
////            System.out.println("tomcat 词语:"+id.getKey()+" 次数:"+id.getValue());
//            ArrayList<String> dirList = mDirListMap.get(Thread.currentThread().getName());
//            if (dirList == null) {
//                dirList = new ArrayList<>();
//            }
//            for (String dirPath: dirList) {
//                if (dirPath.contains(id.getKey())) {
//                    System.out.println("tomcat match success:"+id.getKey());
//                    System.out.println("tomcat path:"+dirPath);
//                    String selectedDir = mSelectedDirMap.get(Thread.currentThread().getName());
//                    if (selectedDir == null) {
//                        selectedDir = "";
//                    }
//                    selectedDir = dirPath;
//                    randomSelectOnePicFromDir(selectedDir);
//                    mSelectedDirMap.put(Thread.currentThread().getName(), selectedDir);
//                    flag = true;
//                    break;
//                }
//            }
//        }
//        if (flag == true) {
//            return;
//        }
        String commonPath = mInsertPicPath;
        System.out.println("tomcat 匹配失败 使用通用路径："+commonPath);
        mSelectedDirMap.put(Thread.currentThread().getName(), commonPath);
        randomSelectOnePicFromDir(commonPath);
    }

    public static synchronized void randomSelectOnePicFromDir(String path) {
        List<String> mPicOneFiles5 = new ArrayList<String>();
        getPicFiles(path, mPicOneFiles5);
        ArrayList<String> haveUsedPicList = mHaveUsedPicMap.get(Thread.currentThread().getName());
        if (haveUsedPicList == null) {
            haveUsedPicList = new ArrayList<>();
//            mHaveUsedPicMap.put(Thread.currentThread().getName(), new ArrayList<String>());
        }
        String picPath = getRandomPic(haveUsedPicList, mPicOneFiles5);
        System.out.println("tomcat thread name:"+Thread.currentThread().getName());
        mHaveUsedPicMap.put(Thread.currentThread().getName(), haveUsedPicList);
        mSavePicPathMap.put(Thread.currentThread().getName(), picPath);
    }

    public static synchronized void getPicFiles(String path, List<String> picOneFiles) {
        File file = new File(path);
        File[] tempList = file.listFiles();

        if (tempList == null || tempList.length == 0) {
            return;
        }

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                picOneFiles.add(tempList[i].toString());
            }
        }
    }

    public static synchronized String getRandomPic(List<String> haveUsedPic, List<String> picOneFiles) {
        int len = picOneFiles.size();
        if (len == 0) {
            return "";
        }
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int rNum = random.nextInt(len);
            String path = picOneFiles.get(rNum);
            if (!haveUsedPic.contains(path)) {
                haveUsedPic.add(path);
                return path;
            }
        }
        return "";
    }

    public static synchronized List<Map.Entry<String, Integer>> sort(Map<String,Integer> map) {
        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        return infoIds;
    }

    public static synchronized Map<String, Integer> saveDataIntoMap(ArrayList<String> words) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String word : words) {
            if (map.containsKey(word)) {     //HashMap不允许重复的key，所以利用这个特性，去统计单词的个数
                int count = map.get(word);
                map.put(word, count + 1);     //如果HashMap已有这个单词，则设置它的数量加1
            } else {
                map.put(word, 1);
            }
        }
        return map;
    }
}
