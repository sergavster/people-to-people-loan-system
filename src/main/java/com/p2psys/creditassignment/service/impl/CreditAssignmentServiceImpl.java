package com.p2psys.creditassignment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.creditassignment.dao.CaTradeSerialDao;
import com.p2psys.creditassignment.dao.CreditAssignmentDao;
import com.p2psys.creditassignment.domain.CaTradeSerial;
import com.p2psys.creditassignment.domain.CreditAssignment;
import com.p2psys.creditassignment.service.CreditAssignmentService;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.CollectionDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.Collection;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.creditassignment.BuyCaFeeBackLog;
import com.p2psys.model.accountlog.creditassignment.BuyCaFeeFreezeLog;
import com.p2psys.model.accountlog.creditassignment.BuyCaFreezeLog;
import com.p2psys.model.accountlog.creditassignment.BuyCaSuccLog;
import com.p2psys.model.accountlog.creditassignment.BuyCaUnfreezeLog;
import com.p2psys.model.accountlog.creditassignment.SellCaSuccLog;
import com.p2psys.tool.Page;
import com.p2psys.util.CaUtil;
import com.p2psys.util.DateUtils;

@Service
public class CreditAssignmentServiceImpl implements CreditAssignmentService{
	
	@Resource
	private CreditAssignmentDao creditAssignmentDao;
	@Resource
	private CaTradeSerialDao caTradeSerialDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private CollectionDao collectionDao;

	@Override
	public boolean add(CreditAssignment creditAssignment) {
		//查询此债权是否在转让中，如果是，则转让失败
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("sell_user_id", creditAssignment.getSell_user_id());
		map.put("related_borrow_id", creditAssignment.getRelated_borrow_id());
		List<Byte> statusList = new ArrayList<Byte>();
		statusList.add(Constant.CA_STATUS_INIT);
		statusList.add(Constant.CA_STATUS_VERIFY_SUCC);
		statusList.add(Constant.CA_STATUS_INIT);
		map.put("statusList", statusList);
		List<CreditAssignment> creaditAssList = creditAssignmentDao.getList(map);
		if(creaditAssList != null && creaditAssList.size() > 0){
			creditAssignmentDao.add(creditAssignment);
			return true;
		}
		return false;
	}
	/*
	 * 债权转让取消或者审核失败时，退回用户的资金
	 * @param caId 撤销的债权转让标
	 */
	private void CancelTradeSerial(long caId){
		//获取认购列表
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("caId", caId);
		map.put("status", Constant.CATS_STATUS_INIT);
		
		List<CaTradeSerial> catsList = caTradeSerialDao.getCatsList(map);
		//退回冻结的认购资金
		for (CaTradeSerial caTradeSerial : catsList) {
			long buyerId = caTradeSerial.getBuy_user_id();
			//退回资金
			Account buyerAct = accountDao.getAccount(buyerId);
			double buyAccount = caTradeSerial.getBuy_account();
			accountDao.updateAccount(0, buyAccount, -buyAccount, buyerId);
			
			Global.setTransfer("money", buyAccount);
			BaseAccountLog bcuLog=new BuyCaUnfreezeLog(buyAccount, buyerAct, buyerId);
			bcuLog.doEvent();
			
			//退回冻结的认购手续费
			buyerAct = accountDao.getAccount(buyerId);
			double buyFee = caTradeSerial.getBuy_account();
			accountDao.updateAccount(0, buyFee, -buyFee, buyerId);
			
			Global.setTransfer("money", buyFee);
			BaseAccountLog bcfbLog=new BuyCaFeeBackLog(buyFee, buyerAct, buyerId);
			bcfbLog.doEvent();
			
			//修改认购记录状态
			caTradeSerial.setStatus(Constant.CATS_STATUS_CANCEL);
			caTradeSerialDao.modifyCa(caTradeSerial);
		}
	}

