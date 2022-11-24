package com.weizhuan.work.function;

import com.weizhuan.work.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainProcess {

    public int mLoopTimes = 0;
    public String mType = "";

    public static void main(String[] args) throws Exception {
        List<String> ls = new ArrayList<>();
        ls.add("自媒体");
        ls.add("抖音运营");
        MainProcess mainProcess = new MainProcess();
        mainProcess.startProcess(ls, "1");
    }

    public static String get4RandomsString() {
        String s = "";
        for (int i = 0; i < 4; i++) {
            int j = getRandomNum(10);
            s += j;
        }
        return s;
    }

    public static int getRandomNum(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }

    public void startProcess(List<String> ls, String type) throws Exception {
        mType = type;
        if (mLoopTimes >= 4) {
            System.out.println("tomcat xunhuan time over 3 times");
            return;
        }
        System.out.println("tomcat xunhuan time :"+ mLoopTimes);
//        List<String> ls = new ArrayList<>();
//        ls.add("母婴店怎么用短视频做推广111");
//        ls.add("母婴店怎么用短视频做推广222rrr");
//        ls.add("母婴店怎么用短视频做推广333aaa");

        String specialTag = "8009";
//        String specialTag = get4RandomsString();
        List<String> needProduceList = new ArrayList<>();
        Set<String> needProduceSet = new HashSet<>();
        for (String s: ls) {
            needProduceList.add(s+".txt");
            needProduceSet.add(s+".txt");
        }

        // 0 生成文件夹
        String qianzhuiPath = "D:\\weizhuan\\test\\test_" + specialTag;
//        String path = "\\file\\test\\test_" + specialTag;
//        String path = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\test\\test_" + specialTag;
        File directory = new File(qianzhuiPath);
        boolean hasSucceeded = directory.mkdir();
        System.out.println("创建文件夹结果：" + hasSucceeded);

//        System.out.println("tomcat path:"+csvPath);
//
//        // 1 生成CSV 文件
//        TestCSV.startProcess(ls, qianzhuiPath);
        // 2 走python脚本 生产 txt文件
        Set<String> notProductSet = pythonProduceTxt(qianzhuiPath,  needProduceSet);
        while (notProductSet.size() != 0) {
            if (mLoopTimes >= 4) {
                break;
            }
            mLoopTimes++;
            notProductSet = pythonProduceTxt(qianzhuiPath, notProductSet);
        }
        if (mLoopTimes >= 4 && notProductSet.size() != 0) {
            // 准备退出,说明生成失败
            System.out.println("tomcat mLoopTimes >= 4 notProductSet.size() != 0 ");
        }

        System.out.println("tomcat out of circle");
        String youhuaPath = qianzhuiPath +"\\test";
          // 3 查重和优化代码
        Main.startProcess(youhuaPath);

          // 4 打包成zip文件
        ZipUtils.startProcess(youhuaPath);
    }

    public Set<String> pythonProduceTxt(String path, Set<String> needProduceSet) throws InterruptedException, IOException {
        String csvPath = path + File.separator + "test.csv";
//        PiLiangShengCheng.startProcess(csvPath);

        String youhuaPath = path +"\\test";

        System.out.println("tomcat start");

        Set<String> notProductSet = new HashSet<>();
        Set<String> haveProductSet = new HashSet<>();
        int times = 0;
        boolean produceFlag = true;
        while (true) {
            Thread.sleep(5000);
//            Thread.sleep(30000);
            File file = new File(youhuaPath);
            if (file == null) {
                continue;
            }
            File[] files = file.listFiles();

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
