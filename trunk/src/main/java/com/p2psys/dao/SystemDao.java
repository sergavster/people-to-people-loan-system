package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.SystemConfig;

public interface SystemDao extends BaseDao {
	public List getsystem();
	
	/**
	 * 更新SystemConfig信息
	 * @param list
	 * 
	 */
	public void updateSystemById(List<SystemConfig> list);
	
	
	/**
	 * 根据模块获取系统设置
	 * @param i
	 * @return
	 */
	public List getSystemListBySytle(int i);
	
	public void addSystemConfig(SystemConfig systemConfig);
	
	public List getAllowIp();
	
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 start
	/**
	 * 根据nid查询
	 * @param nid nid
	 * @return 实体
	 */
	SystemConfig getByNid(String nid);
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-06 end
	
	// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 start
	/**
	 * 更新value 
	 * @param systemConfig systemConfig
	 */
	void modifySystemConfig(SystemConfig systemConfig);
	// v1.6.7.1 RDPROJECT-439 zza 2013-11-12 end
}
