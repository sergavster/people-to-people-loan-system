package com.p2psys.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.p2psys.common.constant.ConsStatusRecord;
import com.p2psys.context.Constant;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.StatusRecord;
import com.p2psys.exception.NoEnoughUseMoneyException;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.borrow.BorrowHelper;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.AutoBorrowService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.NoticePayBorrowService;
import com.p2psys.service.RewardStatisticsService;
import com.p2psys.service.StatusRecordService;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.treasure.service.TreasureRechargeService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;

public class QuartzJob {

	private Logger logger=Logger.getLogger(QuartzJob.class);
	
	private BorrowService borrowService;
	
	private AutoBorrowService autoBorrowService;
	
	private NoticePayBorrowService noticePayBorrowService;
	
	private UserService userService;
	
	private UserCreditService userCreditService;
	
	private AccountService accountService;
	// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
	private StatusRecordService statusRecordService;
	// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
	// v1.6.7.2 理财宝 zhangyz 2013-12-13 start
	@Resource
	private TreasureRechargeService treasureRechargeService;
	// v1.6.7.2 理财宝 zhangyz 2013-12-13 end
	
	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 start
	/**
	 * 奖励service
	 */
	private RewardStatisticsService rewardStatisticsService;
	// v1.6.7.1 RDPROJECT-395 zza 2013-11-04 end
	
	private int sleepTime=5;
	
	@SuppressWarnings("unchecked")
	public void doAutoQueue(){
		// 满标复审
		List<BorrowModel> slist = borrowService.getBorrowListByStatus(3);
		for (BorrowModel b : slist) {
			JobQueue.FULL_SUCCESS.offer(b);
		}
		// 复审失败
		List<BorrowModel> flist = borrowService.getBorrowListByStatus(4);
		for (BorrowModel b : flist) {
			JobQueue.FULL_FAIL.offer(b);
		}
		// 用户取消
		List<BorrowModel> clist = borrowService.getBorrowListByStatus(5);
		for (BorrowModel b : clist) {
			JobQueue.CANCEL.offer(b);
		}
		// 普通还款
		List<Repayment> rlist = borrowService.getAllRepaymentList(1);
		for (Repayment r : rlist) {
			JobQueue.REPAY.offer(r);
		}
		// 流转标还款
		List<DetailCollection> dclist = borrowService.getFlowRepayCollectList(0);
		for (DetailCollection c : dclist) {
			if (c.getStatus() == 0) {
				JobQueue.FLOW_REPAY.offer(c);
			}
		}
		// 自动投标
		List<BorrowModel> blist = borrowService.getBorrowListByStatus(19);
		for (BorrowModel b : blist) {
			JobQueue.AUTO_TENDER.offer(b);
		}
		
		// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 start
        //净值标流标自动撤回
		List<BorrowModel> jblist = borrowService.getJinBorrowListByStatusAndType(1, StringUtils.isNull(Constant.TYPE_PROPERTY));
		for(BorrowModel b : jblist){
			JobQueue.AUTO_RECALL.offer(b);
		}
		// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 end
	
	}
	
	public void doTask(){
		if(true){
			Thread fullSuccessTask=new Thread(){
				@Override
				public void run() {doAutoVerifyFullSuccess();}
			};
			fullSuccessTask.setName("doAutoVerifyFullSuccess-Thread");
			fullSuccessTask.start();
			
			Thread fullFailTask=new Thread(){
				@Override
				public void run() {doAutoVerifyFullFail();}
			};
			fullFailTask.setName("doAutoVerifyFullFail-Thread");
			fullFailTask.start();
			
			Thread cancelTask=new Thread(){
				@Override
				public void run() {doAutoCancel();}
			};
			cancelTask.setName("doAutoCancel-Thread");
			cancelTask.start();
			
			Thread repayTask=new Thread(){
				@Override
				public void run() {doAutoRepay();}
			};
			repayTask.setName("doAutoRepay-Thread");
			repayTask.start();
			
			Thread flowRepayTask=new Thread(){
				@Override
				public void run() {doAutoFlowRepay();}
			};
			flowRepayTask.start();
			flowRepayTask.setName("doAutoFlowRepay-Thread");
			
			
			Thread autoTenderTask=new Thread(){
				@Override
				public void run() {doAutoTender();}
			};
			autoTenderTask.start();
			autoTenderTask.setName("doAutoTender-Thread");
			
			// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 start
			Thread autoRecallTask=new Thread(){
				@Override
				public void run() {autoRecall();}
			};
			autoRecallTask.start();
			autoRecallTask.setName("autoRecall-Thread");
			// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 end
		}
	}
	
