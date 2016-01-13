package com.zhwyd.server.service;

import java.util.List;

import javax.servlet.http.HttpSession;
import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.gotopweb.commons.ajax.AjaxResult;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.common.util.JqueryZTreeNode;

public interface RoleServices {
	public List<JqueryZTreeNode> loadTree(String id,boolean isAdmin);
	
	public AjaxResult saveTree(String userId,String roleCheckId,HttpSession session);
	
	public RoleTable findById(Long id);
	
	public AjaxResult saveRole(String roleName,String roleCode,String roleDesc,HttpSession session);
	
	public AjaxResult updateRole(String roleName,String roleCode,String roleDesc,String id,HttpSession session);
	
	public AjaxResult deleteRoles(String ids,HttpSession session);
	
	public PageResponse<RoleTable> findPageList(PageRequest pageRequest);
	
	public List<JqueryZTreeNode> loadUserTree(Long rootId);
	
	public AjaxResult saveRoleUserTree(String roelId,String roleUserCheckId,HttpSession session);
}
