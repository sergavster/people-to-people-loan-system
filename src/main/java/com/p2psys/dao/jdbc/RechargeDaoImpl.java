package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.common.enums.EnumPayInterface;
import com.p2psys.context.Global;
import com.p2psys.dao.RechargeDao;
import com.p2psys.domain.AccountRecharge;
import com.p2psys.model.PaymentSumModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;

public class RechargeDaoImpl extends BaseDaoImpl implements RechargeDao {
    private Logger logger =Logger.getLogger(RechargeDaoImpl.class);
    @Override
    public List getList(long user_id) {
        String sql="select p1.*,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  " +
                "from dw_account_recharge as p1 " +
                "left join dw_user as p2 on p1.user_id=p2.user_id " +
                "left join dw_payment as p3 on p1.payment=p3.nid " +
                "where p1.user_id=?";
//      List list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, new AccountRechargeMapper());
        List list=this.getJdbcTemplate().query(sql, new Object[]{user_id}, getBeanMapper(AccountRecharge.class));
        return list;
    }
    
    String queryRechargeSql=" from dw_account_recharge as p1 " +
            "left join dw_user as p2 on p1.user_id=p2.user_id " +
            "left join dw_user as verify_kefu on p1.verify_userid=verify_kefu.user_id " +
            "left join dw_payment as p3 on p1.payment=p3.nid where 1=1 ";
    String newqueryRechargeSql=" from dw_account_recharge as p1 " +
            "left join dw_user as p2 on p1.user_id=p2.user_id " +
            "left join dw_user as recharge_kefu on p1.recharge_kefuid=recharge_kefu.user_id " +
            "left join dw_user as verify_kefu on p1.verify_userid=verify_kefu.user_id " +
            "left join dw_payment as p3 on p1.payment=p3.nid where 1=1 ";
    String queryCashSql=" from dw_account_cash as p1 " +
            "left join dw_user as p2 on p1.user_id=p2.user_id " +
            " where 1=1 ";
    String queryTenderInterestSql=" from dw_borrow_tender as p1 " +
            "left join dw_user as p2 on p1.user_id=p2.user_id " +
            "left join dw_borrow as p3 on p1.borrow_id=p3.id " +
            "where 1=1 and p3.status in (1,3,6,7,8) ";
    
    @Override
    public int getCount(long user_id,SearchParam param) {
        StringBuffer sb=new StringBuffer("select count(p1.id) ");
        sb.append(queryRechargeSql).append(" and p1.user_id=? ").append(param.getSearchParamSql());
        String sql=sb.toString();
        logger.debug("getCount():"+sql);
        int count=0;
        count=count(sql, new Object[]{user_id});
        return count;
    }

