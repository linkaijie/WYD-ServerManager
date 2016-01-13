package com.zhwyd.server.service.impl;
import java.util.List;
import com.zhwyd.server.bean.JdbcUrl;
import com.zhwyd.server.dao.JdbcUrlDao;
import com.zhwyd.server.service.JdbcUrlService;
public class JdbcUrlServiceImpl extends ServiceSupportImpl<JdbcUrl> implements JdbcUrlService {
    protected JdbcUrlDao jdbcUrlDao;

    public void setJdbcUrlDao(JdbcUrlDao jdbcUrlDao) {
        super.setDaoSupport(jdbcUrlDao);
        this.jdbcUrlDao = jdbcUrlDao;
    }

    /**
     * 获取jdbc列表
     */
    public List<JdbcUrl> getJdbcUrlList(int type) {
        return jdbcUrlDao.getJdbcUrlList(type);
    }
}
