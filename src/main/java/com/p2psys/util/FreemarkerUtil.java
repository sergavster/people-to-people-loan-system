package com.p2psys.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import com.p2psys.domain.AccountRecharge;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * freemarker的模板处理工具类
 * 
 * 未经授权不得进行修改、复制、出售及商业使用。  (c)</b> 杭州科技有限公司-<br/>
 * 
 * @ClassName: FreemarkerUtil
 * @Description:
 
 * @date 2013-8-21 下午3:54:17
 */
public class FreemarkerUtil {

    public static Configuration CONFIG;
    public static String[] str = { "recharge", "money", "award", "credited", "deduct", "tenderAccount" };

    public static String renderTemplate(String s, Map<String, Object> data) throws IOException, TemplateException {
        // v1.6.7.2 RDPROJECT-484 wcw 2013-12-19 start
        try {
            getTwoDecimalPlaces(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // v1.6.7.2 RDPROJECT-484 wcw 2013-12-19 end
        Template t = new Template(null, new StringReader(s), CONFIG);
        // 执行插值，并输出到指定的输出流中
        StringWriter w = new StringWriter();
        t.getConfiguration();
        t.process(data, w);
        return w.getBuffer().toString();
    }

    // v1.6.7.2 RDPROJECT-484 wcw 2013-12-19 start
    /**
     * log模板中浮点型数据截取小数点后两位
     * wcw
     * @param data map
     */
    private static void getTwoDecimalPlaces(Map<String, Object> data) {
        String moneyStr = "";
        for (String name : str) {
            // AccountRecharge 对象
            if ("recharge".equals(name)) {
                AccountRecharge recharge = (AccountRecharge) data.get(name);
                if (recharge != null) {
                    moneyStr = recharge.getMoney() + "";
                    double money = getTwoDecimalPlaces(moneyStr);
                    recharge.setMoney(money);
                    data.put(name, recharge);
                    break;
                }
            }
            moneyStr = StringUtils.isNull(data.get(name));
            if (!StringUtils.isBlank(moneyStr)) {
                double money = getTwoDecimalPlaces(moneyStr);
                data.put(name, money);
                break;
            }
        }
    }

    /**
     * 截取double类型小数点后两位小数主方法
     * 
     * @param moneyStr double类型字符串
     * @return money(两位小数)
     */
    public static double getTwoDecimalPlaces(String moneyStr) {
        double money = NumberUtils.getDouble(moneyStr);
        BigDecimalUtil bigDecimalUtil = new BigDecimalUtil();
        money = bigDecimalUtil.decimal(money, 2);
        return money;
    }

    // v1.6.7.2 RDPROJECT-484 wcw 2013-12-19 end
    public static String renderFileTemplate(String file, Map<String, Object> data) throws IOException,
            TemplateException {
        Configuration cfg = CONFIG;
        cfg.setDefaultEncoding("UTF-8");
        // 取得模板文件
        Template t = cfg.getTemplate(file);
        // 执行插值，并输出到指定的输出流中
        StringWriter w = new StringWriter();
        t.getConfiguration();
        t.process(data, w);
        return w.getBuffer().toString();
    }

}
