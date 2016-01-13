package com.zhwyd.server.sshxcute;
public class Sshbean {
    // 连接池属性
    private String  sshIp;
    private String  userName;
    private String  password;
    private int     sshPort;
    private int     useType;
    // 连接池名字
    private String  poolName;
    private int     minConnections       = 10;            // 空闲池，最小连接数
    // private int maxConnections = 30;
    private int     maxConnections       = 3;            // 空闲池，最大连接数
    private int     initConnections      = 1;             // 初始化连接数
    private long    connTimeOut          = 1000;          // 重复获得连接的频率
    private int     maxActiveConnections = 100;           // 最大允许的连接数，和数据库对应
    private long    connectionTimeOut    = 1000 * 60 * 20; // 连接超时时间，默认20分钟
    private boolean isCurrentConnection  = true;          // 是否获得当前连接，默认true
    private boolean isCheakPool          = true;          // 是否定时检查连接池
    private long    lazyCheck            = 1000 * 60 * 60; // 延迟多少时间后开始 检查
    private long    periodCheck          = 1000 * 60 * 60; // 检查频率

    public Sshbean(String sshIp, String url, String userName, String password, String poolName) {
        super();
        this.sshIp = sshIp;
        this.userName = userName;
        this.password = password;
        this.poolName = poolName;
    }

    public Sshbean() {
    }

    public String getSshIp() {
        return sshIp;
    }

    public void setSshIp(String sshIp) {
        this.sshIp = sshIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMinConnections() {
        return minConnections;
    }

    public void setMinConnections(int minConnections) {
        this.minConnections = minConnections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getInitConnections() {
        return initConnections;
    }

    public void setInitConnections(int initConnections) {
        this.initConnections = initConnections;
    }

    public long getConnTimeOut() {
        return connTimeOut;
    }

    public void setConnTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
    }

    public int getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public void setMaxActiveConnections(int maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
    }

    public long getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(long connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public boolean isCurrentConnection() {
        return isCurrentConnection;
    }

    public void setCurrentConnection(boolean isCurrentConnection) {
        this.isCurrentConnection = isCurrentConnection;
    }

    public boolean isCheakPool() {
        return isCheakPool;
    }

    public void setCheakPool(boolean isCheakPool) {
        this.isCheakPool = isCheakPool;
    }

    public long getLazyCheck() {
        return lazyCheck;
    }

    public void setLazyCheck(long lazyCheck) {
        this.lazyCheck = lazyCheck;
    }

    public long getPeriodCheck() {
        return periodCheck;
    }

    public void setPeriodCheck(long periodCheck) {
        this.periodCheck = periodCheck;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }
}
