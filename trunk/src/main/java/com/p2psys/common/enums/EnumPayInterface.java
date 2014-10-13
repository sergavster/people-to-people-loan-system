package com.p2psys.common.enums;

/**
 *  银行
 */
public enum EnumPayInterface {
    //----第三方支付接口
    GOPAY("gopay"), // 国付宝
    
    IPS_PAY("ips_pay"), // 环迅
    
    XS_PAY("xs_pay"), // 新生支付
    
    YB_PAY("yb_pay"), // 易宝支付
    
    BAOFOO_PAY("baofoo_pay"),//宝付支付
    
    CHINABANK_PAY("chinabank_pay"), //网银在线
    
    ALLIN_PAY("allin_pay"),// 通联支付
    
    SHENG_PAY("sheng_pay"),//盛付通
    
    DINPAY("dinpay"), //智付
    
    ECPSSPAY("ecpsspay"), //汇潮
    
    ECPSSCHINA_PAY("ecpsschina_pay"), //汇潮中行积分通道
    
    TENPAY("tenpay"),//财付通
    
    RONGBAO_PAY("rongbao_pay"),//融宝支付
    
    ECPSSFAST_PAY("ecpssfast_pay"),//汇潮快捷支付（专门用于非网银用户使用）
    
    YISHENG_PAY("yisheng_pay"), //易生支付
    
    EPAY("epay"),//双潜支付
    //v1.6.7.1 RDPROJECT-417 wcw  2013-11-04 start
    EPAYLINKS("epaylinks"),//易票联支付
    //v1.6.7.1 RDPROJECT-417 wcw  2013-11-04 end
    //v1.6.7.1 RDPROJECT-410 wcw  2013-11-07 start
    UNSPAY("unspay"),//银生支付
    //v1.6.7.1 RDPROJECT-410 wcw  2013-11-07 end
    //其他
    OFFLINE_RECHARGE("offline_recharge"),  //线下充值
    //v1.6.7.2 RDPROJECT-512 wcw 2013-12-06 start
    //v1.6.7.2 RDPROJECT-538 wcw 2013-12-06 end
    BACK("back"),  //后台扣款
    //v1.6.7.2 RDPROJECT-538 wcw 2013-12-03 start
    EBATONG_PAY("ebatong_pay"),
    //v1.6.7.2 RDPROJECT-512 wcw 2013-12-03 end
    BACK_RECHARGE("back_recharge");//后台充值
    
    EnumPayInterface(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return this.value;
    }

    public boolean equals(String value) {
        if (value != null && this.value.equals(value)) {
            return true;
        }
        return false;
    }
    
}
