package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

public class T_System_termuser extends Model<T_System_termuser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final T_System_termuser DAO = new T_System_termuser();
	public static final String tableName = TableMapping.me().getTable(T_System_termuser.class).getName();

}
