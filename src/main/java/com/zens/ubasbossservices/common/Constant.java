/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.common								 
 **    Type    Name : Constant 							     	
 **    Create  Time : 2016年9月14日 下午12:57:21								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.common;

/**常量类
 * @author  Chasonx
 * @email   xzc@zensvision.com
 * @create  2016年9月14日下午12:57:21
 * @version 1.0 
 */
public class Constant {
	
	public static final String PROJECT_NAME = "UBAS";
	public static final String PROJECT_VERSION = "1.0.0";
	public static final String PROJECT_BUILD = "20160914";
	public static final String UBASBOSS_ClientGuid = "ubasbossservices_i88n9e3t6213b37f"; //这是ubas这个系统的id。
	public static int isC1OrC2 = 1; //用来判断本次的记录数是放到缓存1还是缓存2中。值为1表示放到缓存1中，值为2表示放到缓存2中。
	public static final String IMAGE_URL="uploadFile" +"/";
	public static String token="ZHANGKAINIUBI";
	public static String EHCACHENAME="UBAS_BOSS_SERVICES_CACHE3";
	public static String LOGINGCOOKIE="LOGINGCOOKIE";
	public static String FQRcode_URL="";
	
	
	
	/************************************************短信参数*****************************************************************/	
	//验证码通知
	public static String SHORTMESSAGE="http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	//C
	public static String SHORTAPIID="C31140394";
	
	public static String SHORTAPIKEY="30cadf190dcce92e7e6607ccb199dce4";
	//短信语音通知
	public static String cccc="http://api.voice.ihuyi.com/webservice/voice.php?method=Submit";
	
	public static String aaaa="http://api.vm.ihuyi.com/webservice/voice.php?method=Submit";
	
	/**
	 * ehcache 缓存名称
	 * @author  Chasonx
	 * @email   xzc@zensvision.com
	 * @create  2016年9月19日下午5:11:27
	 * @version 1.0
	 */
	public static enum CACHE_NAME { //enum中文是枚举的意思，表示数据CACHE_NAME的类型为 “枚举类型”。枚举就是一个数据集，一组常量数据可以放在枚举中，用起来也方便选择。
		UBASDATACHCACHE,
		UBAS_BOSS_SERVICES_CACHE1,
		UBAS_BOSS_SERVICES_CACHE2
	}
	
	/**
	 * 与服务端相关的常量
	 * @author  Chasonx
	 * @email   xzc@zensvision.com
	 * @create  2016年9月19日下午4:47:05
	 * @version 1.0
	 */
	public static enum UBAS_SERVICE_CONFIG {
		SERVICE_URL, //url类型（数据库zens_ubas的t_config表中的Ftype字段的值）
		UBASUTERMINALINFOSERVICE, //UTerminalInfo服务端地址。
		NESTEDDS, //NestedDataSet服务端地址。
		UBASUBIZREGIONSERVICE //UDimension服务端地址。
	}
}
