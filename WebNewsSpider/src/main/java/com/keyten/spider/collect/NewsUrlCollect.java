package com.keyten.spider.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLColumn;
import com.keyten.base.bean.TBLPost;
import com.keyten.base.bean.TBLUrlConfig;
import com.keyten.base.util.HtmlUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.spider.controller.CollectNewsHelper;

@Component
@Scope("prototype")
public class NewsUrlCollect extends BaseCollect implements Runnable{
	@Autowired
	private SpringContextUtil springUtil;
	private TBLColumn column;
	public void run() {
		try {
			//获取栏目页内容
			String html = super.
					getHtml(column.getColumnurl(),column.getColumncharset());
			if(!html.equals("")){
				regexUrl(html);
			}
		} catch (Exception e) {
			if(e.getMessage()!=null&&e.getMessage().contains("time out")) {
				LogUtil.addLogError(column.getColumnurl()+"访问超时:",this.getClass().getName());
			}else {
				LogUtil.addLogError(column.getColumnurl()+"采集异常:",this.getClass().getName());
			}
		}
	}
	private void regexUrl(String html){
		try {
			HtmlUtil htmlUtil = new HtmlUtil();
			List<TBLUrlConfig> urls = column.getUrlcfg();
			//用于存放直接采集到的URL
			Map<String,String> mmap = new HashMap<String,String>();
			//用于存放  相对/绝对  处理过后最终版的ＵＲＬ
			List<TBLPost> artList = new ArrayList<>();
			for(int i = 0 ; i < urls.size() ; i++){
				TBLUrlConfig urlcfg = urls.get(i);
				//URL规则包含匹配范围  则定位标签范围内的内容  若无定义则从全文查找符合的链接
				if(null!=urlcfg.getMatchrange()&&!"".equals(urlcfg.getMatchrange())){
					if(html.indexOf(urlcfg.getMatchrange())>-1)
						html = htmlUtil.unDoNestTag(html, urlcfg.getMatchrange());
					else
						html = "";
				}
				//开始通过正则 规则匹配所有URL信息
				Pattern pattern = Pattern.compile(urlcfg.getMatchrule());// 创建 Pattern 对象
			    Matcher matcher = pattern.matcher(html);//现在创建 matcher 对象
			    while(matcher.find()){
			    	String url = matcher.group();
			    	if(mmap.get(url)==null){//新匹配到文章地址  需要保存
			    		mmap.put(url,url);
			    		url = htmlUtil.fmtUrl(url);
			    		//检查本栏目下的文章是  相对匹配/绝对匹配    并 拼接前缀
				    	if(column.getMatchtype().equals("1")){
				    		url = column.getBcontent()+url;
				    	}
				    	TBLPost post = (TBLPost) springUtil.getBean("TBLPost");
				    	post.setColumnid(column.getColumnid());
				    	post.setArturl(url);
				    	post.setWebid(column.getWebid());
				    	artList.add(post);
			    	}
			    }
			}
			if(artList.size()==0){
				TBLPost ai = (TBLPost) springUtil.getBean("TBLPost");
				ai.setColumnid(column.getColumnid());
				ai.setArturl("");
				ai.setWebid(column.getWebid());
		    	artList.add(ai);
			}
			//最后将所有处理过的URL信息保存到公共对象中   用于URL判断线程去重
			CollectNewsHelper.urlQueue.addAll(artList);
			LogUtil.addLogInfo("当前对比队列任务数："+CollectNewsHelper.urlQueue.size(), this.getClass().getName());
		} catch (Exception e) {
			LogUtil.addLogError(column.getColumnurl()+"采集uRL异常:",this.getClass().getName());
		}
	}
	public void setColumn(TBLColumn column) {
		this.column = column;
	}
	
}
