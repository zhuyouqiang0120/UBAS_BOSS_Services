/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS_BOSS_Services
 **    Package Name : com.zens.ubas.config								 
 **    Type    Name : Dconfig 							     	
 **    Create  Time : 2016年9月13日 下午4:18:47								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.config;

import java.io.File;
import java.util.List;

import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors; 
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.zens.ubasbossservices.common.ApiUtils;
import com.zens.ubasbossservices.common.Constant;
import com.zens.ubasbossservices.controller.ActivitiController;
import com.zens.ubasbossservices.controller.AppController;
import com.zens.ubasbossservices.controller.ImportUserController;
import com.zens.ubasbossservices.controller.LoginController;
import com.zens.ubasbossservices.controller.SubscriberPackageController;
import com.zens.ubasbossservices.controller.TermUserController;
import com.zens.ubasbossservices.controller.TerminfoController;
import com.zens.ubasbossservices.controller.UBAS_APPControllers;
import com.zens.ubasbossservices.controller.UBAS_AcdemoController;
import com.zens.ubasbossservices.entity.T_User;
import com.zens.ubasbossservices.entity.T_termuser_appuser;
import com.zens.ubasbossservices.entity.t_Appuser_Resource;
import com.zens.ubasbossservices.entity.T_Account;
import com.zens.ubasbossservices.entity.T_App_User;
import com.zens.ubasbossservices.entity.T_Authentication_log;
import com.zens.ubasbossservices.entity.T_Config;
import com.zens.ubasbossservices.entity.T_NestedDataSet;
import com.zens.ubasbossservices.entity.T_Order_log;
import com.zens.ubasbossservices.entity.T_Scavenging;
import com.zens.ubasbossservices.entity.T_Strategy;
import com.zens.ubasbossservices.entity.T_SubscriberPackage;
import com.zens.ubasbossservices.entity.T_Subscriberpackage_strategy;
import com.zens.ubasbossservices.entity.T_SysUser;
import com.zens.ubasbossservices.entity.T_System_termuser;
import com.zens.ubasbossservices.entity.T_TermInfo;
import com.zens.ubasbossservices.entity.T_TermUser;
import com.zens.ubasbossservices.entity.T_TermUserGroup;
import com.zens.ubasbossservices.entity.T_TermUserRegion;
import com.zens.ubasbossservices.entity.T_TermUserSubscriberPackage;
import com.zens.ubasbossservices.entity.T_TermUserTermInfo;
import com.zens.ubasbossservices.entity.T_Token;
import com.zens.ubasbossservices.httpapi.GroupServiceController;
import com.zens.ubasbossservices.httpapi.RegionServiceController;
import com.zens.ubasbossservices.interceptor.MyExceptionInterceptor;
import com.zens.ubasbossservices.utils.ImportBossJob;
import com.zens.ubasbossservices.utils.StatisticsJobsUtil;

import cn.dreampie.web.handler.SkipHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 
 * @author Chasonx
 * @email xzc@zensvision.com
 * @create 2016年9月14日上午10:15:47
 * @version 1.0
 */
public class Dconfig extends JFinalConfig { // 这个类被配置在了/UBAS/src/main/webapp/WEB-INF/web.xml中。

