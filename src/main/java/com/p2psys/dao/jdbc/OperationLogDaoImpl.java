package com.p2psys.dao.jdbc;

import java.util.List;

import org.apache.log4j.Logger;

import com.p2psys.dao.OperationLogDao;
import com.p2psys.domain.OperationLog;
import com.p2psys.model.SearchParam;
import com.p2psys.model.account.OperationLogModel;
import com.p2psys.util.StringUtils;

public class OperationLogDaoImpl extends BaseDaoImpl implements OperationLogDao {

	private static Logger logger = Logger.getLogger(BorrowDaoImpl.class);  
	private String orderSql=" order by p1.addtime desc ";
	private String limitSql=" limit ?,? ";
//添加操作日志
	@Override
	public void addOperationLog(OperationLog log) {
		String sql="insert into dw_operation_log(" +
				"user_id,type,verify_user,addtime,addip,operationResult) values(?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, log.getUser_id(),log.getType(),log.getVerify_user(),
				log.getAddtime(),log.getAddip(),log.getOperationResult());
	}

	//操作日志count
	@Override
	public int getOperationLogCount(SearchParam param) {
		//  v1.6.6.1 RDPROJECT-205 zza 2013-09-27 start
		String newsql = "select count(*) from dw_operation_log p1 "
				+ "left join dw_user p5 on p5.user_id=p1.verify_user "
				+ "left join dw_user p2 on p2.user_id=p1.user_id "
				+ "left join dw_user_type p3 on p5.type_id=p3.type_id "
				+ "left join dw_linkage p4 on p4.value=p1.type and p4.type_id=42 " + "where 1=1 ";
		//  v1.6.6.1 RDPROJECT-205 zza 2013-09-27 end
		StringBuffer sb = new StringBuffer(newsql);
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 start
		if (!StringUtils.isBlank(param.getUser_typeid())) {
			sb.append(" and p5.user_id="
					+ StringUtils.isNull(param.getUser_id()));
		}
		if (!StringUtils.isBlank(param.getUsername())) {
			sb.append(" and p2.username like '%").append(param.getUsername().trim()).append("%'");
		}
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 end
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		int count = 0;
		count = count(sql);
		return count;
	}
	
	//操作日志list
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getOperationLogList(int start, int pernum, SearchParam param) {
		//  v1.6.6.1 RDPROJECT-205 zza 2013-09-26 start
		String newsql="select p3.name as usertypename,p4.name as typename,p1.*,p5.username as verify_username,p2.username as username from dw_operation_log p1 " +
				"left join dw_user p5 on p5.user_id=p1.verify_user " +
				"left join dw_user p2 on p2.user_id=p1.user_id " +
				"left join dw_user_type p3 on p5.type_id=p3.type_id " +
				"left join dw_linkage p4 on p4.value=p1.type and p4.type_id=42 " +
				"where 1=1 ";
		//  v1.6.6.1 RDPROJECT-205 zza 2013-09-26 end
		StringBuffer sb = new StringBuffer(newsql);
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 start
		if (!StringUtils.isBlank(param.getUser_typeid())) {
			sb.append(" and p5.user_id="
					+ StringUtils.isNull(param.getUser_id()));
		}
		if (!StringUtils.isBlank(param.getUsername())) {
			sb.append(" and p2.username like '%").append(param.getUsername().trim()).append("%'");
		}
		sb.append(orderSql).append(limitSql);
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 end
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		List<OperationLogModel> list = getJdbcTemplate().query(sql, new Object[] { start, pernum }, getBeanMapper(OperationLogModel.class));
		return list;
	}
	
	//操作日志导出list
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<OperationLogModel> getOperationLogList(SearchParam param) {
		//  v1.6.6.1 RDPROJECT-205 zza 2013-09-27 start
		String newsql="select p3.name as usertypename,p4.name as typename,p1.*," + 
				"p5.username as verify_username,p2.username as username from dw_operation_log p1 " +
				"left join dw_user p5 on p5.user_id=p1.verify_user " +
				"left join dw_user p2 on p2.user_id=p1.user_id " +
				"left join dw_user_type p3 on p5.type_id=p3.type_id " +
				"left join dw_linkage p4 on p4.value=p1.type and p4.type_id=42 " +
				"where 1=1 ";
		//  v1.6.6.1 RDPROJECT-205 zza 2013-09-27 end
		StringBuffer sb = new StringBuffer(newsql);
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 start
		if (!StringUtils.isBlank(param.getUser_typeid())) {
			sb.append(" and p5.user_id="
					+ StringUtils.isNull(param.getUser_id()));
		}
		if (!StringUtils.isBlank(param.getUsername())) {
			sb.append(" and p2.username like '%").append(param.getUsername().trim()).append("%'");
		}
		sb.append(orderSql);
		// v1.6.7.2 RDPROJECT-525 zza 2013-12-19 end
		String sql = sb.toString();
		logger.debug("SQL:" + sql);
		List<OperationLogModel> list = getJdbcTemplate().query(sql, new Object[] {}, getBeanMapper(OperationLogModel.class));
		return list;
	}

}
