package com.zens.ubasbossservices.controller;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.SystemException;

import com.chasonx.tools.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zens.activiti.hessian.ActivitiHessian;
import com.zens.ubasbossservices.annotation.ApiDesc;
import com.zens.ubasbossservices.annotation.ParamInterceptor;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.HessianFactory;
import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.entity.T_SubscriberPackage;
import com.zens.ubasbossservices.interceptor.MyExceptionInterceptor;
import com.zens.ubasbossservices.interceptor.ThreadInterceptor;

/**
 * UBAS_Activiti工作流模块
 * 
 * @author zhangkai
 * @e-mail zhangkai@zensvision.com
 * @date 2018年3月7日 09:52:00
 */

public class ActivitiController extends Controller {
	/**
	 * 
	 * ActivitiHessian activitiHessian =
	 * HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);
	 * activitiHessian.definition("");
	 */
	/**
	 * 部署启动流程
	 * 
	 * @author ZKill
	 * @create 2018年3月7日 09:53:59
	 * @update
	 * @return void
	 */
	public void index() {
		System.out.println("1");
	}

	@ApiDesc("部署启动流程")
	public void definition() throws Exception {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String ModelId = getPara("ModelId");
		String business = getPara("business");
		try {
			ActivitiHessian activitiHessian = HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);
			System.out.println(activitiHessian);
			result = activitiHessian.definition(ModelId, business);

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

	@ApiDesc("删除流程模型")
	public void deleteModel() throws Exception {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String ModelId = getPara("ModelId");
		try {
			ActivitiHessian activitiHessian = HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);
			System.out.println(activitiHessian);
			result = activitiHessian.deleteModel(ModelId);

			// rd.setData(data);
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

	@ApiDesc("删除流程实例")
	public void deleteProcessDefinitionKey() throws Exception {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String DefinitionKey = getPara("DefinitionKey");
		try {
			ActivitiHessian activitiHessian = HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);
			System.out.println(activitiHessian);
			result = activitiHessian.deleteProcessDefinitionKey(DefinitionKey);

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
	 * @throws Exception
	 */
	@ApiDesc("获取审批人需要审批的任务列表")
	public void tasklist() throws Exception {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String pageNumber = getPara("pageNumber");
		String pageSize = getPara("pageSize");
		String assignee = getPara("assignee");

		try {
			ActivitiHessian activitiHessian = HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);
			data = activitiHessian.tasklist(assignee, Integer.parseInt(pageNumber),
					Integer.parseInt(pageSize));
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

		// System.out.println(getAttr("_methodName").toString());
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else {

			renderJson(rd);
		}

	}

	// completetask
	@ApiDesc("完成任务节点,更新任务状态")
	public void completetask() throws Exception {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String approval = getPara("approval");
		String taskid = getPara("taskId");
		String processInstanceId = getPara("processInstanceId");
		String taskreason=getPara("taskreason");
		try {
			ActivitiHessian activitiHessian = HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);
			System.out.println(activitiHessian);
			data = activitiHessian.completetask(taskid, approval, processInstanceId,taskreason);
			if (data.getStr("resultcode").equals("SUCCESS")) {
				// 说明流程审核通过
				Db.update("UPDATE " + T_SubscriberPackage.tableName + " SET  FFreezed=0 WHERE FGUID='"
						+ data.getStr("sub") + "'");

			}
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

	@ApiDesc("根据系统的标识,查询对应的流程模型列表")
	public void selectModel() throws Exception {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String System = getPara("System");
		String pageNumber = getPara("pageNumber");
		String pageSize = getPara("pageSize");
		try {
			ActivitiHessian activitiHessian = HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);

			List<Record> records = activitiHessian.selectModel(System, Integer.parseInt(pageNumber),
					Integer.parseInt(pageSize));

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

	@ApiDesc("根据系统的标识,查询对应的流程模型列表")
	public void selectModelId(){
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String key = getPara("key");
		String Category = getPara("Category");
		String tenantId = getPara("secondKey");
		try {
			ActivitiHessian activitiHessian = HessianFactory.create(ActivitiHessian.class, ApiUtils.ACTIVITI);

			String data1 = activitiHessian.selectModelid(key, Category, tenantId);
			System.out.println(data1);

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

	public void create() throws Exception {
		try {

			String api = "https://api.weixin.qq.com/cgi-bin" + "/message/template/send?access_token=7_Fl4u89"
					+ "yns7GyYVVkpIogz8EhYyLNIqSurCXJzjpf9s4YMk4V_1-3xZ0SdV"
					+ "fLh88sFFfK8UzW1t2DXXxcQuAjDtErjHT3RDZX2kZ1195SWJqhV-5ZOPfeIuLWsx9iDG9CtnsGtlD0w_DJ-UmgMDBdABATKI";
			String param = "";
			param = api;

			/** 发送httppost请求 */

			HttpPost request = new HttpPost(param);
			String result = "";
			try {
				HttpResponse response = HttpClients.createDefault().execute(request);
				if (response.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(response.getEntity());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
