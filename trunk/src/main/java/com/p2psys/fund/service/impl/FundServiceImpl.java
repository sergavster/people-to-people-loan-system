package com.p2psys.fund.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.p2psys.domain.User;
import com.p2psys.exception.BussinessException;
import com.p2psys.fund.dao.FundDao;
import com.p2psys.fund.dao.FundTenderDao;
import com.p2psys.fund.domain.Fund;
import com.p2psys.fund.domain.FundTender;
import com.p2psys.fund.model.FundModel;
import com.p2psys.fund.service.FundService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.Page;
import com.p2psys.util.DateUtils;

@Service
public class FundServiceImpl implements FundService {
	
	private static final Logger logger=Logger.getLogger(FundServiceImpl.class);
	
	@Resource
	private FundDao fundDao;
	@Resource
	private FundTenderDao fundTenderDao;
	
	@Override
	public List<FundModel> list(int start,int pernum,SearchParam param) {
		return fundDao.list(start, pernum, param);
	}
	
	@Override
	public PageDataList page(int startPage, SearchParam param) {
		int total = fundDao.count(param);
		Page p = new Page(total, startPage);
		List<FundModel> list = fundDao.list(p.getStart(), p.getPernum(), param);
		PageDataList plist = new PageDataList(p, list);
		return plist;
	}
	
	@Override
	public FundModel get(long id) {
		return fundDao.get(id);
	}

	@Override
	public int add(Fund model) {
		return fundDao.add(model);
	}

	@Override
	public int modify(Fund model) {
		return fundDao.modify(model);
	}
	
	@Override
	public int modify(String colum, Object value, long id) {
		return fundDao.modify(colum, value, id);
	}
	
	@Override
	public int addTender(long id, double account, User user){
		logger.info("fund addTender begin, fundId:"+id+" ,userId:"+user.getUser_id());
		//用户状态
		if(user.getReal_status()==0){
			throw new BussinessException("请先进行实名认证!");
		}
		if(user.getPhone_status()==0){
			throw new BussinessException("请先进行手机认证!");
		}
		FundModel model = fundDao.get(id);
		if(model==null){
			throw new BussinessException("未找到对应基金信托信息!");
		}
		int remainTime = model.getAddTime()+Integer.parseInt(model.getValidTime())*24*60*60-DateUtils.getNowTime();
		//基金信托状态
		if(model.getStatus()!=1 || model.getAccountYes()>=model.getAccount() || remainTime<0){
			logger.info("model.status:"+model.getStatus());
			logger.info("model.account:"+model.getAccount());
			logger.info("model.accountYes:"+model.getAccountYes());
			logger.info("remainTime:"+remainTime);
			throw new BussinessException("该基金信托已结束,认购意向提交失败!");
		}
		//认购金额
		if(account<model.getLowestAccount()){
			throw new BussinessException("认购金额不能低于最低认购金额,认购意向提交失败!");
		}
		if(account>model.getMostAccount() && model.getMostAccount()>0){
			throw new BussinessException("认购金额不能高于最大认购金额,认购意向提交失败!");
		}
		double surplus = model.getAccount()-model.getAccountYes();
		account = (account > surplus)?surplus:account;
		//update account_yes
		int r = fundDao.updateTener(id, account);
		if(r!=1){
			throw new BussinessException("认购意向提交失败,可能原因是认购已满!");
		}
		if(account>=surplus){
			r = fundDao.modify("status", 8, id);
			if(r==1){
				logger.info("基金信托信息认购已满,修改状态为8,成功,id:"+id);
			}else{
				logger.info("基金信托信息认购已满,修改状态为8,失败,id:"+id);
			}
		}
		FundTender tender = new FundTender();
		tender.setUserId(user.getUser_id());
		tender.setFundId(id);
		tender.setAccount(account);
		tender.setAddTime(DateUtils.getNowTime());
		//添加认购信息
		r = fundTenderDao.add(tender);
		if(r!=1){
			throw new BussinessException("认购信息存储失败！");
		}
		logger.info("fund addTender end, result:"+r);
		return r;
	}
	

}
