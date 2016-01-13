package com.zhwyd.server.controller.model;
public class ServerManageModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    private String            areaId;
    private Integer           serverType;
    private String            accountName;
    private Integer           accountId;
    private Integer           accountState;
    private String            ipdName;
    private Integer           ipdId;
    private Integer           ipdState;
    private String            worldName;
    private Integer           worldId;
    private Integer           worldState;
    private String            dispatchOneName;
    private Integer           dispatchOneId;
    private Integer           dispatchOneState;
    private String            dispatchTwoName;
    private Integer           dispatchTwoId;
    private Integer           dispatchTwoState;
    private String            dispatchThreeName;
    private Integer           dispatchThreeId;
    private Integer           dispatchThreeState;
    private String            dispatchFourName;
    private Integer           dispatchFourId;
    private Integer           dispatchFourState;
    private String            dispatchFiveName;
    private Integer           dispatchFiveId;
    private Integer           dispatchFiveState;
    private String            publicIp;
    private Integer           startAreaId;          // 开始分区号
    private Integer           endAreaId;            // 结束分区号

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountState() {
        return accountState;
    }

    public void setAccountState(Integer accountState) {
        this.accountState = accountState;
    }

    public String getIpdName() {
        return ipdName;
    }

    public void setIpdName(String ipdName) {
        this.ipdName = ipdName;
    }

    public Integer getIpdId() {
        return ipdId;
    }

    public void setIpdId(Integer ipdId) {
        this.ipdId = ipdId;
    }

    public Integer getIpdState() {
        return ipdState;
    }

    public void setIpdState(Integer ipdState) {
        this.ipdState = ipdState;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Integer getWorldId() {
        return worldId;
    }

    public void setWorldId(Integer worldId) {
        this.worldId = worldId;
    }

    public Integer getWorldState() {
        return worldState;
    }

    public void setWorldState(Integer worldState) {
        this.worldState = worldState;
    }

    public String getDispatchOneName() {
        return dispatchOneName;
    }

    public void setDispatchOneName(String dispatchOneName) {
        this.dispatchOneName = dispatchOneName;
    }

    public Integer getDispatchOneId() {
        return dispatchOneId;
    }

    public void setDispatchOneId(Integer dispatchOneId) {
        this.dispatchOneId = dispatchOneId;
    }

    public Integer getDispatchOneState() {
        return dispatchOneState;
    }

    public void setDispatchOneState(Integer dispatchOneState) {
        this.dispatchOneState = dispatchOneState;
    }

    public String getDispatchTwoName() {
        return dispatchTwoName;
    }

    public void setDispatchTwoName(String dispatchTwoName) {
        this.dispatchTwoName = dispatchTwoName;
    }

    public Integer getDispatchTwoId() {
        return dispatchTwoId;
    }

    public void setDispatchTwoId(Integer dispatchTwoId) {
        this.dispatchTwoId = dispatchTwoId;
    }

    public Integer getDispatchTwoState() {
        return dispatchTwoState;
    }

    public void setDispatchTwoState(Integer dispatchTwoState) {
        this.dispatchTwoState = dispatchTwoState;
    }

    public String getDispatchThreeName() {
        return dispatchThreeName;
    }

    public void setDispatchThreeName(String dispatchThreeName) {
        this.dispatchThreeName = dispatchThreeName;
    }

    public Integer getDispatchThreeId() {
        return dispatchThreeId;
    }

    public void setDispatchThreeId(Integer dispatchThreeId) {
        this.dispatchThreeId = dispatchThreeId;
    }

    public Integer getDispatchThreeState() {
        return dispatchThreeState;
    }

    public void setDispatchThreeState(Integer dispatchThreeState) {
        this.dispatchThreeState = dispatchThreeState;
    }

    public String getDispatchFourName() {
        return dispatchFourName;
    }

    public void setDispatchFourName(String dispatchFourName) {
        this.dispatchFourName = dispatchFourName;
    }

    public Integer getDispatchFourId() {
        return dispatchFourId;
    }

    public void setDispatchFourId(Integer dispatchFourId) {
        this.dispatchFourId = dispatchFourId;
    }

    public Integer getDispatchFourState() {
        return dispatchFourState;
    }

    public void setDispatchFourState(Integer dispatchFourState) {
        this.dispatchFourState = dispatchFourState;
    }

    public String getDispatchFiveName() {
        return dispatchFiveName;
    }

    public void setDispatchFiveName(String dispatchFiveName) {
        this.dispatchFiveName = dispatchFiveName;
    }

    public Integer getDispatchFiveId() {
        return dispatchFiveId;
    }

    public void setDispatchFiveId(Integer dispatchFiveId) {
        this.dispatchFiveId = dispatchFiveId;
    }

    public Integer getDispatchFiveState() {
        return dispatchFiveState;
    }

    public void setDispatchFiveState(Integer dispatchFiveState) {
        this.dispatchFiveState = dispatchFiveState;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    public String getPublicIp() {
        return this.publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
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
}
