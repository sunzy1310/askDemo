package com.keyten.spider.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keyten.base.bean.TBLColumn;
import com.keyten.base.dao.TBLColumnMapper;

@Service
public class TBLColumnService {
	@Autowired
	TBLColumnMapper tblColumnMapper;
	
	public void save(Object obj) {
		TBLColumn tblColumn = (TBLColumn) obj;
		tblColumnMapper.insert(tblColumn);
	}
	
	public void update(Object obj) {
		TBLColumn tblColumn = (TBLColumn) obj;
		tblColumnMapper.updateByPrimaryKeySelective(tblColumn);
	}
	
	public Object getById(String id) {
		TBLColumn tblColumn = tblColumnMapper.selectByPrimaryKey(id);
		return tblColumn;
	}
	//获取所有当前服务器需要采集的规则和栏目信息
	public Map<String,TBLColumn> getAllColumnAndRules(String ipaddr) {
		return tblColumnMapper.selectAllColumnAndRules(ipaddr);
	}

	//获取指定栏目的规则信息
	public TBLColumn getColumnAndRulesById(String id){
		return tblColumnMapper.selectColumnAndRulesById(id);
	}
	
	//获取指定栏目的规则信息
	public List<String> getTaskColumnIds(String ipaddr){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if((hour>5&&hour<11)||(hour>12&&hour<15))
			return tblColumnMapper.selectTaskColumnIds(ipaddr, 1);
		else
			return tblColumnMapper.selectTaskColumnIds(ipaddr, 0);
		
	}
	
}
