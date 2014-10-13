package com.p2psys.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.context.Constant;
import com.p2psys.dao.TenderDao;
import com.p2psys.domain.Tender;
import com.p2psys.domain.TenderAwardYesAndNo;
import com.p2psys.model.BorrowNTender;
import com.p2psys.model.BorrowTender;
import com.p2psys.model.DetailTender;
import com.p2psys.model.ProtocolBorrowTender;
import com.p2psys.model.RankModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.StringUtils;

/**
 * 获取每个借款的tender列表
 
 * @date 2012-7-17-下午2:38:00
 * @version  
 *
 *  (c)</b> 2012-51-<br/>
 *
 */
public class TenderDaoImpl extends BaseDaoImpl implements TenderDao {
	
	private static Logger logger = Logger.getLogger(TenderDaoImpl.class);
	
	private String queryAllTenderSql="SELECT p1.*,p2.username," +
			"p3.name as borrowname FROM dw_borrow_tender AS p1" +
			" LEFT JOIN dw_user AS p2 ON p1.user_id=p2.user_id " +
			"LEFT JOIN dw_borrow AS p3 ON p1.borrow_id=p3.id WHERE 1=1 ";

	private String queryAllTenderCountSql="SELECT count(1) FROM dw_borrow_tender AS p1" +
			" LEFT JOIN dw_user AS p2 ON p1.user_id=p2.user_id " +
			"LEFT JOIN dw_borrow AS p3 ON p1.borrow_id=p3.id WHERE 1=1 ";
	/*String queryTenderByBorrowidSql="select t.*,u.username from dw_borrow_tender t " +
			"left join dw_user u on u.user_id=t.user_id where t.borrow_id=? ";*/
	
	// V1.6.6.2 RDPROJECT-353 ljd 2013-10-21 start
	/*private String queryTenderByBorrowidSql="select t.*,collection.repay_time as repay_time,collection.repay_account as repay_account,u.username,borrow.part_account AS part_account from dw_borrow_tender t " +
			"left join dw_borrow_collection collection on t.id=collection.tender_id " +
			"left join dw_user u on u.user_id=t.user_id " +
			"left join dw_borrow borrow on t.borrow_id = borrow.id where t.borrow_id=? ";*/
	
	private String queryTenderByBorrowidSql="select t.*,collection.repay_time as repay_time,collection.repay_yestime as repay_yestime,collection.repay_account as repay_account,u.username,borrow.part_account AS part_account from dw_borrow_tender t " +
			"left join dw_borrow_collection collection on t.id=collection.tender_id " +
			"left join dw_user u on u.user_id=t.user_id " +
			"left join dw_borrow borrow on t.borrow_id = borrow.id where t.borrow_id=? ";
	// V1.6.6.2 RDPROJECT-353 ljd 2013-10-21 end
	
	// 爱贷协议自处理 协议只针对流转标   v1.6.6.2 RDPROJECT-82  2013-10-28 gc start
	private String queryProtocolTenderByBorrowidSql="select t.*,collection.repay_time as repay_time,collection.order as c_order,borrow.verify_time as verify_time,u.username as username,collection.repay_account as repay_account,borrow.time_limit as time_limit,borrow.apr as apr" +
			" from dw_borrow_tender t " +
			"left join dw_borrow_collection collection on t.id=collection.tender_id " +
			"left join dw_user u on u.user_id=t.user_id " +
			"left join dw_borrow borrow on t.borrow_id = borrow.id where t.id=? ";
	// 爱贷协议自处理 协议只针对流转标   v1.6.6.2 RDPROJECT-82  2013-10-28 gc end
	
