/**********************************************************************
 **********************************************************************
 **    Project Name : UDimensionService
 **    Package Name : com.zens.udimension.annotation								 
 **    Type    Name : ParamInterceptor 							     	
 **    Create  Time : 2016年10月11日 上午11:42:32								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.annotation;

import java.lang.annotation.Annotation;

import com.chasonx.tools.StringUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.zens.ubasbossservices.common.Tools;

/**
 * @author  Chason.x <zuocheng911@163.com>
 * @createTime 2016年7月20日 下午2:41:24
 * @remark 
 */
public class ParamInterceptor implements Interceptor {
	
	/**
	 * 验证表单数据
	 * 验证返回错误格式为JSON {"message" : "this name is null","targetname" : "name"}
	 */
	@Override
	public void intercept(Invocation inv) {

		boolean require = false;
		String mess = "";
		String fieldName = "";
		Controller con = inv.getController();
		String actionName = inv.getActionKey();
		String methodName = inv.getMethodName();
		try{
			Required req = inv.getMethod().getAnnotation(Required.class);
			Annotation[] anno = req.value();
			String _v;
			String[] _vs;
			String _suffix;
			ParaEntity p;
			for(Annotation a : anno){
				p = (ParaEntity) a;
				if(StringUtils.hasText(p.name())){
					
					fieldName = p.name();
					_suffix = "参数 ["+ p.name() +"] 值";
					
					if(fieldName.indexOf("[]") != -1){
						_vs = con.getParaValues(fieldName);
						if(p.empty() && (_vs == null || _vs.length == 0)){
							require = true;
							mess = StringUtils.hasText(p.msg())?p.msg():_suffix + " 为空！";
							break;
						}else if(_vs != null){
							if(p.mlen() != -1 && _vs.length < p.mlen()){
								require = true;
								mess = StringUtils.hasText(p.msg())?p.msg():_suffix + "长度不小于" + p.mlen() + " ！";
								break;
							}else if(p.xlen() != -1 && _vs.length > p.xlen()){
								require = true;
								mess = StringUtils.hasText(p.msg())?p.msg():_suffix + "长度不大于" + p.xlen() + " ！";
								break;
							}
						}
					}else{
						_v = con.getPara(p.name());
						if(StringUtils.hasText(_v) && _v.indexOf("\\\\u") != -1){
							_v = StringUtils.deUnicode(_v);
						}
						if(p.empty() && !StringUtils.hasText(_v)){
							require = true;
							mess = StringUtils.hasText(p.msg())?p.msg():_suffix + " 为空！";
							break;
						}else if(StringUtils.hasText(_v)){
							if(p.mlen() != -1 && _v.trim().length() < p.mlen()){
								require = true;
								mess = StringUtils.hasText(p.msg())?p.msg():_suffix + "长度不能小于 " + p.mlen() + " 个字符！";
								break;
							}else if(p.xlen() != -1 && _v.trim().length() > p.xlen()){
								require = true;
								mess =StringUtils.hasText(p.msg())?p.msg():_suffix + "长度大于 " + p.xlen() + " 个字符";
								break;
							}
						}
					}
				}
			}
		}catch(Exception e){
			Tools.writeLog(inv, e);
		}
		if(require){
			Record result = new Record();
			result.set("message", mess)
			.set("fieldname", fieldName)
			.set("action", actionName)
			.set("method", methodName)
			.set("code", 500);
			con.renderJson(result);
		}
		else{
			con.setAttr("_actionKey", actionName);
			con.setAttr("_methodName", methodName);
			inv.invoke();
		}
	}
}
