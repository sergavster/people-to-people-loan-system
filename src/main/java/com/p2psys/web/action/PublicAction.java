package com.p2psys.web.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.allinpay.ets.client.PaymentResult;
import com.eitop.platform.tools.Charset;
import com.eitop.platform.tools.encrypt.MD5Digest;
import com.eitop.platform.tools.encrypt.xStrEncrypt;
import com.mypay.merchantutil.Md5Encrypt;
import com.p2psys.common.enums.EnumPayInterface;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.exception.AccountException;
import com.p2psys.payment.Epaylinks;
import com.p2psys.payment.GoPay;
import com.p2psys.payment.IPSPay;
import com.p2psys.payment.RongPay;
import com.p2psys.payment.ShengPay;
import com.p2psys.payment.TranGood;
import com.p2psys.payment.Unspay;
import com.p2psys.payment.YbPay;
import com.p2psys.payment.YiShengPay;
import com.p2psys.payment.hnapay;
import com.p2psys.payment.tenpay.TenpayRequestHandler;
import com.p2psys.payment.tenpay.TenpayResponseHandler;
import com.p2psys.payment.tenpay.client.ClientResponseHandler;
import com.p2psys.payment.tenpay.client.TenpayHttpClient;
import com.p2psys.service.AccountService;
import com.p2psys.tool.coder.BASE64Decoder;
import com.p2psys.tool.coder.MD5;
import com.p2psys.util.CharsetTypeEnum;
import com.p2psys.util.StringUtils;
import com.p2psys.util.tenpayUtil.EpayUtil;

/**
 * 第三方接口通知返回类
 * 
 
 * 
 */
