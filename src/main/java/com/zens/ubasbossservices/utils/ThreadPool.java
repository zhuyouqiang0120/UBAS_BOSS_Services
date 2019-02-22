package com.zens.ubasbossservices.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 /**
 * @author 开发者
 *
 */
public class ThreadPool {
	public static ExecutorService pool;
	//public static boolean flag;
	static {
		pool = Executors.newFixedThreadPool(10);
	}



}
