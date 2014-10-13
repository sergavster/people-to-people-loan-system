package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.context.Global;
import com.p2psys.dao.AccountLogDao;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.AutoTenderOrder;
import com.p2psys.model.DetailTender;
import com.p2psys.model.SearchParam;
import com.p2psys.model.account.AccountLogModel;
import com.p2psys.model.account.AccountLogSumModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.util.StringUtils;

public class AccountLogDaoImpl extends BaseDaoImpl implements AccountLogDao {

	private static Logger logger = Logger.getLogger(BorrowDaoImpl.class);

	String getAccountLogSql = "from dw_account_log as p1 "
			+ "left join dw_user as p2 on p1.user_id=p2.user_id "
			+ "left join dw_user as p3 on p3.user_id=p1.to_user "
			+ "left join dw_linkage as p4 on p4.value=p1.type and p4.type_id=30 "
			+ "where 1=1 ";

	String selectSql = "select p1.*,p2.username,p2.realname,p3.username as to_username,p4.name as typename ";
	String countSql = "select count(p1.id) ";
	String orderSql = " order by p1.addtime desc,p1.id desc ";
	String limitSql = " limit ?,? ";
	String groupSql = " group by p1.type";
	String selectSql1 = "select sum(p1.money) as sum,p4.value as type,p4.name as typename ";
	String tenderLogSql = " from dw_borrow_tender as p1 "
			+ "left join dw_user as p2 on p1.user_id=p2.user_id "
			+ "left join dw_borrow as p3 on p3.id = p1.borrow_id "
			+ "where p3.status in (1,3,6,7,8) ";
	
	// v1.6.7.1 RDPROJECT-510 cx 2013-12-04 start
	//p3.award,p3.late_award p3.part_account,p3.funds
	String tenderSelSql = "select p1.*,p1.account as tender_account,p1.money as tender_money,"
			+ "p2.user_id as borrow_userid,p4.username as op_username,p2.username as username,p2.realname as realname,"
			+ "p3.apr,p3.time_limit,p3.time_limit_day,p3.isday,p3.name as borrow_name,p3.id as borrow_id,"
			+ "p3.account as borrow_account ,p3.account_yes as borrow_account_yes,p3.part_account as part_account,"
			+ "p3.verify_time,p3.award,p3.late_award,p3.part_account,p3.funds,p5.value as credit_jifen,p6.pic as credit_pic "
			+ "from dw_borrow_tender as p1 "
			+ "left join dw_borrow as p3 on p1.borrow_id=p3.id "
			+ "left join dw_user as p2 on p2.user_id = p1.user_id "
			+ "left join dw_user as p4 on p4.user_id = p3.user_id "
			+ "left join dw_credit as p5 on p1.user_id=p5.user_id "
			+ "left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 "
			+ "where p3.status in (1,3,6,7,8) ";
	// v1.6.7.1 RDPROJECT-510 cx 2013-12-04 end
	@Override
	public List getAccountLogList(long user_id) {
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getAccountLogSql).append(" and p1.user_id=? ")
				.append(orderSql).append(" limit 0,20");
		String sql = sb.toString();
		logger.debug("getAccountLogList()" + sql);
		List list = new ArrayList();
//		list = this.getJdbcTemplate().query(sql, new Object[] { user_id },
//				new AccountLogMapper());
		list = this.getJdbcTemplate().query(sql, new Object[] { user_id },
				getBeanMapper(AccountLogModel.class));
		return list;
	}

	@Override
	public List getAccountLogList(long user_id, int start, int end,
			SearchParam param) {
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		sb.append(getAccountLogSql).append(" and p1.user_id=? ")
				.append(searchSql).append(orderSql).append(limitSql);
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		List list = new ArrayList();
//		list = getJdbcTemplate().query(sql,
//				new Object[] { user_id, start, end }, new AccountLogMapper());
		list = getJdbcTemplate().query(sql,
				new Object[] { user_id, start, end }, getBeanMapper(AccountLogModel.class));
		return list;
	}

	/**
	 * 统计当前用户的资金记录的数量
	 * 
	 * @param user_id
	 * @return
	 */
	public int getAccountLogCount(long user_id, SearchParam param) {
		// sql性能优化 start
//		StringBuffer sb = new StringBuffer(countSql);
//		sb.append(getAccountLogSql).append(" and p1.user_id=? ")
//				.append(param.getSearchParamSql());
		StringBuffer sb = new StringBuffer("SELECT count(p1.id) FROM dw_account_log AS p1");
		sb.append(" LEFT JOIN dw_user AS p2 ON p1.user_id = p2.user_id WHERE  p1.user_id=?");
		sb.append(param.getSearchParamSql());
		// sql性能优化 end
		
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		int count = 0;
		count = count(sql, new Object[] { user_id });
		return count;
	}

	@Override
	public double getAccountLogTotalMoney(long user_id) {
		String sql = "select sum(money) as total from dw_account_log where user_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		double total = 0.0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
				new Object[] { user_id });
		if (rs.next()) {
			total = rs.getDouble("total");
		}
		return total;
	}

