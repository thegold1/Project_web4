package com.weizhuan.work.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<String> readLines(File file) {
        List<String> re = new ArrayList<>();
        BufferedReader br = null;
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            re.add(line);
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if (line != null) {
//                    System.out.println("tomcat line:"+line);
                    re.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        } finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return re;
    }

}
