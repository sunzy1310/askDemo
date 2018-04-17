package com.keyten.spider.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLColumn;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.collect.NewsUrlCollect;
import com.keyten.spider.service.TBLColumnService;

/**
 * 新闻/博客/论坛 信息采集控制线程
 * @author liym
 * @version 2018-03-08
 */
@Component
public class NewsUrlCollectThread implements Runnable {
	@Autowired
	SpringContextUtil springContextUtil;
	@Autowired
	TBLColumnService tBLColumnService;
	//上次初始化所有规则是时间（12个小时初始化一次）
	private int preHour = 0;
	@Override
	public void run() {
		while(true) {
			try{
				//没有对比任务或者对比任务完成开始准备新一轮采集
				if(CollectNewsHelper.hasUrlToCompare == 0) {
					String ipAddr = SysConfig.SERVER_IP;
					//获取当前的hour，与preHour对比，不相同则为下一个小时了，需要更新一次规则了
					Calendar c = Calendar.getInstance();
					int hour = c.get(Calendar.HOUR_OF_DAY);
					//(相当于每天0点和12点更新，这两个时间点刚好信息发布频率较低)
					if(hour!=preHour&&(hour%12==0)) {
						//系统每小时加载规则信息    如果没有加载成功（临时错误） 下次重新加载
						//这一步是基础，没有规则信息，后面的采集都进行不了，正常不会出问题，除非改库等
						Map<String,TBLColumn> allMap  = tBLColumnService.getAllColumnAndRules(ipAddr);
						//先清除之前的然后在加入本次查询到的（有新分配的或者取消、禁用的，所以需要每次加载最新的栏目）
						CollectNewsHelper.allColAndRules.clear();
						CollectNewsHelper.allColAndRules.putAll(allMap);
					}
					LogUtil.addLogInfo("开始获取本次任务信息",this.getClass().getName());
					//加载本次需要采集的栏目Id列表
					List<String> columnids = tBLColumnService.getTaskColumnIds(ipAddr);
					CollectNewsHelper.taskOfCid.clear();
					CollectNewsHelper.taskOfCid.addAll(columnids);
					LogUtil.addLogInfo("本次任务信息"+columnids.size(),this.getClass().getName());
					if(CollectNewsHelper.taskOfCid.size()>0) {
						collectURL();
					}else{
						int sleepTime = SysConfig.sleepTime;
						Thread.sleep(sleepTime);
					}
				}else {
					int sleepTime = SysConfig.sleepTime;
					Thread.sleep(sleepTime);
				}
			}catch (Exception e) {
				LogUtil.addLogError("URL采集==》初始化规则异常",this.getClass().getName());
			}
		}
	}
	
	private void collectURL() {
		try {
			for(String cid : CollectNewsHelper.taskOfCid) {
				//开始循环遍历每一个栏目 同时采集url信息
				NewsUrlCollect newsUrlCollect = 
						(NewsUrlCollect) springContextUtil.getBean("newsUrlCollect");
				TBLColumn column = CollectNewsHelper.allColAndRules.get(cid);
				if(null == column) {
					column = tBLColumnService.getColumnAndRulesById(cid);
					CollectNewsHelper.allColAndRules.put(cid, column);
				}
				newsUrlCollect.setColumn(column);
				CollectNewsHelper.executor.execute(newsUrlCollect);
			}
			CollectNewsHelper.hasUrlToCompare = 1;
		} catch (Exception e) {
			LogUtil.addLogError("URL采集==》线程池创建异常",this.getClass().getName());
		}
		
	}
}
