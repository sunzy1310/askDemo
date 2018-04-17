package com.keyten.base.dao;

import java.util.List;
import com.keyten.base.bean.TBLHotNews;

public interface TBLHotNewsMapper {
    int deleteByPrimaryKey(String newsid);

    int insert(TBLHotNews record);

    int insertSelective(TBLHotNews record);

    TBLHotNews selectByPrimaryKey(String newsid);

    int updateByPrimaryKeySelective(TBLHotNews record);

    int updateByPrimaryKey(TBLHotNews record);
    
    List<TBLHotNews> selectThingByUrls(List<String> urls);
    
    List<TBLHotNews> selectNewsByIp(String ipaddr);
}