    @Override
    public List getList(long user_id, int start,int end,SearchParam param) {
        StringBuffer sb=new StringBuffer();
        if(Global.getWebid().equals("jsy")){
            sb=new StringBuffer("select p1.*,recharge_kefu.username as recharge_kefu_username,verify_kefu.username as verify_username,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
            String orderSql=" order by p1.addtime desc ";
            String limitSql="limit ?,?";
            String searchSql=param.getSearchParamSql();
            sb.append(newqueryRechargeSql).append(" and p1.user_id=? ").append(searchSql).append(orderSql).append(limitSql);
        }else{
            sb=new StringBuffer("select p1.*,verify_kefu.username as verify_username,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
            String orderSql=" order by p1.addtime desc ";
            String limitSql="limit ?,?";
            String searchSql=param.getSearchParamSql();
            sb.append(queryRechargeSql).append(" and p1.user_id=? ").append(searchSql).append(orderSql).append(limitSql);
        }
        String sql=sb.toString();
        logger.debug("getList():"+sql);
        //System.out.println("================"+sql);
        List list=new ArrayList();
//      list=this.getJdbcTemplate().query(sql, new Object[]{user_id,start,end},new AccountRechargeMapper());
        list=this.getJdbcTemplate().query(sql, new Object[]{user_id,start,end}, getBeanMapper(AccountRecharge.class));
        return list;
    }
    
    @Override
    public void addRecharge(AccountRecharge r) {
        if(Global.getWebid().equals("jsy")){
            String sql="insert into dw_account_recharge(trade_no,user_id,status,money,payment,`return`," +
                    "type,remark,fee,verify_userid,verify_time,verify_remark,addtime,addip,recharge_kefuid) values(" +
                    "?,?,?,?,?,?" +
                    ",?,?,?,?,?,?,?,?,?)";
            this.getJdbcTemplate().update(sql, r.getTrade_no(),r.getUser_id(),r.getStatus(),r.getMoney(),r.getPayment(),r.getReturntext(),
                    r.getType(),r.getRemark(),r.getFee(),r.getVerify_userid(),r.getVerify_time(),r.getVerify_remark(),
                    r.getAddtime(),r.getAddip(),r.getRecharge_kefuid());
        }else{
            String sql="insert into dw_account_recharge(trade_no,user_id,status,money,payment,`return`," +
                    "type,remark,fee,verify_userid,verify_time,verify_remark,addtime,addip) values(" +
                    "?,?,?,?,?,?" +
                    ",?,?,?,?,?,?,?,?)";
            this.getJdbcTemplate().update(sql, r.getTrade_no(),r.getUser_id(),r.getStatus(),r.getMoney(),r.getPayment(),r.getReturntext(),
                    r.getType(),r.getRemark(),r.getFee(),r.getVerify_userid(),r.getVerify_time(),r.getVerify_remark(),
                    r.getAddtime(),r.getAddip());
            }
    }
    
    @Override
    public void addExcelRecharge(List<AccountRecharge> list) {
        String sql = "insert into dw_account_recharge(trade_no,user_id,status,money,payment,`return`," +
                "type,remark,fee,verify_userid,verify_time,verify_remark,addtime,addip) values(" +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<Object[]> listo = new ArrayList<Object[]>(); 
        if (list.size() > 0) {
            for (AccountRecharge r : list) {  
                Object[] args = {r.getTrade_no(), r.getUser_id(), r.getStatus(), 
                        r.getMoney(), r.getPayment(), r.getReturntext(), r.getType(), 
                        r.getRemark(), r.getFee(), r.getVerify_userid(), 
                        r.getVerify_time(), r.getVerify_remark(),
                        r.getAddtime(), r.getAddip()};  
                listo.add(args);  
            }  
            this.getJdbcTemplate().batchUpdate(sql, listo);
        }
    }
    
    @Override
    public AccountRecharge getRechargeByTradeno(String trade_no) {
        String sql="select * from dw_account_recharge where trade_no=?";
        AccountRecharge r=null;
        try {
            r=this.getJdbcTemplate().queryForObject(sql, new Object[]{trade_no}, getBeanMapper(AccountRecharge.class));
        } catch (DataAccessException e) {
            logger.debug(e.getMessage());
        }
        return r;
    }

    @Override
    public int updateRecharge(int status,String returnText,String trade_no) {
        // 2013.10.22  高才  【新生】可能发生充值记录由成功变为失败，增加状态限制 start
        String sql="update dw_account_recharge set status=?,`return`=?,verify_time=? where trade_no=? and status = 0 ";
        //String sql="update dw_account_recharge set status=?,`return`=?,verify_time=? where trade_no=? ";
        // 2013.10.22  高才  【新生】可能发生充值记录由成功变为失败，增加状态限制 end
        int count=0;
        try {
            count=getJdbcTemplate().update(sql, new Object[]{status,returnText,DateUtils.getNowTimeStr(),trade_no});
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return count;
    }
    @Override
    public int updateRechargeByStatus(int status, String returnText,
            String trade_no) {
        String sql="update dw_account_recharge set status=?,`return`=?,verify_time=? where trade_no=? and status=0 ";
        int count=0;
        try {
            count=getJdbcTemplate().update(sql, new Object[]{status,returnText,DateUtils.getNowTimeStr(),trade_no});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public int getAllCount(SearchParam param) {
        StringBuffer sb=new StringBuffer("select count(p1.id) ");
        // sql性能优化 start
//      if(Global.getWebid().equals("jsy")){
//            sb.append(newqueryRechargeSql).append(param.getSearchParamSql());
//      }else{
//           sb.append(queryRechargeSql).append(param.getSearchParamSql());
//      }
        sb.append(" FROM dw_account_recharge as p1 ");
        sb.append(" LEFT JOIN dw_user as p2 ON p1.user_id=p2.user_id WHERE 1=1");
        sb.append(param.getSearchParamSql());
        // sql性能优化 end
        String sql=sb.toString();
        logger.debug("countsql======"+sql);
        int count=0;
        count=count(sql);
        return count;
    }

    @Override
    public List getAllList(int start, int end, SearchParam param) {
        StringBuffer sb=new StringBuffer();
        if(Global.getWebid().equals("jsy")){
            sb=new StringBuffer("select p1.*,recharge_kefu.username as recharge_kefu_username,verify_kefu.username as verify_username,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
        }else{
            sb=new StringBuffer("select p1.*,p1.money,verify_kefu.username as verify_username,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
        }
        String orderSql=" order by p1.addtime desc ";
        String limitSql="limit ?,?";
        String searchSql=param.getSearchParamSql();
        if(Global.getWebid().equals("jsy")){
            sb.append(newqueryRechargeSql).append(searchSql).append(orderSql).append(limitSql);
        }else{
            sb.append(queryRechargeSql).append(searchSql).append(orderSql).append(limitSql);

        }
        String sql=sb.toString();
        logger.debug("RechargeDao.getList():"+sql);
        List list=new ArrayList();
//      list=this.getJdbcTemplate().query(sql, new Object[]{start,end},new AccountRechargeMapper());
        list=this.getJdbcTemplate().query(sql, new Object[]{start,end}, getBeanMapper(AccountRecharge.class));
        return list;
    }
    
    @Override
    public List getAllList(SearchParam param) {
        StringBuffer sb=new StringBuffer();
        if(Global.getWebid().equals("jsy")){
            sb=new StringBuffer("select p1.*,recharge_kefu.username as recharge_kefu_username,verify_kefu.username as verify_username,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
        }else{
            sb=new StringBuffer("select p1.*,verify_kefu.username as verify_username,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
        }
        String orderSql=" order by p1.addtime desc ";
        String searchSql=param.getSearchParamSql();
        if(Global.getWebid().equals("jsy")){
            sb.append(newqueryRechargeSql).append(searchSql).append(orderSql);
        }else{
            sb.append(queryRechargeSql).append(searchSql).append(orderSql);

        }
        String sql=sb.toString();
        logger.debug("RechargeDao.getList():"+sql);
        List list=new ArrayList();
//      list=this.getJdbcTemplate().query(sql,new AccountRechargeMapper());
        list=this.getJdbcTemplate().query(sql, getBeanMapper(AccountRecharge.class));
        return list;
    }

    @Override
    public AccountRecharge getRecharge(long id) {
        StringBuffer sb=new StringBuffer();
        if(Global.getWebid().equals("jsy")){
             sb=new StringBuffer("select p1.*,recharge_kefu.username as recharge_kefu_username,verify_kefu.username as verify_username,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
            sb.append(newqueryRechargeSql).append(" and p1.id=? ");
        }else{
             sb=new StringBuffer("select p1.*,verify_kefu.username as verify_username,p1.money,p1.money-p1.fee as total,p2.username,p2.realname,p3.name as paymentname  ");
            sb.append(queryRechargeSql).append(" and p1.id=? ");
        }
        
        String sql=sb.toString();
        AccountRecharge r=null;
//      r=this.getJdbcTemplate().queryForObject(sql, new Object[]{id},new AccountRechargeMapper());
        r=this.getJdbcTemplate().queryForObject(sql, new Object[]{id}, getBeanMapper(AccountRecharge.class));
        return r;
    }
    @Override
    public void updateRecharge(AccountRecharge r) {
        String sql="update dw_account_recharge set status=?,`return`=?,verify_userid=?,verify_time=?,verify_remark=? where id=?";
        this.getJdbcTemplate().update(sql, r.getStatus(), r.getReturntext(),r.getVerify_userid(),r.getVerify_time(),r.getVerify_remark(),r.getId());
    }
    @Override
    public void updateRechargeFee(double fee,long id) {
        String sql="update dw_account_recharge set fee=? where id=?";
        this.getJdbcTemplate().update(sql,fee,id);
    }

    @Override
    public double getLastRechargeSum(long user_id) {
        Date d=Calendar.getInstance().getTime();
        d=DateUtils.rollDay(d, -15);
        String sql="select sum(money) as num from dw_account_recharge where user_id = ? and status=1 and verify_time>"+d.getTime()/1000;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
        double sum=0;
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }
    
    public double getLastRechargeSum(long user_id,int day) {
        Date d=Calendar.getInstance().getTime();
        d=DateUtils.rollDay(d, -day);
        String sql="select sum(money) as num from dw_account_recharge where user_id = ? and status=1 and verify_time>"+d.getTime()/1000;
        logger.debug("sql========="+sql);
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
        double sum=0;
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }
    
    @Override
    public double getLastRechargeSum(long user_id, int type, long start_time,long end_time) {
        
        if(user_id <= 0){
            logger.error("user_id is null.");
            return 0;
        }
        
        StringBuffer sql= new StringBuffer("select sum(money) as num from dw_account_recharge where user_id = ? and status=1 ");
        
        // 查询数据数据封装 and verify_time>?
        List<Object> list = new ArrayList<Object>();
        list.add(user_id);
        
        //充值类型
        if(type > 0){
            list.add(type);
            sql.append(" and type = ? ");
        }
        
        //充值成功开始时间
        if(start_time >= 0){
            list.add(start_time);
            sql.append("  and verify_time >= ? ");
        }
        
        //充值成功结束时间
        if(end_time > 0){
            list.add(end_time);
            sql.append("  and verify_time <= ? ");
        }
        
        // 新建obj数组，用于JDBC查询，数组程度为list size
        Object[] obj = new Object[list.size()]; 
        // 遍历list，将值存入obj数组中
        for(int i = 0 ; i < list.size() ; i++){
            obj[i] = list.get(i);
        }
        
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql.toString(),obj);
        double sum=0;
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }

    public double getLastOnlineRechargeSum(long user_id) {
        Date d=Calendar.getInstance().getTime();
        d=DateUtils.rollDay(d, -15);
        String sql="select sum(money) as num from dw_account_recharge where user_id = ? and status=1 and type=1 and verify_time>"+d.getTime()/1000;
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
        double sum=0;
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }
    
    @Override
    public List getLastOfflineRechargeList(long user_id) {
        Date d=Calendar.getInstance().getTime();
        d=DateUtils.rollDay(d, -15);
        String sql="select * from dw_account_recharge r " +
                "left join dw_payment p on p.nid=r.payment " +
                "where p.nid='offline' and r.status=1 and r.user_id=? and r.verify_time>?";
        List list=new ArrayList();
        try {
            list=getJdbcTemplate().query(sql, new Object[]{user_id,d.getTime()/1000}, getBeanMapper(AccountRecharge.class));

        } catch (DataAccessException e) {
        }
        
        return list;
    }
    

    @Override
    public double getAccount_sum(SearchParam param, int ids) {
        StringBuffer sb = new StringBuffer("select sum(p1.money) as sum");
        String searchSql = param.getSearchParamSql();
        String sql = "";
        if (ids == 1) {
            // SQL性能优化 start
//          if (Global.getWebid().equals("jsy")) {
//              sb.append(newqueryRechargeSql).append(searchSql);
//          } else {
//              sb.append(queryRechargeSql).append(searchSql);
//          }
            sb.append(" FROM dw_account_recharge as p1 ");
            sb.append(" LEFT JOIN dw_user as p2 ON p1.user_id=p2.user_id WHERE 1=1");
            sb.append(searchSql);
            // SQL性能优化 end
            sql = sb.toString();
        } else if (ids == 2) {
            StringBuffer newsb = new StringBuffer("select sum(p1.total) as sum");
            newsb.append(queryCashSql).append(searchSql);
            sql = newsb.toString();
        } else if (ids == 3) {
            StringBuffer newsb = new StringBuffer("select sum(p1.fee) as sum");
            newsb.append(queryCashSql).append(searchSql);
            sql = newsb.toString();
        } else if (ids == 4) {
            StringBuffer newsb = new StringBuffer("select sum(p1.credited) as sum");
            newsb.append(queryCashSql).append(searchSql);
            sql = newsb.toString();
        } else if (ids == 5) {
            StringBuffer newsb = new StringBuffer("select sum(p1.interest) as sum");
            newsb.append(queryTenderInterestSql).append(searchSql);
            sql = newsb.toString();
            // v1.6.6.1 RDPROJECT-265 zza 2013-10-08 start
        } else if (ids == 6) {
            StringBuffer newsb = new StringBuffer("select sum(p1.money) as sum");
            newsb.append(queryTenderInterestSql).append(searchSql);
            sql = newsb.toString();
        } else if (ids == 7) {
            StringBuffer newsb = new StringBuffer("select sum(p1.account) as sum");
            newsb.append(queryTenderInterestSql).append(searchSql);
            sql = newsb.toString();
        } else if (ids == 8) {
            StringBuffer newsb = new StringBuffer("select sum(p1.repayment_account) as sum");
            newsb.append(queryTenderInterestSql).append(searchSql);
            sql = newsb.toString();
        }
        // v1.6.6.1 RDPROJECT-265 zza 2013-10-08 end

        logger.debug("RechargeDao.getAccount_sum:"+sql);    
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql);
        double sum=0;
        if(rs.next()){
            sum=rs.getDouble("sum");
        }
        return sum;
    }

    @Override
    public double getRechargeSumWithNoAdmin(long user_id, int day) {
        Date d=Calendar.getInstance().getTime();
        d=DateUtils.rollDay(d, -day);
        String sql="select sum(money) as num from dw_account_recharge where user_id = ? and status=1 and payment!="+EnumPayInterface.BACK_RECHARGE.getValue()+" and verify_time>"+d.getTime()/1000;
        logger.debug("sql========="+sql);
        SqlRowSet rs=this.getJdbcTemplate().queryForRowSet(sql,new Object[]{user_id});
        double sum=0;
        if(rs.next()){
            sum=rs.getDouble("num");
        }
        return sum;
    }
    //获取用户第一次充值成功的有效充值金额
    @Override
    public AccountRecharge getMinRecharge(long user_id,String status) {
        StringBuffer sb=new StringBuffer();
        sb=new StringBuffer("SELECT * FROM dw_account_recharge WHERE id IN (SELECT  MIN(id) FROM dw_account_recharge WHERE STATUS=? AND user_id=?) AND STATUS=? AND user_id=? ");
        String sql=sb.toString();
        AccountRecharge r=null;
//      r=this.getJdbcTemplate().queryForObject(sql, new Object[]{status,user_id,status,user_id},new AccountMinInviteRechargeMapper());
        r=this.getJdbcTemplate().queryForObject(sql, new Object[]{status,user_id,status,user_id}, getBeanMapper(AccountRecharge.class));
        return r;
    }
    @Override
    public void updateAccountRechargeYes_no(AccountRecharge accountRecharge){
        String sql="update dw_account_recharge set yes_no=? where id=?";
        this.getJdbcTemplate().update(sql,accountRecharge.getYes_no(),accountRecharge.getId());
    }
   @Override
    public double getTodayOnlineRechargeTotal(long user_id, int day) {
        Date d = Calendar.getInstance().getTime();
        d = DateUtils.rollDay(d, -day);
        String sql = "select sum(money) as num from dw_account_recharge where user_id = ? and status=1 and type=1 and verify_time>"
                + d.getTime() / 1000 +" and verify_time>"
                        + (DateUtils.getIntegralTime().getTime() / 1000)
                        + " and verify_time<"
                        + (DateUtils.getLastIntegralTime().getTime() / 1000);
        logger.debug("sql=========" + sql);
        SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
                new Object[] { user_id });
        double sum = 0;
        if (rs.next()) {
            sum = rs.getDouble("num");
        }
        return sum;
    }

    @Override
    public double getTodayRechargeTotal(long user_id, int day) {
        Date d = Calendar.getInstance().getTime();
        d = DateUtils.rollDay(d, -day);
        String sql = "select sum(money) as num from dw_account_recharge where user_id = ? and status=1 and verify_time>"
                + d.getTime() / 1000 +" and verify_time>"
                        + (DateUtils.getIntegralTime().getTime() / 1000)
                        + " and verify_time<"
                        + (DateUtils.getLastIntegralTime().getTime() / 1000);
        logger.debug("sql=========" + sql);
        SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql,
                new Object[] { user_id });
        double sum = 0;
        if (rs.next()) {
            sum = rs.getDouble("num");
        }
        return sum;
    }

    @Override
    public List getLastRechargeList(long user_id, long type, int days) {
        Date d = Calendar.getInstance().getTime();
        d = DateUtils.rollDay(d, -days);
        String sql = "select * from dw_account_recharge r where 1=1";
        if (type != 0)
            sql = (new StringBuilder(String.valueOf(sql))).append(" r.type=").append(type).toString();
        sql = (new StringBuilder(String.valueOf(sql))).append(" and r.status=1 and r.user_id=? and r.verify_time>?").toString();
        List list = new ArrayList();
        try
        {
            list = getJdbcTemplate().query(sql, new Object[] {
                Long.valueOf(user_id), Long.valueOf(d.getTime() / 1000)
            }, getBeanMapper(AccountRecharge.class));

        }
        catch (DataAccessException dataaccessexception) { }
        return list;
    }
    
    @Override
    public double getDayRechargeAccount() {
        String sql = "select sum(money) as num from dw_account_recharge where status=1 and verify_time > "
            + (DateUtils.getIntegralTime().getTime() / 1000) + " and verify_time < "
            + (DateUtils.getLastIntegralTime().getTime() / 1000);
        logger.debug("SQL语句：" + sql);
        SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
        double sum = 0;
        if (rs.next()) {
            sum = rs.getDouble("num");
        }
        return sum;
    }
    
    @Override
    public double getDayOnlineRechargeAccount() {
        String sql = "select sum(money) as num from dw_account_recharge where type = 1 and status=1 and verify_time > "
            + (DateUtils.getIntegralTime().getTime() / 1000) + " and verify_time < "
            + (DateUtils.getLastIntegralTime().getTime() / 1000);
        logger.debug("SQL语句：" + sql);
        SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
        double sum = 0;
        if (rs.next()) {
            sum = rs.getDouble("num");
        }
        return sum;
    }
    
    @Override
    public double getDayOfflineRechargeAccount() {
        String sql = "select sum(money) as num from dw_account_recharge where type = 2 and status=1 and verify_time > "
            + (DateUtils.getIntegralTime().getTime() / 1000) + " and verify_time < "
            + (DateUtils.getLastIntegralTime().getTime() / 1000);
        logger.debug("SQL语句：" + sql);
        SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
        double sum = 0;
        if (rs.next()) {
            sum = rs.getDouble("num");
        }
        return sum;
    }
    
    public List<PaymentSumModel> getDayPaymentAccount() {
        String sql = "select sum(money) as account, p.name as name from dw_account_recharge as ar " +
                "left join dw_payment as p on ar.payment = p.nid where ar.status = 1 and ar.verify_time > "
            + (DateUtils.getIntegralTime().getTime() / 1000) + " and verify_time < "
            + (DateUtils.getLastIntegralTime().getTime() / 1000) + " group by p.name;";
        logger.debug("SQL语句：" + sql);
        List<PaymentSumModel> list = new ArrayList<PaymentSumModel>();
        list = this.getJdbcTemplate().query(sql, getBeanMapper(PaymentSumModel.class));
        return list;
    }
    //v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 start
    @Override
    public List<AccountRecharge> getRechargeList(int type) {
        String sql = "select * from dw_account_recharge  where status in(0,2) and type=? and payment='baofoo_pay'";
        List<AccountRecharge> list = new ArrayList<AccountRecharge>();
        try
        {
            list = getJdbcTemplate().query(sql, new Object[] {type}, getBeanMapper(AccountRecharge.class));
        }
        catch (DataAccessException dataaccessexception) { }
        return list;
    }
    //v1.6.7.1 RDPROJECT-368 wcw 2013-11-11 end
}
