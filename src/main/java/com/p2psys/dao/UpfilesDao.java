package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Upfiles;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UpfilesModel;

/**
 * 批量充值上传excel Dao
 * 
 
 * @version 1.0
 * @since 2013-8-20
 */
public interface UpfilesDao extends BaseDao {
	
	/**
	 * 添加excel记录
	 * @param upfiles
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
	public List<UpfilesModel> getAllUpfiles(int start, int pernum, SearchParam param, String type);
	
	/**
	 * 分页
	 * @param param
	 * @return
	 */
	int upfilesTotal(SearchParam param, String type);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Upfiles getUpfilesById(long id);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delUpfiles(long id);
	
}
