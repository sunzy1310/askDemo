package com.keyten.spider.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.keyten.spider.controller.AttentionNewsPLThread;
import com.keyten.spider.controller.AttentionNewsThread;
import com.keyten.spider.controller.SpreadNewsAnalysisThread;
import com.keyten.spider.controller.SpreadNewsCollectThread;


/**
 * 线程系统监听器
 *
 */
public class TaskStartListener implements ServletContextListener {
    /**
     * Default constructor. 
     */
    public TaskStartListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	WebApplicationContext ctx = 
    			WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
    	AttentionNewsThread attentionNewsThread = 
    			(AttentionNewsThread) ctx.getBean("attentionNewsThread");
		AttentionNewsPLThread attentionNewsPLThread = 
				(AttentionNewsPLThread) ctx.getBean("attentionNewsPLThread");;
    	SpreadNewsAnalysisThread spreadNewsAnalysisThread = 
    			(SpreadNewsAnalysisThread) ctx.getBean("spreadNewsAnalysisThread");
    	SpreadNewsCollectThread spreadNewsCollectThread = 
    			(SpreadNewsCollectThread) ctx.getBean("spreadNewsCollectThread");
    	//网名关注热点新闻采集
		new Thread(attentionNewsThread).start();
		//网民关注热点新闻评论采集
		new Thread(attentionNewsPLThread).start();
		//媒体传播热点新闻  事件分析
		new Thread(spreadNewsAnalysisThread).start();
		//媒体传播热点新闻内容采集
		new Thread(spreadNewsCollectThread).start();
    }
	
}
