package com.zhwyd.server.controller.model;
import java.io.Serializable;
import com.zhwyd.server.web.page.Pager;
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer           gameId;               // 游戏
    private Integer           serverId;             // 服务（机子）ID
    private String            name;                 // 进程名称
    private String            path;                 // 服务路径
    private String            command;              // 输入命令
    private String            Pid;                  // 进程号
    private String            areaIds;              // 开始分区号
    private Integer           startAreaId;          // 开始分区号
    private Integer           endAreaId;            // 结束分区号
    private Integer           serverType;
    private Integer           updateType;           // 更新類型
    private String            publicIp;
    private Integer           status;
    private Pager             pager;                // 分页

    public BaseModel() {
    }

    public BaseModel(Integer gameId, Integer serverId, String name, String path, String command) {
        this.gameId = gameId;
        this.serverId = serverId;
        this.name = name;
        this.path = path;
        this.command = command;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public Pager getPager() {
        if (pager == null) {
            pager = new Pager();
        }
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public Integer getStartAreaId() {
        return startAreaId;
    }

    public void setStartAreaId(Integer startAreaId) {
        this.startAreaId = startAreaId;
    }

    public Integer getEndAreaId() {
        return endAreaId;
    }

    public void setEndAreaId(Integer endAreaId) {
        this.endAreaId = endAreaId;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }
}
