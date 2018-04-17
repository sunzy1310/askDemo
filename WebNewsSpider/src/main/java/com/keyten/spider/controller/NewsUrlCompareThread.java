package com.keyten.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLColumn;
import com.keyten.base.bean.TBLPost;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.spider.service.TBLColumnService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
/**
 * 新闻URL对比线程
 * @author liym
 *
 */
@Component
public class NewsUrlCompareThread implements Runnable{
	@Autowired
	private TBLColumnService tblColumnService;
	@Autowired
	private TBLColumn column;
	@Autowired
	private JedisSentinelPool jedisSentinelPool;
	@Override
	public void run() {
		while(true){
			try {
				if(CollectNewsHelper.urlQueue.size()>0){
					compare();
				}else{
					CollectNewsHelper.hasUrlToCompare = 0;
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
			post = CollectNewsHelper.urlQueue.take();
			String artUrl = post.getArturl();
			String columnId = post.getColumnid();
			if(!artUrl.equals("")){
				Jedis jedis = jedisSentinelPool.getResource();
				if(!jedis.sismember("Urls",artUrl)){
					jedis.sadd("Urls",artUrl);
					CollectNewsHelper.artQueue.add(post);
				}
				jedis.close();
			}
			//遍历到新的栏目后需要更新栏目最后采集日期
			if(this.column.getColumnid()==null||!this.column.getColumnid().equals(columnId)){
				this.column.setColumnid(columnId);
				long datetime = System.currentTimeMillis();
				this.column.setLastcollectdate(
						DateTimeUtil.getDateByLong(datetime));
				this.column.setLastcollecttime(
						DateTimeUtil.getTimeByLong(datetime));
					tblColumnService.update(column);
			}
		} catch (Exception e) {
			if(post!=null)
				LogUtil.addLogError(post.getArturl()+"对比出现异常",this.getClass().getName());
			e.printStackTrace();
		}
		
		
	}
	
}
