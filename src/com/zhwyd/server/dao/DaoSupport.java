package com.zhwyd.server.dao;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import com.zhwyd.server.web.page.Pager;
public interface DaoSupport<T> {
    public Serializable save(T entity);

    public void delete(T entity);

    public void update(T entity);

    public void merge(T entity);
    
    public void saveOrUpdate(T entity);

    public T get(Serializable id);

    public T get(String propertyName, Object value);

    public T get(String[] propertyNames, Object[] values);

    public List<T> getAll();

    public List<T> getAll(String propertyName, Object value);

    public Pager getPageList(Pager page);

    public Pager getPageList(CharSequence hql, CharSequence countHql, Pager page, Object... values);

    public Object uniqueResult(CharSequence hql, Object... values);

    public List<?> getList(CharSequence hql, Object... values);

    public List<?> getListBySql(CharSequence hql, Object... values);

    public int executeUpdate(CharSequence hql, Object... values);

    /**
     * 根据Pager和DetachedCriteria对象进行查询(提供分页、查找、排序功能).
     * 
     * @param pager Pager对象
     * @return      Pager对象
     */
    public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria);
    
    public Query createSqlQuery(CharSequence sql, Object... values);
}
