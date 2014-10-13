package com.p2psys.freemarker.method;

import java.util.List;

import com.p2psys.dao.LinkageDao;
import com.p2psys.dao.UserDao;
import com.p2psys.util.NumberUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 根据ID获取证件类型 或者获取证件列表(用户类型等)
 * 
 
 * 
 */
public class AttestationTypeNameModel implements TemplateMethodModel {
	private LinkageDao linkageDao;
	private UserDao userDao;

	public AttestationTypeNameModel(LinkageDao linkageDao, UserDao userdao) {
		this.linkageDao = linkageDao;
		this.userDao = userdao;
	}

	@Override
	public Object exec(List arg0) throws TemplateModelException {
		if (arg0.size() > 1) {
			if (arg0.get(0).equals("list")) {
				if (arg0.get(1).equals("usertype")) {
					return userDao.getAllUserType();
				}
			}
			if (arg0.get(1).equals("name"))
				return linkageDao.getLinkageNameByid(NumberUtils.getLong(arg0
						.get(0) + ""));

			if (arg0.get(1).equals("area")) {
				return linkageDao.getAreaByPid(arg0.get(0).toString());
			}
			if (arg0.get(1).equals("usertype")) {
				return userDao.getUserTypeByid(arg0.get(0).toString());
			}
		}
		return "-";
	}

}
