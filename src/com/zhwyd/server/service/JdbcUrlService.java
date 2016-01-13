package com.zhwyd.server.service;
import java.util.List;
import com.zhwyd.server.bean.JdbcUrl;
public interface JdbcUrlService extends ServiceSupport<JdbcUrl> {
	
    /**
     * 获取jdbc信息列表
     */
	public List<JdbcUrl> getJdbcUrlList(int type);
	
}
