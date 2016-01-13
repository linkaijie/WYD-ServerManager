package com.zhwyd.server.ssh.vo;

import java.io.Serializable;

public class SshParam implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ip;
	private String userName;
	private String password;
	
	public SshParam() {
	}
	
	public SshParam(String ip, String userName, String password ) {
		this.ip = ip;
		this.userName = userName;
		this.password = password; 
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
