package com.weizhuan.work.simhash;

import com.weizhuan.work.function.Simhash;
import com.weizhuan.work.function.ZipUtils;
//import org.apache.commons.io.FileUtils;
import com.weizhuan.work.servlet.GetDataServlet5;
import com.weizhuan.work.util.FileUtils;

import java.io.*;
import java.util.*;

/**
 * Simhash测试
 * 
 * @author litaoxiao
 * 
 */
public class SimhashUtil {
	public static HashMap<Long, String> mSimHashContentMap = new HashMap<>();
	public static HashMap<String, List<String>> mContentPathMap = new HashMap<>();
	// 数据结构： | 标题 自身段落 相似段落 相似段落的标题 | 标题 自身段落 相似段落 相似段落的标题
	// 里面的段落有可能重复
	public static List<List<String>> mPaperSimResult = new ArrayList<>();
	// key 标题
	// value 自身段落
	// 段落已经去重
	public static HashMap<String, List<List<String>>> mPagerSimMap = new HashMap<>();

	public static String QIAN_ZHUI = "D:\\weizhuan\\去重\\";
	public static String PROCESS_DIR_PATH = QIAN_ZHUI + "缓存\\生成";

	public static boolean mSwitch = false;
	public static String USED_PATH = QIAN_ZHUI + "已经使用";
	// 本次处理文件夹里面的所有txt文本
	public static String PROCESS_DIR_TXT_PATH = QIAN_ZHUI + "缓存\\all_file_name.txt";
	public static String SIM_RESULT_PATH = QIAN_ZHUI + "缓存\\sim_result.txt";
	public static String DELETE_RESULT_PATH = QIAN_ZHUI + "缓存\\delete_result.txt";
	public static String MERGE_MARK_PATTERN = ".,!?:;.；：？！，。、“”(),";
	public static HashMap<String, Integer> paperLenMap = new HashMap<>();

	public static List<String> mDeleteList = new ArrayList<>();

	public static String CONTENT_PATH = QIAN_ZHUI + "缓存\\contentpath.txt";

	public static Set<String> usedSet = new HashSet<>();

	public static String SIM_HASH_CONTENT_PATH = QIAN_ZHUI + "缓存\\simhashContent.txt";
	// 已经使用的文件夹，数据已经被缓存
	public static String DATA_DIR_PATH = QIAN_ZHUI + "缓存\\data_dir.txt";

	public static String PAPER_LEN_PATH = QIAN_ZHUI + "缓存\\paper_len.txt";

	public static void copySourcePathDir(String sourcePath) throws Exception {
//		String sourcePath =  "D:\\weizhuan_web\\生成";
		String[] tmp = sourcePath.split("\\\\");
		String target = tmp[tmp.length - 1];
		String lastWord = target;

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		System.out.println("tomcat year:"+year+" month:"+month+" day:"+day+" hour:"+hour);

		String underLine = "_";

		target = QIAN_ZHUI+"已经使用\\"+target+underLine+year+underLine+month+underLine+day+underLine+hour+underLine+minute;
		usedSet.add(target);
		System.out.println("tomcat target path:"+target);
		copyFolder(sourcePath, target);

		String name = lastWord+underLine+year+underLine+month+underLine+day+underLine+hour+underLine+minute;
		String target2 = "D:\\Idea_WorkSpace\\Project_web4\\out\\artifacts\\Project_web4_war_exploded\\file\\"
				+name;
		copyFolder(sourcePath, target2);
		ZipUtils.startProcess(target2);
		GetDataServlet5.name = name+".zip";
	}

