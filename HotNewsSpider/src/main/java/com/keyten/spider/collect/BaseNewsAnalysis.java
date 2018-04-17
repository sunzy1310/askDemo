package com.keyten.spider.collect;

import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.HtmlUtil;

/**
 * 新浪新闻采集和分析
 * @author liym
 * 2017-02-06
 */
@Component
public class BaseNewsAnalysis {
	/**
	 * 网易新闻分析
	 * @param contentHTML
	 * @return
	 */
	public String[] getWangYiNewsInfo(String contentHTML,String newsUrl){
		String[] newsinfo = new String[8];
		String title = "";
		String datetime = "";
		String author = "";
		String source = "";
		String content = "";
		HtmlUtil hUtil = new HtmlUtil();
		
//		Document dom = Jsoup.parse(contentHTML);
		//标题采集
		String tempHtml = contentHTML.substring(contentHTML.indexOf("<h1")+1);
		title = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</h1>")).trim();
		
		//时间采集
		tempHtml = contentHTML.substring(contentHTML.indexOf("</h1>"));
		datetime = tempHtml.substring(0,tempHtml.indexOf("来")).trim();
		
		//来源采集
		int indexStr = contentHTML.indexOf("id=\"ne_article_source\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</a>")).trim();
		}
		
		//作者采集
		indexStr = contentHTML.indexOf("ep-editor");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+16);
			author = tempHtml.substring(0,tempHtml.indexOf("</span>")).trim();
		}
		//内容采集
		indexStr = contentHTML.indexOf("id=\"endText\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr);
			if(tempHtml.indexOf("<div class=\"ep-source")>-1)
				content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<div class=\"ep-source")).trim();
			else if(tempHtml.indexOf("<p class=\"f_center erweima_big\"")>-1)
				content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<p class=\"f_center erweima_big\"")).trim();
			else
				content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<div class=\"post_btmshare\"")).trim();
		}
		//评论页面访问地址
		String newsid = newsUrl.substring(newsUrl.lastIndexOf("/")+1,newsUrl.indexOf(".html"));
		long time = System.currentTimeMillis();
		String commentUrl = "http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/"+newsid+"/comments/newList?offset={pageno}&limit={pagesize}&showLevelThreshold=72&headLimit=1&tailLimit=2&callback=getData&ibc=newspc&_="+time;
		
		//数据装载到 数组中
		newsinfo[0] = hUtil.delHTMLTag(title, "");
		newsinfo[1] = hUtil.delHTMLTag(author, "");
		newsinfo[2] = hUtil.delHTMLTag(source, "");
		newsinfo[3] = hUtil.delHTMLTag(content, "");
		newsinfo[4] = DateTimeUtil.fmtDate(datetime);
		newsinfo[5] = DateTimeUtil.fmtTime(datetime);
		newsinfo[6] = commentUrl;
		
		return newsinfo;
	}
	/**
	 * 新浪新闻分析
	 * @param contentHTML
	 * @return
	 */
	public String[] getSinaNewsInfo(String contentHTML){
		String[] newsinfo = new String[8];
		String title = "";
		String datetime = "";
		String author = "";
		String source = "";
		String content = "";
		HtmlUtil hUtil = new HtmlUtil();
		
		//标题采集
		int indexStr = contentHTML.indexOf("<h1 class=\"main-title\"");
		String tempHtml = "";
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+1);
			title = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</h1>")).trim();
		}else if((indexStr = contentHTML.indexOf("<h1>"))>-1){
			tempHtml = contentHTML.substring(indexStr+4);
			title = tempHtml.substring(0,tempHtml.indexOf("</h1>")).trim();
		}else if((indexStr = contentHTML.indexOf("<h1 id=\"artibodyTitle\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			title = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</h1>")).trim();
		}
		
		//时间采集
		indexStr = contentHTML.indexOf("class=\"time-source\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr);
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<span>")).trim();
		}else if((indexStr = contentHTML.indexOf("class=\"source-time\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
		}else if((indexStr = contentHTML.indexOf("<span class=\"date\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
		}else if((indexStr = contentHTML.indexOf("<span id=\"pub_date\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
		}
		//来源采集
		indexStr = contentHTML.indexOf("\"media_name\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+15);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</a>")).trim();
		}else if((indexStr = contentHTML.indexOf("id=\"art_source\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<span>")).trim();
		}else if((indexStr = contentHTML.indexOf("class=\"source"))>-1){
			tempHtml = contentHTML.substring(indexStr);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<")).trim();
		}
		
		//作者采集
		indexStr = contentHTML.indexOf("class=\"article-editor");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+21);
			author = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</p>")).trim();
		}else if((indexStr = contentHTML.indexOf("class=\"show_author\""))>-1){
			tempHtml = contentHTML.substring(indexStr+25);
			author = tempHtml.substring(0,tempHtml.indexOf("</p>")).trim();
		}
		

		//内容采集
		indexStr = contentHTML.indexOf("class=\"article\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr);
			content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<!-- 正文")).trim();
		}else if((indexStr = contentHTML.indexOf("class=\"article-body main-body\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<!--正文页微信二维码")).trim();
		}else if((indexStr = contentHTML.indexOf("id=\"artibody\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<p class=\"article-editor\"")).trim();
		}
		

		//评论页组装
		tempHtml = contentHTML.substring(contentHTML.indexOf("channel: '")+10);
		String channel = tempHtml.substring(0,tempHtml.indexOf("'"));
		tempHtml = contentHTML.substring(contentHTML.indexOf("newsid: '")+9);
		String newsid = tempHtml.substring(0,tempHtml.indexOf("'"));
		double num = (Math.random()*89999999)+10000000;
		String requestId = "requestId_" + Math.floor(num);
		//评论页面访问地址
		String commentUrl = "http://comment5.news.sina.com.cn/page/info?format=js&channel="+channel+"&newsid="+newsid+"&group=&compress=1&ie=gbk&oe=gbk&page={page}&page_size={pagesize}&jsvar="+requestId;
		//数据装载到 数组中
		newsinfo[0] = hUtil.delHTMLTag(title, "");
		newsinfo[1] = hUtil.delHTMLTag(author, "");
		newsinfo[2] = hUtil.delHTMLTag(source, "");
		newsinfo[3] = hUtil.delHTMLTag(content, "");
		newsinfo[4] = DateTimeUtil.fmtDate(datetime);
		newsinfo[5] = DateTimeUtil.fmtTime(datetime);
		newsinfo[6] = commentUrl;
		return newsinfo;
	}
	/**
	 * 凤凰新闻分析
	 * @param contentHTML
	 * @return
	 * @throws Exception 
	 */
	public String[] getIfengNewsInfo(String contentHTML,String newsUrl) throws Exception{
		String[] newsinfo = new String[8];
		String title = "";
		String datetime = "";
		String author = "";
		String source = "";
		String content = "";
		HtmlUtil hUtil = new HtmlUtil();
		if(contentHTML.indexOf("<div class=\"photoMHD\"")>-1){
			//放弃图片新闻采集
			return newsinfo;
		}
		//标题采集
		String tempHtml = contentHTML.substring(contentHTML.indexOf("<h1")+1);
		title = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</h1>")).trim();
		
		//时间采集
		if(contentHTML.indexOf("itemprop=\"datePublished\"")>-1){
			tempHtml = contentHTML.substring(contentHTML.indexOf("itemprop=\"datePublished\""));
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
		}else{
			tempHtml = contentHTML.substring(contentHTML.indexOf("<p><span>")+9);
			datetime = tempHtml.substring(0,tempHtml.indexOf("</span>")).trim();
		}
		//图片新闻找不到作者和来源
		if(contentHTML.indexOf("<div class=\"hdpPic\" id=\"imgBox\"")>-1){
			//System.out.println("采集图片新闻内容,无作者和来源信息");
			
			//内容采集
			if(contentHTML.indexOf("var share_data = [")>-1){
				tempHtml = contentHTML.substring(contentHTML.indexOf("var share_data = [")+18);
				content = tempHtml.substring(0,tempHtml.indexOf("];")).trim();
				content = content.replaceAll("\\{title:'|',","");
				content = content.replaceAll("image: '","<img src='");
				content = content.replaceAll("\\},|\\}",">");
			}
		}else{
			//来源采集
			int indexStr = contentHTML.indexOf("itemprop=\"publisher\"");
			if(indexStr>-1){
				tempHtml = contentHTML.substring(indexStr);
				source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
			}else{
				tempHtml = contentHTML.substring(contentHTML.indexOf("</span><a")+9);
				source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</a>")).trim();
			}
			//作者采集
			indexStr = contentHTML.indexOf("<p class=\"yc_zb\"");
			if(indexStr>-1){
				tempHtml = contentHTML.substring(contentHTML.indexOf("<p class=\"yc_zb\"")+20);
				author = tempHtml.substring(0,tempHtml.indexOf("</p>")).trim();
			}else if(contentHTML.indexOf("<p class=\"yc_zb\"")>-1){
				tempHtml = contentHTML.substring(contentHTML.indexOf("<p class=\"iphone_none\">")+13);
				author = tempHtml.substring(tempHtml.indexOf("[")+6,tempHtml.indexOf("]")).trim();
			}
			//内容采集
			if(contentHTML.indexOf("<div class=\"yc_con_txt\"")>-1){
				tempHtml = contentHTML.substring(contentHTML.indexOf("<div class=\"yc_con_txt\"")+24);
				content = tempHtml.substring(0,tempHtml.indexOf("<div class=\"yc_con_txt\"")).trim();
			}else{
				tempHtml = contentHTML.substring(contentHTML.indexOf("id=\"main_content\""));
				content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</div>")).trim();
			}
		}
		
		//评论页面访问地址
		String skey = contentHTML.substring(contentHTML.indexOf("skey")+6);
		skey = skey.substring(skey.indexOf("\"")+1,skey.indexOf(",")-1);
		String newsUrl_en = URLEncoder.encode(newsUrl,"UTF-8");
		String commentUrl = "http://comment.ifeng.com/get.php?callback=newCommentListCallBack&orderby=&docUrl="+newsUrl_en+"&format=js&job=1&p={page}&pageSize={pagesize}&callback=newCommentListCallBack&skey="+skey;
		
		//数据装载到 数组中
		newsinfo[0] = hUtil.delHTMLTag(title, "");
		newsinfo[1] = hUtil.delHTMLTag(author, "");
		newsinfo[2] = hUtil.delHTMLTag(source, "");
		newsinfo[3] = hUtil.delHTMLTag(content, "");
		newsinfo[4] = DateTimeUtil.fmtDate(datetime);
		newsinfo[5] = DateTimeUtil.fmtTime(datetime);
		newsinfo[6] = commentUrl;
		
		return newsinfo;
	}
	/**
	 * 搜狐新闻分析
	 * @param contentHTML
	 * @return
	 */
	public String[] getSohuNewsInfo(String contentHTML,String newsurl){
		String[] newsinfo = new String[8];
		String title = "";
		String datetime = "";
		String author = "";
		String source = "";
		String content = "";
		HtmlUtil hUtil = new HtmlUtil();
		
		//标题采集
		String tempHtml = contentHTML.substring(contentHTML.indexOf("<h1")+1);
		title = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</h1>")).trim();
		
		//时间采集
		int indexStr = contentHTML.indexOf("id=\"pubtime_baidu\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr);
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</div>")).trim();
		}else if((indexStr = contentHTML.indexOf("<span class=\"time\""))>-1){
			tempHtml = contentHTML.substring(indexStr+1);
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
		}
		
		//来源采集
		indexStr = contentHTML.indexOf("itemprop=\"name\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+15);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
		}else if((indexStr = contentHTML.indexOf("来源:<a href"))>-1){
			tempHtml = contentHTML.substring(indexStr+10);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
		}
		
		//作者采集
		indexStr = contentHTML.indexOf("id=\"editor_baidu\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+19);
			author = tempHtml.substring(0,tempHtml.indexOf(")</span>")).trim();
			author = author.replace("责任编辑", "");
		}
		

		//内容采集
		indexStr = contentHTML.indexOf("<article class=\"article\"");
		if(indexStr > -1){
			tempHtml = contentHTML.substring(indexStr+1);
			content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</article>")).trim();
		}else if((indexStr = contentHTML.indexOf("<div itemprop=\"articleBody\""))>-1){
			tempHtml = contentHTML.substring(indexStr+28);
			content = tempHtml.substring(0,tempHtml.indexOf("<script")).trim();
		}
		

		//评论页地址
		String source_id = contentHTML.substring(contentHTML.indexOf("news_id: \"")+10);
		source_id = source_id.substring(0,source_id.indexOf("\""));
		String topic_source_id = contentHTML.substring(contentHTML.indexOf("cms_id: \"")+9);
		topic_source_id = topic_source_id.substring(0,topic_source_id.indexOf("\"")).trim();
		long nowtime = System.currentTimeMillis();
		String commentUrl = "http://apiv2.sohu.com/api/topic/load?page_size={pagesize}&topic_source_id="+topic_source_id+"&page_no={page}&hot_size=5&source_id=mp_"+source_id+"&_="+nowtime;
//			String commentUrl = "http://changyan.sohu.com/api/2/topic/comments?callback=jQuery1707406553067964248_"+nowtime+"&client_id="+client_id+"&page_size={pagesize}&topic_id={newsid}&page_no=1&_=1486610003516";
		
		
		//System.out.println(title + "\n"+datetime + "\n"+source + "\n"+author + "\n"+content +"\n"+commentUrl);
		
		
		
		//数据装载到 数组中
		newsinfo[0] = hUtil.delHTMLTag(title, "");
		newsinfo[1] = hUtil.delHTMLTag(author, "");
		newsinfo[2] = hUtil.delHTMLTag(source, "");
		newsinfo[3] = hUtil.delHTMLTag(content, "");
		newsinfo[4] = DateTimeUtil.fmtDate(datetime);
		newsinfo[5] = DateTimeUtil.fmtTime(datetime);
		newsinfo[6] = commentUrl;
		
		return newsinfo;
	}
	/**
	 * 腾讯新闻分析
	 * @param contentHTML
	 * @return
	 */
	public String[] getQQNewsInfo(String contentHTML){
		String[] newsinfo = new String[8];
		String title = "";
		String datetime = "";
		String author = "";
		String source = "";
		String content = "";
		HtmlUtil hUtil = new HtmlUtil();
		
		if(contentHTML.indexOf("catalog:'photon'")>-1){
			//放弃图片新闻采集
			return newsinfo;
		}
		//标题采集
		String tempHtml = contentHTML.substring(contentHTML.indexOf("title:'")+7);
		title = tempHtml.substring(0,tempHtml.indexOf("',")).trim();
		
		//时间采集
		int indexStr = contentHTML.indexOf("pubtime:'");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+9);
			datetime = tempHtml.substring(0,tempHtml.indexOf("',")).trim();
		}
