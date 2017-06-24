package com.Music.utils;
/**
 * 日志工具类同一输出日志类
 * @author songshixin
 *
 */
public class Logger {
	private static String name="";
	private static final org.apache.log4j.Logger logger=org.apache.log4j.Logger.getLogger(name);
	public static org.apache.log4j.Logger getlogger(String classname){
		name=classname;
		return logger;
	}
}
