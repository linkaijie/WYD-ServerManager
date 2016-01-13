package com.zhwyd.server.service;

import java.util.List;
import javax.servlet.http.HttpSession;
import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.gotopweb.commons.ajax.AjaxResult;
import com.zhwyd.server.bean.ResourceTable;
import com.zhwyd.server.common.util.JqueryZTreeNode;

public interface ResourceServices {
    
    public ResourceTable getRootResource();
    
    public List<JqueryZTreeNode> loadTree(Long pid,String paramId);
    
    public List<JqueryZTreeNode> loadAllTree(String paramId);
    
    public AjaxResult saveTree(String roleId,String resourcesCheckId,HttpSession session);
    
    public ResourceTable findById(Long id);
    
    public AjaxResult deleteResources(String ids,HttpSession session);
    
    public PageResponse<ResourceTable> findPageList(PageRequest pageRequest);
    
    public AjaxResult save(String resoName,String resoCode,String resoUrl,String resoLevel,String resoNo,String resoClass,String treePid,HttpSession session);
    
    public AjaxResult update(String resoName,String resoType,String resoUrl,String resoLevel,String resoNo,String resoClass,String id,HttpSession session);
    
    public List<JqueryZTreeNode> loadResource(String id);
    
    public AjaxResult saveRoleTree(String resourcesId,String roleCheckId,HttpSession session);
    
    public List<ResourceTable> findByPid(Long pid);
    
    public List<ResourceTable> findAllChild();
}