	/**
	 * 债权转让撤回
	 * @param sellerAct 操作者资金帐户实体数据
	 * @param creditAssignment 债权实体数据
	 * @return
	 */
	@Override
	public void cancel(Account sellerAct, CreditAssignment ca) {
		//设置债权转让的状态为取消
		ca.setStatus(Constant.CA_STATUS_CANCEL);
		creditAssignmentDao.modifyCa(ca);
				
		//撤销认购记录
		CancelTradeSerial(ca.getId());
	}

	@Override
	public PageDataList getPageCAS(int page, Map<String , Object> map) {
		if(map == null) map = new HashMap<String, Object>();
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(creditAssignmentDao.getPageCount(map) , page );
		pageDataList.setList(creditAssignmentDao.getPageList(pages.getStart(), pages.getPernum() , map));
		pageDataList.setPage(pages);
		return pageDataList;
	}

	@Override
	public PageDataList listInCas(int page, SearchParam param) {
		return null;
	}

	@Override
	public CreditAssignment getOne(long caId) {
		return creditAssignmentDao.getOne(caId);
	}
	
	/**
	 * 债权购买核心方法
	 * @param buyerAct 购买人帐户实体
	 * @param ca 债权转让实体
	 * @param buyAccount 购买金额
	 * @return
	 */
	@Override
	public void buy(Account buyerAct, CreditAssignment ca, double buyAccount) {
		long buyerId = buyerAct.getUser_id();
		//数据效验已经全部完成后才能进入这个核心方法，因此核心方法内部不继续检验
		
		//冻结购买需要的手续费
		
		//冻结购买需要支付的费用
		accountDao.updateAccount(0, -buyAccount, buyAccount, buyerId);
		
		Global.setTransfer("money", buyAccount);
		BaseAccountLog bcfLog=new BuyCaFreezeLog(buyAccount, buyerAct, buyerId);
		bcfLog.doEvent();
		
		//冻结认购手续费
		double buyFee = CaUtil.calBuyFee(buyAccount);
		accountDao.updateAccount(0, -buyFee, buyFee, buyerId);
		
		Global.setTransfer("money", buyFee);
		BaseAccountLog bcffLog=new BuyCaFeeFreezeLog(buyFee, buyerAct, buyerId);
		bcffLog.doEvent();
		
		
		//修改债权转让的认购情况
		double newSoldAccount = ca.getSold_account() + buyAccount;
		ca.setSold_account(newSoldAccount);
		creditAssignmentDao.modifyCa(ca);
		
		//算出出售所需要的手续费，但不需要冻结
		double sellFee = CaUtil.calSellFee(buyAccount);
		
		//生成债权认购记录
		CaTradeSerial caTradeSerial = new CaTradeSerial();
		
		caTradeSerial.setCa_id(ca.getId());
		caTradeSerial.setStatus(Constant.CATS_STATUS_INIT);
		caTradeSerial.setBuy_user_id(Integer.parseInt(String.valueOf(buyerId)));
		caTradeSerial.setBuy_time(DateUtils.getNowTime());
		caTradeSerial.setBuy_account(buyAccount);
		caTradeSerial.setBuy_fee(buyFee);
		caTradeSerial.setBuy_fee_status(Constant.CA_BUY_FEE_STATUS_FREEZE);
		caTradeSerial.setSell_fee(sellFee);
		caTradeSerial.setSell_fee_status(Constant.CA_SELL_FEE_STATUS_UNPAYED);
		caTradeSerial.setAddtime(DateUtils.getNowTime());
		
		caTradeSerialDao.add(caTradeSerial);
	}

