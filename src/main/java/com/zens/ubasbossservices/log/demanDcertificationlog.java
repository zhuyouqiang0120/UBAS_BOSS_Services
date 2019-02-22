package com.zens.ubasbossservices.log;


import com.jfinal.log.Log;


public class demanDcertificationlog{
	public static Log log = Log.getLog("Dcertificationlog");
	
	/*
	 * 此方法记录的日志级别为 info
	 */
	public static void server(String connent) {
		log.info(connent);
		
		
	}

}
