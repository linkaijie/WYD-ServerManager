package com.zhwyd.server.bean;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "jdbc_url")
public class JdbcUrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private String            jdbcKey;
    private String            jdbcValue;
    private String            remark;
    private Integer           type;
    private Integer           status;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "jdbc_key")
    public String getJdbcKey() {
        return jdbcKey;
    }

    public void setJdbcKey(String jdbcKey) {
        this.jdbcKey = jdbcKey;
    }

    @Column(name = "jdbc_value")
    public String getJdbcValue() {
        return jdbcValue;
    }

    public void setJdbcValue(String jdbcValue) {
        this.jdbcValue = jdbcValue;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
