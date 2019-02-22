/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.dao								 
 **    Type    Name : SubscriberPackageDao 							     	
 **    Create  Time : 2017年3月15日 下午5:57:31								
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
 * @create  2017年3月15日下午5:57:31
 * @version 1.0 
 */
public interface SubscriberPackageDao {
	
	public boolean queryByGUID(String GUID) throws Exception;

}
