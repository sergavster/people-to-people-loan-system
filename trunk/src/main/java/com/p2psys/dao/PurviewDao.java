package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Purview;

public interface PurviewDao extends BaseDao {
	/**
	 * 根据Pid获取权限列表
	 * @return
	 */
	public List getPurviewByPid(long pid);
	
	/**
	 * 根据用户ID获取该用户的后台访问权限
	 * @param user_id
	 * @return
	 */
	public List getPurviewByUserid(long user_id);
	
	/**
	 * 获取所有的权限
	 * @return
	 */
	public List getAllPurview();
	/**
	 * 获取所有的权限,
	 * @return
	 */
	public List getAllCheckedPurview(long user_typeid);
	
	public void addPurview(Purview p);
	
	public Purview getPurview(long id);
	
	public void delPurview(long id);
	
	public boolean isRoleHasPurview(long id);
	
	public void modifyPurview(Purview p);
	
	public void addUserTypePurviews(List<Integer> purviewid,long user_type_id);
	
	public void delUserTypePurviews(long user_type_id) ;
	
}
