/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.interceptor								 
 **    Type    Name : UbasService 							     	
 **    Create  Time : 2016年9月19日 下午4:41:15								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;
import com.zens.ubasbossservices.common.Constant;

/**
 * @author  Chasonx
 * @email   xzc@zensvision.om
 * @create  2016年9月19日下午4:41:15
 * @version 1.0 
 */
public class UbasService implements Interceptor { //Interceptor是拦截器的意思。这个UbasService类在/UBAS/src/main/java/com/zens/ubas/controller/HomeController.java中通过注解@Before的方式被调用。

	/* (non-Javadoc)
	 * @see com.jfinal.aop.Interceptor#intercept(com.jfinal.aop.Invocation)
	 */
	@Override
	public void intercept(Invocation inv) { //intercept为拦截的意思。
		try{
			Controller con = inv.getController();
			con.setAttr("APIURL", CacheKit.get(Constant.CACHE_NAME.UBASDATACHCACHE.toString(), Constant.UBAS_SERVICE_CONFIG.SERVICE_URL.toString())); //为什么要先在这个拦截器类中把这个变量setAttr,然后再在被拦截的方法（/UBAS/src/main/java/com/zens/ubas/controller/HomeController.java中的getDataList方法）中getAttr?成哥的回答是“在访问controller之前拦截器对request里面的参数可以做修改，这里先setAttr 是方便调用hessian的API，这里存了个hessian服务端的地址前缀而已”。这里CacheKit.get从缓存中所取的变量是在/UBAS/src/main/java/com/zens/ubas/config/Dconfig.java中通过CacheKit.put方法放到缓存中的。
			inv.invoke(); //这一行代码是对目标方法的调用，在这一行代码的前后插入切面代码可以很方便地实现 AOP。
		}catch(Exception e){
			
		}
	}

}
