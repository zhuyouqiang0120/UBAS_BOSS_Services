/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.interceptor								 
 **    Type    Name : MyExceptionInterceptor 							     	
 **    Create  Time : 2016年9月14日 下午12:37:08								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.interceptor;




import com.chasonx.tools.DateFormatUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

import sun.net.www.content.text.plain;


/**
 * 全局异常捕获拦截器
 * 
 * @author Chasonx
 * @email xzc@zensvision.com
 * @create 2016年9月14日下午12:37:08
 * @version 1.0
 */
public class MyExceptionInterceptor implements Interceptor { // 这个类在/UBAS/src/main/java/com/zens/ubas/config/Dconfig.java中被调用。

	private static final Log log = Log.getLog(MyExceptionInterceptor.class);
	public static String stdPicURL = "";
	public static String fpPicURL = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.aop.Interceptor#intercept(com.jfinal.aop.Invocation)
	 */
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		try {
			String __param = controller.getPara("__param");

			String param = controller.getPara("param");
		
			/*
			 * if (__param != null) { __param = new
			 * String(__param.getBytes("iso8859-1"), "utf-8"); } else { param =
			 * new String(param.getBytes("iso8859-1"), "utf-8"); }
			 */
		
			controller.setAttr("__param", __param);
			controller.setAttr("param", param);
			System.out.println("First");
			controller.setAttr("_actionKey",  inv.getActionKey());
			controller.setAttr("_methodName", inv.getMethodName());
		
			inv.invoke();
		
		} catch (Exception e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder(
					"\n ---------- UCGS Log " + DateFormatUtil.formatString(null) + "  ----------- \n");
			sb.append("Controller 	   : " + inv.getController().getClass().getName() + "\n")
					.append("Method 		   : " + inv.getMethodName() + "\n")
					.append("UrlPara         : " + inv.getController().getPara() + "\n")
					.append("Exception Type  : " + e.getClass().getName() + "\n")
					.append("Exception Detail: " + e.getMessage());
			log.error(sb.toString(), e);
			controller.renderText("error error");
		}

	}
		}
