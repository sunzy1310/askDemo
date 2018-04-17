package com.keyten.spider.collect;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLHotNews;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.service.TBLHotNewsService;

@Component
public class SpreadNewsCollect{
	@Autowired
	TBLHotNewsService hotNewsService;
	@Autowired
	BaseCollectNews collectNews;
	@Autowired
	SpringContextUtil springUtil;
	/**
	 * 公开方法  调用后 开始采集热点新闻详细内容
	 * @param newsid
	 * @param newsurl
	 */
	public void collect(String newsid,String newsurl){
		try {
			String[] newsinfo = collectNews.collectNews(newsurl);
			if(null!=newsinfo[0]&&null!=newsinfo[4]&&!"".equals(newsinfo[0])&&!"".equals(newsinfo[4])){
				saveData(newsid,newsinfo);
			}else{
				System.out.println(newsurl+"的信息采集不全，可能网站改版");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveData(String newsid,String[] newsinfo){
		try {
			//首先将内容保存为HTML
			String content = newsinfo[3];
			String strDate = DateTimeUtil.getDateByLong(System.currentTimeMillis());
			String strYear = strDate.substring(0, 4);
			String strMonth = strDate.substring(5, 7);
			String strDay = strDate.substring(8);
			String savePath = SysConfig.FILEPATH_HTML +"/"+strYear+"/"+strMonth+"/"+strDay+"/";
			String serverPath = SysConfig.SERVER_FILEPATH_HTML +"/"+strYear+"/"+strMonth+"/"+strDay+"/";
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
				System.out.println("html已生成完毕！");
			}
			catch(Exception ex){
				ex.printStackTrace();
				serverPath = "";
				System.out.println("HTML生成失败");
			}
			if(!serverPath.equals("")){
				TBLHotNews hotNews = (TBLHotNews) springUtil.getBean("hotNews"); 
				hotNews.setNewsid(newsid);
				hotNews.setNewsplurl(newsinfo[6]);
				hotNews.setNewstitle(newsinfo[0]);
				hotNews.setNewssource(newsinfo[2]);
				hotNews.setNewsauthor(newsinfo[1]);
				hotNews.setNewscontent(serverPath);
				hotNews.setPubdate(newsinfo[4]);
				hotNews.setPubtime(newsinfo[5]);
				long datetime = System.currentTimeMillis();
				hotNews.setMakedate(DateTimeUtil.getDateByLong(datetime));
				hotNews.setMaketime(DateTimeUtil.getTimeByLong(datetime));
				hotNewsService.update(hotNews);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
