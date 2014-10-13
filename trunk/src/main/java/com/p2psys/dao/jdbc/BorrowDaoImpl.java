package com.p2psys.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.BorrowDao;
import com.p2psys.domain.Advanced;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.CreditRank;
import com.p2psys.domain.FinancialData;
import com.p2psys.domain.Reservation;
import com.p2psys.domain.RunBorrow;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.AccountTypeModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.model.borrow.ReservationModel;
import com.p2psys.model.invest.InvestBorrowModel;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;

/**
 * 借款相关的Dao操作类
 * 
 
 * @date 2012-7-10-下午3:07:56
 * @version
 * 
 *   (c)</b> 2012-51-<br/>
 * 
 */
public class BorrowDaoImpl extends BaseDaoImpl implements BorrowDao {

	private static Logger logger = Logger.getLogger(BorrowDaoImpl.class);
	/**
	 * 根据类型、排序、分页进行查找借款的数据
	 */
	public List getList(BorrowModel model) {
		// v1.6.7.2 我要投资页面取得的项目改成和首页的一致，减少关联表的个数 lhm 2013-12-20 start
//		String sql = "select  p1.*,p2.username,p8.name as user_area ,u.username as kefu_username,p2.qq," +
//				"ifnull(p3.value,0) as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales," +
//				"p7.name as usetypename,p2.realname "
//				+ "from dw_borrow as p1 "
//				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
//				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
//				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
//				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
//				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
//				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
//				+ "left join dw_linkage p7 on p1.use=p7.id and p7.type_id=19 "
//				+ "left join dw_area p8 on p2.province=p8.id "
//				+ "where 1=1 ";
		String sql = "select  p1.*,p2.username,p2.area as user_area,p2.qq," +
				"ifnull(p3.value,0) as credit_jifen,p2.area as add_area,p1.account_yes/p1.account as scales,p2.realname "
				+ "from dw_borrow as p1 "
				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
				+ "where 1=1 ";
		// v1.6.7.2 我要投资页面取得的项目改成和首页的一致，减少关联表的个数 lhm 2013-12-20 end
		// 拼装SQL
		String connectSql=connectSql(model);
		sql = sql + connectSql;
		
		List<UserBorrowModel> list = new ArrayList<UserBorrowModel>();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(UserBorrowModel.class));	
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		for(UserBorrowModel b:list){
			CreditRank cr=Global.getCreditRank(b.getCredit_jifen());
			if(cr!=null){
				b.setCredit_pic(cr.getPic());
			}
		}
		return list;
	}
