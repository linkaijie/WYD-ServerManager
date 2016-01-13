package com.zhwyd.server.bean;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.zhwyd.server.common.constant.CommanConstant;
@Entity
@Table(name = "server")
public class Server implements Serializable, BeanInterface {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private String            serverName;           // 服务名称（机子外网IP）
    private String            serverIp;             // 机子内网ip
    private Integer           serverPort;           // 端口
    private Integer           httpPort;             // http端口（暂无用）
    private String            sshName;              // 登录名
    private String            sshPwd;               // 登录密码
    private String            sshUrl;               // url（暂无用）
    private Date              createTime;           // 创建时间
    private Date              updateTime;           // 更新时间
    private Date              rsaTime;              // 授权时间
    private Integer           type;                 // lib或serverLib
    private Integer           useType;              // 服务器使用类型（1：游戏服务器，2:缓存服务器，3:数据库服务器）
    private String            remark;               // 描述

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "server_name")
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Column(name = "server_ip")
    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @Column(name = "server_port")
    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Column(name = "http_port")
    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    @Column(name = "ssh_name")
    public String getSshName() {
        return sshName;
    }

    public void setSshName(String sshName) {
        this.sshName = sshName;
    }

    @Column(name = "ssh_pwd")
    public String getSshPwd() {
        return sshPwd;
    }

    public void setSshPwd(String sshPwd) {
        this.sshPwd = sshPwd;
    }

    @Column(name = "ssh_url")
    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "rsa_time")
    public Date getRsaTime() {
        return rsaTime;
    }

    public void setRsaTime(Date rsaTime) {
        this.rsaTime = rsaTime;
    }

    @Column(name = "use_type")
    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    @Override
    @Transient
    public Integer getGameId() {
        return 1;
    }

    @Override
    @Transient
    public String getName() {
        return this.getServerName();
    }

    @Override
    @Transient
    public Integer getServerId() {
        return this.getId();
    }

    @Override
    @Transient
    public String getPath() {
        String path = "";
        if (this.type == CommanConstant.SERVER_LIB) {
            path = "/data/apps/gunsoul/serverLib";
        } else if (this.type == CommanConstant.LIB) {
            path = "/data/apps/gunsoul/lib";
        }
        return path;
    }

    @Transient
    public String getLibPath() {
        return "/data/apps/gunsoul/lib";
    }

    @Override
    @Transient
    public Integer getState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transient
    public Integer getIsDeploy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transient
    public String getModelPath() {
        String modelPath = "";
        if (this.type == CommanConstant.SERVER_LIB) {
            modelPath = "/data/apps/model/model/serverLib";
        } else if (this.type == CommanConstant.LIB) {
            modelPath = "/data/apps/model/model/lib";
        }
        return modelPath;
    }

    @Transient
    public String getLibModelPath() {
        return "/data/apps/model/model/lib";
    }

    @Override
    @Transient
    public String getUpdatePath() {
        String updatePath = "";
        if (this.type == CommanConstant.SERVER_LIB) {
            updatePath = "/data/apps/model/update/serverLib";
        } else if (this.type != null && this.type == CommanConstant.LIB) {
            updatePath = "/data/apps/model/update/lib";
        }
        return updatePath;
    }

    @Transient
    public String getLibUpdatePath() {
        return "/data/apps/model/update/lib";
    }

    @Override
    @Transient
    public void setIsDeploy(Integer isDeploy) {
        // TODO Auto-generated method stub
    }

    @Override
    @Transient
    public void setState(Integer state) {
        // TODO Auto-generated method stub
    }

    @Transient
    public Integer getType() {
        return type;
    }

    @Transient
    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
