package com.keyten.base.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;


public class SysConfig {
	//网站采集间隔时间
	public static int sleepTime = 5*1000;
	
	public static int cpuCore = Runtime.getRuntime().availableProcessors();

	//存储网页快照目录
	public static String FILEPATH_HTML = "";
	
	//本机ＩＰ地址
	public static String SERVER_IP = "";
	
	//数据库保存的快照地址
//	public static String SERVER_FILEPATH_HTML = "D:/foxdata/HTML/";
	public static String SERVER_FILEPATH_HTML = "/home/foxdata/HTML/";
	
	public static String CLUSTER_NAME = "";
	
	
	static 
	{
		try
		{
			String path = SysConfig.class.getResource("/").getPath();
			path = path.substring(0,path.length()-1);
			path = path.substring(0,path.lastIndexOf("/"))+"/classes/";
			path = URLDecoder.decode(path, "utf-8");
			System.out.println((new StringBuilder("path:")).append(path).toString());			
			path = path+"sysconfig.properties";
			InputStream in = new FileInputStream(path);
			Properties p = new Properties();
			p.load(in);
			in.close();
			String SERVER_IP = p.getProperty("SERVER_IP");
			String FILEPATH_HTML = p.getProperty("FILEPATH_HTML");
			SysConfig.FILEPATH_HTML = FILEPATH_HTML;
			SysConfig.SERVER_IP = SERVER_IP;
		}
		catch (Exception e)
		{
			System.out.println("系统运行参数配置文件加载错误");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args)
	{
		System.out.println(SysConfig.cpuCore);
	}
}
