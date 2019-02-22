/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.entity								 
 **    Type    Name : T_TermUserTermInfo 							     	
 **    Create  Time : 2017年4月5日 下午4:39:07								
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
import com.zens.ubasbossservices.dao.TermUserTermInfoDao;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年4月5日下午4:39:07
 * @version 1.0 
 */
public class T_TermUserTermInfo extends Model<T_TermUserTermInfo> implements TermUserTermInfoDao {

	private static final long serialVersionUID = 1L;

	public static T_TermUserTermInfo dao = new T_TermUserTermInfo();
	

	public static String tableName = TableMapping.me().getTable(T_TermUserTermInfo.class).getName();




	
	
	/* 根据field、uniqueKey找到终端设备的GUID
	 * @see com.zens.ubasbossservices.dao.TermUserTermInfoDao#getSubsGUIDByTermInfoID(java.lang.String)
	 */
	@Override
	public String getSubsGUIDByTermInfoID(String termInfoID) throws Exception {
		String sql = "SELECT FSubsGUID FROM " + tableName + " WHERE FTermInfoID = ?" ;
		Record termUser = Db.findFirst(sql, termInfoID);
		String guid = termUser.getStr("FGUID");
		return guid;
	}

}
