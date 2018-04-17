package com.keyten.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class imageSrcConvers {
	

	/**
	 * @param 
	 * @param 
	 * @return
	 */
	public String delIMGTag(String weburl,String content)
	{
		try
		{   String regEx_html="<(?i)(IMG)[^>]*>";
	         
	        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	        Matcher m_html=p_html.matcher(content); 
	       while (m_html.find()) {
	        	 String imgtext=m_html.group(0);
	        	 imgtext=imgtext.replaceAll(" ", "'");
	        	 imgtext=imgtext.replaceAll("\"", "'");
	        	 imgtext=imgtext.replaceAll("src=", "src='");
	        	while(imgtext.contains("''")){
	        		 imgtext=imgtext.replaceAll("''", "'");
	        	}
	        	if(imgtext.contains("src='")){
	        		   String imgsrc=imgtext.substring(imgtext.indexOf("src='")+5) ;
	        		   if(imgsrc.contains("'")){
	        		     imgsrc=imgsrc.substring(0,imgsrc.indexOf("'"));
	        		     String imgurl=this.getRightUrl( weburl,imgsrc);
	        		     content= content.replace(imgsrc, imgurl); 
	        		   }
	        	}
	        	if(imgtext.contains("SRC='")){
	        		   String imgsrc=imgtext.substring(imgtext.indexOf("SRC='")+5) ;
	        		   if(imgsrc.contains("'")){
	        		     imgsrc=imgsrc.substring(0,imgsrc.indexOf("'"));
	        		     String imgurl=this.getRightUrl( weburl,imgsrc);
	        		     content= content.replace(imgsrc, imgurl); 
	        		   }
	        	}
	        	 
	        } 
			
	        return content;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return content;
		}
	}

	
	
	public String getRightUrl(String weburl,String outurl){
		try {
			int i=0;
		   while(outurl.indexOf(weburl.substring(0, 7))<0&&weburl.indexOf("http")==0){
			   i++;
			   if(i>5){
				   break;
			   }
			  if(outurl.indexOf("../")==0){
				  String weburlbegin=weburl;
				while(outurl.indexOf("../")==0){
				  weburlbegin=weburlbegin.substring(0, weburlbegin.lastIndexOf("/"));
				  weburlbegin=weburlbegin.substring(0, weburlbegin.lastIndexOf("/")+1);
				  outurl= outurl.substring(3) ;
//				  System.out.println(weburlbegin);
//				  System.out.println(outurl);
				}
				outurl=weburlbegin+outurl;
			  }else if(outurl.indexOf("./")==0){
				  outurl= weburl.substring(0, weburl.lastIndexOf("/"))+outurl.substring(1) ;
			  }else if(outurl.indexOf("/")==0){
				  outurl= weburl.substring(0, weburl.indexOf("/", 8))+outurl ;
			  }else{ 
				  outurl= weburl.substring(0, weburl.lastIndexOf("/")+1)+outurl ;
			  }
		   }
		} catch (Exception e) {
		   e.printStackTrace();
		}
		   
		return   outurl; 
	}
	
	
	
	
	
	
	public static void main(String [] args)
	{
//		imageSrcConvers t = new imageSrcConvers();
//		String url = "http://adsfdasfdsaf.wdfwef.sfaads/dsafda/���ո�ȥ";
//		String str = "<><IMG src=\"../../../images/1/ccwb/2017-06/11/A16/20170611A16_res12_attpic_icon.jpg\"><span class=\\\"friend_5f3a5350d68a794036a26bccd7754a70 sendfriendapply\\\" action_data=\\\"bulaozhuai@sohu.com\\\">\\u52a0\\u597d\\u53cb<\\/span>";
//		str = t.delIMGTag("http://ccwb.yunnan.cn/html/2017-06/11/content_1153910.htm",str);
//		System.out.println(str);
		String weburl="https://dzb.cien.com.cn";
//		String outurl=t.getRightUrl("http://dzb.cien.com.cn/zgcjxw/20170623/html/index_content_000.htm","f04581aa6bcf4af2a5fdfbcdda94569d.png");
		System.out.println(weburl.substring(0, 7));
//		System.out.prin
//		System.out.println(t.urlEncoder(url,"GBK"));
		
	}
	
}
