package com.keyten.base.dao;

import com.keyten.base.bean.TBLWeb;

public interface TBLWebMapper {
    int deleteByPrimaryKey(String webid);

    int insert(TBLWeb record);

    int insertSelective(TBLWeb record);

    TBLWeb selectByPrimaryKey(String webid);

    int updateByPrimaryKeySelective(TBLWeb record);

    int updateByPrimaryKey(TBLWeb record);
}