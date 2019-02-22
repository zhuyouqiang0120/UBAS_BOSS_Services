/**********************************************************************
 **********************************************************************
 **    Project Name : UDimensionService
 **    Package Name : com.zens.udimension.common								 
 **    Type    Name : Tools 							     	
 **    Create  Time : 2016年10月11日 上午11:42:32								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.chasonx.tools.DateFormatUtil;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;
import com.zens.ubasbossservices.annotation.ApiDesc;
import com.zens.ubasbossservices.annotation.ParaEntity;
import com.zens.ubasbossservices.annotation.Required;

/**
 * @author Chason.x <zuocheng911@163.com>
 * @createTime 2015年8月31日下午6:05:13
 * @remark
 */

/**
 * @author Chason.x <zuocheng911@163.com>
 * @createTime 2015年8月31日下午6:05:13
 * @remark
 */
public class Tools {

	private static final Log log = Log.getLog(Tools.class);

	
	/**
	 * 错误日志记录
	 * @author chasonx
	 * @create 2016年10月11日 下午3:37:26
	 * @update
	 * @param  
	 * @return void
	 */
	public static void writeLog(Invocation v,Exception e){
		StringBuilder sb = new StringBuilder("\n ---------- UCGS Log "+ DateFormatUtil.formatString(null) +"  ----------- \n");
		sb.append("Controller 	   : " + v.getController().getClass().getName() + "\n")
		  .append("Method 		   : " + v.getMethodName() + "\n")
		  .append("UrlPara         : " + v.getController().getPara() + "\n")
		  .append("Exception Type  : " + e.getClass().getName() + "\n")
		  .append("Exception Detail: " + e.getMessage());
		log.error(sb.toString(),e);
	}
	
	/**
	 * API描述信息读取
	 * @author chasonx
	 * @create 2016年10月18日 下午2:01:00
	 * @update
	 * @param  
	 * @return List<Record>
	 */
	public static List<Record> gennerApiDesc(String className,String baseUrl,String requestMethod) throws Exception{
		List<Record> apiList = new ArrayList<Record>();
		Method[] method = Class.forName(className).getDeclaredMethods();
		Annotation[] anns;
		
		List<Record> paramList;
		Record rec;
		Record paramRec;
		ResultDesc rDesc;
		ApiDesc adesc;
		Required req;
		ParaEntity para;
		for(int i = 0;i < method.length;i ++){
			rec = new Record();
			if(method[i].getParameterCount() == 0){
				adesc = method[i].getAnnotation(ApiDesc.class);
				req = method[i].getAnnotation(Required.class);
				anns = req.value();
				paramList = new ArrayList<Record>();
				
				for(Annotation p : anns){
					para = (ParaEntity) p;
					paramRec = new Record()
					.set("fieldName", para.name())
					.set("desc", para.desc())
					.set("type", para.type())
					.set("maxLength", para.xlen() == -1?0:para.xlen())
					.set("minLength", para.mlen() == -1?para.empty()?1:0:para.mlen())
					.set("isNull", para.empty()?"否":"是");
					paramList.add(paramRec);
				}
				
				rDesc = new ResultDesc();
				rDesc.setAction(baseUrl + method[i].getName());
				rDesc.setCode(200);
				rDesc.setMessage("OK");
				rDesc.setMethod(method[i].getName());
				rDesc.setResult(true);
				
				rec.set("action", method[i].getName())
				.set("desc", adesc.value())
				.set("method", requestMethod)
				.set("retrunData", JsonKit.toJson(rDesc))
				.set("baseUrl", baseUrl)
				.set("paramList", paramList);
				apiList.add(rec);
			}
		}
		return apiList;
	}
}
