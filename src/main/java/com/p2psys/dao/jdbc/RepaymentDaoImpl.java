package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.common.constant.ConsStatusRecord;
import com.p2psys.context.Constant;
import com.p2psys.dao.RepaymentDao;
import com.p2psys.domain.Repayment;
import com.p2psys.exception.BussinessException;
import com.p2psys.exception.RepaymentException;
import com.p2psys.model.CollectionSumModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.borrow.FastExpireModel;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;

public class RepaymentDaoImpl extends BaseDaoImpl implements RepaymentDao {
	private static Logger logger = Logger.getLogger(RepaymentDaoImpl.class);  

	@Override
	public List getRepaymentList(long user_id) {
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
		String sql="select  p1.*,(p1.order+1) as real_order,p2.name as borrow_name,p2.verify_time,p2.isday,p2.time_limit_day,p2.style as borrow_style, p2.type,p2.time_limit,p3.username,p3.user_id,p3.phone,p3.area " +
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 end

				"from dw_borrow_repayment as p1,dw_borrow as p2 ,dw_user as p3 " +
				"where p1.borrow_id=p2.id and p2.user_id=p3.user_id and ((p2.status=3 or p2.status=6 or p2.status=7 and p2.type<>"+Constant.TYPE_FLOW+") or (p2.status=1 and p2.type="+Constant.TYPE_FLOW+")) and p2.user_id=? order by p1.repayment_time asc";
		logger.debug("SQl:"+sql);
		logger.debug("SQL:"+user_id);
		List list=new ArrayList();
		// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(Repayment.class));
		// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
		return list;
	}
	
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 start
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Repayment> getRepaymentList(SearchParam param, long userId, long borrowId) {
		StringBuffer sb = new StringBuffer();
		// v1.6.7.2 RDPROJECT-494 sj 2013-12-05 start
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
		/*String selectSql = "select p2.*,(p2.order+1) as real_order,p1.name as borrow_name,"
				+ "p1.verify_time,p1.isday,p1.time_limit_day,p1.type,p1.style as borrow_style,"
				+ "p1.time_limit,p3.username,p3.user_id,p3.phone,p3.area "
				// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
				+ "from dw_borrow_repayment as p2,dw_borrow as p1 ,dw_user as p3 "
				+ "where p2.borrow_id=p1.id and p1.user_id=p3.user_id and "
				+ "((p1.status=3 or p1.status=6 or p1.status=7 or p1.status=8 and p1.type<>"
				+ Constant.TYPE_FLOW
				+ ") or (p1.status=1 and p1.type="+Constant.TYPE_FLOW+")) "
				+ "and p1.user_id=? and p1.id=?";*/
		String selectSql = "select p2.*,(p2.order+1) as real_order,p1.name as borrow_name,"
				+ "p1.verify_time,p1.isday,p1.time_limit_day,p1.type,p1.style as borrow_style,"
				+ "p1.time_limit,p3.username,p3.user_id,p3.phone,p3.area "
				// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
				+ "from dw_borrow_repayment as p2,dw_borrow as p1 ,dw_user as p3 "
				+ "where p2.borrow_id=p1.id and p1.user_id=p3.user_id and "
				+ "p1.user_id=? and p1.id=?";
		// v1.6.7.2 RDPROJECT-494 sj 2013-12-05 end
		String searchSql = searchSql(param);
		if (!StringUtils.isBlank(searchSql)) {
			sb.append(selectSql).append(searchSql);
		} else {
			sb.append(selectSql);
		}
		String sql = sb.toString();
		logger.debug("SQl:" + sql);
		logger.debug("SQL:" + userId);
		logger.debug("SQL:" + borrowId);
		List<Repayment> list = new ArrayList<Repayment>();
		list = this.getJdbcTemplate().query(sql, new Object[] { userId, borrowId }, getBeanMapper(Repayment.class));
		return list;
	}
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 end

	/**
	 * 查询条件共通
	 * @param param param
	 * @param sb sb
	 * @return sql
	 */
	private String searchSql(SearchParam param) {
		StringBuffer sb = new StringBuffer();
		String sql = null;
		if (!StringUtils.isBlank(param.getKeywords())) {
			sql = sb.append(" and p1.name like '%"
					+ StringUtils.isNull(param.getKeywords()) + "%'").toString();
		}
		if (!StringUtils.isBlank(param.getRepayment_time1())) {
			String paymentTime1 = Long.toString(
					DateUtils.valueOf(param.getRepayment_time1()).getTime() / 1000);
			sql = sb.append(" and p2.repayment_time >= " + paymentTime1).toString();
		}
		if (!StringUtils.isBlank(param.getRepayment_time2())) {
			String repaymentTime = param.getRepayment_time2() + " 23:59:59";
			String paymentTime2 = Long.toString(DateUtils.valueOf(repaymentTime).getTime() / 1000);
			sql = sb.append(" and p2.repayment_time <= " + paymentTime2).toString();
		}
		return sql;
	}
	
