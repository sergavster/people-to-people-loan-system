package com.p2psys.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.p2psys.context.Global;
import com.p2psys.dao.CashDao;
import com.p2psys.domain.AccountCash;
import com.p2psys.model.SearchParam;
import com.p2psys.util.StringUtils;

public class CashDaoImpl extends BaseDaoImpl implements CashDao {

	private static Logger logger = Logger.getLogger(CashDaoImpl.class);  
	
	@Override
	public List getAccountCashList(long user_id) {
		String sql="select p1.*,p4.name as bankname,p2.username,p2.realname,admin.username as verify_username " +
				"from dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"left join dw_user as admin on admin.user_id=p1.verify_userid " +
				"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
				"where 1=1 and p1.user_id=?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		List list=new ArrayList();
//		list=getJdbcTemplate().query(sql, new Object[]{user_id},new AccountCashMapper());
		list=getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(AccountCash.class));
		return list;
	}

	@Override
	public int getAccountCashCount(long user_id,SearchParam param) {
		String sql="select count(p1.id) from  dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"where 1=1 and p1.user_id=?";
		sql=sql+param.getSearchParamSql();
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		int count=0;
		count=this.count(sql, new Object[]{user_id});
		return count;
	}

	@Override
	public List getAccountCashList(long user_id, int start,int end,SearchParam param) {
		String sql="select p1.*,p4.name as bankname,p2.username,p2.realname,admin.username as verify_username " +
				"from dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"left join dw_user as admin on admin.user_id=p1.verify_userid " +
				"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
				"where 1=1 and p1.user_id=? ";
		String orderSql=" order by p1.addtime desc ";
		String limitSql="limit ?,?";
		String searchSql=param.getSearchParamSql();
		sql=sql+searchSql+orderSql+limitSql;
		logger.debug("getAccountCashList():"+sql);
		List list=new ArrayList();
//		list=getJdbcTemplate().query(sql, new Object[]{user_id,start,end},new AccountCashMapper());
		list=getJdbcTemplate().query(sql, new Object[]{user_id,start,end}, getBeanMapper(AccountCash.class));
		return list;
	}

	@Override
	public AccountCash addCash(final AccountCash cash) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 start
		final String sql="insert into dw_account_cash(user_id,account,bank,branch,total,credited,fee,old_fee,addtime,addip,hongbao) " +
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 end
		logger.info("SQL:"+sql);
		this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, cash.getUser_id());
				ps.setString(2, cash.getAccount());
				ps.setString(3, cash.getBank());
				ps.setString(4, cash.getBranch());
				ps.setString(5, cash.getTotal());
				ps.setString(6, cash.getCredited());
				ps.setString(7, cash.getFee());
				// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 start
				ps.setString(8, cash.getOld_fee());
				ps.setString(9, cash.getAddtime());
				ps.setString(10, cash.getAddip());
				ps.setDouble(11, cash.getHongbao());
				// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 end
				return ps;
			}
		}, keyHolder);
		long key=keyHolder.getKey().longValue();
		cash.setId(key);
		return cash;
	}

	String queryAllSql=" from dw_account_cash as p1 " +
			"left join dw_user as p2 on p1.user_id=p2.user_id " +
			"left join dw_user as admin on admin.user_id=p1.verify_userid " +
			"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
			"where 1=1 ";
	
	@Override
	public int getAllCashCount(SearchParam param) {
		String selectSql="select count(p1.id) ";
		String searchSql=param.getSearchParamSql();
		StringBuffer sb=new StringBuffer(selectSql);
		String querySql=sb.append(queryAllSql).append(searchSql).toString();
		
		int total=0;
		total=count(querySql);
		return total;
	}

	@Override
	public List getAllCashList(int start,int pernum, SearchParam param) {
		String selectSql="select p1.*,p4.name as bankname,p2.username,p2.realname,admin.username as verify_username ";
		String searchSql=param.getSearchParamSql();
		String orderSql=" order by p1.addtime desc ";
		if(("1").equals(param.getStatus())){
			orderSql=" order by p1.verify_time desc ";
		}
		String limitSql="limit ?,?";
		StringBuffer sb=new StringBuffer(selectSql);
		String querySql=sb.append(queryAllSql).append(searchSql)
				.append(orderSql).append(limitSql).toString();
		logger.debug("getAllCashList():"+querySql);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(querySql, new Object[]{start,pernum}, new AccountCashMapper());
		list=this.getJdbcTemplate().query(querySql, new Object[]{start,pernum}, getBeanMapper(AccountCash.class));
		return list;
	}
	
	public List getAllCashList(SearchParam param) {
		String selectSql="select p1.*,p4.name as bankname,p2.username,p2.realname,admin.username as verify_username ";
		String searchSql=param.getSearchParamSql();
		String orderSql=" order by p1.addtime desc ";
		// v1.6.5.3 RDPROJECT-186 xx 2013.09.18 start
		if(("1").equals(param.getStatus())){
			orderSql=" order by p1.verify_time desc ";
		}
		// v1.6.5.3 RDPROJECT-186 xx 2013.09.18 end
		String webid=Global.getValue("webid");
		if(StringUtils.isNull(webid).equals("ssjb")){
			orderSql=" order by p1.addtime asc";
		}
		
		StringBuffer sb=new StringBuffer(selectSql);
		String querySql=sb.append(queryAllSql).append(searchSql)
				.append(orderSql).toString();
		logger.debug("getAllCashList():"+querySql);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(querySql, new AccountCashMapper());
		list=this.getJdbcTemplate().query(querySql,getBeanMapper(AccountCash.class));
		return list;
	}

	@Override
	public AccountCash getAccountCash(long id) {
		String selectSql="select p1.*,p4.name as bankname,p2.username,p2.realname,admin.username as verify_username ";
		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(queryAllSql).append(" and p1.id=?");
		AccountCash c=null;
		try {
//			c=getJdbcTemplate().queryForObject(sb.toString(), new Object[]{id}, new AccountCashMapper());
			c=getJdbcTemplate().queryForObject(sb.toString(), new Object[]{id}, getBeanMapper(AccountCash.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		
		return c;
	}

	@Override
	public void updateCash(AccountCash cash) {
		String sql="update dw_account_cash set status=?,credited=?,fee=?,verify_userid=?,verify_time=?,verify_remark=? where id=?";
		getJdbcTemplate().update(sql, new Object[]{cash.getStatus(),cash.getCredited(),cash.getFee(),
				cash.getVerify_userid(),cash.getVerify_time(),cash.getVerify_remark(),cash.getId()});
	}

	// v1.6.5.3 RDPROJECT-174 xx 2013.09.16 start
	@Override
	public int verifyCash(AccountCash cash, int preStatus) {
		String sql="update dw_account_cash set status=?,credited=?,fee=?,verify_userid=?,verify_time=?,verify_remark=? where id=? and status="+preStatus;
		//logger.debug("更新提现： " + sql);
		return getJdbcTemplate().update(sql, new Object[]{cash.getStatus(),cash.getCredited(),cash.getFee(),
				cash.getVerify_userid(),cash.getVerify_time(),cash.getVerify_remark(),cash.getId()});
	}
	// v1.6.5.3 RDPROJECT-174 xx 2013.09.16 end

	@Override
	public int getAccountCashNum(long user_id, int status){
		String sql = "SELECT COUNT(1) FROM dw_account_cash AS p1  " +
				"LEFT JOIN dw_user AS p2 ON p1.user_id=p2.user_id  " +
				"WHERE DATE(FROM_UNIXTIME(p1.addtime)) = CURDATE() AND p1.user_id  = ?  AND p1.status = ?";
		logger.debug("SQL "+sql);
		int cashNum  =  getJdbcTemplate().queryForInt(sql, new Object[]{user_id,status });
		return cashNum;
	}
	
	// v1.6.5.3 RDPROJECT-96 xx 2013.09.10 start
	@Override
	public double getAccountCashDailySum(long user_id){
		String sql = "SELECT SUM(p1.total) FROM dw_account_cash AS p1  " +
				"LEFT JOIN dw_user AS p2 ON p1.user_id=p2.user_id  " +
				"WHERE DATE(FROM_UNIXTIME(p1.addtime)) = CURDATE() AND p1.user_id  = ?  AND p1.status in (0,1)";
		logger.debug("SQL "+sql);
		int cashNum  =  getJdbcTemplate().queryForInt(sql, new Object[]{user_id});
		return cashNum;
	}
	// v1.6.5.3 RDPROJECT-96 xx 2013.09.10 end
	
	@Override
	public List getAccountCashList(long user_id, int status) {
		String sql="select p1.*,p4.name as bankname,p2.username,p2.realname,admin.username as verify_username " +
				"from dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"left join dw_user as admin on admin.user_id=p1.verify_userid " +
				"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
				"where 1=1 and p1.user_id=? and p1.status=?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		List list=new ArrayList();
//		list=getJdbcTemplate().query(sql, new Object[]{user_id,status},new AccountCashMapper());
		list=getJdbcTemplate().query(sql, new Object[]{user_id,status}, getBeanMapper(AccountCash.class));
		return list;
	}

	@Override
	public double getAccountCashSum(long user_id, int status) {
		String sql="select sum(p1.total) as num " +
				"from dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"left join dw_user as admin on admin.user_id=p1.verify_userid " +
				"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
				"where 1=1 and p1.user_id=? and p1.status=?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		double total=0;
		total=this.sum(sql, new Object[]{user_id,status});
		return total;
	}

	@Override
	public List getAccountCashList(long user_id, int status, long startTime) {
		String sql="select p1.*,p4.name as bankname,p2.username,p2.realname,admin.username as verify_username " +
				"from dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"left join dw_user as admin on admin.user_id=p1.verify_userid " +
				"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
				"where 1=1 and p1.user_id=? and p1.status=? and p1.addtime>?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		List list=new ArrayList();
//		list=getJdbcTemplate().query(sql, new Object[]{user_id,status,startTime},new AccountCashMapper());
		list=getJdbcTemplate().query(sql, new Object[]{user_id,status,startTime}, getBeanMapper(AccountCash.class));
		return list;
	}

	@Override
	public double getAccountCashSum(long user_id, int status, long startTime) {
		String sql="select sum(p1.total) as num " +
				"from dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"left join dw_user as admin on admin.user_id=p1.verify_userid " +
				"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
				"where 1=1 and p1.user_id=? and p1.status=? and p1.verify_time>?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		double total=0;
		total=this.sum(sql, new Object[]{user_id,status,startTime});
		return total;
	}

	@Override
	public double getAccountCashSum(long user_id, int status, long startTime,
			long endTime) {
		String sql="select sum(p1.total) as num " +
				"from dw_account_cash as p1 " +
				"where 1=1 and p1.user_id=? and p1.status=? and p1.verify_time>? and p1.verify_time<?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		double total=0;
		total=this.sum(sql, new Object[]{user_id,status,startTime,endTime});
		return total;
	}
	
	@Override
	public double getAccountApplyCashSum(long user_id, long startTime,
			long endTime) {
		String sql="select sum(p1.total) as num " +
				"from dw_account_cash as p1 " +
				"where 1=1 and p1.user_id=? and p1.status in (0, 1) and p1.addtime>=? and p1.addtime<=?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		double total=0;
		total=this.sum(sql, new Object[]{user_id,startTime,endTime});
		return total;
	}
	
	@Override
	public double getSumTotal() {
		String sql="select sum(total) as num from dw_account";
		logger.debug("SQL:"+sql);
		double sumTotal=this.sum(sql);
		return sumTotal;
	}

	@Override
	public double getSumUseMoney() {
		String sql="select sum(use_money) as num from dw_account";
		logger.debug("SQL:"+sql);
		double sumUserMoney=this.sum(sql);
		return sumUserMoney;
	}

	@Override
	public double getSumNoUseMoney() {
		String sql="select sum(no_use_money) as num from dw_account";
		logger.debug("SQL:"+sql);
		double sumNoUseMoney=this.sum(sql);
		return sumNoUseMoney;
	}

	@Override
	public double getSumCollection() {
		String sql="select sum(collection) as num from dw_account";
		logger.debug("SQL:"+sql);
		double sumCollection=this.sum(sql);
		return sumCollection;
	}
	
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
	//垫付资金
//	@Override
//	public void getAdvance_insert(Advanced advanced) {
//		String sql="insert into dw_advanced(advance_reserve,no_advanced_account) " +
//				"values(?,?)";
//		logger.info("SQL:"+sql);
//		this.getJdbcTemplate().update(sql, advanced.getAdvance_reserve(),advanced.getNo_advanced_account());
//		
//	}
//	@Override
//	public List getAdvanceList(){
//		String sql="select * from dw_advanced";
//		logger.debug("SQL:"+sql);
//		List list=new ArrayList();
////		list=getJdbcTemplate().query(sql, new Object[]{},new AdvancedMapper());
//		list=getJdbcTemplate().query(sql, new Object[]{}, getBeanMapper(Advanced.class));
//		return list;
//	}
//	@Override
//	public void getAdvance_update(Advanced advanced){
//		String sql="update dw_advanced set advance_reserve=?,no_advanced_account=?,borrow_total=?,wait_total=?,borrow_day_total = ? where id=?";
//		getJdbcTemplate().update(sql, new Object[]{advanced.getAdvance_reserve(),advanced.getNo_advanced_account(),advanced.getBorrow_total(),advanced.getWait_total(),advanced.getBorrow_day_total(),advanced.getId()});
//	}
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end

	@Override
	public double getAccountNoCashSum(long user_id, int status, long startTime) {
		String sql="select sum(p1.total) as num " +
				"from dw_account_cash as p1 " +
				"left join dw_user as p2 on p1.user_id=p2.user_id " +
				"left join dw_user as admin on admin.user_id=p1.verify_userid " +
				"left join dw_linkage as p4 on p4.id=p1.bank and p4.type_id=25 " +
				"where 1=1 and p1.user_id=? and p1.status=? and p1.addtime>?";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		double total=0;
		total=this.sum(sql, new Object[]{user_id,status,startTime});
		return total;
	}

	@Override
	public AccountCash addFreeCash(final AccountCash cash) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 start
		final String sql="insert into dw_account_cash(user_id,account,bank,branch,total,credited,fee,old_fee,addtime,addip,hongbao,free_cash,interest_cash,award_cash,recharge_cash,borrow_cash,huikuan_cash) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 end
		logger.info("SQL:"+sql);
		
		this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, cash.getUser_id());
				ps.setString(2, cash.getAccount());
				ps.setString(3, cash.getBank());
				ps.setString(4, cash.getBranch());
				ps.setString(5, cash.getTotal());
				ps.setString(6, cash.getCredited());
				ps.setString(7, cash.getFee());
				// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 start
				ps.setString(8, cash.getOld_fee());
				ps.setString(9, cash.getAddtime());
				ps.setString(10, cash.getAddip());
				ps.setDouble(11, cash.getHongbao());
				ps.setDouble(12, cash.getFreecash());
				// v1.6.5.3 RDPROJECT-97 zza 2013-09-18 end
				ps.setDouble(13, cash.getInterest_cash());
				ps.setDouble(14, cash.getAward_cash());
				ps.setDouble(15, cash.getRecharge_cash());
				ps.setDouble(16, cash.getBorrow_cash());
				ps.setDouble(17, cash.getHuikuan_cash());
				return ps;
			}
		}, keyHolder);
		long key=keyHolder.getKey().longValue();
		cash.setId(key);
		return cash;
	}

}