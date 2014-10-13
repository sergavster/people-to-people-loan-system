package com.p2psys.treasure.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.treasure.dao.TreasureDao;
import com.p2psys.treasure.domain.Treasure;
import com.p2psys.treasure.model.TreasureModel;

/**
 * 理财宝信息DAO接口实现类
 
 * @version 1.0
 * @since 2013-11-27
 */
@Repository
public class TreasureDaoImpl extends BaseDaoImpl implements TreasureDao {

	/**
	 * 理财宝信息table分页数
	 * @param map
	 * @return
	 */
	public int getTreasureCount(Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("select count(p1.id) from dw_treasure p1 , dw_user p2 where p1.user_id = p2.user_id");
		String name = (String) map.get("name");
		if(name != null && name.length() > 0){
			sql.append(" and p1.name like concat('%',:name,'%')");
		}
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		Byte audit_status = (Byte) map.get("audit_status");
		if(audit_status != null && audit_status >= 0){
			sql.append(" and p1.audit_status = :audit_status");
		}
		Byte style = (Byte)map.get("style");
		if(style != null && style >= 0){
			sql.append(" and p1.style = :style");
		}
		Byte back_verify_type = (Byte)map.get("back_verify_type");
		if(back_verify_type != null && back_verify_type >= 0){
			sql.append(" and p1.back_verify_type = :back_verify_type");
		}
		return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}
	
	/**
	 * 理财宝信息table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreasureModel> getTreasurePage(int page, int max,Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("select p1.* , p2.username from dw_treasure p1 , dw_user p2 where p1.user_id = p2.user_id");
		String name = (String) map.get("name");
		if(name != null && name.length() > 0){
			sql.append(" and p1.name like concat('%',:name,'%')");
		}
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		Byte audit_status = (Byte) map.get("audit_status");
		if(audit_status != null && audit_status >= 0){
			sql.append(" and p1.audit_status = :audit_status");
		}
		Byte style = (Byte)map.get("style");
		if(style != null && style >= 0){
			sql.append(" and p1.style = :style");
		}
		Byte back_verify_type = (Byte)map.get("back_verify_type");
		if(back_verify_type != null && back_verify_type >= 0){
			sql.append(" and p1.back_verify_type = :back_verify_type");
		}
		sql.append(" order by p1.update_time desc limit :page , :max");
		map.put("page", page);
		map.put("max", max);
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(TreasureModel.class));
	}

	/**
	 * 根据map值理财宝信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TreasureModel> getTreasure(Map<String , Object> map){
		
		StringBuffer sql = new StringBuffer("select p1.* , p2.username from dw_treasure p1 , dw_user p2 where p1.user_id = p2.user_id");
		Long id = (Long) map.get("id");
		if(id != null && id > 0){
			sql.append(" and p1.id = :id");
		}
		String name = (String) map.get("name");
		if(name != null && name.length() > 0){
			sql.append(" and p1.name like concat('%',:name,'%')");
		}
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		Byte audit_status = (Byte) map.get("audit_status");
		if(audit_status != null && audit_status >= 0){
			sql.append(" and p1.audit_status = :audit_status");
		}
		Byte style = (Byte)map.get("style");
		if(style != null && style >= 0){
			sql.append(" and p1.style = :style");
		}
		Byte back_verify_type = (Byte)map.get("back_verify_type");
		if(back_verify_type != null && back_verify_type >= 0){
			sql.append(" and p1.back_verify_type = :back_verify_type");
		}
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(TreasureModel.class));
	}
	
	/**
	 * 根据主键ID查询理财宝信息
	 * @param id
	 * @return
	 */
	public Treasure getTreasureById(long id){
		return (Treasure) this.findById(Treasure.class, id);
	}
	
	/**
	 * 修改理财宝信息
	 * @param model
	 * @return
	 */
	public boolean editTreasure(Treasure item){
		int result = this.modify(Treasure.class, item);
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 添加理财宝信息
	 * @param model
	 * @return
	 */
	public long addTreasure(Treasure item){
		return this.insertHoldKey(item);
	}
	
	/**
	 * 修改特殊信息方法
	 * 审核信息or停止/启用理财宝信息
	 * @param model
	 * @return
	 */
	public boolean specialEdit(Map<String,Object> map){
		StringBuffer sql = new StringBuffer("update dw_treasure set update_time = :update_time");
		Long verify_time = (Long) map.get("verify_time");
		if(verify_time != null){
			sql.append(" ,verify_time = :verify_time");
		}
		Long verify_user_id = (Long) map.get("verify_user_id");
		if(verify_user_id != null){
			sql.append(" ,verify_user_id = :verify_user_id");
		}
		String verify_remark = (String) map.get("verify_remark");
		if(verify_remark != null){
			sql.append(" ,verify_remark = :verify_remark");
		}
		String operator = (String) map.get("operator");
		if(operator != null){
			sql.append(" , operator = :operator");
		}
		Byte audit_status = (Byte) map.get("audit_status");
		if(audit_status != null){
			sql.append(" ,audit_status = :audit_status");
		}
		Byte status = (Byte) map.get("status");
		if(status != null){
			sql.append(" ,status = :status");
		}
		Byte back_verify_type = (Byte) map.get("back_verify_type");
		if(back_verify_type != null){
			sql.append(" ,back_verify_type = :back_verify_type");
		}
		sql.append(" where id = :id");
		Byte audit_status_ = (Byte) map.get("audit_status_");
		if(audit_status_ != null){
			sql.append(" and audit_status = :audit_status_");
		}
		Byte status_ = (Byte) map.get("status_");
		if(status_ != null){
			sql.append(" and status = :status_");
		}
		int result = this.namedParameterJdbcTemplate.update(sql.toString(), map);
		if(result > 0) return true;
		return false;
	}
	
	/**
	 * 修改理财宝信息投资额度
	 */
	public boolean editTreasureInvest(double invest, long id){
		StringBuffer sql = new StringBuffer("update dw_treasure set invest = ifnull(invest,0)+ :invest where id = :id ");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("invest", invest);
		map.put("id", id);
		int result = this.namedParameterJdbcTemplate.update(sql.toString(), map);
		if(result > 0) return true;
		return false;
	}
}
