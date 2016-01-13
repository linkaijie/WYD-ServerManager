package com.zhwyd.server.dao;

import java.util.List;
import com.gotop.framework.core.dao.BaseDao;
import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.zhwyd.server.bean.RoleTable;

public interface RoleDao  extends BaseDao<RoleTable>{
	
	public List<Object[]> findByRoleId(Long Id);
	
	public int deleteByIds(String ids);
	
	public PageResponse<RoleTable> findPageList(PageRequest pageRequest);

}
