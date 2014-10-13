package com.p2psys.tool.order.ips;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jxl.common.Logger;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.p2psys.util.NumberUtils;
/**
 * 环迅接口返回全部结果
 
 *2013-11-15
 */
public class OrderMsg {
	private static Logger logger = Logger.getLogger(OrderMsg.class);
	public int Count; // 当前记录数
	public int Total; // 总计路数
	public int Page; // 当前页码
	public String ErrCode; // 异常代码=0000表示成功，其他表示失败
	public List<OrderRecord> list = new ArrayList<OrderRecord>(); // 查询返回记录集

	/**
	 * 通过提交url获取远程服务器返回的xml字符串
	 * 
	 * @param url
	 *            提交url
	 * @return xml字符串
	 */
	public String getOrderXmlStringBuffer(String url) {
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		// 创建GET方法的实例
		GetMethod getMethod = new GetMethod(url);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		String xml = "";
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.info("Method failed: " + getMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = getMethod.getResponseBody();
			xml = new String(responseBody);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			logger.info("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			logger.info("发生网络异常");
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return xml;
	}
	/**
	 * 将xml字符串转化成list集合形式
	 * @param xml xml字符串
	 * @return  list集合
	 */
	public  OrderMsg getOrderList(String xml) {
		/**
		 * 申明一个SAXBuilder解析对象
		 */
		SAXBuilder builder = new SAXBuilder();
		// 实例化map集合
		OrderMsg orderMsg= new OrderMsg();
		try {

			// 申明一个EcpssPayOrder对象来保存读到的数据(先根据XML节点编写相应的javaBean实体类)
			// 用SAXBuilder对象读到参数路径的文件到DOC对象中
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document

			Document document = sb.build(source);
			// 得到根节点
			//Element eltRoot = (Element) XPath.selectSingleNode(document,
			//		"/OrderMsg");
			Element eltRoot=document.getRootElement();
			// 得到子节点集合，得到的不包括属性
			List<Element> listChildNode = eltRoot.getChildren();
			//获得XML中的命名空间（XML中未定义可不写）
		    Namespace ns = eltRoot.getNamespace();
			// 循环解析得到的子节点集合，并且用对象接收添加到list集合中
			if(null!=eltRoot.getChild("Count", ns).getText()){
				orderMsg.setCount(NumberUtils.getInt(eltRoot.getChild("Count", ns).getText()));
			}
			if(null!=eltRoot.getChild("Total", ns).getText()){
				orderMsg.setTotal(NumberUtils.getInt(eltRoot.getChild("Total", ns).getText()));
			}
			if(null!=eltRoot.getChild("Page", ns).getText()){
				orderMsg.setPage(NumberUtils.getInt(eltRoot.getChild("Page", ns).getText()));
			}
			if(null!=eltRoot.getChild("ErrCode", ns).getText()){
				orderMsg.setErrCode(eltRoot.getChild("ErrCode", ns).getText());
			}
			List<OrderRecord> newlist=new ArrayList<OrderRecord>();
			for (Element e1 : listChildNode) {
				List<Element> newListChildNode = e1.getChildren();
				if (newListChildNode.size() > 0) {
					for (Element e : newListChildNode) {
						// 构造实体对象，保存数据
						OrderRecord orderRecord = new OrderRecord();
						// 如果子节点名为“name”的子节点内容不为空就用EcpssPayOrder对象保存起来
						logger.debug("value======"+e.getChild("OrderNo", ns).getText());

						if (e.getChild("OrderNo", ns).getText() != null)
							orderRecord.setOrderNo(e.getChild("OrderNo", ns).getText());
						// 如果子节点名为“sex”的子节点内容不为空就用EcpssPayOrder对象保存起来
						if (e.getChild("IPSOrderNo", ns).getText() != null)
							orderRecord.setIPSOrderNo(e.getChild("IPSOrderNo", ns).getText());
						// 如果子节点名为“age”的子节点内容不为空就用EcpssPayOrder对象保存起来
						if (e.getChild("Trd_Code", ns).getText()!= null)
							orderRecord.setTrd_Code(e.getChild("Trd_Code", ns).getText());
						// 如果子节点名为“sid”的属性内容不为空就用EcpssPayOrder对象保存起来
						if (e.getChild("Cr_Code", ns).getText()!= null)
							orderRecord.setCr_Code(e.getChild("Cr_Code", ns).getText());
						// 如果子节点名为“add”的属性内容不为空就用EcpssPayOrder对象保存起来
						if (null != e.getChild("Amount", ns).getText())
							orderRecord.setAmount(NumberUtils.getDouble(e.getChild("Amount", ns).getText()));
						if (null != e.getChild("MerchantOrderTime", ns).getText())
							orderRecord.setMerchantOrderTime(e.getChild("MerchantOrderTime", ns).getText());
						if (null !=e.getChild("IPSOrderTime", ns).getText())
							orderRecord.setIPSOrderTime(e.getChild("IPSOrderTime", ns).getText());
						if (null != e.getChild("Flag", ns).getText())
							orderRecord.setFlag(NumberUtils.getInt(e.getChild("Flag", ns).getText()));
						if (null != e.getChild("Attach", ns).getText())
							orderRecord.setAttach(e.getChild("Attach", ns).getText());
						if (null != e.getChild("Sign", ns).getText())
							orderRecord.setSign(e.getChild("Sign", ns).getText());
						newlist.add(orderRecord);
					}
				}

			}
			orderMsg.setList(newlist);
			// 将EcpssPayOrder对象添加到list集合中
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return orderMsg;

	}
	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

	public int getTotal() {
		return Total;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public int getPage() {
		return Page;
	}

	public void setPage(int page) {
		Page = page;
	}

	public String getErrCode() {
		return ErrCode;
	}

	public void setErrCode(String errCode) {
		ErrCode = errCode;
	}
	public List<OrderRecord> getList() {
		return list;
	}
	public void setList(List<OrderRecord> list) {
		this.list = list;
	}



}
