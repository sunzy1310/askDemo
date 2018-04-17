package com.keyten.spider.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.keyten.spider.controller.NewsArtInfoCollectThread;
import com.keyten.spider.controller.NewsUrlCollectThread;
import com.keyten.spider.controller.NewsUrlCompareThread;
import com.keyten.spider.controller.SaveThread;

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
    	NewsUrlCollectThread urlCollectThread = 
    			(NewsUrlCollectThread) ctx.getBean("newsUrlCollectThread");
		NewsUrlCompareThread urlCompareThread = 
				(NewsUrlCompareThread) ctx.getBean("newsUrlCompareThread");;
    	NewsArtInfoCollectThread artCollectThread = 
    			(NewsArtInfoCollectThread) ctx.getBean("newsArtInfoCollectThread");
    	SaveThread saveThread = (SaveThread) ctx.getBean("saveThread");
    	//采集url调度线程
		new Thread(urlCollectThread).start();
		//url对比线程
		new Thread(urlCompareThread).start();
		//内容采集调度线程
		new Thread(artCollectThread).start();
		//信息存储线程	
		new Thread(saveThread).start();
    }
	
}
