package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

public class t_Appuser_Resource extends Model<t_Appuser_Resource>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final  t_Appuser_Resource DAO = new  t_Appuser_Resource();
	public static final String tableName = TableMapping.me().getTable(t_Appuser_Resource.class).getName();
}
