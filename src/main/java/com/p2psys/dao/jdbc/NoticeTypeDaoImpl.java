package com.p2psys.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.NoticeTypeDao;
import com.p2psys.domain.NoticeType;
import com.p2psys.model.SearchParam;

public class NoticeTypeDaoImpl extends BaseDaoImpl implements NoticeTypeDao {
	private static Logger logger = Logger.getLogger(NoticeTypeDaoImpl.class);
	
	@Override
	public List<NoticeType> getList(int start, int pernum, SearchParam param) {
		String sql = "select * from dw_notice_type where 1=1 ";
		sql += param.getSearchParamSql();
		sql += " order by nid asc, notice_type asc limit :start,:pernum";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("pernum", pernum);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(NoticeType.class));
	}	

	@Override
	public int getListCount(SearchParam param) {
		String sql = "select count(1) from dw_notice_type where 1=1 ";
		sql += param.getSearchParamSql();
		try {
			return namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void update(NoticeType noticeType) {
		String sql = "update dw_notice_type set name=:name "
				//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
//				+ ", type=:type, send=:send, title_templet=:title_templet, templet=:templet, remark=:remark "
				+ ", type=:type, send=:send, send_route=:send_route, title_templet=:title_templet, templet=:templet, remark=:remark "
				//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
				+ ", updatetime=:updatetime, updateip=:updateip where nid=:nid and notice_type = :notice_type";
		logger.debug("SQL:" + sql);
		SqlParameterSource ps = new BeanPropertySqlParameterSource(noticeType);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

	@Override
	public List<NoticeType> getAllSendNoticeType() {
		//系统通知短信用户也可以配置为接收或者不接收，所以传回前台的配置类型不能只是用户类型短信了，要全部发送短信类型，但验证类的短信比较特殊，不能配置，所以只有可canswitch为1才能配置
		String sql = "select * from dw_notice_type where send = 1 and canswitch = 1 ";
		Map<String, Object> map = new HashMap<String, Object>();
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(NoticeType.class));
	}

	@Override
	public NoticeType getNoticeTypeByNid(String nid, byte notice_type) {
		String sql = "select * from dw_notice_type where nid = :nid and notice_type = :notice_type ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nid", nid);
		map.put("notice_type", notice_type);
		try {
			
			return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(NoticeType.class));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<NoticeType> getList() {
		String sql = "select * from dw_notice_type order by nid asc, notice_type asc ";
		Map<String, Object> map = new HashMap<String, Object>();
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(NoticeType.class));
	}

	//v1.6.7.2  RDPROJECT-535 liukun 2013-12-09 start
	/*@Override
	public List<NoticeType> getList(String nid) {
		String sql = "select * from dw_notice_type where nid = :nid order by notice_type asc ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nid", nid);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(NoticeType.class));
	}*/
	//v1.6.7.2  RDPROJECT-535 liukun 2013-12-09 end



}
