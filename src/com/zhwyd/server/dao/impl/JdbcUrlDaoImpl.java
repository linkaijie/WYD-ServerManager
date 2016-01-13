package com.zhwyd.server.dao.impl;
import java.util.List;
import com.zhwyd.server.bean.JdbcUrl;
import com.zhwyd.server.dao.JdbcUrlDao;
public class JdbcUrlDaoImpl extends DaoSupportImpl<JdbcUrl> implements JdbcUrlDao {
    /**
     * 获取jdbc列表
     */
    @SuppressWarnings("unchecked")
    public List<JdbcUrl> getJdbcUrlList(int type) {
        StringBuffer hql = new StringBuffer();
        hql.append(" From JdbcUrl ");
        hql.append(" Where 1 = 1 ");
        if (type > 0) {
            hql.append(" AND type = " + type);
        }
        hql.append(" AND status = " + 1);
        return (List<JdbcUrl>) this.getList(hql.toString());
    }
}
