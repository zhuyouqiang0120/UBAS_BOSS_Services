package com.zens.ubasbossservices.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.chasonx.tools.StringUtils;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.template.stat.ast.Return;
import com.jfinal.upload.MultipartRequest;
import com.sun.jndi.dns.ResourceRecord;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.Constant;
import com.zens.ubasbossservices.common.Tools;
import com.zens.ubasbossservices.controller.UBAS_APPControllers;
import com.zens.ubasbossservices.entity.T_App_User;
import com.zens.ubasbossservices.entity.T_Scavenging;
import com.zens.ubasbossservices.entity.T_TermInfo;
import com.zens.ubasbossservices.entity.T_TermUser;
import com.zens.ubasbossservices.entity.T_TermUserTermInfo;
import com.zens.ubasbossservices.entity.T_termuser_appuser;
import com.zens.ubasbossservices.entity.t_Appuser_Resource;
import com.zens.ubasbossservices.log.APPUserModulelog;
import com.zens.ubasbossservices.service.AppService;
import com.zens.ubasbossservices.utils.AppUTil;
import com.zens.ubasbossservices.utils.TimeUT;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.net.aso.MD5;
import redis.clients.jedis.Jedis;

public class AppServicelmpl implements AppService {
	/**
	 * 注册APP用户(向用户手机发送验证码）
	 * 
	 * @author ZKill
	 * @create 2017年12月1日 11:02:17
	 * @update
	 * @param
	 * @return void
	 */
	@Override
	public Record cappuserVerificationcode(String param) throws Exception {
		Record record = new Record();
		JSONObject jsonObject = JSONObject.fromObject(param);
		// 获取注册用户手机的手机号
		String FTelephone = (String) jsonObject.get("FTelephone");
		String type = jsonObject.getString("type");
		APPUserModulelog.server("当前发送验证码的手机号是" + FTelephone);
		// 生成随机验证码

		String FVerificationcode = AppUTil.AccountRandom();
		APPUserModulelog.server("当前生成的七位数验证码是" + FVerificationcode);
		List<String> list = new ArrayList<>();
		list.add("FAccount");
		Record records = Db.findFirst(Db.getSqlPara("app.selectcappuserVerificationcode",
				Kv.by("tablename", T_App_User.tableName).set("FTelephone", FTelephone)));
		Kv cond = Kv.by("FTelephone", FTelephone);
		if (type.equals("0")) {
			if (Db.findFirst(Db.getSqlPara("app.selectapp",
					Kv.by("tablename", T_App_User.tableName).set("list", list).set("cond", cond))) != null) {
				APPUserModulelog.server(FTelephone + "号码已经绑定了帐号,请更换手机号");
				record.set("data", "此号码已经绑定了帐号,请更换手机号");
				record.set("resultstate", "10002");
				return record;
			}
		}
		if (type.equals("1")) {
			// 获取此账户是否存在
			if (records == null) {
				APPUserModulelog.server(FTelephone + "此帐号不存在,发送验证码失败");
				record.set("data", FTelephone + "此帐号不存在,发送验证码失败");
				record.set("resultstate", "10005");
			}

		}
		boolean reslut = AppUTil.Short(FVerificationcode, FTelephone);
		if (reslut) {
			// 短信发送成功，记录库中
			APPUserModulelog.server(FTelephone + "验证码发送成功");
			Kv cond1 = Kv.by("FGuid", UUID.randomUUID()).set("FVerificationcode", FVerificationcode)
					.set("FVerificationcodetime", TimeUT.getCurrTime()).set("FTelephone", FTelephone);

			if (records != null) {
				// 说明已经发送过验证码
				Kv cond2 = Kv.by("FVerificationcode", FVerificationcode).set("FVerificationcodetime",
						TimeUT.getCurrTime());
				if (Db.update(Db.getSqlPara("app.cappuserVerificationcodeupdate",
						Kv.by("cond", cond2).set("FTelephone", FTelephone).set("tablename", T_App_User.tableName))
						.getSql()) > 0) {
					record.set("data", "发送验证码成功");
					record.set("resultstate", "SUCCESS");
					APPUserModulelog.server(FTelephone + "注册APP,发送验证码成功");
				} else {
					APPUserModulelog.server(FTelephone + "验证码存入库失败,系统错误");
					record.set("data", "系统错误");
					record.set("resultstate", "10006");
				}
			} else {
				if (Db.update(Db.getSqlPara("app.cappuserVerificationcode",
						Kv.by("tablename", T_App_User.tableName).set("cond", cond1)).getSql()) > 0) {
					record.set("data", "发送验证码成功");
					record.set("resultstate", "SUCCESS");
					APPUserModulelog.server(FTelephone + "注册APP,发送验证码成功");
				} else {
					APPUserModulelog.server(FTelephone + "验证码存入库失败,系统错误");
					record.set("data", "系统错误");
					record.set("resultstate", "10006");
				}
			}

		} else {

			record.set("data", "验证码发送失败");
			record.set("resultstate", "10003");
			APPUserModulelog.server(FTelephone + "验证码发送失败");
			return record;

		}

		return record;
	}

