package com.zhwyd.server.service;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.gotopweb.commons.ajax.AjaxResult;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserTable;
import com.zhwyd.server.web.page.Pager;
public interface UserTableServices extends ServiceSupport<UserTable> {
    /**
     * 根据用户名查询用户
     * 
     * @param username
     * @return
     */
    public UserTable findByUserName(String username);

    /**
     * 根据用户查找角色
     * 
     * @param user
     * @return
     */
    public List<RoleTable> findRolesByUser(UserTable user);

    /**
     * 根据ID值查询用户
     * 
     * @param id
     * @return
     */
    public UserTable findById(Long id);

    /**
     * 根据多个ID值删除用户
     * 
     * @param ids
     * @return
     */
    public AjaxResult deleteUsers(String ids, HttpSession session);

    // 用户修改密码时，密码检验
    public AjaxResult checkedPwd(String oldPwd, String userId, HttpSession session);

    /**
     * 创建用户时检验用户名
     * 
     * @param userName
     * @return
     */
    public AjaxResult checkedUserName(String userName, HttpSession session);

    /**
     * 是否提示系统提醒信息
     * 
     * @param userId
     * @param type
     */
    public void changeSts(String userId, String type);

    /**
     * 批量分配人员提醒
     * 
     * @param checkedId
     * @return
     */
    public AjaxResult changeSts(String checkedId, HttpSession session);

    /**
     * 找出所有异常提醒的用户
     * 
     * @return
     */
    public List<UserTable> findAlarmed();

    /**
     * 更新用户信息
     * 
     * @param user
     */
    public void update(UserTable user);

    /**
     * 删除用户信息
     * 
     * @param user
     */
    public void deleteUser(UserTable user);

    /**
     * 获取用户信息
     * 
     * @param user
     */
    public UserTable getUser(long id);

    /**
     * 保存用户分配的游戏服
     * 
     * @param userId
     * @param serverIds
     * @return
     */
    public AjaxResult saveServerIds(String userId, String serverIds, HttpSession session);

    /**
     * 根据用户名查询用户
     * 
     * @param username
     * @return 用户
     */
    public UserTable findByUsername(String username);

    /**
     * 获取用户列表
     * author:LKJ 
     * 2014-11-27 
     * @param pager
     * @return
     */
    public List<UserTable> getUserTableList(Pager pager);

    /**
     * 保存玩家信息
     * author:LKJ 
     * 2014-11-27 
     * @param userTable
     */
    public void saveUser(UserTable userTable);
}
