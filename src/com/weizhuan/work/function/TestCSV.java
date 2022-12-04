package com.weizhuan.work.function;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class TestCSV {

    static String mPath = "";
    public static void startProcess(List<String> ls, String path) {
        mPath = path;
        List<PointsParamDto> pointsParamDtos = new ArrayList<PointsParamDto>();
        for (int i = 0; i < ls.size(); i++) {
            String s = ls.get(i);
            pointsParamDtos.add(new PointsParamDto(s, s));
        }
        PointsToCsvFile(pointsParamDtos);
    }

//    public static void main(String[] args) {
//        List<PointsParamDto> pointsParamDtos = new ArrayList<PointsParamDto>();
//        pointsParamDtos.add(new PointsParamDto("母婴店怎么用短视频做推广111","母婴店怎么用短视频做推广11"));
//        pointsParamDtos.add(new PointsParamDto("母婴店怎么用短视频做推广222","母婴店怎么用短视频做推广22"));
//        pointsParamDtos.add(new PointsParamDto("母婴店怎么用短视频做推广333","母婴店怎么用短视频做推广33"));
//        PointsToCsvFile(pointsParamDtos);
//    }

    private static void PointsToCsvFile(List<PointsParamDto> pointsList){
        if (pointsList!=null && pointsList.size() > 0){
            // 表格头
//	        String[] headArr = new String[]{"PointId", "X", "Y"};
            //CSV文件路径及名称
//	        LocalDateTime localDateTime = LocalDateTime.now();
//	        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//            String filePath = "E:\\TestCsvDirectory"; //CSV文件路径
//	        String fileName = "CSV_"+ df.format(localDateTime) +".csv";//CSV文件名称
//            String fileName = "CSV_" + "1.csv";//CSV文件名称
            String fileName = "test.csv";//CSV文件名称
            File csvFile = null;
            BufferedWriter csvWriter = null;
            try {
                csvFile = new File(mPath + File.separator + fileName);
                File parent = csvFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                csvFile.createNewFile();

                // GB2312使正确读取分隔符","
                csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);

                //这部分在第一行居中展示文件名称，根据实际情况，可选择取消注释
	            /*int num = headArr.length / 2;
	            StringBuffer buffer = new StringBuffer();
	            for (int i = 0; i < num; i++) {
	                buffer.append(",");
	            }
	            csvWriter.write(buffer.toString() + fileName + buffer.toString());
	            csvWriter.newLine();*/

                // 写入文件头部标题行
//	            csvWriter.write(String.join(",", headArr));
//	            csvWriter.newLine();

                // 写入文件内容
                for (PointsParamDto points : pointsList) {
                    csvWriter.write(points.toRow());
                    csvWriter.newLine();
                }
                csvWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 坐标点参数实体类
     */
    static class PointsParamDto {
        /**
         * 坐标id(由1开始，累加1，这样的：1,2,3,4,5...)
         */
        private String pointId;

        /**
         * X 坐标点
         */
        private String x;

        /**
         * X 坐标点
         */
        private String y;

        public PointsParamDto(){}

        public PointsParamDto(String pointId,String x){
            this.pointId = pointId;
            this.x = x;
//            this.y = y;
        }

        public String getPointId() {
            return pointId;
        }

        public void setPointId(String pointId) {
            this.pointId = pointId;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

//        public String getY() {
//            return y;
//        }

        public void setY(String y) {
            this.y = y;
        }

        public String toRow(){
            return String.format("%s,%s",this.pointId,this.x);
        }
    }

}
