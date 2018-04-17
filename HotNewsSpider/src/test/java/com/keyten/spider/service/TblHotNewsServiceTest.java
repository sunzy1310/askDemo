package com.keyten.spider.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.keyten.base.bean.TBLHotNews;
import com.keyten.spider.service.TBLHotNewsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TblHotNewsServiceTest {
	@Autowired
	TBLHotNewsService hotNewsService;
	@Test
	public void getThingByUrlTest() {
//		List<String> urls = new ArrayList<>();
//		urls.add("http://tj.jjj.qq.com/a/20170320/003656.htm");
//		urls.add("http://tj.sina.com.cn/news/m/2017-03-20/detail-ifycnpiu9151272.shtml");
		String ip = "192.168.12.250";
		List<TBLHotNews> thing= hotNewsService.getNewsByIp(ip);
		if(thing.size()>0) {
			System.out.println(thing.get(0).getColumnid());
		}
	}
}
