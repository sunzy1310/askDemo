package com.keyten.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.keyten.base.bean.TBLAttentionHotNews;

public interface TBLAttentionHotNewsMapper {
    int deleteByPrimaryKey(String newsid);

    int insert(TBLAttentionHotNews record);

    int insertSelective(TBLAttentionHotNews record);

    TBLAttentionHotNews selectByPrimaryKey(String newsid);

    int updateByPrimaryKeySelective(TBLAttentionHotNews record);

    int updateByPrimaryKey(TBLAttentionHotNews record);
    
    int selectCountByUrlAndTargetId(@Param("tid")String tid,@Param("url")String url);
    
    long selectPKBySeqName(String seqname);
    
    List<TBLAttentionHotNews> selectAttNewsByIPAndDate(@Param("ipaddr")String ipaddr,@Param("datetime")String datetime);
}