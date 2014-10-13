package com.p2psys.dao.jdbc;

import org.apache.log4j.Logger;

import com.p2psys.dao.CooperationLoginDao;
import com.p2psys.domain.CooperationLogin;

public class CooperationLoginDaoImpl extends BaseDaoImpl implements CooperationLoginDao {

	private static Logger logger = Logger.getLogger(CooperationLoginDaoImpl.class);

	/**
	 * 新增联合登陆信息
	 * @param cooperation
	 */
	public void addCooperationLogin(CooperationLogin c){
		String sql="insert into dw_cooperation_login(type,user_id,open_id,open_key,gmt_create) values(?,?,?,?,now())";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql,c.getType(),c.getUser_id(),c.getOpen_id(),c.getOpen_key());
	}
	
	/**
	 * 根据open id , open key和type查询联合登陆信息
	 * @param open_id
	 * @param opend_key
	 * @param type 合作登陆类型
	 * @return
	 */
	public CooperationLogin getCooperationLogin(String open_id , String opend_key , Byte type){
	
		String sql = "select * from dw_cooperation_login where open_id = ? and open_key = ? and type = ?";
		
		CooperationLogin cooperationLogin = null;
		
		try {
			cooperationLogin = this.getJdbcTemplate().queryForObject(sql,new Object[] { open_id, opend_key, type },
					getBeanMapper(CooperationLogin.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return cooperationLogin;
	}
	
	/**
	 * 根据user id查询联合登陆信息
	 * @param user_id会员Id
	 * @param type 合作登陆类型
	 * @return
	 */
	public CooperationLogin getCooperationLoginByUserId(long user_id , Byte type){
		
		String sql = "select * from dw_cooperation_login where user_id = ? and type = ?";
		
		CooperationLogin cooperationLogin = null;
		try {
			cooperationLogin = this.getJdbcTemplate().queryForObject(sql,new Object[] { user_id, type },
					getBeanMapper(CooperationLogin.class));
		} catch (Exception e) {
			logger.info("查询结果为空！");
		}
		return cooperationLogin;
	}
	
	
	/**
	 * 根据open id , open key和type修改联合登陆user id
	 * @param open_id
	 * @param open_key
	 * @param type 合作登陆类型
	 * @param user_id 会员ID
	 */
	public void updateCooperationUserId(long user_id , String open_id , String open_key , Byte type){
		
		String sql = "update dw_cooperation_login set user_id = ? where open_id = ? and open_key = ? and type = ?";
		
		this.getJdbcTemplate().update(sql , user_id , open_id , open_key , type);
	}
	
	/**
	 * 根据主键id修改联合登陆user id
	 * @param user_id
	 * @param id
	 */
	public void updateCooperationUserIdById(long user_id, long id){
		
		String sql = "update dw_cooperation_login set user_id = ? where id = ? ";
		
		this.getJdbcTemplate().update(sql , user_id ,id);
	}

	/**
	 * 根据id查询联合登陆信息
	 * @param id主键Id
	 * @return
	 */
	public CooperationLogin getCooperationLoginById(long id){
		
		String sql = "select * from dw_cooperation_login where id = ? ";
		CooperationLogin cooperationLogin = null;
		try {
			cooperationLogin = this.getJdbcTemplate().queryForObject(sql,new Object[] { id },
					getBeanMapper(CooperationLogin.class));
		} catch (Exception e) {
			logger.info("查询结果为空,或有多条信息！主键："+id);
		}
		return cooperationLogin;
		
	}
	
	
}
