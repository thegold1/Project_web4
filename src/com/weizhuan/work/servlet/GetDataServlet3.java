package com.weizhuan.work.servlet;

import com.weizhuan.work.function.MainProcess;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

// 通过FileUtils.readLines读取数据不行，必须通过InputStreamReader读取
public class GetDataServlet3 extends HttpServlet {

	public static String name = "";
	// ServletException, IOException
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String data = new String(request.getParameter("email").getBytes("iso-8859-1"), "utf-8");
//		String data = request.getParameter("email");
		System.out.println("email:"+data);

//		try {
//			Thread.sleep(61000);
//		} catch(Exception e) {
//
//		}
//		s = "自媒体运营的方式方法有很多种，比如可以的做微信公众号，可以的做头条号，可以的做百家号，也可以去搜狐号，这些平台都是可以进行自媒体运营的。可以去参加腾讯看点升级后的春雨计划为了激励更多优质创作，鼓励更多优质作者，腾讯看点「春雨计划」将于7月6日进行整体的改版升级。";
//		String id = request.getRemoteAddr();

//		String id = getIpAddress(request);
//		System.out.println("remote id:"+id);
//		request.getSession().setAttribute("user_msg", s);
//		request.getRequestDispatcher("/getData.jsp").forward(request, response);


//		request.getRequestDispatcher("/file/test.zip").forward(request, response);
//		try {
//			MainProcess.startProcess();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}

//		String path = "\\file\\test\\test_" + "9527";
//		String path = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\test\\test_" + "9528";
//		File directory = new File(path);
//		boolean hasSucceeded = directory.mkdir();
//		System.out.println("创建文件夹结果：" + hasSucceeded);

//		String ziyuan = "/file/" + name;
//		request.getRequestDispatcher(ziyuan).forward(request, response);

		String path = "D:\\weizhuan_web\\去重\\缓存\\contentpath.txt";
//		String path = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\test\\contentpath.txt";
//		String path = "\\file\\contentpath.txt";
//		String path2 = "/file/contentpath.txt";
//		File file = new File(path2);
//		if (!file.exists()) {
//			System.out.println("tomcat file not exists");
//		} else {
//			System.out.println("tomcat file exists");
//		}

//		List<String> list1 = FileUtils.readLines(file);
//		List<String> list1 = FileUtils.readLines(file);
		System.out.println("tomcat ddd111");

//		String path = this.getServletContext().getRealPath("/file/contentpath.txt");
		System.out.println("tomcat path:"+path);
		File file3 = new File(path);

		BufferedReader br = null;
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file3));
			br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();
			while (line != null) {
				line = br.readLine(); // 一次读入一行数据
				if (line != null) {
					System.out.println("tomcat line:"+line);
				}
			}
//			System.out.println("tomcat line:"+line);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

//		if (!file3.exists()) {
//			System.out.println("tomcat file3 not exists");
//		} else {
//			System.out.println("tomcat file3 exists");
//		}
//		List<String> list3 = FileUtils.readLines(file3);
//		for (String s: list3) {
//			System.out.println("tomcat data:"+s);
//		}

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
