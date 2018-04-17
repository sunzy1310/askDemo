package com.keyten.spider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyten.base.bean.TBLTarget;
import com.keyten.base.dao.TBLTargetMapper;

@Service
public class TBLTargetService {
	@Autowired
	TBLTargetMapper tblTargetMapper;
	
	public void save(TBLTarget target) {
		tblTargetMapper.insert(target);
	}
	
	public List<String> getTargetByIp(String ipaddr) {
		return tblTargetMapper.selectIdByIpaddr(ipaddr);
	}
}
