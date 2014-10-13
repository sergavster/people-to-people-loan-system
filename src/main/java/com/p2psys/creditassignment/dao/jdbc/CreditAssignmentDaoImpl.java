package com.p2psys.creditassignment.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.p2psys.creditassignment.dao.CreditAssignmentDao;
import com.p2psys.creditassignment.domain.CreditAssignment;
import com.p2psys.creditassignment.model.CreditAssignmentModel;
import com.p2psys.dao.jdbc.BaseDaoImpl;

@Repository
public class CreditAssignmentDaoImpl extends BaseDaoImpl implements CreditAssignmentDao {

	@Override
	public void add(CreditAssignment creditAssignment) {
		insert(creditAssignment);
		
	}

	@Override
	public CreditAssignment getOne(long id) {
		return (CreditAssignment)findById(CreditAssignment.class, id);
	}

	@Override
	public void modifyCa(CreditAssignment creditAssignment) {
		this.modify(CreditAssignment.class, creditAssignment);
	}
	
	/**
	 * 查询债权转让信息
	 * @param map 查询参数
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CreditAssignment> getList(Map<String, Object> map){
		StringBuffer sql = new StringBuffer("select * from dw_credit_assignment as p1 where 1 = 1");
		// 债权拥有人
		Long sell_user_id = (Long) map.get("sell_user_id");
		if(sell_user_id != null && sell_user_id > 0){
			sql.append(" and p1.sell_user_id = :sell_user_id");
		}
		// 债权标查询
		Long related_borrow_id = (Long) map.get("related_borrow_id");
		if(related_borrow_id != null && related_borrow_id > 0){
			sql.append(" and p1.related_borrow_id = :related_borrow_id");
		}
		// 债权标查询
		Long related_tender_id = (Long) map.get("related_tender_id");
		if(related_tender_id != null && related_tender_id > 0){
			sql.append(" and p1.related_tender_id = :related_tender_id");
		}
		// 债权标查询
		Long related_collection_id = (Long) map.get("related_collection_id");
		if(related_collection_id != null && related_collection_id > 0){
			sql.append(" and p1.related_collection_id = :related_collection_id");
		}
		Byte type = (Byte) map.get("type");
		if(type != null && type >= 0){
			sql.append(" and p1.type = :type");
		}
		// 多状态查询
		List statusList = (List) map.get("statusList");
		if(statusList != null && statusList.size() > 0){
			sql.append(" and p1.status in ( :statusList )");
		}
		// 债权单状态查询
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		// 债权等级：1普通、2优先
		Byte level = (Byte) map.get("level");
		if(level != null && level >= 0){
			sql.append(" and level = :level");
		}
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(CreditAssignment.class));
	}
	
	/**
	 * 债权转让分页信息
	 * @param page
	 * @param max
	 * @param map 查询参数
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CreditAssignmentModel> getPageList(int page , int max ,Map<String, Object> map){
		StringBuffer sql = new StringBuffer("select p1.*,p2.username ,p3.name as borrow_name from dw_credit_assignment p1 left join dw_user p2 on p1.sell_user_id = p2.user_id");
		sql.append(" left join dw_borrow p3 on p3.id = p1.related_borrow_id where 1 = 1");
		// 债权拥有人
		Long sell_user_id = (Long) map.get("sell_user_id");
		if(sell_user_id != null && sell_user_id > 0){
			sql.append(" and p1.sell_user_id = :sell_user_id");
		}
		// 债权标查询
		Long related_borrow_id = (Long) map.get("related_borrow_id");
		if(related_borrow_id != null && related_borrow_id > 0){
			sql.append(" and p1.related_borrow_id = :related_borrow_id");
		}
		// 债权tender查询
		Long related_tender_id = (Long) map.get("related_tender_id");
		if(related_tender_id != null && related_tender_id > 0){
			sql.append(" and p1.related_tender_id = :related_tender_id");
		}
		// 债权标collection查询
		Long related_collection_id = (Long) map.get("related_collection_id");
		if(related_collection_id != null && related_collection_id > 0){
			sql.append(" and p1.related_collection_id = :related_collection_id");
		}
		Byte type = (Byte) map.get("type");
		if(type != null && type >= 0){
			sql.append(" and p1.type = :type");
		}
		// 多状态查询
		List statusList = (List) map.get("statusList");
		if(statusList != null && statusList.size() > 0){
			sql.append(" and p1.status in ( :statusList )");
		}
		// 债权单状态查询
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		// 债权等级：1普通、2优先
		Byte level = (Byte) map.get("level");
		if(level != null && level >= 0){
			sql.append(" and p1.level = :level");
		}
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		Byte order = (Byte) map.get("order");
		String orderStr = this.getOrderSql(order);
		sql.append(orderStr);
		sql.append(" limit :page , :max");
		map.put("page", page);
		map.put("max", max);
		return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, this.getBeanMapper(CreditAssignmentModel.class));
	}
	
	/**
	 * 债权转让分页统计
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int getPageCount(Map<String, Object> map){
		StringBuffer sql = new StringBuffer("select count(*) from dw_credit_assignment p1 left join dw_user p2 on p1.sell_user_id = p2.user_id");
		sql.append(" left join dw_borrow p3 on p3.id = p1.related_borrow_id where 1 = 1");
		// 债权拥有人
		Long sell_user_id = (Long) map.get("sell_user_id");
		if(sell_user_id != null && sell_user_id > 0){
			sql.append(" and p1.sell_user_id = :sell_user_id");
		}
		// 债权标查询
		Long related_borrow_id = (Long) map.get("related_borrow_id");
		if(related_borrow_id != null && related_borrow_id > 0){
			sql.append(" and p1.related_borrow_id = :related_borrow_id");
		}
		// 债权标查询
		Long related_tender_id = (Long) map.get("related_tender_id");
		if(related_tender_id != null && related_tender_id > 0){
			sql.append(" and p1.related_tender_id = :related_tender_id");
		}
		// 债权标查询
		Long related_collection_id = (Long) map.get("related_collection_id");
		if(related_collection_id != null && related_collection_id > 0){
			sql.append(" and p1.related_collection_id = :related_collection_id");
		}
		Byte type = (Byte) map.get("type");
		if(type != null && type >= 0){
			sql.append(" and p1.type = :type");
		}
		// 多状态查询
		List statusList = (List) map.get("statusList");
		if(statusList != null && statusList.size() > 0){
			sql.append(" and p1.status in ( :statusList )");
		}
		// 债权单状态查询
		Byte status = (Byte) map.get("status");
		if(status != null && status >= 0){
			sql.append(" and p1.status = :status");
		}
		// 债权等级：1普通、2优先
		Byte level = (Byte) map.get("level");
		if(level != null && level >= 0){
			sql.append(" and p1.level = :level");
		}
		String username = (String) map.get("username");
		if(username != null && username.length() > 0){
			sql.append(" and p2.username like concat('%',:username,'%')");
		}
		return this.getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
	}
	
	public String getOrderSql(Byte order){
		String orderSql="";
		if(order == null){
			orderSql = " order by p1.id desc";
			return orderSql;
		}
		switch (order) {
		case 0:
			orderSql = " ";
			break;
		case 1:// 收益率升序
			orderSql = " order by (p1.credit_value - p1.credit_price ) / p1.credit_value asc";
			break;
		case -1://收益率降序
			orderSql = " order by (p1.credit_value - p1.credit_price ) / p1.credit_value desc";
			break;
		case 2:// 债权预计值升序
			orderSql = " order by p1.credit_value asc";
			break;
		case -2:// 债权预计值降序p1.credit_price
			orderSql = " order by p1.credit_value desc";
			break;
		case 3:// 债权转让值升序
			orderSql = " order by p1.credit_value asc";
			break;
		case -3:// 债权转让值降序
			orderSql = " order by p1.credit_value desc";
			break;
		default:
			orderSql=" order by p1.id desc";
		}
		return orderSql;
	}
}
