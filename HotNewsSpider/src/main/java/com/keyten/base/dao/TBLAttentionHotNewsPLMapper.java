package com.keyten.base.dao;

import java.util.List;

import com.keyten.base.bean.TBLAttentionHotNewsPL;

public interface TBLAttentionHotNewsPLMapper {
    int deleteByPrimaryKey(String plid);

    int insert(TBLAttentionHotNewsPL record);

    int insertSelective(TBLAttentionHotNewsPL record);
    
    int insertList(List<TBLAttentionHotNewsPL> pllist);

    TBLAttentionHotNewsPL selectByPrimaryKey(String plid);

    int updateByPrimaryKeySelective(TBLAttentionHotNewsPL record);

    int updateByPrimaryKey(TBLAttentionHotNewsPL record);
}