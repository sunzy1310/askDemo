package com.keyten.spider.collect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.keyten.base.bean.TBLNewspaperWebConfig;
import com.keyten.base.bean.TBLPost;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.spider.controller.CollectPaperHelper;
/**
 * 报刊以日刊为例
 * 每天生成一篇刊物，
 * 	1   每天的刊物的地址格式都是  http://www.host.com/a/2018-04-08 
 * 	          后面的日期每天修改一下，便是每天需要采集的报刊页地址
 *  2 跳转的方式获取到对应的报刊页地址
 * 然后通过报刊页地址获取到报刊中所有版面的地址
 * 然后再从每一个版面中获取到对应的文章列表
 * @author liym
 */
@Component
@Scope("prototype")
public class PaperUrlCollect extends BaseCollect implements Runnable{
	@Autowired
	private SpringContextUtil springUtil;
	private TBLNewspaperWebConfig webconfig;
	@Override
	public void run() {
		try {
			//获取当天刊物地址
			String paperUrl = getPaperUrl();
			if(!StringUtils.isEmpty(paperUrl)) {
				//获取所有版面地址
				String columnRule = webconfig.getColumnrule();
				JSONArray columnurls = getUrlList(paperUrl,columnRule);
				//获取所有新闻地址
				getArtUrls(columnurls);
			}
		} catch (Exception e) {
			if(e.getMessage()!=null&&e.getMessage().contains("time out")) {
				LogUtil.addLogError("访问超时:",this.getClass().getName());
			}else {
				e.printStackTrace();
			}
		}
	}
	private void getArtUrls(JSONArray columnurls) {
		String artrule = webconfig.getArticlerule();
		List<TBLPost> artList = new ArrayList<>();
		for(int i = 0,l = columnurls.length() ; i<l ; i++) {
			try {
				String columnurl = columnurls.getString(i);
				Thread.sleep(500);
				JSONArray artUrls = this.getUrlList(columnurl,artrule);
				for(int j = 0,len = artUrls.length() ; j < len ; j++ ) {
					String arturl = artUrls.getString(j);
					TBLPost post = (TBLPost) springUtil.getBean("TBLPost");
			    	post.setColumnid(webconfig.getWebruleid());
			    	post.setArturl(arturl);
			    	post.setWebid(webconfig.getWebid());
			    	artList.add(post);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if(artList.size()==0) {
			TBLPost ai = (TBLPost) springUtil.getBean("TBLPost");
			ai.setColumnid(webconfig.getWebruleid());
			ai.setArturl("");
			ai.setWebid(webconfig.getWebid());
	    	artList.add(ai);
		}
		CollectPaperHelper.urlQueue.addAll(artList);
	}
	//访问报刊页地址   返回版面地址 jsonarray对象
	private JSONArray getUrlList(String paperUrl,String regex) {
		JSONArray columnurls = new JSONArray();
		try {
			if(StringUtils.isEmpty(regex)) {
				columnurls.put(paperUrl);
			}else {
				//获取
				String html = super.getHtml(paperUrl,webconfig.getColumncharset());
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(html);
				Set<String> set = new HashSet<>();
				while(matcher.find()) {
					String url = matcher.group(0);
					if(!set.contains(url)) {
						set.add(url);
						columnurls.put(url);
					}
				}
			}
		} catch (Exception e) {
			
		}
		return columnurls;
	}
	//获取报刊页地址
	private String getPaperUrl() {
		String url = "";
		try {
			if(webconfig.getWebruletype().equals("DATE")){
				String pubdate=DateTimeUtil.getDateByLong(System.currentTimeMillis());
				String YYYY=pubdate.split("-")[0];
				String MM=pubdate.split("-")[1];
				String DD=pubdate.split("-")[2];
				url=webconfig.getWebrule().replace("[YYYY]", YYYY).replace("[MM]", MM).replace("[DD]", DD).replace("[yyyy]", YYYY).replace("[mm]", MM).replace("[dd]", DD).replace("[yyyymm]", YYYY+MM).replace("[yyyymmdd]", YYYY+MM+DD).replace("[YYYYMM]", YYYY+MM).replace("[YYYYMMDD]", YYYY+MM+DD);
				return url;
			}else {
				String[] mRules = webconfig.getWebrule().split("&&&");
				if(mRules[0].length()>=1){
					url = webconfig.getWeburl();
					//获取栏目页内容
					String html = super.getHtml(webconfig.getWeburl(),webconfig.getColumncharset());
					if((html.indexOf(mRules[0])>-1)){
						int fristIndex = html.indexOf(mRules[0])+mRules[0].length();
						url = html.substring(fristIndex);
						int lastIndex = url.indexOf(mRules[1]);
						if(lastIndex>-1){
							url = url.substring(0,url.indexOf(mRules[1]));
							//路劲补全
							url = getRightUrl(webconfig.getWeburl(),url);  
						}
					}
				}
				
			}
		} catch (Exception e) {
			
		}
		return url;
	}
	//将相对路劲自动匹配对比，拼接处绝对路劲如：   ../path/a.html变成  http://www.host.com/path/a.html
	public String getRightUrl(String weburl,String outurl){
		try {
		   while(outurl.indexOf("http://")!=0){
			  if(outurl.indexOf("../")==0){
				  String weburlbegin=weburl;
				while(outurl.indexOf("../")==0){
				  weburlbegin=weburlbegin.substring(0, weburlbegin.lastIndexOf("/"));
				  weburlbegin=weburlbegin.substring(0, weburlbegin.lastIndexOf("/")+1);
				  outurl= outurl.substring(3) ;
				}
				outurl=weburlbegin+outurl;
			  }else if(outurl.indexOf("./")==0){
				  outurl= weburl.substring(0, weburl.lastIndexOf("/"))+outurl.substring(1) ;
			  }else if(outurl.indexOf("/")==0){
				  outurl= weburl.substring(0, weburl.indexOf("/", 8))+outurl ;
			  }else{ // 类似 html/2016-03/14/node_2.htm的形式
				  outurl= weburl.substring(0, weburl.lastIndexOf("/")+1)+outurl ;
			  }
		   }
		} catch (Exception e) {
		   e.printStackTrace();
		}
		return outurl; 
	}
	

	public void setWebConfig(TBLNewspaperWebConfig webconfig) {
		this.webconfig = webconfig;
	}
}
