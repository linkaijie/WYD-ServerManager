package com.zhwyd.server.service;

import java.util.List;

import com.zhwyd.server.bean.ResourceTable;



public interface RoleResourceServices {
	
	public List<ResourceTable> findByRoleId(Long id);
	
	/**
	 * 根据角色查询出资源菜单
	 * @param ids
	 * @return
	 */
	public List<ResourceTable> findChild(String ids,String resoLevel);
}
