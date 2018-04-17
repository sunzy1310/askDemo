package com.keyten.spider.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.keyten.base.bean.TBLPost;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.spider.controller.CollectNewsHelper;
import com.keyten.spider.controller.NewsArtInfoCollectThread;
import com.keyten.spider.controller.NewsUrlCollectThread;
import com.keyten.spider.controller.NewsUrlCompareThread;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ControllerTest {
	@Autowired
	SpringContextUtil springContextUtil;
	@Test
	public void collectUrlTest() {
		NewsUrlCollectThread urlController = 
				(NewsUrlCollectThread) springContextUtil.getBean("newsUrlCollectThread");
		urlController.run();
	}
	@Test
	public void compareTest() {
		NewsUrlCompareThread urlComp = 
				(NewsUrlCompareThread) springContextUtil.getBean("newsUrlCompareThread");
		//采集url线程
		TBLPost post = new TBLPost();
		post.setColumnid("1");
		post.setArturl("http://hb.jjj.qq.com/a/20180322/010754.htm");
		TBLPost post1 = new TBLPost();
		post1.setColumnid("1");
		post1.setArturl("http://hb.jjj.qq.com/a/20180322/010751.htm");
		try {
			CollectNewsHelper.urlQueue.put(post);
			CollectNewsHelper.urlQueue.put(post1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		urlComp.run();
	}
	@Test
	public void collectArtTest() {
		NewsArtInfoCollectThread artCollect = 
				(NewsArtInfoCollectThread) springContextUtil.getBean("newsArtInfoCollectThread");
		TBLPost post = new TBLPost();
		post.setColumnid("CI0000000011");
		post.setArturl("http://tj.sina.com.cn/news/m/2018-03-23/detail-ifysnevk9345162.shtml");
		TBLPost post1 = new TBLPost();
		post1.setColumnid("CI0000000011");
		post1.setArturl("http://tj.sina.com.cn/news/m/2018-03-23/detail-ifysnevk9359224.shtml");
		try {
			CollectNewsHelper.artQueue.put(post);
			CollectNewsHelper.artQueue.put(post1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		artCollect.run();
	}
}