//	@Override
//	public double getAccountLogInterestTotalMoney(long user_id) {
//		String sql = "select sum(money) as total from dw_account_log where user_id=? and type='wait_interest'";
//		logger.debug("SQL:" + sql);
//		logger.debug("SQL:" + user_id);
//		double total = 0.0;
//		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
//				new Object[] { user_id });
//		if (rs.next()) {
//			total = rs.getDouble("total");
//		}
//		return total;
//	}

	@Override
	public void addAccountLog(AccountLog log) {
		String sql = "insert into dw_account_log("
				+ "user_id,type,total,money,use_money,no_use_money,collection,"
				+ "to_user,remark,addtime,addip) values(?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, log.getUser_id(), log.getType(),
				log.getTotal(), log.getMoney(), log.getUse_money(),
				log.getNo_use_money(), log.getCollection(), log.getTo_user(),
				log.getRemark(), log.getAddtime(), log.getAddip());
	}
//	@Override
//	public void addAccountLog(BaseAccountLog log) {
//		String sql = "insert into dw_account_log("
//				+ "user_id,type,total,money,use_money,no_use_money,collection,"
//				+ "to_user,remark,addtime,addip) values(?,?,?,?,?,?,?,?,?,?,?)";
//		this.getJdbcTemplate().update(sql, log.getUser_id(), log.getType(),
//				log.getTotal(), log.getMoney(), log.getUse_money(),
//				log.getNo_use_money(), log.getCollection(), log.getTo_user(),
//				log.getRemark(), log.getAddtime(), log.getAddip());
//	}

	@Override
	public int getAccountLogCount(SearchParam param) {
		StringBuffer sb = new StringBuffer("SELECT count(p1.id) FROM dw_account_log AS p1");
		sb.append(" LEFT JOIN dw_user AS p2 ON p1.user_id = p2.user_id WHERE 1 = 1");
		sb.append(param.getSearchParamSql());
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		int count = 0;
		count = count(sql);
		return count;
	}

	@Override
	public List getAccountLogList(int start, int pernum, SearchParam param) {
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		sb.append(getAccountLogSql).append(searchSql).append(orderSql)
				.append(limitSql);
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
//		list = getJdbcTemplate().query(sql, new Object[] { start, pernum },
//				new AccountLogMapper());
		list = getJdbcTemplate().query(sql, new Object[] { start, pernum }, getBeanMapper(AccountLogModel.class));
		return list;
	}

	public List getAccountLogList(SearchParam param) {
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		
		String webid = Global.getValue("webid");
		if (StringUtils.isNull(webid).equals("ssjb")) {
			orderSql = " order by p1.addtime desc,p1.id asc ";
		}
		
		sb.append(getAccountLogSql).append(searchSql).append(orderSql);
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
//		list = getJdbcTemplate().query(sql, new AccountLogMapper());
		list = getJdbcTemplate().query(sql, getBeanMapper(AccountLogModel.class));
		return list;
	}

	public double getAwardSum(long user_id) {
		String sql = "select sum(money) as sum from dw_account_log where type='award_add' and user_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		double total = 0.0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
				new Object[] { user_id });
		if (rs.next()) {
			total = rs.getDouble("sum");
		}
		return total;
	}

