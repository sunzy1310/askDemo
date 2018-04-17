package com.keyten.spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLAttentionHotNews;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.collect.AttentionNewsPLCollect;
import com.keyten.spider.service.TBLAttentionNewsService;

@Component
public class AttentionNewsPLThread implements Runnable{
	@Autowired
	TBLAttentionNewsService attnewsService;
	@Autowired
	AttentionNewsPLCollect attNewsPLCollect;
	
	@Override
	public void run() {
		while(true){
			try {
				collect();
				Thread.sleep(SysConfig.CollectNewsPLStopTime);
			} catch (InterruptedException e) {
				LogUtil.addLogError(e.toString(),this.getClass().getName());
			}
		}
	}
	void collect(){
		try {
			String serverIP = SysConfig.SERVER_IP;
			String datetime = DateTimeUtil.getNowByDateStr("3天前");
			List<TBLAttentionHotNews> attHotNews = attnewsService.getAttNewsByIPAndDate(serverIP, datetime);
			if(attHotNews.size()>0){
				for(TBLAttentionHotNews hotnews : attHotNews){
					String newsid = hotnews.getNewsid();
					String newsplurl = hotnews.getNewsplurl();
					String newsurl = hotnews.getNewsurl();
					attNewsPLCollect.collect(newsid, newsplurl,newsurl);
				}
			}else{
				System.out.println("当前没有需要采集的热点新闻");
			}
			
		} catch (Exception e) {
			LogUtil.addLogError(e.toString(),this.getClass().getName());
		}
	}
}
