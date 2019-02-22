package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

public class T_termuser_appuser extends Model<T_termuser_appuser>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final   T_termuser_appuser DAO = new   T_termuser_appuser();
	public static final String tableName = TableMapping.me().getTable( T_termuser_appuser.class).getName();
}
