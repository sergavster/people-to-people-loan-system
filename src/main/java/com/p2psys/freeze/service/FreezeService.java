package com.p2psys.freeze.service;

import java.util.List;

import com.p2psys.freeze.domain.Freeze;
import com.p2psys.freeze.model.FreezeModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;


/**
 * 用户冻结Service接口
 
 * @version 1.0
 * @since 2013-10-29
 */
public interface FreezeService {

	/**
	 * 分页查询
	 * @param start
	 * @param pernum
	 * @param param
	 * @return
	 */
	public PageDataList page(int startPage,SearchParam param);
	
	/**
	 * 查
	 * @param start
	 * @param pernum
	 * @param param
	 * @return
	 */
	public List<FreezeModel> list(int start,int pernum,SearchParam param);
	
	/**
	 * 增
	 * @param model
	 * @return
	 */
	public int add(Freeze model);
	
	/**
	 * 查-根据主键获取对象
	 * @param id
	 * @return
	 */
	public FreezeModel get(long id);
	
	/**
	 * 查-根据用户ID获取对象
	 * @param id
	 * @return
	 */
	public FreezeModel getByUserId(long userId);
	
	/**
	 * 查-根据用户名获取对象
	 * @param id
	 * @return
	 */
	public FreezeModel getByUserName(String username);
	
	/**
	 * 查-根据用户名获取对象
	 * @param id
	 * @return
	 */
	public boolean isExistsByUserName(String username);
	
	/**
	 * 改
	 * @param model
	 * @return
	 */
	public int modify(Freeze model);
	
	/**
	 * 改-修改某一字段
	 * @param column
	 * @param value
	 * @param id
	 * @return
	 */
	public int modify(String column, Object value ,long id);
	
}
