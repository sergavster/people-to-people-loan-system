package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.CreditCard;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface CreditCardService {
	/**
	 * 获取信用卡列表
	 * @return
	 */
	public PageDataList getList(int page, SearchParam p);
	
	/**
	 * 根据类型查询信用卡
	 * @param type
	 * @return
	 */
	public List getListByType(int type);
	
	/**
	 * 新增信用卡
	 * @param c
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
	 * 更新图片路径
	 */
	public void updateLitpic(String litpic, int cardId);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delCreditCard(int id);
}
