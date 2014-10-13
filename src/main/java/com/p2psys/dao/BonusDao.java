package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.BonusApr;
import com.p2psys.domain.Borrow;


public interface BonusDao extends BaseDao {
	
	public void addBonusApr(Borrow borrow);
	
	public void modifyBonusAprById(long id,double apr);
	
	public List getBonusAprList(long borrow_id);
	
	public BonusApr getBonus(long id);
	
}
