package com.weizhuan.work.servlet;

import com.weizhuan.work.bean.PaperBean;
import com.weizhuan.work.function.MainProcess;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetDataServlet8 extends HttpServlet {

	public static String name = "";
	// ServletException, IOException
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String data = new String(request.getParameter("content"));

		List<String> ls = new ArrayList<>();
//		String[] datas = data.split("~");
		String[] datas = data.split("\\$");
		for (String s: datas) {
			ls.add(s);
		}
		String type = new String(request.getParameter("type"));
		System.out.println("tomcat type2:"+type);

		MainProcess mainProcess = new MainProcess();
		int times = 0;
		while(true) {
			boolean flag = false;
			try {
				flag = mainProcess.startProcess(ls, type);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (flag || times >= 10) {
				break;
			}
			try {
				times++;
				Thread.sleep(60000);
			} catch (Exception e) {

			}
		}

		int count = 0;

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		PaperBean bean = new PaperBean();
		int nameSize = mainProcess.mNameMap.size();
		int namePathSize = mainProcess.mNamePathMap.size();
		PaperBean.Data[] datass = new PaperBean.Data[nameSize];
		if (nameSize < ls.size() || nameSize < ls.size()) {
			bean.setResult(0);
		} else {
			bean.setResult(1);
			Iterator iterator = mainProcess.mNameMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String,String> elem = (Map.Entry) iterator.next();
				String nameTmp = elem.getKey();
				String nameTxtTmp = elem.getValue();
				String namePathTxt = mainProcess.mNamePathMap.get(nameTxtTmp);
				if ("".equals(nameTmp) || "".equals(nameTxtTmp) || "".equals(namePathTxt)) {
					datass = new PaperBean.Data[nameSize];
					bean.setResult(0);
					break;
				}
				String contentTmp = getPathContent(namePathTxt);
				PaperBean.Data dataTmp = new PaperBean.Data();
				dataTmp.setTitle(nameTmp);
				dataTmp.setContent(contentTmp);
				datass[count] = dataTmp;
				count++;
			}
		}

		bean.setDatas(datass);

		String s = JSONObject.fromObject(bean).toString();
		writer.write(s);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 *
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public static String getPathContent(String path) {
		StringBuilder sb = new StringBuilder();
		try {
			File file = new File(path); // 要读取以上路径的input。txt文件
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(file)); // 建立一个输入流对象reader
			InputStreamReader reader = new InputStreamReader(
//					new FileInputStream(file), "GBK");
//            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
			String line = "";
			line = br.readLine();
			while (line != null) {
				sb.append(line).append("\r\n");
				line = br.readLine(); // 一次读入一行数据
			}
			br.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return sb.toString();
	}
}
