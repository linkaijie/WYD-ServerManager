package com.zhwyd.server.service;

import java.util.List;

import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserRole;

public interface UserRoleServices {
	public List<RoleTable> getRoleByUserName(String userName);
	
	public List<RoleTable> findAllById(Long id);
	
	public int deleteById(Long id);
	
	public UserRole save(UserRole userRole);
}