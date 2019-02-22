/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS-SERVICE
 **    Package Name : com.zens.ubasservice.entity								 
 **    Type    Name : TUser 							     	
 **    Create  Time : 2016年9月19日 下午2:19:54								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.zens.ubasbossservices.dao.TermUserDao;

/**
 * 
 * @author zyq
 * @e-mail zhuyq@zensvision.com
 * @date 2016年10月20日 上午12:29:47
 */
public class T_TermUser extends Model<T_TermUser> implements TermUserDao {

	private static final long serialVersionUID = 1L;

	public static T_TermUser t_TermUser = new T_TermUser();
	
	public static String tableName = TableMapping.me().getTable(T_TermUser.class).getName();
	
	/* 根据uniqueKey找到该终端用户的GUID。
	 * @see com.zens.ubasbossservices.dao.TermUserDao#getTermUserByUniqueKey(java.lang.String)
	 */
	@Override
	public String getGUID(String trueField, String uniqueKey) throws Exception {
		String sql = "SELECT FGUID FROM " + tableName + " WHERE " + trueField + " = ?" ;
		Record termUser = Db.findFirst(sql, uniqueKey);
		String guid = termUser.getStr("FGUID");
		return guid;
	}
}
