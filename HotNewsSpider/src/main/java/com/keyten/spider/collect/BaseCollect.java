package com.keyten.spider.collect;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.keyten.base.util.UnicodeConverter;

public class BaseCollect {
	/**
	 * 新闻页面采集
	 * @param artUrl
	 * @param charset
	 * @return
	 */
	public String collect(String artUrl,String charset){
		String con = "";
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(artUrl);
			get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko");
			RequestConfig requestConfig = RequestConfig.custom()  
	        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
	        .setSocketTimeout(5000).build();  
			get.setConfig(requestConfig);  
			CloseableHttpResponse response = httpClient.execute(get);
			HttpEntity httpEntity = response.getEntity();
			con = EntityUtils.toString(httpEntity,charset);
			con = UnicodeConverter.decode(con, 0);
			EntityUtils.consume(httpEntity);
			get.abort();
			response.close();
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	/**
	 * 新闻评论页面采集
	 * @param artUrl
	 * @param charset
	 * {page} 页数（第几页）     pagesize 页容量（每页多少条）    {pageno} 开始位置（从第几条数据开始）
	 * @return
	 */
	public String collect(String newsplurl,String page,String pagesize,String cmtid,String charset){
		String con = "";
		try {
			String url = newsplurl.replace("{page}",page).replace("{pagesize}",pagesize)
			.replace("{pageno}",((Integer.parseInt(page)-1)*Integer.parseInt(pagesize))+"").replace("{cmtid}",cmtid);
//			System.out.println(url);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet(url);
			get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko");
			RequestConfig requestConfig = RequestConfig.custom()  
	        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
	        .setSocketTimeout(5000).build();  
			get.setConfig(requestConfig);  
			CloseableHttpResponse response = httpClient.execute(get);
			HttpEntity httpEntity = response.getEntity();
			con = EntityUtils.toString(httpEntity,charset);
			con = UnicodeConverter.decode(con, 0);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
