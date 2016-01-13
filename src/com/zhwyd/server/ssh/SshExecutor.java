package com.zhwyd.server.ssh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import com.zhwyd.server.ssh.vo.SshParam;
import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
public class SshExecutor {
    private Connection                 conn;
    private String                     ip;
    private String                     usr;
    private String                     psword;
    private String                     charset      = "UTF-8";
    private static final int           TIME_OUT     = 1000 * 5 * 60;
    private static boolean             isconn       = false;
    private static Map<String, Object> exeResultMap = new HashMap<String, Object>();

    public SshExecutor(SshParam param) {
        this.ip = param.getIp();
        this.usr = param.getUserName();
        this.psword = param.getPassword();
    }

    public SshExecutor(String ip, String usr, String ps) {
        this.ip = ip;
        this.usr = usr;
        this.psword = ps;
    }

    /**
     * 验证登录 author:LKJ 2014-9-6
     * 
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        if (conn != null) {
            if (!ip.equals(conn.getHostname())) {
                isconn = takeConn();
            } else if (!isconn) {
                isconn = takeConn();
            }
        } else {
            isconn = takeConn();
        }
        return isconn;
    }

    /**
     * 远程连接 author:LKJ 2014-9-6
     * 
     * @return
     * @throws IOException
     */
    private boolean takeConn() throws IOException {
        if (conn != null) {
            conn.close();
            conn = null;
        }
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(usr, psword);
    }

    /**
     * 远程操作 author:LKJ 2014-9-6
     * 
     * @param cmds
     *            命令
     * @return
     * @throws Exception
     */
    public Map<String, Object> exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (login()) {
                Session session = conn.openSession();
                session.execCommand(cmds);
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);
                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                // System.out.println("outStr=" + outStr);
                // System.out.println("outErr=" + outErr);
                ret = session.getExitStatus();
                if (exeResultMap != null || !exeResultMap.isEmpty() || exeResultMap.size() > 0) {
                    exeResultMap.clear();
                }
                exeResultMap.put("outStr", outStr);
                exeResultMap.put("outErr", outErr);
                exeResultMap.put("exitStatus", ret);
                session.close();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            // 这里为关闭conn连接，在下次登录时再判断是否重连，可以设置超时时间
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return exeResultMap;
    }

    /**
     * 接收ssh返回结果 author:LKJ 2014-9-13
     * 
     * @param in
     * @param charset
     * @return
     * @throws Exception
     */
    private String processStream(InputStream in, String charset) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            sb.append(URLDecoder.decode(line + "</br>", charset));
        }
        return sb.toString();
    }
}