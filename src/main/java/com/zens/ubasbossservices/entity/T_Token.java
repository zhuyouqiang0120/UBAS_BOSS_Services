/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.entity								 
 **    Type    Name : T_Token 							     	
 **    Create  Time : 2017年4月6日 上午11:45:13								
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
 * @create  2017年4月6日上午11:45:13
 * @version 1.0 
 */
public class T_Token extends Model<T_Token> {

	private static final long serialVersionUID = 1L;

	public static T_Token t_Token = new T_Token();
	
	String tableName = TableMapping.me().getTable(T_Token.class).getName();

}
