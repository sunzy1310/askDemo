package com.keyten.spider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyten.base.bean.TBLPost;
import com.keyten.base.dao.TBLPostMapper;

@Service
public class TBLPostService{
	@Autowired
	TBLPostMapper tblPostMapper;
	
	public int save(TBLPost post) {
		return tblPostMapper.insertSelective(post);
	}
	public long getPKBySeqName() {
		String seqname = "Seq_TBLPost";
		long pk = tblPostMapper.selectPKBySeqName(seqname);
		return pk;
	}
}