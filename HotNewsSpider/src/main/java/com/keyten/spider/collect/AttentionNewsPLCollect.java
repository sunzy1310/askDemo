package com.keyten.spider.collect;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLAttentionHotNews;
import com.keyten.base.bean.TBLAttentionHotNewsPL;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.spider.service.TBLAttentionNewsPLService;

/**
 * 网民关注新闻评论采集
 * @author liym
 * 2018-03-26
 */
@Component
public class AttentionNewsPLCollect extends BaseCollect{
	@Autowired
	TBLAttentionNewsPLService attNewsPLService;
	@Autowired
	BaseNewsPLAnalysis plAnalysis;
	@Autowired
	SpringContextUtil springContext;
	
	public void collect(String newsid, String newsplurl,String newsurl) {
		List<String[]> pllist = new ArrayList<String[]>();
		int plCount = 0;
		try {
			//1 首先采集评论信息
			if(newsplurl.indexOf("sina.com")>-1){
				String content = super.collect(newsplurl,"1","10","", "GBK");
				content = content.substring(content.indexOf("=")+1);
				JSONObject json = new JSONObject(content);
				//最热
				JSONArray hotlist = json.getJSONObject("result").getJSONArray("hot_list");
				if(hotlist.length()>0){
					pllist.addAll(plAnalysis.sinaPLCollect(hotlist,"new"));
				}
				//最新
				JSONArray cmntlist = json.getJSONObject("result").getJSONArray("cmntlist");
				if(cmntlist.length()>0){
					pllist.addAll(plAnalysis.sinaPLCollect(cmntlist,"hot"));
				}
				//评论数
				plCount = json.getJSONObject("result").getJSONObject("count").getInt("show");
			}else if(newsplurl.indexOf("sohu.com")>-1){
				String content = super.collect(newsplurl,"1","10","", "GBK");
				JSONObject json = new JSONObject(content);
				//最热
				JSONArray hotlist = json.getJSONObject("jsonObject").getJSONArray("hots");
				if(hotlist.length()>0){
					pllist.addAll(plAnalysis.sohuPLCollect(hotlist,"hot"));
				}
				//最新
				JSONArray cmntlist = json.getJSONObject("jsonObject").getJSONArray("comments");
				if(cmntlist.length()>0){
					pllist.addAll(plAnalysis.sohuPLCollect(cmntlist,"new"));
				}
				//评论数
				plCount = json.getJSONObject("jsonObject").getInt("cmt_sum");
			}else if(newsplurl.indexOf("163.com")>-1){
				String content = super.collect(newsplurl,"1","10","", "GBK");
				content = content.substring(content.indexOf("(")+1,content.length()-2);
				JSONObject json = new JSONObject(content);
				//最新
				JSONArray cmntlist = json.getJSONArray("commentIds");
				if(cmntlist.length()>0){
					pllist.addAll(plAnalysis.wangYiPLCollect(json,"new"));
				}
				//评论数
				plCount = json.getInt("newListSize");
				
				//
				newsplurl = newsplurl.replace("newList", "hotList");
				content = super.collect(newsplurl,"1","5","", "GBK");
				content = content.substring(content.indexOf("(")+1,content.length()-2);
				json = new JSONObject(content);
				//最热
				JSONArray hotlist = json.getJSONArray("commentIds");
				if(hotlist.length()>0){
					pllist.addAll(plAnalysis.wangYiPLCollect(json,"hot"));
				}
				
			}else if(newsplurl.indexOf("qq.com")>-1){
				String content = super.collect(newsplurl,"1","10","", "GBK");
				JSONObject json = new JSONObject(content);
				//最新
				JSONObject data = json.getJSONObject("data");
				if(data.getJSONArray("oriCommList").length()>0){
					pllist.addAll(plAnalysis.qqPLCollect(data,"new"));
				}
				
				newsplurl = newsplurl.replace("oriorder=t", "oriorder=o");
				content = super.collect(newsplurl,"1","5","", "GBK");
				json = new JSONObject(content);
				//最热
				data = json.getJSONObject("data");
				if(data.getJSONArray("oriCommList").length()>0){
					pllist.addAll(plAnalysis.qqPLCollect(data,"hot"));
				}
				//评论数
				plCount = data.getInt("oritotal");
			}else if(newsplurl.indexOf("ifeng.com")>-1){
				String content = super.collect(newsplurl,"1","10","", "GBK");
				content = content.replace("\"{", "'{").replace("}\"", "'}");
				content = content.substring(content.indexOf("=")+1,content.indexOf("};")+1);
				JSONObject json = new JSONObject(content);
				//最新
				if(json.getJSONArray("comments").length()>0){
					pllist.addAll(plAnalysis.ifengPLCollect(json,"new"));
				}

				
				newsplurl = newsplurl.replace("orderby=", "orderby=uptimes");
				content = super.collect(newsplurl,"1","5","", "GBK");
				content = content.replace("\"{", "{").replace("}\"", "}");
				content = content.substring(content.indexOf("=")+1,content.indexOf("};")+1);
				json = new JSONObject(content);
				//最热
				if(json.getJSONArray("comments").length()>0){
					pllist.addAll(plAnalysis.ifengPLCollect(json,"hot"));
				}

				//评论数
				plCount = json.getInt("count");
				
				System.out.println(plCount);
			}else if(newsplurl.indexOf("thepaper.cn")>-1){
				String content = super.collect(newsplurl,"1","1","", "UTF-8");
				Document dom = Jsoup.parseBodyFragment(content);
				Element mainContent = dom.getElementById("mainContent");
				List<Element> childNodes = mainContent.children();
				//页面上一开始先是热评
				String type = "hot";
				for(Element element : childNodes){
					try {
						if(element.attr("class").equals("comment_title")){
							//当找到 div中内容为新评论的标签时    往下循环的评论都是最新评论
							if(element.text().contains("新评论"))
								type = "new";
						}
						if(element.attr("class").equals("comment_que")){
							String[] plinfo = plAnalysis.thePaperPLCollect(element,type);
							if(plinfo!=null)
								pllist.add(plinfo);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//澎湃新闻的评论数信息在新闻内容页上。。。
				String newsinfo = super.collect(newsurl,"1","1","", "UTF-8");
				if(content.indexOf("<p class=\"news_txt\" style=\"padding:140px 0;text-align:center\">此文章已下线</p>")>-1){
					System.out.println("此文章已经下线啦");
				}else{
					String count = newsinfo.substring(newsinfo.indexOf("<h2 id=\"comm_span\">"));
					count = count.substring(count.indexOf("（")+1,count.indexOf("）"));
					System.out.println(count.substring(0,count.length()-1));
					if(count.contains("k"))
						plCount = (int) (Double.parseDouble(count.substring(0,count.length()-1))*1000);
					else
						plCount = Integer.parseInt(count);
				}
			}
			//2 保存评论信息（先删除 后保存）
			saveData(pllist,plCount,newsid);
		} catch (Exception e) {
			LogUtil.addLogError(newsurl+"\r\n"+e.toString(),this.getClass().getName());
		}
	}
	/*
	 * 删除原评论，存储新评论，修改新闻评论数、最后采集日期时间
	 */
	private void saveData(List<String[]> pllist,int count,String newsid){
		try {
			List<TBLAttentionHotNewsPL> plList = new ArrayList<>();
			//组装本次采集到的评论信息
			for(String[] pl : pllist){
				TBLAttentionHotNewsPL hotnewspl = 
						(TBLAttentionHotNewsPL) springContext.getBean("attentionNewsPL");
				hotnewspl.setNewsid(newsid);
				hotnewspl.setPlauthor(pl[0]);
				hotnewspl.setPlcontent(pl[1]);
				hotnewspl.setPltype(pl[5]);
				hotnewspl.setDzcount(pl[2]);
				hotnewspl.setPubdate(pl[3]);
				hotnewspl.setPubtime(pl[4]);
				long datetime = System.currentTimeMillis();
				hotnewspl.setMakedate(DateTimeUtil.getDateByLong(datetime));
				hotnewspl.setMaketime(DateTimeUtil.getTimeByLong(datetime));
				plList.add(hotnewspl);
			}
			//组装新闻信息（需要修改部分）
			TBLAttentionHotNews hotnews = 
					(TBLAttentionHotNews) springContext.getBean("attentionHotNews");
			hotnews.setNewsid(newsid);
			hotnews.setPlpage(count+"");
			long datetime = System.currentTimeMillis();
			hotnews.setLastupdatedate(DateTimeUtil.getDateByLong(datetime));
			hotnews.setLastupdatetime(DateTimeUtil.getDateTimeByLong(datetime));
			//入库
			attNewsPLService.savePLAndUpdateNews(plList,hotnews);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
