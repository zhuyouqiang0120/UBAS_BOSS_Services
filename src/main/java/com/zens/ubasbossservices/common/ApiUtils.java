/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.common								 
 **    Type    Name : ApiUtils 							     	
 **    Create  Time : 2016年9月19日 下午5:23:49								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jfinal.plugin.redis.RedisPlugin;

import redis.clients.jedis.Jedis;

/**
 * 服务端请求的API 地址
 * 
 * @author Chasonx
 * @email xzc@zensvision.com
 * @create 2016年9月19日下午5:23:49
 * @version 1.0
 */
public class ApiUtils {

	/**
	 * hello 方法地址
	 */
	public static final String HELLO = "hello";
	public static final String NESTEDDATASET = "nesteddataset";
	public static String ACTIVITI = "";
	public static Jedis jedis;
	public static RedisPlugin reids;

	public static Jedis GetJedis() {
		try {
			Properties prop = getproperties(ApiUtils.class, "jdbc.properties");
			if (jedis == null) {
				// new JED
				jedis = new Jedis(prop.getProperty("redisIP"));
				System.out.println("连接成功");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jedis;
	}

	/**
	 * 读取JDBC配置文件
	 * 
	 * @author Zkill
	 * @create 2017年12月20日14:55:09
	 * @update
	 * @param <T>
	 *            Fret
	 * @throws IOException
	 */
	public static <T> Properties getproperties(Class<T> Configs, String Filename) throws IOException {
		InputStream inStream = Configs.getClassLoader().getResourceAsStream(Filename);
		Properties prop = new Properties();
		prop.load(inStream);
		return prop;
	}

}
