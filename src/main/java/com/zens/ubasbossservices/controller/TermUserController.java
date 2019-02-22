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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chasonx.tools.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;

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

/**
 * 终端用户
 * 
 * @author zyq
 * @e-mail zhuyq@zensvision.com
 * @date 2016年10月31日 下午3:34:11
 */
public class TermUserController extends Controller {

	T_TermUserService t_TermUserService = new T_TermUserServiceImpl();

	String defRegionUrl = "";

	/*
	 * 分页获取终端用户
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数"), })
	@Clear
	@Before(ParamInterceptor.class)
	public void get() { // 分页获取所用户信息
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = t_TermUserService.get(param, result);
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
	 * 根据区域获取终端用户
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数"), })
	@Before(ParamInterceptor.class)
	public void getByRegion() {// 根据区域码获取所有用户信息
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record data = new Record();

		String param = getAttr("param");
		try {
			data = t_TermUserService.get(param, true);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 添加终端用户
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void add() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record record = new Record();
		String param = getAttr("param");
		try {
			record = t_TermUserService.add(param);
			rd.setData(record);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 查询用户账户信息
	 * 
	 * @author Zkill
	 * @create 2017年7月17日 13:33:24
	 * @update
	 * @param
	 * @return Record
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void selectaccount() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record record = new Record();
		String param = getPara("param");
		try {
			record = t_TermUserService.selectaccount(param);
			rd.setData(record);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 查询用户订购产品记录信息（账单）
	 * 
	 * @author Zkill
	 * @create 2017年7月18日 15:14:56
	 * @update
	 * @param
	 * @return Record
	 */

	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void selectProductrecord() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record record = new Record();
		String param = getPara("param");
		try {
			record = t_TermUserService.selectProductrecord(param);
			rd.setData(record);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 查询用户可开通的产品（未订购，包括策略包和业务包）
	 * 
	 * @author Zkill
	 * @create 2017年7月24日 12:02:47
	 * @update
	 * @param
	 * @return Record
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void getByTermUserbindsub() {
		ResultDesc rd = new ResultDesc();
		Record record = new Record();
		boolean res = true;
		String param = getAttr("param");
		Record data = new Record();
		try {
			record = t_TermUserService.getByTermUserbidsub(param);
			data.set("termuser_subscriberPackages", record);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 用户点播鉴权接口（重要）
	 * 
	 * @author Zkill
	 * @create 2017年7月18日 15:14:56
	 * @update
	 * @param
	 * @return Record
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void VODauthentication() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record data = new Record();
		String param = getPara("param");
		try {
			data = t_TermUserService.VODauthentication(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);

	}

	/**
	 * 批量删除注册用户信息
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void del() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");

		try {
			res = t_TermUserService.delete(param);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 批量恢复删除注册用户信息
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void unDel() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");
		try {
			res = t_TermUserService.undelete(param);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 批量更新注册用户信息
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void set() {
		ResultDesc rd = new ResultDesc();
		List<Record> records = new ArrayList<>();
		boolean res = true;
		String param = getPara("param");

		try {
			records = t_TermUserService.update(param);
			rd.setListdata(records);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/************************************** 终端用户、区域关联操作 *****************************************/

	/**
	 * 为用户设置区域（一个用户可以关联多个区域）
	 * 
	 * @author Johnson
	 * @create 2017年2月13日 上午11:49:49
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void setRegions() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");

		try {
			res = t_TermUserService.setRegions(param);

			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 查询指定注册用户所属区域（支持根据任意能唯一表示注册用户的字段进行查询）
	 * 
	 * @author floristy
	 * @create 2017年3月28日 下午5:55:28
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void getRegionsByTermUser() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record data = new Record();
		List<Record> regions;
		String param = getAttr("param");
		try {
			regions = t_TermUserService.getRegionsByTermUser(param);
			data.set("regions", regions);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/************************************** 终端用户、分组关联操作 *****************************************/

	/**
	 * 为用户设置分组（一个用户只能关联一个分组）
	 * 
	 * @author Johnson
	 * @create 2017年2月13日 上午11:50:25
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void setGroup() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");

		try {
			res = t_TermUserService.setGroup(param);

			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/************************************** 终端用户、业务包关联操作 *****************************************/

	/**
	 * 为终端用户设置业务包
	 * 
	 * @author Johnson
	 * @create 2017年2月13日 上午11:50:25
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数"),
			@ParaEntity(name = "action", xlen = 9000, desc = "操作类型") })
	@Before(ParamInterceptor.class)
	public void setSubscriberPackages() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");

		List<Record> record = new ArrayList<>();
		String action = getPara("action"); // action可能的值是：bind（开通）、unbind（停断）、freeze（暂停）、unfreeze（开始）、renew（续订）。
		String operation = getPara("operation");
		try {
			if (action.equals("bind")) {
				record = t_TermUserService.bindTermUser_SubscriberPackages(param, operation);
			} else if (action.equals("unbind")) {
				record = t_TermUserService.unbindTermUser_SubscriberPackages(param);
			} else if (action.equals("suspend")) {
				res = t_TermUserService.suspendTermUser_SubscriberPackages(param);
			} else if (action.equals("continue")) {
				res = t_TermUserService.continueTermUser_SubscriberPackages(param);
			} else if (action.equals("freeze")) {
				res = t_TermUserService.freezeTermUser_SubscriberPackages(param);
			} else if (action.equals("unfreeze")) {
				res = t_TermUserService.unfreezeTermUser_SubscriberPackages(param);
			} else if (action.equals("renew")) {
				record = t_TermUserService.renewTermUser_SubscriberPackages(param);
			}
			rd.setListdata(record);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 查询某终端用户是否已订购指定的业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:42:11
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void hasSubscriberPackage() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");

		try {
			res = t_TermUserService.hasSubscriberPackage(param);

			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 查询某终端用户已经订购的产品
	 * 
	 * @author ZKill
	 * @create 2017年3月10日 上午11:42:11
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void hasSubproduct() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");
		Record data = new Record();
		try {
			data = t_TermUserService.hasSubproduct(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * UBAS终端用户认证接口
	 * 
	 * @author zhangkai
	 * @create 2017年6月19日 13:36:50
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void Userauthentication() {
		ResultDesc rd = new ResultDesc();
		Record record = new Record();
		boolean res = true;
		String param = getPara("param");

		try {
			record = t_TermUserService.userauthentication(param);
			rd.setData(record);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/************************************** 监狱管理 *****************************************/
	/**
	 * 
	 * 通过设备信息查询该设备下的人员信息或通过区域查询设备信息
	 * 
	 * @author zhangkai
	 * @create 2017年6月16日 10:10:36
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Clear
	@Before(ParamInterceptor.class)
	public void getiplpsuser() {
		ResultDesc rd = new ResultDesc();
		List<Record> record;
		boolean res = true;
		String param = getPara("param");

		try {
			record = t_TermUserService.getiplpsuserfo(param);
			rd.setListdata(record);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 
	 * 通过设备的mac地址查询对应的区域信息
	 * 
	 * @author zhangkai
	 * @create 2017年6月16日 10:10:36
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Clear
	@Before(ParamInterceptor.class)
	public void getRegion() {
		ResultDesc rd = new ResultDesc();
		Record record = new Record();
		boolean res = true;
		String param = getPara("param");

		try {
			record = t_TermUserService.getRegion(param);
			rd.setData(record);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			res = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(res);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 新增用户档案用户
	 * 
	 * @author ZKill
	 * @create 2017年5月11日13:34:34
	 * @update
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before({ UserParamInterception.class })
	public void addUserZE() {
		T_TermUserServiceImpl impl = new T_TermUserServiceImpl();
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String param = getAttr("param");
		String jsonpFunc = getPara("jsonpFunc");

		try {
			result = impl.addZE(param);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());
		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 删除用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 15:03:13
	 * @update
	 * @return void
	 */

	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })

	@Before(ParamInterceptor.class)
	public void deleteZE() {

		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");

		try {
			result = t_TermUserService.deleteZE(param);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());
		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 批量设置用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 15:16:47
	 * @update
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })

