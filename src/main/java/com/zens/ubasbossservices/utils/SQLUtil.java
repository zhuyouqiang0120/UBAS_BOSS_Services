/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.utils								 
 **    Type    Name : SQLUtil 							     	
 **    Create  Time : 2017年3月7日 下午3:45:03								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.utils;

import java.util.List;
import java.util.Map;

import com.chasonx.tools.StringUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import net.sf.json.JSONObject;

/**
 * 
 * @author Floristy
 * @email yangsen@zensvision.com
 * @create 2017年3月7日下午3:45:03
 * @version 1.0
 */

public class SQLUtil {

	/**
	 * 循环拼出SQL片段
	 * 
	 * @author Johnson
	 * @create 2017年2月10日 下午2:28:59
	 * @update
	 * @param
	 * @return void
	 */
	public static void addParamsToSetSQL(StringBuffer setSQL, JSONObject jO, List<String> setNames) throws Exception {
		for (String setName : setNames) {
			String value = (String) jO.get(setName);
			if (value != null) {
				String keyName = "F" + StrKit.firstCharToUpperCase(setName);
				setSQL.append(keyName);
				setSQL.append(" = '");
				setSQL.append(value);
				setSQL.append("' ");
				setSQL.append(", ");
			}
		}
	}

	/**
	 * 根据参数拼出SQL语句中的WHERE片段
	 * 
	 * @author Johnson
	 * @create 2017年2月27日 上午11:00:36
	 * @update
	 * @param
	 * @return String
	 */
	public static String getWhereSql(Map<String, String> map) throws Exception {
		StringBuffer whereSql = new StringBuffer(" WHERE 1=1 ");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String keyName = entry.getKey();
			String value = entry.getValue();

			if (StringUtils.hasText(value)) {
				// 查询按天计费的业务包
				if (keyName.equals("FCycle") && value.equals("1")) {
					whereSql.append(" AND ");
					whereSql.append(keyName + "!=0");
					whereSql.append(" ");

				} else if (keyName.equals("FCycle") && value.equals("2")) {
					// 查询自定义天数计费的业务包
					whereSql.append(" AND ");
					whereSql.append(keyName + "%30");
					whereSql.append(" != '");
					whereSql.append("0");
					whereSql.append("' ");
				} else {
					// 查询按次的业务包
					whereSql.append(" AND ");
					whereSql.append(keyName);
					whereSql.append(" = '");
					whereSql.append(value);
					whereSql.append("' ");
				}
			}
		}
		return whereSql.toString();
	}

	public static String getterminoWhereSql(Map<String, String> map) throws Exception {
		StringBuffer whereSql = new StringBuffer(" WHERE 1=1 ");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String keyName = entry.getKey();
			String value = entry.getValue();

			if (StringUtils.hasText(value)) {
				// 查询按天计费的业务包
				if (keyName.equals("FCycle") && value.equals("1")) {
					whereSql.append(" AND ");
					whereSql.append(keyName + "!=0");
					whereSql.append(" ");

				} else if (keyName.equals("FCycle") && value.equals("2")) {
					// 查询自定义天数计费的业务包
					whereSql.append(" AND ");
					whereSql.append(keyName + "%30");
					whereSql.append(" != '");
					whereSql.append("0");
					whereSql.append("' ");
				} else {
					// 查询按次的业务包
					whereSql.append(" AND ");
					whereSql.append("f." + keyName);
					whereSql.append(" = '");
					whereSql.append(value);
					whereSql.append("' ");
				}
			}
		}
		return whereSql.toString();
	}

	/**
	 * 获取用户分页的SQL片段
	 * 
	 * @author jhonson
	 * @create 2017年2月22日 下午3:14:16
	 * @update
	 * @param
	 * @return String
	 */
	public static String getLimitSQL(Integer pageSize, Integer page) throws Exception {
		if (page == null) {
			return "";
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		StringBuffer limitSQL = new StringBuffer(" LIMIT ");
		limitSQL.append(pageSize * (page - 1));
		limitSQL.append(", ");
		limitSQL.append(pageSize);
		return limitSQL.toString();
	}

	/**
	 * 将前台传的字段名转换成数据库中对应的字段名（比如将"level"转换成"FLevel"）。
	 * 
	 * @author floristy
	 * @create 2017年3月28日 下午4:16:08
	 * @update
	 * @param >>>>>>>
	 *            branch 'master' of
	 *            https://git.oschina.net/zensvision/ubas-boss-service.git
	 * @return String
	 */
	public static String getTrueField(String field) throws Exception {
		StringBuffer trueField = new StringBuffer("F");
		trueField.append(StrKit.firstCharToUpperCase(field));
		return trueField.toString();
	}

	/**
	 * 
	 * 数据导入字段匹配
	 * 
	 * @author
	 * @create 2018年1月2日 14:58:04
	 * @update
	 * @param
	 * @return String
	 */
	public Record BossimportRecord(String orgin, Record record,String value) {
		switch (orgin) {
		case "USER_CODE":
			record.set("FSubsGUID", value);
			break;
		case "PRODOFFERING_CODE":
			record.set("FSubscriberPackageGUID", value);
			break;
		case "SUBSCRIBE_TIME":
			record.set("FSubsGUID", value);
			break;
		case "ENABLE_TIME":
			record.set("FSubsGUID", value);
			break;
		case "DISABLE_TIME":
			record.set("FSubsGUID", value);
			break;
		case "UPDATE_TIME":
			record.set("FSubsGUID", value);
			break;
		case "STATUS":
			record.set("FSubsGUID", value);
			break;
		
		default:
			break;
		}

		return record;
	}

}
