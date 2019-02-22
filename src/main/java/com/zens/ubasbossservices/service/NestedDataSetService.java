/**********************************************************************
 **********************************************************************
 **    Project Name : NestDataSetService
 **    Package Name : com.zens.nestdataset.service								 
 **    Type    Name : NestDataSetService 							     	
 **    Create  Time : 2016年10月11日 下午3:54:11								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**系统嵌套数据管理接口
 * @author  Chasonx
 * @email   xzc@zensvision.om
 * @create  2016年10月11日下午3:54:11
 * @version 1.0 
 */
public interface NestedDataSetService {
	/** 查询所有字段
	 * @author jhonson
	 * @create 2016年12月23日 下午12:44:42
	 * @update
	 * @param  
	 * @return void
	 */
	public List<Record> getFields();
	/**
	 * 添加一行维度数据
	 * @author chasonx
	 * @create 2016年10月11日 下午4:09:37
	 * @update
	 * @param typeGuid value envalue parentGuid level icon remark state type extdata clientGuid
	 * @return boolean
	 */
	public boolean saveNestedDataSet(String __param) throws Exception;
	/**
	 * 批量更新
	 * @author chasonx
	 * @create 2016年10月11日 下午4:09:59
	 * @update
	 * @param  guid typeGuid value envalue parentGuid level icon remark state type extdata
	 * @return boolean
	 */
	public boolean batchUpdate(String __param) throws Exception;
	/**
	 * 批量删除
	 * @author chasonx
	 * @create 2016年10月11日 下午4:10:07
	 * @update
	 * @param  guid
	 * @return boolean
	 */
	public boolean delByGUIDs(String __param) throws Exception;
	/** 
	 * 批量恢复删除
	 * @author jhonson
	 * @create 2017年2月22日 下午2:03:38
	 * @update
	 * @param  
	 * @return boolean
	 */
	public boolean unDelByGUIDs(String __param) throws Exception;
	/**
	 * 
	 * @author chasonx
	 * @create 2016年10月11日 下午4:39:54
	 * @update
	 * @param  derection={top,bottom,append} sourceGuid targetGuid
	 * @return boolean
	 */
	public boolean move(String __param) throws Exception;
	/**
	 * 获取所有嵌套数据
	 * @author chasonx
	 * @create 2016年10月11日 下午4:27:30
	 * @update
	 * @param  state = null 查询所有  ,  type
	 * @return List<TDimension>
	 */
	public List<Record> getAllNestedDataSet(String __param) throws Exception;
	/** 
	 * 获取所有未嵌套的数据（不包含已删除的数据）
	 * @author Johnson
	 * @create 2017年2月9日 下午1:35:15
	 * @update
	 * @param  
	 * @return List<Record>
	 */
	public List<Record> getAllDataSet(String __param);
}
