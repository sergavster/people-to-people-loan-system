package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.PasswordToken;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface PasswordTokenService {

	public void updatePasswordTokenByList(List<PasswordToken> list);

	public void addPasswordToken(List<PasswordToken> tokenList, long userId);

	public void deletePasswordTokenById(long id);

	public List<PasswordToken> getPasswordToken();
	
	public List<PasswordToken> getPasswordTokenByUserId(long userId);
	
	public String getAnswerByQuestion(String question, long userId);
	
	public List<PasswordToken> getPasswordTokenByUsername(String username);
	
//  v1.6.6.2 RDPROJECT-315  2013-10-28 gc start
	/**
	 * 根据用户名、真实姓名查询密保列表
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDataList getPasswordTokenList(SearchParam param,int page);
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
