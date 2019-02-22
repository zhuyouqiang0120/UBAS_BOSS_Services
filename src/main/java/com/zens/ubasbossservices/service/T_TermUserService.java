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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceOctetStreamData;

/**
 * @author Chasonx
 * @email xzc@zensvision.om
 * @create 2016年9月19日上午10:42:37
 * @version 1.0
 */
public interface T_TermUserService {

	/**
	 * 添加终端用户
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:24:51
	 * @update
	 * @param
	 * @return boolean
	 */

	public Record add(String param) throws Exception;

	/**
	 * 查询用户账户信息
	 * 
	 * @author ZKill
	 * @create 2017年3月10日 上午11:24:51
	 * @update
	 * @param
	 * @return Record
	 */

	public Record selectaccount(String param) throws Exception;

	/**
	 * 查询用户产品订购记录
	 * 
	 * @author ZKill
	 * @create 2017年7月18日 15:18:10
	 * @update
	 * @param
	 * @return Record
	 */
	public Record selectProductrecord(String param) throws Exception;

	/**
	 * 查询用户可订购的产品(未开通，包括策略包和业务包）
	 * 
	 * @author ZKill
	 * @create 2017年7月24日14:57:58
	 * @update
	 * @param
	 * @return Record
	 */

	public Record getByTermUserbidsub(String param) throws Exception;

	/**
	 * 用户点播鉴权接口（重要）
	 * 
	 * @author ZKill
	 * @create 2017年7月18日 17:19:01
	 * @update
	 * @param
	 * @return Record
	 */

	public Record VODauthentication(String param) throws Exception;

	/**
	 * 删除终端用户
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:25:06
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean delete(String param) throws Exception;

	/**
	 * 恢复删除终端用户
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:25:18
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean undelete(String param) throws Exception;

	/**
	 * 更新终端用户
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:25:37
	 * @update
	 * @param
	 * @return boolean
	 */
	public List<Record> update(String param) throws Exception;

	/**
	 * 为终端用户设置区域
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:25:50
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean setRegions(String param) throws Exception;

	/**
	 * 为终端用户设置分组
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:26:49
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean setGroup(String param) throws Exception;

	/**
	 * 为终端用户开通业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:27:05
	 * @update
	 * @param
	 * @return boolean
	 */
	public List<Record> bindTermUser_SubscriberPackages(String param, String operation) throws Exception;

	/**
	 * 为终端用户停断业务包
	 * 
	 * @author floristy
	 * @create 2017年3月15日 下午5:14:54
	 * @update
	 * @param
	 * @return boolean
	 */
	public List<Record> unbindTermUser_SubscriberPackages(String param) throws Exception;

	/**
	 * 暂停业务包
	 * 
	 * @author floristy
	 * @create 2017年3月21日 下午2:37:08
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean suspendTermUser_SubscriberPackages(String param) throws Exception;

	/**
	 * 继续业务包
	 * 
	 * @author floristy
	 * @create 2017年3月21日 下午2:38:20
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean continueTermUser_SubscriberPackages(String param) throws Exception;

	/**
	 * 为终端用户暂停业务包
	 * 
	 * @author floristy
	 * @create 2017年3月15日 下午5:16:00
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean freezeTermUser_SubscriberPackages(String param) throws Exception;

	/**
	 * 为终端用户恢复暂停业务包
	 * 
	 * @author floristy
	 * @create 2017年3月15日 下午5:16:27
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean unfreezeTermUser_SubscriberPackages(String param) throws Exception;

	/**
	 * 为终端用户续订业务包
	 * 
	 * @author floristy
	 * @create 2017年3月15日 下午5:17:01
	 * @update
	 * @param
	 * @return boolean
	 */
	public List<Record> renewTermUser_SubscriberPackages(String param) throws Exception;

	/**
	 * 查询某终端用户是否已订购指定的业务包
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:27:25
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean hasSubscriberPackage(String param) throws Exception;

	/**
	 * 查询某终端用户已订购的产品
	 * 
	 * @author zhangkai
	 * @create 2017年7月17日 15:37:15
	 * @update
	 * @param
	 * @return Record
	 */

	public Record hasSubproduct(String param) throws Exception;

	/**
	 * 查询指定注册用户所属区域（支持根据任意能唯一表示注册用户的字段进行查询）
	 * 
	 * @author floristy
	 * @create 2017年3月28日 下午3:05:35
	 * @update
	 * @param
	 * @return boolean
	 */
	public List<Record> getRegionsByTermUser(String param) throws Exception;

	/**
	 * 获取终端用户所有信息（包括与终端用户关联的区域和分组信息）
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:28:09
	 * @update
	 * @param
	 * @return Record
	 */
	public Record get(String param, boolean byRegionGUID) throws Exception;

	/**
	 * 终端用户认证接口
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:28:09
	 * @update
	 * @param
	 * @return Record
	 */

	public Record userauthentication(String param) throws Exception;

	/**
	 * 删除ZExamMS用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:00:46
	 * @update
	 * @param
	 * @return Record
	 */
	public boolean deleteZE(String param) throws Exception;

	/**
	 * 获取ZExamMS用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:17:37
	 * @update
	 * @param
	 * @return Record
	 */
	public Record getZE(String param) throws Exception;

	/**
	 * 更新ZExamMS用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:17:37
	 * @update
	 * @param
	 * @return Record
	 */

	public boolean ZEupdate(String param) throws Exception;

	/**
	 * 根据区域查设备信息或根据设备查询该设备下的人员信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:17:37
	 * @update
	 * @param
	 * @return Record
	 */

	public List<Record> getiplpsuserfo(String param) throws Exception;

	/**
	 * 根据设备的mac地址查询对应的区域
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:17:37
	 * @update
	 * @param
	 * @return Record
	 */
	public Record getRegion(String param) throws Exception;

	/**
	 * 新增
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:17:37
	 * @update
	 * @param
	 * @return Record
	 */
	public boolean addZE(String param) throws Exception;

	/**
	 * 更新用户
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:17:37
	 * @update
	 * @param
	 * @return Record
	 */
	public boolean ZEsetuser(String param) throws Exception;

	public Record getBossUserInfo(String FGuid) throws Exception;
	// public Record Bossdataimport(String filePath) throws Exception;
	// public List<Record> Bossdataimport(String filePath, String[] Sitvkey)
	// throws Exception;

	public Record getBossUserSubscriberPackage(String param) throws Exception;

	/**
	 * 用户点播认证
	 * 
	 * @author ZKill
	 * @create 2018年3月1日 15:11:04
	 * @update
	 * @param
	 * @return Record
	 */

	public Record demanDcertification(String param, HttpServletRequest request) throws Exception;

	/**
	 * UAMS获取标识用接口
	 * 
	 * @author ZKill
	 * @create 2018年3月1日 15:11:04
	 * @update
	 * @param
	 * @return Record
	 */

	public String UAGetTOken(String IP) throws Exception;
	/**
	 * 获取UBAS系统用户信息
	 * 
	 * @author ZKill
	 * @create 2018年4月10日 10:22:58
	 * @update
	 * @param
	 * @return Record
	 */
	public List<Record> getSystemtermuser() throws Exception;


}
