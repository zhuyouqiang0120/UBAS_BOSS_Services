package com.zens.ubasbossservices.controller;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chasonx.tools.StringUtils;
import com.chasonx.tools.TokenUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.plugin.redis.RedisPlugin;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.entity.T_App_User;
import com.zens.ubasbossservices.service.AppService;
import com.zens.ubasbossservices.serviceimpl.AppServicelmpl;
import com.zens.ubasbossservices.utils.AppUTil;

import redis.clients.jedis.Jedis;

public class UBAS_APPControllers extends Controller {
	AppService appService = new AppServicelmpl();

	/**
	 * 注册APP用户（验证码验证）
	 * 
	 * @author ZKill
	 * @create 2017年12月1日 10:16:00
	 * @update
	 * @return void
	 */
	public void APPcappuser() {

		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.cappuser(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * APP用户（向用户手机发送验证码）
	 * 
	 * @author ZKill	
	 * @create 2017年12月1日 10:16:00
	 * @update
	 * @return void
	 */
	public void APPcappuserVerificationcode() {

		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.cappuserVerificationcode(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * APP用户编辑
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 14:04:14
	 * @update
	 * @return void
	 */
	public void APPeditAppuser() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();

		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			data = appService.editAppuser(request);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(request.getParameter("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * APP用户找回密码
	 * 
	 * @author ZKill
	 * @create 2017年12月6日 10:31:31
	 * @update
	 * @return void
	 */

	public void APPRetrievePassword() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.RetrievePassword(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * APP用户登录接口
	 * 
	 * @author ZKill
	 * @create 2017年12月6日 14:24:50
	 * @update
	 * @return void
	 * @throws IOException 
	 */

	public void APPSignin() throws IOException {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		String timestamp = getPara("timestamp");
		HttpServletRequest request = getRequest();

		try {
			data = appService.Signin(param, request, timestamp);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());
		// 连接redis数据库

		if (data.get("resultstate").equals("SUCCESS")) {
			//RedisPlugin jedis = ApiUtils.GetJedis();
			Properties prop =ApiUtils.getproperties(UBAS_APPControllers.class, "jdbc.properties");
			Cache redis= Redis.use(prop.getProperty("redis.name"));
			System.out.println(redis);
			Jedis jedis=redis.getJedis();
			String token = AppUTil.md5Hex(TokenUtil.getToken() + System.currentTimeMillis());
			jedis.set(token, data.get("userguid").toString());
			jedis.expire(token, 1000 * 60 * 60 * 24);
			// 说明登录通过
			setCookie("LOGINGCOOKIE", data.get("SESSIONID").toString(), 24 * 60 * 60 * 1000);
			setCookie("ACCOUNT", data.getStr("Account"), 24 * 60 * 60 * 1000);
			data.set("token", token);
			//记录登录时间和登录ip
			

		}

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

}
