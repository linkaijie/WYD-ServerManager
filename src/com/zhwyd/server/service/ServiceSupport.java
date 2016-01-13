package com.zhwyd.server.service;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import com.zhwyd.server.web.page.Pager;
public interface ServiceSupport<T> extends CommonSupport {
    public Serializable save(T entity);

    public void save(List<T> list);

    public void delete(T entity);

    public void update(T entity);
    
    public void merge(T entity);

    public void saveOrUpdate(T entity);

    public void saveOrUpdate(List<T> list);

    public T get(Serializable id);

    public T get(String propertyName, Object value);

    public T get(String[] propertyNames, Object[] values);

    public List<T> getAll();

    public Pager getPageList(Pager pager);

    public Pager getPageList(CharSequence hql, CharSequence countHql, Pager pager, Object... values);

    public Object uniqueResult(CharSequence hql, Object... values);
    
    public Method getMethod(ServiceInterface serviceInterface, String classType, String methodName, String... params) throws Exception;
}
