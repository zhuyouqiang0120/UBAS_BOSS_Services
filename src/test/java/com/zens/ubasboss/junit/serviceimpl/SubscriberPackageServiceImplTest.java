/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasboss.junit.serviceimpl								 
 **    Type    Name : SubscriberPackageServiceImplTest 							     	
 **    Create  Time : 2017年3月17日 上午11:23:16								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasboss.junit.serviceimpl;

import org.junit.Test;

import com.jfinal.kit.JsonKit;
import com.zens.ubasbossservices.service.SubscriberPackageService;
import com.zens.ubasbossservices.serviceimpl.SubscriberPackageServiceImpl;

/**
 * 业务包服务测试
 * @author  Floristy
 * @email   yangsen@zensvision.com
 * @create  2017年3月17日上午11:23:16
 * @version 1.0 
 */
public class SubscriberPackageServiceImplTest {
	
	/** 
	 * 添加一个业务包
	 * @author floristy
	 * @create 2017年3月17日 上午11:29:42
	 * @update
	 * @param  
	 * @return void
	 */
	@Test
	public void add() {
		SubscriberPackageService test = new  SubscriberPackageServiceImpl();
		String __param = "{\"type\":\"1\",\"name\":\"松江包月\",\"code\":\"asdf\",\"price\":\"20\",\"ext\":\"备用字段\"}";
		try {
			String data = JsonKit.toJson(test.add(__param));
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 * 批量标记删除业务包
	 * @author floristy
	 * @create 2017年3月17日 上午11:29:42
	 * @update
	 * @param  
	 * @return void
	 */
	@Test
	public void delete() {
		SubscriberPackageService test = new  SubscriberPackageServiceImpl();
		String __param = "{\"GUIDs\":[\"6870ea7d-2cea-4c1b-842a-ad836089ee19\",\"46e14877-3c17-4b7a-8426-45e9292bb25a\"]}";
		try {
			String data = JsonKit.toJson(test.delete(__param));
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 * 批量恢复标记删除业务包
	 * @author floristy
	 * @create 2017年3月17日 上午11:32:42
	 * @update
	 * @param  
	 * @return void
	 */
	@Test
	public void undelete() {
		SubscriberPackageService test = new  SubscriberPackageServiceImpl();
		String __param = "{\"GUIDs\":[\"6870ea7d-2cea-4c1b-842a-ad836089ee19\",\"46e14877-3c17-4b7a-8426-45e9292bb25a\"]}";
		try {
			String data = JsonKit.toJson(test.undelete(__param));
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 * 批量更新业务包
	 * @author floristy
	 * @create 2017年3月17日 上午11:35:42
	 * @update
	 * @param  
	 * @return void
	 */
	@Test
	public void update() {
		SubscriberPackageService test = new  SubscriberPackageServiceImpl();
		String __param = "[{\"GUID\":\"6870ea7d-2cea-4c1b-842a-ad836089ee19\",\"price\":\"30\"},{\"GUID\":\"46e14877-3c17-4b7a-8426-45e9292bb25a\",\"freezed\":\"1\"}]";
		try {
			String data = JsonKit.toJson(test.update(__param));
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 * 根据终端用户查询业务包
	 * @author floristy
	 * @create 2017年3月17日 上午11:29:42
	 * @update
	 * @param  
	 * @return void
	 */
	@Test
	public void getByTermUser() {
		SubscriberPackageService test = new  SubscriberPackageServiceImpl();
		String __param = "{\"subsGUID\":\"c088a1cf-9b99-425f-8873-f373ab04419b\"}";
		try {
			String data = JsonKit.toJson(test.getByTermUser(__param));
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 * 分页查询业务包
	 * @author floristy
	 * @create 2017年3月17日 上午11:43:54
	 * @update
	 * @param  
	 * @return void
	 */
	@Test
	public void get() {
		SubscriberPackageService test = new  SubscriberPackageServiceImpl();
		String __param = "{\"pageSize\":\"2\",\"page\":\"1\"}";
		try {
			String data = JsonKit.toJson(test.get(__param));
			System.out.println(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
