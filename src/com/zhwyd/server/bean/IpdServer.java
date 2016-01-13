package com.zhwyd.server.bean;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "ipd_server")
public class IpdServer implements Serializable, BeanInterface {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private Integer           gameId;
    private Integer           serverId;
    private String            name;
    private String            localip;
    private Integer           port;
    private Integer           http;
    private String            path;
    private Integer           state;
    private Integer           isDeploy;
    private String            modelPath        = "/data/apps/model/update/ipdmain";
    private String            updatePath       = "/data/apps/model/update/ipdmain";
    private Date              updateTime;
    private Integer           isMain;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "isid", length = 11)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "game_id")
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "port")
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "server_id")
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Column(name = "localip")
    public String getLocalip() {
        return localip;
    }

    public void setLocalip(String localip) {
        this.localip = localip;
    }

    @Column(name = "http")
    public Integer getHttp() {
        return http;
    }

    public void setHttp(Integer http) {
        this.http = http;
    }

    @Column(name = "is_deploy")
    public Integer getIsDeploy() {
        return isDeploy;
    }

    public void setIsDeploy(Integer isDeploy) {
        this.isDeploy = isDeploy;
    }

    @Column(name = "model_path")
    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    @Column(name = "update_path")
    public String getUpdatePath() {
        return updatePath;
    }

    public void setUpdatePath(String updatePath) {
        this.updatePath = updatePath;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "is_main")
    public Integer getIsMain() {
        return isMain;
    }

    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }
}
