package com.weizhuan.work.servlet;

import com.weizhuan.work.function.MainProcess;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataServlet5 extends HttpServlet {

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
		System.out.println("tomcat type:"+type);


		try {
			MainProcess mainProcess = new MainProcess();
			mainProcess.startProcess(ls, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}



//		String path = "\\file\\test\\test_" + "9527";
//		String path = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\test\\test_" + "9528";
//		File directory = new File(path);
//		boolean hasSucceeded = directory.mkdir();
//		System.out.println("创建文件夹结果：" + hasSucceeded);

		String ziyuan = "/file/" + name;
//		String ziyuan = name;
		System.out.println("tomcat ziyuan path:"+ziyuan);
		request.getRequestDispatcher(ziyuan).forward(request, response);

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
