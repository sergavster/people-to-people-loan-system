package com.p2psys.web.action.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.mypay.merchantutil.Md5Encrypt;
import com.p2psys.common.enums.EnumPayInterface;
import com.p2psys.domain.AccountRecharge;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.model.paymentInterface.BaofooOrder;
import com.p2psys.payment.EbatongPay;
import com.p2psys.service.AccountService;
import com.p2psys.tool.coder.BASE64Encoder;
import com.p2psys.tool.coder.MD5;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.tool.order.orderTool;
import com.p2psys.tool.order.ebatong.EbatongOrder;
import com.p2psys.tool.order.ecpss.EcpssPayOrder;
import com.p2psys.tool.order.ips.GetOrderByBankNo;
import com.p2psys.tool.order.ips.GetOrderByTime;
import com.p2psys.tool.order.ips.GetOrderMsgByNo;
import com.p2psys.tool.order.ips.OrderMsg;
import com.p2psys.tool.order.ips.OrderRecord;
import com.p2psys.util.DateUtils;
import com.p2psys.util.HttpUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 订单管理Action RDPROJECT-28
 * 
 
 * @date 2012-10-18 下午2:54:43
 * @version
 * 
 *  (c)</b> 2012-rongdu-<br/>
 * 
 */
public class ManageOrderAction extends BaseAction {

    private static Logger logger = Logger.getLogger(ManageOrderAction.class);

    private AccountService accountService;

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 汇潮充值记录查询、报表导出 主方法
     * 
     * @return forward路径
     * @throws Exception
     *             异常
     */
    public String ecpssPayOrderList() throws Exception {
        String type = paramString("type");
        String url = getSumitUrl();
        orderTool orderTool = new orderTool();
        String xml = orderTool.getOrderXmlStringBuffer(url);
        // 处理内容
        EcpssPayOrder ecpssPayOrder = new EcpssPayOrder();
        EcpssPayOrder order = ecpssPayOrder.getOrderList(xml);
        List list = new ArrayList();
        if (order != null) {
            request.setAttribute("eorder", order);
            int resultCount = order.getResultCount();
            int pagesize = order.getPageSize();
            if (pagesize > 0) {
                int count = resultCount / pagesize + 1;
                List countList = new ArrayList();
                for (int i = 0; i < count; i++) {
                    countList.add(i);
                }
                request.setAttribute("countList", countList);
                request.setAttribute("count", count);
            }
            list = order.getList();
            request.setAttribute("resultCount", resultCount);
            request.setAttribute("list", list);
            logger.info("count======" + resultCount);
        }
        if (StringUtils.isBlank(type)) {
            return SUCCESS;
        } else {
            String contextPath = ServletActionContext.getServletContext().getRealPath("/");
            String downloadFile = "ecpssPayOrder_" + System.currentTimeMillis() + ".xls";
            String infile = contextPath + "/data/export/" + downloadFile;
            String[] names;
            String[] titles;
            names = new String[] { "orderAmount", "orderDate", "orderNumber", "orderStatus", "gouduiStatus" };
            titles = new String[] { "订单金额", "订单交易时间", "交易的订单号", "订单的状态", "勾兑状态" };
            ExcelHelper.writeExcel(infile, list, EcpssPayOrder.class, Arrays.asList(names), Arrays.asList(titles));
            this.export(infile, downloadFile);
            return null;
        }
    }

    /**
     * 宝付接口查询接口主方法 2013-11-08
     */
    public String baofooOrder() {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.BAOFOO_PAY.getValue());
        String TransID = paramString("TransID");
        if (StringUtils.isBlank(TransID)) {
            return SUCCESS;
        }
        String md5key = paymentInterface.getKey();
        String MerchantID = paymentInterface.getMerchant_id();
        String Md5Sign = MerchantID + TransID + md5key;
        MD5 md5 = new MD5();
        Md5Sign = md5.getMD5ofStr(Md5Sign);// 计算MD5值

