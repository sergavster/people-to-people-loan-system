package com.p2psys.tool.order;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.p2psys.util.StringUtils;
import com.p2psys.web.action.admin.ManageOrderAction;

public class orderTool {
    private static Logger logger = Logger.getLogger(orderTool.class);
    /**
     * 通过提交url获取远程服务器返回的xml字符串
     * @param url 提交url
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
                logger.info("Method failed: "
                        + getMethod.getStatusLine());
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
     * 去除除数字之外的字符
     * @param time 时间
     * @return  时间
     */
    public String getTimeConvert(String time) {
        if (!StringUtils.isBlank(time)) {
            if (time.contains("-")) {
                time = time.replace("-", "");
            }
            if (time.contains(":")) {
                time = time.replace(":", "");
            }else{
                time=time+"000000";
            }
            if (time.contains(" ")) {
                time = time.replace(" ", "");
            }
        }
        return time;
    }
}
