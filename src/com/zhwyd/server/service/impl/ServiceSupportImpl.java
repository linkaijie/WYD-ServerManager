package com.zhwyd.server.service.impl;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import com.zhwyd.server.dao.DaoSupport;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.service.ServiceSupport;
import com.zhwyd.server.web.page.Pager;
public abstract class ServiceSupportImpl<T> extends CommonSupportImpl implements ServiceSupport<T> {
    protected DaoSupport<T> daoSupport;

    public void setDaoSupport(DaoSupport<T> daoSupport) {
        this.daoSupport = daoSupport;
    }

    @Override
    public Serializable save(T entity) {
        return daoSupport.save(entity);
    }

    @Override
    public void save(List<T> list) {
        for (T t : list) {
            daoSupport.save(t);
        }
    }

    @Override
    public void delete(T entity) {
        daoSupport.delete(entity);
    }

    @Override
    public void update(T entity) {
        daoSupport.update(entity);
    }

    @Override
    public void merge(T entity) {
        daoSupport.merge(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        daoSupport.saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdate(List<T> list) {
        for (T t : list) {
            daoSupport.saveOrUpdate(t);
        }
    }

    @Override
    public T get(Serializable id) {
        return daoSupport.get(id);
    }

    @Override
    public T get(String propertyName, Object value) {
        return daoSupport.get(propertyName, value);
    }

    @Override
    public T get(String[] propertyNames, Object[] values) {
        return daoSupport.get(propertyNames, values);
    }

    @Override
    public List<T> getAll() {
        return daoSupport.getAll();
    }

    @Override
    public Pager getPageList(Pager pager) {
        return daoSupport.getPageList(pager);
    }

    @Override
    public Pager getPageList(CharSequence hql, CharSequence countHql, Pager pager, Object... values) {
        return daoSupport.getPageList(hql, countHql, pager, values);
    }

    @Override
    public Object uniqueResult(CharSequence hql, Object... values) {
        return daoSupport.uniqueResult(hql, values);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Method getMethod(ServiceInterface serviceInterface, String classType, String methodName, String... params) throws Exception {
        // 通过完整的类型路径获取类
        Class<?> classes = null;
        if (serviceInterface != null) {
            classes = serviceInterface.getClass();
        } else {
            classes = Class.forName(classType);
        }
        Class[] classArray = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            classArray[i] = Class.forName(params[i]);
        }
        // 使用newInstance创建对象
        // Object invokeTester = classType.newInstance();
        // 使用默认构造函数获取对象
        // Object invokeTester = classes.getConstructor(new Class[] {}).newInstance(new Object[] {});
        Method addMethod = null;
        if (classArray.length > 0) {
//            addMethod = classes.getMethod(methodName, classArray);
            addMethod = classes.getDeclaredMethod(methodName, classArray);
        } else {
//            addMethod = classes.getMethod(methodName);
            addMethod = classes.getDeclaredMethod(methodName);
        }
        // 调用invokeTester对象上的add()方法
        // Object result = addMethod.invoke(invokeTester, new Object[] { new Integer(100), new Integer(200)});
        return addMethod;
    }
    
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("java.lang.Integer");
    }
}
