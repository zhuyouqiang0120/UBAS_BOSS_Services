package com.zens.ubasbossservices.test;

import org.apache.tools.ant.taskdefs.condition.Http;

import com.chasonx.tools.HttpUtil;
import com.jfinal.kit.PathKit;

import Down.DownFiles;
import LoginDemo.Login;
import LoginDemo.Logoff;

public class DownTest {
	public static void main(String[] args) {
		Login ftpLogin=new Login();
		int countLogin = ftpLogin.Logins("192.168.0.51", 21,"zftp", "zftp");
		if (countLogin==230) {
			System.out.println("连接成功");
			DownFiles downFile = new DownFiles();
			//ftp下载
	
			boolean flag = downFile.dpwnFile("/","boss.import.test","D://ubas-boss-service/src/main/webapp:", "");
			System.out.println(flag);
			Logoff closeFtp =new Logoff();
			closeFtp.Logoffs();
			
	}
	}
	

}
