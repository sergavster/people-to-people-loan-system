package com.p2psys.creditassignment.service;

import java.util.List;
import java.util.Map;

import com.p2psys.creditassignment.domain.CreditAssignment;
import com.p2psys.domain.Account;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;


/**
 * 债权转让接口
 
 * @version 1.0
 * @since 2013-12-16
 */
public interface CreditAssignmentService {
	
	/**
	 * 发布债权转让
	 * @param creditAssignment 债权实体数据
	 * @return
	 */
	public boolean add(CreditAssignment creditAssignment);
	
	/**
	 * 债权转让撤回
	 * @param sellerAct 操作者资金帐户实体数据
	 * @param creditAssignment 债权实体数据
	 * @return
	 */
	public void cancel(Account sellerAct, CreditAssignment ca);
	
	/**
	 * 查询指定用户债权转出列表
	 * @param user_id 查询用户的id
	 * @return
	 */
	public PageDataList getPageCAS(int page,Map<String , Object> map);
	/**
	 * 查询指定用户债权购买列表
	 * @param user_id 查询用户的id
	 * @return
	 */
	public PageDataList listInCas(int page, SearchParam param);
	
	/**
	 * 查询指定债权转让详细信息
	 * @param caId 债权转让id
	 * @return
	 */
	public CreditAssignment getOne(long caId);
	
	/**
	 * 债权购买
	 * @param buyerAct 购买人帐户实体
	 * @param ca 债权转让实体
	 * @param buyAccount 购买金额
	 * @return
	 */
	public void buy(Account buyerAct, CreditAssignment ca, double buyAccount);
	
	/**
	 * 债权价值计算
	 * @param user_id 债权人id
	 * @param fk_id 债权对应的Id
	 * @param type 债权转让类型：1标级别转让，2tender级别转让，3collection级别转出
	 * @return
	 */
	public double calCaValue(long user_id, long fk_id , byte type);
	
	/**
	 * 初审债权转让
	 * @param ca 债权转让实体
	 * @param status 审核状态
	 * @return
	 */
	public void verifyCa(CreditAssignment ca, int status);
	/**
	 * 复审债权转让
	 * @param ca 债权转让实体
	 * @param status 审核状态
	 * @return
	 */
	public void fullCa(CreditAssignment ca, int status);
	
	/**
	 * 查询债权转让信息
	 * @param map 查询参数
	 * @return
	 */
	public List<CreditAssignment> getList(Map<String, Object> map);
	
}
