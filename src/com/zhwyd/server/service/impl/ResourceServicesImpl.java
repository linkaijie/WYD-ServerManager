package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.util.StringUtils;
import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.gotopweb.commons.ajax.AjaxResult;
import com.zhwyd.server.bean.ResourceTable;
import com.zhwyd.server.bean.RoleResource;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.common.util.I18NUtil;
import com.zhwyd.server.common.util.JqueryZTreeNode;
import com.zhwyd.server.common.util.PageUtil;
import com.zhwyd.server.dao.ResourceDao;
import com.zhwyd.server.dao.RoleDao;
import com.zhwyd.server.dao.RoleResourceDao;
import com.zhwyd.server.service.ResourceServices;

public class ResourceServicesImpl implements ResourceServices {
    private ResourceDao     resourceDao;
    private RoleResourceDao roleResourceDao;
    private RoleDao         roleDao;

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public void setRoleResourceDao(RoleResourceDao roleResourceDao) {
        this.roleResourceDao = roleResourceDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public ResourceTable getRootResource() {
        return resourceDao.getRootResource();
    }

    public List<JqueryZTreeNode> loadTree(Long pid, String paramId) {
        try {
            List<JqueryZTreeNode> treeList = new ArrayList<JqueryZTreeNode>();
            // if(pid!=null){
            List<ResourceTable> list = resourceDao.findByPid(pid);
            if (list != null && list.size() > 0) {
                for (ResourceTable res : list) {
                    JqueryZTreeNode tree = new JqueryZTreeNode();
                    tree.setName(res.getResoName());
                    tree.setId(res.getId().toString());
                    tree.setOpen(true);
                    tree.setChecked(false);
                    if (resourceDao.findByPid(res.getId()).size() > 0) {
                        tree.setIsParent("true");
                        tree.setIconOpen(JqueryZTreeNode.iconOpenText);
                        tree.setIconClose(JqueryZTreeNode.iconCloseText);
                    } else {
                        tree.setIsParent("false");
                        tree.setIcon(JqueryZTreeNode.iconText);
                    }
                    // tree.setChecked(true);
                    if (StringUtils.hasText(paramId)) {
                        List<ResourceTable> values = roleResourceDao.findByRoleId(Long.valueOf(paramId));
                        if (values.size() > 0) {
                            for (ResourceTable r : values) {
                                if (r.getId().equals(res.getId())) {
                                    tree.setChecked(true);
                                }
                            }
                        }
                    }
                    treeList.add(tree);
                }
                // }
                return treeList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JqueryZTreeNode> loadAllTree(String paramId) {
        try {
            // HttpServletRequest request = ServletActionContext.getRequest();
            // String ctx=request.getContextPath();
            List<JqueryZTreeNode> treeList = new ArrayList<JqueryZTreeNode>();
            List<ResourceTable> values = new ArrayList<ResourceTable>();
            if (StringUtils.hasText(paramId)) {
                values = roleResourceDao.findByRoleId(Long.valueOf(paramId));
            }
            // if(pid!=null){
            List<ResourceTable> list = resourceDao.findAll();
            if (list != null && list.size() > 0) {
                for (ResourceTable res : list) {
                    JqueryZTreeNode tree = new JqueryZTreeNode();
                    tree.setName(res.getResoName());
                    tree.setId(res.getId().toString());
                    tree.setOpen(true);
                    tree.setChecked(false);
                    if (res.getResourceTable() != null) {
                        tree.setpId(res.getResourceTable().getId().toString());
                    }
                    if (resourceDao.findByPid(res.getId()).size() > 0) {
                        tree.setIsParent("true");
                        tree.setOpen(true);
                        // tree.setIconOpen(ctx+"/scripts/zTree/zTreeStyle/img/minus.gif");
                        // tree.setIconClose(ctx+"/scripts/zTree/zTreeStyle/img/plus.gif");
                    } else {
                        tree.setIsParent("false");
                        tree.setOpen(false);
                        // tree.setIcon(ctx+"/scripts/zTree/zTreeStyle/img/join.gif");
                    }
                    // tree.setChecked(true);
                    if (values.size() > 0) {
                        for (int i = 0; i < values.size(); i++) {
                            ResourceTable r = values.get(i);
                            if (r.getId().equals(res.getId())) {
                                tree.setChecked(true);
                                values.remove(i);
                            }
                        }
                    }
                    treeList.add(tree);
                }
                // }
                return treeList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AjaxResult saveTree(String roleId, String resourcesCheckId, HttpSession session) {
        AjaxResult result = new AjaxResult();
        try {
            if (StringUtils.hasText(roleId)) {
                if (StringUtils.hasText(resourcesCheckId)) {
                    roleResourceDao.deleteByRoleId(Long.valueOf(roleId));
                    List<Long> list = PageUtil.getCheckedId(resourcesCheckId);
                    RoleTable role = roleDao.findById(Long.valueOf(roleId));
                    for (Long checkId : list) {
                        RoleResource roleResource = new RoleResource();
                        roleResource.setResourceTable(resourceDao.findById(checkId));
                        roleResource.setRoleTable(role);
                        roleResourceDao.insert(roleResource);
                    }
                    result.setInfo(I18NUtil.returnBundle(session).getString("AllocateResourcesSuccess"));
                    result.setSuccess(true);
                } else {
                    roleResourceDao.deleteByRoleId(Long.valueOf(roleId));
                    result.setInfo(I18NUtil.returnBundle(session).getString("DeleteResourceSuccess"));
                    result.setSuccess(true);
                }
            }
        } catch (Exception ex) {
            result.setFailure(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("ExceptionOfSubmite"));
            ex.printStackTrace();
        }
        return result;
    }

    public ResourceTable findById(Long id) {
        ResourceTable resourceTable = resourceDao.findById(id);
        Hibernate.initialize(resourceTable.getResourceTable());
        return resourceTable;
    }

    public AjaxResult deleteResources(String ids, HttpSession session) {
        AjaxResult result = new AjaxResult();
        try {
            int count = resourceDao.deleteByIds(ids);
            if (count > 0) {
                result.setSuccess(true);
                result.setInfo(I18NUtil.returnBundle(session).getString("SuccessToDeleteResourceInformation"));
            } else {
                result.setFailure(true);
                result.setInfo(I18NUtil.returnBundle(session).getString("FailedToDeleteResourceInformation"));
            }
        } catch (RuntimeException e) {
            result.setFailure(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("ResourceInformationIsInUse"));
        }
        return result;
    }

    public PageResponse<ResourceTable> findPageList(PageRequest pageRequest) {
        return resourceDao.findPageList(pageRequest);
    }

    public AjaxResult save(String resoName, String resoCode, String resoUrl, String resoLevel, String resoNo, String resoClass, String treePid, HttpSession session) {
        try {
            ResourceTable resourceTable = new ResourceTable();
            resourceTable.setResoName(resoName);
            resourceTable.setResoCode(resoCode);
            resourceTable.setResoUrl(resoUrl);
            resourceTable.setResoLevel(Integer.parseInt(resoLevel));
            resourceTable.setResoNo(Integer.parseInt(resoNo));
            resourceTable.setResoClass(resoClass);
            resourceTable.setResourceTable(resourceDao.findById(Long.valueOf(treePid)));
            ResourceTable newResourceTable = resourceDao.insert(resourceTable);
            AjaxResult result = new AjaxResult();
            if (newResourceTable != null) {
                result.setInfo(I18NUtil.returnBundle(session).getString("SavingResourcesSuccess"));
                result.setSuccess(true);
            } else {
                result.setInfo(I18NUtil.returnBundle(session).getString("SavingResourcesFail"));
                result.setFailure(true);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AjaxResult update(String resoName, String resoCode, String resoUrl, String resoLevel, String resoNo, String resoClass, String id, HttpSession session) {
        try {
            ResourceTable resourceTable = resourceDao.findById(Long.valueOf(id));
            resourceTable.setResoName(resoName);
            resourceTable.setResoCode(resoCode);
            resourceTable.setResoUrl(resoUrl);
            resourceTable.setResoLevel(Integer.parseInt(resoLevel));
            resourceTable.setResoNo(Integer.parseInt(resoNo));
            resourceTable.setResoClass(resoClass);
            ResourceTable newResourceTable = resourceDao.update(resourceTable);
            AjaxResult result = new AjaxResult();
            if (newResourceTable != null) {
                result.setInfo(I18NUtil.returnBundle(session).getString("ModifyResourceSuccess"));
                result.setSuccess(true);
            } else {
                result.setInfo(I18NUtil.returnBundle(session).getString("ModificationResourceFailure"));
                result.setFailure(true);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JqueryZTreeNode> loadResource(String id) {
        List<JqueryZTreeNode> treeList = new ArrayList<JqueryZTreeNode>();
        List<RoleTable> list = roleResourceDao.findByResourceId(Long.valueOf(id));
        if (list != null && list.size() > 0) {
            for (RoleTable role : list) {
                JqueryZTreeNode tree = new JqueryZTreeNode();
                tree.setName(role.getRoleName());
                tree.setId(role.getId().toString());
                tree.setChecked(true);
                treeList.add(tree);
            }
        }
        return treeList;
    }

    public AjaxResult saveRoleTree(String resourcesId, String roleCheckId, HttpSession session) {
        AjaxResult result = new AjaxResult();
        try {
            if (StringUtils.hasText(resourcesId)) {
                if (StringUtils.hasText(roleCheckId)) {
                    roleResourceDao.deleteByResourceId(Long.valueOf(resourcesId));
                    List<Long> list = PageUtil.getCheckedId(roleCheckId);
                    ResourceTable resource = resourceDao.findById(Long.valueOf(resourcesId));
                    for (Long checkId : list) {
                        RoleResource roleResource = new RoleResource();
                        roleResource.setResourceTable(resource);
                        roleResource.setRoleTable(roleDao.findById(checkId));
                        roleResourceDao.insert(roleResource);
                    }
                    result.setInfo(I18NUtil.returnBundle(session).getString("AllocateResourcesSuccess"));
                    result.setSuccess(true);
                } else {
                    roleResourceDao.deleteByResourceId(Long.valueOf(resourcesId));
                    result.setInfo(I18NUtil.returnBundle(session).getString("DeleteResourceSuccess"));
                    result.setSuccess(true);
                }
            }
        } catch (Exception ex) {
            result.setFailure(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("ExceptionOfSubmite"));
            ex.printStackTrace();
        }
        return result;
    }

    public List<ResourceTable> findByPid(Long pid) {
        return resourceDao.findByPid(pid);
    }

    public List<ResourceTable> findAllChild() {
        List<ResourceTable> list = resourceDao.findAllChild();
        for (ResourceTable r : list) {
            Hibernate.initialize(r.getResourceTable());
        }
        return list;
    }
}
