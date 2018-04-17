package com.keyten.base.dao;

import com.keyten.base.bean.TBLPost;

public interface TBLPostMapper {
    int deleteByPrimaryKey(String articleid);

    int insert(TBLPost record);

    int insertSelective(TBLPost record);

    int updateByPrimaryKeySelective(TBLPost record);

    int updateByPrimaryKey(TBLPost record);
    
    TBLPost selectByPrimaryKey(String articleid);
    
    long selectPKBySeqName(String seqname);
}