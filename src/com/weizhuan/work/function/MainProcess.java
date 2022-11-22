package com.weizhuan.work.function;

import com.weizhuan.work.Main;
import com.weizhuan.work.util.TxtUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainProcess {

    public static int xunhuanCount = 0;

    public static void main(String[] args) throws Exception {
//        startProcess();
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

    public static void startProcess(List<String> ls, String type) throws Exception {
        if (xunhuanCount >= 4) {
            System.out.println("tomcat xunhuan time over 3 times");
            return;
        }
//        List<String> ls = new ArrayList<>();
//        ls.add("母婴店怎么用短视频做推广111");
//        ls.add("母婴店怎么用短视频做推广222rrr");
//        ls.add("母婴店怎么用短视频做推广333aaa");

        String specialTag = "9527";
//        String specialTag = get4RandomsString();
        List<String> needProduceList = new ArrayList<>();
        Set<String> needProduceSet = new HashSet<>();
        for (String s: ls) {
            needProduceList.add(s+".txt");
            needProduceSet.add(s+".txt");
        }

        // 0 生成文件夹
        String path = "D:\\weizhuan\\test\\test_" + specialTag;
//        String path = "\\file\\test\\test_" + specialTag;
//        String path = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\test\\test_" + specialTag;
        File directory = new File(path);
        boolean hasSucceeded = directory.mkdir();
        System.out.println("创建文件夹结果：" + hasSucceeded);

        String csvPath = path + File.separator + "test.csv";
//        System.out.println("tomcat path:"+csvPath);
//
//        // 1 生成CSV 文件
        TestCSV.startProcess(ls, path);
        // 2 走python脚本 生产 txt文件
//        PiLiangShengCheng.startProcess(csvPath);

        String youhuaPath = path +"\\test";

        System.out.println("tomcat start");
        boolean flag = true;
        List<String> notProductList = new ArrayList<>();
        List<String> haveProductList = new ArrayList<>();
        int times = 0;
        while (true) {
            Thread.sleep(10000);
            File file = new File(youhuaPath);
            if (file == null) {
                continue;
            }
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                String fileName = files[0].getName();
                if (needProduceSet.contains(fileName)) {
                    haveProductList.add(fileName);
                    System.out.println("tomcat contain name:"+fileName);
                } else {
                    notProductList.add(fileName);
                    System.out.println("tomcat not contain name:"+fileName);
                }
            }
            if (notProductList.size() != 0) {
//                startProcess(notProductList, type);
                flag = false;
                xunhuanCount++;
                break;
            } else {
                if (files.length == ls.size()) {
                    System.out.println("tomcat success");
                    break;
                } else {
                    if (times >= 2) {
                        break;
                    }
                    times++;
                    System.out.println("tomcat failure");
                }
            }
        }
        System.out.println("tomcat end");
        for (String s: needProduceSet) {
            System.out.println("tomcat set data:"+s);
        }
//        if (!flag) {
//            System.out.println("tomcat restart");
//            startProcess(notProductList, type);
//        } else {
//            System.out.println("tomcat not restart");
//        }
//
//          // 3 查重和优化代码
//        Main.startProcess(youhuaPath);
//
//          // 4 打包成zip文件
//        ZipUtils.startProcess(youhuaPath);
    }
}
