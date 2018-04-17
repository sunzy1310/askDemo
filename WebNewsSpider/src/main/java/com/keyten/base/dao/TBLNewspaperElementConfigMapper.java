package com.keyten.base.dao;

import com.keyten.base.bean.TBLNewspaperElementConfig;

public interface TBLNewspaperElementConfigMapper {
    int deleteByPrimaryKey(String ruleid);

    int insert(TBLNewspaperElementConfig record);

    int insertSelective(TBLNewspaperElementConfig record);

    TBLNewspaperElementConfig selectByPrimaryKey(String ruleid);

    int updateByPrimaryKeySelective(TBLNewspaperElementConfig record);

    int updateByPrimaryKey(TBLNewspaperElementConfig record);
}