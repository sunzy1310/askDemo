package com.keyten.base.dao;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;

import com.keyten.base.bean.TBLNewspaperWebConfig;

public interface TBLNewspaperWebConfigMapper {
    int deleteByPrimaryKey(String webruleid);

    int insert(TBLNewspaperWebConfig record);

    int insertSelective(TBLNewspaperWebConfig record);

    TBLNewspaperWebConfig selectByPrimaryKey(String webruleid);
    
    @MapKey("webruleid")
   	Map<String,TBLNewspaperWebConfig> selectAllColumnAndRules(String ipaddr);

    int updateByPrimaryKeySelective(TBLNewspaperWebConfig record);

    int updateByPrimaryKey(TBLNewspaperWebConfig record);
}