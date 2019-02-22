package com.zens.ubasbossservices.interceptor;

import java.io.File;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

/**
 * 用户档案信息图片参数解析拦截器
 * 
 * @author ZKill
 * @email zhangkai@zensvision.com
 * @create 2017年6月14日 15:18:16
 * @version 1.0
 */
public class UserParamInterception implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		String stdPicURL = "";
		String fpPicURL = "";
		UploadFile uploadFiles = controller.getFile("stdPhotoLoad");
		UploadFile fpuploadFiles = controller.getFile("fpPhotoLoad");
		if (uploadFiles != null) {
			stdPicURL = "upload////"
		 + uploadFiles.getFileName();
		}
		if (fpuploadFiles != null) {
			fpPicURL = "upload////"

					+ fpuploadFiles.getFileName();
		}

		String sex = controller.getPara("sex");
		if (sex.equals("未知")) {
			sex = "0";
		} else if (sex.equals("男")) {
			sex = "M";
		} else if (sex.equals("女")) {
			sex = "F";
		} else {
			sex = "H";
		}
		String GUID = controller.getPara("GUID");
		String name = controller.getPara("name");
		String exname = controller.getPara("exname");
		String pidAddr = controller.getPara("pidaddr");
		String age = controller.getPara("age");
		String mobile = controller.getPara("mobile");
		String iphone = controller.getPara("Iphone");
		String pidSN = controller.getPara("pidSN");
		String minorLang = controller.getPara("minorlang");
		String majorLang = controller.getPara("majorlang");
		String height = controller.getPara("height");
		String cop = controller.getPara("cop");
		if (cop != null) {
			if (cop.equals("黑色")) {
				cop = "BK";
			} else if (sex.equals("绿色")) {
				cop = "GN";
			} else if (sex.equals("蓝色")) {
				cop = "BU";
			} else {
				cop = "BN";
			}
		} else {
			cop = "";
		}
		String coh = controller.getPara("coh");
		if (coh != null) {
			if (coh.equals("黑色")) {
				coh = "BK";
			} else if (sex.equals("棕色")) {
				coh = "BN";
			} else if (sex.equals("红色")) {
				coh = "RD";
			} else if (sex.equals("橙色")) {
				coh = "OG";
			} else if (sex.equals("黄色")) {
				coh = "YE";
			} else if (sex.equals("绿色")) {
				coh = "GN";
			} else if (sex.equals("蓝色(或淡蓝色)")) {
				coh = "BU";
			} else if (sex.equals("紫色(紫红)")) {
				coh = "VT";
			} else if (sex.equals("灰色(蓝灰)")) {
				coh = "GY";
			} else if (sex.equals("白色")) {
				coh = "WH";
			} else if (sex.equals("粉红色")) {
				coh = "PK";
			} else if (sex.equals("金黄色")) {
				coh = "GD";
			} else if (sex.equals("青绿色")) {
				coh = "TQ";
			} else if (sex.equals("银白色")) {
				coh = "SR";
			} else if (sex.equals("绿/黄双色")) {
				coh = "GNYE";
			}
		} else {
			coh = "";
		}
		String province = controller.getPara("selectp");// 选择省份
		String city = controller.getPara("selectc");// 选择市区
		String dob = controller.getPara("birthday");
		String pidPOVBegin = controller.getPara("pidpovbegin");
		String pidPOVEnd = controller.getPara("pidpovend");
		String prisonernumber = controller.getPara("prisonernumber");
		String prisonerlevel = controller.getPara("prisonerlevel");
		String prisonstatus = controller.getPara("prisonstatus");
		if (prisonstatus != null) {
			if (prisonstatus.equals("未知")) {
				prisonstatus = "0";
			} else if (prisonstatus.equals("在押")) {
				prisonstatus = "11";
			} else if (prisonstatus.equals("隔离")) {
				prisonstatus = "12";
			} else if (prisonstatus.equals("保外就医")) {
				prisonstatus = "21";
			} else if (prisonstatus.equals("保释")) {
				prisonstatus = "22";
			} else if (prisonstatus.equals("在逃")) {
				prisonstatus = "41";
			} else if (prisonstatus.equals("狱内死亡")) {
				prisonstatus = "42";
			} else if (prisonstatus.equals("保外死亡")) {
				prisonstatus = "43";
			} else if (prisonstatus.equals("刑满获释")) {
				prisonstatus = "81";
			} else if (prisonstatus.equals("减刑获释")) {
				prisonstatus = "82";
			} else if (prisonstatus.equals("加刑获释")) {
				prisonstatus = "83";
			}
		} else {
			prisonstatus = "";
		}
		String jailtime = controller.getPara("jailtime");
		String releasetime = controller.getPara("releasetime");
		String outprisonTime = controller.getPara("outprisontime");
		String crimeCauses = controller.getPara("crimeCauses");
		System.out.println(stdPicURL);
		System.out.println(fpPicURL);
		String param = "{\"stdPicURL\":\"" + stdPicURL + "\",\"fpPicURL\":\"" + fpPicURL + "\",\"name\":\"" + name
				+ "\",\"exName\":\"" + exname + "\",\"pidAddr\":\"" + pidAddr + "\",\"age\":\"" + age
				+ "\",\"mobile\":\"" + mobile + "\",\"iphone\":\"" + iphone + "\",\"pidSN\":\"" + pidSN
				+ "\",\"minorLang\":\"" + minorLang + "\",\"majorLang\":\"" + majorLang + "\",\"height\":\"" + height
				+ "\",\"cop\":\"" + cop + "\",\"coh\":\"" + coh + "\",\"province\":\"" + province + "\",\"city\":\""
				+ city + "\",\"dob\":\"" + dob + "\",\"pidPOVBegin\":\"" + pidPOVBegin + "\",\"pidPOVEnd\":\""
				+ pidPOVEnd + "\",\"prisonerlevel\":\"" + prisonerlevel + "\",\"prisonstatus\":\"" + prisonstatus
				+ "\",\"jailtime\":\"" + jailtime + "\",\"releasetime\":\"" + releasetime + "\",\"prisonernumber\":\""
				+ prisonernumber + "\",\"crimeCauses\":\"" + crimeCauses + "\",\"outprisonTime\":\"" + outprisonTime
				+ "\",\"sex\":\"" + sex + "\",\"GUID\":\"" + GUID + "\"}";
		String actionName = inv.getActionKey();
		String methodName = inv.getMethodName();
		System.out.println(param);
		controller.setAttr("param", param);
		controller.setAttr("_actionKey", actionName);
		controller.setAttr("_methodName", methodName);
   
		inv.invoke();

	}

}