/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.controller								 
 **    Type    Name : HomeController 							     	
 **    Create  Time : 2016年9月14日 上午10:46:40								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */ 
package com.zens.ubasbossservices.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * shiro realm 权限验证逻辑处理
 * @author  Chasonx
 * @email   xzc@zensvision.com
 * @create  2016年9月14日上午10:46:40
 * @version 1.0 
 */
public class MyShiroRealm  extends AuthenticatingRealm {

	/**
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken auToken) throws AuthenticationException {
		if(null == auToken) return null;
		UsernamePasswordToken uPasswordToken = (UsernamePasswordToken) auToken;
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(uPasswordToken.getUsername(),uPasswordToken.getPassword(),getName());
		
		return info; 
	}
	
	/**
	 * 授权查询 缓存中用户鉴权信息调用
	 * @param collection
	 * @return
	 *AuthenticationInfo
	 */
	protected AuthorizationInfo  doGetAuthorizationInfo(
			PrincipalCollection collection){
		if(collection == null)
			return null;
		
		Integer uid = (Integer) collection.fromRealm(getName()).iterator().next();
		return null;
	}
	
	/***
	 * 更新缓存
	 * @param principal
	 * void
	 */
	public void clearCachedAuthorizationInfo(String principals){
		SimplePrincipalCollection principal = new SimplePrincipalCollection(principals, getName());
		clearCachedAuthenticationInfo(principal);
	}

	/**
	 * 清除授权用户信息 
	 * void
	 */
	public void clearAllCachedAuthorizationInfo(){
		Cache<Object, AuthenticationInfo> cache = getAuthenticationCache();
		if(cache != null){
			for(Object key:cache.keys()){
				cache.remove(key);
			}
		}
	}
}
