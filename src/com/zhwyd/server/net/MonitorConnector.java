package com.zhwyd.server.net;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import com.wyd.net.ISession;
import com.wyd.protocol.data.AbstractData;
import com.wyd.protocol.data.DataBeanFilter;
import com.wyd.protocol.handler.IDataHandler;
import com.wyd.protocol.s2s.S2SDecoder;
import com.wyd.protocol.s2s.S2SEncoder;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.util.SessionMap;
import com.zhwyd.server.config.Application;
public abstract class MonitorConnector implements ISession {
    protected static final Logger              log                 = Logger.getLogger(MonitorConnector.class);
    /** 连接器 */
    protected static final NioSocketConnector  CONNECTOR;
    /** 连接器配置参数 */
    protected static final SocketSessionConfig CONNECTOR_CONFIG;
    protected static final int                 RECEIVE_BUFFER_SIZE = 327670;
    protected static final int                 SEND_BUFFER_SIZE    = 327670;
    protected boolean                          valid               = false;
    protected InetSocketAddress                address;
    // 是否需要安全性验证
    protected boolean                          needSecureAuth;
    protected String                           userName            = "";
    protected String                           password            = "";
    protected boolean                          needRetry           = true;
    protected IoSession                        session;
    protected IDataHandler                     loginHandler;
    protected IDataHandler                     customHandler;
    protected IDataHandler                     currentHandler;
    protected IoHandler                        mainHandler         = null;
    protected Object                           lock                = new Object();
    protected String                           id;
    // 一个连接对应一个玩家user
    protected User                             user;
    protected DispatchServer                   dispatchServer;
    protected String                           sessionId;
    static {
        // 此处的I/O Processor的线程数量由CPU的核数决定
        CONNECTOR = new NioSocketConnector(Runtime.getRuntime().availableProcessors() * 2);
        // 获取连接器的配置
        CONNECTOR_CONFIG = CONNECTOR.getSessionConfig();
        // 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
        CONNECTOR_CONFIG.setTcpNoDelay(true);
        CONNECTOR_CONFIG.setSendBufferSize(SEND_BUFFER_SIZE);
        CONNECTOR_CONFIG.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
        // 设置连接超时检查时间
        // connector.setConnectTimeoutCheckInterval(30);
        // 读写通道在10秒内无任何操作进入空闲状态
        // config.setIdleTime(IdleStatus.BOTH_IDLE, 10);
        // Socket关闭后，底层Socket立即关闭
        // this.config.setSoLinger(0);
        // 设置事件处理器
        CONNECTOR.setHandler(new OriginalSessionHandler());
        // 初始化,如加入过滤器等操作
        init();
    }

    /*
     * 初始化拦截器
     */
    private static void init() {
        // 此过滤器的作用是把INetSegment进行再次加密处理
        String key = Application.getConfig("system", "key");
        CONNECTOR.getFilterChain().addLast("uwap2codec", new ProtocolCodecFilter(new S2SEncoder(true, key), new S2SDecoder()));
        // 此过滤器放置最后,DataBean过滤器的作用是把DataBean对象转换为INetSegment,客户端发数据时首先调用此过滤器
        CONNECTOR.getFilterChain().addLast("uwap2databean", new DataBeanFilter());
        CONNECTOR.getFilterChain().addLast("exceutor", new ExecutorFilter());
        // this.connector.getFilterChain().addLast("codec", new WYDFilter());
        // LoggingFilter loggingFilter = new LoggingFilter();
        // loggingFilter.setExceptionCaughtLogLevel(LogLevel.ERROR);
        // loggingFilter.setSessionOpenedLogLevel(LogLevel.ERROR);
        // loggingFilter.setSessionCreatedLogLevel(LogLevel.ERROR);
        // loggingFilter.setSessionIdleLogLevel(LogLevel.ERROR);
        // loggingFilter.setSessionClosedLogLevel(LogLevel.ERROR);
        // loggingFilter.setMessageReceivedLogLevel(LogLevel.ERROR);
        // loggingFilter.setMessageSentLogLevel(LogLevel.ERROR);
        // //日志拦截器
        // TODO sign)日志拦截器
        // CONNECTOR.getFilterChain().addLast("logger", new LoggingFilter());
    }

