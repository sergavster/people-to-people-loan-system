package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.UserCreditDao;
import com.p2psys.domain.CreditRank;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserCreditLog;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCreditLogModel;
import com.p2psys.model.UserCreditModel;

@Resource
public class UserCreditDaoImpl extends BaseDaoImpl implements UserCreditDao {

	private static Logger logger = Logger.getLogger(UserCreditDaoImpl.class);

	public void addUserCredit(UserCredit uc) {
		String sql = "insert into  dw_credit(user_id, value,op_user, addtime, addip,updatetime, updateip, tender_value, borrow_value, gift_value, expense_value ,valid_value) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, uc.getUser_id(), uc.getValue(),
				uc.getOp_user(), uc.getAddtime(), uc.getAddip(),uc.getUpdatetime(),
				uc.getUpdateip(),uc.getTender_value() , uc.getBorrow_value() , uc.getGift_value() , uc.getExpense_value(),uc.getValid_value());
	}

	@Override
	public List getUserCreditByPageNumber(int Page, int Max) {
		String sql = "SELECT p.*,`user`.username,`user`.realname,type.name as creditTypeName,log.remark as creditLogRemark ,log.addtime as creditLogAddTime from dw_credit AS p LEFT JOIN  dw_credit_log as log on p.user_id=log.user_id LEFT JOIN dw_credit_type as type on log.type_id=type.id LEFT JOIN dw_user as user on p.user_id=`user`.user_id LIMIT ?,?";
//		List list=getJdbcTemplate().query(sql,new Object[]{Page*Max,Max},new UserCreditModelMapper());
		List list=getJdbcTemplate().query(sql,new Object[]{Page*Max,Max}, getBeanMapper(UserCreditModel.class));
		return list;
	}

	@Override
	public void updateUserCredit(UserCredit userCredit) {
		
		String sql = "update dw_credit set value = ? where user_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + userCredit.getUser_id());
		this.getJdbcTemplate().update(sql, userCredit.getValue(),userCredit.getUser_id());
	}
	
	@Override
	public void updateCreditTenderValue(UserCredit userCredit) {
		String sql = "update dw_credit set tender_value = ? where user_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + userCredit.getUser_id());
		this.getJdbcTemplate().update(sql, userCredit.getTender_value(),userCredit.getUser_id());
	}

	@Override
	public UserCredit getUserCreditByUserId(long user_id) {
		String sql = "select * from dw_credit where user_id = ?";
		UserCredit userCredit = null;
		try {
//			userCredit = this.getJdbcTemplate().queryForObject(sql,
//					new Object[] {user_id}, new UserCreditMapper());
			userCredit = this.getJdbcTemplate().queryForObject(sql,
					new Object[] {user_id}, getBeanMapper(UserCredit.class));
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
		return userCredit;
		
	}

	/**
	 * 查询总积分
	 */
	@Override
	public int getCreditValueByUserId(long user_id) {
		String sql = "select value from dw_credit where user_id=?";
		return getJdbcTemplate().queryForInt(sql, new Object[]{user_id});
	}

	@Override
	public int getUserCreditCount(SearchParam p) {
		String sql = "select count(1) from dw_user as p2 left join dw_credit as p1 on p1.user_id=p2.user_id  where 1=1";
		sql += p.getSearchParamSql();
		return getJdbcTemplate().queryForInt(sql);
	}

	@Override
	public List getUserCredit(int page, int max, SearchParam p) {
		String sql = "select p1.*, p2.username, p2.realname,p3.pic as credit_pic " +
				"from dw_user as p2 " +
				"left join dw_credit as p1 on p1.user_id = p2.user_id " +
				"left join dw_credit_rank as p3 " +
				"on p1.value <= p3.point2 and p1.value >= p3.point1 " +
				"where 1 = 1 ";
		sql += p.getSearchParamSql();
		sql += "  LIMIT ?,?";
//		return getJdbcTemplate().query(sql, new Object[] { page , max },new UserCreditModelMapper());
		return getJdbcTemplate().query(sql, new Object[] { page , max }, getBeanMapper(UserCreditModel.class));
	}
	
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-12 start
	@Override
	public List<UserCreditModel> getUserCredit(SearchParam p) {
		String sql = "select p1.*, p2.username, p2.realname,p3.pic as credit_pic " +
				"from dw_user as p2 " +
				"left join dw_credit as p1 on p1.user_id = p2.user_id " +
				"left join dw_credit_rank as p3 " +
				"on p1.value <= p3.point2 and p1.value >= p3.point1 " +
				"where 1 = 1 ";
		sql += p.getSearchParamSql();
		return getJdbcTemplate().query(sql, getBeanMapper(UserCreditModel.class));
	}
	// v1.6.7.2 RDPROJECT-509 zza 2013-12-12 end

	@Override
	public void addUserCreditLog(UserCreditLog ucLog) {
		// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
		String sql="insert into dw_credit_log(id,user_id,type_id,op,value,valid_value," +
				"remark,op_user,addtime,addip) values(?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, ucLog.getId(), ucLog.getUser_id(),
				ucLog.getType_id(), ucLog.getOp(), ucLog.getValue(),
				ucLog.getValid_value(), ucLog.getRemark(), ucLog.getOp_user(),
				ucLog.getAddtime(), ucLog.getAddip());
		// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
	}

	@Override
	public UserCreditModel getCreditModelById(long user_id) {
		String sql = "select p1.*, p2.username, p2.realname,p3.pic as credit_pic " +
				"from dw_user as p2 " +
				"left join dw_credit as p1 on p1.user_id = p2.user_id " +
				"left join dw_credit_rank as p3 " +
				"on p1.value <= p3.point2 and p1.value >= p3.point1 " +
				"where 1 = 1 and p2.user_id=?";
		UserCreditModel credit=null;
		try {
//			credit=getJdbcTemplate().queryForObject(sql, new Object[] { user_id },
//					new UserCreditModelMapper());
			credit=getJdbcTemplate().queryForObject(sql, new Object[] { user_id },
					getBeanMapper(UserCreditModel.class));
		} catch (DataAccessException e) {
			logger.info("查询结果为空！");
		}
		return credit;
	}

	@Override
	public UserCreditLogModel getCreditLogByUserId(long user_id) {
		String sql = "select p1.*,p2.username,p2.realname from dw_credit_log as p1 left join dw_user as p2 on p1.user_id = p2.user_id where 1=1 and p1.user_id =?";
		return getJdbcTemplate().queryForObject(sql, new Object[] { user_id },
				getBeanMapper(UserCreditLogModel.class));
	}

	@Override
	public int getCreditLogCount(SearchParam p) {
		String sql = "select count(1) from dw_user as p2 left join dw_credit_log as p1 on p1.user_id=p2.user_id  where 1=1";
		sql += p.getSearchParamSql();
		return getJdbcTemplate().queryForInt(sql);
	}

	@Override
	public List getCreditLog(int page, int max,SearchParam p) {
		
		String sql = "SELECT p1.*,p2.username,p2.realname,p3.name as typeName " +
				"from dw_user as p2 left join dw_credit_log as p1 on p1.user_id=p2.user_id left join dw_credit_type as p3 on p1.type_id = p3.id where 1=1";
		sql += p.getSearchParamSql();
		sql += "  LIMIT ?,?";
		return getJdbcTemplate().query(sql, new Object[] { page , max }, getBeanMapper(UserCreditLogModel.class));
	}
	
	
	/**
	 * 根据积分类型代码查询积分类型
	 * @param nid 积分类型代码
	 * @return 积分类型CreditType
	 */
	public CreditType getCreditTypeByNid(String nid){
		String sql = "select dct.id, dct.name, dct.nid, dct.value, dct.cycle, dct.award_times, dct.interval, dct.remark, dct.op_user, dct.addtime, dct.addip, dct.updatetime, dct.updateip, dct.rule_nid, dct.credit_category from dw_credit_type dct where dct.nid = ? ";
		
		try {
			CreditType type = getJdbcTemplate().queryForObject(sql, new Object[] { nid }, getBeanMapper(CreditType.class));
			return type;
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return null; 
	}
	
	public CreditType getCreditTypeById(long id){
		String sql = "select dct.id, dct.name, dct.nid, dct.value, dct.cycle, dct.award_times, dct.interval, dct.remark, dct.op_user, dct.addtime, dct.addip, dct.updatetime, dct.updateip, dct.rule_nid, dct.credit_category from dw_credit_type dct where dct.id = ? ";
		
		try {
			CreditType type = getJdbcTemplate().queryForObject(sql, new Object[] { id }, getBeanMapper(CreditType.class));
			return type;
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return null; 
	}
	
	/**
	 * 根据积分种类查询积分类型
	 * @param credit_category 积分种类
	 * @return 积分类型 List
	 */
	public List<CreditType> getCreditTypeList(String credit_category){
		String sql = "select dct.id, dct.name, dct.nid, dct.value, dct.cycle, dct.award_times, dct.interval, dct.remark, dct.op_user, dct.addtime, dct.addip, dct.updatetime, dct.updateip, dct.rule_nid, dct.credit_category from dw_credit_type dct where dct.credit_category = ? ";
		return getJdbcTemplate().query(sql, new Object[] { credit_category }, getBeanMapper(CreditType.class));
	}
	
	
	/**
	 * 根据user_id修改有效积分。
	 * @param user_id
	 * @param value
	 */
	public void editCreditValue(long user_id , int value){
		if(value > 0){//如果积分为正数。则是加积分操作，那么须将总积分加，有效积分加
			String sql ="update dw_credit dc set  dc.value = ifnull(dc.value,0) + ? , dc.valid_value = ifnull(dc.valid_value,0) + ? where user_id = ? ";
			this.getJdbcTemplate().update(sql, value,value,user_id);
		}
	}
	
	/**
	 * 根据user_id修改投资积分（加减）。
	 * @param user_id
	 * @param value
	 */
	public void editTenderValue(long user_id , int value ,int total_value){
		String sql ="";
		if(value > 0){//如果积分为正数。则是加积分操作，那么须将总积分加，有效积分加，投资积分加
			sql ="update dw_credit dc set dc.value = ifnull(dc.value,0) + ? , dc.tender_value = ifnull(dc.tender_value,0) + ? , dc.valid_value = ifnull(dc.valid_value,0) + ? where user_id = ? ";
			this.getJdbcTemplate().update(sql, total_value , value ,total_value , user_id);
		}
	}
	
	/**
	 * 根据user_id修改借款积分。
	 * @param user_id
	 * @param value
	 */
	public void editBorrowValue(long user_id , int value){
		if(value > 0){//如果积分为正数。则是加积分操作，那么须将总积分加，有效积分加，借款积分加
			String sql ="update dw_credit dc set dc.value = ifnull(dc.value,0) + ? , dc.borrow_value = ifnull(dc.borrow_value,0) + ? , dc.valid_value = ifnull(dc.valid_value,0) + ? where user_id = ? ";
			this.getJdbcTemplate().update(sql, value, value , value,user_id);
		}
	}
	
	public void editGiftValue(long user_id , int value , int total_value , int valid_value){
		if(value > 0){//如果积分为正数。则是加积分操作，那么须将总积分加，有效积分加，赠送积分加
			String sql ="update dw_credit dc set dc.value = ifnull(dc.value,0) + ? , dc.gift_value = ifnull(dc.gift_value,0)+ ? , dc.valid_value = ifnull(dc.valid_value,0) + ? where user_id = ? ";
			this.getJdbcTemplate().update(sql, total_value , value, valid_value,user_id);
		}
	}
	
	/**
	 * 根据user_id修改消费积分（加）。
	 * @param user_id
	 * @param value
	 */
	public void editExpenseValue(long user_id , int value){
		//如果积分为正数。则是加积分操作，那么须将有效积分减，消费积分加,如果为负数，则有效积分加，消费积分减
		String sql ="update dw_credit dc set dc.valid_value = ifnull(dc.valid_value,0) - ? , dc.expense_value = ifnull(dc.expense_value ,0)+ ? where user_id = ? ";
		this.getJdbcTemplate().update(sql, value ,value,user_id);
	}
	
	/**
	 * 根据user id 查询该用户拥有哪些积分类型
	 * @param user_id
	 * @return
	 */
	public List<CreditType> getCreditTypeByUserId(long user_id){
		String sql ="select dct.* from dw_credit_log dcl left join dw_credit_type dct on dcl.type_id = dct.id  where dcl.user_id = ? group by dct.id ";
		return getJdbcTemplate().query(sql, new Object[] { user_id }, getBeanMapper(CreditType.class));
	}
	
	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	/**
	 * 根基条件查询积分记录信息
	 * @param p
	 * @param type_id 积分类型ID
	 * @param user_id 会员ID
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getUserCreditLogCount(SearchParam p,long type_id){
		String sql = "select count(1) from dw_credit_log as p1 left join dw_user as p2  on p1.user_id=p2.user_id  where 1 = 1";
		// 新建一个list 用于存值
		List list =  new ArrayList();
		// 如果type_id存在，则拼接sql，将值放入list
		if(type_id > 0){
			sql +=" and p1.type_id = ?";
			list.add(type_id);
		}
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
		sql += p.getSearchParamSql();
		// 新建obj数组，用于JDBC查询，数组程度为list size
		Object[] obj = new Object[list.size()];
		// 遍历list，将值存入obj数组中
		for(int i = 0 ; i < list.size() ; i++){
			obj[i] = list.get(i);
		}
		
		return getJdbcTemplate().queryForInt(sql, obj);
	}
	
	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getCreditLogPage(int page, int max,SearchParam p , long type_id) {
		
		String sql = "SELECT p1.*,p2.username,p2.realname,p3.name as typeName " +
				"from dw_credit_log as p1 left join dw_user as p2 on p1.user_id=p2.user_id left join dw_credit_type as p3 on p1.type_id = p3.id where 1=1";
		// 新建一个list 用于存值
		List list =  new ArrayList();
		
		// 如果type_id存在，则拼接sql，将值放入list
		if(type_id > 0){
			sql +=" and p1.type_id = ?";
			list.add(type_id);
		}
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
		// 将分页信息存入list中
		list.add(page);
		list.add(max);
		
		// 新建obj数组，用于JDBC查询，数组程度为list size
		Object[] obj = new Object[list.size()]; 
		// 遍历list，将值存入obj数组中
		for(int i = 0 ; i < list.size() ; i++){
			obj[i] = list.get(i);
		}
		
		sql += p.getSearchParamSql();
		sql += " order by p1.id desc LIMIT ?,?";
		return getJdbcTemplate().query(sql, obj , getBeanMapper(UserCreditLogModel.class));
	}
	
	/**
	 * 查询所有的积分类型
	 * @param user_id
	 * @return
	 */
	public List<CreditType> getCreditTypeAll(){
		String sql ="select * from dw_credit_type ";
		return getJdbcTemplate().query(sql, getBeanMapper(CreditType.class));
	}
	
	/**
	 * 查询积分记录信息
	 * @param user_id
	 * @param type_id积分类型表主键ID
	 * @return
	 */
	public List getCreditLogList(long user_id , long type_id){
		String sql ="select * from dw_credit_log where user_id = ? and type_id = ? ";
//		return getJdbcTemplate().query(sql, new Object[] { user_id  ,type_id},new UserCreditLogMapper());
		return getJdbcTemplate().query(sql, new Object[] { user_id  ,type_id}, getBeanMapper(UserCreditLog.class));
	}

	/**
	 * 查询有效积分
	 */
	@Override
	public int getValidValueByUserId(long user_id) {
		String sql = "select valid_value from dw_credit where user_id=?";
		return getJdbcTemplate().queryForInt(sql, new Object[]{user_id});
	}
	
	public List<CreditRank> getCreditRankList(){
		String sql = "select rank,point1,point2,pic from dw_credit_rank ";
		return getJdbcTemplate().query(sql, new RowMapper(){
			@Override
			public Object mapRow(ResultSet rs, int num) throws SQLException {
				CreditRank r=new CreditRank();
				r.setRank(rs.getInt("rank"));
				r.setPoint1(rs.getInt("point1"));
				r.setPoint2(rs.getInt("point2"));
				r.setPic(rs.getString("pic"));
				return r;
			}
		}); 
	}
	
	/**
	 * 后台修改会员综合积分，有效积分和消费积分
	 */
	public void updateCredit(long value , long valid_value , long expense_value , long user_id){
		StringBuffer sql = new StringBuffer("update dw_credit set value = ifnull(value ,0) + :value , valid_value = ifnull(valid_value ,0) + :valid_value , expense_value = ifnull(expense_value ,0) + :expense_value where user_id = :user_id ");
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("value", value);
		map.put("valid_value", valid_value);
		map.put("expense_value", expense_value);
		map.put("user_id", user_id);
	    this.getNamedParameterJdbcTemplate().update(sql.toString(), map);
	}
	
	/**
	 * 查询所有的积分会员
	 * @return
	 */
	public List<UserCredit> getCreditAll(){
		String sql ="select * from dw_credit ";
//		return getJdbcTemplate().query(sql,new UserCreditMapper());
		return getJdbcTemplate().query(sql, getBeanMapper(UserCredit.class));
	}
	/**
	 * 更具条件查询一段时间内的积分总和
	 * @param start_time
	 * @param end_time
	 * @param user_id
	 * @param type_id
	 * @return
	 */
	public int getCreditLogCount(long start_time , long end_time , long user_id , long type_id){
		String sql = "select count(1) from dw_credit_log  where addtime >= ? and addtime <= ? and user_id = ? and type_id = ? ";
		return getJdbcTemplate().queryForInt(sql,new Object[] { start_time ,end_time, user_id  ,type_id});
	}
	
	/**
	 * 根据等级查询会员积分等级
	 * @param bank 等级
	 * @return
	 */
	public CreditRank getCreditRank(int rank){
		String sql = "select rank,point1,point2,pic from dw_credit_rank where rank = ? ";
		try {
			return getJdbcTemplate().queryForObject(sql, new Object[] { rank }, getBeanMapper(CreditRank.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return null; 
	}
	
	/**
	 * 根据会员ID修改会员积分等级
	 * @param bank 等级
	 * @param user_id
	 * @return
	 */
	public void updateCreditLevel(int rank , long user_id){
		try {
			String sql = "update dw_credit  set user_credit_level = ? where user_id = ?	";
			this.getJdbcTemplate().update(sql, rank, user_id);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 根据综合积分确定积分等级
	 * @param value 综合积分
	 * @return
	 */
	public CreditRank getCreditRankByValue(int value){
		String sql = "select rank,point1,point2,pic from dw_credit_rank where point1 <= ? and  point2 >= ? ";
		try {
			return getJdbcTemplate().queryForObject(sql, new Object[] { value ,value},getBeanMapper(CreditRank.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return null; 
	}
	
	/**
	 * 修改会员论坛积分
	 * @param value总积分变动值
	 * @param valid_value有效积分变动值
	 * @param bbs_value论坛积分变动值
	 * @param user_id 会员ID
	 */
	public void updateCreditBBS(long value , long valid_value , long bbs_value , long user_id){
		StringBuffer sql = new StringBuffer("update dw_credit set value = ifnull(value ,0) + :value , valid_value = ifnull(valid_value ,0) + :valid_value , bbs_value = ifnull(bbs_value ,0) + :bbs_value where user_id = :user_id ");
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("value", value);
		map.put("valid_value", valid_value);
		map.put("bbs_value", bbs_value);
		map.put("user_id", user_id);
	    this.getNamedParameterJdbcTemplate().update(sql.toString(), map);
	}
	
	//v1.6.7.2 RDPROJECT-510 cx 2013-12-12 start
	public List<UserCreditModel> getUserCreditModelList(SearchParam param){
		String sql = "select p1.*, p2.username, p2.realname,p3.pic as credit_pic " +
				"from dw_user as p2 " +
				"left join dw_credit as p1 on p1.user_id = p2.user_id " +
				"left join dw_credit_rank as p3 " +
				"on p1.value <= p3.point2 and p1.value >= p3.point1 " +
				"where 1 = 1 ";
		sql += param.getSearchParamSql();
		return getJdbcTemplate().query(sql, getBeanMapper(UserCreditModel.class));
	}
	//v1.6.7.2 RDPROJECT-510 cx 2013-12-12 end
}
