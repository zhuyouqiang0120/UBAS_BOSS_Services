package com.zens.ubasbossservices.utils;

import java.text.ParseException;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 定时任务，查询已过期业务包，并冻结
 * 
 * @author Johnson
 * @create 2017年4月14日 18:31:57
 * @update
 * @param
 * @return void
 */
public class StatisticsJobsUtil implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
	 	String sql;
		 sql="Select FEfectivetime,FGUID FROM t_subscriberpackage ";
		 //如果是自动冻结的 FFreezed=2
	    List<Record> Fectivetime=Db.find(sql);
	    //获取到当前时间
	    long newtime=System.currentTimeMillis();
	    for(Record fectivetime:Fectivetime){
	    	
			try {
				if( fectivetime.getStr("FEfectivetime")!=null){
				 long Fefectime = TimeUT.getMillsTime(fectivetime.getStr("FEfectivetime"));
				 System.out.println(Fefectime);
				   //当前业务包已过期，直接冻结
				   if(newtime>Fefectime){
		    	    	sql="Update  t_subscriberpackage  Set FFreezed= 2 Where FGUID=  ?";
		    	    	//sql="Update" +subscriberPackageTableName+"Set FEfectivetime= 1 Where FGUID= "+subscriberPackageGUID; 
		    	        Db.update(sql,fectivetime.getStr("FGUID"));
		    	    
		    	    }
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 
	    	 
	    	
	    }
	
		
	

	}

}