    public MonitorConnector(String id, InetSocketAddress address, boolean needSecureAuth) {
        this.id = id;
        this.address = address;
        this.needSecureAuth = needSecureAuth;
        this.initConnector();
    }

    public MonitorConnector(String id, InetSocketAddress address, boolean needSecureAuth, User user) {
        this.id = id;
        this.address = address;
        this.needSecureAuth = needSecureAuth;
        this.user = user;
        this.initConnector();
    }

    public MonitorConnector(String id, InetSocketAddress address, boolean needSecureAuth, DispatchServer dispatchServer) {
        this.id = id;
        this.address = address;
        this.needSecureAuth = needSecureAuth;
        this.dispatchServer = dispatchServer;
        this.initConnector();
    }

    public String getId() {
        return this.id;
    }

    /**
     * 初始化连接器各基本参数
     */
    private void initConnector() {
        this.loginHandler = new LoginMessageHandler();
    }

    public void addIoFilterAdapter(String id, IoFilter filter) {
        if (CONNECTOR != null) {
            CONNECTOR.getFilterChain().addLast(id, filter);
        }
    }

    public String connect() {
        String connectResult = "";
        // 与服务器建立连接
        try {
            ConnectFuture future = CONNECTOR.connect(this.address);
            // 等待连接创建完成
            future.awaitUninterruptibly();
            // 获取连接成功后的session
            IoSession session = future.getSession();
            // 保存在成员变量中
            this.session = session;
            // 将当前user保存在session中,使得回调Handler可以使用
            session.setAttribute("currentUser", user);
            // 将自动生成是sessionId绑定到map中
            // SessionMap.getInstance().bindNew(this.id, String.valueOf(session.getId()));
            // 二选一(与上面)
            SessionMap.getInstance().bind(String.valueOf(session.getId()), user);
            connectResult = "true";
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            connectResult = e.getMessage();
        }
        return connectResult;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNeedRetry(boolean needRetry) {
        this.needRetry = needRetry;
    }

    public boolean isNeedRetry() {
        return this.needRetry;
    }

    public boolean isConnected() {
        if (this.session == null) {
            return false;
        }
        return this.session.isConnected();
    }

    public boolean isValid() {
        return this.valid;
    }

    public boolean send(AbstractData data) {
        send0(data);
        return needRetry;
    }

    public void close() {
        if ((this.session != null) && (this.session.isConnected())) {
            this.session.close(true);
        }
    }

    /**
     * 真正的关闭连接
     */
    public void stopConnect() {
        CONNECTOR.dispose();
    }

    public IoSession getSession() {
        return session;
    }

    public SocketAddress getRemoteAddress() {
        return this.session.getRemoteAddress();
    }

    public boolean equals(ISession s) {
        if (this == s) return true;
        return this.id.equals(s.getId());
    }

    protected abstract void sendSecureAuthMessage();

    protected void send0(AbstractData data) {
        session.write(data);
        // System.out.println("data.getTypeString()="+data.toString());
        // JSONObject jsonObject = JSONObject.fromObject(data);
        // System.out.println("jsonObject="+jsonObject);
        // byte[] bytes = CryptionUtil.Encrypt(jsonObject.toString(), "pifnwkjdhn");
        // System.out.println("bytes="+bytes);
        // session.write(bytes);
    }

    protected void connected() {
    }

    protected abstract void processLogin(AbstractData paramAbstractData) throws Exception;

    protected void loginOk() {
        synchronized (this.lock) {
            this.currentHandler = null;
            this.valid = true;
            this.lock.notify();
            connected();
            log.info("ServerLoginOk");
        }
    }

    protected void loginFailed() {
        synchronized (this.lock) {
            this.lock.notify();
        }
    }
    class LoginMessageHandler implements IDataHandler {
        public void handle(AbstractData data) throws Exception {
            MonitorConnector.this.processLogin(data);
        }
    }
}
