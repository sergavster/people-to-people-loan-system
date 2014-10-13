package com.p2psys.domain;

public class Notice {


	public Notice() {
		
	}
	private long receive_userid;
	private long send_userid;
	private String title;
	private String content;
	private String name="";
	private String addtime;
	private int status;
	private String mobile;
	private String result;
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	private String nid;
	private String type;
	//private String receive_addr;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	
	//v1.6.7.1 RDPROJECT-522 liukun 2013-12-03 start
	private String receive_addr;
	
	public String getReceive_addr() {
		return receive_addr;
	}
	public void setReceive_addr(String receive_addr) {
		this.receive_addr = receive_addr;
	}
	
	//v1.6.7.1 RDPROJECT-522 liukun 2013-12-03 end
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	
	public long getReceive_userid() {
		return receive_userid;
	}
	public void setReceive_userid(long receive_userid) {
		this.receive_userid = receive_userid;
	}
	public long getSend_userid() {
		return send_userid;
	}
	public void setSend_userid(long send_userid) {
		this.send_userid = send_userid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String warpContent(){
		return this.getContent();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	
}
