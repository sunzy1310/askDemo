package com.keyten.spider.controller;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import com.keyten.base.bean.TBLNewspaperWebConfig;
import com.keyten.base.bean.TBLPost;

public class CollectPaperHelper {
	//用于存储所有本机负责的栏目  url规则   文章元素规则   信息    Map<columnid,TBLColumn>
	public static Map<String,TBLNewspaperWebConfig> allColAndRules = new HashMap<String,TBLNewspaperWebConfig>();
	//用于存储本次需要采集的栏目Id
	public static List<String> taskOfCid = new ArrayList<>();
	//用于判断对比任务是否执行完毕，若为0则执行完毕，可以开始新一轮的采集URL——》对比
	public static int hasUrlToCompare = 0 ;
	//用于存储新闻地址
	public static BlockingQueue<TBLPost> urlQueue = new LinkedBlockingQueue<TBLPost>();
	//文章元素采集任务队列   map<文章地址,columnid>
	public static BlockingQueue<TBLPost> artQueue = new LinkedBlockingQueue<TBLPost>();
	//文章存储任务队列
	public static BlockingQueue<TBLPost> saveQueue = new LinkedBlockingQueue<TBLPost>();
	
	public static ExecutorService executor = null;//线程池对象
	static{
		//获取核心数
		int hexinshu = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(hexinshu*2); 
	}
}
