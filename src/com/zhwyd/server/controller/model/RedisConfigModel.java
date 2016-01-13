package com.zhwyd.server.controller.model;
public class RedisConfigModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    private Integer           redisConfigId;
    private Integer           areaId;

    public Integer getRedisConfigId() {
        return redisConfigId;
    }

    public void setRedisConfigId(Integer redisConfigId) {
        this.redisConfigId = redisConfigId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}
