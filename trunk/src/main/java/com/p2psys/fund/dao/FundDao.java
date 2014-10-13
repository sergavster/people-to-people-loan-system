package com.p2psys.fund.dao;

import java.util.List;

import com.p2psys.fund.domain.Fund;
import com.p2psys.fund.model.FundModel;
import com.p2psys.model.SearchParam;

/**
 * 基金信托Dao接口
 
 * @version 1.0
 * @since 2013-10-22
 */
public interface FundDao {
	
	/**
	 * 计数
	 * @param param
	 * @return
	 */
	public int count(SearchParam param);
	
	/**
	 * 查
	 * @param start
	 * @param pernum
	 * @param param
	 * @return
	 */
	public List<FundModel> list(int start,int pernum,SearchParam param);
	
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
	 * 认购
	 * @param id
	 * @param account
	 * @return
	 */
	public int updateTener(long id, double account);
	
}
