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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

/**
 * 自定义Shiro  SessionDao 管理
 * @author  Chasonx
 * @email   xzc@zensvision.com
 * @create  2016年9月14日上午10:46:40
 * @version 1.0 
 */
public class MySessionDao extends AbstractSessionDAO {
	
	public static Map<Serializable, Session> sessionMap = new HashMap<Serializable, Session>();
	
	private  String activeSessionsCacheName;

	/**
	 * @return the activeSessionsCacheName
	 */
	public String getActiveSessionsCacheName() {
		return activeSessionsCacheName;
	}

	/**
	 * @param activeSessionsCacheName the activeSessionsCacheName to set
	 */
	public void setActiveSessionsCacheName(String activeSessionsCacheName) {
		this.activeSessionsCacheName = activeSessionsCacheName;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#delete(org.apache.shiro.session.Session)
	 */
	@Override
	public void delete(Session session) {
		sessionMap.remove(session.getId());
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#getActiveSessions()
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		return sessionMap.values();
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#update(org.apache.shiro.session.Session)
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		sessionMap.put(session.getId(), session);
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doCreate(org.apache.shiro.session.Session)
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		sessionMap.put(sessionId, session);
		
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doReadSession(java.io.Serializable)
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		return sessionMap.get(sessionId);
	}

}
