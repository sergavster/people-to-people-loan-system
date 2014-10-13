package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.ProtocolDao;
import com.p2psys.domain.Protocol;
import com.p2psys.model.ProtocolModel;
import com.p2psys.model.SearchParam;

public class ProtocolDaoImpl extends BaseDaoImpl implements ProtocolDao {

	private static Logger logger = Logger.getLogger(ProtocolDaoImpl.class);  
	private String orderSql=" order by p1.addtime desc ";
	private String limitSql=" limit ?,? ";
  //添加协议书
	@Override
	public void addProtocol(Protocol protocol) {
		String sql="insert into dw_protocol(" +
				"pid,user_id,money,protocol_type,addtime,addip,remark,borrow_id,repayment_account,interest,repayment_time,bank_account,bank_branch) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,protocol.getPid(),protocol.getUser_id(),protocol.getMoney(), protocol.getProtocol_type(),protocol.getAddtime(),protocol.getAddip(),protocol.getRemark(),protocol.getBorrow_id(),protocol.getRepayment_account(),protocol.getInterest(),protocol.getRepayment_time(),protocol.getBank_account(),protocol.getBank_branch());
	}
	//协议书count
	@Override
	public int getProtocolCount(SearchParam param) {
		String newsql="select count(1) from dw_protocol p1 " +
				"left join dw_user p2 on p2.user_id=p1.user_id " +
				"left join dw_linkage p4 on p4.value=p1.protocol_type and p4.type_id=44 " +
				"where 1=1 ";
		StringBuffer sb=new StringBuffer(newsql);
		sb.append(param.getSearchParamSql());
		String sql=sb.toString();
		logger.debug("SQL:"+sql); 
		int count=0;
		count=count(sql);
		return count;
	}
	//操作日志list
	@Override
	public List getProtocolList(int start, int pernum, SearchParam param) {
		String newsql="select p1.*,p2.username,p4.name as protocol_type_name from dw_protocol p1 " +
				"left join dw_user p2 on p2.user_id=p1.user_id " +
				"left join dw_linkage p4 on p4.value=p1.protocol_type and p4.type_id=44 " +
				"where 1=1 ";
		StringBuffer sb=new StringBuffer(newsql);
		String searchSql=param.getSearchParamSql();
		sb.append(searchSql).append(orderSql).append(limitSql);
		String sql=sb.toString();
		logger.debug("SQL:"+sql);
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql, new Object[]{start,pernum}, getBeanMapper(ProtocolModel.class));
		return list;
	}
	//操作日志导出list
	@Override
	public List getProtocolList(SearchParam param) {
		String newsql="select p1.*,p2.username,p4.name as protocol_type_name,p2.card_id,p2.realname,p6.verify_time,p6.account as borrow_account,p6.name as borrowname from dw_protocol p1 " +
				"left join dw_user p2 on p2.user_id=p1.user_id " +
				"left join dw_linkage p4 on p4.value=p1.protocol_type and p4.type_id=44 " +
				"left join dw_borrow p6 on p6.id=p1.borrow_id " +
				"where 1=1 ";
		StringBuffer sb=new StringBuffer(newsql);
		String searchSql=param.getSearchParamSql();
		sb.append(searchSql).append(orderSql);
		String sql=sb.toString();
		logger.debug("SQL:"+sql);
		List list=new ArrayList();
		list=getJdbcTemplate().query(sql, new Object[]{}, getBeanMapper(ProtocolModel.class));
		return list;
	}
	@Override
	public ProtocolModel getProtocolById(long id){
		String newsql="select p1.*,p2.username,p4.name as protocol_type_name,p2.card_id,p2.realname,p6.verify_time as verify_time,p6.account as borrow_account,p6.name as borrowname from dw_protocol p1 " +
				"left join dw_user p2 on p2.user_id=p1.user_id " +
				"left join dw_linkage p4 on p4.value=p1.protocol_type and p4.type_id=44 " +
				"left join dw_borrow p6 on p6.id=p1.borrow_id " +
				"where 1=1 and p1.id=? ";
		ProtocolModel protocol=null;
		logger.debug("sql==="+newsql);
		try {
			protocol=this.getJdbcTemplate().queryForObject(newsql, new Object[]{id}, getBeanMapper(ProtocolModel.class));
		} catch (DataAccessException e) {
			logger.debug(e.getMessage());
		}
		return protocol;
	}

}
