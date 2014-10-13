package com.p2psys.fund.dao;

import java.util.List;

import com.p2psys.dao.BaseDao;
import com.p2psys.fund.domain.FundTender;
import com.p2psys.fund.model.FundTenderModel;
import com.p2psys.model.SearchParam;

/**
 * 基金信托认购信息Dao接口
 
 * @version 1.0
 * @since 2013-10-27
 */
public interface FundTenderDao extends BaseDao {
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
	public List<FundTenderModel> list(int start,int pernum,SearchParam param);
	
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
