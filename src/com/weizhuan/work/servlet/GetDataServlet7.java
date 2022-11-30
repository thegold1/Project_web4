package com.weizhuan.work.servlet;

import com.weizhuan.work.bean.PaperBean;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GetDataServlet7 extends HttpServlet {

	public static String name = "";
	// ServletException, IOException
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
//		String data = new String(request.getParameter("email").getBytes("iso-8859-1"), "utf-8");
		String data = new String(request.getParameter("content"));
//		String data = request.getParameter("email");
//		System.out.println("email:"+data);
		List<String> ls = new ArrayList<>();
		String[] datas = data.split("~");
		for (String s: datas) {
			ls.add(s);
		}
		String type = new String(request.getParameter("type"));
		System.out.println("tomcat type2:"+type);
//		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
//		String data1 = "银魂";
//		writer.write(data1);
		PaperBean b = new PaperBean();
		b.setResult(1);
		PaperBean.Data data1 = new PaperBean.Data();
		data1.setTitle("第一");
		data1.setContent("打卡机快递费");

		PaperBean.Data data2 = new PaperBean.Data();
		data2.setTitle("第二");
		data2.setContent("打卡机快递费2222");

		PaperBean.Data[] data3 = new PaperBean.Data[2];
		data3[0] = data1;
		data3[1] = data2;

		b.setDatas(data3);

		String s = JSONObject.fromObject(b).toString();
		writer.write(s);

//		try {
//			MainProcess mainProcess = new MainProcess();
//			mainProcess.startProcess(ls, type);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}

//		String path = "\\file\\test\\test_" + "9527";
//		String path = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\test\\test_" + "9528";
//		File directory = new File(path);
//		boolean hasSucceeded = directory.mkdir();
//		System.out.println("创建文件夹结果：" + hasSucceeded);

//		String ziyuan = "/file/" + name;
//		String ziyuan = "/file/test_2022_11_19_11_47.zip";
//		String ziyuan = name;
//		System.out.println("tomcat ziyuan path:"+ziyuan);
//		request.getRequestDispatcher(ziyuan).forward(request, response);

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

}
