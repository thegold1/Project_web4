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
//			System.out.println("tomcat s:"+s);
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

	public final static String getIpAddress(HttpServletRequest request) throws IOException {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Forwarded-For");
//	    if (logger.isInfoEnabled()) {
//	        logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
//	    }
//
//	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	            ip = request.getHeader("Proxy-Client-IP");
//	            if (logger.isInfoEnabled()) {
//	                logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
//	            }
//	        }
//	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	            ip = request.getHeader("WL-Proxy-Client-IP");
//	            if (logger.isInfoEnabled()) {
//	                logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
//	            }
//	        }
//	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	            ip = request.getHeader("HTTP_CLIENT_IP");
//	            if (logger.isInfoEnabled()) {
//	                logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
//	            }
//	        }
//	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//	            if (logger.isInfoEnabled()) {
//	                logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
//	            }
//	        }
//	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	            ip = request.getRemoteAddr();
//	            if (logger.isInfoEnabled()) {
//	                logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
//	            }
//	        }
//	    } else 
		if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (! ("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

}
