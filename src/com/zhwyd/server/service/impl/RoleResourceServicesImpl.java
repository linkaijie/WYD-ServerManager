package com.zhwyd.server.service.impl;

import java.util.List;

import com.zhwyd.server.bean.ResourceTable;
import com.zhwyd.server.dao.RoleResourceDao;
import com.zhwyd.server.service.RoleResourceServices;
//import com.wyd.empire.admin.dao.RoleResourceDao;
//import com.wyd.empire.admin.domain.ResourceTable;

public class RoleResourceServicesImpl implements RoleResourceServices {
	
	private RoleResourceDao roleResourceDao;

	public List<ResourceTable> findByRoleId(Long id) {
		return roleResourceDao.findByRoleId(id);
	}

	public void setRoleResourceDao(RoleResourceDao roleResourceDao) {
		this.roleResourceDao = roleResourceDao;
	}

	public List<ResourceTable> findChild(String ids,String resoLevel) {
		return roleResourceDao.findChild(ids,resoLevel);
	}

	
}
