package com.keyten.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.keyten.base.bean.TBLColumn;

public interface TBLColumnMapper {
    int deleteByPrimaryKey(String columnid);

    int insert(TBLColumn record);

    int insertSelective(TBLColumn record);

    TBLColumn selectByPrimaryKey(String columnid);
    @MapKey("columnid")
	Map<String,TBLColumn> selectAllColumnAndRules(String ipaddr);
	
	TBLColumn selectColumnAndRulesById(String columnid);
	
	List<String> selectTaskColumnIds(@Param("ipaddr")String ipaddr,@Param("isHottime")int isHottime);

    int updateByPrimaryKeySelective(TBLColumn record);

    int updateByPrimaryKey(TBLColumn record);

}