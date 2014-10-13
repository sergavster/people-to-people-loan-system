package com.p2psys.model.borrow.protocol;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.p2psys.util.ReflectUtils;
import com.p2psys.util.StringUtils;

/**
 * 未经授权不得进行修改、复制、出售及商业使用。
 *  (c)</b> 杭州科技有限公司-<br/>
 * @ClassName: ProtocolValue
 * @Description: 
 
 * @date 2013-4-3 上午11:37:37
 */
public class ProtocolValue {
	
	private Map<String,Object> data=new HashMap<String,Object>();
	
	private BorrowProtocol protocol;
	private final String NULL="";
	
	public ProtocolValue() {
		super();
	}

	public Object printProtocol(String var){
		Object ret="";
		String[] tVars=var.split("\\.");
		if(tVars==null){
			return ret;
		}else if(tVars.length==1){
			ret=data.get(tVars[0]);
		}else if(tVars.length==2){
			ret=data.get(tVars[0]);
			if(ret==null){
				return NULL;
			}else if(tVars[0].equals("tenderTable")){
				try {
					protocol.addPdfTable(protocol.getPdf(), protocol.getBorrow());
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}else{
				ret=ReflectUtils.invokeGetMethod(ret.getClass(), ret, tVars[1]);
			}
		}
		return StringUtils.isNull(ret);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
