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

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.zens.ubasbossservices.dao.SubscriberPackageDao;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年3月7日上午10:35:16
 * @version 1.0 
 */
public class T_SubscriberPackage extends Model<T_SubscriberPackage> implements SubscriberPackageDao {
		
	private static final long serialVersionUID = 1L;
	public static T_SubscriberPackage dao = new T_SubscriberPackage();
	

	public static final String tableName = TableMapping.me().getTable(T_SubscriberPackage.class).getName();

	String subscriberPackageSelectSQL = "SELECT ID AS id,FGUID AS GUID,FType AS type,FCode AS code,FName AS name,FPrice AS price,FCreateTime AS createTime,FCreateUserID AS createUserID,FUpdateTime AS updateTime,FUpdateUserID AS updateUserID,FFreezed AS freezed,FDeleted AS deleted,FExt AS ext FROM " + tableName;
	
	/* 根据GUID查询，有没有某条业务包记录。
	 * @see com.zens.ubasbossservices.dao.SubscriberPackageDao#queryByGUID(java.lang.String)
	 */
	@Override
	public boolean queryByGUID(String GUID) throws Exception {
		String sql = "SELECT * FROM " + tableName + " WHERE FGUID = ? AND FDeleted = '0' AND FFreezed = '0' ";
		Record record = Db.findFirst(sql, GUID);
	
		if (record != null) {
			return true;
		}
		return false;
	}

}
