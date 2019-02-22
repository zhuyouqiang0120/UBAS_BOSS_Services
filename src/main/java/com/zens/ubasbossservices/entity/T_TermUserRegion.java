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

import java.util.List;

import com.chasonx.tools.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.zens.ubasbossservices.dao.TermUserRegionDao;

/**
 * 
 * @author zyq
 * @e-mail zhuyq@zensvision.com
 * @date 2016年10月20日 上午12:09:15
 */
public class T_TermUserRegion extends Model<T_TermUserRegion> implements TermUserRegionDao {

	private static final long serialVersionUID = 1L;

	public static T_TermUserRegion t_Term_Region = new T_TermUserRegion();
	
	String tableName = TableMapping.me().getTable(T_TermUserRegion.class).getName();

	/* 根据终端用户GUID获取指点字段的记录，如果不指定字段，默认返回所有字段。
	 * @see com.zens.ubasbossservices.dao.TermUserRegionDao#getBySubsGUID(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Record> getBySubsGUID(String trueField, String subsGUID) throws Exception {
		if (!StringUtils.hasText(trueField)) {
			trueField = "*";
		}
		String sql = "SELECT " + trueField + " FROM " + tableName + " WHERE FSubsGUID = ?";
		return Db.find(sql,subsGUID);
	}
}
