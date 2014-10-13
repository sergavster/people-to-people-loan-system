package com.p2psys.web.action.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.service.AccountService;
import com.p2psys.service.AccountSumService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class InitAccountSumAction extends BaseAction {
	private static Logger logger = Logger.getLogger(InitAccountSumAction.class);	
	private AccountService accountService;
	private AccountSumService accountSumService;
	private AccountDao accountDao;

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	
	
	public AccountSumService getAccountSumService() {
		return accountSumService;
	}

	public void setAccountSumService(AccountSumService accountSumService) {
		this.accountSumService = accountSumService;
	}

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public String inittest(){
		logger.debug("init begin");
		return SUCCESS;
	}
	
	public String initdb(){
		String actionType = request.getParameter("actionType");
		
		if (StringUtils.isBlank(actionType)) {
			return "pre";
		}
		Connection conn = null;
		Statement stmt = null;
		
		PreparedStatement pstmt_recharge = null;
		PreparedStatement pstmt_interest  = null;
		PreparedStatement pstmt_award = null;
		PreparedStatement pstmt_borrow_cash = null;
		PreparedStatement pstmt_jin_amount = null;
		PreparedStatement pstmt_15_recharge = null;
		PreparedStatement pstmt_out_money = null;
		PreparedStatement pstmt_colletion_capital = null;
		PreparedStatement pstmt_used_huikuan = null;
		
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		ResultSet tmpRs = null;
		
		String url = null;
		String user = null;
		String password = null;
		
		SimpleDateFormat dfstart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		long timeStart= new Date().getTime();
		logger.debug("操作开始：" + dfstart.format(new Date()));// new Date()为获取当前系统时间
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载mysq驱动
		} catch (ClassNotFoundException e) {
			logger.debug("驱动加载错误");
			e.printStackTrace();// 打印出错详细信息
		}
		try {//jdbc:mysql://121.199.59.227:3306/dev1?useUnicode=true&amp;characterEncoding=utf8
			/*url = "jdbc:mysql://121.199.59.227:3306/dev1?useUnicode=true&amp;characterEncoding=utf8";// 简单写法：url	%25																																// =
			user = "dev1";
			password = "0/o~*.]%zA9$.";*/
			String dburl = request.getParameter("dburl")+"?useUnicode=true&amp;characterEncoding=utf8";
			String dbusername = request.getParameter("dbusername");
			String dbpassword = request.getParameter("dbpassword");

			url = dburl;// 简单写法：url	%25																																// =
			user = dbusername;
			password = dbpassword;
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			logger.debug("数据库链接错误");
			e.printStackTrace();
		}
		try {
			stmt = conn.createStatement();
			String checkinitsql = "select * from  dw_system where nid = 'con_account_sum_init'";
			ResultSet rs_checkinit = stmt.executeQuery(checkinitsql);
			
			
			if (rs_checkinit.next() &&  "1".equalsIgnoreCase(rs_checkinit.getString("value"))){
				logger.debug("已经初始化过，不能重复初始化。");
				request.setAttribute("rsmsg", "已经初始化过，不能重复初始化！");
				return SUCCESS;
			}
			
		
			
			String initsql = "delete from dw_account_sum";
			stmt.executeUpdate(initsql);
			String initsql2 = "insert into dw_account_sum(user_id) select user_id from dw_user";
			stmt.executeUpdate(initsql2);
			
			String sql_total_recharge = "select ifnull(sum(money-fee), 0) as total_recharge from dw_account_recharge where user_id = ? and status = 1 ";
			pstmt_recharge = conn.prepareStatement(sql_total_recharge);
			
//			String sql_total_interest = "select ifnull(sum(money), 0) as total_interest from dw_account_log  where user_id = ? and "
//					+ "(type = 'interest_collect' or type ='late_collection') ";
			String sql_total_interest = "select ifnull(sum(bc.interest-bc.interest*0.1), 0) as total_interest from dw_borrow_collection bc"
					+ " left join dw_borrow_tender bt on bc.tender_id = bt.id "
					+ " left join dw_borrow bw on bt.borrow_id = bw.id "
					+ "where bc.status = 1 and bt.user_id = ? and (bw.status in(3,6,7,8) or (bw.status in(1,8) and bw.is_flow=1))";
			pstmt_colletion_capital = conn.prepareStatement(sql_total_interest);
			
			pstmt_interest = conn.prepareStatement(sql_total_interest);
			
			String sql_total_award = "select ifnull(sum(money), 0) as total_award from dw_account_log  where user_id = ? and "
					+ "("
					+ "    type = 'award_add' "
					+ " or type='huikuan_award' "
					+ " or type='vouch_award' "
					+ " or type='offrecharge_award' "
					+ " or type='trouble_award' "
					+ " or type='invite_award' "
					+ " or type='lottery_award'"
					+ " or type='repayment_award' "
					+ " or type='first_tender_award')";
			pstmt_award = conn.prepareStatement(sql_total_award);
			
			String sql_borrow_cash = "select ifnull(sum(money), 0) as total_borrow_cash  from dw_account_log where user_id = ?  and "
					+ "type = 'borrow_success' ";
			pstmt_borrow_cash = conn.prepareStatement(sql_borrow_cash);
			
			String sql_jin = "select ifnull(sum(capital), 0) as jin_amount from dw_borrow_repayment "
					+ "where (status = 0 or status = 2) and  borrow_id in (select id from dw_borrow where user_id = ?)";
			pstmt_jin_amount = conn.prepareStatement(sql_jin);
			
			String sql_15_recharge = "select ifnull(sum(money-fee), 0) as total_15_recharge  from dw_account_recharge where status=1 and user_id = ? and addtime > ? "; 
			pstmt_15_recharge = conn.prepareStatement(sql_15_recharge);
			
			String sql_out_money = "select ifnull(sum(money), 0) as total_out_money  from dw_account_log where user_id = ?  "
					+ "and type in('award_deduct', 'borrow_fee', 'cash_success', 'late_rate', 'late_repayment', 'manage_fee', 'realname'"
					+ " 'repaid', 'scene_account', 'transaction_fee',  'video', 'vip_fee', 'trouble_donate' ) ";
			pstmt_out_money = conn.prepareStatement(sql_out_money);
			
			//已用充值=总充值-可用+待收本金 
			//待收本金
			String colletionCapitalSql = "select ifnull(sum(bc.capital), 0) as total_collection from dw_borrow_collection bc"
					+ " left join dw_borrow_tender bt on bc.tender_id = bt.id "
					+ " left join dw_borrow bw on bt.borrow_id = bw.id "
					+ "where bc.status = 0 and bt.user_id = ? and (bw.status in(3,6,7,8) or (bw.status in(1,8) and bw.is_flow=1))";
			pstmt_colletion_capital = conn.prepareStatement(colletionCapitalSql);
			
			//直接查询已用回款
			String usedHuikuanSql = "select sum(p1.huikuan_money) as used_huikuan from dw_huikuan p1  where p1.user_id=? and (p1.status=1 or p1.status=0 )";
			pstmt_used_huikuan = conn.prepareStatement(usedHuikuanSql);
			
			
			String updateSql = "update dw_account_sum set recharge = ?, interest = ?, award = ?, borrow_cash = ? "
					+ ", used_recharge = ?, used_interest = ?, used_award = ?, used_borrow_cash = ?,  huikuan = ?, used_huikuan = ? "
					+ "  where user_id = ? ";
			pstmt = conn.prepareStatement(updateSql);
			
			//检索全部用户
			String sql_alluser = "select * from dw_account   order by user_id asc";
			ResultSet rs_alluser = stmt.executeQuery(sql_alluser);
			
			while (rs_alluser.next()) {
				long curr_user_id = rs_alluser.getLong("user_id");

				//算出分项的总和
				
				
				double total_recharge;
				double total_interest;
				double total_award;
				double total_borrow_cash;
				double total_out_money;

				

				pstmt_recharge.setString(1, String.valueOf(curr_user_id));
				tmpRs = pstmt_recharge.executeQuery();
				tmpRs.next();
				total_recharge = tmpRs.getDouble("total_recharge");
				total_recharge = total_recharge >=0?total_recharge:0;
				
				

				pstmt_interest.setString(1, String.valueOf(curr_user_id));
				tmpRs = pstmt_interest.executeQuery();
				tmpRs.next();
				total_interest = tmpRs.getDouble("total_interest");
				total_interest = total_interest >=0?total_interest:0;
				

				pstmt_award.setString(1, String.valueOf(curr_user_id));
				tmpRs = pstmt_award.executeQuery();
				tmpRs.next();
				total_award = tmpRs.getDouble("total_award");
				total_award = total_award >=0?total_award:0;

				pstmt_borrow_cash.setString(1, String.valueOf(curr_user_id));
				tmpRs = pstmt_borrow_cash.executeQuery();
				tmpRs.next();
				total_borrow_cash = tmpRs.getDouble("total_borrow_cash");
				total_borrow_cash = total_borrow_cash >=0?total_borrow_cash:0;
				
//				pstmt_out_money.setString(1, String.valueOf(curr_user_id));
//				tmpRs = pstmt_out_money.executeQuery();
//				tmpRs.next();
//				total_out_money = tmpRs.getDouble("total_out_money");
				
				//当前可用余额
				double curr_use_money = rs_alluser.getLong("use_money");
				//当前未还的净值标本金
				double curr_jin_amount = 0;

				pstmt_jin_amount.setString(1, String.valueOf(curr_user_id));
				tmpRs = pstmt_jin_amount.executeQuery();
				tmpRs.next();
				curr_jin_amount = tmpRs.getDouble("jin_amount");
				//当前15天以内的充值
				double curr_15_recharge = 0;
				Date d = Calendar.getInstance().getTime();
				d = DateUtils.rollDay(d, -15);
				pstmt_15_recharge.setString(1, String.valueOf(curr_user_id));
				pstmt_15_recharge.setLong(2, (d.getTime() / 1000));
				tmpRs = pstmt_15_recharge.executeQuery();
				tmpRs.next();
				curr_15_recharge = tmpRs.getDouble("total_15_recharge");
				//计算免费提现额
				double freeCashAmount = (curr_use_money - (curr_jin_amount + curr_15_recharge))>0?(curr_use_money - (curr_jin_amount + curr_15_recharge)):0;

				//总待收本金
				double total_collection = 0;
				pstmt_colletion_capital.setLong(1, curr_user_id);
				logger.debug(pstmt_colletion_capital.toString());
				tmpRs = pstmt_colletion_capital.executeQuery();
				tmpRs.next();
				total_collection = tmpRs.getDouble("total_collection");
				
				
				//根据可用余额和前三项总额来计算总奖励
				//total_award = curr_use_money - (total_recharge + total_interest + total_borrow_cash - total_out_money);
				
				
				double used_recharge = total_recharge;
				double used_interest = total_interest;
				double used_award = total_award;
				double used_borrow_cash = total_borrow_cash;
				/*
				//如果免费提现额为0，则当所有收入的已用设置为对应数据项的总数
				if (freeCashAmount > 0){
					//以如下顺序算出已用
					//如果 免费提现额 》 奖励总额 那么 已用奖励为0
					if(freeCashAmount > total_award){
						used_award = 0;
						//如果免费提现额 - 奖励总额 - 利息总额 > 回款总额 那么 回款总额 为 0
						if (freeCashAmount > (total_award + total_interest)){
							used_interest = 0;
							//如果免费提现额 - 奖励总额 - 利息总额 > 回款总额 那么 回款总额 为 0
							if(freeCashAmount > (total_award + total_interest + total_borrow_cash)){
								used_borrow_cash = 0;
								//奖励，利息，借款入账，充值的总和一定大于免费提现额
								used_recharge = total_recharge - (freeCashAmount - (total_award + total_interest + total_borrow_cash));
							}else{
								used_borrow_cash = total_borrow_cash - (freeCashAmount - (total_award + total_interest));
							}
						}else{
							used_interest = total_interest - (freeCashAmount - total_award);
						}
					}else{
						used_award = total_award - freeCashAmount;
					}
				}*/
				//如果免费提现额为0，则当所有收入的已用设置为对应数据项的总数
				/*if (freeCashAmount > 0){
					//以如下顺序算出已用
					
						//如果免费提现额 - 奖励总额 - 利息总额 > 回款总额 那么 回款总额 为 0
						if (freeCashAmount >total_interest){
							used_interest = 0;
							//如果免费提现额 - 奖励总额 - 利息总额 > 回款总额 那么 回款总额 为 0
							if(freeCashAmount > ( total_interest + total_borrow_cash)){
								used_borrow_cash = 0;
								if(freeCashAmount > (total_interest + total_borrow_cash + total_recharge)){
									used_recharge = 0;
									//奖励，利息，借款入账，充值的总和一定大于免费提现额
									if(freeCashAmount > (total_interest + total_borrow_cash + total_recharge + total_award)){
										total_award += freeCashAmount -(total_interest + total_borrow_cash + total_recharge);
										
									}else{
										used_award = total_award -(freeCashAmount -(total_interest + total_borrow_cash + total_recharge));
									}
									
								}else{
									
									used_recharge = total_recharge - (freeCashAmount - ( total_interest + total_borrow_cash));
								}
							}else{
								used_borrow_cash = total_borrow_cash - (freeCashAmount - ( total_interest));
							}
						}else{
							used_interest = total_interest - (freeCashAmount );
						}
					}*/
				String initType = request.getParameter("initType");
				
				if(initType.equalsIgnoreCase("1")){
					//总的充值-可用余额的值看作是已用充值，忽略处理已用奖励，可用奖励（这两项直接等于总额）
					used_recharge = total_recharge - curr_use_money - total_collection;
					//如果已用充值为负，虽然重新置为0，并将差额补到奖励总额上去
					if(used_recharge < 0){
						total_award += Math.abs(used_recharge);
						used_recharge = 0;
					}
				}
				
				//计算回款总额和已用回款
				long start=NumberUtils.getLong(Global.getString("huikuan_start_time"));
				int isMonth=Global.getInt("huikuan_month"); //是否启用月标限制
				int isday=(isMonth==1?0:1);
				
				double huikuan = accountDao.getCollectionSumNoJinAndSecond(curr_user_id, 1, isday, start);
				
				//已用回款
				double used_huikuan = 0;
				pstmt_used_huikuan.setLong(1, curr_user_id);
				logger.debug(pstmt_used_huikuan.toString());
				tmpRs = pstmt_used_huikuan.executeQuery();
				tmpRs.next();
				used_huikuan = tmpRs.getDouble("used_huikuan");
				
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setString(1, String.valueOf(total_recharge));
				pstmt.setString(2, String.valueOf(total_interest));
				pstmt.setString(3, String.valueOf(total_award));
				pstmt.setString(4, String.valueOf(total_borrow_cash));
				pstmt.setString(5, String.valueOf(used_recharge));
				pstmt.setString(6, String.valueOf(used_interest));
				pstmt.setString(7, String.valueOf(used_award));
				pstmt.setString(8, String.valueOf(used_borrow_cash));
				pstmt.setString(9, String.valueOf(huikuan));
				pstmt.setString(10, String.valueOf(used_huikuan));
				pstmt.setString(11, String.valueOf(curr_user_id));
				logger.debug(pstmt.toString());
				pstmt.execute();
				

			}
			//写入一条记录标志已经初始化过			
			String tagEndsql = "INSERT INTO `dw_system` (`id` ,`name` ,`nid` ,`value` ,`type` ,`style` ,`status`)VALUES (NULL , 'V1.6.6.1 Account Sum init over at "+DateUtils.dateStr4(new Date())+"', 'con_account_sum_init', '1', '0', '5', '1')";
			stmt.executeUpdate(tagEndsql);
			request.setAttribute("rsmsg", "Account_sum表初始化成功结束！");
		} catch (SQLException e) {
			logger.debug("数据操作错误");
			e.printStackTrace();
		}
		// 关闭数据库
		try {
//			if (rs != null) {
//				rs.close();
//				rs = null;
//			}
//			if (stmt != null) {
//				stmt.close();
//				stmt = null;
//			}
//			if (conn != null) {
//				conn.close();
//				conn = null;
//			}
//			rs.close();
//			tmpRs.close();
			
			stmt.close();
			pstmt_recharge.close();
			pstmt_interest.close();
			pstmt_award.close();
			pstmt_borrow_cash.close();
			pstmt_jin_amount.close();
			pstmt_15_recharge.close();
			pstmt.close();
			
			conn.close();
			
		} catch (Exception e) {
			logger.debug("数据库关闭错误");
			e.printStackTrace();
		}
		SimpleDateFormat dfend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		long timeEnd= new Date().getTime();
		logger.debug("操作结束：" + dfend.format(new Date()));// new Date()为获取当前系统时间
		logger.debug("操作耗时：" + (timeEnd - timeStart) / 1000);
		

		
		return SUCCESS;
		
	}
	


}
