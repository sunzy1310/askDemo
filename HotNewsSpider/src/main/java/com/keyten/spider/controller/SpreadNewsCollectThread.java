package com.keyten.spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLHotNews;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.collect.SpreadNewsCollect;
import com.keyten.spider.service.TBLHotNewsService;

@Component
public class SpreadNewsCollectThread implements Runnable{
	@Autowired
	TBLHotNewsService hotNewsService;
	@Autowired
	SpreadNewsCollect spreadNewsCollect;
	@Override
	public void run() {
		while(true){
			try {
				collect();
				Thread.sleep(SysConfig.CollectNewsStopTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void collect(){
		try {
			String serverIP = SysConfig.SERVER_IP;
			List<TBLHotNews> newslist = hotNewsService.getNewsByIp(serverIP);
			if(newslist.size()>0){
				for(TBLHotNews news : newslist){
					String newsid = news.getNewsid();
					String newsurl = news.getNewsurl();
					spreadNewsCollect.collect(newsid, newsurl);
				}
			}else{
				System.out.println("当前没有需要采集的热点新闻");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
