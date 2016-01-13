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
@Table(name = "redis_config")
public class RedisConfig implements Serializable, BeanInterface {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private Integer           gameId;
    private String            name;                 // 名称
    private Integer           serverId;             // 服务ID
    private Integer           areaId;               // 分区号，对应分区表
    private Byte              redisType;            // 1：游戏服redis， 2：角色服redis
    private String            ports;                // 端口号，一个或多个
    private Integer           state;                // 开启状态
    private Integer           isDeploy;             // 是否部署
    private String            modelPath;            // 模板路径
    private String            updatePath;           // 更新路径
    private String            path;                 // 部署路径
    private Date              createTime;           // 创建时间
    private Date              updateTime;           // 更新时间

    public RedisConfig() {
        this.modelPath = "/data/apps/model/model/redis";
        this.updatePath = "/data/apps/model/update/redis";
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

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "area_id")
    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    @Column(name = "server_id")
    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Column(name = "redis_type")
    public Byte getRedisType() {
        return redisType;
    }

    public void setRedisType(Byte redisType) {
        this.redisType = redisType;
    }

    @Column(name = "ports")
    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
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

    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    @Column(name = "game_id")
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
