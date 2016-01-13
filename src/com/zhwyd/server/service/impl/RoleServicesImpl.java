package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.gotop.framework.core.pagination.PageRequest;
import com.gotop.framework.core.pagination.PageResponse;
import com.gotopweb.commons.ajax.AjaxResult;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserRole;
import com.zhwyd.server.bean.UserTable;
import com.zhwyd.server.common.util.I18NUtil;
import com.zhwyd.server.common.util.JqueryZTreeNode;
import com.zhwyd.server.common.util.PageUtil;
import com.zhwyd.server.dao.RoleDao;
import com.zhwyd.server.dao.UserRoleDao;
import com.zhwyd.server.dao.UserTableDao;
import com.zhwyd.server.service.RoleServices;
import com.zhwyd.server.service.UserRoleServices;
import com.zhwyd.server.service.UserTableServices;
//import com.wyd.empire.admin.dao.RoleDao;
//import com.wyd.empire.admin.dao.UserRoleDao;
//import com.wyd.empire.admin.dao.UserTableDao;
//import com.wyd.empire.admin.domain.RoleTable;
//import com.wyd.empire.admin.domain.UserRole;
//import com.wyd.empire.admin.domain.UserTable;
//import com.wyd.empire.utils.I18NUtil;
//import com.wyd.empire.utils.JqueryZTreeNode;
//import com.wyd.empire.utils.PageUtil;
public class RoleServicesImpl implements RoleServices {
    private RoleDao           roleDao;
    private UserRoleServices  userRoleServices;
    private UserTableServices userTableServices;
    private UserRoleDao       userRoleDao;
    private UserTableDao      userTableDao;

