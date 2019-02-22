package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

public class T_App_User extends Model<T_App_User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final  T_App_User DAO = new  T_App_User();
	public static final String tableName = TableMapping.me().getTable(T_App_User.class).getName();
}
