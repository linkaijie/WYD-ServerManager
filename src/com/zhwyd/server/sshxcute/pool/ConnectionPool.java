package com.zhwyd.server.sshxcute.pool;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.SSHExec;
import com.zhwyd.server.sshxcute.Sshbean;
public class ConnectionPool implements IConnectionPool {
    // 连接池配置属性
    private Sshbean                     sshbean;
    private boolean                     isActive         = false;                     // 连接池活动状态
    private int                         contActive       = 0;                         // 记录创建的总的连接数
    private int                         waitNum          = 0;                         // 等待数
    // 空闲连接
    private List<SSHExec>               freeConnection   = new Vector<SSHExec>();
    // 活动连接
    private List<SSHExec>               activeConnection = new Vector<SSHExec>();
    // 将线程和连接绑定，保证事务能统一执行
    private static ThreadLocal<SSHExec> threadLocal      = new ThreadLocal<SSHExec>();
    /** 同步锁，防止发放记录列表同时操作出错 */
    private Object                      lock             = new Object();

    public ConnectionPool(Sshbean sshbean) {
        super();
        this.sshbean = sshbean;
        init();
        cheackPool();
    }

    public ConnectionPool(Sshbean sshbean, String sign) {
        super();
        this.sshbean = sshbean;
        initNew();
        cheackPool();
    }

    /**
     * 初始化连接
     */
    public void init() {
        try {
            for (int i = 0; i < sshbean.getInitConnections(); i++) {
                SSHExec sshExec;
                sshExec = newConnection();
                // 初始化最小连接数
                if (sshExec != null) {
                    freeConnection.add(sshExec);
                    contActive++;
                } else {
                    System.out.println("contActive=" + contActive + ",getPoolName=" + sshbean.getPoolName() + "，error");
                }
                System.out.println("contActive=" + contActive + ",getPoolName=" + sshbean.getPoolName());
                Thread.sleep(10l);
            }
            isActive = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化（新增服务器时调用）
     */
    public void initNew() {
        try {
            for (int i = 0; i < sshbean.getMaxConnections(); i++) {
                SSHExec sshExec;
                sshExec = newConnection();
                // 初始化最小连接数
                if (sshExec != null) {
                    freeConnection.add(sshExec);
                    contActive++;
                } else {
                    System.out.println("contActive=" + contActive + ",getPoolName=" + sshbean.getPoolName() + "，error");
                }
                System.out.println("contActive=" + contActive + ",getPoolName=" + sshbean.getPoolName());
                Thread.sleep(100l);
            }
            isActive = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建剩余连接
     */
    public void createConnect() {
        try {
            int initNum = contActive;
            for (int i = initNum; i < sshbean.getMaxConnections(); i++) {
                SSHExec sshExec;
                sshExec = newConnection();
                if (sshExec != null) {
                    freeConnection.add(sshExec);
                    contActive++;
                } else {
                    System.out.println("contActive=" + contActive + ",getPoolName=" + sshbean.getPoolName() + "，error");
                }
                System.out.println("contActive=" + contActive + ",getPoolName=" + sshbean.getPoolName());
                Thread.sleep(50l);
            }
            isActive = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得当前连接
     */
    public SSHExec getCurrentConnecton() {
        // 默认线程里面取
        SSHExec sshExec = threadLocal.get();
        if (!isValid(sshExec)) {
            sshExec = getConnection();
        }
        return sshExec;
    }

    /**
     * 获得连接
     */
    public SSHExec getConnection() {
        SSHExec sshExec = null;
        synchronized (lock) {
            try {
                // 判断是否超过最大连接数限制
                if (freeConnection.size() > 0) {
                    sshExec = freeConnection.get(0);
                    if (sshExec != null) {
                        threadLocal.set(sshExec);
                    }
                    freeConnection.remove(0);
                } else {
                    waitNum++;
                    lock.wait(2000l);
                    sshExec = getConnection();
                }
                if (isValid(sshExec)) {
                    activeConnection.add(sshExec);
                    // contActive++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sshExec;
    }

    /**
     * 新建连接
     * @return
     * @throws Exception
     */
    private synchronized SSHExec newConnection() throws Exception {
        SSHExec sshExec = null;
        if (sshbean != null) {
            ConnBean cb = new ConnBean(sshbean.getSshIp(), sshbean.getUserName(), sshbean.getPassword());
            sshExec = SSHExec.getInstance(cb);
            SSHExec.setOption(IOptionName.SSH_PORT_NUMBER, sshbean.getSshPort());
            SSHExec.setOption(IOptionName.HALT_ON_FAILURE, true);
            SSHExec.setOption(IOptionName.INTEVAL_TIME_BETWEEN_TASKS, 100l);
            if (!sshExec.connect()) {
                return null;
            }
        }
        return sshExec;
    }

    /**
     * 释放连接
     */
    public void releaseConn(SSHExec sshExec) throws Exception {
        synchronized (lock) {
            if (isValid(sshExec) && !(freeConnection.size() > sshbean.getMaxConnections())) {
                freeConnection.add(sshExec);
                activeConnection.remove(sshExec);
                // contActive--;
                threadLocal.remove();
                // 唤醒所有正待等待的线程，去抢连接
                if (waitNum > 0) {
                    // notifyAll();//不使用
                    waitNum = 0;
                }
            }
        }
    }

    /**
     * 判断连接是否可用
     * @param sshExec
     * @return
     */
    private boolean isValid(SSHExec sshExec) {
        try {
            if (sshExec == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 销毁连接池
     */
    public synchronized void destroy() {
        for (SSHExec sshExec : freeConnection) {
            try {
                if (isValid(sshExec)) {
                    sshExec.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (SSHExec sshExec : activeConnection) {
            try {
                if (isValid(sshExec)) {
                    sshExec.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isActive = false;
        contActive = 0;
    }

    /**
     * 连接池状态
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

    /**
     * 定时检查连接池情况
     */
    @Override
    public void cheackPool() {
        if (sshbean.isCheakPool()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // 1.对线程里面的连接状态
                    // 2.连接池最小 最大连接数
                    // 3.其他状态进行检查，因为这里还需要写几个线程管理的类，暂时就不添加了
                    System.out.println("空线池连接数：" + freeConnection.size());
                    System.out.println("活动连接数：：" + activeConnection.size());
                    System.out.println("总的连接数：" + contActive);
                }
            }, sshbean.getLazyCheck(), sshbean.getPeriodCheck());
        }
    }
}
