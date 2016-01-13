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
@Table(name = "dispatch_server")
public class DispatchServer implements Serializable, BeanInterface {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private Integer           gameId;
    private String            name;
    private Integer           serverId;
    private Integer           worldId;
    private Integer           ipdId;
    private String            worldip;
    private Integer           worldport;
    private String            localip;
    private Integer           port;
    private String            servertype;
    private String            publicserver;
    private Integer           publicport;
    private Integer           dispatchServerId;
    private String            serverarea;
    private Integer           machinecode;
    private Integer           companycode;
    private String            dispatcherserver;
    private Integer           dispatcherport;
    private String            path;
    private Integer           state;
    private Integer           isDeploy;
    private String            modelPath        = "/data/apps/model/model/dispatchServer";
    private String            updatePath       = "/data/apps/model/update/dispatchServer";
    private Date              updateTime;
    private Integer           isUse;
    private Integer           orders;
    private Integer           number;                                                     // 暂时无用
    private Integer           serverType;                                                  // 類型：2混服；3硬核

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dsid", length = 11)
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

    @Column(name = "server_id")
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Column(name = "world_id")
    public Integer getWorldId() {
        return worldId;
    }

    public void setWorldId(Integer worldId) {
        this.worldId = worldId;
    }

    @Column(name = "ipd_id")
    public Integer getIpdId() {
        return ipdId;
    }

    public void setIpdId(Integer ipdId) {
        this.ipdId = ipdId;
    }

    @Column(name = "worldip")
    public String getWorldip() {
        return worldip;
    }

    public void setWorldip(String worldip) {
        this.worldip = worldip;
    }

    @Column(name = "worldport")
    public Integer getWorldport() {
        return worldport;
    }

    public void setWorldport(Integer worldport) {
        this.worldport = worldport;
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

    @Column(name = "servertype")
    public String getServertype() {
        return servertype;
    }

    public void setServertype(String servertype) {
        this.servertype = servertype;
    }

    @Column(name = "publicserver")
    public String getPublicserver() {
        return publicserver;
    }

    public void setPublicserver(String publicserver) {
        this.publicserver = publicserver;
    }

    @Column(name = "publicport")
    public Integer getPublicport() {
        return publicport;
    }

    public void setPublicport(Integer publicport) {
        this.publicport = publicport;
    }

    @Column(name = "id")
    public Integer getDispatchServerId() {
        return dispatchServerId;
    }

    public void setDispatchServerId(Integer id) {
        this.dispatchServerId = id;
    }

    @Column(name = "serverarea")
    public String getServerarea() {
        return serverarea;
    }

    public void setServerarea(String serverarea) {
        this.serverarea = serverarea;
    }

    @Column(name = "machinecode")
    public Integer getMachinecode() {
        return machinecode;
    }

    public void setMachinecode(Integer machinecode) {
        this.machinecode = machinecode;
    }

    @Column(name = "companycode")
    public Integer getCompanycode() {
        return companycode;
    }

    public void setCompanycode(Integer companycode) {
        this.companycode = companycode;
    }

    @Column(name = "dispatcherserver")
    public String getDispatcherserver() {
        return dispatcherserver;
    }

    public void setDispatcherserver(String dispatcherserver) {
        this.dispatcherserver = dispatcherserver;
    }

    @Column(name = "dispatcherport")
    public Integer getDispatcherport() {
        return dispatcherport;
    }

    public void setDispatcherport(Integer dispatcherport) {
        this.dispatcherport = dispatcherport;
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

    @Column(name = "is_use")
    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    @Column(name = "orders")
    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    @Column(name = "number")
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Column(name = "server_type")
    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }
}
