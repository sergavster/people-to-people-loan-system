package com.p2psys.payment;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.util.CharsetTypeEnum;


public class XsPay  {
    private final static Logger logger=Logger.getLogger(XsPay.class);
    
    private String version; //网关版本号
    private String url="http://www.hnapay.com/website/pay.htm"; //提交的路径
    private String serialID;  //流水号
    private String submitTime; //订单提交时间
    private String failureTime; //订单失效时间
    private String customerIP; //客户下单域名及IP  customerIP
    private String orderDetails;//订单明细信息
    private String totalAmount;//订单总金额 
    private String type;//交易类型 
    private String buyerMarked;//付款方新生账户号
    private String payType;  //付款方式         ALL：全部（默认），ACCT_RMB：账户支付，BANK_B2C：网银B2C支付，BANK_B2B：网银B2B支付
    private String orgCode;  //目标资金机构代码
    private String currencyCode;//交易币种1：人民币（默认）,2：预付卡（选择用预付费卡支付时，可选）,3：授信额度
    private String directFlag;//是否直连  0：非直连 （默认）,1：直连
    private String borrowingMarked;//资金来源借贷标识  0：无特殊要求（默认）,1：只借记,2：只贷记
    private String couponFlag; //优惠券标识 couponFlag  1：可用 （默认）,0：不可用
    private String platformID; //平台商ID
    private String returnUrl;//商户回调地址   returnUrl
    private String noticeUrl; //商户通知地址
    private String partnerID;//商户ID
    private String remark;//扩展字段
    private String charset;//编码方式
    private String signType;//编码方式  1：RSA 方式（推荐）,2：MD5 方式
    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    private String signMsg;//签名字符串
    private String orderID;  //订单id
    private String orderAmount;//订单明细金额
    private String displayName;//下单商户显示名
    private String goodsName;//商品名称
    private String goodsCount;//商品数量
    public String getOrderAmount() {
        return orderAmount;
    }
    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getGoodsName() {
        return goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsCount() {
        return goodsCount;
    }
    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }
    
    public XsPay() {
        super();
    }
    public void init(XsPay xspay,PaymentInterface paymentInterface){
        this.version="2.6";
        this.payType="ALL";
        this.currencyCode="1";
        this.borrowingMarked="0";
        this.couponFlag="0";
        this.type="1000";
        this.currencyCode="1";
        this.directFlag="0";    
        this.couponFlag="1";
        this.charset="1";
        this.platformID="";
        this.buyerMarked="";
        this.orgCode="";
        this.goodsName=Global.getValue("webname");
        Date date=new Date();
        long newdate=date.getTime();
        String stringdate=String.valueOf(newdate);
        this.serialID=stringdate;
        String weburl = Global.getValue("weburl");
        String returnurl="";
        //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
        if(paymentInterface!=null){
            returnurl = weburl + paymentInterface.getReturn_url();
            xspay.setPartnerID(paymentInterface.getMerchant_id());
            //v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 start
            xspay.setSignType(paymentInterface.getSignType());
            xspay.setUrl(paymentInterface.getGatewayUrl());
            //v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 end
        }
        //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
        logger.debug("returnurl:" + returnurl);
        xspay.setReturnUrl(returnurl);
        xspay.setNoticeUrl(returnurl);
        //this.orderID=stringdate.substring(5)+"1";
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY); 
        int minute = cal.get(Calendar.MINUTE); 
        int second = cal.get(Calendar.SECOND); 
        String x = year+"";
        String y = year+1+""; 
        if(month+1<10){
           x += "0"+(month);
           y += "0"+(month);
        }
        else
        {
            x+=month;
            y+=month;
        }
        if(day<10){
            x += "0"+day;
            y += "0"+day;
        }
        else{
            x +=day;
            y +=day;
        }
            
        if(hour<10){
            x += "0"+hour;
            y += "0"+hour;
        }
        else{
            x +=hour;
            y +=hour;
        }
            
        if(minute<10){
            x += "0"+minute;
            y += "0"+minute;
        }
        else{
            x +=minute;
            y +=minute;
        }
            
        if(second<10){
            x += "0"+second;
            y += "0"+second;
        }
        else{
            x +=second;
            y +=second;
        }
        if(x.length() == 15){
            x = x.substring(0, 14);
        }
        if(x.length() ==  15){
            y = y.substring(0, 14);
        }       
        this.submitTime=x;
        this.failureTime=y;
        String key1="";
        String[] fieldArr = new String[]{this.orderID,this.orderAmount,this.displayName,this.goodsName,this.goodsCount};        
        for(int i = 0;i<fieldArr.length;i++) {
            
            if(i==fieldArr.length-1){
                key1 += fieldArr[i];
                break;  
            }
            key1 += fieldArr[i]+ "," ;
        }
        
        this.orderDetails=key1;
        this.signMsg=this.submitGet();
    