	/**
	 * 复制文件夹(使用缓冲字节流)
	 * @param sourcePath 源文件夹路径
	 * @param targetPath 目标文件夹路径
	 */
	public static void copyFolder(String sourcePath,String targetPath) throws Exception {
		//源文件夹路径
		File sourceFile = new File(sourcePath);
		//目标文件夹路径
		File targetFile = new File(targetPath);

		if (!sourceFile.exists()) {
			throw new Exception("文件夹不存在");
		}
		if (!sourceFile.isDirectory()) {
			throw new Exception("源文件夹不是目录");
		}
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		if (!targetFile.isDirectory()) {
			throw new Exception("目标文件夹不是目录");
		}

		File[] files = sourceFile.listFiles();
		if (files == null || files.length == 0) {
			return;
		}

		for (File file : files) {
			//文件要移动的路径
			String movePath = targetFile+File.separator+file.getName();
			if (file.isDirectory()){
				//如果是目录则递归调用
				copyFolder(file.getAbsolutePath(), movePath);
			} else {
				//如果是文件则复制文件
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(movePath));

				byte[] b = new byte[1024];
				int temp = 0;
				while ((temp = in.read(b)) != -1) {
					out.write(b,0,temp);
				}
				out.close();
				in.close();
			}
		}
	}


	public static void startRemoveDuplicate() throws Exception {
		readContentPathMapData();
		List<String> contents = readSimHashContentMap();
		readUsedDir();
		readPaperLen();
		List<String> ls = new ArrayList<>();
		int sum = 0;
		StringBuilder sb = new StringBuilder();

		ls.addAll(contents);

		File usedFile = new File(USED_PATH);
		File[] usedFiles = usedFile.listFiles();
		int usedLen = usedFiles.length;


		for (int i = 0; i < usedLen; i++) {
			if (usedFiles[i].isDirectory()) {
				String dir = usedFiles[i].toString();
				if (!usedSet.contains(dir)) {
					usedSet.add(dir);
				} else {
					continue;
				}
				File file1 = new File(dir);
				File[] files1 = file1.listFiles();
				for (int j = 0; j < files1.length; j++) {
					sb.setLength(0);
					String file_path = files1[j].toString();
					File file = new File(file_path);
					if (!file.exists()) {
						continue;
					}
					List<String> lss = FileUtils.readLines(file);

					for (int k = 0; k < lss.size(); k++) {
						String s = lss.get(k);
						s = removeSymbol(s);
						lss.set(k, s);
					}

					for (int k = 0; k < lss.size(); k++) {
						String s = lss.get(k);
						if (s.length() <= 10) {
							lss.remove(k);
							k--;
						}
					}
					ls.addAll(lss);
					for (String s: lss) {
						sb.append(s);
						if (mContentPathMap.containsKey(s)) {
							List<String> l = mContentPathMap.get(s);
							l.add(file_path);
							mContentPathMap.put(s, l);
						} else {
							List<String> l = new ArrayList<>();
							l.add(file_path);
							mContentPathMap.put(s, l);
						}
						sum++;
					}
					paperLenMap.put(file_path, new Integer(sb.toString().length()));
				}
			}
		}


		File processDirFile = new File(PROCESS_DIR_PATH);
		File[] processDirFiles = processDirFile.listFiles();
		int processDirFilesLen = processDirFiles.length;
		List<String> processDirFileList = new ArrayList<>();

		for (int i = 0; i < processDirFilesLen; i++) {
			sb.setLength(0);
			String processDirFilePath = processDirFiles[i].toString();
			processDirFileList.add(processDirFilePath);

			File file = new File(processDirFilePath);
			if (!file.exists()) {
				continue;
			}
			List<String> lss = FileUtils.readLines(file);

			for (int j = 0; j < lss.size(); j++) {
				String s = lss.get(j);
				s = removeSymbol(s);
				lss.set(j, s);
			}

			for (int j = 0; j < lss.size(); j++) {
				String s = lss.get(j);
				if (s.length() <= 10) {
					lss.remove(j);
					j--;
				}
			}
			ls.addAll(lss);
			for (String s: lss) {
				sb.append(s);
				if (mContentPathMap.containsKey(s)) {
					List<String> l = mContentPathMap.get(s);
					l.add(processDirFilePath);
					mContentPathMap.put(s, l);
				} else {
					List<String> l = new ArrayList<>();
					l.add(processDirFilePath);
					mContentPathMap.put(s, l);
				}
				sum++;
			}
			paperLenMap.put(processDirFilePath, new Integer(sb.toString().length()));
		}

		List<String> paperLenList = new ArrayList<>();
		for (Map.Entry<String, Integer> entry: paperLenMap.entrySet()) {
			String s1 = entry.getKey();
			int paperLen = entry.getValue();
			paperLenList.add(s1);
			paperLenList.add(""+paperLen);
		}
		insertListToPath2(PAPER_LEN_PATH, paperLenList);

		insertListToPath2(PROCESS_DIR_TXT_PATH, processDirFileList);
		System.out.println("tomcat sum:"+sum);

		// 默认是第二个参数是3
//		Simhash simhash = new Simhash(4, 3);
		Simhash simhash = new Simhash(4, 5);
		System.out.println("tomcat 汉明距离的衡量标准:"+5);

		int count = 0;

		for (String content : ls) {
			count++;
			Long simhashVal = simhash.calSimhash(content);
			System.out.println(content);
			System.out.println(Long.toBinaryString(simhashVal));
			boolean flag = simhash.isDuplicate(content);
			System.out.println(flag);
			mSimHashContentMap.put(simhashVal, content);
			simhash.store(simhashVal, content);

			System.out.println("tomcat hashCode:"+simhashVal);
			System.out.println("tomcat content:"+content);
			System.out.println("tomcat ------------------------------------------");

		}

		System.out.println("tomcat count:"+count);
		Set<String> set1 = new HashSet<>();
		// 轮询文章中相似的段落和标题 里面的段落有可能重复
		for (int i = 0; i < mPaperSimResult.size(); i++) {
			List<String> list = mPaperSimResult.get(i);
			int mark_index1 = -1;
			int mark_index2 = -1;
			for (int j = 0; j < list.size(); j++) {
				String s = list.get(j);
				if (s.equals(Simhash.MARK1)) {
					mark_index1 = j;
				}
				if (s.equals(Simhash.MARK2)) {
					mark_index2 = j;
				}
				System.out.println("data:"+list.get(j));
				if (j >= 2) {
					set1.add(list.get(j));
				}
				if (mark_index2 != -1 && j > mark_index2) {
					boolean mHasParaFlag = false;
					List<List<String>> listTmp = null;
					if (mPagerSimMap.containsKey(s)) {
						listTmp = mPagerSimMap.get(s);
						for (int k = 0; k < listTmp.size(); k++) {
							List<String> listk = listTmp.get(k);
							if (list.get(1).equals(listk.get(0))) {
								mHasParaFlag = true;
								break;
							}
						}
					} else {
						listTmp = new ArrayList<>();
					}
					if (!mHasParaFlag) {
						List tmp = new ArrayList();
						// 自己的段落
						tmp.add(list.get(1));
						// 相似的段落
						tmp.add(list.get(0));
						// 相似的段落的标题
						if (s.equals(list.get(mark_index1 + 1))) {
							tmp.add(list.get(mark_index1 + 2));
						} else {
							tmp.add(list.get(mark_index1 + 1));
						}
						listTmp.add(tmp);
						mPagerSimMap.put(s, listTmp);
					}
				}
			}
			System.out.println("tomcat ========================");
		}
		System.out.println("tomcat res.size:"+ mPaperSimResult.size());
		System.out.println("tomcat set size:"+set1.size());

		insertListToPath(SIM_RESULT_PATH, mPagerSimMap);
		List<String> delListInfo = getDeleteListInfo(mPagerSimMap);
		insertListToPath2(DELETE_RESULT_PATH, delListInfo);
		deleteList();


		List<String> contentPathLists = new ArrayList<>();
		Set<String> tmpSet = new HashSet<>();
		for (Map.Entry<String, List<String>> entry: mContentPathMap.entrySet()) {
			String content = entry.getKey();
			List<String> paths = entry.getValue();
			contentPathLists.add(content);
			tmpSet.clear();
			for (String s : paths) {
				tmpSet.add(s);
			}
			contentPathLists.add(""+tmpSet.size());
			for (String s: tmpSet) {
				contentPathLists.add(s);
			}
		}

		insertListToPath2(CONTENT_PATH, contentPathLists);



		List<String> simHashContentList = new ArrayList<>();
		for (Map.Entry<Long, String> entry: mSimHashContentMap.entrySet()) {
			String simHashValue = ""+entry.getKey();
			String content = entry.getValue();
			simHashContentList.add(simHashValue);
			simHashContentList.add(content);
		}

		insertListToPath2(SIM_HASH_CONTENT_PATH, simHashContentList);

		List<String> usedList = new ArrayList<>();

		copySourcePathDir(PROCESS_DIR_PATH);

		for (String s: usedSet) {
			usedList.add(s);
		}
		insertListToPath2(DATA_DIR_PATH, usedList);
	}

	public static void readPaperLen() throws IOException {
		try {
			File file = new File(PAPER_LEN_PATH);
			if (!file.exists()) {
				return;
			}
			List<String> list1 = FileUtils.readLines(file);
			for (int i = 0; i < list1.size(); i += 2) {
				String s = list1.get(i);
				String s2 = list1.get(i + 1);
				int paperLen = Integer.valueOf(s2);
				paperLenMap.put(s, paperLen);
			}
		} catch (Exception e) {

		}
	}

	public static List<String> readSimHashContentMap() throws IOException {
		try {
			File file = new File(SIM_HASH_CONTENT_PATH);
			if (!file.exists()) {
				return new ArrayList<String>();
			}
			List<String> list1 = FileUtils.readLines(file);
			List<String> re = new ArrayList<>();
			for (int i = 0; i < list1.size() - 1; i += 2) {
				String s1 = list1.get(i);
				String s2 = list1.get(i + 1);
				Long l = null;
				try {
					l = Long.valueOf(s1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mSimHashContentMap.put(l, s2);
				re.add(s2);
			}
			return re;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public static void readUsedDir() throws IOException {
		try {
			File file = new File(DATA_DIR_PATH);
			if (!file.exists()) {
				return;
			}
			List<String> list1 = FileUtils.readLines(file);
			for (String s: list1) {
				usedSet.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	在头条号上想获得头条的推荐量首先你的内容必须是垂直且有价值的这样被推荐的几率才会更大
	//	2
	//	D:\weizhuan\test\test_7897\test\抖音运营.txt
	//	D:\weizhuan\test\test_7897\test\自媒体.txt
	// 第一行表示 一行文本内容，第二行表示拥有这行内容的文本数量，第三、四行表示拥有第一行文本内容的文章标题
	public static void readContentPathMapData () throws IOException {
		try {
			File file = new File(CONTENT_PATH);
			if (!file.exists()) {
				return;
			}
			List<String> list1 = FileUtils.readLines(file);

			for (int i = 0; i < list1.size(); i++) {
				String key = list1.get(i);
				if (i + 1 < list1.size()) {
					Integer j = new Integer(1);
					try {
						String s1 = list1.get(i + 1);
						j = Integer.valueOf(s1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (i + 1 + j < list1.size()) {
						List<String> l = new ArrayList<>();
						for (int k = i + 2; k <= i + 1 + j; k++) {
							String tmp = list1.get(k);
							l.add(tmp);
						}
						mContentPathMap.put(key, l);
					}
					i += 1 + j;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void deleteList() {
		for (int i = 0; i < mDeleteList.size(); i++) {
			String path = mDeleteList.get(i);
			if (path.contains(PROCESS_DIR_PATH)) {
				File myObj = new File(path);
				myObj.delete();
			}
		}
	}

	public static List<String> getDeleteListInfo(HashMap<String, List<List<String>>> simMap) {
		List<String> ree = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, List<List<String>>> entry : simMap.entrySet()) {
			sb.setLength(0);
			String title = entry.getKey();
			List<List<String>> ls = entry.getValue();
			for (int i = 0; i < ls.size(); i++) {
				List<String> lss = ls.get(i);
				String s = lss.get(0);
				sb.append(s);
			}
			int len1 = sb.toString().length();

			int totalLen = paperLenMap.get(title);
			if (len1 > totalLen * 2 / 10 && title.contains(PROCESS_DIR_PATH)) {
				System.out.println("tomcat title~~~~:"+title);
				int ii = totalLen * 2 / 10;
				mDeleteList.add(title);
				ree.add(title+" total:"+totalLen +" simLen:"+len1+" 百分之20："+ii+" to delete");
			}
		}
		ree.add("size:"+ree.size());
		return ree;
	}

	public static void insertListToPath2(String path, List<String> ls) {
		try {
			File file = new File(path);

			// if file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			for (String s: ls) {
				out.write(s);
				out.write("\r\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void insertListToPath(String path, HashMap<String, List<List<String>>> simMap) {
		if (simMap == null || "".equals(path)) {
			return;
		}
		try {
			File file = new File(path);

			// if file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");

			for (Map.Entry<String, List<List<String>>> entry : simMap.entrySet()) {
				String title = entry.getKey();
				if (!title.contains(PROCESS_DIR_PATH)) {
					continue;
				}
				out.write(title);
				out.write("\r\n");
				List<List<String>> ls = entry.getValue();
				for (int i = 0; i < ls.size(); i++) {
					List<String> lss = ls.get(i);
					for (int j = 0; j < lss.size(); j++) {
						String s = lss.get(j);
						out.write(s);
						out.write("\r\n");
					}
					out.write("----------------------------------------------------------------------------------------------------------");
					out.write("\r\n");
				}
				out.write("===============================================================================================================");
				out.write("\r\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String removeSymbol(String s) {
//        String s =  "我的，，。魔法书";
//        String pattern = "；：？！，。、“”";
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (MERGE_MARK_PATTERN.contains(new String(new char[] {chars[i]} ))) {
				chars[i] = ' ';
			}
		}
		String re = String.valueOf(chars);
		re = re.replace(" ","");
		return re;
	}
}
