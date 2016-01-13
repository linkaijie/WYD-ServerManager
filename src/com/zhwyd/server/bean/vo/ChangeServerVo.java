package com.zhwyd.server.bean.vo;
public class ChangeServerVo extends BatchOpearVo {
    private Integer serverId;
    private Integer dispatchServerId;

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getDispatchServerId() {
        return dispatchServerId;
    }

    public void setDispatchServerId(Integer dispatchServerId) {
        this.dispatchServerId = dispatchServerId;
    }
}