        String url = paymentInterface.getOrderInquireUrl();
        StringBuffer sb = new StringBuffer();
        sb.append(url).append("?MerchantID=").append(MerchantID).append("&TransID=").append(TransID)
                .append("&Md5Sign=").append(Md5Sign);
        String submitUrl = sb.toString();
        String s = HttpUtils.getHttpResponse(submitUrl);
        BaofooOrder baofooOrder = new BaofooOrder();
        // v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
        String[] returnParamter = s.split("\\|");
        baofooOrder.setMerchantID(returnParamter[0]);
        baofooOrder.setTransID(returnParamter[1]);
        baofooOrder.setCheckResult(returnParamter[2]);
        baofooOrder.setFactMoney(returnParamter[3]);
        baofooOrder.setSuccTime(returnParamter[4]);
        baofooOrder.setMd5Sign(returnParamter[5]);
        // v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
        request.setAttribute("baofooOrder", baofooOrder);
        return SUCCESS;

    }

    /**
     * 易八联通接口查询接口主方法 2013-11-08
     */
    public String ebatongOrder() {
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.EBATONG_PAY.getValue());
        String out_trade_no = paramString("out_trade_no");
        if (StringUtils.isBlank(out_trade_no)) {
            return SUCCESS;
        }
        EbatongPay ebatongPay = new EbatongPay();
        String partner = paymentInterface.getMerchant_id();
        String service = "single_direct_query";
        String sign_type = "MD5";
        String sign = getEbatongSign(paymentInterface);
        String url = paymentInterface.getOrderInquireUrl();
        StringBuffer sb = new StringBuffer();
        sb.append(url).append("?out_trade_no=").append(out_trade_no).append("&partner=").append(partner)
                .append("&service=").append(service).append("&sign_type=").append(sign_type).append("&sign=")
                .append(sign);
        String submitUrl = sb.toString();
        logger.info("submitUrl=======" + submitUrl);
        orderTool getXmlStr = new orderTool();
        String xml = getXmlStr.getOrderXmlStringBuffer(submitUrl);
        logger.info("xml======" + xml);
        EbatongOrder ebatongOrder = ebatongPay.getOrderList(xml);
        // if(sign.equals(ebatongOrder.getSign())){
        request.setAttribute("ebatongOrder", ebatongOrder);
        // }
        return SUCCESS;

    }

    /**
     * 获取易八通sign
     * 
     * @param paymentInterface
     * @return
     */
    private String getEbatongSign(PaymentInterface paymentInterface) {
        AccountRecharge accountRecharge = accountService.getRecharge(paramLong("id"));
        // 加密(MD5加签)，默认已取UTF-8字符集，如果需要更改，可调用Md5Encrypt.setCharset(inputCharset)
        logger.info("returnText======" + accountRecharge.getReturntext());
        String sign = Md5Encrypt.encrypt(accountRecharge.getReturntext());
        logger.info("sign=============" + sign);
        return sign;
    }

    /**
     * 环迅接口查询接口主方法 2013-11-08
     */
    public String ipsOrder() {
        String type = paramString("type");
        int selectType = paramInt("selectType");
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.IPS_PAY.getValue());
        if (paymentInterface != null) {
            String TradeType = "NT";
            int Page = paramInt("Page");
            int Max = paramInt("Max");
            OrderMsg newOrderMsg = new OrderMsg();
            OrderMsg orderMsg = new OrderMsg();
            String submitUrl = "";
            GetOrderByTime getOrderByTime = new GetOrderByTime();
            GetOrderByBankNo getOrderByBankNo = new GetOrderByBankNo();
            GetOrderMsgByNo getOrderMsgByNo = new GetOrderMsgByNo();
            String xml = "";
            Page = (Page == 0) ? 1 : Page;
            Max = (Max == 0) ? 1000 : Max;
            String StartTime = paramString("StartTime");
            String EndTime = paramString("EndTime");
            orderTool o=new orderTool();
            StartTime=o.getTimeConvert(StartTime);
            EndTime=o.getTimeConvert(EndTime);
            StartTime = StringUtils.isBlank(StartTime) ? "19700101010101" : StartTime;
            EndTime = StringUtils.isBlank(EndTime) ? "20221101010101" : EndTime;
            // selectType 1:按时间
            if (selectType == 1) {
                try {
                    getOrderByTime = new GetOrderByTime(paymentInterface.getMerchant_id(), "", TradeType, StartTime,
                            EndTime, Page, Max);
                    submitUrl = getOrderByTime.orderByTime(getOrderByTime, paymentInterface);
                    xml = newOrderMsg.getOrderXmlStringBuffer(submitUrl);
                    orderMsg = newOrderMsg.getOrderList(xml);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 按银行订单号
            if (selectType == 2) {
                try {
                    String BankNo = paramString("BankNo");
                    getOrderByBankNo = new GetOrderByBankNo(paymentInterface.getMerchant_id(), "", TradeType,
                            StartTime, EndTime, Page, BankNo, Max);
                    submitUrl = getOrderByBankNo.orderByBankNo(getOrderByBankNo, paymentInterface);
                    xml = newOrderMsg.getOrderXmlStringBuffer(submitUrl);
                    orderMsg = newOrderMsg.getOrderList(xml);
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
            // 按订单号查询
            if (selectType == 3) {
                try {
                    String StartNo = paramString("StartNo");
                    String EndNo = paramString("EndNo");
                    getOrderMsgByNo = new GetOrderMsgByNo(paymentInterface.getMerchant_id(), "", TradeType, StartNo,
                            EndNo, Page, Max);
                    submitUrl = getOrderMsgByNo.orderByNo(getOrderMsgByNo, paymentInterface);
                    xml = newOrderMsg.getOrderXmlStringBuffer(submitUrl);
                    logger.info("xml======" + xml);
                    orderMsg = newOrderMsg.getOrderList(xml);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("xml======" + xml);
            int total = orderMsg.getTotal();
            int count = orderMsg.getCount();
            int totalPage = 0;
            if (count != 0)
                totalPage = total / count;
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("orderMsg", orderMsg);
            request.setAttribute("selectType", selectType);
            
            if (StringUtils.isBlank(type)) {
                return SUCCESS;
            } else {
                String contextPath = ServletActionContext.getServletContext().getRealPath("/");
                String downloadFile = "orderRecord_" + System.currentTimeMillis() + ".xls";
                String infile = contextPath + "/data/export/" + downloadFile;
                String[] names;
                String[] titles;
                names = new String[] {  "orderNo", "IPSOrderNo", "trd_Code", "cr_Code","amount","merchantOrderTime","IPSOrderTime","flag","attach" };
                titles = new String[] {  "商户订单号", "ips订单号", "交易代码", "交易币种","交易金额","商户时间","IPS订单时间","成功标志","订单备注信息"};
                try {
                    ExcelHelper.writeExcel(infile, orderMsg.getList(), OrderRecord.class, Arrays.asList(names), Arrays.asList(titles));
                    this.export(infile, downloadFile);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
        }
        return SUCCESS;

    }

    /**
     * 汇潮订单查询获取提交url
     * 
     * @return url
     */
    private String getSumitUrl() {
        int page = paramInt("page");
        if (page == 0) {
            page = 1;
        }
        request.setAttribute("page", page);
        if (page == 0) {
            page = 1;
        }
        // v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
        PaymentInterface paymentInterface = accountService.getPaymentInterface(EnumPayInterface.ECPSSFAST_PAY
                .getValue());
        String merNo = paymentInterface.getMerchant_id();
        String MD5key = paymentInterface.getKey();
        // v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
        // 获取商户号以及key，通过MD5加密获取sign（签名）
        // String merNo = Global.getValue("ecpsspay_fast_merchantID");
        // String MD5key = Global.getValue("ecpsspay_fast_mer_key");
        String signString = merNo + MD5key;
        MD5 md5 = new MD5();
        String sign = md5.getMD5ofStr(signString);
        String dotime1 = paramString("dotime1");
        String dotime2 = paramString("dotime2");
        orderTool orderTool = new orderTool();
        if (!StringUtils.isBlank(dotime1) && !StringUtils.isBlank(dotime2)) {
            dotime1 = orderTool.getTimeConvert(dotime1);
            dotime2 = orderTool.getTimeConvert(dotime2);
        } else {
            dotime1 = "20130117101010";
            dotime2 = "20131017101010";
        }
        String orderNumber = paramString("orderNumber");
        if ("请输入订单号".equals(orderNumber)) {
            orderNumber = "";
        }
        String aa = "<?xml version='1.0' encoding='utf-8'?>" + "<root tx='1001'>" + "<merCode>" + merNo + "</merCode>"
                + "<orderNumber>" + orderNumber + "</orderNumber>" + "<beginTime>" + dotime1 + "</beginTime>"
                + "<endTime>" + dotime2 + "</endTime>" + "<pageIndex>" + page + "</pageIndex>" + "<sign>" + sign
                + "</sign>" + "</root>";
        BASE64Encoder encoder = new BASE64Encoder();
        String requestDomain = encoder.encode(aa.toString().getBytes());
        requestDomain = "requestDomain=" + requestDomain;
        String postUrl = paymentInterface.getOrderInquireUrl();
        String url = postUrl + "?" + requestDomain;
        logger.info("url=======" + url);
        return url;
    }

}
