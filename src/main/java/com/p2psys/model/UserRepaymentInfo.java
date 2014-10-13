package com.p2psys.model;

/**
 * 还款信用类
 * 
 
 */
public class UserRepaymentInfo {
    private long user_id;
    private long repay_success_count; // 已还款数量
    private long repay_fail_count; // 未还款数量
    private long flow_borrow_count; // 流标数量
    private long advance_repay_count; // 提前还款数量
    private long late_repay_count; // 迟还款数量
    private long overdue_repay_count; // 30天内逾还款shul
    private long overdues_repay_count; // 超过30后还款数量
    private long overdue_unrepay_count; // 逾期未还款
    private long borrow_success_count;//借款成功

    public UserRepaymentInfo(long user_id) {
        super();
        this.user_id = user_id;
    }

    public long getRepay_success_count() {
        return repay_success_count;
    }

    public void setRepay_success_count(long repay_success_count) {
        this.repay_success_count = repay_success_count;
    }

    public long getRepay_fail_count() {
        return repay_fail_count;
    }

    public void setRepay_fail_count(long repay_fail_count) {
        this.repay_fail_count = repay_fail_count;
    }

    public long getFlow_borrow_count() {
        return flow_borrow_count;
    }

    public void setFlow_borrow_count(long flow_borrow_count) {
        this.flow_borrow_count = flow_borrow_count;
    }

    public long getAdvance_repay_count() {
        return advance_repay_count;
    }

    public void setAdvance_repay_count(long advance_repay_count) {
        this.advance_repay_count = advance_repay_count;
    }

    public long getLate_repay_count() {
        return late_repay_count;
    }

    public void setLate_repay_count(long late_repay_count) {
        this.late_repay_count = late_repay_count;
    }

    public long getOverdue_repay_count() {
        return overdue_repay_count;
    }

    public void setOverdue_repay_count(long overdue_repay_count) {
        this.overdue_repay_count = overdue_repay_count;
    }

    public long getOverdues_repay_count() {
        return overdues_repay_count;
    }

    public void setOverdues_repay_count(long overdues_repay_count) {
        this.overdues_repay_count = overdues_repay_count;
    }

    public long getOverdue_unrepay_count() {
        return overdue_unrepay_count;
    }

    public void setOverdue_unrepay_count(long overdue_unrepay_count) {
        this.overdue_unrepay_count = overdue_unrepay_count;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getBorrow_success_count() {
        return borrow_success_count;
    }

    public void setBorrow_success_count(long borrow_success_count) {
        this.borrow_success_count = borrow_success_count;
    }
    
}
