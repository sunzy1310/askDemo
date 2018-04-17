package com.keyten.spider.controller;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLPost;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SysConfig;
import com.keyten.spider.service.TBLPostService;
/**
 * 信息保存入库线程
 * @author Administrator
 *
 */
@Component
public class SaveThread implements Runnable{
	@Autowired
	TBLPostService tblPostService;
	public void run() {
		while(true){
			try {
				if(CollectNewsHelper.saveQueue.size()>0){
					savePost();
				}else{
					LogUtil.addLogInfo("暂无文章存储任务",this.getClass().getName());
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void savePost(){
		try {
			TBLPost post = CollectNewsHelper.saveQueue.take();
			String content = post.getArtcontent();
			String title = post.getArttitle();
			//将内容存为HTML    
			if(content==null||title==null||content.equals("")||title.trim().equals("")){
				LogUtil.addLogInfo(post.getArturl()+"的内容采集不全，可能网站改版，放弃保存",this.getClass().getName());
			}else{
				String artid = Long.toString(tblPostService.getPKBySeqName());
//				//先保存HTML
				String saveURL = saveHtml(artid,content);
				//将内容存储到DB
				if(!saveURL.equals("")){
					post.setArticleid(artid);
					post.setArtcontent(saveURL);
					post.setCreatedate(
							DateTimeUtil.getDateByLong(System.currentTimeMillis()));
					post.setCreatetime(
							DateTimeUtil.getTimeByLong(System.currentTimeMillis()));
					tblPostService.save(post);
				}else{
					LogUtil.addLogInfo(post.getArturl()+"HTML生成失败",this.getClass().getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 文章内容存储为HTML
	 * 以后可以改为存到hadoop中
	 * @return
	 */
	private String saveHtml(String artid,String content){
		String saveURL = "";
		String strContentUrl = "";
		try {
			String filePath = SysConfig.FILEPATH_HTML+"";
			String strDate = DateTimeUtil.getDateByLong(System.currentTimeMillis());
			String strYear = strDate.substring(0, 4);
			String strMonth = strDate.substring(5, 7);
			String strDay = strDate.substring(8);
			filePath = filePath+"/"+strYear+"/"+strMonth+"/"+strDay+"/";
			File dir = new File(filePath);
			System.out.println(filePath);
			if(dir.exists()==false){
				dir.mkdirs();
			}
			String fileName = artid+".html";
			strContentUrl = (filePath+fileName);
			saveURL = SysConfig.SERVER_FILEPATH_HTML +strYear+"/"+strMonth+"/"+strDay+"/" + fileName;
			try{
				//生成html
				File f = new File(strContentUrl);
				if(f.exists()==false){
					f.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(content.getBytes());
				fos.flush();
				fos.close();
				System.out.println("html已生成完毕！");
			}
			catch(Exception ex){
				ex.printStackTrace();
				saveURL = "";
				System.out.println("HTML生成失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			saveURL = "";
		}
		return saveURL;
	}
}
