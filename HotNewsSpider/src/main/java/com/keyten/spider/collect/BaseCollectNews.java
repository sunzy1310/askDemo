package com.keyten.spider.collect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.util.LogUtil;

/**
 * 热点新闻采集主类
 * @author liym
 * 2017-02-06
 */
@Component
public class BaseCollectNews extends BaseCollect{
	@Autowired
	BaseNewsAnalysis analysis;
	
	/**
	 * 文章内容采集 分析 主方法
	 * @param newsurl
	 * @return
	 */
	public String[] collectNews(String newsurl){
		String[] newsinfo = new String[8];
		try {
			if(newsurl.indexOf("sina.com")>-1){
				//新浪新闻采集
				String content = super.collect(newsurl, "UTF-8");
				if(content.indexOf("页面没有找到 5秒钟之后将会带您进入新浪首页")>-1){
					System.out.println("页面没有找到");
				}else{
					newsinfo = analysis.getSinaNewsInfo(content);
				}
				
			}else if(newsurl.indexOf("sohu.com")>-1){
				//搜狐新闻采集
				String content = super.collect(newsurl, "GBK");
				newsinfo = analysis.getSohuNewsInfo(content,newsurl);
			}else if(newsurl.indexOf("163.com")>-1){
				//网易新闻采集
				String content = super.collect(newsurl, "GBK");
				if((content.indexOf("网易公益")>-1 && content.indexOf("404_page")>-1)||content.indexOf("special/special.html")>-1){
					System.out.println("404页面了");
				}else{
					newsinfo = analysis.getWangYiNewsInfo(content,newsurl);
				}
			}else if(newsurl.indexOf("qq.com")>-1){
				//腾讯新闻采集
				String content = super.collect(newsurl, "GBK");
				newsinfo = analysis.getQQNewsInfo(content);
			}else if(newsurl.indexOf("ifeng.com")>-1){
				//凤凰新闻采集
				String content = super.collect(newsurl, "UTF-8");
				newsinfo = analysis.getIfengNewsInfo(content,newsurl);
			}else if(newsurl.indexOf("thepaper.cn")>-1){
				//澎湃新闻采集
				String content = super.collect(newsurl, "UTF-8");
				if(content.indexOf("<p class=\"news_txt\" style=\"padding:140px 0;text-align:center\">此文章已下线</p>")>-1){
					System.out.println("此文章已经下线啦");
				}else
					newsinfo = analysis.getThePaperNewsInfo(content);
			}
		} catch (Exception e) {
			String error = newsurl+"\r\n" + e.toString();
			LogUtil.addLogError(error,this.getClass().getName());
		}
		return newsinfo;
	}
	/**
	 * 评论数采集（只采集评论数不采集评论内容，用于判断是否为网民关注热点新闻）
	 * @param newsplurl
	 * @param newsinfo
	 * @param targetId
	 * @return
	 */
	public int collectPLCount(String newsplurl,String newsinfo,String targetId) {
		int showcount = 0;
		try {
			if(newsplurl.indexOf("sina.com")>-1){
				String content = super.collect(newsplurl,"1","1","", "GBK");
				String showcountStr = content.substring(content.indexOf("show")+6);
				showcount = Integer.parseInt(showcountStr.substring(0,showcountStr.indexOf("}")).trim());
				if(targetId.equals("1")||targetId.equals("7")){//全国热点和全国财经
					if(showcount<1000)
						showcount = 0;
				}else{//省级热点
					if(showcount<50)
						showcount = 0;
				}
			}else if(newsplurl.indexOf("ifeng.com")>-1){
				String content = super.collect(newsplurl,"1","1","", "UTF-8");
				String showcountStr = content.substring(content.indexOf("count")+7);
				showcount = Integer.parseInt(showcountStr.substring(0,showcountStr.indexOf(",")).trim());
				if(targetId.equals("1")||targetId.equals("7")){//全国热点和全国财经
					if(showcount<1000)
						showcount = 0;
				}else{//省级热点
					if(showcount<50)
						showcount = 0;
				}
			}else if(newsplurl.indexOf("sohu.com")>-1){
				String content = super.collect(newsplurl,"1","1","", "UTF-8");
				String showcountStr = content.substring(content.indexOf("cmt_sum")+9);
				showcount = Integer.parseInt(showcountStr.substring(0,showcountStr.indexOf(",")).trim());
				if(targetId.equals("1")||targetId.equals("7")){//全国热点和全国财经
					if(showcount<200)
						showcount = 0;
				}else{//省级热点
					if(showcount<50)
						showcount = 0;
				}
			}else if(newsplurl.indexOf("thepaper.cn")>-1){
				showcount = 0;
				if(newsinfo.indexOf("k")>-1){
					newsinfo = newsinfo.substring(0,newsinfo.length()-1);
					showcount = (int) (Double.parseDouble(newsinfo) * 1000);
				}else{
					showcount = Integer.parseInt(newsinfo);
				}
				if(showcount<2500)
					showcount = 0;
			}else if(newsplurl.indexOf("qq.com")>-1){
				String content = super.collect(newsplurl,"1","1","0", "GBK");
				System.out.println(newsplurl);
				String showcountStr = content.substring(content.indexOf("commentnum")+13);
				showcount = Integer.parseInt(showcountStr.substring(0,showcountStr.indexOf("\"")).trim());
				if(targetId.equals("1")||targetId.equals("7")){//全国热点和全国财经
					if(showcount<1000)
						showcount = 0;
				}else{//省级热点
					if(showcount<150)
						showcount = 0;
				}
			}else if(newsplurl.indexOf("163.com")>-1){
				String content = super.collect(newsplurl,"1","1","", "GBK");
				String showcountStr = content.substring(content.indexOf("newListSize")+13);
				showcount = Integer.parseInt(showcountStr.substring(0,showcountStr.indexOf("}")).trim());
				if(targetId.equals("1")||targetId.equals("7")){//全国热点和全国财经
					if(showcount<1000)
						showcount = 0;
				}else{//省级热点
					if(showcount<100)
						showcount = 0;
				}
			}
		}catch (Exception e) {
			String error = newsplurl+"\r\n" + e.toString();
			LogUtil.addLogError(error,this.getClass().getName());
			showcount = 0;			
		}
		return showcount;
	}
}
