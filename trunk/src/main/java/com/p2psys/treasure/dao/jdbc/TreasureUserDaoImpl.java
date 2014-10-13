package com.p2psys.treasure.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.treasure.dao.TreasureUserDao;
import com.p2psys.treasure.domain.TreasureUser;
import com.p2psys.treasure.model.TreasureUserModel;

@Repository
public class TreasureUserDaoImpl extends BaseDaoImpl implements TreasureUserDao {

	@Override
	public long addTreasure(TreasureUser item) {
		// TODO Auto-generated method stub
		return this.insertHoldKey(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreasureUser getTreasureUserByUserId(long user_id) {
		StringBuffer sql = new StringBuffer("select p1.* from dw_treasure_user p1 where user_id = :user_id ");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, this.getBeanMapper(TreasureUser.class)); 
	}

	@Override
	public boolean editTreasureUser(double total,double interest_total,double  use_moeny ,long user_id) {
		StringBuffer sql = new StringBuffer("update dw_treasure_user set total = ifnull(total,0) + :total , interest_total = ifnull(interest_total,0) + :interest_total");
		sql.append(", use_moeny = ifnull(use_moeny,0)+ :use_moeny where user_id = :user_id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("interest_total", interest_total);
		map.put("use_moeny", use_moeny);
		map.put("user_id", user_id);
		int resurt = this.getNamedParameterJdbcTemplate().update(sql.toString(), map);
		if(resurt > 0) return true;
		return false;
	}
	
	/**
	 * 理财宝用户信息table分页数
	 * @param map
	 * @return
	 */
	public int getTreasureUserCount(Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("select count(p1.id) from dw_treasure_user p1 , dw_user p2 where p1.user_id = p2.user_id");
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}
	
	/**
	 * 理财宝用户信息table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreasureUserModel> getTreasureUserPage(int page, int max,Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("select p1.* , p2.username from dw_treasure_user p1 , dw_user p2 where p1.user_id = p2.user_id");
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		sql.append(" order by p1.id desc limit :page , :max");
		map.put("page", page);
		map.put("max", max);
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(TreasureUserModel.class));
	}

}
