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
@Table(name = "world_server")
public class WorldServer implements Serializable, BeanInterface {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private Integer           gameId;
    private Integer           serverId;
    private Integer           accountId;
    private String            modelPath        = "/data/apps/model/model/worldServer";
    private String            name;
    private String            localip;
    private Integer           port;
    private String            adminip;
    private Integer           adminport;
    private String            httpip;
    private String            publicip;
    private Integer           httpport;
    private String            authip;
    private Integer           authport;
    private String            serverpassword;
    private String            redisip;
    private Integer           redisport;
    private String            areaid;
    private Integer           machinecode;
    private Integer           serverType;
    private String            did;
    private String            path;
    private Integer           state;
    private Integer           isDeploy;
    private String            updatePath       = "/data/apps/model/update/worldServer";
    private Date              updateTime;
    private Integer           orders;
    private String            mergeIds;
    private String            battleip;
    private Integer           battleport;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "wsid", length = 11)
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

    @Column(name = "server_id")
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "localip")
    public String getLocalip() {
        return localip;
    }

    public void setLocalip(String localip) {
        this.localip = localip;
    }

    @Column(name = "port")
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Column(name = "adminip")
    public String getAdminip() {
        return adminip;
    }

    public void setAdminip(String adminip) {
        this.adminip = adminip;
    }

    @Column(name = "adminport")
    public Integer getAdminport() {
        return adminport;
    }

    public void setAdminport(Integer adminport) {
        this.adminport = adminport;
    }

    @Column(name = "httpip")
    public String getHttpip() {
        return httpip;
    }

    public void setHttpip(String httpip) {
        this.httpip = httpip;
    }

    @Column(name = "publicip")
    public String getPublicip() {
        return publicip;
    }

    public void setPublicip(String publicip) {
        this.publicip = publicip;
    }

    @Column(name = "httpport")
    public Integer getHttpport() {
        return httpport;
    }

    public void setHttpport(Integer httpport) {
        this.httpport = httpport;
    }

    @Column(name = "authip")
    public String getAuthip() {
        return authip;
    }

    public void setAuthip(String authip) {
        this.authip = authip;
    }

    @Column(name = "authport")
    public Integer getAuthport() {
        return authport;
    }

    public void setAuthport(Integer authport) {
        this.authport = authport;
    }

    @Column(name = "serverpassword")
    public String getServerpassword() {
        return serverpassword;
    }

    public void setServerpassword(String serverpassword) {
        this.serverpassword = serverpassword;
    }

    @Column(name = "redisip")
    public String getRedisip() {
        return redisip;
    }

    public void setRedisip(String redisip) {
        this.redisip = redisip;
    }

    @Column(name = "redisport")
    public Integer getRedisport() {
        return redisport;
    }

    public void setRedisport(Integer redisport) {
        this.redisport = redisport;
    }

    @Column(name = "areaid")
    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    @Column(name = "machinecode")
    public Integer getMachinecode() {
        return machinecode;
    }

    public void setMachinecode(Integer machinecode) {
        this.machinecode = machinecode;
    }

    @Column(name = "serverType")
    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    @Column(name = "did")
    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
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

    @Column(name = "orders")
    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    @Column(name = "merge_ids")
    public String getMergeIds() {
        return mergeIds;
    }

    public void setMergeIds(String mergeIds) {
        this.mergeIds = mergeIds;
    }

    @Column(name = "battleip")
    public String getBattleip() {
        return battleip;
    }

    public void setBattleip(String battleip) {
        this.battleip = battleip;
    }

    @Column(name = "battleport")
    public Integer getBattleport() {
        return battleport;
    }

    public void setBattleport(Integer battleport) {
        this.battleport = battleport;
    }
}