    public List<JqueryZTreeNode> loadTree(String id,boolean isAdmin) {
        List<JqueryZTreeNode> treeList = new ArrayList<JqueryZTreeNode>();
        List<RoleTable> list = roleDao.findAll();
        if (list != null && list.size() > 0) {
            for (RoleTable r : list) {
                if(r.getRoleCode().equals("ROLE_ADMIN")&&!isAdmin){
                    continue;
                }
                JqueryZTreeNode tree = new JqueryZTreeNode();
                tree.setName(r.getRoleName());
                tree.setId(r.getId().toString());
                if (StringUtils.hasText(id)) {
                    List<RoleTable> values = userRoleServices.findAllById(Long.valueOf(id).longValue());
                    if (values != null && values.size() > 0) {
                        for (RoleTable rv : values) {
                            if (r.getId().equals(rv.getId())) {
                                tree.setChecked(true);
                            }
                        }
                    }
                    treeList.add(tree);
                }
            }
        }
        return treeList;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setUserRoleServices(UserRoleServices userRoleServices) {
        this.userRoleServices = userRoleServices;
    }

    public AjaxResult saveTree(String userId, String roleCheckId, HttpSession session) {
        AjaxResult result = new AjaxResult();
        try {
            if (StringUtils.hasText(userId)) {
                if (StringUtils.hasText(roleCheckId)) {
                    Long id = Long.valueOf(userId).longValue();
                    userRoleServices.deleteById(id);
                    List<Long> list = PageUtil.getCheckedId(roleCheckId);
                    UserTable user = userTableServices.findById(id);
                    for (Long checkId : list) {
                        UserRole userRole = new UserRole();
                        userRole.setUserTable(user);
                        userRole.setRoleTable(roleDao.findById(checkId));
                        userRoleServices.save(userRole);
                    }
                    result.setInfo("分配角色成功!");
                    result.setSuccess(true);
                } else {
                    result.setInfo("请给人员选择角色!");
                    result.setFailure(true);
                }
            }
        } catch (Exception ex) {
            result.setFailure(true);
            result.setInfo("提交时发生异常!");
            ex.printStackTrace();
        }
        return result;
    }

    public void setUserTableServices(UserTableServices userTableServices) {
        this.userTableServices = userTableServices;
    }

    public RoleTable findById(Long id) {
        return roleDao.findById(id);
    }

    public AjaxResult deleteRoles(String ids, HttpSession session) {
        AjaxResult result = new AjaxResult();
        try {
            int count = roleDao.deleteByIds(ids);
            if (count > 0) {
                result.setSuccess(true);
                result.setInfo(I18NUtil.returnBundle(session).getString("DeleteRoleInfoSuccess"));
            } else {
                result.setFailure(true);
                result.setInfo(I18NUtil.returnBundle(session).getString("DeleteRoleInfoFail"));
            }
        } catch (RuntimeException e) {
            result.setFailure(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("RoleBeUsing"));
        }
        return result;
    }

    public PageResponse<RoleTable> findPageList(PageRequest pageRequest) {
        return roleDao.findPageList(pageRequest);
    }

    public AjaxResult saveRole(String roleName, String roleCode, String roleDesc, HttpSession session) {
        try {
            RoleTable role = new RoleTable();
            role.setRoleName(roleName);
            role.setRoleCode(roleCode);
            role.setRoleDesc(roleDesc);
            RoleTable newRole = roleDao.insert(role);
            AjaxResult result = new AjaxResult();
            if (newRole != null) {
                result.setInfo(I18NUtil.returnBundle(session).getString("AddRoleSuccess"));
                result.setSuccess(true);
            } else {
                result.setInfo(I18NUtil.returnBundle(session).getString("AddRoleFail"));
                result.setFailure(true);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AjaxResult updateRole(String roleName, String roleCode, String roleDesc, String id, HttpSession session) {
        try {
            RoleTable role = roleDao.findById(Long.valueOf(id));
            role.setRoleName(roleName);
            role.setRoleCode(roleCode);
            role.setRoleDesc(roleDesc);
            RoleTable newRole = roleDao.update(role);
            AjaxResult result = new AjaxResult();
            if (newRole != null) {
                result.setInfo(I18NUtil.returnBundle(session).getString("UpdateRoleSuccess"));
                result.setSuccess(true);
            } else {
                result.setInfo(I18NUtil.returnBundle(session).getString("UpdateRoleFail"));
                result.setFailure(true);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JqueryZTreeNode> loadUserTree(Long rootId) {
        List<JqueryZTreeNode> treeList = new ArrayList<JqueryZTreeNode>();
        List<UserTable> list = userRoleDao.findByRoleId(rootId);
        if (list != null && list.size() > 0) {
            for (UserTable user : list) {
                JqueryZTreeNode tree = new JqueryZTreeNode();
//                tree.setName(user.getStaffTable().getFullName() + "(" + user.getUserName() + ")");
                tree.setId(user.getId().toString());
                tree.setChecked(true);
                treeList.add(tree);
            }
        }
        return treeList;
    }

    public void setUserRoleDao(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    public AjaxResult saveRoleUserTree(String roelId, String roleUserCheckId, HttpSession session) {
        AjaxResult result = new AjaxResult();
        try {
            if (StringUtils.hasText(roelId)) {
                if (StringUtils.hasText(roleUserCheckId)) {
                    userRoleDao.deleteByRoleId(Long.valueOf(roelId));
                    List<Long> list = PageUtil.getCheckedId(roleUserCheckId);
                    RoleTable role = roleDao.findById(Long.valueOf(roelId));
                    for (Long checkId : list) {
                        UserRole userRole = new UserRole();
                        userRole.setUserTable(userTableDao.get(checkId));
                        userRole.setRoleTable(role);
                        userRoleServices.save(userRole);
                    }
                    result.setInfo(I18NUtil.returnBundle(session).getString("UpdateRoleUserSuccess"));
                    result.setSuccess(true);
                } else {
                    userRoleDao.deleteByRoleId(Long.valueOf(roelId));
                    result.setInfo(I18NUtil.returnBundle(session).getString("DeleteRoleUserFail"));
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

    public void setUserTableDao(UserTableDao userTableDao) {
        this.userTableDao = userTableDao;
    }
}
