package com.keyten.spider.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.keyten.base.bean.TBLNewspaperWebConfig;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.collect.PaperUrlCollect;
import com.keyten.spider.service.TBLNewspaperWebConfigService;
/**
 * 报刊URL总调度线程，用于采集每天发表刊物中的文章列表
 * @author liym
 */
@Component
public class PaperURLCollectThread implements Runnable {
	@Autowired
	SpringContextUtil springContextUtil;
	@Autowired
	TBLNewspaperWebConfigService newspaperCfgService;
	
	//上次初始化所有规则是时间（12个小时初始化一次）
	private int preHour = 0;
	@Override
	public void run() {
		while(true) {
			try{
				//没有对比任务或者对比任务完成开始准备新一轮采集
				if(CollectPaperHelper.hasUrlToCompare == 0) {
					String ipAddr = SysConfig.SERVER_IP;
					//获取当前的hour，与preHour对比，不相同则为下一个小时了，需要更新一次规则了
					Calendar c = Calendar.getInstance();
					int hour = c.get(Calendar.HOUR_OF_DAY);
					//(相当于每天0点和12点更新，这两个时间点刚好信息发布频率较低)
					if(hour!=preHour&&(hour%12==0)) {
						//系统每12小时加载规则信息    如果没有加载成功（临时错误） 下次重新加载
						//这一步是基础，没有规则信息，后面的采集都进行不了
						Map<String,TBLNewspaperWebConfig> allMap  = 
								newspaperCfgService.getAllWebCfgAndRules(ipAddr);
						//先清除之前的然后在加入本次查询到的（有新分配的或者取消、禁用的，所以需要每次加载最新的栏目）
						CollectPaperHelper.allColAndRules.clear();
						CollectPaperHelper.allColAndRules.putAll(allMap);
					}
					//加载本次需要采集的栏目Id列表
					List<String> columnids = newspaperCfgService.getTaskWebCfgIds(ipAddr);
					CollectPaperHelper.taskOfCid.clear();
					CollectPaperHelper.taskOfCid.addAll(columnids);
					if(CollectPaperHelper.taskOfCid.size()>0) {
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
				e.printStackTrace();
				LogUtil.addLogError("报刊URL采集==》初始化规则异常",this.getClass().getName());
			}
		}
	}
	//采集调度方法，获取每一个报刊分配不同的线程去采集
	private void collectURL() {
		try {
			for(Entry<String,TBLNewspaperWebConfig> entry : CollectPaperHelper.allColAndRules.entrySet()) {
				//开始循环遍历每一个报刊信息 同时采集url信息
				PaperUrlCollect paperUrlCollect = 
						(PaperUrlCollect) springContextUtil.getBean("paperUrlCollect");
				TBLNewspaperWebConfig webCfg = entry.getValue();
				paperUrlCollect.setWebConfig(webCfg);
				CollectPaperHelper.executor.execute(paperUrlCollect);
			}
			CollectPaperHelper.hasUrlToCompare = 1;
		} catch (Exception e) {
			LogUtil.addLogError("报刊URL采集==》线程池创建异常",this.getClass().getName());
		}
	}
	
}
