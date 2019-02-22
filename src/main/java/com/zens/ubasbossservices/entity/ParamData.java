/**********************************************************************
 **********************************************************************
 **    Project Name : UBAS-SERVICE
 **    Package Name : com.zens.ubasservice.entity								 
 **    Type    Name : TUser 							     	
 **    Create  Time : 2016年9月19日 下午2:19:54								
 ** 																
 **    (C) Copyright Zensvision Information Technology Co., Ltd.	 
 **            Corporation 2016 All Rights Reserved.				
 **********************************************************************
 **	     注意： 本内容仅限于上海仁视信息科技有限公司内部使用，禁止转发		 **
 **********************************************************************
 */
package com.zens.ubasbossservices.entity;

import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * @author zyq
 * @e-mail zhuyq@zensvision.com
 * @date 2016年10月20日 上午12:29:53
 */
public class ParamData extends Model<ParamData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String stbIP;
	String stbMAC;
	String stbCA;
	String Serial;
	String regionID;

	// T_TERMUSER表中的字段名
	String ID;
	String FGUID;
	String FType;
	String FBossID;
	String FBossArea;
	String FBossCity;
	String FName;
	String FIdCard;
	String FSex;
	String FAge;
	String FBirthday;
	String FAddr;
	String FIphone;
	String FMobile;
	String FEmail;
	String FLevel;
	String FIntergration;
	String FCreateTime;
	String FCreateUserID;
	String FUpdateTime;
	String FUpdateUserID;
	String FFreezed;
	String FDeleted;
	String FExt;
	
	// M_TERM_REGION表中特有的字段名
	String FSubsGUID;
	String FRegionGUID;
	String FIsDefault;
	String FIsCharge;
	

	/**
	 * @return the fSubsGUID
	 */
	public String getFSubsGUID() {
		return FSubsGUID;
	}

	/**
	 * @param fSubsGUID the fSubsGUID to set
	 */
	public void setFSubsGUID(String fSubsGUID) {
		FSubsGUID = fSubsGUID;
	}
	
	/**
	 * @return the fRegionGUID
	 */
	public String getFRegionGUID() {
		return FRegionGUID;
	}

	/**
	 * @param fRegionGUID the fRegionGUID to set
	 */
	public void setFRegionGUID(String fRegionGUID) {
		FRegionGUID = fRegionGUID;
	}

	/**
	 * @return the fIsDefault
	 */
	public String getFIsDefault() {
		return FIsDefault;
	}

	/**
	 * @param fIsDefault the fIsDefault to set
	 */
	public void setFIsDefault(String fIsDefault) {
		FIsDefault = fIsDefault;
	}

	/**
	 * @return the fIsCharge
	 */
	public String getFIsCharge() {
		return FIsCharge;
	}

	/**
	 * @param fIsCharge the fIsCharge to set
	 */
	public void setFIsCharge(String fIsCharge) {
		FIsCharge = fIsCharge;
	}

	public String getStbIP() {
		return stbIP;
	}

	public void setStbIP(String stbIP) {
		this.stbIP = stbIP;
	}

	public String getStbMAC() {
		return stbMAC;
	}

	public void setStbMAC(String stbMAC) {
		this.stbMAC = stbMAC;
	}

	public String getStbCA() {
		return stbCA;
	}

	public void setStbCA(String stbCA) {
		this.stbCA = stbCA;
	}

	public String getSerial() {
		return Serial;
	}

	public void setSerial(String serial) {
		Serial = serial;
	}

	public String getRegionID() {
		return regionID;
	}

	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getFGUID() {
		return FGUID;
	}

	public void setFGUID(String fGUID) {
		FGUID = fGUID;
	}

	public String getFType() {
		return FType;
	}

	public void setFType(String fType) {
		FType = fType;
	}

	public String getFBossID() {
		return FBossID;
	}

	public void setFBossID(String fBossID) {
		FBossID = fBossID;
	}

	public String getFBossArea() {
		return FBossArea;
	}

	public void setFBossArea(String fBossArea) {
		FBossArea = fBossArea;
	}


	public String getFBossCity() {
		return FBossCity;
	}

	public void setFBossCity(String fBossCity) {
		FBossCity = fBossCity;
	}

	public String getFName() {
		return FName;
	}

	public void setFName(String fName) {
		FName = fName;
	}

	public String getFIdCard() {
		return FIdCard;
	}

	public void setFIdCard(String fIdCard) {
		FIdCard = fIdCard;
	}

	public String getFSex() {
		return FSex;
	}

	public void setFSex(String fSex) {
		FSex = fSex;
	}

	public String getFAge() {
		return FAge;
	}

	public void setFAge(String fAge) {
		FAge = fAge;
	}

	public String getFBirthday() {
		return FBirthday;
	}

	public void setFBirthday(String fBirthday) {
		FBirthday = fBirthday;
	}

	public String getFAddr() {
		return FAddr;
	}

	public void setFAddr(String fAddr) {
		FAddr = fAddr;
	}

	public String getFIphone() {
		return FIphone;
	}

	public void setFIphone(String fIphone) {
		FIphone = fIphone;
	}

	public String getFMobile() {
		return FMobile;
	}

	public void setFMobile(String fMobile) {
		FMobile = fMobile;
	}

	public String getFEmail() {
		return FEmail;
	}

	public void setFEmail(String fEmail) {
		FEmail = fEmail;
	}

	public String getFLevel() {
		return FLevel;
	}

	public void setFLevel(String fLevel) {
		FLevel = fLevel;
	}

	public String getFIntergration() {
		return FIntergration;
	}

	public void setFIntergration(String fIntergration) {
		FIntergration = fIntergration;
	}

	public String getFCreateTime() {
		return FCreateTime;
	}

	public void setFCreateTime(String fCreateTime) {
		FCreateTime = fCreateTime;
	}

	public String getFCreateUserID() {
		return FCreateUserID;
	}

	public void setFCreateUserID(String fCreateUserID) {
		FCreateUserID = fCreateUserID;
	}

	public String getFUpdateTime() {
		return FUpdateTime;
	}

	public void setFUpdateTime(String fUpdateTime) {
		FUpdateTime = fUpdateTime;
	}

	public String getFUpdateUserID() {
		return FUpdateUserID;
	}

	public void setFUpdateUserID(String fUpdateUserID) {
		FUpdateUserID = fUpdateUserID;
	}

	public String getFFreezed() {
		return FFreezed;
	}

	public void setFFreezed(String fFreezed) {
		FFreezed = fFreezed;
	}

	public String getFDeleted() {
		return FDeleted;
	}

	public void setFDeleted(String fDeleted) {
		FDeleted = fDeleted;
	}

	public String getFExt() {
		return FExt;
	}

	public void setFExt(String fExt) {
		FExt = fExt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
