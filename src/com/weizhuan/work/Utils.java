package com.weizhuan.work;

import com.google.gson.Gson;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.LexicalAnalysisRequest;
import com.tencentcloudapi.nlp.v20190408.models.LexicalAnalysisResponse;
import com.tencentcloudapi.nlp.v20190408.models.SimilarWordsRequest;
import com.tencentcloudapi.nlp.v20190408.models.SimilarWordsResponse;
import com.weizhuan.work.bean.SimilarWordBean;
import com.weizhuan.work.bean.SplitWordBean;
import com.weizhuan.work.bean.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    public static List<String> mSaveDataNameList = new ArrayList<String>();

    static NlpClient client;
    public static void init() {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential("AKIDyqVv99vTmqgJispcBYifNq55qPlvugEZ",
                    "QbYGk270f6JI3atxmT0JPGtISO6ZnnY5");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            client = new NlpClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
//            LexicalAnalysisRequest req = new LexicalAnalysisRequest();
////            req.setText("欢迎使用腾讯知文自然语言处理");
//            req.setText(sentence);
//            // 返回的resp是一个LexicalAnalysisResponse的实例，与请求对象对应
//            LexicalAnalysisResponse resp = client.LexicalAnalysis(req);
            // 输出json格式的字符串回包
//            return LexicalAnalysisResponse.toJsonString(resp);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
//        return "";
    }

    public static String splitSentence(String sentence){
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
//            Credential cred = new Credential("AKIDyqVv99vTmqgJispcBYifNq55qPlvugEZ",
//                    "QbYGk270f6JI3atxmT0JPGtISO6ZnnY5");
//            // 实例化一个http选项，可选的，没有特殊需求可以跳过
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("nlp.tencentcloudapi.com");
//            // 实例化一个client选项，可选的，没有特殊需求可以跳过
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
//            // 实例化要请求产品的client对象,clientProfile是可选的
//            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);
            if (client == null) {
                init();
            }
            // 实例化一个请求对象,每个接口都会对应一个request对象
            LexicalAnalysisRequest req = new LexicalAnalysisRequest();
//            req.setText("欢迎使用腾讯知文自然语言处理");
            req.setText(sentence);
            // 返回的resp是一个LexicalAnalysisResponse的实例，与请求对象对应
            LexicalAnalysisResponse resp = client.LexicalAnalysis(req);
            // 输出json格式的字符串回包
            return LexicalAnalysisResponse.toJsonString(resp);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return "";
    }

    public static String getSimilarWords(String word){
        // 相似词
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential("AKIDyqVv99vTmqgJispcBYifNq55qPlvugEZ",
                    "QbYGk270f6JI3atxmT0JPGtISO6ZnnY5");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SimilarWordsRequest req = new SimilarWordsRequest();
            req.setText(word);
            // 返回的resp是一个SimilarWordsResponse的实例，与请求对象对应
            SimilarWordsResponse resp = client.SimilarWords(req);
            // 输出json格式的字符串回包
            return SimilarWordsResponse.toJsonString(resp);
        } catch (TencentCloudSDKException e) {
            return "";
        }
    }

    public static ArrayList<String> parseSplitSentence(String splitSentenceRes){
        String jsonData = splitSentenceRes;

        // 使用Gson解析
        Gson gson = new Gson();
        SplitWordBean data = gson.fromJson(jsonData, SplitWordBean.class);

        ArrayList<Word> words = data.PosTokens;
        ArrayList<String> ls = new ArrayList<String>();
        for (Word info : words) {
            ls.add(info.Word);
        }
        return ls;
    }

    public static ArrayList<String> parseSimilarSentence(String similarSentenceRes){
        String jsonData = similarSentenceRes;
        // 使用Gson解析
        Gson gson = new Gson();
        SimilarWordBean data = gson.fromJson(jsonData, SimilarWordBean.class);

        String[] words = data.SimilarWords;
        ArrayList<String> ls = new ArrayList<String>();
        for (int i = 0;i<words.length;i++) {
            ls.add(words[i]);
        }
        return ls;
    }

    public static List<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
            }
        }
        return files;
    }

    public static String dealSelectedLogic(String s) {
        if (s == null || "".equals(s)) {
            return "";
        }
        String[] res = s.split("\\\\");
        String s1 = res[res.length - 1];
        System.out.println("s1:" + s1);
        String[] res2 = s1.split("\\.");
        return res2[0];
    }

    public static int getRandomNum(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }

    public static String getRandomName(String path) {
        mSaveDataNameList = new ArrayList<String>();
        try {
            File file = new File(path); // 要读取以上路径的input。txt文件
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(file)); // 建立一个输入流对象reader
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file),"GBK");
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(file), "UTF-8");
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                mSaveDataNameList.add(line);
                line = br.readLine(); // 一次读入一行数据
            }
            br.close();
            reader.close();
            if (mSaveDataNameList.size() != 0) {
                String s = mSaveDataNameList.get(getRandomNum(mSaveDataNameList.size()));
                mSaveDataNameList.remove(s);
                return s;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

