package com.p2psys.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.p2psys.common.enums.EnumPayInterface;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountBank;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Huikuan;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.domain.Rule;
import com.p2psys.domain.User;
import com.p2psys.domain.UserAmount;
import com.p2psys.exception.NoEnoughUseMoneyException;
import com.p2psys.model.HongBaoModel;
import com.p2psys.model.HuikuanModel;
import com.p2psys.model.Interest;
import com.p2psys.model.KefuAndUserInvest;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.account.AccountModel;
import com.p2psys.model.account.AccountOneDayModel;
import com.p2psys.model.account.AccountReconciliationModel;
import com.p2psys.model.account.AccountSumModel;
import com.p2psys.model.account.BorrowSummary;
import com.p2psys.model.account.CollectSummary;
import com.p2psys.model.account.InvestSummary;
import com.p2psys.model.account.RepaySummary;
import com.p2psys.model.account.TiChengModel;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

@Service("accountDao")
public class AccountDaoImpl extends BaseDaoImpl implements AccountDao {

    private static Logger logger = Logger.getLogger(BorrowDaoImpl.class);  
    
//    @SuppressWarnings("rawtypes")
//    public List getAccountLogSummary(long user_id) {
//        String sql="select type,sum(money) as money from dw_account_log where user_id=? group by type ";
//        
//        logger.info("SQL:"+sql);
//        logger.info("SQL:"+user_id);
//        List list=new ArrayList();
//        list=this.getJdbcTemplate().query(sql,new Object[]{user_id}, new RowMapper<AccountLog>(){
//
//            public AccountLog mapRow(ResultSet rs, int rowNum)
//                    throws SQLException {
//                AccountLog as=new AccountLog();
//                as.setType(rs.getString("type"));
//                as.setMoney(rs.getDouble("money"));
//                return as;
//            }
//        });
//        return list;
//    }