//	public String getSql(String[] borrows){
//		String initSql="";
//		/*if(borrows.length> 0){
//			initSql+=" when p1.is_mb=1 then "+borrows[0]+"";
//		}
//		if(borrows.length> 1){
//			initSql+=" when p1.is_recommend=1 then "+borrows[1]+"";
//		}
//		if(borrows.length>2){
//			initSql+=" when p1.is_flow=1 then "+borrows[2]+"";
//		}
//		if(borrows.length> 3){
//			initSql+=" when p1.is_fast=1 then "+borrows[3]+"";
//		}
//		if(borrows.length> 4){
//			initSql+=" when p1.is_jin=1 then "+borrows[4]+"";
//		}
//		if(borrows.length> 5){
//			initSql+=" when p1.is_xin=1 then "+borrows[5]+"";
//		}
//		if(borrows.length> 6){
//			initSql+=" when p1.is_is_offvouch=1 then "+borrows[6]+"";
//		}*/
//		
//		if(borrows.length> 0){
//			initSql+=" when p1.type="+Constant.TYPE_SECOND+" then "+borrows[0]+"";
//		}
//		if(borrows.length> 1){
//			initSql+=" when p1.is_recommend=1 then "+borrows[1]+"";
//		}
//		if(borrows.length>2){
//			initSql+=" when p1.type="+Constant.TYPE_FLOW+" then "+borrows[2]+"";
//		}
//		if(borrows.length> 3){
//			initSql+=" when p1.type="+Constant.TYPE_MORTGAGE+" then "+borrows[3]+"";
//		}
//		if(borrows.length> 4){
//			initSql+=" when p1.type="+Constant.TYPE_PROPERTY+" then "+borrows[4]+"";
//		}
//		if(borrows.length> 5){
//			initSql+=" when p1.type="+Constant.TYPE_CREDIT+" then "+borrows[5]+"";
//		}
//		if(borrows.length> 6){
//			initSql+=" when p1.type="+Constant.TYPE_OFFVOUCH+" then "+borrows[6]+"";
//		}
//		return initSql;
//	}
//	/**
//	 * 根据标种类型、添加时间查找借款的数据
//	 */
//	public List getBorrowList(BorrowModel model,String[] borrows) {
//		String sql = "select  p1.*,p2.username,p8.name as user_area ,u.username as kefu_username,p2.qq," +
//				"p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales," +
//				"p7.name as usetypename,p2.realname";
//		String fromSql=" from dw_borrow as p1 "
//				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
//				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
//				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
//				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
//				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
//				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
//				+ "left join dw_linkage p7 on p1.use=p7.id and p7.type_id=19 "
//				+ "left join dw_area p8 on p2.province=p8.id "
//				+ "where 1=1 ";
//		 
//		// 拼装SQL
//		String connectSql=connectSql(model);
//		sql = sql+ fromSql + connectSql;
//		logger.debug("SQL:" + sql);
//		List<UserBorrowModel> list = new ArrayList<UserBorrowModel>();
//		try {
//			list = this.getJdbcTemplate().query(sql, getBeanMapper(UserBorrowModel.class));
//		} catch (DataAccessException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	@Override
	public int count(BorrowModel model) {
		int total = 0;
		String sql = "select count(1) "
				+ "from dw_borrow as p1 "
				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
//				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
//				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
//				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
//				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
				// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 start 
				//+ "left join dw_daizi as p6 on p1.id=p6.borrow_id "
				// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 end
//				+ "left join dw_linkage p7 on p1.use=p7.id and p7.type_id=19 "
//				+ "left join dw_area p8 on p2.province=p8.id "
				+ "where 1=1 ";
		logger.debug("SQL:" + sql);
		String connectSql=this.connectCountSql(model);
		// 拼装SQL
		sql = sql + connectSql;
		logger.debug("SQL:" + sql);
		try {
			total = this.getJdbcTemplate().queryForInt(sql);
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}
	
	public List getIndexList(BorrowModel model) {
		String sql = "select  p1.*,p2.username,p2.area as user_area,p2.qq," +
				"ifnull(p3.value,0) as credit_jifen,p2.area as add_area,p1.account_yes/p1.account as scales,p2.realname "
				+ "from dw_borrow as p1 "
				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
				+ "where 1=1 ";
		// 拼装SQL
		String connectSql=connectSql(model);
		sql = sql + connectSql;
		List<UserBorrowModel> list = new ArrayList<UserBorrowModel>();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(UserBorrowModel.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
		}
		for(UserBorrowModel b:list){
			CreditRank cr=Global.getCreditRank(b.getCredit_jifen());
			if(cr!=null){
				b.setCredit_pic(cr.getPic());
			}
		}
		return list;
	}
	@Override
	public int countIndex(BorrowModel model) {
		int total = 0;
		String sql = "select count(1) "
				+ "from dw_borrow as p1 "
				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
				+ "where 1=1 ";
		logger.debug("SQL:" + sql);
		String connectSql=this.connectCountSql(model);
		// 拼装SQL
		sql = sql + connectSql;
		logger.debug("SQL:" + sql);
		try {
			total = this.getJdbcTemplate().queryForInt(sql);
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	/*@Override
	public List getListWithSimple(BorrowModel model) {
		String sql = "select  p1.id,p1.user_id,p1.name,p1.status,p1.verify_user,verify_time,verify_remark," +
				"p1.`use`,p1.time_limit,p1.style,p1.account,p1.account_yes,p1.tender_times,p1.apr," +
				"p1.lowest_account,p1.most_account,p1.valid_time,p1.award,p1.part_account,p1.funds,p1.addtime," +
				"p1.is_mb,p1.is_fast,p1.is_jin,p1.pwd,p1.isday,p1.time_limit_day,p1.is_xin,p1.is_flow,p1.flow_count," +
				"p1.flow_yescount,p1.flow_money,p1.is_student,p1.is_offvouch,p1.is_donation,p1.is_pledge,p1.is_recommend "
				+ " from dw_borrow as p1 "
				+ " left join dw_user as p2 on p1.user_id=p2.user_id "
				+ " where 1=1 ";
		// 拼装SQL
		String connectSql=connectSql(model);
		sql = sql + connectSql;
		logger.debug("SQL:" + sql);
		List<UserBorrowModel> list = new ArrayList<UserBorrowModel>();
		try {
			list = this.getJdbcTemplate().query(sql,  getBeanMapper(UserBorrowModel.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
		}
		//查询用户名，积分等信息
		Map<Long,UserBorrowModel> map=new HashMap<Long,UserBorrowModel>();
		String getUserSql="select p2.username,p2.realname,p3.value as credit_jifen,p4.pic as credit_pic from dw_user p2 " +
				"left join dw_credit p3 on p2.user_id=p3.user_id "+
				"left join dw_credit_rank p4 on p3.value<=p4.point2  and p3.value>=p4.point1 " +
				"where p2.user_id=?";
		logger.debug(getUserSql);
		for(UserBorrowModel m:list){
			//如果同一个用户不进行
			UserBorrowModel cacheModel=null;
			if(map.containsKey(m.getUser_id())){
				cacheModel=map.get(m.getUser_id());
			}else{
				cacheModel=getJdbcTemplate().queryForObject(getUserSql, new Object[]{m.getUser_id()}, new SimpleUserMapper());
			}
			m.setCredit_jifen(cacheModel.getCredit_jifen());
			m.setCredit_pic(cacheModel.getCredit_pic());
			m.setUsername(cacheModel.getUsername());
			m.setRealname(cacheModel.getRealname());
			m.setScales(m.getAccount_yes()/m.getAccount());
		}
		return list;
	}*/
//	/**
//	 * 单表查询
//	 * @param model
//	 * @return
//	 */
//	@Override
//	public int countWithSimple(BorrowModel model) {
//		int total = 0;
//		String sql = "select count(1) "
//				+ "from dw_borrow as p1 "
//				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
//				+ "where 1=1 ";
//		logger.debug("SQL:" + sql);
//		String connectSql=this.connectCountSql(model);
//		// 拼装SQL
//		sql = sql + connectSql;
//		logger.debug("SQL:" + sql);
//		try {
//			total = this.getJdbcTemplate().queryForInt(sql);
//		} catch (DataAccessException e) {
//			logger.debug("数据库查询结果为空！");
//		}
//		return total;
//	}
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	/**
//	 * 实时财务
//	 */
//	@Override
//	public List getBorrowList(String type, int start,int end, SearchParam param) {
//		String sql = "SELECT p3.username,p2.repayment_account AS repayment_account,p2.repayment_time AS repayment_time,p2.repayment_yestime AS repayment_yestime ,p2.borrow_id AS borrow_id FROM dw_borrow p1 " +
//				"LEFT JOIN dw_borrow_repayment AS p2 ON p1.id=p2.borrow_id " +
//				"LEFT JOIN dw_user AS p3 ON p1.user_id=p3.user_id WHERE 1=1 ";
//		String newsql = "SELECT p3.username,p2.*,p1.* FROM dw_borrow p1 LEFT JOIN dw_borrow_repayment AS p2 ON p1.id=p2.borrow_id " +
//				"left join dw_user as p3 on p1.user_id=p3.user_id " +
//				" where 1=1  ";	
//		String flowsql="SELECT collection.repay_account AS repayment_account,collection.repay_time AS repayment_time,collection.repay_yestime AS repayment_yestime,tender.borrow_id AS borrow_id  FROM dw_borrow_tender AS tender " +
//				"LEFT JOIN dw_borrow_collection AS collection ON tender.id=collection.tender_id " +
//				"LEFT JOIN dw_borrow AS b ON tender.borrow_id=b.id WHERE 1=1 ";
//		String typeSql = getBorrowTypeSql(type,sql,newsql,flowsql,0);
//		String flowTypeSql=getBorrowTypeSql(type, sql, newsql, flowsql, 1);
//		String searchSql=param.getSearchParamSql();
//		
//		String limitSql = " limit " + start + "," + end;
//		sql = typeSql+searchSql+limitSql;
//		flowsql = flowTypeSql+searchSql+limitSql;
//		// 拼装SQL
//		logger.debug("SQL:" + sql);
//		logger.debug("SQL:" + flowsql);
//		List list = new ArrayList();
//		try {
//			if("wait_repay".equals(type)){
//				list =getJdbcTemplate().query(sql, new Object[]{},new RowMapper<Repayment>(){
//					public Repayment mapRow(ResultSet rs, int rowNum)
//							throws SQLException {
//						Repayment ua1=new Repayment();
//						ua1.setRepayment_account(rs.getString("repayment_account"));
//						ua1.setRepayment_time(rs.getString("repayment_time"));
//						ua1.setRepayment_yestime(rs.getString("repayment_yestime"));
//						ua1.setUsername(rs.getString("username"));
//						ua1.setBorrow_id(rs.getLong("borrow_id"));
//						return ua1;
//					}
//				});
//				
//			}else{
//				list=getJdbcTemplate().query(sql, new Object[]{},new RowMapper<Repayment>(){
//					public Repayment mapRow(ResultSet rs, int rowNum)
//							throws SQLException {
//						Repayment ua=new Repayment();
//						ua.setRepayment_account(rs.getString("repayment_account"));
//						ua.setRepayment_time(rs.getString("repayment_time"));
//						ua.setRepayment_yestime(rs.getString("repayment_yestime"));
//						ua.setUsername(rs.getString("username"));
//						ua.setBorrow_id(rs.getLong("borrow_id"));
//						return ua;
//					}
//				});
//			}
//
//		} catch (DataAccessException e) {
//			logger.debug(e.getMessage());
//			e.printStackTrace();
//		}
//		return list;
//	}
//	@Override
//	public List getFlowBorrowList(String type, int start,int end, SearchParam param) {
//		String sql = "SELECT p3.username,p2.repayment_account AS repayment_account,p2.repayment_time AS repayment_time,p2.repayment_yestime AS repayment_yestime ,p2.borrow_id AS borrow_id FROM dw_borrow p1 " +
//				"LEFT JOIN dw_borrow_repayment AS p2 ON p1.id=p2.borrow_id " +
//				"LEFT JOIN dw_user AS p3 ON p1.user_id=p3.user_id WHERE 1=1 ";
//		String newsql = "SELECT p3.username,p2.*,p1.* FROM dw_borrow p1 LEFT JOIN dw_borrow_repayment AS p2 ON p1.id=p2.borrow_id " +
//				"left join dw_user as p3 on p1.user_id=p3.user_id " +
//				" where 1=1  ";	
//		String flowsql="SELECT u.username,collection.repay_account AS repayment_account,collection.repay_time AS repayment_time,collection.repay_yestime AS repayment_yestime,tender.borrow_id AS borrow_id  FROM dw_borrow_tender AS tender " +
//				"LEFT JOIN dw_borrow_collection AS collection ON tender.id=collection.tender_id " +
//				"LEFT JOIN dw_borrow AS b ON tender.borrow_id=b.id " +
//				"left join dw_user as u on u.user_id=b.user_id " +
//				"WHERE 1=1 ";
//		String typeSql = getBorrowTypeSql(type,sql,newsql,flowsql,0);
//		String flowTypeSql=getBorrowTypeSql(type, sql, newsql, flowsql, 1);
//		String searchSql=param.getSearchParamSql();
//		
//		String limitSql = " limit " + start + "," + end;
//		sql = typeSql+searchSql+limitSql;
//		flowsql = flowTypeSql+searchSql+limitSql;
//		// 拼装SQL
//		logger.debug("SQL:" + sql);
//		logger.debug("SQL:" + flowsql);
//		List list = new ArrayList();
//		List flowlist=new ArrayList();
//		List newlist=new ArrayList();
//		try {
//			if("wait_repay".equals(type)){
//				list =getJdbcTemplate().query(flowsql, new Object[]{},new RowMapper<Repayment>(){
//					public Repayment mapRow(ResultSet rs, int rowNum)
//							throws SQLException {
//						Repayment ua=new Repayment();
//						ua.setRepayment_account(rs.getString("repayment_account"));
//						ua.setRepayment_time(rs.getString("repayment_time"));
//						ua.setRepayment_yestime(rs.getString("repayment_yestime"));
//						ua.setUsername(rs.getString("username"));
//						ua.setBorrow_id(rs.getLong("borrow_id"));
//						return ua;
//					}
//				});
//			}
//		} catch (DataAccessException e) {
//			logger.debug(e.getMessage());
//			e.printStackTrace();
//		}
//		return list;
//	}
//	

//	/**
//	 * 实时财务
//	 * @param type
//	 * @param user_id
//	 * @param param
//	 * @return
//	 */
//	@Override
//	public int getBorrowCount(String type, SearchParam param) {
//		int newtotal = 0;
//		int flowTotal=0;
//		int total=0;
//		String sql = "SELECT count(1) FROM dw_borrow p1 " +
//				"LEFT JOIN dw_borrow_repayment AS p2 ON p1.id=p2.borrow_id " +
//				"LEFT JOIN dw_user AS p3 ON p1.user_id=p3.user_id WHERE 1=1 ";
//		String newsql = "SELECT count(1) FROM dw_borrow p1 LEFT JOIN dw_borrow_repayment AS p2 ON p1.id=p2.borrow_id " +
//				"left join dw_user as p3 on p1.user_id=p3.user_id " +
//				" where 1=1  ";	
//		String flowsql="SELECT count(1)  FROM dw_borrow_tender AS tender " +
//				"LEFT JOIN dw_borrow_collection AS collection ON tender.id=collection.tender_id " +
//				"LEFT JOIN dw_borrow AS b ON tender.borrow_id=b.id WHERE 1=1 ";
//		String typeSql = getBorrowTypeSql(type,sql,newsql,flowsql,0);
//		String flowTypeSql=getBorrowTypeSql(type, sql, newsql, flowsql, 1);
//		String searchSql=param.getSearchParamSql();
//		sql = typeSql+searchSql;
//		flowsql = flowTypeSql+searchSql;
//		logger.debug("SQL:" + sql);
//		try {
//			if("wait_repay".equals(type)){
//				newtotal = getJdbcTemplate().queryForInt(sql);
//				flowTotal = getJdbcTemplate().queryForInt(flowsql);
//				total=newtotal+flowTotal;
//			}else{
//				total = getJdbcTemplate().queryForInt(sql);
//			}
//		} catch (DataAccessException e) {
//			logger.debug("数据库查询结果为空！");
//		}
//		return total;
//	}
//
//	private String getBorrowTypeSql(String type,String sql,String newsql,String flowsql,int i) {
//		/*String sundayString=DateUtils.getCurrentWeekday()+" 23:59:59";
//		String mondayString=DateUtils.getMondayOFWeek()+"  00:00:00";
//		String monday=DateUtils.getTime(mondayString)+"";
//		String sumday=DateUtils.getTime(sundayString)+"";
//		logger.debug("本周一时间为"+monday);
//		logger.debug("本周日时间为"+sumday);*/
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//	    logger.info("当前时间:" + df.format(new Date()));
//	    String mondayString = StringUtils.getMonday(df.format(new Date())) + " 00:00:00";
//	    String sumdayString = StringUtils.getSunday(df.format(new Date())) + " 23:59:59";
//	    String monday = DateUtils.getTime(mondayString)+"";
//	    String sumday = DateUtils.getTime(sumdayString)+"";
//	    logger.debug("本周一时间为" + monday);
//	    logger.debug("本周日时间为" + sumday);
//
//		String typeSql ="";
//		/*if(i==0){
//			typeSql =sql+ " and p2.repayment_time<"+sumday+" AND p2.repayment_time>"+monday+"  and p1.is_flow!=1  ";//一周内待还
//		}
//		if(i==1){
//			typeSql =flowsql+ " and collection.repay_time<"+sumday+" AND collection.repay_time>"+monday+"  and b.is_flow=1";//一周内待还
//		}
//		if ("overdue_10_day_down_unrepay".equals(type)) {
//			typeSql =newsql+ " and p2.repayment_time+10*24*60*60>"+DateUtils.getNowTimeStr()+" AND p2.repayment_time<"+DateUtils.getNowTimeStr()+" AND p2.repayment_yestime IS NULL and p1.is_flow!=1 ";// 逾期10天内待还
//		} else if ("overdue_10_day_up_unrepay".equals(type)) {
//			typeSql =newsql+ " and 10*24*60*60+p2.repayment_time<"+DateUtils.getNowTimeStr()+" AND p2.repayment_yestime IS NULL and p1.is_flow!=1 ";// 逾期10天以上未还
//		} else if ("overdue_yesrepay".equals(type)) {
//			typeSql =newsql+ " and p2.repayment_time<"+DateUtils.getNowTimeStr()+" AND p2.repayment_yestime IS NOT  NULL and p2.repayment_yestime > p2.repayment_time+30*60 and p1.is_flow!=1 ";// 逾期已还
//		}*/
//		if(i==0){
//			typeSql =sql+ " and p2.repayment_time<"+sumday+" AND p2.repayment_time>"+monday+"  and p1.type!="+Constant.TYPE_FLOW;//一周内待还
//		}
//		if(i==1){
//			typeSql =flowsql+ " and collection.repay_time<"+sumday+" AND collection.repay_time>"+monday+"  and b.type="+Constant.TYPE_FLOW;//一周内待还
//		}
//		if ("overdue_10_day_down_unrepay".equals(type)) {
//			typeSql =newsql+ " and p2.repayment_time+10*24*60*60>"+DateUtils.getNowTimeStr()+" AND p2.repayment_time<"+DateUtils.getNowTimeStr()+" AND p2.repayment_yestime IS NULL and p1.type!="+Constant.TYPE_FLOW;// 逾期10天内待还
//		} else if ("overdue_10_day_up_unrepay".equals(type)) {
//			typeSql =newsql+ " and 10*24*60*60+p2.repayment_time<"+DateUtils.getNowTimeStr()+" AND p2.repayment_yestime IS NULL and p1.type!="+Constant.TYPE_FLOW;// 逾期10天以上未还
//		} else if ("overdue_yesrepay".equals(type)) {
//			typeSql =newsql+ " and p2.repayment_time<"+DateUtils.getNowTimeStr()+" AND p2.repayment_yestime IS NOT  NULL and p2.repayment_yestime > p2.repayment_time+30*60 and p1.type!="+Constant.TYPE_FLOW;// 逾期已还
//		}
//		
//		return typeSql;
//	}
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	
	@Override
	public BorrowModel getBorrowById(long id) {
		String sql = "select p1.* from dw_borrow  p1 where id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + id);
		BorrowModel b = null;
		try {
			b = this.getJdbcTemplate().queryForObject(sql, new Object[] { id },
					 getBeanMapper(BorrowModel.class));
			logger.debug("borrowto======" + b.getBorrow_time());
		} catch (DataAccessException e) {
			logger.debug("getBorrowById()" + e.getMessage());
		}
		return b;
		/*Object obj = super.findById(Borrow.class, id);
		if(obj!=null){
			return (BorrowModel)obj;
		}
		return null;*/
	}
	
	@Override
	public UserBorrowModel getUserBorrowById(long id) {
		String sql = "select  p1.*,p2.username,p8.name as user_area ,u.username as kefu_username,p2.qq," +
				"p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales," +
				"p7.name as usetypename,p2.realname "
				+ "from dw_borrow as p1 "
				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
				// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 start 
				//+ "left join dw_daizi as p6 on p1.id=p6.borrow_id "
				// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 end
				+ "left join dw_linkage p7 on p1.use=p7.id and p7.type_id=19 "
				+ "left join dw_area p8 on p2.province=p8.id "
				+ "where 1=1 and p1.id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + id);
		UserBorrowModel b = null;
		try {
			b = this.getJdbcTemplate().queryForObject(sql, new Object[] { id }, getBeanMapper(UserBorrowModel.class));
		} catch (DataAccessException e) {
			logger.error("getBorrowById()"+e.getMessage());
		}
		return b;
	}
	// v1.6.7.2 wcw 2013-12-20 start
	@Override
	public long addBorrow(Borrow borrow) {
		StringBuffer sql  =  new StringBuffer();
		sql.append("insert into  dw_borrow(");
		sql.append("user_id,");
		sql.append("`name`,");
		sql.append("`status`,");
		sql.append("`order`,");
		sql.append("flag,");
		sql.append("type,");
		sql.append("view_type,");
		sql.append("vouch_award,");
		sql.append("vouch_user,");
		sql.append("vouch_account,");
		sql.append("vouch_times,");
		sql.append("source,");
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
		sql.append("collateral,");
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
		sql.append("publish,");
		if (!StringUtils.isBlank(borrow.getVerify_user())) {
			sql.append("verify_user,");
		}
		sql.append("full_verifytime,");
		sql.append("verify_time,");
		sql.append("verify_remark,");
		sql.append("repayment_account,");
		sql.append("repayment_yesaccount,");
		sql.append("repayment_yesinterest,");
		sql.append("repayment_time,");
		sql.append("repayment_remark,");
		sql.append("payment_account,");
		sql.append("`use`,");
		sql.append("time_limit,");
		sql.append("style,");
		sql.append("account,");
		sql.append("account_yes,");
		sql.append("tender_times,");
		sql.append("apr,");
		sql.append("lowest_account,");
		sql.append("most_account,");
		sql.append("valid_time,");
		sql.append("award,");
		sql.append("part_account,");
		sql.append("funds,");
		sql.append("open_account,");
		sql.append("open_borrow,");
		sql.append("open_tender,");
		sql.append("open_credit,");
		sql.append("content,");
		sql.append("addtime,");
		sql.append("addip,");
		sql.append("pwd,");
		sql.append("isday,");
		sql.append("time_limit_day,");
		sql.append("flow_status,");
		sql.append("flow_money,");
		sql.append("flow_count,");
		sql.append("flow_yescount,");
		sql.append("is_recommend,");
		sql.append("late_award,");
		if (!StringUtils.isBlank(borrow.getBorrow_time())) {
			sql.append("borrow_time,");
		}
		if (borrow.getBorrow_account() > 0) {
			sql.append("borrow_account,");
		}
		if (!StringUtils.isBlank(borrow.getBorrow_time_limit())) {
			sql.append("borrow_time_limit,");
		}
		if (!StringUtils.isBlank(borrow.getCollection_limit())) {
			sql.append("collection_limit,");
		}
		sql.append("extension_day,");
		sql.append("extension_apr,");
		sql.append("lowest_single_limit,");
		
		//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
		sql.append("most_single_limit,");
		if(borrow.getMost_tender_times() != null){
			sql.append("most_tender_times,");
		}
		if(borrow.getTender_days() != null){
			sql.append("tender_days,");
		}
		sql.append("convest_collection");
		//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
		
		sql.append(") values (");
		sql.append(":user_id,");
		sql.append(":name,");
		sql.append(":status,");
		sql.append(":order,");
		sql.append(":flag,");
		sql.append(":type,");
		sql.append(":view_type,");
		sql.append(":vouch_award,");
		sql.append(":vouch_user,");
		sql.append(":vouch_account,");
		sql.append(":vouch_times,");
		sql.append(":source,"); 
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
		sql.append(":collateral,");
	    //V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
		sql.append(":publish,");
		if (!StringUtils.isBlank(borrow.getVerify_user())) {
			sql.append(":verify_user,");
		}
		sql.append(":full_verifytime,");
		sql.append(":verify_time,");
		sql.append(":verify_remark,");
		sql.append(":repayment_account,");
		sql.append(":repayment_yesaccount,");
		sql.append(":repayment_yesinterest,");
		sql.append(":repayment_time,");
		sql.append(":repayment_remark,");
		sql.append(":payment_account,");
		sql.append(":use,");
		sql.append(":time_limit,");
		sql.append(":style,");
		sql.append(":account,");
		sql.append(":account_yes,");
		sql.append(":tender_times,");
		sql.append(":apr,");
		sql.append(":lowest_account,");
		sql.append(":most_account,");
		sql.append(":valid_time,");
		sql.append(":award,");
		sql.append(":part_account,");
		sql.append(":funds,");
		sql.append(":open_account,");
		sql.append(":open_borrow,");
		sql.append(":open_tender,");
		sql.append(":open_credit,");
		sql.append(":content,");
		sql.append(":addtime,");
		sql.append(":addip,");
		sql.append(":pwd,");
		sql.append(":isday,");
		sql.append(":time_limit_day,");
		sql.append(":flow_status,");
		sql.append(":flow_money,");
		sql.append(":flow_count,");
		sql.append(":flow_yescount,");
		sql.append(":is_recommend,");
		sql.append(":late_award,");
		if (!StringUtils.isBlank(borrow.getBorrow_time())) {
			sql.append(":borrow_time,");
		}
		if (borrow.getBorrow_account() > 0) {
			sql.append(":borrow_account,");
		}
		if (!StringUtils.isBlank(borrow.getBorrow_time_limit())) {
			sql.append(":borrow_time_limit,");
		}
		if (!StringUtils.isBlank(borrow.getCollection_limit())) {
			sql.append(":collection_limit,");
		}
		sql.append(":extension_day,");
		sql.append(":extension_apr,");
		sql.append(":lowest_single_limit,");
		
		//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
		sql.append(":most_single_limit,");
		if(borrow.getMost_tender_times() != null){
			sql.append(":most_tender_times,");
		}
		if(borrow.getTender_days() != null){
			sql.append(":tender_days,");
		}
		sql.append(":convest_collection)");
		//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
		
		SqlParameterSource ps = new BeanPropertySqlParameterSource(borrow);
		try {
		    KeyHolder keyHolder = new GeneratedKeyHolder();
		    this.getNamedParameterJdbcTemplate().update(sql.toString(), ps,keyHolder);
		    return keyHolder.getKey().intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return 0;
		}
	// v1.6.7.2 wcw 2013-12-20 end
	public void updateBorrow(Borrow borrow) {
		String sql = "update dw_borrow t set t.account=:account,t.tender_times=:tender_times,t.apr=:apr," 
				+"style=:style,t.use=:use,t.time_limit=:time_limit,t.time_limit_day=:time_limit_day,t.isday=:isday,"
				+ "t.lowest_account=:lowest_account,t.most_account=:most_account,t.valid_time=:valid_time,t.name=:name,t.content=:content,"
				+ "t.pwd=:pwd,t.award=:award,t.funds=:funds,t.part_account=:part_account,t.vouch_award=:vouch_award,t.vouch_user=:vouch_user,"
				+ "t.open_tender=:open_tender,t.status=:status,t.full_verifytime=:full_verifytime,t.verify_time=:verify_time,t.verify_remark=:verify_remark "
				//V1.6.6.1 RDPROJECT-33 liukun 2013-09-10 start
				//截标时更新repayment_account
				+ ", t.repayment_account=:repayment_account"
				//V1.6.6.1 RDPROJECT-33 liukun 2013-09-10 end
				//V1.6.6.1 RDPROJECT-8 wcw 2013-09-26 start
				+ ", t.flow_money=:flow_money,t.late_award=:late_award "
				//V1.6.6.1 RDPROJECT-8 wcw 2013-09-26 end
				// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 start
				+ ", t.flow_time=:flow_time ,t.flow_totalyescount=:flow_totalyescount " 
				// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 end
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 start
				+", t.extension_apr=:extension_apr ,t.extension_day=:extension_day "
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 end
				//v1.6.6.2 RDPROJECT-333 wcw 2013-10-19 start
				+", t.lowest_single_limit=:lowest_single_limit ,t.most_single_limit=:most_single_limit " 
				//v1.6.6.2 RDPROJECT-333 wcw 2013-10-19 end
				
				//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 start
				+", t.vip_tender_limit=:vip_tender_limit ,t.vip_tender_limit_days=:vip_tender_limit_days " 
				//V1.6.7.1 RDPROJECT-406 liukun 2013-11-06 end
				
				//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 start
				+", t.convest_collection=:convest_collection,t.most_tender_times=:most_tender_times,t.tender_days=:tender_days " 
				//v1.6.7.2 RDPROJECT-473 sj 2013-12-16 end
				
				+ " where t.id=:id and t.user_id=:user_id ";
		logger.debug("SQL:" + sql);
		
		SqlParameterSource ps = new BeanPropertySqlParameterSource(borrow);
	    this.getNamedParameterJdbcTemplate().update(sql, ps);
	}
	@Override
	public void updateJinBorrow(Borrow borrow){
		String sql="update dw_borrow set verify_time=? where id=?";
		try {
			this.getJdbcTemplate().update(sql,borrow.getVerify_time(),borrow.getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	public void updateFullBorrow(Borrow borrow){
		String sql="update dw_borrow set full_verifytime=? where id=?";
		try {
			this.getJdbcTemplate().update(sql,borrow.getFull_verifytime(),borrow.getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	public int updateBorrow(double account,int status,long id) {
		String sql="update dw_borrow set tender_times=tender_times+1,account_yes=round(account_yes+?,2),status=? " +
				"where id=? and round(account_yes+?)<=account+0";
		int count=0;
		try {
			count=getJdbcTemplate().update(sql, account,status,id,account);
		} catch (Exception e) {
			logger.error(e);
		}
		return count;
	}

	@Override
	public void deleteBorrow(Borrow borrow) {
		String sql = "update dw_borrow set status=5 where borrow_id=?";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, borrow.getId());
	}
	@Override
	public List getSuccessListByUserid(long user_id,String type) {
		String sql = "select bt.repayment_yesaccount, bt.repayment_account, bt.addtime as tender_time, bt.account as anum, " +
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
				"bt.repayment_account - bt.account as inter, bo.name as borrow_name,bo.extension_day, " +
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end				
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 start
				"bo.account, bo.time_limit, bo.isday, bo.time_limit_day, bo.apr,bo.extension_apr, " +
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 end
				"u.username, cr.value as credit, bo.id,u.user_id from dw_borrow_tender as bt, " +
				"dw_borrow as bo, dw_user as u, dw_credit as cr where bt.user_id = ? and " +
				"bo.user_id = u.user_id and cr.user_id = bo.user_id and bt.borrow_id = bo.id " +
				"and bo.status =1";
		String _sql="";
		// v1.6.7.2 RDPROJECT-564 zza 2013-12-11 start
		if (type!=null&&type.equals("1")) { //wait
			_sql = " and (CONVERT(bt.repayment_yesaccount, " + 
				"DECIMAL(20,6))+CONVERT(bt.repayment_yesinterest, " + 
				"DECIMAL(20,6)))<>CONVERT(bt.repayment_account,DECIMAL(20,6)) " + 
				"ORDER BY bt.addtime desc";
			sql=sql+_sql;
		} else if(type!=null&&type.equals("2")) { //yes
			_sql = " and (CONVERT(bt.repayment_yesaccount," + 
				"DECIMAL(20,6))+CONVERT(bt.repayment_yesinterest," + 
				"DECIMAL(20,6)))=CONVERT(bt.repayment_account,DECIMAL(20,6)) " +
				"ORDER BY bt.addtime desc";
			sql=sql+_sql;
		}
		// v1.6.7.2 RDPROJECT-564 zza 2013-12-11 end
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		logger.debug("SQL:" + type);
		List<InvestBorrowModel> list = null;
		try {
			list = this.getJdbcTemplate().query(sql,new Object[]{user_id}, getBeanMapper(InvestBorrowModel.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public List getSuccessBorrowList(int start,int end,SearchParam param){
		return null;
	}
	
//	@Override
//	public List getSuccessBorrowList(String type, int start, int end,SearchParam param) {
//		String sql = "select bt.repayment_yesaccount, bt.repayment_account, bt.addtime as tender_time, bt.account as anum, " +
//				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
//				"bt.repayment_account - bt.account as inter, bo.name as borrow_name, bo.extension_day," +
//				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
//				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 start
//				"bo.account, bo.time_limit, bo.isday, bo.time_limit_day, bo.apr,bo.extension_apr, " +
//				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 end
//				"u.username, cr.value as credit, bo.id,u.user_id from dw_borrow_tender as bt, " +
//				"dw_borrow as bo, dw_user as u, dw_credit as cr where 1=1 and " +
//				"bo.user_id = u.user_id and cr.user_id = bo.user_id and bt.borrow_id = bo.id " +
//				"and bo.status =1";
//		String _sql="";
//		if(type!=null&&type.equals("1")){//wait
//			_sql = " and (CONVERT(bt.repayment_yesaccount,DECIMAL(20,6))+CONVERT(bt.repayment_yesinterest,DECIMAL(20,6)))<>CONVERT(bt.repayment_account,DECIMAL(20,6)) ";
//			sql=sql+_sql;
//		}else if(type!=null&&type.equals("2")){//yes
//			_sql = " and (CONVERT(bt.repayment_yesaccount,DECIMAL(20,6))+CONVERT(bt.repayment_yesinterest,DECIMAL(20,6)))=CONVERT(bt.repayment_account,DECIMAL(20,6)) ";
//			sql=sql+_sql;
//		}
//		logger.debug("SQL:" + sql);
//		logger.debug("SQL:" + type);
//		List<InvestBorrowModel> list = null;
//		try {
//			list = this.getJdbcTemplate().query(sql, getBeanMapper(InvestBorrowModel.class));
//		} catch (DataAccessException e) {
//			logger.debug(e.getMessage());
//			e.printStackTrace();
//		}
//		return list;
//	}

	@Override
	public List getSuccessBorrowList(long user_id, String type,int start,int end,
			SearchParam param) {
		String sql = "select bt.repayment_yesaccount,bt.repayment_account, bt.addtime as tender_time, bt.account as anum, " +
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
				"bt.repayment_account - bt.account as inter, bo.name as borrow_name, bo.extension_day," +
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 start
				"bo.account, bo.time_limit, bo.isday, bo.time_limit_day, bo.apr,bo.extension_apr, " +
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-14 end
				"u.username, cr.value as credit, bo.id,u.user_id from dw_borrow_tender as bt, " +
				"dw_borrow as bo, dw_user as u, dw_credit as cr where bt.user_id = ? and " +
				"bo.user_id = u.user_id and cr.user_id = bo.user_id and bt.borrow_id = bo.id " +
				//"and ((bo.status =1 and bo.is_flow=1) or bo.status in (3,6,7,8)) ";
				"and ((bo.status =1 and bo.type="+Constant.TYPE_FLOW+") or bo.status in (3,6,7,8)) ";
		String _sql="";
		if(type!=null&&type.equals("1")){//wait
			_sql = " and (CONVERT(bt.repayment_yesaccount,DECIMAL(20,6)))<>CONVERT(bt.repayment_account,DECIMAL(20,6)) ";
			sql=sql+_sql;
		}else if(type!=null&&type.equals("2")){//yes
			_sql = " and (CONVERT(bt.repayment_yesaccount,DECIMAL(20,6)))=CONVERT(bt.repayment_account,DECIMAL(20,6)) ";
			sql=sql+_sql;
		}
		String searchSql=param.getSearchParamSql();
		String limitSql=" limit "+start+","+end;
		String orderSql=" order by bt.addtime desc ";
		sql=sql+searchSql+orderSql+limitSql;
		logger.debug("SQL:" + sql);
		List<InvestBorrowModel> list = null;
		try {
			list = this.getJdbcTemplate().query(sql,new Object[]{user_id}, getBeanMapper(InvestBorrowModel.class));
		} catch (DataAccessException e) {
			logger.debug("getSuccessBorrowList():"+e.getMessage());
		}
		return list;
	}

	@Override
	public int getSuccessBorrowCount(long user_id, String type,
			SearchParam param) {
		String sql = "select count(1) from dw_borrow_tender as bt, " +
				"dw_borrow as bo, dw_user as u, dw_credit as cr where bt.user_id = ? and " +
				"bo.user_id = u.user_id and cr.user_id = bo.user_id and bt.borrow_id = bo.id " +
				//"and ((bo.status =1 and bo.is_flow=1) or bo.status in (3,6,7,8)) ";
				"and ((bo.status =1 and bo.type="+Constant.TYPE_FLOW+") or bo.status in (3,6,7,8)) ";
		String _sql="";
		// v1.6.6.1 RDPROJECT-242 zza 2013-09-30 start
		if(type!=null&&type.equals("1")){//wait
			_sql = " and (CONVERT(bt.repayment_yesaccount,DECIMAL(20,6)))<>CONVERT(bt.repayment_account,DECIMAL(20,6)) ";
			sql=sql+_sql;
		}else if(type!=null&&type.equals("2")){//yes
			_sql = " and (CONVERT(bt.repayment_yesaccount,DECIMAL(20,6)))=CONVERT(bt.repayment_account,DECIMAL(20,6)) ";
			sql=sql+_sql;
		}
		// v1.6.6.1 RDPROJECT-242 zza 2013-09-30 end
		String searchSql=param.getSearchParamSql();
		sql=sql+searchSql;
		logger.debug("SQL:" + sql);
		int total=0;
		try {
			total=this.count(sql, user_id);
		} catch (DataAccessException e) {
			logger.debug("getSuccessBorrowCount():"+e.getMessage());
		}
		return total;
	}

//	@Override
//	public List getListByUserid(long user_id) {
//		String sql = "select  p1.*,p2.username,p2.realname,p2.area as user_area ,u.username as kefu_username,p2.qq,p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales "
//				+ "from dw_borrow as p1 "
//				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
//				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
//				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
//				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
//				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
//				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
//				// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 start 
//				//+ "left join dw_daizi as p6 on p1.id=p6.borrow_id where 1=1 and user_id=?";
//				+ "where 1=1 and user_id=?";
//				// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 end 
//		List<UserBorrowModel> list = null;
//		try {
//			list = this.getJdbcTemplate().query(sql,new Object[]{user_id},getBeanMapper(UserBorrowModel.class));
//		} catch (DataAccessException e) {
//			logger.debug(e.getMessage());
//			e.printStackTrace();
//		}
//		return list;
//	}

//	@Override
//	public List<Borrow> getBorrowList(long user_id) {
//		String sql="select * from dw_borrow where user_id=? and status in (3,6,7)";
//		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(Borrow.class));
//		return list;
//	}
//	@Override
//	public List<Borrow> getBorrowList(int start,int end,SearchParam param) {
//		String sql="select * from dw_borrow p1 where 1=1 ";
//		String searchSql=param.getSearchParamSql();
//		String limitSql=" limit ?,? ";
//		String orderSql=" order by p1.addtime desc";
//		sql=sql+searchSql+orderSql+limitSql;
//		logger.debug("SQL:"+sql);
//		List list=new ArrayList();
//		list=getJdbcTemplate().query(sql, new Object[]{start,end}, getBeanMapper(Borrow.class));
//		return list;
//	}
//	@Override
//	public int getBorrowCount(SearchParam param) {
//		String sql="select * from dw_borrow p1 where 1=1 ";
//		String searchSql=param.getSearchParamSql();
//		sql=sql+searchSql;
//		logger.debug("SQL:"+sql);
//		int total=0;
//		total=count(sql);
//		return total;
//	}
	
//	@Override
//	public int borrowCount(long user_id, SearchParam param) {
//		String sql = "select count(1) from dw_borrow p1 where p1.user_id=? "
//				+ "and (p1.type <> 110 and p1.status in (3,6,7,8)) or (p1.type = 110 and p1.status in (1,8))";
//		String searchSql = param.getSearchParamSql();
//		sql = sql + searchSql;
//		logger.debug("SQL:" + sql);
//		logger.debug("SQL:" + user_id);
//		int total = 0;
//		total = this.getJdbcTemplate().queryForInt(sql, new Object[]{user_id});
//		return total;
//	}
	
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-13 start
	@Override
	public List<Borrow> getBorrowList(long user_id, SearchParam param, int start, int end) {
		String selectSql = "select * from dw_borrow p1 where p1.user_id=? "
				+ "and (p1.type <> 110 and p1.status in (3,6,7,8)) or (p1.type = 110 and p1.status in (1,8))";
		StringBuffer sb = new StringBuffer(selectSql);
		String searchSql = param.getSearchParamSql();
		String orderSql = "order by p1.addtime desc";
		sb.append(searchSql).append(orderSql).append(" limit ?,?");
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + user_id);
		List<Borrow> list = new ArrayList<Borrow>();
		list = this.getJdbcTemplate().query(sql, new Object[] { user_id, start, end }, getBeanMapper(Borrow.class));
		return list;
	}
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-13 end

//	@Override
//	public int getBorrowCount(long user_id) {
//		String sql="select count(id) from dw_borrow where user_id=? and status>=3";
//		int count=0;
//		count=this.getJdbcTemplate().queryForInt(sql,new Object[]{user_id});
//		return count;
//	}

	//查询所有的SQL
	private String queryAllBorrowSql = "select  p1.*,p2.username,p8.name as user_area ,u.username as kefu_username,p2.qq," +
			"p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales," +
			"p7.name as usetypename,p2.realname,p1.verify_user "
			+ "from dw_borrow as p1 "
			+ "left join dw_user as p2 on p1.user_id=p2.user_id "
			+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
			+ "left join dw_user as u on u.user_id=uca.kefu_userid "
			+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
			+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
			+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
			// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 start 
			//+ "left join dw_daizi as p6 on p1.id=p6.borrow_id "
			// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 end
			+ "left join dw_linkage p7 on p1.use=p7.id and p7.type_id=19 "
			+ "left join dw_area p8 on p2.province=p8.id "
			+ "where 1=1 ";
	private String queryBorrowCountSql = "select count(1) "
			+ "from dw_borrow as p1 "
			+ "left join dw_user as p2 on p1.user_id=p2.user_id "
			+ "left join dw_user_cache as uca on uca.user_id=p2.user_id "
			+ "left join dw_user as u on u.user_id=uca.kefu_userid "
			+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
			+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
			+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
			// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 start 
			//+ "left join dw_daizi as p6 on p1.id=p6.borrow_id "
			// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 end
			+ "left join dw_linkage p7 on p1.use=p7.id and p7.type_id=19 "
			+ "left join dw_area p8 on p2.province=p8.id "
			+ "where 1=1 ";
	private String queryBorrowSumSql = "select sum(p.account) as num "
			+ "from dw_borrow_tender p left join dw_borrow as p1 on p.borrow_id=p1.id "
			+ "left join dw_user as p2 on p1.user_id=p2.user_id "
			+ "where p1.status not in(49,59) ";
	// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 start
	private String queryBorrowRepaymentYesAccountSumSql="select sum(p1.repayment_yesaccount) as num from dw_borrow p1 left join dw_user as p2 on p1.user_id=p2.user_id where 1=1 ";
	private String queryBorrowUnRepaymentAccountSumSql="select sum(CAST(p1.repayment_account AS DECIMAL(20,6))-CAST(p1.repayment_yesaccount AS DECIMAL(20,6))) as num from dw_borrow p1 left join dw_user as p2 on p1.user_id=p2.user_id where 1=1 ";
	// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 end
	@Override
	public List getAllBorrowList(int start, int pernum, SearchParam param) {
		StringBuffer sb = new StringBuffer(queryAllBorrowSql);
		String searchSql = param.getSearchParamSql();
		String limitSql = " order by p1.id desc limit ?,?";
		String sql = sb.append(searchSql).append(limitSql).toString();
		logger.debug("getAllBorrowList():" + sql);
		List list = getJdbcTemplate().query(sql, new Object[] { start, pernum }, getBeanMapper(UserBorrowModel.class));
		return list;
	}
	@Override
	public int getAllBorrowCount(SearchParam param) {
		StringBuffer sb=new StringBuffer(queryBorrowCountSql);
		String searchSql= param.getSearchParamSql();
		String sql=sb.append(searchSql).toString();
		logger.debug("getAllBorrowCount():"+sql);
		int total=0;
		total=count(sql);
		return total;
	}
	
	
	//String fullSql=" and (p1.account_yes+0)=(p1.account+0) and p1.is_flow<>1 ";//暂时这样处理
	String fullSql=" and (p1.account_yes+0)=(p1.account+0) and p1.type!="+Constant.TYPE_FLOW;//暂时这样处理
	String queryFullBorrowSql=queryAllBorrowSql+fullSql;
	String queryFullBorrowCountSql=queryBorrowCountSql+fullSql;
	
		@Override
		public List getFullBorrowList(int start, int pernum, SearchParam param) {
			StringBuffer sb=new StringBuffer(queryFullBorrowSql);
			String searchSql= param.getSearchParamSql();
			String limitSql=" order by p1.id desc limit ?,?";
			String sql=sb.append(searchSql).append(limitSql).toString();
			logger.debug("getAllBorrowList():"+sql);
			List list=getJdbcTemplate().query(sql, new Object[]{start,pernum} , getBeanMapper(UserBorrowModel.class));
			return list;
		}
		@Override
		public int getFullBorrowCount(SearchParam param) {
			StringBuffer sb=new StringBuffer(queryFullBorrowCountSql);
			String searchSql= param.getSearchParamSql();
			String sql=sb.append(searchSql).toString();
			logger.debug("getAllBorrowCount():"+sql);
			int total=0;
			total=count(sql);
			return total;
		}

		@Override
		public List unfinshBorrowList(long user_id) {
			//String sql="select * from dw_borrow where user_id=? and status in (0,1) and is_flow<>1";
			String sql="select * from dw_borrow where user_id=? and status in (0,1) and type!="+Constant.TYPE_FLOW;
			List list=new ArrayList();
			list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(Borrow.class));
			return list;
		}
		
		public List unfinshJinBorrowList(long user_id) {
			//String sql = "select * from dw_borrow where user_id=? and status in (0,1) and is_jin = 1";
			String sql = "select * from dw_borrow where user_id=? and status in (0,1) and type="+Constant.TYPE_PROPERTY;
			List list = new ArrayList();
			list = this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(Borrow.class));
			return list;
		}
		// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 start
		@Override
		public int updateBorrowFlowTime(Borrow b) {
			//String sql="update dw_borrow set flow_totalyescount=? where ?<= (flow_time*flow_count) AND id=?";
			int count = 0;
			if(b!=null){
				String sql="update dw_borrow set flow_totalyescount=? ";
				if(b.getFlow_time()>0){
					sql+=" where ?<= (flow_time*flow_count) AND id=?";
				}else{
					sql+=" where 0<=? AND id=?";
				}
				logger.debug("updateBorrowFlowTime sql:"+sql);
				try {
					count = getJdbcTemplate().update(sql, b.getFlow_totalyescount(), b.getFlow_totalyescount(), b.getId());
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
			}
			return count;
		}
		// v1.6.6.2 RDPROJECT-295 xx 2013-10-11 end
		@Override
		public void updateBorrowFlow(Borrow b) {
			String sql="update dw_borrow set flow_yescount=?";
			getJdbcTemplate().update(sql, b.getFlow_yescount());
		}

		@Override
		public double hasTenderTotalPerBorrowByUserid(long borrow_id,long user_id) {
			String sql="select sum(account) as num from dw_borrow_tender where borrow_id=? and user_id=? for update";
			return sum(sql,borrow_id,user_id);
		}	
//		//借款总额，待收总额
//		@Override
//		public double getBorrowAccountSum() {
//			String sql="select sum(money) as num from dw_account_log where type='borrow_success'";
//			double total=0;
//			total=sum(sql);
//			return total;
//		}	
//		@Override
//		public double getDayBorrowAccountSum(String startTime,String endTime){
//			String sql="select sum(money) as num from dw_account_log where type='borrow_success' and addtime>=? and addtime<=? ";
//			double total=0;
//			total=sum(sql, new Object[]{ startTime,endTime});
//			return total;
//		}
		public void addjk(RunBorrow runBorrow){
			final String sql="insert into dw_run_borrow(money,username,tel,description) values(?,?,?,?)";
			final RunBorrow r=runBorrow;
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				@Override
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, r.getMoney());
					ps.setString(2, r.getUsername());
					ps.setString(3, r.getTel());
					ps.setString(4, r.getDescription());
					return ps;
				}
			}, keyHolder);
			long key=keyHolder.getKey().longValue();
			runBorrow.setId(key);
		}
		public List jklist(int start, int end, SearchParam param){
			String sql="select * from dw_run_borrow ";
			List list=null;
			String limitSql="  limit ?,?";
			StringBuffer sb=new StringBuffer("");
			String querySql=sb.append(sql)
					.append(limitSql)
					.toString();
			
			try{
			list=getJdbcTemplate().query(querySql,new Object[]{start,end},  new RowMapper<RunBorrow>(){
				@Override
				public RunBorrow mapRow(ResultSet rs, int num)
						throws SQLException {
					RunBorrow s=new RunBorrow();
					s.setId(rs.getLong("id"));
			        s.setMoney(rs.getString("money"));
			        s.setDescription(rs.getString("description"));
			        s.setTel(rs.getString("tel"));
			        s.setUsername(rs.getString("username"));
					return s;
				}
			});
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
			return list;
		}
		public int getjkCount(SearchParam param){
			int total=0;
			String sql="select count(1) from dw_run_borrow";
			try {
				total=count(sql);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return total;
		}


		// v1.6.7.1 RDPROJECT-467 2013-11-19 start
		@Override
		public double getRepayTotalWithJin(long user_id) {
			//String sql="select sum(account) as num from dw_borrow where type="+Constant.TYPE_PROPERTY+" and status in (1,3,6,7) and user_id=? ";
			String sql="SELECT SUM(r.repayment_account) AS num FROM dw_borrow b,dw_borrow_repayment r WHERE r.borrow_id=b.id AND b.type="+Constant.TYPE_PROPERTY+" AND r.status=0 AND b.user_id=? ";
			double total=0;
			total=sum(sql,user_id);
			return total;
		}
		// v1.6.7.1 RDPROJECT-467 2013-11-19 end
		
	    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//		@Override
//		public List advancedList(){
//			String sql="select * from dw_advanced";
////			List list=getJdbcTemplate().query(sql,new Object[]{},new AdvancedMapper()); 
//			List list=getJdbcTemplate().query(sql,new Object[]{}, getBeanMapper(Advanced.class));
//			return list;
//		}
	    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
//		//系统当前融资总额
//		@Override
//		public double getBorrowSum() {
//			//String sql="SELECT SUM(tender.account) AS num FROM  dw_borrow_tender AS tender LEFT JOIN dw_borrow AS b ON tender.borrow_id=b.id WHERE  ((b.status IN(3,6,7,8) AND b.is_flow!=1) OR ( b.is_flow=1)) ";
//			String sql="SELECT SUM(tender.account) AS num FROM  dw_borrow_tender AS tender LEFT JOIN dw_borrow AS b ON tender.borrow_id=b.id WHERE  ((b.status IN(3,6,7,8) AND b.type!="+Constant.TYPE_FLOW+") OR ( b.type="+Constant.TYPE_FLOW+")) ";
//			double total=0;
//			total=sum(sql);
//			return total;
//		}	
//		//当天应还本息总额
//		@Override
//		public double getTotalRepayAccountAndInterest(String todayStartTime,String todayEndTime) {
//			String sql="select sum(p1.repay_account) as num from dw_borrow_collection as p1 where p1.repay_time>"+todayStartTime+" and p1.repay_time<"+todayEndTime+" and p1.repay_yestime is null";
//			double total=0;
//			total=sum(sql);
//			return total;
//		}	
		//借款总额统计
		@Override
		public double getSumBorrowAccount(SearchParam param) {
			StringBuffer sb=new StringBuffer(queryBorrowSumSql);
			String searchSql= param.getSearchParamSql();
			String sql=sb.append(searchSql).toString();
			logger.debug("getAllBorrowSum():"+sql);
			double total=0;
			total=sum(sql);
			return total;
		}
		// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 start
		@Override
		public double getSumBorrowRepaymentYesAccount(SearchParam param) {
			StringBuffer sb=new StringBuffer(queryBorrowRepaymentYesAccountSumSql);
			String searchSql= param.getSearchParamSql();
			String sql=sb.append(searchSql).toString();
			logger.debug("getAllBorrowSum():"+sql);
			double total=0;
			total=sum(sql);
			return total;
		}
		@Override
		public double getSumBorrowUnRepaymentAccount(SearchParam param) {
			StringBuffer sb=new StringBuffer(queryBorrowUnRepaymentAccountSumSql);
			String searchSql= param.getSearchParamSql();
			String sql=sb.append(searchSql).toString();
			logger.debug("getAllBorrowSum():"+sql);
			double total=0;
			total=sum(sql);
			return total;
		}
		// v1.6.6.2 RDPROJECT-118 wcw 2013-10-22 end
		//查看借款人是否借款
		@Override
		public int hasBorrowCountByUserid(long borrow_id,long user_id) {
			String sql="SELECT count(1) AS num FROM dw_borrow  WHERE id=? AND user_id=? FOR UPDATE";
			int count =0;
			count=count(sql,borrow_id,user_id);
			return count;
		}
		

		@Override
		public void reservation_add(Reservation reservation) {
			final String sql="insert into dw_reservation(reservation_user,tender_account,borrow_apr,addtime,addip) values(?,?,?,?,?)";
			final Reservation r=reservation;
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				@Override
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, r.getReservation_user());
					ps.setDouble(2, r.getTender_account());
					ps.setString(3, r.getBorrow_apr());
					ps.setString(4, r.getAddtime());
					ps.setString(5, r.getAddip());
					return ps;
				}
			}, keyHolder);
			long key=keyHolder.getKey().longValue();
			reservation.setId(key);
		}
		//预约标列表
		@Override
		public List getReservation_list(int start,int end,SearchParam param){
			String sql="select p.*,p1.username from dw_reservation as p left join dw_user as p1 on p1.user_id=p.reservation_user";
			List list=getJdbcTemplate().query(sql,new Object[]{}, getBeanMapper(ReservationModel.class));
			return list;
		}
		
		@Override
		public int getReservation_list_count(SearchParam parm) {
			String sql="select count(*) as num from dw_reservation";
			int total=0;
			total=count(sql);
			return total;
		}

		// v1.6.7.1 防止标重复审核 xx 2013-11-20 start
		@Override
		public int updateBorrowStatus(int status, int lastStatus, long id) {
			String sql="UPDATE dw_borrow SET status=? WHERE status=? AND id=? ";
			int count = getJdbcTemplate().update(sql, status, lastStatus, id);
			if(count!=1){
				throw new BorrowException("该标已经满标复审通过，请勿重复操作！");
			}
			return count;
		}
		// v1.6.7.1 防止标重复审核 xx 2013-11-20 end
		
		@Override
		public int updateBorrowStatus(int status, long id) {
			String sql="update dw_borrow set status=? where id=? ";
			logger.debug("updateBorrow():"+sql);
			logger.debug("updateBorrow() params:,"+status+","+id);
			int count=0;
			try {
				count=getJdbcTemplate().update(sql, status,id);
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			logger.debug("updateBorrow() result:"+count);
			return count;
		}
		@Override
		public int updateBorrowFlowStatus(int flow_status, long id) {
			String sql="update dw_borrow set flow_status=? where id=? ";
			logger.debug("updateBorrow():"+sql);
			logger.debug("updateBorrow() params:,"+flow_status+","+id);
			int count=0;
			try {
				count=getJdbcTemplate().update(sql, flow_status,id);
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			logger.debug("updateBorrow() result:"+count);
			return count;
		}
		@Override
		public List getBorrowListByStatus(int status) {
			String sql="select id,status from dw_borrow where status=?";
			List list=new ArrayList();
			list=this.getJdbcTemplate().query(sql, getBeanMapper(BorrowModel.class), status);
			return list;
		}
		@Override
		public int releaseFlowBorrow(double account,long id) {
			String sql="update dw_borrow set account_yes=CAST(account_yes AS DECIMAL(20,6))-? " +
					"where id=? and account_yes-?>=0";
			logger.debug("releaseFlowBorrow():"+sql);
			logger.debug("releaseFlowBorrow() params:"+account);
			int count=0;
			try {
				count=getJdbcTemplate().update(sql, account,id,account);
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			logger.debug("releaseFlowBorrow() result:"+count);
			return count;
		}
		//v1.6.6.2 RDPROJECT-118 wcw 2013-10-21 start
		@Override
		public int updateBorrowAndRepay(BorrowModel b) {
			String sql="update dw_borrow set repayment_account=CAST(repayment_account AS DECIMAL(20,6))+?,repayment_yesaccount=CAST(repayment_yesaccount AS DECIMAL(20,6))+?, repayment_yesinterest=CAST(repayment_yesinterest AS DECIMAL(20,6))+? " +
					"where id=? ";
			logger.debug("releaseFlowBorrow():"+sql);
			logger.debug("releaseFlowBorrow() params:"+b.getRepayment_yesaccount());
			int count=0;
			try {
				count=getJdbcTemplate().update(sql, b.getRepayment_account(),b.getRepayment_yesaccount(),b.getRepayment_yesinterest(),b.getId());
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			logger.debug("releaseFlowBorrow() result:"+count);
			return count;
		}
		//v1.6.6.2 RDPROJECT-118 wcw 2013-10-21 end
		@Override
		public int getBorrowCountForSuccess() {
			String sql ="SELECT COUNT(*) FROM dw_borrow WHERE (STATUS IN(3,6,7,8) AND type <>" + Constant.TYPE_FLOW + ") OR (STATUS IN(1,3,6,7,8) AND type=" + Constant.TYPE_FLOW + ")";
			logger.debug("BorrowCountForSuccess:"+sql);
			return getJdbcTemplate().queryForInt(sql);
		}
		
		/**
		 * 更新为推荐标，在线金融首页显示
		 * @param borrow
		 */
		@Override
		public void updateRecommendBorrow(Borrow borrow){
			String sql="update dw_borrow set is_recommend=? where id=?";
			try {
				this.getJdbcTemplate().update(sql,borrow.getIs_recommend(),borrow.getId());
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		/**
		 * 获得正在流转并且100%完成的流转标
		 */
		public List getFullFlowBorrow() {
			String sql = "select * from dw_borrow where status=1 and type="+Constant.TYPE_FLOW+"";
			List list = new ArrayList();
			list = this.getJdbcTemplate().query(sql, getBeanMapper(Borrow.class));
			return list;
		}
		
		
		/**
		 * 每日发标金额（初审通过）
		 * @return
		 */
		public double getDayFirstVerifyAccount() {
			String sql = "select sum(account) as num from dw_borrow where status in (1,3,6,7,8) and " +
				"(verify_time > " + (DateUtils.getIntegralTime().getTime() / 1000) + 
				" and verify_time < " + (DateUtils.getLastIntegralTime().getTime() / 1000) + ")";
			logger.debug("SQL语句：" + sql);
			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
			double sum = 0;
			if (rs.next()) {
				sum = rs.getDouble("num");
			}
			return sum;
		}
		
		/**
		 * 每日复审通过的投标总金额
		 * @return
		 */
		public double getDayFullVerifyAccount() {
			String sql = "select sum(account) as num from dw_borrow where (status in (3,6,7,8) or (type="+Constant.TYPE_FLOW+" and status in(1,3,6,7,8)))" +
				" and (full_verifytime >= " + (DateUtils.getIntegralTime().getTime() / 1000) + 
				" and full_verifytime <= " + (DateUtils.getLastIntegralTime().getTime() / 1000) + ")";
			logger.debug("SQL语句：" + sql);
			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
			double sum = 0;
			if (rs.next()) {
				sum = rs.getDouble("num");
			}
			return sum;
		}
		
		/**
		 * 每日复审利息总额
		 * @return
		 */
		public double getDayInterestAccount() {
			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 start
			String sql = "select sum(bt.interest) as interest from dw_borrow_tender as bt left join dw_borrow as b on b.id = bt.borrow_id " +
					"where b.type<>"+Constant.TYPE_SECOND+" and ((b.status in (3, 6, 7, 8) and b.type<>"+Constant.TYPE_FLOW+" " +
					"and (b.full_verifytime >= " + (DateUtils.getIntegralTime().getTime() / 1000) + 
					" and b.full_verifytime <=" + (DateUtils.getNowTimeStr()) + " )) " +
					"or ( b.type="+Constant.TYPE_FLOW+" and b.status in (1, 3, 6, 7, 8) " +
					"and (bt.addtime >= " + (DateUtils.getIntegralTime().getTime() / 1000) + 
					" and bt.addtime <= " + (DateUtils.getNowTimeStr()) + ")))";
			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 end
			logger.debug("SQL语句：" + sql);
			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
			double sum = 0;
			if (rs.next()) {
				sum = rs.getDouble("interest");
			}
			return sum;
		}
		
		/**
		 * 今日投标奖励发放总额
		 * @return
		 */
		public double getDayAwardAccount() {
			String sql = "select sum(case when b.award=1 then b.account*(b.part_account/100) when b.award= 2 then b.funds end) award " +
				"from dw_borrow b where b.award <>0 and (b.status in (3,6,7,8) or (b.type="+Constant.TYPE_FLOW+" and b.status in(1,3,6,7,8)))" +
				" and (b.full_verifytime >= " + (DateUtils.getIntegralTime().getTime() / 1000) + 
				" and b.full_verifytime <= " + (DateUtils.getLastIntegralTime().getTime() / 1000) + ")";
			logger.debug("SQL语句：" + sql);
			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
			double sum = 0;
			if (rs.next()) {
				sum = rs.getDouble("award");
			}
			return sum;
		}
		
		/**
		 * 各标种投标总额
		 * @return
		 */
		public List<AccountTypeModel> getBorrowTypeAccount() {
			String sql = "select sum(bt.account) account, sum(bt.interest) interest, " +
				"case when b.type="+Constant.TYPE_MORTGAGE+" then '抵押标' when b.type="+Constant.TYPE_FLOW+" then '流转标' " +
				"when b.type="+Constant.TYPE_SECOND+" then '秒标' when b.type="+Constant.TYPE_PROPERTY+" then '净值标' when b.type="+Constant.TYPE_CREDIT+" then '信用标' " +
				"when b.type="+Constant.TYPE_OFFVOUCH+" then '线下担保标' end `type` from dw_borrow b " +
				"left join dw_borrow_tender bt on bt.borrow_id = b.id " +
				"where (b.status in (3,6,7,8) or (b.type="+Constant.TYPE_FLOW+" and b.status in(1,3,6,7,8))) " +
				"and (b.full_verifytime >= " + (DateUtils.getIntegralTime().getTime() / 1000) +
				" and b.full_verifytime <= " + (DateUtils.getLastIntegralTime().getTime() / 1000) + ") group by `type`";
			logger.debug("SQL语句：" + sql);
			List<AccountTypeModel> list = new ArrayList<AccountTypeModel>();
			list = this.getJdbcTemplate().query(sql, getBeanMapper(AccountTypeModel.class));
			return list;
		}
		
		/**
		 * 各标种奖励总额
		 * @return
		 */
		public List<AccountTypeModel> getBorrowAwardAccount() {
			String sql = "select sum(case when b.award=1 then b.account*(b.part_account/100) when b.award= 2 then b.funds end) award, " +
				"case when b.type="+Constant.TYPE_MORTGAGE+" then '抵押标' when b.type="+Constant.TYPE_FLOW+" then '流转标' " +
				"when b.type="+Constant.TYPE_SECOND+" then '秒标' when b.type="+Constant.TYPE_PROPERTY+" then '净值标' when b.type="+Constant.TYPE_CREDIT+" then '信用标' " +
				"when b.type="+Constant.TYPE_OFFVOUCH+" then '线下担保标' end `type` from dw_borrow b " +
				"where (b.status in(3,6,7,8) or (b.type="+Constant.TYPE_FLOW+" and b.status in(1,3,6,7,8))) " +
				"and (b.full_verifytime > " + (DateUtils.getIntegralTime().getTime() / 1000) +
				" and b.full_verifytime < " + (DateUtils.getLastIntegralTime().getTime() / 1000) + ") group by `type`";
			logger.debug("SQL语句：" + sql);
			List<AccountTypeModel> list = new ArrayList<AccountTypeModel>();
			list = this.getJdbcTemplate().query(sql, getBeanMapper(AccountTypeModel.class));
			return list;
		}
		
		@Override
		public int getApplyBorrowCount() {
			String sql ="select count(1) from dw_borrow where type<>" + Constant.TYPE_SECOND;
			logger.debug("aplyBorrowCount:"+sql);
			return getJdbcTemplate().queryForInt(sql);
		}
		
		@Override
		public double getApplyBorrowTotal() {
			String sql="select sum(account) as num from dw_borrow where type<>" + Constant.TYPE_SECOND+ " and type<>" + Constant.TYPE_PROPERTY + " ";
			double total=0;
			total=sum(sql);
			return total;
		}
		@Override
		public Map getUserTenderNum(long userid, String biao_type,
				Date beginDate, Date endDate) {
			String sql="select count(ba.id) as tender_num, sum(ba.account) as tender_account from dw_borrow_tender ba, dw_borrow bw " +
						"where ba.user_id = ? and ba.borrow_id = bw.id and bw.type="+Constant.TYPE_SECOND+" and(bw.status in (1, 3, 6, 8)) and ba.addtime between ? and ? ";
			logger.debug("SQL:" + sql);
			long bd = DateUtils.getTime(DateUtils.getFirstSecIntegralTime(beginDate));
			long ed = DateUtils.getTime(DateUtils.getLastSecIntegralTime(endDate));
			return getJdbcTemplate().queryForMap(sql, new Object[] { userid, bd, ed });
		}
		
		// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//		@Override
//		public double getTodayBorrowTotal() {
//			String sql = "select sum(account) as num from dw_borrow where type <> "+Constant.TYPE_SECOND+" and status in (1,3,6,7,8)" +
//					" and (verify_time > " + (DateUtils.getIntegralTime().getTime() / 1000) + 
//					" and verify_time < " + (DateUtils.getNowTimeStr()) + ")";
//				logger.debug("SQL语句：" + sql);
//				SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
//				double sum = 0;
//				if (rs.next()) {
//					sum = rs.getDouble("num");
//				}
//				return sum;
//		}
//		@Override
//		public double getTodayInvestTotal() {
//			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 start
//			String sql = "select sum(bt.account) as num from dw_borrow_tender as bt left join dw_borrow as b on b.id = bt.borrow_id " +
//					"where b.type<>"+Constant.TYPE_SECOND+" and ((b.status in (3, 6, 7, 8) and b.type<>"+Constant.TYPE_FLOW+" " +
//					"and (b.full_verifytime > " + (DateUtils.getIntegralTime().getTime() / 1000) + 
//					" and b.full_verifytime < " + (DateUtils.getNowTimeStr()) + " )) " +
//					"or ( b.type="+Constant.TYPE_FLOW+" and b.status in (1, 3, 6, 7, 8) " +
//					"and (bt.addtime > " + (DateUtils.getIntegralTime().getTime() / 1000) + 
//					" and bt.addtime < " + (DateUtils.getNowTimeStr()) + ")))";
//			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 end
//			logger.debug("SQL语句：" + sql);
//			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
//			double sum = 0;
//			if (rs.next()) {
//				sum = rs.getDouble("num");
//			}
//			return sum;
//		}
//		@Override
//		public int getTodayDealCount() {
//			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 start
//			String sql = "select count(*) from dw_borrow_tender as bt left join dw_borrow as b on b.id = bt.borrow_id " +
//					"where b.type<>"+Constant.TYPE_SECOND+" and ((b.status in (3, 6, 7, 8) and b.type<>"+Constant.TYPE_FLOW+" " +
//					"and (b.full_verifytime > " + (DateUtils.getIntegralTime().getTime() / 1000) + 
//					" and b.full_verifytime < " + (DateUtils.getNowTimeStr()) + " )) " +
//					"or ( b.type="+Constant.TYPE_FLOW+" and b.status in (1, 3, 6, 7, 8) " +
//					"and (bt.addtime > " + (DateUtils.getIntegralTime().getTime() / 1000) + 
//					" and bt.addtime < " + (DateUtils.getNowTimeStr()) + ")))";
//			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 end
//			logger.debug("getTodayDealCount():" + sql);
//			return this.getJdbcTemplate().queryForInt(sql);
//		}
//		@Override
//		public double getInvestTotal() {
//			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 start
//			String sql = "select sum(account_yes) as num from dw_borrow_tender as bt left join dw_borrow as b on b.id = bt.borrow_id " +
//					"where b.type <> " + Constant.TYPE_SECOND + " and ((b.status in (3, 6, 7, 8) and b.type <> " + Constant.TYPE_FLOW  +
//					" and b.full_verifytime < " + (DateUtils.getIntegralTime().getTime() / 1000) + " ) " +
//					"or ( b.type = " + Constant.TYPE_FLOW + " and b.status in (1, 3, 6, 7, 8) " +
//					" and bt.addtime < " + (DateUtils.getIntegralTime().getTime() / 1000) + "))";
//			// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 end
//			logger.debug("getInvestTotal():" + sql);
//			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
//			double sum = 0;
//			if(rs.next()){
//				sum = rs.getDouble("num");
//			}
//			return sum;
//		}
		
		// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
		/*@Override
		public double getInterestTotal() {
			String sql =" select sum(bt.interest) as sum "+
					" from dw_borrow_tender as bt left join dw_borrow as b on b.id = bt.borrow_id where b.is_mb <> 1  and "+
					" ( ( b.status in (3, 6, 7, 8)  and b.is_flow <> 1) or "+
					" ( b.is_flow = 1 and b.status in (1, 3, 6, 7, 8)and (bt.addtime < "+(DateUtils.getIntegralTime().getTime() / 1000)+")))";
			logger.debug("getInterestTotal():"+ sql );
			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
			double sum = 0;
			if(rs.next()){
				sum = rs.getDouble("sum");
			}
			return sum;
		}*/
//		@Override
//		public List getList(BorrowModel model,String[] borrows) {
//			// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
////			String getSql=getSql(borrows);
////			String sql = "select  p1.*,p2.username,p8.name as user_area ,u.username as kefu_username,p2.qq," +
////					"p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales," +
////					"p7.name as usetypename,p2.realname,(CASE "+getSql+" END) AS biaozhong_status  ";
//			String sql = "select  p1.*,p2.username,p8.name as user_area ,u.username as kefu_username,p2.qq," +
//					"p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales," +
//					"p7.name as usetypename,p2.realname ";
//			// v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
//			String fromSql=" from dw_borrow as p1 "
//					+ "left join dw_user as p2 on p1.user_id=p2.user_id "
//					+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
//					+ "left join dw_user as u on u.user_id=uca.kefu_userid "
//					+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
//					+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
//					+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
//					// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 start 
//					//+ "left join dw_daizi as p6 on p1.id=p6.borrow_id "
//					// v1.6.6.2 dw_daizi删除 lhm 2013-10-22 end
//					+ "left join dw_linkage p7 on p1.use=p7.id and p7.type_id=19 "
//					+ "left join dw_area p8 on p2.province=p8.id "
//					+ "where 1=1 ";
//			 
//			// 拼装SQL
//			String connectSql=connectSqlWithNoLimit(model);
//			sql = sql+fromSql + connectSql;
//			logger.debug("SQL:" + sql);
//			List<UserBorrowModel> list = new ArrayList<UserBorrowModel>();
//			try {
//				list = this.getJdbcTemplate().query(sql, getBeanMapper(UserBorrowModel.class));
//					
//			} catch (DataAccessException e) {
//				logger.debug(e.getMessage());
//				e.printStackTrace();
//			}
//			return list;
//		}
		@Override
		public List getAllBorrowList(SearchParam param) {
			StringBuffer sb=new StringBuffer(queryAllBorrowSql);
			String searchSql= param.getSearchParamSql();
			String limitSql=" order by p1.addtime desc ";
			String sql=sb.append(searchSql).append(limitSql).toString();
			logger.debug("getAllBorrowList():"+sql);
			List list=getJdbcTemplate().query(sql, new Object[]{} , getBeanMapper(UserBorrowModel.class));
			return list;
		}
		
		// 从BorrowFlowDaoImpl迁移过来
	@Override
	public long getBorrowFlowCountByUserId(long user_id) {
		String sql = "SELECT count(*) FROM dw_borrow WHERE (STATUS=2 OR STATUS=4 OR STATUS=5) AND user_id =?";

		return getJdbcTemplate().queryForLong(sql, new Object[] { user_id });
	}
	
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 start（无分页）
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Borrow> getSuccessBorrowList(long user_id) {
		String sql = "select * from dw_borrow p1 where p1.user_id=? "
				+ "and (p1.type <> 110 and p1.status in (3,6,7,8)) "
				// v1.6.7.2 RDPROJECT-564 zza 2013-12-11 start
				+ "or (p1.type = 110 and p1.status in (1,8)) order by p1.id desc";
				// v1.6.7.2 RDPROJECT-564 zza 2013-12-11 end
		logger.debug("SQL:" + user_id);
		List<Borrow> list = new ArrayList<Borrow>();
		list = this.getJdbcTemplate().query(sql, new Object[] { user_id }, getBeanMapper(Borrow.class));
		return list;
	}
	// v1.6.7.1 RDPROJECT-356 zza 2013-11-19 end
	
	
	//根据borrow_id得到当前所有投标金额
	//v1.6.7.2RDPROJECT-510 cx 2013-12-13 start
	public double getSumAccountByBorrowId(long borrow_id){
		String borrowSql="select sum(t.account) as total from dw_borrow_tender t left join dw_borrow_collection collection on t.id=collection.tender_id left join dw_user u on u.user_id=t.user_id left join dw_borrow borrow on t.borrow_id = borrow.id where t.borrow_id="+borrow_id;
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(borrowSql);
		if(list!=null && list.size()>0){
			Map<String,Object> map=list.get(0);
			if(map.get("total")!=null){
				return Double.parseDouble(map.get("total").toString());
			}
		}
		return 0;
	}
	
	//投标总金额  full_verifytime
	public double getAllTenderMoneyByDate(String startDate,String endDate,String validTime,String isday,Integer type){
		String sql = "select sum(b.account) as total from dw_borrow a,dw_borrow_tender b where a.id=b.borrow_id and (a.status in (3,6,7,8) or (a.type="+Constant.TYPE_FLOW+" and a.status in(1,3,6,7,8)))" +
				" and b.addtime between '"+startDate+"' and '"+endDate+"'";
		if(!StringUtils.isEmpty(isday)){
			if(("0").equals(isday)){  //月标
				sql+=" and a.time_limit="+validTime+" and isday=0";
			}else if(("1").equals(isday)){  //天标
				sql+=" and a.time_limit_day="+validTime+" and isday=1";
			}
		}
		if(type!=null){
			sql+=" and a.type="+type;
		}
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		double totalMoney = 0;
		if (rs.next()) {
			totalMoney = rs.getDouble("total");
		}
		return totalMoney;
	}
	
	//发标笔数
	public Integer getCountBorrowByDate(String startDate,String endDate,String validTime,String isday,Integer type){
		String sql = "select count(1) as total from dw_borrow where (status in (3,6,7,8) or (type="+Constant.TYPE_FLOW+" and status in(1,3,6,7,8)))" +
				" and addtime between '"+startDate+"' and '"+endDate+"'";
		if(!StringUtils.isEmpty(isday)){
			if(("0").equals(isday)){  //月标
				sql+=" and time_limit="+validTime+" and isday=0";
			}else if(("1").equals(isday)){  //天标
				sql+=" and time_limit_day="+validTime+" and isday=1";
			}
		}
		if(type!=null){
			sql+=" and type="+type;
		}
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		Integer count = 0;
		if (rs.next()) {
			count = rs.getInt("total");
		}
		return count;
	}
	
	//投标笔数
	public Integer getCountTenderBorrowByDate(String startDate,String endDate,String validTime,String isday){
		String sql="select count(1) as total from dw_borrow_tender a,dw_borrow b where (b.status in (3,6,7,8) or (type="+Constant.TYPE_FLOW+" and b.status in(1,3,6,7,8))) and a.borrow_id=b.id and a.addtime between '"+startDate+"' and '"+endDate+"'";
		if(!StringUtils.isEmpty(isday)){
			if(("0").equals(isday)){  //月标
				sql+=" and b.time_limit="+validTime+" and isday=0";
			}else if(("1").equals(isday)){  //天标
				sql+=" and b.time_limit_day="+validTime+" and isday=1";
			}
		}
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		Integer count = 0;
		if (rs.next()) {
			count = rs.getInt("total");
		}
		return count;
	}
	
	//投标人数
	public Integer getCountTenderUserByDate(String startDate,String endDate,String validTime,String isday){
		String sql="select count(distinct(a.user_id)) as total from dw_borrow_tender a,dw_borrow b where (b.status in (3,6,7,8) or (type="+Constant.TYPE_FLOW+" and b.status in(1,3,6,7,8))) and a.borrow_id=b.id and a.addtime between '"+startDate+"' and '"+endDate+"'";
		if(!StringUtils.isEmpty(isday)){
			if(("0").equals(isday)){  //月标
				sql+=" and b.time_limit="+validTime+" and isday=0";
			}else if(("1").equals(isday)){  //天标
				sql+=" and b.time_limit_day="+validTime+" and isday=1";
			}
		}
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		Integer count = 0;
		if (rs.next()) {
			count = rs.getInt("total");
		}
		return count;
	}
	
	// 0 月标  1 天标
	public Integer getBorrowTypeByIsDay(String startDate,String endDate,Integer isday,Integer type){
		String sql = "select count(1) as total from dw_borrow where (status in (3,6,7,8) or (type="+Constant.TYPE_FLOW+" and status in(1,3,6,7,8)))" +
					" and addtime between '"+startDate+"' and '"+endDate+"'";
		if(isday!=null){
			sql+=" and isday="+isday;
		}
		if(type!=null){
			sql+=" and type="+type;
		}
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		Integer count = 0;
		if (rs.next()) {
			count = rs.getInt("total");
		}
		return count;
	}
	//v1.6.7.2RDPROJECT-510 cx 2013-12-13 end
	
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
	@Override
	public List<FinancialData> getFinancialList(int type, int start, int num) {
		String sql = "SELECT t.* FROM dw_financial_data t WHERE t.type=:type "
				+ "ORDER BY t.repayment_time,t.id LIMIT :start,:num";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("start", start);
		map.put("num", num);
		return getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(FinancialData.class));
	}

	@Override
	public int getFinancialCount(int type) {
		String sql = "SELECT count(1) FROM dw_financial_data t WHERE t.type=:type";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		return getNamedParameterJdbcTemplate().queryForInt(sql, map);
	}
	
	@Override
	public Advanced getAdvanced() {
		String sql = "SELECT * FROM dw_advanced";
		List<Advanced> list = this.getJdbcTemplate().query(sql, getBeanMapper(Advanced.class));
		if  (list.size() > 0)  {
			return list.get(0);
		} else {
			return new Advanced();
		}
	}
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end
	
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 start
	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowModel> getJinBorrowListByStatusAndType(int status,
			String type) {
		String sql = "select * from dw_borrow where status=? and type=? and UNIX_TIMESTAMP(NOW()) - verify_time > valid_time*86400";
		List<BorrowModel> list = new ArrayList<BorrowModel>();
		list = this.getJdbcTemplate().query(sql, new Object[] { status, type }, getBeanMapper(BorrowModel.class));
		return list;
	}
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 end
	
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-19 start
	@Override
	public double getCollectionByBorrowTimeLimit(int tender_days) {
		String sql = "SELECT SUM(c.repay_account) FROM dw_borrow b ,dw_borrow_collection c WHERE "
				+ "c.borrow_id = b.id AND b.isday = 1 AND b.`status` IN (3,6,7) AND b.time_limit_day >= :tender_days AND c.`status` = 0";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("tender_days", tender_days);
		double collectionMoney = getNamedParameterJdbcTemplate().queryForLong(sql, map);
		return collectionMoney;
	}
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-19 end
}
