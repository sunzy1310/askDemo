package com.keyten.spider.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyten.base.bean.TBLHotNews;
import com.keyten.base.bean.TBLHotThing;
import com.keyten.base.dao.TBLHotNewsMapper;
import com.keyten.base.dao.TBLHotThingMapper;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.SpringContextUtil;

@Service
public class TBLHotNewsService {
	@Autowired
	SpringContextUtil springUtil;
	@Autowired
	TBLHotNewsMapper hotNewsMapper;
	@Autowired
	TBLHotThingMapper hotThingMapper;
	public void update(TBLHotNews news) {
		hotNewsMapper.updateByPrimaryKeySelective(news);
	}
	public List<TBLHotNews> getNewsByIp(String ipaddr){
		return hotNewsMapper.selectNewsByIp(ipaddr);
	}
	/**
	 * 先判断有没有之前采集过的新闻
	 * 如果有，取出之前的事件
	 * 如果没有，插入一个新的事件
	 * 然后将本事件下的新闻全部保存起来
	 * @param thing
	 * @param newslist
	 * @param targetId
	 */
	public void saveHotNewsAndThing(String thing,List<String[]> newslist,String targetId) {
		List<String> urls = new ArrayList<>();
		//循环遍历本事件对应的所有新闻信息
		for(String[] newsinfo : newslist){
			urls.add(newsinfo[0]);
		}
		//查询出已存在的新闻和对应的时间ID
		List<TBLHotNews> hasNewsList = hotNewsMapper.selectThingByUrls(urls);
		String thingId;
		if(hasNewsList.size()>0){
			thingId = hasNewsList.get(0).getThingid();
		}else{
			TBLHotThing hotThing = (TBLHotThing) springUtil.getBean("hotThing");
			hotThing.setThingword(thing);
			hotThing.setMakedate(DateTimeUtil.getDateByLong(System.currentTimeMillis()));
			hotThing.setMaketime(DateTimeUtil.getTimeByLong(System.currentTimeMillis()));
			hotThingMapper.insert(hotThing);
			thingId = hotThing.getThingid();
		}
		list:for(String[] newsinfo : newslist) {
			String url = newsinfo[0];
			String columnid = newsinfo[2];
			for(TBLHotNews hnews : hasNewsList) {
				if(url.equals(hnews.getNewsurl())) {
					continue list;
				}
			}
			TBLHotNews hotNews = (TBLHotNews) springUtil.getBean("hotNews");
			hotNews.setTargetid(targetId);
			hotNews.setColumnid(columnid);
			hotNews.setThingid(thingId);
			hotNews.setNewsurl(newsinfo[0]);
			hotNews.setMakedate(DateTimeUtil.getDateByLong(System.currentTimeMillis()));
			hotNews.setMaketime(DateTimeUtil.getTimeByLong(System.currentTimeMillis()));
			hotNews.setPlpage("0");
			hotNews.setCollectcount("0");
			hotNews.setLastcollectdate("1970-01-01");
			hotNews.setLastcollecttime("00:00:00");
			hotNewsMapper.insertSelective(hotNews);
		}
	}
}
