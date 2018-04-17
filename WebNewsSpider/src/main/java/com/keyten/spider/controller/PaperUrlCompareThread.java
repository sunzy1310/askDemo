package com.keyten.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLNewspaperWebConfig;
import com.keyten.base.bean.TBLPost;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.spider.service.TBLNewspaperWebConfigService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
/**
 * 报刊URL对比线程
 * @author liym
 *
 */
@Component
public class PaperUrlCompareThread implements Runnable{
	@Autowired
	private TBLNewspaperWebConfigService newspaperWebCfgService;
	@Autowired
	private TBLNewspaperWebConfig newspaperWebCfg;
	@Autowired
	private JedisSentinelPool jedisSentinelPool;
	@Override
	public void run() {
		while(true){
			try {
				if(CollectPaperHelper.urlQueue.size()>0){
					compare();
				}else{
					CollectPaperHelper.hasUrlToCompare = 0;
					LogUtil.addLogError("当前没有对比任务",this.getClass().getName());
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				LogUtil.addLogError("对比任务异常",this.getClass().getName());
			}
		}
	}
	private void compare() {
		TBLPost post = null;
		try {
			post = CollectPaperHelper.urlQueue.take();
			String artUrl = post.getArturl();
			String columnId = post.getColumnid();
			if(!artUrl.equals("")){
				Jedis jedis = jedisSentinelPool.getResource();
				if(!jedis.sismember("UrlN",artUrl)){
					jedis.sadd("UrlN",artUrl);
					CollectPaperHelper.artQueue.add(post);
				}
				jedis.close();
			}
			//遍历到新的栏目后需要更新栏目最后采集日期
			if(this.newspaperWebCfg.getWebruleid()==null||!this.newspaperWebCfg.getWebruleid().equals(columnId)){
				this.newspaperWebCfg.setWebruleid(columnId);
				long datetime = System.currentTimeMillis();
				this.newspaperWebCfg.setLastcollectdate(
						DateTimeUtil.getDateByLong(datetime));
				this.newspaperWebCfg.setLastcollecttime(
						DateTimeUtil.getTimeByLong(datetime));
					newspaperWebCfgService.update(newspaperWebCfg);
			}
		} catch (Exception e) {
			if(post!=null)
				LogUtil.addLogError(post.getArturl()+"对比出现异常",this.getClass().getName());
			e.printStackTrace();
		}
	}
}
