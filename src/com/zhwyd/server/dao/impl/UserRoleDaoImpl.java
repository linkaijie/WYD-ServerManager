package com.zhwyd.server.dao.impl;

import java.util.List;
import org.springframework.util.StringUtils;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserRole;
import com.zhwyd.server.bean.UserTable;
import com.zhwyd.server.dao.UserRoleDao;

public class UserRoleDaoImpl extends DaoSupportImpl<UserRole> implements UserRoleDao{


	@SuppressWarnings("unused")
    public List<UserRole> findAllById(Long id) {
		if(StringUtils.hasText(String.valueOf(id))){
			String hql="from UserRole ur where ur.userTable.id=:id";
//			return (List<UserRole>)this.getSession().createQuery(hql).setParameter("id", id).list();
		}
		return null;
	}

	public int deleteByUserId(Long id) {
//		if (StringUtils.hasText(String.valueOf(id))) {
//			Query query=this.getSession().createQuery("delete UserRole ur where ur.userTable.id="+id);
//			return query.executeUpdate();
//		}
		return 0;
	}

	public UserRole save(UserRole userRole) {
		this.getHibernateTemplate().saveOrUpdate(userRole);
		return userRole;
	}


	public int deleteByRoleId(Long id) {
//		String hql="delete UserRole ur where ur.roleTable.id=:roleId";
//		return this.getSession().createQuery(hql).setParameter("roleId", id).executeUpdate();
		return -1;
	}

	public void deleteByUserIds(String ids) {
//		String hql="delete UserRole ur where ur.userTable.id in("+ids+")";
//		this.getSession().createQuery(hql).executeUpdate();
	}

	public List<UserTable> findByRoleId(Long id) {
//		String hql="select ur.userTable from UserRole ur where ur.roleTable.id=:roleId";
//		return (List<UserTable>)this.getSession().createQuery(hql).setParameter("roleId", id).list();
		return null;
	}

	public List<RoleTable> findByUserName(String userName) {
//		String hql="select ur.roleTable from UserRole ur where ur.userTable.userName=:userName";
//		return (List<RoleTable>)this.getSession().createQuery(hql).setParameter("userName", userName).list();
		return null;
	}
}
