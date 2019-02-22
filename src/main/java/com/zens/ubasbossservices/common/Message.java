package com.zens.ubasbossservices.common;

import java.util.function.Supplier;

public class Message extends Throwable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @throws Exception
	 * 
	 */

	public Message(String Message) throws Exception {
		throw new Exception(Message);
	}



}
