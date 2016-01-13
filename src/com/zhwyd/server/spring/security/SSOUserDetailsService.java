package com.zhwyd.server.spring.security;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.zhwyd.server.bean.RoleTable;
import com.zhwyd.server.bean.UserTable;
import com.zhwyd.server.service.UserTableServices;
/**
 * @author HeQing (GoTop, Inc.)
 * @version 1.0 Nov 11, 2009
 */
public class SSOUserDetailsService implements UserDetailsService {
    private UserTableServices userTableServices;

    /*
     * (non-Javadoc)
     * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        UserTable user = userTableServices.findByUserName(userName);
        if (user == null) throw new UsernameNotFoundException(userName + "is not exist!");
        List<RoleTable> roleList = userTableServices.findRolesByUser(user);
        GrantedAuthority[] arrayAuths = new GrantedAuthority[roleList.size() + 1];
        for (int i = 0; i < roleList.size(); i++) {
            arrayAuths[i] = new GrantedAuthorityImpl(roleList.get(i).getRoleCode());
        }
        arrayAuths[roleList.size()] = new GrantedAuthorityImpl("ROLE_DEFAULT");
        return new User(userName, user.getPwd(), true, true, true, true, arrayAuths);
    }

    public void setUserTableServices(UserTableServices userTableServices) {
        this.userTableServices = userTableServices;
    }
}