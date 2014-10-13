package com.p2psys.report.dao;

import java.util.List;

import com.p2psys.report.domain.Column;
import com.p2psys.report.model.ColumnModel;

/**
 * 导出报表列名表DAO
 
 * @version 1.0
 * @since 2013-11-5
 */
public interface ColumnDao {

	/**
	 * 导出报表table分页数
	 * @param column
	 * @return
	 */
	public int getColumnCount(Column column);
	
	/**
	 * 导出报表table分页
	 * @param page分页开始页
	 * @param max分页数量
	 * @param Column
	 * @return
	 */
	public List<Column> getColumnPage(int page, int max,Column column);
	
	/**
	 * 根据条件查询信息
	 * @param column 查询参数
	 * @return 返回model List
	 */
	public List<ColumnModel> getColumnModelList(ColumnModel column);
	
	/**
	 * 根据条件查询信息
	 * @param column 查询参数
	 * @return 返回实体List
	 */
	public List<Column> getColumnList(Column column);

	/**
	 * 根据ID查询单条列名信息
	 * @param id
	 * @return
	 */
	public Column getColumnById(long id);
	
	/**
	 * 添加信息
	 * @param column 需要处理的参数
	 * @return 返回主键ID
	 */
	public long insertColumn(Column column);
	
	/**
	 * 根据id修改信息
	 * @param column
	 * @return
	 */
	public void editColumnById(Column column);
	
	/**
	 * 删除列表
	 * @param id
	 */
	public void deleteById(long id);
	
}
