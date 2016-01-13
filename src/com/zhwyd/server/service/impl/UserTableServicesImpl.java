package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Hibernate;
import com.gotopweb.commons.ajax.AjaxResult;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserRole;
import com.zhwyd.server.bean.UserTable;
import com.zhwyd.server.common.util.I18NUtil;
import com.zhwyd.server.common.util.PageUtil;
import com.zhwyd.server.dao.UserRoleDao;
import com.zhwyd.server.dao.UserTableDao;
import com.zhwyd.server.service.UserTableServices;
import com.zhwyd.server.web.page.Pager;
public class UserTableServicesImpl extends ServiceSupportImpl<UserTable> implements UserTableServices {
    private UserTableDao userTableDao;
    private UserRoleDao  userRoleDao;

    public void setUserRoleDao(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    public void setUserTableDao(UserTableDao userTableDao) {
        this.userTableDao = userTableDao;
    }

    public List<UserTable> getUserTableList(Pager pager) {
        return userTableDao.getUserTableList();
    }

    public UserTable findByUserName(String username) {
        List<UserTable> list = userTableDao.getAll("userName", username);
        if (list != null && list.size() > 0) {
            UserTable user = list.get(0);
            return user;
        }
        return null;
    }

    public List<RoleTable> findRolesByUser(UserTable user) {
        List<RoleTable> roleList = new ArrayList<RoleTable>();
        List<UserRole> urList = userRoleDao.getAll("userTable", user);
        for (UserRole userRole : urList) {
            Hibernate.initialize(userRole.getRoleTable());
            roleList.add(userRole.getRoleTable());
        }
        return roleList;
    }

    public UserTable findById(Long id) {
        UserTable user = userTableDao.get(id);
        return user;
    }

    public boolean createUser(UserTable userTable, HttpSession session) {
        try {
            UserTable hasUser = userTableDao.get("userName", userTable.getUserName());
            if (hasUser != null) {
                return false;
            }
            userTable.setNickName(userTable.getNickName());
            userTable.setUserName(userTable.getUserName());
            userTable.setPwd(DigestUtils.md5Hex(userTable.getPwd()));
            userTableDao.save(userTable);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AjaxResult deleteUsers(String ids, HttpSession session) {
        AjaxResult result = null;
        try {
            result = new AjaxResult();
            userRoleDao.deleteByUserIds(ids);
            List<Long> list = PageUtil.getCheckedId(ids);
            for (Long id : list) {
                UserTable userTable = userTableDao.get(id);
                if (userTable != null) {
                    userTableDao.delete(userTable);
                }
            }
            result.setSuccess(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("DeleteRoleInfoSuccess"));
        } catch (RuntimeException e) {
            result.setFailure(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("DeleteRoleInfoFail"));
            e.printStackTrace();
        }
        return result;
    }

    public boolean updateUser(UserTable userTable) {// 不能修改密码
        UserTable user = userTableDao.get(userTable.getId());
        this.save(user);
        return false;
    }

    public AjaxResult checkedPwd(String oldPwd, String userId, HttpSession session) {
        UserTable user = userTableDao.get(Long.valueOf(userId));
        String tempPwd = DigestUtils.md5Hex(oldPwd);
        AjaxResult result = new AjaxResult();
        if (tempPwd.equals(user.getPwd())) {
            result.setSuccess(true);
        } else {
            result.setFailure(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("OldPasswordError"));
        }
        return result;
    }

    public AjaxResult checkedUserName(String userName, HttpSession session) {
        AjaxResult result = null;
        try {
            result = new AjaxResult();
            List<UserTable> list = userTableDao.getAll("userName", userName);
            if (list.size() > 0) {
                result.setFailure(true);
                result.setInfo(I18NUtil.returnBundle(session).getString("LoginNameExist"));
            } else {
                result.setSuccess(true);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void changeSts(String userId, String type) {
        try {
            UserTable user = userTableDao.get(Long.valueOf(userId));
            if (user != null) {
                if (("start").equals(type)) {
                    user.setAlarmed("Y");
                    userTableDao.update(user);
                } else {
                    user.setAlarmed("N");
                    userTableDao.update(user);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 找出所有异常提醒的用户
     * 
     * @return
     */
    public List<UserTable> findAlarmed() {
        return userTableDao.getAll("alarmed", "Y");
    }

    public AjaxResult changeSts(String checkedId, HttpSession session) {
        AjaxResult result = new AjaxResult();
        try {
            List<Long> list = PageUtil.getCheckedId(checkedId);
            if (list != null && list.size() > 0) {
                for (Long id : list) {
                    UserTable user = userTableDao.get("id", id);
                    if (user != null) {
                        user.setAlarmed("Y");
                        userTableDao.update(user);
                    }
                }
                result.setSuccess(true);
                result.setInfo(I18NUtil.returnBundle(session).getString("DispatchPeopleSMSSuccess"));
            }
        } catch (Exception e) {
            result.setFailure(true);
            result.setInfo(I18NUtil.returnBundle(session).getString("DispatchPeopleSMSFail"));
            e.printStackTrace();
        }
        return result;
    }

    public AjaxResult saveServerIds(String userId, String serverIds, HttpSession session) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            UserTable userTable = userTableDao.get(Long.valueOf(userId));
            userTable.setServerIds(serverIds);
            userTableDao.update(userTable);
            ajaxResult.setSuccess(true);
            ajaxResult.setInfo(I18NUtil.returnBundle(session).getString("SuccessfulOperation"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ajaxResult.setFailure(true);
            ajaxResult.setInfo(I18NUtil.returnBundle(session).getString("OperationFailed"));
        }
        return ajaxResult;
    }

    public void update(UserTable user) {
        userTableDao.update(user);
    }

    /**
     * 删除用户信息
     * 
     * @param user
     */
    public void deleteUser(UserTable user) {
        userTableDao.delete(user);
    }

    /**
     * 获取用户信息
     * 
     * @param user
     */
    public UserTable getUser(long id) {
        return userTableDao.get(id);
    }

    @Override
    public UserTable findByUsername(String username) {
        return userTableDao.get("username", username);
    }

    public void saveUser(UserTable userTable) {
        userTableDao.save(userTable);
    }
}
