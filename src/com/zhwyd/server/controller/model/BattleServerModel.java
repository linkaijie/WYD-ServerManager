package com.zhwyd.server.controller.model;
public class BattleServerModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    private Integer           battleId;

    public Integer getBattleId() {
        return battleId;
    }

    public void setBattleId(Integer battleId) {
        this.battleId = battleId;
    }
}
