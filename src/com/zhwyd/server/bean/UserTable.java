package com.zhwyd.server.bean;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.gotop.framework.core.domain.StampBaseEntity;
/**
 * UserTable entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user", uniqueConstraints = {})
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class UserTable extends StampBaseEntity {
    private static final long serialVersionUID = 1L;
    private String            userName;
    private String            pwd;
    private String            nickName;
    private String            alarmed;
    private String            isOverdue;
    private String            serverIds;
    private String            roleName;
    private boolean           isLoginIpLimit;//登录时是否限制ip

    public UserTable() {
    }

    public UserTable(String userName, String pwd) {
        this.userName = userName;
        this.pwd = pwd;
    }

    @Column(name = "user_name", nullable = false, length = 30)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "pwd", nullable = false, length = 50)
    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Column(name = "alarmed", length = 1)
    public String getAlarmed() {
        return this.alarmed;
    }

    public void setAlarmed(String alarmed) {
        this.alarmed = alarmed;
    }

    @Column(name = "is_overdue", length = 1)
    public String getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(String isOverdue) {
        this.isOverdue = isOverdue;
    }

    @Column(name = "server_ids", length = 1)
    public String getServerIds() {
        return serverIds;
    }

    public void setServerIds(String serverIds) {
        this.serverIds = serverIds;
    }

    @Transient
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    @Column(name = "is_login_ip_limit" , length = 1)
    public boolean getLoginIpLimit() {
        return isLoginIpLimit;
    }

    public void setLoginIpLimit(boolean isLoginIpLimit) {
        this.isLoginIpLimit = isLoginIpLimit;
    }

    @Column(name = "nick_name")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}