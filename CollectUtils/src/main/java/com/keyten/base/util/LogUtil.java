package com.keyten.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 日志类
 * 统一log4j日志添加入口
 * @author liym
 *
 */
public class LogUtil {
	private static Log infolog = LogFactory.getLog("infolog");
	private static Log errorlog = LogFactory.getLog("errorlog");
//	private static Logger log = Logger.getLogger(LogUtil.class);
	
	public synchronized static void addLogInfo(String message,String classname) {
		infolog.info(classname+" " +message);
	}
	
	public synchronized static void addLogError(String message,String classname) {
		errorlog.error(classname+" \r\n" +message);
	}
}
