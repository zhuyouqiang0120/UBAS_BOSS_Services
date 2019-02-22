/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasbossservices.serviceimpl								 
 **    Type    Name : SubscriberPackageServiceImpl 							     	
 **    Create  Time : 2017年3月7日 上午10:49:24								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2017 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.serviceimpl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.ExtendedSSLSession;
import javax.print.attribute.ResolutionSyntax;

import org.omg.CORBA.BAD_CONTEXT;

import com.caucho.hessian.server.HessianServlet;
import com.chasonx.tools.StringUtils;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.TableMapping;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zens.ubasbossservices.entity.T_Strategy;
import com.zens.ubasbossservices.entity.T_SubscriberPackage;
import com.zens.ubasbossservices.entity.T_Subscriberpackage_strategy;
import com.zens.ubasbossservices.entity.T_System_termuser;
import com.zens.ubasbossservices.entity.T_TermUserSubscriberPackage;
import com.zens.ubasbossservices.service.SubscriberPackageService;
import com.zens.ubasbossservices.utils.RecordUtil;
import com.zens.ubasbossservices.utils.SQLUtil;
import com.zens.ubasbossservices.utils.TimeUT;

import freemarker.ext.beans.BeansWrapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Floristy
 * @email yangsen@zensvision.com
 * @create 2017年3月7日上午10:49:24
 * @version 1.0
 */
public class SubscriberPackageServiceImpl extends HessianServlet implements SubscriberPackageService {

	private static final long serialVersionUID = 1L;

	String subscriberPackageTableName = TableMapping.me().getTable(T_SubscriberPackage.class).getName();
	String tU_SPTableName = TableMapping.me().getTable(T_TermUserSubscriberPackage.class).getName();
	String subscriberPackageSelectSQL = "SELECT ID AS id,FGUID AS GUID,FCycle AS cycle,FCode AS code,FName AS name,FPrice AS price,FCreateTime AS createTime,FCreateUserID AS createUserID,FUpdateTime AS updateTime,FEfectivetime AS efectivetime,FUpdateUserID AS updateUserID,FFreezed AS freezed,FDeleted AS deleted,FExt AS ext,FAvailabilityTime AS availabilityTime,FProductID AS productID FROM "
			+ subscriberPackageTableName;

	/*
	 * 添加一个业务包记录
	 * 
	 * @see com.zens.ubasbossservices.service.SubscriberPackageService#
	 * addProductPackage(java.lang.String)
	 */
	@Override
	public Record add(String param) throws Exception {

		UUID uuid = UUID.randomUUID();
		JSONObject paramData = JSONObject.fromObject(param);
		Record record = new Record();
		Record staterecord = new Record();
		List<String> paramNames = new ArrayList<String>();
		paramNames.add("cycle");
		paramNames.add("code");
		paramNames.add("name");
		paramNames.add("price");
		paramNames.add("createUserID");
		paramNames.add("freezed");
		paramNames.add("deleted");
		paramNames.add("ext");
		paramNames.add("efectivetime");
		paramNames.add("availabilityTime");
		paramNames.add("productID");
		// 新加业务包的时候验证产品ID的唯一性
		if (Integer.parseInt(Db
				.findFirst(Db.getSqlPara("subscriberpackage.addsubscriberpackage", Kv
						.by("tablename", T_SubscriberPackage.tableName).set("FProductID", paramData.get("productID"))))
				.get("num").toString()) > 0) {
			// 产品ID已存在，返回添加失败状态
			staterecord.set("state", "产品ID已存在添加失败");
			return staterecord;
		}
		;

		RecordUtil.setRecord(paramNames, paramData, record);
		record.set("FGUID", uuid.toString()).set("FCreateTime", TimeUT.getCurrTime());
		if (Db.save(subscriberPackageTableName, record)) {
			staterecord.set("state", "操作成功");
			return staterecord;
		}
		;

		staterecord.set("state", "操作失败");
		return staterecord;
	}

	/*
	 * 批量标记删除业务包
	 * 
	 * @see com.zens.ubasbossservices.service.SubscriberPackageService#
	 * deleteProductPackages(java.lang.String)
	 */
	@Override
	public boolean delete(String param) throws Exception {
		boolean success = false;
		JSONObject paramData = JSONObject.fromObject(param);
		JSONArray GUIDsJA = paramData.getJSONArray("GUIDs");
		if (canBeDeletedOrFreezed(GUIDsJA)) { // 如果可以被删除
			String GUIDsArrayStr = GUIDsJA.toString();
			final String GUIDs = GUIDsArrayStr.replace("[", "").replace("]", "");
			if (!GUIDs.equals("")) {
				// 事务回滚
				success = Db.tx(new IAtom() {
					@Override
					public boolean run() throws SQLException {
						boolean result = true;
						try {
							result = Db.update(
									"UPDATE " + subscriberPackageTableName + " SET FDeleted = '1' , FUpdateTime = '"
											+ TimeUT.getCurrTime() + "'  WHERE FGUID IN (" + GUIDs + ")") > 0;
							// 更新终端用户业务包关联表
							result = Db
									.update("UPDATE " + tU_SPTableName + " SET FDeleted = '1' , FUpdateTime = '"
											+ TimeUT.getCurrTime() + "'  WHERE FSubsGUID IN (" + GUIDs + ")") >= 0
									&& result;
							// 重置对应策略的策略开始和生效时间
							result = Db.update("UPDATE " + T_Strategy.tableName
									+ " set FAvailabilityTime='',FEfectivetime='' WHERE FGUID in (SELECT FStrategyguid "
									+ " FROM " + T_Subscriberpackage_strategy.tableName + " WHERE FSubguid IN (" + GUIDs
									+ ") AND FDeleted!=1)  AND FDeleted!=1") >= 0 && result;
							// 解除与他绑定计费策略的关系
							result = Db.update("UPDATE " + T_Subscriberpackage_strategy.tableName
									+ " SET FDeleted='1',FUpdateTime='" + TimeUT.getCurrTime()
									+ "'  WHERE FSubguid IN (" + GUIDs + ")") >= 0 && result;

						} catch (Exception e) {
							e.printStackTrace();
							result = false;
						}
						return result;
					}
				});
			}
		}
		return success;
	}

