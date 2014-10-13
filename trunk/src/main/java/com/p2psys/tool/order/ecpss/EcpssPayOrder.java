package com.p2psys.tool.order.ecpss;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.p2psys.util.NumberUtils;
/**
 * 汇潮支付订单查询          RDPROJECT-28
 
 *
 */
public class EcpssPayOrder implements Serializable{
	private static final long serialVersionUID = -5290637298310888840L;
   
	private int resultCount;
	private int pageSize;
	private int pageIndex;
	private int resultCode;
	private String beginDate;
	private String endDate;
	private String orderNumber;
	private String orderDate;
	private String orderAmount;
	private String orderStatus;
	private String gouduiStatus;
	private String refundStatus;
	private List list=new ArrayList();
	
	/**
     * 汇潮将xml字符串转化成list集合形式
     * @param xml xml字符串
     * @return  list集合
     */
    public EcpssPayOrder getOrderList(String xml) {
        /**
         * 申明一个SAXBuilder解析对象
         */
        SAXBuilder builder = new SAXBuilder();
        // 实例化map集合
        List<EcpssPayOrder> list = new ArrayList<EcpssPayOrder>();
        EcpssPayOrder order=null;
        try {

            // 申明一个EcpssPayOrder对象来保存读到的数据(先根据XML节点编写相应的javaBean实体类)
            EcpssPayOrder ecpssPayOrder = null;
            // 用SAXBuilder对象读到参数路径的文件到DOC对象中
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document

            Document document = sb.build(source);
            // 得到根节点
             //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
            Element eltRoot=document.getRootElement();
             //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
            // 得到子节点集合，得到的不包括属性
            List<Element> listChildNode = eltRoot.getChildren();
            // 循环解析得到的子节点集合，并且用对象接收添加到list集合中
            order=new EcpssPayOrder();
            if(null!=eltRoot.getChildText("resultCount")){
                order.setResultCount(NumberUtils.getInt(eltRoot.getChildText("resultCount")));
            }
            if(null!=eltRoot.getChildText("beginDate")){
                order.setBeginDate(eltRoot.getChildText("beginDate"));
            }
            if(null!=eltRoot.getChildText("endDate")){
                order.setEndDate(eltRoot.getChildText("endDate"));
            }
            if(null!=eltRoot.getChildText("pageIndex")){
                order.setPageIndex(NumberUtils.getInt(eltRoot.getChildText("pageIndex")));
            }
            if(null!=eltRoot.getChildText("pageSize")){
                order.setPageSize(NumberUtils.getInt(eltRoot.getChildText("pageSize")));
            }
            if(null!=eltRoot.getChildText("resultCode")){
                order.setResultCode(NumberUtils.getInt(eltRoot.getChildText("resultCode")));
            }
            for (Element e1 : listChildNode) {
                List<Element> newListChildNode = e1.getChildren();
                if (newListChildNode.size() > 0) {
                    for (Element e : newListChildNode) {
                        // 构造实体对象，保存数据
                        ecpssPayOrder = new EcpssPayOrder();
                        // 如果子节点名为“name”的子节点内容不为空就用EcpssPayOrder对象保存起来
                        if (e.getChildText("orderNumber") != null)
                            ecpssPayOrder.setOrderNumber(e
                                    .getChildText("orderNumber"));
                        // 如果子节点名为“sex”的子节点内容不为空就用EcpssPayOrder对象保存起来
                        if (e.getChildText("orderDate") != null)
                            ecpssPayOrder.setOrderDate(e
                                    .getChildText("orderDate"));
                        // 如果子节点名为“age”的子节点内容不为空就用EcpssPayOrder对象保存起来
                        if (e.getChildText("orderAmount") != null)
                            ecpssPayOrder.setOrderAmount(e
                                    .getChildText("orderAmount"));
                        // 如果子节点名为“sid”的属性内容不为空就用EcpssPayOrder对象保存起来
                        if (e.getChildText("orderStatus") != null)
                            ecpssPayOrder.setOrderStatus(e
                                    .getChildText("orderStatus"));
                        // 如果子节点名为“add”的属性内容不为空就用EcpssPayOrder对象保存起来
                        if (null != e.getChildText("gouduiStatus"))
                            ecpssPayOrder.setGouduiStatus(e
                                    .getChildText("gouduiStatus"));
                        if (null != e.getChildText("refundStatus"))
                            ecpssPayOrder.setRefundStatus(e
                                    .getChildText("refundStatus"));
                        list.add(ecpssPayOrder);
                    }
                }
                order.setList(list);
                // 将EcpssPayOrder对象添加到list集合中
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return order;

    }
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getGouduiStatus() {
		return gouduiStatus;
	}
	public void setGouduiStatus(String gouduiStatus) {
		this.gouduiStatus = gouduiStatus;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
}
