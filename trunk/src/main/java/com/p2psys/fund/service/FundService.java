package com.p2psys.fund.service;

import java.util.List;

import com.p2psys.domain.User;
import com.p2psys.fund.domain.Fund;
import com.p2psys.fund.model.FundModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

/**
 * 基金信托Service接口
 
 * @version 1.0
 * @since 2013-10-23
 */
public interface FundService {
	
	/**
	 * 查
	 * @param param
	 * @return
	 */
	public List<FundModel> list(int start,int pernum,SearchParam param);
	
	/**
	 * 分页查询
	 * @param start
	 * @param pernum
	 * @param param
	 * @return
	 */
	public PageDataList page(int startPage,SearchParam param);
	
	/**
	 * 查-根据主键获取对象
	 * @param id
	 * @return
	 */
	public FundModel get(long id);
	
	/**
	 * 增
	 * @param model
	 * @return
	 */
	public int add(Fund model);
	
	/**
	 * 改
	 * @param model
	 * @return
	 */
	public int modify(Fund model);
	
	/**
	 * 改-修改某一字段
	 * @param column
	 * @param value
	 * @param id
	 * @return
	 */
	public int modify(String column, Object value ,long id);
	
	/**
	 * 提交认购意向
	 * @param fundId
	 * @param account
	 * @return
	 */
	public int addTender(long id, double account, User user);
}
