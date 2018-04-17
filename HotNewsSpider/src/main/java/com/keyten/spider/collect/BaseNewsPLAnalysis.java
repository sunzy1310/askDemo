package com.keyten.spider.collect;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.keyten.base.util.DateTimeUtil;

@Component
public class BaseNewsPLAnalysis {
	/**
	 * 新浪新闻   评论内容   分析
	 * @param newslist
	 * @param type 
	 */
	public List<String[]> sinaPLCollect(JSONArray newslist, String type){
		List<String[]> pllist = new ArrayList<String[]>();
		
		for(int i = 0,length = newslist.length(); i < length; i++){
			try {
				JSONObject newNews = newslist.getJSONObject(i);
				//评论标识码
//				String comment_flag = newNews.getString("mid");
//				System.out.println(comment_flag);
				
				//发布人
				String pubuser = newNews.getString("config");
				pubuser = pubuser.substring(pubuser.indexOf("wb_screen_name=")+15);
				pubuser = pubuser.substring(0,pubuser.indexOf("&"));
				pubuser = (pubuser.equals("TYPE=14")||pubuser.indexOf("area=")>-1) ? newNews.getString("nick") : pubuser;
				
				//评论内容
				String commentcontent = newNews.getString("content");
				
				//点赞数量
				String dzcount = newNews.getString("against");
				
				//发布日期时间
				String[] datetime = newNews.getString("time").split(" ");
				String pubdate = datetime[0];
				String pubtime = datetime[1];
				
//				System.out.println(pubuser);
//				System.out.println(commentcontent);
//				System.out.println(dzcount);
//				System.out.println(pubdate);
//				System.out.println(pubtime);
//				System.out.println("\n\n");
				
				pllist.add(initPLInfo(pubuser,commentcontent,dzcount,pubdate,pubtime,type));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pllist;
	}
	public List<String[]> sohuPLCollect(JSONArray newslist, String type) {
		List<String[]> pllist = new ArrayList<String[]>();
		try {
			for(int i = 0,length = newslist.length(); i < length; i++){
				JSONObject newNews = newslist.getJSONObject(i);
				//评论标识码
//				String comment_flag = newNews.getString("mid");
//				System.out.println(comment_flag);
				
				//发布人
				String pubuser = newNews.getJSONObject("passport").getString("nickname");
				
				//评论内容
				String commentcontent = newNews.getString("content");
				
				//点赞数量
				String dzcount = newNews.getInt("support_count")+"";
				
				//发布日期时间
				long datetime = newNews.getLong("create_time");
				String pubdate = DateTimeUtil.getDateByLong(datetime);
				String pubtime = DateTimeUtil.getTimeByLong(datetime);

//				System.out.println(pubuser);
//				System.out.println(commentcontent);
//				System.out.println(dzcount);
//				System.out.println(pubdate);
//				System.out.println(pubtime);
//				System.out.println("\n\n");
				
				pllist.add(initPLInfo(pubuser,commentcontent,dzcount,pubdate,pubtime,type));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pllist;
	}
	public List<String[]> wangYiPLCollect(JSONObject json, String type) {
		List<String[]> pllist = new ArrayList<String[]>();
		try {
			JSONArray commentIds = json.getJSONArray("commentIds");
			System.out.println(commentIds.length());
			JSONObject comments = json.getJSONObject("comments");
			for(int i = 0 ; i < commentIds.length() ; i ++){
				String commentid = commentIds.getString(i);
				if(commentid.indexOf(",")>-1)
					commentid = commentid.substring(commentid.lastIndexOf(",")+1);
				JSONObject newNews = comments.getJSONObject(commentid);
				
				//发布人
				String pubuser = "";
				JSONObject user = newNews.getJSONObject("user");
				if(user.getInt("userId") == 0){
					pubuser = user.getString("location")+"网友";
				}else{
					pubuser = user.getString("nickname");
				}
					
				//评论内容
				String commentcontent = newNews.getString("content");
				
				//点赞数量
				String dzcount = newNews.getInt("vote")+"";
				
				//发布日期时间
				String[] datetime = newNews.getString("createTime").split(" ");
				String pubdate = datetime[0];
				String pubtime = datetime[1];

//				System.out.println(pubuser);
//				System.out.println(commentcontent);
//				System.out.println(dzcount);
//				System.out.println(pubdate);
//				System.out.println(pubtime);
//				System.out.println("\n\n");
				
				pllist.add(initPLInfo(pubuser,commentcontent,dzcount,pubdate,pubtime,type));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pllist;
	}
	public List<String[]> qqPLCollect(JSONObject data, String type) {
		List<String[]> pllist = new ArrayList<String[]>();
		try {
			JSONArray cmtList = data.getJSONArray("oriCommList");
			JSONObject userList = data.getJSONObject("userList");
			for(int i = 0 ; i < cmtList.length() ; i ++){
				JSONObject newNews = cmtList.getJSONObject(i);
				
				//发布人
				String userid = newNews.getString("userid");
				String pubuser = userList.getJSONObject(userid).getString("nick");
				
				//评论内容
				String commentcontent = newNews.getString("content");
				
				//点赞数量
				String dzcount = newNews.getString("up");
				
				//发布日期时间
				long datetime = Long.parseLong(newNews.getString("time")+"000");
				String pubdate = DateTimeUtil.getDateByLong(datetime);
				String pubtime = DateTimeUtil.getTimeByLong(datetime);

//				System.out.println(pubuser);
//				System.out.println(commentcontent);
//				System.out.println(dzcount);
//				System.out.println(pubdate);
//				System.out.println(pubtime);
//				System.out.println("\n\n");
				
				pllist.add(initPLInfo(pubuser,commentcontent,dzcount,pubdate,pubtime,type));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pllist;
	}
	public List<String[]> ifengPLCollect(JSONObject json, String type) {
		List<String[]> pllist = new ArrayList<String[]>();
		try {
			JSONArray cmtList = json.getJSONArray("comments");
			for(int i = 0 ; i < cmtList.length() ; i ++){
				JSONObject newNews = cmtList.getJSONObject(i);
				
				//发布人
				String pubuser = newNews.getString("uname");
				
				//评论内容
				String commentcontent = newNews.getString("comment_contents");
				
				//点赞数量
				String dzcount = newNews.getInt("uptimes") + "";
				
				//发布日期时间
				long datetime = Long.parseLong(newNews.getString("create_time")+"000");
				String pubdate = DateTimeUtil.getDateByLong(datetime);
				String pubtime = DateTimeUtil.getTimeByLong(datetime);

//				System.out.println(pubuser);
//				System.out.println(commentcontent);
//				System.out.println(dzcount);
//				System.out.println(pubdate);
//				System.out.println(pubtime);
//				System.out.println("\n\n");
				
				pllist.add(initPLInfo(pubuser,commentcontent,dzcount,pubdate,pubtime,type));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pllist;
	}
	public String[] thePaperPLCollect(Element element, String type) {
		String[] plinfo = null;
		try {
			
			//发布人
			Elements elementH3 = element.getElementsByTag("h3");
			String pubuser = elementH3.get(0).children().get(0).text();
			
			//评论内容
			String commentcontent = 
					element.getElementsByClass("ansright_cont").get(0).children().get(0).text();
			
			//点赞数量
			String dzcount = 
					element.getElementsByClass("ansright_time").get(0).children().get(0).text();
			
			//发布日期时间
			String[] datetime = DateTimeUtil.getNowByDateStr(elementH3.get(0).children().get(1).text()).split(" ");
			String pubdate = datetime[0];
			String pubtime = datetime[1];

//			System.out.println(pubuser);
//			System.out.println(commentcontent);
//			System.out.println(dzcount);
//			System.out.println(pubdate);
//			System.out.println(pubtime);
//			System.out.println("\n\n");
			
			plinfo = initPLInfo(pubuser,commentcontent,dzcount,pubdate,pubtime,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plinfo;
	}
	private String[] initPLInfo(String pubuser,String commentcontent,String dzcount,String pubdate,String pubtime,String type){
		String[] plinfo = new String[6];
		plinfo[0] = pubuser;
		plinfo[1] = commentcontent;
		plinfo[2] = dzcount;
		plinfo[3] = pubdate;
		plinfo[4] = pubtime;
		plinfo[5] = type;
		return plinfo;
	}
}
