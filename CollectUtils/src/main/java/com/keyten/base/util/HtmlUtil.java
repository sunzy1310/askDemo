package com.keyten.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlUtil {
	
	/**
	 * 过滤采集内容中的特殊字符
	 * @return
	 */
	public String htmlFilter(String  strContent)
	{
		String strResult = strContent;
		try
		{
			//去掉内容最前面和末尾的空格
			strResult = strResult.trim();
			//去掉反斜杠\
			strResult = strResult.replaceAll("\\\\","\\\\\\\\");//首先将\替换为\\
			strResult = strResult.replaceAll("\\\\","");//然后将\\替换为空
			
			//影响平台分割数据
			strResult = strResult.replaceAll("\\|","");//竖杠
			strResult = strResult.replaceAll("\\|\\|", "");//双竖杠
			strResult = strResult.replaceAll("\\^","");
			
			//
            strResult = strResult.replaceAll("	", "");//tab
            strResult = strResult.replaceAll("'", "");//单引号
            strResult = strResult.replaceAll("&nbsp;", " ");//空格
            strResult = strResult.replaceAll("  ", " ");//tab
            strResult = strResult.replaceAll("[\\s\\u00A0]+", " ");//ASCII 码 160空格过滤
            
            //去掉回车换行符
            strResult = strResult.replaceAll("\r", " ");//换行
            strResult = strResult.replaceAll("\n", " ");//回车
            
            
			
            
            
            return strResult;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strResult;
	}	
	/**
	 * 通过传入的头标签从文中找到相应的尾标签
	 * @return
	 */
	public String unDoNestTag(String conHtml,String matchTag){
		String strContent = "";
		try{
			conHtml = conHtml.replaceAll("<(\\\\)+/","</");
//			System.out.println(conHtml);
			conHtml = conHtml.substring(conHtml.indexOf(matchTag));
			//判断为哪种标签
			String fristTag = matchTag;
			if(matchTag.contains(" ")){
				fristTag = matchTag.substring(0,matchTag.indexOf(" "));
			}
			String lastTag = "</"+fristTag.substring(1)+">";
			//用于返回得到的内容
			strContent = fristTag;
			//去掉头标签
			conHtml = conHtml.substring(fristTag.length());
			//嵌套Div的层数,默认为0
			int i=0;
			while(true)
			{
				if(conHtml.indexOf(fristTag)>=0)
				{
					if(conHtml.indexOf(lastTag)>conHtml.indexOf(fristTag))
					{
						//System.out.println("if");
						strContent = strContent+ conHtml.substring(0,conHtml.indexOf(fristTag));
						conHtml = conHtml.substring(conHtml.indexOf(fristTag));
						strContent = strContent+ conHtml.substring(0,conHtml.indexOf(">")+1);
						conHtml = conHtml.substring(conHtml.indexOf(">")+1);
						i++;
					}
					else
					{
						//System.out.println("if-else");
						strContent = strContent + conHtml.substring(0,conHtml.indexOf(lastTag)+lastTag.length());
						conHtml = conHtml.substring(conHtml.indexOf(lastTag)+lastTag.length());
						i--;
					}
				}
				else if(conHtml.indexOf(fristTag)<0)
				{
					//System.out.println("else");
					strContent = strContent + conHtml.substring(0,conHtml.indexOf(lastTag)+lastTag.length());
					conHtml = conHtml.substring(conHtml.indexOf(lastTag)+lastTag.length());
					i--;
				}
				if(i<0)
				{
					break;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		strContent = strContent.equals("")?" ":strContent;
		return strContent;
	}
	/**
	 * 去掉html标签的方法  单独剩下tags
	 * @param htmlStr
	 * @param tags 要保留的标签       可以为多个   格式为     IMG|tr|TD......
	 * @return
	 */
	public String delHTMLTag(String htmlStr,String tags)
	{
		try
		{
			tags = (null==tags||tags.equals(""))?" ":tags;
			String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	        String regEx_zhushi="<!--[\\s\\S]*?-->"; //定义style的正则表达式 
	        String regEx_html="<(?!/?(?i)("+tags+")).*?>"; //定义HTML标签的正则表达式 （?i:忽略大小写） 除了(img|IMG|iMg...)
	         
	        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        Matcher m_script=p_script.matcher(htmlStr); 
	        htmlStr=m_script.replaceAll(""); //过滤script标签 
	         
	        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        Matcher m_style=p_style.matcher(htmlStr); 
	        htmlStr=m_style.replaceAll(""); //过滤style标签 
	        
	        Pattern p_zhushi=Pattern.compile(regEx_zhushi,Pattern.CASE_INSENSITIVE); 
	        Matcher m_zhushi=p_zhushi.matcher(htmlStr); 
	        htmlStr=m_zhushi.replaceAll(""); //过滤style标签 
	         
	        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	        Matcher m_html=p_html.matcher(htmlStr); 
	        while(m_html.find()){
	        	String html = m_html.group(0);
		        Matcher cm_html=p_html.matcher(html.substring(1)); 
	        	if(cm_html.find()){
	        		String c_html = cm_html.group();
	        		htmlStr=htmlStr.replace(c_html,""); //过滤html标签 
	        	}else{
	        		htmlStr=htmlStr.replace(html,""); //过滤html标签 
	        	}
	        }
	        //二次过滤    <a alt="<dafdsafdsaf></dsafdsaf>" href="daf" />的情况
	        m_html=p_html.matcher(htmlStr); 
	        htmlStr=m_html.replaceAll(""); //过滤html标签 
	        
	        
	        //去掉空白区域
//	        htmlStr = htmlStr.replaceAll(" ","");
//	        htmlStr = htmlStr.replaceAll("	","");
//	        htmlStr = htmlStr.replaceAll("\r\n","");
			
	        return htmlStr;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 去掉URL中的相对路劲部分
	 * @param url
	 * @return
	 */
	public String fmtUrl(String url){
		try {
			while(url.contains("../")){
				url = url.substring(url.indexOf("../")+3);
			}
			while(url.contains("./")){
				url = url.substring(url.indexOf("./")+2);
			}
			while(url.contains("/")){
				if(url.indexOf("/")>0){
					break;
				}
				url = url.substring(url.indexOf("/")+1);
			}
			url = url.replaceAll("&amp;","&");
			url = url.replaceAll(" ","");//去掉空格
//			System.out.println(url+"!");
			if(url.indexOf("href=")==0||url.indexOf("href='")==0||url.indexOf("href=\"")==0){
				url = url.replaceAll("href=(\"|')?","");
			}
			if(url.indexOf("\"")==0||url.indexOf(":")==0){
				url = url.replaceAll("\"","");
				url = url.substring(1);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return url;
	}
	/**
	 * 格式化URL信息    将特殊字符、汉字等替换且转为URI
	 * @param url
	 * @param charset
	 * @return
	 */
	public String urlEncoder(String url,String charset){
		String urlWord = "";
		try {
			url = url.replace("{","%7b");//替换字符
			url = url.replace("}","%7d");//替换字符
			url = url.replace("\\","");//替换字符
			Pattern patt = Pattern.compile("[\u4E00-\u9FFF]+");
			Matcher mat = patt.matcher(url);
			if(mat.find()){
				String chinese = mat.group();
				urlWord = URLEncoder.encode(chinese,charset);
				url = url.replaceAll("[\u4E00-\u9FFF]+", urlWord);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return url;
	}
	public URI fmtUrl2Uri(String url){
		URI uri = null;
		try {
			URL url1 = new URL(url);
			uri = new URI(url1.getProtocol(), url1.getHost(), url1.getPath(), url1.getQuery(),null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return uri;
	}
	public static void main(String [] args)
	{
		HtmlUtil t = new HtmlUtil();
//		String url = "http://adsfdasfdsaf.wdfwef.sfaads/dsafda/我勒个去";
		String str = "<!--dfewag<input type='' adf='' src='<dsafdasf><url></url>' />--><!--vreh--><dsafdasf sarc=<dsafdsaf>>aregh";
//		str = t.unDoNestTag(str, "<link");
		str = t.delHTMLTag(str, "img");
//		str = t .urlEncoder(str, "UTF-8");
		System.out.println(str);
//		System.out.prin
//		System.out.println(t.urlEncoder(url,"GBK"));
		
	}
	
}
