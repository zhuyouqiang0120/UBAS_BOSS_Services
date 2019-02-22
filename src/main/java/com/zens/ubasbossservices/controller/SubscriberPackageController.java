/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.controller								 
 **    Type    Name : SubscriberPackageController 							     	
 **    Create  Time : 2017年3月6日 下午4:51:55								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.controller;

import java.util.ArrayList;
import java.util.List;

import com.chasonx.tools.StringUtils;
import com.jfinal.aop.Before;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.zens.ubasbossservices.annotation.ParaEntity;
import com.zens.ubasbossservices.annotation.ParamInterceptor;
import com.zens.ubasbossservices.annotation.Required;
import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.service.SubscriberPackageService;
import com.zens.ubasbossservices.serviceimpl.SubscriberPackageServiceImpl;

/**
 * 业务包管理
 * 
 * @author Floristy
 * @email yangsen@zensvision.com
 * @create 2017年3月6日下午4:51:55
 * @version 1.0
 */
public class SubscriberPackageController extends Controller {
	SubscriberPackageService subscriberPackageService = new SubscriberPackageServiceImpl();

	/**
	 * 添加一个业务包
	 * 
	 * @author floristy
	 * @create 2017年3月7日 上午10:53:57
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void add() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record record=new Record();
		String param = getAttr("param");
		try {
			record= subscriberPackageService.add(param);
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
	 * 批量删除业务包
	 * 
	 * @author floristy
	 * @create 2017年3月8日 上午9:11:31
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void delete() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");
		try {
			res = subscriberPackageService.delete(param);
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
	 * 批量恢复删除业务包
	 * 
	 * @author floristy
	 * @create 2017年3月8日 上午9:11:45
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void unDelete() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");
		try {
			res = subscriberPackageService.undelete(param);
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
	 * 批量更新业务包
	 * 
	 * @author floristy
	 * @create 2017年3月8日 上午9:12:03
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void set() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");
		System.out.println(param);
		try {
			res = subscriberPackageService.update(param);
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
	 * 查询某注册用户已定购的业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:52:24
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void getByTermUser() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");
		Record data = new Record();
		try {
			Record subscriberPackages = subscriberPackageService.getByTermUser(param);
			data.set("termuser_subscriberPackages", subscriberPackages);
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
	 * 查询业务包
	 * 
	 * @author ZKill
	 * @create 2017年3月13日 下午3:47:37
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	public void get() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getAttr("param");
		Record data = new Record();
		try {
			data = subscriberPackageService.get(param);
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
	 * 分页查询业务包计费策略
	 * 
	 * @author ZKill
	 * @create 2017年3月13日 下午3:47:37
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void getstategy() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");
		Record data = new Record();
		try {
			data = subscriberPackageService.getstategy(param);
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
	 * 绑定业务包计费策略
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 09:37:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void setsubstategy() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");

		try {
			res = subscriberPackageService.setsubstategy(param);

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
	 * 计费策略绑定业务包
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 09:37:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void setstategysub() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");

		try {
			res = subscriberPackageService.setstategysub(param);

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
	 * 解除业务包和计费策略的绑定（一对多）
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 09:37:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void deletesubstategy() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		List<Record> list=new ArrayList<>();
		String param = getPara("param");
		String Grouptype=getPara("Grouptype");

		try {
			list = subscriberPackageService.deletesubstategy(param,Grouptype);
			rd.setListdata(list);
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
	 * 解除计费策略和业务包的绑定（一对一）
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 09:37:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void deletestategysub() {

		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");

		try {
			res = subscriberPackageService.deletestategysub(param);

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
	 * 查看指定业务包下面有哪些计费策略
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 09:37:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void selectSubstrategy() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");
		Record data = new Record();
		try {
			data = subscriberPackageService.selectSubstrategy(param);
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
	 * 查看指定业务包下未开通计费策略
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 09:37:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void selectunSubstrategy() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");
		Record data = new Record();
		try {
			data = subscriberPackageService.selectunSubstrategy(param);
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
	 * 新增计费策略
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 16:13:27
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void addstrategy() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		Record record=new Record();
		String param = getPara("param");

		try {
			record =subscriberPackageService.addstrategy(param);
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
	 * 计费策略的编辑
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 16:13:27
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void updatestrategy() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");

		try {
			res = subscriberPackageService.updatestrategy(param);

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
	 * 查看计费策略绑定的对应的业务包的信息
	 * 
	 * @author ZKill
	 * @create 2017年7月14日 15:39:33
	 * @update
	 * @param
	 * @return void
	 *
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void strategysub() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");
		Record data = new Record();
		try {
			data = subscriberPackageService.strategysub(param);
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
	 * 分页获取所有的业务包和策略包
	 * 
	 * @author ZKill
	 * @create 2017年7月25日 11:51:13
	 * @update
	 * @param
	 * @return void
	 *
	 */
	
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Before(ParamInterceptor.class)
	@Clear
	public void getsubstr() {
		ResultDesc rd = new ResultDesc();
		boolean res = true;
		String param = getPara("param");
		Record data = new Record();
		try {
			data = subscriberPackageService.getsubstr(param);
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
}