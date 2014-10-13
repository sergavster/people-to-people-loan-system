package com.p2psys.freeze.service.impl;

import java.util.List;

import com.p2psys.freeze.dao.FreezeDao;
import com.p2psys.freeze.domain.Freeze;
import com.p2psys.freeze.model.FreezeModel;
import com.p2psys.freeze.service.FreezeService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.Page;

public class FreezeServiceImpl implements FreezeService{
	
	private FreezeDao freezeDao;
	
	@Override
	public List<FreezeModel> list(int start, int pernum, SearchParam param) {
		return this.freezeDao.list(start, pernum, param);
	}
	
	@Override
	public PageDataList page(int startPage, SearchParam param) {
		int total = freezeDao.count(param);
		Page p = new Page(total, startPage);
		List<FreezeModel> list = freezeDao.list(p.getStart(), p.getPernum(), param);
		PageDataList plist = new PageDataList(p, list);
		return plist;
	}

	@Override
	public int add(Freeze model) {
		return freezeDao.add(model);
	}

	@Override
	public FreezeModel get(long id) {
		return freezeDao.get(id);
	}
	
	public FreezeModel getByUserId(long userId){
		return freezeDao.getByUserId(userId);
	}
	
	public FreezeModel getByUserName(String username){
		return freezeDao.getByUserName(username);
	}
	
	@Override
	public boolean isExistsByUserName(String username) {
		return freezeDao.isExistsByUserName(username);
	}

	@Override
	public int modify(Freeze model) {
		return freezeDao.modify(model);
	}

	@Override
	public int modify(String column, Object value, long id) {
		return freezeDao.modify(column, value, id);
	}
	
	public FreezeDao getFreezeDao() {
		return freezeDao;
	}
	
	public void setFreezeDao(FreezeDao freezeDao) {
		this.freezeDao = freezeDao;
	}


}
