package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.BorrowAutoDao;
import com.p2psys.domain.AutoTenderOrder;
import com.p2psys.domain.BorrowAuto;

/**
 * 借款相关的Dao操作类
 * 
 
 * @date 2012-7-10-下午3:07:56
 * @version
 * 
 *           (c)</b> 2012-51-<br/>
 * 
 */
public class BorrowAutoDaoImpl extends BaseDaoImpl implements BorrowAutoDao {

	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(BorrowAutoDaoImpl.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getBorrowAutoList(long userId) {
		String sql = "select  p1.* from dw_borrow_auto as p1 where p1.user_id=? order by p1.`id` desc ";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + userId);
		List list = new ArrayList();
		list = this.getJdbcTemplate().query(sql, new Object[] { userId }, getBeanMapper(BorrowAuto.class));
		return list;
	}

	@Override
	public void deleteBorrowAuto(long userId) {
		String sql = "delete from dw_borrow_auto where user_id=?";
		this.getJdbcTemplate().update(sql, userId);
	}
	
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-15 start
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<AutoTenderOrder> getBorrowAutoListByStatus(long status) {
		String sql = "";
		if (status == 0) {
			sql = "select  p1.* from dw_borrow_auto as p1 "
					+ "inner join dw_auto_tender_order p2 on p1.user_id = p2.user_id " 
					+ "where p1.status = 1 order by p2.auto_order ";
		} else if (status == 1) {
			sql = "select p1.* from dw_borrow_auto as p1 where p1.status=1 order by p1.addtime ";
		}
		List<AutoTenderOrder> list = new ArrayList<AutoTenderOrder>();
		try {
			list = this.getJdbcTemplate().query(sql, getBeanMapper(BorrowAuto.class));
		} catch (Exception e) {
			logger.info("没有数据");
		}
		return list;
	}
	
	//自动投标排名
	@Override
	public void updateAutoTenderOrder(AutoTenderOrder auto) {
		String sql = "update dw_auto_tender_order set last_tender_time = :last_tender_time where user_id = :user_id ";
		logger.debug("SQL:" + sql);
		SqlParameterSource ps = new BeanPropertySqlParameterSource(auto);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public AutoTenderOrder getAutoTenderOrder(long userId) {
		String sql = "select  p1.* from dw_auto_tender_order as p1 where p1.user_id = :user_id order by p1.auto_order";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		List<AutoTenderOrder> list = this.getNamedParameterJdbcTemplate().query(sql, map,
				getBeanMapper(AutoTenderOrder.class));
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<AutoTenderOrder> getAutoTenderOrder() {
		List<AutoTenderOrder> autoTenderOrderList = new ArrayList<AutoTenderOrder>();
		String sql = "select p1.* from dw_auto_tender_order as p1 order by p1.auto_order";
		logger.debug("SQL:" + sql);
		try {
			autoTenderOrderList = this.getJdbcTemplate().query(sql, getBeanMapper(AutoTenderOrder.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return autoTenderOrderList;
	}
	
	@Override
	public int getAutoTenderOrderCount() {
		String sql = "select count(1) from dw_auto_tender_order as p1 "
				+ "where p1.addtime is not null order by p1.auto_order";
		return getJdbcTemplate().queryForInt(sql);
	}
	// v1.6.7.1 RDPROJECT-124 zza 2013-11-15 end
	
}
