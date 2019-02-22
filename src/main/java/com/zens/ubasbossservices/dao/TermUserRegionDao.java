/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.dao								 
 **    Type    Name : TermUserRegionDao 							     	
 **    Create  Time : 2017年3月28日 下午4:42:16								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年3月28日下午4:42:16
 * @version 1.0 
 */
public interface TermUserRegionDao {
	
	/** 
	 * 根据终端用户GUID获取指点字段的记录，如果不指定字段，默认返回所有字段。
	 * @author floristy
	 * @create 2017年3月28日 下午4:45:31
	 * @update
	 * @param  
	 * @return List<Record>
	 */
	public List<Record> getBySubsGUID(String trueField, String subsGUID) throws Exception;

}
