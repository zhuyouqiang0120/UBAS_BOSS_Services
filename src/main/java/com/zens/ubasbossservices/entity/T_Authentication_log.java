package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.TableMapping;

import cn.dreampie.web.model.Model;

public class T_Authentication_log  extends Model<T_Authentication_log>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static T_Authentication_log dao = new T_Authentication_log();
	public static final String tableName = TableMapping.me().getTable(T_Authentication_log.class).getName();

}
