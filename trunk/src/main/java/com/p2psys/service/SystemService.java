package com.p2psys.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.p2psys.domain.SystemConfig;
import com.p2psys.model.SystemInfo;

public interface SystemService {
	public SystemInfo getSystemInfo();
	
	public List getSystemInfoForList();
	/**
	 * 根据模块显示系统设置信息
	 * @return
	 */
	public List getSystemInfoForListBysytle(int i);
	/**
	 
	 * @param list<SystemConfig>
	 */
	public void updateSystemInfo(List list,ServletContext context);

	/**
	 
	 * @param url
	 * url 为网站根目录路径
	 */
	public void clean(String url);
	
	public void addSystemConfig(SystemConfig systemConfig);
}
