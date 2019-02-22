/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS-BOSS
 **    Package Name : com.zens.ubasboss.httpapi								 
 **    Type    Name : RegionServiceController 							     	
 **    Create  Time : 2016年12月25日 下午4:38:49								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
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
import com.jfinal.template.expr.ast.Index;
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
 * @author Jhonson
 * @email yangsen@zensvision.com
 * @create 2016年12月25日下午4:38:49
 * @version 1.0
 */
public class RegionServiceController extends Controller {

	String type = "1"; // 规定
						// UBAS_BOSS_Services的区域管理对应的type为1，UBAS_BOSS_Services的分组管理对应的type为2。
	String clientGuid = Constant.UBASBOSS_ClientGuid;

	/**
	 * 添加一个区域信息
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
	@ApiDesc("添加一个区域信息")
	@Before({NestDataSetInterceptor.class, ParamInterceptor.class})
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
	 * 删除区域及子区域
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:16:53
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@ApiDesc("删除区域及子区域")
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
	public void Index(){
		renderText("333");
	}
	/**
	 * 恢复删除区域及其后代区域（支持批量）
	 * 
	 * @author floristy
	 * @create 2017年3月15日 上午10:00:43
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
	 * 更区域信息
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:16:12
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@ApiDesc("更新区域信息")
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
			result=netDataSetService.move(__param);
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
	 * 分页获取嵌套区域
	 * 
	 * @author jhonson
	 * @create 2016年12月22日 上午11:16:55
	 * @update
	 * @param
	 * @return void
	 */
	@Required({ @ParaEntity(name = "__param", xlen = 9000, desc = "总参数") })
	@ApiDesc("分页获取嵌套区域")
	@Before({ NestDataSetInterceptor.class, ParamInterceptor.class })
	public void get() {
		ResultDesc rd = new ResultDesc();
		boolean result = false;
		String __param = getAttr("__param");
		String jsonpFunc = getPara("jsonpFunc");
		String apiUrl = getAttr("APIURL"); 
		List<Record> data;
		Record record = new Record();
		System.out.println(apiUrl + ApiUtils.NESTEDDATASET);
		JSONObject jO = JSONObject.fromObject(__param);
		jO.put("type", type);
		jO.put("cid", clientGuid);
		try {
			NestedDataSetService nestedDataSetService = HessianFactory.create(NestedDataSetService.class,
					apiUrl + ApiUtils.NESTEDDATASET);
			System.out.println("调用接口url:" + apiUrl + ApiUtils.NESTEDDATASET);
			System.out.println("---------------:" + "开始调用");
			data = nestedDataSetService.getAllNestedDataSet(jO.toString());
			System.out.println("调用完毕返回数据条数：" + data.size());
			record.set("regions", data);
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
