package com.keyten.spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.collect.AttentionNewsCollect;
import com.keyten.spider.service.TBLTargetService;
/**
 * 用于采集网民关注的热点新闻
 * 热点新闻：从各个新闻网采集新闻信息，如果评论数大于一个设定好的阈值，那么这条新闻就是一条热点新闻
 * @author liym
 * 2018-03-26
 */
@Component
public class AttentionNewsThread extends Thread{
	@Autowired
	TBLTargetService targetService;
	@Autowired
	AttentionNewsCollect attnews;
	
	@Override
	public void run() {
		while(true){
			try {
				collect();
				Thread.sleep(SysConfig.CompapreStopTime);
			} catch (Exception e) {
				LogUtil.addLogError(e.toString(),this.getClass().getName());
			}
		}
	}
	
	void collect() {
		String serverIP = SysConfig.SERVER_IP;
		List<String> targetList = targetService.getTargetByIp(serverIP); 
		if(targetList.size()>0){
			for(String targetId : targetList){
				attnews.collect(targetId);
			}	
		}else
			LogUtil.addLogInfo("当前没有目标需要采集", this.getClass().getName());
	}
}