	/**
	 * 债权价值计算
	 * @param user_id 债权人id
	 * @param fk_id 债权对应的Id
	 * @param type 债权转让类型：1标级别转让，2tender级别转让，3collection级别转出
	 * @return
	 */
	public double calCaValue(long user_id, long fk_id , byte type) {
		double sRepayAccount = 0;
		double sRepayAward = 0;
		Map<String , Object> map = new HashMap<String, Object>();
		if(type == Constant.CA_TYPE_BORROW){
			map.put("borrowId", fk_id);
		}else if(type == Constant.CA_TYPE_TENDER){
			map.put("tenderId", fk_id);
		}else if(type == Constant.CA_TYPE_COLLECTION){
			map.put("collectionId", fk_id);
		}
		map.put("user_id", user_id);
		//查出债权对应的待收
		List<Collection> sellerColList= collectionDao.getCollectionList(map);
		for (Collection collection : sellerColList) {
			sRepayAccount += Double.parseDouble(collection.getRepay_account());
			sRepayAward += collection.getRepay_award();
		}
		
		return sRepayAccount + sRepayAward;
	}

	@Override
	public void verifyCa(CreditAssignment ca, int status) {
		//设置债权转让状态
		if (Constant.CA_STATUS_VERIFY_SUCC == status){
			//初审成功
			ca.setStatus(Constant.CA_STATUS_VERIFY_SUCC);
		}else{
			//初审失败
			ca.setStatus(Constant.CA_STATUS_VERIFY_FAIL);
		}
		
		creditAssignmentDao.modifyCa(ca);
	}

