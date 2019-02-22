/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.dao								 
 **    Type    Name : TermInfoDao 							     	
 **    Create  Time : 2017年4月5日 下午5:10:12								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.dao;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年4月5日下午5:10:12
 * @version 1.0 
 */
public interface TermInfoDao {
	
	/** 
	 * 根据uniqueKey找到该终端设备的GUID（uniqueKey是任意能唯一表示终端设备的字段。）
	 * @author floristy
	 * @create 2017年4月5日 下午5:11:33
	 * @update
	 * @param  
	 * @return String
	 */
	public String getGUID(String trueField, String uniqueKey) throws Exception;

}
