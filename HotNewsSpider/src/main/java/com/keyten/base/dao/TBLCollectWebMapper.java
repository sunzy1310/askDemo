package com.keyten.base.dao;

import java.util.List;

import com.keyten.base.bean.TBLCollectWeb;

public interface TBLCollectWebMapper {
    int deleteByPrimaryKey(String columnid);

    int insert(TBLCollectWeb record);

    int insertSelective(TBLCollectWeb record);

    int updateByPrimaryKeySelective(TBLCollectWeb record);

    int updateByPrimaryKey(TBLCollectWeb record);

    TBLCollectWeb selectByPrimaryKey(String columnid);
    
    List<TBLCollectWeb> selectCollectWebByTargetID(String targetId) ;
}