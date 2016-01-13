package com.zhwyd.server.dao.impl;

import java.util.List;
import com.gotop.framework.core.dao.AbstractBaseDao;
import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.dao.RoleDao;

public class RoleDaoImpl extends AbstractBaseDao<RoleTable> implements RoleDao{

	@SuppressWarnings("rawtypes")
    @Override
	protected Class getEntityClass() {
		return RoleTable.class;
	}


	@SuppressWarnings({ "unchecked", "deprecation"})
    public List<Object[]> findByRoleId(Long Id) {
		String hql="select r.id,r.roleName,r.roleCode,r.roleDesc from RoleTable r where r.id=:id";
		return (List<Object[]>)this.getSession().createQuery(hql).setParameter("id", Id).list();
	}


	@SuppressWarnings("deprecation")
    public int deleteByIds(String ids) {
		String hql="delete RoleTable r where r.id in("+ids+")";
		return this.getSession().createQuery(hql).executeUpdate();
	}


	public PageResponse<RoleTable> findPageList(PageRequest pageRequest) {
		String hql="from RoleTable r order by r.updateDate desc";
		return this.findByHql(hql, pageRequest);
	}

}
