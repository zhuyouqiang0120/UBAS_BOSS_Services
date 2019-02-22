package com.zens.ubasbossservices.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.kit.PathKit;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;

import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.log.Importlog;

import Down.DownFiles;
import LoginDemo.Login;
import LoginDemo.Logoff;
import sun.misc.CEFormatException;

public class ImportBossJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
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
				Importlog.server("文件下载成功");
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

								// System.out.println(cellValue);
								if (cellValue.contains("Sep-")) {
									System.out.println(sheet.getRow(j).getCell(k));
									// System.out.println(myFmt.format(sheet.getRow(j).getCell(k).getDateCellValue()));
									// 说明是日期 转换日期格式
									cellValue = myFmt.format(sheet.getRow(j).getCell(k).getDateCellValue());
									return;
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

							if (j % 2 == 0) {
								if (!macSQL.equals(
										"INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss)  VALUES ")) {

									macSQL += macValue;
									macValue = "";
									macSQL = macSQL.substring(0, macSQL.lastIndexOf(","));

									if (Db.update(macSQL) >= 2) {

										macSQL = "INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss) VALUES ";
									}
									;

								}

								SQL += "( " + Value + ",'" + 3 + "')";

								Value = "";

								if (Db.update(SQL) >= 2) {
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
					Importlog.server(files.getName() + "本次数据导入完毕");

				}
				// 刪除项目下文件

				for (File deletefies : fileList) {
					deletefies.delete();
					System.out.println("删除" + deletefies.getName() + "文件成功");
				}

			} else {

				Importlog.server("文件下载失败");
			}
		} catch (Exception e) {
			Importlog.server("数据导入失败");
			e.printStackTrace();
		}

	}

}
