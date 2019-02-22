package com.zens.ubasbossservices.common;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class ResultUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean result;
	private List<Record> data;
	private String msg;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public List<Record> getData() {
		return data;
	}
	public void setData(List<Record> data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
