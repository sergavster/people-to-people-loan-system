package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.LogTemplate;

/**
 * 
 * 日志模板处理Service
 * 
 
 * @version 1.0
 * @since 2013-10-11
 */
public interface LogTemplateService {

	/**
	 * 根据模板类型和日志类型查询日志模板
	 * @param type 模板类型：1资金日志，2合计日志，3会员日志
	 * @param log_type 日志类型
	 * @return 日志模板
	 */
	LogTemplate getLogTemplate(byte type , String log_type);
	
	/**
	 * 查询所有的日志模板信息
	 * @return
	 */
	List<LogTemplate> getLogTemplateAll();
	
	/**
	 * 修改日志模板
	 * @param logTemplate 日志模板
	 */
	void editLogTemplate(LogTemplate logTemplate);
	
	/**
	 * 添加日志模板
	 * @param logTemplate 日志模板
	 */
	void insertLogTemplate(LogTemplate logTemplate);
}