	@Before(ParamInterceptor.class)

	public void updateZE() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");

		try {

			result = t_TermUserService.ZEupdate(param);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 批量更新用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 15:16:47
	 * @update
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })

	@Before(UserParamInterception.class)

	public void ZEsetuser() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String param = getAttr("param");
		String jsonpFunc = getPara("jsonpFunc");

		try {
			result = t_TermUserService.ZEsetuser(param);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 分页获取用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 16:43:37
	 * @update
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Clear
	@Before(ParamInterceptor.class)
	public void getZE() {

		ResultDesc rd = new ResultDesc();
		boolean result = false;
		Record data = new Record();
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");

		try {

			data = t_TermUserService.getZE(param);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setData(data);
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * UBAS BOSS数据导入
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 16:43:37
	 * @update
	 * @return void
	 */
	@Clear
	public void Bossdataimport() {

		ResultDesc rd = new ResultDesc();
		boolean result = false;
		Record data = new Record();
		// String param = getPara("param");
		// String jsonpFunc = getPara("jsonpFunc");
		// System.out.println(PathKit.getWebRootPath() + "/");
		boolean flag = BossUtil.DownFile();
		if (flag) {
			System.out.println("下载文件成功");
		}
		try {

			// data = t_TermUserService.getZE(param);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setData(data);
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());
		renderText("OK");
		// if (StringUtils.hasText(jsonpFunc))
		/// renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		// else
		// renderJson(rd);
	}

	/**
	 * 根据用户唯一标识查询用户信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 16:43:37
	 * @update
	 * @return void
	 */

	@Clear
	@Before(ParamInterceptor.class)
	public void getBossUserInfo() {

		ResultDesc rd = new ResultDesc();
		boolean result = false;
		Record data = new Record();
		String guid = getPara("guid");
		String jsonpFunc = getPara("jsonpFunc");
		System.out.println(guid);
		try {

			data = t_TermUserService.getBossUserInfo(guid);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setData(data);
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);

	}

	/**
	 * 根据用户唯一标识查询用户信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 16:43:37
	 * @update
	 * @return void
	 */

	public void getBossUserSubscriberPackage() {

		ResultDesc rd = new ResultDesc();
		boolean result = false;
		Record data = new Record();
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");
		// System.out.println(guid);
		try {

			data = t_TermUserService.getBossUserSubscriberPackage(param);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setData(data);
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);

	}

	/**
	 * 用户点播认证接口
	 * 
	 * @author ZKill
	 * @create 2018年3月1日 15:08:16
	 * @update
	 * @return void
	 */
	public void demanDcertification() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		Record data = new Record();
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");
		HttpServletRequest request = getRequest();
		// System.out.println(guid);
		try {

			data = t_TermUserService.demanDcertification(param, request);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setData(data);
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * UAMS 获取缓存时间戳 测试用接口
	 * 
	 * @author ZKill
	 * @create 2018年3月5日 15:12:50
	 * @update
	 * @return void
	 */
	public void UAGetTOken() {
		ResultDesc rd = new ResultDesc();
		;

		String IP = getPara("IP");
		String data = "";
		// System.out.println(guid);
		try {

			data = t_TermUserService.UAGetTOken(IP);

		} catch (Exception e) {

			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}

		renderText(data);
	}

	/**
	 * 获取UBAS系统用户（对Activiti开发HTTP接口)
	 * 
	 * @author ZKill
	 * @create 2018年4月10日 10:21:34
	 * @update
	 * @return void
	 */
	public void getSystemtermuser() {
		ResultUser rd = new ResultUser();
		boolean result = false;
		HttpServletResponse response = getResponse();
		response.setHeader("Access-Control-Allow-Origin", "*");
		String jsonpFunc = getPara("jsonpFunc");
		List<Record> datalist = null;
		// System.out.println(guid);
		try {

			datalist = t_TermUserService.getSystemtermuser();
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();

			rd.setMsg(e.getMessage());
		}

		// rd.setData(data);
		rd.setResult(result);

		rd.setData(datalist);
		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

}