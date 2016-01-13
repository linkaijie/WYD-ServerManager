package com.zhwyd.server.dao.impl;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;
import com.zhwyd.server.dao.DaoSupport;
import com.zhwyd.server.web.page.Pager;
import com.zhwyd.server.web.page.Pager.OrderType;
public abstract class DaoSupportImpl<T> implements DaoSupport<T> {
    protected Class<T>        entityClass;
    private SessionFactory    sessionFactory;
    private HibernateTemplate hibernateTemplate;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获取HibernateTemplate
     * @return  HibernateTemplate
     */
    public HibernateTemplate getHibernateTemplate() {
        if (hibernateTemplate == null) {
            hibernateTemplate = new HibernateTemplate(this.sessionFactory);
        }
        return hibernateTemplate;
    }

    @SuppressWarnings("unchecked")
    protected DaoSupportImpl() {
        Class<?> c = getClass();
        Type type = c.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            this.entityClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Serializable save(T entity) {
        return getCurrentSession().save(entity);
    }

    @Override
    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void update(T entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        Assert.notNull(entity, "entity is required");
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void merge(T entity) {
        getCurrentSession().merge(entity);
    }
    
    @Override
    public T get(Serializable id) {
        return entityClass.cast(getCurrentSession().get(entityClass, id));
    }

    @Override
    public T get(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = String.format("FROM %s AS model WHERE model.%s = ?", entityClass.getName(), propertyName);
        return entityClass.cast(getCurrentSession().createQuery(hql).setParameter(0, value).uniqueResult());
    }

    @Override
    public T get(String[] propertyNames, Object[] values) {
        Assert.isTrue(propertyNames.length == values.length, "param length must be equal");
        Assert.noNullElements(propertyNames, "propertyNames is required");
        Assert.noNullElements(values, "values is required");
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        for (int i = 0; i < propertyNames.length; i++) {
            detachedCriteria.add(Restrictions.eq(propertyNames[i], values[i]));
        }
        return entityClass.cast(detachedCriteria.getExecutableCriteria(getCurrentSession()).uniqueResult());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        String hql = String.format("FROM %s AS model", entityClass.getName());
        return getCurrentSession().createQuery(hql).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = String.format("FROM %s AS model WHERE model.%s = ?", entityClass.getName(), propertyName);
        return getCurrentSession().createQuery(hql).setParameter(0, value).list();
    }

    @Override
    public Pager getPageList(Pager pager) {
        String hql = String.format("FROM %s AS model", entityClass.getName());
        String countHql = "SELECT COUNT(*) " + hql;
        return getPageList(hql, countHql, pager);
    }

    @Override
    public Pager getPageList(CharSequence hql, CharSequence countHql, Pager pager, Object... values) {
        Long totalCount = (Long) uniqueResult(countHql, values);
        Query q = createQuery(hql, values).setFirstResult((pager.getPageNumber() - 1) * pager.getPageSize()).setMaxResults(pager.getPageSize());
        pager.setList(q.list());
        pager.setTotalCount(totalCount.intValue());
        return pager;
    }

    @Override
    public Object uniqueResult(CharSequence hql, Object... values) {
        return createQuery(hql, values).uniqueResult();
    }

    @Override
    public List<?> getList(CharSequence hql, Object... values) {
        return createQuery(hql, values).list();
    }

    @Override
    public List<?> getListBySql(CharSequence sql, Object... values) {
        return createSqlQuery(sql, values).list();
    }

    @Override
    public int executeUpdate(CharSequence hql, Object... values) {
        return createQuery(hql, values).executeUpdate();
    }

    private Query createQuery(CharSequence hql, Object... values) {
        Query q = getCurrentSession().createQuery(hql.toString());
        for (int i = 0; i < values.length; i++) {
            q.setParameter(i, values[i]);
        }
        return q;
    }

    @Override
    public Query createSqlQuery(CharSequence sql, Object... values) {
        Query q = getCurrentSession().createSQLQuery(sql.toString());
        for (int i = 0; i < values.length; i++) {
            q.setParameter(i, values[i]);
        }
        return q;
    }

    /**
     * 根据Pager和DetachedCriteria对象进行查询(提供分页、查找、排序功能).
     * @param pager Pager对象
     * @return      Pager对象
     */
    @Override
    public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria) {
        if (pager == null) {
            pager = new Pager();
        }
        Integer pageNumber = pager.getPageNumber();
        Integer pageSize = pager.getPageSize();
        String orderBy = pager.getOrderBy();
        OrderType orderType = pager.getOrderType();
        Criteria criteria = detachedCriteria.getExecutableCriteria(getCurrentSession());
        Number totalCount = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();
        criteria.setProjection(null);
        criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        criteria.setFirstResult((pageNumber - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        if (StringUtils.isNotEmpty(orderBy) && orderType != null) {
            if (orderType == OrderType.asc) {
                criteria.addOrder(Order.asc(orderBy));
            } else {
                criteria.addOrder(Order.desc(orderBy));
            }
        }
        pager.setTotalCount(totalCount.intValue());
        pager.setList(criteria.list());
        return pager;
    }
}
