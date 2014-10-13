package com.p2psys.fund.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.fund.dao.FundTenderDao;
import com.p2psys.fund.domain.FundTender;
import com.p2psys.fund.model.FundTenderModel;
import com.p2psys.fund.service.FundTenderService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.Page;

@Service
public class FundTenderServiceImpl implements FundTenderService {
	
	@Resource
	private FundTenderDao fundTenderDao;
	
	@Override
	public List<FundTenderModel> list(int start,int pernum,SearchParam param) {
		return fundTenderDao.list(start, pernum, param);
	}
	
	@Override
	public PageDataList page(int startPage, SearchParam param) {
		int total = fundTenderDao.count(param);
		Page p = new Page(total, startPage);
		List<FundTenderModel> list = fundTenderDao.list(p.getStart(), p.getPernum(), param);
		PageDataList plist = new PageDataList(p, list);
		return plist;
	}
	
	@Override
	public FundTenderModel get(long id) {
		return fundTenderDao.get(id);
	}

	@Override
	public int add(FundTender model) {
		return fundTenderDao.add(model);
	}

	@Override
	public int modify(FundTender model) {
		return fundTenderDao.modify(model);
	}
	
	public FundTenderDao getFundTenderDao() {
		return fundTenderDao;
	}

	public void setFundTenderDao(FundTenderDao fundTenderDao) {
		this.fundTenderDao = fundTenderDao;
	}


}