//	public double getrepayInterestSum(long user_id) {
//		String sql = "select sum(money) as sum from dw_account_log where type='wait_interest' and user_id=?";
//		logger.debug("SQL:" + sql);
//		logger.debug("SQL:" + user_id);
//		double total = 0.0;
//		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
//				new Object[] { user_id });
//		if (rs.next()) {
//			total = rs.getDouble("sum");
//		}
//		return total;
//	}

	public double getInvestInterestSum(long user_id) {
		String sql = "select investInterest  from view_invest_sum where  user_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		double total = 0.0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
				new Object[] { user_id });
		if (rs.next()) {
			total = rs.getDouble("investInterest");
		}
		return total;
	}

	@Override
	public double getAwardSum(long user_id, long starttime) {
		String sql = "select sum(money) as sum from dw_account_log where type='award_add' and user_id=? and addtime>=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		logger.debug("SQL:" + starttime);
		double total = 0.0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
				new Object[] { user_id, starttime });
		if (rs.next()) {
			total = rs.getDouble("sum");
		}
		return total;
	}

	@Override
	public int getTenderLogCount(SearchParam param) {
		StringBuffer sb = new StringBuffer(countSql);
		sb.append(tenderLogSql).append(param.getSearchParamSql());
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		int count = 0;
		count = count(sql);
		return count;
	}

	/**
	 * 投资列表
	 * 
	 * @param start start
	 * @param pernum pernum
	 * @param param param
	 * @return list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getTenderLogList(int start, int pernum, SearchParam param) {
		StringBuffer sb = new StringBuffer(tenderSelSql);
		String searchSql = param.getSearchParamSql();
		// v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
		sb.append(searchSql).append(orderSql).append("limit :start,:pernum");
		String sql = sb.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("pernum", pernum);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(DetailTender.class));
		// v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
	}

	/**
	 * 投资列表导出
	 * 
	 * @param param param
	 * @return list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getTenderLogList(SearchParam param) {
		StringBuffer sb = new StringBuffer(tenderSelSql);
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql).append(orderSql);
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
		list = getJdbcTemplate().query(sql, getBeanMapper(DetailTender.class));
		return list;
	}

	@Override
	public List getAccountLogList(long user_id, SearchParam param) {
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		sb.append(getAccountLogSql).append(" and p1.user_id=? ")
				.append(searchSql).append(orderSql);
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		List list = new ArrayList();
//		list = getJdbcTemplate().query(sql,
//				new Object[] { user_id }, new AccountLogMapper());
		list = getJdbcTemplate().query(sql,
				new Object[] { user_id }, getBeanMapper(AccountLogModel.class));
		return list;
	}

	@Override
	public double getAccountLogSum(String type) {
		String sql = "select sum(money) as sum from dw_account_log where type=?";
		double accountLogSum = 0;
		SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql,new Object[]{type});
		if(rs.next()){
			 accountLogSum = rs.getDouble("sum");
		}
		return accountLogSum;
	}

	@Override
	public List getAccountLogSumWithMonth(SearchParam param) {
		StringBuffer sb = new StringBuffer(selectSql1);
		String searchSql = param.getSearchParamSql();
		sb.append(getAccountLogSql).append(searchSql).append(groupSql);
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
		list = getJdbcTemplate().query(sql,this.getBeanMapper(AccountLogSumModel.class));
		return list;
	}

//	@Override
//	public int getAccountLogSumCount() {
//		String sql = "select count(*) from dw_linkage where type_id=30";
//		logger.debug("SQL:" + sql);
//		return getJdbcTemplate().queryForInt(sql);
//	}
	
//	/**
//	 * 查询user的线下充值奖励
//	 * @param userId
//	 * @param type 类型
//	 * @param startTime 开始时间
//	 * @param endTime 结束时间
//	 * @return
//	 */
//	public double getOfflineRewardSum(long userId, String type , long startTime, long endTime){
//		
//		if(userId <= 0){
//			logger.error("user_id is null.");
//			return 0;
//		}
//		
//		StringBuffer sql = new StringBuffer("select sum(dal.money) as num from dw_account_log dal where dal.user_id = ? ");
//		
//		// 查询数据数据封装 
//		List<Object> list = new ArrayList<Object>();
//		list.add(userId);
//		
//		//状态
//		if(type  != null && type.length() > 0){
//			list.add(type);
//			sql.append(" and dal.type = ? ");
//		}
//		
//		//奖励成功开始时间
//		if(startTime > 0){
//			list.add(startTime);
//			sql.append(" and  dal.addtime >= ? ");
//		}
//		
//		//奖励成功结束时间
//		if(endTime > 0){
//			list.add(endTime);
//			sql.append(" and dal.addtime <= ? ");
//		}
//		
//		// 新建obj数组，用于JDBC查询，数组程度为list size
//		Object[] obj = new Object[list.size()]; 
//		// 遍历list，将值存入obj数组中
//		for(int i = 0 ; i < list.size() ; i++){
//			obj[i] = list.get(i);
//		}
//		
//		double sum=0.0;
//		SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql.toString(),obj);
//		if(rs.next()){
//			sum=rs.getDouble("num");
//		}
//		return sum;
//	}

	
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-14 start
	@Override
	public int getAutoTenderLogCount(SearchParam param) {
		String selectSql = "SELECT COUNT(1) FROM dw_auto_tender_order as p2 where 1=1";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql);
		String sql = sb.toString();
		return getJdbcTemplate().queryForInt(sql);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<AutoTenderOrder> getAutoTenderLogList(int start, int pernum, SearchParam param) {
		String selectSql = "SELECT * FROM dw_auto_tender_order as p2 WHERE 1=1 ";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql).append(" ORDER BY p2.auto_order").append(" LIMIT :start,:pernum");
		String sql = sb.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("pernum", pernum);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(AutoTenderOrder.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<AutoTenderOrder> getAutoTenderLogList(SearchParam param) {
		String selectSql = "SELECT * FROM dw_auto_tender_order as p2 WHERE 1=1 ";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql).append(" ORDER BY p2.auto_order");
		String sql = sb.toString();
		return getJdbcTemplate().query(sql, getBeanMapper(AutoTenderOrder.class));
	}
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-14 end	
}
