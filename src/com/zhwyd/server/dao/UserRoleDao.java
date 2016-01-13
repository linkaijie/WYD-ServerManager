package com.zhwyd.server.dao; 

import java.util.List;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserRole;
import com.zhwyd.server.bean.UserTable;

public interface UserRoleDao  extends DaoSupport<UserRole>{
	
	public List<UserRole> findAllById(Long id);
	
	public int deleteByUserId(Long id);
	
	public int deleteByRoleId(Long id);
	
	public UserRole save(UserRole userRole);
	
	public void deleteByUserIds(String ids);
	
	public List<UserTable> findByRoleId(Long id);
	
	public List<RoleTable> findByUserName(String userName);
}
