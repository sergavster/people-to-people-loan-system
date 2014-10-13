package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.AccountBack;
import com.p2psys.model.AccountBackModel;
import com.p2psys.model.SearchParam;

/**
 * 扣款管理Dao接口
 * 
 
 * @version 1.0
 * @since 2013-12-12
 */
public interface AccountBackDao extends BaseDao{
	/**
	 * 新增扣款
	 * @param back
	 */
	public void addBack(AccountBack back);

	/**
	 * 获取扣款记录数
	 * @param param
	 * @return
	 */
	public int getBackCount(SearchParam param);
	/**
	 * 获取扣款记录，分页
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List<AccountBack> getBackList(int start,int end,SearchParam param);
	/**
	 * 
	 * @param id
	 * @return
	 */
	public AccountBackModel getBack(long id);
	/**
	 * 更新审核扣款记录
	 * @param b
	 */
	public void updateBack(AccountBackModel b);
}
