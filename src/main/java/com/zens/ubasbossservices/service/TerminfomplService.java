package com.zens.ubasbossservices.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public interface TerminfomplService {

	public Record getterminfo(String param) throws Exception;

	public boolean addterminfo(String param) throws Exception;

	public boolean updateminfo(String param) throws Exception;

	public List<Record> getRegionterminfo(String param) throws Exception;
	
	

}
