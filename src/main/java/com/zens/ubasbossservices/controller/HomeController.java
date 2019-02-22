/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.controller								 
 **    Type    Name : HomeController 							     	
 **    Create  Time : 2016年9月14日 上午10:46:40								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.controller;

import java.net.MalformedURLException;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.HessianFactory;
import com.zens.ubasbossservices.common.PageUtils;
import com.zens.ubasbossservices.entity.T_User;
import com.zens.ubasbossservices.interceptor.UbasService;
import com.zens.ubasbossservices.service.TestService;

/**
 * 首页视图/逻辑控制类
 * @author  Chasonx
 * @email   xzc@zensvision.om
 * @create  2016年9月14日上午10:46:40
 * @version 1.0 
 */
public class HomeController extends Controller {

	/**
	 * 首页地址
	 * @author chasonx
	 * @create 2016年9月14日 上午10:47:25
	 * @update
	 * @param  
	 * @return void
	 * @throws MalformedURLException 
	 */
	public void index(){
		render(PageUtils.PAGE_HOME);
	}
	
	public void demo(){
		render(PageUtils.PAGE_DEMO);
	}
	
	/**
	 * 调用服务端demo
	 * @author chasonx
	 * @create 2016年9月19日 下午5:26:31
	 * @update
	 * @param  
	 * @return void
	 */
	@Before(UbasService.class) //这句代码表示本方法会被拦截器/UBAS/src/main/java/com/zens/ubas/interceptor/UbasService.java拦截。
	public void getDataList(){
		String apiUrl = getAttr("APIURL"); //APIURL来自/UBAS/src/main/java/com/zens/ubas/interceptor/UbasService.java。
		try {
			//apiUrl的值为http://localhost:8080/UBAS-SERVICE/api/，首先/UBAS/src/main/java/com/zens/ubas/config/Dconfig.java在初始化的时候将这个值从数据库中取出来放到缓存中，再在/UBAS/src/main/java/com/zens/ubas/interceptor/UbasService.java中将这个值从缓存中取出来放到request请求中。然后通过上面代码中的getAttr方法将它取出来。
			//apiUrl + ApiUtils.HELLO的值就是http://localhost:8080/UBAS-SERVICE/api/hello，它与服务器端（即UBAS-SERVICE项目）的web.xml文件中配置的HessianServlet映射的访问URL地址对应。
			TestService test = HessianFactory.create(TestService.class, apiUrl + ApiUtils.HELLO); //创建TestService接口的实例对象
			List<T_User> list = test.getListUser(); //调用Hessian服务器端(UBAS-SERVICE项目)的ServiceImpl类中的getListUser方法来获取一个列表。
			renderJson(list);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			renderText(e.getMessage());
		}
	}
}
