package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

public class T_Scavenging extends Model<T_Scavenging>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final  T_Scavenging DAO = new  T_Scavenging();
	public static final String tableName = TableMapping.me().getTable(T_Scavenging.class).getName();
}
