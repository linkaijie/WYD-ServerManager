package com.zhwyd.server.bean;
import java.util.Date;
public interface BeanInterface {
    public abstract Integer getId();

    public abstract Integer getGameId();

    public abstract String getName();

    public abstract Integer getServerId();

    public abstract String getPath();

    public abstract Integer getState();

    public abstract Integer getIsDeploy();

    public abstract String getModelPath();

    public abstract String getUpdatePath();

    public abstract void setUpdateTime(Date date);

    public abstract void setIsDeploy(Integer isDeploy);

    public abstract void setState(Integer state);
}
