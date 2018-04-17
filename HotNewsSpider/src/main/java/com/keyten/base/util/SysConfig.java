package com.keyten.base.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;


public class SysConfig {
	//重点人系统zip包生成路劲
	public static String FILE_PATH = "zdrdata/";
	
	//网站采集间隔时间
	public static int CompapreStopTime = 5*60*1000;
	
	//新闻内容采集间隔时间
	public static int CollectNewsStopTime = 5*60*1000;
	
	//新闻评论采集采集间隔时间
	public static int CollectNewsPLStopTime = 5*60*1000;

	//存储网页快照目录
	public static String FILEPATH_HTML = "";//传播范围
	public static String ATTEFILEPATH_HTML = "";//网名关注
	
	//本机ＩＰ地址
	public static String SERVER_IP = "";
	
	//网民关注新闻更新时间   默认三天？
	public static String ATTENTIONNEWSDAYS = "3";
	
	//网民关注新闻评论采集天数    默认3天
	public static String PLDAYS = "3";
	
	//数据库保存的快照地址
	public static String SERVER_FILEPATH_HTML = "D:/zdrnews/HTML";//传播范围
	public static String SERVER_ATTFILEPATH_HTML = "D:/zdrnews/ATTHTML";//网名关注
	
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
			String ATTEFILEPATH_HTML = p.getProperty("ATTEFILEPATH_HTML");
			SysConfig.FILEPATH_HTML = FILEPATH_HTML;
			SysConfig.SERVER_IP = SERVER_IP;
			SysConfig.ATTEFILEPATH_HTML = ATTEFILEPATH_HTML;
		}
		catch (Exception e)
		{
			System.out.println("系统运行参数配置文件加载错误");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args)
	{
		System.out.println(SysConfig.FILEPATH_HTML);
	}
}
