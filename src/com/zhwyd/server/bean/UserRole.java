package com.zhwyd.server.bean;

// default package
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.gotop.framework.core.domain.StampBaseEntity;
/**
 * UserRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user_role", uniqueConstraints = {})
@AttributeOverride(name = "id", column = @Column(name = "ur_id"))
public class UserRole extends StampBaseEntity{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Fields
    private RoleTable roleTable;
    private UserTable userTable;

    // Constructors
    /** default constructor */
    public UserRole() {
    }

    // Property accessors

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public RoleTable getRoleTable() {
        return this.roleTable;
    }

    public void setRoleTable(RoleTable roleTable) {
        this.roleTable = roleTable;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserTable getUserTable() {
        return this.userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }
}