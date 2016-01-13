package com.zhwyd.server.dao;

import java.util.List;
import com.gotop.framework.core.dao.BaseDao;
import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.zhwyd.server.bean.ResourceTable;

public interface ResourceDao  extends BaseDao<ResourceTable>{
    
    public int deleteByIds(String ids);
    
    public ResourceTable save(ResourceTable resource);
    
    public ResourceTable getByid(Long id);
    
    public Object[] get(Long id);

    public ResourceTable getRootResource();
    
    public List<ResourceTable> findByPid(Long pid);
    
    public PageResponse<ResourceTable> findPageList(PageRequest pageRequest);
    
    public List<ResourceTable> findAllChild();
    
}
