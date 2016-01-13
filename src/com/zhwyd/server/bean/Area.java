package com.zhwyd.server.bean;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "area_info")
public class Area implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private Integer           serverType;           // 服务类型
    private String            name;                 // 名称
    private String            sign;                 // 地区标志
    private String            trib;                 // redis-trib.rb中的所有节点信息
    private Byte              statue;               // 状态

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "server_type")
    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    @Column(name = "sign")
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Column(name = "trib")
    public String getTrib() {
        return trib;
    }

    public void setTrib(String trib) {
        this.trib = trib;
    }

    @Column(name = "statue")
    public Byte getStatue() {
        return statue;
    }

    public void setStatue(Byte statue) {
        this.statue = statue;
    }
}
