package com.zhwyd.server.bean;
import java.util.concurrent.CountDownLatch;
/**
 * 玩家信息
 * 
 * @author rL.
 */
public class User {
    private String         username;  // 用户名
    private String         password;  // 密码
    private String         uuid;      // 从账号平台获取的id
    private Integer        sessionId; // sessionId用户唯一标示
    private CountDownLatch countDown; // 主要是用于给ProtocolErrorHandler在有错误信息时把计数器-1，防止因为抛错而卡死
    private boolean        hasError;  // 状态标识
    private Integer        playerId;  // 角色id
    private String         playerName; // 角色名称

    public User() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public CountDownLatch getCountDown() {
        return countDown;
    }

    public void setCountDown(CountDownLatch countDown) {
        this.countDown = countDown;
    }

    public boolean hasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isHasError() {
        return hasError;
    }
}
