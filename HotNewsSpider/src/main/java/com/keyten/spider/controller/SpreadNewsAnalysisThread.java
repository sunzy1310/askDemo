package com.keyten.spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.collect.SpreadNewsAnalysis;
import com.keyten.spider.service.TBLTargetService;

/**
 * 采集各个新闻网，并分析采集到的信息是不是热点新闻
 * 热点新闻：同时在多个新闻网站发布的新闻标题相似度较高的新闻
 * @author liym
 * 2018-03-26
 */
@Component
public class SpreadNewsAnalysisThread implements Runnable{
	@Autowired
	TBLTargetService targetService;
	@Autowired
	SpreadNewsAnalysis analysis;
	
	@Override
	public void run() {
		while(true){
			try {
				compare();
				Thread.sleep(SysConfig.CompapreStopTime);
			} catch (InterruptedException e) {
				LogUtil.addLogError(e.toString(),this.getClass().getName());
			}
		}
	}
	
	void compare(){
		String serverIP = SysConfig.SERVER_IP;
		List<String> targetList = targetService.getTargetByIp(serverIP); 
		
		if(targetList.size()>0){
			for(String targetId : targetList){
				analysis.compare(targetId);
			}	
		}else
			LogUtil.addLogInfo("当前没有目标需要采集", this.getClass().getName());
	}
	
}
