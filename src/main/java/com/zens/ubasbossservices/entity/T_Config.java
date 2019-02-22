/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.entity								 
 **    Type    Name : TConfig 							     	
 **    Create  Time : 2016年9月19日 下午5:16:52								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * @author  Chasonx
 * @email   xzc@zensvision.com
 * @create  2016年9月19日下午5:16:52
 * @version 1.0 
 */
public class T_Config extends Model<T_Config> { //这个类在/UBAS/src/main/java/com/zens/ubas/config/Dconfig.java中被调用了。

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static T_Config config = new T_Config();
	public static final String tableName = TableMapping.me().getTable(T_Config.class).getName();
}
