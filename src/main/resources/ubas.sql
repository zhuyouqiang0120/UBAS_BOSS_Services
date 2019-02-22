#namespace("subscriberpackage")
	#sql("addsubscriberpackage")
	SELECT count(*) AS num FROM #(tablename) WHERE FProductID='#(FProductID)' AND FDeleted!=1
	#end
	   	
   #sql("selects")
   SELECT #(d) FROM #(tablename) WHERE #(wheres) 
	#end
   	
   	
#end
#namespace("stategy")
	
   	#sql("getstategy")
    SELECT ID AS id,FGUID AS GUID,FCycle AS cycle,FCode AS code,FName AS name,FCreateTime AS createTime,FCreateUserID AS createUserID,FUpdateTime AS updateTime,FEfectivetime AS efectivetime,FUpdateUserID AS updateUserID,FFreezed AS freezed,FDeleted AS deleted,FExt AS ext,FAvailabilityTime AS availabilityTime,FProductID AS productID,FLifenumber AS lifenumber,FPrice AS price,FLifecycle AS lifecycle  
    FROM #(tablename) #(WHERES) 
   ORDER BY ID DESC
  	#end
    #sql("tgetstategy")
	UPDATE #(tablename) SET FFreezed='2' WHERE FEfectivetime!='' AND NOW()>str_to_date(FEfectivetime, '%Y/%m/%d %H:%i:%s') AND FDeleted!=1
  	#end
  	#sql("setsubstategy")
     INSERT INTO #(tablename) (FSubguid,FStrategyguid,FCrecatetime,FAvailabilityTime)  VALUES(#(FSubguid),#(FStrategyguid),#(FCrecatetime),#(FAvailabilityTime));
  	#end
  	 #sql("setsubstategy1")
   SELECT FCycle,FEfectivetime,FAvailabilityTime FROM #(tablename) WHERE FGUID='#(SubGUID)'
  	#end
  	#sql("setsubstategy2")
   UPDATE #(tablename) SET FEfectivetime='#(FEfectivetime)',FLifecycle='#(FLifecycle)',FAvailabilityTime='#(FAvailabilityTime)'  WHERE FGUID='#(FStrategyguid)'
  	#end
  	#sql("setsubstategy3")
   SELECT FLifenumber  FROM #(tablename)  WHERE FGUID='#(FStrategyguid)'
  	#end
    #sql("setsubstategycount")
	SELECT FSubguid,FStrategyguid from t_subscriberpackage_strategy  where FSubguid=#para(FSubguid) AND FStrategyguid=#para(FStrategyguid) AND FDeleted!=1;		 
  	#end
  	#sql("setstategysub")
  	UPDATE #(tablename) SET FSubGUID=#(FSubGUID)  WHERE FGUID =#(FGUID)
  	#end
  	#sql("deletesubstategy")
   	UPDATE   #(tablename) SET FDeleted='1' WHERE  FSubGUID='#(FSubGUID)' AND FStrategyguid	='#(FStrategyguid)';		 
  	#end
  	#sql("deletestategysub")
  	UPDATE #(tablename) SET FDeleted='1' WHERE FStrategyguid ='#(Strategyguid)'		 
  	#end
  	#sql("tdeletestategysub")
  	SELECT count(*) AS num FROM #(tablename) WHERE FSubscriberPackageGUID='#(FSubscriberPackageGUID)' and FDeleted!=1  AND NOW()<str_to_date(FExpiryTime, '%Y/%m/%d %H:%i:%s')
  	#end
    #sql("selectSubstrategy")
     SELECT * FROM #(tablename1) WHERE FGUID in (SELECT distinct(FStrategyguid) FROM #(tablename) WHERE FSubguid='#(FSubGUID)' AND FDeleted!=1 AND FFreezed!=1) AND FDeleted!=1 AND FFreezed!=1
  	#end
  	 #sql("selectunSubstrategy")
     SELECT * FROM #(tablename1) WHERE FGUID not in(SELECT FStrategyguid FROM #(tablename) WHERE FSubguid='#(FSubGUID)' AND FDeleted!=1 AND FFreezed!=1) AND FLifecycle='0' AND FDeleted!=1 AND FEfectivetime!=''
  	#end
  	 #sql("addstrategy")
     INSERT INTO #(tablename) (FGUID,FName,FProductID,FPrice,FLifenumber,FCreateTime) values ('#(FGUID)','#(FName)','#(FProductID)','#(FPrice)','#(FLifenumber)','#(FCreateTime)')
  	#end
     #sql("updatestrategy")
    UPDATE #(tablename)  SET  #(Param) FUpdateTime='#(FUpdateTime)' WHERE FGUID ='#(FStrategyguid)'
  	#end
  	 #sql("updatedeletestrategy")
    UPDATE  #(tablename) SET  FDeleted='1'  WHERE FGUID ='#(FStrategyguid)'
  	#end
  	 #sql("updatedeletesubstrategy")
    UPDATE  #(tablename)  SET FDeleted='1' WHERE FStrategyguid in('#(FStrategyguid)')
  	#end
  	#sql("updateffreezed")
  	UPDATE #(tablename)  SET FFreezed='#(FFreezed)' WHERE FDeleted!=1 AND FGUID in(SELECT FStrategyguid FROM #(tablename1)  WHERE FDeleted!=1 AND FSubguid='#(FSubguid)')
  	#end
  	#sql("updatedelete")
     UPDATE #(tablename)  SET FLifecycle='0',FEfectivetime='',FAvailabilityTime='',FUpdateTime='#(FUpdateTime)'WHERE FGUID ='#(FStrategyguid)'
  	#end
  	 #sql("strategysub")
    SELECT s.* FROM #(tablename) t JOIN #(tablename1)  s ON t.FSubguid=s.FGUID WHERE t.FStrategyguid='#(FStrategyguid)' AND t.FDeleted!=1 ORDER BY s.ID desc
  	#end
  	
  	#sql("unstrategysub")
   SELECT * FROM #(tablename) WHERE FGUID not IN(SELECT FSubguid FROM #(tablename1) WHERE FStrategyguid='#(FStrategyguid)'  AND FDeleted!=1) AND FDeleted!=1 AND FFreezed!=1  AND FEfectivetime>'#(newTIME)' ORDER BY ID DESC
  	#end
  	
 #end
 #namespace("SubcriberPackage")
 	#sql("getByTermUser")
 	SELECT  t.FExpiryTime AS expiryTime,t.FSubscriberPackageGUID AS subscriberPackageGUID,b.FGUID AS GUID,b.FType AS type,b.FCycle AS cycle,b.FCode AS code,b.FName AS Name,b.FPrice AS price,b.FCreateTime AS createTime,b.FCreateUserID AS createUserID,b.FUpdateTime AS updateTime,b.FUpdateUserID AS updateUserID,b.FFreezed AS freezed,b.FDeleted AS deleted,b.FExt AS ext,b.FEfectivetime AS efectivetime,b.FAvailabilityTime AS availabilityTime,b.FProductID AS productID,b.FFreezed AS freezed,b.FUpdateTime AS updateTime,b.FPrice AS price  
 	FROM #(tablename) t JOIN #(tablename1) b ON t.FSubscriberPackageGUID=b.FGUID 
 	WHERE FSubsGUID='#(FSubsGUID)'
 	#end
 	#sql("getsubstr")
	select * from(
	SELECT  FGUID,FName,FProductID,FCycle,FPrice FROM  #(tablename) WHERE FFreezed not in (1,2,4) AND FDeleted !=1
	 union
	SELECT  FGUID,FName,FProductID,FLifecycle AS FCycle,FPrice FROM  #(tablename1) WHERE FFreezed not in (1,2) AND FDeleted !=1 AND FLifecycle!=0

	)as temp
 	#end
 
  #end
  
  
  
   #namespace("termuser")
   #sql("finterruser")
   SELECT FFreezed FROM #(tablename) WHERE FGUID='#(FGUID)';
   #end
    #sql("adduserterminfo")
   	SELECT count(*) AS num FROM #(tablename) WHERE #(terminfokey)='#(userterminfo)' AND FDeleted!=1 
   	#end
   	  #sql("updateuserterminfo")
   	SELECT count(*) AS num FROM #(tablename) WHERE #(terminfokey)='#(userterminfo)' AND FDeleted!=1  AND  FGUID!=(SELECT FTermInfoID FROM t_termuser_terminfo WHERE FSubsGUID='#(SubsGUID)' AND FDeleted!=1 ) 
   	#end
    #sql("updatetermin")
   	SELECT * FROM #(tablename) WHERE FGUID='#(FGUID)'
   	#end
   	 #sql("updatetermuser")
     SELECT * FROM #(tablename) WHERE FGUID='#(FGUID)'
   	  #end
   	 #sql("hasSubproduct")
	 SELECT i.FSubsGUID AS SubsGUID,i.FFreezed AS FFreezed  FROM #(tablename) i JOIN #(tablename1) f ON i.FTermInfoID=f.FGUID WHERE #(params)
   	#end
   	  	 #sql("hasSubproductTesting")
	 UPDATE #(tablename) SET FDeleted=CASE WHEN NOW()>str_to_date(FExpiryTime, '%Y/%m/%d %H:%i:%s') THEN 1 ELSE 0 END WHERE FSubsGUID=('#(SubsGUID)') AND FDeleted!=1 AND FFreezed!=1
   	#end
   	
   	
   	 #sql("findusersub")
	select * from(
 	SELECT e.FGUID,e.FName,e.FProductID,e.FPrice,e.FCycle,e.FEfectivetime,t.FAvailabilityTime,t.FExpiryTime FROM #(tablename) t inner JOIN  #(tablename1) e  ON t.FSubscriberPackageGUID=e.FGUID WHERE t.FSubsGUID='#(SubsGUID)'  AND  t.FDeleted!=1 AND t.FFreezed!=1 AND e.FDeleted!=1 AND e.FFreezed!=1 
 	union
 	SELECT e.FGUID,e.FName,e.FProductID,e.FPrice,e.FLifecycle AS FCycle,e.FEfectivetime,t.FAvailabilityTime,t.FExpiryTime FROM #(tablename) t inner JOIN  #(tablename2) e  ON t.FSubscriberPackageGUID=e.FGUID WHERE t.FSubsGUID='#(SubsGUID)'  AND  t.FDeleted!=1 AND t.FFreezed!=1 AND e.FDeleted!=1 AND e.FFreezed!=1 
	)as temp 
   	#end
  	 #sql("selectaccount")
	SELECT * FROM #(tablename) WHERE FUserGUID='#(FUserGUID)' 
   	#end
   	#sql("selectProductrecord")
   	select * from(
 	SELECT e.FName,e.FProductID,e.FPrice,t.FAvailabilityTime FROM #(tablename1) t inner JOIN   #(tablename2) e  ON t.FSubscriberPackageGUID=e.FGUID WHERE t.FSubsGUID='#(SubsGUID)'  
 	 union
	SELECT e.FName,e.FProductID,e.FPrice,t.FAvailabilityTime FROM #(tablename3) t inner JOIN   #(tablename4) e  ON t.FSubscriberPackageGUID=e.FGUID WHERE t.FSubsGUID='#(SubsGUID)'
	)as temp
   	#end
   	 #sql("TVODauthentication")
 	SELECT CASE FType WHEN '1' THEN 't_subscriberpackage'  when '2' THEN  't_strategy' END   
	AS tablename FROM #(tablename) WHERE FSubsGUID='#(FSubsGUID)' AND  
	FSubscriberPackageGUID='#(FSubscriberPackageGUID)' AND FDeleted!=1
   	#end
   	#sql("TVODauthenticationtime")
	SELECT FEfectivetime,FFreezed FROM #(tablename) WHERE FGUID='#(FGUID)'  AND FDeleted!=1
   	#end
   	#sql("VODauthentication")
 	 SELECT  count(*)  AS num ,FExpiryTime  FROM #(tablename) WHERE FSubscriberPackageGUID='#(FSubscriberPackageGUID)'  AND FSubsGUID='#(FUserGUID)' AND FFreezed!=1 AND FDeleted!=1 AND str_to_date(FExpiryTime,'%Y/%m/%d %H:%i:%s')>str_to_date('#(newTime)','%Y/%m/%d %H:%i:%s')
   	#end
   	#sql("TVODauthenticationss")
   	SELECT FGUID FROM #(tablename) WHERE 	FProductID='#(FColumnID)' or FProductID='#(FMediafundedID)' AND FDeleted!=1
   	union
   	SELECT FGUID FROM #(tablename1) WHERE 	FProductID='#(FColumnID)'  or FProductID='#(FFMediafundedID)' AND FDeleted!=1
   	
   	
   	#end
	 #sql("unfindusersub")
	SELECT FGUID,FCycle,FName,FPrice,FPrice,FProductID  
    FROM #(tablename) WHERE  
    FGUID not in (SELECT FSubscriberPackageGUID 
   FROM #(tablename1)  WHERE FSubsGUID='#(FSubsGUID)' AND FFreezed not in(1,2) AND FDeleted!=1  AND NOW()<str_to_date(FExpiryTime,'%Y/%m/%d %H:%i:%s'))  AND FFreezed not in (1,2) AND FDeleted!=1 AND NOW()<str_to_date(FEfectivetime,'%Y/%m/%d %H:%i:%s')
   	#end
    #sql("unfinduserstrategy")
	SELECT FGUID,FLifecycle,FName,FPrice,FProductID  
    FROM #(tablename) WHERE  
    FGUID not in (SELECT FSubscriberPackageGUID 
   FROM #(tablename1)  WHERE FSubsGUID='#(FSubsGUID)' AND FFreezed not in(1,2) AND FDeleted!=1  AND NOW()<str_to_date(FExpiryTime,'%Y/%m/%d %H:%i:%s'))  AND FFreezed not in(1,2) AND FDeleted!=1 AND FLifecycle!=0  AND NOW()<str_to_date(FEfectivetime,'%Y/%m/%d %H:%i:%s')
   	#end
   	#sql("bindTermUser_SubscriberPackages_findsub")
 	SELECT FPrice,FCycle,FEfectivetime,FAvailabilityTime FROM #(tablename) WHERE FGUID='#(FGUID)' AND  FFreezed!=1 AND FDeleted!=1
   	#end
   	#sql("bindTermUser_SubscriberPackages_findstrategy")
 	SELECT FPrice,FLifecycle,FEfectivetime,FAvailabilityTime FROM #(tablename) WHERE FGUID='#(FGUID)' AND  FFreezed!=1 AND FDeleted!=1
   	#end
   	 #sql("bindTermUser_SubscriberPackages_feededuction")
 	UPDATE #(tablename) SET FAccountbalance='#(FAccountbalance)',FUpdatetime='#(FUpdatetime)'  WHERE FUserGUID='#(FUserGUID)'
   	#end
   	
	 #sql("bindTermUser_SubscriberPackages_checkbalance")
 	SELECT FAccountbalance FROM #(tablename) WHERE  FUserGUID='#(FUserGUID)'
   	#end
   	 #sql("bindTermUser_SubscriberPackages_FFreezed")
 	SELECT FFreezed FROM #(tablename) WHERE FGUID='#(FGUID)'
   	#end
   	#sql("bindTermUser_SubscriberPackages_log")
	INSERT INTO (FORDER_TIME,FUSER_ID,FTERMINAL_ID,FPRODUCT_CODE,FPRICE,FVALID_TIME,FEXPIRE_TIME,FRDER_TYPE,FCRTETIME) 
	VALUES ('#(FORDER_TIME)','#(FUSER_ID)','#(FTERMINAL_ID)','#(FPRODUCT_CODE)','#(FPRICE)','#(FVALID_TIME)','#(FEXPIRE_TIME)','#(FRDER_TYPE)','#(FCRTETIME)')
   	#end
	 #sql("checkvalidstrategy")
 	SELECT FExpiryTime FROM #(tablename) WHERE FSubsGUID='#(FSubsGUID)' AND FSubscriberPackageGUID='#(FSubscriberPackageGUID)' AND FDeleted!=1
   	#end
   	 #sql("addaccount")
 	 INSERT INTO #(tablename) (FUserGUID,FCreatetime) VALUES ('#(FUserGUID)','#(FCreatetime)');
   	#end
    #sql("getByTermUserbidsub")
 	select * from (
   SELECT FGUID,FCycle,FName,FPrice,FProductID,FEfectivetime
    FROM #(tablename) WHERE  
    FGUID not in (SELECT FSubscriberPackageGUID 
   FROM #(tablename2)  WHERE FSubsGUID='#(SubsGUID)'  AND FFreezed not in(1,2) AND FDeleted!=1)  AND FFreezed not in(1,2) AND FDeleted!=1 
   union 
   SELECT FGUID,FLifecycle as FCycle,FName,FPrice,FProductID,FEfectivetime
    FROM #(tablename1) WHERE  
    FGUID not in (SELECT FSubscriberPackageGUID 
   FROM #(tablename2)  WHERE FSubsGUID='#(SubsGUID)'  AND FFreezed not in(1,2)  AND FDeleted!=1)  AND FFreezed not in(1,2) AND FDeleted!=1 AND FLifecycle!=0 
	) as temp  
   	#end
   	
   	#sql("GetBossterminone")
   	SELECT FGUID,FType,FName,FIdCard,FEmail,FLevel,FIntergration,FCreateTime,
   	FCreateUserID,FUpdateUserID,FFreezed,FMac,FDisCount FLocationcode,FSmartcardtype,Fimportboss 
   	FROM #(tablename)  WHERE FGUID='#(FGUID)' AND FDeleted!=1
   	#end
   	#sql("GetBosstermin")
   	SELECT * FROM #(tablename) WHERE FGUID in(SELECT FSubscriberPackageGUID  FROM #(tablename1) WHERE 
	FSubsGUID='#(FSubsGUID)' AND FDeleted!=1 ) AND FDeleted!=1
   	#end
   	
   	#sql("GetBosstermintwo")
   	SELECT * FROM #(tablename) WHERE FGUID='#(FGUID)' AND FDeleted!=1
   	#end
   	
   	
   	
   	
   	
  
   	
   	
   	
   	
   	
   	
   	
   	
   	
   	
   #end