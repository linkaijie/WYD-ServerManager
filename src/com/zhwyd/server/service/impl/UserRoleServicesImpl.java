package com.zhwyd.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserRole;
import com.zhwyd.server.dao.UserRoleDao;
import com.zhwyd.server.service.UserRoleServices;

public class UserRoleServicesImpl implements UserRoleServices {

	private UserRoleDao userRoleDao;
	
	public List<RoleTable> getRoleByUserName(String userName) {
		List<RoleTable> role = userRoleDao.findByUserName(userName);
		return role;
	}

	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	public List<RoleTable> findAllById(Long id) {
		if(StringUtils.hasText(String.valueOf(id))){
			List<UserRole> list=userRoleDao.findAllById(id);
			if(list!=null&&list.size()>0){
				List<RoleTable> roleList=new ArrayList<RoleTable>();
				for(UserRole ur:list){
					roleList.add(ur.getRoleTable());
				}
				return roleList;
			}
		}
		return null;
	}

	public int deleteById(Long id) {
		if(StringUtils.hasText(String.valueOf(id))){
			return userRoleDao.deleteByUserId(id);
		}
		return 0;
	}

	public UserRole save(UserRole userRole) {
		return userRoleDao.save(userRole);
	}

	
}