	@Override
	public void fullCa(CreditAssignment ca, int status) {
		//设置债权转让状态
		if (Constant.CA_STATUS_FULL_SUCC == status){
			//复审成功
			ca.setStatus(Constant.CA_STATUS_FULL_SUCC);
		}else{
			//复审失败
			ca.setStatus(Constant.CA_STATUS_FULL_FAIL);
		}
		creditAssignmentDao.modifyCa(ca);
		long sellerUserId = ca.getSell_user_id();
		long caRelateBorrowId = ca.getRelated_borrow_id();
		double caValue = ca.getCredit_value();
		double caPrice = ca.getCredit_price();
		
		if (Constant.CA_STATUS_FULL_SUCC == status){
			//复审成功
			
			Account sellerAct = accountDao.getAccount(sellerUserId);
			
			//查出债权对应的待收
			byte type = ca.getType();
			Map<String , Object> mapCa = new HashMap<String, Object>();
			if(type == Constant.CA_TYPE_BORROW){
				mapCa.put("borrowId", ca.getRelated_borrow_id());
			}else if(type == Constant.CA_TYPE_TENDER){
				mapCa.put("tenderId", ca.getRelated_tender_id());
			}else if(type == Constant.CA_TYPE_COLLECTION){
				mapCa.put("collectionId", ca.getRelated_collection_id());
			}
			mapCa.put("user_id", sellerUserId);
			List<Collection> sellerColList= collectionDao.getCollectionList(mapCa);
			
			//处理所有认购记录
			//获取认购列表
			Map<String , Object> map = new HashMap<String, Object>();
			map.put("caId", ca.getId());
			map.put("status", Constant.CATS_STATUS_INIT);
			List<CaTradeSerial> catsList = caTradeSerialDao.getCatsList(map);
			//外层为购买流水，内层为待收，处理资金，生成新待收
			for (CaTradeSerial caTradeSerial : catsList) {
				long buyerId = caTradeSerial.getBuy_user_id();
				//扣除认购的冻结资金
				double buyAccount = caTradeSerial.getBuy_account();
				accountDao.updateAccount(-buyAccount, 0, -buyAccount, buyerId);
				
				Global.setTransfer("money", buyAccount);
				Account buyerAct = accountDao.getAccount(buyerId);
				BaseAccountLog bcsLog=new BuyCaSuccLog(buyAccount, buyerAct, buyerId);
				bcsLog.doEvent();
				
				//扣除认购手续费
				double buyFee = CaUtil.calBuyFee(buyAccount);
				accountDao.updateAccount(0, -buyFee, buyFee, buyerId);
				
				Global.setTransfer("money", buyFee);
				BaseAccountLog bcffLog=new BuyCaFeeFreezeLog(buyFee, buyerAct, buyerId);
				bcffLog.doEvent();
				
				//设置债权认购状态
				caTradeSerial.setStatus(Constant.CATS_STATUS_SUCC);
				caTradeSerialDao.modifyCa(caTradeSerial);
				
				//通过认购份额计算出应得的债权比例
				double caPercent = buyAccount / caPrice; 
				for (Collection sellerCol : sellerColList) {
					double sRepayAccount = Double.parseDouble(sellerCol.getRepay_account());
					double sCapital = Double.parseDouble(sellerCol.getCapital());
					double sInterest = Double.parseDouble(sellerCol.getInterest());
					double sRepayAward = sellerCol.getRepay_award();
										
					//生成新的待收
					Collection newCol = new Collection();
					newCol.setUser_id(buyerId);
					newCol.setBorrow_id(caRelateBorrowId);
					newCol.setStatus(Constant.COLLECTION_STATUS_NORMAL);
					
					//购买债权的算法是：原待收 * 认购到的债权比例
					newCol.setRepay_account(String.valueOf(sRepayAccount * caPercent));
					newCol.setCapital(String.valueOf(sCapital * caPercent));
					newCol.setInterest(String.valueOf(sInterest * caPercent));
					newCol.setRepay_award(sRepayAward * caPercent);
					newCol.setRepay_time(sellerCol.getRepay_time());
					newCol.setOrder(sellerCol.getOrder());
					
					collectionDao.addCollection(newCol);
				}
				
				//支付认购债权金额给债权人
				accountDao.updateAccount(buyAccount, buyAccount, 0, sellerUserId);
				
				Global.setTransfer("money", buyAccount);
				BaseAccountLog scsLog=new SellCaSuccLog(buyAccount, sellerAct, sellerUserId);
				scsLog.doEvent();
			}
			
			//原待收列表为外层，内层为认购序列两重循环处理旧待收
			for (Collection sellerCol : sellerColList) {
				double sRepayAccount = Double.parseDouble(sellerCol.getRepay_account());
				double sCapital = Double.parseDouble(sellerCol.getCapital());
				double sInterest = Double.parseDouble(sellerCol.getInterest());
				double sRepayAward = sellerCol.getRepay_award();
				
				for (CaTradeSerial caTradeSerial : catsList) {
					
					double buyAccount = caTradeSerial.getBuy_account();
					
					//通过认购份额计算出应得的债权比例
					double caPercent = buyAccount / caPrice; 
					
					//购买债权的算法是：原待收 * 认购到的债权比例
					String nRepayAccount = String.valueOf(Double.parseDouble(sellerCol.getRepay_account()) - (sRepayAccount * caPercent));
					sellerCol.setRepay_account(nRepayAccount);
					
					String nCapital = String.valueOf(Double.parseDouble(sellerCol.getCapital()) - (sCapital * caPercent)); 
					sellerCol.setCapital(nCapital);
					
					String nInterest = String.valueOf(Double.parseDouble(sellerCol.getInterest()) - (sInterest * caPercent));
					sellerCol.setInterest(nInterest);
					
					double nRepayAward = sellerCol.getRepay_award() -  (sRepayAward * caPercent);
					
					sellerCol.setRepay_award(nRepayAward);
					
				}
				//债权全部转让了
				if (0 == Double.parseDouble(sellerCol.getRepay_account())){
					sellerCol.setStatus(Constant.COLLECTION_STATUS_CA);
				}
				//内层的认购流水处理完后，这一条原待收也就全处理完了，可以保存了
				collectionDao.modifyCollection(sellerCol);
			}
			
		}else{
			//复审失败
			//撤销所有认购记录
			CancelTradeSerial(ca.getId());
		}
	}
	
	/**
	 * 查询债权转让信息
	 * @param map 查询参数
	 * @return
	 */
	public List<CreditAssignment> getList(Map<String, Object> map){
		return creditAssignmentDao.getList(map);
	}
}
