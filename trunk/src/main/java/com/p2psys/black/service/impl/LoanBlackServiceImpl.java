package com.p2psys.black.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.p2psys.black.dao.LoanBlackDao;
import com.p2psys.black.domain.LoanBlack;
import com.p2psys.black.service.LoanBlackService;
import com.p2psys.dao.UserDao;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.Page;
import com.p2psys.util.DateUtils;

@Service("loanBlackService")
public class LoanBlackServiceImpl implements LoanBlackService{
	@Autowired
	private LoanBlackDao loanBlackDao;
	@Autowired
	private UserDao userDao;
	
	

	@Override
	public PageDataList getLoanBlackList(SearchParam param, int startPage) {
		PageDataList plist = new PageDataList();
		int total = loanBlackDao.getCount(param);
		Page p = new Page(total, startPage);
		List<LoanBlack> loanBlackList = loanBlackDao.getLoanBlackList(param,
				p.getStart(), p.getPernum());
		plist.setPage(p);
		plist.setList(loanBlackList);
		return plist;
	}

	@Override
	public List<LoanBlack> getLoanBlackList(SearchParam param) {
		return loanBlackDao.getLoanBlackList(param, 0, 0);
	}

	@Override
	public List<LoanBlack> getLoanBlack(String username, String identity,
			String mobile, String email, String qq) {
		return loanBlackDao.getLoanBlack(username, identity,
				mobile, email, qq);
	}

	@Override
	public void addLoanBlack(String username) {
		User u=userDao.getUserByUsername(username);
		if(u!=null){
			//通过fk_ID查询，不存在返回true
			LoanBlack loanBlack=loanBlackDao.getLoanBlackByFkId(u.getUser_id());
			if(loanBlack!=null){
				loanBlack.setUsername(u.getRealname());
				loanBlack.setIdentity(u.getCard_id());
				loanBlack.setMobile(u.getPhone());
				loanBlack.setEmail(u.getEmail());
				loanBlack.setQq(u.getQq());
				loanBlack.setAddtime(DateUtils.getNowTime());
				loanBlack.setFk_id(u.getUser_id());
				loanBlackDao.updateLoanBlack(loanBlack);;
			}else{
				LoanBlack lb=new LoanBlack();
				lb.setPlatfrom("招商贷");
				lb.setIs_upload(0);
				lb.setUsername(u.getRealname());
				lb.setIdentity(u.getCard_id());
				lb.setMobile(u.getPhone());
				lb.setEmail(u.getEmail());
				lb.setQq(u.getQq());
				lb.setAddtime(DateUtils.getNowTime());
				lb.setFk_id(u.getUser_id());
				loanBlackDao.saveLoanBlack(lb);
			}
		}
	}
	
}
