package com.keyten.spider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyten.base.bean.TBLAttentionHotNews;
import com.keyten.base.dao.TBLAttentionHotNewsMapper;

@Service
public class TBLAttentionNewsService {
	@Autowired
	TBLAttentionHotNewsMapper hotNewsMapper;
	
	public int save(TBLAttentionHotNews news) {
		return hotNewsMapper.insert(news);
	}
	
	public int getCountByUrlAndTargetId(String tid,String url) {
		return hotNewsMapper.selectCountByUrlAndTargetId(tid, url);
	}
	
	public long getPKBySeqName() {
		String seqname = "Seq_AttentionNews";
		long pk = hotNewsMapper.selectPKBySeqName(seqname);
		return pk;
	}
	
	public List<TBLAttentionHotNews> getAttNewsByIPAndDate(String ipaddr,String datetime){
		return hotNewsMapper.selectAttNewsByIPAndDate(ipaddr, datetime);
	}
}
