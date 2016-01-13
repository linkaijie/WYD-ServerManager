package com.zhwyd.server.dao.impl;

import java.util.List;
import org.springframework.util.StringUtils;
import com.gotop.framework.core.dao.AbstractBaseDao;
import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.zhwyd.server.bean.ResourceTable;
import com.zhwyd.server.dao.ResourceDao;

public class ResourceDaoImpl extends AbstractBaseDao<ResourceTable> implements ResourceDao{
 
    @SuppressWarnings("rawtypes")
    @Override
    protected Class getEntityClass() {
        return ResourceTable.class;
    }


    @SuppressWarnings({ "deprecation"})
    public int deleteByIds(String ids) {
        String hql="delete ResourceTable r where r.id in("+ids+")";
        return this.getSession().createQuery(hql).executeUpdate();
    }

    public Object[] get(Long id) {
        if (StringUtils.hasText(String.valueOf(id))){
            String hql="select r.resoName,r.resoType,r.resoUrl,r.resoDesc from ResourceTable r where r.id='"+id+"'";
            return (Object[])this.getHibernateTemplate().find(hql).get(0);
        }
        return null;
    }

    public ResourceTable getByid(Long id) {
        if (StringUtils.hasText(String.valueOf(id))) {
            return (ResourceTable)this.getHibernateTemplate().get(ResourceTable.class, id);
        }
        return null;
    }

    public ResourceTable save(ResourceTable resource) {
        this.getHibernateTemplate().saveOrUpdate(resource);
        return resource;
    }


    @SuppressWarnings("deprecation")
    public ResourceTable getRootResource() {
        String hql="from ResourceTable r where r.resourceTable is null order by r.id asc";
        return (ResourceTable)this.getSession().createQuery(hql).list().get(0);
    }


    @SuppressWarnings({ "deprecation", "unchecked"})
    public List<ResourceTable> findByPid(Long pid) {
        if(pid!=null){
            String hql="from ResourceTable r where r.resourceTable.id =:pid order by r.id asc";
            return (List<ResourceTable>)this.getSession().createQuery(hql).setParameter("pid", pid).list();
        }else{
            String hql="from ResourceTable r where r.resourceTable.id is null order by r.id asc";
            return (List<ResourceTable>)this.getSession().createQuery(hql).list();
        }
    }


    public PageResponse<ResourceTable> findPageList(PageRequest pageRequest) {
        String hql="from ResourceTable r where r.id='"+pageRequest.getParameter("pid")+"' or r.resourceTable.id='"+pageRequest.getParameter("pid")+"' order by r.id ";
        return this.findByHql(hql, pageRequest);
    }


    @SuppressWarnings({ "deprecation", "unchecked"})
    public List<ResourceTable> findAllChild() {
        String hql="from ResourceTable r where r.resourceTable is not null order by r.id asc";
        return this.getSession().createQuery(hql).list();
    }
}
