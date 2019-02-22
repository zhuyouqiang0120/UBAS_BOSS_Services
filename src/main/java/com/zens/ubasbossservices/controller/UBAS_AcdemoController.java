package com.zens.ubasbossservices.controller;

import java.util.Optional;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import com.zens.ubasbossservices.annotation.ApiDesc;

import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.entity.T_System_termuser;


public class UBAS_AcdemoController extends Controller {
	/**
	 * Activitdemo用接口
	 * 
	 * @author ZKill
	 * @create 2018年3月7日 09:53:59
	 * @update
	 * @return void
	 */
	@ApiDesc("系統人員登錄")
	public void SLogings() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String Account = getPara("Account");
		String password = getPara("password");
		data = Db.findFirst("SELECT * FROM " + T_System_termuser.tableName + " WHERE F_Username='" + Account
				+ "' AND F_Password='" + password + "'");
		if (Optional.ofNullable(data).hashCode() == 0) {
			result = false;
			rd.setResult(result);
			rd.setCode(201);

		} else {
			result = true;
			rd.setResult(result);
			rd.setCode(200);
			rd.setData(data);
		}
		renderJson(JsonKit.toJson(rd));
	}
}
