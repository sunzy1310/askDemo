package com.keyten.spider.collect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLCollectWeb;
import com.keyten.base.util.HtmlUtil;
import com.keyten.base.util.LogUtil;

@Component
public class BaseCollectUrl extends BaseCollect{
	private List<TBLCollectWeb> columns;

	//	private String[][] columns = {{"http://www.sina.com.cn/","","http://news\\.sina\\.com\\.cn/[a-z]+/([a-z]+/)?\\d{4}-\\d{2}-\\d{2}/doc-[a-z0-9]{15}\\.shtml"},};
	private String charSet = "GBK";
	private String contentType = "html";
	/*
	 * 
	 */
	public Map<String,String[]> collectIndex(){
		Map<String,String[]> map = new HashMap<String, String[]>();
		try {
			for(TBLCollectWeb column : columns){
				//本次采集的url主页
				String url = column.getColumnurl();
				//采集到的文章地址前缀 （相对地址时候用到， 一遍为 host） 
				String host = column.getUrlhost()==null?"":column.getUrlhost(); 
				//URL正则表达式
				String regexURL = column.getUrlrule();
				String columnid = column.getColumnid();
				//httpclient 访问column地址得到访问后的页面内容
				String content = super.collect(url, charSet);
				if(!content.equals("")){
					Map<String,String[]> mmap = null;
					if(contentType.equals("html")){
						mmap = regexURL(content,regexURL,host,columnid);
					}else{
						mmap = jsonUrl(content,regexURL,host,columnid);
					}
					if(mmap!=null){
						map.putAll(mmap);
					}
				}
			}
			
		} catch (Exception e) {
			String error = "URL主页采集异常 \r\n" + e.toString();
			LogUtil.addLogError(error,this.getClass().getName());
		}
		return map;
	}
	/**
	 * 返回url和标题的集合
	 * 此方法适用于html页面匹配     格式为   <a href="url地址" ... >标题</a>
	 * @param content
	 * @return
	 */
	public Map<String,String[]> regexURL(String content,String matchrule,String host,String columnid){
		Map<String,String[]> map = null;
		HtmlUtil htmlUtil = new HtmlUtil();
		try {
			// 创建 Pattern 对象
			Pattern pattern = Pattern.compile(matchrule);
			// 现在创建 matcher 对象
		    Matcher matcher = pattern.matcher(content);
		    map = new HashMap<String, String[]>();
			Map<String,String> mmap = new HashMap<String,String>();
			String title = "";
		    while(matcher.find()){
		    	String url = matcher.group();
		    	if(mmap.get(url)==null){
		    		mmap.put(url,url);
		    		try {
			    		//获取到url后定位URL 对应的文章标题   (各个网站略有区别)
			    		String tempCon = content.substring(content.indexOf(url));
			    		title = tempCon.substring(tempCon.indexOf(">")+1,tempCon.indexOf("</a>"));
			    		
		    			if(title.indexOf("<img")>-1){
		    				String str = title.substring(title.indexOf("alt=\"")+4,title.indexOf("alt=\"")+5);
			    			title = title.substring(title.indexOf("alt=")+5);
			    			title = title.substring(0,title.indexOf(str));
			    		}
			    		if(title.indexOf("<div")>-1||title.equals("")){
			    			continue;
			    		}
					} catch (Exception e) {
//						System.out.println(url + "采集列表出错了");
						continue;
					}
		    		
		    		//去掉回车换行符
		    		title = title.replaceAll("\r", "");//换行
		    		title = title.replaceAll("\n", "");//回车
		    		title = htmlUtil.delHTMLTag(title, " ");
		    		url = host + htmlUtil.fmtUrl(url);
		    		String[] newsinfo = new String[2];
		    		newsinfo[0] = title;
		    		newsinfo[1] = columnid;
		    		map.put(url,newsinfo);
//		    		System.out.println("url"+url+" title"+title+" cid:"+columnid);
		    	}
		    }
		} catch (Exception e) {
			String error = columnid+"URL分析异常 \r\n" + e.toString();
			LogUtil.addLogError(error,this.getClass().getName());
		}
		return map;
	}
	
	public Map<String,String[]> jsonUrl(String content,String matchrule,String host,String columnid){
		Map<String,String[]> map = null;
		HtmlUtil htmlUtil = new HtmlUtil();
		try {
		    map = new HashMap<String, String[]>();
		    int index = -1;
		    while((index = content.indexOf("\"title\":\""))>-1){
		    	content = content.substring(index+9);
		    	String title = content.substring(0,content.indexOf("\""));
		    	content = content.substring(content.indexOf("url\":\"")+6);
		    	String url = content.substring(0,content.indexOf("\""));
		    	
		    	if(url.matches(matchrule)){
		    		title = htmlUtil.delHTMLTag(title, " ");
		    		url = host + htmlUtil.fmtUrl(url);
		    		String[] newsinfo = new String[2];
		    		newsinfo[0] = title;
		    		newsinfo[1] = columnid;
		    		map.put(url,newsinfo);
		    	}
		    }
		} catch (Exception e) {
			String error = columnid+"URL分析异常 \r\n" + e.toString();
			LogUtil.addLogError(error,this.getClass().getName());
		}
		return map;
	}
	
	public void setColumns(List<TBLCollectWeb> columns) {
		this.columns = columns;
	}	
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
