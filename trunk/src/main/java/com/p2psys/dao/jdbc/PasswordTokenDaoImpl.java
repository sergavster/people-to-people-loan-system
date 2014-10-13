package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.PasswordTokenDao;
import com.p2psys.domain.PasswordToken;
import com.p2psys.model.SearchParam;

public class PasswordTokenDaoImpl extends BaseDaoImpl implements PasswordTokenDao {
	
	private static Logger logger = Logger.getLogger(PasswordTokenDaoImpl.class);  

	@Override
	public List<PasswordToken> getAllList() {
		String sql = "select * from dw_password_token order by user_id desc";
		List<PasswordToken> list = new ArrayList<PasswordToken>();
//		return this.getJdbcTemplate().query(sql, new PasswordTokenMapper());
		return this.getJdbcTemplate().query(sql, getBeanMapper(PasswordToken.class));
	}

	@Override
	public void addPasswordToken(List<PasswordToken> tokenList, long userId) {
		int listSize = tokenList.size();
		PasswordToken passwordToken = null;
		if (listSize>0) {
			for (int i = 0; i < listSize; i++) {
				passwordToken = tokenList.get(i);
				int result = 0;
				String sql = "insert into dw_password_token(user_id,question,answer) values (?,?,?)";
				logger.debug("Sql:" + sql);
				result = this.getJdbcTemplate().update(sql, new Object[] { userId, passwordToken.getQuestion(), passwordToken.getAnswer()});
				logger.debug("result:" + result);
			}
		}
	}

	@Override
	public void deletePasswordTokenById(long id) {
		String sql = "delete from dw_password_token where id=?";
		getJdbcTemplate().update(sql, new Object[] { id });
	}

	@Override
	public void updatePasswordTokenByList(List<PasswordToken> list) {
		String sql = "update dw_password_token set user_id=?,question=?,answer=? where id=?";
		for (PasswordToken pt : list) {
			if (pt == null)
				continue;
			getJdbcTemplate().update(
					sql, new Object[] { pt.getUser_id(), pt.getQuestion(), 
							pt.getAnswer(), pt.getId()});
		} 
	}

	@Override
	public List<PasswordToken> getPasswordTokenByUserId(long userId) {
		String sql = "select * from dw_password_token where user_id = ?";
		logger.info("SQL:"+sql);
		logger.info("SQL:"+userId);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new PasswordTokenMapper(), userId);
		list=this.getJdbcTemplate().query(sql, getBeanMapper(PasswordToken.class), userId);
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PasswordToken> getPasswordTokenByUsername(String username) {
		String sql = "select p.* from dw_password_token as p " +
				"left join dw_user as u on p.user_id = u.user_id where u.username = :username";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(PasswordToken.class));
	}

	@Override
	public String getAnswerByQuestion(String question, long userId) {
		String sql = "select answer from dw_password_token where user_id = ?";
		logger.info("SQL:"+sql);
		logger.info("question:"+question + "userId:" + userId);
		return getJdbcTemplate().queryForObject(sql, new Object[] { question, userId }, String.class);
	}
	
	

//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc start
	@Override
	public int getPasswordTokenCount(SearchParam param) {
		String queryCountSql = "from dw_password_token p1 left join dw_user p2 on p2.user_id = p1.user_id where 1 = 1";
		StringBuffer sb= new StringBuffer("select count(p1.id) ");
		sb.append(queryCountSql).append(param.getSearchParamSql());
		String sql=sb.toString();
		logger.info("getPassworkTokenCount():"+sql);
		int total =count(sql);
		return total;
	}
	
	@Override
	public List getPasswordTokenList(int page, int max,SearchParam param) {
		String queryCountSql = "from dw_password_token p1 left join dw_user p2 on p2.user_id = p1.user_id where 1 = 1";
		
		StringBuffer sb = new StringBuffer("SELECT p1.id,p1.user_id,p1.question,p1.answer,p2.username ");		
		String orderSql=" order by p1.id desc ";
		String limitSql=" limit ?,?";
		sb.append(queryCountSql).append(param.getSearchParamSql()).append(orderSql).append(limitSql);
		String sql=sb.toString();
		Object[] o = new Object[] { page , max };
		
		logger.info("getPasswordTokenList():"+sql + ",username" + param.getUsername());
		List list= getJdbcTemplate().query(sql,o,new RowMapper<PasswordToken>() {
			public PasswordToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				PasswordToken passwordToken = new PasswordToken();
				passwordToken.setId(rs.getLong("id"));
				passwordToken.setUser_id(rs.getLong("user_id"));
				passwordToken.setQuestion(rs.getString("question"));
				passwordToken.setAnswer(rs.getString("answer"));
				passwordToken.setUsername(rs.getString("username"));				
				return passwordToken;
			}
		});
		return list;
	}
	//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc end

	// v1.6.7.2 RDPROJECT-505 zza 2013-12-05 start
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PasswordToken getPasswordTokenById(long id) {
		String sql = "select * from dw_password_token where id = :id";
		logger.info("SQL:"+sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(PasswordToken.class));
	}
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-05 end

}