    try {
        if("2".equals(this.signType)){
            String pkey="";
            //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
            if(paymentInterface!=null){
                pkey=paymentInterface.getKey();
            }
            //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
            signMsg = signMsg+"&pkey="+pkey;
            this.signMsg = hnapay.genSignByMD5(signMsg,CharsetTypeEnum.UTF8);
            logger.debug("signMsg========="+this.signMsg);
        }
        if("1".equals(this.signType)){
            this.signMsg = hnapay.genSignByRSA(signMsg,CharsetTypeEnum.UTF8);
        }
    } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.debug("SignMsg:"+this.signMsg);
        String _v=this.getSignVal();
        logger.debug("Sign明文:"+_v);
        
    }
    
    private  String getSignVal(){
        StringBuffer sb=new StringBuffer();
        sb.append("version=[").append(this.getVersion()).append("]");
        sb.append("serialID=[").append(this.getSerialID()).append("]");
        sb.append("submitTime=[").append(this.getSubmitTime()).append("]");
        sb.append("failureTime=[").append(this.getFailureTime()).append("]");
        sb.append("customerIP=[").append(this.getCustomerIP()).append("]");
        sb.append("orderDetails=[").append(this.getOrderDetails()).append("]");
        sb.append("totalAmount=[").append(this.getTotalAmount()).append("]");
        sb.append("type=[").append(this.getType()).append("]");
        sb.append("buyerMarked=[").append(this.getBuyerMarked()).append("]");
        sb.append("payType=[").append(this.getPayType()).append("]");
        sb.append("orgCode=[").append(this.getOrgCode()).append("]");
        sb.append("currencyCode=[").append(this.getCurrencyCode()).append("]");
        sb.append("directFlag=[").append(this.getDirectFlag()).append("]");
        sb.append("borrowingMarked=[").append(getBorrowingMarked()).append("]");
        sb.append("couponFlag=[").append(getCouponFlag()).append("]");
        sb.append("platformID=[").append(getPlatformID()).append("]");
        sb.append("returnUrl=[").append(getReturnUrl()).append("]");
        sb.append("noticeUrl=[").append(getNoticeUrl()).append("]");
        sb.append("partnerID=[").append(getPartnerID()).append("]");
        sb.append("remark=[").append(getRemark()).append("]");
        sb.append("charset=[").append(getCharset()).append("]");
        sb.append("signType=[").append(getSignType()).append("]");
        sb.append("signMsg=[").append(getSignMsg()).append("]");
        
        return sb.toString();
    }
    public String submitGet(){
        StringBuffer url=new StringBuffer();
        //url.append(this.getUrl()).append("?");
        url.append("version=").append(this.getVersion()).append("&");//网关版本号
        url.append("serialID=").append(this.getSerialID()).append("&");//字符集 1 GBK 2 UTF-8
        url.append("submitTime=").append(this.getSubmitTime()).append("&");//1 中文 2 英文
        url.append("failureTime=").append(this.getFailureTime()).append("&");//报文加密方式 1 MD5 2 SHA
        url.append("customerIP=").append(this.getCustomerIP()).append("&");//交易代码 本域指明了交易的类型，支付网关接口必须为8888
        url.append("orderDetails=").append(this.getOrderDetails()).append("&");//商户代码
        url.append("totalAmount=").append(this.getTotalAmount()).append("&");//订单号
        url.append("type=").append(this.getType()).append("&");//交易金额
        url.append("buyerMarked=").append(this.getBuyerMarked()).append("&");//
        
        url.append("payType=").append(this.getPayType()).append("&");//156，代表人民币
        url.append("orgCode=").append(this.getOrgCode()).append("&");//商户前台通知地址
        url.append("currencyCode=").append(this.getCurrencyCode()).append("&");
        
        url.append("directFlag=").append(this.getDirectFlag()).append("&");//本域为订单发起的交易时间
        url.append("borrowingMarked=").append(this.getBorrowingMarked()).append("&");//本域指卖家在国付宝平台开设的国付宝账户号
        url.append("couponFlag=").append(this.getCouponFlag()).append("&");//发起交易的客户IP地址
        url.append("platformID=").append(this.getPlatformID()).append("&");//   0不允许重复 1 允许重复 
        
        url.append("returnUrl=").append(this.getReturnUrl()).append("&");
        url.append("noticeUrl=").append(this.getNoticeUrl()).append("&");
        url.append("partnerID=").append(this.getPartnerID()).append("&");
        url.append("remark=").append(this.getRemark()).append("&");
        url.append("charset=").append(this.getCharset()).append("&");
        url.append("signType=").append(this.getSignType());
        logger.info(url.toString());
        return url.toString();
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getSerialID() {
        return serialID;
    }
    public void setSerialID(String serialID) {
        this.serialID = serialID;
    }
    public String getSubmitTime() {
        return submitTime;
    }
    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }
    public String getFailureTime() {
        return failureTime;
    }
    public void setFailureTime(String failureTime) {
        this.failureTime = failureTime;
    }
    public String getCustomerIP() {
        return customerIP;
    }
    public void setCustomerIP(String customerIP) {
        this.customerIP = customerIP;
    }
    public String getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBuyerMarked() {
        return buyerMarked;
    }
    public void setBuyerMarked(String buyerMarked) {
        this.buyerMarked = buyerMarked;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getOrgCode() {
        return orgCode;
    }
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    
    public String getPlatformID() {
        return platformID;
    }
    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }
    public String getReturnUrl() {
        return returnUrl;
    }
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
    public String getNoticeUrl() {
        return noticeUrl;
    }
    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }
    public String getPartnerID() {
        return partnerID;
    }
    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public String getDirectFlag() {
        return directFlag;
    }
    public void setDirectFlag(String directFlag) {
        this.directFlag = directFlag;
    }
    public String getBorrowingMarked() {
        return borrowingMarked;
    }
    public void setBorrowingMarked(String borrowingMarked) {
        this.borrowingMarked = borrowingMarked;
    }
    public String getCouponFlag() {
        return couponFlag;
    }
    public void setCouponFlag(String couponFlag) {
        this.couponFlag = couponFlag;
    }
    public String getCharset() {
        return charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    public String getSignType() {
        return signType;
    }
    public void setSignType(String signType) {
        this.signType = signType;
    }
    public String getSignMsg() {
        return signMsg;
    }
    public void setSignMsg(String signMsg) {
        this.signMsg = signMsg;
    }
    public static Logger getLogger() {
        return logger;
    }
    public String callbackSign(){
        String content="version" + version + "url" + url + " serialID"
                + serialID + "submitTime" + submitTime + " failureTime"
                + failureTime + " customerIP" + customerIP
                + " orderDetails" + orderDetails + " totalAmount"
                + totalAmount + " type" + type + " buyerMarked"
                + buyerMarked + " payType" + payType + " orgCode" + orgCode
                + "currencyCode" + currencyCode + " directFlag"
                + directFlag + " borrowingMarked" + borrowingMarked
                + " couponFlag" + couponFlag + " platformID" + platformID
                + " returnUrl" + returnUrl + " noticeUrl" + noticeUrl
                + " partnerID" + partnerID + " remark" + remark
                + " charset" + charset + " signType" + signType
                + " signMsg" + signMsg + " orderID" + orderID
                + " orderAmount" + orderAmount + " displayName"
                + displayName + " goodsName" + goodsName + " goodsCount"
                + goodsCount;  
        return content;
    }
    public String encodeSignMD5(){
        String content="version" + version + "url" + url + " serialID"
                + serialID + "submitTime" + submitTime + " failureTime"
                + failureTime + " customerIP" + customerIP
                + " orderDetails" + orderDetails + " totalAmount"
                + totalAmount + " type" + type + " buyerMarked"
                + buyerMarked + " payType" + payType + " orgCode" + orgCode
                + "currencyCode" + currencyCode + " directFlag"
                + directFlag + " borrowingMarked" + borrowingMarked
                + " couponFlag" + couponFlag + " platformID" + platformID
                + " returnUrl" + returnUrl + " noticeUrl" + noticeUrl
                + " partnerID" + partnerID + " remark" + remark
                + " charset" + charset + " signType" + signType
                + " signMsg" + signMsg + " orderID" + orderID
                + " orderAmount" + orderAmount + " displayName"
                + displayName + " goodsName" + goodsName + " goodsCount"
                + goodsCount;  
        String signMD5="";
        try {
             signMD5=hnapay.genSignByMD5(content,CharsetTypeEnum.UTF8);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return signMD5;
    }
    @Override
    public String toString() {
        return "XsPay [version=" + version + ", url=" + url + ", serialID="
                + serialID + ", submitTime=" + submitTime + ", failureTime="
                + failureTime + ", customerIP=" + customerIP
                + ", orderDetails=" + orderDetails + ", totalAmount="
                + totalAmount + ", type=" + type + ", buyerMarked="
                + buyerMarked + ", payType=" + payType + ", orgCode=" + orgCode
                + ", currencyCode=" + currencyCode + ", directFlag="
                + directFlag + ", borrowingMarked=" + borrowingMarked
                + ", couponFlag=" + couponFlag + ", platformID=" + platformID
                + ", returnUrl=" + returnUrl + ", noticeUrl=" + noticeUrl
                + ", partnerID=" + partnerID + ", remark=" + remark
                + ", charset=" + charset + ", signType=" + signType
                + ", signMsg=" + signMsg + ", orderID=" + orderID
                + ", orderAmount=" + orderAmount + ", displayName="
                + displayName + ", goodsName=" + goodsName + ", goodsCount="
                + goodsCount + "]";
    }
    
    
}
