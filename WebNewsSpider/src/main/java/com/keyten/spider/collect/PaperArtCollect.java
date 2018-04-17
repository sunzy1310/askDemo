package com.keyten.spider.collect;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLNewspaperElementConfig;
import com.keyten.base.bean.TBLNewspaperWebConfig;
import com.keyten.base.bean.TBLPost;
import com.keyten.base.util.DateTimeUtil;
import com.keyten.base.util.HtmlUtil;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.UnicodeConverter;
import com.keyten.base.util.imageSrcConvers;
import com.keyten.spider.controller.CollectNewsHelper;
/**
 * 新闻信息采集类
 * 2018-04-09
 * @author liym
 *
 */
@Component
@Scope("prototype")
public class PaperArtCollect extends BaseCollect implements Runnable{
	private TBLNewspaperWebConfig newspaperWebCfg;
	private String artUrl;
	private HtmlUtil htmlUtil = new HtmlUtil();
	public void run() {
		try{
			getArtInfo();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 采集文章内容具体方法
	 */
	private void getArtInfo(){
		try {
			//第一步访问文章地址返回内容
			String html = super.getHtml(artUrl,newspaperWebCfg.getColumncharset());
			if(!html.equals("")){
				//以下开始分析各个规则，并将规则对应的标题内容等分析出来
				analyticArt(html);
			}
		} catch (Exception e) {
			if(e.getMessage()!=null&&e.getMessage().contains("time out")) {
				LogUtil.addLogError(artUrl+"文章访问超时:",this.getClass().getName());
			}else {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 根据文章元素规则   分析文章内容
	 * @param artUrl
	 * @param html
	 * @param column
	 */
	private void analyticArt(String html){
		try {
			
			imageSrcConvers imageConv = new imageSrcConvers();
			List<TBLNewspaperElementConfig> artlist = newspaperWebCfg.getElementCfgs();
			TBLPost post = new TBLPost();
			post.setColumnid(newspaperWebCfg.getWebruleid());
			post.setColumnurl(newspaperWebCfg.getWeburl());
			post.setColumnname(newspaperWebCfg.getWebname());
			post.setArturl(artUrl);
			post.setWebid(newspaperWebCfg.getWebid());
			post.setWebname(newspaperWebCfg.getWebname());
			post.setWeburl(newspaperWebCfg.getWeburl());
			post.setWebtype("10");
			for(int i = 0,len = artlist.size() ; i < len ; i ++){
				TBLNewspaperElementConfig artCfg = artlist.get(i);
				String content = collectArt(html,artCfg);
				if(artCfg.getElementtype().equals("PUBTIME")){
					content = htmlUtil.delHTMLTag(content, "span");
					post.setArtpubdate(DateTimeUtil.fmtDate(content));
					post.setArtpubtime(DateTimeUtil.fmtTime(content));
				}else{
					content = UnicodeConverter.decode(content, 0);
					content = htmlUtil.htmlFilter(content);
					if(artCfg.getElementtype().equals("CONTENT")){
						content = htmlUtil.delHTMLTag(content, "IMG|P|BR");
						content = imageConv.delIMGTag(artUrl, content);
						post.setArtcontent(content);
					}else{
						content = htmlUtil.delHTMLTag(content, " ");
						if(artCfg.getElementtype().equals("TITLE")){
							post.setArttitle(content);
						}else if(artCfg.getElementtype().equals("AUTHOR")){
							post.setArtauthor(content);
						}else if(artCfg.getElementtype().equals("SOURCE")){
							post.setArtsource(content);
						}
					}
				}
			}
			//保存都是同一个表中保存，所以都放到一个阻塞队列中即可
			CollectNewsHelper.saveQueue.put(post);
		} catch (Exception e) {
			LogUtil.addLogError(artUrl+"文章信息分析异常",this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 验证方法
	 * @return 返回字符串数据
	 */
	private String collectArt(String content,TBLNewspaperElementConfig elecfg){
		String contentTemp = "";
		String contentStr = "";
		if(elecfg.getRuletype().equals("LABEL")){
			if(elecfg.getMatchrule().length()>1&&elecfg.getMatchrule().trim().indexOf("<")!=0)
				return " ";
			HtmlUtil htmlUtil = new HtmlUtil();
			//标签选择器
			if(elecfg.getMatchorder().toString().equals("1")){//说明是第一个
				if(content.indexOf(elecfg.getMatchrule())>-1){
					if(null==elecfg.getChildlabel()||"".equals(elecfg.getChildlabel())){//说明没有子标记
						contentStr = htmlUtil.unDoNestTag(content, elecfg.getMatchrule());
					}else{
						contentTemp = content.substring(content.indexOf(elecfg.getMatchrule())+elecfg.getMatchrule().length());
						if(contentTemp.indexOf(elecfg.getChildlabel())>-1)
							contentStr = htmlUtil.unDoNestTag(contentTemp, elecfg.getChildlabel());
					}
				}
			}else{
				//不是第一个 首先找到开始位置之后
				if(content.indexOf(elecfg.getMatchrule())>-1){
					contentTemp = content.substring(content.indexOf(elecfg.getMatchrule())+elecfg.getMatchrule().length());
					for(int index = 1 ; index<elecfg.getMatchorder().intValue() ; index++ ){
						if(contentTemp.indexOf(elecfg.getMatchrule())>-1){
							if(elecfg.getMatchorder().intValue()-1==index){
								contentTemp = contentTemp.substring(contentTemp.indexOf(elecfg.getMatchrule()));
								continue;
							}
							contentTemp = contentTemp.substring(contentTemp.indexOf(elecfg.getMatchrule())+elecfg.getMatchrule().length());
						}
						else{ 
							contentTemp = "";
							break;
						}
					}
					if(!contentTemp.equals("")){
						if(null==elecfg.getChildlabel()||"".equals(elecfg.getChildlabel())){//说明没有子标记
							if(contentTemp.indexOf(elecfg.getMatchrule())>-1)
								contentStr = htmlUtil.unDoNestTag(contentTemp, elecfg.getMatchrule());
						}else{
							if(contentTemp.indexOf(elecfg.getChildlabel())>-1){
								contentTemp = contentTemp.substring(elecfg.getMatchrule().length());
								contentStr = htmlUtil.unDoNestTag(contentTemp, elecfg.getChildlabel());
							}
						}
					}
				}
			}
		}else if(elecfg.getRuletype().equals("CLASS")||elecfg.getRuletype().equals("ID")){
			if(elecfg.getRuletype().equals("CLASS")&&elecfg.getMatchrule().trim().toLowerCase().indexOf("class")!=0)
				return " ";
			if(elecfg.getRuletype().equals("ID")&&elecfg.getMatchrule().trim().toLowerCase().indexOf("id")!=0)
				return " ";
			//class选择器 或者 id选择器
			int fristIndex = content.indexOf(elecfg.getMatchrule());
			if(fristIndex>-1){
				contentTemp = content.substring(fristIndex);
				contentTemp = contentTemp.substring(contentTemp.indexOf(">")+1);
				contentStr = contentTemp.substring(0,contentTemp.indexOf("<"));
				contentStr = contentStr.equals("")?" ":contentStr;
			}
		}else if(elecfg.getRuletype().equals("MARK")){
			//固定标示
			String[] mRules = elecfg.getMatchrule().split("&&&");
			if(content.indexOf(mRules[0])>-1){
				int fristIndex = content.indexOf(mRules[0])+mRules[0].length();
				contentTemp = content.substring(fristIndex);
				int lastIndex = contentTemp.indexOf(mRules[1]);
				if(lastIndex>-1){
					contentStr = contentTemp.substring(0,contentTemp.indexOf(mRules[1]));
					contentStr = contentStr.equals("")?" ":contentStr;
				}
			}
		}
		return contentStr;
	}
	
	public void setNewspaperWebCfg(TBLNewspaperWebConfig newspaperWebCfg) {
		this.newspaperWebCfg = newspaperWebCfg;
	}
	public void setArtUrl(String artUrl) {
		this.artUrl = artUrl;
	}

}
