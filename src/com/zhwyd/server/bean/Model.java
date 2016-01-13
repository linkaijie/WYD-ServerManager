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
@Table(name = "model")
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private String            name;
    private Integer           gameId;
    private Integer           serverId;
    private String            accountPath;
    private String            ipdmainPath;
    private String            worldPath;
    private String            dispatchPatch;
    private Date              createTime;
    private Date              updateTime;
    private Date              worldUpdateTime;
    private Date              dispatchUpdateTime;
    private Date              ipdUpdateTime;
    private Date              accountUpdateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 11)
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

    @Column(name = "account_path")
    public String getAccountPath() {
        return accountPath;
    }

    public void setAccountPath(String accountPath) {
        this.accountPath = accountPath;
    }

    @Column(name = "ipdmain_path")
    public String getIpdmainPath() {
        return ipdmainPath;
    }

    public void setIpdmainPath(String ipdmainPath) {
        this.ipdmainPath = ipdmainPath;
    }

    @Column(name = "world_path")
    public String getWorldPath() {
        return worldPath;
    }

    public void setWorldPath(String worldPath) {
        this.worldPath = worldPath;
    }

    @Column(name = "dispatch_patch")
    public String getDispatchPatch() {
        return dispatchPatch;
    }

    public void setDispatchPatch(String dispatchPatch) {
        this.dispatchPatch = dispatchPatch;
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

    @Column(name = "world_update_time")
    public Date getWorldUpdateTime() {
        return worldUpdateTime;
    }

    public void setWorldUpdateTime(Date worldUpdateTime) {
        this.worldUpdateTime = worldUpdateTime;
    }

    @Column(name = "dispatch_update_time")
    public Date getDispatchUpdateTime() {
        return dispatchUpdateTime;
    }

    public void setDispatchUpdateTime(Date dispatchUpdateTime) {
        this.dispatchUpdateTime = dispatchUpdateTime;
    }

    @Column(name = "ipd_update_time")
    public Date getIpdUpdateTime() {
        return ipdUpdateTime;
    }

    public void setIpdUpdateTime(Date ipdUpdateTime) {
        this.ipdUpdateTime = ipdUpdateTime;
    }

    @Column(name = "account_update_time")
    public Date getAccountUpdateTime() {
        return accountUpdateTime;
    }

    public void setAccountUpdateTime(Date accountUpdateTime) {
        this.accountUpdateTime = accountUpdateTime;
    }
}
