package com.weizhuan.work.function;

import com.weizhuan.work.Main;

import java.io.*;
import java.util.*;

public class MainProcess {

    public int mLoopTimes = 0;
    public String mType = "";

    public String mQianZhuiPath = "";

    List<String> needProduceList = new ArrayList<>();
    Set<String> needProduceSet = new HashSet<>();

    public String mChachongPath = "";

    public String mChachongResultPath = "";

    // key = name, value = name.txt
    public Map<String, String> mNameMap = new HashMap<>();

    public Map<String, String> mNamePathMap = new HashMap<>();

    public String specialTag = "";

    public static void main(String[] args) throws Exception {
        List<String> ls = new ArrayList<>();
        ls.add("apph5打开微信小程序");
        ls.add("app带参数跳转小程序");
        MainProcess mainProcess = new MainProcess();
        mainProcess.startProcess(ls, "1");
    }

    public String get10RandomsString() {
        String s = "";
        for (int i = 0; i < 10; i++) {
            int j = getRandomNum(10);
            s += j;
        }
        return s;
    }

    public int getRandomNum(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }

    public boolean startProcess(List<String> ls, String type) throws Exception {
        mType = type;
        if (mLoopTimes >= 4) {
            System.out.println("tomcat xunhuan time over 3 times");
            return true;
        }
        System.out.println("tomcat xunhuan time :"+ mLoopTimes);

//        String specialTag = "1234";
        if ("".equals(specialTag)) {
            specialTag = get10RandomsString();
        }
        for (int i = 0; i < ls.size(); i++) {
            String s = ls.get(i);
            if (s.contains(".txt")) {
                int index = s.indexOf(".txt");
                s = s.substring(0, index);
                ls.set(i, s);
            }
        }
        needProduceList.clear();
        needProduceSet.clear();

        for (String name: ls) {
            String txtName = name + ".txt";
            needProduceList.add(txtName);
            needProduceSet.add(txtName);
            mNameMap.put(name, txtName);
        }

        // 0 生成文件夹
        mQianZhuiPath = "D:\\weizhuan\\test\\test_" + specialTag;
//        String path = "\\file\\test\\test_" + specialTag;
//        String path = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\test\\test_" + specialTag;
        File directory = new File(mQianZhuiPath);
        boolean hasSucceeded = directory.mkdir();
        System.out.println("创建文件夹结果：" + hasSucceeded);

//        System.out.println("tomcat path:"+csvPath);
//
//        // 1 生成CSV 文件
        TestCSV.startProcess(ls, mQianZhuiPath);
        // 2 走python脚本 生产 txt文件
        Set<String> notProductSet = pythonProduceTxt(mQianZhuiPath,  needProduceSet);
        while (notProductSet.size() != 0) {
            if (mLoopTimes >= 4) {
                break;
            }
            mLoopTimes++;
            notProductSet = pythonProduceTxt(mQianZhuiPath, notProductSet);
        }
        if (mLoopTimes >= 4 && notProductSet.size() != 0) {
            // 准备退出,说明生成失败
            System.out.println("tomcat mLoopTimes >= 4 notProductSet.size() != 0 ");
        }
        System.out.println("tomcat out of circle");

          // 3 查重
        String youhuaPath = mQianZhuiPath +"\\test";
        System.out.println("tomcat source path:"+youhuaPath);
        List<String> unSurvivalList = chachong(youhuaPath);
        if (unSurvivalList.size() == 0) {
            System.out.println("tomcat unsurvival list size == 0");
        } else {
            mLoopTimes++;
            System.out.println("tomcat prepare start process again");
            startProcess(unSurvivalList, type);
        }
        System.out.println("tomcat 333333 out");
        if (mLoopTimes >= 4) {
            return true;
        }

        // 4 优化代码
        // a 复制
        String path1 = mQianZhuiPath + "\\test_chachong_result";
        String path2 = mQianZhuiPath +"\\test_youhua";
        copyFolder(path1, path2);
//         b 优化
        Main.startProcess(path2);
//         c 将 /test_youhua 结果 复制到 /test_youhua_result里面
        String path3 = mQianZhuiPath +"\\test_youhua_result";
        copyFolder(path2, path3);
//         d
        List<String> youhuaUnSurvivalList = getYouhuaUnSurvivalList();
        if (youhuaUnSurvivalList.size() == 0) {
            System.out.println("tomcat youhua unsurvival list size == 0");
        } else {
            mLoopTimes++;
            startProcess(youhuaUnSurvivalList, type);
        }

        if (mLoopTimes >= 4) {
            return true;
        }


        String youhuaresultPath = mQianZhuiPath + "\\test_youhua_result";
        File processDirFile = new File(youhuaresultPath);
        File[] processDirFiles = processDirFile.listFiles();
        for (int i = 0; i < processDirFiles.length; i++) {
            String fileName = processDirFiles[i].getName();
            System.out.println("tomcat fileName:"+fileName);
            String path = processDirFiles[i].getAbsolutePath();
            System.out.println("tomcat path:"+path);
            mNamePathMap.put(fileName, path);
        }
        return true;
    }

