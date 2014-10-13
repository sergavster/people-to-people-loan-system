package com.p2psys.treasure.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.treasure.dao.TreasureRechargeDao;
import com.p2psys.treasure.domain.TreasureRecharge;
import com.p2psys.treasure.model.TreasureRechargeModel;

/**
 * 理财宝转入信息DAO实现类
 
 * @version 1.0
 * @since 2013-11-30
 */
@Repository
public class TreasureRechargeDaoImpl extends BaseDaoImpl implements TreasureRechargeDao {

	/**
	 * 添加理财宝转入信息
	 * @param item
	 * @return
	 */
	public long addTreasureRecharge(TreasureRecharge item){
		return this.insertHoldKey(item);
	}
	
	/**
	 * 理财宝转入信息table分页数
	 * @param map
	 * @return
	 */
	public int getTreasureRechargeCount(Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("select count(p1.id) from dw_treasure_recharge p1 , dw_user p2 ,dw_treasure p3 where p1.user_id = p2.user_id and p1.treasure_id = p3.id");
		Long start_time =  (Long) map.get("start_time");
		if(start_time != null && start_time > 0){	
			sql.append(" and p1.add_time >= :start_time");
		}
		Long end_time = (Long) map.get("end_time");
		if(end_time != null && end_time > 0){
			sql.append(" and p1.add_time <= :end_time");
		}
		Long user_id = (Long) map.get("user_id");
		if(user_id != null && user_id > 0){
			sql.append(" and p1.user_id = :user_id");
		}
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		Long treasure_id = (Long) map.get("treasure_id");
		if(treasure_id != null && treasure_id > 0){
			sql.append(" and p1.treasure_id = :treasure_id");
		}
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		String name = (String) map.get("name");
		if(name != null && name.length() > 0){
			sql.append(" and p3.name like concat('%',:name,'%')");
		}
		return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}
	
	/**
	 * 理财宝转入信息table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreasureRechargeModel> getTreasureRechargePage(int page, int max,Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("select p1.* , p2.username,p3.name from dw_treasure_recharge p1 , dw_user p2 ,dw_treasure p3 where p1.user_id = p2.user_id and p1.treasure_id = p3.id");
		Long start_time =  (Long) map.get("start_time");
		if(start_time != null && start_time > 0){	
			sql.append(" and p1.add_time >= :start_time");
		}
		Long end_time = (Long) map.get("end_time");
		if(end_time != null && end_time > 0){
			sql.append(" and p1.add_time <= :end_time");
		}
		Long user_id = (Long) map.get("user_id");
		if(user_id != null && user_id > 0){
			sql.append(" and p1.user_id = :user_id");
		}
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		Long treasure_id = (Long) map.get("treasure_id");
		if(treasure_id != null && treasure_id > 0){
			sql.append(" and p1.treasure_id = :treasure_id");
		}
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		String name = (String) map.get("name");
		if(name != null && name.length() > 0){
			sql.append(" and p3.name like concat('%',:name,'%')");
		}
		sql.append(" order by p1.id desc limit :page , :max");
		map.put("page", page);
		map.put("max", max);
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(TreasureRechargeModel.class));
	}
	
	/**
	 * 查询理财宝转入信息
	 * @param id
	 * @return
	 */
	public TreasureRecharge getTreasureRecharge(long id){
		return (TreasureRecharge) this.findById(TreasureRecharge.class, id);
	}
	/**
	 * 修改理财宝转入信息
	 * @param money 转出额度
	 * @param id
	 * @return
	 */
	public boolean editRechargeUserMoney(double money, long id){
		StringBuffer sql = new StringBuffer("update dw_treasure_recharge set use_money = ifnull(use_money,0)+ :money where status = 1 and id = :id ");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("money", money);
		map.put("id", id);
		int result = this.namedParameterJdbcTemplate.update(sql.toString(), map);
		if(result > 0) return true;
		return false;
	}
	
