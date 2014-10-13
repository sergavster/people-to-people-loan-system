package com.p2psys.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.UpfilesDao;
import com.p2psys.domain.Upfiles;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UpfilesModel;

/**
 * 批量充值上传excel接口实现类
 * 
 
 * @version 1.0
 * @since 2013-8-20
 */
public class UpfilesDaoImpl extends BaseDaoImpl implements UpfilesDao {
	
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	@Override
	public void addUpfiles(Upfiles upfiles) {
		String sql="insert into dw_upfiles(file_name, file_path, file_remark, user_id, addtime, " +
				"verify_user, verify_user_id, verify_time, status, type) "+
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, upfiles.getFile_name(), upfiles.getFile_path(), upfiles.getFile_remark(), 
				upfiles.getUser_id(), upfiles.getAddtime(), upfiles.getVerify_user(), upfiles.getVerify_user_id(), 
				upfiles.getVerify_time(), upfiles.getStatus(), upfiles.getType());
	}

	@Override
	public void modifyUpfiles(Upfiles upfiles) {
		String sql = "update dw_upfiles set file_name=?, file_path=?, file_remark=?, " +
				"verify_user=?, verify_user_id=?, verify_time=?, status=? where id=?";
		this.getJdbcTemplate().update(sql, upfiles.getFile_name(), upfiles.getFile_path(), upfiles.getFile_remark(), 
				upfiles.getVerify_user(), upfiles.getVerify_user_id(), upfiles.getVerify_time(), upfiles.getStatus(), upfiles.getId());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int upfilesTotal(SearchParam param, String type) {
		String selSql = "SELECT COUNT(1) FROM dw_upfiles AS p1, dw_user AS p2 WHERE p2.user_id = p1.user_id AND p1.type = :type";
		StringBuffer sb = new StringBuffer();
		String searchSql = param.getSearchParamSql();
		sb.append(selSql).append(searchSql);
		String sql = sb.toString();
		logger.debug("批量上传excel分页SQL： " + sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		int total = 0;
		try {
			total = this.getNamedParameterJdbcTemplate().queryForInt(sql, map);
		} catch (Exception e) {
			logger.debug("upfilesTotal():" + e.getMessage());
		}
		return total;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<UpfilesModel> getAllUpfiles(int start, int pernum, SearchParam param, String type) {
		String selSql = "SELECT p1.*, p2.username AS username FROM dw_upfiles AS p1, dw_user AS p2 " + 
				"WHERE p2.user_id = p1.user_id AND p1.type = :type";
		StringBuffer sb = new StringBuffer();
		String searchSql = param.getSearchParamSql();
		String limitSql = " ORDER BY p1.addtime DESC limit :start, :pernum ";
		sb.append(selSql).append(searchSql).append(limitSql);
		String sql = sb.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("start", start);
		map.put("pernum", pernum);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(UpfilesModel.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Upfiles getUpfilesById(long id) {
		String sql = "SELECT p1.*, p2.username AS username FROM dw_upfiles AS p1, dw_user AS p2 WHERE p2.user_id = p1.user_id AND p1.id = ?";
		logger.debug("SQL: " + sql);
		Upfiles upfiles = null;
		try {
			upfiles = this.getJdbcTemplate().queryForObject(sql, new Object[]{ id }, getBeanMapper(UpfilesModel.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return upfiles;
	}

	@Override
	public void delUpfiles(long id) {
		String sql = "delete from dw_upfiles where id=?";
		getJdbcTemplate().update(sql, id);
	}

}