	/*
	 * 批量标记恢复删除业务包
	 * 
	 * @see com.zens.ubasbossservices.service.SubscriberPackageService#
	 * undeleteProductPackages(java.lang.String)
	 */
	@Override
	public boolean undelete(String param) throws Exception {
		boolean success = false;
		JSONObject paramData = JSONObject.fromObject(param);
		JSONArray GUIDsJA = paramData.getJSONArray("GUIDs");
		String GUIDsArrayStr = GUIDsJA.toString();
		final String GUIDs = GUIDsArrayStr.replace("[", "").replace("]", "");
		if (!GUIDs.equals("")) {
			// 事务回滚
			success = Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					boolean result = true;
					try {
						result = Db
								.update("UPDATE " + subscriberPackageTableName + " SET FDeleted = '0' , FUpdateTime = '"
										+ TimeUT.getCurrTime() + "'  WHERE FGUID IN (" + GUIDs + ")") > 0;
						// 更新终端用户业务包关联表
						result = Db
								.update("UPDATE " + tU_SPTableName + " SET FDeleted = '0' , FUpdateTime = '"
										+ TimeUT.getCurrTime() + "'  WHERE FSubsGUID IN (" + GUIDs + ")") >= 0
								&& result;
					} catch (Exception e) {
						e.printStackTrace();
						result = false;
					}
					return result;
				}
			});
		}
		return success;
	}

	/*
	 * 批量更新业务包
	 * 
	 * @see com.zens.ubasbossservices.service.SubscriberPackageService#
	 * updateProductPackages(java.lang.String)
	 */
	@Override
	public boolean update(String param) throws Exception {
		final JSONArray paramData = JSONArray.fromObject(param);
		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : paramData) {
						String updateSQL = "UPDATE " + subscriberPackageTableName + " SET ";
						JSONObject jO = (JSONObject) o;
						String GUID = (String) jO.get("GUID");
						String freezed = (String) jO.get("freezed");
						if ("1".equals(freezed)) { // 如果前台要冻结这条记录，就要提高警惕了！
							result = canBeDeletedOrFreezed(GUID) && result; // 判断这条记录是否可以被冻结
							if (result == false) {
								break; // 直接跳出for循环
							}
							// 更改所有对应绑定计费策略的状态
							Db.update(Db.getSqlPara("stategy.updateffreezed",
									Kv.by("tablename", T_Strategy.tableName).set("FFreezed", "3")
											.set("tablename1", T_Subscriberpackage_strategy.tableName)
											.set("FSubguid", GUID))
									.getSql());

						}
						// 如果发现当前需要解冻的业务包已过期那么
						if ("0".equals(freezed)) {
							if (!getsubscriberpackstate(GUID)) {
								result = false;
								break;// 直接跳出for循环
							}
							// 更改所有对应绑定计费策略的状态
							Db.update(Db.getSqlPara("stategy.updateffreezed",
									Kv.by("tablename", T_Strategy.tableName).set("FFreezed", "0")
											.set("tablename1", T_Subscriberpackage_strategy.tableName)
											.set("FSubguid", GUID))
									.getSql());
						}

						// 如果发现当前业务包没有过期但是已经冻结就无法进行更新编辑（只能进行解冻操作）
						if (getsubscriberpackstate(GUID) && !Checkstatus(GUID)) {

							if (freezed.equals("0")) {
								result = true;
							} else {
								result = false;
								break;// 直接跳出for循环
							}
						}
						// 如果发现做对业务包做了延期操作那么直接把该业务包的状态改为正常
						if (jO.get("efectivetime") != null) {
							String sql = "UPDATE " + subscriberPackageTableName + " SET  FFreezed= 0 WHERE FGUID=?";
							if (Db.update(sql, GUID) == 1) {
								result = true;
							} else {
								result = false;
								break;// 直接跳出for循环
							}
							;
						}

						String setSQL = getSetSQL(jO);
						updateSQL += setSQL;
						updateSQL += " WHERE FGUID = '" + GUID + "' ";
						System.out.println(updateSQL);
						result = updateSubscriberPackage(updateSQL) && result;
						if (result == false) {
							break; // 直接跳出for循环
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				return result;
			}
		});
		return success;
	}

	/**
	 * 更新一条业务包记录
	 * 
	 * @author floristy
	 * @create 2017年3月7日 下午3:42:15
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean updateSubscriberPackage(String sql) throws Exception {
		return Db.update(sql) > 0;
	}

	/**
	 * 拼出更新业务包记录所需的SET片段
	 * 
	 * @author floristy
	 * @create 2017年3月8日 下午1:18:32
	 * @update
	 * @param
	 * @return String
	 */
	public String getSetSQL(JSONObject jO) throws Exception {
		StringBuffer setSQL = new StringBuffer();
		String result;
		List<String> setNames = new ArrayList<String>();
		// setNames.add("type");
		setNames.add("cycle");
		setNames.add("code");
		setNames.add("name");
		setNames.add("price");
		setNames.add("freezed");
		setNames.add("ext");
		setNames.add("efectivetime");
		setNames.add("availabilityTime");
		setNames.add("productID");
		SQLUtil.addParamsToSetSQL(setSQL, jO, setNames);
		setSQL.append(" FUpdateTime = '");
		setSQL.append(TimeUT.getCurrTime());
		setSQL.append("' ");
		result = setSQL.toString();
		return result;
	}

	/**
	 * 批量判断业务包记录能否被标记删除或冻结（只要有一个不能，就全部都不能）
	 * 
	 * @author floristy
	 * @create 2017年3月8日 下午3:15:00
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean canBeDeletedOrFreezed(JSONArray GUIDsJA) {
		boolean result = true;
		for (Object o : GUIDsJA) {
			String GUID = (String) o;
			result = canBeDeletedOrFreezed(GUID) && result;
		}
		return result;
	}

	/**
	 * 判断业务包记录能否被标记删除或冻结
	 * 
	 * @author floristy
	 * @create 2017年3月8日 下午2:43:50
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean canBeDeletedOrFreezed(String GUID) {
		String currentTime = TimeUT.getCurrTime();

		// 去用户业务包关联表中查下 有没有该业务包的关联记录是开通着的并在合法期之内。

		String sql = "SELECT * FROM " + tU_SPTableName
				+ " WHERE FSubscriberPackageGUID = ? AND FDeleted = '0' AND FFreezed = '0' AND FExpiryTime > ? "; // TimeUT.getCurrTime()格式为2017/03/08
																													// 14:57:53
		Record record = Db.findFirst(sql, GUID, currentTime);
		if (record != null) {
			return false;
		}
		return true;
	}

	/*
	 * 根据终端用户GUID查询业务包
	 * 
	 * @see
	 * com.zens.ubasbossservices.service.SubscriberPackageService#getByTermUser
	 * (java.lang.String)
	 */
	@Override
	public Record getByTermUser(String param) throws Exception {
		Record record = new Record();
		JSONObject paramJO = JSONObject.fromObject(param);
		Integer pageSize = StringUtils.hasText((String) paramJO.get("pageSize"))
				? Integer.parseInt((String) paramJO.get("pageSize")) : null;
		Integer page = StringUtils.hasText((String) paramJO.get("page"))
				? Integer.parseInt((String) paramJO.get("page")) : null;

		String subsGUID = paramJO.getString("subsGUID");
		// 根据终端用户GUID查出所有相关的终端用户业务包关联记录
		String sql = "SELECT  t.FExpiryTime AS expiryTime,t.FSubscriberPackageGUID AS subscriberPackageGUID,b.FGUID AS GUID,b.FType AS type,b.FCycle AS cycle,b.FCode AS code,b.FName AS name,b.FPrice AS price,b.FCreateTime AS createTime,b.FCreateUserID AS createUserID,b.FUpdateTime AS updateTime,b.FUpdateUserID AS updateUserID,b.FFreezed AS freezed,b.FDeleted AS deleted,b.FExt AS ext,b.FEfectivetime AS efectivetime,b.FAvailabilityTime AS availabilityTime,b.FProductID AS productID,b.FFreezed AS freezed,b.FUpdateTime AS updateTime,b.FPrice AS price  FROM t_termuser_subscriberpackage t JOIN t_subscriberpackage b ON t.FSubscriberPackageGUID=b.FGUID  WHERE FSubsGUID=?";
		// List<Record> subscriberPackages = null;
		if (pageSize != null && page != null) {

			SqlPara sqlPara = Db.getSqlPara("SubcriberPackage.getByTermUser", Kv.by("tablename", tU_SPTableName)
					.set("tablename1", subscriberPackageTableName).set("FSubsGUID", subsGUID));
			Page<Record> tU_SPs = Db.paginate(page, pageSize, sqlPara);
			record.set("tU_SPs", tU_SPs);
		} else {

			List<Record> tU_SPs = Db.find(sql, subsGUID);
			System.out.println(tU_SPs);
			record.set("tU_SPs", tU_SPs);
		}

		// 根据终端用户GUID查出所有相关的终端用户业务包关联记录

		return record;
	}

	/**
	 * 根据终端用户GUID查出所有相关的终端用户业务包关联记录
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午10:10:22
	 * @update
	 * @param
	 * @return List<Record>
	 */
	public List<Record> getTU_SPs(String subsGUID) {
		String sql = "SELECT FSubscriberPackageGUID AS subscriberpackageguid,FAvailabilityTime AS AvailabilityTime,FExpiryTime AS expirytime FROM "
				+ tU_SPTableName + " WHERE FSubsGUID = ? ORDEY BY ID DESC ";
		return Db.find(sql, subsGUID);
	}

	/**
	 * 遍历接收到的终端用户业务包关联记录，将它们的FSubscriberPackageGUID提取出来拼成SQL片段用以查询业务包记录。
	 * 
	 * @author floristy
	 * @create 2017年3月10日 上午11:13:15
	 * @update
	 * @param
	 * @return String
	 */
	public String getSubscriberPackageGUIDs(List<Record> tU_SPs) {
		StringBuilder GUIDs = new StringBuilder("");
		for (Record tU_SP : tU_SPs) {
			String subscriberPackageGUID = tU_SP.getStr("subscriberpackageguid");

			GUIDs.append("'");
			GUIDs.append(subscriberPackageGUID);
			GUIDs.append("',");
		}
		if (',' == GUIDs.charAt(GUIDs.length() - 1)) { // 如果最后一个字符是逗号，就将它删除。
			GUIDs = GUIDs.deleteCharAt(GUIDs.length() - 1);

		}
		return GUIDs.toString();
	}

	/*
	 * 分页查询业务包
	 * 
	 * @see
	 * com.zens.ubasbossservices.service.SubscriberPackageService#get(java.lang
	 * .String)
	 */
	@Override
	public Record get(String param) throws Exception {
		/*
		 * String sqls;
		 * sqls="Select FEfectivetime,FGUID FROM t_subscriberpackage ";
		 * //如果是自动冻结的 FFreezed=2 List<Record> Fectivetime=Db.find(sqls);
		 * //获取到当前时间 long newtime=System.currentTimeMillis(); for(Record
		 * fectivetime:Fectivetime){
		 * 
		 * 
		 * if( fectivetime.getStr("FEfectivetime")!=null){ long Fefectime =
		 * TimeUT.getMillsTime(fectivetime.getStr("FEfectivetime"));
		 * 
		 * //当前业务包已过期，直接冻结 if(newtime>Fefectime){
		 * sqls="Update  t_subscriberpackage  Set FFreezed= 2 Where FGUID=  ?";
		 * //sql="Update"
		 * +subscriberPackageTableName+"Set FEfectivetime= 1 Where FGUID= "
		 * +subscriberPackageGUID; Db.update(sqls,fectivetime.getStr("FGUID"));
		 * 
		 * } } }
		 */
		// 检查业务包是否过期 如果过期修改成过期状态
		Db.update(Db.getSqlPara("stategy.tgetstategy", Kv.by("tablename", T_SubscriberPackage.tableName)).getSql());
		Record record = new Record();
		JSONObject paramData = JSONObject.fromObject(param);

		List<Record> subscriberPackages = null;
		Integer pageSize = StringUtils.hasText((String) paramData.get("pageSize"))
				? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = StringUtils.hasText((String) paramData.get("page"))
				? Integer.parseInt((String) paramData.get("page")) : null;

		String keyword = (String) paramData.get("keyword");
		String type = (String) paramData.get("type");
		String freezed = (String) paramData.get("freezed");
		String deleted = (String) paramData.get("deleted");
		String cycle = (String) paramData.get("cycle");
		if (!StringUtils.hasText(deleted)) {
			deleted = "0";
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("FType", type);
		paramMap.put("FFreezed", freezed);
		paramMap.put("FCycle", cycle);
		String whereSQL = SQLUtil.getWhereSql(paramMap);

		if (StringUtils.hasText(keyword)) {
			String keywordSQL = getSQLByKeyword(keyword);
			whereSQL += keywordSQL;
		}
		String deletedSQL = " AND FDeleted = '" + deleted + "' ";
		whereSQL += deletedSQL;
		String sql = subscriberPackageSelectSQL + whereSQL + " ORDER BY ID DESC ";
		if (pageSize != 0) {
			String limitSQL = SQLUtil.getLimitSQL(pageSize, page);
			sql += limitSQL;

		}
		subscriberPackages = Db.find(sql);
		record.set("subscriberPackages", subscriberPackages);
		if (pageSize != 0) {
			// 获取前端分页所需参数
			getParamForLimitPage(record, whereSQL, pageSize);
		}
		return record;
	}

	/**
	 * 根据keyword拼出（用于筛选的）SQL片段。
	 * 
	 * @author floristy
	 * @create 2017年3月13日 下午4:13:09
	 * @update
	 * @param
	 * @return String
	 */
	public String getSQLByKeyword(String keyword) throws Exception {
		StringBuffer sql = new StringBuffer(" AND (FGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FType LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FProductID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCode LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FName LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrice LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCreateTime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCreateUserID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FUpdateTime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FUpdateUserID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FExt LIKE'%");
		sql.append(keyword);
		sql.append("%')");
		return sql.toString();
	}

	/**
	 * 获取前端分页所需参数
	 * 
	 * @author floristy
	 * @create 2017年3月13日 下午4:18:46
	 * @update
	 * @param
	 * @return void
	 */
	public void getParamForLimitPage(Record record, String whereSQL, Integer pageSize) throws Exception {
		Number number = count(whereSQL);
		int pages = number.intValue() / pageSize;
		int res = number.intValue() % pageSize;
		int total = number.intValue();
		int totalPages = res == 0 ? pages : pages + 1; // totalPages是若返回所有（未删除的）的记录应该显示的页数。
		record.set("total", total); // 能够返回的（即未删除的）记录数。
		record.set("totalPages", totalPages); // totalPages是若返回所有记录应该显示的页数。
	}

	/**
	 * 获取t_subscriberpackage表中所有未标记删除的记录数
	 * 
	 * @author Johnson
	 * @param freezed
	 * @param type
	 * @create 2017年2月20日 下午4:19:20
	 * @update
	 * @param
	 * @return Number
	 */
	public Number count(String whereSQL) throws Exception {
		String sql = "SELECT COUNT(ID) FROM " + subscriberPackageTableName + whereSQL;
		return Db.queryNumber(sql);
	}

	/**
	 * 
	 * 对业务包进行解冻操作时查询该业务包是否过期 如果过期了 那么则无法解冻
	 * 
	 * @author Johnson
	 * @param freezed
	 * @param type
	 * @create 2017-04-18 11:21:51
	 * @update
	 * @param
	 * @return Number
	 */
	public boolean getsubscriberpackstate(String GUID) {
		boolean success = true;

		try {

			// 获取到当前时间
			long time = System.currentTimeMillis();
			// 获取业务包过期时间
			String sql = "SELECT FEfectivetime FROM t_subscriberpackage  WHERE FGUID=? ";
			Record record = Db.findFirst(sql, GUID);

			if (time > TimeUT.getMillsTime(record.getStr("FEfectivetime"))) {
				success = false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return success;

	}

	/**
	 * 
	 * 查看当前业务包是否处于被冻结的状态
	 * 
	 * @author zhangkai
	 * @param freezed
	 * @param type
	 * @create 2017-04-24 15:32:16
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean Checkstatus(String guid) {
		boolean status = true;
		String sql = "Select FFreezed   FROM t_subscriberpackage  WHERE FGUID=?";

		if (Db.findFirst(sql, guid).getStr("FFreezed").equals("1")) {
			// 说明该业务包处于被冻结状态
			status = false;

		}
		return status;
	}

	/******************************************************** 计费策略管理 **************************************************************/
	/**
	 * 
	 * 分页查询计费策略
	 * 
	 * @author zhangkai
	 * 
	 * @param type
	 * @create 2017年7月12日 15:56:52
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record getstategy(String param) throws Exception {
		Record record = new Record();
		JSONObject paramData = JSONObject.fromObject(param);
		// 获取计费策略之前 查看计费策略是否过期，如果过期那么修改成过期状态
		Db.update(Db.getSqlPara("stategy.tgetstategy", Kv.by("tablename", T_Strategy.tableName)).getSql());
		Page<Record> stategy = null;
		Integer pageSize = StringUtils.hasText((String) paramData.get("pageSize"))
				? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = StringUtils.hasText((String) paramData.get("page"))
				? Integer.parseInt((String) paramData.get("page")) : null;
		String keyword = (String) paramData.get("keyWord");
		String FLifenumber = (String) paramData.get("FLifenumber");// 周期个数
		String FLifecycle = (String) paramData.get("FLifecycle");// 计费周期
		String FPrice = (String) paramData.get("FPrice");// 价格
		String FFreezed = (String) paramData.get("FFreezed");

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("FLifenumber", FLifenumber);
		paramMap.put("FLifecycle", FLifecycle);
		paramMap.put("FPrice", FPrice);
		paramMap.put("FFreezed", FFreezed);
		String whereSQL = SQLUtil.getWhereSql(paramMap);

		if (StringUtils.hasText(keyword)) {
			System.out.println("模糊查询");
			String keywordSQL = getSQLstategy(keyword);
			whereSQL += keywordSQL;
			System.out.println(whereSQL);
		}
		whereSQL += " AND FDeleted!=1";
		Kv cond = Kv.by("tablename", T_Strategy.tableName).set("WHERES", whereSQL);
		SqlPara sqlPara = Db.getSqlPara("stategy.getstategy", cond);
		// System.out.println(sqlPara);
		// stategy=Db.find(sqlPara);
		stategy = Db.paginate(page, pageSize, sqlPara);
		/*
		 * String limitSQL = SQLUtil.getLimitSQL(pageSize, page); String sql =
		 * subscriberPackageSelectSQL + whereSQL + " ORDER BY ID DESC " +
		 * limitSQL; subscriberPackages = Db.find(sql);
		 */
		record.set("stategy", stategy);
		/*
		 * if (pageSize != null) { // 获取前端分页所需参数 getParamForLimitPage(record,
		 * whereSQL, pageSize); }
		 */
		return record;
	}

	/**
	 * 根据keyword拼出（用于筛选的）SQL片段。
	 * 
	 * @author floristy
	 * @create 2017年3月13日 下午4:13:09
	 * @update
	 * @param
	 * @return String
	 */
	public String getSQLstategy(String keyword) throws Exception {
		if (keyword.equals("过期")) {
			keyword = "2";
			StringBuffer sql = new StringBuffer(" AND (FFreezed LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		} else if (keyword.equals("正常")) {
			keyword = "0";
			StringBuffer sql = new StringBuffer(" AND (FFreezed LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		} else if (keyword.equals("冻结")) {
			keyword = "1";
			StringBuffer sql = new StringBuffer(" AND (FFreezed LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		}
		StringBuffer sql = new StringBuffer(" AND (FName LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FDeleted LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FEfectivetime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FAvailabilityTime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FLifenumber LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FProductID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrice LIKE'%");
		sql.append(keyword);
		sql.append("%')");
		return sql.toString();
	}

	/**
	 * 获取前端分页所需参数
	 * 
	 * @author floristy
	 * @create 2017年3月13日 下午4:18:46
	 * @update
	 * @param
	 * @return void
	 */
	public void getstategyParamForLimitPage(Record record, String whereSQL, Integer pageSize) throws Exception {
		Number number = count(whereSQL);
		int pages = number.intValue() / pageSize;
		int res = number.intValue() % pageSize;
		int total = number.intValue();
		int totalPages = res == 0 ? pages : pages + 1; // totalPages是若返回所有（未删除的）的记录应该显示的页数。
		record.set("total", total); // 能够返回的（即未删除的）记录数。
		record.set("totalPages", totalPages); // totalPages是若返回所有记录应该显示的页数。
	}

	/**
	 * 获取表中所有未标记删除的记录数
	 * 
	 * @author Johnson
	 * @param freezed
	 * @param type
	 * @create 2017年2月20日 下午4:19:20
	 * @update
	 * @param
	 * @return Number
	 */
	public Number statecount(String whereSQL) throws Exception {
		String sql = "SELECT COUNT(ID) FROM " + T_Strategy.tableName + whereSQL;
		return Db.queryNumber(sql);
	}

	/**
	 * 业务包绑定计费策略(批量）
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 14:33:11
	 * @update
	 * @param
	 * @return boolean
	 */
	@Override
	public boolean setsubstategy(String param) throws Exception {
		final JSONArray JO = JSONArray.fromObject(param);

		boolean success = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {

					for (Object o : JO) {
						JSONObject parmData = (JSONObject) o;
						String subsGUID = (String) parmData.get("subsGUID");
						JSONArray parmDataArray = JSONArray.fromObject(parmData.get("strategyGUID"));
						for (Object parao : parmDataArray) {
							// 检查是否重复绑定计费策略
							Kv cond1 = Kv.by("tablename", T_Subscriberpackage_strategy.tableName)
									.set("FSubguid", subsGUID).set("FStrategyguid", parao);
							SqlPara sqlPara1 = Db.getSqlPara("stategy.setsubstategycount", cond1);
							List<Record> state = Db.find(sqlPara1);
							// 说明有重复绑定了直接跳出
							if (state.size() > 0) {
								continue;
							}
							// 綁定計費策略的同時計算生命周期,和計費周期
							Kv cond2 = Kv.by("tablename", T_SubscriberPackage.tableName).set("SubGUID", subsGUID);
							SqlPara subPara = Db.getSqlPara("stategy.setsubstategy1", cond2);
							Record record = Db.findFirst(subPara);
							// 获取周期个数
							SqlPara stategyPara1 = Db.getSqlPara("stategy.setsubstategy3",
									Kv.by("tablename", T_Strategy.tableName).set("FStrategyguid", parao));
							String Cyclenumber = Db.findFirst(stategyPara1).get("FLifenumber");
							int FLifecycle = Integer.parseInt(record.getStr("FCycle")) * Integer.parseInt(Cyclenumber);// 計費周期
							Kv cond3 = Kv.by("tablename", T_Strategy.tableName)
									.set("FEfectivetime", record.get("FEfectivetime")).set("FLifecycle", FLifecycle)
									.set("FStrategyguid", parao)
									.set("FAvailabilityTime", record.get("FAvailabilityTime"));
							SqlPara stategyPara = Db.getSqlPara("stategy.setsubstategy2", cond3);
							result = Db.update(stategyPara.getSql()) > 0;
							// 修改對應策略包的生命周期和計費周期
							Kv cond = Kv.by("tablename", T_Subscriberpackage_strategy.tableName)
									.set("FSubguid", "'" + subsGUID + "'").set("FStrategyguid", "'" + parao + "'")
									.set("FCrecatetime", "'" + TimeUT.getCurrTime() + "'")
									.set("FAvailabilityTime", "'" + TimeUT.getCurrTime() + "'");

							SqlPara sqlPara = Db.getSqlPara("stategy.setsubstategy", cond);

							result = Db.update(sqlPara.getSql()) > 0;

						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				return result;
			}
		});

		// TODO Auto-generated method stub
		return success;
	}

	/**
	 * 计费策略绑定业务包(批量）
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 14:33:06
	 * @update
	 * @param
	 * @return boolean
	 */

	@Override
	public boolean setstategysub(String param) throws Exception {

		final JSONArray JO = JSONArray.fromObject(param);
		boolean success = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : JO) {
						JSONObject parmdata = (JSONObject) o;
						String strategyGUID = (String) parmdata.get("strategyGUID");
						String subsGUID = (String) parmdata.get("subsGUID");
						Kv cond1 = Kv.by("tablename", T_Subscriberpackage_strategy.tableName).set("FSubguid", subsGUID)
								.set("FStrategyguid", strategyGUID);
						SqlPara sqlPara1 = Db.getSqlPara("stategy.setsubstategycount", cond1);
						List<Record> state = Db.find(sqlPara1);
						// 说明有重复绑定了直接跳出
						if (state.size() > 0) {
							continue;
						}
						// 查看
						// 綁定計費策略的同時計算生命周期,和計費周期
						Kv cond2 = Kv.by("tablename", T_SubscriberPackage.tableName).set("SubGUID", subsGUID);
						SqlPara subPara = Db.getSqlPara("stategy.setsubstategy1", cond2);
						Record record = Db.findFirst(subPara);
						// 获取周期个数
						SqlPara stategyPara1 = Db.getSqlPara("stategy.setsubstategy3",
								Kv.by("tablename", T_Strategy.tableName).set("FStrategyguid", strategyGUID));
						String Cyclenumber = Db.findFirst(stategyPara1).get("FLifenumber");
						int FLifecycle = Integer.parseInt(record.getStr("FCycle")) * Integer.parseInt(Cyclenumber);// 計費周期
						// 否则绑定
						Kv cond3 = Kv.by("tablename", T_Strategy.tableName)
								.set("FEfectivetime", record.get("FEfectivetime")).set("FLifecycle", FLifecycle)
								.set("FStrategyguid", strategyGUID)
								.set("FAvailabilityTime", record.get("FAvailabilityTime"));
						SqlPara stategyPara = Db.getSqlPara("stategy.setsubstategy2", cond3);
						result = Db.update(stategyPara.getSql()) > 0;
						// 修改對應策略包的生命周期和計費周期
						Kv cond = Kv.by("tablename", T_Subscriberpackage_strategy.tableName)
								.set("FSubguid", "'" + subsGUID + "'").set("FStrategyguid", "'" + strategyGUID + "'")
								.set("FCrecatetime", "'" + TimeUT.getCurrTime() + "'")
								.set("FAvailabilityTime", "'" + TimeUT.getCurrTime() + "'");

						SqlPara sqlPara = Db.getSqlPara("stategy.setsubstategy", cond);

						result = Db.update(sqlPara.getSql()) > 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				// TODO Auto-generated method stub
				return result;
			}
		});

		return success;
	}

	/**
	 * 业务包和计费策略的解除(批量）
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 14:37:12
	 * @update
	 * @param
	 * @return boolean
	 */

	@Override
	public List<Record> deletesubstategy(String param, final String Grouptype) throws Exception {
		final JSONArray JO = JSONArray.fromObject(param);
		final List<Record> listdata = new ArrayList<>();
		Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : JO) {
						Record record = new Record();
						JSONObject parmdata = (JSONObject) o;
						String strategyGUID = (String) parmdata.get("strategyGUID");
						String subsGUID = (String) parmdata.get("subsGUID");
						Kv cond = Kv.by("tablename", T_Subscriberpackage_strategy.tableName).set("FSubGUID", subsGUID)
								.set("FStrategyguid", strategyGUID);
						if (Grouptype != null && Grouptype.equals("true")) {
							if (Integer
									.parseInt(Db
											.findFirst(
													Db.getSqlPara("stategy.tdeletestategysub",
															Kv.by("tablename", tU_SPTableName)
																	.set("FSubscriberPackageGUID", subsGUID)))
											.get("num").toString()) > 0) {
								// 说明有用户正在开通着跳过
								record.set("strategyGUID", strategyGUID).set("state", "该策略包绑定的业务包已被用户开通并且处于有效期，无法解除绑定");
								listdata.add(record);
								continue;

							}
						} else {
							if (Integer
									.parseInt(Db
											.findFirst(
													Db.getSqlPara("stategy.tdeletestategysub",
															Kv.by("tablename", tU_SPTableName)
																	.set("FSubscriberPackageGUID", strategyGUID)))
											.get("num").toString()) > 0) {
								// 说明有用户正在开通着跳过
								record.set("subsGUID", subsGUID).set("state", "该业务包绑定的策略包已被用户开通并且处于有效期，无法解除绑定");
								listdata.add(record);
								continue;

							}
						}

						SqlPara sqlPara = Db.getSqlPara("stategy.deletesubstategy", cond);
						result = Db.update(sqlPara.getSql()) > 0 && Db.update(Db
								.getSqlPara("stategy.updatedelete", Kv.by("tablename", T_Strategy.tableName)
										.set("FStrategyguid", strategyGUID).set("FUpdateTime", TimeUT.getCurrTime()))
								.getSql()) > 0;
						;

					}
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				// TODO Auto-generated method stub
				return result;
			}
		});

		return listdata;
	}

	/**
	 * 计费策略和业务包的绑定解除(批量）
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 14:37:12
	 * @update
	 * @param
	 * @return boolean
	 */

	@Override
	public boolean deletestategysub(String param) throws Exception {
		final JSONArray JO = JSONArray.fromObject(param);
		boolean success = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : JO) {
						JSONObject parmdata = (JSONObject) o;
						String strategyGUID = (String) parmdata.get("strategyGUID");
						Kv cond = Kv.by("tablename", T_Subscriberpackage_strategy.tableName).set("FStrategyguid",
								strategyGUID);
						SqlPara sqlPara = Db.getSqlPara("stategy.deletestategysub", cond);
						// 检查当前解绑的业务包是否已经被用户开通，如果被开通则无法解除绑定

						result = Db.update(sqlPara.getSql()) > 0 && Db.update(Db
								.getSqlPara("stategy.updatedelete", Kv.by("tablename", T_Strategy.tableName)
										.set("FStrategyguid", strategyGUID).set("FUpdateTime", TimeUT.getCurrTime()))
								.getSql()) > 0;

					}
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				// TODO Auto-generated method stub
				return result;
			}
		});

		return success;
	}

	/**
	 * 查看指定业务包已开通计费策略
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 15:22:32
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record selectSubstrategy(String param) throws Exception {
		Record record = new Record();
		try {

			JSONObject paramData = JSONObject.fromObject(param);
			// 业务包的GUID
			String subsGUID = (String) paramData.get("subsGUID");
			Integer pageSize = StringUtils.hasText((String) paramData.get("pageSize"))
					? Integer.parseInt((String) paramData.get("pageSize")) : null;
			Integer page = StringUtils.hasText((String) paramData.get("page"))
					? Integer.parseInt((String) paramData.get("page")) : null;
			// 查询该个业务包已开通的计费策略
			Kv cond = Kv.by("tablename1", T_Strategy.tableName).set("tablename", T_Subscriberpackage_strategy.tableName)
					.set("FSubGUID", subsGUID);
			SqlPara sqlPara = Db.getSqlPara("stategy.selectSubstrategy", cond);
			Page<Record> date = Db.paginate(page, pageSize, sqlPara);
			record.set("Substrategy", date);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;
	}

	/**
	 * 查看指定业务包未开通计费策略
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 16:16:49
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record selectunSubstrategy(String param) throws Exception {
		Record record = new Record();
		try {
			JSONObject paramData = JSONObject.fromObject(param);
			String subsGUID = (String) paramData.get("subsGUID");
			Integer pageSize = StringUtils.hasText((String) paramData.get("pageSize"))
					? Integer.parseInt((String) paramData.get("pageSize")) : null;
			Integer page = StringUtils.hasText((String) paramData.get("page"))
					? Integer.parseInt((String) paramData.get("page")) : null;
			Kv cond = Kv.by("tablename1", T_Strategy.tableName).set("tablename", T_Subscriberpackage_strategy.tableName)
					.set("FSubGUID", subsGUID);
			SqlPara sqlPara = Db.getSqlPara("stategy.selectunSubstrategy", cond);

			// 获取到当前业务包未开通的计费策略
			Page<Record> date = Db.paginate(page, pageSize, sqlPara);
			// 获取未开通并且没有被其他业务包开通的计费策略

			record.set("unSubstrategy", date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;
	}

	/**
	 * 新增计费策略
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 16:17:15
	 * @update
	 * @param
	 * @return boolean
	 */

	@Override
	public Record addstrategy(String param) throws Exception {
		Record staterecord = new Record();
		try {

			JSONObject paramData = JSONObject.fromObject(param);
			String FName = (String) paramData.get("FName");// 策略名称
			String FProductID = (String) paramData.get("FProductID");// 产品ID
			String FPrice = (String) paramData.get("FPrice");// 价格
			String FLifenumber = (String) paramData.get("FLifenumber");// 周期个数
			// 验证产品ID 是否已存在，产品ID 是唯一标识 不可重复
			if (Integer.parseInt(Db
					.findFirst(Db.getSqlPara("subscriberpackage.addsubscriberpackage",
							Kv.by("tablename", T_Strategy.tableName).set("FProductID", FProductID)))
					.get("num").toString()) > 0) {
				// 产品ID已存在，返回添加失败状态
				staterecord.set("state", "产品ID已存在添加失败");
				return staterecord;
			}
			;
			UUID uuid = UUID.randomUUID();
			Kv cond = Kv.by("tablename", T_Strategy.tableName).set("FGUID", uuid).set("FName", FName)
					.set("FProductID", FProductID).set("FPrice", FPrice).set("FLifenumber", FLifenumber)
					.set("FCreateTime", TimeUT.getCurrTime());
			SqlPara sqlPara = Db.getSqlPara("stategy.addstrategy", cond);
			if (Db.update(sqlPara.getSql()) > 0) {
				staterecord.set("state", "操作成功");
				return staterecord;
			}
			;
		} catch (Exception e) {
			e.printStackTrace();

		}
		staterecord.set("state", "操作失败");
		return staterecord;
	}

	/**
	 * 计费策略的编辑(批量)
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 17:25:34 *
	 * @param
	 * @return boolean
	 */
	@Override
	public boolean updatestrategy(String param) throws Exception {
		final JSONArray jsonArray = JSONArray.fromObject(param);

		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				for (Object o : jsonArray) {
					JSONObject paramData = (JSONObject) o;
					String FStrategyguid = (String) paramData.get("FStrategyguid");
					if (StringUtils.hasText((String) paramData.get("FDelete"))) {
						// 如果传了这个参数，代表想做删除操作 删除计费策略，并解除已删除计费策略和业务包的关联关系
						// 并且把计费周期重置
						result = Db
								.update(Db
										.getSqlPara("stategy.updatedeletestrategy",
												Kv.by("tablename", T_Strategy.tableName)
														.set("FStrategyguid", FStrategyguid))
										.getSql()) > 0
								&& Db.update(
										Db.getSqlPara("stategy.updatedeletesubstrategy",
												Kv.by("tablename", T_Subscriberpackage_strategy.tableName)
														.set("FStrategyguid", FStrategyguid))
												.getSql()) >= 0
								&& Db.update(Db.getSqlPara("stategy.updatedelete",
										Kv.by("tablename", T_Strategy.tableName).set("FStrategyguid", FStrategyguid)
												.set("FUpdateTime", TimeUT.getCurrTime()))
										.getSql()) >= 0;

						continue;
					}
					List<String> strategylist = new ArrayList<>();
					strategylist.add("FFreezed");
					strategylist.add("FName");
					strategylist.add("FProductID");
					strategylist.add("FCharging");
					strategylist.add("FAvailabilityTime");
					strategylist.add("FLifenumber");
					strategylist.add("FPrice");
					strategylist.add("FEfectivetime");
					String sql = "";
					for (String strategy : strategylist) {
						if (StringUtils.hasText((String) paramData.get(strategy))) {
							sql += strategy + "='" + (String) paramData.get(strategy) + "',";
						}
					}

					result = Db.update(Db.getSqlPara("stategy.updatestrategy",
							Kv.by("tablename", T_Strategy.tableName).set("Param", sql)
									.set("FUpdateTime", TimeUT.getCurrTime()).set("FStrategyguid", FStrategyguid))
							.getSql()) > 0;
				}

				return result;
			}
		});

		return success;
	}

	/**
	 * 查看指定策略包绑定的业务包的信息或未绑定的业务包信息
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 17:25:34 *
	 * @param
	 * @return Record
	 */

	@Override
	public Record strategysub(String param) throws Exception {
		Record record = new Record();
		List<Record> datas = new ArrayList<>();
		JSONObject paramData = JSONObject.fromObject(param);

		String Identification = (String) paramData.get("Identification");
		String FStrategyguid = (String) paramData.get("FStrategyguid");
		try {
			if (Identification.equals("true")) {
				// 查询计费策略开通业务包的信息

				Kv cond = Kv.by("tablename", T_Subscriberpackage_strategy.tableName)
						.set("tablename1", T_SubscriberPackage.tableName).set("FStrategyguid", FStrategyguid);
				SqlPara sqlPara = Db.getSqlPara("stategy.strategysub", cond);

				// 获取到当前业务包未开通的计费策略
				if (Db.findFirst(sqlPara) != null) {
					datas.add(Db.findFirst(sqlPara));
				}
				record.set("strategysub", datas);

			} else if ((Identification.equals("false"))) {
				// 查询计费策略未开通的业务包信息
				Integer pageSize = StringUtils.hasText((String) paramData.get("pageSize"))
						? Integer.parseInt((String) paramData.get("pageSize")) : null;
				Integer page = StringUtils.hasText((String) paramData.get("page"))
						? Integer.parseInt((String) paramData.get("page")) : null;
				Kv cond = Kv.by("tablename", T_SubscriberPackage.tableName)
						.set("tablename1", T_Subscriberpackage_strategy.tableName).set("FStrategyguid", FStrategyguid)
						.set("newTIME", TimeUT.getCurrTime());
				SqlPara sqlPara = Db.getSqlPara("stategy.unstrategysub", cond);
				Page<Record> data = Db.paginate(page, pageSize, sqlPara);

				record.set("strategysub", data);
			}
			// 获取未开通并且没有被其他业务包开通的计费策略

		} catch (Exception e) {
			e.printStackTrace();
		}

		return record;
	}

	/**
	 * 分页获取所有的可以使用的业务包和策略包
	 * 
	 * @author ZKill
	 * @param set
	 * @param type
	 * @create 2017年7月13日 17:25:34 *
	 * @param
	 * @return Record
	 */

	@Override
	public Record getsubstr(String param) throws Exception {
		Record record = new Record();
		JSONObject paramData = JSONObject.fromObject(param);
		Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
		if (pageSize == null && page == null) {
			List<Record> pagerecord = Db.find(Db.getSqlPara("SubcriberPackage.getsubstr",
					Kv.by("tablename", T_SubscriberPackage.tableName).set("tablename1", T_Strategy.tableName)));
			record.set("substrlist", pagerecord);
		} else {
			Page<Record> pagerecord = Db.paginate(page, pageSize, Db.getSqlPara("SubcriberPackage.getsubstr",
					Kv.by("tablename", T_SubscriberPackage.tableName).set("tablename1", T_Strategy.tableName)));
			record.set("substrlist", pagerecord);
		}

		return record;
	}

	public List<Record> getsub(String subid) throws Exception {

		return Db.find("SELECT * FROM " + T_SubscriberPackage.tableName + " WHERE FGUID in(" + subid + ")");

	}

	@Override
	public boolean updatesub(String sub) throws Exception {
		
		return Db.update("UPDATE " +T_SubscriberPackage.tableName+" SET  FFreezed=0 WHERE FGUID='"+sub+"'")>0;
	}

	@Override
	public List<Record> getSystemtermuser() throws Exception {
		return Db.find("SELECT * FROM " + T_System_termuser.tableName);
	}

}
