package com.zhwyd.server.sshxcute.pool;
import net.neoremind.sshxcute.core.SSHExec;
public interface IConnectionPool {
    // 获得连接
    public SSHExec getConnection();

    // 获得当前连接
    public SSHExec getCurrentConnecton();

    // 回收连接
    public void releaseConn(SSHExec sshExec) throws Exception ;

    // 销毁清空
    public void destroy();

    // 连接池是活动状态
    public boolean isActive();

    // 定时器，检查连接池
    public void cheackPool();
    
    /**
     * 创建剩余连接
     */
    public void createConnect();
}
