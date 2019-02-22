/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasboss.controller								 
 **    Type    Name : TermUserController 							     	
 **    Create  Time : 2017年2月6日 下午16:59:00								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chasonx.tools.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zens.ubasbossservices.annotation.ParaEntity;
import com.zens.ubasbossservices.annotation.ParamInterceptor;
import com.zens.ubasbossservices.annotation.Required;
import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.common.ResultUser;
import com.zens.ubasbossservices.interceptor.UserParamInterception;
import com.zens.ubasbossservices.service.T_TermUserService;
import com.zens.ubasbossservices.serviceimpl.T_TermUserServiceImpl;
import com.zens.ubasbossservices.utils.BossUtil;

import android.R.bool;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import net.sf.json.JSONObject;
import sun.tools.tree.ThisExpression;

/**
 * http://localhost:8080/UBAS_BOSS_Services/login/index?param={"username":"admin","pwd":"123456"}
 * 
 * @author zyq
 *
 */
public class LoginController extends Controller {

	public void index() { // 用户数据导入
		String param = getAttr("param");  

		System.out.println(param);
		JSONObject paramData = JSONObject.fromObject(param);

		String username = paramData.getString("username");
		String pwd = paramData.getString("pwd");

		System.out.println(username);
		System.out.println(pwd);

		String sql = "SELECT * FROM t_ubasuser where username = '" + username + "' AND pwd =  '" + pwd + "'";

		List<Record> records = Db.find(sql);

		Record record = new Record();

		if (records.size() == 0) {
			record.set("result", false);
			record.set("userName", "");
			record.set("tName", "");
			record.set("createtime", "");
			record.set("updatetime", "");
		} else {
			record.set("result", true);
			record.set("tName", records.get(0).get("tname"));
			record.set("userName", records.get(0).get("username"));
			record.set("createtime", records.get(0).get("createtime"));
			record.set("updatetime", records.get(0).get("updatetime"));
		}

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(record) + ");");
		else
			renderJson(record);
	}

	public void all() { //
		String param = getAttr("param");
		String sql = "SELECT * FROM t_ubasuser ";
		if(param.length()!=0) {
			sql = "SELECT * FROM t_ubasuser where username = '"+param+"'";
		}

		List<Record> records = Db.find(sql);

		Record record = new Record();

		if (records.size() == 0) {
			if (StringUtils.hasText(getPara("jsonpFunc")))
				renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(record) + ");");
			else
				renderJson(record);
		} else {
			if (StringUtils.hasText(getPara("jsonpFunc")))
				renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(records) + ");");
			else
				renderJson(records);
		}

	}

	/**
	 * http://localhost:8080/UBAS_BOSS_Services/login/add?param={"tname":"管理员","username":"admin","pwd":"123456"}
	 */
	public void add() {
		String param = getAttr("param");
		JSONObject paramData = JSONObject.fromObject(param);
		String tname = paramData.getString("tname");
		String username = paramData.getString("username");
		String pwd = paramData.getString("pwd");

		String sql = "SELECT * FROM t_ubasuser where username = '" + username + "'";
		List<Record> records = Db.find(sql);
		Record record = new Record();
		if (records.size() == 0) {
			String sqladd = "INSERT INTO t_ubasuser (tname, username, pwd, createtime) VALUES ('" + tname + "','"
					+ username + "','" + pwd + "','" + formatTimes(new Date()) + "')";

			Db.update(sqladd);

			record.set("result", true);
			record.set("tName", tname);
			record.set("userName", username);
			record.set("createtime", formatTimes(new Date()));
			record.set("updatetime", "");
		} else {
			record.set("result", false);
			record.set("msg", "user is exist !");
		}

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(record) + ");");
		else
			renderJson(record);
	}

	/**
	 * http://localhost:8080/UBAS_BOSS_Services/login/update?param={"tname":"管理员","username":"admin","pwd":"admin"}
	 */
	public void update() {
		String param = getAttr("param");
		JSONObject paramData = JSONObject.fromObject(param);
		String tname = paramData.getString("tname");
		String username = paramData.getString("username");
		String pwd = paramData.getString("pwd");

		String sqlup = "UPDATE t_ubasuser SET tname = '" + tname + "', pwd = '" + pwd + "', updatetime = '"
				+ formatTimes(new Date()) + "' WHERE username = '" + username + "'";

		Db.update(sqlup);

		String sql = "SELECT * FROM t_ubasuser where username = '" + username + "'";
		List<Record> records = Db.find(sql);
		System.out.println(records.size());
		Record record = new Record();

		record.set("result", true);
		record.set("tName", records.get(0).get("tname"));
		record.set("userName", username);
		record.set("createtime", records.get(0).get("createtime"));
		record.set("updatetime", records.get(0).get("updatetime"));

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(record) + ");");
		else
			renderJson(record);
	}

	public void delete() {
		String param = getAttr("param");
		String delP = param.replace("[", "").replaceAll("]", "");
		System.out.println(delP);
		String sqldel = "DELETE FROM t_ubasuser WHERE  username IN ("+ delP +")";
		
		Db.update(sqldel);
		
		Record record = new Record();
		record.set("result", true);
		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(record) + ");");
		else
			renderJson(record);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public String formatTimes(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr;
	}

}