//			else if((indexStr = contentHTML.indexOf("class=\"article-time\""))>-1){
//				tempHtml = contentHTML.substring(indexStr);
//				datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
//			}
		
		//来源采集
		indexStr = contentHTML.indexOf("class=\"a_source\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(indexStr+15);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
			source = hUtil.delHTMLTag(source, "");
		}else if((indexStr = contentHTML.indexOf("class=\"color-a-1\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			source = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
			source = hUtil.delHTMLTag(source, "");
		}else if((indexStr = contentHTML.indexOf("name: \""))>-1){
			//图片新闻走这里
			tempHtml = contentHTML.substring(indexStr+7);
			source = tempHtml.substring(0,tempHtml.indexOf("\",")).trim();
		}
		
		//作者采集
		indexStr = contentHTML.indexOf("id=\"QQeditor\"");
		if(indexStr>-1){
			tempHtml = contentHTML.substring(contentHTML.indexOf("id=\"QQeditor\"")+19);
			author = tempHtml.substring(0,tempHtml.indexOf("</div>")).trim();
		}else if((indexStr = contentHTML.indexOf("class=\"color-a-3\""))>-1){
			tempHtml = contentHTML.substring(indexStr);
			author = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</span>")).trim();
			author = hUtil.delHTMLTag(source, "");
		}
		

		//内容采集
//			//System.out.println(contentHTML);
		if(contentHTML.indexOf("id=\"Cnt-Main-Article-QQ\"")>-1){
			tempHtml = contentHTML.substring(contentHTML.indexOf("id=\"Cnt-Main-Article-QQ\""));
			content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<span style=\"width:0;height:0;")).trim();
		}else{
			content = "图片新闻，详细内容请打开网页查看";
		}
		
		//组装评论页地址
		String newsid = "";
		if(contentHTML.indexOf("cmt_id = ")>-1){
			tempHtml = contentHTML.substring(contentHTML.indexOf("cmt_id = ")+9);
			newsid = tempHtml.substring(0,tempHtml.indexOf(";"));
		}else if(contentHTML.indexOf("aid: \"")>-1){
			tempHtml = contentHTML.substring(contentHTML.indexOf("aid: \"")+6);
			newsid = tempHtml.substring(0,tempHtml.indexOf("\","));
		}
		String commentUrl = "http://coral.qq.com/article/"+newsid+"/comment/v2?orinum={pagesize}&oriorder=t&_=1517197180590";
