package com.keyten.spider.collect;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLAttentionHotNews;
import com.keyten.base.bean.TBLCollectWeb;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.service.TBLAttentionNewsService;
import com.keyten.spider.service.TBLCollectWebService;

@Component
public class AttentionNewsCollect{
	@Autowired
	TBLCollectWebService collectWebService;
	@Autowired
	TBLAttentionNewsService attentionHotNewsService;
	@Autowired
	BaseCollectUrl collectUrl;
	@Autowired
	BaseCollectNews collectNews;
	@Autowired
	TBLAttentionHotNews attentionHotNews;
	/*
	 * 这一步主要采集的是文章url地址，全都采集好后组装好，为下一步采集内容做准备
	 */
	public void collect(String targetId) {
		try {
			List<TBLCollectWeb> weblist = collectWebService.getCollectWebByTargetId(targetId);
			if(weblist.size()>0){
				//用于在最后存储所有采集到的url信息
				Map<String,String[]> urls = new HashMap<String, String[]>(); 
				//存储所有GBK编码的网站  相关    栏目信息  （返回内容格式为HTML）
				List<TBLCollectWeb> gbkColumn = new ArrayList<TBLCollectWeb>();
				List<TBLCollectWeb> utf8Column = new ArrayList<TBLCollectWeb>();//utf-8
				List<TBLCollectWeb> gbkJsonColumn = new ArrayList<TBLCollectWeb>();//gbk and json
				List<TBLCollectWeb> utf8JsonColumn = new ArrayList<TBLCollectWeb>();
				for(TBLCollectWeb column : weblist){
					String webtype = column.getWebtype();
					if(webtype.equals("SINA")){
						utf8Column.add(column);
					}else if(webtype.equals("THEPAPER")){
						utf8Column.add(column);
					}else if(webtype.equals("WANGYI")){
						gbkColumn.add(column);
					}else if(webtype.equals("SOHU")){
						gbkColumn.add(column);
					}else if(webtype.equals("IFENG")){
						utf8Column.add(column);
					}else if(webtype.equals("TENCENT")){
						gbkColumn.add(column);
					}else if(webtype.equals("WANGYIJSON")){
						gbkJsonColumn.add(column);
					}
				}
				if(gbkJsonColumn.size()>0){
					collectUrl.setColumns(gbkJsonColumn);
					collectUrl.setCharSet("GBK");
					collectUrl.setContentType("json");
					urls.putAll(collectUrl.collectIndex());
				}
				if(gbkColumn.size()>0){
					collectUrl.setColumns(gbkColumn);
					collectUrl.setCharSet("GBK");
					collectUrl.setContentType("html");
					urls.putAll(collectUrl.collectIndex());
				}
				if(utf8Column.size()>0){
					collectUrl.setColumns(utf8Column);
					collectUrl.setCharSet("UTF-8");
					collectUrl.setContentType("html");
					urls.putAll(collectUrl.collectIndex());
				}
				//url地址都采集到了     开始采集内容信息了
				collectNewsInfo(urls,targetId);
				LogUtil.addLogInfo("AttentionNewsURL采集完成", this.getClass().getName());
			}
		} catch (Exception e) {
			String error = "AttentionNewsURL采集异常 \r\n" + e.toString();
			LogUtil.addLogError(error,this.getClass().getName());
		}
	}
	/**
	 * 采集新闻内容
	 * @param urls
	 * @param targetId
	 */
	private void collectNewsInfo(Map<String,String[]> urls,String targetId) {
		for(Entry<String, String[]> entry:urls.entrySet()){
			String url = entry.getKey();
			String[] news = entry.getValue();
			String columnid = news[1];
			try {
				String[] newsinfo = collectNews.collectNews(url);
				if(null!=newsinfo[0]&&null!=newsinfo[4]&&!"".equals(newsinfo[0])&&!"".equals(newsinfo[4])){
					String plUrl = newsinfo[6];
					int plcount = collectNews.collectPLCount(plUrl, newsinfo[7], targetId);
					if(plcount>0){
						newsinfo[7] = plcount+"";
						//大于0 说明验证为热点新闻   保留数量了       这样就可以保存热点新闻信息了
						saveData(newsinfo,targetId,columnid,url);
						LogUtil.addLogInfo("AttentionHotNews："+url, this.getClass().getName());
					}
				}else{
					LogUtil.addLogInfo(url+"的信息采集不全，可能网站改版", this.getClass().getName());
				}
			} catch (Exception e) {
				String error = url+" \r\n" + e.toString();
				LogUtil.addLogError(error,this.getClass().getName());
			}
			
			
		}
	}
	
