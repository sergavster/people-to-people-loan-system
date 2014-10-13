package com.p2psys.fund.service;

import java.util.List;

import com.p2psys.fund.domain.FundTender;
import com.p2psys.fund.model.FundTenderModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

/**
 * 基金信托认购信息Service接口
 
 * @version 1.0
 * @since 2013-10-27
 */
public interface FundTenderService {
	
	/**
	 * 查
	 * @param param
	 * @return
	 */
	public List<FundTenderModel> list(int start,int pernum,SearchParam param);
	
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
	public FundTenderModel get(long id);
	
	/**
	 * 增
	 * @param model
	 * @return
	 */
	public int add(FundTender model);
	
	/**
	 * 改
	 * @param model
	 * @return
	 */
	public int modify(FundTender model);
	
}
