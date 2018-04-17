package com.keyten.spider.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keyten.base.bean.TBLNewspaperWebConfig;
import com.keyten.base.dao.TBLNewspaperWebConfigMapper;

/**
 * 报刊栏目规则service
 * @author liym
 */
@Service
public class TBLNewspaperWebConfigService {
	@Autowired
	TBLNewspaperWebConfigMapper newspaperWebCfgMapper;
	public void update(TBLNewspaperWebConfig newspaperWebCfg) {
		newspaperWebCfgMapper.updateByPrimaryKeySelective(newspaperWebCfg);
	}
	
	//获取报刊部分的所有规则信息
	public Map<String, TBLNewspaperWebConfig> getAllWebCfgAndRules(String ipAddr) {
		// TODO Auto-generated method stub
		return null;
	}
	//获取指定栏目的规则信息
	public List<String> getTaskWebCfgIds(String ipaddr){
		return null;
	}

	public TBLNewspaperWebConfig getWebCfgAndRulesById(String columnid) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
