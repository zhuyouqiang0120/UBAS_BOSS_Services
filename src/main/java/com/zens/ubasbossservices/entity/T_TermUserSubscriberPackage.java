/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS-SERVICE
 **    Package Name : com.zens.ubasservice.entity								 
 **    Type    Name : TUser 							     	
 **    Create  Time : 2016年9月19日 下午2:19:54								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.entity;


import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.zens.ubasbossservices.dao.TermUserSubscriberPackageDao;
import com.zens.ubasbossservices.utils.TimeUT;



/**
 * 
 * @author zyq
 * @e-mail zhuyq@zensvision.com
 * @date 2016年10月20日 上午12:09:15
 */
public class T_TermUserSubscriberPackage extends Model<T_TermUserSubscriberPackage>
		implements TermUserSubscriberPackageDao {

	private static final long serialVersionUID = 1L;

	public static T_TermUserSubscriberPackage dao = new T_TermUserSubscriberPackage();

	public static String tableName = TableMapping.me().getTable(T_TermUserSubscriberPackage.class).getName();

	/*
	 * 判断某个终端用户是否已开通了某个业务包并且还没有失效。
	 * 
	 * @see com.zens.ubasbossservices.dao.TermUserSubscriberPackageDao#
	 * hasEffectiveRecord(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasEffectiveRecord(String subsGUID, String subscriberPackageGUID) throws Exception {
		String sql = "SELECT * FROM " + tableName
				+ " WHERE FSubsGUID = ? AND FSubscriberPackageGUID = ? AND FExpiryTime > ? AND FDeleted!=1 ";
		String currentTime = TimeUT.getCurrTime();
		Record record = Db.findFirst(sql, subsGUID, subscriberPackageGUID, currentTime);
		if (record != null) {
			return true;
		}
		return false;
	}

	/*
	 * 为指定的用户暂停某个业务包（这个关联记录需在有效期内）
	 * 
	 * @see
	 * com.zens.ubasbossservices.dao.TermUserSubscriberPackageDao#suspendRecord(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean suspendRecord(String subsGUID, String subscriberPackageGUID) throws Exception {
		String currentTime = TimeUT.getCurrTime();
		String sql = " UPDATE " + tableName + " SET FSuspended = '1' AND FSuspendTime = ? ";
		return Db.update(sql, currentTime) > 0;
	}

	/*
	 * 为指定的用户继续某个业务包
	 * 
	 * @see
	 * com.zens.ubasbossservices.dao.TermUserSubscriberPackageDao#continueRecord
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public boolean continueRecord(String subsGUID, String subscriberPackageGUID) throws Exception {
		String currentTime = TimeUT.getCurrTime();
		String sql = "SELECT * FROM " + tableName
				+ " WHERE FSubsGUID = ? AND FSubscriberPackageGUID = ? AND FSuspended = '1' AND FFreezed = '0' AND FDeleted = '0'";
		T_TermUserSubscriberPackage t_TermUserSubscriberPackage = T_TermUserSubscriberPackage.dao.findFirst(sql,
				subsGUID, subscriberPackageGUID);
		t_TermUserSubscriberPackage.set("FSuspended", "0");
		String suspendTime = t_TermUserSubscriberPackage.getStr("FSuspendTime");
		String oldExpiryTime = t_TermUserSubscriberPackage.getStr("FSuspendTime");
		long interval = TimeUT.getMillsTime(suspendTime) - TimeUT.getMillsTime(currentTime);
		String newExpiryTime = TimeUT.getTime(TimeUT.getMillsTime(oldExpiryTime) + interval);
		t_TermUserSubscriberPackage.set("FExpiryTime", newExpiryTime);
		t_TermUserSubscriberPackage.set("FSuspendTime", "");
		return t_TermUserSubscriberPackage.save();
	}

	/*
	 * 为指定的终端用户继续指定的业务包
	 * 
	 * @see
	 * com.zens.ubasbossservices.dao.TermUserSubscriberPackageDao#renewRecord(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Record renewRecord(String subsGUID, String subscriberPackageGUID, String newExpiryTime, Record substr,
			String cycle) throws Exception {
		// 查询账户余是否够延期
		// 此账户余额
		Record record = new Record();
		Integer accountmoney = Integer.parseInt(Db
				.findFirst(Db.getSqlPara("termuser.bindTermUser_SubscriberPackages_checkbalance",
						Kv.by("tablename", T_Account.tableName).set("FUserGUID", subsGUID)))
				.get("FAccountbalance").toString());
		if (substr.get("FLifecycle") != null) {
			// 续订要扣的钱
			Integer strategmoney = Integer.parseInt(cycle) / Integer.parseInt(substr.get("FLifecycle").toString())
					* Integer.parseInt(substr.get("FPrice").toString());

			//
			if (accountmoney < strategmoney) {
				// 账户余额不足,无法续订
				record.set(subsGUID, subscriberPackageGUID).set("state", "账户余额不足,无法续订").set("money",
						strategmoney - accountmoney);
				return record;
			}
			if (Db.update(Db
					.getSqlPara("termuser.bindTermUser_SubscriberPackages_feededuction",
							Kv.by("tablename", T_Account.tableName).set("FAccountbalance", accountmoney - strategmoney)
									.set("FUpdatetime", TimeUT.getCurrTime()).set("FUserGUID", subsGUID))
					.getSql()) == 0) {
				// 扣费失败
				record.set(subsGUID, subscriberPackageGUID).set("state", "扣费失败，续订策略包失败");// 扣费失败，开通失败状态码

				return record;
			}
		}
		// 余额足够，进行扣费
		if (substr.get("FCycle") != null) {
			// 续订要扣的钱
			Integer strategmoney = Integer.parseInt(cycle) / Integer.parseInt(substr.get("FCycle").toString())
					* Integer.parseInt(substr.get("FPrice").toString());
			if (accountmoney < strategmoney) {
				// 账户余额不足,无法续订
				record.set(subsGUID, subscriberPackageGUID).set("state", "账户余额不足,无法续订").set("money",
						strategmoney - accountmoney);
				return record;
			}

			if (Db.update(Db
					.getSqlPara("termuser.bindTermUser_SubscriberPackages_feededuction",
							Kv.by("tablename", T_Account.tableName).set("FAccountbalance", accountmoney - strategmoney)
									.set("FUpdatetime", TimeUT.getCurrTime()).set("FUserGUID", subsGUID))
					.getSql()) == 0) {
				// 扣费失败
				record.set(subsGUID, subscriberPackageGUID).set("state", "扣费失败，续订业务包失败");// 扣费失败，开通失败状态码
				return record;

			}
		}
		
		System.out.println(newExpiryTime);
		//扣費成功，续订业务包
		String updateTime = TimeUT.getCurrTime();
		String sql = "UPDATE " + tableName + " SET "
				+ " FExpiryTime = ?,FUpdateTime = ? WHERE FsubsGUID = ? AND FSubscriberPackageGUID = ? AND FDeleted = '0' AND FFreezed = '0' ";
		if (Db.update(sql, newExpiryTime, updateTime, subsGUID, subscriberPackageGUID) > 0) {
			record.set(subsGUID, subscriberPackageGUID).set("state", "续订成功");
			//续订成功，记录订购日志
		}
		;
		return record;
     }	
	}