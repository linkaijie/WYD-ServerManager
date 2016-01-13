package com.zhwyd.server.dao;
import java.util.List;
import com.zhwyd.server.bean.UserTable;
public interface UserTableDao extends DaoSupport<UserTable> {
    /**
     * 根据多个ID值删除用户
     * 
     * @param ids
     * @return
     */
    public int deleteByIds(String ids);

    public List<UserTable> getUserTableList();
}