//			String commentUrl = "http://coral.qq.com/article/"+newsid+"/comment?commentid={cmtid}&reqnum={pagesize}&tag=&callback=mainComment&_="+System.currentTimeMillis();
		//数据装载到 数组中
		newsinfo[0] = hUtil.delHTMLTag(title, "");
		newsinfo[1] = hUtil.delHTMLTag(author, "");
		newsinfo[2] = hUtil.delHTMLTag(source, "");
		newsinfo[3] = hUtil.delHTMLTag(content, "");
		newsinfo[4] = DateTimeUtil.fmtDate(datetime);
		newsinfo[5] = DateTimeUtil.fmtTime(datetime);
		newsinfo[6] = commentUrl;
			
		return newsinfo;
	}
	/**
	 * 澎湃新闻分析
	 * @param contentHTML
	 * @return
	 */
	public String[] getThePaperNewsInfo(String contentHTML){
		String[] newsinfo = new String[8];
		String title = "";
		String datetime = "";
		String author = "";
		String source = "";
		String content = "";
		HtmlUtil hUtil = new HtmlUtil();
		
		if(contentHTML.indexOf("<div class=\"video_bo\"")>-1){
			//视频新闻在此处采集
			//标题采集
			String tempHtml = contentHTML.substring(contentHTML.indexOf("<div class=\"video_txt_t\""));
			title = tempHtml.substring(tempHtml.indexOf("<h2>")+4,tempHtml.indexOf("</h2>")).trim();
			//内容采集
			tempHtml = contentHTML.substring(contentHTML.indexOf("<div class=\"video_txt_l\""));
			content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<div class=\"t_source_1\">")).trim();
			//来源采集
			tempHtml = contentHTML.substring(contentHTML.indexOf("<div class=\"t_source_1\">"));
			tempHtml = tempHtml.substring(0,tempHtml.indexOf("<div class=\"video_txt_r\""));
			if(tempHtml.indexOf("来源")>-1){
				tempHtml = tempHtml.substring(tempHtml.indexOf("来源")+3);
				source = tempHtml.substring(0,tempHtml.indexOf(" "));
			}
			//时间采集
			datetime = tempHtml.substring(0,tempHtml.indexOf("</div>"));
			
		}else if(contentHTML.indexOf("<div class=\"photoMHD\"")>-1||contentHTML.indexOf("<div class=\"live_mian_bg\"")>-1){
			//图片新闻 感觉没必要采集    标题没有   作者没有    来源没有     只有点图片和对应的内容
			//直播也是没有采集的必要了   根本就没有什么标题云云
			return newsinfo;
		}else{
			//标题采集
			String tempHtml = contentHTML.substring(contentHTML.indexOf("<h1")+1);
			title = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</h1>")).trim();
			//时间采集
			tempHtml = contentHTML.substring(contentHTML.indexOf("class=\"news_about\""));
			datetime = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("</div>")).trim();
			//来源采集
			source = tempHtml.substring(tempHtml.indexOf("<p>")+3,tempHtml.indexOf("</p>")).trim();
			//作者采集
			int indexStr = contentHTML.indexOf("class=\"news_editor\"");
			if(indexStr>-1){
				tempHtml = contentHTML.substring(contentHTML.indexOf("class=\"news_editor\"")+25);
				author = tempHtml.substring(0,tempHtml.indexOf("<span>")).trim();
			}
			//内容采集
			tempHtml = contentHTML.substring(contentHTML.indexOf("class=\"news_txt\""));
			content = tempHtml.substring(tempHtml.indexOf(">")+1,tempHtml.indexOf("<div class=\"news_editor\"")).trim();
		}
		
		//得到newsid  组装第一页评论地址
		String commentUrl = contentHTML.substring(contentHTML.indexOf("hidden_contid"));
		commentUrl = commentUrl.substring(commentUrl.indexOf("value=\"")+7,commentUrl.indexOf("\" />"));
		commentUrl = "http://www.thepaper.cn/newDetail_commt.jsp?contid="+commentUrl+"&_="+System.currentTimeMillis();
		

		String plCount = contentHTML.substring(contentHTML.indexOf("<h2 id=\"comm_span\">"));
		plCount = plCount.substring(plCount.indexOf("（")+1,plCount.indexOf("）"));
		//数据装载到 数组中
		newsinfo[0] = hUtil.delHTMLTag(title, "");
		newsinfo[1] = hUtil.delHTMLTag(author, "");
		newsinfo[2] = hUtil.delHTMLTag(source, "");
		newsinfo[3] = hUtil.delHTMLTag(content, "");
		newsinfo[4] = DateTimeUtil.fmtDate(datetime);
		newsinfo[5] = DateTimeUtil.fmtTime(datetime);
		newsinfo[6] = commentUrl;
		newsinfo[7] = plCount;
		
		return newsinfo;
	}
}
