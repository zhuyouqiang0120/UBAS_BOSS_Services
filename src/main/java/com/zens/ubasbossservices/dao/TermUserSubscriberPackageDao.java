/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.dao								 
 **    Type    Name : TermUserSubscriberPackageDao 							     	
 **    Create  Time : 2017年3月21日 下午12:37:07								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.dao;


import com.jfinal.plugin.activerecord.Record;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年3月21日下午12:37:07
 * @version 1.0 
 */
public interface TermUserSubscriberPackageDao {
	
	/** 
	 * 判断某个终端用户是否已开通了某个业务包并且还没有失效。
	 * @author floristy
	 * @create 2017年3月21日 下午12:40:58
	 * @update
	 * @param  
	 * @return boolean
	 */
	public boolean hasEffectiveRecord(String subsGUID, String subscriberPackageGUID) throws Exception;	
	
	/** 
	 * 设置指定记录的FSuspended值为0或1
	 * @author floristy
	 * @create 2017年3月21日 下午3:21:53
	 * @update
	 * @param  
	 * @return boolean
	 */
	public boolean suspendRecord(String subsGUID, String subscriberPackageGUID) throws Exception;
	
	/** 
	 * 为指定的终端用户继续某个业务包
	 * @author floristy
	 * @create 2017年3月21日 下午3:40:25
	 * @update
	 * @param  
	 * @return boolean
	 */
	public boolean continueRecord(String subsGUID, String subscriberPackageGUID) throws Exception;
	
	/** 
	 * 为指定的终端用户继续指定的业务包
	 * @author floristy
	 * @param subscriberRecord 
	 * @create 2017年3月23日 下午5:34:20
	 * @update
	 * @param  
	 * @return boolean
	 */
	public Record renewRecord(String subsGUID, String subscriberPackageGUID, String newExpiryTime, Record subscriberRecord,String cycle) throws Exception;

}
