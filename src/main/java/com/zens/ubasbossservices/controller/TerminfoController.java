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

import com.zens.ubasbossservices.service.TerminfomplService;

import com.zens.ubasbossservices.serviceimpl.Terminfompl;

public class TerminfoController extends Controller {

	TerminfomplService Terminfo = new Terminfompl();
	/**
	 * 分页获取设备信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日13:34:34
	 * @update
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Clear
	@Before(ParamInterceptor.class)
	public void get() {

		ResultDesc rd = new ResultDesc();
		boolean result = false;
		Record data = new Record();
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");

		try {

			data = Terminfo.getterminfo(param);
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
	 * 新增设备信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日13:34:34
	 * @update
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Clear
	@Before(ParamInterceptor.class)
	public void add() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");
		try {

			result = Terminfo.addterminfo(param);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
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
	 * 批量更新设备设备信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日13:34:34
	 * @update
	 * @return void
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Clear
	@Before(ParamInterceptor.class)
	public void update() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");
		try {

			result = Terminfo.updateminfo(param);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
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
	 * 根据区域ID查询设备
	 * 
	 * @author ZKill
	 * @create 2017年6月7日14:33:46
	 * @update
	 * @return record
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数") })
	@Clear
	@Before(ParamInterceptor.class)
	public void getRegionterminfo() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		List<Record> data = new ArrayList<>();
		String param = getPara("param");
		String jsonpFunc = getPara("jsonpFunc");
		try {

			data = Terminfo.getRegionterminfo(param);
			rd.setCode(200);
			rd.setMessage("ok");
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setListdata(data);
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

}
