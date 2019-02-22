/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasboss.httpapi								 
 **    Type    Name : GroupServiceController 							     	
 **    Create  Time : 2017年2月10日 上午9:00:01								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.httpapi;

import java.util.List;

import com.chasonx.tools.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.zens.ubasbossservices.annotation.ApiDesc;
import com.zens.ubasbossservices.annotation.ParaEntity;
import com.zens.ubasbossservices.annotation.ParamInterceptor;
import com.zens.ubasbossservices.annotation.Required;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.Constant;
import com.zens.ubasbossservices.common.HessianFactory;
import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.interceptor.NestDataSetInterceptor;
import com.zens.ubasbossservices.service.NestedDataSetService;

import net.sf.json.JSONObject;

/**
<<<<<<< HEAD
 * @author Johnson
 * @email yangsen@zensvision.com
 * @create 2017年2月10日上午9:00:01
 * @version 1.0
 */
public class GroupServiceController extends Controller {

	String type = "2"; // 规定
						// UBAS_BOSS_Services的区域管理对应的type为1，UBAS_BOSS_Services的分组管理对应的type为2。
	String clientGuid = Constant.UBASBOSS_ClientGuid;

	/**
	 * 添加一个分组信息
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:15:29
	 * @update
	 * @param type
	 *            value envalue parentGuid level icon remark state extdata
	 *            clientGuid
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@ApiDesc("添加一个分组信息")
	@Before({ NestDataSetInterceptor.class, ParamInterceptor.class})
	public void add() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String __param = getAttr("__param");
		String jsonpFunc = getPara("jsonpFunc");
		String apiUrl = getAttr("APIURL");

		JSONObject jO = JSONObject.fromObject(__param);
		jO.put("type", type);
		jO.put("state", "0");
		jO.put("cid", clientGuid);
		try {
			NestedDataSetService netDataSetService = HessianFactory.create(NestedDataSetService.class,
					apiUrl + ApiUtils.NESTEDDATASET);
			result = netDataSetService.saveNestedDataSet(jO.toString());
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
	 * 删除分组及其后代分组（支持批量）
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:16:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@Before({ NestDataSetInterceptor.class, ParamInterceptor.class })
	public void delete() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String __param = getAttr("__param");
		String jsonpFunc = getPara("jsonpFunc");
		String apiUrl = getAttr("APIURL");
		try {
			NestedDataSetService netDataSetService = HessianFactory.create(NestedDataSetService.class,
					apiUrl + ApiUtils.NESTEDDATASET);
			result = netDataSetService.delByGUIDs(__param);
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
	 * 恢复删除分组及其后代分组（支持批量）
	 * 
	 * @author floristy
	 * @create 2017年3月22日 上午10:18:42
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@Before({ NestDataSetInterceptor.class, ParamInterceptor.class })
	public void undelete() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String __param = getAttr("__param");
		String jsonpFunc = getPara("jsonpFunc");
		String apiUrl = getAttr("APIURL");
		try {
			NestedDataSetService netDataSetService = HessianFactory.create(NestedDataSetService.class,
					apiUrl + ApiUtils.NESTEDDATASET);
			result = netDataSetService.unDelByGUIDs(__param);
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
	 * 批量更新分组信息
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:16:12
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@ApiDesc("批量更新分组信息")
	@Before({ NestDataSetInterceptor.class, ParamInterceptor.class })
	public void set() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String __param = getAttr("__param");
		String jsonpFunc = getPara("jsonpFunc");
		String apiUrl = getAttr("APIURL");

		try {
			NestedDataSetService netDataSetService = HessianFactory.create(NestedDataSetService.class,
					apiUrl + ApiUtils.NESTEDDATASET);
			result = netDataSetService.batchUpdate(__param);
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
	 * 移动排列
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:16:58
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@ApiDesc("移动排列")
	@Before({ NestDataSetInterceptor.class, ParamInterceptor.class })
	public void move() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String __param = getAttr("__param");
		String jsonpFunc = getPara("jsonpFunc");
		String apiUrl = getAttr("APIURL");
		try {
			NestedDataSetService netDataSetService = HessianFactory.create(NestedDataSetService.class,
					apiUrl + ApiUtils.NESTEDDATASET);
			netDataSetService.move(__param);
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
	 * 分页获取嵌套分组
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:16:55
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@ApiDesc("分页获取嵌套分组")
	@Before({ NestDataSetInterceptor.class, ParamInterceptor.class })
	public void get() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String __param = getAttr("__param");
		String jsonpFunc = getPara("jsonpFunc");
		String apiUrl = getAttr("APIURL");
		List<Record> data;
		Record record = new Record();

		JSONObject jO = JSONObject.fromObject(__param);
		jO.put("type", type);
		jO.put("cid", clientGuid);
		try {
			NestedDataSetService nestedDataSetService = HessianFactory.create(NestedDataSetService.class,
					apiUrl + ApiUtils.NESTEDDATASET);
			data = nestedDataSetService.getAllNestedDataSet(jO.toString());
			record.set("groups", data);
			rd.setCode(200);
			rd.setMessage("ok");
			rd.setData(record);
			result = true;
		} catch (Exception e) {
			rd.setCode(500);
			rd.setMessage(e.getMessage());
			e.printStackTrace();
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());
		if (StringUtils.hasText(jsonpFunc))
			renderJavascript(jsonpFunc + "(" + JsonKit.toJson(rd) + ");");
	}
	}
