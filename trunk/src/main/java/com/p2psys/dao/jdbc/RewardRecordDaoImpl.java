package com.p2psys.dao.jdbc;
//v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
//新增类
//v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.p2psys.dao.RewardRecordDao;
import com.p2psys.domain.RewardRecord;
import com.p2psys.model.RewardRecordModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;

public class RewardRecordDaoImpl extends BaseDaoImpl implements RewardRecordDao{
	
	@Override
	public void save(byte type, long fk_id, double account, long rewardUserId, long pasiveUserId) {
		RewardRecord rr=new RewardRecord();
		rr.setAddtime(DateUtils.getNowTime());;
		rr.setType(type);
		rr.setFk_id(fk_id);
		rr.setReward_account(account);
		rr.setReward_user_id(rewardUserId);
		rr.setPassive_user_id(pasiveUserId);
		insert(rr);
	}
	
	@Override
	public int getCount(SearchParam param) {
		StringBuffer sql = new StringBuffer("select count(1) from dw_reward_record as p1 ");
		sql.append("left join dw_user as p2 on p2.user_id = p1.reward_user_id ");
		sql.append("left join dw_user as us on us.user_id = p1.passive_user_id where 1=1 ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p2.username like concat('%',:username,'%')"); 
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.addtime >= :dotime1");
				map.put("dotime1", param.getSearchTime(param.getDotime1(),0));
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.addtime <= :dotime2");
				map.put("dotime2", param.getSearchTime(param.getDotime2(),1));
			}
			if (!StringUtils.isBlank(param.getType()) && !"0".equals(param.getType())) {
				sql.append(" and p1.type = :type");
				map.put("type", Integer.parseInt(param.getType()));
			}
			if (!StringUtils.isBlank(param.getPassive_username())) {
				sql.append(" and us.username like concat('%',:passive_username,'%')"); 
				map.put("passive_username", param.getPassive_username());
			}
		}
		return getNamedParameterJdbcTemplate().queryForInt(sql.toString(),map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RewardRecordModel> getRewardRecordList(SearchParam param, int start, int pernum) {
		StringBuffer sql = new StringBuffer("select *,p1.addtime addtime, p2.username, us.username as passive_username from dw_reward_record as p1 ");
		sql.append("left join dw_user as p2 on p2.user_id = p1.reward_user_id ");
		sql.append("left join dw_user as us on us.user_id = p1.passive_user_id where 1=1 ");
		Map<String, Object> map = new HashMap<String, Object>();
		if (param != null) {
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p2.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.addtime >= :dotime1");
				map.put("dotime1", param.getSearchTime(param.getDotime1(),0));
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.addtime <= :dotime2");
				map.put("dotime2", param.getSearchTime(param.getDotime2(),1));
			}
			if (!StringUtils.isBlank(param.getType()) && !"0".equals(param.getType())) {
				sql.append(" and p1.type = :type");
				map.put("type", Integer.parseInt(param.getType()));
			}
			if (!StringUtils.isBlank(param.getPassive_username())) {
				sql.append(" and us.username like concat('%',:passive_username,'%')");
				map.put("passive_username", param.getPassive_username());
			}
		}
		sql.append(" order by p1.id desc ");
		if (pernum > 0) {// 分页查询，若为0，则取所有（导出报表时取所有）
			sql.append(" LIMIT :start,:pernum");
			map.put("start", start);
			map.put("pernum", pernum);
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(RewardRecordModel.class));
	}
	
	
}
