/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.dao								 
 **    Type    Name : TermUserDao 							     	
 **    Create  Time : 2017年3月28日 下午3:21:49								
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
 * @create  2017年3月28日下午3:21:49
 * @version 1.0 
 */
public interface TermUserDao {
	
	/** 
	 * 根据uniqueKey找到该终端用户的GUID（uniqueKey是任意能唯一表示注册用户的字段。）
	 * @author floristy
	 * @create 2017年3月28日 下午3:24:18
	 * @update
	 * @param  
	 * @return boolean
	 */
	public String getGUID(String trueField, String uniqueKey) throws Exception;

}
