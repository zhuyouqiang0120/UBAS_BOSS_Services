package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.TableMapping;

import cn.dreampie.web.model.Model;

public class T_Order_log  extends Model<T_Order_log>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static T_Order_log dao = new T_Order_log();
	public static final String tableName = TableMapping.me().getTable(T_Order_log.class).getName();

}
