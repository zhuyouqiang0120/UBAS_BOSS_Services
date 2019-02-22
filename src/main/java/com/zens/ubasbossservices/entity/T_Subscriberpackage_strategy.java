/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.entity								 
 **    Type    Name : SubscriberPackageDao 							     	
 **    Create  Time : 2017年3月7日 上午10:35:16								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;

import com.jfinal.plugin.activerecord.TableMapping;

/**
 * @author Floristy
 * @email yangsen@zensvision.com
 * @create 2017年3月7日上午10:35:16
 * @version 1.0
 */
public class T_Subscriberpackage_strategy extends Model<T_Subscriberpackage_strategy> {

	private static final long serialVersionUID = 1L;
	public static T_Subscriberpackage_strategy dao = new T_Subscriberpackage_strategy();

	public static final String tableName = TableMapping.me().getTable(T_Subscriberpackage_strategy.class).getName();

	/*
	 * 根据GUID查询，有没有某条业务包记录。
	 * 
	 * @see
	 * com.zens.ubasbossservices.dao.SubscriberPackageDao#queryByGUID(java.lang.
	 * String)
	 */

}
