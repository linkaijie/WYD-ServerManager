package com.zhwyd.server.ssh.util;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.jcraft.jsch.SftpException;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.constant.SshConstant;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.sftp.Sftp;
import com.zhwyd.server.ssh.SshExecutor;
public class SshExecutorUtils extends HttpServlet {
    private static final long  serialVersionUID = 1L;
    static SshExecutor         sshExecutor;
    static Map<String, Object> resultMap        = new HashMap<String, Object>();

    /**
     * 部署服务
     * 
     * @param server
     * @param modelPath
     * @param targetPath
     * @param deploySb
     * @param session
     * @return
     */
    public static String deployServer(Server server, String modelPath, String targetPath, StringBuffer deploySb, HttpSession session) {
        String deployResult = "false";
        try {
            deploySb.append("部署目录检查。。。</br>");
            String isExist = (String) dirIsExist(server, targetPath).get("outStr");
            if (isExist.replace("</br>", "").equals("DIR IS EXIST")) {
                return Global.DIR_IS_EXIST;
            }
            String createDir = (String) createDir(server, targetPath).get("outStr");
            System.out.println("createDir=" + createDir);
            String isExist2 = (String) dirIsExist(server, targetPath).get("outStr");
            if (isExist2.replace("</br>", "").equals("DIR IS NOT EXIST")) {
                return Global.DIR_IS_NOT_EXIST;
            }
            deploySb.append("部署目录检查通过</br>");
            session.setAttribute("deploySb", deploySb);
            deployResult = uploadFile(server, modelPath, targetPath, deploySb, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deployResult;
    }

    /**
     * 更新服务
     * 
     * @param server
     * @param modelPath
     * @param targetPath
     * @return
     */
    public static String updateServer(Server server, String modelPath, String targetPath) {
        try {
            String isExist = (String) dirIsExist(server, targetPath).get("outStr");
            if (isExist.replace("</br>", "").equals("DIR IS NOT EXIST")) {
                return Global.DIR_IS_NOT_EXIST;
            }
            uploadFile(server, modelPath, targetPath, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 判断文件夹目录是否存在
     * 
     * @author:LKJ 2014-9-25
     * @param targetPath
     * @return
     * @throws Exception
     */
    public static Map<String, Object> dirIsExist(Server server, String targetPath) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        resultMap = sshExecutor.exec(new String(SshConstant.SHOW_DIR_IS_EXIST.replace("{0}", targetPath).getBytes("UTF-8"), "UTF-8"));
        return resultMap;
    }

    /**
     * 创建目标文件夹
     * 
     * @author:LKJ
     * @param modelPath
     */
    public static Map<String, Object> createDir(Server server, String targetPath) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        resultMap = sshExecutor.exec(SshConstant.CREATE_DIR.replace("{0}", targetPath));
        return resultMap;
    }

    /**
     * 上传文件夹下的所有内容
     * 
     * @author:LKJ 2014-10-8
     * @param server
     * @param modelPath
     * @param targetPath
     * @param deploySb
     * @param session
     * @return
     * @throws SftpException
     */
    public static String uploadFile(Server server, String modelPath, String targetPath, StringBuffer deploySb, HttpSession session) {
        Map<Integer, Object> sftpMap = new HashMap<Integer, Object>();
        try {
            sftpMap.put(Sftp.HOST, server.getServerIp());
            sftpMap.put(Sftp.PORT, server.getServerPort());
            sftpMap.put(Sftp.USERNAME, server.getSshName());
            sftpMap.put(Sftp.PASSWORD, server.getSshPwd());
            sftpMap.put(Sftp.REMOTE, targetPath);
            sftpMap.put(Sftp.LOCAL_DIR, modelPath);
            sftpMap.put(Sftp.FILE_FILTER_REGEX, null);
            sftpMap.put(Sftp.LS_MODIFIED_PATH, true);
            sftpMap.put(Sftp.UP_OR_DOWN, CommanConstant.UPLOAD);
            Sftp.upAndDownload(sftpMap, deploySb, session);
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    /**
     * 修改配置文件通配信息
     * 
     * @author:LKJ 2014-10-8
     * @param wildcards
     * @return
     * @throws Exception
     */
    public static Map<String, Object> updateWildcard(Server server, String path, String wildcards, String fileName) {
        try {
            sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
            resultMap = sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.UPDATE_CONTENT.replace("{0}", wildcards).replace("{1}", fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 获取进程号（PID）
     * 
     * @author:LKJ 2014-9-18
     * @param Pid
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getPid(Server server, String path, String name) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        resultMap = sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.GET_CUR_PID.replace("{0}", name)));
        return resultMap;
    }

    /**
     * 查看进程是否存在
     * 
     * @param Pid
     * @return
     * @throws Exception
     */
    public static Map<String, Object> progressIsExist(Server server, String Pid) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        resultMap = sshExecutor.exec(SshConstant.SHOW_PROGRESS_IS_EXIST.replace("{0}", Pid));
        return resultMap;
    }

    /**
     * 启动服务
     * 
     * @throws Exception
     */
    public static Map<String, Object> runExec(Server server, String path) {
        try {
            sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
            StringBuilder command = new StringBuilder();
            command.append(SshConstant.CD_SERVER_PATH.replace("{0}", path));
            command.append(SshConstant.CLEAR_STDOUT);
            if (server.getServerIp().equals("180.150.188.84")) {
                command.append("export JAVA_HOME='/usr/local/java/default';");
            }else {
                command.append("export JAVA_HOME='/usr/local/jdk';");// 这里为JAVA_HOME路径 赋值，如果服务器JAVA_HOME路径改变则要做相应的修改（远程无法获取环境变量，原因不明）
            }
            command.append(SshConstant.RUN_SHELL);
            resultMap = sshExecutor.exec(command.toString());
            System.out.println("outStr=" + resultMap.get("outStr"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * kill服务
     * 
     * @throws Exception
     */
    public static Map<String, Object> killExec(Server server, String path) throws Exception {
        try {
            sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
            resultMap = sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.KILL_SHELL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * kill当前服务运行的进程
     * 
     * @throws Exception
     */
    public static Map<String, Object> killCurProgress(Server server) throws Exception {
        // kill当前进程，将‘x’改成当前服务的名称，如account服务，则为：
        // command = "ps aux | grep account | grep -v 'grep' | awk '{print $2}' | xargs kill -9";
        // 查询进程是否存在，存在返回1，否则返回2
        // test -n `ps aux | grep 3409 | awk '$2==3409{print $2}'` && echo 1 || echo 2
        String command = "ps aux | grep " + "{0}" + " | grep -v 'grep' | awk '{print $2}' | xargs kill -9";// {0}代表服务名称
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        resultMap = sshExecutor.exec(command);
        return resultMap;
    }

    /**
     * 输出操作日志
     * 
     * @param sshExecutor
     *            ssh对象
     * @return
     * @throws Exception
     */
    public static Map<String, Object> showStdout(Server server, String path) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        return sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.SHOW_STDOUT));
    }

    /**
     * 输出操作日志(最后n行)
     * 
     * @param sshExecutor
     *            ssh对象
     * @return
     * @throws Exception
     */
    public static Map<String, Object> showStdoutLast(Server server, String path) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        return sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.SHOW_STDOUT_LAST));
    }

    /**
     * 输出操作日志
     * 
     * @param sshExecutor
     *            ssh对象
     * @return
     * @throws Exception
     */
    public static Map<String, Object> clearStdout(Server server, String path) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        return sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.CLEAR_STDOUT));
    }

    /**
     * 运行输入日志（手动输入）
     * 
     * @param command
     *            命令
     * @throws Exception
     */
    public static Map<String, Object> inputCommandExec(Server server, String path, String command) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        resultMap = sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(command));
        return resultMap;
    }

    /**
     * 查看进程是否存在
     * 
     * @throws Exception
     */
    public static boolean progressIsExistNew(Server server, String path, String name) throws Exception {
        boolean isExist = false;
        try {
            sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
            resultMap = sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.SHOW_PROGRESS_IS_EXIST_NEW.replace("{0}", name)));
            if (resultMap != null && resultMap.size() > 0) {
                String result = (String) resultMap.get("outStr");
                String finalResult = result.replace("</br>", "");
                if (finalResult.equals("1")) {
                    isExist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    /**
     * 开启服务
     * 
     * @author:LKJ 2015-3-7
     * @param beanInterface
     *            各个服务的service
     * @param fileName
     *            PID文件名称
     * @return
     * @throws Exception
     *             启动出错返回的异常
     */
    public static String startEachServer(BeanInterface beanInterface, String fileName) throws Exception {
        String startResult = "";
        Server server = CacheService.getServerById(beanInterface.getServerId());
        // 判断是否已经部署
        if (beanInterface.getIsDeploy() == CommanConstant.STATE_STOP) {
            return startResult = "ID为：" + beanInterface.getId() + "的" + fileName + "未部署";
        }
        if (server == null) {
            return startResult = "ID为：" + beanInterface.getServerId() + "Server不存在";
        }
        // 检测该服务是否在运行中
        boolean result = progressIsExistNew(server, beanInterface.getPath(), fileName);
        if (!result) {
            Map<String, Object> resultMap = runExec(server, beanInterface.getPath());
            // 启动出错时关闭该服务
            if (resultMap != null && resultMap.get("outErr") != null && !("").equals(resultMap.get("outErr"))) {
                killExec(server, beanInterface.getPath());
                startResult = resultMap.get("outErr").toString().replace("</br>", "");
            } else {
                startResult = "true";
            }
        } else {
            startResult = fileName + "所在服务已启动，请关闭重新启动！";
        }
        return startResult;
    }

    /**
     * 关闭服务
     * 
     * @author LKJ
     * @date 2015-3-7
     * @param beanInterface
     * @param serviceInterface
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String stopEachServer(BeanInterface beanInterface, ServiceInterface serviceInterface, String fileName) throws Exception {
        String startResult = "";
        Server server = CacheService.getServerById(beanInterface.getServerId());
        if (server == null) {
            return startResult = "ID为：" + beanInterface.getServerId() + "Server不存在 </br>";
        }
        // 关闭Dispatch前先清除world日志
        // if (beanInterface instanceof DispatchServer) {
        // SshExecutorUtils.clearStdout(server, CacheService.getWorldServerById(((DispatchServer) beanInterface).getWorldId()).getPath());
        // }
        Map<String, Object> resultMap = killExec(server, beanInterface.getPath());
        if (resultMap != null && resultMap.get("outErr") != null && !("").equals(resultMap.get("outErr"))) {
            startResult = resultMap.get("outErr").toString().replace("</br>", "");
        } else {
            // 检测
            boolean result = progressIsExistNew(server, beanInterface.getPath(), fileName);
            if (!result) {
                serviceInterface.updateState(beanInterface.getId(), CommanConstant.STATE_STOP, CommanConstant.RUN_STATE);
                startResult = "ID为：" + beanInterface.getId() + "的" + fileName + "服务关闭成功！</br>";
            } else {
                startResult = "ID为：" + beanInterface.getId() + "的" + fileName + "服务关闭失败！</br>";
            }
        }
        return startResult;
    }

    /**
     * 检测开启结果
     */
    public static void checkStartServer(BeanInterface beanInterface, ServiceInterface serviceInterface, HttpSession session, String successStr) throws Exception {
        Server server = CacheService.getServerById(beanInterface.getServerId());
        if (server == null) {
            session.setAttribute("stdout", "ID为：" + beanInterface.getServerId() + "的Server不存在");
            return;
        }
        if (session.getAttribute("stdout") != null && !session.getAttribute("stdout").equals("")) {
            session.setAttribute("stdout", "");
        }
        String stdout = "";
        for (int i = 0; i < 120; i++) {
            stdout = getEachStdout(session, beanInterface, serviceInterface, successStr);
            if (!StringUtils.isEmpty(stdout) && (stdout.equals("true") || stdout.equals("false"))) {
                if (stdout.equals("false")) {
                    SshExecutorUtils.killExec(server, beanInterface.getPath());
                }
                break;
            }
            Thread.sleep(2500);
            if (i >= 119) {
                SshExecutorUtils.killExec(server, beanInterface.getPath());
                session.setAttribute("stdout", "服务启动超时");
            }
        }
    }

    /**
     * 获取服务日志
     */
    public static String getEachStdout(HttpSession session, BeanInterface beanInterface, ServiceInterface serviceInterface, String successStr) throws Exception {
        String result = "";
        boolean successResult = false;
        boolean exceptionResult = false;
        Server server = CacheService.getServerById(beanInterface.getServerId());
        Map<String, Object> resultMap = showStdout(server, beanInterface.getPath());
        String exceptionStr = "Exception";
        if (resultMap != null && resultMap.size() > 0 && resultMap.get("outStr") != null) {
            successResult = CommonUtil.Pattern(successStr, resultMap.get("outStr").toString());
            exceptionResult = CommonUtil.Pattern(exceptionStr, resultMap.get("outStr").toString());
            if (exceptionResult) {
                result = "false";
                String logContent = beanInterface.getName() + ":" + beanInterface.getId() + "," + resultMap.get("outStr").toString().replace("</br>", "");
                serviceInterface.writeBatchLog(session, logContent);
            } else if (successResult) {
                result = "true";
                serviceInterface.updateState(beanInterface.getId(), CommanConstant.STATE_START, CommanConstant.RUN_STATE);// 更新服务运行状态
            }
            session.setAttribute("stdout", resultMap.get("outStr"));
        }
        return result;
    }

    /**
     * 获取服务日志
     */
    public static void showStdout(BeanInterface beanInterface, ServiceInterface serviceInterface, HttpSession session, String successStr) throws Exception {
        if (session.getAttribute("stdout") != null && !session.getAttribute("stdout").equals("")) {
            session.setAttribute("stdout", "");
        }
        Server server = CacheService.getServerById(beanInterface.getServerId());
        if (server == null) {
            session.setAttribute("stdout", "ID为：" + beanInterface.getServerId() + "的Server不存在");
            return;
        }
        Map<String, Object> resultMap = showStdout(server, beanInterface.getPath());
        session.setAttribute("stdout", resultMap.get("outStr"));
    }

    /**
     * 部署服务(即將停用)
     */
    public static void deployEachServer(HttpServletResponse response, BeanInterface beanInterface, ServiceInterface serviceInterface, String propertyName, HttpSession session) throws Exception {
        if (session.getAttribute("deploySb") != null && !session.getAttribute("deploySb").equals("")) {
            session.setAttribute("deploySb", "");
        }
        Server server = CacheService.getServerById(beanInterface.getServerId());
        if (server == null) {
            response.getWriter().write("ID为：" + beanInterface.getServerId() + "的Server不存在");
            return;
        }
        StringBuffer deploySb = new StringBuffer();
        deploySb.append("准备部署。。。</br>");
        session.setAttribute("deploySb", deploySb);
        String result = "";
        result = SshExecutorUtils.deployServer(server, beanInterface.getModelPath(), beanInterface.getPath(), deploySb, session);
        if (result.equals("success")) {
            // 修改配置的通配字段
            deploySb.append("获取通配字段</br>");
            String wildCard = serviceInterface.getWildcard(beanInterface.getId());
            deploySb.append("获取通配字段成功</br>");
            SshExecutorUtils.updateWildcard(server, beanInterface.getPath(), wildCard, propertyName);
            deploySb.append("修改配置文件通配信息</br>");
            System.out.println("上传成功~~！");
            serviceInterface.updateState(beanInterface.getId(), CommanConstant.STATE_START, CommanConstant.DEPLOY_STATE);// //1代表更新部署状态
            deploySb.append("<font color='greed'>恭喜您，部署成功！</font>");
            session.setAttribute("deploySb", deploySb);
            response.getWriter().write("success");
        } else {
            response.getWriter().write(result);
        }
    }

    /**
     * 更新服务
     * 
     * @author:LKJ 2014-11-27
     * @return
     */
    public static String updateEachServer(BeanInterface beanInterface, HttpSession session) {
        String updateResult = "";
        StringBuffer updateBuffer = new StringBuffer();
        try {
            Server server = CacheService.getServerById(beanInterface.getServerId());
            if (server == null) {
                return "ID为：" + beanInterface.getServerId() + "的Server不存在";
            }
            String isExist = (String) dirIsExist(server, beanInterface.getPath()).get("outStr");
            if (isExist.replace("</br>", "").equals("DIR IS NOT EXIST")) {
                return Global.DIR_IS_NOT_EXIST;
            }
            updateResult = uploadFile(server, beanInterface.getUpdatePath(), beanInterface.getPath(), updateBuffer, session);
        } catch (Exception e) {
            updateResult = e.getMessage();
            e.printStackTrace();
        }
        return updateResult;
    }

    /**
     * 批量获取服务日志
     */
    public static String getBatchStdout(HttpSession session, BeanInterface beanInterface, ServiceInterface serviceInterface, StringBuilder stringBuilder, String successStr) throws Exception {
        String result = "";
        boolean successResult = false;
        boolean exceptionResult = false;
        Server server = CacheService.getServerById(beanInterface.getServerId());
        Map<String, Object> resultMap = showStdout(server, beanInterface.getPath());
        String exceptionStr = "Exception";
        if (resultMap != null && resultMap.size() > 0 && resultMap.get("outStr") != null) {
            successResult = CommonUtil.Pattern(successStr, resultMap.get("outStr").toString());
            exceptionResult = CommonUtil.Pattern(exceptionStr, resultMap.get("outStr").toString());
        }
        if (exceptionResult) {
            result = "false";
            String logContent = beanInterface.getName() + ":" + beanInterface.getId() + "," + resultMap.get("outStr").toString().replace("</br>", "");
            stringBuilder.append(logContent);
            serviceInterface.writeBatchLog(session, logContent);
        } else if (successResult) {
            result = "true";
            serviceInterface.updateState(beanInterface.getId(), CommanConstant.STATE_START, CommanConstant.RUN_STATE);// 更新服务运行状态
        }
        return result;
    }
    // /**
    // * 查看所有运行的程序 author:LKJ 2014-9-5
    // *
    // * @throws Exception
    // */
    // public static Map<String, Object> showAllProc() throws Exception {
    // if (sshParam != null) {
    // sshExecutor = new SshExecutor(sshParam);
    // }
    // resultMap = sshExecutor.exec(SshConstant.SHOW_ALL_PROCEDURE);
    // return resultMap;
    // }
    // /**
    // * 创建目标文件夹 author:LKJ 2014-9-25
    // *
    // * @param modelPath
    // * @return
    // * @throws Exception
    // */
    // public static Map<String, Object> createDir(String targetPath) throws Exception {
    // if (sshParam != null) {
    // sshExecutor = new SshExecutor(sshParam);
    // }
    // resultMap = sshExecutor.exec(SshConstant.CREATE_DIR.replace("{0}", targetPath));
    // return resultMap;
    // }
    //
    // /**
    // * 复制模板到指定目录 author:LKJ 2014-9-25
    // *
    // * @param targetPath
    // * @param modelPath
    // * @return
    // * @throws Exception
    // */
    // public static Map<String, Object> copyIntoDir(String modelPath, String targetPath) throws Exception {
    // if (sshParam != null) {
    // sshExecutor = new SshExecutor(sshParam);
    // }
    // resultMap = sshExecutor.exec(SshConstant.CP_FILE_INTO_DIR.replace("{0}", modelPath).replace("{1}", targetPath));
    // return resultMap;
    // }
}
