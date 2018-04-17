package com.keyten.base.dao;

import java.util.List;

import com.keyten.base.bean.TBLTarget;

public interface TBLTargetMapper {
    int deleteByPrimaryKey(String targetid);

    int insert(TBLTarget record);

    int insertSelective(TBLTarget record);

    int updateByPrimaryKeySelective(TBLTarget record);

    int updateByPrimaryKey(TBLTarget record);

    TBLTarget selectByPrimaryKey(String targetid);
    
    List<String> selectIdByIpaddr(String ipaddr);
}