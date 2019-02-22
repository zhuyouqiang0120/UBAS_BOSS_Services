/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.entity								 
 **    Type    Name : T_SysUser 							     	
 **    Create  Time : 2017年4月6日 上午11:42:33								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年4月6日上午11:42:33
 * @version 1.0 
 */
public class T_SysUser extends Model<T_SysUser> {
	
	private static final long serialVersionUID = 1L;

	public static T_SysUser dao = new T_SysUser();
	
	String tableName = TableMapping.me().getTable(T_SysUser.class).getName();

}
