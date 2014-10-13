package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.common.constant.ConsStatusRecord;
import com.p2psys.common.enums.EnumBorrow;
import com.p2psys.context.Constant;
import com.p2psys.dao.CollectionDao;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Tender;
import com.p2psys.model.CollectionSumModel;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;
import com.sun.tools.jdi.DoubleValueImpl;

public class CollectionDaoImpl extends BaseDaoImpl implements CollectionDao {

	private Logger logger=Logger.getLogger(CollectionDaoImpl.class);
	
	@Override
	public Collection addCollection(Collection c) {
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
		String sql="insert into dw_borrow_collection(addtime,addip,user_id,borrow_id,tender_id,order,repay_time,repay_account,interest,capital,manage_fee) "+
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		int ret=this.getJdbcTemplate().update(sql,c.getAddtime(),c.getAddip(),c.getUser_id(),c.getBorrow_id(),c.getTender_id(),c.getOrder(),
				c.getRepay_time(),c.getRepay_account(),c.getInterest(),c.getCapital(),c.getManage_fee());
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 end
		if(ret<0)
			return null;
		return c;
	}
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
	/*@Override
	public void addBatchCollection(List<Collection> list) {
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
		String sql="insert into dw_borrow_collection(addtime,addip,user_id,borrow_id,tender_id,`order`,repay_time,repay_account,interest,capital,manage_fee) "+
				"values(?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> listo = new ArrayList<Object[]>(); 
		if(list.size()>0){
			for (Collection c:list) {  
		        Object[] args = { c.getAddtime(),c.getAddip(),c.getUser_id(),c.getBorrow_id(),c.getTender_id(),c.getOrder(),
						c.getRepay_time(),c.getRepay_account(),c.getInterest(),c.getCapital(),c.getManage_fee()};  
		        listo.add(args);  
		    }  
			this.getJdbcTemplate().batchUpdate(sql, listo);
		}
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
	}*/
	@Override
	public void addBatchCollection(List<Collection> list) {
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
		String sql="insert into dw_borrow_collection(addtime,addip,user_id,borrow_id,tender_id,`order`,repay_time,repay_account,interest,capital,manage_fee,repay_award,repay_award_status) "+
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> listo = new ArrayList<Object[]>(); 
		if(list.size()>0){
			for (Collection c:list) {  
				Object[] args = { c.getAddtime(),c.getAddip(),c.getUser_id(),c.getBorrow_id(),c.getTender_id(),c.getOrder(),
						c.getRepay_time(),c.getRepay_account(),c.getInterest(),c.getCapital(),c.getManage_fee(),c.getRepay_award(),c.getRepay_award_status()};  
				listo.add(args);  
			}  
			this.getJdbcTemplate().batchUpdate(sql, listo);
		}
		// v1.6.7.1 添加投资人用户ID、标ID xx 2013-11-11 start
	}
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 end

	@Override
	public List getDetailCollectionList(long user_id, int status) {
		String sql="select p1.*,p3.name as borrow_name,p3.id as borrow_id,p3.time_limit,p4.username  from dw_borrow_collection as p1 " +
				"left join dw_borrow_tender as p2 on  p1.tender_id = p2.id " +
				"left join dw_borrow as p3 on  p3.id = p2.borrow_id " +
				"left join dw_user as p4 on  p4.user_id = p3.user_id " +
				"where p3.user_id=? and p1.status=? order by p1.id";
		List list=this.getJdbcTemplate().query(sql, new Object[]{user_id,status},
				getBeanMapper(DetailCollection.class));
		return list;
	}

	@Override
	public DetailCollection getCollection(long id) {
		String sql="select * from dw_borrow_collection where id=?";
		DetailCollection c=this.getJdbcTemplate().queryForObject(sql,new Object[]{id},
				getBeanMapper(DetailCollection.class));
		return c;
	}

