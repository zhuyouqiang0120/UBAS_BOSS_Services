/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.utils								 
 **    Type    Name : RecordUtil 							     	
 **    Create  Time : 2017年3月7日 下午1:35:00								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.utils;

import java.util.List;

import com.chasonx.tools.StringUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import net.sf.json.JSONObject;

/**
<<<<<<< HEAD
 * @author Floristy
 * @email yangsen@zensvision.com
 * @create 2017年3月7日下午1:35:00
 * @version 1.0
 */
public class RecordUtil {

	/**
	 * 为Record添加属性
	 * 
	 * @author floristy
	 * @create 2017年3月7日 下午12:26:43
	 * @update
	 * @param
	 * @return void
	 */
	public static void setRecord(List<String> paramNames, JSONObject paramData, Record record) throws Exception {
		try {
			for (String paramName : paramNames) {
				
				String value = (String) paramData.get(paramName);
		
			
				if (StringUtils.hasText(value)) {
					String sqlKey = "F" + StrKit.firstCharToUpperCase(paramName);
					record.set(sqlKey, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
