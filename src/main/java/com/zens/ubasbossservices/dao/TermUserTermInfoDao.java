/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.dao								 
 **    Type    Name : TermUserTermInfoDao 							     	
 **    Create  Time : 2017年4月5日 下午5:17:20								
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
 * @create  2017年4月5日下午5:17:20
 * @version 1.0 
 */
public interface TermUserTermInfoDao {
	
	/** 
	 * 根据field、uniqueKey找到终端设备的GUID
	 * @author floristy
	 * @create 2017年4月5日 下午5:21:00
	 * @update
	 * @param  
	 * @return String
	 */
	public String getSubsGUIDByTermInfoID(String termInfoID) throws Exception;

}
