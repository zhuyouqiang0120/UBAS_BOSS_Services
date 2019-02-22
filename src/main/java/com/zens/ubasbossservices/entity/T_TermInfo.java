/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.entity								 
 **    Type    Name : T_TermInfo 							     	
 **    Create  Time : 2017年4月5日 下午4:32:40								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.zens.ubasbossservices.dao.TermInfoDao;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年4月5日下午4:32:40
 * @version 1.0 
 */
public class T_TermInfo extends Model<T_TermInfo> implements TermInfoDao {

	private static final long serialVersionUID = 1L;

	public static T_TermInfo dao = new T_TermInfo();
	
	public static String tableName = TableMapping.me().getTable(T_TermInfo.class).getName();

	
	
	/* 根据uniqueKey找到该终端设备的GUID。
	 * @see com.zens.ubasbossservices.dao.TermInfoDao#getGUID(java.lang.String, java.lang.String)
	 */
	@Override
	public String getGUID(String trueField, String uniqueKey) throws Exception {
		String sql = "SELECT FGUID FROM " + tableName + " WHERE " + trueField + " = ?" ;
		Record termUser = Db.findFirst(sql, uniqueKey);
		String guid = termUser.getStr("FGUID");
		return guid;
	}

}
