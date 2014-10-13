package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.AutoTenderOrder;

public interface BorrowAutoDao extends BaseDao {
	
	public List getBorrowAutoList(long user_id);
	
//	public void addBorrowAuto(BorrowAuto auto);
//	
//	public void updateBorrowAuto(BorrowAuto auto);
	
	public void deleteBorrowAuto(long id);
	
//	public void insertAutoTender();
	
	public List getBorrowAutoListByStatus(long status);
	
	public void updateAutoTenderOrder(AutoTenderOrder auto);
	public AutoTenderOrder getAutoTenderOrder(long user_id);
	public List getAutoTenderOrder();
	public int getAutoTenderOrderCount();
}
