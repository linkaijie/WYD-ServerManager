package com.zhwyd.server.sshxcute.pool;
import java.util.Hashtable;
import net.neoremind.sshxcute.core.SSHExec;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.common.util.CryptionUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.sshxcute.Sshbean;
public class ConnectionPoolManager {
    // 连接池存放
    public Hashtable<String, IConnectionPool> pools = new Hashtable<String, IConnectionPool>();

    /**
     * 初始化
     */
    private ConnectionPoolManager() {
        init();// 初始化连接
    }

    /**
     * 单例实现
     * @return
     */
    public static ConnectionPoolManager getInstance() {
        return Singtonle.instance;
    }
    /**
     * 获取单例对象
     */
    private static class Singtonle {
        private static ConnectionPoolManager instance = new ConnectionPoolManager();
    }

    /**
     * 创建剩余连接
     * @return
     */
    public void initSurplusConnect() {
        createSurplusConnect();// 多线程创建剩余连接
    }

    /**
     * 初始化所有的连接池
     */
    public void init() {
        try {
            for (int i = 0; i < SshInitInfo.beans.size(); i++) {
                Sshbean bean = SshInitInfo.beans.get(i);
                ConnectionPool pool = new ConnectionPool(bean);
                if (pool != null) {
                    pools.put(bean.getPoolName(), pool);
                    System.out.println("Info:Init connection successed ->" + bean.getPoolName());
                }
                Thread.sleep(100l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建连接
     * @param server
     */
    public void newPool(Server server) {
        Sshbean sshbean = new Sshbean();
        sshbean.setPoolName(server.getServerIp());
        sshbean.setSshIp(server.getServerIp());
        sshbean.setUserName(server.getSshName());
        sshbean.setSshPort(server.getServerPort());
        String psw = CryptionUtil.getDecryptString(server.getSshPwd(), Application.getConfig("system", "key"));
        sshbean.setPassword(psw);
        ConnectionPool pool = new ConnectionPool(sshbean, "");
        if (pool != null) {
            pools.put(sshbean.getPoolName(), pool);
            System.out.println("Info:Init connection successed ->" + sshbean.getPoolName());
        }
    }

    /**
     * 获得连接,根据连接池名字 获得连接
     * @param poolName
     * @return
     */
    public SSHExec getConnection(String poolName) {
        SSHExec sshExec = null;
        if (pools.size() > 0 && pools.containsKey(poolName)) {
            sshExec = getPool(poolName).getConnection();
        } else {
            System.out.println("Error:Can't find this connecion pool ->" + poolName);
        }
        return sshExec;
    }

    /**
     * 关闭，回收连接
     * @param poolName
     * @param sshExec
     */
    public void close(String poolName, SSHExec sshExec) {
        IConnectionPool pool = getPool(poolName);
        try {
            if (pool != null) {
                pool.releaseConn(sshExec);
            }
        } catch (Exception e) {
            System.out.println("连接池已经销毁");
            e.printStackTrace();
        }
    }

    /**
     * 清空连接池
     * @param poolName
     */
    public void destroy(String poolName) {
        IConnectionPool pool = getPool(poolName);
        if (pool != null) {
            pool.destroy();
        }
    }

    /**
     * 获得连接池
     * @param poolName
     * @return
     */
    public IConnectionPool getPool(String poolName) {
        IConnectionPool pool = null;
        if (pools.size() > 0) {
            pool = pools.get(poolName);
        }
        return pool;
    }

    /**
     * 创建剩余连接
     */
    public void createSurplusConnect() {
        for (int i = 0; i < SshInitInfo.beans.size(); i++) {
            Sshbean bean = SshInitInfo.beans.get(i);
            if (bean.getUseType() == 1) {// 游戏服才创建
                Application.getManager().getSimpleThreadPool().execute(this.createSurplusConnectTask(bean));
            }
        }
    }

    /**
     * 创建剩余连接
     */
    private Runnable createSurplusConnectTask(Sshbean bean) {
        return new SurplusConnectThread(bean);
    }
    /**
     * 创建剩余连接线程类
     */
    public class SurplusConnectThread implements Runnable {
        Sshbean               bean;
        ConnectionPoolManager connectionPoolManager;

        public SurplusConnectThread(Sshbean bean) {
            this.bean = bean;
            this.connectionPoolManager = ConnectionPoolManager.getInstance();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(20 * 1000l);// 20秒后在创建
                if (connectionPoolManager.pools.size() > 0 && connectionPoolManager.pools.containsKey(bean.getPoolName())) {
                    IConnectionPool pool = connectionPoolManager.getPool(bean.getPoolName());
                    pool.createConnect();
                } else {
                    System.out.println("Error:Can't find this connecion pool ->" + bean.getPoolName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
