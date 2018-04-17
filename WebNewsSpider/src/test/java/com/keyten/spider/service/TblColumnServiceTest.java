package com.keyten.spider.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.keyten.base.bean.TBLColumn;
import com.keyten.spider.service.TBLColumnService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TblColumnServiceTest {
	@Autowired
	TBLColumnService columnservice;
	
	@Test
	public void testGetAll() {
		String ipaddr = "192.168.10.53";
		Map<String,TBLColumn> allColumnAndRules = columnservice.getAllColumnAndRules(ipaddr);
		System.out.println( allColumnAndRules.size() + "sdfdsafs");
		for(Map.Entry<String,TBLColumn> entry : allColumnAndRules.entrySet()) {
			System.out.println(entry.getKey()+
					" u:"+entry.getValue().getUrlcfg().size()+
					" e:" + entry.getValue().getElecfg().size() );
		}
		
			
	}
	@Test
	public void testGetById() {
		String id = "CI0000000010";
		TBLColumn column = (TBLColumn) columnservice.getById(id);
		System.out.println(column.getColumnname());
	}
	@Test
	public void testUpdate() {
		String id = "CI0000000010";
		TBLColumn column = (TBLColumn) columnservice.getById(id);
		column.setColumnname(column.getColumnname()+"!!!");
		columnservice.update(column);
	}
	@Test
	public void testTaskColumnids() {
		String ipaddr = "192.168.10.53";
		List<String> columnids = columnservice.getTaskColumnIds(ipaddr);
		System.out.println( columnids.size() + "sdfdsafs");
		for(String columnid : columnids) {
			System.out.println(columnid);
		}
	}
}
