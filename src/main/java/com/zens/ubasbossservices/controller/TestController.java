/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.controller								 
 **    Type    Name : TestController 							     	
 **    Create  Time : 2016年12月1日 上午9:25:16								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.controller;

import com.jfinal.core.Controller;

/**
 * @author  Jhonson
 * @email   yangsen@zensvision.com
 * @create  2016年12月1日上午9:25:16
 * @version 1.0 
 */
public class TestController extends Controller {
	
	public void index(){
		render("2016-10-8-tree/test.ftl");
	}

}
