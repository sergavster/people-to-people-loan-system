package com.p2psys.black.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.p2psys.black.dao.LoanBlackDao;
import com.p2psys.black.domain.LoanBlack;
import com.p2psys.dao.jdbc.AccountBackDaoImpl;
import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.model.AccountBackModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.StringUtils;

@Repository("loanBlackDao")
public class LoanBlackDaoImpl extends BaseDaoImpl implements LoanBlackDao {
	private Logger logger =Logger.getLogger(LoanBlackDaoImpl.class);
	@Override
	public void saveLoanBlack(LoanBlack lb){
		this.insert(lb);
	}

	@Override
	public int getCount(SearchParam param) {
		StringBuffer sql = new StringBuffer("select count(1) from dw_loan_black p1 where p1.status=0 ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p1.username like concat('%',:username,'%')"); 
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.addtime >= :dotime1");
				map.put("dotime1", param.getSearchTime(param.getDotime1(),0));
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.addtime <= :dotime2");
				map.put("dotime2", param.getSearchTime(param.getDotime2(),1));
			}
		}
		logger.debug("loanBlackDao.getCount"+sql.toString());
		return getNamedParameterJdbcTemplate().queryForInt(sql.toString(),map);
	}

	@Override
	public List<LoanBlack> getLoanBlackList(SearchParam param, int start,
			int pernum) {
		StringBuffer sql = new StringBuffer("select * from dw_loan_black  p1  where p1.status=0 ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p1.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.addtime >= :dotime1");
				map.put("dotime1", param.getSearchTime(param.getDotime1(),0));
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.addtime <= :dotime2");
				map.put("dotime2", param.getSearchTime(param.getDotime2(),1));
			}
		}
		sql.append(" order by p1.id desc ");
		if (pernum > 0) {// 分页查询，若为0，则取所有（导出报表时取所有）
			sql.append(" LIMIT :start,:pernum");
			map.put("start", start);
			map.put("pernum", pernum);
		}
		logger.debug("loanBlackDao.getLoanBlackList"+sql.toString());
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(LoanBlack.class));
	}

	@Override
	public List<LoanBlack> getLoanBlack(String username, String identity,
			String mobile, String email, String qq) {
		StringBuffer sql = new StringBuffer("select * from dw_loan_black  p1  where p1.status=0 ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isBlank(username)) {
			sql.append(" and p1.username=:username");
			map.put("username", username);
		}
		if (!StringUtils.isBlank(identity)) {
			sql.append(" and p1.identity=:identity");
			map.put("identity", identity);
		}
		if (!StringUtils.isBlank(mobile)) {
			sql.append(" and p1.mobile=:mobile");
			map.put("mobile", mobile);
		}if (!StringUtils.isBlank(email)) {
			sql.append(" and p1.email=:email");
			map.put("email", email);
		}
		if (!StringUtils.isBlank(qq)) {
			sql.append(" and p1.qq=:qq");
			map.put("qq", qq);
		}
		logger.debug("loanBlackDao.getLoanBlack"+sql.toString());
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(LoanBlack.class));
	}

	@Override
	public LoanBlack getLoanBlackByFkId(long user_id) {
		StringBuffer sb=new StringBuffer();
        sb=new StringBuffer("select * from dw_loan_black  p1  where p1.status=0 and p1.fk_id=:fk_id limit 0,1 ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fk_id", user_id);
        String sql=sb.toString();
        logger.debug("loanBlackDao.getLoanBlackByFkId(user_id):"+sql.toString());
        return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(LoanBlack.class));
    }
    @Override
    public void updateLoanBlack(LoanBlack lb) {
    	String sql="update dw_loan_black set username=:username where id=:id";
    	logger.debug("BackDao.updateBack(b):"+sql.toString());
    	SqlParameterSource ps = new BeanPropertySqlParameterSource(lb);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
    }
	
}