    @Override
    public double getSum(String sql,long user_id){
        double total=0;
        try {
            total=sum(sql, user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    @Override
    public int getCount(String sql,long user_id){
        int total=0;
        try {
            total=count(sql, user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    
//    @Override
//    public UserAccountSummary getUserAccountSummary(long user_id) {
//        String sql="select invest.*,borrow.*,late.*,account.total as accountTotal," +
//                "account.use_money as accountUseMoney," +
//                "account.no_use_money as accountNoUseMoney," +
//                "account.collection as collectTotal  from  " +
//                "dw_account account left join view_borrow_sum  borrow on borrow.user_id=account.user_id " +
//                "left join view_invest_sum invest on invest.user_id=account.user_id " +
//                "left join view_late_sum late on late.user_id=account.user_id " +
//                "where account.user_id=?"; 
//        logger.debug("getUserAccountSummary()"+sql);
//        UserAccountSummary summary=null;
//        try {
//
//        summary=getJdbcTemplate().queryForObject(sql, new Object[]{user_id},new RowMapper<UserAccountSummary>(){
//            @Override
//            public UserAccountSummary mapRow(ResultSet rs, int num) throws SQLException {
//                UserAccountSummary uas=new UserAccountSummary();
//                uas.setUser_id(rs.getLong("user_id"));
//                uas.setAccountTotal(getResultDouble(rs,"accountTotal"));
//                uas.setAccountUseMoney(getResultDouble(rs,"accountUseMoney"));
//                uas.setAccountNoUseMoney(getResultDouble(rs,"accountNoUseMoney"));
//                
//                uas.setBorrowTotal(getResultDouble(rs,"borrowTotal"));
//                uas.setBorrowInterest(getResultDouble(rs,"borrowInterest"));
//                uas.setBorrowTimes(getResultInt(rs,"borrowTimes"));
//                
//                uas.setInvestTotal(getResultDouble(rs,"investTotal"));
//                uas.setInvestInterest(getResultDouble(rs,"investInterest"));
//                uas.setInvestTimes(getResultInt(rs,"investTimes"));
//                
//                uas.setLateTotal(getResultDouble(rs,"lateTotal"));
//                uas.setLateInterest(getResultDouble(rs,"lateInterest"));
//                uas.setOverdueInterest(getResultDouble(rs,"overdueInterest"));
//                uas.setAccountOwnMoney(uas.getAccountTotal()+uas.getInvestTotal()+uas.getInvestInterest()
//                        -uas.getBorrowTotal()-uas.getBorrowInterest());
//                
//                uas.setRepayTotal(getResultDouble(rs,"repayTotal"));
//                uas.setRepayInterest(getResultDouble(rs,"repayInterest"));
//                uas.setCollectTotal(getResultDouble(rs,"collectTotal"));
//                
//                uas.setNewestCollectDate(rs.getString("newestCollectDate"));
//                uas.setNewestCollectMoney(rs.getDouble("newestCollectMoney"));
//                uas.setNewestRepayDate(rs.getString("newestRepayDate"));
//                uas.setNewestRepayMoney(rs.getDouble("newestRepayMoney"));
//                //uas.setMax_cash(rs.getDouble("max_cash"));
//                /*uas.setCashTotal(getResultDouble(rs,"cashTotal"));
//                uas.setRechargeTotal(getResultDouble(rs,"rechargeTotal"));
//                
//                uas.setWaitRepayCount(getResultInt(rs,"waitRepayCount"));
//                uas.setHadRepayCount(getResultInt(rs,"hadRepayCount"));
//                uas.setHadDueRepayCount(getResultInt(rs,"hadDueRepayCount"));
//                uas.setWaitDueRepayCount(getResultInt(rs,"waitDueRepayCount"));*/
//                
//                
//                
//                return uas;
//            }
//        });
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//        return summary;
//    }
    
//    private double getResultDouble(ResultSet rs,String name) throws SQLException{
//        double result=0.0;
//        try {
//            result=rs.getDouble(name);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
//    }
    
//    private int getResultInt(ResultSet rs,String name) throws SQLException{
//        int result=0;
//        try {
//            result=rs.getInt(name);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
//    }



//    /**
//     * 获取个人借款的合计
//     */
//    public double getBorrowSum(long user_id) {
//        String sql="select sum(account) as num from dw_borrow where status in (3,6,7) and user_id =? ";
//        
//        logger.info("SQL:"+sql);
//        logger.info("SQL:"+user_id);
//        double sum=0.0;
//        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
//        if(rs.next()){
//            sum=rs.getDouble("num");
//        }
//        return sum;
//    }
    
//    public int getBorrowTimes(long user_id){
//        String sql="select count(account) as times from dw_borrow where status=3 and user_id =? ";
//        logger.info("SQL:"+sql);
//        logger.info("SQL:"+user_id);
//        int times=0;
//        times=this.getJdbcTemplate().queryForInt(sql, new Object[]{user_id});
//        return times;
//    }

//    /**
//     * 借款额度
//     */
//    public double getBorrowAmount(long user_id) {
//        String sql="select borrow_amount from dw_user_cache where user_id = ? ";
//        logger.info("SQL:"+sql);
//        logger.info("SQL:"+user_id);
//        double amount=0;
//        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql, new Object[]{user_id});
//        if(rs.next()){
//            amount=rs.getDouble("borrow_amount");
//        }
//        return amount;
//    }

    public UserAmount getUserAmount(long user_id) {
        String sql="select * from dw_user_amount where user_id=? ";
        logger.info("SQL:"+sql);
        logger.info("SQL:"+user_id);
        
        UserAmount amount=null;
        try {
            amount=this.getJdbcTemplate().queryForObject(sql,new Object[]{user_id}, new RowMapper<UserAmount>(){
                public UserAmount mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    UserAmount ua=new UserAmount();
                    ua.setId(rs.getLong("id"));
                    ua.setUser_id(rs.getLong("user_id"));
                    ua.setCredit(rs.getDouble("credit"));
                    ua.setCredit_use(rs.getDouble("credit_use"));
                    ua.setCredit_nouse(rs.getDouble("credit_nouse"));
                    ua.setBorrow_vouch( rs.getDouble("borrow_vouch"));
                    ua.setBorrow_vouch_use(rs.getDouble("borrow_vouch_use"));
                    ua.setBorrow_vouch_nouse(rs.getDouble("borrow_vouch_nouse"));
                    ua.setTender_vouch(rs.getDouble("tender_vouch"));
                    ua.setTender_vouch_use(rs.getDouble("tender_vouch_use"));
                    ua.setTender_vouch_nouse(rs.getDouble("tender_vouch_nouse"));
                    return ua;
                }
            });
        } catch (DataAccessException e) {
            logger.info(e.getMessage());
        }
        return amount;
    }

    public AccountModel getAccount(long user_id) {
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
        String sql="select p2.username,p2.realname,p1.*,p3.account as bankaccount,p3.bank as bank,p3.branch as branch,p3.addtime as addtime,p3.modify_username as modify_username,p3.bank_realname as bank_realname,p3.payment as payment,p3.order as `order`,p3.province as province,p3.city as city,p3.area as area,p5.name as province_name,p6.name as city_name,p7.name as area_name, p4.name as bankname from dw_account as p1 " +
                "left join dw_user as p2 on p1.user_id=p2.user_id " +
                "left join dw_account_bank as p3 on p1.user_id=p3.user_id " +
                "left join dw_linkage as p4 on p4.id=p3.bank and p4.type_id=25 " +
                 "left join dw_area as p5 on p5.id=p3.province "+ 
                "left join dw_area as p6 on p6.id=p3.city "+
                "left join dw_area as p7 on p7.id=p3.area "+
                "where p1.user_id=? limit 0,1";
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
        AccountModel account=null;
        logger.debug(sql);
        try {
            account=this.getJdbcTemplate().queryForObject(sql,new Object[]{user_id}, getBeanMapper(AccountModel.class));
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
        }
        return account;
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public Account getAccountByUserId(long user_id) {
    	String sql = "select * from dw_account where user_id = :user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", user_id);
		try {
			return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(Account.class));
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    	/*Object obj = super.findObjByProperty(Account.class, "user_id", user_id);
		if(obj!=null){
			return (Account)obj;
		}
    	return null;*/
	}
// 使用的地方只使用Account的内容，该方法删除
//	public AccountModel getAccount_hongbao(long user_id) {
//        String sql="select p2.username,p2.realname,p1.*,p3.*,p3.account as bankaccount,p4.name as bankname from dw_account as p1 " +
//                "left join dw_user as p2 on p1.user_id=p2.user_id " +
//                "left join dw_account_bank as p3 on p1.user_id=p3.user_id " +
//                "left join dw_linkage as p4 on p4.id=p3.bank and p4.type_id=25 " +
//                "where p1.user_id=? limit 0,1";
//        AccountModel account=null;
//        logger.debug(sql);
//        try {
//            account=this.getJdbcTemplate().queryForObject(sql,new Object[]{user_id},getBeanMapper(AccountModel.class));
//        } catch (DataAccessException e) {
//            logger.error(e.getMessage());
//        }
//        return account;
//    }
//    public List getInterest(long user_id) {
//        String sql="select p1.status ," +
//                "sum(p1.repay_account) as total_repay_account ," +
//                "sum(p1.interest) as total_interest_account," +
//                "sum(p1.capital) as total_capital_account  " +
//                "from dw_borrow_collection as p1 " +
//                "left join dw_borrow_tender as p2  on p1.tender_id = p2.id  " +
//                "where p2.status=1  and p2.user_id = ? " +
//                "and p2.borrow_id in (select id from dw_borrow where status=3)  " +
//                "group by p1.status";
//        List list=new ArrayList();
//        list=this.getJdbcTemplate().query(sql, new Object[]{user_id},getBeanMapper( Interest.class));
//        return list;
//    }
    public AccountModel getAccountByBankAccount(long user_id,String bankAccount) {
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
        String sql="select p2.username,p2.realname,p1.*,p3.*,p3.account as bankaccount,p3.bank as bank,p3.branch as branch,p3.addtime as addtime,p3.modify_username as modify_username,p3.bank_realname as bank_realname,p3.payment as payment,p3.order as `order`,p3.province as province,p3.city as city,p3.area as area,p5.name as province_name,p6.name as city_name,p7.name as area_name,p4.name as bankname from dw_account as p1 " +
                "left join dw_user as p2 on p1.user_id=p2.user_id " +
                "left join dw_account_bank as p3 on p1.user_id=p3.user_id " +
                "left join dw_linkage as p4 on p4.id=p3.bank and p4.type_id=25 " +
                 "left join dw_area as p5 on p5.id=p3.province "+ 
                    "left join dw_area as p6 on p6.id=p3.city "+
                    "left join dw_area as p7 on p7.id=p3.area "+
                "where p3.account=? and p3.user_id=? limit 0,1";
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
        AccountModel account=null;
        logger.debug(sql);
        try {
            account=this.getJdbcTemplate().queryForObject(sql,new Object[]{bankAccount,user_id}, getBeanMapper(AccountModel.class));
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
        }
        return account;
    }
    //v1.6.7.2 RDPROJECT-470 wcw 2013-12-04 start
    public AccountBank getAccountByBankAccount(long id) {
        AccountBank bank=(AccountBank) findById(AccountBank.class, id);
        return bank;
    }
    //v1.6.7.2 RDPROJECT-470 wcw 2013-12-04 end
//    public Newpay getNewpay(long user_id){
//        String sql="select repayment_time,repayment_account from dw_borrow_repayment " +
//                "where status !=1 and borrow_id in (select id from dw_borrow where user_id = ? " +
//                "and status=3) order by repayment_time";
//        Newpay newpay=null;
//        try {
//            newpay=this.getJdbcTemplate().queryForObject(sql,new Object[]{user_id}, new RowMapper<Newpay>(){
//                public Newpay mapRow(ResultSet rs, int rowNum)
//                        throws SQLException {
//                    Newpay n=new Newpay();
//                    n.setNew_repay_account(rs.getDouble("repayment_account"));
//                    n.setNew_repay_time(rs.getString("repayment_time"));
//                    return n;
//                }
//            });
//        } catch (DataAccessException e) {
//            logger.error(e.getMessage());
//        }
//        return newpay;
//    }
    
//    public NewCollection getNewCollection(long user_id){
//        String sql="select repay_time,repay_account  from dw_borrow_collection  " +
//                "where tender_id in (select p2.id from dw_borrow_tender  " +
//                "as p2 left join dw_borrow as p3 on p2.borrow_id=p3.id " +
//                "where p3.status=3 and p2.user_id = ? and p2.status=1) and repay_time > "+(new Date()).getTime()/1000+" and status=0 order by repay_time asc";
//        NewCollection newcollect=null;
//        try {
//            newcollect=this.getJdbcTemplate().queryForObject(sql,new Object[]{user_id}, new RowMapper<NewCollection>(){
//                public NewCollection mapRow(ResultSet rs, int rowNum)
//                        throws SQLException {
//                    NewCollection n=new NewCollection();
//                    n.setNew_collection_time(rs.getString("repay_time"));
//                    n.setNew_collection_account(rs.getDouble("repay_account"));
//                    return n;
//                }
//            });
//        } catch (DataAccessException e) {
//            logger.error(e.getMessage());
//        }
//        return newcollect;
//    }
    
//    public List<WaitPayment> getWaitpayment(long user_id){
//        String sql = "select status," +
//                "count(1) as repay_num," +
//                "sum(repayment_account) as borrow_num ," +
//                "sum(capital) as capital_num ," +
//                "sum(repayment_yesaccount) as borrow_yesnum " +
//                "from dw_borrow_repayment " +
//                "where borrow_id in (select id from dw_borrow where user_id = ? and status=3) " +
//                "group by status ";
//        List list=new ArrayList();
//        try {
//            list=this.getJdbcTemplate().query(sql,new Object[]{user_id}, new RowMapper<WaitPayment>(){
//                public WaitPayment mapRow(ResultSet rs, int rowNum)
//                        throws SQLException {
//                    WaitPayment w=new WaitPayment();
//                    w.setBorrow_num(rs.getDouble("borrow_num"));
//                    w.setBorrow_yesnum(rs.getDouble("borrow_yesnum"));
//                    w.setCapital_num(rs.getDouble("capital_num"));
//                    w.setRepay_num(rs.getDouble("repay_num"));
//                    w.setStatus(rs.getInt("status"));
//                    return w;
//                }
//            });
//        } catch (DataAccessException e) {
//            logger.error(e.getMessage());
//        }
//        return list;
//    }
    
    public User getKf(long user_id){
        String sql="select * from dw_user as u left join dw_user_cache as uca on uca.kefu_userid=u.user_id where uca.user_id=?";
        User u=null;
        try {
            u=this.getJdbcTemplate().queryForObject(sql, new Object[]{user_id},getBeanMapper(User.class));
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
        }
        return u;
    }
    
//    /**
//     * 统计当前用户的资金记录的数量
//     * @param user_id
//     * @return
//     */
//    public int getAccountLogCount(long user_id) {
//        String sql="select count(p1.id) " +
//                "from dw_account_log as p1 " +
//                "left join dw_user as p2 on p1.user_id=p2.user_id " +
//                "left join dw_user as p3 on p3.user_id=p1.to_user " +
//                "left join dw_linkage as p4 on p4.value=p1.type and p4.type_id=30 "+
//                "where 1=1 and p2.user_id=? ";
//        logger.debug("SQL:"+sql);
//        logger.debug("SQL:"+user_id);
//        int count=0;
//        count=this.getJdbcTemplate().queryForInt(sql, new Object[]{user_id});
//        return count;
//    }
//    @Override
//    public double getFlowDayTenderCollection(long user_id) {
//        //String sql="SELECT SUM(p2.wait_account) AS num FROM dw_borrow_tender  AS p2  LEFT JOIN dw_borrow AS p3 ON p2.borrow_id=p3.id WHERE p2.user_id=? AND p3.isday=1  and p3.time_limit_day=7 AND (p3.is_flow=1)";
//        String sql="SELECT SUM(p2.wait_account) AS num FROM dw_borrow_tender  AS p2  LEFT JOIN dw_borrow AS p3 ON p2.borrow_id=p3.id WHERE p2.user_id=? AND p3.isday=1 and p3.time_limit_day=7 AND (p3.type="+Constant.TYPE_FLOW+")";
//        
//        logger.info("SQL:"+sql);
//        logger.info("SQL:"+user_id);
//        double sum=0.0;
//        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
//        if(rs.next()){
//            sum=rs.getDouble("num");
//        }
//        return sum;
//    }
    /**
     * 信达财富投流转天标限制月标待收
     * 
     */
        @Override
        public double getFlowMonthTenderCollection(long user_id) {
            //String sql="SELECT SUM(p2.wait_account) AS num FROM dw_borrow_tender  AS p2  LEFT JOIN dw_borrow AS p3 ON p2.borrow_id=p3.id WHERE p2.user_id=? AND p3.isday!=1  AND (p3.is_flow=1 OR p3.is_fast=1)";
            String sql="SELECT SUM(p2.wait_account) AS num FROM dw_borrow_tender  AS p2  LEFT JOIN dw_borrow AS p3 ON p2.borrow_id=p3.id WHERE p2.user_id=? AND p3.isday!=1  AND (p3.type="+Constant.TYPE_FLOW+" OR p3.type="+Constant.TYPE_MORTGAGE+")";
            
            logger.info("SQL:"+sql);
            logger.info("SQL:"+user_id);
            double sum=0.0;
            SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
            if(rs.next()){
                sum=rs.getDouble("num");
            }
            return sum;
        }
        
    @Override
    public Account addAccount(Account account) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql="insert into dw_account(user_id,total,use_money,no_use_money,collection) values(?,?,?,?,?)";
        final Account a=account;
        this.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(Connection conn)
                    throws SQLException {
                PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, a.getUser_id());
                ps.setDouble(2, a.getTotal());
                ps.setDouble(3, a.getUse_money());
                ps.setDouble(4, a.getNo_use_money());
                ps.setDouble(5, a.getCollection());
                return ps;
            }
        }, keyHolder);
        long key=keyHolder.getKey().longValue();
        account.setId(key);
        return account;
    }

    @Override
    public AccountBank addBank(AccountBank bank) {
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
        bank=(AccountBank) insert(bank);
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
        return bank;
    }

    @Override
    public AccountBank updateBank(AccountBank bank) {
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
        String sql="update dw_account_bank set user_id=?,account=?,bank=?,branch=?,addtime=?,addip=?,modify_username=?,bank_realname=?,payment=?,`order`=?,province=?,city=?,area=? where user_id=?";
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
        int ret=this.getJdbcTemplate().update(sql, bank.getUser_id(),bank.getAccount(),bank.getBank(),
                //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
                bank.getBranch(),bank.getAddtime(),bank.getAddip(),bank.getModify_username(),bank.getBank_realname(),bank.getPayment(),bank.getOrder(),bank.getProvince(),bank.getCity(),bank.getArea(),bank.getUser_id());
                //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
        if(ret==1) return bank;
        return bank;
    }
    @Override
    public AccountBank updateBank(AccountBank bank,int init) {
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
        String sql="update dw_account_bank set user_id=?,account=?,bank=?,branch=?,addtime=?,addip=?,modify_username=?,bank_realname=?,payment=?,`order`=?,province=?,city=?,area=? where 1=? and id=?";
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
        int ret=this.getJdbcTemplate().update(sql, bank.getUser_id(),bank.getAccount(),bank.getBank(),
                //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
                bank.getBranch(),bank.getAddtime(),bank.getAddip(),bank.getModify_username(),bank.getBank_realname(),bank.getPayment(),bank.getOrder(),bank.getProvince(),bank.getCity(),bank.getArea(),init,bank.getId());
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
        if(ret==1) return bank;
        return bank;
    }
    
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 start
    @Override
    public void updateAccount(Account act) {
        String sql="update dw_account set total=?,use_money=?,no_use_money=?,collection=? where user_id=? and ?>=0";
        logger.debug("SQL"+sql);
        logger.debug("SQL:"+act.getUser_id());
        int count = this.getJdbcTemplate().update(sql,act.getTotal(),act.getUse_money(),
                act.getNo_use_money(),act.getCollection(),act.getUser_id(),act.getUse_money());
        if(count!=1){
            throw new NoEnoughUseMoneyException();
        }
    }
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 end
    
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 start
    @Override
    public void updateAccount(double totalVar,double useVar,double nouseVar,long user_id){
        String sql="update dw_account set total=total+?,use_money=use_money+?,no_use_money=no_use_money+? where user_id=? and use_money+?>=0";
        logger.debug("SQL"+sql);
        int count = this.getJdbcTemplate().update(sql,totalVar,useVar,nouseVar,user_id,useVar);
        if(count!=1){
            throw new NoEnoughUseMoneyException();
        }
    }
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 end
    
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 start
    @Override
    public void updateAccount(double totalVar,double useVar,double nouseVar,long user_id,double hongbao,int aa){
        String sql="update dw_account set total=total+?,use_money=use_money+?,no_use_money=no_use_money+?,hongbao=hongbao+? where user_id=? and 1=? and use_money+?>=0";
        logger.debug("SQL"+sql);
        int count = this.getJdbcTemplate().update(sql,totalVar,useVar,nouseVar,hongbao,user_id,aa,useVar);
        if(count!=1){
            throw new NoEnoughUseMoneyException();
        }
    }
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 end
    
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 start
    @Override
    public int updateAccountNotZero(double totalVar, double useVar,double nouseVar, long user_id) {
        String sql="update dw_account set total=total+?,use_money=use_money+?,no_use_money=no_use_money+? where user_id=? and use_money+?>=0";
        logger.debug("SQL"+sql);
        int count = getJdbcTemplate().update(sql,totalVar,useVar,nouseVar,user_id,useVar);
        if(count!=1){
            throw new NoEnoughUseMoneyException();
        }
        return count;
    }
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 end
    
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 start
    @Override
    public void updateAccount(double totalVar,double useVar,double nouseVar,double collectVar,long user_id){
        String sql="update dw_account set total=total+?,use_money=use_money+?,no_use_money=no_use_money+?,collection=collection+? where user_id=? and use_money+?>=0";
        logger.debug("SQL"+sql);
        int count = this.getJdbcTemplate().update(sql,totalVar,useVar,nouseVar,collectVar,user_id,useVar);
        if(count!=1){
            throw new NoEnoughUseMoneyException();
        }
    }
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 end
    
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 start
    //山水聚宝累计投标奖励
    @Override
    public void updateAccount(double totalVar,double useVar,double nouseVar,double tender_award,long user_id,int a){
        String sql="update dw_account set total=total+?,use_money=use_money+?,no_use_money=no_use_money+?,total_tender_award=total_tender_award+? where user_id=? and 1=? and use_money+?>=0";
        logger.debug("SQL"+sql);
        int count = this.getJdbcTemplate().update(sql,totalVar,useVar,nouseVar,tender_award,user_id,a,useVar);
        if(count!=1){
            throw new NoEnoughUseMoneyException();
        }
    }
    // v1.6.7.2 防止用户可用余额出现负数 xx 2013-12-10 end
    
    @Override
    public AccountBank updateBankByAccount(AccountBank bank,String bankaccount) {
        String sql="update dw_account_bank set account=?,bank=?,branch=?,addtime=?,addip=?,modify_username=?,payment=?,province=?,city=?,area=? where id=? ";
        int ret=getJdbcTemplate().update(sql,bank.getAccount(),bank.getBank(),
                bank.getBranch(),bank.getAddtime(),bank.getAddip(),bank.getModify_username(),bank.getPayment(),bank.getProvince(),bank.getCity(),bank.getArea(),bank.getId());
        if(ret==1) return bank;
        return bank;
    }
    
    String getUserAcountSql=" from dw_user p2 " +
            "left join dw_account p1 on p1.user_id = p2.user_id " +
            "LEFT JOIN (" +
                "select t1.user_id,sum(t2.repayment_account) as wait_repay " +
                "from dw_borrow t1 " +
                "left join dw_borrow_repayment t2 on t2.borrow_id=t1.id where t2.status=0 GROUP BY t1.user_id" +
            ") as repay on repay.user_id =p2.user_id " +
            // v1.6.7.1 RDPROJECT-467 2013-11-19 start
            "left join ( " +
                "select t1.user_id,SUM(t2.repayment_account) AS jin_wait_repay " +
                "from dw_borrow t1 " +
                "left join dw_borrow_repayment t2 on t2.borrow_id=t1.id where t2.status=0 AND t1.type ="+Constant.TYPE_PROPERTY+" group by t1.user_id   " +
            ") as jin_repay ON jin_repay.user_id =p2.user_id " +    
            // v1.6.7.1 RDPROJECT-467 2013-11-19 end
            "where 1=1 " ;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List getUserAcount(int start, int end, SearchParam param) {
        String selectSql = "select p1.id,p2.user_id,p2.username,p2.realname," +
                "p1.total,p1.use_money,p1.no_use_money," +
                "p1.collection as wait_collect,repay.wait_repay" +
                // v1.6.7.1 RDPROJECT-467 2013-11-19 start
                ",jin_repay.jin_wait_repay ";
                // v1.6.7.1 RDPROJECT-467 2013-11-19 end 
        String searchSql = param.getSearchParamSql();
        // v1.6.7.2 RDPROJECT-554 zza start
//      searchSql = searchSql.replace("p1", "p2");
        // v1.6.7.2 RDPROJECT-554 zza end
        String groupSql = "group by p1.user_id ";
        String orderSql = "order by p1.user_id desc ";
        String limitSql = "limit :start, :end";
        StringBuffer sb = new StringBuffer(selectSql);
        String querySql = sb.append(getUserAcountSql).append(searchSql).append(groupSql).append(orderSql)
                .append(limitSql).toString();
        logger.debug("getUserAcount():" + querySql);
        // v1.6.7.2 RDPROJECT-554 zza start
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("end", end);
        return getNamedParameterJdbcTemplate().query(querySql, map, getBeanMapper(AccountSumModel.class));
        /* List list = getJdbcTemplate().query(querySql, new Object[] { start, end }, getBeanMapper(AccountSumModel.class));
            @Override
            public AccountSumModel mapRow(ResultSet rs, int num) throws SQLException {
                AccountSumModel s = new AccountSumModel();
                s.setId(rs.getLong("id"));
                s.setUser_id(rs.getLong("user_id"));
                s.setUsername(rs.getString("username"));
                s.setRealname(rs.getString("realname"));
                s.setTotal(rs.getDouble("total"));
                s.setUse_money(rs.getDouble("use_money"));
                s.setNo_use_money(rs.getDouble("no_use_money"));
                s.setWait_collect(rs.getDouble("wait_collect"));
                s.setWait_repay(rs.getDouble("wait_repay"));
                // v1.6.7.1 RDPROJECT-467 2013-11-19 start
                s.setJin_wait_repay(rs.getDouble("jin_wait_repay"));
                // v1.6.7.1 RDPROJECT-467 2013-11-19 end
                return s;
            }
        });
        return list; */
        // v1.6.7.2 RDPROJECT-554 zza end
    }
    

    @Override
    public List getUserOneDayAcount(int start, int end, SearchParam param) {
        String selectSql="select * from dw_account_tj p1 left join dw_user p2 on p2.user_id=p1.user_id where 1=1 ";
        String searchSql=param.getSearchParamSql();
        String limitSql="limit ?,?";
        StringBuffer sb=new StringBuffer(selectSql);
        String querySql=sb.append(searchSql).append(limitSql).toString();
        logger.debug("getUserAcount():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{start,end}, new RowMapper<AccountOneDayModel>(){
            @Override
            public AccountOneDayModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                AccountOneDayModel s=new AccountOneDayModel();
                s.setId(rs.getLong("id"));
                s.setUser_id(rs.getLong("user_id"));
                s.setUsername(rs.getString("username"));
                s.setRealname(rs.getString("realname"));
                s.setTotal(rs.getDouble("total"));
                s.setUse_money(rs.getDouble("use_money"));
                s.setJin_money(rs.getDouble("jin_money"));
                s.setNo_use_money(rs.getDouble("no_use_money"));
                s.setWait_collect(rs.getDouble("collection"));
                s.setWait_repay(rs.getDouble("wait_repayMoney"));
                s.setAddtime(rs.getString("addtime"));
                return s;
            }
        });
        return list;
    }


    @Override
    public int getUserAccountCount(SearchParam param) {
        String selectSql="select count(p2.user_id) from dw_user p2 where 1=1 ";
        String searchSql=param.getSearchParamSql();
        // v1.6.7.2 RDPROJECT-554 zza start
//      searchSql=searchSql.replace("p1", "p2");
        // v1.6.7.2 RDPROJECT-554 zza end
        StringBuffer sb=new StringBuffer(selectSql);
        String querySql=sb.append(searchSql).toString();
        logger.debug("getUserAccountCount():"+querySql);
        int total=0;
        total=count(querySql);
        return total;
    }
    
    

    @Override
    public int getUserOneDayAccountCount(SearchParam param) {
        String selectSql="select count(1) from dw_account_tj p1 left join dw_user p2 on p2.user_id=p1.user_id where 1=1 ";
        String searchSql=param.getSearchParamSql();
        StringBuffer sb=new StringBuffer(selectSql);
        String querySql=sb.append(searchSql).toString();
        logger.debug("getUserOneDayAccountCount():"+querySql);
        int total=0;
        total=count(querySql);
        return total;
    }


    public List getUserAcount(SearchParam param) {
        String selectSql = "select p1.id,p2.user_id,p2.username,p2.realname,p1.total,p1.use_money,p1.no_use_money,"
                + "p1.collection as wait_collect,repay.wait_repay" +
                // v1.6.7.1 RDPROJECT-467 2013-11-19 start
                ",jin_repay.jin_wait_repay ";
                // v1.6.7.1 RDPROJECT-467 2013-11-19 end 
        String searchSql = param.getSearchParamSql();
        String groupSql = "group by p1.user_id ";
        String orderSql = "order by p1.user_id desc ";
        StringBuffer sb = new StringBuffer(selectSql);
        String querySql = sb.append(getUserAcountSql).append(searchSql).append(groupSql).append(orderSql).toString();
        logger.debug("getUserAcount():" + querySql);
        List list = getJdbcTemplate().query(querySql, new RowMapper<AccountSumModel>() {
            @Override
            public AccountSumModel mapRow(ResultSet rs, int num) throws SQLException {
                AccountSumModel s = new AccountSumModel();
                s.setId(rs.getLong("id"));
                s.setUser_id(rs.getLong("user_id"));
                s.setUsername(rs.getString("username"));
                s.setRealname(rs.getString("realname"));
                s.setTotal(rs.getDouble("total"));
                s.setUse_money(rs.getDouble("use_money"));
                s.setNo_use_money(rs.getDouble("no_use_money"));
                s.setWait_collect(rs.getDouble("wait_collect"));
                s.setWait_repay(rs.getDouble("wait_repay"));
                s.setNet_assets(s.getTotal() - s.getWait_repay());
                // v1.6.7.1 RDPROJECT-467 2013-11-19 start
                s.setJin_wait_repay(rs.getDouble("jin_wait_repay"));
                // v1.6.7.1 RDPROJECT-467 2013-11-19 end
                return s;
            }
        });
        return list;
    }
    
    public AccountModel getAccountList(long user_id) {
        String sql="select p2.username,p1.*,p3.*,p3.account as bankaccount,p4.name as bankname from dw_account as p1 " +
                "left join dw_user as p2 on p1.user_id=p2.user_id " +
                "left join dw_account_bank as p3 on p1.user_id=p3.user_id " +
                "left join dw_linkage as p4 on p4.id=p3.bank and p4.type_id=25 " +
                "where p1.user_id=?";
        AccountModel account=null;
        logger.debug(sql);
        try {
            account=this.getJdbcTemplate().queryForObject(sql,new Object[]{user_id}, getBeanMapper(AccountModel.class));
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
        }
        return account;
    }

    //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 start
        String accountBanksql="select p2.username,p2.realname,p3.id as bank_id,p1.*,p3.bank as bank,p3.branch as branch,p3.addtime as addtime,p3.modify_username as modify_username,p3.bank_realname as bank_realname,p3.payment as payment,p3.order as `order`,p3.province as province,p3.city as city,p3.area as area,p5.name as province_name,p6.name as city_name,p7.name as area_name,p3.account as bankaccount,p4.name as bankname from dw_account as p1 " +
                "left join dw_user as p2 on p1.user_id=p2.user_id " +
                "left join dw_account_bank as p3 on p1.user_id=p3.user_id " +
                "left join dw_linkage as p4 on p4.id=p3.bank and p4.type_id=25 " +
                 "left join dw_area as p5 on p5.id=p3.province "+
                 "left join dw_area as p6 on p6.id=p3.city "+
                 "left join dw_area as p7 on p7.id=p3.area "+
                "where 1=1 ";
        String accountBankCountsql="select count(1) from dw_account as p1 " +
                "left join dw_user as p2 on p1.user_id=p2.user_id " +
                "left join dw_account_bank as p3 on p1.user_id=p3.user_id " +
                "left join dw_linkage as p4 on p4.id=p3.bank and p4.type_id=25 " +
                 "left join dw_area as p5 on p5.id=p3.province "+
                 "left join dw_area as p6 on p6.id=p3.city "+
                 "left join dw_area as p7 on p7.id=p3.area "+
                "where 1=1 ";
        //v1.7.7.2 RDPROJECT-470 wcw 2013-11-28 end
    // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 start
    @SuppressWarnings({ "unchecked"})
    @Override
    public List<AccountModel> getAccountList(int start, int end, SearchParam param) {
        String searchSql = param.getSearchParamSql();
        String orderSql = "order by p1.user_id desc ";
        String limitSql = "limit :start,:end";
        StringBuffer sb = new StringBuffer(accountBanksql);
        String querySql = sb.append(searchSql).append(orderSql).append(limitSql).toString();
        logger.debug("getAccountList():" + querySql);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("end", end);
        return getNamedParameterJdbcTemplate().query(querySql, map, getBeanMapper(AccountModel.class));
    }

    @SuppressWarnings({ "unchecked"})
    @Override
    public List<AccountModel> getAccountList(SearchParam param) {
        // v1.6.7.2 RDPROJECT-603 xx 2013-12-17 start
        StringBuffer sb = new StringBuffer(accountBanksql);
        if(param!=null){
            if(!StringUtils.isBlank(param.getUsername())){
                if(StringUtils.isBlank(param.getSearch_type()) || "0".equals(param.getSearch_type())){
                    sb.append(" AND p2.username LIKE '%").append(param.getUsername()).append("%'");
                }else{
                    sb.append(" AND p2.username = '"+param.getUsername().trim()+"'");
                }
            }
            if (!StringUtils.isBlank(param.getRealname())) {
                sb.append(" AND p2.realname LIKE '%").append(param.getRealname()).append("%'");
            }
            if(param.getUser_id()>0){
                sb.append(" AND p2.user_id = ").append(param.getUser_id());
            }
        }
        sb.append(" ORDER BY p1.user_id DESC , p1.id DESC ");
        return getNamedParameterJdbcTemplate().query(sb.toString(), new HashMap<String, Object>(), getBeanMapper(AccountModel.class));
        // v1.6.7.2 RDPROJECT-603 xx 2013-12-17 end
    }
    // v1.6.7.1 RDPROJECT-402 zza 2013-11-22 end

    @Override
    public int getAccountCount(SearchParam param) {
        int total = 0;
        String searchSql = param.getSearchParamSql();
        StringBuffer sb = new StringBuffer(accountBankCountsql);
        String querySql = sb.append(searchSql).toString();
        logger.debug("getAccountList():" + querySql);
        try {
            total = count(querySql);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return total;
    }
    public void addHuikuanMoney(Huikuan huikuan){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String  huikuansql="insert into dw_huikuan(huikuan_money,huikuan_award,user_id,status,remark,addtime,cash_id) values(?,?,?,?,?,?,?)";
        final Huikuan h=huikuan;
        this.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(Connection conn)
                    throws SQLException {
                PreparedStatement ps=conn.prepareStatement(huikuansql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, h.getHuikuan_money());
                ps.setString(2, h.getHuikuan_award());
                ps.setLong(3, h.getUser_id());
                ps.setString(4, h.getStatus());
                ps.setString(5, h.getRemark());
                ps.setString(6, h.getAddtime());
                ps.setLong(7, h.getCash_id());
                return ps;
            }
        }, keyHolder);
        long key=keyHolder.getKey().longValue();
        huikuan.setId(key);
    }
    public List huikuanlist(int start, int end, SearchParam param){
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String sql="select p1.*,p2.username,p2.realname from dw_huikuan p1 left join dw_user p2 on p1.user_id=p2.user_id where 1=1 ";
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        List list=null;
        String searchSql=param.getSearchParamSql();
        String orderSql=" order by p1.addtime desc ";
        String limitSql="  limit ?,?";
        StringBuffer sb=new StringBuffer("");
        String querySql=sb.append(sql)
                .append(searchSql).append(orderSql).append(limitSql)
                .toString();
        
        try{
        list=getJdbcTemplate().query(querySql,new Object[]{start,end},  new RowMapper<HuikuanModel>(){
            @Override
            public HuikuanModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                HuikuanModel s=new HuikuanModel();
                s.setId(rs.getLong("id"));
                s.setUser_id(rs.getLong("user_id"));
                s.setHuikuan_money(rs.getString("huikuan_money"));
                s.setHuikuan_award(rs.getString("huikuan_award"));
                s.setStatus(rs.getString("status"));
                s.setRemark(rs.getString("remark"));
                s.setAddtime(rs.getString("addtime"));
                // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 START
                s.setVerify_remark(rs.getString("verify_remark"));
                // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 end
                s.setUsername(rs.getString("username"));
                s.setCash_id(rs.getLong("cash_id"));
                // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
                s.setRealname(rs.getString("realname"));
                // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
                return s;
            }
        });
    } catch (DataAccessException e) {
        logger.error(e.getMessage());
        e.printStackTrace();
    }
        return list;
    }
    
    public List huikuanlist(SearchParam param){
        String sql="select p1.*,p2.username from dw_huikuan p1 left join dw_user p2 on p1.user_id=p2.user_id where 1=1 ";
        List list=null;
        String searchSql=param.getSearchParamSql();
        String orderSql=" order by p1.addtime desc ";
        StringBuffer sb=new StringBuffer("");
        String querySql=sb.append(sql)
                .append(searchSql).append(orderSql)
                .toString();
        
        try{
        list=getJdbcTemplate().query(querySql,new Object[]{},  new RowMapper<HuikuanModel>(){
            @Override
            public HuikuanModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                HuikuanModel s=new HuikuanModel();
                s.setId(rs.getLong("id"));
                s.setUser_id(rs.getLong("user_id"));
                s.setHuikuan_money(rs.getString("huikuan_money"));
                s.setHuikuan_award(rs.getString("huikuan_award"));
                s.setStatus(rs.getString("status"));
                s.setRemark(rs.getString("remark"));
                // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 START
                s.setVerify_remark(rs.getString("verify_remark"));
                // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 end
                s.setAddtime(rs.getString("addtime"));
                s.setUsername(rs.getString("username"));
                s.setCash_id(rs.getLong("cash_id"));
                return s;
            }
        });
    } catch (DataAccessException e) {
        logger.error(e.getMessage());
        e.printStackTrace();
    }
        return list;
    }
    public int gethuikuanCount(SearchParam param){
        int total=0;
        String countSql="select count(1) from dw_huikuan p1 left join dw_user p2 on p1.user_id=p2.user_id where 1=1";
        StringBuffer sb = new StringBuffer(countSql);
        String sql = sb.append(param.getSearchParamSql()).toString();
        try {
            total=count(sql);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return total;
    }
    
    public double gethuikuanSum(long user_id,int status){
        double total=0;
        String sql="select sum(p1.huikuan_money) as num from dw_huikuan p1  where p1.user_id=? and p1.status=? ";
        try {
            total=this.sum(sql, new Object[]{user_id,status});
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return total;
    }
    
    public double gethuikuanSum(long user_id){
        double total=0;
        //V1.6.7.2 RDPROJECT-596 liukun 2013-12-16 start
        String sql="select sum(p1.huikuan_money) as num from dw_huikuan p1  where p1.user_id=? and (p1.status=1 or p1.status=0 or p1.status=4) ";
//        String sql="select sum(p1.huikuan_money) as num from dw_huikuan p1  where p1.user_id=? and (p1.status=1 or p1.status=0) ";
        //V1.6.7.2 RDPROJECT-596 liukun 2013-12-16 end
        try {
            total=this.sum(sql, new Object[]{user_id});
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return total;
    }
    
    public HuikuanModel viewhuikuan(int id){
        String sql="select p1.*,p2.username from dw_huikuan p1 left join dw_user p2 on p1.user_id=p2.user_id where p1.id=?";
        HuikuanModel huikuan=this.getJdbcTemplate().queryForObject(sql,new Object[]{id}, new RowMapper<HuikuanModel>(){
            @Override
            public HuikuanModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                HuikuanModel s=new HuikuanModel();
                s.setId(rs.getLong("id"));
                s.setUser_id(rs.getLong("user_id"));
                s.setHuikuan_money(rs.getString("huikuan_money"));
                s.setHuikuan_award(rs.getString("huikuan_award"));
                s.setStatus(rs.getString("status"));
                s.setRemark(rs.getString("remark"));
                s.setAddtime(rs.getString("addtime"));
                s.setUsername(rs.getString("username"));
                // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 START
                s.setVerify_remark(rs.getString("verify_remark"));
                // v1.6.6.1 RDPROJECT-30 wcw 2013-09-27 end
                s.setCash_id(rs.getLong("cash_id"));
                return s;
            }
        });
        return huikuan;
    }
    public void verifyhuikuan(Huikuan huikuan){
        String sql="update dw_huikuan set user_id=?,huikuan_money=?,huikuan_award=?,status=?,remark=?,addtime=?,verify_remark=? where id=?";
        this.getJdbcTemplate().update(sql, huikuan.getUser_id(),huikuan.getHuikuan_money(),huikuan.getHuikuan_award(),
                huikuan.getStatus(),huikuan.getRemark(),huikuan.getAddtime(),huikuan.getVerify_remark(),huikuan.getId());
    }

    @Override
    public RepaySummary getRepaySummary(long user_id) {
        /*String sql="select sum(t.repayment_account) as repayTotal," +
                " sum(t.interest) as repayInterest," +
                " min(t.repayment_time) as repayTime" +
                " from dw_borrow_repayment t" +
                " left join dw_borrow b on b.id = t.borrow_id" +
                " where t.repayment_yesaccount <= 0 " +
                " and ((b.status in (3,6,7) and b.is_flow<>1) or (b.status in(1,8) and b.is_flow=1)) and t.status!=1" +
                " and b.user_id =?";*/
        String sql="select sum(t.repayment_account) as repayTotal," +
                " sum(t.interest) as repayInterest," +
                " min(t.repayment_time) as repayTime" +
                " from dw_borrow_repayment t" +
                " left join dw_borrow b on b.id = t.borrow_id" +
                " where t.repayment_yesaccount <= 0 " +
                " and ((b.status in (3,6,7) and b.type<>"+Constant.TYPE_FLOW+") or (b.status in(1,8) and b.type="+Constant.TYPE_FLOW+")) and t.status!=1" +
                " and b.user_id =?";
        logger.info("RepaySummary SQL:"+sql);
        double repayAccount = 0;
        // 查询最近待还金额
        /*String sql2 = "SELECT t.repayment_account as num " +
                "FROM dw_borrow_repayment t LEFT JOIN dw_borrow b ON b.id=t.borrow_id " +
                "WHERE t.repayment_yesaccount<=0 AND ((b.status IN(3,6,7) AND b.is_flow<>1) or (b.status IN(1,8) AND b.is_flow=1)) " +
                "AND t.status!=1 AND b.user_id=? AND t.repayment_time=(SELECT MIN(t.repayment_time) AS repayTime " +
                "FROM dw_borrow_repayment t LEFT JOIN dw_borrow b ON b.id = t.borrow_id " +
                "WHERE t.repayment_yesaccount <= 0 AND ((b.status IN (3,6,7) AND b.is_flow<>1) or (b.status IN(1,8) AND b.is_flow=1)) AND t.status!=1 AND b.user_id =?)";*/
        String sql2 = "SELECT t.repayment_account as num " +
                "FROM dw_borrow_repayment t LEFT JOIN dw_borrow b ON b.id=t.borrow_id " +
                "WHERE t.repayment_yesaccount<=0 AND ((b.status IN(3,6,7) AND b.type<>"+Constant.TYPE_FLOW+") or (b.status IN(1,8) AND b.type="+Constant.TYPE_FLOW+")) " +
                "AND t.status!=1 AND b.user_id=? AND t.repayment_time=(SELECT MIN(t.repayment_time) AS repayTime " +
                "FROM dw_borrow_repayment t LEFT JOIN dw_borrow b ON b.id = t.borrow_id " +
                "WHERE t.repayment_yesaccount <= 0 AND ((b.status IN (3,6,7) AND b.type<>"+Constant.TYPE_FLOW+") or (b.status IN(1,8) AND b.type="+Constant.TYPE_FLOW+")) AND t.status!=1 AND b.user_id =?)";
        repayAccount = this.sum(sql2, new Object[] { user_id, user_id });
        RepaySummary r=new RepaySummary();
        r.setUser_id(user_id);
        try {
            r=getJdbcTemplate().queryForObject(sql, new RowMapper<RepaySummary>(){
                @Override
                public RepaySummary mapRow(ResultSet rs, int num) throws SQLException {
                    RepaySummary r=new RepaySummary();
                    r.setRepayTotal(rs.getDouble("repayTotal"));
                    r.setRepayInterest(rs.getDouble("repayInterest"));
                    r.setRepayTime(rs.getString("repayTime"));
                    return r;
                }
            }, user_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        r.setRepayAccount(repayAccount);
        return r;
    }

    @Override
    public BorrowSummary getBorrowSummary(long user_id) {
        
        /*String sql="select sum(b.account) as borrowTotal," +
                " sum(b.repayment_account-b.account) as borrowInterest," +
                " count(1)as borrowTimes,b.user_id " +
                " from dw_borrow b " +
                "where b.status in(3,6,7,8) and b.user_id=? group by b.user_id";*/
        // v1.6.7.2 RDPROJECT-454 lx 2013.12.04 start
        String sql="SELECT COUNT(distinct borrow_id) borrowTimes,SUM(b.capital) borrowTotal, "
             // v1.6.7.2 RDPROJECT-601 wcw 2013.12.04 start
                + "SUM(interest) borrowInterest,b.user_id FROM dw_borrow_repayment b WHERE b.user_id=? group by  b.user_id";
             // v1.6.7.2 RDPROJECT-601 wcw 2013.12.04 end
        // v1.6.7.2 RDPROJECT-454 lx 2013.12.04 end
        BorrowSummary b=new BorrowSummary();
        b.setUser_id(user_id);
        try {
            b=getJdbcTemplate().queryForObject(sql, new RowMapper<BorrowSummary>(){
                @Override
                public BorrowSummary mapRow(ResultSet rs, int num) throws SQLException {
                    BorrowSummary b=new BorrowSummary();
                    b.setBorrowTotal(rs.getDouble("borrowTotal"));
                    b.setBorrowInterest(rs.getDouble("borrowInterest"));
                    b.setBorrowTimes(rs.getInt("borrowTimes"));
                    return b;
                }
            }, user_id);
        } catch (Exception e) {
//          logger.error(e.getMessage());
            logger.info("查询结果为空！");
        }
        return b;
    }

    @Override
    public InvestSummary getInvestSummary(long user_id) {
        // v1.6.7.2 RDPROJECT-601 wcw 2013.12.18 start
        String sql="select sum(c.capital) as investTotal, " +
                "sum(c.interest) as investInterest, " +
                "count(1) as investTimes," +
                "t.user_id as user_id from dw_borrow_tender t " +
                "left join dw_borrow b on b.id=t.borrow_id " +
                "left join dw_borrow_collection c on c.tender_id=t.id " +
                "where t.status=1  and b.status in(1,3,6,7,8) " +
                "and t.user_id=? group by t.user_id"; 
        // v1.6.7.2 RDPROJECT-601 wcw 2013.12.18 end
        InvestSummary i=new InvestSummary();
        i.setUser_id(user_id);
        try {
            i=getJdbcTemplate().queryForObject(sql, new RowMapper<InvestSummary>(){
                @Override
                public InvestSummary mapRow(ResultSet rs, int num) throws SQLException {
                    InvestSummary i=new InvestSummary();
                    i.setInvestTotal(rs.getDouble("investTotal"));
                    i.setInvestInterest(rs.getDouble("investInterest"));
                    i.setInvestTimes(rs.getInt("investTimes"));
                    return i;
                }
            }, user_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return i;
    }

    @Override
    public CollectSummary getCollectSummary(long user_id) {
        CollectSummary c=new CollectSummary();
        c.setUser_id(user_id);
        
        String collectInterestTotalSql="select sum(wait_interest) as num from dw_borrow_tender where user_id=?";
        double collectInterestTotal=getSum(collectInterestTotalSql, user_id);
        c.setCollectInterest(collectInterestTotal);
        
        /*String sql="select * from dw_borrow_collection where id=" +
                "(select c.id from dw_borrow_tender  t " +
                "inner join dw_borrow b on b.id=t.borrow_id and (b.status in(3,6,7) or ((b.status=1 or b.status=8) and b.is_flow=1)) " +
                "inner join dw_borrow_collection c on c.tender_id=t.id and c.status=0  " +
                "where t.user_id=? group by c.id order by c.repay_time asc limit 0,1)";*/
        String sql="select * from dw_borrow_collection where id=" +
                "(select c.id from dw_borrow_tender  t " +
                "inner join dw_borrow b on b.id=t.borrow_id and (b.status in(3,6,7) or ((b.status=1 or b.status=8) and b.type="+Constant.TYPE_FLOW+")) " +
                "inner join dw_borrow_collection c on c.tender_id=t.id and c.status=0  " +
                "where t.user_id=? group by c.id order by c.repay_time asc limit 0,1)";
        Collection tc=new Collection();
        try {
            tc=getJdbcTemplate().queryForObject(sql,new Object[]{user_id}, getBeanMapper(Collection.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        c.setCollectMoney(NumberUtils.getDouble(tc.getRepay_account()));
        c.setCollectTime(tc.getRepay_time());
        return c;
    }
    
    public double getCollectionSum(long user_id,int status) {
        String sql="SELECT SUM(a.`repay_account`) as num FROM dw_borrow_collection a LEFT JOIN dw_borrow_tender b ON a.`tender_id`=b.id WHERE b.`user_id`=?  AND a.status=?";
        logger.info("SQL:"+sql);
        logger.info("SQL:"+user_id);
        double sum=0.0;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id,status});
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }

    @Override
    public double gethuikuanSum(long user_id, int status, long start) {
        double total=0;
        String sql="select sum(p1.huikuan_money) as num from dw_huikuan p1  where p1.user_id=? and p1.status=? and p1.addtime>?";
        try {
            total=this.sum(sql, new Object[]{user_id,status,start});
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return total;
    }
    
    /**
     * 回款续投奖励合计
     * @param user_id
     * @param status 状态
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @return
     */
    public double getHuikuanRewardSum(long user_id,int status,long start_time , long end_time){
        
        if(user_id <= 0){
            logger.error("user_id is null.");
            return 0;
        }
        
        double total=0;
        StringBuffer sql= new StringBuffer("select sum(dh.huikuan_award) as num from dw_huikuan dh where dh.user_id = ? ");
        
        // 查询数据数据封装 
        List<Object> list = new ArrayList<Object>();
        list.add(user_id);
        
        //状态
        if(status >= 0){
            list.add(status);
            sql.append(" and dh.status = ? ");
        }
        
        //奖励成功开始时间
        if(start_time > 0){
            list.add(start_time);
            sql.append(" and  dh.addtime >= ? ");
        }
        
        //奖励成功结束时间
        if(end_time > 0){
            list.add(end_time);
            sql.append(" and dh.addtime <= ? ");
        }
        
        // 新建obj数组，用于JDBC查询，数组程度为list size
        Object[] obj = new Object[list.size()]; 
        // 遍历list，将值存入obj数组中
        for(int i = 0 ; i < list.size() ; i++){
            obj[i] = list.get(i);
        }
        
        double sum=0.0;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql.toString(),obj);
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
        
    }
    
    @Override
    public HuikuanModel getHuikuanByCashid(long cash_id) {
        String sql="select p1.*,p2.username from dw_huikuan p1 left join dw_user p2 on p1.user_id=p2.user_id where p1.cash_id=?";
        HuikuanModel huikuan= this.getJdbcTemplate().queryForObject(sql,new Object[]{cash_id}, new RowMapper<HuikuanModel>(){
                @Override
                public HuikuanModel mapRow(ResultSet rs, int num)
                        throws SQLException {
                    HuikuanModel s=new HuikuanModel();
                    s.setId(rs.getLong("id"));
                    s.setUser_id(rs.getLong("user_id"));
                    s.setHuikuan_money(rs.getString("huikuan_money"));
                    s.setHuikuan_award(rs.getString("huikuan_award"));
                    s.setStatus(rs.getString("status"));
                    s.setRemark(rs.getString("remark"));
                    s.setAddtime(rs.getString("addtime"));
                    s.setUsername(rs.getString("username"));
                    s.setCash_id(rs.getLong("cash_id"));
                    return s;
                }
            });
        return huikuan;
    }

    @Override
    public double getCollectionSum(long user_id, int status, long startTime) {
        String sql="SELECT SUM(a.`repay_account`) as num FROM dw_borrow_collection a " +
                "LEFT JOIN dw_borrow_tender b ON a.`tender_id`=b.id " +
                "WHERE b.`user_id`=?  AND a.status=? and a.repay_yestime>?";
        logger.info("SQL:"+sql);
        logger.info("SQL:"+user_id);
        logger.info("SQL:"+startTime);
        double sum=0.0;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id,status});
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }
    
    @Override
    public double getCollectionSumNoJinAndSecond(long user_id, int status, long startTime) {
        // v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//      String sql="select SUM(c.`repay_account`) as num from dw_borrow_tender t " +
//              "left join dw_borrow_collection c on t.id=c.tender_id and c.status=? and c.repay_yestime>? " +
//              "left join dw_borrow b on b.id= t.borrow_id " +
//              "where b.is_mb!=1 and b.is_jin!=1  and t.user_id=? ";
        String sql="select SUM(c.`repay_account`) as num from dw_borrow_tender t " +
                "left join dw_borrow_collection c on t.id=c.tender_id and c.status=? and c.repay_yestime>? " +
                "left join dw_borrow b on b.id= t.borrow_id " +
                "where b.type!="+Constant.TYPE_SECOND+" and b.type!="+Constant.TYPE_PROPERTY+"  and t.user_id=? ";
        // v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
        logger.info("SQL:"+sql);
        logger.info("SQL:"+user_id);
        logger.info("SQL:"+startTime);
        double sum=0.0;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{status,startTime,user_id});
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }
    
    public double getCollectionSumNoJinAndSecond(long user_id, int status, int isday,long startTime) {
        double borrow_fee=Global.getDouble("borrow_fee");
        // v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 start
//      String sql="select SUM(c.`repay_account`-c.interest*?) as num from dw_borrow_tender t " +
//              "left join dw_borrow_collection c on t.id=c.tender_id and c.status=? and c.repay_yestime>? " +
//              "left join dw_borrow b on b.id= t.borrow_id " +
//              "where b.is_mb!=1 and b.is_jin!=1  and t.user_id=? ";
        String sql="select SUM(c.`repay_account`-c.interest*?) as num from dw_borrow_tender t " +
                "left join dw_borrow_collection c on t.id=c.tender_id and c.status=? and c.repay_yestime>? " +
                "left join dw_borrow b on b.id= t.borrow_id " +
                "where b.type!="+Constant.TYPE_SECOND+" and b.type!="+Constant.TYPE_PROPERTY+"  and t.user_id=? ";
        // v1.6.6.7 RDPROJECT-428 lhm 2013-11-06 end
        Object[] args=new Object[]{borrow_fee,status,startTime,user_id};
        if(isday==0){
            sql+=" and b.isday=?";
            args=new Object[]{borrow_fee,status,startTime,user_id,isday};
        }
        logger.info("SQL:"+sql);
        logger.info("SQL:"+user_id);
        logger.info("SQL:"+startTime);
        double sum=0.0;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,args);
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }

    @Override
    public List getUserOneDayAcount(SearchParam param) {
        String selectSql="select * from dw_account_tj ";
        String searchSql=param.getSearchParamSql();
        StringBuffer sb=new StringBuffer(selectSql);
        String querySql=sb.append(searchSql).toString();
        logger.debug("getUserAcount():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{}, new RowMapper<AccountOneDayModel>(){
            @Override
            public AccountOneDayModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                AccountOneDayModel s=new AccountOneDayModel();
                s.setId(rs.getLong("id"));
                s.setUser_id(rs.getLong("user_id"));
                s.setUsername(rs.getString("username"));
                s.setRealname(rs.getString("realname"));
                s.setTotal(rs.getDouble("total"));
                s.setUse_money(rs.getDouble("use_money"));
                s.setJin_money(rs.getDouble("jin_money"));
                s.setNo_use_money(rs.getDouble("no_use_money"));
                s.setWait_collect(rs.getDouble("collection"));
                s.setWait_repay(rs.getDouble("wait_repayMoney"));
                s.setAddtime(rs.getString("addtime"));
                return s;
            }
        });
        return list;
    }

    @Override
    public int getFriendTiChengAccountCount(SearchParam param) {
        String selectSql="select count(1) from (SELECT invite.username as usernames,SUM(p2.account) as money,DATE_FORMAT(FROM_UNIXTIME(p2.addtime),'%Y-%m') as addtimes,p1.invite_userid as invite_userid" +
                " FROM  dw_borrow_tender  AS p2 " +
                "LEFT JOIN dw_user AS p1 ON p1.user_id=p2.user_id " +
                "LEFT JOIN dw_borrow AS p3 ON p3.id=p2.borrow_id " +
                "LEFT JOIN dw_user AS invite ON invite.user_id=p1.invite_userid " +
                "WHERE p1.invite_userid IS NOT NULL " +
                "AND p1.invite_userid>0  " +
                "AND ((p3.isday IS NULL) OR (p3.isday<>1)) " +
                "GROUP BY  p1.invite_userid ,DATE_FORMAT(FROM_UNIXTIME(p2.addtime),'%Y-%m')) as p where 1=1 ";
        StringBuffer sb = new StringBuffer(selectSql);
//      StringBuffer sb=new StringBuffer(selectSql);
        // 提成的 字段 和 SearchParam 里的 字段名 不兼容，故重新判断
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and p.usernames like '%").append(param.getUsername()).append("%'");
        }
        String querySql=sb.toString();
        logger.debug("getTiChengAccountCount():"+querySql);
        int total=0;
        total=count(querySql);
        return total;
    }
    @Override
    public int getTiChengAccountCount(SearchParam param) {
        String selectSql="select count(1) from view_tc_backend where 1=1 ";
        StringBuffer sb = new StringBuffer(selectSql);
//      StringBuffer sb=new StringBuffer(selectSql);
        // 提成的 字段 和 SearchParam 里的 字段名 不兼容，故重新判断
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and usernames like '%").append(param.getUsername()).append("%'");
        }
        String querySql=sb.toString();
        logger.debug("getTiChengAccountCount():"+querySql);
        int total=0;
        total=count(querySql);
        return total;
    }
    @Override
    public List getTiChengAcount(SearchParam param) {
        String selectSql="select * from view_tc_backend ";
//      String searchSql=param.getSearchParamSql();
        StringBuffer sb=new StringBuffer(selectSql);
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and usernames like '%").append(param.getUsername()).append("%'");
        }
        String querySql=sb.toString();
        logger.debug("getUserAcount():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{}, new RowMapper<TiChengModel>(){
            @Override
            public TiChengModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                TiChengModel s=new TiChengModel();
                s.setMoney(rs.getString("money"));
                s.setUsername(rs.getString("usernames"));
                s.setAddtimes(rs.getString("addtimes"));
                return s;
            }
        });
        return list;
    }
    @Override
    public List getFriendTiChengAcount(SearchParam param) {
        String selectSql="select  p.* from (SELECT invite.username as usernames,SUM(p2.account) as money,DATE_FORMAT(FROM_UNIXTIME(p2.addtime),'%Y-%m') as addtimes,p1.invite_userid as invite_userid" +
                " FROM  dw_borrow_tender  AS p2 " +
                "LEFT JOIN dw_user AS p1 ON p1.user_id=p2.user_id " +
                "LEFT JOIN dw_borrow AS p3 ON p3.id=p2.borrow_id " +
                "LEFT JOIN dw_user AS invite ON invite.user_id=p1.invite_userid " +
                "WHERE p1.invite_userid IS NOT NULL " +
                "AND p1.invite_userid>0  " +
                "AND ((p3.isday IS NULL) OR (p3.isday<>1)) " +
                "GROUP BY  p1.invite_userid ,DATE_FORMAT(FROM_UNIXTIME(p2.addtime),'%Y-%m')) as p where 1=1 ";
//      String searchSql=param.getSearchParamSql();
        StringBuffer sb=new StringBuffer(selectSql);
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and p.usernames like '%").append(param.getUsername()).append("%'");
        }
        String querySql=sb.toString();
        logger.debug("getUserAcount():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{}, new RowMapper<TiChengModel>(){
            @Override
            public TiChengModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                TiChengModel s=new TiChengModel();
                s.setMoney(rs.getString("money"));
                s.setUsername(rs.getString("usernames"));
                s.setAddtimes(rs.getString("addtimes"));
                return s;
            }
        });
        return list;
    }
    @Override
    public List getFriendTiChengAccountList(int start, int end, SearchParam param) {
        String selectSql="select  p.* from (SELECT invite.username as usernames,SUM(p2.account) as money,DATE_FORMAT(FROM_UNIXTIME(p2.addtime),'%Y-%m') as addtimes,p1.invite_userid as invite_userid" +
                " FROM  dw_borrow_tender  AS p2 " +
                "LEFT JOIN dw_user AS p1 ON p1.user_id=p2.user_id " +
                "LEFT JOIN dw_borrow AS p3 ON p3.id=p2.borrow_id " +
                "LEFT JOIN dw_user AS invite ON invite.user_id=p1.invite_userid " +
                "WHERE p1.invite_userid IS NOT NULL " +
                "AND p1.invite_userid>0  " +
                "AND ((p3.isday IS NULL) OR (p3.isday<>1)) " +
                "GROUP BY  p1.invite_userid ,DATE_FORMAT(FROM_UNIXTIME(p2.addtime),'%Y-%m')) as p  where 1=1 ";
        String limitSql=" limit ?,?";
        StringBuffer sb=new StringBuffer(selectSql);
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and p.usernames like '%").append(param.getUsername()).append("%'");
        }
        String querySql=sb.append(limitSql).toString();
        logger.debug("getTiChengAccountCount():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{start,end}, new RowMapper<TiChengModel>(){
            @Override
            public TiChengModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                TiChengModel s=new TiChengModel();
                s.setMoney(rs.getString("money"));
                s.setUsername(rs.getString("usernames"));
                s.setAddtimes(rs.getString("addtimes"));
                return s;
            }
        });
        return list;
    }
    @Override
    public List getTiChengAccountList(int start, int end, SearchParam param) {
        String selectSql="select * from view_tc_backend where 1=1 ";
        String limitSql="limit ?,?";
        StringBuffer sb=new StringBuffer(selectSql);
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and usernames like '%").append(param.getUsername()).append("%'");
        }
        String querySql=sb.append(limitSql).toString();
        logger.debug("getTiChengAccountCount():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{start,end}, new RowMapper<TiChengModel>(){
            @Override
            public TiChengModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                TiChengModel s=new TiChengModel();
                s.setMoney(rs.getString("money"));
                s.setUsername(rs.getString("usernames"));
                s.setAddtimes(rs.getString("addtimes"));
                return s;
            }
        });
        return list;
    }
    
    /**
     * 红包dao
     */
    @Override
    public List getHongBaoList(int start, int end, SearchParam param) {
        String selectSql="select p.*,p1.username,p4.name as typename,p5.hongbao as hongbao from dw_hongbao as p left join dw_user as p1 on p.user_id=p1.user_id left join dw_linkage as p4  on p.type=p4.value left join dw_account as p5 on p1.user_id=p5.user_id where 1=1 ";
        String limitSql=" limit ?,?";
        StringBuffer sb=new StringBuffer(selectSql);
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and p1.username="+"'"+param.getUsername()+"'");
        }
        String orderSql=" order by p.addtime desc ";
        sb.append(orderSql);
        String querySql=sb.append(limitSql).toString();
        logger.debug("getHongBaoList():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{start,end}, new RowMapper<HongBaoModel>(){
            @Override
            public HongBaoModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                HongBaoModel s=new HongBaoModel();
                s.setId(rs.getLong("id"));
                s.setAddtime(rs.getString("addtime"));
                s.setRemark(rs.getString("remark"));
                s.setHongbao_money(rs.getDouble("hongbao_money"));
                s.setUsername(rs.getString("username"));
                s.setTypename(rs.getString("typename"));
                s.setHongbao(rs.getString("hongbao"));
                return s;
            }
        });
        return list;
    }
    @Override
    public int getHongBaoListCount(SearchParam param) {
        String selectSql="select count(1) from dw_hongbao as p left join dw_user as p1 on p.user_id=p1.user_id left join dw_linkage as p4 on p.type=p4.value left join dw_account as p5 on p1.user_id=p5.user_id where 1=1  ";
        StringBuffer sb = new StringBuffer(selectSql);
//      StringBuffer sb=new StringBuffer(selectSql);
        // 提成的 字段 和 SearchParam 里的 字段名 不兼容，故重新判断
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and p1.username="+"'"+param.getUsername()+"'");
        }
        String querySql=sb.toString();
        logger.debug("getHongBaoCount():"+querySql);
        int total=0;
        total=count(querySql);
        return total;
    }
    String accountReconciliationSql=" FROM dw_user AS p1 " +
            "LEFT JOIN dw_account AS p2 ON p1.user_id=p2.user_id " +
            "LEFT JOIN (SELECT SUM(money) AS recharge_money,p3.user_id FROM dw_account_recharge p3 WHERE p3.status=1 GROUP BY p3.user_id) AS p4 ON p4.user_id=p1.user_id " +         //充值总额  （充值资金1）
            "LEFT JOIN (SELECT SUM(money) AS log_recharge_money,p5.user_id FROM dw_account_log p5 WHERE (TYPE='recharge' OR TYPE='recharge_success') GROUP BY p5.user_id) AS p6 ON p6.user_id=p1.user_id " +   //资金记录中充值总额 （充值资金2）
            "LEFT JOIN (SELECT SUM(money) AS up_recharge_money,p7.user_id FROM dw_account_recharge p7 WHERE p7.status=1 AND p7.type=1 GROUP BY p7.user_id) AS p8 ON p8.user_id=p1.user_id " +    //线上充值总额（其中：线上）
            "LEFT JOIN (SELECT SUM(money) AS down_recharge_money,p9.user_id FROM dw_account_recharge p9 WHERE p9.status=1 AND p9.type=2 GROUP BY p9.user_id) AS p10 ON p10.user_id=p1.user_id " +  //线下充值总额  （其中：线下1）
            "LEFT JOIN (SELECT SUM(money) AS houtai_recharge_money,p11.user_id FROM dw_account_recharge p11 WHERE p11.status=1 AND p11.type=2 AND p11.payment='"+EnumPayInterface.BACK_RECHARGE.getValue()+"' GROUP BY p11.user_id) AS p12 ON p12.user_id=p1.user_id " +  //后台充值总额(其中：线下2)
            "LEFT JOIN (SELECT SUM(repayment_account) AS allcollection,p13.user_id FROM dw_borrow_tender  p13 GROUP BY p13.user_id) AS p14 ON p14.user_id=p1.user_id " +   //用户投标所有待收资金总和（待收资金2）
            "LEFT JOIN (SELECT SUM(credited) AS cash_money,SUM(fee) as cash_fee,p15.user_id FROM dw_account_cash p15 WHERE p15.status=1  GROUP BY p15.user_id) AS p16 ON p16.user_id=p1.user_id " +  //成功提现总额
            "LEFT JOIN (SELECT SUM(p18.account*p18.part_account/100+p18.funds) AS invest_award,p17.user_id FROM dw_borrow_tender AS p17 LEFT JOIN dw_borrow AS p18 ON p17.borrow_id=p18.id GROUP BY p17.user_id) AS p19 ON p19.user_id=p1.user_id " +  //投标奖励金额
            "LEFT JOIN (SELECT SUM(p20.interest-p20.wait_interest) AS invest_yeswait_interest,SUM(p20.wait_interest) AS wait_interest,p20.user_id FROM dw_borrow_tender p20 GROUP BY p20.user_id) AS p21 ON p21.user_id=p1.user_id " +  //投标已收利息
            "LEFT JOIN (SELECT SUM(account*part_account/100+funds) AS borrow_award,user_id FROM dw_borrow GROUP BY user_id) AS p22 ON p22.user_id=p1.user_id " +  //借款标奖励
            "LEFT JOIN (SELECT SUM(money) AS borrow_fee,user_id FROM dw_account_log WHERE TYPE='borrow_fee' GROUP BY user_id) AS p23 ON p23.user_id=p1.user_id " +  //借款管理费
            "LEFT JOIN (SELECT SUM(money) AS manage_fee,user_id FROM dw_account_log WHERE TYPE='manage_fee' GROUP BY user_id) AS p24 ON p24.user_id=p1.user_id " +  //利息管理费
            "LEFT JOIN (SELECT SUM(money) AS system_fee,user_id FROM dw_account_log WHERE (TYPE='manage_fee' OR TYPE='borrow_fee' or (type='vip_fee' AND remark LIKE '%扣除资金%')) GROUP BY user_id) AS p25 ON p25.user_id=p1.user_id " +  //系统扣费
            "LEFT JOIN (SELECT SUM(money) AS invite_money,user_id FROM dw_account_log WHERE TYPE='invite_money' group by user_id) AS p26 ON p26.user_id=p1.user_id " +   //推广奖励（包括邀请好友提成）
            "LEFT JOIN (SELECT SUM(money) AS vip_money,user_id FROM dw_account_log WHERE TYPE='vip_fee' AND remark LIKE '%扣除资金%' group by user_id) AS p27 ON p27.user_id=p1.user_id " +  //vip扣费
            "LEFT JOIN (SELECT  SUM(p28.interest)AS repayment_interest,SUM(p28.repayment_account-p28.interest)AS repayment_principal,p29.user_id FROM dw_borrow_repayment AS p28 LEFT JOIN dw_borrow AS p29 ON p28.borrow_id=p29.id where p29.type!="+Constant.TYPE_FLOW+" and p28.repayment_yestime is null GROUP BY p29.user_id ) AS p30 ON p1.user_id=p30.user_id " +   //普通标待还利息、待还本金  
            "LEFT JOIN (SELECT  SUM(p31.interest)AS repayment_interest,SUM(p31.repay_account-p31.interest)AS repayment_principal,borrow.user_id FROM dw_borrow_collection  AS p31 LEFT JOIN dw_borrow_tender AS p32 ON p31.tender_id=p32.id left join dw_borrow as borrow on borrow.id=p32.borrow_id where borrow.type="+Constant.TYPE_FLOW+" and p31.repay_yestime is null GROUP BY borrow.user_id) AS p33 ON p1.user_id=p33.user_id " +//流转标待还本金，待还利息 
            "LEFT JOIN (SELECT  SUM(p34.interest)AS yes_repayment_interest,p35.user_id FROM dw_borrow_repayment AS p34 LEFT JOIN dw_borrow AS p35 ON p34.borrow_id=p35.id WHERE p35.type!="+Constant.TYPE_FLOW+" and p34.repayment_yestime  IS NOT NULL GROUP BY p35.user_id ) AS p36 ON p1.user_id=p36.user_id " +       //已还利息
            "LEFT JOIN (SELECT  SUM(p37.interest)AS yes_repayment_interest,borrow.user_id FROM dw_borrow_collection  AS p37 LEFT JOIN dw_borrow_tender AS p38 ON p37.tender_id=p38.id left join dw_borrow borrow on borrow.id=p38.borrow_id WHERE borrow.type="+Constant.TYPE_FLOW+" and p37.repay_yestime  IS NOT NULL  GROUP BY borrow.user_id) AS p39 ON p1.user_id=p39.user_id  WHERE 1=1";   //已还利息
            
    @Override
    public int getAccountReconciliationListCount(SearchParam param) {
        String selectSql="select count(1)";
        StringBuffer sb = new StringBuffer(selectSql);
//      StringBuffer sb=new StringBuffer(selectSql);
        // 提成的 字段 和 SearchParam 里的 字段名 不兼容，故重新判断
        sb.append(" from dw_user as p1 where 1=1 ");
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and p1.username="+"'"+param.getUsername()+"'");
        }
        String querySql=sb.toString();
        logger.debug("getAccountReconciliationSql():"+querySql);
        int total=0;
        total=count(querySql);
        return total;
    }
    // v1.6.5.3 RDPROJECT-175 zza 2013-09-18 start
    /**
     * 资金对账列表
     * 
     * @param start start
     * @param end end
     * @param param param
     * @return list
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getAccountReconciliationList(int start, int end, SearchParam param) {
        String selectSql = "SELECT p1.username,p1.realname,p2.*,p4.recharge_money,p6.log_recharge_money,"
                + "p8.up_recharge_money,p10.down_recharge_money,p12.houtai_recharge_money,p14.allcollection,"
                + "p16.cash_money,p19.invest_award,p21.invest_yeswait_interest,p21.wait_interest,p22.borrow_award,"
                + "p23.borrow_fee,p24.manage_fee,(p25.system_fee+p16.cash_fee) as system_fee,p26.invite_money,"
                + "p27.vip_money,p30.repayment_interest as repayment_interest,"
                + "p33.repayment_interest AS flow_repayment_interest,p30.repayment_principal as repayment_principal,"
                + "p33.repayment_principal AS flow_repayment_principal,p36.yes_repayment_interest as yes_repayment_interest, "
                + "p39.yes_repayment_interest AS flow_yes_repayment_interest";
        String limitSql = " limit ?,?";
        StringBuffer sb = new StringBuffer(selectSql);
        sb.append(accountReconciliationSql);
        if (!StringUtils.isBlank(param.getUsername())) {
            sb.append(" and p1.username=" + "'" + param.getUsername() + "'");
        }
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        if (!StringUtils.isBlank(param.getRealname())) {
            sb.append(" and p1.realname=" + "'" + param.getRealname() + "'");
        }
        String querySql = sb.append(limitSql).toString();
        logger.debug("getAccountReconciliationSql():" + querySql);
        List list = getJdbcTemplate().query(querySql, new Object[] { start, end }, 
                getBeanMapper(AccountReconciliationModel.class));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        return list;
    }
    
    /**
     * 资金对账列表导出
     * 
     * @param param param
     * @return list
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<AccountReconciliationModel> getAccountReconciliationList(SearchParam param) {
        String selectSql = "SELECT p1.username,p1.realname,p2.*,p4.recharge_money,p6.log_recharge_money,"
                + "p8.up_recharge_money,p10.down_recharge_money,p12.houtai_recharge_money,p14.allcollection,"
                + "p16.cash_money,p19.invest_award,p21.invest_yeswait_interest,p21.wait_interest,p22.borrow_award,"
                + "p23.borrow_fee,p24.manage_fee,(p25.system_fee+p16.cash_fee) as system_fee,p26.invite_money,"
                + "p27.vip_money,p30.repayment_interest as repayment_interest,"
                + "p33.repayment_interest AS flow_repayment_interest,p30.repayment_principal as repayment_principal,"
                + "p33.repayment_principal AS flow_repayment_principal,p36.yes_repayment_interest as yes_repayment_interest, "
                + "p39.yes_repayment_interest AS flow_yes_repayment_interest";
        StringBuffer sb = new StringBuffer(selectSql);
        sb.append(accountReconciliationSql);
        if (!StringUtils.isBlank(param.getUsername())) {
            sb.append(" and p1.username=" + "'" + param.getUsername() + "'");
        }
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        if (!StringUtils.isBlank(param.getRealname())) {
            sb.append(" and p1.realname=" + "'" + param.getRealname() + "'");
        }
        String querySql = sb.toString();
        logger.debug("getAccountReconciliationSql():" + querySql);
        List list = getJdbcTemplate().query(querySql, getBeanMapper(AccountReconciliationModel.class));
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        return list;
    }
    
    // 资金对账查询公共sql
    String selectSql = "SELECT p1.username,p2.*,p4.recharge_money,p6.log_recharge_money,p8.up_recharge_money," +
            "p10.down_recharge_money,p12.houtai_recharge_money,p14.allcollection,p16.cash_money,p19.invest_award," +
            "p21.invest_yeswait_interest,p21.wait_interest,p22.borrow_award,p23.borrow_fee,p24.manage_fee," +
            "(p25.system_fee+p16.cash_fee) as system_fee,p26.invite_money,p27.vip_money,p30.repayment_interest as repayment_interest," +
            "p33.repayment_interest AS flow_repayment_interest,p30.repayment_principal as repayment_principal," +
            "p33.repayment_principal AS flow_repayment_principal,p36.yes_repayment_interest as yes_repayment_interest, " +
            "p39.yes_repayment_interest AS flow_yes_repayment_interest";
    // v1.6.5.3 RDPROJECT-175 zza 2013-09-18 end
    
    @Override
    public List getHongBaoList(SearchParam param) {
        String selectSql="select p.*,p1.username,p4.name as typename,p5.hongbao as hongbao from dw_hongbao as p left join dw_user as p1 on p.user_id=p1.user_id left join dw_linkage as p4  on p.type=p4.value left join dw_account as p5 on p1.user_id=p5.user_id where 1=1 ";
//      String searchSql=param.getSearchParamSql();
        StringBuffer sb=new StringBuffer(selectSql);
        if(!StringUtils.isBlank(param.getUsername())){
            sb.append(" and p1.username="+"'"+param.getUsername()+"'");
        }
        String orderSql=" order by p.addtime desc ";
        sb.append(orderSql);
        String querySql=sb.toString();
        logger.debug("getUserAcount():"+querySql);
        List list=getJdbcTemplate().query(querySql, new Object[]{}, new RowMapper<HongBaoModel>(){
            @Override
            public HongBaoModel mapRow(ResultSet rs, int num)
                    throws SQLException {
                HongBaoModel s=new HongBaoModel();
                s.setId(rs.getLong("id"));
                s.setAddtime(rs.getString("addtime"));
                s.setRemark(rs.getString("remark"));
                s.setHongbao_money(rs.getDouble("hongbao_money"));
                s.setUsername(rs.getString("username"));
                s.setTypename(rs.getString("typename"));
                s.setHongbao(rs.getString("hongbao"));
                return s;
            }
        });
        return list;
    }
    
    @Override
    public void addFreeCash(double free_cash,long user_id) {
        String sql = "select id from dw_account_cash where user_id=? order by id desc limit 1";
        int id = 0;
        SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
                new Object[] { user_id });
        if (rs.next()) {
            id = rs.getInt("id");
        }
        String sql2 = "update dw_account_cash set free_cash=? " +
                "where id="+id;
        this.getJdbcTemplate().update(sql2, free_cash);
    }

    @Override
    public double getFreeCash(long user_id) {
        String sql = "select free_cash from dw_account_cash where user_id=? order by id desc limit 1";
        logger.info("SQL:" + sql);
        logger.info("SQL:" + user_id);
        double free_cash = 0;
        SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
                new Object[] { user_id });
        if (rs.next()) {
            free_cash = rs.getDouble("free_cash");
        }
        return free_cash;
    }

    @Override
    public double getFlowDayTenderCollection(long user_id, int day) {
        //String sql="SELECT SUM(p2.wait_account) AS num FROM dw_borrow_tender  AS p2  LEFT JOIN dw_borrow AS p3 ON p2.borrow_id=p3.id WHERE (p3.is_flow=1) and p2.user_id=? AND p3.isday=1  and p3.time_limit_day=? ";
        String sql="SELECT SUM(p2.wait_account) AS num FROM dw_borrow_tender  AS p2  LEFT JOIN dw_borrow AS p3 ON p2.borrow_id=p3.id WHERE (p3.type="+Constant.TYPE_FLOW+") and p2.user_id=? AND p3.isday=1  and p3.time_limit_day=? ";
        double sum=0.0;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id,day});
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }

    @Override
    public double getFreeCashSum(long user_id) {
        String sql="select sum(p1.free_cash) as num from dw_account_cash as p1 where p1.status in (0,1) and p1.user_id=? ";
        double total=0;
        total=this.sum(sql, new Object[]{user_id});
        return total;
    }
    @Override
    public int getAccountBankCount(long user_id) {
        String sql = "select count(*) from dw_account_bank where user_id=?";
        logger.debug("SQL:" + sql);
        int total = 0;
        total = this.getJdbcTemplate().queryForInt(sql,new Object[]{user_id});
        return total;
    }
    
    //V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 start
    /**
     * 获取列表
     * 
     * @param start start
     * @param end end
     * @param param param
     * @return list
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getKefuUserInvestList(SearchParam param, int start, int end) {
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String selectSql = "SELECT p1.*,d.kefu_username,p2.`username`,p2.`realname`,p2.`addtime` as regiter_time ,"
                + "d.`vip_verify_time` FROM dw_borrow_tender p1 " + "LEFT JOIN dw_borrow b ON p1.`borrow_id`=b.`id` "
                + "LEFT JOIN dw_user p2 ON p1.user_id=p2.`user_id` "
                + "LEFT JOIN dw_user_cache d ON p1.`user_id`=d.`user_id`"
                + "left join dw_user kf on kf.user_id=d.kefu_userid where 1=1 ";
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        String regiteSql = getRegiterTimeRule();
        String borrowSql = getBorrowTypeRule();
        StringBuffer sb = new StringBuffer();
        String orderSql = " order by p1.addtime desc ";
        String limitSql = "limit ?,?";

        String searchSql = param.getSearchParamSql();
        sb.append(selectSql).append(regiteSql).append(searchSql).append(borrowSql).append(orderSql).append(limitSql);
        String sql = sb.toString();
        logger.debug("List():" + sql);
        List list = new ArrayList();
        list = this.getJdbcTemplate().query(sql, new Object[] { start, end }, getBeanMapper(KefuAndUserInvest.class));
        return list;
    } 
    
    /**
     * 获取列表
     * 
     * @param param param
     * @return list
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getKefuUserInvestList(SearchParam param) {
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String selectSql = "SELECT p1.*,d.kefu_username,p2.`username`,p2.`realname`,p2.`addtime` as regiter_time ,"
                + "d.`vip_verify_time` FROM dw_borrow_tender p1 " + "LEFT JOIN dw_borrow b ON p1.`borrow_id`=b.`id` "
                + "LEFT JOIN dw_user p2 ON p1.user_id=p2.`user_id` "
                + "LEFT JOIN dw_user_cache d ON p1.`user_id`=d.`user_id` "
                + "left join dw_user kf on kf.user_id=d.kefu_userid where 1=1 ";
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        String regiteSql = getRegiterTimeRule();
        String borrowSql = getBorrowTypeRule();
        StringBuffer sb = new StringBuffer();
        String orderSql = " order by p1.addtime desc ";
        String searchSql = param.getSearchParamSql();
        sb.append(selectSql).append(regiteSql).append(searchSql).append(borrowSql).append(orderSql);
        String sql = sb.toString();
        logger.debug("List():" + sql);
        List list = new ArrayList();
        list = this.getJdbcTemplate().query(sql, new Object[] {}, getBeanMapper(KefuAndUserInvest.class));
        return list;
    } 
    @Override
    public int getKefuUserInvestCount(SearchParam param){
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String selectSql = "SELECT count(1) FROM dw_borrow_tender p1"
                + " LEFT JOIN dw_borrow b ON p1.`borrow_id`=b.`id` "
                + "LEFT JOIN dw_user p2 ON p1.user_id=p2.`user_id` "
                + "LEFT JOIN dw_user_cache d ON p1.`user_id`=d.`user_id`"
                + "left join dw_user kf on kf.user_id=d.kefu_userid where 1=1 ";
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        String regiteSql = getRegiterTimeRule();
        String borrowSql = getBorrowTypeRule();
        StringBuffer sb = new StringBuffer();

        String searchSql = param.getSearchParamSql();
        sb.append(selectSql).append(regiteSql).append(searchSql).append(borrowSql);
        String sql = sb.toString();
        logger.debug("count():" + sql);
        int total = 0;
        total = count(sql);
        return total;
    } 
    @Override
    public double getKefuUserInvestSum(SearchParam param){
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String selectSql = "SELECT sum(p1.account) as num FROM dw_borrow_tender p1"
                + " LEFT JOIN dw_borrow b ON p1.`borrow_id`=b.`id` "
                + "LEFT JOIN dw_user p2 ON p1.user_id=p2.`user_id` "
                + "LEFT JOIN dw_user_cache d ON p1.`user_id`=d.`user_id`"
                + "left join dw_user kf on kf.user_id=d.kefu_userid where 1=1 ";
        // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
        String regiteSql = getRegiterTimeRule();
        String borrowSql = getBorrowTypeRule();
        StringBuffer sb = new StringBuffer();
        String searchSql = param.getSearchParamSql();
        sb.append(selectSql).append(regiteSql).append(searchSql).append(borrowSql);
        String sql = sb.toString();
        logger.debug("count():" + sql);
        double total = 0;
        total = this.sum(sql, new Object[] {});
        return total;
    } 
    
    private String getBorrowTypeRule(){
        String borrowSql = "";
        Rule rule = Global.getRule(EnumRuleNid.KEFU_USER_INVEST.getValue());
        if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
            RuleModel newRule = new RuleModel(rule);
            // 标种规则
            RuleModel borrowTypeRule = newRule.getRuleByKey("borrow_type");
            borrowSql = getBorrowTypeSql(borrowTypeRule);
        }
        return borrowSql;
    }
    
    private String getRegiterTimeRule(){
        String regiteSql = "";
        Rule rule = Global.getRule(EnumRuleNid.KEFU_USER_INVEST.getValue());
        if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
            RuleModel newRule = new RuleModel(rule);
            // 注册时间段配置
            RuleModel regiteTimeRule = newRule.getRuleByKey("regite_time");
            String start_time = regiteTimeRule.getValueStrByKey("start_time");
            String end_time = regiteTimeRule.getValueStrByKey("end_time");
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 start
            regiteSql = " and p2.addtime>=" + start_time + " and p2.addtime<=" + end_time + " ";
            // v1.6.7.1 RDPROJECT-402 zza 2013-11-07 end
        }
        return regiteSql;
    }
    private String getBorrowTypeSql(RuleModel rule){
        String typeSql="";
        int flow=rule.getValueIntByKey("is_flow");
        int mb=rule.getValueIntByKey("is_mb");
        int xin=rule.getValueIntByKey("is_xin");
        int fast=rule.getValueIntByKey("is_fast");
        int jin=rule.getValueIntByKey("is_jin");
        int pledge=rule.getValueIntByKey("is_pledge");
        int offvouch=rule.getValueIntByKey("is_offvouch");
        /*if(flow==1){
            typeSql+=" or b.is_flow=1 ";
        }
        if(mb==1){
            typeSql+=" or b.is_mb=1 ";

        }
        if(xin==1){
            typeSql+=" or b.is_xin=1 ";

        }
        if(fast==1){
            typeSql+=" or b.is_fast=1 ";

        }
        if(jin==1){
            typeSql+=" or b.is_jin=1 ";

        }
        if(pledge==1){
            typeSql+=" or b.is_pledge=1 ";

        }
        if(offvouch==1){
            typeSql+=" or b.is_offvouch=1 ";

        }*/
        
        if(flow==1){
            typeSql+=" or b.type="+Constant.TYPE_FLOW;
        }
        if(mb==1){
            typeSql+=" or b.type="+Constant.TYPE_SECOND;

        }
        if(xin==1){
            typeSql+=" or b.type="+Constant.TYPE_CREDIT;

        }
        if(fast==1){
            typeSql+=" or b.type="+Constant.TYPE_MORTGAGE;

        }
        if(jin==1){
            typeSql+=" or b.type="+Constant.TYPE_PROPERTY;

        }
        if(pledge==1){
            typeSql+=" or b.type="+Constant.TYPE_PLEDGE;

        }
        if(offvouch==1){
            typeSql+=" or b.type="+Constant.TYPE_OFFVOUCH;

        }
        String borrowTypeSql=" and (1!=1 "+typeSql+")";
        return borrowTypeSql;
    }
    //V1.6.6.1 RDPROJECT-171 wcw 2013-10-08 end
    @Override
    public PaymentInterface getPaymentInterface(String interface_value) {
        String selectSql="select * from dw_payment_interface where interface_value=?";
        StringBuffer sb=new StringBuffer(selectSql);
        String querySql=sb.toString();
        logger.debug("getPaymentInterfacesql():"+querySql);
        PaymentInterface paymentInterface=null;
        try {
             paymentInterface=getJdbcTemplate().queryForObject(querySql, new Object[]{interface_value}, getBeanMapper(PaymentInterface.class));

        } catch (Exception e) {
            // TODO: handle exception
        }
        return paymentInterface;
    }

    // v1.6.7.1 RDPROJECT-434 zza 2013-11-13 start
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public AccountSumModel getAccountSum(SearchParam param) {
        String selectSql = "select sum(p1.total) as total, sum(p1.use_money) as use_money, "
                + "sum(p1.no_use_money) as no_use_money, sum(p1.collection) as collection, "
                + "sum(repay.wait_repay) as wait_repay ";
        String searchSql = param.getSearchParamSql();
        StringBuffer sb = new StringBuffer(selectSql);
        String sql = sb.append(getUserAcountSql).append(searchSql).toString();
        logger.debug("getAccountSum():" + sql);
        return this.getJdbcTemplate().queryForObject(sql, getBeanMapper(AccountSumModel.class));
    }
    // v1.6.7.1 RDPROJECT-434 zza 2013-11-13 end
    
}
