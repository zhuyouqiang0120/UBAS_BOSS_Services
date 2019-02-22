/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubasboss.controller								 
 **    Type    Name : TermUserController 							     	
 **    Create  Time : 2017年2月6日 下午16:59:00								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import com.chasonx.tools.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zens.ubasbossservices.annotation.ParaEntity;
import com.zens.ubasbossservices.annotation.ParamInterceptor;
import com.zens.ubasbossservices.annotation.Required;
import com.zens.ubasbossservices.common.ResultDesc;
import com.zens.ubasbossservices.common.ResultUser;
import com.zens.ubasbossservices.interceptor.UserParamInterception;
import com.zens.ubasbossservices.service.T_TermUserService;
import com.zens.ubasbossservices.serviceimpl.T_TermUserServiceImpl;
import com.zens.ubasbossservices.utils.BossUtil;

import android.R.bool;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import net.sf.json.JSONObject;
import sun.tools.tree.ThisExpression;

/**
 * 导入用户
 * 
 * @author zyq
 * @e-mail zhuyq@zensvision.com
 * @date 2018年09月18日 下午3:34:11
 */
public class ImportUserController extends Controller {

	T_TermUserService t_TermUserService = new T_TermUserServiceImpl();

	String defRegionUrl = "";

	/*
	 * 分页获取终端用户
	 */
	@Required({ @ParaEntity(name = "param", xlen = 9000, desc = "总参数"), })
	@Clear
	@Before(ParamInterceptor.class)
	public void index() { // 用户数据导入
		//http://localhost:8080/UBAS_BOSS_Services/import/index?param={%22regionGUID%22:%22ac2967ba19e39ee9%22,%22filepath%22:%22/Users/zyq/MyWork/Work/%E4%BA%91%E5%8D%97/%E8%A5%BF%E5%8F%8C%E7%89%88%E7%BA%B3/xsbnUser.xls%22}
		String param = getPara("param");
		System.out.println(param);
		JSONObject paramData = JSONObject.fromObject(param);
		String regionGUID = (String) paramData.get("regionGUID");
		String filepath = (String) paramData.get("filepath");
		System.out.println(regionGUID);
		System.out.println(filepath);
		//regionGUID = "dac86973b01b580f";
		//filepath = "/Users/zyq/MyWork/Work/云南/西双版纳/xsbnUser.cvs";
		int count  = importCsv(filepath,regionGUID);

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson("导入成功！共导入"+count+"条记录") + ");");
		else
			renderJson("导入成功！共导入"+count+"条记录");
	}
	
	/*
	 * 上传
	 */
	 
	public void upload() { // 用户数据导入
		int count = 0;
		try {
			UploadFile file = getFile();
			String guid = getPara("guid");
			System.out.println(guid);

			File delfile = new File(file.getUploadPath() + "/" + file.getFileName());
			System.out.println(delfile.getPath());
			count = importCsv(delfile.getPath(), guid);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (StringUtils.hasText(getPara("jsonpFunc")))
			renderJavascript(getPara("jsonpFunc") + "(" + JsonKit.toJson("导入成功！共导入" + count + "条记录") + ");");
		else
			renderJson("导入成功！共导入" + count + "条记录");
	}
	
	public int importCsv(String filepath,String regionGUID) {
		List<String> dataList = new ArrayList<String>();
		int count = 0;

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(filepath)), "GBK"));
			String line = "";
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				//dataList.add(line.trim());
			}
		} catch (Exception e) {
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("GBK");
		try {
			Workbook book = Workbook.getWorkbook(new File(filepath), workbookSettings);
			Sheet rs = book.getSheet(0);

			for (int j = 0; j < rs.getRows(); j++) {
				StringBuffer line = new StringBuffer();
				for (int i = 0; i < rs.getColumns(); i++) {
					String name = rs.getCell(i, j).getContents().trim();
					line.append(name + ",");
					// System.out.print(name + " ");
				}
				// System.out.println(line.toString());
				dataList.add(line.toString());
			}
			System.out.println("data  " + dataList.size());
			System.out.println(dataList.get(0));

			book.close();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// List<String> dataList = importCsv(new
		// File("/Users/zyq/MyWork/Work/云南/西双版纳/test.csv"));
        System.out.println("data  "+dataList.size());
		if (dataList != null && !dataList.isEmpty()) {
			for (int j = 0; j < dataList.size(); j++) {
				System.out.println(dataList.get(j));
				String[] pills = dataList.get(j).replace("	", "").replaceAll("\"", "").split(",");
				
				for (int i = 0; i < pills.length; i++) {
					System.out.print(pills[i].trim() + "|");
				}
				
				
				UUID uuid1 = UUID.randomUUID();
				UUID uuid2 = UUID.randomUUID();
				String sql = "select * from t_terminfo where FCaCard = '"+ pills[14].trim() +"'";
				List<Record> records = Db.query(sql);
				System.out.println(pills[14].trim()+"   "+records.size());
				if(records.size() == 0) {
					String sql1 = "INSERT INTO  t_termuser (FGUID,FIdCard,FName,FAddr,FMobile,FIphone	,FLocationcode)  VALUES " + "('" + uuid1 + "'" + ",'" + pills[7].trim() + "'" + ",'" + pills[2].trim() + "'" + ",'" + pills[19].trim()+ "','" + (pills[20].trim().length() == 7 ? "0691-"+pills[20].trim() : "0691-1234567")+ "','" + (pills[21].trim().length() == 11 ? pills[21].trim() : "13888888888") + "', '1001')";
					String sql2 = "INSERT INTO  t_terminfo (FGUID,FCaCard,FSerial,FIp)  VALUES " + "('" + uuid2 + "'" + ",'" + pills[14].trim() + "'" + ",'" + pills[15].trim() + "'" + ",'192.168.0.1')";
					String sql3 = "INSERT INTO  t_termuser_terminfo (FSubsGUID,FTermInfoID,Fimportboss) VALUES" + "('" + uuid1 + "'" + ",'" + uuid2 + "'" + ",'3')";
					Db.update(sql3);
					
					String sql4 = "INSERT INTO  t_termuser_region (FSubsGUID,FRegionGUID) VALUES" + "('" + uuid1 + "'" + ",'"+ regionGUID +"')";
					
					String sql5 = "INSERT t_account (FUserGUID,FAccountbalance,FCreatetime,FUpdatetime) VALUES " + "('" + uuid1 + "'" + ", '10000', '"+ formatTimes(new Date()) +"', '"+ formatTimes(new Date())  +"' )";
					
					Db.update(sql1);
					Db.update(sql2);
					Db.update(sql5);
					
					if(regionGUID.length() != 0) {
						Db.update(sql4);
					}
					
					count ++;
				}
				
				// System.out.println(pills[0].trim()+"|"+pills[1].trim()+"|"+pills[2].trim()+"|"+pills[3].trim()+"|"+pills[4].trim()+"|"+pills[5].trim()+"|"+pills[6].trim()+"|"+pills[7].trim()+"|"+pills[8].trim()+"|"+pills[9].trim()+"|"+pills[10].trim()+"|"+pills[11].trim()+"|"+pills[12].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim());
				// System.out.println(dataList.get(1).trim().replace(" ", ""));
			}
		}
		
		return count;
	}
	
	public String formatTimes(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = sdf.format(new Date());
		System.out.println(dateStr);
		String dateStr1 = sdf.format(new Date());
		System.out.println(dateStr1);
	}
}