package com.zhwyd.server.bean;

// default package
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.gotop.framework.core.domain.StampBaseEntity;
/**
 * RoleTable entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "role", uniqueConstraints = {})
@AttributeOverride(name = "id", column = @Column(name = "role_id"))
public class RoleTable extends StampBaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Fields
    private String            roleName;
    private String            roleCode;
    private String            roleDesc;

    // Constructors
    /** default constructor */
    public RoleTable() {
    }

    // Property accessors

    @Column(name = "role_name", length = 30)
    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Column(name = "role_code", length = 30)
    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Column(name = "role_desc", length = 30)
    public String getRoleDesc() {
        return this.roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
}