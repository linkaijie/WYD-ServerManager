package com.zhwyd.server.dao;
import java.util.List;
import com.zhwyd.server.bean.JdbcUrl;
public interface JdbcUrlDao extends DaoSupport<JdbcUrl> {
	
    /**
     * 获取jdbc列表
     */
	public List<JdbcUrl> getJdbcUrlList(int type);
}
