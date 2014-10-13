package com.p2psys.report.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.report.dao.ReportTenderDao;
import com.p2psys.report.model.MonthTenderModel;

/**
 * 标相关的统计导出
 
 * @version 1.0
 * @since 2013-11-12
 */
@Repository
public class ReportTenderDaoImpl extends BaseDaoImpl implements ReportTenderDao {

	/**
	 * 每月用户投标数据统计
	 * @param map
	 * @return
	 */
	public int getMonthTenderCount(Map<String , Object> map){

		StringBuffer sql = new StringBuffer("select count(distinct p1.id) from dw_borrow_tender p1 inner join dw_borrow p2 on p2.id = p1.borrow_id inner join  dw_borrow_collection p3 on p1.id = p3.tender_id ");
		sql.append(" inner join dw_user p4 on p1.user_id = p4.user_id inner join dw_borrow_config p5 on p2.type = p5.id ");
		sql.append(" where ( (p2.type = 110 and p2.status in (1,8)) or ( p2.type != 110 and p2.status in (3,6,7,8) ) ) and p1.account > 0 ");
		
		if(map != null && map.size() > 0){
			long startTime = (Long) map.get("startTime");
			if(startTime > 0){
				sql.append(" and p1.addtime >= :startTime");
			}
			long endTime = (Long) map.get("endTime");
			if(endTime > 0){
				sql.append(" and p1.addtime <= :endTime");
			}
			int type = (Integer) map.get("type");
			if(type > 0){
				sql.append(" and p2.type = :type");
			}
			String username = (String) map.get("username");
			if(username != null && username.length() > 0){
				sql.append(" and p4.username like concat('%',:username,'%')");
			}
		}
		return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}
	
	/**
	 * 每月用户投标数据统计
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MonthTenderModel> getMonthTenderPage(int page, int max , Map<String , Object> map){
		StringBuffer sql = new StringBuffer("select distinct p1.id as tender_id , p2.id as id ,p2.name as name ,p4.username ,p1.account as account,p1.money as money ,p2.apr,p1.interest,");
		sql.append(" p1.repayment_account,if(p2.isday = 1 ,'是','否') as isday,p2.time_limit_day,p2.time_limit,p1.addtime,p5.name as type_name");
		sql.append(" from dw_borrow_tender p1 inner join dw_borrow p2 on p2.id = p1.borrow_id inner join  dw_borrow_collection p3 on p1.id = p3.tender_id ");
		sql.append(" inner join dw_user p4 on p1.user_id = p4.user_id inner join dw_borrow_config p5 on p2.type = p5.id ");
		sql.append(" where ( (p2.type = 110 and p2.status in (1,8)) or ( p2.type != 110 and p2.status in (3,6,7,8) ) ) and p1.account > 0 ");
		if(map != null && map.size() > 0){
			long startTime =  (Long) map.get("startTime");
			if(startTime > 0){	
				sql.append(" and p1.addtime >= :startTime");
			}
			long endTime = (Long) map.get("endTime");
			if(endTime > 0){
				sql.append(" and p1.addtime <= :endTime");
			}
			int type = (Integer) map.get("type");
			if(type > 0){
				sql.append(" and p2.type = :type");
			}
			String username = (String) map.get("username");
			if(username != null && username.length() > 0){
				sql.append(" and p4.username like concat('%',:username,'%')");
			}
		}
		sql.append(" order by p2.id desc");
		map.put("page", page);
		map.put("max", max);
		sql.append(" limit :page,:max");
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map,getBeanMapper(MonthTenderModel.class));
	}
}
