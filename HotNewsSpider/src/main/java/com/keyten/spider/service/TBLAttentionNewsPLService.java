package com.keyten.spider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyten.base.bean.TBLAttentionHotNews;
import com.keyten.base.bean.TBLAttentionHotNewsPL;
import com.keyten.base.dao.TBLAttentionHotNewsMapper;
import com.keyten.base.dao.TBLAttentionHotNewsPLMapper;

@Service
public class TBLAttentionNewsPLService {
	@Autowired
	TBLAttentionHotNewsPLMapper attentionHotNewsPLMapper;
	@Autowired
	TBLAttentionHotNewsMapper attentionHotNewsMapper;
	
	public void savePLAndUpdateNews(
			List<TBLAttentionHotNewsPL> plList,TBLAttentionHotNews hotnews) {
		//1 删除本条新闻相关评论
		attentionHotNewsPLMapper.deleteByPrimaryKey(hotnews.getNewsid());
		//2 添加本次采集的评论
		attentionHotNewsPLMapper.insertList(plList);
		//3 修改新闻评论数和最后采集日期时间
		attentionHotNewsMapper.updateByPrimaryKeySelective(hotnews);
	}
}
