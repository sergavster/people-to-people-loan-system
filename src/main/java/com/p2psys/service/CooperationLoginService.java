package com.p2psys.service;

import com.p2psys.domain.CooperationLogin;

/**
 * 联合登陆service
 
 *
 */
public interface CooperationLoginService {

	/**
	 * 新增联合登陆信息
	 * @param cooperation
	 */
	public void addCooperationLogin(CooperationLogin cooperation);
	
	/**
	 * 根据open id , open key和type查询联合登陆信息
	 * @param open_id
	 * @param opend_key
	 * @param type 合作登陆类型
	 * @return
	 */
	public CooperationLogin getCooperationLogin(String open_id , String opend_key , Byte type);
	
	/**
	 * 根据user id查询联合登陆信息
	 * @param user_id会员Id
	 * @param type 合作登陆类型
	 * @return
	 */
	public CooperationLogin getCooperationLoginByUserId(long user_id , Byte type);
	
	
	/**
	 * 根据open id , open key和type修改联合登陆user id
	 * @param user_id 会员Id
	 * @param open_id
	 * @param opend_key
	 * @param type 合作登陆类型
	 */
	public void updateCooperationUserId(long user_id, String open_id , String opend_key , Byte type);
	
	/**
	 * 根据主键id修改联合登陆user id
	 * @param user_id
	 * @param id
	 */
	public void updateCooperationUserIdById(long user_id, long id);
	
	/**
	 * 根据id查询联合登陆信息
	 * @param id主键Id
	 * @return
	 */
	public CooperationLogin getCooperationLoginById(long id);
	
}
