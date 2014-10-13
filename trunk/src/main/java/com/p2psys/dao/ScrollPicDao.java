package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.ScrollPic;
import com.p2psys.model.SearchParam;

/**
 * 
 * TODO 首页滚动图片相关的增删改查
 * 
 
 * @version 1.0
 * @since 2013-7-16
 */
public interface ScrollPicDao extends BaseDao {
	/**
	 * 获取list集合
	 * @param start 起始
	 * @param end   结束
	 * @return
	 */
	public List getList(int type_id,int start, int end);
	/**
	 * 获取list集合
	 * @param start 起始
	 * @param end   结束
	 * @return
	 */
	public List<ScrollPic> getList(int start, int end);
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public ScrollPic getScrollPicById(long id);
	/**
	 * 删除
	 * @param id
	 */
	public void delScrollPic(long id);
	/**
	 * 修改
	 * @param sp
	 */
	public void modifyScrollPic(ScrollPic sp);
	/**
	 * 添加
	 * @param sp
	 * @return
	 */
	public ScrollPic addScrollPic(ScrollPic sp);
	/**
	 * 获取后台图片所有类型
	 * @return
	 */
	public List getScrollPicType();
	/**
	 * 获取后台图片数量
	 * @param type_id
	 * @param param
	 * @return
	 */
	public int count(int type_id,SearchParam param);
}