    public void deleteDirFiles(String path) {
        File dir = new File(path);
        if (dir == null) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        int len = files.length;
        for (int i = len - 1; i >= 0; i--) {
            if (files[i].exists()) {
                files[i].delete();
            }
        }
    }

    public List<String> chachong(String sourcePath) throws Exception {
        // 1 复制 test --> test_chachong
        copyPath2Chachong(sourcePath);
        // 2 删除text路径下面的文章
        deleteDirFiles(sourcePath);
        // 2 在D:\weizhuan\去重\处理 新建txt文本 里面写上 text_chachong目录
        insertContent2ChachongPath(sourcePath);
        // 3 轮询
        String chachongEndFlag = mQianZhuiPath + "\\chachong_end_flag";
        List<String> unSurvivalList = new ArrayList<>();
        while (true) {
            try {
//                Thread.sleep(30000);
                Thread.sleep(5000);
            } catch (Exception e) {

            }
            File file = new File(chachongEndFlag);
            if (file.exists()) {
                unSurvivalList = getUnSurvivalList();
                //            file.delete();
                break;
            } else {

            }
        }
        return unSurvivalList;
    }

    private List<String> getYouhuaUnSurvivalList() throws Exception {
        List<String> unSurvivalList = new ArrayList<>();
        // 将test_chachong文件夹下面的文件 复制 到 test_chachong_result里面
//        copyFolder(mChachongPath, mChachongResultPath);
        String path = mQianZhuiPath + "\\test_youhua_result";
        File processDirFile = new File(path);
        File[] processDirFiles = processDirFile.listFiles();
        if (processDirFiles == null) {
            return new ArrayList<String>();
        }
        int processDirFilesLen = processDirFiles.length;
        Set<String> resultSet = new HashSet<>();
        for (int i = 0; i < processDirFilesLen; i++) {
            String fileName = processDirFiles[i].getName();
            resultSet.add(fileName);
        }

        for (String name: needProduceSet) {
            System.out.println("tomcat need produce:"+name);
            if (resultSet.contains(name)) {

            } else {
                unSurvivalList.add(name);
            }
        }

        return unSurvivalList;
    }

    private List<String> getUnSurvivalList() throws Exception {
        List<String> unSurvivalList = new ArrayList<>();
            // 将test_chachong文件夹下面的文件 复制 到 test_chachong_result里面
        copyFolder(mChachongPath, mChachongResultPath);
        File processDirFile = new File(mChachongResultPath);
        File[] processDirFiles = processDirFile.listFiles();
        int processDirFilesLen = processDirFiles.length;
        Set<String> resultSet = new HashSet<>();
        for (int i = 0; i < processDirFilesLen; i++) {
            String fileName = processDirFiles[i].getName();
            resultSet.add(fileName);
        }

        for (String name: needProduceSet) {
            System.out.println("tomcat need produce:"+name);
            if (resultSet.contains(name)) {

            } else {
                unSurvivalList.add(name);
            }
        }

        return unSurvivalList;
    }