	public static Routes shiroRouters = null;
	public static String dPlugin = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.config.JFinalConfig#configConstant(com.jfinal.config.
	 * Constants)
	 */
	@Override
	public void configConstant(Constants con) {

		con.setDevMode(true);
		con.setViewType(ViewType.FREE_MARKER);
		con.setMaxPostSize(10 * 1024 * 1024);
		con.setEncoding("UTF-8");
		con.setBaseUploadPath(PathKit.getWebRootPath() + "/");
		System.out.println(PathKit.getWebRootPath());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.config.JFinalConfig#configRoute(com.jfinal.config.Routes)
	 */
	@Override
	public void configRoute(Routes routes) {
		routes.setBaseViewPath("/WEB-INF/views");
		// 系统常规路由
		routes.add("/region", RegionServiceController.class);
		routes.add("/group", GroupServiceController.class);
		routes.add("/termUser", TermUserController.class);
		routes.add("/subscriberPackage", SubscriberPackageController.class);
		routes.add("/terminfo", TerminfoController.class);
		routes.add("/ubas_app", UBAS_APPControllers.class);
		routes.add("/app", AppController.class);
		routes.add("/ubas_activiti", ActivitiController.class);
		routes.add("/ubas_acdemo", UBAS_AcdemoController.class);
		routes.add("/import", ImportUserController.class);
		routes.add("/login", LoginController.class);

		// 对外API路由
		shiroRouters = routes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jfinal.config.JFinalConfig#configPlugin(com.jfinal.config.Plugins)
	 */
	@Override
	public void configPlugin(Plugins plugins) {
		loadPropertyFile("jdbc.properties");
		dPlugin = getProperty("jdbc.dPlugin");// 分配数据源
		// 数据库连接池（主数据源）
		DruidPlugin dPlugin1 = new DruidPlugin(getProperty("jdbc.url1"), getProperty("jdbc.username1"),
				getProperty("jdbc.password1"));
		plugins.add(dPlugin1);
		ActiveRecordPlugin ar1 = new ActiveRecordPlugin("dPlugin1", dPlugin1);
		plugins.add(ar1);
		ar1.setShowSql(true);
		// ar1.setTransactionLevel(transactionLevel)设置事务；
		// 开启SQL防火墙
		WallFilter wallFilter1 = new WallFilter();
		wallFilter1.setDbType("mysql");
		dPlugin1.addFilter(wallFilter1);
		ar1.setDevMode(true);
		ar1.setTransactionLevel(4);
		ar1.addMapping("t_config", T_Config.class);
		ar1.addMapping("t_test", T_User.class);
		ar1.addMapping("t_termuser_region", T_TermUserRegion.class);
		ar1.addMapping("t_termuser_group", T_TermUserGroup.class);
		ar1.addMapping("t_termuser", T_TermUser.class);
		ar1.addMapping("t_subscriberpackage", T_SubscriberPackage.class);
		ar1.addMapping("t_termuser_subscriberpackage", T_TermUserSubscriberPackage.class);
		ar1.addMapping("t_terminfo", T_TermInfo.class);
		ar1.addMapping("t_termuser_terminfo", T_TermUserTermInfo.class);
		ar1.addMapping("t_sysuser", T_SysUser.class);
		ar1.addMapping("t_token", T_Token.class);
		ar1.addMapping("t_strategy", T_Strategy.class);
		ar1.addMapping("t_subscriberpackage_strategy", T_Subscriberpackage_strategy.class);
		ar1.addMapping("t_account", T_Account.class);
		ar1.addMapping("t_order_log", T_Order_log.class);
		ar1.addMapping("t_authentication_log", T_Authentication_log.class);
		ar1.addMapping("t_app_user", T_App_User.class);
		ar1.addMapping("t_termuser_appuser", T_termuser_appuser.class);
		ar1.addMapping("t_scavenging", T_Scavenging.class);
		ar1.addMapping("t_appuser_resource", t_Appuser_Resource.class);
		ar1.addMapping("t_system_termuser", T_System_termuser.class);
		// ar1.addMapping("t_appdatainfo", T_Authentication_log.class);
		ar1.setBaseSqlTemplatePath(PathKit.getRootClassPath());
		ar1.addSqlTemplate("ubas.sql");
		ar1.addSqlTemplate("app.sql");
		// 数据库连接池（辅数据源）
		DruidPlugin dPlugin2 = new DruidPlugin(getProperty("jdbc.url2"), getProperty("jdbc.username2"),
				getProperty("jdbc.password2"));
		plugins.add(dPlugin2);

		ActiveRecordPlugin ar2 = new ActiveRecordPlugin("dPlugin2", dPlugin2);
		ar2.setShowSql(true);
		plugins.add(ar2);
		DruidPlugin dPlugin3 = new DruidPlugin(getProperty("jdbc.url3"), getProperty("jdbc.username3"),
				getProperty("jdbc.password3"));
		plugins.add(dPlugin3);
		ActiveRecordPlugin ar3 = new ActiveRecordPlugin("dPlugin3", dPlugin3);
		plugins.add(ar3);
		ar3.setShowSql(true);
		// 开启SQL防火墙
		WallFilter wallFilter2 = new WallFilter();
		wallFilter2.setDbType("mysql");
		dPlugin2.addFilter(wallFilter2);

		ar2.setDevMode(true);
		ar2.setTransactionLevel(4);
		ar2.addMapping("t_nesteddataset", T_NestedDataSet.class);

		// 开启缓存
		EhCachePlugin cachePlugin = new EhCachePlugin();
		// 定时任务插件 目前配置了一个每10分钟执行一次的定时脚本
		QuartzPlugin quartzPlugin = new QuartzPlugin("job.properties");
		// redis數據庫

		RedisPlugin reids = new RedisPlugin(getProperty("redis.name"), getProperty("redis.host"),
				getPropertyToInt("redis.port"), getProperty("redis.pwd"));
		// RedisPlugin reids = new RedisPlugin
		// reids.start();
		System.out.println(reids);
		plugins.add(reids);

		plugins.add(cachePlugin);
		plugins.add(quartzPlugin);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.config.JFinalConfig#configInterceptor(com.jfinal.config.
	 * Interceptors)
	 */
	@Override
	public void configInterceptor(Interceptors interceptors) {

		// me.addGlobalActionInterceptor(new GlobalException());

		interceptors.addGlobalActionInterceptor(new MyExceptionInterceptor());

		setDefaultEhcache();
		QuartzPlugin qua = new QuartzPlugin();
		// cron表达式 0 */1 * * * ?每分钟执行一次（测试使用））
		// 每日凌晨1点 执行一次0 0 1 * * ?
		qua.add("0 0 1 * * ?", new StatisticsJobsUtil());
		// qua.add("0 */1 * * * ?", new ImportBossJob());
		qua.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jfinal.config.JFinalConfig#configHandler(com.jfinal.config.Handlers)
	 */
	@Override
	public void configHandler(Handlers handlers) {

		handlers.add(new ContextPathHandler("PATH"));
		handlers.add(new SkipHandler("/api/*"));
		handlers.add(new UrlSkipHandler("/apidoc", false));
		handlers.add(new UrlSkipHandler("/sub", false));

	}

	/**
	 * 默认缓存内容
	 * 
	 * @author chasonx
	 * @create 2016年9月19日 下午5:14:31
	 * @update
	 * @param
	 * @return void
	 */
	private void setDefaultEhcache() {
		List<T_Config> tConfigs = T_Config.config.find(
				"SELECT * FROM " + TableMapping.me().getTable(T_Config.class).getName() + " WHERE Ftype = ?",
				Constant.UBAS_SERVICE_CONFIG.SERVICE_URL.toString()); // 到数据库表t_config中，查询出属性Ftype的值为SERVICE_URL的记录。
		for (int i = 0; i < tConfigs.size(); i++) {
			T_Config tConfig = tConfigs.get(i);
			// System.out.println(tConfig.get("FconfigName")+"本语句打印自/UBAS/src/main/java/com/zens/ubas/config/Dconfig.java");
			CacheKit.put(Constant.CACHE_NAME.UBASDATACHCACHE.toString()/* 缓存名 */, tConfig.get("FconfigName")/* 键名 */,
					tConfig.get("FconfigValue")/* 值名 */); // 取出tConfig这个记录属性FconfigValue所对应的值http://localhost:8080/UBAS-SERVICE/api/，并将这个值通过CacheKit.put放到缓存中。
		}
	}

	// 启动时加载
	@Override
	public void afterJFinalStart() {
		System.out.println("start UBAS.....");
		String sql = "SELECT FconfigValue FROM " + T_Config.tableName + " WHERE FconfigName=?";
		Constant.FQRcode_URL = Db.findFirst(sql, "QRcode").getStr("FconfigValue");
		String ActSQL = "SELECT FconfigValue FROM " + T_Config.tableName + " WHERE FconfigName=?";
		ApiUtils.ACTIVITI = Db.findFirst(ActSQL, "ACTIVITI").getStr("FconfigValue");
		/*
		 * try { System.out.println("start Jfinal");
		 * Constant.Information=Db.findFirst(Db.getSqlPara("DGInform.Inforurl",
		 * Kv.by("tablename",TglobalProperty.tablename))).get("Fremotedir");
		 * System.out.println("start 加载"+Constant.Information); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */

		// super.afterJFinalStart();
		// Cache cache = Redis.use();
		// System.out.println("" +
		// cache.getJedis().get("05f5842f73b9cf328506b70b12b3d087"));
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}

}