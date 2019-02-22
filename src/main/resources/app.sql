#namespace("app")
	#sql("cappuserVerificationcode")
	 INSERT INTO #(tablename)(
  	#for(x:cond)
  	#(for.index==0?"":",") #(x.key)
  	 #end
  	 ) VALUES 
  	 (
  	 #for(x:cond)
  	 #(for.index==0?"":",") '#(x.value)'
  	 #end
  	 )
	#end
	
	#sql("cappuserVerificationcodeupdate")
	UPDATE #(tablename) SET 
	#for(x:cond)
	 #(for.index==0?"":",") #(x.key)='#(x.value)'
	#end
	WHERE FTelephone='#(FTelephone)' AND FDelete!=1	
	#end
	
	#sql("selectapp")
	SELECT 
	#for(n:list)
  	#(for.index==0?"":",") #(n)
  	 #end
  	 FROM #(tablename) WHERE 
  	 #for(x:cond)
  	#(for.index==0?"":"AND") #(x.key)='#(x.value)'
  	 #end
  	 AND FDelete!=1 AND FAccount!=""
	#end
	
	
	#sql("selectcappusers")
	SELECT count(*) as num  FROM #(tablename) WHERE FAccount='#(FAccount)'  AND FDelete!=1
	#end
	
	#sql("selectcappuserVerificationcode")
	SELECT FVerificationcode FROM #(tablename) WHERE FTelephone='#(FTelephone)' AND	FDelete!=1 
	#end
	
	#sql("selectAppuser")
	SELECT * FROM (SELECT v.ID AS APPID,v.FGuid AS APPFGuid,v.FAccount AS APPFAccount,v.FPassword AS APPFPassword,v.FType AS APPFType
	,v.FHeadurl AS APPFHeadurl,v.FUserdescription AS APPFUserdescription,v.FSex AS APPFSex,v.FTelephone AS APPFTelephone,
	v.FName AS APPFName,v.FIdcard AS APPFIdcard,v.FEmail AS APPFEmail,v.FLastlogintime AS APPFLastlogintime,
	v.Flastloginip AS APPFlastloginip,v.FState AS APPFState,v.FDelete AS APPFDelete,v.FVerificationcode AS APPFVerificationcode,
	v.FCreatetime AS APPFCreatetime,v.FUpdatatime AS APPFUpdatatime,v.FVerificationcodetime AS APPFVerificationcodetime,t.FGUID FROM #(tablename) v LEFT   
	JOIN #(tablename1) s ON v.FGuid=s.FAppuserguid 
	LEFT  JOIN #(tablename2) t ON  t.FGUID=s.FTermuserguid 
	#for(x:cond)
	 	#if(x.key=='keyword')
	 	WHERE 
	   #for(n:keyword)
	   #if(n!='FTermuserguid')
	    v.#(n)  LIKE '%#(x.value)%' or
	   #end
  		#if(n=='FTermuserguid')
  		 s.FTermuserguid LIKE '%#(x.value)%' 
  		 #end
  	 #end
	#end

		#if(x.key!='keyword')
		AND v.#(x.key)='#(x.value)' 
	#end
	#end
	) a WHERE a.APPFDelete!=1  AND a.APPFAccount!=''
	#end
	
	
	
	#sql("SelectsubmitHistoryData")
	SELECT * FROM #(tablename) WHERE 
	#for(x:cond)
	#if(x.key=='keyword')
	   #for(n:keyword)
	   #(n)  LIKE '%#(x.value)%'  
	    #if(!for.last)
                or
            #end
           #if(for.last)
                AND
            #end   
	   #end
	#end
	#if(x.key!='keyword')
	 #(x.key)='#(x.value)' AND
	#end
	#end
	  FDelete!=1 AND FAppuserguid='#(FAppuserguid)'
	#end
	
	#sql("editAppuser")
  	UPDATE #(tablename) SET 
  	#for(x:cond)
  	#(for.index==0?"":",") #(x.key)='#(x.value)'
  	 #end
  	 WHERE 
  	 FGuid='#(FGuid)'  AND FDelete!=1

  	#end
  	
  	#sql("editAppuserapp")
  	UPDATE #(tablename) SET 
  	#for(x:cond)
  	#(for.index==0?"":",") #(x.key)='#(x.value)'
  	 #end
  	 WHERE 
  	 FTelephone='#(FTelephone)'  AND FDelete!=1

  	#end
	
	
 	#sql("editAppusertwo")
  	SELECT  count(FAccount) AS num  FROM #(tablename) WHERE FAccount='#(FAccount)' AND FDelete!=1  AND FGuid='#(FGuid)'
  	#end
  	
  	#sql("editAppuserthree")
  	SELECT  FState  FROM #(tablename) WHERE FAccount!='' AND FDelete!=1  AND FTelephone='#(FTelephone)'
  	#end
  	
  	#sql("editAppuserFive")
  	UPDATE #(tablename) SET FState='2' WHERE FTelephone='#(FTelephone)' AND FDelete!=1
  	#end
  	
  	#sql("editAppusersix")
  	SELECT FAccount,FName,FSex,FTelephone,FIdcard,FEmail,FUserdescription,FHeadurl
  	FROM #(tablename) WHERE FGuid='#(FGuid)' AND  FDelete!=1
  	#end
  	#sql("bindAppuser")
  	DELETE FROM #(tablename) WHERE FAppuserguid in (
  	#for(x:Applist) 
  	 	#(for.index==0?"":",") '#(x)'
  	#end
  	)
  	AND FTermuserguid in (
  	#for(x:Terlist) 
  	 	#(for.index==0?"":",") '#(x)'
  	#end
  	)
  	
  	#end
  	
  	#sql("selectbindAppuser")
  	SELECT count(*) AS num FROM #(tablename) WHERE FAppuserguid='#(FAppuserguid)' 
  	#end
  	
	#sql("insertbindAppuser")
  	INSERT INTO #(tablename) (FAppuserguid,FTermuserguid,FCreatetime) #(insertSQL)
	#end
	
	#sql("cappuser")
	SELECT FVerificationcodetime,FVerificationcode FROM #(tablename) WHERE FTelephone='#(FTelephone)' AND FDelete!=1 
	#end
	
	#sql("selectuser")
	SELECT * FROM #(tablename) WHERE 
	FGuid in(SELECT FAppuserguid FROM #(tablename1) WHERE FTermuserguid ='#(FTermuserguid )')
	#end
	
	#sql("DeleteAppuser")
	UPDATE #(tablename) SET FDelete='1'  WHERE FGuid in (
	#for(x:cond)
	#(for.index==0?"":",") '#(x)'
	#end
	)
	#end
		
	#sql("DeletesubmitHistoryData")
	UPDATE #(tablename) SET FDelete='1'  WHERE FAppuserguid in (
	#for(x:cond)
	#(for.index==0?"":",") '#(x)'
	#end
	)
	#end
	
	#sql("RetrievePassword")
	SELECT FGuid,FAccount,FVerificationcode,FVerificationcodetime FROM #(tablename)
	WHERE FTelephone='#(FTelephone)' AND FAccount!='' AND   FDelete!=1
	#end
	
	#sql("userSignintwo")
  	SELECT * FROM #(tablename) WHERE   FAccount='#(FAccount)' 
  	 AND FDelete!=1  AND  FState!=1
  	#end
	#sql("QRcodelSignin")
	SELECT count(*) AS num FROM #(tablename) 
	WHERE FAppuserguid='#(FAppuserguid)'   AND FTermuserguid='#(FTermuserguid)'
	
	#end
  	
  	
	#sql("Logonstatus")
	SELECT * FROM #(tablename) WHERE FSid='#(FSid)' AND FisOK!=1
	#end
	#sql("UpdateLogonstatus")
	UPDATE #(tablename) SET FisOK='1' WHERE FSid='#(FSid)'
	#end
	
	
	#sql("Logonstatustwo")
	SELECT * FROM #(tablename) WHERE  FGuid='#(FGuid)' AND FDelete!=1 AND FAccount!='';
	#end
	
	
	
	
	
	#sql("CreateQRcode")
	SELECT FSubsGUID FROM #(tablename)
	WHERE FTermInfoID in 
	(SELECT FGUID FROM #(tablename1) WHERE FCaCard='#(FCaCard)')
	#end
	
	#sql("DemanDcertification")
	SELECT * FROM #(tablename) WHERE FSubsGUID=(SELECT FTermuserguid  
	FROM #(tablename1)  WHERE FAppuserguid='#(FAppuserguid)')   AND
	 FSubscriberPackageGUID='#(FSubscriberPackageGUID)' AND  NOW()<str_to_date(FExpiryTime,'%Y/%m/%d %H:%i:%s')
 	#end
 	
 	#sql("DemanDcertificationtwo")
	SELECT * FROM #(tablename)  
	WHERE FSubsGUID =(SELECT FSubsGUID FROM #(tablename1)
	 WHERE FTermInfoID=
	(SELECT FGUID FROM #(tablename2) WHERE FCaCard='#(FCaCard)'))
	AND FSubscriberPackageGUID='#(FSubscriberPackageGUID)' 
	AND NOW()<str_to_date(FExpiryTime,'%Y/%m/%d %H:%i:%s')  AND 
	NOW()>str_to_date(FAvailabilityTime,'%Y/%m/%d %H:%i:%s')
	AND FSuspended!=1 AND FFreezed!=1 AND FDeleted!=1;
 	#end
	
#end