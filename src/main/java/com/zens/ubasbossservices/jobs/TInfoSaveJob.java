/**********************************************************************
 **********************************************************************
 **    Project Name : UTerminalInfoService
 **    Package Name : com.zens.ubasservice.jobs								 
 **    Type    Name : TInfoSaveJob 							     	
 **    Create  Time : 2016年10月19日 上午11:02:58								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.jobs;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zens.ubasbossservices.service.CacheService;
import com.zens.ubasbossservices.serviceimpl.CacheServiceImpl;

/**
 * @author  Jhonson
 * @email   yangsen@zensvision.om
 * @create  2016年10月19日上午11:02:58
 * @version 1.0 
 */
public class TInfoSaveJob implements Job {	
	
	static Logger logger = LogManager.getLogger(TInfoSaveJob.class.getName());

	/* 本方法用来将当前存储记录的缓存中的数据一个个地取出来，存进数据库，并清空当前存储记录的这个缓存。
	 * 当然在这之前要先切换一下当前存记录的缓存，一方再清空前的那一瞬间，TerminalInfoImpl类又往这个缓存中存了记录，那
	 * 这个记录还没来得及保存到数据库就直接被清空了。
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		CacheService cacheService = new CacheServiceImpl();
		try {
			cacheService.start(0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e); 
		}
	}
	

}