	private String add="select t.*,u.username from dw_borrow_tender t " +
			"left join dw_user u on u.user_id=t.user_id left join dw_borrow b on b.user_id=u.id where t.borrow_id=? ";
	public List<BorrowTender> getTenderListByBorrowid(long id) {
	/*	String queryTenderByBorrowidSql="select t.*,u.username from dw_borrow_tender t " +
				"left join dw_user u on u.user_id=t.user_id where t.borrow_id=? ";*/
		String groupSql=" GROUP BY t.id ";
		String sql=queryTenderByBorrowidSql+groupSql+" order by t.addtime desc";
		logger.debug("SQL:"+sql);
		List tenders =null;
		try {
			tenders=this.getJdbcTemplate().query(sql,
			        new Object[]{id},
			        getBeanMapper(BorrowTender.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return tenders ;
	}
	//山水聚宝活动时间内累计投资总金额
	@Override
	public double getTenderListByUserid(long user_id,String starttime,String endtime) {
			String queryTenderByBorrowidSql="select sum(t.money) as money from dw_borrow_tender t " +
					"left join dw_user u on u.user_id=t.user_id " +
					"left join dw_borrow b on b.id=t.borrow_id " +
					"where t.user_id=? and t.addtime>=? and t.addtime<=? and (b.type="+Constant.TYPE_FLOW+" or b.type="+Constant.TYPE_MORTGAGE+" or b.type="+Constant.TYPE_CREDIT+" ) and b.isday!=1";
			String groupSql=" GROUP BY t.id ";
			String sql=queryTenderByBorrowidSql+groupSql+" order by t.addtime desc";
			logger.debug("SQL:"+sql);
			double total=0.0;
			SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql, new Object[]{user_id,starttime,endtime});
			if(rs.next()){
				total=rs.getDouble("money");
			}
			return total;
		}
	// v1.6.7.2 RDPROJECT-580 lhm 2013-12-11 start
//	public List getTenderListByBorrowid(long id,int num) {
//		/*String queryTenderByBorrowidSql="select t.*,u.username from dw_borrow_tender t " +
//				"left join dw_user u on u.user_id=t.user_id where t.borrow_id=? ";*/
//		logger.debug("SQL:"+queryTenderByBorrowidSql);
//		logger.debug("SQL:"+id);
//		String groupSql=" GROUP BY t.id ";
//		String sql=queryTenderByBorrowidSql+groupSql+" order by t.addtime desc"+" limit 0,?";
//		List tenders =null;
//		try {
//			tenders=this.getJdbcTemplate().query(sql,
//			        new Object[]{id,num},
//			        getBeanMapper(BorrowTender.class));
//		} catch (DataAccessException e) {
//			logger.debug("数据库查询结果为空！");
//			e.printStackTrace();
//		}
//		return tenders ;
//	}
	// v1.6.7.2 RDPROJECT-580 lhm 2013-12-11 end
	public List getTenderListByBorrow(long id) {
		logger.debug("SQL:"+queryTenderByBorrowidSql);
		logger.debug("SQL:"+id);
		String groupSql=" GROUP BY t.id ";
		List tenders =null;
		String querystring=queryTenderByBorrowidSql+groupSql;
		try {
			tenders=this.getJdbcTemplate().query(querystring,
			        new Object[]{id},
			        getBeanMapper(BorrowTender.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return tenders ;
	}
	
	@Override
	public List getTenderListByBorrow(long id, long user_id) {
		logger.debug("SQL:"+queryTenderByBorrowidSql);
		logger.debug("SQL:"+id);
		String groupSql=" GROUP BY t.id ";
		List tenders =null;
		String querystring=queryTenderByBorrowidSql+" and t.user_id=?"+groupSql;
		try {
			tenders=this.getJdbcTemplate().query(querystring,
			        new Object[]{id,user_id},
			        getBeanMapper(BorrowTender.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return tenders ;
	}
	@Override
	public List getTenderListByBorrowid(long id, int start, int pernum, SearchParam param) {
		String searchSql = param.getSearchParamSql();
		String limitSql = " limit " + start + "," + pernum;
		String orderSql = param.getOrderSql();
		String groupSql = " GROUP BY t.id ";
		String sql = queryTenderByBorrowidSql + searchSql + groupSql + orderSql + limitSql;
		logger.debug("SQL:" + queryTenderByBorrowidSql);
		logger.debug("SQL:" + id);
		List tenders = null;
		try {
			tenders = this.getJdbcTemplate().query(sql, new Object[] { id }, getBeanMapper(BorrowTender.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return tenders;
	}

	String queryTenderCountByBorrowidSql="select count(1) from dw_borrow_tender t " +
			"left join dw_user u on u.user_id=t.user_id where t.borrow_id=? ";
	
	@Override
	public int getTenderCountByBorrowid(long id, SearchParam param) {
		String searchSql=param.getSearchParamSql();
		String sql=queryTenderCountByBorrowidSql+searchSql;
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+id);
		int total=0;
		try {
			total=count(sql,new Object[]{id});
		} catch (DataAccessException e) {
			logger.error("getTenderCountByBorrowid():"+e.getMessage());
		}
		return total ;
	}

	@Override
	public Tender addTender(Tender t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into dw_borrow_tender(user_id,status,borrow_id,money,account,addtime,auto_repurchase,is_auto_tender)" +
							"values(?,?,?,?,?,?,?,?)";
		final Tender tender=t;
		/*int ret=this.getJdbcTemplate().update(sql,t.getUser_id(),t.getStatus(),t.getBorrow_id(),t.getMoney(),t.getAccount(),t.getAddtime());
		if(ret<1) 
			return null;*/
		this.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, tender.getUser_id());
				ps.setInt(2, tender.getStatus());
				ps.setLong(3, tender.getBorrow_id());
				ps.setString(4, tender.getMoney());
				ps.setString(5, tender.getAccount());
				ps.setString(6, tender.getAddtime());
				ps.setInt(7, tender.getAuto_repurchase());
				ps.setInt(8, tender.getIs_auto_tender());
				return ps;
			}
		}, keyHolder);
		long key=keyHolder.getKey().longValue();
		t.setId(key);
		return t;
	}

	@Override
	public Tender modifyTender(Tender tender) {
		String sql = "update dw_borrow_tender set repayment_account=?,wait_account =?," +
				"interest =?,wait_interest = ?, status= ?,repayment_yesinterest=? where id=?";
		int ret = this.getJdbcTemplate().update(sql, tender.getRepayment_account(), tender.getWait_account(),
				tender.getInterest(), tender.getWait_interest(), tender.getStatus(),tender.getRepayment_yesinterest(), tender.getId());
		if(ret<1) 
			return null;
		return tender;
	}

	// v1.6.7.2 RDPROJECT-529 lx 2013-12-17 start
	public void updateTenderStatusByBorrowId(long borrow_id,int status){
		String sql = "update dw_borrow_tender set status=:status where borrow_id=:borrow_id";
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("status", status);
    	map.put("borrow_id", borrow_id);
    	logger.debug("updateTenderStatusByBorrowId:"+sql.toString());
        this.getNamedParameterJdbcTemplate().update(sql.toString(), map);
	}
	// v1.6.7.2 RDPROJECT-529 lx 2013-12-17 end
	@Override
	public List getInvestTenderListByUserid(long user_id) {
		String sql="select p1.*,p1.account as tender_account,p1.money as tender_money,p2.user_id as borrow_userid,p2.username as op_username,p4.username as username," +
				"p3.apr,p3.time_limit,p3.time_limit_day,p3.isday,p3.name as borrow_name,p3.id as borrow_id," +
				"p3.account as borrow_account ,p3.account_yes as borrow_account_yes,p3.verify_time,p5.value as credit_jifen,p6.pic as credit_pic from dw_borrow_tender as p1 " +
				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
				"left join dw_user as p2 on p3.user_id = p2.user_id " +
				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
				"left join dw_credit as p5 on p3.user_id=p5.user_id " +
				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
				"where p1.user_id=? " +
				"order by p1.addtime desc;";
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(DetailTender.class));
		return list;
	}

//	@Override
//	public List getInvestTenderingListByUserid(long user_id) {
//		String sql="select p1.*,p1.account as tender_account,p1.money as tender_money,p2.user_id as borrow_userid,p2.username as op_username,p4.username as username," +
//				"p3.apr,p3.time_limit,p3.time_limit_day,p3.isday,p3.name as borrow_name,p3.id as borrow_id," +
//				"p3.account as borrow_account ,p3.account_yes as borrow_account_yes,p3.verify_time,p5.value as credit_jifen,p6.pic as credit_pic from dw_borrow_tender as p1 " +
//				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
//				"left join dw_user as p2 on p3.user_id = p2.user_id " +
//				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
//				"left join dw_credit as p5 on p3.user_id=p5.user_id " +
//				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
//				"where p1.user_id=? and p3.status=1 " +
//				"order by p1.addtime desc;";
//		logger.debug("SQL:"+sql);
//		logger.debug("SQL:"+user_id);
//		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(DetailTender.class));
//		return list;
//	}

	@Override
	public List getInvestTenderListByUserid(long user_id, int start, int end,
			SearchParam param) {
		String sql="select p1.*,p1.account as tender_account,p1.money as tender_money,p2.user_id as borrow_userid,p2.username as op_username,p4.username as username," +
				"p3.apr,p3.time_limit,p3.time_limit_day,p3.isday,p3.name as borrow_name,p3.id as borrow_id," +
				"p3.account as borrow_account ,p3.account_yes as borrow_account_yes,p3.verify_time,p5.value as credit_jifen,p6.pic as credit_pic from dw_borrow_tender as p1 " +
				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
				"left join dw_user as p2 on p3.user_id = p2.user_id " +
				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
				"left join dw_credit as p5 on p1.user_id=p5.user_id " +
				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
				"where p1.user_id=? "+
				" and ((p3.status =1 and p3.type="+Constant.TYPE_FLOW+") or p3.status in (3,6,7,8)) ";
		String orderSql="order by p1.addtime desc";
		String searchSql=param.getSearchParamSql();
		String limitSql=" limit "+start+","+end;
		sql=sql+searchSql+orderSql+limitSql;
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(DetailTender.class));
		return list;
	}

	@Override
	public int getInvestTenderCountByUserid(long user_id, SearchParam param) {
		String sql="select count(p1.id) from dw_borrow_tender as p1 " +
				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
				"left join dw_user as p2 on p3.user_id = p2.user_id " +
				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
				"left join dw_credit as p5 on p1.user_id=p5.user_id " +
				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
				"where p1.user_id=? " +
				" and ((p3.status =1 and p3.type="+Constant.TYPE_FLOW+") or p3.status in (3,6,7,8)) ";
		String searchSql=param.getSearchParamSql();
		sql=sql+searchSql;
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		int total=0;
		total=count(sql, new Object[]{user_id});
		return total;
	}
	
	@Override
	public List getInvestTenderingListByUserid(long user_id, int start,
			int end, SearchParam param) {
		String sql="select p1.*,p1.account as tender_account,p1.money as tender_money,p2.user_id as borrow_userid,p2.username as op_username,p4.username as username," +
				"p3.apr,p3.time_limit,p3.time_limit_day,p3.isday,p3.name as borrow_name,p3.id as borrow_id," +
				"p3.account as borrow_account ,p3.account_yes as borrow_account_yes,p3.verify_time,p5.value as credit_jifen,p6.pic as credit_pic from dw_borrow_tender as p1 " +
				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
				"left join dw_user as p2 on p3.user_id = p2.user_id " +
				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
				"left join dw_credit as p5 on p3.user_id=p5.user_id " +
				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
				"where p1.user_id=? and p3.status=1 " ;
		String orderSql="order by p1.addtime desc";
		String searchSql=param.getSearchParamSql();
		String limitSql=" limit ?,?";
		sql=sql+searchSql+orderSql+limitSql;
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql, new Object[]{user_id,start,end}, getBeanMapper(DetailTender.class));
		return list;
	}

	@Override
	public int getInvestTenderingCountByUserid(long user_id, SearchParam param) {
		String sql="select count(1) from dw_borrow_tender as p1 " +
				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
				"left join dw_user as p2 on p3.user_id = p2.user_id " +
				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
				"left join dw_credit as p5 on p3.user_id=p5.user_id " +
				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
				"where p1.user_id=? and p3.status=1 " ;
		String searchSql=param.getSearchParamSql();
		sql=sql+searchSql;
		logger.debug("SQL:"+sql);
		logger.debug("SQL:"+user_id);
		int total=0;
		total=this.count(sql, user_id);
		return total;
	}

//	@Override
//	public List getSuccessTenderList(long user_id) {
//		String sql="select p1.*,p1.account as tender_account,p1.money as tender_money,p2.user_id as borrow_userid,p2.username as op_username,p4.username as username," +
//				"p3.apr,p3.time_limit,p3.time_limit_day,p3.isday,p3.name as borrow_name,p3.id as borrow_id," +
//				"p3.account as borrow_account ,p3.account_yes as borrow_account_yes,p3.verify_time,p5.value as credit_jifen,p6.pic as credit_pic " +
//				"from dw_borrow_tender as p1 " +
//				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
//				"left join dw_user as p2 on p3.user_id = p2.user_id " +
//				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
//				"left join dw_credit as p5 on p1.user_id=p5.user_id " +
//				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
//				"where p3.status in (3,6,7) and p1.user_id=? " +
//				"order by p1.addtime desc;";
//		logger.debug("SQL:"+sql);
//		logger.debug("SQL:"+user_id);
//		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(DetailTender.class));
//		return list;
//	}
	
//	@Override
//	public List getSuccessTenderList(long user_id, int start, int end,
//			SearchParam param) {
//		String sql="select p1.*,p1.account as tender_account,p1.money as tender_money,p2.user_id as borrow_userid,p2.username as op_username,p4.username as username," +
//				"p3.apr,p3.time_limit,p3.time_limit_day,p3.isday,p3.name as borrow_name,p3.id as borrow_id," +
//				"p3.account as borrow_account ,p3.account_yes as borrow_account_yes,p3.verify_time,p5.value as credit_jifen,p6.pic as credit_pic from dw_borrow_tender as p1 " +
//				"left join dw_borrow as p3 on p1.borrow_id=p3.id " +
//				"left join dw_user as p2 on p3.user_id = p2.user_id " +
//				"left join dw_user as p4 on p4.user_id = p1.user_id " + 
//				"left join dw_credit as p5 on p1.user_id=p5.user_id " +
//				"left join dw_credit_rank as p6 on p5.value<=p6.point2  and p5.value>=p6.point1 " +
//				"where p1.user_id=? ";
//		String orderSql="order by p1.addtime desc";
//		String searchSql=param.getSearchParamSql();
//		String limitSql=" limit "+start+","+end;
//		sql=sql+searchSql+orderSql+limitSql;
//		logger.debug("SQL:"+sql);
//		logger.debug("SQL:"+user_id);
//		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(DetailTender.class));
//		return list;
//	}
	

//	@Override
//	public double getTotalTenderByUserid(long userid) {
//		String sql="select sum(account) as total from dw_borrow_tender where status=1 and user_id=?";
//		double total=0.0;
//		SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql, new Object[]{userid});
//		if(rs.next()){
//			total=rs.getDouble("total");
//		}
//		return total;
//	}

//	@Override
//	public int getTotalTenderTimesByUserid(long userid) {
//		String sql="select count(*) from dw_borrow_tender where status=1 and user_id=?";
//		int times=0;
//		try {
//			times=this.getJdbcTemplate().queryForInt(sql,new Object[]{userid});
//		} catch (DataAccessException e) {
//			logger.debug("getTotalTenderByUserid():"+e.getMessage());
//		}
//		return times;
//	}
	@Override
	public int getRankTotalByTime(SearchParam param) {
		String selSql = "select count(*) from (select u.user_id,u.username,u.realname,sum(p1.account) as tenderMoney from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
				" left join dw_user u on u.user_id=p1.user_id where ((b.status in(3,6,7,8) and b.type<>"+Constant.TYPE_FLOW+") or (b.status in(1,3,6,7,8) and b.type="+Constant.TYPE_FLOW+"))";
		String paramSql =" group by u.username) as num";
		StringBuffer sb = new StringBuffer();
		String searchSql = param.getSearchParamSql();
		sb.append(selSql).append(searchSql).append(paramSql);
		String sql = sb.toString();
		int total = 0;
		try {
			total = this.getJdbcTemplate().queryForInt(sql);
		} catch (Exception e) {
			logger.debug("getRankTotalByTime():"+e.getMessage());
		}
		return total;
	}
	
	@Override
	public List<RankModel> getRankListByTime(int start, int pernum, SearchParam param) {
		String selSql = "select u.user_id,u.username,u.realname,sum(p1.account) as tenderMoney from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
						" left join dw_user u on u.user_id=p1.user_id where ((b.status in(3,6,7,8) and b.type<>"+Constant.TYPE_FLOW+") or (b.status in(1,3,6,7,8) and b.type="+Constant.TYPE_FLOW+"))";
		String paramSql =" group by u.username order by tenderMoney desc";
		StringBuffer sb = new StringBuffer();
		String searchSql = param.getSearchParamSql();
		String limitSql = " limit ?,? ";
		sb.append(selSql).append(searchSql).append(paramSql).append(limitSql);
		String sql = sb.toString();
		List<RankModel> list = new ArrayList<RankModel>();
		list = this.getJdbcTemplate().query(sql, new Object[] { start, pernum }, getBeanMapper(RankModel.class));
		return list;
	}
	
	@Override
	public List<RankModel> getRankListByTime(SearchParam param) {

		String selSql = "select u.user_id,u.username,u.realname,sum(p1.account) as tenderMoney from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
						" left join dw_user u on u.user_id=p1.user_id where ((b.status in(3,6,7,8) and b.type<>"+Constant.TYPE_FLOW+") or (b.status in(1,3,6,7,8) and b.type="+Constant.TYPE_FLOW+"))";
		String paramSql =" group by u.username order by tenderMoney desc";
		StringBuffer sb = new StringBuffer();
		String searchSql = param.getSearchParamSql();
		sb.append(selSql).append(searchSql).append(paramSql);
		String sql = sb.toString();

		List<RankModel> list = new ArrayList<RankModel>();
		list = this.getJdbcTemplate().query(sql,  getBeanMapper(RankModel.class));
		return list;
	}
	
	// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 start
	@Override
	public List<RankModel> getRankListByTime(String startTime, String endTime, int num, String init) {
		String selSql = "select u.username,sum(p1.account) as tenderMoney from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
						" left join dw_user u on u.user_id=p1.user_id where ";
		String paramSql =" group by u.username order by tenderMoney desc";
		String conditSql = "((b.status in(3,6,7,8) and b.type<>"+Constant.TYPE_FLOW+") or (b.status in(1,3,6,7,8) and b.type="+Constant.TYPE_FLOW+"))";
		StringBuffer sb = new StringBuffer(selSql);
		if (StringUtils.isBlank(init)) {
			sb.append(conditSql);
		} else {
			sb.append(init);
		}
		sb.append(" and p1.addtime>=? and p1.addtime<=? ").append(paramSql).append(" limit 0, " + num);
		// v1.6.7.2 RDPROJECT-567 zza 2013-12-18 end
		String sql = sb.toString();
		List<RankModel> list = new ArrayList<RankModel>();
		list = this.getJdbcTemplate().query(sql, new Object[]{startTime,endTime},getBeanMapper(RankModel.class));
		return list;
	}
	
	@Override
	public double countTenderAccount(String startTime,String endTime) {

		String selSql = "select sum(p1.account) as num from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
						"  where ((b.status in(3,6,7,8) and b.type<>" + Constant.TYPE_FLOW + ") or (b.status in(1,3,6,7,8) and b.type=" + Constant.TYPE_FLOW + "))";
		StringBuffer sb = new StringBuffer();
		sb.append(selSql).append(" and p1.addtime>=? and p1.addtime<=? ");
		String sql=sb.toString();
		double sumAccount=0.0;
		sumAccount = sum(sql, new Object[]{startTime,endTime});
		return sumAccount;
	}
	
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 start
	@Override
	public List getMoreRankListByTime(String startTime,String endTime,int num,String init) {

		String selSql = "select u.username,sum(p1.account) as tenderMoney,COUNT(p1.id) AS tenderCount,MAX(p1.addtime) AS lastTenderTime from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
						" left join dw_user u on u.user_id=p1.user_id where ";
		String paramSql =" group by u.username order by tenderMoney desc";
		StringBuffer sb = new StringBuffer();
		sb.append(selSql).append(init).append(" and p1.addtime>=? and p1.addtime<=? ").append(paramSql).append(" limit 0,").append(num);
		String sql=sb.toString();

		List<RankModel> list = new ArrayList<RankModel>();
		list = this.getJdbcTemplate().query(sql, new Object[]{startTime,endTime},getBeanMapper(RankModel.class));
		return list;
	}
	//v1.6.6.1 RDPROJECT-209 yl 2013-09-27 end
	
	// v1.6.7.1 RDPROJECT-355 zza 2013-11-08 start 
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getRankList() {
		// 德赛投资排行榜（天标超过30天也算在投资排行榜里）
		String selSql = "select p3.username ,sum(p1.account) as tenderMoney from dw_borrow_tender p1"
				+ " left join dw_borrow p2 on p2.id=p1.borrow_id"
				+ " left join dw_user p3 on p3.user_id=p1.user_id"
				+ " where ";
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.MONTH_RANK.getValue()));
		int day = rule.getValueIntByKey("day");
		String daySql = null;
		if (rule != null && rule.getStatus() == 1) { 
			daySql = "(p2.isday <>1 or p2.time_limit_day >= " + day + ")";
		} else {
			daySql = "p2.isday<>1";
		}
		String conditSql = "and ((p2.type=" + Constant.TYPE_MORTGAGE
				+ " and p2.status in (3,6,7,8)) or (p2.type=" + Constant.TYPE_FLOW
				+ " and p2.status in (1,3,6,7,8)))";
		String groupSql = " group by p3.username order by tenderMoney desc";
		StringBuffer sb = new StringBuffer();
		sb.append(selSql).append(daySql).append(conditSql).append(groupSql).append(" limit 0,50");
		String sql = sb.toString();
		logger.debug("getRankList():" + sql);
		List<RankModel> list = new ArrayList<RankModel>();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(RankModel.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return list;
	}*/
	// v1.6.7.1 RDPROJECT-355 zza 2013-11-08 end
	
	@Override
	public List getAllRankList() {
		String selSql = "select p3.username ,sum(p1.account) as tenderMoney from dw_borrow_tender p1 left join dw_borrow p2 on p2.id=p1.borrow_id" +
				" left join dw_user p3 on p3.user_id=p1.user_id where p2.isday<>1 and ((p2.type="+Constant.TYPE_MORTGAGE+" and p2.status in (3,6,7,8)) or (p2.type="+Constant.TYPE_FLOW+" and p2.status in (1,3,6,7,8)))";
		String groupSql = " group by p3.username order by tenderMoney desc";
		StringBuffer sb = new StringBuffer();
		sb.append(selSql).append(groupSql);
		String sql =sb.toString();
		logger.debug("getRankList():" + sql);
		List<RankModel> list = new ArrayList<RankModel>();
		list = this.getJdbcTemplate().query(sql, new Object[]{},new RowMapper<RankModel>(){
			@Override
			public RankModel mapRow(ResultSet rs, int num)
					throws SQLException {
				RankModel r=new RankModel();
				r.setUsername(rs.getString("username"));
				r.setTenderMoney(rs.getDouble("tenderMoney"));
				return r;
			}
		});
		return list;
	}
	String secsql="SELECT a.* FROM dw_borrow_tender a inner JOIN dw_user c ON a.user_id=c.user_id inner JOIN dw_borrow b ON a.borrow_id=b.id ";
//	@Override
//	public List getTenderList(){
//		long start=System.currentTimeMillis();
//		String selectsql=" and ((b.status in (3,6,7,8) and b.type!="+Constant.TYPE_FLOW+" ) or (b.status in(1,3,6,7,8) and b.type="+Constant.TYPE_FLOW+")) ";
//		StringBuffer sb=new StringBuffer(secsql);
//		sb.append(selectsql);
//		String scsql=sb.toString();
//		logger.debug("getTenderList():"+scsql);
//		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(scsql,getBeanMapper(BorrowTender.class));
//		long end=System.currentTimeMillis();
//		logger.info("GetTenderList Cost Time:"+(end-start));
//		return list;
//	}
	@Override
	public List getNewTenderList(){
		String selectsql=" and b.status in(1,3) order by a.addtime desc limit 0,20";
		StringBuffer sb=new StringBuffer();
		sb.append(secsql).append(selectsql);
		String scsql=sb.toString();
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(scsql,getBeanMapper(BorrowTender.class));
		return list;
	}
	@Override
	public int getAllTenderCount(SearchParam param) {
		String searchSql=param.getSearchParamSql();
		String sql=queryAllTenderCountSql+searchSql;
		logger.debug("SQL:"+sql);
		int total=0;
		total=count(sql);
		return total;
	}
	@Override
	public List getAllTenderList(int start,int pernum, SearchParam param) {
		String searchSql=param.getSearchParamSql();
		String limitSql=" limit "+start+","+pernum;
		String orderSql=param.getOrderSql();
		String sql=queryAllTenderSql+searchSql+orderSql+limitSql;
		logger.debug("SQL:"+queryAllTenderSql);
		List list=getJdbcTemplate().query(sql, getBeanMapper(BorrowNTender.class));
		return list ;
	}
	@Override
	public List getAllTenderList(SearchParam param) {
		String searchSql=param.getSearchParamSql();
		String orderSql=param.getOrderSql();
		String sql=queryAllTenderSql+searchSql+orderSql;
		logger.debug("SQL:"+queryAllTenderSql);
		List list=getJdbcTemplate().query(sql, getBeanMapper(BorrowNTender.class));
		return list ;
	}
	@Override
	public void updateTenderAwardYesAndNo(TenderAwardYesAndNo tenderAwardYesAndNo){
		String sql="update dw_tender_award_yes_no set tender_award_yes_no=? where id=?";
		this.getJdbcTemplate().update(sql,tenderAwardYesAndNo.getTender_award_yes_no(),tenderAwardYesAndNo.getId());
	}
	@Override
	public TenderAwardYesAndNo tenderAwardYesAndNo(int id){
		String sql="select * from dw_tender_award_yes_no where id=?";
		TenderAwardYesAndNo tenderAwardYesAndNo=new TenderAwardYesAndNo();
		try {
			tenderAwardYesAndNo = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { id }, new RowMapper<TenderAwardYesAndNo>(){
				@Override
				public TenderAwardYesAndNo mapRow(ResultSet rs, int num)
						throws SQLException {
					TenderAwardYesAndNo r=new TenderAwardYesAndNo();
					r.setId(rs.getInt("id"));
					r.setTender_award_yes_no(rs.getInt("tender_award_yes_no"));
					return r;
				}
			});
		} catch (DataAccessException e) {
			logger.debug("tenderAwardYesAndNo============ " + e.getMessage());
		}
		return tenderAwardYesAndNo;
	}
	@Override
	public List allTenderListBybid(long borrow_id){
		String sql="SELECT * FROM dw_borrow_tender where borrow_id=?";
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql,getBeanMapper(BorrowTender.class),borrow_id);
		return list;
		
	}
	@Override
	public Tender getTenderById(long tid) {
		String sql="SELECT * FROM dw_borrow_tender where id=?";
		Tender t=null;
		try {
			t=getJdbcTemplate().queryForObject(sql,getBeanMapper(BorrowTender.class),tid);
		} catch (DataAccessException e) {
		}
		return t;
	}
	@Override
	public void modifyRepayTender(double capital, double interest, long id) {
		String sql="update dw_borrow_tender set repayment_yesaccount=repayment_yesaccount+?," +
				"repayment_yesinterest =repayment_yesinterest+?," +
				"wait_account =wait_account-?,wait_interest =wait_interest-? where id=?";
		this.getJdbcTemplate().update(sql,capital,interest,
				capital,interest,id);
	}
	//自动投标记录
	@Override
	public int getAutoTenderByUserid(long userid,long is_auto_tender,long borrow_id,long status) {
		String sql="select count(*) from dw_borrow_tender p1 left join dw_borrow p2 on p2.id=p1.borrow_id where p2.status=? and p1.user_id=? and p1.borrow_id=? and p1.is_auto_tender=? ";
		int times=0;
		try {
			times=this.getJdbcTemplate().queryForInt(sql,new Object[]{status,userid,borrow_id,is_auto_tender});
		} catch (DataAccessException e) {
			logger.debug("getTotalTenderByUserid():"+e.getMessage());
		}
		return times;
	}
	@Override
	public Tender getFirstNoRepayTenderByBorrow(long borrow_id) {
		String sql="select * from dw_borrow_tender where borrow_id=? and wait_account>0 limit 0,1";
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql,getBeanMapper(BorrowTender.class),borrow_id);
		if(list==null||list.size()<1) return null;
		return (Tender)list.get(0);
	}
	
	@Override
	public List getProtocolTenderListByBorrowid(long id) {
		String groupSql=" GROUP BY total.id ";
		String sql= "select * from (" + queryProtocolTenderByBorrowidSql + " order by repay_time desc) as total " + groupSql ;
		logger.debug("SQL:"+sql);
		List tenders =null;
		try {
			tenders=this.getJdbcTemplate().query(sql,
			        new Object[]{id},
			        getBeanMapper(ProtocolBorrowTender.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return tenders ;
	}
	
	//RDPROJECT-282 fxx 2013-10-18 start
	@Override
	public double countTenderAccountByUserid(long userid,long start,long end,String extSql) {
		long s=System.currentTimeMillis();
		String selSql = "select sum(p1.account) as num from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
						"  where 1=1 and p1.user_id=? ";
		StringBuffer sb = new StringBuffer();
		sb.append(selSql).append(" and p1.addtime>=? and p1.addtime<=? ");
		sb.append(extSql.replace("{alias}", "b"));
		String sql=sb.toString();
		logger.info("countTenderAccount:"+sql);
		double sumAccount=0.0;
		sumAccount = sum(sql, new Object[]{userid,start,end});
		long e=System.currentTimeMillis();
		logger.info("countTenderAccount cost:"+(e-s));
		return sumAccount;
	}
	//RDPROJECT-282 fxx 2013-10-18 end
	// v1.6.7.2 RDPROJECT-530 lx 2013-12-11 start
	public double sumTenderAccount(long user_id) {
		String selSql = "select sum(p1.account) as num from dw_borrow_tender p1 left join dw_borrow b on b.id=p1.borrow_id"+
						"  where ((b.status in(3,6,7,8) and b.type<>" + Constant.TYPE_FLOW + ") or (b.status in(1,8) and b.type=" + Constant.TYPE_FLOW + ")) and p1.user_id=:user_id";
		double sumAccount=0.0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		sumAccount= getNamedParameterJdbcTemplate().queryForInt(selSql.toString(),map);
		return sumAccount;
	}
	// v1.6.7.2 RDPROJECT-530 lx 2013-12-11 end
	
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
	@Override
	public int getTenderTimes(long borrow_id,long user_id) {
		String sql = "select count(1) from dw_borrow_tender where borrow_id = :borrow_id and user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("borrow_id", borrow_id);
		map.put("user_id", user_id);
		return getNamedParameterJdbcTemplate().queryForInt(sql, map);
	}
	//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
}
