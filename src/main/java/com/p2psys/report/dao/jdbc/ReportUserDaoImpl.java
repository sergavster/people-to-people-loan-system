package com.p2psys.report.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.model.SearchParam;
import com.p2psys.report.dao.ReportUserDao;
import com.p2psys.report.model.ReportUserModel;

/**
 * 用户相关统计
 
 * @version 1.0
 * @since 2013-11-13
 */
@Repository
public class ReportUserDaoImpl extends BaseDaoImpl implements ReportUserDao {

	private static Logger logger = Logger.getLogger(ReportUserDaoImpl.class);
	
	@Override
	public int getUserCount(SearchParam p) {
		StringBuffer sql = new StringBuffer("select count(distinct p1.user_id) from dw_user as p1 left join dw_user_cache as p2 on p1.user_id=p2.user_id");
		sql.append(" left join dw_area as p3 on p3.id=p1.province left join dw_area as p4 on p4.id=p1.city");
        sql.append(" left join dw_borrow_tender p5 on p1.user_id = p5.user_id inner join dw_borrow p6 on p5.borrow_id = p6.id");
        sql.append(" and ( (p6.type = 110 and p6.status in (1,8)) or ( p6.type != 110 and p6.status in (3,6,7,8) ) ) where 1 = 1");
		sql.append(p.getSearchParamSql());
		logger.debug("getUserCount():"+sql);
		int total=count(sql.toString());
		return total;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportUserModel> getUserList(int start, int pernum,SearchParam p) {
		StringBuffer sql = new StringBuffer();
		sql.append("select p1.user_id , p1.username, p1.realname , p1.real_status, p1.email , p1.email_status, p1.phone , p1.phone_status , p2.vip_status,p2.vip_verify_time,");
		sql.append("p1.addtime ,concat(p3.name,' ',p4.name) as address ,count(p5.id) as tender_num ,sum(p5.account) as tender_sum , p1.birthday");
		sql.append(" from dw_user as p1 left join dw_user_cache as p2 on p1.user_id=p2.user_id");
		sql.append(" left join dw_area as p3 on p3.id=p1.province left join dw_area as p4 on p4.id=p1.city");
        sql.append(" left join dw_borrow_tender p5 on p1.user_id = p5.user_id inner join dw_borrow p6 on p5.borrow_id = p6.id");
        sql.append(" and ( (p6.type = 110 and p6.status in (1,8)) or ( p6.type != 110 and p6.status in (3,6,7,8) ) ) where 1 = 1");
        sql.append(p.getSearchParamSql());
        sql.append(" group by p1.user_id order by p1.addtime desc limit :page,:max");
        Map<String , Object> map = new HashMap<String, Object>();
        map.put("page", start);
		map.put("max", pernum);
		
		try {
			return this.getNamedParameterJdbcTemplate().query(sql.toString(), map,getBeanMapper(ReportUserModel.class));
		} catch (DataAccessException e) {
			logger.error("getUserList():"+sql);
		}
		return null;
	}
	
	/**
	 * 查询用户本月新注册数和本月用户手机认证数等等
	 * @param map
	 * @return
	 */
	public int getMonthNewRegister(Map<String, Object> map){
		StringBuffer sql = new StringBuffer("SELECT COUNT(DISTINCT p1.user_id) FROM  dw_user p1");
		sql.append(" left join dw_user_cache p2 on p2.user_id = p1.user_id WHERE 1 = 1");
		if(map != null && map.size() > 0){
			Long start_time =  (Long) map.get("start_time");
			if(start_time != null && start_time > 0){	
				sql.append(" and p1.addtime >= :start_time");
			}
			Long end_time = (Long) map.get("end_time");
			if(end_time != null && end_time > 0){
				sql.append(" and p1.addtime <= :end_time");
			}
			Integer real_status = (Integer) map.get("real_status");
			if(real_status != null && real_status > 0){
				sql.append(" and p1.real_status = :real_status");
			}
			Integer phone_status = (Integer) map.get("phone_status");
			if(phone_status != null && phone_status > 0){
				sql.append(" and p1.phone_status = :phone_status");
			}
			Integer vip_status = (Integer) map.get("vip_status");
			if(vip_status != null && vip_status > 0){
				sql.append(" and p2.vip_status = :vip_status");
			}
		}
		try {
			return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
		} catch (DataAccessException e) {
			logger.error("getMonthNewRegister():"+sql);
		}
		return 0;
	}
	
	/**
	 * 查询本月新注册会员有多少人投标
	 * @param map
	 * @return
	 */
	public int getNewRegisterTender(Map<String, Object> map){
		
		StringBuffer sql = new StringBuffer("select count(distinct p1.user_id) from dw_borrow_tender p1 left join  dw_user p2  on p1.user_id = p2.user_id  WHERE 1 = 1");
		if(map != null && map.size() > 0){
			Long start_time =  (Long) map.get("start_time");
			if(start_time != null && start_time > 0){	
				sql.append(" and p2.addtime >= :start_time");
			}
			Long end_time = (Long) map.get("end_time");
			if(end_time != null && end_time > 0){
				sql.append(" and p2.addtime <= :end_time");
			}
			Integer real_status = (Integer) map.get("real_status");
			if(real_status != null && real_status > 0){
				sql.append(" and p2.real_status = :real_status");
			}
			Integer phone_status = (Integer) map.get("phone_status");
			if(phone_status != null && phone_status > 0){
				sql.append(" and p2.phone_status = :phone_status");
			}
		}
		try {
			return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
		} catch (DataAccessException e) {
			logger.error("getNewRegisterTender():"+sql);
		}
		return 0;
	}
	
}
