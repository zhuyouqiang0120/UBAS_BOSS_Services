package com.zens.ubasbossservices.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.chasonx.tools.StringUtils;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.plugin.redis.RedisPlugin;
import com.sun.imageio.plugins.wbmp.WBMPImageReader;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.controller.UBAS_APPControllers;
import com.zens.ubasbossservices.entity.T_TermUserSubscriberPackage;

import Down.DownFiles;
import LoginDemo.Login;
import LoginDemo.Logoff;
import jxl.Sheet;
import jxl.Workbook;
import redis.clients.jedis.Jedis;

public class BossUtil {

	public static boolean DownFile() {
		try {
			Login ftpLogin = new Login();

			int countLogin = ftpLogin.Logins("192.168.0.51", 21, "zftp", "zftp");
			System.out.println(countLogin);
			if (countLogin == 230) {
				DownFiles downFile = new DownFiles();
				// ftp下载
				boolean flag = downFile.dpwnFile("/", "boss.import.test", "D:\\ubas-boss-service/src/main/webapp/", "");
				System.out.println(flag);
			}
			Logoff closeFtp = new Logoff();
			closeFtp.Logoffs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static void startPlugin() {
		PropKit.use("jdbc.properties");
		DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbc.url1"), PropKit.get("jdbc.username1"),
				PropKit.get("jdbc.password1").trim());
		ActiveRecordPlugin arp = new ActiveRecordPlugin("mysql", druidPlugin);
		arp.addMapping("t_termuser_subscriberpackage", T_TermUserSubscriberPackage.class);
		RedisPlugin reids = new RedisPlugin(PropKit.get("redis.name"), PropKit.get("redis.host"),
				PropKit.getInt("redis.port"), PropKit.get("reids.pwd"));
		reids.start();
		druidPlugin.start();
		arp.start();
	}

	public static void main(String[] args) {
		try {
			startPlugin();
			boolean flag = false;
			Login ftpLogin = new Login();
			int countLogin = ftpLogin.Logins("192.168.0.51", 21, "zftp", "zftp");
			System.out.println(countLogin);
			if (countLogin == 230) {
				DownFiles downFile = new DownFiles();
				// ftp下载
				// flag = downFile.dpwnFile("/", "boss.import.test",
				// "D:\\ubas-boss-service/src/main/webapp/", "");
				// System.out.println(flag);

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
			if (!flag) {
				String t1 = "DELETE FROM t_termuser WHERE Fimportboss=?";
				String t2 = "DELETE FROM t_termuser_subscriberpackage WHERE Fimportboss=?";
				String t3 = "DELETE FROM t_termuser_terminfo WHERE Fimportboss=?";
				Db.update(t1, "3");
				Db.update(t2, "3");
				Db.update(t3, "3");
				
				// 说明文件下载成功
				File file = new File("D:/ubas-boss-service/src/main/webapp/boss.import.test");

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
					System.out.println(files.getName());
					HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(
							"D:/ubas-boss-service/src/main/webapp/boss.import.test" + "/" + files.getName()));

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
								String cellValue = sheet.getRow(j).getCell(k).toString();

								// System.out.println(cellValue);
								if (cellValue.contains("月-")) {

									// System.out.println(myFmt.format(sheet.getRow(j).getCell(k).getDateCellValue()));
									// 说明是日期 转换日期格式
									cellValue = myFmt.format(sheet.getRow(j).getCell(k).getDateCellValue());
								}
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

							if (j % 10 == 0) {
								if (!macSQL.equals(
										"INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss)  VALUES ")) {

									macSQL += macValue;
									macValue = "";
									macSQL = macSQL.substring(0, macSQL.lastIndexOf(","));

									if (Db.update(macSQL) >= 10) {

										macSQL = "INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss) VALUES ";
									}
									;

								}

								SQL += "( " + Value + ",'" + 3 + "')";

								Value = "";

								System.out.println(SQL);

								if (Db.update(SQL) >= 10) {
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

									// System.out.println(SQL);
									// System.out.println("Sss"+macSQL);

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
					System.out.println("本次数据导入完毕");
					// 刪除项目下文件
					// File deltefile = new
					// File("D:/ubas-boss-service/src/main/webapp/boss.import.test");
					// if (deltefile.delete()) {
					// System.out.println("文件删除完毕");

					// }

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Excel时间格式转换
	 * 
	 * @param time
	 * 
	 * 
	 */
	/**
	 * Excel日期格式转换
	 * 
	 * @param time
	 * 
	 * 
	 */
	public static String setExceldate(String date) {
		String temp = "";
		String[] dates = date.split("-");

		switch (dates[1]) {
		case "一月":
			temp = "01";
			break;
		case "二月":
			temp = "02";
			break;
		case "三月":
			temp = "03";
			break;
		case "四月":
			temp = "04";
			break;
		case "五月":
			temp = "05";
			break;
		case "六月":
			temp = "06";
			break;
		case "七月":
			temp = "07";
			break;
		case "八月":
			temp = "08";
			break;
		case "九月":
			temp = "09";
			break;
		case "十月":
			temp = "10";
			break;
		case "十一月":
			temp = "11";
			;
			break;
		case "十二月":
			temp = "12";
			;
			break;
		default:
			break;
		}

		return dates[2] + "/" + temp + "/" + dates[0];
	}

}
