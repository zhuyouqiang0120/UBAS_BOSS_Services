package com.zens.ubasbossservices.serviceimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.caucho.hessian.server.HessianServlet;
import com.chasonx.tools.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;
import com.zens.ubasbossservices.config.Dconfig;
import com.zens.ubasbossservices.entity.T_TermInfo;
import com.zens.ubasbossservices.entity.T_TermUser;
import com.zens.ubasbossservices.entity.T_TermUserTermInfo;
import com.zens.ubasbossservices.service.TerminfomplService;
import com.zens.ubasbossservices.utils.RecordUtil;
import com.zens.ubasbossservices.utils.SQLUtil;
import com.zens.ubasbossservices.utils.TimeUT;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Terminfompl extends HessianServlet implements TerminfomplService {
	String terminfotableName = TableMapping.me().getTable(T_TermInfo.class).getName();
	/*String terminfoSQL = "SELECT id,FGUID AS GUID,FMac AS mac,FIp AS lp,FSerial AS serial,FCaCard AS cacard,(SELECT t.FFreezed  FROM "+T_TermInfo.tableName+  "t INNER JOIN"+T_TermUserTermInfo.tableName +"o ON t.FGUID=o.FSubsGUID WHERE t.FDeleted!=1 AND o.FDeleted!=1) AS  freezed ,FType AS type,FRegionGUID AS regionGUID,FCellGUID AS cellGUID,FPrisonGUID AS prisonGUID,FRegionname AS regionname,FCellname AS cellname,FPrisonname AS prisonname FROM  "
			+ terminfotableName;*/
	String terminfoSQL = "SELECT f.FFreezed AS  freezed,f.id,f.FGUID AS GUID,f.FMac AS mac,f.FIp AS lp,"
			+" f.FSerial AS serial,f.FCaCard AS cacard,f.FType AS type,f.FRegionGUID AS regionGUID,f.FCellGUID AS cellGUID,f.FPrisonGUID AS prisonGUID,f.FRegionname"
			+" AS regionname,f.FCellname AS cellname,f.FPrisonname AS prisonname"
			+" FROM "+ T_TermUser.tableName+ " t INNER JOIN "+T_TermUserTermInfo.tableName+ " o ON t.FGUID=o.FSubsGUID INNER JOIN  "+T_TermInfo.tableName +" f ON  o.FTermInfoID=f.FGUID "  ;
	String countterminfoSQL="SELECT COUNT(f.ID)"
			+" FROM "+ T_TermUser.tableName+ " t INNER JOIN "+T_TermUserTermInfo.tableName+ " o ON t.FGUID=o.FSubsGUID INNER JOIN  "+T_TermInfo.tableName +" f ON  o.FTermInfoID=f.FGUID " ;
	private static final long serialVersionUID = 1L;

	/**
	 * 分页获取设备信息
	 * 
	 * @author ZKill
	 * @create 2017年5月24日 12:51:41
	 * @update
	 * @param
	 * @return void
	 */
	@Override
	public Record getterminfo(String param) throws Exception {
		Record record = new Record();
		JSONObject paramData = JSONObject.fromObject(param);
		List<Record> termminfo = null;

		Integer pageSize = paramData.has("pageSize") ? Integer.parseInt((String) paramData.get("pageSize")) : null;
		Integer page = paramData.has("page") ? Integer.parseInt((String) paramData.get("page")) : null;
		String keyword = (String) paramData.get("keyWord");
		String deleted = (String) paramData.get("deleted");
		String freezed = (String) paramData.get("freezed");
		if (!StringUtils.hasText(deleted)) {
			deleted = "0";
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("FMac", mac);
		paramMap.put("FType", "ex");
		paramMap.put("FFreezed", freezed);
		String whereSQL = SQLUtil.getterminoWhereSql(paramMap);
		;
		whereSQL += "AND t.FDeleted!=1 AND o.FDeleted!=1 AND f.FDeleted!=1";
		if (StringUtils.hasText(keyword)) {
			String keywordSQL = getSQLByKeyword(keyword);
			whereSQL += keywordSQL;
		}
		String limitSQL = SQLUtil.getLimitSQL(pageSize, page);
		String sql = terminfoSQL + whereSQL + " ORDER BY f.ID DESC " + limitSQL;
		termminfo = Db.use(Dconfig.dPlugin).find(sql);
		record.set("ZExamMSterm", termminfo);
		// 获取前端分页所需参数
		getParamForLimitPages(record, whereSQL, pageSize);

		return record;

	}

	public void getParamForLimitPages(Record record, String whereSQL, Integer pageSize) throws Exception {

		Number number = counts(whereSQL);
		int pages = number.intValue() / pageSize;
		int res = number.intValue() % pageSize;
		int total = number.intValue();
		int totalPages = res == 0 ? pages : pages + 1; // totalPages是若返回所有（未删除的）的记录应该显示的页数。
		record.set("total", total); // 能够返回的（即未删除的）记录数。
		record.set("totalPages", totalPages); // totalPages是若返回所有记录应该显示的页数。
	}

	public Number counts(String whereSQL) throws Exception {
		String sql = countterminfoSQL  + whereSQL;
		return Db.use(Dconfig.dPlugin).queryNumber(sql);
	}

	/**
	 * 新增设备信息
	 * 
	 * @author ZKill
	 * @create 2017年5月24日 20:11:14
	 * @update
	 * @param
	 * @return boolean
	 */
	@Override
	public boolean addterminfo(String param) throws Exception {
		UUID uuid = UUID.randomUUID();
		JSONObject paramData = JSONObject.fromObject(param);
		Record record = new Record();
		List<String> paramNames = new ArrayList<String>();
		paramNames.add("mac");
		paramNames.add("ip");
		paramNames.add("serial");
		paramNames.add("cacard");
		paramNames.add("regionGUID");
		paramNames.add("cellGUID");
		paramNames.add("prisonGUID");
		paramNames.add("regionname");
		paramNames.add("cellname");
		paramNames.add("prisonname");
		paramNames.add("ext");
		RecordUtil.setRecord(paramNames, paramData, record);
		record.set("FGUID", uuid.toString()).set("FCreateTime", TimeUT.getCurrTime()).set("FType", "ex");
		return Db.use(Dconfig.dPlugin).save(terminfotableName, record);
	}

	/**
	 * 批量更新编辑设备信息
	 * 
	 * @author ZKill
	 * @create 2017年5月24日 20:13:42
	 * @update
	 * @param
	 * @return boolean
	 */

	@Override
	public boolean updateminfo(String param) throws Exception {
		final JSONArray paramData = JSONArray.fromObject(param);

		// 事务回滚
		boolean success = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				boolean result = true;
				try {
					for (int i = 0; i < paramData.size(); i++) {

						String updateSQL = "UPDATE " + terminfotableName + " SET ";
						JSONObject jO = (JSONObject) paramData.get(i);
						String GUID = (String) jO.get("GUID");
						String setSQL = getSetSQLForTermUser_Group(jO);
						updateSQL += setSQL;
						updateSQL += "WHERE FGUID = '" + GUID + "' ";

						result = updateTermUser(updateSQL) && result;

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = false;
				}

				return result;
			}
		});
		System.out.println(success);
		return success;

	}

	public boolean updateTermUser(String sql) throws Exception {
		return Db.use(Dconfig.dPlugin).update(sql) > 0;
	}

	public String getSetSQLForTermUser_Group(JSONObject jO) throws Exception {
		StringBuffer setSQL = new StringBuffer();
		String result;
		List<String> setNames = new ArrayList<String>();
		setNames.add("type");
		setNames.add("mac");
		setNames.add("ip");
		setNames.add("serial");
		setNames.add("cacard");
		setNames.add("regionGUID");
		setNames.add("cellGUID");
		setNames.add("prisonGUID");
		setNames.add("regionname");
		setNames.add("cellname");
		setNames.add("prisonname");
		setNames.add("freezed");
		setNames.add("deleted");
		setNames.add("ext");
		SQLUtil.addParamsToSetSQL(setSQL, jO, setNames);
		setSQL.append(" FUpdateTime = '");
		setSQL.append(TimeUT.getCurrTime());
		setSQL.append("' ");
		result = setSQL.toString();
		return result;
	}

	/**
	 * 根据keyword拼出（用于筛选的）SQL片段。
	 * 
	 * @author ZKill
	 * @create 2017年5月25日 14:34:06
	 * @update
	 * @param
	 * @return String
	 */
	public String getSQLByKeyword(String keyword) throws Exception {
		// FGUID AS GUID,FMac AS mac,FLp AS lp,FSerial AS serial,FCaCard AS
		// cacard,FRegionID AS region,FFreezed AS freezed
		StringBuffer sql = new StringBuffer(" AND (f.FGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FMac LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FRegionname LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FCellname LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FPrisonname LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FSerial LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FCaCard LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FRegionGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FCellGUID LIKE'%");
		sql.append(keyword);
		sql.append("%' OR f.FPrisonGUID LIKE'%");
		sql.append(keyword);
		sql.append("%')");

		return sql.toString();
	}

	/**
	 * 根据区域获取设备信息
	 * 
	 * @author ZKill
	 * @create 2017年5月25日 14:34:06
	 * @update
	 * @param
	 * @return String
	 */
	@Override
	public List<Record> getRegionterminfo(String param) throws Exception {
		JSONObject paramData = JSONObject.fromObject(param);
		String RegionID = (String) paramData.get("RegionID");
		// String sql = "SELECT *,GROUP_CONCAT(FRegionID SEPARATOR '-') AS
		// region FROM t_terminfo WHERE FIND_IN_SET(?,FRegionID)";
		String sql = "SELECT * FROM t_terminfo WHERE FRegionGUID=? or FCellGUID=? or FPrisonGUID=? AND FFreezed!=1 AND FDeleted!=1";
		List<Record> record = Db.use(Dconfig.dPlugin).find(sql, RegionID, RegionID, RegionID);
		return record;
	}

}
