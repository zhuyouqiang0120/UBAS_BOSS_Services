/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS
 **    Package Name : com.zens.ubas.common								 
 **    Type    Name : ResultDesc 							     	
 **    Create  Time : 2016年12月19日 上午10:40:32								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.common;


import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
 * @author Jhonson
 * @email yangsen@zensvision.com
 * @create 2016年12月19日上午10:40:32
 * @version 1.0
 */
public class ResultDesc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private boolean result;
	private int code;
	private String action;
	private String method;
	private Record data;
	private List<Record> listdata;
	

	public List<Record> getListdata() {
		return listdata;
	}

	public void setListdata(List<Record> listdata) {
		this.listdata = listdata;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return the data
	 */
	public Record getData() {
		return data == null ? new Record() : data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Record data) {
		this.data = data;
	}
}



