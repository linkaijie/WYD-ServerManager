package com.zhwyd.server.dao.impl;
import java.util.List;
import com.zhwyd.server.bean.UserTable;
import com.zhwyd.server.dao.UserTableDao;
public class UserTableDaoImpl extends DaoSupportImpl<UserTable> implements UserTableDao {
    public int deleteByIds(String ids) {
        String hql = "delete UserTable u where u.id in(" + ids + ")";
        return this.executeUpdate(hql, new Object[] {});
    }

    public List<UserTable> getUserTableList() {
        return this.getAll();
    }
}
