package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.CreditCard;
import com.p2psys.model.SearchParam;

public interface CreditCardDao extends BaseDao {
	/**
	 * 查询所有的信用卡
	 * @return
	 */
	public List getList(int page, int max, SearchParam p);
	
	public int getSearchCard(SearchParam p);
	
	/**
	 * 新增信用卡
	 * @param c
	 * @return 
	 */
	public CreditCard addCreditCard(CreditCard c);
	
	/**
	 * 根据ID取得对应的信用卡信息
	 * @param cardId
	 * @return
	 */
	public CreditCard getCardById(int cardId);

	/**
	 * 修改
	 */
	public void updateCreditCard(CreditCard c);
	
	/**
	 * 根据类型取得对应的信用卡信息
	 * @param type
	 * @return
	 */
	public List getCardByType(int type);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delCreditCard(int cardId);
	
	/**
	 * 更新图片路径
	 */
	public void updateLitpic(String litpic, int cardId);

}
