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
 * RoleResource entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "role_resource", uniqueConstraints = {})
@AttributeOverride(name = "id", column = @Column(name = "rr_id"))
public class RoleResource extends StampBaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Fields
    private RoleTable     roleTable;
    private ResourceTable resourceTable;

    // Constructors
    /** default constructor */
    public RoleResource() {
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
    @JoinColumn(name = "reso_id")
    public ResourceTable getResourceTable() {
        return this.resourceTable;
    }

    public void setResourceTable(ResourceTable resourceTable) {
        this.resourceTable = resourceTable;
    }
}