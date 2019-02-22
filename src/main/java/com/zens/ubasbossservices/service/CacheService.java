/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.service								 
 **    Type    Name : CacheService 							     	
 **    Create  Time : 2017年3月28日 下午5:21:26								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年3月28日下午5:21:26
 * @version 1.0 
 */
public interface CacheService {
	
	/** 
	 * 根据多个区域的GUID从缓存获取对应区域记录并返回
	 * @author floristy
	 * @create 2017年3月28日 下午5:40:04
	 * @update
	 * @param  
	 * @return List<Record>
	 */
	public List<Record> getRegionsByRegionGUIDs(List<Record> termUserRegions) throws Exception;
	
	/** 
	 * 判断当前应用中的缓存是否为空，如果为空，更新缓存。
	 * @author floristy
	 * @create 2017年3月28日 下午5:41:08
	 * @update
	 * @param  
	 * @return void
	 */
	public void checkAndUpdateCache() throws Exception;
	
	/** 
	 * 获取当前使用的缓存名
	 * @author floristy
	 * @create 2017年3月28日 下午5:42:42
	 * @update
	 * @param  
	 * @return void
	 */
	public String getCacheName() throws Exception;
	
	public void start(int isWaite) throws Exception;

}
