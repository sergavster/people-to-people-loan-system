package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.AccountRecharge;
import com.p2psys.domain.Upfiles;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

/**
 * 批量充值上传excel接口
 * 
 
 * @version 1.0
 * @since 2013-8-20
 */
public interface UpfilesService {
	
	/**
	 * 添加excel记录
	 * @param upfiles
	 * @return
	 */
	public void addUpfiles(Upfiles upfiles);
	
	/**
	 * 修改excel信息
	 * @param upfiles
	 * @return
	 */
	public void modifyUpfiles(Upfiles upfiles);
	
	/**
	 * excel记录列表
	 * @return
	 */
	public PageDataList getAllUpfiles(int page, SearchParam param, String type);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Upfiles getUpfilesById(long id);
	
	/**
	 * 页面根据excel生成列表
	 * @param data
	 * @return
	 */
	public List<AccountRecharge> addBatchRecharge(List[] data);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delUpfiles(long id);

}
