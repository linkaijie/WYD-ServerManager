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
@Table(name = "battle_server")
public class BattleServer implements Serializable, BeanInterface {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private Integer           gameId;
    private Integer           serverId;
    private String            name;
    private String            serverip;
    private Integer           port;
    private String            httpip;
    private Integer           httpport;
    private String            path;
    private Integer           state;
    private Integer           isDeploy;
    private String            modelPath;
    private String            updatePath;
    private Date              updateTime;

    public BattleServer() {
        this.modelPath = "/data/apps/model/model/battleServer";
        this.updatePath = "/data/apps/model/update/battleServer";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
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

    @Column(name = "serverip")
    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
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

    @Column(name = "httpip")
    public String getHttpip() {
        return httpip;
    }

    public void setHttpip(String httpip) {
        this.httpip = httpip;
    }

    @Column(name = "httpport")
    public Integer getHttpport() {
        return httpport;
    }

    public void setHttpport(Integer httpport) {
        this.httpport = httpport;
    }
}
