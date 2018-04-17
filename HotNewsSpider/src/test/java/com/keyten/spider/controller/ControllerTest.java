package com.keyten.spider.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.keyten.spider.controller.AttentionNewsPLThread;
import com.keyten.spider.controller.AttentionNewsThread;
import com.keyten.spider.controller.SpreadNewsAnalysisThread;
import com.keyten.spider.controller.SpreadNewsCollectThread;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ControllerTest {
	@Autowired
	AttentionNewsThread attentionNewsThread;
	@Autowired
	AttentionNewsPLThread attentionNewsplThread;
	@Autowired
	SpreadNewsAnalysisThread spreadNewsAnalysisThread;
	@Autowired
	SpreadNewsCollectThread spreadNewsThread;
	
	@Test
	public void attentionNewsTest() {
		attentionNewsThread.collect();
	}
	@Test
	public void attentionNewsPLTest() {
		attentionNewsplThread.collect();
	}
	@Test
	public void spreadNewsAnalysisTest() {
		spreadNewsAnalysisThread.compare();
	}
	@Test
	public void spreadNewsTest() {
		spreadNewsThread.collect();
	}
}
