package com.zhwyd.server.dao;

import java.util.List;
import com.gotop.framework.core.dao.BaseDao;
import com.zhwyd.server.bean.ResourceTable;
import com.zhwyd.server.bean.RoleResource;
import com.zhwyd.server.bean.RoleTable;

public interface RoleResourceDao  extends BaseDao<RoleResource>{

	public List<ResourceTable> findByRoleId(Long id);
	
	public int deleteByRoleId(Long id);
	
	public List<RoleTable> findByResourceId(Long id);
	
	public int deleteByResourceId(Long id);
	
	/**
	 * 根据角色查询出资源菜单
	 * @param ids
	 * @return
	 */
	public List<ResourceTable> findChild(String ids,String resoLevel);
}