	// V1.6.6.2 RDPROJECT-346 ljd 2013-10-21 start
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 start
	@Override
	public int getRepaymentCount(SearchParam param, long userId) {
		StringBuffer sb = new StringBuffer();
		String selectSql = "select count(1) from dw_borrow_repayment as p2,dw_borrow as p1 ,dw_user as p3 "
				+ "where p2.borrow_id=p1.id and p1.user_id=p3.user_id and ((p1.status in (3,6,7,8) and p1.type<>"
				+ Constant.TYPE_FLOW + ") or (p1.status=1 and p1.type=" + Constant.TYPE_FLOW + ")) and p1.user_id=?";
		String searchSql = searchSql(param);
		if (!StringUtils.isBlank(searchSql)) {
			sb.append(selectSql).append(searchSql);
		} else {
			sb.append(selectSql);
		}
		String sql = sb.toString();
		int total = 0;
		try {
			total = this.count(sql, new Object[] { userId });
		} catch (Exception e) {
			logger.error("getRepaymentCount():" + e.getMessage());
		}
		return total;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Repayment> getRepaymentList(SearchParam param, long userId, int start, int end) {
		StringBuffer buffer = new StringBuffer(
			// v1.6.7.2 RDPROJECT-494 sj 2013-12-05 start	
			/*"select p2.*,(p2.order+1) as real_order,p1.name as borrow_name,p1.verify_time,p1.isday,"
				+ "p1.time_limit_day,p1.style as borrow_style,p1.type,p1.time_limit,p3.username,"
				+ "p3.user_id,p3.phone,p3.area from dw_borrow_repayment as p2,dw_borrow as p1 ,dw_user as p3 "
				+ "where p2.borrow_id=p1.id and p1.user_id=p3.user_id and ((p1.status in (3,6,7,8) and p1.type<>"
				+ Constant.TYPE_FLOW + ") or (p1.status=1 and p1.type=" + Constant.TYPE_FLOW
				+ ")) and p1.user_id=?");*/
				"select p2.*,(p2.order+1) as real_order,p1.name as borrow_name,p1.verify_time,p1.isday,"
				+ "p1.time_limit_day,p1.style as borrow_style,p1.type,p1.time_limit,p3.username,"
				+ "p3.user_id,p3.phone,p3.area from dw_borrow_repayment as p2,dw_borrow as p1 ,dw_user as p3 "
				+ "where p2.borrow_id=p1.id and p1.user_id=p3.user_id and p1.user_id=?");
		    // v1.6.7.2 RDPROJECT-494 2013-12-05 end
		// v1.6.7.1 RDPROJECT-464 lhm 2013-11-18 start
		// String orderSql = " order by p1.repayment_time asc";
		String orderSql = " order by p2.repayment_time,p2.id asc";
		// v1.6.7.1 RDPROJECT-464 lhm 2013-11-18 end
		String limitSql = " limit " + start + "," + end;
		String searchSql = searchSql(param);
		if (!StringUtils.isBlank(searchSql)) {
			buffer.append(searchSql).append(orderSql).append(limitSql);
		} else {
			buffer.append(orderSql).append(limitSql);
		}
		String sql = buffer.toString();
		List<Repayment> list = this.getJdbcTemplate().query(sql, 
				new Object[] { userId }, getBeanMapper(Repayment.class));
		return list;
	}
	// v1.6.7.1 RDPROJECT-485 zza 2013-11-25 end
	// V1.6.6.2 RDPROJECT-346 ljd 2013-10-21 end
	
	@Override
	public Repayment getRepayment(long repay_id) {
		String sql = "select * from dw_borrow_repayment where id=?";
		logger.debug("getRepayment():" + sql);
		logger.debug("id:" + repay_id);
		Repayment r = null;
		try {
			r = getJdbcTemplate().queryForObject(sql, new Object[] { repay_id }, getBeanMapper(Repayment.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
		}
		return r;
	}
	
	@Override
	public Repayment getRepayment(int order, long borrow_id) {
		String sql="select * from dw_borrow_repayment where `order`=? and borrow_id=?";
		 logger.debug("getRepayment():"+sql);
		 logger.debug("order:"+order);
		 logger.debug("borrow_id:"+borrow_id);
		Repayment r=null;
		try {
			r=getJdbcTemplate().queryForObject(sql, new Object[]{order,borrow_id}, getBeanMapper(Repayment.class));
		} catch (DataAccessException e) {
		  logger.debug(e.getMessage());
		  e.printStackTrace();
		}
		return r;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Repayment getRepaymentByTenderIdAndPeriod(long tender_id, long peroid) {
		String sql = "select * from dw_borrow_repayment r where r.tender_id=:tender_id and r.order=:peroid";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tender_id", tender_id);
		map.put("peroid", peroid);
		try {
            return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(Repayment.class));
        } catch (DataAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return null;
	}

	@Override
	public void addBatchRepayment(Repayment[] repays) {
		// v1.6.7.2 RDPROJECT-526 xx start
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
		String sql="insert into dw_borrow_repayment(user_id,borrow_id,`order`,status,webstatus,repayment_time,repayment_account,interest,capital,addtime,tender_id) " +
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		List list=new ArrayList();
		Object[] arg=null;
		for(int i=0;i<repays.length;i++){
			arg=new Object[]{repays[i].getUser_id(),repays[i].getBorrow_id(),repays[i].getOrder(),repays[i].getStatus(),repays[i].getWebstatus(),repays[i].getRepayment_time(),repays[i].getRepayment_account(),
					repays[i].getInterest(),repays[i].getCapital(),repays[i].getAddtime(),repays[i].getTender_id()};
			list.add(arg);
		}
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 end
		// v1.6.7.2 RDPROJECT-526 xx end
		getJdbcTemplate().batchUpdate(sql, list);
	}
	
	@Override
	public void addFlowRepayment(Repayment repay) {
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
		String sql = "insert into dw_borrow_repayment(user_id,borrow_id,`order`,status,repayment_time,repayment_account,interest,capital,addtime,tender_id) " +
				"values(?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,repay.getUser_id(),repay.getBorrow_id(),repay.getOrder(),repay.getStatus(),repay.getRepayment_time(),repay.getRepayment_account(),
				repay.getInterest(),repay.getCapital(),repay.getAddtime(),repay.getTender_id());
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 end
	}
	
	@Override
	public void modifyRepayment(Repayment repay) {
		String sql="update dw_borrow_repayment set webstatus=? where id=?";
		getJdbcTemplate().update(sql, repay.getWebstatus(), repay.getId());
	}
	
	@Override
	public int modifyRepaymentYes(Repayment repay) {
		//String sql="update dw_borrow_repayment set status=?,webstatus=?,repayment_yestime=?,repayment_yesaccount=? where id=?";
		String sql="update dw_borrow_repayment set status=?,webstatus=?,repayment_yestime=?,repayment_yesaccount=? where status=0 AND id=?";
		int count = getJdbcTemplate().update(sql, repay.getStatus(),repay.getWebstatus(),repay.getRepayment_yestime(),repay.getRepayment_yesaccount(), repay.getId());
		if(count!=1){
			throw new BussinessException("该还款计划已还款，请勿重复操作！");
		}
		return count;
	}
	
	@Override
	public void modifyFlowRepaymentYes(Repayment repay) {
		String sql = "update dw_borrow_repayment set status=:status,webstatus=:webstatus,repayment_yestime=:repayment_yestime,repayment_yesaccount=:repayment_yesaccount where id=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", repay.getStatus());
		map.put("webstatus", repay.getWebstatus());
		map.put("repayment_yestime", repay.getRepayment_yestime());
		map.put("repayment_yesaccount", repay.getRepayment_yesaccount());
		map.put("id", repay.getId());
		this.getNamedParameterJdbcTemplate().update(sql, map);
	}
	
	@Override
	public void modifyRepaymentBonus(Repayment repay) {
		String sql="update dw_borrow_repayment set repayment_account=?,bonus=? where id=?";
		getJdbcTemplate().update(sql, repay.getRepayment_account(), repay.getBonus(),repay.getId());
	}

	String getRepaymentListSql="from dw_borrow_repayment as p3,dw_borrow as p1 ,dw_user as p2 " +
			"where p3.borrow_id=p1.id and p1.user_id=p2.user_id ";
	
	
//	String allListSql="from dw_award_payments as p4, dw_borrow_repayment as p3,dw_borrow as p1 ,"
//			+ "dw_user as p2 where p4.id=p1.award and p3.borrow_id=p1.id and p1.user_id=p2.user_id ";
	
	@Override
	public List getRepaymentList(SearchParam param,int start,int pernum) {
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
		String selectSql="select  p3.*,p3.repayment_time as expire_time,(p3.order+1) as real_order,p1.name as borrow_name,p1.*,p1.style as borrow_style,p2.username,p2.user_id,p2.phone,p2.area ";
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 end
        StringBuffer sb=new StringBuffer(selectSql);
        // v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		sb.append(getRepaymentListSql).append("and p1.type<>"+Constant.TYPE_FLOW)
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
			.append(param.getSearchParamSql())
			.append(" order by p3.repayment_time")
			.append(" limit ?,?");
		logger.debug("getRepaymentList() SQL:"+sb.toString());
		List list=new ArrayList();
		try {
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			list=this.getJdbcTemplate().query(sb.toString(), new Object[]{start,pernum}, getBeanMapper(Repayment.class));
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getRepaymentCount(SearchParam param) {
		String selectSql="select count(1)";
		StringBuffer sb=new StringBuffer(selectSql);
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
		sb.append(getRepaymentListSql).append("and p1.type<>"+Constant.TYPE_FLOW+" ").append(param.getSearchParamSql());
		// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end
		logger.debug("getRepaymentCount() SQL:"+sb.toString());
		int total=0;
		total=count(sb.toString());
		return total;
	}

	String getLateListSql = "from dw_borrow_repayment as p3,dw_borrow as p1 ,dw_user as p2 "
			+ "where p3.borrow_id=p1.id and p1.user_id=p2.user_id ";
	@Override
	public List getLateList(SearchParam param, String nowTime, int start,
			int pernum) {
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
		// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//		String selectSql = "select  p3.*,(p3.order+1) as real_order,p1.name as borrow_name,p1.is_fast,p1.style AS borrow_style,p1.is_xin,p1.is_mb,p1.is_flow,p1.is_jin,p1.time_limit,p1.time_limit_day,p1.isday,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		String selectSql = "select  p3.*,(p3.order+1) as real_order,p1.name as borrow_name,p1.type,p1.style AS borrow_style,p1.time_limit,p1.time_limit_day,p1.isday,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 end
		String lateSql = " and p3.repayment_time+0<? AND p1.type<>"+Constant.TYPE_SECOND+" AND p1.type<>"+Constant.TYPE_FLOW+" AND (p3.repayment_yestime>p3.repayment_time OR p3.repayment_yestime IS NULL)";
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getLateListSql).append(param.getSearchParamSql())
				.append(lateSql).append(" limit ?,?");
		logger.debug("getLateList():" + sb.toString());
		logger.debug("nowTime:" + nowTime);
		List list = new ArrayList();
		try {
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			list = getJdbcTemplate().query(sb.toString(),
					new Object[] { nowTime, start, pernum },getBeanMapper(Repayment.class));
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	/*
	 * public List getLateList(SearchParam param,String nowTime, int start, int
	 * pernum) { String selectSql=
	 * "select  p3.*,p1.is_jin,p1.is_fast,p1.is_flow,p1.is_xin,p1.style as borrow_style,p1.name as borrow_name,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area "
	 * ; String lateSql=" and ((p3.repayment_yestime is null)) "; StringBuffer
	 * sb=new StringBuffer(selectSql);
	 * sb.append(getLateListSql).append(param.getSearchParamSql
	 * ()).append(lateSql).append(" order by p3.repayment_time asc limit ?,? ");
	 * logger.debug("getLateList():"+sb.toString());
	 * logger.debug("nowTime:"+nowTime); List list=new ArrayList();
	 * list=getJdbcTemplate().query(sb.toString(), new Object[]{start,pernum},
	 * new RepaymentUnionBorrowMapper()); return list; }
	 */

	@Override
	public int getLateCount(SearchParam param, String nowTime) {
		String selectSql = "select count(1) ";
		String lateSql = " and p3.repayment_time+0<? AND p1.type<>"+Constant.TYPE_SECOND+" AND p1.type<>"+Constant.TYPE_FLOW+" AND (p3.repayment_yestime>p3.repayment_time OR p3.repayment_yestime IS NULL)";
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getLateListSql).append(param.getSearchParamSql())
				.append(lateSql);
		logger.debug("getLateCount():" + sb.toString());
		logger.debug("nowTime:" + nowTime);
		int total = 0;
		total = this.count(sb.toString(), nowTime);
		return total;
	}

	@Override
	public boolean hasRepaymentAhead(int period, long borrow_id) {
		String sql="SELECT count(1) FROM dw_borrow_repayment WHERE status=0 AND `order`< :period AND borrow_id=:borrow_id ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("period", period);
		map.put("borrow_id", borrow_id);
		int count = this.getNamedParameterJdbcTemplate().queryForInt(sql, map);
		if(count>0){
			return true;
		}
		return false;
	}

	@Override
	public long getRepaymentCount(String search, long user_id) {
		String searchsql = getSearchSql(search);
		String sql = "SELECT count(*) FROM dw_borrow_repayment t1 INNER JOIN dw_borrow t2 ON t1.borrow_id = t2.id WHERE t2.user_id=?";
		sql = sql + searchsql;
		logger.debug("SQL:" + sql);
		long count = 0;
		try {
			count = getJdbcTemplate().queryForLong(sql, new Object[] { user_id });
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return count;
	}

	/**新加方法
	 * 根据搜索条件拼装sql
	 * 
	 * @param type
	 * @return
	 */
	private String getSearchSql(String search) {
		String searchSql = " AND t1.repayment_time > t1.repayment_yestime AND t1.STATUS=1";// 提前还款
		if ("lateRepayment".equals(search)) {
			searchSql = " AND t1.repayment_time < t1.repayment_yestime AND t1.STATUS=1";// 迟还款
		} else if ("overdueRepayment".equals(search)) {
			searchSql = " AND t1.late_days <=30 AND t1.late_days>0 AND t1.STATUS=1";// 30天内逾期还款
		} else if ("overdueRepayments".equals(search)) {
			searchSql = " AND t1.late_days>30 AND t1.STATUS=1";// 30天后逾期还款
		} else if ("overdueNoRepayments".equals(search)){
			String nowTime = Long.toString(System.currentTimeMillis() / 1000);
			searchSql = " AND "+nowTime+">t1.repayment_time AND t1.STATUS=0";
		}
		return searchSql;
	}

	@Override
	public int getCountRepaid(int user_id) {
		String sql= "select count(*) from dw_borrow where status=8 and user_id="+user_id;
		return count(sql);
	}

	@Override
	public int getCountExprire(int user_id) {
		// TODO Auto-generated method stub
		return 0;
	}
	String getOverdueListSql="from dw_borrow_repayment as p3,dw_borrow as p1 ,dw_user as p2 " +
			"where p3.borrow_id=p1.id and p1.user_id=p2.user_id and p1.status in(3,6,7,8) ";
	 //逾期（overdue）的借款标count 
		@Override
		public int getOverdueCount(SearchParam param,String nowTime) {
			String selectSql="select count(1) ";
			//String lateSql=" and ((p3.repayment_yestime is null)) ";
			String overdueSql=" and ((p3.repayment_yestime is null and p3.repayment_time<"+nowTime+") or (p3.repayment_yestime is not null and p3.repayment_yestime>p3.repayment_time)) ";
			StringBuffer sb=new StringBuffer(selectSql);
			sb.append(getOverdueListSql).append(param.getSearchParamSql()).append(overdueSql);
			logger.debug("getOverdueCount():"+sb.toString());
			logger.debug("nowTime:"+nowTime);
			int total=0;
			total=this.count(sb.toString());
			logger.debug("getOverdueCount"+total);
			return total;
		}
		 //逾期（overdue）的借款标列表
		@Override
		public List getOverdueList(SearchParam param,String nowTime, int start, int pernum) {
			// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//			String selectSql="select  p3.*,p1.is_mb,p1.is_jin,p1.is_fast,p1.is_flow,p1.is_xin,p1.is_offvouch,p1.is_pledge,p1.style as borrow_style,p1.name as borrow_name,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area,(("+nowTime+"-p3.repayment_time)/(24*60*60)) as unRepayTimeOverdue,((p3.repayment_yestime-p3.repayment_time)/(24*60*60)) as OverdueTime,p1.account ";
			String selectSql="select  p3.*,p1.type,p1.style as borrow_style,p1.name as borrow_name,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area,(("+nowTime+"-p3.repayment_time)/(24*60*60)) as unRepayTimeOverdue,((p3.repayment_yestime-p3.repayment_time)/(24*60*60)) as OverdueTime,p1.account ";
			// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
			//String lateSql=" and ((p3.repayment_yestime is null)) ";
			String overdueSql=" and ((p3.repayment_yestime is null and p3.repayment_time<"+nowTime+") or (p3.repayment_yestime is not null and p3.repayment_yestime>p3.repayment_time)) ";
			StringBuffer sb=new StringBuffer(selectSql);
			sb.append(getOverdueListSql).append(param.getSearchParamSql()).append(overdueSql).append(" order by p3.repayment_time asc limit ?,? ");
			logger.debug("getOverdueList():"+sb.toString());
			logger.debug("nowTime:"+nowTime);
			List list=new ArrayList();
			list=getJdbcTemplate().query(sb.toString(), new Object[]{start,pernum}, getBeanMapper(Repayment.class));
			return list;
		}
		@Override
		public List getOverdueList(SearchParam param,String nowTime) {
			// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//			String selectSql="select  p3.*,p1.is_mb,p1.is_jin,p1.is_fast,p1.is_flow,p1.is_xin,p1.is_offvouch,p1.is_pledge,p1.style as borrow_style,p1.name as borrow_name,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area,(("+nowTime+"-p3.repayment_time)/(24*60*60)) as unRepayTimeOverdue,((p3.repayment_yestime-p3.repayment_time)/(24*60*60)) as OverdueTime,p1.account ";
			String selectSql="select  p3.*,p1.type,p1.style as borrow_style,p1.name as borrow_name,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area,(("+nowTime+"-p3.repayment_time)/(24*60*60)) as unRepayTimeOverdue,((p3.repayment_yestime-p3.repayment_time)/(24*60*60)) as OverdueTime,p1.account ";
			// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
			//String lateSql=" and ((p3.repayment_yestime is null)) ";
			String overdueSql=" and ((p3.repayment_yestime is null and p3.repayment_time<"+nowTime+") or (p3.repayment_yestime is not null and p3.repayment_yestime>p3.repayment_time)) ";
			StringBuffer sb=new StringBuffer(selectSql);
			sb.append(getOverdueListSql).append(param.getSearchParamSql()).append(overdueSql).append(" order by p3.repayment_time asc");
			logger.debug("getOverdueList():"+sb.toString());
			logger.debug("nowTime:"+nowTime);
			List list=new ArrayList();
			list=getJdbcTemplate().query(sb.toString(), new Object[]{}, getBeanMapper(Repayment.class));
			return list;
		}
		@Override
		public List getRepaymentList(SearchParam param) {
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
			String selectSql="select  p3.*,p3.repayment_time as expire_time,(p3.order+1) as real_order,p1.name as borrow_name,p1.*,p1.style as borrow_style,p2.username,p2.user_id,p2.phone,p2.area ";
			// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
			StringBuffer sb=new StringBuffer(selectSql);
			sb.append(getRepaymentListSql).append("and p1.type<>"+Constant.TYPE_FLOW+" ")
				.append(param.getSearchParamSql())
				.append(" order by p3.repayment_time  ");
			logger.debug("getRepaymentList() SQL:"+sb.toString());
			List list=new ArrayList();
			try {
				// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
				list=this.getJdbcTemplate().query(sb.toString(), new Object[]{}, getBeanMapper(Repayment.class));
				// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
			return list;
		}
		/**
		 * 统计待还款总额 
		 * @param param	
		 * @return
		 */
		@Override
		public double getRepaymentSum(SearchParam param)
		{
			String selectSql = "select sum(p3.repayment_account) as sum ";
			StringBuffer sb = new StringBuffer(selectSql);
			sb.append(getRepaymentListSql).append(param.getSearchParamSql());
			logger.debug((new StringBuilder("getRepaymentSum() SQL:")).append(sb.toString()).toString());
			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString());
			double sum = 0;
			if (rs.next()) {
				sum = rs.getDouble("sum");
			}
			return sum;
		}
	
	@Override
	public List getAllRepaymentList(int status) {
		// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
		// String sql="select * from dw_borrow_repayment where webstatus=? and status!=1";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM dw_borrow_repayment r WHERE NOT EXISTS (SELECT sr.id FROM dw_status_record sr WHERE sr.type=");
		sb.append(ConsStatusRecord.SR_TYPE_REPAY);
		sb.append(" AND sr.fk_id=r.id AND sr.current_status=0 AND sr.target_status=1 AND sr.status=1) ");
		sb.append("AND r.webstatus=? AND r.status!=1");
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sb.toString(), new Object[]{status}, getBeanMapper(Repayment.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
		// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
	}

	@Override
	public List getAllRepaymentList(int webstatus, long start,long end) {
		String sql="select * from dw_borrow_repayment where webstatus=? and status!=1 and repayment_time>? and repayment_time<?";
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sql, new Object[]{webstatus,start,end}, getBeanMapper(Repayment.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void modifyRepaymentStatus(long id, int status, int webstatus) {
		String sql="update dw_borrow_repayment set status=?,webstatus=? where id=?";
		getJdbcTemplate().update(sql, status,webstatus,id);
	}
	
	@Override
	public int predictRepay(long id) {
		String sql = "UPDATE dw_borrow_repayment SET webstatus=1 WHERE status=0 AND webstatus=0 AND id=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		int c = this.getNamedParameterJdbcTemplate().update(sql, map);
		if (c != 1) {
			throw new RepaymentException("该期借款已经还款,请勿重复操作！");
		}
		return c;
	}
	
	/**
	 * 查询所有未还款的计划，每个标取最小的一个
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getToPayRepayMent() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT MIN(a.repayment_time) repayment_time, a.borrow_id, b.user_id,  b.name,a.order,a.repayment_account  FROM dw_borrow_repayment a ");
		sql.append(" INNER JOIN dw_borrow b on a.borrow_id = b.id ");
		sql.append(" where  a.status = 0 AND a.webstatus = 0  AND a.repayment_yestime is NULL GROUP BY a.borrow_id ");
		logger.info("查找所有待还" + sql);
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(
				sql.toString());
		return list;
	}

	@Override
	public List<Map<String, Object>> getLateList() {
		// 查询系统中所有逾期的标
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
		String sql = "select id,repayment_time,borrow_id,real_extension_day,capital,`order`,late_days,late_interest,interest from dw_borrow_repayment where status = 0 and  repayment_yestime is null and  repayment_time+0<?";
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end			
		logger.info("查找所有逾期未还的标" + sql);
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(
				sql, DateUtils.getNowTimeStr());
		return list;
	}

	@Override
	public double getSumLateMoney(long id) {
		String sql = "select sum(capital) capital from dw_borrow_repayment  where borrow_id  =?  and status = 0 ";
		logger.info("根据标的id查找还款计划表中逾期信息" + sql);
		double sumMoney = this.getJdbcTemplate().queryForObject(sql,
				new Object[] { id }, Double.class);
		return sumMoney;
	}

	@Override
	public void updateLaterepayment(long lateDay, long id, double late_interest) {
		String sql = "update dw_borrow_repayment set late_days=?,late_interest=? where id=? ";
		logger.info("更改还款计划表中逾期信息" + sql);
		this.getJdbcTemplate().update(sql,
				new Object[] { lateDay, late_interest, id });

	}

	@Override
	public List getLateRepaymentByBorrowid(long borrow_id) {
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
		// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//		String selectSql = "select  p3.*,(p3.order+1) as real_order,p1.name as borrow_name,p1.is_fast,p1.style AS borrow_style,p1.is_xin,p1.is_mb,p1.is_flow,p1.is_jin,p1.time_limit,p1.time_limit_day,p1.isday,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		String selectSql = "select  p3.*,(p3.order+1) as real_order,p1.name as borrow_name,p1.type,p1.style AS borrow_style,p1.time_limit,p1.time_limit_day,p1.isday,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 end
		String lateSql = " and p3.repayment_time+0<? AND p3.repayment_yestime IS NULL AND p3.borrow_id=?";
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getLateListSql).append(lateSql);
		logger.debug("getLateList():" + sb.toString());
		logger.debug("nowTime:" + DateUtils.getNowTimeStr());
		List list = new ArrayList();
		try {
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 start
			list = getJdbcTemplate().query(sb.toString(),
					new Object[] { DateUtils.getNowTimeStr(), borrow_id },getBeanMapper(Repayment.class));
			// v1.6.7.2 RDPROJECT-510 cx 2013-12-10 end
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public double getAllSumLateMoney(int status) {
		String sql = "select sum(capital) as sum from dw_borrow_repayment where status=?";
		double sum = 0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql, new Object[]{status});
		if(rs.next()){
			sum = rs.getDouble("sum");
		}
		return sum;
	}

	@Override
	public List getFastExpireList(SearchParam param,int start, int pernum) {
		String selectSql = "select p3.*,p1.id as borrow_id,p2.username as borrow_user,p1.name as borrow_name ";
		String fastSql = " and p1.type<>"+Constant.TYPE_SECOND+" and p1.type<>"+Constant.TYPE_FLOW+" and ((p3.repayment_time>=? and p3.repayment_time>p3.repayment_yestime) or ((p3.repayment_yestime is null and p3.repayment_time<?) or (" +
				"p3.repayment_yestime is not null and p3.repayment_time<p3.repayment_yestime)))";
		StringBuffer sb =new StringBuffer(selectSql);
		sb.append(getLateListSql).append(fastSql).append(param.getSearchParamSql()).append(" limit ?,?");
		logger.debug("getFastExpireList:" + sb.toString());
		logger.debug("nowTime:" + DateUtils.getNowTimeStr());
		List list = new ArrayList();
		try{
			list = getJdbcTemplate().queryForList(sb.toString(),new Object[]{DateUtils.getNowTimeStr(),DateUtils.getNowTimeStr(),start,pernum});
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getFastExpireCount(SearchParam param) {
		String selectSql = "select count(1)";
		String fastSql = " and p1.type<>"+Constant.TYPE_SECOND+" and p1.type<>"+Constant.TYPE_FLOW+" and ((p3.repayment_time>=? and p3.repayment_time>p3.repayment_yestime) or ((p3.repayment_yestime is null and p3.repayment_time<?) or (" +
				"p3.repayment_yestime is not null and p3.repayment_time<p3.repayment_yestime)))";
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getLateListSql).append(fastSql).append(param.getSearchParamSql());
		logger.debug("getFastExpireCount:" + sb.toString());
		int count = 0;
		try{
			count = count(sb.toString(),new Object[]{DateUtils.getNowTimeStr(),DateUtils.getNowTimeStr()});
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List getFastExpireList(SearchParam param) {
		String selectSql = "select p3.*,p1.id as borrow_id,p2.username as borrow_user,p1.name as borrow_name ";
		String fastSql = " and p1.type<>"+Constant.TYPE_SECOND+" and p1.type<>"+Constant.TYPE_FLOW+" and ((p3.repayment_time>=? and p3.repayment_time>p3.repayment_yestime) or ((p3.repayment_yestime is null and p3.repayment_time<?) or (" +
				"p3.repayment_yestime is not null and p3.repayment_time<p3.repayment_yestime)))";
		StringBuffer sb =new StringBuffer(selectSql);
		sb.append(getLateListSql).append(fastSql).append(param.getSearchParamSql());
		logger.debug("getFastExpireList:" + sb.toString());
		logger.debug("nowTime:" + DateUtils.getNowTimeStr());
		List list = new ArrayList();
		try{
			list = this.getJdbcTemplate().query(sb.toString(),new Object[]{DateUtils.getNowTimeStr(),DateUtils.getNowTimeStr()},getBeanMapper(FastExpireModel.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public double getAllLateSumWithNoRepaid() {
		String selectSql = "select sum(p3.repayment_account) as sum ";
		String lateSql = " and p1.type<>"+Constant.TYPE_SECOND+" and p3.status=0 and p3.repayment_yestime is null and p3.repayment_time<?";
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getLateListSql).append(lateSql);
		logger.debug("getAllLateSumWithNoRepaid:" +sb.toString());
		double total = 0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString(), new Object[]{DateUtils.getNowTimeStr()});
		if(rs.next()){
			total = rs.getDouble("sum");
		}
		return total;
	}

	@Override
	public double getAllLateSumWithYesRepaid() {
		String selectSql = "select sum(p3.repayment_account) as sum ";
		String lateSql = " and p1.type<>"+Constant.TYPE_SECOND+" and p3.status=1 and p3.repayment_yestime is not null and p3.repayment_time<p3.repayment_yestime";
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getLateListSql).append(lateSql);
		logger.debug("getAllLateSumWithYesRepaid:" +sb.toString());
		double total = 0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString(), new Object[]{});
		if(rs.next()){
			total = rs.getDouble("sum");
		}
		return total;
	}
	
	// v1.6.7.1 RDPROJECT-419 zza 2013-11-26 start
	@Override
	public double getDayMatureAccount(String dotime1, String dotime2) { // 每日到期金额合计
		String selectSql = "select sum(p3.repayment_account) as account ";
		StringBuffer sb = new StringBuffer(selectSql);
		selectMature(sb, dotime1, dotime2);
		logger.debug("getRepaymentList() SQL:" + sb.toString());
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString());
		double sum = 0;
		if (rs.next()) {
			sum = rs.getDouble("account");
		}
		return sum;
	}
	
	@Override
	public double getDayCapitalAccount(String dotime1, String dotime2) { // 每日到期本金合计
		String selectSql = "select sum(p3.capital) as capital ";
		StringBuffer sb = new StringBuffer(selectSql);
		selectMature(sb, dotime1, dotime2);
		logger.debug("getRepaymentList() SQL:" + sb.toString());
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString());
		double sum = 0;
		if (rs.next()) {
			sum = rs.getDouble("capital");
		}
		return sum;
	}
	
	@Override
	public double getDayMatureInterest(String dotime1, String dotime2) { // 每日到期利息合计
		String selectSql = "select sum(p3.interest) as interest ";
		StringBuffer sb = new StringBuffer(selectSql);
		selectMature(sb, dotime1, dotime2);
		logger.debug("getRepaymentList() SQL:" + sb.toString());
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString());
		double sum = 0;
		if (rs.next()) {
			sum = rs.getDouble("interest");
		}
		return sum;
	}

	/**
	 * 查询到期（应还时间）列表共通
	 * @param sb sb
	 * @param dotime1 查询开始时间
	 * @param dotime2 查询结束时间
	 */
	private void selectMature(StringBuffer sb, String dotime1, String dotime2) {
		// 如果页面不输入时间段，默认查询当天的00:00:00~23:59:59
		if (StringUtils.isBlank(dotime1) && StringUtils.isBlank(dotime2)) {
			sb.append(getRepaymentListSql).append(
					" and (p3.repayment_time >= " + DateUtils.getIntegralTime().getTime() / 1000
							+ " and p3.repayment_time <= " + DateUtils.getLastIntegralTime().getTime() / 1000 + ")");
		} else {
			String dotimeStr1 = null;
			String dotimeStr2 = null;
			try {
				dotimeStr1 = Long.toString(DateUtils.valueOf(dotime1).getTime() / 1000);
			} catch (Exception e) {
				dotimeStr1 = "";
			}
			try {
				Date d = DateUtils.valueOf(dotime2);
				d = DateUtils.rollDay(d, 1);
				dotimeStr2 = Long.toString(d.getTime() / 1000);
			} catch (Exception e) {
				dotimeStr2 = "";
			}
			sb.append(getRepaymentListSql).append(
					" and (p3.repayment_time >= " + dotimeStr1 + " and p3.repayment_time <= " + dotimeStr2 + ")");
		}
	}
	
	/**
	 * 查询已还款列表共通
	 * @param sb sb
	 * @param dotime1 查询开始时间
	 * @param dotime2 查询结束时间
	 */
	private void selectRepayYes(StringBuffer sb, String dotime1, String dotime2) {
		// 如果页面不输入时间段，默认查询当天的00:00:00~23:59:59
		if (StringUtils.isBlank(dotime1) && StringUtils.isBlank(dotime2)) {
			sb.append(getRepaymentListSql).append(
					" and (p3.repayment_yestime >= " + DateUtils.getIntegralTime().getTime() / 1000
							+ " and p3.repayment_yestime <= " + DateUtils.getLastIntegralTime().getTime() / 1000 + ")");
		} else {
			String dotimeStr1 = null;
			String dotimeStr2 = null;
			try {
				dotimeStr1 = Long.toString(DateUtils.valueOf(dotime1).getTime() / 1000);
			} catch (Exception e) {
				dotimeStr1 = "";
			}
			try {
				Date d = DateUtils.valueOf(dotime2);
				d = DateUtils.rollDay(d, 1);
				dotimeStr2 = Long.toString(d.getTime() / 1000);
			} catch (Exception e) {
				dotimeStr2 = "";
			}
			sb.append(getRepaymentListSql).append(
					" and (p3.repayment_yestime >= " + dotimeStr1 + " and p3.repayment_yestime <= " + dotimeStr2 + ")");
		}
	}
	
	@Override
	public double getDayRepayCapital(String dotime1, String dotime2) { // 每日还款本金合计
		String selectSql = "select sum(p3.capital) as capital ";
		StringBuffer sb = new StringBuffer(selectSql);
		selectRepayYes(sb, dotime1, dotime2);
		logger.debug("getRepaymentList() SQL:" + sb.toString());
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString());
		double sum = 0;
		if (rs.next()) {
			sum = rs.getDouble("capital");
		}
		return sum;
	}
	
	@Override
	public double getDayRepayInterest(String dotime1, String dotime2) { // 每日还款利息合计
		String selectSql = "select sum(p3.interest) as interest ";
		StringBuffer sb = new StringBuffer(selectSql);
		selectRepayYes(sb, dotime1, dotime2);
		logger.debug("getRepaymentList() SQL:" + sb.toString());
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sb.toString());
		double sum = 0;
		if (rs.next()) {
			sum = rs.getDouble("interest");
		}
		return sum;
	}
	
	// v1.6.7.1 添加分页  zza 2013-11-25 start
	@Override
	public int getDayMatureCount(SearchParam p, String dotime1, String dotime2) {
		StringBuffer sql = new StringBuffer("select count(*) ");
		selectMature(sql, dotime1, dotime2);
		logger.debug("getUserCount():" + sql);
		int total = count(sql.toString());
		return total;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Repayment> getDayMatureList(int page, int pernum, SearchParam p, String dotime1, String dotime2) {
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 START
		String selectSql = "select p3.*,(p3.order+1) as real_order,p1.name as borrow_name,"
				+ "p1.time_limit,p1.style as borrow_style,p2.username,p2.realname,p2.user_id,p2.phone,p2.area ";
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-27 end
		StringBuffer sb = new StringBuffer(selectSql);
		selectMature(sb, dotime1, dotime2);
		sb.append(" limit :page, :pernum");
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("page", page);
		map.put("pernum", pernum);
		logger.debug("getRepaymentList() SQL:" + sb.toString());
		return this.getNamedParameterJdbcTemplate().query(sb.toString(), map, getBeanMapper(Repayment.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Repayment> getDayMatureList(SearchParam param, String dotime1, String dotime2) {
		String selectSql = "select p3.*,(p3.order+1) as real_order,p1.name as borrow_name,"
				+ "p1.time_limit,p1.style as borrow_style,p2.username,p2.realname,p2.user_id,p2.phone,p2.area ";
		StringBuffer sb = new StringBuffer(selectSql);
		selectMature(sb, dotime1, dotime2);
		logger.debug("getRepaymentList() SQL:" + sb.toString());
		List<Repayment> list = new ArrayList<Repayment>();
		list = getJdbcTemplate().query(sb.toString(), getBeanMapper(Repayment.class));
		return list;
	}
	// v1.6.7.1 添加分页  zza 2013-11-25 end
	
	@Override
	public double getDayTenderFunds(String dotime1, String dotime2) { // 每日投标时发放的奖励
		String selectSql = "select sum(money) as money from dw_account_log where type = 'award_add' ";
		return getCount(dotime1, dotime2, selectSql);
	}

	/**
	 * 奖励统计共通
	 * @param dotime1 查询开始时间
	 * @param dotime2 查询结束时间
	 * @param selectSql 拼接sql
	 * @return double
	 */
	private double getCount(String dotime1, String dotime2, String selectSql) {
		StringBuffer sb = new StringBuffer(selectSql);
		// 如果页面不输入时间段，默认查询当天的00:00:00~23:59:59
		if (StringUtils.isBlank(dotime1) && StringUtils.isBlank(dotime2)) {
			sb.append(
					"and (addtime >= " + DateUtils.getIntegralTime().getTime() / 1000
							+ " and addtime <= " + DateUtils.getLastIntegralTime().getTime() / 1000 + ")");
		} else {
			String dotimeStr1 = null;
			String dotimeStr2 = null;
			try {
				dotimeStr1 = Long.toString(DateUtils.valueOf(dotime1).getTime() / 1000);
			} catch (Exception e) {
				dotimeStr1 = "";
			}
			try {
				Date d = DateUtils.valueOf(dotime2);
				d = DateUtils.rollDay(d, 1);
				dotimeStr2 = Long.toString(d.getTime() / 1000);
			} catch (Exception e) {
				dotimeStr2 = "";
			}
			sb.append("and (addtime >= " + dotimeStr1 + " and addtime <= " + dotimeStr2 + ")");
		}
		String sql = sb.toString();
		logger.debug("getCountSql: " + sql);
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		double sum = 0;
		if (rs.next()) {
			sum = rs.getDouble("money");
		}
		return sum;
	}
	
	@Override
	public double getDayRepaymentFunds(String dotime1, String dotime2) { // 每日还款时发放的奖励
		String selectSql = "select sum(money) as money from dw_account_log where type = 'repayment_award' ";
		return getCount(dotime1, dotime2, selectSql);
	}
	// v1.6.7.1 RDPROJECT-419 zza 2013-11-26 end

	@Override
	public List<Repayment> getRepaymentByBorrowId(long borrow_id) {
		String sql = "select a.* from dw_borrow_repayment a left join dw_borrow b on a.borrow_id=b.id where a.borrow_id=? and a.repayment_yestime is null";
		logger.debug("getRepaymentByBorrowId():" + sql);
		List<Repayment> list = new ArrayList<Repayment>();
		try{
			list = this.getJdbcTemplate().query(sql, new Object[]{borrow_id}, getBeanMapper(Repayment.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<Repayment> getRepayListByBorrowId(long borrow_id) {
		String sql = "select a.* from dw_borrow_repayment a left join dw_borrow b on a.borrow_id=b.id where a.borrow_id=? ";
		logger.debug("getRepaymentByBorrowId():" + sql);
		List<Repayment> list = new ArrayList<Repayment>();
		try{
			list = this.getJdbcTemplate().query(sql, new Object[]{borrow_id}, getBeanMapper(Repayment.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 统计不同类型某时间段的用户总额
	 * @param user_id
	 * @param start 统计时间开始时间
	 * @param end 统计时间结束时间
	 * @param status 还款利息状态 ,如果status为-1，则为所有类型
	 * @return
	 */
	public double getRepayInterestSum(long user_id,int status, long start_time , long end_time){
		
		if(user_id <= 0){
			logger.error("user_id is null.");
			return 0;
		}
		
		StringBuffer sql = new StringBuffer("select sum(p3.interest) as interest ");
		sql.append("from dw_borrow_repayment as p3,dw_borrow as p1 ,dw_user as p2 where p3.borrow_id=p1.id and p1.user_id=p2.user_id and p1.user_id = ? ");
		
		// 查询数据数据封装 and verify_time>?
		List<Object> list = new ArrayList<Object>();
		list.add(user_id);
		
		//待还状态
		if(status >= 0){
			list.add(status);
			sql.append(" and p3.status = ? ");
		}
		
		//还款成功开始时间
		if(start_time > 0){
			list.add(start_time);
			sql.append(" and p3.repayment_yestime >= ? ");
		}
		
		//还款成功结束时间
		if(end_time > 0){
			list.add(end_time);
			sql.append(" and p3.repayment_yestime <= ? ");
		}
		
		// 新建obj数组，用于JDBC查询，数组程度为list size
		Object[] obj = new Object[list.size()]; 
		// 遍历list，将值存入obj数组中
		for(int i = 0 ; i < list.size() ; i++){
			obj[i] = list.get(i);
		}
		
		SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql.toString(),obj);
		double sum=0;
		if(rs.next()){
			sum=rs.getDouble("interest");
		}
		return sum;
	}

	@Override
	public List<Map<String, Object>> getAllDueRepayMent(Date date) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.repayment_time, a.borrow_id, b.user_id,  b.name,a.order,a.repayment_account  FROM dw_borrow_repayment a ");
		sql.append(" INNER JOIN dw_borrow b on a.borrow_id = b.id ");
		sql.append(" where  a.status = 0 AND a.webstatus = 0  AND a.repayment_yestime is NULL AND FROM_UNIXTIME(a.repayment_time, '%Y-%m-%d') = ? ");
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(
				sql.toString(), new Object[]{DateUtils.dateStr2(date)});
		return list;
	}

	@Override
	public void modifyRepaymentExtensionInterest(Repayment repay) {
		String sql="update dw_borrow_repayment set extension_interest=?,real_extension_day=? where id=?";
		getJdbcTemplate().update(sql, repay.getExtension_interest(),repay.getReal_extension_day(), repay.getId());
	}
	
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 start
	/**
	 * 已还款、未还款本金合计、本息合计、利息合计
	 * @param param param
	 * @param type type
	 * @return list
	 */
	public CollectionSumModel getRepaySum(SearchParam param, String type) {
		String sql;
		if ("1".equals(type)) {
			sql = "select sum(p3.repayment_yesaccount) as account, "
					+ "sum(p3.interest) as interest, sum(p3.capital) as capital ";
		} else {
			sql = "select sum(p3.repayment_account) as account, "
					+ "sum(p3.interest) as interest, sum(p3.capital) as capital ";
		}
		StringBuffer sb = new StringBuffer(sql);
		sb.append(getRepaymentListSql).append("and p1.type<>"+Constant.TYPE_FLOW+" ").append(param.getSearchParamSql());
		logger.debug("getRepaySum()还款统计 SQL:" + sb.toString());
		CollectionSumModel model = getJdbcTemplate().queryForObject(sb.toString(), new RowMapper<CollectionSumModel>() {
			public CollectionSumModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				CollectionSumModel model = new CollectionSumModel();
				model.setAccount(rs.getDouble("account"));
				model.setCapital(rs.getDouble("capital"));
				model.setInterest(rs.getDouble("interest"));
				return model;
			}
		});
		return model;
	}
	// v1.6.6.2 RDPROJECT-379 zza 2013-10-24 end

}
