package com.zens.ubasbossservices.log;


import com.jfinal.log.Log;


public class APPUserModulelog {
	public static Log log = Log.getLog("APPUserModulelog");
	
	/*
	 * 此方法记录的日志级别为 info
	 */
	public static void server(String connent) {
		log.info(connent);
		
		
	}

}
