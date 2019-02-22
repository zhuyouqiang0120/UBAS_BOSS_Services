/**********************************************************************
 **********************************************************************
getRegionsByRegionGUIDs **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.serviceimpl								 
 **    Type    Name : CacheServiceImpl 							     	
 **    Create  Time : 2017年3月28日 下午5:36:11								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.Constant;
import com.zens.ubasbossservices.common.HessianFactory;
import com.zens.ubasbossservices.service.CacheService;
import com.zens.ubasbossservices.service.NestedDataSetService;

import net.sf.ehcache.Cache;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年3月28日下午5:36:11
 * @version 1.0 
 */
public class CacheServiceImpl implements CacheService {
	
	static Logger logger = LogManager.getLogger(CacheServiceImpl.class.getName());
	String cache1Name = Constant.CACHE_NAME.UBAS_BOSS_SERVICES_CACHE1.toString();
	String cache2Name = Constant.CACHE_NAME.UBAS_BOSS_SERVICES_CACHE2.toString();
	int isWaite;
	List<Record> list = null;
	List<String> typeList = new ArrayList<String>();

	/* 根据多个区域的GUID从缓存获取对应区域记录并返回
	 * @see com.zens.ubasbossservices.service.CacheService#getRegionsByRegionGUIDs(java.util.List)
	 */
	@Override
	public List<Record> getRegionsByRegionGUIDs(List<Record> termUserRegions) throws Exception {
		List<Record> regions = new ArrayList<Record>();
		//去缓存中取出所有相关的区域记录
		checkAndUpdateCache();
		for (Record termUser_Region : termUserRegions) {
			String cacheName = getCacheName();
			String FRegionGUID = termUser_Region.getStr("FRegionGUID");
			Record region = new Record();
			region = CacheKit.get(cacheName, FRegionGUID);
			if (region != null) {
				regions.add(region);
			}
		}
		return regions;
	}

	/* 判断当前应用中的缓存是否为空，如果为空，更新缓存。
	 * @see com.zens.ubasbossservices.service.CacheService#checkAndUpdateCache(java.util.List)
	 */
	@Override
	public void checkAndUpdateCache() throws Exception {
		String cacheName = getCacheName();
		Cache cache = null;
		try {
			cache = CacheKit.getCacheManager().getCache(cacheName);
		} catch (Exception e) {
			System.out.println(e);
		}
		int size = cache.getSize();
		if (size == 0) {
			start(0);
		}
		
	}

	/* 获取当前使用的缓存名
	 * @see com.zens.ubasbossservices.service.CacheService#getCacheName()
	 */
	@Override
	public String getCacheName() throws Exception {
		String cacheName = null;
		if (Constant.isC1OrC2 == 1) {
			cacheName = Constant.CACHE_NAME.UBAS_BOSS_SERVICES_CACHE1.toString();
		} else if (Constant.isC1OrC2 == 2) {
			cacheName = Constant.CACHE_NAME.UBAS_BOSS_SERVICES_CACHE2.toString();
		}
		return cacheName;
		
	}

	/* (non-Javadoc)
	 * @see com.zens.ubasbossservices.service.CacheService#start(int)
	 */
	@Override
	public synchronized void start(int isWaite) throws Exception {
		isWaite = this.isWaite;
		String apiUrl = CacheKit.get(Constant.CACHE_NAME.UBASDATACHCACHE.toString()/*缓存名*/, Constant.UBAS_SERVICE_CONFIG.NESTEDDS.toString()/*键名*/); 
		Record record = new Record();
		String clientGuid = Constant.UBASBOSS_ClientGuid;		
		typeList.add("1"); // 区域管理
		typeList.add("2"); // 分组管理
		record.set("cid", clientGuid);
		record.set("typeList", typeList);
		String cacheName = null;
		String removeCacheName = null;	
		logger.warn("开始调NestedDS获取区域和分组数据并放入缓存");	
		String __param = record.toJson();
		try {
			NestedDataSetService nestDataSetService = HessianFactory.create(NestedDataSetService.class, apiUrl + ApiUtils.NESTEDDATASET);			 
			list = nestDataSetService.getAllDataSet(__param); // 调NestedDS获取所有UBAS_BOSS的区域和分组记录。
			} catch (Exception e) {
			e.printStackTrace();
		}
		if (Constant.isC1OrC2 == 1) {
			cacheName = cache2Name;
			removeCacheName = cache1Name;
			updateCache(cacheName, removeCacheName);
			Constant.isC1OrC2 = 2;
		}
		else if(Constant.isC1OrC2 == 2) {
			cacheName = cache1Name;
			removeCacheName = cache2Name;
			updateCache(cacheName, removeCacheName);
			Constant.isC1OrC2 = 1;
		}
		logger.warn("操作结束！");		
	}
	
	/** 
	 * 更新缓存
	 * @author Johnson
	 * @create 2017年2月23日 下午1:58:53
	 * @update
	 * @param  
	 * @return void
	 */
	public void updateCache(String cacheName,String removeCacheName) throws Exception {
		putDataIntoCache(list, cacheName);
		System.out.println("isWaite : " + isWaite);
		if(isWaite == 1) {
			// 等待1分钟。
			wait(2000); 
			//清空缓存2
			CacheKit.removeAll(removeCacheName);
		}	
	}
	
	/** 
	 * 将记录放进相应的缓存
	 * @author Johnson
	 * @create 2017年2月14日 下午4:49:53
	 * @update
	 * @param  
	 * @return void
	 */
	public synchronized void putDataIntoCache(List<Record> list, String cacheName) throws Exception {
		//遍历list，将每条记录的GUID取出来作为缓存记录的键名和该记录一起放入缓存中。 
		for (Record record : list) {
			String guid = record.getStr("guid");
			CacheKit.put(cacheName,guid,record);
		}
	}
	
	/** 
	 * 使程序等待指定的毫秒数
	 * @author Johnson
	 * @create 2017年2月14日 下午4:42:44
	 * @update
	 * @param  
	 * @return void
	 */
	private synchronized void wait(int mSeconds) throws Exception {		
		System.out.println("进入等待中...");
		Thread.sleep(mSeconds);
		System.out.println("等待结束");
	}

}
