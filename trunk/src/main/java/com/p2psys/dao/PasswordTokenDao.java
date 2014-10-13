package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.PasswordToken;
import com.p2psys.model.SearchParam;

public interface PasswordTokenDao extends BaseDao {
	
	/**
	 * 获取列表
	 * @return
	 */
	public List<PasswordToken> getAllList();

	/**
	 * 添加 
	 * @param passwordToken
	 * @return
	 */
	public void addPasswordToken(List<PasswordToken> tokenList, long userId);

	/**
	 * 根据id删除
	 * @param id
	 */
	public void deletePasswordTokenById(long id);

	/**
	 * 批量更新
	 * @param list
	 */
	public void updatePasswordTokenByList(List<PasswordToken> list);
	
	/**
	 * 根据用户获取密保信息
	 * @return
	 */
	public List<PasswordToken> getPasswordTokenByUserId(long userId);
	
	/**
	 * 根据问题查询答案
	 * @param question
	 */
	public String getAnswerByQuestion(String question, long userId);
	
	/**
	 * 根据用户名查询密保列表
	 * @param username
	 * @return
	 */
	public List<PasswordToken> getPasswordTokenByUsername(String username);
	
	//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc start
	/**
	 * 根据用户名、真实姓名查询密保列表
	 * @param param
	 * @param page
	 * @return
	 */
	public List getPasswordTokenList(int page, int max,SearchParam param);
	
	public int getPasswordTokenCount(SearchParam param);
//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc end
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-05 start
	/**
	 * 根据id获取密保
	 * @param id
	 * @return
	 */
	public PasswordToken getPasswordTokenById(long id);
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-05 end

}
