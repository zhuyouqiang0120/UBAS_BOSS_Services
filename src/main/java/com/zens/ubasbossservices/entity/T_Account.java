package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

public class T_Account extends Model<T_Account> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final T_Account DAO = new T_Account();
	public static final String tableName = TableMapping.me().getTable(T_Account.class).getName();

}
