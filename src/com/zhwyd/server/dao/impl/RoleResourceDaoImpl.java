package com.zhwyd.server.dao.impl;

import java.util.List;
import org.springframework.util.StringUtils;
import com.gotop.framework.core.dao.AbstractBaseDao;
import com.zhwyd.server.bean.ResourceTable;
import com.zhwyd.server.bean.RoleResource;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.dao.RoleResourceDao;

public class RoleResourceDaoImpl extends AbstractBaseDao<RoleResource> implements RoleResourceDao{
 
	@SuppressWarnings("rawtypes")
    @Override
	protected Class getEntityClass() {
		return RoleResource.class;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation"})
	public List<ResourceTable> findByRoleId(Long id) {
		//判断传递进来的id值是否为空
		if (StringUtils.hasText(String.valueOf(id))) {
			//组装hql语句
			String hql="select rr.resourceTable from RoleResource rr where rr.roleTable.id= :id";
			return (List<ResourceTable>)this.getSession().createQuery(hql).setParameter("id", id).list();
		}
		return null;
	}
	
	/**
	 * 根据id值删除角色资源对象
	 */
	@SuppressWarnings("deprecation")
    public int deleteByRoleId(Long id) {
		return this.getSession().createQuery("delete RoleResource rr where rr.roleTable.id="+id).executeUpdate();
	}

	@SuppressWarnings({ "unchecked", "deprecation"})
    public List<RoleTable> findByResourceId(Long id) {
		String hql="select rr.roleTable from RoleResource rr where rr.resourceTable.id=:id";
		return (List<RoleTable>)this.getSession().createQuery(hql).setParameter("id", id).list();
	}

	@SuppressWarnings("deprecation")
    public int deleteByResourceId(Long id) {
		String hql="delete RoleResource rr where rr.resourceTable.id=:id";
		return this.getSession().createQuery(hql).setParameter("id",id).executeUpdate();
	}

	@SuppressWarnings({ "deprecation", "unchecked"})
    public List<ResourceTable> findChild(String ids,String resoLevel) {
		String hql=" select distinct rr.resourceTable from RoleResource rr where rr.roleTable.id in ("+ids+") ";
		if(StringUtils.hasText(resoLevel)){
			hql+=" and rr.resourceTable.resoLevel='"+resoLevel+"' ";
		}	
			hql+=" order by rr.resourceTable.resoNo asc ";
		return this.getSession().createQuery(hql).list();
	}
}