	/**
	 * 修改理财宝转入信息
	 * @param status 转入信息状态
	 * @param id
	 * @return
	 */
	public boolean editRechargeStatus(byte status, long id){
		StringBuffer sql = new StringBuffer("update dw_treasure_recharge set status = :status where id = :id ");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("id", id);
		int result = this.namedParameterJdbcTemplate.update(sql.toString(), map);
		if(result > 0) return true;
		return false;
	}
	
	/**
	 * 理财宝资金转入信息分页
	 * @param map 查询参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreasureRechargeModel> getTreasureRechargeList(Map<String , Object> map){
		StringBuffer sql = new StringBuffer("select p1.* , p2.username,p3.name from dw_treasure_recharge p1 , dw_user p2 ,dw_treasure p3 where p1.user_id = p2.user_id and p1.treasure_id = p3.id");
		Long start_time =  (Long) map.get("start_time");
		if(start_time != null && start_time > 0){	
			sql.append(" and p1.add_time >= :start_time");
		}
		Long end_time = (Long) map.get("end_time");
		if(end_time != null && end_time > 0){
			sql.append(" and p1.add_time <= :end_time");
		}
		Long user_id = (Long) map.get("user_id");
		if(user_id != null && user_id > 0){
			sql.append(" and p1.user_id = :user_id");
		}
		Long treasure_id = (Long) map.get("treasure_id");
		if(treasure_id != null && treasure_id > 0){
			sql.append(" and p1.treasure_id = :treasure_id");
		}
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		String name = (String) map.get("name");
		if(name != null && name.length() > 0){
			sql.append(" and p3.name like concat('%',:name,'%')");
		}
		sql.append(" order by p1.id desc");
		Integer size = (Integer) map.get("size");
		if(size != null && size > 0){
			sql.append(" limit :size");
		}
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(TreasureRechargeModel.class));
	}

	/**
	 * 修改理财宝转入信息
	 * @param map 修改信息
	 * @return
	 */
	public boolean editRecharge(Map<String , Object> map){
		StringBuffer sql = new StringBuffer("update dw_treasure_recharge set update_time = :update_time");
		Double interest = (Double) map.get("interest");
		if(interest != null){
			sql.append(" ,interest = :interest");
		}
		Double fee = (Double) map.get("fee");
		if(fee != null){
			sql.append(" ,fee = :fee");
		}
		Double use_interest = (Double) map.get("use_interest");
		if(use_interest != null){
			sql.append(" ,use_interest = :use_interest");
		}
		Long tender_day = (Long) map.get("tender_day");
		if(tender_day != null){
			sql.append(" ,tender_day = :tender_day");
		}
		Long interest_end_time = (Long) map.get("interest_end_time");
		if(interest_end_time != null){
			sql.append(" ,interest_end_time = :interest_end_time");
		}
		sql.append(" where id = :id");
		int result = this.namedParameterJdbcTemplate.update(sql.toString(), map);
		if(result > 0) return true;
		return false;
	}
	
	/**
	 * 用户投资金额查询
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public double getRechargeMoney(Map<String , Object> map){
		StringBuffer sql = new StringBuffer("select sum(p1.use_money) as num from dw_treasure_recharge p1 where 1 = 1");
		List<Byte> status =  (List<Byte>) map.get("status");
		if(status != null && status.size() > 0){
			sql.append(" and p1.status in ( :status )");
		}
		Long user_id = (Long) map.get("user_id");
		if(user_id != null && user_id > 0){
			sql.append(" and p1.user_id =  :user_id ");
		}
		Long treasure_id = (Long) map.get("treasure_id");
		if(treasure_id != null && treasure_id > 0){
			sql.append(" and p1.treasure_id =  :treasure_id ");
		}
		double sum = 0.0;
		SqlRowSet rs = this.getNamedParameterJdbcTemplate().queryForRowSet(sql.toString(), map);
		if (rs.next()) {
			sum = rs.getDouble("num");
		}
		return sum;
	}
	
	
}