	/**
	 * 将分析完成的热点新闻信息保存到数据库中
	 * @param newsinfo
	 * @param url 
	 * @param columnid 
	 * @param targetId 
	 */
	private	void saveData(String[] newsinfo, String targetId, String columnid, String url){
		try {
			int count = attentionHotNewsService.getCountByUrlAndTargetId(targetId, url);
			if(count > 0){
				System.out.println("当前目标已经采集过这条新闻");
			}
			else{
				long newsid = attentionHotNewsService.getPKBySeqName();
				//首先将内容保存为HTML
				String content = newsinfo[3];
				long nowDatetime = System.currentTimeMillis();
				String strDate = 
						DateTimeUtil.getDateByLong(nowDatetime);
				String strYear = strDate.substring(0, 4);
				String strMonth = strDate.substring(5, 7);
				String strDay = strDate.substring(8);
				String savePath = 
						SysConfig.ATTEFILEPATH_HTML +"/"+strYear+"/"+strMonth+"/"+strDay+"/";
				String serverPath = 
						SysConfig.SERVER_ATTFILEPATH_HTML +"/"+strYear+"/"+strMonth+"/"+strDay+"/";
				try{
					//创建目录
					File filepath = new File(savePath);
					if(!filepath.exists()){
						filepath.mkdirs();
					}
					//创建文件   命名为热点新闻主键
					String filePath = savePath +newsid+".html";
					serverPath = serverPath +newsid+".html";
					File file = new File(filePath);
					if(!file.exists())
						file.createNewFile();
					//将内容写入html文件中
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(content.getBytes());
					fos.flush();
					fos.close();
					LogUtil.addLogInfo(url+"：html已生成完毕！", this.getClass().getName());
				}
				catch(Exception ex){
					serverPath = "";
					LogUtil.addLogInfo(url+"：html已生成失败！", this.getClass().getName());
				}
				if(!serverPath.equals("")){
					attentionHotNews.setNewsid(newsid+"");
					attentionHotNews.setTargetid(targetId);
					attentionHotNews.setColumnid(columnid);
					attentionHotNews.setNewsurl(url);
					attentionHotNews.setNewsplurl(newsinfo[6]);
					attentionHotNews.setNewstitle(newsinfo[0]);
					attentionHotNews.setNewsauthor(newsinfo[1]);
					attentionHotNews.setNewssource(newsinfo[2]);
					attentionHotNews.setNewscontent(serverPath);
					attentionHotNews.setPubdate(newsinfo[4]);
					attentionHotNews.setPubtime(newsinfo[5]);
					attentionHotNews.setPlpage(newsinfo[7]);
					String currentDate = strDate;
					String currentTime = 
							DateTimeUtil.getDateByLong(System.currentTimeMillis());
					attentionHotNews.setMakedate(currentDate);
					attentionHotNews.setMaketime(currentTime);
					attentionHotNews.setLastupdatedate(currentDate);
					attentionHotNews.setLastupdatetime(currentTime);
					attentionHotNewsService.save(attentionHotNews);
				}
			}
		} catch (Exception e) {
			String error = url+"保存新闻异常 \r\n" + e.toString();
			LogUtil.addLogError(error,this.getClass().getName());
		}
	}
}
