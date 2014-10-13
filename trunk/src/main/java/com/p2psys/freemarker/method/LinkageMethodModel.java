package com.p2psys.freemarker.method;

import java.util.List;

import com.p2psys.dao.LinkageDao;
import com.p2psys.domain.Linkage;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 根据ID获取证件类型 或者获取证件列表(用户类型等)
 * 
 
 * 
 */
public class LinkageMethodModel implements TemplateMethodModel {
	private LinkageDao linkageDao;

	public LinkageMethodModel(LinkageDao linkageDao) {
		this.linkageDao = linkageDao;
	}

	@Override
	public Object exec(List arg) throws TemplateModelException {
		String nid="",type="",value="";
		if (arg.size() ==3) {
			nid=StringUtils.isNull(arg.get(0));
			type=StringUtils.isNull(arg.get(1));
			value=StringUtils.isNull(arg.get(2));
		}else{
			return "Illegal arguments";
		}
		Linkage l=null;
		if(type.equals("id")){
			long id=NumberUtils.getLong(value);
			l=linkageDao.getLinkageById(id);
		}else{
			l=linkageDao.getLinkageByValue(nid, value);
		}
		return l;
	}

}