	public void doAutoVerifyFullSuccess(){
		while(true){
			BorrowModel b=JobQueue.FULL_SUCCESS.peek();
			if(b!=null){
				try {
					autoBorrowService.autoVerifyFullSuccess(b);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}finally{
					JobQueue.FULL_SUCCESS.remove(b);
				}
			}
			sleep(sleepTime);
		}
	}

	public void doAutoVerifyFullFail(){
		while(true){
			BorrowModel b=JobQueue.FULL_FAIL.peek();
			if(b!=null){
				try {
					autoBorrowService.autoVerifyFullFail(b);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}finally{
					JobQueue.FULL_FAIL.remove(b);
				}
			}
			sleep(sleepTime);
		}
	}
	
	public void doAutoCancel(){
		while(true){
			BorrowModel b=JobQueue.CANCEL.peek();
			if(b!=null){
				try {
					autoBorrowService.autoCancel(b);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}finally{
					//移除队列中的数据
					JobQueue.CANCEL.remove(b);
				}
			}
			sleep(sleepTime);
		}
	}
	public void doAutoRepay(){
		while(true){
			//获取队列中的数据，但是不移除此队列
			Repayment r=JobQueue.REPAY.peek();
			if(r!=null){
				try {
					autoBorrowService.autoRepay(r);
				} 
				// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
				catch (NoEnoughUseMoneyException e) {
					statusRecordService.add(new StatusRecord(ConsStatusRecord.SR_TYPE_REPAY, r.getId(), (byte) 0, (byte) 1, ConsStatusRecord.SR_RESULT_NOENOUCHMONEY, e.getMessage()));
				}
				// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
				catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				} finally{
					//移除队列中的数据
					JobQueue.REPAY.remove(r);
				}
			}
			sleep(sleepTime);
		}
	}
	
	public void doAutoFlowRepay(){
		while(true){
			DetailCollection dc=JobQueue.FLOW_REPAY.peek();
			if(dc!=null&&dc.getStatus()==0){
				try {
					autoBorrowService.autoFlowRepay(dc);
				} 
				// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
				catch (NoEnoughUseMoneyException e) {
					statusRecordService.add(new StatusRecord(ConsStatusRecord.SR_TYPE_FLOWREPAY, dc.getId(), (byte) 0, (byte) 1, ConsStatusRecord.SR_RESULT_NOENOUCHMONEY, e.getMessage()));
				} 
				// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
				catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}finally{
					JobQueue.FLOW_REPAY.remove(dc);
				}
			}
			sleep(sleepTime);//十分钟一次
		} 
	}
	/**
	 * 自动投标
	 */
	public void doAutoTender(){
		while(true){
			BorrowModel borrow = JobQueue.AUTO_TENDER.peek();
			if (borrow != null && borrow.getStatus() == 19) {
				try {
					autoBorrowService.autoDealTender(borrow);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}finally{
					JobQueue.AUTO_TENDER.remove(borrow);
				}
			}
			sleep(sleepTime);//十分钟一次
		}
	}
	
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 start
	/**
	 * 净值标流标自动撤回
	 */
	public void autoRecall(){
		while(true){
			BorrowModel borrow = JobQueue.AUTO_RECALL.peek();
			if(borrow != null){
				try {
					borrow.setStatus(5);
					AccountLog log = new AccountLog(borrow.getUser_id(),Constant.UNFREEZE,borrow.getUser_id(),DateUtils.getNowTimeStr(),borrow.getAddip());
					borrowService.deleteBorrow(borrow,log);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}finally{
					JobQueue.AUTO_RECALL.remove(borrow);
				}
			}
			sleep(sleepTime);
		}
	}
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 end
	  
	private void sleep(int sec){
		try {
			Thread.sleep(1000*sec);
		} catch (InterruptedException e) {
		}
	}
	
	public void doMsgTimer(){
	}
	
	public BorrowService getBorrowService() {
		return borrowService;
	}
	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	public AutoBorrowService getAutoBorrowService() {
		return autoBorrowService;
	}
	public void setAutoBorrowService(AutoBorrowService autoBorrowService) {
		this.autoBorrowService = autoBorrowService;
	}
	
	/**
	 * 获取userService
	 * 
	 * @return userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * 设置userService
	 * 
	 * @param userService 要设置的userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 更新vip到期状态
	 */
	public void doAutoUpdateExpireUser() {
		logger.debug("doAutoUpdateExpireUser job start....");
		userService.doAutoUpdateExpireUser();
	}
	
	/**
	 * 每天早上7点定时查询平台的借款总金额和待收总金额
	 */
	public void doAutoUpdateTotalAndCollection() {
		logger.debug("doAutoUpdateTotalAndCollection job start....");
		borrowService.updateTenderAndCollection();
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-18 start
	//TODO RDPROJECT-314 DELETE
	/**
	 * 一周以内的  需要还款的标 发站内信  和  邮件通知。
	 */
	/*public void doAutoNoticePayBorrow(){
			logger.debug("doAutoNoticeToPayBorrow job start....");
			noticePayBorrowService.autoNoticePayBorrow();
	}*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-18 start
	
	/**
	 * 每天定时计算逾期的天数和利息。
	 */
	public void doAutoCalcuLateInterest(){
		logger.debug("计算逾期的天数和利息go....");
		noticePayBorrowService.CalcLateInterest();
	}
	/**
	 * 流转标到期提醒
	 */
	public void autoNoticeFlowBorrow(){
		logger.debug("计算逾期的天数和利息go....");
		noticePayBorrowService.autoNoticeFlowBorrow();
	}
	/**
	 * VIP用户生日提醒
	 */
	public void  autoNoticeVIPBirthday(){
		noticePayBorrowService.autoNoticeVIPBirthday();
	}
	/**
	 * 借款人还款提醒
	 */
	public void  autoBorrowerRepayNotice(){
		noticePayBorrowService.autoBorrowerRepayNotice();
	}
	/**
	 * 投资人收款提醒
	 */
	public void  autoLoanerRepayNotice(){
		noticePayBorrowService.autoLoanerRepayNotice();
	}
	
	/**
	 * 根据会员综合积分和论坛积分设置会员积分等级
	 */
	public void doUpdateCreditLevel(){
		userCreditService.doUpdateCreditLevel();
	}
	
	/**
	 * 理财宝利息计算
	 */
	public void doTreasureInterest(){
		treasureRechargeService.doTreasureInterest();
	}
	
	
	/**
	 * 第三方支付定时补单
	 */
	//v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 start
	public void doUpdateOrder(){
		accountService.getAccountRechargeList();
	}
	//v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 end

	public NoticePayBorrowService getNoticePayBorrowService() {
		return noticePayBorrowService;
	}

	public void setNoticePayBorrowService(
			NoticePayBorrowService noticePayBorrowService) {
		this.noticePayBorrowService = noticePayBorrowService;
	}

	public UserCreditService getUserCreditService() {
		return userCreditService;
	}

	public void setUserCreditService(UserCreditService userCreditService) {
		this.userCreditService = userCreditService;
	}

	/**
	 * 获取rewardStatisticsService
	 * 
	 * @return rewardStatisticsService
	 */
	public RewardStatisticsService getRewardStatisticsService() {
		return rewardStatisticsService;
	}

	/**
	 * 设置rewardStatisticsService
	 * 
	 * @param rewardStatisticsService 要设置的rewardStatisticsService
	 */
	public void setRewardStatisticsService(RewardStatisticsService rewardStatisticsService) {
		this.rewardStatisticsService = rewardStatisticsService;
	}

	// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 start
	public StatusRecordService getStatusRecordService() {
		return statusRecordService;
	}

	public void setStatusRecordService(StatusRecordService statusRecordService) {
		this.statusRecordService = statusRecordService;
	}
	// v1.6.7.2 RDPROJECT-579 xx 2013-12-11 end
	
}
