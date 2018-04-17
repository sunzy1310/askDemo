package com.keyten.spider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyten.base.bean.TBLCollectWeb;
import com.keyten.base.dao.TBLCollectWebMapper;

@Service
public class TBLCollectWebService {
	@Autowired
	TBLCollectWebMapper collectWebMapper;
	
	public void update(){
		
	}
	
	public List<TBLCollectWeb> getCollectWebByTargetId(String targetId){
		return collectWebMapper.selectCollectWebByTargetID(targetId);
	}
}
