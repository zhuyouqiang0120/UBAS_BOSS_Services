/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS-SERVICE
 **    Package Name : com.zens.ubasservice.services								 
 **    Type    Name : TestService 							     	
 **    Create  Time : 2016年9月19日 上午10:42:37								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.service;

import java.io.InputStream;
import java.util.List;

import com.zens.ubasbossservices.entity.T_User;


/**
 * @author  Chasonx
 * @email   xzc@zensvision.com
 * @create  2016年9月19日上午10:42:37
 * @version 1.0 
 */
public interface TestService {

	public void hello(String name);
	
	public int addUser(String name,String sex);
	
	public List<T_User> getListUser();
	
	public List<String> getLongText();
	
	public int uploadFile(String fileName,InputStream data);
	
}