	@Override
	public List getCollectionList(long user_id, int status, int start, int end,
			SearchParam param) {
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 start
		String sql = "select p3.*,p1.name as borrow_name,p1.id as borrow_id,p1.time_limit,"
				+ "p1.style as borrow_style, p4.username from dw_borrow_collection as p3 "
				+ "left join dw_borrow_tender as p2 on p3.tender_id = p2.id "
				+ "left join dw_borrow as p1 on p1.id = p2.borrow_id "
				+ "left join dw_user as p4 on p4.user_id = p1.user_id "
				+ "where p2.user_id=? "
				+ "and p3.status=? "
				+ "and ((p1.status =1 and p1.type="+Constant.TYPE_FLOW+") or p1.status in (3,6,7,8)) ";
		String searchSql = param.getSearchParamSql();
		String limitSql = " limit " + start + "," + end;
		String orderSql = " order by p3.repay_yestime asc,p3.repay_time asc ";
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 end
		sql = sql + searchSql + orderSql + limitSql;
		List list = this.getJdbcTemplate().query(sql, new Object[] { user_id, status }, 
				getBeanMapper(DetailCollection.class));
		return list;
	}
	
	@Override
	public double getAcceptSum(long user_id) {
		String sql="select sum(p1.repay_yesaccount) as num from dw_borrow_collection as p1 " +
				"left join dw_borrow_tender as p2 on  p1.tender_id = p2.id " +
				"left join dw_borrow as p3 on  p3.id = p2.borrow_id " +
				"left join dw_user as p4 on  p4.user_id = p3.user_id " +
				"where p2.user_id=? " +
				" and ((p3.status =1 and p3.type="+Constant.TYPE_FLOW+") or p3.status in (3,6,7,8)) ";
		double sum=0.0;
		SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
		if(rs.next()){
			sum=rs.getDouble("num");
		}
		return sum;
	}
	@Override
	public double getCollectionInterestSum(long user_id, int status) {
		// v1.6.6.1 RDPROJECT-326 zza 2013-10-14 start
		String sql="select sum(p1.interest - p1.manage_fee+p1.extension_interest) as num from dw_borrow_collection as p1 " +
				// v1.6.6.1 RDPROJECT-326 zza 2013-10-14 end
				"left join dw_borrow_tender as p2 on  p1.tender_id = p2.id " +
				"left join dw_borrow as p3 on  p3.id = p2.borrow_id " +
				"left join dw_user as p4 on  p4.user_id = p3.user_id " +
				"where p2.user_id=? " +
				" and ((p3.status =1 and p3.type="+Constant.TYPE_FLOW+") or p3.status in (3,6,7,8)) " +
				//v1.6.7.1 RDPROJECT-170 wcw 2013-11-20 start
	 			" and ((p1.status=? and p3.style!=4) or (p3.style=4  and p2.wait_interest=0.0))";
		        //v1.6.7.1 RDPROJECT-170 wcw 2013-11-20 end
		logger.info("SQL:"+sql);
		logger.info("SQL:"+user_id); 
		double sum=0.0;
		SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id,status});
		if(rs.next()){
			sum=rs.getDouble("num");
		}
		return sum;
	}
	@Override
	public int getCollectionListCount(long user_id, int status, SearchParam param) {
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 start
		String sql = "select count(p3.id) from dw_borrow_collection as p3 "
				+ "left join dw_borrow_tender as p2 on p3.tender_id = p2.id "
				+ "left join dw_borrow as p1 on p1.id = p2.borrow_id "
				+ "left join dw_user as p4 on p4.user_id = p1.user_id "
				+ "where p2.user_id=? and p3.status=? "
				+ " and ((p1.status =1 and p1.type="+Constant.TYPE_FLOW+") or p1.status in (3,6,7,8)) ";
		// v1.6.6.2 RDPROJECT-313 zza 2013-10-31 end
		String searchSql = param.getSearchParamSql();
		sql = sql + searchSql;
		int total = 0;
		try {
			total = this.count(sql, new Object[] { user_id, status });
		} catch (Exception e) {
			logger.error("getCollectionListCount():" + e.getMessage());
		}
		return total;
	}

	@Override
	public void modifyCollectionBonus(int order, double apr,List tlist) {
		String sql="update dw_borrow_collection set repay_account=repay_account+capital*?,bonus=capital*? where `order`=? and tender_id=?";
		List args=new ArrayList();
		for(int i=0;i<tlist.size();i++){
			Tender t=(Tender)tlist.get(i);
			Object[] arg=new Object[]{apr,apr,order,t.getId()};
			args.add(arg);
		}
		getJdbcTemplate().batchUpdate(sql, args);
		
	}

	String collectSql=" from dw_borrow_collection c " +
			"left join dw_borrow_tender t on t.id=c.tender_id " +
			"left join dw_borrow b on b.id=t.borrow_id " +
			"left join dw_user as u on  u.user_id = t.user_id " +
			"where b.id=?";
	
	String selectSql="select c.*,u.username,t.addtime as tendertime ";
	
	String countSql="select count(1) ";
	
	@Override
	public List getCollectionLlistByBorrow(long bid, int start, int end,
			SearchParam param) {
		String sql=selectSql+collectSql;
		String searchSql=param.getSearchParamSql();
		String limitSql=" limit "+start+","+end;
		sql=sql+searchSql+limitSql;
		List list=new ArrayList();
		try {
//			list=this.getJdbcTemplate().query(sql, new Object[]{bid},new UserCollectionMapper());
			list=this.getJdbcTemplate().query(sql, new Object[]{bid}, getBeanMapper(DetailCollection.class));
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
		return list;
	}
	
	@Override
	public List getCollectionLlistByBorrow(long bid) {
		String sql=selectSql+collectSql;
		List list=new ArrayList();
		try {
//			list=this.getJdbcTemplate().query(sql, new Object[]{bid},new UserCollectionMapper());
			list=this.getJdbcTemplate().query(sql, new Object[]{bid}, getBeanMapper(DetailCollection.class));
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	@Override
	public int getCollectionCountByBorrow(long bid, SearchParam param) {
		String sql=countSql+collectSql;
		String searchSql=param.getSearchParamSql();
		sql=sql+searchSql;
		int count=0;
		count=count(sql, bid);
		return count;
	}
	String getUnFinishFlowListSql="from dw_borrow_collection as p3,dw_borrow_tender as p4,dw_user as p2 ,dw_borrow as p1" +
			" where p3.tender_id=p4.id and p1.id=p4.borrow_id and p2.user_id=p4.user_id and p4.user_id=p2.user_id and p1.status in(1,8) and p1.type="+Constant.TYPE_FLOW+"";
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 start
 	String getFlowListSql="from dw_borrow_collection as p3,dw_borrow_tender as p4,dw_user as p2 ,dw_borrow as p1" +
			" where p3.tender_id=p4.id and p1.id=p4.borrow_id and p2.user_id=p4.user_id and p4.user_id=p2.user_id and p1.status in(1,8) and p1.type="+Constant.TYPE_FLOW+"";
	
	@Override
	public int getUnFinishFlowCount(SearchParam param,String nowTime){
		String selectSql="select count(1) ";
		String unFinishFlowSql=" and ((p3.repay_yestime is null)) ";
		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(getUnFinishFlowListSql).append(param.getSearchParamSql()).append(unFinishFlowSql);
		logger.debug("getUnFinishFlowCount():"+sb.toString());
		logger.debug("nowTime:"+nowTime);
		int total=0;
		total=this.count(sb.toString());
		logger.debug("getUnFinishFlowCount"+total);
		return total;
	}
	@Override
	public List getUnFinishFlowList(SearchParam param,String nowTime, int start, int pernum) {
		String selectSql="select  p3.*,(p3.order+1) as real_order,p1.name as borrow_name,p1.*,p4.borrow_id,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		String lateSql=" and ((p3.repay_yestime is null)) ";
		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(getUnFinishFlowListSql).append(param.getSearchParamSql()).append(lateSql).append(" order by p3.repay_time asc limit ?,? ");
		logger.debug("getUnFinishFlowList():"+sb.toString());
		logger.debug("nowTime:"+nowTime);
		List list=new ArrayList();
		list=getJdbcTemplate().query(sb.toString(), new Object[]{start,pernum}, getBeanMapper(Collection.class));
		return list;
	}

	@Override
	public List getUnFinishFlowList(SearchParam param){
		String selectSql="select  p3.*,(p3.order+1) as real_order,p1.name as borrow_name,p1.*,p4.borrow_id,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		String lateSql=" and ((p3.repay_yestime is null)) ";
		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(getUnFinishFlowListSql).append(param.getSearchParamSql()).append(lateSql).append(" order by p3.repay_time asc");
		logger.debug("getUnFinishFlowList():"+sb.toString());
		List list=  new ArrayList();
		try {
			list = getJdbcTemplate().query(sb.toString(),new Object[]{}, getBeanMapper(Collection.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getFlowCount(SearchParam param, String nowTime, String type){
		String selectSql = "select count(1) ";
		String lateSql = "";
		if (EnumBorrow.NOPAYFLOW.getValue().equals(type)) {
			lateSql = " and ((p3.repay_yestime is null)) ";
		} else {
			lateSql = " and ((p3.repay_yestime is not null)) ";

		}
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getFlowListSql).append(param.getSearchParamSql()).append(lateSql);
		logger.debug("getFlowCount():" + sb.toString());
		logger.debug("nowTime:" + nowTime);
		int total = 0;
		total = this.count(sb.toString());
		logger.debug("getFlowCount" + total);
		return total;
	}
	@Override
	public CollectionSumModel getFlowSum(SearchParam param, String nowTime, String type) {
		String selectSql = "select sum(p3.repay_account) as account, " +
				"sum(p3.interest) as interest, sum(p3.capital) as capital ";
		String lateSql = "";
		if (EnumBorrow.NOPAYFLOW.getValue().equals(type)) {
			lateSql = " and ((p3.repay_yestime is null)) ";
		} else {
			lateSql = " and ((p3.repay_yestime is not null)) ";

		}
		StringBuffer sb = new StringBuffer(selectSql);
		sb.append(getFlowListSql).append(param.getSearchParamSql()).append(lateSql);
		logger.debug("getFlowSum():" + sb.toString());
		logger.debug("nowTime:" + nowTime);
		CollectionSumModel model = getJdbcTemplate().queryForObject(sb.toString(), new RowMapper<CollectionSumModel>() {
			public CollectionSumModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				CollectionSumModel model = new CollectionSumModel();
				model.setAccount(rs.getDouble("account"));
				model.setCapital(rs.getDouble("capital"));
				model.setInterest(rs.getDouble("interest"));
				return model;
			}
		});
		return model;
	}
	@Override
	public List getFlowList(SearchParam param,String nowTime, int start, int pernum,String type) {
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 START
		String selectSql="select  p3.*,(p3.order+1) as real_order,p1.name as borrow_name,p1.*,p4.borrow_id,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 START
		String lateSql="";
		if(EnumBorrow.NOPAYFLOW.getValue().equals(type)){
			lateSql=" and ((p3.repay_yestime is null)) ";
		}else{
			lateSql=" and ((p3.repay_yestime is not null)) ";

		}		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(getFlowListSql).append(param.getSearchParamSql()).append(lateSql).append(" order by p3.repay_time asc limit ?,? ");
		logger.debug("getUnFinishFlowList():"+sb.toString());
		logger.debug("nowTime:"+nowTime);
		List list=new ArrayList();
		//v.1.7.2 cx 2013-12-11 start
		list=getJdbcTemplate().query(sb.toString(), new Object[]{start,pernum}, getBeanMapper(DetailCollection.class));
		//v.1.7.2 cx 2013-12-11 end
		return list;
	}

	@Override
	public List getFlowList(SearchParam param,String type){
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 START
		//String selectSql="select  p3.*,p1.name as borrow_name,p1.*,p4.borrow_id,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		String selectSql="select  p3.*,(p3.order+1) as real_order, p1.name as borrow_name,p1.*,p4.borrow_id,p1.isday,p1.time_limit,p1.time_limit_day,p1.verify_time,p2.username,p2.user_id,p2.phone,p2.area ";
		// v1.6.5.5 RDPROJECT-119 wcw 2013-09-25 end
		String lateSql="";
		if(EnumBorrow.NOPAYFLOW.getValue().equals(type)){
			lateSql=" and ((p3.repay_yestime is null)) ";
		}else{
			lateSql=" and ((p3.repay_yestime is not null)) ";

		}
		StringBuffer sb=new StringBuffer(selectSql);
		sb.append(getFlowListSql).append(param.getSearchParamSql()).append(lateSql).append(" order by p3.repay_time asc");
		logger.debug("getUnFinishFlowList():"+sb.toString());
		List list=  new ArrayList();
		try {
			//v.1.7.2 cx 2013-12-11 start
			list = getJdbcTemplate().query(sb.toString(),new Object[]{},getBeanMapper(DetailCollection.class));
			//v.1.7.2 cx 2013-12-11 end
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}
	// v1.6.5.5 RDPROJECT-119 wcw 2013-09-24 end

	
	@Override
	public List getCollectionListByTender(long tid) {
		//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
		//按还款序号排序，便于处理
		String sql="select * from dw_borrow_collection where tender_id=? order by `order` asc";
		//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 end
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sql, new Object[]{tid}, getBeanMapper(Collection.class));
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
		return list;
	}
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
	/*@Override
	public void modifyBatchCollection(List<Collection> collects) {
		String sql="update dw_borrow_collection set status=?,repay_yestime=?,repay_yesaccount=?,repay_time=?," +
				"repay_account=?,late_interest=?,late_days=? where id=?";
		List<Object[]> listo = new ArrayList<Object[]>(); 
		if(collects!=null&&collects.size()>0){
			for (Collection c:collects) {  
		        Object[] args = {c.getStatus(),c.getRepay_yestime(),c.getRepay_yesaccount(),c.getRepay_time(),
		        		c.getRepay_account(),c.getLate_interest(),c.getLate_days(),c.getId()};  
		        listo.add(args);  
		    }  
			this.getJdbcTemplate().batchUpdate(sql, listo);
		}
	}*/
	@Override
	public void modifyBatchCollection(List<Collection> collects) {
		String sql="update dw_borrow_collection set status=?,repay_yestime=?,repay_yesaccount=?,repay_time=?," +
				"repay_account=?,late_interest=?,late_days=?,repay_award=?, repay_award_status=? where id=?";
		List<Object[]> listo = new ArrayList<Object[]>(); 
		if(collects!=null&&collects.size()>0){
			for (Collection c:collects) {  
				Object[] args = {c.getStatus(),c.getRepay_yestime(),c.getRepay_yesaccount(),c.getRepay_time(),
						c.getRepay_account(),c.getLate_interest(),c.getLate_days(),c.getRepay_award(), c.getRepay_award_status(), c.getId()};  
				listo.add(args);  
			}  
			this.getJdbcTemplate().batchUpdate(sql, listo);
		}
	}
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
	
	@Override
	public List getCollectionLlistByBorrow(long bid,int order) {
		logger.error("borrow_id===="+bid);
		//v1.6.7.2 RDPROJECT-86 liukun 2013-12-18 start
		//只查询出状态为0的待收记录，状态为其它的都不需要再还款，所以不需要查询出来
		//String sql=selectSql+collectSql+" and c.`order`=? ";
		String sql=selectSql+collectSql+" and c.`order`=? and c.status = " +Constant.COLLECTION_STATUS_NORMAL +  " ";
		//v1.6.7.2 RDPROJECT-86 liukun 2013-12-18 end
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sql, new Object[]{bid,order},getBeanMapper(DetailCollection.class));
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
		return list;
	}
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 start
	/*@Override
	public void modifyCollection(Collection c) {
		// v1.6.6.1 RDPROJECT-347 zza 2013-10-15 start
		String sql = "update dw_borrow_collection set status=?,repay_yestime=?,repay_yesaccount=?,repay_time=?,"
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
				+ "repay_account=?,late_interest=?,late_days=?,manage_fee=?,extension_interest=? where id=?";
		getJdbcTemplate().update(sql, c.getStatus(), c.getRepay_yestime(), c.getRepay_yesaccount(), c.getRepay_time(),
				c.getRepay_account(), c.getLate_interest(), c.getLate_days(), c.getManage_fee(),c.getExtension_interest(), c.getId());
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
		// v1.6.6.1 RDPROJECT-347 zza 2013-10-15 end
	}*/
	@Override
	public void modifyCollection(Collection c) {
		// v1.6.6.1 RDPROJECT-347 zza 2013-10-15 start
		String sql = "update dw_borrow_collection set status=?,repay_yestime=?,repay_yesaccount=?,repay_time=?,"
				//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start
				+ "repay_account=?,late_interest=?,late_days=?,manage_fee=?,extension_interest=?,repay_award=?, repay_award_status=? where id=?";
		getJdbcTemplate().update(sql, c.getStatus(), c.getRepay_yestime(), c.getRepay_yesaccount(), c.getRepay_time(),
				c.getRepay_account(), c.getLate_interest(), c.getLate_days(), c.getManage_fee(),c.getExtension_interest(), c.getRepay_award(), c.getRepay_award_status(),c.getId());
		//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end
		// v1.6.6.1 RDPROJECT-347 zza 2013-10-15 end
	}
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-19 end
	
	String selectFlowSql="select c.*,b.name as borrow_name,b.id as borrow_id,b.time_limit,u.username,u.user_id ";
	String allFlowSql=" from dw_borrow_collection c " +
			"left join dw_borrow_tender t on t.id=c.tender_id " +
			"left join dw_borrow b on b.id=t.borrow_id " +
			"left join dw_user as u on  u.user_id = t.user_id " ;
	// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
	//String whereFlowSql=" where c.status=? and b.type=" + Constant.TYPE_FLOW + " and b.status in (1,8) and c.repay_time<=?";
	String whereFlowSql=" WHERE NOT EXISTS (SELECT sr.id FROM dw_status_record sr WHERE sr.type="+ConsStatusRecord.SR_TYPE_FLOWREPAY
			+ " AND sr.fk_id=c.id AND sr.current_status=0 AND sr.target_status=1 AND sr.status=1)"
			+ " AND c.status=? AND b.type=" + Constant.TYPE_FLOW + " AND b.status in (1,8) AND c.repay_time<=?";
	// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
	/*@Override
	public List getAllFlowCollectList(int status) {
		return getAllFlowCollectList(status, 0);
	}*/
	
	@Override
	public List getAllFlowCollectList(int status,int aheadtime) {
		String sql=selectFlowSql+allFlowSql+whereFlowSql;
		String repay_time=System.currentTimeMillis()/1000+aheadtime+"";
		List list=new ArrayList();
		try {
			list=this.getJdbcTemplate().query(sql, new Object[]{status,repay_time}, getBeanMapper(DetailCollection.class));
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DetailCollection> getFlowRepayCollectList(int status,int aheadtime) {
		StringBuffer sb = new StringBuffer("select c.id,c.status from dw_borrow_collection c, dw_borrow b where c.borrow_id = b.id ");
		sb.append("AND c.status=? AND b.type=" + Constant.TYPE_FLOW + " AND b.status in (1,8) AND c.repay_time<=?");
		String repay_time=System.currentTimeMillis()/1000+aheadtime+"";
		List<DetailCollection> list = new ArrayList<DetailCollection>();
		try {
			list = this.getJdbcTemplate().query(sb.toString(),
					new Object[] { status, repay_time },
					getBeanMapper(DetailCollection.class));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return list;
	}
	

	@Override
	public double getRepaymentAccount(int status) {
		// v1.6.7.2 RDPROJECT-501 lx 2013.12.05 start
		//String sql = "select sum(repay_account) as sum from dw_borrow_collection where status=?";
		String sql = "SELECT SUM(repayment_account) AS sum FROM dw_borrow_repayment WHERE status=?";
		// v1.6.7.2 RDPROJECT-501 lx 2013.12.05 start
		double sum = 0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,new Object[]{status});
		if(rs.next()){
			sum = rs.getDouble("sum");
		}
		return sum;
	}

	@Override
	public List getAllDueColletion(Date date) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(repay_account) repay_account,  dw_borrow_tender.user_id user_id  from dw_borrow_collection "); 
		sql.append(" left join dw_borrow_tender on dw_borrow_collection.tender_id = dw_borrow_tender.id "); 
		sql.append(" left join dw_borrow on dw_borrow_tender.borrow_id = dw_borrow.id "); 
		sql.append(" where  dw_borrow.status = 6  and  FROM_UNIXTIME(dw_borrow_collection.repay_time, '%Y-%m-%d') = ? and dw_borrow_collection.status = 0 "); 
		sql.append(" group by dw_borrow_tender.user_id");
		/*logger.info("查找所有待收" + sql);
		logger.info("查找所有待收" + DateUtils.dateStr2(date));*/
		
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(
				sql.toString(), new Object[]{DateUtils.dateStr2(date)});
		return list;
	}
	
	/**
	 * 查询某段时间内待收或已收的利息
	 * @param user_id
	 * @param status -1为查询所有类型的利息，0待收，1已收
	 * @param status_time 已收的开始时间
	 * @param end_time 已收的结束时间
	 * @return
	 */
	public double getCollectInterestSum(long user_id,int status , long start_time , long end_time){
		
		if(user_id <= 0){
			logger.error("user_id is null.");
			return 0;
		}
		
		StringBuffer sql= new StringBuffer("select sum(p1.interest) as num from dw_borrow_collection as p1 "+
					"left join dw_borrow_tender as p2 on  p1.tender_id = p2.id "+
					"where p2.user_id=? ");
							
		// 查询数据数据封装 and verify_time>?
		List<Object> list = new ArrayList<Object>();
		list.add(user_id);
		
		//待还状态
		if(status >= 0){
			list.add(status);
			sql.append(" and p1.status = ? ");
		}
		
		//还款成功开始时间
		if(start_time > 0){
			list.add(start_time);
			sql.append(" and  p1.repay_yestime >= ? ");
		}
		
		//还款成功结束时间
		if(end_time > 0){
			list.add(end_time);
			sql.append(" and p1.repay_yestime <= ? ");
		}
		
		// 新建obj数组，用于JDBC查询，数组程度为list size
		Object[] obj = new Object[list.size()]; 
		// 遍历list，将值存入obj数组中
		for(int i = 0 ; i < list.size() ; i++){
			obj[i] = list.get(i);
		}
		
		double sum=0.0;
		SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql.toString(),obj);
		if(rs.next()){
			sum=rs.getDouble("num");
		}
		return sum;
	}
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	@Override
//	public double getTodayInterest() {
//		String sql = "select sum(c.interest) as sum from dw_borrow_collection as c" +
//				" left join dw_borrow_tender as t on c.tender_id= t.id" +
//				" left join dw_borrow as b on b.id=t.borrow_id" +
//				" where c.status=0 and b.type<>"+Constant.TYPE_SECOND+""+
//				" and (c.repay_time > " + (DateUtils.getIntegralTime().getTime() / 1000) + 
//				" and c.repay_time < " + (DateUtils.getNowTimeStr()) + ")";
//		logger.debug("getTodayInterest():" + sql );
//		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
//		double sum = 0;
//		if(rs.next()){
//			sum = rs.getDouble("sum");
//		}
//		return sum;
//	}

//	@Override
//	public double getInterestTotal() {
//		// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 start
//		String sql =" select sum(bt.interest) as sum from dw_borrow_tender as bt " +
//				"left join dw_borrow as b on b.id = bt.borrow_id where b.type <> " + Constant.TYPE_SECOND + "  and "+
//				" (( b.status in (3, 6, 7, 8) and b.type <> " + Constant.TYPE_FLOW + ") or "+
//				" (b.type = " + Constant.TYPE_FLOW + " and b.status in (1, 3, 6, 7, 8))) " +
//				"and (bt.addtime < "+(DateUtils.getIntegralTime().getTime() / 1000)+")";
//		// v1.6.6.1 RDPROJECT-41 zza 2013-09-24 end
//		logger.debug("getInterestTotal():"+ sql );
//		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
//		double sum = 0;
//		if(rs.next()){
//			sum = rs.getDouble("sum");
//		}
//		return sum;
//	}
	// v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 end

	@Override
	public List getCollectionListByBorrow(long id) {
		String sql = "select c.* from dw_borrow_collection as c " +
				"left join dw_borrow_tender as t on c.tender_id = t.id " +
				"left join dw_borrow as b on b.id = t.borrow_id " +
				"where b.id = ?";
		logger.debug("getCollectionListByBorrow():" + sql);
		List list = new ArrayList();
		try{
			list = this.getJdbcTemplate().query(sql, new Object[]{id}, getBeanMapper(Collection.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	// v1.6.7.1 RDPROJECT-399 zza 2013-11-06 start
	/**
	 * 还款时奖励（未收）
	 * @param user_id 用户id
	 * @param status 状态
	 * @return 奖励money
	 */
	public double getRepayAward(long user_id, int status) {
		String sql = "select ifnull(sum(t.award),0) as num from "
				+ "(select bt.borrow_id, bc.tender_id, (bt.account * b.late_award / 100) as award, "
				+ "min(bc.`status`) as mix_status from dw_borrow_collection bc "
				+ "inner join dw_borrow_tender bt on bc.tender_id = bt.id and bt.user_id = ? "
				+ "inner join dw_borrow b on bt.borrow_id = b.id "
				+ "where b.status in (6,7,8) or (b.type = 110 and b.status in (1, 8)) "
				+ "group by bc.tender_id having mix_status = ?) as t ";
		double sum = 0.0;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql, new Object[] { user_id, status });
		if (rs.next()) {
			sum = rs.getDouble("num");
		}
		return sum;
	}
	// v1.6.7.1 RDPROJECT-399 zza 2013-11-06 end

	// v1.6.7.2 理财宝 zhangyz 2013-12-23 start
	/**
	 * 查询用户的待收金额
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public double getUserWaitMoney(Map<String,Object> map){
		StringBuffer sql = new StringBuffer("SELECT SUM(c.repay_account) as num FROM dw_borrow_collection AS c,dw_borrow AS b WHERE");
		sql.append(" c.borrow_id = b.id AND  ( (b.type <> 110 AND   b.status IN (3,6,7) )  OR b.type = 110 ) AND c.status = 0 ");
		List borrow_type = (List) map.get("borrow_type");
		if(borrow_type != null && borrow_type.size() > 0){
			sql.append("  AND b.type IN ( :borrow_type )");
		}
		Integer isday = (Integer) map.get("isday");
		if(isday != null && isday >= 0){
			sql.append(" AND b.isday = :isday");
		}
		Long user_id = (Long) map.get("user_id");
		if(user_id != null && user_id >= 0){
			sql.append(" AND c.user_id = :user_id");
		}
		double sum = 0.0;
		SqlRowSet rs = this.getNamedParameterJdbcTemplate().queryForRowSet(sql.toString(), map);
		if (rs.next()) {
			sum = rs.getDouble("num");
		}
		return sum;
	}
	// v1.6.7.2 理财宝 zhangyz 2013-12-23 end
	
	
	// v1.6.7.2 债权转让 zhangyz 2013-12-25 start
	/**
	 * 查询用户的待收列表
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Collection> getCollectionList(Map<String,Object> map){
		StringBuffer sql = new StringBuffer("SELECT c.* FROM dw_borrow_collection AS c");
		sql.append(" LEFT JOIN dw_borrow AS b ON b.id = c.borrow_id WHERE c.status = 0");
		sql.append(" AND  ( (b.type <> 110 AND   b.status IN (3,6,7) )  OR b.type = 110 )");
		Long borrowId = (Long) map.get("borrowId");
		if(borrowId != null && borrowId >= 0){
			sql.append(" AND b.id = :borrowId");
		}
		Long tenderId = (Long) map.get("tenderId");
		if(tenderId != null && tenderId >= 0){
			sql.append(" AND c.tender_id = :tenderId");
		}
		Long collectionId = (Long) map.get("collectionId");
		if(collectionId != null && collectionId >= 0){
			sql.append(" AND c.id = :collectionId");
		}
		Long user_id = (Long) map.get("user_id");
		if(user_id != null && user_id >= 0){
			sql.append(" AND c.user_id = :user_id");
		}
		try{
			return this.getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(Collection.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询用户的待收列表
	 * @param id
	 * @return
	 */
	public Collection getCollectionById(long id){
		return (Collection) this.findById(Collection.class, id);
	}
	// v1.6.7.2  债权转让 zhangyz 2013-12-25 end
}
