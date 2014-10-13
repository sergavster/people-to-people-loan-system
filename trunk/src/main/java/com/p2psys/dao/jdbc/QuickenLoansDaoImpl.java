package com.p2psys.dao.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.QuickenLoansDao;
import com.p2psys.domain.QuickenLoans;
import com.p2psys.model.SearchParam;

/**
 * 贷款快速通道相关的Dao操作类
 
 * 
 */
public class QuickenLoansDaoImpl extends BaseDaoImpl implements QuickenLoansDao {

	private static Logger logger = Logger.getLogger(QuickenLoansDaoImpl.class);
	
	/**
	 * 查找所有的贷款快速通道信息
	 */
	public List getList(int page, int max, SearchParam p) {
		String sql = "select * from dw_quicken_loans";
		sql += p.getSearchParamSql();
		sql += "  LIMIT ?,?";
		List list = getJdbcTemplate().query(sql, new Object[] { page , max },getBeanMapper(QuickenLoans.class));
		return list;
	}
	
	public int getSearchCard(SearchParam param) {
		String selectSql = "select count(p2.loans_id) from dw_quicken_loans p2 where 1=1 ";
		String searchSql = param.getSearchParamSql();
		// v1.6.7.2 RDPROJECT-554 zza start
//		searchSql = searchSql.replace("p1", "p2");
		// v1.6.7.2 RDPROJECT-554 zza end
		StringBuffer sb = new StringBuffer(selectSql);
		String querySql = sb.append(searchSql).toString();
		logger.debug("getSearchCard():" + querySql);
		int total = 0;
		total = count(querySql);
		return total;
		
	}
	
	/**
	 * 添加贷款快速通道信息
	 */
	public void addQuickenLoans(QuickenLoans quickenLoans) {
		String sql = "insert into dw_quicken_loans(name,phone,area,remark,create_time) values (?,?,?,?,?)";
		getJdbcTemplate().update(
				sql, new Object[] { quickenLoans.getName(), quickenLoans.getPhone(), quickenLoans.getArea(),
						quickenLoans.getRemark(), quickenLoans.getCreateTime() });
	}
	
	/**
	 * 根据ID取得对应的贷款快速通道信息
	 * @param loansId
	 * @return
	 */
	public QuickenLoans getLoansById(int loansId) {
		String sql = "select * from dw_quicken_loans where loans_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + loansId);
		QuickenLoans quickenLoans = null;
		try {
			quickenLoans = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { loansId }, getBeanMapper(QuickenLoans.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return quickenLoans;
	}

	/**
	 * 删除
	 */
	public void delQuickenLoans(int loansId) {
		String sql="delete from dw_quicken_Loans where loans_id=?";
		getJdbcTemplate().update(sql,loansId);
		
	}
		
}