    public void insertContent2ChachongPath(String sourcePath) {
        String dirPath = "D:\\weizhuan\\去重\\处理";
        System.out.println("tomcat sorcepath~~~~:"+sourcePath);
        String[] ss = sourcePath.split("\\\\");
        String processPath = "";
        // txtName = test_1234.txt
        String txtName = "";
        for (int i = 0; i < ss.length; i++) {
            if (i == ss.length - 2) {
                txtName = ss[i] + ".txt";
            }
            if (i == ss.length - 1) {
                processPath += "test_chachong";
            } else {
                processPath += ss[i] +"\\\\";
            }
        }
        String txtNamePath = dirPath+"\\"+txtName;
        //  processPath = D:\\weizhuan\\test\\test_1234\\test_chachong
        insertContent2Path(txtNamePath, processPath);
    }

    public void insertContent2Path(String path, String content) {
//        if (insertList == null || "".equals(path)) {
//            return;
//        }
//        if (insertList.size() == 0) {
//            insertList.add("");
//        }
        try {
            File file = new File(path);

            // if file doesn't exist, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
//            for (String s: insertList) {
//                if (s.contains("$")) {
////                    s = s.substring(0, s.indexOf("$"));
//                    s = s.replace("$","");
//                }
                out.write(content);
//                out.write("\r\n");
//            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyPath2Chachong(String sourcePath) throws Exception {
        String[] ss = sourcePath.split("\\\\");
        String targetPath = "";
        for (int i = 0; i < ss.length; i++) {
            if (i == ss.length - 1) {
                targetPath += "test_chachong";
            } else {
                targetPath += ss[i] +"\\";
            }
        }
        System.out.println("tomcat target path:" + targetPath);
        mChachongPath = targetPath;
        mChachongResultPath = mChachongPath + "_result";
        copyFolder(sourcePath, targetPath);
    }

    public void copyFolder(String sourcePath,String targetPath) throws Exception {
        //源文件夹路径
        File sourceFile = new File(sourcePath);
        //目标文件夹路径
        File targetFile = new File(targetPath);

        if (!sourceFile.exists()) {
            throw new Exception("文件夹不存在");
        }
        if (!sourceFile.isDirectory()) {
            throw new Exception("源文件夹不是目录");
        }
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        if (!targetFile.isDirectory()) {
            throw new Exception("目标文件夹不是目录");
        }

        File[] files = sourceFile.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            //文件要移动的路径
            String movePath = targetFile+File.separator+file.getName();
            if (file.isDirectory()){
                //如果是目录则递归调用
                copyFolder(file.getAbsolutePath(), movePath);
            } else {
                //如果是文件则复制文件
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(movePath));

                byte[] b = new byte[1024];
                int temp = 0;
                while ((temp = in.read(b)) != -1) {
                    out.write(b,0,temp);
                }
                out.close();
                in.close();
            }
        }
    }

    public Set<String> pythonProduceTxt(String path, Set<String> needProduceSet) throws InterruptedException, IOException {
        String csvPath = path + File.separator + "test.csv";
        PiLiangShengCheng.startProcess(csvPath);

        String youhuaPath = path +"\\test";

        System.out.println("tomcat start");

        Set<String> notProductSet = new HashSet<>();
        Set<String> haveProductSet = new HashSet<>();
        int times = 0;
        boolean produceFlag = true;
        while (true) {
            Thread.sleep(60000);
//            Thread.sleep(30000);
            File file = new File(youhuaPath);
            if (file == null) {
                continue;
            }
            File[] files = file.listFiles();
            if (files == null) {
                continue;
            }

            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                haveProductSet.add(fileName);
            }
            for (String s:needProduceSet) {
                if (haveProductSet.contains(s)) {

                } else {
                    produceFlag = false;
                }
            }
            if (produceFlag) {
                System.out.println("tomcat success");
                break;
            } else {
                produceFlag = true;
                System.out.println("tomcat failure");
                if (times >= 3) {
                    System.out.println("tomcat times >= 3");
                    break;
                }
                times++;
            }
        }
        System.out.println("tomcat end");
        for (String s: needProduceSet) {
            if (haveProductSet.contains(s)) {

            } else {
                notProductSet.add(s);
            }
        }
        return notProductSet;
    }
}
