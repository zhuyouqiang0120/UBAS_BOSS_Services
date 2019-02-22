/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS-SERVICE
 **    Package Name : com.zens.ubasservice.impl								 
 **    Type    Name : TestImpl 							     	
 **    Create  Time : 2016年9月19日 上午10:45:28								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.serviceimpl;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;

import com.caucho.hessian.server.HessianServlet;
import com.chasonx.tools.Des3;
import com.chasonx.tools.StringUtils;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.template.stat.ast.Return;
import com.mchange.v2.sql.filter.RecreatePackage;
import com.oreilly.servlet.multipart.ExceededSizeException;
import com.sun.jmx.snmp.Timestamp;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.Constant;
import com.zens.ubasbossservices.common.Message;
import com.zens.ubasbossservices.dao.SubscriberPackageDao;
import com.zens.ubasbossservices.dao.TermInfoDao;
import com.zens.ubasbossservices.dao.TermUserDao;
import com.zens.ubasbossservices.dao.TermUserRegionDao;
import com.zens.ubasbossservices.dao.TermUserSubscriberPackageDao;
import com.zens.ubasbossservices.dao.TermUserTermInfoDao;
import com.zens.ubasbossservices.entity.T_Account;
import com.zens.ubasbossservices.entity.T_Authentication_log;
import com.zens.ubasbossservices.entity.T_Order_log;
import com.zens.ubasbossservices.entity.T_Strategy;
import com.zens.ubasbossservices.entity.T_SubscriberPackage;
import com.zens.ubasbossservices.entity.T_System_termuser;
import com.zens.ubasbossservices.entity.T_TermInfo;
import com.zens.ubasbossservices.entity.T_TermUser;
import com.zens.ubasbossservices.entity.T_TermUserGroup;
import com.zens.ubasbossservices.entity.T_TermUserRegion;
import com.zens.ubasbossservices.entity.T_TermUserSubscriberPackage;
import com.zens.ubasbossservices.entity.T_TermUserTermInfo;
import com.zens.ubasbossservices.entity.T_termuser_appuser;
import com.zens.ubasbossservices.interceptor.MyExceptionInterceptor;
import com.zens.ubasbossservices.log.demanDcertificationlog;
import com.zens.ubasbossservices.service.CacheService;
import com.zens.ubasbossservices.service.T_TermUserService;
import com.zens.ubasbossservices.utils.TimeUT;

import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.text.format.Time;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import sun.net.www.content.text.plain;

import com.zens.ubasbossservices.utils.AppUTil;
import com.zens.ubasbossservices.utils.RecordUtil;
import com.zens.ubasbossservices.utils.SQLUtil;

/**
 * 注册用户相关服务实现类（注册用户和终端用户是一个概念）
 * 
 * @author zyq
 * @e-mail zhuyq@zensvision.com
 * @date 2016年12月16日 下午12:45:10
 */
