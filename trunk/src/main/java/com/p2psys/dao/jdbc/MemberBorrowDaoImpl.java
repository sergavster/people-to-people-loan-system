package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.context.Constant;
import com.p2psys.dao.MemberBorrowDao;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserBorrowModel;

/**
 * 借款相关的Dao操作类
 * 
 
 * @date 2012-7-10-下午3:07:56
 * @version
 * 
 *   (c)</b> 2012-51-<br/>
 * 
 */
public class MemberBorrowDaoImpl extends BaseDaoImpl implements MemberBorrowDao {

	private static Logger logger = Logger.getLogger(MemberBorrowDaoImpl.class);

	/**
	 * 根据类型、排序、分页进行查找未发布的借款的数据
	 */
	public List getBorrowList(String type,long user_id, int start,int end, SearchParam param) {
		// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 start
//		String sql = "select  p1.*,p6.isqiye,p6.id as fastid,p2.username,p2.realname,p2.area as user_area ,u.username as kefu_username,p2.qq,p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales,p1.use as usetypename "
//				+ "from dw_borrow as p1 "
//				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
//				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
//				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
//				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
//				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
//				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
//				+ "left join dw_daizi as p6 on p1.id=p6.borrow_id where p1.user_id=? ";
		StringBuffer sql = new StringBuffer();
		if ("repayment".equals(type)) {
			//  v1.6.7.2 RDPROJECT-372 sj 2013-12-05 start
			sql.append(" SELECT  t2.*, t2.repayment_yesaccount / t2.repayment_account AS scales, t2.`use` AS usetypename, ");
			//  v1.6.7.2 RDPROJECT-372 sj 2013-12-05 end
			sql.append(" t1.repayment_time AS min_repayment_time");
			sql.append(" FROM");
			sql.append(" (SELECT p1.id,  min(p2.repayment_time)AS repayment_time FROM  dw_borrow AS p1");
			sql.append(" LEFT JOIN dw_borrow_repayment p2 ON p2.borrow_id = p1.id  AND p2.repayment_yestime IS NULL");
			sql.append(" WHERE  p1.user_id = ?");
			sql.append(this.getTypeSql(type));
			sql.append(param.getSearchParamSql());
			sql.append(" GROUP BY  p1.id");
			sql.append(" )t1");
			sql.append(" INNER JOIN dw_borrow t2 ON t1.id = t2.id");
			sql.append(" order by t2.addtime desc");
			sql.append(" limit " + start + "," + end);
		} else {
			//  v1.6.7.2 RDPROJECT-372 sj 2013-12-06 start
			// v1.6.7.2 RDPROJECT-553 zza 2013-12-16 start
			sql.append(" SELECT  p1.*, p1.account_yes / p1.account AS scales, p1.`use` AS usetypename ");
//			sql.append(" SELECT  p1.*, p1.repayment_yesaccount / p1.repayment_account AS scales, p1.`use` AS usetypename ");
			// v1.6.7.2 RDPROJECT-553 zza 2013-12-16 end
			//  v1.6.7.2 RDPROJECT-372 sj 2013-12-06 end
			sql.append(" FROM dw_borrow  p1 WHERE  p1.user_id = ?");
			sql.append(this.getTypeSql(type));
			sql.append(param.getSearchParamSql());
			sql.append(" order by p1.addtime desc");
			sql.append(" limit " + start + "," + end);
		}

		// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 end
		// 拼装SQL
		logger.debug("SQL:" + sql.toString());
		List<UserBorrowModel> list = new ArrayList();
		try {
			// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 start
			//list =getJdbcTemplate().query(sql, new Object[]{user_id},new UserBorrowMapper());
			list =getJdbcTemplate().query(sql.toString(), new Object[]{user_id},getBeanMapper(UserBorrowModel.class));
			// v1.6.6.2 RDPROJECT-151 lhm 2013-10-22 end
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查找未发布的借款的数据的数量
	 * @param type
	 * @param user_id
	 * @param param
	 * @return
	 */
	public int getBorrowCount(String type,long user_id, SearchParam param) {
		int total = 0;
		String sql = "select count(p1.id) "
				+ "from dw_borrow as p1 "
				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id where p1.user_id="+ user_id;
//				+ "left join dw_daizi as p6 on p1.id=p6.borrow_id where p1.user_id="+ user_id;
		logger.debug("SQL:" + sql);
		String typeSql = getTypeSql(type);
		String searchSql=param.getSearchParamSql();
		sql = sql + typeSql+searchSql;
		// 拼装SQL
		sql = sql + typeSql;
		logger.debug("SQL:" + sql);
		try {
			total = getJdbcTemplate().queryForInt(sql);
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return total;
	}
	
	
	/**
	 * 根据借款的类型拼装SQL
	 * 
	 * @param type type
	 * @return String
	 */
	private String getTypeSql(String type) {
		//V1.6.6.1 RDPROJECT-137 wcw 2013-09-26 start
		String typeSql = " and ((p1.type <> " + Constant.TYPE_FLOW + " and p1.status=1) or (p1.type="
				+ Constant.TYPE_FLOW + " and p1.account>p1.account_yes+0 and p1.status=1)) "; // 正在招标
		// V1.6.6.1 RDPROJECT-137 wcw 2013-09-26 end
		if ("unpublish".equals(type)) {
			typeSql = " and p1.status=0"; // 尚未发布的借款
		} else if ("repayment".equals(type)) {
			// v1.6.6.7 RDPROJECT-398 zza 2013-11-08 start
			typeSql = " and ((p1.type <> " + Constant.TYPE_FLOW
					+ " and p1.status in (3, 6, 7)) or (p1.type =" + Constant.TYPE_FLOW
					+ " and p1.status in (1,8) and p1.account_yes<>0))"; // 正在还款的借款
		} else if ("repaymentyes".equals(type)) {
			typeSql = " and ((p1.type <> " + Constant.TYPE_FLOW + " and p1.status=8) or (p1.type = "
					+ Constant.TYPE_FLOW + " and p1.account_yes=0 and p1.status=8))"; // 已还完的借款
			// v1.6.6.7 RDPROJECT-398 zza 2013-11-08 end
		}
		return typeSql;
	}
	
	/**
	 * 新加方法
	 */
	@Override
	// 标详情页面性能优化 lhm 2013/10/11 start
//	public List getRepamentList(String type, long user_id) {
//		String typeSql = getTypeSql(type);
//		String sql = "select  p1.*,p6.isqiye,p6.id as fastid,p2.username,p2.realname,p2.area as user_area ,u.username as kefu_username,p2.qq,p3.value as credit_jifen,p4.pic as credit_pic,p5.area as add_area,p1.account_yes/p1.account as scales,p1.use as usetypename "
//				+ "from dw_borrow as p1 "
//				+ "left join dw_user as p2 on p1.user_id=p2.user_id "
//				+ "left join dw_user_cache as uca on uca.user_id=p1.user_id "
//				+ "left join dw_user as u on u.user_id=uca.kefu_userid "
//				+ "left join dw_credit as p3 on p1.user_id=p3.user_id "
//				+ "left join dw_credit_rank as p4 on p3.value<=p4.point2  and p3.value>=p4.point1 "
//				+ "left join dw_userinfo as p5 on p1.user_id=p5.user_id "
//				+ "left join dw_daizi as p6 on p1.id=p6.borrow_id where p1.user_id=? ";
//		String orderSql = " order by p1.addtime desc";
//		sql = sql + typeSql + orderSql;
//		// 拼装SQL
//		logger.debug("SQL:" + sql);
//		List<UserBorrowModel> list = new ArrayList();
//		try {
//			list = getJdbcTemplate().query(sql, new Object[] { user_id },
//					new UserBorrowMapper());
//		} catch (DataAccessException e) {
//			logger.debug(e.getMessage());
//			e.printStackTrace();
//		}
//		return list;
//	}
	public long getRepamentCount(String type, long user_id) {
		String typeSql = getTypeSql(type);
		String sql = "select  count(*) from dw_borrow as p1 where p1.user_id=? ";
		sql = sql + typeSql;
		// 拼装SQL
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
	// 标详情页面性能优化 lhm 2013/10/11 end

	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 start
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<UserBorrowModel> getVerifyFailList(long user_id, int start, int end,
			SearchParam param, String type) {
		StringBuffer sql = new StringBuffer();
		// v1.6.7.2 RDPROJECT-553 zza 2013-12-16 start
		sql.append("SELECT p1.*, p1.account_yes / p1.account AS scales, p1.`use` AS usetypename FROM dw_borrow AS p1 WHERE p1.user_id = :user_id");
		// v1.6.7.2 RDPROJECT-553 zza 2013-12-16 end
		if ("verifyFail".equals(type)) {
			sql.append(" AND p1.`status` IN (4, 49,2)");
		} else {
			sql.append(" AND p1.`status` IN (5, 59)");
		}
		sql.append(param.getSearchParamSql());
		sql.append(" order by p1.addtime desc");
		sql.append(" limit :start, :end");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		map.put("start", start);
		map.put("end", end);
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(UserBorrowModel.class));
	}

	@Override
	public int getVerifyFailCount(long user_id, SearchParam param, String type) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(1) FROM dw_borrow AS p1 WHERE p1.user_id = :user_id");
		if ("verifyFail".equals(type)) {
			sb.append(" AND p1.`status` IN (49,2)");
		} else {
			sb.append(" AND p1.`status` IN (5, 59)");
		}
		String searchSql = param.getSearchParamSql();
		sb.append(searchSql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		try {
			return getNamedParameterJdbcTemplate().queryForInt(sql, map);
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
		}
		return 0;
	}
	// v1.6.7.2 RDPROJECT-471 zza 2013-11-29 end
}
