/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.service								 
 **    Type    Name : SubscriberPackageService 							     	
 **    Create  Time : 2017年3月7日 上午10:49:01								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import com.sun.jndi.dns.ResourceRecord;
import com.zens.ubasbossservices.entity.T_SubscriberPackage;

import sun.net.www.content.text.plain;

/**
 * @author Floristy
 * @email yangsen@zensvision.com
 * @create 2017年3月7日上午10:49:01
 * @version 1.0
 */
public interface SubscriberPackageService {

	/**
	 * 添加业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:20:52
	 * @update
	 * @param
	 * @return boolean
	 */
	public Record add(String param) throws Exception;

	/**
	 * 删除业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:20:39
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean delete(String param) throws Exception;

	/**
	 * 恢复删除业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:20:25
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean undelete(String param) throws Exception;

	/**
	 * 批量更新业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:20:12
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean update(String param) throws Exception;

	/**
	 * 根据终端用户查询业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:20:03
	 * @update
	 * @param
	 * @return List<Record>
	 */
	public Record getByTermUser(String param) throws Exception;

	/**
	 * 分页查询业务包
	 * 
	 * @author floristy
	 * @create 2017年3月13日 下午3:56:49
	 * @update
	 * @param
	 * @return Record
	 */
	public Record get(String param) throws Exception;

	/**
	 * 分页查询业务包计费策略
	 * 
	 * @author ZKill
	 * @create 2017年3月13日 下午3:56:49
	 * @update
	 * @param
	 * @return Record
	 */
	public Record getstategy(String param) throws Exception;

	/**
	 * 业务包设置计费策略
	 * 
	 * @author ZKill
	 * @create 2017年3月13日 下午3:56:49
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean setsubstategy(String param) throws Exception;

	/**
	 * 计费策略绑定业务包
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 下午3:56:49
	 * @update
	 * @param
	 * @return boolean
	 */

	public boolean setstategysub(String param) throws Exception;

	/**
	 * 业务包和计费策略的解除（一对多）
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 14:36:22
	 * @update
	 * @param
	 * @return boolean
	 */

	public List<Record> deletesubstategy(String param, String Grouptype) throws Exception;

	/**
	 * 计费策略和业务包解除（一对一）
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 14:36:22
	 * @update
	 * @param
	 * @return boolean
	 */

	public boolean deletestategysub(String param) throws Exception;

	/**
	 * 查看指定业务包下面有哪些计费策略
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 14:36:22
	 * @update
	 * @param
	 * @return Record
	 */

	public Record selectSubstrategy(String param) throws Exception;

	/**
	 * 查看指定业务包下面未开通计费策略
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 14:36:22
	 * @update
	 * @param
	 * @return Record
	 */

	public Record selectunSubstrategy(String param) throws Exception;

	/**
	 * 新增计费策略
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 16:15:54
	 * @update
	 * @param
	 * @return boolean
	 */

	public Record addstrategy(String param) throws Exception;

	/**
	 * 计费策略的编辑
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 16:15:54
	 * @update
	 * @param
	 * @return boolean
	 */

	public boolean updatestrategy(String param) throws Exception;

	/**
	 * 查询指定策略包对应的业务包的信息
	 * 
	 * @author ZKill
	 * @create 2017年7月13 日 16:15:54
	 * @update
	 * @param
	 * @return boolean
	 */

	public Record strategysub(String param) throws Exception;

	/**
	 * 分页获取所有的可以使用业务包和策略包
	 * 
	 * @author ZKill
	 * @create 2017年7月13日 16:15:54
	 * @update
	 * @param
	 * @return Reocrd
	 */

	public Record getsubstr(String param) throws Exception;

	public List<Record> getsub(String subid) throws Exception;

	public boolean updatesub(String sub) throws Exception;

	public List<Record> getSystemtermuser() throws Exception;

}