public class T_TermUserServiceImpl extends HessianServlet implements T_TermUserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final T_TermUserServiceImpl termUserServiceImpl = new T_TermUserServiceImpl();
	String cache1Name = Constant.CACHE_NAME.UBAS_BOSS_SERVICES_CACHE1.toString();
	String cache2Name = Constant.CACHE_NAME.UBAS_BOSS_SERVICES_CACHE2.toString();
	String termUserTableName = TableMapping.me().getTable(T_TermUser.class).getName();
	String terminfotableName = TableMapping.me().getTable(T_TermInfo.class).getName();
	String AccounttableName = TableMapping.me().getTable(T_Account.class).getName();
	String termUserInfotableName = TableMapping.me().getTable(T_TermUserTermInfo.class).getName();
	String termUserRegionTableName = TableMapping.me().getTable(T_TermUserRegion.class).getName();
	String termUserGroupTableName = TableMapping.me().getTable(T_TermUserGroup.class).getName();
	String tU_SPTableName = TableMapping.me().getTable(T_TermUserSubscriberPackage.class).getName();
	String subscriberPackageTableName = TableMapping.me().getTable(T_SubscriberPackage.class).getName();
	String terminfouserTableName = TableMapping.me().getTable(T_TermUserTermInfo.class).getName();
	String associatedGUID;
	String hasAssociated;
	String termUserSelectSQL = "SELECT ID AS id,FGUID AS GUID,FType AS type,FBossID AS bossID,FBossArea AS bossArea,FBossCity AS bossCity,FName AS name,FIdCard AS idCard,FSex AS sex,FAge AS age,FBirthday AS birthday,FAddr AS addr,FIphone AS iphone,FMobile AS mobile,FEmail AS email,FLevel AS level,FIntergration AS intergration,FCreateTime AS createTime,FCreateUserID AS createUserID,FUpdateTime AS updateTime,FUpdateUserID AS updateUserID,FFreezed AS freezed,FDeleted AS deleted,FExt AS ext,FHasRegion AS hasRegion,FHasGroup AS hasGroup,FAccountbalance AS accountbalance  FROM     "
			+ termUserTableName;
	String termUserSQL = "SELECT ID AS id,FGUID AS GUID,FType AS type,FName AS name,FSex AS sex,FAge AS age,FIphone AS iphone,FMobile AS mobile,FCreateTime AS createTime,FCreateUserID AS createUserID,FUpdateTime AS updateTime,FUpdateUserID AS updateUserID,FFreezed AS freezed,FDeleted AS deleted,FExt AS ext,FPrisonumber AS prisonumber,FPrisonernumber AS prisonernumber,FPrisonerlevel AS  prisonerlevel, FJailtime AS jailtime,FReleasetime AS  releasetime,FPrisonstatus AS prisonstatus,FMac AS mac,FUsertestmark AS usertestmark   "
			+ ",FExName AS exname,FDob AS dob,FHeight AS height,FCOP AS cop,FCOH AS coh,FMajorLang AS majorlang,FMinorLang AS minorlang,FStdPicURL AS stdpicurl,FFpPicURL AS fppicurl,FPidSN AS pidsn,FPidPOVBegin AS pidpovbegin,FPidPOVEnd AS pidpovend,FProvince AS province,FCity AS city,FPidAddr AS pidaddr,FPrisonGUID AS prisonguid,FCellGUID AS cellguid,FCrimeCauses AS crimecauses,FOutprisonTime AS outprisontime,FPrisonname AS prisonname,FCellname AS cellname,FRegionname AS regionname,FStdPicURL AS stdpicurl,FFpPicURL  AS fppicurl FROM  t_zexamtermuser  ";
	TermUserDao termUserDao = new T_TermUser();
	SubscriberPackageDao subscriberPackageDao = new T_SubscriberPackage();
	TermUserSubscriberPackageDao termUserSubscriberPackageDao = new T_TermUserSubscriberPackage();
	TermUserRegionDao termUserRegionDao = new T_TermUserRegion();
	TermInfoDao termInfoDao = new T_TermInfo();
	TermUserTermInfoDao termUserTermInfoDao = new T_TermUserTermInfo();

	CacheService cacheService = new CacheServiceImpl();

	/*
	 * 添加一个终端用户
	 * 
	 * @see com.zens.ubasboss.services.T_TermUserService#addTermUser(com.jfinal.
	 * plugin.activerecord.Record)
	 */
	@Override
	public Record add(String param) throws Exception {
		Record record = new Record();
		UUID uuid = UUID.randomUUID();
		JSONObject paramData = JSONObject.fromObject(param);
		// 添加用户之前先验证添加的设备信息是否已经存在
		if (Integer
				.parseInt(Db
						.findFirst(
								Db.getSqlPara("termuser.adduserterminfo",
										Kv.by("tablename", terminfotableName).set("terminfokey", "FMac")
												.set("userterminfo", paramData.get("mac"))))
						.get("num").toString()) > 0) {
			// 说明已存在该mac地址的设备
			record.set("FMacstate", "用户关联的设备MAC地址已存在,请重新填写");
		}
		if (Integer
				.parseInt(Db
						.findFirst(
								Db.getSqlPara("termuser.adduserterminfo",
										Kv.by("tablename", terminfotableName).set("terminfokey", "FCacard")
												.set("userterminfo", paramData.get("cacard"))))
						.get("num").toString()) > 0) {
			record.set("FCacardstate", "用户关联的设备CA卡号已存在,请重新填写");
		}
		if (Integer
				.parseInt(Db
						.findFirst(
								Db.getSqlPara("termuser.adduserterminfo",
										Kv.by("tablename", terminfotableName).set("terminfokey", "FSerial")
												.set("userterminfo", paramData.get("serial"))))
						.get("num").toString()) > 0) {
			record.set("FSerialstate", "用户关联的设备串号已存在,请重新填写");
		}

		if (record.get("FMacstate") != null || record.get("FCacardstate") != null
				|| record.get("FSerialstate") != null) {
			return record;
		}
		System.out.println("解析通过");

		Record terminfouser = new Record();
		List<String> paramNames = new ArrayList<String>();
		paramNames.add("type");
		paramNames.add("bossID");
		paramNames.add("bossArea");
		paramNames.add("bossCity");
		paramNames.add("name");
		paramNames.add("idCard");
		paramNames.add("sex");
		paramNames.add("age");
		paramNames.add("birthday");
		paramNames.add("addr");
		paramNames.add("iphone");
		paramNames.add("mobile");
		paramNames.add("email");
		paramNames.add("level");
		paramNames.add("intergration");
		paramNames.add("createUserID");
		paramNames.add("ext");
		paramNames.add("mac");
		paramNames.add("userregion");
		paramNames.add("accountbalance");
		// paramNames.add("")
		RecordUtil.setRecord(paramNames, paramData, record);
		record.set("FGUID", uuid.toString()).set("FCreateTime", TimeUT.getCurrTime()).set("FHasregion", "-1")
				.set("FHasgroup", "-1");
		Db.save(termUserTableName, record);
		// 新增用户的同时新增设备信息并获取到刚刚新增设备的GUID
		String terminfoGUID = addterminfo(param);
		// 用户关联设备
		terminfouser.set("FSubsGUID", uuid.toString()).set("FTermInfoID", terminfoGUID).set("FCreateTime",
				TimeUT.getCurrTime());
		// 新增用户的时候新增一个账户
		Db.update(Db.getSqlPara("termuser.addaccount", Kv.by("tablename", T_Account.tableName)
				.set("FUserGUID", uuid.toString()).set("FCreatetime", TimeUT.getCurrTime())).getSql());

		Db.save(terminfouserTableName, terminfouser);
		return record;

	}

	// 用户关联设备用方法
	public String addterminfo(String param) throws Exception {
		UUID uuid = UUID.randomUUID();
		JSONObject paramData = JSONObject.fromObject(param);
		Record record = new Record();
		List<String> paramNames = new ArrayList<String>();
		paramNames.add("mac");
		paramNames.add("ip");
		paramNames.add("serial");
		paramNames.add("cacard");
		paramNames.add("regionGUID");
		paramNames.add("cellGUID");
		paramNames.add("prisonGUID");
		paramNames.add("regionname");
		paramNames.add("cellname");
		paramNames.add("prisonname");
		paramNames.add("ext");
		RecordUtil.setRecord(paramNames, paramData, record);
		record.set("FGUID", uuid.toString()).set("FCreateTime", TimeUT.getCurrTime()).set("FType", "ex");
		Db.save(terminfotableName, record);
		return uuid.toString();
	}

	/**
	 * 获取T_TERMUSER表中所有未标记删除的记录数
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
		String sql = "SELECT COUNT(ID) FROM  t_termuser " + whereSQL;
		return Db.queryNumber(sql);
	}

	public Number countZE(String whereSQL) throws Exception {
		String sql = "SELECT COUNT(ID) FROM  t_zexamtermuser " + whereSQL;
		return Db.use("dPlugin3").queryNumber(sql);
	}

	/**
	 * 根据keyword拼出（用于筛选的）SQL片段。
	 * 
	 * @author Johnson
	 * @create 2017年2月22日 上午10:40:20
	 * @update
	 * @param
	 * @return List<Record>
	 */
	public String getSQLByKeyword(String keyword) throws Exception {
		if (keyword.equals("vip")) {
			keyword = "3";
			StringBuffer sql = new StringBuffer(" AND (FType LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		}
		if (keyword.equals("个人")) {
			keyword = "2";
			StringBuffer sql = new StringBuffer(" AND (FType LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		}
		if (keyword.equals("集团")) {
			keyword = "1";
			StringBuffer sql = new StringBuffer(" AND (FType LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		}

		if (keyword.equals("正常")) {
			keyword = "0";
			StringBuffer sql = new StringBuffer(" AND (FFreezed LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		}
		if (keyword.equals("冻结")) { 
			keyword = "1";
			StringBuffer sql = new StringBuffer(" AND (FFreezed LIKE'%");
			sql.append(keyword);
			sql.append("%')");
			return sql.toString();
		}
//OR FGUID  =(SELECT tuser.FSubsGUID FROM t_terminfo tinfo, t_termuser_terminfo tuser  WHERE tinfo.FGUID = tuser.FTermInfoID AND  tinfo.FCaCard LIKE '%岩温帝%')
		StringBuffer sql = new StringBuffer(" AND (FBossArea LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FName LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FAddr LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FIphone LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FGUID = (SELECT tuser.FSubsGUID FROM t_terminfo tinfo, t_termuser_terminfo tuser  WHERE tinfo.FGUID = tuser.FTermInfoID AND  tinfo.FCaCard LIKE'%");//增加CA卡号查询
		sql.append(keyword);
		sql.append("%'))");
		//sql.append("%')");
		return sql.toString();   
	}

	public String getZESQLByKeyword(String keyword) throws Exception {
		StringBuffer sql = new StringBuffer(" AND (FGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FType LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FName LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FSex LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FAge LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FIphone LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FMobile LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FEmail LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCreateTime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCreateUserID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FUpdateTime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrisonernumber LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrisonumber LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrisonerlevel LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FJailtime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FReleasetime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FUsertestmark LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrisonstatus LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FMac LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FExName LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FDob LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FHeight LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCOP LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCOH LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FMajorLang LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FMinorLang LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FStdPicURL LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FFpPicURL LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPidSN LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPidPOVBegin LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPidPOVEnd LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FProvince LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCity LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPidAddr LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrisonGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCellGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FRegionGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCrimeCauses LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FOutprisonTime LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FCellName LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FRegionName  LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FPrisonName LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FUpdateUserID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR FExt LIKE'%");
		sql.append(keyword);
		sql.append("%')");
		return sql.toString();

	}

	/**
	 * 遍历注册用户记录，为每条记录加上与它关联的区域记录和分组记录。
	 * 
	 * @author Johnson
	 * @create 2017年2月14日 下午5:05:49
	 * @update
	 * @param
	 * @return void
	 */
	public void addRegionsAndGroup(List<Record> list) throws Exception {
		// 判断当前应用中的缓存是否为空，如果为空，更新缓存
		cacheService.checkAndUpdateCache();
		for (int i = 0; i < list.size(); i++) {
			Record termUser = list.get(i);
			// 为终端用户添加与他关联的所有区域记录。
			List<Record> regions = addRegionsForTermUser(termUser);
			// 为终端用户添加与他关联的所有设备信息
			Record terminfo = addterminForTerUser(termUser);
			if (terminfo != null && !terminfo.equals("")) {
				termUser.set("terminfo", terminfo);
			} else {
				termUser.set("terminfo", null);
			}
			if (regions.size() != 0) {
				termUser.set("regions", regions);
			} else {
				termUser.set("regions", null);
			}
			// 为终端用户添加与他关联的分组记录。
			Record group = addGroupForTermUser(termUser);
			termUser.set("group", group);
		}
	}

	/**
	 * 为终端用户添加与他关联的所有设备信息。
	 * 
	 * @author Johnson
	 * @create 2017年2月22日 上午9:44:04
	 * @update
	 * @param
	 * @return void
	 */
	public Record addterminForTerUser(Record termUser) throws Exception {
		// 去终端用户和区域的关联表中取出该终端用户所有记录。
		Record termUserRegions = Db.findFirst(
				"SELECT t.* FROM  t_terminfo t INNER JOIN t_termuser_terminfo f ON t.FGUID=f.FTermInfoID WHERE f.FSubsGUID='"
						+ termUser.getStr("GUID")
						+ "' AND t.FDeleted!=1 AND t.FFreezed!=1 AND f.FDeleted!=1 AND f.FFreezed!=1 ORDER BY t.ID DESC");

		return termUserRegions;
	}

	/**
	 * 为终端用户添加与他关联的所有区域记录。
	 * 
	 * @author Johnson
	 * @create 2017年2月22日 上午9:44:04
	 * @update
	 * @param
	 * @return void
	 */
	public List<Record> addRegionsForTermUser(Record termUser) throws Exception {
		// 去终端用户和区域的关联表中取出该终端用户所有记录。
		List<Record> termUserRegions = getTU_RsByFSubsGUID(termUser);
		return cacheService.getRegionsByRegionGUIDs(termUserRegions);
	}

	/**
	 * 为终端用户添加与他关联的分组记录。
	 * 
	 * @author Johnson
	 * @create 2017年2月22日 上午9:50:30
	 * @update
	 * @param
	 * @return void
	 */
	public Record addGroupForTermUser(Record termUser) throws Exception {
		// 去注册用户分组的关联表中取出该注册用户的记录
		Record termUser_Group = getTU_GByFSubsGUID(termUser);
		Record group = new Record();
		if (termUser_Group != null) {
			String groupGUID = termUser_Group.getStr("FGroupGUID");
			String cacheName = cacheService.getCacheName();
			group = CacheKit.get(cacheName, groupGUID);
		}
		return group;
	}

	/*
	 * 批量更新终端用户记录
	 * 
	 * @see
	 * com.zens.ubasboss.services.T_TermUserService#updateTermUsers(java.lang.
	 * String)
	 */
	@Override
	public List<Record> update(String param) throws Exception {
		final JSONArray paramData = JSONArray.fromObject(param);
		final List<Record> listrecord = new ArrayList<>();
		// 事务回滚
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (int i = 0; i < paramData.size(); i++) {
						Record record = new Record();
						String updateSQL = "";
						JSONObject jO = (JSONObject) paramData.get(i);
						String GUID = (String) jO.get("GUID");

						updateSQL = "UPDATE " + termUserTableName + " SET ";
						String termInfoSQL = "UPDATE " + terminfotableName + " SET ";
						String setSQL = getSetSQLForTermUser_Group(jO);
						String teminfoSQL = getSetSQLtermino(jO);
						termInfoSQL += teminfoSQL;
						updateSQL += setSQL;
						if (Integer.parseInt(Db
								.findFirst(Db.getSqlPara("termuser.updateuserterminfo",
										Kv.by("tablename", terminfotableName).set("terminfokey", "FMac")
												.set("userterminfo", jO.get("mac")).set("SubsGUID", GUID)))
								.get("num").toString()) > 0) {
							// 说明已存在该mac地址的设备
							record.set("FMacstate", "用户关联的设备MAC地址已存在,请重新填写");
						}
						if (Integer.parseInt(Db
								.findFirst(Db.getSqlPara("termuser.updateuserterminfo",
										Kv.by("tablename", terminfotableName).set("terminfokey", "FCacard")
												.set("userterminfo", jO.get("cacard")).set("SubsGUID", GUID)))
								.get("num").toString()) > 0) {
							record.set("FCacardstate", "用户关联的设备CA卡号已存在,请重新填写");
						}
						if (Integer.parseInt(Db
								.findFirst(Db.getSqlPara("termuser.updateuserterminfo",
										Kv.by("tablename", terminfotableName).set("terminfokey", "FSerial")
												.set("userterminfo", jO.get("serial")).set("SubsGUID", GUID)))
								.get("num").toString()) > 0) {
							record.set("FSerialstate", "用户关联的设备串号已存在,请重新填写");
						}

						if (record.get("FMacstate") != null || record.get("FCacardstate") != null
								|| record.get("FSerialstate") != null) {
							listrecord.add(record);

							continue;

						}
						if (!teminfoSQL.equals("") && setSQL.equals("")) {

							// 说明只是对用户设备的编辑
							termInfoSQL += " FUpdateTime='" + TimeUT.getCurrTime()
									+ "' WHERE FGUID=(SELECT FTermInfoID FROM t_termuser_terminfo WHERE FSubsGUID='"
									+ GUID + "')";

							result = updateTermUser(teminfoSQL) && result;
							// 編輯完成后獲取到所編輯設備的信息
							if (result) {

								record.set("teminfo",
										Db.findFirst(Db.getSqlPara("termuser.updatetermin",
												Kv.by("tablename", terminfotableName).set("FGUID",
														(Db.findFirst(
																"SELECT FTermInfoID FROM t_termuser_terminfo WHERE FSubsGUID='"
																		+ GUID + "'")).getStr("FTermInfoID")))));
								listrecord.add(record);
							}

						}
						if (!setSQL.equals("") && teminfoSQL.equals("")) {
							// 说明只是对用户的编辑
							updateSQL += " FUpdateTime='" + TimeUT.getCurrTime() + "' WHERE FGUID = '" + GUID + "' ";

							result = updateTermUser(updateSQL) && result;

							// 編輯完成后獲取到所編輯用戶的信息

							if (result) {
								record.set("termuser", Db.findFirst(Db.getSqlPara("termuser.updatetermuser",
										Kv.by("tablename", termUserTableName).set("FGUID", GUID))));

								listrecord.add(record);
							}

						}

						if (!teminfoSQL.equals("") && !setSQL.equals("")) {
							// 说明是对用户及其关联的设备同时编辑
							termInfoSQL += " FUpdateTime='" + TimeUT.getCurrTime()
									+ "' WHERE FGUID=(SELECT FTermInfoID FROM t_termuser_terminfo WHERE FSubsGUID='"
									+ GUID + "')";
							updateSQL += " FUpdateTime='" + TimeUT.getCurrTime() + "' WHERE FGUID = '" + GUID + "' ";

							result = updateTermUser(updateSQL) && updateTermUser(termInfoSQL) && result;
							if (result) {
								record.set("teminfo",
										Db.findFirst(Db.getSqlPara("termuser.updatetermin",
												Kv.by("tablename", terminfotableName).set("FGUID",
														(Db.findFirst(
																"SELECT FTermInfoID FROM t_termuser_terminfo WHERE FSubsGUID='"
																		+ GUID + "'")).getStr("FTermInfoID")))));

								record.set("termuser", Db.findFirst(Db.getSqlPara("termuser.updatetermuser",
										Kv.by("tablename", termUserTableName).set("FGUID", GUID))));
								listrecord.add(record);
							}

						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = false;
				}
				return result;
			}
		});
		return listrecord;
	}

	public boolean updateTermUser(String sql) throws Exception {
		return Db.update(sql) > 0;
	}

	public boolean updateZETermUser(String sql) throws Exception {
		return Db.use("dPlugin3").update(sql) > 0;
	}

	/**
	 * 组装出用于更新与用户关联的设备的SQL语句
	 * 
	 * @author Johnson
	 * @create 2017年2月10日 下午2:28:21
	 * @update
	 * @param
	 * @return String
	 */

	public String getSetSQLtermino(JSONObject jO) throws Exception {
		StringBuffer setSQL = new StringBuffer();
		String result = "";
		List<String> setNames = new ArrayList<String>();
		setNames.add("mac");
		setNames.add("ip");
		setNames.add("serial");
		setNames.add("cacard");
		setNames.add("regionGUID");
		setNames.add("freezed");
		setNames.add("deleted");
		setNames.add("ext");

		SQLUtil.addParamsToSetSQL(setSQL, jO, setNames);
		result += setSQL.toString();
		return result;
	}

	/**
	 * 组装出用于更新T_TERMUSER表的SQL语句的SET片段
	 * 
	 * @author Johnson
	 * @create 2017年2月10日 下午2:28:21
	 * @update
	 * @param
	 * @return String
	 */
	public String getSetSQLForTermUser_Group(JSONObject jO) throws Exception {
		StringBuffer setSQL = new StringBuffer();
		String result = "";
		List<String> setNames = new ArrayList<String>();

		setNames.add("bossID");
		setNames.add("bossArea");
		setNames.add("bossCity");
		setNames.add("name");
		setNames.add("idCard");
		setNames.add("sex");
		setNames.add("age");
		setNames.add("birthday");
		setNames.add("addr");
		setNames.add("iphone");
		setNames.add("mobile");
		setNames.add("email");
		setNames.add("level");
		setNames.add("intergration");
		setNames.add("createUserID");
		setNames.add("ext");
		setNames.add("prisonumber");
		setNames.add("prisonernumber");
		setNames.add("prisonerlevel");
		setNames.add("jailtime");
		setNames.add("releasetime");
		setNames.add("prisonstatus");
		setNames.add("mac");
		setNames.add("userregion");
		setNames.add("exName");
		setNames.add("dob");
		setNames.add("cop");
		setNames.add("coh");
		setNames.add("majorLang");
		setNames.add("minorLang");
		setNames.add("stdPicURL");
		setNames.add("fpPicURL");
		setNames.add("pidSN");
		setNames.add("pidPOVBegin");
		setNames.add("pidPOVEnd");
		setNames.add("province");
		setNames.add("city");
		setNames.add("usertestmark");
		setNames.add("pidAddr");
		setNames.add("prisonGUID");
		setNames.add("cellGUID");
		setNames.add("height");
		setNames.add("crimeCauses");
		setNames.add("outprisonTime");
		setNames.add("regionGUID");
		setNames.add("prisonname");
		setNames.add("cellname");
		setNames.add("regionname");
		setNames.add("freezed");
		setNames.add("accountbalance");

		SQLUtil.addParamsToSetSQL(setSQL, jO, setNames);

		result += setSQL.toString();
		return result;
	}

	/*
	 * 批量标记删除注册用户记录
	 * 
	 * @see
	 * com.zens.ubasbossservices.service.T_TermUserService#deleteTermUsers(java.
	 * lang.String)
	 */
	@Override
	public boolean delete(String param) throws Exception {
		JSONObject paramData = JSONObject.fromObject(param);
		final JSONArray GUIDs = paramData.getJSONArray("GUIDs");
		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				for (int i = 0; i < GUIDs.size(); i++) {
					try {
						String GUID = GUIDs.getString(i);
						result = (deleteTermUser(GUID) > 0) && result; // 删除用户
						result = (deleteTermInfo(GUID) > 0) && result; // 删除设备
						result = (deleteTermUserInfo(GUID) > 0) && result; // 删除设备
						result = (deleteAccount(GUID) > 0) && result; // 删除账户
						result = (deleteTermUserRegion(GUID) >= 0) && result; // 删除用户区域关联
						result = (deleteTermUserGroup(GUID) >= 0) && result; // 删除用户分组关联
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						result = false;
					}
				}
				return result;
			}
		});
		return success;
	}

	/**   
	 * 标记删除注册用户记录  
	 * 
	 * @author Johnson
	 * @create 2017年2月25日 下午1:07:36
	 * @update
	 * @param
	 * @return boolean
	 */
	public int deleteTermUser(String GUID) throws Exception {
		/*
		return Db.update(
				"update " + termUserTableName + " set FDeleted = 1 ,FHasRegion = -1,FHasGroup = -1,FUpdateTime = '"
						+ TimeUT.getCurrTime() + "'  where FGUID = '" + GUID + "' ");
		*/
		return Db.update(
				"delete from " + termUserTableName + " where FGUID = '" + GUID + "' ");

	}
	
	/**
	 * 标记删除注册设备记录
	 * 
	 * @author Johnson
	 * @create 2017年2月25日 下午1:07:36
	 * @update
	 * @param
	 * @return boolean
	 */
	public int deleteTermInfo(String GUID) throws Exception {
		/*
		return Db.update(
				"update " + termUserTableName + " set FDeleted = 1 ,FHasRegion = -1,FHasGroup = -1,FUpdateTime = '"
						+ TimeUT.getCurrTime() + "'  where FGUID = '" + GUID + "' ");
		*/
		List<Record> list = Db.find("SELECT * FROM " + termUserInfotableName + " WHERE FSubsGUID = '" + GUID + "'");
		
		return Db.update(
				"delete from " + terminfotableName + " where FGUID = '" + list.get(0).getStr("FTermInfoID") + "' ");

	}
	
	/**
	 * 标记删除注册用户、设备记录
	 * 
	 * @author Johnson
	 * @create 2017年2月25日 下午1:07:36
	 * @update
	 * @param
	 * @return boolean
	 */
	public int deleteTermUserInfo(String GUID) throws Exception {
		/*
		return Db.update(
				"update " + termUserTableName + " set FDeleted = 1 ,FHasRegion = -1,FHasGroup = -1,FUpdateTime = '"
						+ TimeUT.getCurrTime() + "'  where FGUID = '" + GUID + "' ");
		*/
		 
		return Db.update(
				"delete from " + termUserInfotableName + " where FSubsGUID = '" + GUID + "' ");

	}
	
	/**
	 * 标记删除账户
	 * 
	 * @author Johnson
	 * @create 2017年2月25日 下午1:07:36
	 * @update
	 * @param
	 * @return boolean
	 */
	public int deleteAccount(String GUID) throws Exception {
		/*
		return Db.update(
				"update " + termUserTableName + " set FDeleted = 1 ,FHasRegion = -1,FHasGroup = -1,FUpdateTime = '"
						+ TimeUT.getCurrTime() + "'  where FGUID = '" + GUID + "' ");
		*/ 
		
		return Db.update(
				"delete from " + AccounttableName + " where FUserGUID = '" + GUID + "' ");

	}

	public int ZEdeleteTermUser(String GUID) throws Exception {
		return Db.use("dPlugin3").update("update t_zexamtermuser set FDeleted = 1,FUpdateTime = '"
				+ TimeUT.getCurrTime() + "'  where FGUID = '" + GUID + "' ");

	}

	/**
	 * 标记删除注册用户区域关联记录
	 * 
	 * @author Johnson
	 * @create 2017年2月25日 下午1:07:50
	 * @update
	 * @param
	 * @return boolean
	 */
	public int deleteTermUserRegion(String GUID) throws Exception {
		/*
		return Db.update("update " + termUserRegionTableName + " set FDeleted = 1 , FUpdateTime = '"
				+ TimeUT.getCurrTime() + "'  where FSubsGUID = '" + GUID + "' ");
		*/
		return Db.update("delete from " + termUserRegionTableName + " where FSubsGUID = '" + GUID + "' ");
	}

	/**
	 * 标记删除注册用户分组关联记录
	 * 
	 * @author Johnson
	 * @create 2017年2月25日 下午1:08:09
	 * @update
	 * @param
	 * @return boolean
	 */
	public int deleteTermUserGroup(String GUID) throws Exception {
		/*
		return Db.update("update " + termUserGroupTableName + " set FDeleted = 1 , FUpdateTime = '"
				+ TimeUT.getCurrTime() + "'  where FSubsGUID = '" + GUID + "' ");
		*/
		return Db.update("delete from  " + termUserGroupTableName + " where FSubsGUID = '" + GUID + "' ");
	}

	@Override
	public boolean undelete(String param) throws Exception {
		boolean result = true;
		JSONObject paramData = JSONObject.fromObject(param);
		JSONArray GUIDsJA = (JSONArray) paramData.get("GUIDs");
		String GUIDs = GUIDsJA.toString();
		String ids = GUIDs.replace("[", "").replace("]", "");
		if (!ids.equals("")) {
			return Db.update("UPDATE " + termUserTableName + " SET FDeleted = 0 , FUpdateTime = '"
					+ TimeUT.getCurrTime() + "'  WHERE FGUID IN (" + ids + ")") > 0;
		}
		return result;
	}

	/*
	 * 根据相应条件获取终端用户所有信息
	 * 
	 * @see
	 * com.zens.ubasboss.services.T_TermUserService#getListUserByGUIDs(java.lang
	 * .String, int, int)
	 */
	@Override
	public Record get(String param, boolean byRegionGUID) throws Exception {
		Record record = new Record();
		JSONObject paramData = JSONObject.fromObject(param);
		List<Record> termUsers = null;

		Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
		String keyword = (String) paramData.get("keyword");
		String type = (String) paramData.get("type");
		String freezed = (String) paramData.get("freezed");
		String deleted = (String) paramData.get("deleted");
		String hasRegion = (String) paramData.get("hasRegion");

		if (!StringUtils.hasText(deleted)) {
			deleted = "0";
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("FType", type);
		paramMap.put("FFreezed", freezed);
		paramMap.put("FHasRegion", hasRegion);
		String whereSQL = SQLUtil.getWhereSql(paramMap);

		if (StringUtils.hasText(keyword)) {
			String keywordSQL = getSQLByKeyword(keyword);
			whereSQL += keywordSQL;
		}

		if (byRegionGUID == true) {
			String regionGUID = (String) paramData.get("regionGUID");
			String getByRegionSQL = getSQLByRegionGUID(regionGUID);
			// if (getByRegionSQL.equals("")) {
			// return record;
			// }
			whereSQL += getByRegionSQL;
		}
		System.out.println(whereSQL);

		String deletedSQL = " AND FDeleted = '" + deleted + "' ";
		whereSQL += deletedSQL;

		String limitSQL = SQLUtil.getLimitSQL(pageSize, page);
		String sql = termUserSelectSQL + whereSQL + " ORDER BY ID DESC " + limitSQL;
		System.out.println(sql);
		termUsers = Db.find(sql);
		// 为每个注册用户加上他的区域和分组信息。
		addRegionsAndGroup(termUsers);
		record.set("termUsers", termUsers);
		// 获取前端分页所需参数
		getParamForLimitPage(record, whereSQL, pageSize);

		return record;

	}

	public static void main(String[] args) throws Exception {
		try {
			String str = "000000";
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			System.out.println(new BigInteger(1, md.digest()).toString(16));
		} catch (Exception e) {
			// throw new SpeedException("MD5加密出现错误");
		}
	}

	/********************************************************************************************************************************************************
	 * 注册用户和区域关联
	 **************************************************************************************************************************************************************/

	/**
	 * 根据区域GUID查询T_TermUser_Region表中与它相关的记录,将这些记录的FSubsGUID拼成字符串（用逗号隔开），并返回。
	 * 
	 * @author Johnson
	 * @create 2017年2月20日 下午5:18:04
	 * @update
	 * @param
	 * @return String
	 */
	public String getFSubsGUIDs(String regionGUID) throws Exception {
		String FSubsGUIDs = "";
		List<Record> list = getTU_RsByFRegionGUID(regionGUID);
		if (list.size() > 0) {
			FSubsGUIDs = assembleSubsGUIDs(list);
		}
		return FSubsGUIDs;
	}

	/**
	 * 根据区域GUID查询T_TERMUSER_REGION表中与它相关的记录
	 * 
	 * @author Johnson
	 * @create 2017年2月14日 上午9:41:07
	 * @update
	 * @param
	 * @return List<T_TermUser_Region>
	 */
	public List<Record> getTU_RsByFRegionGUID(String regionGUID) throws Exception {

		List<Record> list = Db.find("SELECT * FROM " + termUserRegionTableName + " WHERE FRegionGUID = '" + regionGUID
				+ "' and FDeleted = '0' ");
		return list;
	}

	/**
	 * 根据用户GUID查询T_TERMUSER_REGION表中与它相关的记录
	 * 
	 * @author Johnson
	 * @create 2017年2月14日 下午6:01:50
	 * @update
	 * @param
	 * @return List<Record>
	 */
	public List<Record> getTU_RsByFSubsGUID(Record record) throws Exception {
		List<Record> list = Db.find("SELECT * FROM " + termUserRegionTableName
				+ " WHERE FDeleted = '0' AND FSubsGUID = '" + record.getStr("GUID") + "'");
		return list;
	}

	/**
	 * 组装所有SubsGUID得到SubsGUIDs（SubsGUID之间用逗号隔开）
	 * 
	 * @author Johnson
	 * @create 2017年2月14日 上午9:48:02
	 * @update
	 * @param
	 * @return String
	 */
	public String assembleSubsGUIDs(List<Record> list) throws Exception {
		StringBuffer subsGUIDs = new StringBuffer();
		if (list.size() > 1) {
			subsGUIDs.append("'" + list.get(0).get("FSubsGUID") + "'");
			for (int i = 1; i < list.size(); i++) {
				Record t_TermUser_Region = list.get(i);
				subsGUIDs.append("," + "'" + t_TermUser_Region.getStr("FSubsGUID") + "'");
			}
		} else if (list.size() == 1) {
			subsGUIDs.append("'" + list.get(0).get("FSubsGUID") + "'");
		}
		return subsGUIDs.toString();
	}

	/*
	 * 为终端用户设置关联的区域（一个用户可以关联多个区域）
	 * 先遍历每个用户，将T_TermUser_Region表中与这些用户有关的记录的FDeleted设置为1即已删除。
	 * 遍历每一个含有用户GUID和区域GUID的json对象（即前台传来的一条用户区域关联记录），
	 * 根据用户GUID和区域GUID查下T_TermUser_Region表中有无该记录，
	 * 有的话直接将FDeleted改回0，没有的话向T_TermUser_Region表中插入这条记录。
	 * 
	 * @see
	 * com.zens.ubasboss.services.T_TermUser_RegionService#setRegionsForTermUser
	 * (java.lang.String)
	 */
	@Override
	public boolean setRegions(String param) throws Exception {
		associatedGUID = "regionGUID";
		hasAssociated = "FHasRegion";
		return setRegionOrGroupForTermUser(param, termUserRegionTableName);
	}

	/**
	 * 查询指定注册用户所属区域（支持根据任意能唯一表示注册用户的字段进行查询）
	 * 
	 * @author Johnson
	 * @create 2017年4月14日 14:38:07
	 * @update
	 * @param
	 * @return List
	 */

	@Override
	public List<Record> getRegionsByTermUser(String param) throws Exception {
		JSONObject paramJO = JSONObject.fromObject(param);
		String field = (String) paramJO.get("field");
		String trueField = SQLUtil.getTrueField(field);
		String uniqueKey = (String) paramJO.get("uniqueKey");
		// 根据传入的设备信息去获取到对应设备的GUID
		System.out.println(uniqueKey);
		String terminfowheresql = "SELECT FGUID FROM t_terminfo WHERE " + trueField + "= ?";

		Record termininfo = Db.findFirst(terminfowheresql, uniqueKey);

		System.out.println(termininfo.getStr("FGUID"));
		String termuserinfosql = "SELECT  FSubsGUID FROM t_termuser_terminfo  WHERE  FTermInfoID= ?";
		// 根据对应设备ID 查询对应的用户的GUID
		Record termmuserinfo = Db.findFirst(termuserinfosql, termininfo.getStr("FGUID"));
		// String FSubsGUID=termmuserinfo.getStr("FSubsGUID");
		// 根据用户的GUID查询与区域的关联表 获取到对应区域的GUID；
		String termuserregionsql = "SELECT FRegionGUID FROM t_termuser_region WHERE FSubsGUID= ? and FDeleted = 0";
		Record termuserregion = Db.findFirst(termuserregionsql, termmuserinfo.getStr("FSubsGUID"));
		// String FRegionGUID=termuserregion.getStr("FRegionGUID");
		// 根据对应用户的GUID查询到所属区域信息
		String termusersql = "SELECT id,Fguid AS guid,Ftype AS type,Fvalue AS value,Fenvalue AS envalue,FparentGuid AS parentGuid,Flevel AS level,Ficon AS icon,FsortNumber AS sortNumber,FmoveRestrict AS moveRestrict,Fremark AS remark,Fstate AS state,Fdelete AS deletes,Fextdata AS extdata,FclientGuid AS clientGuid FROM t_nesteddataset WHERE FGUID= ?";
		return Db.use("dPlugin2").find(termusersql, termuserregion.getStr("FRegionGUID"));

		/*
		 * String subsGUID; if ("guid".equals(field)) { subsGUID = uniqueKey; }
		 * else { String termInfoID = termInfoDao.getGUID(trueField, uniqueKey);
		 * if (termInfoID == null) { return null; } subsGUID =
		 * termUserTermInfoDao.getSubsGUIDByTermInfoID(termInfoID); }
		 * //获取与该终端用户关联的所有区域的GUID List<Record> termUserRegions =
		 * termUserRegionDao.getBySubsGUID("FRegionGUID", subsGUID); return
		 * cacheService.getRegionsByRegionGUIDs(termUserRegions);
		 */

		// 根据传入的设备信息去获取到对应设备的GUID

	}

	/********************************************************************************************************************************************************
	 * 注册用户和分组关联
	 **************************************************************************************************************************************************************/

	/**
	 * 根据用户GUID查询T_TERMUSER_GROUP表中与它相关的记录
	 * 
	 * @author Johnson
	 * @create 2017年2月14日 下午6:04:30
	 * @update
	 * @param
	 * @return List<Record>
	 */

	public Record getTU_GByFSubsGUID(Record record) throws Exception {
		Record termUser_Group = Db.findFirst("SELECT * FROM " + termUserGroupTableName
				+ " WHERE FDeleted = '0' AND FSubsGUID = '" + record.getStr("GUID") + "'");
		return termUser_Group;
	}

	/*
	 * 为用户设置关联的分组（一个用户只能关联一个分组）
	 * 
	 * @see
	 * com.zens.ubasboss.services.T_TermUser_GroupService#setGroupsForTermUser(
	 * java.lang.String)
	 */
	@Override
	public boolean setGroup(String param) throws Exception {
		associatedGUID = "groupGUID";
		hasAssociated = "FHasGroup";
		return setRegionOrGroupForTermUser(param, termUserGroupTableName);
	}

	/********************************************************************************************************************************************************
	 * 终端用户和业务包关联
	 **************************************************************************************************************************************************************/

	/*
	 * 给终端用户开通业务包
	 * 
	 * @see com.zens.ubasbossservices.service.T_TermUserService#
	 * bindTermUser_SubscriberPackages(java.lang.String)
	 */
	@Override
	public List<Record> bindTermUser_SubscriberPackages(String param, final String operation) throws Exception {
		final JSONArray paramJA = JSONArray.fromObject(param);

		final List<Record> records = new ArrayList<>();

		// 事务
		Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {

				boolean result = false;
				try {

					if (operation != null && operation.equals("true")) {
						Record recordstate = new Record();
						// 代表的是一个用户开通业务包，先计算用户余额是否足以开通所有业务包
						Integer subscriberPackageprice = 0;// 所有开通业务包的价格
						JSONObject subsGUIDOb = (JSONObject) paramJA.get(0);
						String subsGUIDob = subsGUIDOb.getString("subsGUID");
						// 当前账户余额
						Integer accountmoney = Integer.parseInt(Db
								.findFirst(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_checkbalance",
										Kv.by("tablename", T_Account.tableName).set("FUserGUID", subsGUIDob)))
								.get("FAccountbalance").toString());
						for (Object jo : paramJA) {

							JSONObject recordJO = (JSONObject) jo;
							String cycle = (String) recordJO.get("cycle");
							String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
							// 首先判断用户开包类别（是策略包还是业务包并且获取到对应的价格和时长）
							Record subscriberRecord = Db
									.findFirst(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_findsub",
											Kv.by("tablename", subscriberPackageTableName).set("FGUID",
													subscriberPackageGUID)));
							Record strategy = Db.findFirst(Db.getSqlPara(
									"termuser.bindTermUser_SubscriberPackages_findstrategy",
									Kv.by("tablename", T_Strategy.tableName).set("FGUID", subscriberPackageGUID)));
							if (subscriberRecord != null) {
								// 说明是业务包
								if (cycle.trim().equals("0")) {
									// 说明是按次的业务包获取到价格

									subscriberPackageprice += Integer
											.parseInt(subscriberRecord.get("FPrice").toString());
								} else {
									// 套餐业务包的价格
									subscriberPackageprice += Integer.parseInt(cycle)
											/ Integer.parseInt(subscriberRecord.getStr("FCycle"))
											* Integer.parseInt(subscriberRecord.getStr("FPrice"));
								}
							}
							if (strategy != null) {
								if (cycle.trim().equals("0")) {
									// 说明是按次的业务包获取到价格
									subscriberPackageprice += Integer
											.parseInt(subscriberRecord.get("FPrice").toString());
								} else {
									// 套餐业务包的价格
									subscriberPackageprice += Integer.parseInt(cycle)
											/ Integer.parseInt(strategy.getStr("FLifecycle").toString())
											* Integer.parseInt(strategy.getStr("FPrice").toString());
								}

							}

						}

						if (accountmoney < subscriberPackageprice) {
							// 当前账户余额不足以开通此次所有的业务包直接返回失败的用户和所开的包

							records.add(recordstate.set("money", subscriberPackageprice - accountmoney).set("state",
									"账户余额不足,开通业务包失败"));
							return false;

						}
					}

					for (Object o : paramJA) {
						Record recordstate = new Record();
						JSONObject recordJO = (JSONObject) o;
						String subsGUID = (String) recordJO.get("subsGUID");
						String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
						// availabilityTiem生效时间
						String availabilityTime = (String) recordJO.get("availabilityTime");
						String cycle = (String) recordJO.get("cycle");
						// 首先判断用户开包类别（是策略包还是业务包并且获取到对应的价格和时长）
						Record subscriberRecord = Db.findFirst(Db.getSqlPara(
								"termuser.bindTermUser_SubscriberPackages_findsub",
								Kv.by("tablename", subscriberPackageTableName).set("FGUID", subscriberPackageGUID)));
						Record strategy = Db
								.findFirst(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_findstrategy",
										Kv.by("tablename", T_Strategy.tableName).set("FGUID", subscriberPackageGUID)));
						// String
						// efectivetime=recordJO.getString("efectivetime");
						if (subscriberRecord != null) {
							String expiryTime = "";
							// 说明开通的是业务包
							// 获取到当前时间
							long newtime = System.currentTimeMillis();
							// 获取当前业务包的生效时间

							long Fefectime = TimeUT.getMillsTime(subscriberRecord.get("FAvailabilityTime").toString());

							if (newtime < Fefectime) {
								expiryTime = TimeUT.getTime(Fefectime + Long.parseLong(cycle) * 24 * 60 * 60 * 1000);

							} else {

								expiryTime = TimeUT.getTime(newtime + Long.parseLong(cycle) * 24 * 60 * 60 * 1000);

							}

							// boolean hasSubscriberPackage =
							// subscriberPackageDao.queryByGUID(subscriberPackageGUID);
							// if (!hasSubscriberPackage) {
							// return false;
							// }
							// 开通之前:
							// 先去t_subscriberpackage表中查下有没有GUID为subscriberPackageGUID且当前能用的记录
							String sql = "SELECT FName FROM " + subscriberPackageTableName + " WHERE FGUID = ?";
							List<Record> hasSubscriberPackage = Db.find(sql, subscriberPackageGUID);
							if (hasSubscriberPackage.size() < 0) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
										"无可用业务包记录，开通业务包失败"));
								continue;
							}
							// 检查 当前开通的业务包有没有过期，过期的话将被冻结
							// T_TermUserServiceImpl.termUserServiceImpl.setEfectivetime(subscriberPackageGUID);
							// 检查当前的业务包的状态，如果是冻结状态，那么无法开通
							if (!T_TermUserServiceImpl.termUserServiceImpl
									.SubscriberPackageState(subscriberPackageGUID)) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
										"当前选择的业务包已冻结，开通业务包失败"));
								continue;
							}
							;
							// 检查当前开通的业务是否该用户已经开通，如果已开通，那么无法开通
							/*
							 * if (!T_TermUserServiceImpl.termUserServiceImpl
							 * .SubscriberPackageOpenState(
							 * subscriberPackageGUID, subsGUID)) {
							 * records.add(record.set(subsGUID,
							 * subscriberPackageGUID).set("state",
							 * "当前选择的业务包已经开通过，无法继续开通"));
							 * 
							 * continue; }
							 */
							// 然后去终端用户区域包关联表中看下有没有该终端用户与这个注册包在有效期的关联记录
							if (termUserSubscriberPackageDao.hasEffectiveRecord(subsGUID, subscriberPackageGUID)) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
										"当前选择的业务包已开通,并且处于有效期内,开通业务包失败"));
								continue;
							}
							String FRDER_TYPE = "0";
							if (cycle.trim().equals("0")) { // 如果cycle是"0"，就表示本次终端用户订购的是按次业务
								// 直接扣除业务包费用的价格
								// 检查账户余额是否足够
								// 业务包价格
								FRDER_TYPE = "1";
								expiryTime = subscriberRecord.get("FEfectivetime");
								Integer submoney = Integer.parseInt(subscriberRecord.get("FPrice").toString());
								// 当前账户余额
								Integer accountmoney = Integer.parseInt(Db.findFirst(
										Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_checkbalance",
												Kv.by("tablename", T_Account.tableName).set("FUserGUID", subsGUID)))
										.get("FAccountbalance").toString());
								if (submoney > accountmoney) {
									// 账户余额不足,无法开通此类业务包
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"账户余额不足，开通业务包失败"));

									continue;
								}

								// 进行扣费
								if (Db.update(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_feededuction",

										Kv.by("tablename", T_Account.tableName)
												.set("FAccountbalance", accountmoney - submoney)
												.set("FUpdatetime", TimeUT.getCurrTime()).set("FUserGUID", subsGUID))
										.getSql()) == 0) {
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"扣费失败，开通业务包失败"));

									// 扣费失败
									continue;
								}
								// continue;)
							} else {
								// 此次要扣费的金额
								FRDER_TYPE = "3";
								Integer submoney = Integer.parseInt(cycle)
										/ Integer.parseInt(subscriberRecord.getStr("FCycle"))
										* Integer.parseInt(subscriberRecord.getStr("FPrice"));
								// 当前账户余额
								Integer accountmoney = Integer.parseInt(Db.findFirst(
										Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_checkbalance",
												Kv.by("tablename", T_Account.tableName).set("FUserGUID", subsGUID)))
										.get("FAccountbalance").toString());

								if (submoney > accountmoney) {
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"账户余额不足，开通业务包失败"));
									// 账户余额不足,无法开通此类业务包

									continue;
								}

								// 进行扣费
								if (Db.update(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_feededuction",

										Kv.by("tablename", T_Account.tableName)
												.set("FAccountbalance", accountmoney - submoney)
												.set("FUpdatetime", TimeUT.getCurrTime()).set("FUserGUID", subsGUID))
										.getSql()) == 0) {
									// 扣费失败
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"扣费失败，开通业务包失败"));

									continue;
								}
							}
							// 扣费成功，用户关联业务包
							Record record = new Record();
							record.set("FSubsGUID", subsGUID).set("FSubscriberPackageGUID", subscriberPackageGUID)
									.set("FAvailabilityTime", availabilityTime).set("FExpiryTime", expiryTime)
									.set("FCreateTime", TimeUT.getCurrTime()).set("FType", "1");
							result = Db.save(tU_SPTableName, record);
							if (result) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state", "开通业务包成功"));
								Record recordlog = new Record();
								String price = "0";
								if (cycle.equals("0")) {
									price = subscriberRecord.get("FPrice").toString();
								} else {
									price = Integer.parseInt(cycle)
											/ Integer.parseInt(subscriberRecord.getStr("FCycle"))
											* Integer.parseInt(subscriberRecord.getStr("FPrice")) + "";
								}
								// 业务包订购成功之后记录订购日志

								recordlog.set("FORDER_TIME", TimeUT.getCurrTime()).set("FUSER_ID", subsGUID)
										.set("FUSER_ID", subsGUID)
										.set("FTERMINAL_ID",
												Db.findFirst(
														"SELECT FCaCard FROM " + terminfotableName
																+ " WHERE  FGUID in (SELECT FTermInfoID FROM "
																+ T_TermUserTermInfo.tableName + " WHERE FSubsGUID=?)",
														subsGUID).get("FCaCard").toString())
										.set("FPRODUCT_CODE",
												Db.findFirst("SELECT FProductID FROM " + T_SubscriberPackage.tableName
														+ " WHERE FGUID=?", subscriberPackageGUID).get("FProductID"))
										.set("FPRICE", price).set("FVALID_TIME", availabilityTime)
										.set("FEXPIRE_TIME", expiryTime).set("FCRTETIME", TimeUT.getCurrTime())
										.set("FRDER_TYPE", FRDER_TYPE);
								Db.save(T_Order_log.tableName, recordlog);

								/*
								 * Db.update(Db.getSqlPara(
								 * "findusersub.bindTermUser_SubscriberPackages_log",
								 * Kv.by("FORDER_TIME",
								 * TimeUT.getCurrTime()).set("FUSER_ID",
								 * subsGUID) .set("FTERMINAL_ID",
								 * Db.findFirst("SELECT FCaCard FROM " +
								 * terminfotableName +
								 * " WHERE  FGUID in (SELECT FTermInfoID FROM "
								 * + T_TermUserTermInfo.tableName +
								 * " WHERE FSubsGUID=?)",
								 * subsGUID).get("FCaCard").toString())
								 * .set("FPRODUCT_CODE",
								 * Db.findFirst("SELECT FProductID FROM " +
								 * T_SubscriberPackage.tableName +
								 * " WHERE FGUID=?",
								 * subscriberPackageGUID).get("FProductID"))
								 * .set("FPRICE", Integer.parseInt(cycle) /
								 * Integer.parseInt(subscriberRecord.getStr(
								 * "FCycle"))
								 * Integer.parseInt(subscriberRecord.getStr(
								 * "FPrice"))) .set("FVALID_TIME",
								 * availabilityTime).set("FEXPIRE_TIME",
								 * expiryTime) .set("FCRTETIME",
								 * TimeUT.getCurrTime())).getSql());
								 */

							}

						}
						if (strategy != null) {
							String expiryTime = "";
							long newtime = System.currentTimeMillis();
							// 获取当前策略包的生效时间
							long Fefectime = TimeUT.getMillsTime(strategy.get("FAvailabilityTime").toString());

							if (newtime < Fefectime) {
								expiryTime = TimeUT.getTime(Fefectime + Long.parseLong(cycle) * 24 * 60 * 60 * 1000);

							} else {

								expiryTime = TimeUT.getTime(newtime + Long.parseLong(cycle) * 24 * 60 * 60 * 1000);

							}
							// 说明开通的是策略包

							// 检查策略包的生命周期没有生命周期的策略包不予开通
							if (strategy.get("FLifecycle").toString().equals("0")) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
										"策略包没有绑定业务包 ,开通策略包失败"));
								continue;
							}

							// 检查策略包是否处于冻结状态，处于状态无法开通
							if (Db.findFirst(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_FFreezed",
									Kv.by("tablename", T_Strategy.tableName).set("FGUID", subscriberPackageGUID)))
									.get("FFreezed").toString().equals("1")) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
										"策略包处于冻结状态，开通策略包失败"));

								continue;
							}
							// 检查策略包是否已经被开通，如果已经开通并且在有效期内无法再开通
							if (checkvalidstrategy(subsGUID, subscriberPackageGUID)) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
										"策略包已经开通并处于有效期内，开通策略包失败"));
								continue;
							}
							;
							String FRDER_TYPE = "0";
							if (cycle.trim().equals("0")) { // 如果cycle是"0"，就表示本次终端用户订购的是按次业务
								// 直接扣除业务包费用的价格
								// 检查账户余额是否足够
								// 策略包价格
								FRDER_TYPE = "1";
								expiryTime = strategy.get("FEfectivetime");
								Integer strategymoney = Integer.parseInt(strategy.get("FPrice").toString());
								// 当前账户余额
								Integer accountmoney = Integer.parseInt(Db.findFirst(
										Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_checkbalance",
												Kv.by("tablename", T_Account.tableName).set("FUserGUID", subsGUID)))
										.get("FAccountbalance").toString());

								if (strategymoney > accountmoney) {
									// 账户余额不足,无法开通此类业务包
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"账户余额不足,开通策略包失败"));
									continue;
								}
								// 进行扣费
								if (Db.update(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_feededuction",
										Kv.by("tablename", T_Account.tableName)
												.set("FAccountbalance", accountmoney - strategymoney)
												.set("FUpdatetime", TimeUT.getCurrTime()).set("FUserGUID", subsGUID))
										.getSql()) == 0) {
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"扣费失败,开通策略包失败"));
									continue;
								}
								// continue;)
							} else {
								// 策略包价格
								FRDER_TYPE = "3";
								Integer strategyprice = Integer.parseInt(cycle)
										/ Integer.parseInt(strategy.get("FLifecycle").toString())
										* Integer.parseInt(strategy.get("FPrice").toString());
								// 账户余额
								Integer accountmoney = Integer.parseInt(Db.findFirst(
										Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_checkbalance",
												Kv.by("tablename", T_Account.tableName).set("FUserGUID", subsGUID)))
										.get("FAccountbalance").toString());
								if (strategyprice > accountmoney) {
									// 账户余额不足,无法开通此类业务包
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"账户余额不足，开通策略包失败"));
									continue;
								}
								// 进行扣费
								if (Db.update(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_feededuction",
										Kv.by("tablename", T_Account.tableName)
												.set("FAccountbalance", accountmoney - strategyprice)
												.set("FUpdatetime", TimeUT.getCurrTime()).set("FUserGUID", subsGUID))
										.getSql()) == 0) {
									// 扣费失败
									records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state",
											"扣费失败，开通策略包失败"));
									;// 扣费失败，开通失败状态码

									continue;
								}
							}

							// 扣费完成用户关联业务包
							Record record = new Record();
							record.set("FSubsGUID", subsGUID).set("FSubscriberPackageGUID", subscriberPackageGUID)
									.set("FAvailabilityTime", availabilityTime).set("FExpiryTime", expiryTime)
									.set("FCreateTime", TimeUT.getCurrTime()).set("FType", "2");
							result = Db.save(tU_SPTableName, record);
							if (result) {
								records.add(recordstate.set(subsGUID, subscriberPackageGUID).set("state", "策略包开通成功"));
								;// 开通成功状态码
								Record recordlog = new Record();
								String price = "0";
								if (cycle.equals("0")) {
									price = subscriberRecord.get("FPrice").toString();
								} else {
									price = Integer.parseInt(cycle)
											/ Integer.parseInt(strategy.get("FLifecycle").toString())
											* Integer.parseInt(strategy.get("FPrice").toString()) + "";
								}

								recordlog.set("FORDER_TIME", TimeUT.getCurrTime()).set("FUSER_ID", subsGUID)
										.set("FUSER_ID", subsGUID)
										.set("FTERMINAL_ID",
												Db.findFirst(
														"SELECT FCaCard FROM " + terminfotableName
																+ " WHERE  FGUID in (SELECT FTermInfoID FROM "
																+ T_TermUserTermInfo.tableName + " WHERE FSubsGUID=?)",
														subsGUID).get("FCaCard").toString())
										.set("FPRODUCT_CODE",
												Db.findFirst("SELECT FProductID FROM " + T_Strategy.tableName
														+ " WHERE FGUID=?", subscriberPackageGUID).get("FProductID"))
										.set("FPRICE", price).set("FVALID_TIME", availabilityTime)
										.set("FEXPIRE_TIME", expiryTime).set("FCRTETIME", TimeUT.getCurrTime())
										.set("FRDER_TYPE", FRDER_TYPE);
								Db.save(T_Order_log.tableName, recordlog);
							}

						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					result = false;

				}

				return result;

			}

		});

		return records;
	}

	/**
	 * 检查当前策略包是否已开通并处于有效期内
	 * 
	 * 
	 */
	public boolean checkvalidstrategy(String subGUID, String strategyGUID) {
		boolean result = false;
		try {

			Record checkerecord = Db
					.findFirst(Db.getSqlPara("termuser.checkvalidstrategy", Kv.by("tablename", tU_SPTableName)
							.set("FSubsGUID", subGUID).set("FSubscriberPackageGUID", strategyGUID)));
			if (checkerecord != null) {
				if (System.currentTimeMillis() < TimeUT.getMillsTime(checkerecord.get("FExpiryTime").toString())) {
					// 说明还有没过期
					result = true;
				}
				;
			} else {
				result = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;

	}

	/*
	 * 给终端用户停断业务包
	 * 
	 * @see com.zens.ubasbossservices.service.T_TermUserService#
	 * unbindTermUser_SubscriberPackages(java.lang.String)
	 */
	@Override
	public List<Record> unbindTermUser_SubscriberPackages(String param) throws Exception {
		final JSONArray paramJA = JSONArray.fromObject(param);
		final List<Record> records = new ArrayList<>();
		// 事务回滚
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : paramJA) {
						Record record = new Record();
						JSONObject recordJO = (JSONObject) o;
						String subsGUID = (String) recordJO.get("subsGUID");
						String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
						// String updateTime = TimeUT.getCurrTime();
						String sql = "UPDATE " + tU_SPTableName + " SET FDeleted='1',FUpdateTime='"
								+ TimeUT.getCurrTime() + "' WHERE FsubsGUID = ? AND FSubscriberPackageGUID = ? ";

						result = (Db.update(sql, subsGUID, subscriberPackageGUID) > 0) && result;
						if (result) {
							record.set(subsGUID, subscriberPackageGUID).set("state", "退订成功");
						} else {
							record.set(subsGUID, subscriberPackageGUID).set("state", "退订失败");
						}
						records.add(record);
					}
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				System.out.println(result);
				return result;
			}
		});
		return records;
	}

	/*
	 * 给终端用户暂停业务包
	 * 
	 * @see com.zens.ubasbossservices.service.T_TermUserService#
	 * suspendTermUser_SubscriberPackages(java.lang.String)
	 */
	@Override
	public boolean suspendTermUser_SubscriberPackages(String param) throws Exception {
		boolean result = true;
		JSONArray paramJA = JSONArray.fromObject(param);
		for (Object o : paramJA) {
			JSONObject recordJO = (JSONObject) o;
			String subsGUID = (String) recordJO.get("subsGUID");
			String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
			result = termUserSubscriberPackageDao.suspendRecord(subsGUID, subscriberPackageGUID) && result;
		}
		return result;
	}

	/*
	 * 给终端用户继续业务包
	 * 
	 * @see com.zens.ubasbossservices.service.T_TermUserService#
	 * continueTermUser_SubscriberPackages(java.lang.String)
	 */
	@Override
	public boolean continueTermUser_SubscriberPackages(String param) throws Exception {
		boolean result = true;
		JSONArray paramJA = JSONArray.fromObject(param);
		for (Object o : paramJA) {
			JSONObject recordJO = (JSONObject) o;
			String subsGUID = (String) recordJO.get("subsGUID");
			String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
			result = termUserSubscriberPackageDao.continueRecord(subsGUID, subscriberPackageGUID) && result;
		}
		return result;
	}

	/*
	 * 给终端用户冻结业务包
	 * 
	 * @see com.zens.ubasbossservices.service.T_TermUserService#
	 * freezeTermUser_SubscriberPackages(java.lang.String)
	 */
	@Override
	public boolean freezeTermUser_SubscriberPackages(String param) throws Exception {
		final JSONArray paramJA = JSONArray.fromObject(param);
		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : paramJA) {
						JSONObject recordJO = (JSONObject) o;
						String subsGUID = (String) recordJO.get("subsGUID");
						String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
						String updateTime = TimeUT.getCurrTime();
						String sql = "UPDATE " + tU_SPTableName + " SET "
								+ "FFreezed = '1',FUpdateTime = ? WHERE FsubsGUID = ? AND FSubscriberPackageGUID = ? ";
						result = (Db.update(sql, updateTime, subsGUID, subscriberPackageGUID) > 0) && result;
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

	/*
	 * 给终端用户解冻业务包
	 * 
	 * @see com.zens.ubasbossservices.service.T_TermUserService#
	 * unfreezeTermUser_SubscriberPackages(java.lang.String)
	 */
	@Override
	public boolean unfreezeTermUser_SubscriberPackages(String param) throws Exception {
		final JSONArray paramJA = JSONArray.fromObject(param);
		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : paramJA) {
						JSONObject recordJO = (JSONObject) o;
						String subsGUID = (String) recordJO.get("subsGUID");
						String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
						String updateTime = TimeUT.getCurrTime();
						String sql = "UPDATE " + tU_SPTableName + " SET "
								+ "FFreezed = '0',FUpdateTime = ? WHERE FsubsGUID = ? AND FSubscriberPackageGUID = ? ";
						result = (Db.update(sql, updateTime, subsGUID, subscriberPackageGUID) > 0) && result;
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

	/*
	 * 给终端用户续订业务包
	 * 
	 * @see com.zens.ubasbossservices.service.T_TermUserService#
	 * renewTermUser_SubscriberPackages(java.lang.String)
	 */
	@Override
	public List<Record> renewTermUser_SubscriberPackages(String param) throws Exception {
		final JSONArray paramJA = JSONArray.fromObject(param);
		final List<Record> records = new ArrayList<>();
		// 事务回滚
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (Object o : paramJA) {
						Record record = new Record();
						JSONObject recordJO = (JSONObject) o;
						String subsGUID = (String) recordJO.get("subsGUID");
						String oldExpiryTime = (String) recordJO.get("expiryTime");
						String cycle = (String) recordJO.get("cycle");
						String subscriberPackageGUID = (String) recordJO.get("subscriberPackageGUID");
						// 续订之前
						// 先去t_subscriberpackage表中查下有没有GUID为subscriberPackageGUID且当前能用的记录，没有就直接跳出for循环
						// String sql = "SELECT FName FROM " +
						// subscriberPackageTableName + " WHERE FGUID = ? AND
						// FFreezed!=1 AND FDeleted!=1";
						// 首先判断用户开包类别（是策略包还是业务包并且获取到对应的价格和时长）
						Record subscriberRecord = Db.findFirst(Db.getSqlPara(
								"termuser.bindTermUser_SubscriberPackages_findsub",
								Kv.by("tablename", subscriberPackageTableName).set("FGUID", subscriberPackageGUID)));
						Record strategy = Db
								.findFirst(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_findstrategy",
										Kv.by("tablename", T_Strategy.tableName).set("FGUID", subscriberPackageGUID)));

						/*
						 * List<Record> hasSubscriberPackage = Db.find(sql,
						 * subscriberPackageGUID);
						 * 
						 * if (hasSubscriberPackage.size() < 0) { result =
						 * false; return result; }
						 */
						String newExpiryTime = TimeUT.getTime(
								TimeUT.getMillsTime(oldExpiryTime) + Long.parseLong(cycle) * 24 * 60 * 60 * 1000);
						if (subscriberRecord != null) {
							// 说明延期的是业务包

							record = termUserSubscriberPackageDao.renewRecord(subsGUID, subscriberPackageGUID,
									newExpiryTime, subscriberRecord, cycle);
						}
						if (strategy != null) {
							// 说明延期的是策略包
							record = termUserSubscriberPackageDao.renewRecord(subsGUID, subscriberPackageGUID,
									newExpiryTime, strategy, cycle);
						}

						records.add(record);
					}
				} catch (Exception e) {
					e.printStackTrace();
					result = false;
				}
				return result;
			}
		});
		return records;
	}

	/*
	 * 查询某终端用户是否已订购指定的业务包
	 * 
	 * @see
	 * com.zens.ubasbossservices.service.T_TermUserService#hasSubscriberPackage(
	 * java.lang.String)
	 */
	@Override
	public boolean hasSubscriberPackage(String param) throws Exception {
		JSONObject paramJO = JSONObject.fromObject(param);
		String subsGUID = paramJO.getString("subsGUID");
		String subscriberPackageGUID = paramJO.getString("subscriberPackageGUID");
		String sql = "SELECT * FROM " + tU_SPTableName + " WHERE FSubsGUID = ? AND FSubscriberPackageGUID = ? "
				+ " AND FDeleted = '0' AND FFreezed = '0' AND FAvailabilityTime < ? AND FExpiryTime > ?";
		String currentTime = TimeUT.getCurrTime();
		Record record = Db.findFirst(sql, subsGUID, subscriberPackageGUID, currentTime, currentTime);
		if (null == record) {
			return false;
		} else {
			return true;
		}
	}

	/********************************************************************************************************************************************************
	 * 公用方法
	 **************************************************************************************************************************************************************/

	/**
	 * 过滤掉不合法的数组元素。
	 * 
	 * @author jhonson
	 * @create 2017年2月24日 下午3:25:06
	 * @update
	 * @param
	 * @return void
	 */
	public void deleteIllegalElement(JSONArray jA, String associatedGUID) throws Exception {
		for (int i = jA.size() - 1; i >= 0; i--) {
			JSONObject jO = (JSONObject) jA.get(i);
			String associatedGUIDVal = (String) jO.get(associatedGUID);
			if (associatedGUIDVal != null) {
				// 判断当前应用中的缓存是否为空，如果为空，更新缓存
				cacheService.checkAndUpdateCache();
				// 区域/分组guid验证 验证提交的guid是否合法 避免恶意提交
				boolean isValidated = validateRegionOrGroup(associatedGUIDVal);
				if (isValidated == false) {
					jA.remove(i);
				}
			}
		}
	}

	/**
	 * 验证用户关联的区域是否合法（即在当前的缓存中是否存在）。
	 * 
	 * @author jhonson
	 * @create 2017年2月24日 下午3:30:34
	 * @update
	 * @param
	 * @return void
	 */
	public boolean validateRegionOrGroup(String regionOrGroupGUID) throws Exception {
		// 区域guid验证 验证提交的guid是否合法 避免恶意提交
		String cacheName = cacheService.getCacheName();
		if (CacheKit.get(cacheName, regionOrGroupGUID) == null) {
			return false;
		}
		return true;
	}

	/**
	 * 拼出要给用户设置的 区域/分组 guids
	 * 
	 * @author floristy
	 * @create 2017年3月16日 下午3:25:01
	 * @update
	 * @param
	 * @return String
	 */
	public String getAssociatedGUIDs(JSONObject JO, String associatedGUID) throws Exception {
		StringBuffer guids = new StringBuffer();

		String guid = JO.getString(associatedGUID);
		guids.append("'");
		guids.append(guid);
		guids.append("'");
		guids.append(",");

		if (guids.length() > 0) {
			guids.deleteCharAt(guids.length() - 1);
		}

		return guids.toString();
	}

	/**
	 * 根据subsGUIDs标记删除注册用户和 区域/分组 关联表中的所有记录
	 * 
	 * @author jhonson
	 * @create 2017年2月24日 下午3:48:27
	 * @update
	 * @param
	 * @return void
	 */
	public boolean deleteAllByGUIDs(String tableName, JSONArray paramData) throws Exception {
		String subsGUIDs = "";
		for (int i = 0; i < paramData.size(); i++) {
			JSONObject JO = (JSONObject) paramData.get(i);
			subsGUIDs += "'" + JO.get("subsGUID") + "'" + ",";
		}
		subsGUIDs = subsGUIDs.substring(0, subsGUIDs.lastIndexOf(","));
		return Db.update("UPDATE " + tableName + " SET FDeleted = 1 WHERE FSubsGUID IN (" + subsGUIDs + ")") >= 0;
	}

	/**
	 * 根据用户GUID删除该用户所有与区域或分组（依据associatedGUID而定）关联关系
	 * 
	 * @author floristy
	 * @create 2017年3月16日 下午4:07:41
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean deleteAllAssociations(final String subsGUID, String associatedGUID, final String tableName) {
		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					result = Db.update(
							"UPDATE " + tableName + " SET FDeleted = '1' WHERE FSubsGUID = '" + subsGUID + "' ") >= 0
							&& result;
					result = Db.update("UPDATE " + termUserTableName + " SET " + hasAssociated + " = -1 WHERE FGUID = '"
							+ subsGUID + "' ") >= 0 && result;
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
	 * 更新用户与区域（或分组）的关联关系
	 * 
	 * @author floristy
	 * @create 2017年3月16日 下午4:48:16
	 * @update
	 * @param
	 * @return boolean
	 */
	public boolean updateAssociations(final JSONObject JO, final String tableName, final String associatedGUID,
			final String associatedGUIDs) {
		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {

					final String subsGUID = (String) JO.get("subsGUID");

					String associatedGUIDVal = (String) JO.get(associatedGUID);

					String currentTime = TimeUT.getCurrTime();
					String addOrUpdateSql = "INSERT INTO " + tableName + " (FSubsGUID," + "F" + associatedGUID
							+ ",FIsDefault,FIsCharge,FCreateTime) VALUES ('" + subsGUID + "','" + associatedGUIDVal
							+ "','0','0','" + currentTime + "') ON DUPLICATE KEY UPDATE FDeleted = '0',FUpdateTime = '"
							+ currentTime + "';";

					result = Db.update(addOrUpdateSql) >= 0 && result;

					// System.out.println("result"+result);
					// 将用户表中这个用户的FHasRegion/FHasGroup改成1。
					result = (Db.update("UPDATE " + termUserTableName + " SET " + hasAssociated + " = 1 WHERE FGUID = '"
							+ subsGUID + "' ") > 0) && result;
					// 将以前关联了，但这次没有关联的记录全部标记删除。
					// 将以前关联了，但这次没有关联的记录全部标记删除。

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
	 * 为注册用户设置区域或分组
	 * 
	 * @author jhonson
	 * @create 2017年2月24日 下午4:36:33
	 * @update
	 * @param
	 * @return void
	 */
	public boolean setRegionOrGroupForTermUser(String param, final String tableName) throws Exception {
		final JSONArray paramData = JSONArray.fromObject(param);
		// 过滤掉不合法的数组元素。
		// 设置区域前把以前关联的区域给删掉
		deleteAllByGUIDs(tableName, paramData);

		for (int i = 0; i < paramData.size(); i++) {
			JSONObject JO = (JSONObject) paramData.get(i);
			String subsGUID = JO.getString("subsGUID");

			if (!JO.has(associatedGUID)) { // 如果前台没传 区域/分组
											// GUID，说明是删除这个用户所有的区域/分组 关联记录
				return deleteAllAssociations(subsGUID, associatedGUID, tableName);
			} else {

				deleteIllegalElement(paramData, associatedGUID);
				// 拼出要给用户设置的 区域/分组 guids
				String associatedGUIDs = getAssociatedGUIDs(JO, associatedGUID);

				updateAssociations(JO, tableName, associatedGUID, associatedGUIDs);
			}
		}

		return true;
	}

	/**
	 * 获取前端分页所需参数
	 * 
	 * @author Johnson
	 * @create 2017年2月27日 上午11:35:46
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

	public void getZEParamForLimitPage(Record record, String whereSQL, Integer pageSize) throws Exception {

		Number number = countZE(whereSQL);
		int pages = number.intValue() / pageSize;
		int res = number.intValue() % pageSize;
		int total = number.intValue();
		int totalPages = res == 0 ? pages : pages + 1; // totalPages是若返回所有（未删除的）的记录应该显示的页数。
		record.set("total", total); // 能够返回的（即未删除的）记录数。
		record.set("totalPages", totalPages); // totalPages是若返回所有记录应该显示的页数。
	}

	/**
	 * 获得 根据区域GUID查询用户的SQL片段
	 * 
	 * @author Johnson
	 * @create 2017年2月27日 上午11:52:54
	 * @update
	 * @param
	 * @return void
	 */
	public String getSQLByRegionGUID(String regionGUID) throws Exception {
		String sql;
		if(regionGUID.length() == 0) {
			sql = "";
		}else {
			String GUIDs = getFSubsGUIDs(regionGUID);
			if (!GUIDs.equals("")) {
				sql = " AND FGUID IN (" + GUIDs + ") ";
			} else {
				sql = " AND FGUID IN (\"  \") ";
			}
		}
		

		return sql;
	}

	/**
	 * 
	 * 开通之前先去t_subscriberpackage表中查下此业务包有没有过期，如果过期了 直接把该业务包给冻结掉
	 * 
	 * @author Johnson
	 * @create 2017年4月13日下午18:32:54
	 * @update
	 * @param
	 * @return void
	 * @throws ParseException
	 */

	/*
	 * public void setEfectivetime(String subscriberPackageGUID) throws
	 * ParseException{ //先查询该业务包的有效时间 String sql;
	 * sql="Select FEfectivetime FROM "+subscriberPackageTableName+
	 * " where FGUID= ?"; Record fectivetime= Db.findFirst(sql,
	 * subscriberPackageGUID); //有效时间
	 * System.out.println(fectivetime.getStr("FEfectivetime")); long Fefectime=
	 * TimeUT.getMillsTimes(fectivetime.getStr("FEfectivetime")); //获取到当前时间 long
	 * newtime=System.currentTimeMillis(); //当前业务包已过期，直接冻结
	 * if(newtime>Fefectime){ sql="Update  "
	 * +subscriberPackageTableName+"  Set FFreezed= 1 Where FGUID=  ?";
	 * //sql="Update"
	 * +subscriberPackageTableName+"Set FEfectivetime= 1 Where FGUID= "
	 * +subscriberPackageGUID; Db.update(sql, subscriberPackageGUID); }
	 * 
	 * 
	 * }
	 */

	/**
	 * 检查当前用户所选择的业务包状态(是否被冻结）
	 * 
	 * @author Johnson
	 * @create 2017年4月13日 18:45:51
	 * @update
	 * @param
	 * @return boolean
	 */

	public boolean SubscriberPackageState(String subscriberPackageGUID) {
		boolean success = true;
		String sql;
		sql = "Select FFreezed FROM " + subscriberPackageTableName + " where FGUID= ?";

		Record FFreezed = Db.findFirst(sql, subscriberPackageGUID);

		if (FFreezed.getStr("FFreezed").equals("1")) {
			// 说明该业务包已经被冻结不能开通
			success = false;
		}

		return success;
	}

	/**
	 * 检查当前用户所选择的业务包状态(是否已开通）
	 * 
	 * @author Johnson
	 * @create 2017年4月14日 13:30:21
	 * @update
	 * @param
	 * @return boolean
	 */

	public boolean SubscriberPackageOpenState(String subscriberPackageGUID, String subsGUID) {
		boolean success = true;
		String sql;
		sql = "Select FSubscriberPackageGUID FROM " + tU_SPTableName
				+ " where FSubsGUID= ? AND FFreezed!=1 AND FDeleted!=1 ";
		List<Record> subscriberPackage = Db.find(sql, subsGUID);
		for (Record subberpage : subscriberPackage) {

			// 如果发现当前用户已经开通该业务包 那么也不能开通
			if (subberpage.getStr("FSubscriberPackageGUID").equals(subscriberPackageGUID)) {

				success = false;

			}

		}

		return success;
	}

	/**
	 * 终端 用户认证接口
	 * 
	 * @author zhangkai
	 * @create 2017年6月24日 11:49:15
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record userauthentication(String param) throws Exception {
		Record userrecord = new Record();

		JSONObject paramData = JSONObject.fromObject(param);
		String mac = (String) paramData.get("mac"); // 设备的mac地址
		String serial = (String) paramData.get("serial");// 设备序列号
		String cacard = (String) paramData.get("cacard");// 用户的卡号
		// 通过设备参数查询该设备 是否合法（是否存在）
		String terminfosql = "SELECT FRegionID FROM t_terminfo  ";
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("FMac", mac);
		paramMap.put("FSerial", serial);
		paramMap.put("FCaCard", cacard);
		String whereSQL = SQLUtil.getWhereSql(paramMap);
		whereSQL += "AND FFreezed!=1 AND FDeleted!=1";

		Record record = Db.findFirst(terminfosql + whereSQL);
		// 说明该设备合法
		if (record.getStr("FRegionID") != null) {
			// 通过设备区域ID查询到该区域下的合法用户 返回用户所在区域主页地址
			String tnesteddatasetsql = "SELECT FIndexUrl FROM t_nesteddataset WHERE Fguid=? AND FFreezed!=1 AND FDeleted!=1";

			userrecord = Db.use("dPlugin2").findFirst(tnesteddatasetsql, record.getStr("FRegionID"));
		}
		// 返回用戶主頁地址
		return userrecord;
	}

	/********************************************************************************************************************************************************
	 * 用户档案使用方法
	 **************************************************************************************************************************************************************/
	/**
	 * 新增ZExamMS用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年6月16日 11:28:00
	 * @update
	 * @param
	 * @return boolean
	 */

	@Override
	public boolean addZE(String param) throws Exception {
		UUID uuid = UUID.randomUUID();
		JSONObject paramData = JSONObject.fromObject(param);
		System.out.println("解析通过");
		Record record = new Record();
		List<String> paramNames = new ArrayList<String>();
		paramNames.add("name");
		paramNames.add("sex");
		paramNames.add("age");
		paramNames.add("iphone");
		paramNames.add("mobile");
		paramNames.add("email");
		paramNames.add("createUserID");
		paramNames.add("ext");
		paramNames.add("prisonumber");
		paramNames.add("prisonernumber");
		paramNames.add("prisonerlevel");
		paramNames.add("jailtime");
		paramNames.add("releasetime");
		paramNames.add("prisonstatus");
		paramNames.add("mac");
		paramNames.add("userregion");
		paramNames.add("exName");
		paramNames.add("dob");
		paramNames.add("cop");
		paramNames.add("coh");
		paramNames.add("majorLang");
		paramNames.add("minorLang");
		paramNames.add("stdPicURL");
		paramNames.add("fpPicURL");
		paramNames.add("pidSN");
		paramNames.add("pidPOVBegin");
		paramNames.add("pidPOVEnd");
		paramNames.add("province");
		paramNames.add("city");
		paramNames.add("usertestmark");
		paramNames.add("pidAddr");
		paramNames.add("prisonGUID");
		paramNames.add("crimeCauses");
		paramNames.add("outprisonTime");
		paramNames.add("height");
		RecordUtil.setRecord(paramNames, paramData, record);
		record.set("FType", "z");
		record.set("FGUID", uuid.toString()).set("FCreateTime", TimeUT.getCurrTime());

		System.out.println(record);
		// .set("FFpPicURL", MyExceptionInterceptor.fpPicURL).set("FStdPicURL",
		// MyExceptionInterceptor.stdPicURL);
		return Db.use("dPlugin3").save("t_zexamtermuser", record);

	}

	/*
	 * 批量设置终端用户记录
	 * 
	 * @see
	 * com.zens.ubasboss.services.T_TermUserService#updateTermUsers(java.lang.
	 * String)
	 */
	@Override
	public boolean ZEupdate(String param) throws Exception {
		final JSONArray paramData = JSONArray.fromObject(param);

		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (int i = 0; i < paramData.size(); i++) {

						String updateSQL = "UPDATE t_zexamtermuser SET ";
						JSONObject jO = (JSONObject) paramData.get(i);
						String GUID = (String) jO.get("GUID");
						String setSQL = getSetSQLForTermUser_Group(jO);
						updateSQL += setSQL;
						updateSQL += "WHERE FGUID = '" + GUID + "' ";
						result = ZEupdateTermUser(updateSQL) && result;

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = false;
				}
				return result;
			}
		});
		return success;
	}

	public boolean ZEupdateTermUser(String sql) throws Exception {
		return Db.use("dPlugin3").update(sql) > 0;
	}

	/*
	 * 更新终端用户记录
	 * 
	 * @author ZKill
	 * 
	 * @create 2017年6月27日 09:09:18
	 * 
	 * @return boolean
	 */
	@Override
	public boolean ZEsetuser(String param) throws Exception {
		final JSONObject paramData = JSONObject.fromObject(param);

		// 事务回滚

		boolean result = true;
		try {
			String updateSQL = "UPDATE t_zexamtermuser SET ";

			String GUID = (String) paramData.get("GUID");

			String setSQL = getSetSQLForTermUser_Group(paramData);
			updateSQL += setSQL + " FUpdateTime='" + TimeUT.getCurrTime() + "'";
			updateSQL += " WHERE FGUID = '" + GUID + "' ";
			result = ZEupdateTermUser(updateSQL) && result;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		return result;

	}

	/**
	 * 删除ZExamMS用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:03:39
	 * @update
	 * @param
	 * @return Record
	 */

	@Override
	public boolean deleteZE(String param) throws Exception {
		JSONObject paramData = JSONObject.fromObject(param);
		final JSONArray GUIDs = paramData.getJSONArray("GUIDs");
		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				for (int i = 0; i < GUIDs.size(); i++) {
					try {
						String GUID = GUIDs.getString(i);
						result = (ZEdeleteTermUser(GUID) > 0) && result; // 删除用户
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						result = false;
					}
				}
				return result;
			}
		});
		return success;
	}

	/**
	 * 获取ZExamMS用户档案信息
	 * 
	 * @author ZKill
	 * @create 2017年5月11日 11:17:37
	 * @update
	 * @param
	 * @return Record
	 */

	@Override
	public Record getZE(String param) throws Exception {
		Record record = new Record();
		JSONObject paramData = JSONObject.fromObject(param);
		List<Record> termUsers = null;

		Record records = new Record();
		Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
		String keyword = (String) paramData.get("keyWord");
		String mac = (String) paramData.get("mac");
		System.out.println(mac);
		String cop = (String) paramData.get("cop");
		String coh = (String) paramData.get("coh");
		String sex = (String) paramData.get("sex");
		String deleted = (String) paramData.get("deleted");
		String freezed = (String) paramData.get("freezed");
		String prisonstatus = (String) paramData.get("prisonstatus");
		if (!StringUtils.hasText(deleted)) {
			deleted = "0";
		}
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("FType", "z");
		paramMap.put("FFreezed", freezed);
		paramMap.put("FCOP", cop);
		paramMap.put("FCOH", coh);
		paramMap.put("FSex", sex);
		paramMap.put("FPrisonstatus", prisonstatus);
		String whereSQL = SQLUtil.getWhereSql(paramMap);
		whereSQL += "AND FDeleted!= 1";
		// 根据设备的mac标识查询到区域信息
		String Region = "";
		if (mac != null) {
			String terminfoSQL = "SELECT FPrisonGUID,FRegionGUID,FCellGUID,FPrisonname,FRegionname,FCellname FROM t_terminfo WHERE FMac=?";
			records = Db.use("dPlugin3").findFirst(terminfoSQL, mac);
			System.out.println(records);
			// String recordsss = records.getStr("FRegionID").replaceAll(",",
			// "-");
			whereSQL += " AND FPrisonGUID=" + "'" + records.getStr("FPrisonGUID") + "' AND FRegionGUID=" + "'"
					+ records.getStr("FRegionGUID") + "'   AND  FCellGUID=" + "'" + records.getStr("FCellGUID") + "'";
			Region = records.getStr("FPrisonname") + records.getStr("FRegionname") + records.getStr("FCellname");
		}

		if (StringUtils.hasText(keyword)) {
			String keywordSQL = getZESQLByKeyword(keyword);
			whereSQL += keywordSQL;
		}

		;
		// if (StringUtils.hasText(keyword)) {
		// String keywordSQL = getSQLByKeyword(keyword);
		// whereSQL += keywordSQL;
		// }

		// String deletedSQL = " AND FDeleted = '" + deleted + "' ";
		// whereSQL += deletedSQL;

		String limitSQL = SQLUtil.getLimitSQL(pageSize, page);
		String sql = termUserSQL + whereSQL + " ORDER BY ID DESC " + limitSQL;

		termUsers = Db.use("dPlugin3").find(sql);
		/*
		 * String Region = "";
		 * 
		 * if (records.getStr("FRegion") != null) { String[] Regions =
		 * records.getStr("FRegion").split("-"); String Region1 =
		 * Regions[1].replaceAll("[\u2E80-\u9FFF]", "").length() == 2 ?
		 * Regions[1].replaceAll("[\u2E80-\u9FFF]", "") + "-" : "0" +
		 * Regions[1].replaceAll("[\u2E80-\u9FFF]", "") + "-"; String Region2 =
		 * Regions[2].replaceAll("[\u2E80-\u9FFF]", "").length() == 2 ?
		 * Regions[1].replaceAll("[\u2E80-\u9FFF]", "") : "0" +
		 * Regions[1].replaceAll("[\u2E80-\u9FFF]", ""); Region = Regions[0] +
		 * "-" + Region1 + Region2;
		 */

		record.set("ZExamMSUsers", termUsers);
		record.set("Region", Region);
		// 获取前端分页所需参数
		getZEParamForLimitPage(record, whereSQL, pageSize);

		return record;

	}

	/**
	 * 根据区域查设备信息根据设备查询该设备下的人员信息
	 * 
	 * @author ZKill
	 * @create 2017年6月16日 10:18:58
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public List<Record> getiplpsuserfo(String param) throws Exception {
		List<Record> record = new ArrayList<Record>();
		JSONObject paramData = JSONObject.fromObject(param);
		String guid = (String) paramData.get("GUID");
		String mac = (String) paramData.get("mac");
		if (mac != null) {
			// 说明查询的是对应mac地址下的区域人员信息

			String terminusersql = "SELECT FRegionGUID,FCellGUID,FPrisonGUID FROM t_terminfo WHERE Fmac=?";

			Record userrecord = Db.use("dPlugin3").findFirst(terminusersql, mac);

			String usersql = "SELECT * FROM t_zexamtermuser WHERE  FPrisonGUID=? AND  FCellGUID=? AND  FRegionGUID=?";

			record = Db.use("dPlugin3").find(usersql, userrecord.get("FPrisonGUID"), userrecord.get("FCellGUID"),
					userrecord.get("FRegionGUID"));
		} else {
			// 说明查询的是对应区域的设备信息
			String terminfosql = "SELECT * FROM t_terminfo WHERE FRegionGUID =? or FCellGUID=? or FPrisonGUID=?";
			record = Db.use("dPlugin3").find(terminfosql, guid, guid, guid);

		}

		return record;
	}

	/**
	 * 根据区域查设备信息根据设备查询该设备下的人员信息
	 * 
	 * @author ZKill
	 * @create 2017年6月16日 10:18:58
	 * @update
	 * @param
	 * @return Record
	 */

	/**
	 * 根据设备的mac地址查询对应的区域信息
	 * 
	 * @author ZKill
	 * @create 2017年6月16日 10:18:58
	 * @update
	 * @param
	 * @return Record
	 */

	public Record getRegion(String param) throws Exception {
		JSONObject paramData = JSONObject.fromObject(param);
		String mac = (String) paramData.get("mac");
		String terminusersql = "SELECT FPrisonname,FRegionname,FCellname FROM t_terminfo WHERE Fmac=?";
		Record userrecord = Db.use("dPlugin3").findFirst(terminusersql, mac);
		return userrecord;
	}

	/**
	 * 查询用户的账户信息
	 * 
	 * @author ZKill
	 * @create 2017年6月16日 10:18:58
	 * @update
	 * @param
	 * @return Record
	 */

	@Override
	public Record selectaccount(String param) throws Exception {
		Record record = new Record();
		JSONObject paramData = JSONObject.fromObject(param);
		String params = "";
		String FCaCard = (String) paramData.get("FCaCard");// ca卡号
		String FSerial = (String) paramData.get("FSerial");// 序列号
		String FMac = (String) paramData.get("FMac");// mac地址
		if (FCaCard != null) {
			params = " FCaCard='" + FCaCard + "'";

		}
		if (FSerial != null) {
			params = " FSerial='" + FSerial + "'";

		}
		if (FMac != null) {
			params = " FMac='" + FMac + "'";

		}

		SqlPara sqlPara = Db.getSqlPara("termuser.hasSubproduct", Kv.by("tablename", T_TermUserTermInfo.tableName)
				.set("tablename1", terminfotableName).set("params", params));
		// 获取用用户的guid

		Record recordd = Db.findFirst(sqlPara);
		List<Record> records = Db.find(Db.getSqlPara("termuser.selectaccount",
				Kv.by("tablename", T_Account.tableName).set("FUserGUID", recordd.get("SubsGUID"))));
		record.set("data", records);
		return record;
	}

	/**
	 * 查询终端用户已经订购的产品(业务包和策略包）
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record hasSubproduct(String param) throws Exception {

		Record records = new Record();
		String params = "";
		JSONObject paramData = JSONObject.fromObject(param);
		Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
		String FCaCard = (String) paramData.get("FCaCard");// ca卡号
		String FSerial = (String) paramData.get("FSerial");// 序列号
		String FMac = (String) paramData.get("FMac");// mac地址
		if (FCaCard != null) {
			params = " FCaCard='" + FCaCard + "'";
			records.set("FCaCard", FCaCard);
		}
		if (FSerial != null) {
			params = " FSerial='" + FSerial + "'";
			records.set("FSerial", FSerial);
		}
		if (FMac != null) {
			params = " FMac='" + FMac + "'";
			records.set("FMac", FMac);
		}

		SqlPara sqlPara = Db.getSqlPara("termuser.hasSubproduct", Kv.by("tablename", T_TermUserTermInfo.tableName)
				.set("tablename1", terminfotableName).set("params", params));

		Record record = Db.findFirst(sqlPara);
		// 检测当前用户已开通的业务包有没有过期，如果过期就删除业务关系

		Db.update(Db
				.getSqlPara("termuser.hasSubproductTesting",
						Kv.by("tablename", tU_SPTableName).set("SubsGUID", record.get("SubsGUID").toString()))
				.getSql());
		// 获取到用户的guid后根据用户的guid查询所开通的业务包和策略包
		// 开通的业务包

		if (pageSize == null && page == null) {
			List<Record> subRecord = Db.find((Db.getSqlPara("termuser.findusersub",
					Kv.by("tablename", tU_SPTableName).set("tablename1", subscriberPackageTableName)
							.set("tablename2", T_Strategy.tableName)
							.set("SubsGUID", record.get("SubsGUID").toString()))));
			records.set("subdata", subRecord);
		} else {
			Page<Record> subRecord = Db.paginate(page, pageSize,
					(Db.getSqlPara("termuser.findusersub",
							Kv.by("tablename", tU_SPTableName).set("tablename1", subscriberPackageTableName)
									.set("tablename2", T_Strategy.tableName)
									.set("SubsGUID", record.get("SubsGUID").toString()))));
			// 开通的策略包tU_SPTableName)
			records.set("subdata", subRecord);
		}

		return records;
	}

	/**
	 * 查询用户产品订购信息记录
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record selectProductrecord(String param) throws Exception {
		Record records = new Record();

		JSONObject paramData = JSONObject.fromObject(param);
		String params = "";
		Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
		String FCaCard = (String) paramData.get("FCaCard");// ca卡号
		String FSerial = (String) paramData.get("FSerial");// 序列号
		String FMac = (String) paramData.get("FMac");// mac地址
		if (FCaCard != null) {
			params = " FCaCard='" + FCaCard + "'";
			records.set("FCaCard", FCaCard);
		}
		if (FSerial != null) {
			params = " FSerial='" + FSerial + "'";
			records.set("FSerial", FSerial);
		}
		if (FMac != null) {
			params = " FMac='" + FMac + "'";
			records.set("FMac", FMac);
		}
		// 获取用户的GUID
		SqlPara sqlPara = Db.getSqlPara("termuser.hasSubproduct", Kv.by("tablename", T_TermUserTermInfo.tableName)
				.set("tablename1", terminfotableName).set("params", params));
		Record record = Db.findFirst(sqlPara);

		// 开通的业务包
		Page<Record> subRecord = Db.paginate(page, pageSize,
				(Db.getSqlPara("termuser.selectProductrecord",
						Kv.by("tablename1", tU_SPTableName).set("tablename2", subscriberPackageTableName)
								.set("tablename3", tU_SPTableName).set("tablename4", T_Strategy.tableName)
								.set("SubsGUID", record.get("SubsGUID").toString()))));
		// 开通的策略包tU_SPTableName
		records.set("subdata", subRecord);
		return records;
	}

	/**
	 * 用户点播鉴权接口（重要）
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record VODauthentication(String param) throws Exception {
		Record authentication = new Record();
		try {

			String params = "";
			JSONObject paramData = JSONObject.fromObject(param);
			String FSerial = (String) paramData.get("FSerial");// 序列号
			String FMac = (String) paramData.get("FMac");// mac地址
			String FCaCard = (String) paramData.get("FCaCard");// 用户唯一标识ca卡号
			String ColumnID = (String) paramData.get("FColumnID");// 栏目ID(包月加按球队）
			String MediafundedID = (String) paramData.get("FMediafundedID");// 媒资ID（按次）
			// 通过传入的用户唯一标识的ca卡号获取到该用户的guid
			if (FCaCard != null) {
				params = " FCaCard='" + FCaCard + "'";
			}
			if (FSerial != null) {
				params = " FSerial='" + FSerial + "'";
			}
			if (FMac != null) {
				params = " FMac='" + FMac + "'";
			}
			SqlPara sqlPara = Db.getSqlPara("termuser.hasSubproduct", Kv.by("tablename", T_TermUserTermInfo.tableName)
					.set("tablename1", terminfotableName).set("params", params));

			Record record = Db.findFirst(sqlPara);

			if (record != null) {
				List<Record> subRecord = Db.find(Db.getSqlPara("termuser.unfindusersub",
						Kv.by("tablename", subscriberPackageTableName).set("tablename1", tU_SPTableName)
								.set("FSubsGUID", record.get("SubsGUID").toString())));

				// 可订购策略包列表
				List<Record> strRecord = Db.find(Db.getSqlPara("termuser.unfinduserstrategy",
						Kv.by("tablename", T_Strategy.tableName).set("tablename1", tU_SPTableName).set("FSubsGUID",
								record.get("SubsGUID").toString())));
				subRecord.addAll(strRecord);

				// 根据产品ID获取到产品的GUID
				Record subguid = Db.findFirst(Db.getSqlPara("termuser.TVODauthenticationss",
						Kv.by("tablename", T_SubscriberPackage.tableName).set("tablename1", T_Strategy.tableName)
								.set("FColumnID", ColumnID).set("FMediafundedID", MediafundedID)));
				if (subguid == null) {
					authentication.set("state", "可用产品不存在,鉴权失败");
					authentication.set("subcode", "1");
					authentication.set("sublist", subRecord);

					return authentication;
				}

				if (Db.findFirst(Db.getSqlPara("termuser.finterruser",
						Kv.by("tablename", termUserTableName).set("FGUID", record.get("SubsGUID")))).get("FFreezed")
						.toString().equals("1")) {
					// 说明此用户已经被冻结无法进行鉴权
					authentication.set("state", "当前鉴权的用户已经被冻结,鉴权失败");
					authentication.set("subcode", "2");
					return authentication;
				}

				// 开始鉴权,首先按照包月加球队鉴权
				Record recordVOD = Db.findFirst(Db.getSqlPara("termuser.VODauthentication",
						Kv.by("FSubscriberPackageGUID", subguid.get("FGUID")).set("tablename", tU_SPTableName)
								.set("FUserGUID", record.get("SubsGUID").toString())
								.set("newTime", TimeUT.getCurrTime())));

				// 说明该用户有开通该球队或者开通包月业务并且处于有效期鉴权通过返回点播串

				if (Integer.parseInt(recordVOD.get("num").toString()) > 0
						&& System.currentTimeMillis() < TimeUT.getMillsTime(recordVOD.get("FExpiryTime").toString())) {
					// 鉴权通过 检查当前业务是否已下架，或者已过期

					if (System
							.currentTimeMillis() > TimeUT
									.getMillsTime(Db
											.findFirst(
													Db.getSqlPara(
															"termuser.TVODauthenticationtime", Kv
																	.by("tablename", Db.findFirst(Db.getSqlPara(
																			"termuser.TVODauthentication",
																			Kv.by("tablename", tU_SPTableName)
																					.set("FSubsGUID",
																							record.get("SubsGUID")
																									.toString())
																					.set("FSubscriberPackageGUID",
																							subguid.get("FGUID"))))
																			.get("tablename"))
																	.set("FGUID", subguid.get("FGUID"))))
											.getStr("FEfectivetime"))
							|| Db.findFirst(
									Db.getSqlPara(
											"termuser.TVODauthenticationtime", Kv
													.by("tablename", Db
															.findFirst(Db.getSqlPara("termuser.TVODauthentication",
																	Kv.by("tablename", tU_SPTableName)
																			.set("FSubsGUID",
																					record.get("SubsGUID").toString())
																			.set("FSubscriberPackageGUID",
																					subguid.get("FGUID"))))
															.get("tablename"))
													.set("FGUID", subguid.get("FGUID"))))
									.getStr("FFreezed").equals("1")) {
						// 说明当前业务包已经过期，无法继续使用,鉴权失败，记录日志
						saveaythentication(record.get("SubsGUID").toString(), params, subguid.get("FGUID").toString(),
								"1");
						authentication.set("state", "该业务已下架,鉴权失败");
						authentication.set("subcode", "3");
						authentication.set("sublist", subRecord);
						return authentication;
					}
					;
					// 鉴权成功，记录日志，返回点播串
					saveaythentication(record.get("SubsGUID").toString(), params, subguid.get("FGUID").toString(), "0");
					authentication.set("Demandstring",
							"http://192.168.0.51:8080/UAMS/upload/uploadMedia/5ddfa1852077b800da31489397457ead.mp4");

					return authentication;
				}
				// 如果没有开通包月业务或者没有开通球队业务 再对按次业务进行鉴权
				Record recordVODsequence = Db.findFirst(Db.getSqlPara("termuser.VODauthentication",
						Kv.by("FSubscriberPackageGUID", subguid.get("FGUID"))
								.set("FUserGUID", record.get("SubsGUID").toString()).set("tablename", tU_SPTableName)
								.set("newTime", TimeUT.getCurrTime())));
				// 说明该用户有开通该单次点播业务并且处于有效期鉴权通过返回点播串
				if (Integer.parseInt(recordVODsequence.get("num").toString()) > 0
						&& System.currentTimeMillis() < TimeUT.getMillsTime(recordVOD.get("FExpiryTime").toString())) {
					// 鉴权通过 检查当前业务是否已下架，或者已过期
					if (System
							.currentTimeMillis() > TimeUT
									.getMillsTime(Db
											.findFirst(
													Db.getSqlPara(
															"termuser.TVODauthenticationtime", Kv
																	.by("tablename", Db.findFirst(Db.getSqlPara(
																			"termuser.TVODauthentication",
																			Kv.by("tablename", tU_SPTableName)
																					.set("FSubsGUID",
																							record.get("SubsGUID")
																									.toString())
																					.set("FSubscriberPackageGUID",
																							subguid.get("FGUID"))))
																			.get("tablename"))
																	.set("FGUID", subguid.get("FGUID"))))
											.getStr("FEfectivetime"))
							|| Db.findFirst(
									Db.getSqlPara(
											"termuser.TVODauthenticationtime", Kv
													.by("tablename", Db
															.findFirst(Db.getSqlPara("termuser.TVODauthentication",
																	Kv.by("tablename", tU_SPTableName)
																			.set("FSubsGUID",
																					record.get("SubsGUID").toString())
																			.set("FSubscriberPackageGUID",
																					subguid.get("FGUID"))))
															.get("tablename"))
													.set("FGUID", subguid.get("FGUID"))))
									.getStr("FFreezed").equals("1")) {
						// 说明当前业务包已经过期，无法继续使用
						saveaythentication(record.get("SubsGUID").toString(), params, subguid.get("FGUID").toString(),
								"1");
						authentication.set("state", "该业务已下架,鉴权失败");
						authentication.set("subcode", "3");
						authentication.set("sublist", subRecord);

						return authentication;
					}
					;
					// 鉴权成功，记录日志返回点播串
					saveaythentication(record.get("SubsGUID").toString(), params, subguid.get("FGUID").toString(), "0");
					authentication.set("Demandstring",
							"http://192.168.0.51:8080/UAMS/upload/uploadMedia/5ddfa1852077b800da31489397457ead.mp4");
					authentication.set("state", "鉴权通过");
					authentication.set("subcode", "0");
					return authentication;
				} else {
					// 该用户还没有开通相关业务
					// 记录日志
					saveaythentication(record.get("SubsGUID").toString(), params, subguid.get("FGUID").toString(), "1");
					// 可订购业务包列表
					authentication.set("state", "该用户还没有开通相关业务");
					authentication.set("subcode", "6");
					authentication.set("sublist", subRecord);
					return authentication;
				}

				// return authentication;
			} else {

				authentication.set("state", "无此用户信息");
				authentication.set("subcode", "4");
			}

			// 栏目ID和用户ID作为条件查询用户开通业务包的映射表
		} catch (Exception e) {
			e.printStackTrace();
			authentication.set("state", "异常");
			authentication.set("subcode", "5");
		}

		return authentication;
	}

	/**
	 * 鉴权日志记录用方法
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */
	public void saveaythentication(String FUSER_ID, String FTERMINAL_ID, String FPRODUCT_CODE, String FSUCCESS) {
		Record record = new Record();
		record.set("FAUTHEN_TIME", TimeUT.getCurrTime()).set("FUSER_ID", FUSER_ID).set("FTERMINAL_ID", FTERMINAL_ID)
				.set("FPRODUCT_CODE", FPRODUCT_CODE).set("FSUCCESS", FSUCCESS).set("FCREATETIME", TimeUT.getCurrTime());

		Db.save(T_Authentication_log.tableName, record);

	}

	/**
	 * 查询终端用户可开通的产品(未开通，包括业务包和策略包）
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record getByTermUserbidsub(String param) throws Exception {
		Record recordteruser = new Record();
		try {

			Record records = new Record();
			String params = "";
			JSONObject paramData = JSONObject.fromObject(param);
			String FCaCard = (String) paramData.get("FCaCard");// ca卡号
			String FSerial = (String) paramData.get("FSerial");// 序列号
			String FMac = (String) paramData.get("FMac");// mac地址
			Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
			Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
			if (FCaCard != null) {
				params = " FCaCard='" + FCaCard + "'";
				records.set("FCaCard", FCaCard);
			}
			if (FSerial != null) {
				params = " FSerial='" + FSerial + "'";
				records.set("FSerial", FSerial);
			}
			if (FMac != null) {
				params = " FMac='" + FMac + "'";
				records.set("FMac", FMac);
			}

			// 获取用户的GUID
			SqlPara sqlPara = Db.getSqlPara("termuser.hasSubproduct", Kv.by("tablename", T_TermUserTermInfo.tableName)
					.set("tablename1", terminfotableName).set("params", params));
			Record record = Db.findFirst(sqlPara);
			// 可以开通的业务包
			if (pageSize == null && page == null) {
				List<Record> subRecord = Db.find((Db.getSqlPara("termuser.getByTermUserbidsub",
						Kv.by("tablename", subscriberPackageTableName).set("tablename1", T_Strategy.tableName)
								.set("tablename2", tU_SPTableName)
								.set("SubsGUID", record.get("SubsGUID").toString()))));
				recordteruser.set("sublist", subRecord);

			} else {
				Page<Record> subRecord = Db.paginate(page, pageSize,
						(Db.getSqlPara("termuser.getByTermUserbidsub",
								Kv.by("tablename", subscriberPackageTableName).set("tablename1", T_Strategy.tableName)
										.set("tablename2", tU_SPTableName)
										.set("SubsGUID", record.get("SubsGUID").toString()))));
				recordteruser.set("sublist", subRecord);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordteruser;

	}

	/**
	 * 获取根据卡号 返回对应用户信息以及对应开通的业务包
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */

	@Override
	public Record getBossUserInfo(String FGUid) throws Exception {

		Record teruserecord = Db.findFirst(Db.getSqlPara("termuser.GetBossterminone",
				Kv.by("tablename", T_TermUser.tableName).set("FGUID", FGUid)));
		if (teruserecord == null) {
			throw new Exception("FGUID不合法");
		}
		// List<Record>subscriberPackage=Db.find(Db.getSqlPara("termuser.GetBosstermin",
		// Kv.by("tablename",T_SubscriberPackage.tableName)
		// .set("tablename1",T_TermUserSubscriberPackage.tableName).set("FSubsGUID",teruserecord.get("FGUID"))));

		return teruserecord;
	}

	/**
	 * 获取根据卡号 返回对应用户信息以及对应开通的业务包
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */
	@Override
	public Record getBossUserSubscriberPackage(String param) throws Exception {

		JSONObject paramData = JSONObject.fromObject(param);
		Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
		String guid = (String) paramData.get("guid");
		String packageid = (String) paramData.get("packageid");
		Record teruserecord = Db.findFirst(Db.getSqlPara("termuser.GetBossterminone",
				Kv.by("tablename", T_TermUser.tableName).set("FGUID", guid)));

		if (teruserecord == null) {
			throw new Exception("用户不合法");

		}
		if (StringUtils.hasText(guid) && StringUtils.hasText(packageid)) {
			// 说明查询的是该用户订购的的对应业务包信息

			Record subscriberPackage = Db.findFirst(Db.getSqlPara("termuser.GetBosstermintwo",
					Kv.by("tablename", T_SubscriberPackage.tableName).set("FGUID", packageid)));
			teruserecord.set("subscriberPackage", subscriberPackage);

		} else {
			// 说明查询的是该用户订购的所有订购表列表
			Page<Record> subscriberPackage = Db.paginate(page, pageSize,
					(Db.getSqlPara("termuser.GetBosstermin", Kv.by("tablename", T_SubscriberPackage.tableName)
							.set("tablename1", T_TermUserSubscriberPackage.tableName).set("FSubsGUID", guid))));
			teruserecord.set("subscriberPackage", subscriberPackage);

		}

		return teruserecord;
	}

	/**
	 * 点播认证
	 * 
	 * @author ZKill
	 * @create 2018年3月2日 16:17:50
	 * @update
	 * @param
	 * @return Record
	 */

	@FunctionalInterface
	interface MessageFactory<M extends Message> {
		M create(String Message);
	}

	@Override
	public Record demanDcertification(String param, HttpServletRequest request) throws Exception {

		Record record = new Record();

		// 获取到客户端IP
		String IP = request.getRemoteAddr();

		JSONObject paramData = JSONObject.fromObject(param);
		Optional<String> ICardop = null;
		Optional<String> Token = null;
		System.out.println(IP);
		// 金顶盒卡号
		if (param.contains("ICard")) {
			ICardop = Optional.ofNullable(paramData.getString("ICard").trim());

			if (ICardop.hashCode() == 0) {
				throw new Exception("ICardop It is a must pass parameter!!!");
			} else {

				if (Optional
						.ofNullable(Db.findFirst(
								Db.getSqlPara("subscriberpackage.selects", Kv.by("tablename", T_TermInfo.tableName)
										.set("d", "*").set("wheres", " FCaCard='" + ICardop.get() + "'"))))
						.hashCode() == 0) {
					record.set("resultstate", "301");
					record.set("data", "找不到相关卡号信息,请检查CA卡号是否正确");
					return record;
				}

			}
		}

		// 当前登录用户的token;
		if (param.contains("Token")) {
			Token = Optional.ofNullable(paramData.getString("Token").trim());
			if (Token.hashCode() == 0)
				throw new Exception("Token It is a must pass parameter!!!(传入的token值不能为空）");

		}
		if (!param.contains("Demand"))
			throw new Exception("Demand It is a must pass parameter!!!(Demand 是必传参数!!!)");

		Optional<String> Demand = Optional.ofNullable(paramData.getString("Demand").trim());// 丢点播资源信息

		if (Demand.hashCode() == 0)
			throw new Exception("Demand Value can not be empty!!!(Demand 参数值不能为空)");

		if (Token == null && ICardop == null)
			throw new Exception(
					"The number of necessary parameters does not conform to the interface specification!!!(传入参数个数不符合接口规范)");

		if (Token != null && ICardop != null)
			throw new Exception(
					"The number of necessary parameters does not conform to the interface specification!!!(传入参数个数不符合接口规范)");
		// Optional<String> ICardop =
		// Optional.ofNullable(paramData.getString("ICard"));// 金顶盒卡号
		// 参数验证

		String FPrice = "";
		// 判断该资源是否免费
		Optional<Record> FPeiceop = Optional.ofNullable(Db
				.findFirst(Db.getSqlPara("subscriberpackage.selects", Kv.by("tablename", T_SubscriberPackage.tableName)
						.set("d", "FPrice").set("wheres", "FGUID='" + Demand.get() + "'"))));
		if (FPeiceop.hashCode() != 0) {
			FPrice = FPeiceop.get().getStr("FPrice");
		} else {
			record.set("resultstate", "302");
			record.set("data", "找不到相关业务信息,请检查点播资源标识是否有误");
			return record;
		}
		;

		Function<String, Integer> f = Integer::parseInt;
		if (f.apply(FPrice) == 0) {
			System.out.println("本次点播的资源免费,返回点播token");
			// 说明是免费资源，生成Des3加密后的点播token返回
			if (param.contains("ICard")) {
				ICardop.ifPresent(value -> {
					try {
						record.set("resultstate", "200");
						record.set("data", CreateDestoken(value, IP, ""));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}

			if (param.contains("Token")) {
				Token.ifPresent(value -> {
					try {
						record.set("resultstate", "200");
						record.set("data", CreateDestoken(value, IP, ""));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}

		} else {
			// 说明是非免费资源
			if (param.contains("ICard")) {
				ICardop.ifPresent(value -> {
					// 判断盒子用户卡号是否开通相关业务
					Record Business = Db.findFirst(Db.getSqlPara("app.DemanDcertificationtwo",
							Kv.by("tablename", T_TermUserSubscriberPackage.tableName)
									.set("tablename1", T_TermUserTermInfo.tableName)
									.set("tablename2", T_TermInfo.tableName).set("FCaCard", value)
									.set("FSubscriberPackageGUID", Demand.get())));
					try {
						Record BusinessRecord = BusinessTesting(Business, value, IP, Demand.get());
						record.set("resultstate", BusinessRecord.get("resultstate"));
						record.set("data", BusinessRecord.get("data"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});
			}

			if (param.contains("Token")) {
				Token.ifPresent(value -> {
					// 判断APP用户是否登录
					Properties po;
					Jedis jedis = null;
					try {
						po = ApiUtils.getproperties(this.getClass(), "jdbc.properties");
						Cache cache = Redis.use(po.get("redis.name").toString());
						jedis = cache.getJedis();
						String FAppuserguid = jedis.get(value);
						if (Optional.ofNullable(FAppuserguid).hashCode() == 0) {
							// 当前用户未登录引导登录
							record.set("resultstate", "510");
							record.set("data", "当前用户还未登录,请先登录再继续点播操作");

						} else {
							// App用户已经登录，检查用户是否有权限观看内容
							Record Business = Db.findFirst(Db.getSqlPara("app.DemanDcertification",
									Kv.by("tablename", T_TermUserSubscriberPackage.tableName)
											.set("tablename1", T_termuser_appuser.tableName)
											.set("FAppuserguid", FAppuserguid)
											.set("FSubscriberPackageGUID", Demand.get())));
							Record BusinessRecord = BusinessTesting(Business, value, IP, Demand.get());
							record.set("resultstate", BusinessRecord.get("resultstate"));
							record.set("data", BusinessRecord.get("data"));
						}
					} catch (IOException | ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						// 释放资源;
						jedis.close();
					}

				});
			}

		}

		/*
		 * String ICard = ICardop.orElseGet(() -> { try { throw new
		 * Exception("ICard values can not be empty!!!"); } catch (Exception e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } return
		 * "values can not be empty"; });
		 */

		return record;
	}

	/**
	 * 业务用检测方法
	 * 
	 * @author ZKill
	 * @create 2018年3月2日 17:17:47
	 * @update
	 * @param
	 * @return Record
	 * @throws Exception
	 */
	public Record BusinessTesting(Record Business, String value, String IP, String Demand) throws ParseException {
		Record record = new Record();
		Record T_sub = Db
				.findFirst(Db.getSqlPara("subscriberpackage.selects", Kv.by("tablename", T_SubscriberPackage.tableName)
						.set("d", " * ").set("wheres", " FGUID='" + Demand + "'")));
		if (Optional.ofNullable(Business).hashCode() != 0) {
			// 说明当前用户有权限观看当前点播资源（并且当前业务处于正常状态)，返回点播资源
			try {
				boolean flag = true;
				LongSupplier S = System::currentTimeMillis;
				if (S.getAsLong() < TimeUT.getMillsTime(T_sub.get("FAvailabilityTime"))) {
					record.set("resultstate", "511");
					record.set("data", "当前业务已过期，无法正常使用");
					flag = false;
				}
				if (T_sub.get("FFreezed").equals("2")) {
					record.set("resultstate", "512");
					record.set("data", "当前业务处于暂停状态,无法正常使用");
					flag = false;
				}
				if (T_sub.get("FFreezed").equals("1")) {
					record.set("resultstate", "513");
					record.set("data", "当前业务处于冻结状态,无法正常点播");
					flag = false;
				}

				if (T_sub.get("FDeleted").equals("1")) {
					record.set("resultstate", "514");
					record.set("data", "当前业务处于已被移除,无法正常点播");
					flag = false;
				}
				if (flag) {
					// 返回点播token
					record.set("resultstate", "200");
					record.set("data", CreateDestoken(value, IP, ""));

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// 当前用户无权限观看当前点播资源 引导开通相关业务(返回相关业务信息);
			boolean flag = true;
			LongSupplier S = System::currentTimeMillis;
			if (S.getAsLong() < TimeUT.getMillsTime(T_sub.get("FAvailabilityTime"))) {
				record.set("resultstate", "515");
				record.set("data", "当前业务已下架,没有相关业务信息");
				flag = false;
			}
			if (T_sub.get("FFreezed").equals("2")) {
				record.set("resultstate", "516");
				record.set("data", "当前业务处于暂停状态,无法订购,请选择其他资源信息");
				flag = false;
			}
			if (T_sub.get("FFreezed").equals("1")) {
				record.set("resultstate", "517");
				record.set("data", "当前业务处于冻结状态,无法订购，请选择其他资源信息");
				flag = false;
			}
			if (T_sub.get("FDeleted").equals("1")) {
				record.set("resultstate", "518");
				record.set("data", "当前业务处于已被移除,请选择其他资源信息");
				flag = false;
			}
			if (flag) {
				// 返回当前业务信息
				record.set("resultstate", "201");
				record.set("data", T_sub);
			}
		}

		return record;
	}

	/**
	 * 生成点播密钥串
	 * 
	 * @author ZKill
	 * @create 2018年3月2日 16:17:35
	 * @update
	 * @param
	 * @return String
	 * @throws Exception
	 */
	public String CreateDestoken(String ICardop, String ip, String token) throws Exception {
		LongSupplier S = System::currentTimeMillis;
		Properties popo = ApiUtils.getproperties(T_TermUserServiceImpl.class, "Destoken.properties");
		String Dcertificationtoken = popo.getProperty("Dcertificationtoken");// 共享密钥
		String Destoken = ip + ICardop + token + S.getAsLong() + Dcertificationtoken;
		Destoken = Des3.generateAuthenticator(Destoken);
		System.out.println("本次Des3加密的点播token:" + Destoken);
		// 将时间戳放入 缓存中,设置过期时间(目前缓存配置时间为两分钟）
		CacheKit.put("UBAS_BOSS_Destoken", ip, S.getAsLong());
		return Destoken;
	}

	/**
	 * 媒资 服务器根据标识获取缓存中的时间戳
	 * 
	 * @author ZKill
	 * @create 2018年3月2日 16:17:35
	 * @update
	 * @param
	 * @return String
	 * @throws Exception
	 */

	public String UAGetTOken(String IP) throws Exception {

		String Timestamp = "";
		System.out.println(CacheKit.get("UBAS_BOSS_Destoken", IP) + "");
		Optional<Long> Timestampop = Optional.ofNullable(CacheKit.get("UBAS_BOSS_Destoken", IP));
		if (Timestampop.hashCode() == 0) {
			// 缓存中无此标识记录(没有进行点播认证,或认证已过期)
			return "-1";
		}
		Timestamp = Timestampop.get().toString();

		return Timestamp;
	}

	

	/**
	 * UBAS BOSS数据导入
	 * 
	 * @author ZKill
	 * @create 2017年7月17日 15:38:12
	 * @update
	 * @param
	 * @return Record
	 */

	/*
	 * public List<Record> Bossdataimport(String filePath, String Sitvkey[])
	 * throws Exception { // 创建对Excel工作簿文件的引用­ HSSFWorkbook hssfWorkbook = new
	 * HSSFWorkbook(new FileInputStream(filePath)); //HSSFSheet sheet =
	 * hssfWorkbook.getSheetAt(0); int begin = sheet.getFirstRowNum(); int end =
	 * sheet.getLastRowNum(); Record record; if
	 * (sheet.getRow(0).getCell(0).equals("CUSTOMER_NAME")) { // 说明是用户信息表 } else
	 * if (sheet.getRow(0).getCell(0).equals("USER_CODE")) { // 说明是大理订购关系信息表 }
	 * List<Record> list = new ArrayList<Record>(); for (int i = begin + 1; i <
	 * end; i++) { record = new Record(); for (int j = begin + 1; j <
	 * sheet.getRow(0).getPhysicalNumberOfCells(); j++) { if
	 * (sheet.getRow(i).getCell(j) == null ||
	 * sheet.getRow(i).getCell(j).toString().length() == 0) { record.clear();
	 * break; } else { record.set(Sitvkey[j],
	 * sheet.getRow(i).getCell(j).toString()); }
	 * 
	 * } list.add(record); } return list; }
	 * 
	 * }
	 */
	@Override
	public List<Record> getSystemtermuser() throws Exception {
		return Db.find("SELECT F_Name as name,F_Guid as guid FROM " + T_System_termuser.tableName);
	}

}
