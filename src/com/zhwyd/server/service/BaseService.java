package com.zhwyd.server.service;
import java.io.Serializable;
import java.util.List;
import com.zhwyd.server.web.page.Pager;
public interface BaseService extends ServiceSupport<Serializable> {
    /**
     * 获得翻页pager的实例
     * @param list
     * @param pager
     * @return
     */
    public Pager getPagerByList(List<?> list, Pager pager);
}
