package com.p2psys.treasure.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.treasure.dao.TreasureCashDao;
import com.p2psys.treasure.domain.TreasureCash;
import com.p2psys.treasure.model.TreasureCashModel;

@Repository
public class TreasureCashDaoImpl extends BaseDaoImpl implements TreasureCashDao {

	@Override
	public int getTreasureCashCount(Map<String, Object> map) {
		StringBuffer sql = new StringBuffer("select count(p1.id) from dw_treasure_cash p1 , dw_user p2 where p1.user_id = p2.user_id");
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
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TreasureCashModel> getTreasureCashPage(int page, int max,Map<String, Object> map) {
		StringBuffer sql = new StringBuffer("select p1.* , p2.username from dw_treasure_cash p1 , dw_user p2 where p1.user_id = p2.user_id");
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
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		sql.append(" order by p1.id desc limit :page , :max");
		map.put("page", page);
		map.put("max", max);
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(TreasureCashModel.class));
	}

	@Override
	public long addTreasureCash(TreasureCash item) {
		// TODO Auto-generated method stub
		return this.insertHoldKey(item);
	}
	
	/**
	 * 查询转出信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TreasureCashModel getTreasureCash(long id){
		StringBuffer sql = new StringBuffer("select p1.* , p2.username from dw_treasure_cash p1 , dw_user p2 where p1.user_id = p2.user_id and p1.id = :id");
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("id", id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, this.getBeanMapper(TreasureCashModel.class));
	}

	/**
	 * 修改转出信息
	 * @param id
	 * @return
	 */
	public boolean eidtTreasureCash(Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("update dw_treasure_cash set status = :status");
		Long verify_time = (Long) map.get("verify_time");
		if(verify_time != null){
			sql.append(" ,verify_time = :verify_time");
		}
		Long verify_user_id = (Long) map.get("verify_user_id");
		if(verify_user_id != null){
			sql.append(" ,verify_user_id = :verify_user_id");
		}
		String remark = (String) map.get("remark");
		if(remark != null){
			sql.append(" ,remark = :remark");
		}
		String verify_user = (String) map.get("verify_user");
		if(verify_user != null){
			sql.append(" ,verify_user = :verify_user");
		}
		sql.append(" where id = :id");
		Byte status_ = (Byte) map.get("status_");
		if(status_ != null){
			sql.append(" and status = :status_");
		}
		int result = this.namedParameterJdbcTemplate.update(sql.toString(), map);
		if(result > 0) return true;
		return false;
		
	}
	
}
