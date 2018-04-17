package com.keyten.spider.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.keyten.spider.service.TBLPostService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TblPostServiceTest {
	@Autowired
	TBLPostService postservice;
	
	@Test
	public void testGetPK() {
		long pk = postservice.getPKBySeqName();
		System.out.println(pk);
	}
}
