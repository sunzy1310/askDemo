package com.keyten.base.dao;

import com.keyten.base.bean.TBLHotThing;

public interface TBLHotThingMapper {
    int deleteByPrimaryKey(String thingid);

    int insert(TBLHotThing record);

    int insertSelective(TBLHotThing record);

    TBLHotThing selectByPrimaryKey(String thingid);

    int updateByPrimaryKeySelective(TBLHotThing record);

    int updateByPrimaryKey(TBLHotThing record);
}