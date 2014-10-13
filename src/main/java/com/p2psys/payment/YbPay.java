package com.p2psys.payment;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.PaymentInterface;
public class YbPay {
	private final static Logger logger=Logger.getLogger(YbPay.class);
	private String p0_Cmd;
	private String p1_MerId;
	private String p2_Order;
	private String p3_Amt;
	private String p4_Cur;
	private String p5_Pid;
	private String nodeAuthorizationURL;
	private String keyValue;
	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getNodeAuthorizationURL() {
		return nodeAuthorizationURL;
	}

	public void setNodeAuthorizationURL(String nodeAuthorizationURL) {
		this.nodeAuthorizationURL = nodeAuthorizationURL;
	}
	private static Object lock              = new Object();
	private static YbPay ybpay     = null;
	private static ResourceBundle rb        = null;
	private static final String CONFIG_FILE = "merchantInfo";
	private static String encodingCharset = "UTF-8";
	public YbPay() {
		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}
	
	public static YbPay getInstance() {
		synchronized(lock) {
			if(null == ybpay) {
				ybpay = new YbPay();
			}
		}
		return (ybpay);
	}
	
	public String getValue(String key) {
		return (rb.getString(key));
	}
	
	public static String getReqMd5HmacForOnlinePayment(String p0_Cmd,String p1_MerId,
			String p2_Order, String p3_Amt, String p4_Cur,String p5_Pid, String p6_Pcat,
			String p7_Pdesc,String p8_Url, String p9_SAF,String pa_MP,String pd_FrpId,
			String pr_NeedResponse,String keyValue) {
		  StringBuffer sValue = new StringBuffer();
		// 业务类型
		sValue.append(p0_Cmd);
		// 商户编号
		sValue.append(p1_MerId);
		// 商户订单号
		sValue.append(p2_Order);
		// 支付金额
		sValue.append(p3_Amt);
		// 交易币种
		sValue.append(p4_Cur);
		// 商品名称
		sValue.append(p5_Pid);
		// 商品种类
		sValue.append(p6_Pcat);
		// 商品描述
		sValue.append(p7_Pdesc);
		// 商户接收支付成功数据的地址
		sValue.append(p8_Url);
		// 送货地址
		sValue.append(p9_SAF);
		// 商户扩展信息
		sValue.append(pa_MP);
		// 银行编码
		sValue.append(pd_FrpId);
		// 应答机制
		sValue.append(pr_NeedResponse);
		
		String sNewString = null;

		sNewString = YbPay.hmacSign(sValue.toString(), keyValue);
		return (sNewString);
	}
    public void init(YbPay ybpay,PaymentInterface paymentInterface){
      //v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 start
    	if(paymentInterface!=null){	
			ybpay.setKeyValue(paymentInterface.getKey());
			ybpay.setP1_MerId(paymentInterface.getMerchant_id());
			ybpay.setP8_Url(Global.getValue("weburl") +paymentInterface.getReturn_url());
		}
    	//v1.6.7.2  RDPROJECT-546 wcw 2013-12-11 end
    }
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes(encodingCharset);
			value = aValue.getBytes(encodingCharset);
			
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}
	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

	public static boolean verifyCallback(String hmac, String p1_MerId,
			String r0_Cmd, String r1_Code, String r2_TrxId, String r3_Amt,
			String r4_Cur, String r5_Pid, String r6_Order, String r7_Uid,
			String r8_MP, String r9_BType, String keyValue) {
		StringBuffer sValue = new StringBuffer();
		// 商户编号
		sValue.append(p1_MerId);
		// 业务类型
		sValue.append(r0_Cmd);
		// 支付结果
		sValue.append(r1_Code);
		// 易宝支付交易流水号
		sValue.append(r2_TrxId);
		// 支付金额
		sValue.append(r3_Amt);
		// 交易币种
		sValue.append(r4_Cur);
		// 商品名称
		sValue.append(r5_Pid);
		// 商户订单号
		sValue.append(r6_Order);
		// 易宝支付会员ID
		sValue.append(r7_Uid);
		// 商户扩展信息
		sValue.append(r8_MP);
		// 交易结果返回类型
		sValue.append(r9_BType);
		String sNewString = null;
		sNewString = YbPay.hmacSign(sValue.toString(), keyValue);
        logger.debug("sNewString======="+sNewString);
        logger.debug("hmac======="+hmac);
		if (hmac.equals(sNewString)) {
			return (true);
		}
		return (false);
	}
	public String getP0_Cmd() {
		return p0_Cmd;
	}
	public void setP0_Cmd(String p0_Cmd) {
		this.p0_Cmd = p0_Cmd;
	}
	public String getP1_MerId() {
		return p1_MerId;
	}
	public void setP1_MerId(String p1_MerId) {
		this.p1_MerId = p1_MerId;
	}
	public String getP2_Order() {
		return p2_Order;
	}
	public void setP2_Order(String p2_Order) {
		this.p2_Order = p2_Order;
	}
	public String getP3_Amt() {
		return p3_Amt;
	}
	public void setP3_Amt(String p3_Amt) {
		this.p3_Amt = p3_Amt;
	}
	public String getP4_Cur() {
		return p4_Cur;
	}
	public void setP4_Cur(String p4_Cur) {
		this.p4_Cur = p4_Cur;
	}
	public String getP5_Pid() {
		return p5_Pid;
	}
	public void setP5_Pid(String p5_Pid) {
		this.p5_Pid = p5_Pid;
	}
	public String getP6_Pcat() {
		return p6_Pcat;
	}
	public void setP6_Pcat(String p6_Pcat) {
		this.p6_Pcat = p6_Pcat;
	}
	public String getP7_Pdesc() {
		return p7_Pdesc;
	}
	public void setP7_Pdesc(String p7_Pdesc) {
		this.p7_Pdesc = p7_Pdesc;
	}
	public String getP8_Url() {
		return p8_Url;
	}
	public void setP8_Url(String p8_Url) {
		this.p8_Url = p8_Url;
	}
	public String getP9_SAF() {
		return p9_SAF;
	}
	public void setP9_SAF(String p9_SAF) {
		this.p9_SAF = p9_SAF;
	}
	public String getPa_MP() {
		return pa_MP;
	}
	public void setPa_MP(String pa_MP) {
		this.pa_MP = pa_MP;
	}
	@Override
	public String toString() {
		return "YbPay [p0_Cmd=" + p0_Cmd + ", p1_MerId=" + p1_MerId
				+ ", p2_Order=" + p2_Order + ", p3_Amt=" + p3_Amt + ", p4_Cur="
				+ p4_Cur + ", p5_Pid=" + p5_Pid + ", nodeAuthorizationURL="
				+ nodeAuthorizationURL + ", keyValue=" + keyValue
				+ ", p6_Pcat=" + p6_Pcat + ", p7_Pdesc=" + p7_Pdesc
				+ ", p8_Url=" + p8_Url + ", p9_SAF=" + p9_SAF + ", pa_MP="
				+ pa_MP + ", pd_FrpId=" + pd_FrpId + ", pr_NeedResponse="
				+ pr_NeedResponse + ", hmac=" + hmac + "]";
	}
	public String getPd_FrpId() {
		return pd_FrpId;
	}
	public void setPd_FrpId(String pd_FrpId) {
		this.pd_FrpId = pd_FrpId;
	}
	public String getPr_NeedResponse() {
		return pr_NeedResponse;
	}
	public void setPr_NeedResponse(String pr_NeedResponse) {
		this.pr_NeedResponse = pr_NeedResponse;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	private String p6_Pcat;
	private String p7_Pdesc;
	private String p8_Url;
	private String p9_SAF;
	private String pa_MP;
	private String pd_FrpId;
	private String pr_NeedResponse;
	private String hmac;
}
