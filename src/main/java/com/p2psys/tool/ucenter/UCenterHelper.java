package com.p2psys.tool.ucenter;

import com.fivestars.interfaces.bbs.client.Client;
import com.p2psys.util.NumberUtils;

public class UCenterHelper {
	
	public static String ucenter_register(String username,String password,String email){
		Client uc = new Client();
		String $returns = uc.uc_user_register(username,password ,email ); 
		int $uid = NumberUtils.getInt($returns);
		if($uid <= 0) {
		    if($uid == -1) {
		    	return "用户名不合法";
		    } else if($uid == -2) {
		    	return "包含要允许注册的词语";
		    } else if($uid == -3) {
		    	return "用户名已经存在";
		    } else if($uid == -4) {
		    	return "Email 格式有误";
		    } else if($uid == -5) {
		    	return "Email 不允许注册";
		    } else if($uid == -6) {
		    	return "该 Email 已经被注册";
		    } else {
		    	return "未定义";
		    } 
		} else {
			return "";
		}
	}
	
	public static String ucenter_edit(String username,String oldpwd,String newpwd,String email){
		Client uc = new Client();
		String $returns = uc.uc_user_edit(username, oldpwd, newpwd, email, 1, "", "");
		int $uid = NumberUtils.getInt($returns);
		if($uid <= 0) {
			if($uid==0){
				return "";
			}else if($uid == -1) {
		    	return "旧密码不正确";
		    } else if($uid == -2) {
		    	return "包含要允许注册的词语";
		    } else if($uid == -4) {
		    	return "email 格式有误";
		    } else if($uid == -5) {
		    	return "Email 不允许注册";
		    } else if($uid == -6) {
		    	return "该 Email 已经被注册";
		    } else {
		    	return "未定义";
		    } 
		} else {
			return "";
		}
	}

}