public class PublicAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(PublicAction.class);

    private AccountService accountService;

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String gopay() throws Exception {
        String signValueFromGopay = StringUtils.isNull(paramString("signValue"));
        HttpServletResponse response = ServletActionContext.getResponse();
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.GOPAY.getValue());
        GoPay pay = gopayCallback(paymentInterface);
        logger.debug("Callback_signValue:         " + pay.getCallbackMd5SignVal());
        logger.debug("Callback_signValueFromGopay:" + signValueFromGopay + "===");
        String s = "";
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        log.setRemark("网上充值,国付宝充值,订单号:" + pay.getGood().getMerOrderNum());
        logger.info("==========" + (pay.getRespCode() != null) + ":"
                + (pay.getRespCode() == null ? "" : pay.getRespCode()).equals("0000") + ":"
                + signValueFromGopay.equals(pay.getCallbackMd5SignVal()));
        String trade_no = pay.getGood().getMerOrderNum();
        // 校验订单号是否在执行
        try {
            if (trade_no != null) {
                if (Global.TRADE_NO_SET.add(trade_no)) {
                    if (pay.getRespCode() != null && pay.getRespCode().equals("0000")
                            && signValueFromGopay.equals(pay.getCallbackMd5SignVal())) {
                        accountService.newRecharge(pay.getGood().getMerOrderNum(), pay.getCallbackSignVal(), log,
                                paymentInterface);
                        s = "RespCode=0000|JumpURL=" + Global.getValue("weburl") + "/member/account/recharge.html";
                    } else {
                        accountService.failRecharge(pay.getGood().getMerOrderNum(), pay.getCallbackSignVal(), log);
                        s = "RespCode=9999|JumpURL=" + Global.getValue("weburl") + "/member/account/recharge.html";
                    }
                } else {
                    logger.info("**********充值失败，交易流水号:" + trade_no + "重复充值！**********");
                    s = "RespCode=9999|JumpURL=" + Global.getValue("weburl") + "/member/account/recharge.html";
                }
            }
        } catch (AccountException e) {
            logger.info("**********" + e.getMessage() + "，交易流水号:" + trade_no + "**********");
            s = "RespCode=9999|JumpURL=" + Global.getValue("weburl") + "/member/account/recharge.html";
        } catch (Exception e) {
            logger.info("**********充值异常:" + e.getMessage() + "，交易流水号:" + trade_no + "**********");
            s = "RespCode=9999|JumpURL=" + Global.getValue("weburl") + "/member/account/recharge.html";
        } finally {
            Global.TRADE_NO_SET.remove(trade_no);
        }
        logger.info("Recharge Result:" + s);
        PrintWriter p = response.getWriter();
        p.print(s);
        p.flush();
        p.close();
        return null;
    }

    public String ipspay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.IPS_PAY.getValue());
        IPSPay ips = this.ipspayCallback(paymentInterface);
        String content = ips.callbackSign();
        logger.info(content);

        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        String trade_no = ips.getBillno();
        log.setRemark("网上充值,环讯IPS充值,订单号:" + trade_no);
        try {
            if (trade_no != null) {
                if (Global.TRADE_NO_SET.add(trade_no)) {
                    cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
                    String SignMD5 = b.toMD5(content + ips.getMer_key()).toLowerCase();
                    if (SignMD5.equals(ips.getSignature()) && ips.getSucc().equalsIgnoreCase("Y")) {
                        accountService.newRecharge(trade_no, ips.callbackSign(), log, paymentInterface);
                        message("充值成功！", "/member/account/recharge.html");
                    } else {
                        accountService.failRecharge(trade_no, ips.callbackSign(), log);
                        message("充值失败！", "/member/account/recharge.html");
                    }
                } else {
                    logger.info("**********充值失败，交易流水号:" + trade_no + "**********");
                    message("充值失败！", "/member/account/recharge.html");
                }
            }
        } catch (AccountException e) {
            message("充值失败！", "/member/account/recharge.html");
            // wcw 2013-12-30 start
            logger.error(e.getMessage());
            // wcw 2013-12-30 end
        } catch (Exception e) {
            message("充值失败！", "/member/account/recharge.html");
            logger.error(e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(trade_no);
        }
        return MSG;
    }

    public String ipspayOrder() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.IPS_PAY.getValue());
        IPSPay ips = this.ipspayCallback(paymentInterface);
        String content = ips.callbackSign();
        logger.info(content);

        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        String trade_no = ips.getBillno();
        log.setRemark("网上充值,环讯IPS充值,订单号:" + trade_no);
        PrintWriter p = response.getWriter();
        try {
            if (trade_no != null) {
                if (Global.TRADE_NO_SET.add(trade_no)) {
                    cryptix.jce.provider.MD5 b = new cryptix.jce.provider.MD5();
                    String SignMD5 = b.toMD5(content + ips.getMer_key()).toLowerCase();
                    if (SignMD5.equals(ips.getSignature()) && ips.getSucc().equalsIgnoreCase("Y")) {
                        accountService.newRecharge(trade_no, ips.callbackSign(), log, paymentInterface);
                        message("充值成功！", "/member/account/recharge.html");
                        p.print("ipscheckok");

                    } else {
                        accountService.failRecharge(trade_no, ips.callbackSign(), log);
                        message("充值失败！", "/member/account/recharge.html");
                        p.print("ipscheckfail");
                    }
                } else {
                    logger.info("**********充值失败，交易流水号:" + trade_no + "重复充值！**********");
                    message("充值失败！", "/member/account/recharge.html");
                    p.print("ipscheckfail");
                }
            }
        } catch (AccountException e) {
            message("充值失败！", "/member/account/recharge.html");
        } catch (Exception e) {
            message("充值失败！", "/member/account/recharge.html");
            logger.error(e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(trade_no);
        }
        p.flush();
        p.close();
        return null;
    }

    // 网银在线 同步通知 （商户页面通知） wcw 2013-12-12
    public String cbpPay_return() throws Exception {
        cbpPay();
        return MSG;
    }

    // 网银在线 异步通知 （后台通知） wcw 2013-12-12
    public String cbpPay_notify() throws Exception {
        String result = cbpPay();
        if (!StringUtils.isBlank(result)) {
            PrintWriter p = response.getWriter();
            p.print(result);
            p.close();
        }
        return null;
    }

    // 网银在线
    private String cbpPay() throws Exception {
        // wcw 2013-12-30 start
        logger.info("欢迎进入网银在线回调方法");
        // wcw 2013-12-30 end
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        String v_oid = paramString("v_oid"); // 订单号
        log.setRemark("网上充值,网银在线充值,订单号:" + v_oid);
        String v_pmode = paramString("v_pmode"); // 支付方式中文说明，如"中行长城信用卡"
        String v_pstatus = paramString("v_pstatus"); // 支付结果，20支付完成；30支付失败；
        String v_pstring = paramString("v_pstring"); // 对支付结果的说明，成功时（v_pstatus=20）为"支付成功"，支付失败时（v_pstatus=30）为"支付失败"
        String v_amount = paramString("v_amount"); // 订单实际支付金额
        String v_moneytype = paramString("v_moneytype"); // 币种
        String v_md5str = paramString("v_md5str"); // MD5校验码
        String remark1 = paramString("remark1"); // 备注1
        String remark2 = paramString("remark2"); // 备注2
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.CHINABANK_PAY
                .getValue());
        String key = "";
        if (paymentInterface != null) {
            key = paymentInterface.getKey();
        }
        // wcw 2013-12-30 start
        logger.info("网银在线订单号:=======" + v_oid);
        // wcw 2013-12-30 ebd
        String text = v_oid + v_pstatus + v_amount + v_moneytype + key;
        MD5 md5 = new MD5();
        String v_md5 = md5.getMD5ofStr(text).toUpperCase();
        String signStr = "v_oid=" + v_oid + "&v_pmode=" + v_pmode + "&v_pstatus=" + v_pstatus + "&v_pstring="
                + v_pstring + "&v_amount=" + v_amount + "&v_moneytype=" + v_moneytype + "&v_md5str=" + v_md5str
                + "&remark1=" + remark1 + "&remark2=" + remark2 + "&key=" + key + "&v_md5=" + v_md5;
        logger.debug("MD5校验码： " + v_md5str);
        logger.debug(v_md5);
        String result = "";
        try {
            if (v_oid != null) {
                if (Global.TRADE_NO_SET.add(v_oid)) {
                    if (v_md5str.equals(v_md5)) {
                        // wcw 2013-12-30 start
                        logger.info("该订单号为" + v_oid + "的订单MD5校验通过");
                        // wcw 2013-12-30 end
                        if (v_pstatus != null && v_pstatus.equals("20")) {
                            // wcw 2013-12-30 start
                            logger.info("网银在线系统返回状态为交易成功状态，进入${webname}系统处理");
                            // wcw 2013-12-30 end
                            accountService.newRecharge(v_oid, signStr, log, paymentInterface);
                            message("充值成功！", "/member/account/recharge.html");
                            result = "success";
                        } else {
                            // wcw 2013-12-30 start
                            logger.info("网银在线系统返回状态为交易失败状态，进入${webname}系统处理");
                            // wcw 2013-12-30 end
                            logger.info("v_pstatus(支付结果)=============" + v_pstatus);
                            accountService.failRecharge(v_oid, signStr, log);
                            message("充值失败！", "/member/account/recharge.html");
                            result = "error";
                        }
                    } else {
                        logger.info("MD5校验失败...请求重发！");
                        message("充值失败！", "/member/account/recharge.html");
                        result = "error";
                    }
                } else {
                    logger.info("**********网银在线充值订单号为" + v_oid + "的订单已经处理，请勿重复充值！**********");
                }
            }
        } catch (Exception e) {
            // wcw 2013-12-30 start
            logger.error("**********充值异常，交易订单号:" + v_oid + "**********");
            logger.error("异常信息====" + e.getMessage());
            // wcw 2013-12-30 end
        } finally {
            Global.TRADE_NO_SET.remove(v_oid);
        }
        logger.info("result=========" + result);
        return result;
    }

    // 新生同步回调 （商户页面返回） wcw 2013-12-12
    public String xspay_return() throws Exception {
        xspay();
        return MSG;
    }

    // 新生异步回调 （后台返回） wcw 2013-12-12
    public String xspay_notify() throws Exception {
        String callbankResults = xspay();
        if (!StringUtils.isBlank(callbankResults)) {
            PrintWriter p = response.getWriter();
            p.print(callbankResults);
            p.close();
        }
        return null;
    }

    private String xspay() throws Exception {
        logger.info("------------欢迎进入新生支付接口回调方法------");
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.XS_PAY.getValue());
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        String orderID = paramString("orderID");
        log.setRemark("网上充值,新生充值,订单号:" + orderID);
        String resultCode = paramString("resultCode");
        String stateCode = paramString("stateCode");
        String orderAmount = paramString("orderAmount");
        String payAmount = paramString("payAmount");
        String acquiringTime = paramString("acquiringTime");
        String completeTime = paramString("completeTime");
        String orderNo = paramString("orderNo");
        String partnerID = paramString("partnerID");
        String remark = paramString("remark");
        String charset = paramString("charset");
        String signType = paramString("signType");
        String signMsg = paramString("signMsg");
        Boolean result = false;
        String signStr = "orderID=" + orderID + "&resultCode=" + resultCode + "&stateCode=" + stateCode
                + "&orderAmount=" + orderAmount + "&payAmount=" + payAmount + "&acquiringTime=" + acquiringTime
                + "&completeTime=" + completeTime + "&orderNo=" + orderNo + "&partnerID=" + partnerID + "&remark="
                + remark + "&charset=" + charset + "&signType=" + signType;
        // wcw 2013-12-30 start
        logger.info("新生接口订单号为========" + orderID);
        // wcw 2013-12-30 end
        String callbankResults = "";
        if (!StringUtils.isBlank(orderID)) {
            try {
                if (Global.TRADE_NO_SET.add(orderID)) {
                    if ("2".equals(signType)) {
                        logger.info("------------进入新生支付MD5验签------");
                        String pkey = "";
                        if (paymentInterface != null) {
                            pkey = paymentInterface.getKey();
                        }
                        signStr = signStr + "&pkey=" + pkey;
                        result = hnapay.verifySignatureByMD5(signStr, signMsg, CharsetTypeEnum.UTF8);
                    } else if ("1".equals(signType)) {
                        logger.info("------------进入新生支付RSA私钥验签-----");
                        // RSA：网关公钥钥解签
                        result = hnapay.verifySignatureByRSA(signStr, signMsg, CharsetTypeEnum.UTF8);
                    }
                    if (result) {
                        logger.info("------------验签成功------");
                        if (stateCode != null && stateCode.equals("2")) {
                            accountService.newRecharge(orderID, signStr, log, paymentInterface);
                            message("充值成功！", "/member/account/recharge.html");
                            logger.info("------------该订单号为" + orderID + "的订单充值成功------");
                            callbankResults = "200";
                        } else {
                            accountService.failRecharge(orderID, signStr, log);
                            message("充值失败！", "/member/account/recharge.html");
                            logger.info("------------该订单号为" + orderID + "的订单充值失败------");
                        }
                    } else {
                        accountService.failRecharge(orderID, signStr, log);
                        message("充值失败！", "/member/account/recharge.html");
                        logger.info("------------验签失败------");
                    }
                } else {
                    // accountService.failRecharge(orderID, signStr, log);
                    // message("充值失败！", "/member/account/recharge.html");
                    logger.info("------------该订单号为" + orderID + "的订单已经进行处理，请勿重复处理------");
                }
            } catch (AccountException e) {
                message("充值失败！", "/member/account/recharge.html");
                logger.error("------------充值异常------");
                // wcw 2013-12-30 start
                logger.error("异常信息======" + e.getMessage());
                // wcw 2013-12-30 end
            } catch (Exception e) {
                logger.info("**********充值异常:" + e.getMessage() + "，交易流水号:" + orderID + "重复充值！**********");
                message("充值失败！", "/member/account/recharge.html");
                // wcw 2013-12-30 start
                logger.error("异常信息======" + e.getMessage());
                // wcw 2013-12-30 end
            } finally {
                Global.TRADE_NO_SET.remove(orderID);
            }
        }
        return callbankResults;
    }

    public String ybpay() throws Exception {
        logger.info("欢迎进入易宝祝福");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());

        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.YB_PAY.getValue());
        String keyValue = "";
        if (paymentInterface != null) {
            keyValue = paymentInterface.getKey();
        }
        String r0_Cmd = paramString("r0_Cmd");
        String p1_MerId = paramString("p1_MerId");
        String r1_Code = paramString("r1_Code");
        String r2_TrxId = paramString("r2_TrxId");
        String r3_Amt = paramString("r3_Amt");
        String r4_Cur = paramString("r4_Cur");
        String r5_Pid = new String(paramString("r5_Pid").getBytes("utf-8"), "gbk");
        String r6_Order = paramString("r6_Order");
        String r7_Uid = paramString("r7_Uid");
        String r8_MP = paramString("r8_MP");
        String r9_BType = paramString("r9_BType");
        String hmac = paramString("hmac");
        Boolean isOK = true;
        String signStr = "keyValue=" + keyValue + "&r0_Cmd=" + r0_Cmd + "&p1_MerId=" + p1_MerId + "&r1_Code=" + r1_Code
                + "&r2_TrxId=" + r2_TrxId + "&r3_Amt=" + r3_Amt + "&r4_Cur=" + r4_Cur + "&r5_Pid=" + r5_Pid
                + "&r6_Order=" + r6_Order + "&r7_Uid=" + r7_Uid + "&r8_MP=" + r8_MP + "&r9_BType=" + r9_BType
                + "&hmac=" + hmac;
        log.setRemark("网上充值,易宝充值,订单号:" + r6_Order);
        logger.debug("callback======" + signStr);
        logger.info("易宝支付订单号======" + r6_Order);
        try {
            isOK = YbPay.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order,
                    r7_Uid, r8_MP, r9_BType, keyValue);
            if (Global.TRADE_NO_SET.add(r6_Order)) {
                if (isOK) {
                    logger.info("校验通过");
                    if (r1_Code.equals("1")) {
                        logger.info("易宝支付服务器返回的支付状态为成功状态=====" + r1_Code);
                        // 产品通用接口支付成功返回-浏览器重定向
                        if (r9_BType.equals("1")) {
                            accountService.newRecharge(r6_Order, signStr, log, paymentInterface);
                            message("充值成功！", "/member/account/recharge.html");
                            return MSG;
                        } else if (r9_BType.equals("2")) {
                            // v1.6.7.2 RDPROJECT-604 wcw 2013-12-18 start
                            accountService.newRecharge(r6_Order, signStr, log, paymentInterface);
                            // v1.6.7.2 RDPROJECT-604 wcw 2013-12-18 end
                            message("充值成功！", "/member/account/recharge.html");
                            PrintWriter p = response.getWriter();
                            p.print(SUCCESS);
                            p.flush();
                            p.close();
                            return null;
                        } else {
                            accountService.failRecharge(r6_Order, signStr, log);
                            message("充值失败！", "/member/account/recharge.html");
                        }
                    } else {
                        logger.info("易宝支付服务器返回的支付状态为失败状态=====" + r1_Code);
                    }
                } else {
                    logger.info("校验不通过");
                }
            }
        } catch (AccountException e) {
            message("充值失败！", "/member/account/recharge.html");
            // wcw 2013-12-30 start
            logger.error("异常信息======" + e.getMessage());
            // wcw 2013-12-30 end
        } catch (Exception e) {
            logger.info("**********充值异常，交易订单号:" + r6_Order + "**********");
            message("充值失败！", "/member/account/recharge.html");
            // wcw 2013-12-30 start
            logger.error("异常信息======" + e.getMessage());
            // wcw 2013-12-30 end
        } finally {
            Global.TRADE_NO_SET.remove(r6_Order);
        }
        return MSG;
    }

    /**
     * 宝付支付 页面回调地址
     * 
     * @return
     * @throws Exception
     */
    public String baoFoopay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.BAOFOO_PAY.getValue());
        logger.info("宝付支付充值回调成功,已经进入方法.....");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        String MerchantID = paramString("MerchantID");// 商户号
        String TransID = paramString("TransID");// 商户流水号
        logger.info("回调订单号： " + TransID);
        String Result = paramString("Result");// 支付结果
        String resultDesc = paramString("resultDesc");// 支付结果描述
        logger.info("支付结果描述: " + resultDesc);
        String factMoney = paramString("factMoney");// 实际成功金额
        logger.info("实际交易金额：" + factMoney);
        String additionalInfo = paramString("additionalInfo");// 订单附加消息
        String SuccTime = paramString("SuccTime");// 支付完成时间
        String Md5Sign = paramString("Md5Sign");// MD5签名
        String Md5key = "";
        if (paymentInterface != null) {
            Md5key = paymentInterface.getKey();
        }
        String md5 = MerchantID + TransID + Result + resultDesc + factMoney + additionalInfo + SuccTime + Md5key;// MD5签名格式
        MD5 md5Obj = new MD5();
        String WaitSign = md5Obj.getMD5ofStr(md5);
        log.setRemark("网上充值,宝付充值,订单号:" + TransID);
        // 从宝付 传过来的 md5 签名 是 小写 这里 转换一下
        WaitSign = WaitSign.toLowerCase();
        try {
            // 这里校验订单号
            if (TransID != null) {
                // 这里没有 校验数据库订单金额 和 宝付支付返回来的金额 是否 相等， 会有少许的订单金额差错。
                if (Global.TRADE_NO_SET.add(TransID)) {
                    if (WaitSign.compareTo(Md5Sign) == 0) {
                        logger.info("校验通过...");
                        // 根据处理的结果 处理订单
                        if ("1".equals(Result)) {
                            logger.info(" 订单：" + TransID + " 宝付系统扣款成功，下面开始充值...");
                            accountService.newRecharge(TransID, WaitSign, log, paymentInterface);
                            logger.info("单号：" + TransID + "充值成功！！！");
                            message("充值成功！", "/member/account/recharge.html");
                        } else {
                            logger.info("订单：" + TransID + " 宝付系统支付失败.. ");
                            accountService.failRecharge(TransID, WaitSign, log);
                            message("充值失败！", "/member/account/recharge.html");
                        }
                    } else {
                        logger.info("MD5校验失败...");
                        message("充值失败！", "/member/account/recharge.html");
                    }
                } else {
                    logger.info("**********充值失败，交易流水号:" + TransID + "**********");
                    message("充值失败！", "/member/account/recharge.html");
                }
            }
        } catch (Exception e) {
            logger.error("充值回调异常======" + e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(TransID);
        }
        return MSG;
    }

    /**
     * 通联支付
     * 
     * @return
     * @throws Exception
     */
    public String allinpay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.ALLIN_PAY.getValue());
        logger.info("通联支付充值回调成功,已经进入方法.....");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        // 接收Server返回的支付结果
        String orderNo = paramString("orderNo");
        logger.info("回调订单号： " + orderNo);
        String payAmount = paramString("payAmount");
        logger.info("实际交易金额：" + paramString("payAmount"));
        String payResult = paramString("payResult");
        logger.info("支付结果描述: " + paramString("payResult"));

        String signMsg = paramString("signMsg");
        // 验签是商户为了验证接收到的报文数据确实是支付网关发送的。
        // 构造订单结果对象，验证签名。
        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setMerchantId(paramString("merchantId"));
        paymentResult.setVersion(paramString("version"));
        paymentResult.setLanguage(paramString("language"));
        paymentResult.setSignType(paramString("signType"));
        paymentResult.setPayType(paramString("payType"));
        paymentResult.setIssuerId(paramString("issuerId"));
        paymentResult.setPaymentOrderId(paramString("paymentOrderId"));
        paymentResult.setOrderNo(orderNo);
        paymentResult.setOrderDatetime(paramString("orderDatetime"));
        paymentResult.setOrderAmount(paramString("orderAmount"));
        paymentResult.setPayDatetime(paramString("payDatetime"));
        paymentResult.setPayAmount(payAmount);
        paymentResult.setExt1(paramString("ext1"));
        paymentResult.setExt2(paramString("ext2"));
        paymentResult.setPayResult(paramString("payResult"));
        paymentResult.setErrorCode(paramString("errorCode"));
        paymentResult.setReturnDatetime(paramString("returnDatetime"));
        paymentResult.setSignMsg(signMsg);
        // v1.6.7.2 RDPROJECT-546 wcw 2013-12-11 start
        /*
         * String contextPath = ServletActionContext.getServletContext() .getRealPath("/");
         * 
         * paymentResult.setCertPath(contextPath + "/data/cert/" + Global.getValue("tl_cert")); // 读取通联公钥证书存放路径
         */paymentResult.setCertPath(paymentInterface.getCert_position());

        // System.out.println(paymentResult.getCertPath());
        logger.info("通联MD5加密key" + paymentInterface.getKey());
        logger.info("通联公钥证书存放路径" + paymentInterface.getCert_position());
        // v1.6.7.2 RDPROJECT-546 wcw 2013-12-11 end
        boolean verifyResult = paymentResult.verify();
        log.setRemark("网上充值,通联充值,订单号:" + orderNo);
        try {
            // 这里校验订单号
            if (orderNo != null) {
                // 这里没有 校验数据库订单金额 和 宝付支付返回来的金额 是否 相等， 会有少许的订单金额差错。
                if (Global.TRADE_NO_SET.add(orderNo)) {
                    if (verifyResult) {
                        logger.info("校验通过...");
                        // 根据处理的结果 处理订单
                        if (payResult.equals("1")) {
                            logger.info(" 订单：" + orderNo + " 通联支付系统扣款成功，下面开始充值...");
                            accountService.newRecharge(orderNo, signMsg, log, paymentInterface);
                            logger.info("单号：" + orderNo + "充值成功！！！");
                            message("充值成功！", "/member/account/recharge.html");
                        } else {
                            logger.info("订单：" + orderNo + " 通联支付系统支付失败.. ");
                            accountService.failRecharge(orderNo, signMsg, log);
                            message("充值失败！", "/member/account/recharge.html");
                        }
                    } else {
                        logger.info("MD5校验失败...");
                        message("充值失败！", "/member/account/recharge.html");
                    }
                } else {
                    logger.info("**********充值失败，交易流水号:" + orderNo + "**********");
                    message("充值失败！", "/member/account/recharge.html");
                }
            }
        } catch (Exception e) {
            logger.error("充值异常--------" + e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(orderNo);
        }
        return MSG;
    }

    /**
     * 智付支付回调 yinliang 2013-04-09
     * 
     * @return
     * @throws Exception
     */
    public String dinpay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.DINPAY.getValue());
        logger.info("智付支付充值回调成功,已经进入方法.....");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());

        String OrderInfo = Charset.ISO8859_2_Gb(paramString("OrderMessage"));

        String MOSignMsg = paramString("Digest");

        String m_orderID = "";

        String payStatus = "";

        // 检查签名
        String key = "";
        if (paymentInterface != null) {
            key = paymentInterface.getKey();
        }
        String digest = MD5Digest.encrypt(OrderInfo + key);
        try {
            if (digest.equals(MOSignMsg)) {
                logger.info("MD5校验成功");
                // 解密
                OrderInfo = xStrEncrypt.StrDecrypt(OrderInfo, key);
                // 对订单号进行处理
                String array[];
                StringTokenizer OrderInfos = new StringTokenizer(OrderInfo, "|");
                array = new String[OrderInfos.countTokens()];
                int i = 0;
                while (OrderInfos.hasMoreTokens()) {
                    array[i] = OrderInfos.nextToken();
                    i++;
                }
                m_orderID = array[2];
                log.setRemark("网上支付，智付充值：" + m_orderID);
                logger.info("智付订单号为========" + m_orderID);
                if (m_orderID != null) {
                    if (Global.TRADE_NO_SET.add(m_orderID)) {
                        if ("2".equals(payStatus)) {
                            logger.info("智付通知返回支付成功状态为=======" + m_orderID);
                            accountService.newRecharge(m_orderID, MOSignMsg, log, paymentInterface);
                            message("充值成功！", "/member/account/recharge.html");
                        } else {
                            logger.info("智付通知返回支付失败状态为=======" + m_orderID);
                            accountService.failRecharge(m_orderID, MOSignMsg, log);
                            message("充值失败！", "/member/account/recharge.html");
                        }
                    } else {
                        message("重复充值！", "/member/account/recharge.html");
                    }
                }
            } else {
                logger.info("MD5校验失败");
                message("充值失败！", "/member/account/recharge.html");
            }
        } catch (AccountException e) {
            logger.error("异常信息======" + e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(m_orderID);
        }
        return MSG;
    }

    /**
     * 汇潮
     * 
     * @param paymentInterface
     * @throws Exception
     */
    public void ecpss(PaymentInterface paymentInterface) throws Exception {
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        // 字符编码
        String CharacterEncoding = "UTF-8";
        request.setCharacterEncoding(CharacterEncoding);
        String BillNo = paramString("BillNo");
        String Amount = paramString("Amount");
        String tradeOrder = paramString("tradeOrder");
        String Succeed = paramString("Succeed");
        String Result = paramString("Result");
        String receiveMD5info = paramString("MD5info");
        String MD5key = "";
        if (paymentInterface != null) {
            MD5key = paymentInterface.getKey();
        }
        MD5 md5 = new MD5();
        String md5src = BillNo + Amount + Succeed + MD5key;
        String MD5info = md5.getMD5ofStr(md5src);
        logger.info("汇潮支付订单号为========" + BillNo);
        try {
            if (StringUtils.isNull(receiveMD5info).equals(MD5info)) {
                logger.info("校验通过...");
                if (Global.TRADE_NO_SET.add(BillNo)) {
                    if (("88").equals(Succeed)) {
                        logger.info("订单" + BillNo + "汇潮支付系统扣款成功，下面开始充值...");
                        log.setRemark("网上支付,汇潮支付：" + BillNo);
                        accountService.newRecharge(BillNo, Succeed, log, paymentInterface);
                        logger.info("单号：" + BillNo + "充值成功！！！");
                        message("充值成功！", "/member/account/recharge.html");

                    } else {
                        logger.info("订单：" + BillNo + " 汇潮支付系统支付失败.. ");
                        accountService.failRecharge(BillNo, Succeed, log);
                        message("充值失败！", "/member/account/recharge.html");

                    }
                } else {
                    logger.info("**********充值失败，交易流水号:" + BillNo + "**********");
                }

            } else {
                logger.info("校验失败...");
            }
        } catch (Exception e) {
            logger.error("异常信息=======" + e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(BillNo);
        }
    }

    /**
     * 汇潮支付回调 yinliang 2013-05-15
     * 
     * @return
     * @throws Exception
     */
    public String ecpsspay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.ECPSSPAY.getValue());
        logger.info("汇潮支付回调成功，已经进入方法...");
        ecpss(paymentInterface);
        return MSG;

    }

    /**
     * 汇潮支付回调 yinliang 2013-07-13
     * 
     * @return
     * @throws Exception
     */
    public String ecpsspayFast() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.ECPSSFAST_PAY
                .getValue());
        logger.info("汇潮快捷支付回调成功，已经进入方法...");
        ecpss(paymentInterface);
        return MSG;

    }

    /**
     * 汇潮中行积分通道直连支付回调 yinliang 2013-07-13
     * 
     * @return
     * @throws Exception
     */
    public String ecpsspayChina() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.ECPSSCHINA_PAY
                .getValue());
        logger.info("汇潮中行积分直连通道支付回调成功，已经进入方法...");
        ecpss(paymentInterface);
        return MSG;

    }

    public String shengpay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.SHENG_PAY.getValue());
        logger.info("盛付通支付回调成功，已经进入方法...");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        // 字符编码
        String CharacterEncoding = "UTF-8";
        request.setCharacterEncoding(CharacterEncoding);
        ShengPay spay = shengpayCallback();
        String signKey = "";
        if (paymentInterface != null) {
            signKey = paymentInterface.getKey();
        }
        ShengPay shengpay = new ShengPay();
        String xx = spay.toSignStringResponse();
        logger.info(xx);
        shengpay = spay.signFormResponse(spay, signKey);
        String signMsg1 = shengpay.getSignMsg();
        logger.info(signMsg1);
        String OrderNo = spay.getOrderNo();
        String signMsg2 = paramString("SignMsg");
        logger.info(signMsg2);
        logger.info("盛付通订单号为=============" + OrderNo);
        String TransStatus = spay.getTransStatus();
        try {
            if (StringUtils.isNull(signMsg1).equals(signMsg2)) {
                logger.info("校验通过...");
                if (Global.TRADE_NO_SET.add(OrderNo)) {
                    if ("01".equals(TransStatus)) {
                        logger.info("订单" + OrderNo + "盛付通支付系统扣款成功，下面开始充值...");
                        log.setRemark("网上支付,盛付通支付：" + OrderNo);
                        accountService.newRecharge(OrderNo, TransStatus, log, paymentInterface);
                        message("充值成功！", "/member/account/recharge.html");
                        logger.info("单号：" + OrderNo + "充值成功！！！");
                    } else {
                        logger.info("订单：" + OrderNo + "盛付通支付系统支付失败.. ");
                        accountService.failRecharge(OrderNo, TransStatus, log);
                        message("充值失败！", "/member/account/recharge.html");
                    }
                } else {
                    logger.info("**********充值失败，交易订单号:" + OrderNo + "重复充值！**********");
                    message("重复充值！", "/member/account/recharge.html");
                }

            } else {
                logger.info("校验失败...");
                message("充值失败！", "/member/account/recharge.html");
            }
        } catch (Exception e) {
            logger.error("异常信息===========" + e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(OrderNo);
        }
        return MSG;

    }

    /**
     * 财付通回调 yinliang 2013-06-04
     * 
     * @return
     * @throws Exception
     */
    public String tenpay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.TENPAY.getValue());
        logger.info("腾讯财付通支付回调成功，已经进入方法...");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        // 商户号
        String merNo = "";
        // 密钥
        String key = "";
        if (paymentInterface != null) {
            // 商户号
            merNo = paymentInterface.getMerchant_id();
            // 密钥
            key = paymentInterface.getKey();
        }
        // 创建支付应答对象
        TenpayResponseHandler resHandler = new TenpayResponseHandler(request, response);
        resHandler.setKey(key);
        // 订单号
        String out_trade_no = "";
        try {
            // 判断签名
            if (resHandler.isTenpaySign()) {
                logger.info("校验通过...");
                // 通知id
                String notify_id = resHandler.getParameter("notify_id");

                // 创建请求对象
                TenpayRequestHandler queryReq = new TenpayRequestHandler(null, null);
                // 通信对象
                TenpayHttpClient httpClient = new TenpayHttpClient();
                // 应答对象
                ClientResponseHandler queryRes = new ClientResponseHandler();

                // 通过通知ID查询，确保通知来至财付通
                queryReq.init();
                queryReq.setKey(key);
                queryReq.setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
                queryReq.setParameter("partner", merNo);
                queryReq.setParameter("notify_id", notify_id);

                // 通信对象
                httpClient.setTimeOut(5);
                // 设置请求内容
                httpClient.setReqContent(queryReq.getRequestURL());
                logger.info("queryReq:" + queryReq.getRequestURL());
                // 后台调用
                if (httpClient.call()) {
                    // 设置结果参数
                    queryRes.setContent(httpClient.getResContent());
                    logger.info("queryRes:" + httpClient.getResContent());
                    queryRes.setKey(key);
                    // 获取返回参数
                    String retcode = queryRes.getParameter("retcode");
                    String trade_state = queryRes.getParameter("trade_state");

                    String trade_mode = queryRes.getParameter("trade_mode");

                    out_trade_no = queryRes.getParameter("out_trade_no");
                    logger.info("财付通订单号为=======" + out_trade_no);
                    // 判断签名及结果
                    if (queryRes.isTenpaySign() && "0".equals(retcode) && "0".equals(trade_state)
                            && "1".equals(trade_mode)) {
                        logger.info("订单查询成功");
                        // 取结果参数做业务处理
                        logger.info("out_trade_no:" + queryRes.getParameter("out_trade_no") + " transaction_id:"
                                + queryRes.getParameter("transaction_id"));
                        logger.info("trade_state:" + queryRes.getParameter("trade_state") + " total_fee:"
                                + queryRes.getParameter("total_fee"));
                        // 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
                        logger.info("discount:" + queryRes.getParameter("discount") + " time_end:"
                                + queryRes.getParameter("time_end"));
                        // ------------------------------
                        // 处理业务开始
                        // ------------------------------
                        if (Global.TRADE_NO_SET.add(out_trade_no)) {
                            resHandler.sendToCFT("Success");
                            // 处理数据库逻辑
                            logger.info("订单" + out_trade_no + "财付通支付系统扣款成功，下面开始充值...");
                            log.setRemark("网上支付,财付通支付：" + out_trade_no);
                            accountService.newRecharge(out_trade_no, trade_state, log, paymentInterface);
                            logger.info("单号：" + out_trade_no + "充值成功！！！");
                            message("充值成功！", "/member/account/recharge.html");
                            // 处理业务完毕
                            // ------------------------------
                        } else {
                            resHandler.sendToCFT("Fail");
                            logger.info("**********充值失败，交易流水号:" + out_trade_no + "重复充值！**********");
                            message("重复充值！", "/member/account/recharge.html");
                            // 处理业务完毕
                            // ------------------------------
                        }
                    } else {
                        resHandler.sendToCFT("Fail");
                        // 错误时，返回结果未签名，记录retcode、retmsg看失败详情。
                        logger.info("查询验证签名失败或业务错误");
                        logger.info("retcode:" + queryRes.getParameter("retcode") + " retmsg:"
                                + queryRes.getParameter("retmsg"));
                        logger.info("订单：" + out_trade_no + " 汇潮支付系统支付失败.. ");
                        accountService.failRecharge(out_trade_no, trade_state, log);
                        message("充值失败！", "/member/account/recharge.html");
                    }

                } else {
                    resHandler.sendToCFT("Fail");
                    logger.info("后台调用通信失败");
                    logger.info(httpClient.getResponseCode());
                    logger.info(httpClient.getErrInfo());
                    // 有可能因为网络原因，请求已经处理，但未收到应答。
                    message("充值失败！", "/member/account/recharge.html");
                }
            } else {
                resHandler.sendToCFT("Fail");
                logger.info("通知签名验证失败");
                logger.info("校验失败...");
                message("充值失败！", "/member/account/recharge.html");
            }
        } catch (Exception e) {
            logger.error("异常信息=======" + e.getMessage());
        } finally {
            Global.TRADE_NO_SET.remove(out_trade_no);
        }

        return MSG;
    }

    /**
     * 融宝支付 同步通知（页面通知）王长伟 2013-12-05
     * 
     * @return
     */
    public String rongpay_return() throws Exception {
        rongpay();
        return MSG;
    }

    /**
     * 融宝支付 王长伟 2013-7-17
     * 
     * @return
     */
    public String rongpay_notify() throws Exception {
        String result = rongpay();
        if (!StringUtils.isBlank(result)) {
            PrintWriter p = response.getWriter();
            p.print(result);
            p.flush();
            p.close();
        }
        return null;
    }

    /**
     * 融宝支付 王长伟 2013-7-17
     * 
     * @return String
     */
    private String rongpay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.RONGBAO_PAY.getValue());
        logger.info("融宝支付回调成功，已经进入方法...");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        String key = "";
        if (paymentInterface != null) {
            key = paymentInterface.getKey();
        }
        RongPay rp = new RongPay();
        // 获取融宝支付GET过来反馈信息
        Map params = rp.transformRequestMap(request.getParameterMap());

        // 判断responsetTxt是否为ture，生成的签名结果mysign与获得的签名结果sign是否一致
        // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        // mysign与sign不等，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String mysign = rp.BuildMysign(params, key, paymentInterface);

        String responseTxt = rp.Verify(paramString("notify_id"), paymentInterface);
        logger.info(responseTxt + "******");
        String sign = paramString("sign");

        // 获取融宝支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        String trade_no = paramString("trade_no"); // 融宝支付交易号
        logger.info("融宝支付交易号======" + trade_no);
        String order_no = paramString("order_no"); // 获取订单号
        logger.info("融宝支付订单号======" + order_no);
        String total_fee = paramString("total_fee"); // 获取总金额
        logger.info("融宝支付实际交易金额======" + total_fee);
        String title = paramString("title"); // 商品名称、订单名称
        String body = paramString("body");
        String buyer_email = paramString("buyer_email"); // 买家融宝支付账号
        String trade_status = paramString("trade_status"); // 交易状态
        String is_success = paramString("is_success");
        // 获取融宝支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        String signStr = "trade_no=" + trade_no + "&order_no=" + order_no + "&total_fee=" + total_fee + "&title="
                + title + "&body=" + body + "&buyer_email=" + buyer_email + "&trade_status=" + trade_status + "&sign="
                + sign;
        String result = "";
        // 建议校验responseTxt，判读该返回结果是否由融宝发出
        try {
            if ("T".equals(is_success)) {
                logger.info("融宝支付通知is_success" + is_success + "，即成功!");
                if (order_no != null) {
                    if (Global.TRADE_NO_SET.add(order_no)) {
                        if (mysign.equals(sign) && responseTxt.equals("true")) {
                            logger.info("融宝支付订单号为" + order_no + "的订单校验成功");
                            if ("TRADE_FINISHED".equals(trade_status)) {
                                logger.info("融宝支付订单号为" + order_no + "的订单通知返回状态为成功状态" + trade_no + "!");
                                accountService.newRecharge(order_no, signStr, log, paymentInterface);
                                message("充值成功！", "/member/account/recharge.html");
                                result = "success";
                            } else {
                                logger.info("融宝支付订单号为" + order_no + "的订单通知返回状态为失败状态" + trade_no + "!");
                                accountService.failRecharge(order_no, signStr, log);
                                message("充值失败！", "/member/account/recharge.html");
                                result = "fail";
                            }
                        } else {
                            logger.info("融宝支付订单号为" + order_no + "的订单校验失败!");
                            result = "fail";
                        }
                    } else {
                        logger.info("融宝支付订单号为" + order_no + "的订单已经处理，请勿重复处理");
                    }
                }
            } else {
                logger.info("融宝支付通知返回is_success为" + is_success + "，即失败!");
            }
        } catch (AccountException e) {
            logger.info("**********" + e.getMessage() + "，交易订单号:" + trade_no + "**********");
        } catch (Exception e) {
            logger.info("**********充值异常" + e.getMessage() + "，交易订单号:" + trade_no + "**********");
        } finally {
            Global.TRADE_NO_SET.remove(order_no);
        }
        logger.info("Recharge Result:" + result);
        return result;
    }

    // v1.6.5.5 RDPROJECT-148 xx 2013-09-23 start
    /**
     * 易生支付 同步通知（页面返回）
     * 
     * @return
     */
    public String yishengpay_return() throws Exception {
        yishengpay();
        return MSG;
    }

    /**
     * 易生支付 异步通知（后台返回）
     * 
     * @return
     */
    public String yishengpay_notify() throws Exception {
        String result = yishengpay();
        if (!StringUtils.isBlank(result)) {
            PrintWriter p = response.getWriter();
            p.print(result);
            p.flush();
            p.close();
        }
        return null;
    }

    /**
     * 易生支付
     * 
     * @return
     */
    private String yishengpay() throws Exception {
        logger.info("易生支付回调成功，已经进入方法...");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.YISHENG_PAY.getValue());
        String key = "";
        if (paymentInterface != null) {
            key = paymentInterface.getKey();
        }
        YiShengPay ysp = new YiShengPay();
        // 将易生支付POST过来反馈信息转换一下
        Map params = ysp.transformRequestMap(request.getParameterMap());

        // 判断responsetTxt是否为ture，生成的签名结果mysign与获得的签名结果sign是否一致
        // responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        // mysign与sign不等，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        // v1.6.7.2 RDPROJECT-546 wcw 2013-12-11 start
        String mysign = ysp.BuildMysign(params, key, paymentInterface);
        String responseTxt = ysp.Verify(paramString("notify_id"), paymentInterface);
        // v1.6.7.2 RDPROJECT-546 wcw 2013-12-11 end
        logger.info(responseTxt + "******");
        String sign = paramString("sign");
        // 获取易生支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        String trade_no = paramString("trade_no"); // 易生支付交易号
        logger.info("易生支付交易号======" + trade_no);
        String out_trade_no = paramString("out_trade_no"); // 获取订单号
        logger.info("易生支付订单号======" + out_trade_no);
        String total_fee = paramString("total_fee"); // 获取总金额
        logger.info("易生支付实际交易金额======" + total_fee);
        String subject = paramString("subject"); // 商品名称、订单名称
        String body = paramString("body");
        String buyer_email = paramString("buyer_email"); // 买家易生支付账号
        String trade_status = paramString("trade_status"); // 交易状态
        String is_success = paramString("is_success");
        // 获取易生支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        String signStr = "trade_no=" + trade_no + "&out_trade_no=" + out_trade_no + "&total_fee=" + total_fee
                + "&subject=" + subject + "&body=" + body + "&buyer_email=" + buyer_email + "&trade_status="
                + trade_status + "&sign=" + sign;
        String result = "";
        // 建议校验responseTxt，判读该返回结果是否由融宝发出
        try {
            if ("T".equals(is_success)) {
                logger.info("易生支付通知返回is_success为" + is_success + "，即成功!");
                if (out_trade_no != null) {
                    if (Global.TRADE_NO_SET.add(out_trade_no)) {
                        // mysign.equals(sign) &&
                        if (mysign.equals(sign) && responseTxt.equals("true")) {
                            logger.info("易生支付校验成功");
                            if ("TRADE_FINISHED".equals(trade_status)) {
                                logger.info("易生支付通知返回支付状态为成功状态" + trade_status + "!");
                                accountService.newRecharge(out_trade_no, signStr, log, paymentInterface);
                                message("充值成功！", "/member/account/recharge.html");
                                result = "success";
                            } else {
                                logger.info("易生支付通知返回支付状态为失败状态" + trade_status + "!");
                                accountService.failRecharge(out_trade_no, signStr, log);
                                message("充值失败！", "/member/account/recharge.html");
                                result = "fail";
                            }
                        } else {
                            logger.info("易生支付校验失败");
                            result = "fail";
                        }
                    } else {
                        logger.info("易生支付订单号为" + out_trade_no + "的订单已经处理，请勿重复处理");
                    }
                }
            } else {
                logger.info("易生支付通知返回is_success为" + is_success + "，即失败!");
            }
        } catch (AccountException e) {
            logger.info("**********" + e.getMessage() + "，交易流水号:" + trade_no + "**********");
        } catch (Exception e) {
            logger.info("**********充值异常，" + e.getMessage() + "交易流水号:" + trade_no + "**********");
        } finally {
            Global.TRADE_NO_SET.remove(out_trade_no);
        }
        logger.info("Recharge Result:" + result);
        return result;
    }

    // v1.6.5.5 RDPROJECT-148 xx 2013-09-23 end

    // v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 start
    /**
     * 双乾支付
     * 
     * @return 跳转页面
     * @throws Exception
     *             异常
     */
    public String epay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.EPAY.getValue());
        logger.info("双乾支付回调成功，已经进入方法...");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        // 字符编码
        String characterEncoding = "UTF-8";
        request.setCharacterEncoding(characterEncoding);
        String merNo = paramString("MerNo");
        String md5key = "";
        if (paymentInterface != null) {
            md5key = paymentInterface.getKey();
        }
        String billNo = paramString("BillNo");
        logger.info("订单号为==========" + billNo);
        String amount = paramString("Amount");
        String succeed = paramString("Succeed");
        String md5info = paramString("MD5info");
        EpayUtil md5util = new EpayUtil();
        String md5str = md5util.signMap(new String[] { merNo, billNo, amount, String.valueOf(succeed) }, md5key, "RES");
        if (md5info.equals(md5str)) {
            logger.info("校验通过...");
            if ("88".equals(succeed)) {
                logger.info("订单" + billNo + "双乾支付系统扣款成功，下面开始充值...");
                log.setRemark("网上支付,双乾支付：" + billNo);
                accountService.newRecharge(billNo, succeed, log, paymentInterface);
                message("充值成功！", "/member/account/recharge.html");
                logger.info("单号：" + billNo + "充值成功！！！");
            } else {
                logger.info("订单：" + billNo + "双乾支付系统支付失败.. ");
                accountService.failRecharge(billNo, succeed, log);
                message("充值失败！", "/member/account/recharge.html");
            }
        } else {
            logger.info("通知签名验证失败");
            logger.info("校验失败...");
            message("充值失败！", "/member/account/recharge.html");
        }
        return MSG;
    }

    // v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 end
    // v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 start
    /**
     * 构造函数
     * 
     * @param request
     * @param response
     */
    public void EpaylinksNotify(Epaylinks epaylinks) {
        Map m = request.getParameterMap();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            String v = ((String[]) m.get(k))[0];
            epaylinks.setParameter(k, v);
        }

    }

    /**
     * 构造函数
     * 
     * @param request
     * @param response
     */
    public void UnspayNotify(Unspay unspay) {
        Map m = request.getParameterMap();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            String v = ((String[]) m.get(k))[0];
            unspay.setParameter(k, v);
        }

    }

    // v1.6.7.2 RDPROJECT-512 wcw 2013-12-05 start
    /**
     * 易票联支付 同步通知
     * 
     * @return
     */
    public String epaylinks_return() throws Exception {
        epaylinks();
        return MSG;
    }

    /**
     * 易票联支付 异步通知
     * 
     * @return
     */
    public String epaylinks_notify() throws Exception {
        String result = epaylinks();
        if (!StringUtils.isBlank(result)) {
            PrintWriter p = response.getWriter();
            p.print(result);
            p.flush();
            p.close();
        }
        return null;
    }

    /**
     * 易票联支付
     * 
     * @return 跳转页面
     * @throws Exception
     *             异常
     */
    private String epaylinks() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.EPAYLINKS.getValue());
        logger.info("易票联支付回调成功，已经进入方法...");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        Epaylinks epaylinks = new Epaylinks();
        EpaylinksNotify(epaylinks);
        epaylinks.setKey(paymentInterface.getKey());
        String partner = epaylinks.getParameter("partner");
        String out_trade_no = epaylinks.getParameter("out_trade_no");
        logger.info("易票联订单号为========" + out_trade_no);
        String pay_no = epaylinks.getParameter("pay_no");
        String amount = epaylinks.getParameter("amount");
        String pay_result = epaylinks.getParameter("pay_result");
        String sett_date = epaylinks.getParameter("sett_date");
        String base64_memo = epaylinks.getParameter("base64_memo");
        String version = epaylinks.getParameter("version");
        String sign_type = epaylinks.getParameter("sign_type");
        String sign = epaylinks.getParameter("sign");
        BASE64Decoder decoder = new BASE64Decoder();
        String memo = new String(decoder.decodeBuffer(base64_memo)).toString();
        String signStr = "out_trade_no=" + out_trade_no + "&pay_no=" + pay_no + "&amount=" + amount + "&pay_result="
                + pay_result + "&sett_date=" + sett_date + "&base64_memo=" + base64_memo + "&version=" + version
                + "&sign_type=" + sign_type + "&sign=" + sign + "&memo=" + memo;
        String result = "";
        try {
            if (Global.TRADE_NO_SET.add(out_trade_no)) {
                if (epaylinks.verifySign()) {
                    logger.info("----验证签名成功-------");
                    logger.info("订单" + out_trade_no + "易票联支付系统扣款成功，下面开始充值...");
                    log.setRemark("网上支付,易票联支付：" + out_trade_no);
                    accountService.newRecharge(out_trade_no, signStr, log, paymentInterface);
                    message("充值成功！", "/member/account/recharge.html");
                    logger.info("单号：" + out_trade_no + "充值成功！！！");
                    result = "success";
                } else {
                    logger.info("----验证签名失败-------");
                    logger.info("订单：" + out_trade_no + "双乾支付系统支付失败.. ");
                    accountService.failRecharge(out_trade_no, signStr, log);
                    message("充值失败！", "/member/account/recharge.html");
                    result = "fail";
                }
            } else {
                logger.info("----易票联订单号为" + out_trade_no + "的订单已经处理过，请勿重复处理-------");
            }
        } catch (AccountException e) {
            logger.info("**********" + e.getMessage() + "，交易流水号:" + out_trade_no + "**********");
        } catch (Exception e) {
            logger.info("**********充值异常，" + e.getMessage() + "交易流水号:" + out_trade_no + "**********");
        } finally {
            Global.TRADE_NO_SET.remove(out_trade_no);
        }
        logger.info("Recharge Result:" + result);
        return result;
    }

    // v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 end
    // v1.6.7.2 RDPROJECT-512 wcw 2013-12-05 start
    private String ebatong_param(PaymentInterface paymentInterface) throws Exception {
        // 得到回传参数，并重新构建，iso-8859-1解决乱码问题
        Map<String, String> paramMap = new HashMap<String, String>(request.getParameterMap());
        for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object value = entry.getValue();
            String paramValue;
            if (value == null) {
                entry.setValue(null);
            } else if (value instanceof String) {
                paramValue = (String) value;
                entry.setValue(paramValue);
            } else if (value instanceof String[]) {
                String[] values = (String[]) value;
                paramValue = (String) values[0];
                entry.setValue(paramValue);
            }
        }
        String input_charset = "UTF-8";
        Md5Encrypt.setCharset(input_charset);
        for (Entry<String, String> entry : paramMap.entrySet()) {
            paramMap.put(entry.getKey(), (new String(entry.getValue().getBytes("iso-8859-1"), input_charset)));
        }
        paramMap.remove("sign");

        List<String> keyList = new ArrayList<String>(paramMap.keySet());
        Collections.sort(keyList);

        StringBuffer strBuf = new StringBuffer();
        for (String paramName : keyList) {
            String paramValue = paramMap.get(paramName);
            Boolean a = false;
            if (paramValue != null || !paramValue.trim().equals("")) {
                a = true;
            }
            strBuf.append((strBuf.length() > 0 ? "&" : "") + paramName + "=" + (a ? paramValue : ""));
        }

        // 加密(MD5加签)，默认已取UTF-8字符集，如果需要更改，可调用Md5Encrypt.setCharset(inputCharset)
        String paramString = strBuf.toString();
        String key = paymentInterface.getKey();
        paramString = new StringBuilder(paramString).append(key).toString();
        logger.info("paramString========" + paramString);
        return paramString;
    }

    /**
     * 易八通通知返回主方法
     * 
     * @return 字符串
     * @throws Exception
     */
    private String ebatong() throws Exception {
        logger.info("易八通通知返回成功");
        String sign = paramString("sign");
        logger.info("requestSign========" + sign);
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.EBATONG_PAY.getValue());
        String paramString = ebatong_param(paymentInterface);
        String signl = Md5Encrypt.encrypt(paramString);
        logger.info("sign========" + signl);
        String out_trade_no = paramString("out_trade_no");
        logger.info("易八通订单号为============" + out_trade_no);
        String trade_status = paramString("trade_status");
        String notify_id = "";
        try {
            if (Global.TRADE_NO_SET.add(out_trade_no)) {
                if (signl.equals(sign)) {
                    logger.info("验签成功");
                    if ("TRADE_FINISHED".equals(trade_status)) {
                        logger.info("订单号" + out_trade_no + "的订单交易状态" + trade_status + "(成功)");
                        logger.info("订单" + out_trade_no + "易八通支付系统扣款成功，下面开始充值...");
                        accountService.newRecharge(out_trade_no, paramString, new AccountLog(), paymentInterface);
                        message("充值成功！", "/member/account/recharge.html");
                        logger.info("单号：" + out_trade_no + "充值成功！！！");
                        notify_id = paramString("notify_id");
                    } else {
                        logger.info("----订单号" + out_trade_no + "的订单交易状态" + trade_status + "(失败)-------");
                        logger.info("订单：" + out_trade_no + "易八通支付系统支付失败.. ");
                        accountService.failRecharge(out_trade_no, paramString, new AccountLog());
                        message("充值失败！", "/member/account/recharge.html");
                        notify_id = " pay fail";
                    }
                } else {
                    logger.info("----验证签名失败-------");
                    notify_id = "pay fail";
                }
            } else {
                logger.info("------易八通订单号为" + out_trade_no + "的订单已经处理-----");
            }
        } catch (AccountException e) {
            logger.info("**********" + e.getMessage() + "，交易流水号:" + out_trade_no + "重复充值！**********");
        } catch (Exception e) {
            logger.info("**********充值异常:" + e.getMessage() + "，交易流水号:" + out_trade_no + "重复充值！**********");
        } finally {
            Global.TRADE_NO_SET.remove(out_trade_no);
        }
        logger.info("Recharge Result:" + notify_id);
        return notify_id;
    }

    /**
     * 易八通支付 同步通知（页面返回）
     */
    public String ebatong_return() throws Exception {
        ebatong();
        return MSG;
    }

    /**
     * 易八通支付 异步通知（后台返回）
     */
    public String ebatong_notify() throws Exception {
        String notify_id = ebatong();
        if (!StringUtils.isBlank(notify_id)) {
            PrintWriter p = response.getWriter();
            p.print(notify_id);
            p.flush();
            p.close();
        }
        return null;
    }

    // v1.6.7.2 RDPROJECT-512 wcw 2013-12-05 end

    /**
     * 银生支付
     * 
     * @return 跳转页面
     * @throws Exception
     *             异常
     */
    private String unspay() throws Exception {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.UNSPAY.getValue());
        logger.info("银生支付回调成功，已经进入方法...");
        AccountLog log = new AccountLog(1L, Constant.RECHARGE, 1L, getTimeStr(), getRequestIp());
        Unspay unspay = new Unspay();
        UnspayNotify(unspay);
        unspay.setMerchantKey(paymentInterface.getKey());
        String merchantId = unspay.getParameter("merchantId");
        String responseMode = unspay.getParameter("responseMode");
        String orderId = unspay.getParameter("orderId");
        logger.info("银生支付订单号为==========" + orderId);
        String currencyType = unspay.getParameter("currencyType");
        String amount = unspay.getParameter("amount");
        String returnCode = unspay.getParameter("returnCode");
        String returnMessage = unspay.getParameter("returnMessage");
        String mac = unspay.getParameter("mac");
        boolean success = "0000".equals(returnCode);
        boolean paid = "0001".equals(returnCode);
        StringBuffer s = new StringBuffer(50);
        // 拼成数据串
        s.append("merchantId=").append(merchantId);
        s.append("&responseMode=").append(responseMode);
        s.append("&orderId=").append(orderId);
        s.append("&currencyType=").append(currencyType);
        s.append("&amount=").append(amount);
        s.append("&returnCode=").append(returnCode);
        s.append("&returnMessage=").append(returnMessage);
        s.append("&merchantKey=").append(unspay.getMerchantKey());
        String result = "";
        // md5加密
        String nowMac = new MD5().getMD5ofStr(s.toString());
        try {
            if (Global.TRADE_NO_SET.add(orderId)) {
                if (nowMac.equals(mac)) { // 若mac校验匹配
                    logger.info("----验证签名成功-------");
                    logger.info("订单" + orderId + "易票联支付系统扣款成功，下面开始充值...");
                    log.setRemark("网上支付,银生支付：" + orderId);
                    accountService.newRecharge(orderId, s.toString(), log, paymentInterface);
                    message("充值成功！", "/member/account/recharge.html");
                    logger.info("单号：" + orderId + "充值成功！！！");
                    result = "success";
                } else { // 若mac校验不匹配
                    logger.info("----验证签名失败1-------");
                    if (success || paid) {
                        success = false;
                        paid = false;
                        returnCode = "0401";
                        returnMessage = "mac值校验错误！";
                        result = "fail";
                        accountService.failRecharge(orderId, s.toString(), log);
                        message("充值失败！", "/member/account/recharge.html");
                        logger.info("----验证签名失败2-------");
                    }
                }
            } else {
                logger.info("银生支付订单号为：" + orderId + "的订单已经进行处理，请勿重复处理.. ");
            }
        } catch (AccountException e) {
            logger.info("**********" + e.getMessage() + "，交易流水号:" + orderId + "重复充值！**********");
        } catch (Exception e) {
            logger.info("**********充值异常:" + e.getMessage() + "，交易流水号:" + orderId + "重复充值！**********");
        } finally {
            Global.TRADE_NO_SET.remove(orderId);
        }
        logger.info("Recharge Result:" + result);
        return result;
    }

    // v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 end
    /**
     * 银生支付 同步通知（页面返回）
     * 
     * @return
     * @throws Exception
     */
    public String unspay_return() throws Exception {
        unspay();
        return MSG;
    }

    /**
     * 银生支付 异步通知 （后台返回）
     * 
     * @return
     * @throws Exception
     */
    public String unspay_notify() throws Exception {
        String result = unspay();
        if (!StringUtils.isBlank(result)) {
            PrintWriter p = response.getWriter();
            p.print(result);
            p.flush();
            p.close();
        }
        return null;
    }

    public GoPay gopayCallback(PaymentInterface paymentInterface) {
        GoPay pay = new GoPay();
        if (paymentInterface != null) {
            pay.setPrivateKey(paymentInterface.getKey());
        }
        TranGood good = this.createTranGood();
        pay.setGood(good);
        pay.setTranIP(paramString("tranIP"));
        pay.setFrontMerUrl(paramString("frontMerUrl"));
        pay.setBackgroundMerUrl(paramString("backgroundMerUrl"));

        pay.setVersion(paramString("version"));
        pay.setTranCode(paramString("tranCode"));
        pay.setMerchantID(paramString("merchantID"));
        pay.setOrderId(paramString("orderId"));
        pay.setGopayOutOrderId(paramString("gopayOutOrderId"));
        pay.setRespCode(paramString("respCode"));
        pay.setServetime(paramString("gopayServerTime"));
        String plain = pay.getSignValue();
        logger.debug(plain);
        return pay;
    }

    public IPSPay ipspayCallback(PaymentInterface paymentInterface) {
        String billno = paramString("billno");
        String currency_type = paramString("Currency_type");
        String amount = paramString("amount");
        String date = paramString("date");
        String succ = paramString("succ");
        String msg = paramString("msg");
        String attach = paramString("attach");
        String ipsbillno = paramString("ipsbillno");
        String retEncodeType = paramString("retencodetype");
        String signature = paramString("signature");
        IPSPay ips = new IPSPay();
        ips.setBillno(billno);
        ips.setCurrency_Type(currency_type);
        ips.setAmount(amount);
        ips.setDate(date);
        ips.setSucc(succ);
        ips.setMsg(msg);
        ips.setAttach(attach);
        ips.setIpsbillno(ipsbillno);
        ips.setRetEncodeType(retEncodeType);
        ips.setSignature(signature);
        if (paymentInterface != null) {
            ips.setMer_key(paymentInterface.getKey());
        }
        return ips;
    }

    public ShengPay shengpayCallback() {
        String Name = paramString("Name");
        String Version = paramString("Version");
        String Charset = paramString("Charset");
        String TraceNo = paramString("TraceNo");
        String MsgSender = paramString("MsgSender");
        String SendTime = paramString("SendTime");
        String MerchantNo = paramString("MerchantNo");
        String InstCode = paramString("InstCode");
        String OrderNo = paramString("OrderNo");
        String OrderAmount = paramString("OrderAmount");
        String TransNo = paramString("TransNo");
        String TransAmount = paramString("TransAmount");
        String TransStatus = paramString("TransStatus");
        String TransType = paramString("TransType");
        String TransTime = paramString("TransTime");
        String ErrorCode = paramString("ErrorCode");
        String ErrorMsg = paramString("ErrorMsg");
        String Ext1 = paramString("Ext1");
        String SignType = paramString("SignType");
        String SignMsg = paramString("SignMsg");
        ShengPay spay = new ShengPay();
        spay.setName(Name);
        spay.setVersion(Version);
        spay.setCharset(Charset);
        spay.setTraceNo(TraceNo);
        spay.setMsgSender(MsgSender);
        spay.setSendTime(SendTime);
        spay.setMerchantNo(MerchantNo);
        spay.setInstCode(InstCode);
        spay.setOrderNo(OrderNo);
        spay.setOrderAmount(OrderAmount);
        spay.setTransNo(TransNo);
        spay.setTransAmount(TransAmount);
        spay.setTransStatus(TransStatus);
        spay.setTransType(TransType);
        spay.setTransTime(TransTime);
        spay.setErrorCode(ErrorCode);
        spay.setErrorMsg(ErrorMsg);
        spay.setExt1(Ext1);
        spay.setSignType(SignType);
        spay.setSignMsg(SignMsg);
        return spay;
    }

    private TranGood createTranGood() {
        TranGood good = new TranGood();
        good.setTranDateTime(paramString("tranDateTime"));
        good.setMerOrderNum(paramString("merOrderNum"));
        // 设置交易金额
        good.setTranAmt(paramString("tranAmt"));
        good.setFeeAmt(paramString("feeAmt"));
        // 设置交易商品信息
        good.setGoodsName(Global.getValue("webname"));
        return good;
    }

    public String blank() throws Exception {
        return SUCCESS;
    }

}