	/**
	 * 注册APP用户(验证验证码后注册成功）
	 * 
	 * @author ZKill
	 * @create 2017年5月24日 12:51:41
	 * @update
	 * @param
	 * @return void
	 */

	@Override
	public Record cappuser(String param) throws Exception {
		Record record = new Record();
		JSONObject jsonObject = JSONObject.fromObject(param);
		String FTelephone = (String) jsonObject.get("FTelephone");
		APPUserModulelog.server("APP用户注册当前待验证的手机号是" + FTelephone);
		String appFVerificationcode = (String) jsonObject.get("FVerificationcode");
		APPUserModulelog.server("APP用户注册当前待验证的验证码是" + appFVerificationcode);
		// 获取库里的验证码进行验证
		Record coderecord = Db.findFirst(
				Db.getSqlPara("app.cappuser", Kv.by("tablename", T_App_User.tableName).set("FTelephone", FTelephone)));
		// 获取到验证码
		if (coderecord == null) {
			record.set("data", "没有号码" + FTelephone + "的注册记录,注册失败");
			record.set("resultstate", "20002");
			APPUserModulelog.server("没有号码" + FTelephone + "的注册记录,注册失败");
			return record;
		}
		// 获取到验证码
		String VerificationCode = coderecord.get("FVerificationcode");
		// 获取到该验证码的创建时间
		String FVerificationcodetime = coderecord.get("FVerificationcodetime");
		// APPUserModulelog.server("我就记录个日志试试");
		try {

			// 获取到当前系统时间
			if (System.currentTimeMillis() - TimeUT.getMillsTime(FVerificationcodetime) > 60000) {
				// 说明该验证码已超时,注册失败
				record.set("data", "电话" + FTelephone + "验证码已超时,注册失败");
				record.set("resultstate", "20003");
				return record;
			}
			if (!VerificationCode.equals(appFVerificationcode)) {
				APPUserModulelog.server("电话" + FTelephone + "用户提交的验证码错误,注册失败");
				record.set("resultstate", "20004");
				return record;
			}
			String FAccount = FAccount();

			// 创建APP账户

			if (Db.update(Db.getSqlPara("app.cappuserVerificationcodeupdate",
					Kv.by("tablename", T_App_User.tableName)
							.set("cond", Kv.by("FAccount", FAccount).set("FCreatetime", TimeUT.getCurrTime()))
							.set("FTelephone", FTelephone))
					.getSql()) > 0) {
				APPUserModulelog.server("号码" + FTelephone + "的注册成功");
				record.set("data", "号码" + FTelephone + "的注册成功");
				record.set("resultstate", "SUCCESS");
			}
			;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;
	}

	// 检查帐号是否存在,存在就重新生成
	public String FAccount() {
		String FAccounts = AppUTil.AccountRandomc();
		if (Integer.parseInt(Db
				.findFirst(Db.getSqlPara("app.selectcappusers",
						Kv.by("tablename", T_App_User.tableName).set("FAccount", FAccounts)))
				.get("num").toString()) > 0) {
			// 说明帐号已经存在，重新生成
			FAccount();
		}
		return FAccounts;
	}

	/**
	 * 分页获取APP用户列表(）
	 * 
	 * @author ZKill
	 * @create 2017年5月24日 12:51:41
	 * @update
	 * @param
	 * @return Record
	 */

	@Override
	public Record selectAppuser(String param) throws Exception {
		Record record = new Record();
		JSONObject paramJO = JSONObject.fromObject(param);
		Integer pageSize = StringUtils.hasText((String) paramJO.get("pageSize"))
				? Integer.parseInt((String) paramJO.get("pageSize")) : null;
		Integer page = StringUtils.hasText((String) paramJO.get("page"))
				? Integer.parseInt((String) paramJO.get("page")) : null;
		List<String> list = new ArrayList<>();
		Kv cond = new Kv();
		if (param.contains("keyword")) {
			String keyword = paramJO.getString("keyword");
			cond.set("keyword", keyword);
		}
		if (param.contains("FState")) {
			list.add("FState");

		}
		if (param.contains("FSex")) {
			list.add("FSex");

		}
		for (int i = list.size() - 1; i >= 0; i--) {
			if (paramJO.getString(list.get(i)) != null) {
				cond.set(list.get(i), paramJO.getString(list.get(i)));
			}
		}
		List<String> keyword = new ArrayList<>();
		keyword.add("FAccount");
		keyword.add("FSex");
		keyword.add("FTelephone");
		keyword.add("FName");
		keyword.add("FCreatetime");
		keyword.add("FTermuserguid");
		Page<Record> listresult = Db.paginate(page, pageSize,
				Db.getSqlPara("app.selectAppuser",
						Kv.by("tablename", T_App_User.tableName).set("tablename1", T_termuser_appuser.tableName)
								.set("tablename2", T_TermUser.tableName).set("cond", cond).set("keyword", keyword)));

		record.set("data", listresult);

		return record;
	}

	/**
	 * APP用户的编辑
	 * 
	 * @author ZKill
	 * @create 2017年5月24日 12:51:41
	 * @update
	 * @param
	 * @return Record
	 */

	@Override
	public Record editAppuser(final HttpServletRequest request) throws Exception {
		final Record record = new Record();
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				// 图片保存地址
				String UrlPath = "upload" + "/";
				MultipartRequest multi = new MultipartRequest(request, UrlPath);
				// Jedis jedis=ApiUtils.GetJedis();
				String userguid = multi.getParameter("userguid");
				String token = multi.getParameter("token");
				String Telephone = multi.getParameter("Telephone");
				// Jedis jedis=ApiUtils.GetJedis();
				System.out.println("token" + token);
				if (StringUtils.hasText(token)) {
					Properties prop;
					try {

						prop = ApiUtils.getproperties(UBAS_APPControllers.class, "jdbc.properties");
						Cache redis = Redis.use(prop.getProperty("redis.name"));
						Jedis jedis = redis.getJedis();
						System.out.println(token);
						userguid = jedis.get(token);
						System.out.println(jedis.get(token));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				System.out.println("电话号码" + Telephone);
				// MultipartRequest multi = new MultipartRequest(request);
				// String userguid = multi.getParameter("FGuid");
				List<String> list = new ArrayList<String>();
				list.add("FAccount");// 用户账户
				list.add("FPassword");// 用户密码
				list.add("FUserdescription");// 用户描述
				list.add("FSex");// 用户性别
				list.add("FTelephone");// 用户联系电话
				list.add("FName");// 用户姓名
				list.add("FIdcard");// 用户身份证号码
				list.add("FEmail");// 用户的邮箱地址
				list.add("FDelete");// 刪除状态
				list.add("FState");// 启用状态
				Iterator<String> it = list.iterator();
				Kv cond = new Kv();
				Kv condreuslt = new Kv();
				while (it.hasNext()) {
					String key = it.next();
					// 说明用户要修改账户,检查数据库里是否有相同的账户
					if (multi.getParameter(key) != null) {
						if (key.equals("FAccount")) {
							Record records = Db.findFirst(
									Db.getSqlPara("app.editAppusertwo", Kv.by("tablename", T_App_User.tableName)
											.set("FAccount", multi.getParameter(key)).set("FGuid", userguid)));
							if (Integer.parseInt(records.get("num").toString()) > 0) {
								// 说明有相同的帐号
								APPUserModulelog.server("用户---" + userguid + "信息编辑失败，已存在相同的帐号");
								record.set("data", "信息编辑失败,已存在相同帐号");
								record.set("resultstate", "10003");
								return false;
							}

						}

						if (key.equals("FPassword")) {

							if (!StringUtils.hasText(userguid) & StringUtils.hasText(Telephone)) {
								// 查询此用户是否拥有修改密码的资格
								Record record2 = Db.findFirst(Db.getSqlPara("app.editAppuserthree",
										Kv.by("tablename", T_App_User.tableName).set("FTelephone", Telephone)));

								String FState = record2.getStr("FState");
								if (FState.equals("0") || FState.equals("3")) {
									// 只有第一次创建账户和找回密码可修改密码
									cond.set(key, multi.getParameter(key).substring(0, 12));
									// 符合资格 修改此账户的状态
									// 修改用户可修改密码状态

									if (Db.update(Db.getSqlPara("app.editAppuserFive",
											Kv.by("tablename", T_App_User.tableName).set("FTelephone", Telephone))
											.getSql()) >= 0) {
										condreuslt.set("FTelephone", Telephone);
										if (Db.update(Db.getSqlPara("app.editAppuserapp", condreuslt).getSql()) > 0) {
											// 编辑成功
											// 获取当前编辑成功用户的信息返回给页面
											APPUserModulelog.server("用户" + Telephone + "编辑成功");

											record.set("data", "编辑成功");
											record.set("resultstate", "SUCCESS");
											result = true;
											return result;
										}
									}
								} else {
									record.set("data", "密码编辑失败,此账户没有更改密码的权限");
									record.set("resultstate", "10004");
									return false;
								}
							} else {
								System.out.println("当前解析的" + multi.getParameter(key));
								cond.set(key, multi.getParameter(key).substring(0, 12));

							}
							continue;
						}
						cond.set(key, multi.getParameter(key));
					}

					cond.set("FUpdatatime", TimeUT.getCurrTime());

				}
				if (multi.getFiles().size() > 0) {
					String FHeadurl = Constant.IMAGE_URL + userguid + "/";
					// 复制文件到指定文件夹下 并修改文件名
					try {
						System.out.println(multi.getFiles().get(0).getFileName());
						String[] Suffixname = multi.getFiles().get(0).getFileName().split(".");
						System.out.println(Suffixname);
						FHeadurl += AppUTil.fileMove(userguid,
								PathKit.getWebRootPath() + "/" + UrlPath + multi.getFiles().get(0).getFileName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					cond.set("FHeadurl", FHeadurl);
				}
				condreuslt.set("tablename", T_App_User.tableName).set("cond", cond).set("FGuid", userguid);
				if (Db.update(Db.getSqlPara("app.editAppuser", condreuslt).getSql()) > 0) {
					// 编辑成功
					APPUserModulelog.server("用户" + userguid + "编辑成功");

					record.set("data", "编辑成功");
					record.set("resultstate", "SUCCESS");
					result = true;
				} else {
					APPUserModulelog.server("用户,系统错误" + userguid + "编辑失败");
					record.set("data", "系统错误,编辑失败");
					record.set("resultstate", "10002");
					result = false;
				}

				Record recorduser = Db.findFirst(Db.getSqlPara("app.editAppusersix",
						Kv.by("tablename", T_App_User.tableName).set("FGuid", userguid)));
				record.set("user", recorduser);

				return result;
			}

		});
		return record;
	}

	/**
	 * APP用户解除绑定和绑定金顶盒用户
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 15:46:58
	 * @update
	 * @return Record
	 */

	@Override
	public Record bindAppuser(String param, String type, String Teruserguid) throws Exception {
		Record record = new Record();

		String insertSQL = " VALUES ";

		// type 0.是绑定,1.解除绑定
		List<String> Terlist = new ArrayList<>();
		List<String> Applist = new ArrayList<>();
		if (type.equals("0")) {
			JSONArray paramData = JSONArray.fromObject(param);
			for (Object o : paramData) {

				// 绑定之前检查此APP用户是否已经绑定过金顶盒

				/*
				 * if(Integer.parseInt(Db.findFirst(Db.getSqlPara(
				 * "app.selectbindAppuser",
				 * Kv.by("tablename",T_termuser_appuser.tableName).set(
				 * "FAppuserguid",o.toString()))) .get("num").toString())>0){
				 * record.set("data",o.toString()+"此APP用户已经绑定过金顶盒,无法重复绑定");
				 * record.set("resultstate", "10003"); return record; };
				 */
				insertSQL += "('" + o.toString() + "','" + Teruserguid + "','" + TimeUT.getCurrTime() + "')";
				insertSQL += ",";

			}
			insertSQL = insertSQL.substring(0, insertSQL.lastIndexOf(","));
			if (Db.update(Db
					.getSqlPara("app.insertbindAppuser",
							Kv.by("tablename", T_termuser_appuser.tableName).set("insertSQL", insertSQL))
					.getSql()) > 0) {
				// 绑定成功
				record.set("data", "绑定成功");
				record.set("resultstate", "SUCCESS");
			} else {
				record.set("data", "绑定失败");
				record.set("resultstate", "10002");

			}
		}
		// 绑定
		if (type.equals("1")) {
			// 解除绑定

			JSONArray paramData1 = JSONArray.fromObject(param);
			for (Object o : paramData1) {
				JSONObject data = (JSONObject) o;
				Applist.add(data.getString("Appuserguid"));
				Terlist.add(data.getString("Teruserguid"));
			}
			if (Db.update(Db.getSqlPara("app.bindAppuser",
					Kv.by("tablename", T_termuser_appuser.tableName).set("Applist", Applist).set("Terlist", Terlist))
					.getSql()) > 0) {
				// 绑定成功
				record.set("data", "解除绑定成功");
				record.set("resultstate", "SUCCESS");
			} else {
				record.set("data", "解除绑定失败");
				record.set("resultstate", "10002");
			}
		}

		return record;
	}

	/**
	 * 获取金顶盒用户已绑定的APP用户信息
	 * 
	 * @author ZKill
	 * @create 2017年12月5日 15:44:27
	 * @update
	 * @return void
	 */

	@Override
	public Record selectuser(String param) throws Exception {
		Record record = new Record();
		JSONObject paramJO = JSONObject.fromObject(param);
		Integer pageSize = StringUtils.hasText((String) paramJO.get("pageSize"))
				? Integer.parseInt((String) paramJO.get("pageSize")) : null;
		Integer page = StringUtils.hasText((String) paramJO.get("page"))
				? Integer.parseInt((String) paramJO.get("page")) : null;
		String FTermuserguid = paramJO.getString("FTermuserguid");
		List<String> list = new ArrayList<String>();
		list.add("FDelete");// 刪除状态
		list.add("FState");// 启用状态
		Page<Record> datalist = Db.paginate(page, pageSize,
				Db.getSqlPara("app.selectuser", Kv.by("tablename", T_App_User.tableName)
						.set("tablename1", T_termuser_appuser.tableName).set("FTermuserguid", FTermuserguid)));
		return null;
	}

	/**
	 * 批量删除APP用户
	 * 
	 * @author ZKill
	 * @create 2017年12月5日 16:40:26
	 * @update
	 * @return void
	 */

	@Override
	public Record DeleteAppuser(String param) throws Exception {
		Record record = new Record();
		JSONArray paramData = JSONArray.fromObject(param);
		List<String> list = new ArrayList<>();
		for (Object o : paramData) {
			list.add(o.toString());
		}
		if (Db.update(Db.getSqlPara("app.DeleteAppuser", Kv.by("tablename", T_App_User.tableName).set("cond", list))
				.getSql()) > 0) {
			record.set("resultstate", "SUCCESS");
			record.set("data", "删除成功");
		} else {
			record.set("resultstate", "10002");
			record.set("data", "删除失败");
		}

		return record;
	}

	/**
	 * APP用户找回密码（调此接口前,先调用发送验证码接口,此接口做验证码验证）
	 * 
	 * @author ZKill
	 * @create 2017年12月6日 10:43:38
	 * @update
	 * @return void
	 */

	@Override
	public Record RetrievePassword(String param) throws Exception {
		Record record = new Record();
		JSONObject jsonObject = JSONObject.fromObject(param);
		// 获取用户的手机号和验证码
		String FTelephone = jsonObject.getString("FTelephone");
		String appVerificationCode = jsonObject.getString("FVerificationcode");

		// 找到当前要找回密码的用户
		Record recordRetrieve = Db.findFirst(Db.getSqlPara("app.RetrievePassword",
				Kv.by("tablename", T_App_User.tableName).set("FTelephone", FTelephone)));
		if (recordRetrieve != null) {
			// 检查该验证码是否过期
			String VerificationCode = recordRetrieve.getStr("FVerificationcode");
			// 获取到该验证码的更新时间
			String Verificationcodeupdatetime = recordRetrieve.getStr("FVerificationcodetime");
			if (System.currentTimeMillis() - TimeUT.getMillsTime(Verificationcodeupdatetime) > 60000) {
				// 说明该验证码已超时,找回密码失败
				APPUserModulelog.server("电话" + FTelephone + "验证码已超时,找回密码失败");
				record.set("resultstate", "20003");
				record.set("data", "电话" + FTelephone + "验证码已超时,找回密码失败");
				return record;
			}

			// 比对验证码是否一致
			if (appVerificationCode.equals(VerificationCode)) {
				Kv cond = Kv.by("FState", "3");
				// 修改用户可修改密码状态
				if (Db.update(Db.getSqlPara("app.cappuserVerificationcodeupdate",
						Kv.by("cond", cond).set("tablename", T_App_User.tableName).set("FTelephone", FTelephone))
						.getSql()) > 0) {
					// 用户发送的验证码和服务端生成的一致,用户重新设置密码,调用用户编辑接口
					APPUserModulelog.server("电话" + FTelephone + "用户,找回密码验证失败");
					record.set("resultstate", "SUCCESS");
					record.set("data", "电话" + FTelephone + "用户,找回密码成功");
				}

			} else {
				APPUserModulelog.server("电话" + FTelephone + "用户发送的验证码和服务端的不一致,找回密码失败");
				record.set("resultstate", "20004");
				record.set("data", "电话" + FTelephone + "用户发送的验证码和服务端的不一致,找回密码失败");
			}

		} else {
			record.set("resultstate", "20002");
			record.set("data", "当前要找回密码的账户不存在");

		}
		return record;
	}

	/**
	 * APP用户登录
	 * 
	 * @author ZKill
	 * @create 2017年12月6日 14:29:08
	 * @update
	 * @return void
	 */

	@Override
	public Record Signin(String param, HttpServletRequest request, String timestamp) throws Exception {
		Record record = new Record();
		JSONObject jsonObject = JSONObject.fromObject(param);
		String FTelephone = jsonObject.getString("FTelephone");
		String appVerificationCode = jsonObject.getString("FVerificationcode");
		String Account = jsonObject.getString("Account");
		String appPassWord = jsonObject.getString("PassWord");
		if (StringUtils.hasText(FTelephone) && StringUtils.hasText(appVerificationCode)) {

			// 说明手机发送验证码登录
			// 说明是手机号验证码登录,验证验证码
			Record recordRetrieve = Db.findFirst(Db.getSqlPara("app.RetrievePassword",
					Kv.by("tablename", T_App_User.tableName).set("FTelephone", FTelephone)));
			if (recordRetrieve != null) {
				// 检查该验证码是否过期
				String VerificationCode = recordRetrieve.getStr("FVerificationcode");
				// 获取到该验证码的更新时间
				String Verificationcodeupdatetime = recordRetrieve.getStr("FVerificationcodetime");
				if (System.currentTimeMillis() - TimeUT.getMillsTime(Verificationcodeupdatetime) > 60000) {
					// 说明该验证码已超时,登录失败
					APPUserModulelog.server("电话" + FTelephone + "验证码已超时,登录失败");
					record.set("resultstate", "20003");
					record.set("data", "电话" + FTelephone + "验证码已超时,登录失败");
					return record;
				}

				if (VerificationCode.equals(appVerificationCode)) {
					// 验证码一致给登录成功,返回SessionID
					String insertSQL = "UPDATE " + T_App_User.tableName
							+ " SET FLastlogintime=?,Flastloginip=? WHERE FTelephone=?";
					Db.update(insertSQL, TimeUT.getCurrTime(), request.getRemoteAddr(), FTelephone);
					APPUserModulelog.server("电话" + FTelephone + "用户登录成功");
					String SESSIONID = request.getSession(true).getId();
					// 放入缓存之中
					String token = AppUTil.MD5Encode(SESSIONID, "UTF-8");
					// String token=JWTUtil.createJWT(SESSIONID, Account,
					// 24*60*60*1000);
					CacheKit.put(Constant.EHCACHENAME, Constant.token + Account, token);
					record.set("SESSIONID", token);
					record.set("Account", Account);
					record.set("SEESION", recordRetrieve);
					record.set("userguid", recordRetrieve.get("FGuid"));
					record.set("resultstate", "SUCCESS");
					record.set("data", "电话" + FTelephone + "用户登录成功");
					return record;
				} else {
					APPUserModulelog.server("电话" + FTelephone + "用户输入的验证码有误,登录失败");
					record.set("resultstate", "20004");
					record.set("data", "电话" + FTelephone + "验证码已超时,登录失败");
					return record;
				}

			} else {
				record.set("resultstate", "20002");
				record.set("data", "此手机号无效,登录失败");
				return record;

			}

		}
		if (StringUtils.hasText(Account) && StringUtils.hasText(appPassWord)) {
			// 说明是账户密码登录,验证账户密码

			Record record2 = Db.findFirst(Db.getSqlPara("app.userSignintwo",
					Kv.by("FAccount", Account).set("tablename", T_App_User.tableName)));
			if (record2 != null) {
				appPassWord = appPassWord.substring(0, 12);
				System.out.println(appPassWord);

				if (record2.get("FPassword").equals(appPassWord)
						|| record2.get("FPassword").toString().contains(appPassWord)) {
					String insertSQL = "UPDATE " + T_App_User.tableName
							+ " SET FLastlogintime=?,Flastloginip=? WHERE FAccount=?";
					Db.update(insertSQL, TimeUT.getCurrTime(), request.getRemoteAddr(), Account);
					// //验证码一致给登录成功,返回SessionID
					APPUserModulelog.server("账户" + Account + "登录成功");
					String SESSIONID = request.getSession(true).getId();
					String token = AppUTil.MD5Encode(SESSIONID, "UTF-8");
					;
					// 放入缓存之中
					CacheKit.put(Constant.EHCACHENAME, Constant.token + Account, token);
					record.set("SESSIONID", token);
					record.set("resultstate", "SUCCESS");
					record.set("SEESION", record2);
					record.set("userguid", record2.get("FGuid"));
					record.set("data", "账户" + Account + "登录成功");
					return record;
				}
				APPUserModulelog.server("账户" + Account + "密码有误,登录失败");
				record.set("resultstate", "30003");
				record.set("data", "账户" + Account + "密码有误,登录失败");
				return record;
				// 比对密码
			} else {
				APPUserModulelog.server("账户" + Account + "有误,找不到该账户信息");
				record.set("resultstate", "30002");
				record.set("data", "账户" + Account + "有误,找不到该账户信息");
				return record;
			}
		} else {
			// 用户没有输入完整的账户和密码
			APPUserModulelog.server("用户,没有输入完整的信息");
			record.set("resultstate", "30001");
			record.set("data", "用户,没有输入完整的信息");
			return record;
		}
	}

	/**
	 * 电视端生成二维码接口
	 * 
	 * @author ZKill
	 * @create
	 * @update
	 * @return void
	 */
	@Override
	public File CreateQRcode(HttpServletRequest request) throws Exception {
		String macuserguid = request.getParameter("macuserguid");
		Record record = new Record();
		String encodeddata = "";
		String RcodeURL = "/" + Constant.IMAGE_URL + encodeddata + ".png";
		File destFile = new File(PathKit.getWebRootPath() + RcodeURL);
		// 生成不唯一的UUID 作为二维码的信息
		destFile = AppUTil.qrCodeEncode(macuserguid + "," + encodeddata, destFile);
		// 返回图片项目地址
		record.set("resultstate", "SUCCESS");
		record.set("data", RcodeURL);
		record.set("SEESION", macuserguid + "," + encodeddata);

		return destFile;

	}

	/**
	 * 电视端二维码登录
	 * 
	 * @author ZKill
	 * @create
	 * @update
	 * @return void
	 */
	@Override
	public Record QRcodelSignin(String sid, String userguid, String stbGuid) throws Exception {
		Record record = new Record();

		// 获取APP用户的唯一标识
		String insertSQL = "VALUES ('" + userguid + "','" + stbGuid + "','" + TimeUT.getCurrTime() + "')";
		List<String> list = new ArrayList<>();
		list.add("FUsername");
		list.add("FToken");
		list.add("FSid");
		list.add("FisOK");
		list.add("FCreatetime");
		// 检查APP用户和盒子是否已经绑定
		if (Integer
				.parseInt(Db
						.findFirst(Db.getSqlPara("app.QRcodelSignin", Kv.by("tablename", T_termuser_appuser.tableName)
								.set("FAppuserguid", userguid).set("FTermuserguid", stbGuid)))
						.get("num").toString()) == 0) {
			if (Db.update(Db
					.getSqlPara("app.insertbindAppuser",
							Kv.by("tablename", T_termuser_appuser.tableName).set("insertSQL", insertSQL))
					.getSql()) > 0) {
				// 将当前用户信息和扫码标识进行绑定,证明该用户已经扫码过
				Kv cond = Kv.by("FUsername", userguid).set("FToken", sid).set("FSid", stbGuid).set("FisOK", "0")
						.set("FCreatetime", TimeUT.getCurrTime());
				if (Db.update(Db.getSqlPara("app.cappuserVerificationcode",
						Kv.by("tablename", T_Scavenging.tableName).set("cond", cond)).getSql()) > 0) {
					record.set("resultstate", "SUCCESS");
					record.set("data", "扫码成功");
				} else {
					record.set("resultstate", "10005");
					record.set("data", "系统异常请稍后再试,二维码扫码失败");
				}
			} else {
				record.set("resultstate", "10005");
				record.set("data", "系统异常请稍后再试,二维码扫码失败");
			}
		} else {

			Kv cond = Kv.by("FUsername", userguid).set("FToken", sid).set("FSid", stbGuid).set("FisOK", "0")
					.set("FCreatetime", TimeUT.getCurrTime());
			if (Db.update(Db.getSqlPara("app.cappuserVerificationcode",
					Kv.by("tablename", T_Scavenging.tableName).set("cond", cond)).getSql()) > 0) {
				record.set("resultstate", "SUCCESS");
				record.set("data", "扫码成功");
			} else {
				record.set("resultstate", "10005");
				record.set("data", "系统异常请稍后再试,二维码扫码失败");
			}
		}
		// 将APP用户和盒子进行绑定

		return record;
	}

	/**
	 * 盒子检测登录状态（盒子循环检测调用接口）
	 * 
	 * @author ZKill
	 * @create 2017年12月7日 16:33:11
	 * @update
	 * @return void
	 */
	@Override
	public Record Logonstatus(String stbGuid) throws Exception {
		// 根据二维码的唯一标识 获取扫码用户的信息
		Record stbRecord = Db
				.findFirst(Db.getSqlPara("app.CreateQRcode", Kv.by("tablename", T_TermUserTermInfo.tableName)
						.set("tablename1", T_TermInfo.tableName).set("FCaCard", stbGuid)));
		Record record = new Record();

		Record t_ScavengingRecord = Db.findFirst(Db.getSqlPara("app.Logonstatus",
				Kv.by("tablename", T_Scavenging.tableName).set("FSid", stbRecord.get("FSubsGUID"))));

		if (t_ScavengingRecord != null && t_ScavengingRecord.get("FisOk").equals("0")) {
			// 验证该扫码该二维码的用户是否合法
			Record record2 = Db.findFirst(Db.getSqlPara("app.Logonstatustwo",
					Kv.by("tablename", T_App_User.tableName).set("FGuid", t_ScavengingRecord.get("FUsername"))));
			if (record2 != null) {
				// 说明用户信息合法登录成功，返回登录用户的信息
				// 把此次扫碼记录清除
				if (Db.update(Db
						.getSqlPara("app.UpdateLogonstatus",
								Kv.by("tablename", T_Scavenging.tableName).set("FSid", stbRecord.get("FSubsGUID")))
						.getSql()) > 0) {
					record.set("resultstate", "SUCCESS");
					record.set("data", record2);
				}

			} else {
				record.set("resultstate", "10002");
				record.set("data", "用户信息不合法");
			}
		} else {
			record.set("resultstate", "10001");
			record.set("data", "无扫码信息");
		}
		return record;
	}

	/**
	 * 观看记录统计接口
	 * 
	 * @author ZKill
	 * @create 2017年12月8日 14:27:59
	 * @update
	 * @return void
	 */

	@Override
	public Record submitHistoryData(String param) throws Exception {
		Record record = new Record();

		JSONObject jsonObject = JSONObject.fromObject(param);
		// APP用户GUID
		String FAppuserguid = jsonObject.getString("FAppuserguid");
		// 提交的资源信息
		String FResource = jsonObject.getString("FResource");
		// 关联提交的资源信息
		Kv cond = Kv.by("FAppuserguid", FAppuserguid).set("FResource", FResource).set("FCreatetime",
				TimeUT.getCurrTime());
		if (Db.update(Db.getSqlPara("app.cappuserVerificationcode",
				Kv.by("tablename", t_Appuser_Resource.tableName).set("cond", cond)).getSql()) > 0) {
			record.set("resultstate", "SUCCESS");
			record.set("data", "信息记录成功");
		} else {
			record.set("resultstate", "10002");
			record.set("data", "信息记录失败");
		}

		return record;
	}

	/**
	 * 删除APP用户观看记录(批量删除）
	 * 
	 * @author ZKill
	 * @create 2017年12月8日 14:27:59
	 * @update
	 * @return void
	 */
	@Override
	public Record DeletesubmitHistoryData(String param) throws Exception {
		Record record = new Record();
		JSONArray paramData = JSONArray.fromObject(param);
		List<String> list = new ArrayList<>();
		for (Object o : paramData) {
			list.add(o.toString());
		}
		if (Db.update(Db.getSqlPara("app.DeletesubmitHistoryData",
				Kv.by("tablename", t_Appuser_Resource.tableName).set("cond", list)).getSql()) > 0) {
			record.set("resultstate", "SUCCESS");
			record.set("data", "删除成功");
		} else {
			record.set("resultstate", "10002");
			record.set("data", "删除失败");
		}

		return record;

	}

	/**
	 * 根据app用户标识查询观看记录数据
	 * 
	 * @author ZKill
	 * @create 2017年12月11日 09:41:00
	 * @update
	 * @return Record
	 */

	@Override
	public Record SelectsubmitHistoryData(String param) throws Exception {
		Record record = new Record();
		JSONObject paramJO = JSONObject.fromObject(param);
		Integer pageSize = StringUtils.hasText((String) paramJO.get("pageSize"))
				? Integer.parseInt((String) paramJO.get("pageSize")) : null;
		Integer page = StringUtils.hasText((String) paramJO.get("page"))
				? Integer.parseInt((String) paramJO.get("page")) : null;
		String FAppuserguid = (String) paramJO.get("FAppuserguid");
		List<String> list = new ArrayList<>();
		Kv cond = new Kv();
		if (param.contains("keyword")) {
			String keyword = paramJO.getString("keyword");
			cond.set("keyword", keyword);
		}
		/*
		 * if (param.contains("FState")) { list.add("FState");
		 * 
		 * } if (param.contains("FSex")) { list.add("FSex");
		 * 
		 * }
		 */
		for (int i = list.size() - 1; i >= 0; i--) {
			if (paramJO.getString(list.get(i)) != null) {
				cond.set(list.get(i), paramJO.getString(list.get(i)));
			}
		}
		List<String> keyword = new ArrayList<>();
		keyword.add("FResource");

		Page<Record> listresult = Db.paginate(page, pageSize,
				Db.getSqlPara("app.SelectsubmitHistoryData", Kv.by("tablename", t_Appuser_Resource.tableName)
						.set("cond", cond).set("keyword", keyword).set("FAppuserguid", FAppuserguid)));

		record.set("data", listresult);

		return record;

	}

}
