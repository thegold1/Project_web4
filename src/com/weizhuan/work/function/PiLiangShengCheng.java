package com.weizhuan.work.function;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.*;



	import java.io.IOException;

public class PiLiangShengCheng {

	public static void startProcess(String path, String type) throws IOException, InterruptedException {
		testExecCmd(path,type);
	}

//	public static void main(String[] args) throws IOException, InterruptedException {
//		testExecCmd();
//	}

	public static void testExecCmd(String path, String type) throws IOException, InterruptedException {
//		String cmd = "cmd /c start /b C:/Users/Administrator/Desktop/hello2.bat";
//		String cmd = "cmd /c start C:/Users/Administrator/Desktop/hello5.bat ";
		String cmd = "";
		if ("自媒体".equals(type)) {
			cmd = "cmd /c start D:/weizhuan_web/zimeiti.bat ";
			System.out.println("tomcat 自媒体");
		} else if ("短链".equals("type")) {
			cmd = "cmd /c start D:/weizhuan_web/duanlian.bat ";
			System.out.println("tomcat 短链");
		} else {
			cmd = "cmd /c start D:/weizhuan_web/zimeiti.bat ";
		}
//		String cmd = "cmd /c start C:/Users/Administrator/Desktop/hello5.bat ";
//		String cmd = "cmd /c start /b C:/Users/Administrator/Desktop/hello5.bat ";
//		System.out.println("tomcat cmd:"+cmd);

		String newPath = produceNewPath(path);
		cmd += newPath;
		System.out.println("tomcat cmd:" + cmd);


		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();
	}

	public static String produceNewPath(String path) {
		if ("".equals(path)) {
			return "";
		}
		String newPath = "";
		String[] ss = path.split("\\\\");
		for (int i = 0; i < ss.length; i++) {
			if (i != ss.length - 1) {
				newPath += ss[i] +"/";
			} else {
				newPath += ss[i];
			}
		}
		return newPath;
	}
}