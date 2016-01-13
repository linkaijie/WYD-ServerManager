package com.zhwyd.server.service.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.zhwyd.server.dao.BaseDao;
import com.zhwyd.server.service.BaseService;
import com.zhwyd.server.web.page.Pager;
public class BaseServiceImpl extends ServiceSupportImpl<Serializable> implements BaseService {
    public void setBaseDao(BaseDao baseDao) {
        super.setDaoSupport(baseDao);
    }

    @Override
    public Pager getPagerByList(List<?> list, Pager pager) {
        List<Object> dataList = new ArrayList<Object>();
        int fromIndex = (pager.getPageNumber() - 1) * pager.getPageSize();
        int toIndex = pager.getPageNumber() * pager.getPageSize();
        int totalCount = list.size();
        if(toIndex > totalCount) {
            toIndex = totalCount;
        }
        for (Object o : list.subList(fromIndex, toIndex)) {
            dataList.add(o);
        }
        pager.setTotalCount(list.size());
        pager.setList(dataList);
        return pager;
    }
}
