package com.zens.ubasbossservices.service;

import java.io.File;

import javax.print.DocFlavor.STRING;
import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.MultipartRequest;
import com.zens.ubasbossservices.entity.T_App_User;

import android.Manifest.permission;

public interface AppService {

	
	/** 
	 * APP用户注册时向用户发生验证码
	 * @author ZKill	
	 * @create 2017年12月1日 10:22:35
	 * @update
	 * @param  param
	 * @return Record
	 */
	
	public Record cappuserVerificationcode(String param) throws Exception;
	
	
	
	/** 
	 * 注册APP用户（验证码验证）
	 * @author ZKill	
	 * @create 2017年12月1日 10:22:35
	 * @update
	 * @param  param
	 * @return Record
	 */
	public Record cappuser(String param) throws Exception;

	
	/** 
	 * 分页获取APP用户列表
	 * @author ZKill	
	 * @create 2017年12月4日 09:51:25
	 * @update
	 * @param  param
	 * @return Record
	 */
	
	public Record selectAppuser(String param) throws Exception;
	
	/**
	 * 获取金顶盒用户已绑定的APP用户信息
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 09:47:15
	 * @update
	 * @return void
	 */
	
	
	public Record  selectuser(String param) throws Exception;
	
	
	
	/** 
	 * APP用户的编辑
	 * @author ZKill	
	 * @create 2017年12月4日 09:51:25
	 * @update
	 * @param  param
	 * @return Record
	 */

	public Record editAppuser(HttpServletRequest request) throws Exception;
	
	/**
	 * APP用户解除绑定和绑定金顶盒用户
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 15:45:35
	 * @update
	 * @return Record
	 */

	public Record bindAppuser(String param, String type, String Teruserguid) throws Exception;

	
	/**
	 * 批量删除APP用户
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 15:45:35
	 * @update
	 * @return Record
	 */
	
	public Record  DeleteAppuser(String param) throws Exception;
	
	
	/**
	 * APP用户找回密码
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 15:45:35
	 * @update
	 * @return Record
	 */
	
	public Record RetrievePassword(String param) throws Exception;
	
	
	/**
	 * APP用户登录
	 * 
	 * @author ZKill
	 * @create 2017年12月4日 15:45:35
	 * @update
	 * @return Record
	 */
	

	public Record Signin(String param, HttpServletRequest httpServletRequest,String timestamp) throws Exception;
	


	/**
	 * 电视端登录生成二维码接口
	 * 
	 * @author ZKill
	 * @create 2017年12月7日 10:18:39
	 * @update
	 * @return Record
	 */

	public File CreateQRcode(HttpServletRequest request) throws Exception;
	

	/**
	 * 电视端扫二维码登录接口
	 * 
	 * @author ZKill
	 * @create 2017年12月7日 10:18:39
	 * @update
	 * @return Record
	 */


	public Record QRcodelSignin(String sid,String userguid,String mac) throws Exception;
	
	
	
	
	/**
	 * 盒子检测登录状态（盒子循环检测调用接口）
	 * 
	 * @author ZKill
	 * @create 2017年12月7日 16:35:36
	 * @update
	 * @return Record
	 */
	
	
	public Record Logonstatus(String mac) throws Exception;

	/**
	 * APP观看记录统计
	 * 
	 * @author ZKill
	 * @create 2017年12月8日 14:27:13
	 * @update
	 * @return Record
	 */
	
	
	
	public Record  submitHistoryData(String param) throws Exception;
	
	/**
	 * 删除APP用户观看记录
	 * 
	 * @author ZKill
	 * @create 2017年12月8日 14:27:13
	 * @update
	 * @return Record
	 */
	
	public Record DeletesubmitHistoryData(String param) throws Exception;
	
	
	/**
	 * 根据app用户标识查询观看记录数据
	 * 
	 * @author ZKill
	 * @create 2017年12月11日 09:41:00
	 * @update
	 * @return Record
	 */
	
	
	public Record SelectsubmitHistoryData(String param) throws Exception;
}
