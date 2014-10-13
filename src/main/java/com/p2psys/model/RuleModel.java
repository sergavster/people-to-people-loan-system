package com.p2psys.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.p2psys.domain.Rule;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 规则表实体
 
 *
 */
public class RuleModel extends Rule implements Serializable {

	private static final long serialVersionUID = 8971537069152829541L;
	
	private Logger logger =Logger.getLogger(RuleModel.class);
	
	public RuleModel(Rule rule) {
		if(rule != null){
			this.setId(rule.getId());
			this.setName(rule.getName());
			this.setStatus(rule.getStatus());
			this.setAddtime(rule.getAddtime());
			this.setNid(rule.getNid());
			this.setRemark(rule.getRemark());
			this.setRule_check(rule.getRule_check());
		}
	}
	
	public RuleModel() {
		super();
	}

	/**
	 * 根据JSON key 提取rule对象
	 * @param key JSON Key
	 * @return Object
	 */
	public RuleModel getRuleByKey(String key) {
		if (key == null) { return null; }
		String json=getValueStrByKey(key);
		RuleModel model=new RuleModel();
		model.setRule_check(json);
		return model;
	} 

	/**
	 * 根据JSON key 提取对象
	 * @param key JSON Key
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public Object getValueByKey(String key) {
		if (key == null) { return null; }
		Map<String, Object>	map = null;
		try {
			map=(Map<String, Object>) JSON.parse(this.getRule_check());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if(map==null) map=new HashMap<String, Object>();
		Object obj = map.get(key);
		return obj;
	} 

	/**
	 * 根据JSON key 提取List
	 * @param key JSON Key
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List getValueListByKey(String key) { 	
		Object obj = this.getValueByKey(key);
		if(obj==null) return new ArrayList();
		List list =new ArrayList();
		try {
			list = (List) obj;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	} 
	
	/**
	 * 根据JSON key 提取Double类型值
	 * @param key JSON Key
	 * @return Double
	 */
	public double getValueDoubleByKey(String key) {
		return NumberUtils.getDouble(StringUtils.isNull(getValueByKey(key)));
	}
	
	/**
	 * 根据JSON key 提取Integer类型值
	 * @param key JSON Key
	 * @return Integer
	 */
	public int getValueIntByKey(String key) {
		return NumberUtils.getInt(StringUtils.isNull(getValueByKey(key)));
	} 
	
	/**
	 * 根据JSON key 提取Long类型值
	 * @param key JSON Key
	 * @return Integer
	 */
	public long getValueLongByKey(String key) {
		return NumberUtils.getLong(StringUtils.isNull(getValueByKey(key)));
	} 
	
	/**
	 * 根据JSON key 提取Integer类型值
	 * @param key JSON Key
	 * @return Integer
	 */
	public float getValueFloatByKey(String key) {
		
		if(key == null || key.length() <= 0 ) return 0;
		
		Object obj = getValueByKey(key);
		
		String val = "";
		if (obj != null) {
			val = obj.toString();
		}
		
		if(val != null && val.length() > 0){
			return Float.parseFloat(val);
		}
		return 0;
	} 
	
	/**
	 * 根据JSON key 提取String类型值
	 * @param key JSON Key
	 * @return String
	 */
	public String getValueStrByKey(String key) {
		return StringUtils.isNull(getValueByKey(key));
	} 
	
	
	/**
	 * 根据JSON key 提取byte类型值
	 * @param key JSON Key
	 * @return Integer
	 */
	public byte getValueByteByKey(String key) {
		String str = StringUtils.isNull(getValueByKey(key));
		if(str==null||str.equals(""))
			return 0;
		byte ret=0;
		try {
			ret=Byte.parseByte(str);
		} catch (NumberFormatException e) {
			ret=0;
		}
		return ret;
	} 
	
	public static void main(String[] args) {
		
		RuleCheckModel t = new RuleCheckModel();
		List<Integer> list = new ArrayList<Integer> ();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(1);
		t.setBorrow_type(list);
		
		String str = JSON.toJSONString(t);
		RuleModel r = new RuleModel();
		
		
		String b = "{check_money_type:1,money_ratio:1,is_day:0,borrow_type:'102,103,105,110,113',aaa:55}";
		r.setRule_check(b);
		//System.out.println(str);
		
		String a =  r.getValueStrByKey("borrow_type");
		System.out.println(a);
		
		//List list2 = JSON.parseObject(str, List.class);
		
		/*Rule rule = new Rule(); 98 171
		rule.setName("ss");
		rule.setNid("sdk");
		String str2 = JSON.toJSONString(rule);
		Rule obj = JSON.parseObject(str2, Rule.class);
		//System.out.println(obj);
		for(int i = 0; i < list2.size() ; i++){
			//System.out.println(list2.get(i));
		}*/
		
	}
}
