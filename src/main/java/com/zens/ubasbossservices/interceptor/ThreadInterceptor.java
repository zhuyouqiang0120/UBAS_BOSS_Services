package com.zens.ubasbossservices.interceptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.zens.ubasbossservices.utils.ThreadPool;

/**
 * @author 开发者
 *
 */
public class ThreadInterceptor implements Interceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.aop.Interceptor#intercept(com.jfinal.aop.Invocation)
	 */
	public void intercept(final Invocation inv) {


		
		
		try {

			Thread r = new Thread(new Runnable() {

				public void run() {
				
					inv.invoke();

				}

			});
			ThreadPool.pool.execute(r);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
