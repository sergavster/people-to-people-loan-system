package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.p2psys.dao.UserAmountDao;
import com.p2psys.domain.UserAmount;
import com.p2psys.domain.UserAmountApply;
import com.p2psys.domain.UserAmountLog;
import com.p2psys.model.SearchParam;

public class UserAmountDaoImpl extends BaseDaoImpl implements
		UserAmountDao {
	private static Logger logger = Logger.getLogger(UserAmountDaoImpl.class);
	
	@Override
	public void add(UserAmountApply amount) {
		String sql="insert into dw_user_amountapply(user_id,type,account," +
				"account_new,account_old,status," +
				"addtime,content,remark,verify_remark," +
				"verify_time,verify_user,addip) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("SQL:"+sql);
		this.getJdbcTemplate().update(sql,amount.getUser_id(),amount.getType(),amount.getAccount(),
				amount.getAccount_new(),amount.getAccount_old(),amount.getStatus(),
				amount.getAddtime(),amount.getContent(),amount.getRemark(),amount.getVerify_remark(),
				amount.getVerify_time(),amount.getVerify_user(),amount.getAddip());
	}

	@Override
	public List getUserAmountApply(long user_id) {
		String sql="select p1.*,p2.username from dw_user_amountapply as  p1 left join dw_user as p2 on p1.user_id=p2.user_id " +
				"where p2.user_id=? and p1.status in (0,2) order by p1.addtime desc";
		logger.debug("SQL:"+sql);
		logger.debug("user_id:"+user_id);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{user_id},new UserAmountApplyMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(UserAmountApply.class));
		return list;
	}

	@Override
	public List getAmountApplyListByUserid(long userid,int start, int pernum, SearchParam param) {
		String sql="select p1.*,p2.username from dw_user_amountapply as  p1 left join dw_user as p2 on p1.user_id=p2.user_id " +
				"where 1=1 and p1.user_id=? " +
				"order by p1.addtime desc " +
				"limit ?,?";
		logger.debug("SQL:"+sql);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{userid,start,pernum}, new UserAmountApplyMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{userid,start,pernum}, getBeanMapper(UserAmountApply.class));
		return list;
	}

	@Override
	public int getAmountApplyCountByUserid(long user_id,SearchParam param) {
		String sql="select count(1) from dw_user_amountapply as  p1 left join dw_user as p2 on p1.user_id=p2.user_id " +
				"where 1=1 and p1.user_id=? ";
		int total=0;
		total=count(sql,user_id);
		return total;
	}
	
	@Override
	public List getUserMountApply(int start, int pernum, SearchParam param) {
		String sql="select p1.*,p2.username from dw_user_amountapply as  p1 left join dw_user as p2 on p1.user_id=p2.user_id " +
				"where 1=1 and p1.status=2 " +
				"order by p1.addtime desc " +
				"limit ?,?";
		logger.debug("SQL:"+sql);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{start,pernum}, new UserAmountApplyMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{start,pernum}, getBeanMapper(UserAmountApply.class));
		return list;
	}

	@Override
	public int getUserMountApplyCount(SearchParam param) {
		String sql="select count(1) from dw_user_amountapply as  p1 left join dw_user as p2 on p1.user_id=p2.user_id " +
				"where 1=1 and p1.status=2 ";
		int total=0;
		total=count(sql);
		return total;
	}

	@Override
	public UserAmount getUserAmount(long user_id) {
		String sql="select p1.*,p2.username from dw_user_amount as  p1 left join dw_user as p2 on p1.user_id=p2.user_id where p2.user_id=? order by p1.id desc";
		logger.debug("SQL:"+sql);
		logger.debug("user_id:"+user_id);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{user_id},new UserAmountMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(UserAmount.class));
		if(list.size()>0) return (UserAmount)list.get(0);
		return null;
	}

	@Override
	public UserAmountApply getUserAmountApplyById(long id) {
		String sql="select p1.*,p2.username from dw_user_amountapply as  p1 left join dw_user as p2 on p1.user_id=p2.user_id where p1.id=? order by p1.id desc";
		logger.debug("SQL:"+sql);
		logger.debug("user_id:"+id);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{id},new UserAmountApplyMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{id}, getBeanMapper(UserAmountApply.class));
		if(list.size()>0) return (UserAmountApply)list.get(0);
		return null;
	}

	@Override
	public void updateCreditAmount(double totalVar, double useVar,
			double nouseVar,UserAmount amount) {
		String sql="update dw_user_amount set credit=credit+?,credit_use=credit_use+?,credit_nouse=credit_nouse+? where user_id=?";
		this.getJdbcTemplate().update(sql, new Object[]{totalVar,useVar,nouseVar,amount.getUser_id()});
	}

	@Override
	public void updateApply(UserAmountApply apply) {
		String sql="update dw_user_amountapply set status=?,account=?,account_new=?,account_old=?,verify_remark=?,verify_time=?,verify_user=? where id=?";
		getJdbcTemplate().update(sql,apply.getStatus(),apply.getAccount(),apply.getAccount_new(),apply.getAccount_old(),
				apply.getVerify_remark(),apply.getVerify_time(),apply.getUser_id(),apply.getId());
	}

	@Override
	public void addAmountLog(UserAmountLog log) {
		String sql="insert into dw_user_amountlog(user_id,type,amount_type,account,account_all,account_use,account_nouse,remark,addtime,addip) " +
				"values(?,?,?,?,?,?,?,?,?,?)";
		getJdbcTemplate().update(sql, log.getUser_id(),log.getType(),log.getAmount_type(),log.getAccount(),log.getAccount_all(),
				log.getAccount_use(),log.getAccount_nouse(),log.getRemark(),log.getAddtime(),log.getAddip());
		}

	@Override
	public UserAmount getUserAmountById(long id) {
		String sql="select p1.*,p2.username from dw_user_amount as  p1 left join dw_user as p2 on p1.user_id=p2.user_id where p.id=?";
		logger.debug("SQL:"+sql);
		logger.debug("user_id:"+id);
		List list=new ArrayList();
//		list=this.getJdbcTemplate().query(sql, new Object[]{id},new UserAmountMapper());
		list=this.getJdbcTemplate().query(sql, new Object[]{id}, getBeanMapper(UserAmount.class));
		if(list.size()>0) return (UserAmount)list.get(0);
		return null;
	}

	@Override
	public void addAmount(UserAmount amount) {
		String sql="insert  into dw_user_amount(user_id,credit,credit_use,credit_nouse,borrow_vouch,borrow_vouch_use,borrow_vouch_nouse,tender_vouch,tender_vouch_use,tender_vouch_nouse) " +
				"value(?,?,?,?,?,?,?,?,?,?)";
		getJdbcTemplate().update(sql, amount.getUser_id(),amount.getCredit(),amount.getCredit_use(),amount.getCredit_nouse(),
				amount.getBorrow_vouch(),amount.getBorrow_vouch_use(),amount.getBorrow_vouch_nouse(),
				amount.getTender_vouch(),amount.getTender_vouch_use(),amount.getTender_vouch_nouse());
	}

	@Override
	public int getUserAmountCount(SearchParam param) {
		String sql="select count(1) from dw_user_amount as  p1 left join dw_user as p2 on p1.user_id=p2.user_id " +
				"where 1=1 ";
		int total = 0;
		total = count(sql);
		return total;
	}

	@Override
	public List getUserAmount(int start, int pernum, SearchParam param) {
		
		String sql="select p1.*,p2.username from dw_user_amount as p1 left join dw_user as p2 on p1.user_id=p2.user_id where 1=1 "+		
				"order by p1.user_id " +
				"limit ?,?";
		logger.debug("SQL:"+sql);
		List list = new ArrayList();
//		list = this.getJdbcTemplate().query(sql, new Object[]{start,pernum}, new UserAmountMapper());
		list = this.getJdbcTemplate().query(sql, new Object[]{start,pernum}, getBeanMapper(UserAmount.class));
		return list;
	}

	
}
