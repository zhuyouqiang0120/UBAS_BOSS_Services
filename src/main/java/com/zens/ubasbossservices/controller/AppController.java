package com.zens.ubasbossservices.controller;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.alibaba.druid.sql.visitor.functions.If;
import com.chasonx.tools.StringUtils;
import com.chasonx.tools.TokenUtil;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.excel.PoiExporter;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.Constant;
import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.common.Tools;
import com.zens.ubasbossservices.entity.T_TermInfo;
import com.zens.ubasbossservices.entity.T_TermUserTermInfo;
import com.zens.ubasbossservices.service.AppService;
import com.zens.ubasbossservices.serviceimpl.AppServicelmpl;
import com.zens.ubasbossservices.utils.AppUTil;
import com.zens.ubasbossservices.utils.BossUtil;
import com.zens.ubasbossservices.utils.SQLUtil;

import Down.DownFiles;
import LoginDemo.Login;
import LoginDemo.Logoff;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * APP用户
 * 
 * @author zhangkai
 * @e-mail zhangkai@zensvision.com
 * @date 2017年12月1日 10:16:41
 */
public class AppController extends Controller {
	AppService appService = new AppServicelmpl();

	/**
	 * 分页获取APP用户列表
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 09:47:15
	 * @update
	 * @return void
	 */
	public void selectAppuser() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.selectAppuser(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 获取金顶盒用户已绑定的APP用户信息
	 * 
	 * @author ZKill
	 * @create 2017年12月5日 15:43:50
	 * @update
	 * @return void
	 */

	public void selectuser() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.selectAppuser(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpF	unc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * APP用户编辑
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 14:04:14
	 * @update
	 * @return void
	 */
	public void editAppuser() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();

		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		// getFile("CCC");
		// response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			data = appService.editAppuser(request);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(request.getParameter("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * APP用户解除绑定和绑定金顶盒用户
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 14:04:14
	 * @update
	 * @return void
	 */
	public void bindAppuser() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		String type = getPara("type");
		String Teruserguid = getPara("Teruserguid");
		try {
			data = appService.bindAppuser(param, type, Teruserguid);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 批量删除APP用户
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 14:04:14
	 * @update
	 * @return void
	 */
	public void DeleteAppuser() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.DeleteAppuser(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 电视端请求生成登录二维碼图片接口
	 * 
	 * @author ZKill
	 * @create 2017年12月6日 10:31:31
	 * @update
	 * @return void
	 * @throws Exception
	 */

	public void CreateQRcode() throws Exception {
		ResultDesc rd = new ResultDesc();
		HttpServletRequest request = getRequest();
		HttpSession ses = request.getSession();

		// 设备的标识
		String stbGuid = request.getParameter("stbGuid");

		// 设备对应的盒子用户
		Record stbRecord = Db
				.findFirst(Db.getSqlPara("app.CreateQRcode", Kv.by("tablename", T_TermUserTermInfo.tableName)
						.set("tablename1", T_TermInfo.tableName).set("FCaCard", stbGuid)));
		try {

			if (stbRecord == null) {
				throw new Exception("CA卡号不合法");
			}
		} catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMessage(e.getMessage());

		}
		if (stbRecord == null) {
			renderJavascript(JsonKit.toJson(rd));
		}
		String uuid = UUID.randomUUID().toString();

		String APPuserguid = stbRecord.getStr("FSubsGUID");
		String APPID = ses.getId();
		// HttpSession session=getSession();
		// session.setAttribute(tokenID,APPuserguid);
		// session.setMaxInactiveInterval(10000000);
		// 将token缓存起来
		CacheKit.put(Constant.EHCACHENAME, APPID, APPuserguid);

		// 根据设备的CA卡号获取到对应用户的guid
		System.out
				.println(Constant.FQRcode_URL + "UBAS_BOSS_Services/app/QRcodelSignin?sid=" + uuid + "&APPID=" + APPID);
		renderQrCode(Constant.FQRcode_URL + "UBAS_BOSS_Services/app/QRcodelSignin?sid=" + uuid + "&APPID=" + APPID, 450,
				450);

	}

	/**
	 * 电视端扫码二维码登录接口(手机扫二维码）
	 * 
	 * @author ZKill
	 * @create 2017年12月6日 10:31:31
	 * @update
	 * @return void
	 * @throws Exception
	 */
	public void QRcodelSignin() throws Exception {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String sid = getPara("sid");
		String token = getPara("token");
		String APPID = getPara("APPID");
		String userguid;
		/*
		 * if(validateToken(token)){ throw new Exception("当前二维码已过期"); }
		 */
		Properties po = ApiUtils.getproperties(this.getClass(), "jdbc.properties");
		Cache cache = Redis.use(po.get("redis.name").toString());
		Jedis jedis = cache.getJedis();
		String tokenjedis = jedis.get(token);
		try {

			if (tokenjedis == null) {
				throw new Exception("登录状态不合法,请重新登录");
			} else {

				userguid = tokenjedis;
			}
			String stbGuid = CacheKit.get(Constant.EHCACHENAME, APPID);
			System.out.println("sid" + sid);
			System.out.println("stbGuid" + stbGuid);
			System.out.println("userguid" + userguid);
			if (!StringUtils.hasText(sid) || !StringUtils.hasText(stbGuid) || !StringUtils.hasText(userguid)) {
				throw new Exception("参数信息不全");
			}
			data = appService.QRcodelSignin(sid, userguid, stbGuid);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
			CacheKit.remove(Constant.EHCACHENAME, token);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		} finally {
			jedis.close();
		}
		rd.setResult(result);
		// rd.setMethod(getAttr("_methodName").toString());
		// rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 
	 * if(ses.getAttribute(uid)==null){ //当前扫的二维码已经过期,请获取新的二维码
	 * data.set("resultstate", "10007"); data.set("data","当前二维码无效或已失效");
	 * 
	 * }else{
	 */

	/**
	 * 盒子检测登录状态（盒子循环检测调用接口）
	 * 
	 * @author ZKill
	 * @create 2017年12月7日 16:33:11
	 * @update
	 * @return void
	 */

	public void Logonstatus() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String mac = getPara("stbGuid");

		try {

			data = appService.Logonstatus(mac);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * APP观看记录统计
	 * 
	 * @author ZKill
	 * @create 2017年12月8日 14:25:17
	 * @update
	 * @return void
	 */

	public void submitHistoryData() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.submitHistoryData(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);
	}

	/**
	 * 删除APP用户观看记录统计
	 * 
	 * @author ZKill
	 * @create 2017年12月8日 14:25:17
	 * @update
	 * @return void
	 */

	public void DeletesubmitHistoryData() {
		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.DeletesubmitHistoryData(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);

	}

	/**
	 * 根据APP用户标识查询观看记录数据
	 * 
	 * @author ZKill
	 * @create 2017年12月11日 09:39:47
	 * @update
	 * @return void
	 */
	public void SelectsubmitHistoryData() {

		ResultDesc rd = new ResultDesc();
		boolean result = true;
		Record data = new Record();
		String param = getAttr("param");
		try {
			data = appService.SelectsubmitHistoryData(param);
			rd.setData(data);
			rd.setCode(200);
			rd.setMessage("ok");
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			rd.setCode(500);
			rd.setMessage(e.getMessage());
		}
		rd.setResult(result);
		rd.setMethod(getAttr("_methodName").toString());
		rd.setAction(getAttr("_actionKey").toString());

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson(rd) + ");");
		else
			renderJson(rd);

	}

	public void importboss() {
		try {

			boolean flag = false;
			Login ftpLogin = new Login();
			PropKit.use("jdbc.properties");
			int countLogin = ftpLogin.Logins(PropKit.get("ftp.ip"), PropKit.getInt("ftp.host"), PropKit.get("ftp.amin"),
					PropKit.get("ftp.pwd"));
			System.out.println(countLogin);

			if (countLogin == 230) {
				DownFiles downFile = new DownFiles();
				flag = downFile.dpwnFile("/", PropKit.get("ftp.Down"), PathKit.getWebRootPath() + "/", "");
				System.out.println(flag);

			}
			Logoff closeFtp = new Logoff();
			closeFtp.Logoffs();

			String Sitvkey;
			Properties prop = ApiUtils.getproperties(BossUtil.class, "jdbc.properties");
			// Cache redis = Redis.use(prop.getProperty("redis.name"));
			// Jedis jedis = redis.getJedis();
			String tablename;
			SimpleDateFormat myFmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// boolean flags = true;
			if (flag) {
				// 将之前数据库的旧数据全部清空
				String t1 = "DELETE FROM t_termuser WHERE Fimportboss=?";
				String t2 = "DELETE FROM t_termuser_subscriberpackage WHERE Fimportboss=?";
				String t3 = "DELETE FROM t_termuser_terminfo WHERE Fimportboss=?";
				Db.update(t1, "3");
				Db.update(t2, "3");
				Db.update(t3, "3");
				// 说明文件下载成功
				File file = new File(PathKit.getWebRootPath() + "/" + PropKit.get("ftp.Down"));

				File[] fileList = file.listFiles();
				// System.out.println(fileList.length);
				for (File files : fileList) {
					int size = 0;
					if (files.getName().contains("大理用户信息")) {
						// 说明解析的是大理用户信息

						/*
						 * if (jedis.get("teruserimbossrow") != null &&
						 * jedis.get("teruserimbossrow").equals("0")) { //
						 * 说明上次已经解析完，本次从新的一页开始解析 size =
						 * Integer.parseInt(jedis.get("teruserimbossSheet")); }
						 * else { size = 0; }
						 */
						Sitvkey = prop.get("Daliuser").toString();
						// flags = true;

						tablename = "t_termuser";
					} else if (files.getName().contains("大理订购关系数据")) {
						// 说明解析的是大理订购关系数据
						/*
						 * if (jedis.get("subiimbossrow") != null &&
						 * jedis.get("subiimbossrow").equals("0")) { //
						 * 说明上次已经解析完，本次从新的一页开始解析 size =
						 * Integer.parseInt(jedis.get("teruserimbossSheet")); }
						 * else {
						 * 
						 * size = 0; }
						 */
						Sitvkey = prop.get("Dalirelationship").toString();
						// flags = false;
						tablename = "t_termuser_subscriberpackage";
					} else {
						// 跳过本次循环 不进行解析
						continue;
					}

					HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(
							PathKit.getWebRootPath() + "/" + PropKit.get("ftp.Down") + "/" + files.getName()));

					int Sheetnumber = hssfWorkbook.getNumberOfSheets();
					// 获取sheet页的数量
					String Value = "";

					String SQL = "INSERT INTO " + tablename + " (" + Sitvkey + ") VALUES ";
					for (int i = size; i <= Sheetnumber - 1; i++) {
						HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
						// HSSFRow row = sheet.getRow(0);
						int begin;
						// String beginString = "";
						/*
						 * if (flags) { beginString = "teruserimbossrow"; } else
						 * { beginString = "subiimbossrow"; }
						 */
						// if (StringUtils.hasText(jedis.get(beginString)) &&
						// !jedis.get(beginString).equals("0")) {
						// begin = Integer.parseInt(jedis.get(beginString));
						// } else {
						begin = sheet.getFirstRowNum();
						// }
						int end = sheet.getLastRowNum();
						// SSFRow row=sheet.getRow(0);
						String macSQL = "INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss)  VALUES ";
						String macValue = "";
						int NumberOfCells = sheet.getRow(0).getPhysicalNumberOfCells();

						for (int j = begin + 1; j <= end; j++) {
							for (int k = 1; k < NumberOfCells; k++) {
								Row r = sheet.getRow(j);
								Cell c = r.getCell(k, Row.RETURN_BLANK_AS_NULL);
								String cellValue = "";
								if (c == null) {
									cellValue = "";
								} else {
									cellValue = sheet.getRow(j).getCell(k).toString();
								}
								switch (c.getCellType()) {
								case HSSFCell.CELL_TYPE_NUMERIC:
									if (sheet.getRow(j).getCell(k).toString().contains("-")) {
										cellValue = myFmt.format(sheet.getRow(j).getCell(k).getDateCellValue());
									}

									break;

								default:
									break;
								}
								// System.out.println(cellValue);

								if (cellValue.contains(".")) {
									// 去掉小数点

									cellValue = cellValue.substring(0, 1);
								}
								// Value += k == NumberOfCells - 1 ? "'" +
								// cellValue + "'" : "'" + cellValue + "',";

								if (tablename.equals("t_termuser")) {

									// 说明导入的是用户信息表 excel表再解析的时候 还要把对应的信息存入到
									// 设备和用户的关联表之中
									if (k == 6) {
										macValue +=

												"('" + cellValue + "'" + ",'" + sheet.getRow(j).getCell(2).toString()
														+ "'";
										macValue += ",'" + 3 + "'),";
									}
									Value += k == NumberOfCells - 1 ? "'" + cellValue + "'" : "'" + cellValue + "',";

								} else {
									Value += k == NumberOfCells - 1 ? "'" + cellValue + "'" : "'" + cellValue + "',";
								}

							}

							if (j % 1000 == 0) {
								if (!macSQL.equals(
										"INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss)  VALUES ")) {

									macSQL += macValue;
									macValue = "";
									macSQL = macSQL.substring(0, macSQL.lastIndexOf(","));

									if (Db.update(macSQL) >= 1000) {

										macSQL = "INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss) VALUES ";
									}
									;

								}

								SQL += "( " + Value + ",'" + 3 + "')";

								Value = "";

								if (Db.update(SQL) >= 1000) {
									SQL = "INSERT INTO " + tablename + " (" + Sitvkey + ") VALUES ";
								}

							} else {
								SQL += "( " + Value + ",'" + 3 + "'),";
								Value = "";
								macSQL += macValue;
								macValue = "";

								if (i == Sheetnumber - 2 && j == end) {

									// 說明已經到最後並且不足1000条,把剩余的存入数据
									if (tablename == "t_termuser") {
										SQL = SQL.substring(0, SQL.lastIndexOf(","));
										macSQL = macSQL.substring(0, macSQL.lastIndexOf(","));
										Db.update(macSQL);
										Db.update(SQL);
									} else {
										SQL = SQL.substring(0, SQL.lastIndexOf(","));
										Db.update(SQL);
									}

								}
							}

						}
					}
					/*
					 * if (files.getName().contains("大理用户信息")) { // 说明解析的是大理用户信息
					 * // continue; if
					 * (hssfWorkbook.getSheetAt(hssfWorkbook.getNumberOfSheets()
					 * - 1).getLastRowNum() == 65536) { // 下次从新的一页开始
					 * jedis.set("teruserimbossSheet", Sheetnumber + 1 + "");
					 * jedis.set("teruserimbossrow", "0"); } else { // 从本页
					 * 本次行开始解析 jedis.set("teruserimbossSheet", Sheetnumber +
					 * ""); jedis.set("teruserimbossrow",
					 * hssfWorkbook.getSheetAt(hssfWorkbook.getNumberOfSheets()
					 * - 1).getLastRowNum() + ""); } } else if
					 * (files.getName().contains("大理订购关系数据")) { //
					 * 说明解析的是大理订购关系数据 if
					 * (hssfWorkbook.getSheetAt(hssfWorkbook.getNumberOfSheets()
					 * - 1).getLastRowNum() == 65536) { // 下次从新的一页开始
					 * jedis.set("subimbossSheet", Sheetnumber + 1 + "");
					 * jedis.set("subiimbossrow", "0"); } else { // 从本页 本次行开始解析
					 * jedis.set("subiimbossSheet", Sheetnumber + "");
					 * jedis.set("subiimbossrow",
					 * hssfWorkbook.getSheetAt(hssfWorkbook.getNumberOfSheets()
					 * - 1).getLastRowNum() + ""); } }
					 */
					System.out.println(files.getName() + "本次数据导入完毕");

				}
				// 刪除项目下文件

				for (File deletefies : fileList) {
					deletefies.delete();
					System.out.println("删除" + deletefies.getName() + "文件成功");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			renderText("数据导入失败");
		}
		renderText("数据导入成功");
	}

}
