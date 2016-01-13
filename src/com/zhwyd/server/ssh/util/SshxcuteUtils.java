package com.zhwyd.server.ssh.util;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
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
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.sftp.Sftp;
import com.zhwyd.server.ssh.SshExecutor;
import com.zhwyd.server.sshxcute.SSHExces;
import com.zhwyd.server.sshxcute.UploadDataToServer;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
public class SshxcuteUtils extends HttpServlet {
    private static final long  serialVersionUID = 1L;
    static SshExecutor         sshExecutor;
    static Map<String, Object> resultMap        = new HashMap<String, Object>();

    /**
     * 启动服务
     * 
     * @throws Exception
     */
    public static Object[] runExec(Server server, String path) {
        Object[] execResult = null;
        try {
            StringBuilder command = new StringBuilder();
            command.append(SshConstant.CD_SERVER_PATH.replace("{0}", path));
            command.append(SshConstant.CLEAR_STDOUT);
            if (server.getServerIp().equals("180.150.188.84")) {
                command.append("export JAVA_HOME='/usr/local/java/default';");
            } else if (server.getServerIp().equals("221.4.215.11")) {
                command.append("export JAVA_HOME='/usr/bin/java/jdk1.7.0_67';");
            } else {
                command.append("export JAVA_HOME='/usr/local/jdk';");// 这里为JAVA_HOME路径 赋值，如果服务器JAVA_HOME路径改变则要做相应的修改（远程无法获取环境变量，原因不明）
            }
            command.append(SshConstant.RUN_SHELL);
            execResult = SSHExces.exce(server, new ExecCommand(command.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execResult;
    }

    /**
     * kill服务
     */
    public static Object[] killExec(Server server, String path) throws Exception {
        Object[] execResult = null;
        try {
            execResult = SSHExces.exce(server, new ExecCommand(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.KILL_SHELL)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execResult;
    }

    /**
     * 开启服务
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
            Object[] runResult = runExec(server, beanInterface.getPath());
            // 启动出错时关闭该服务
            if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != 0) {
                killExec(server, beanInterface.getPath());
                startResult = (String) runResult[1];
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
     */
    public static String stopEachServer(BeanInterface beanInterface, ServiceInterface serviceInterface, String fileName) throws Exception {
        String startResult = "";
        Server server = CacheService.getServerById(beanInterface.getServerId());
        if (server == null) {
            return startResult = "ID为：" + beanInterface.getServerId() + "Server不存在 </br>";
        }
        Object[] killResult = killExec(server, beanInterface.getPath());
        if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            startResult = (String) killResult[1];
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
     * 部署服务
     * 
     * @param server
     * @param modelPath
     * @param targetPath
     * @param deploySb
     * @param session
     * @return
     */
    @SuppressWarnings("unused")
    public static String deployServer(Server server, String modelPath, String targetPath, StringBuffer deploySb, HttpSession session) {
        String deployResult = "false";
        try {
            deploySb.append("部署目录检查。。。</br>");
            Object[] isExist = dirIsExist(server, targetPath);
            if (isExist.length <= 0) {
                return "false";
            }
            if (isExist[1].equals("DIR IS EXIST")) {
                return Global.DIR_IS_EXIST;
            }
            String createDir = (String) createDir(server, targetPath).get("outStr");
            // System.out.println("createDir=" + createDir);
            isExist = dirIsExist(server, targetPath);
            if (isExist.length <= 0) {
                return "false";
            }
            if (isExist[1].equals("DIR IS NOT EXIST")) {
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
            Object[] isExist = dirIsExist(server, targetPath);
            if (isExist.length <= 0) {
                return "false";
            }
            if (isExist[1].equals("DIR IS NOT EXIST")) {
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
    public static Object[] dirIsExist(Server server, String targetPath) throws Exception {
        CustomTask customTasks = new ExecCommand(new String(SshConstant.SHOW_DIR_IS_EXIST.replace("{0}", targetPath).getBytes("UTF-8"), "UTF-8"));
        Object[] execResult = SSHExces.exce(server, customTasks);
        return execResult;
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
     * 修改配置文件通配信息（停用）
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
     * 输出操作日志
     */
    public static Object[] showStdout(Server server, String path) throws Exception {
        Object[] execResult = null;
        try {
            execResult = SSHExces.exce(server, new ExecCommand(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.SHOW_STDOUT_LAST)), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execResult;
    }

    /**
     * 输出操作日志(最后n行)
     */
    public static Object[] showStdoutLast(Server server, String path) throws Exception {
        Object[] execResult = null;
        try {
            execResult = SSHExces.exce(server, new ExecCommand(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.SHOW_STDOUT_LAST)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execResult;
    }

    /**
     * 输出操作日志
     */
    public static Map<String, Object> clearStdout(Server server, String path) throws Exception {
        sshExecutor = new SshExecutor(server.getServerIp(), server.getSshName(), server.getSshPwd());
        return sshExecutor.exec(SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.CLEAR_STDOUT));
    }

    /**
     * 查看进程是否存在
     * 
     * @return true:运行中；false未运行
     */
    public static boolean progressIsExistNew(Server server, String path, String name) throws Exception {
        boolean isExist = false;
        try {
            String command = SshConstant.CD_SERVER_PATH.replace("{0}", path).concat(SshConstant.SHOW_PROGRESS_IS_EXIST_NEW.replace("{0}", name));
            Object[] execResult = SSHExces.exce(server, new ExecCommand(command));
            if (execResult != null && execResult.length > 0) {
                String result = ((String) execResult[1]).replace("\r", "").replace("\n", "");
                if (result.equals("1")) {
                    isExist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    /**
     * 查看进程是否存在
     */
    public static String getProgressOpenNum(Server server, String name) throws Exception {
        String number = "";
        try {
            String command = SshConstant.SHOW_PROGRESS_IS_EXIST_NEW.replace("{0}", name);
            Object[] execResult = SSHExces.exce(server, new ExecCommand(command));
            if (execResult != null && execResult.length > 0 && (Integer) execResult[0] == CommanConstant.RESULT_TRUE_STATE) {
                number = ((String) execResult[1]).replace("\r", "").replace("\n", "");
            } else {
                number = "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
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
                    SshxcuteUtils.killExec(server, beanInterface.getPath());
                }
                break;
            }
            Thread.sleep(3000);
            if (i == 119) {
                SshxcuteUtils.killExec(server, beanInterface.getPath());
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
        Object[] stdResult = showStdout(server, beanInterface.getPath());
        String exceptionStr = "Exception";
        if (stdResult != null && stdResult.length > 0 && stdResult[1] != null) {
            successResult = CommonUtil.Pattern(successStr, stdResult[1].toString());
            exceptionResult = CommonUtil.Pattern(exceptionStr, stdResult[1].toString());
            if (exceptionResult) {
                result = "false";
                String logContent = beanInterface.getName() + ":" + beanInterface.getId() + "," + stdResult[1].toString();
                serviceInterface.writeBatchLog(session, logContent);
            } else if (successResult) {
                result = "true";
                serviceInterface.updateState(beanInterface.getId(), CommanConstant.STATE_START, CommanConstant.RUN_STATE);// 更新服务运行状态
            }
            session.setAttribute("stdout", stdResult[1]);
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
        Object[] stdResult = showStdout(server, beanInterface.getPath());
        session.setAttribute("stdout", stdResult[1]);
    }

    /**
     * 部署服务（没被使用）
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
        result = deployServer(server, beanInterface.getModelPath(), beanInterface.getPath(), deploySb, session);
        if (result.equals("success")) {
            // 修改配置的通配字段
            deploySb.append("获取通配字段</br>");
            String wildCard = serviceInterface.getWildcard(beanInterface.getId());
            deploySb.append("获取通配字段成功</br>");
            SshxcuteUtils.updateWildcard(server, beanInterface.getPath(), wildCard, propertyName);
            deploySb.append("修改配置文件通配信息</br>");
            // System.out.println("上传成功~~！");
            serviceInterface.updateState(beanInterface.getId(), CommanConstant.STATE_START, CommanConstant.DEPLOY_STATE);// //1代表更新部署状态
            deploySb.append("<font color='greed'>恭喜您，部署成功！</font>");
            session.setAttribute("deploySb", deploySb);
            response.getWriter().write("success");
        } else {
            response.getWriter().write(result);
        }
    }

    /**
     * 更新服务（没被使用）
     */
    public static String updateEachServer(BeanInterface beanInterface, HttpSession session) {
        String updateResult = "";
        StringBuffer updateBuffer = new StringBuffer();
        try {
            Server server = CacheService.getServerById(beanInterface.getServerId());
            if (server == null) {
                return "ID为：" + beanInterface.getServerId() + "的Server不存在";
            }
            Object[] isExist = dirIsExist(server, beanInterface.getPath());
            if (isExist.length <= 0) {
                return "false";
            }
            if (isExist[1].equals("DIR IS NOT EXIST")) {
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
    public static Boolean getBatchStdout(HttpSession session, BeanInterface beanInterface, ServiceInterface serviceInterface, String successStr) throws Exception {
        boolean successResult = false;
        boolean exceptionResult = false;
        Server server = CacheService.getServerById(beanInterface.getServerId());
        Object[] stdResult = showStdout(server, beanInterface.getPath());
        String exceptionStr = "Exception";
        if (stdResult != null && stdResult.length > 0 && stdResult[1] != null) {
            // 测试是否启动是否有异常
            exceptionResult = CommonUtil.Pattern(exceptionStr, stdResult[1].toString());
            // 发生异常时打印结果
            if (exceptionResult) {
                System.out.println("execResult[0]=" + stdResult[0]);
                System.out.println("execResult[1]=" + stdResult[1]);
            }
            // 没有异常的情况下才检测是否启动成功
            successResult = !exceptionResult && CommonUtil.Pattern(successStr, stdResult[1].toString());
            // 如果成功时打印结果
            if (successResult) {
                System.out.println("execResult[0]=" + stdResult[0]);
                System.out.println("execResult[1]=" + stdResult[1]);
            }
        }
        // 如果启动发生异常
        Boolean bResult = null;
        if (exceptionResult) {
            String logContent = beanInterface.getName() + ":" + beanInterface.getId() + "," + stdResult[1].toString();
            serviceInterface.writeBatchLog(session, logContent);
            bResult = false;
        } else if (successResult) {
            // 更新服务运行状态
            serviceInterface.updateState(beanInterface.getId(), CommanConstant.STATE_START, CommanConstant.RUN_STATE);
            bResult = true;
        }
        return bResult;
    }

    /**
     * 批量获取服务日志(不更新bean状态，不更新日志)
     */
    public static String getBatchStdout(BeanInterface beanInterface, String successStr) throws Exception {
        String result = "";
        boolean successResult = false;
        boolean exceptionResult = false;
        Server server = CacheService.getServerById(beanInterface.getServerId());
        Object[] stdResult = showStdout(server, beanInterface.getPath());
        String exceptionStr = "Exception";
        if (stdResult != null && stdResult.length > 0 && stdResult[1] != null) {
            successResult = CommonUtil.Pattern(successStr, stdResult[1].toString());
            exceptionResult = CommonUtil.Pattern(exceptionStr, stdResult[1].toString());
        }
        if (exceptionResult) {
            result = "false";
        } else if (successResult) {
            result = "true";
        }
        return result;
    }

    /**
     * 获取远程配置文件信息，并解析到Map中
     */
    public static Map<String, String> getConfigMap(BeanInterface beanInterface, String fileName) {
        Object[] execResult = null;
        Map<String, String> configMap = new HashMap<String, String>();
        try {
            Server server = CacheService.getServerById(beanInterface.getServerId());
            if (server == null) {
                throw new Exception("Server不存在");
            }
            String command = SshConstant.GET_FILE_CONTENT.replace("{0}", beanInterface.getPath()).replace("{1}", fileName);
            execResult = SSHExces.exce(server, new ExecCommand(command));
            if (execResult != null && execResult.length > 0 && (Integer) execResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                throw new Exception(execResult[1].toString());
            } else {
                if (execResult[1] != null && execResult[1].toString().length() > 0) {
                    String[] configStr = execResult[1].toString().split(System.getProperty("line.separator"));
                    for (int i = 0; i < configStr.length; i++) {
                        if (configStr[i].contains("=")) {
                            configMap.put(configStr[i].split("=")[0].trim(), configStr[i].split("=")[1].trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configMap;
    }

    /**
     * 复制公钥
     * 
     * @author:LKJ 2014-9-25
     * @return
     * @throws Exception
     */
    public static Object[] copySshRsa(HttpServletRequest request, Server server) throws Exception {
        String shPath = request.getSession().getServletContext().getRealPath("sh");
        System.out.println("shPath=" + shPath);
        // CustomTask customTasks = new ExecShellScript(shPath, "./sshcopy.sh;", server.getServerIp() + " " + server.getSshPwd());
        CustomTask customTasks = new ExecCommand(SshConstant.SSH_COPT.replace("{0}", shPath).replace("{1}", server.getServerIp()).replace("{2}", server.getSshPwd()).replace("{3}", server.getServerPort().toString()));
        // CustomTask customTasks = new ExecCommand("cat " + "/usr/local/tomcat_gm/webapps/WYD-ServerManager/sh" + "/sshcopy.sh");
        // 本机Server
        String localServerId = Application.getConfig("system", "localServerId");
        Server serverLocal = CacheService.getServerById(Integer.valueOf(localServerId));
        Object[] execResult = SSHExces.exce(serverLocal, customTasks);
        return execResult;
    }

    /**
     * 远程传输文件
     * 
     * @author:LKJ 2014-9-25
     * @param shPath
     * @return
     * @throws Exception
     */
    public static Object[] sshScp(HttpSession session, Server localServer, Server remoteServer, String filePath, String remotePath, String shPath) throws Exception {
        System.out.println("shPath=" + shPath);
        String command = SshConstant.SSH_SCP.replace("{0}", shPath).replace("{1}", filePath).replace("{2}", remoteServer.getServerIp()).replace("{3}", remotePath).replace("{4}", remoteServer.getSshName()).replace("{5}", remoteServer.getSshPwd()).replace("{6}", remoteServer.getServerPort().toString());
        CustomTask customTasks = new ExecCommand(command);
        Object[] execResult = SSHExces.exce(localServer, customTasks);
        return execResult;
    }

    /**
     * 远程传输文件
     * 
     * @author:LKJ 2014-9-25
     * @param shPath
     * @return
     * @throws Exception
     */
    public static Object[] sshScpMore(HttpSession session, Server localServer, Server remoteServer, String[] filePaths, String remotePath, String shPath) throws Exception {
        System.out.println("shPath=" + shPath);
        String[] commands = new String[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            commands[i] = SshConstant.SSH_SCP.replace("{0}", shPath).replace("{1}", filePaths[i]).replace("{2}", remoteServer.getServerIp()).replace("{3}", remotePath).replace("{4}", remoteServer.getSshName()).replace("{5}", remoteServer.getSshPwd()).replace("{6}", remoteServer.getServerPort().toString());
            System.out.println(commands[i]);
        }
        // String command = SshConstant.SSH_SCP.replace("{0}", shPath).replace("{1}", filePath).replace("{2}", remoteServer.getServerIp()).replace("{3}", remotePath).replace("{4}", remoteServer.getSshName()).replace("{5}", remoteServer.getSshPwd());
        CustomTask customTasks = new ExecCommand(commands);
        Object[] execResult = SSHExces.exce(localServer, customTasks);
        return execResult;
    }

    /**
     * 更新服务（停止使用，此功能在CommonSupportImpl中）
     */
    public static boolean updateServersByScp(HttpServletResponse response, BeanInterface beanInterface) throws Exception {
        Object[] updateResult = updateServerByScp(beanInterface, null, null, CommanConstant.STATE_UPLOAD_ALL);
        if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            response.getWriter().write(beanInterface.getName() + "更新失败");
            return false;
        } else {
            beanInterface.setUpdateTime(new Date());
            return true;
        }
    }

    /**
     * 更新服务
     * 
     * @param beanInterface bean
     * @param remotePath 更新模板路径（如果等于bean里面的更新路径，该值为null）
     * @param targetPath 目标路径（如果等于bean里面的目标路径，该值为null）
     * @param type 类型（0：更新单个文件；1更新多个文件）
     */
    public static boolean updateServersByScp(BeanInterface beanInterface, String remotePath, String targetPath, int type) throws Exception {
        Object[] updateResult = updateServerByScp(beanInterface, remotePath, targetPath, type);
        if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通過SCP更新服务
     * 
     * @param beanInterface bean
     * @param remotePath 更新模板路径（如果等于bean里面的更新路径，该值为null）
     * @param targetPath 目标路径（如果等于bean里面的目标路径，该值为null）
     * @param type 类型（0：更新单个文件；1更新多个文件）
     */
    public static Object[] updateServerByScp(BeanInterface beanInterface, String remotePath, String targetPath, int type) throws Exception {
        ServerService serverService = Application.getBean(ServerService.class);
        Server server = serverService.get(beanInterface.getServerId());
        if (server == null) {
            System.out.println("server为空");
        }
        String updatePath = "";// 更新模板路径
        if (remotePath != null) {
            updatePath = remotePath;
        } else {
            updatePath = beanInterface.getUpdatePath();
        }
        String path = "";// 目标路径
        if (targetPath != null) {
            path = targetPath;
        } else {
            path = beanInterface.getPath();
        }
        System.out.println("模板路径=" + updatePath);
        System.out.println("目标路径=" + path);
        String command = "";
        // 命令语句
        if (type == CommanConstant.STATE_UPLOAD_ALL) {
            command = SshConstant.UPDATE_SCP.replace("{0}", updatePath).replace("{1}", server.getServerIp()).replace("{2}", path).replace("{3}", server.getServerPort().toString());
            // System.out.println("command="+command);
        } else if (type == CommanConstant.STATE_UPLOAD_SINGLE) {
            command = SshConstant.UPDATE_FILE_SCP.replace("{0}", updatePath).replace("{1}", server.getServerIp()).replace("{2}", path).replace("{3}", server.getServerPort().toString());
        }
        // System.out.println("command=" + command);
        CustomTask customTasks = new ExecCommand(command);
        // 本机Server
        String localServerId = Application.getConfig("system", "localServerId");
        Server serverLocal = serverService.get(Integer.valueOf(localServerId));// 配置文件只配置的本地serverId
        // Server serverLocal = serverService.get(21);// 84
        Object[] execResult = SSHExces.exce(serverLocal, customTasks);
        return execResult;
    }

    /**
     * 更新服务
     * 
     * @param localServer 更新模板服务（如果该值为null，则默认为本机服务）
     * @param targetServer 更新目标服务（如果该值为null，则默认为本机服务）
     * @param localPath 本地路径
     * @param targetPath 目标路径
     * @param type 类型（0：更新单个文件；1更新多个文件）
     */
    public static boolean updateServerByScp(Server localServer, Server targetServer, String localPath, String targetPath, int type) throws Exception {
        ServerService serverService = Application.getBean(ServerService.class);
        // 本机Server
        String localServerId = Application.getConfig("system", "localServerId");
        Server defaultServer = serverService.get(Integer.valueOf(localServerId));// 配置文件只配置的本地serverId
        if (StringUtils.isEmpty(localPath) && StringUtils.isEmpty(targetPath)) {
            // null
            return false;
        }
        if (localServer == null) {
            localServer = defaultServer;
        }
        if (targetServer == null) {
            targetServer = defaultServer;
        }
        System.out.println("模板路径=" + localPath);
        System.out.println("目标路径=" + targetPath);
        String command = "";
        // 命令语句
        if (type == CommanConstant.STATE_UPLOAD_ALL) {
            command = SshConstant.UPDATE_SCP.replace("{0}", localPath).replace("{1}", targetServer.getServerIp()).replace("{2}", targetPath).replace("{3}", targetServer.getServerPort().toString());
        } else if (type == CommanConstant.STATE_UPLOAD_SINGLE) {
            command = SshConstant.UPDATE_FILE_SCP.replace("{0}", localPath).replace("{1}", targetServer.getServerIp()).replace("{2}", targetPath).replace("{3}", targetServer.getServerPort().toString());
        }
        CustomTask customTasks = new ExecCommand(command);
        // Server serverLocal = serverService.get(21);// 84
        Object[] execResult = SSHExces.exce(localServer, customTasks);
        if (execResult == null || execResult.length <= 0 || (Integer) execResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 上传文件夹下所有内容到指定位置（未使用）
     */
    public static void uploadAllDataToServer(BeanInterface beanInterface) {
        UploadDataToServer uploadDataToServer = new UploadDataToServer();
        Server server = CacheService.getServerById(Integer.valueOf(beanInterface.getServerId()));
        uploadDataToServer.exce(CommanConstant.STATE_UPLOAD_ALL, server, beanInterface.getModelPath(), beanInterface.getPath());
    }

    /**
     * 上传文件夹下所有内容到指定位置（未使用）
     */
    public static void uploadSingleDataToServer(BeanInterface beanInterface, String localPath) {
        UploadDataToServer uploadDataToServer = new UploadDataToServer();
        Server server = CacheService.getServerById(Integer.valueOf(beanInterface.getServerId()));
        uploadDataToServer.exce(CommanConstant.STATE_UPLOAD_SINGLE, server, localPath, beanInterface.getPath());
    }

    /**
     * 修改遠程配置文件
     * 
     * @param response
     * @param beanInterface
     * @param serviceInterface
     * @param propertyName
     * @param session
     * @throws Exception
     */
    public static Object[] updateRemoteConfig(BeanInterface beanInterface, ServiceInterface serviceInterface, String fileName) throws Exception {
        Object[] execResult = null;
        Server server = CacheService.getServerById(beanInterface.getServerId());
        // 获取通配字段
        String wildCard = serviceInterface.getWildcard(beanInterface.getId());
        // 修改配置文件通配信息
        CustomTask customTasks = new ExecCommand(SshConstant.CD_SERVER_PATH.replace("{0}", beanInterface.getPath()).concat(SshConstant.UPDATE_CONTENT.replace("{0}", wildCard).replace("{1}", fileName)));
        System.out.println("command=" + customTasks.getCommand());
        execResult = SSHExces.exce(server, customTasks);
        return execResult;
    }

    /**
     * 修改配置文件通配信息
     *
     * @author:LKJ 2014-10-8
     * @param wildcards
     * @return
     * @throws Exception
     */
    public static Object[] synWildcard(BeanInterface beanInterface, String wildcards, String fileName) {
        Object[] execResult = null;
        try {
            Server server = CacheService.getServerById(beanInterface.getServerId());
            CustomTask customTasks = new ExecCommand(SshConstant.CD_SERVER_PATH.replace("{0}", beanInterface.getPath()).concat(SshConstant.UPDATE_CONTENT.replace("{0}", wildcards).replace("{1}", fileName)));
            // System.out.println("command=" + customTasks.getCommand());
            // 本机Server
            execResult = SSHExces.exce(server, customTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execResult;
    }

    /**
     * 獲取文件夾下文件名列表(模板或更新时使用，其他慎用)
     */
    public static Object[] getFileNameList(String path) {
        Object[] execResult = null;
        try {
            // 本机Server
            String localServerId = Application.getConfig("system", "localServerId");
            Server serverLocal = Application.getBean(ServerService.class).get(Integer.valueOf(localServerId));
            // Server serverLocal = Application.getBean(ServerService.class).get(21);// 84
            CustomTask customTasks = new ExecCommand(SshConstant.GET_FILE_NAME_LIST.replace("{0}", path));
            execResult = SSHExces.exce(serverLocal, customTasks);// 若是获取模板或更新文件时，server为本机Server
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execResult;
    }

    /**
     * 獲取本地文件MD5
     * 
     * @param session
     * @param beanType 对象类型（AccountServer、IpdServer、WorldServer、DispatchServer）
     * @param pathType 路径类型（update、model）
     */
    @SuppressWarnings("unchecked")
    public static Properties getJarMD5(HttpSession session, String beanType, String pathType) {
        Properties properties = new Properties();
        try {
            if (StringUtils.isEmpty(beanType) || StringUtils.isEmpty(pathType)) {
                return null;
            }
            session.removeAttribute(beanType + Global.LOCAL_FILE_MD5_VALUE);
            // 本机Server
            String localServerId = Application.getConfig("system", "localServerId");
            Server serverLocal = Application.getBean(ServerService.class).get(Integer.valueOf(localServerId));
            // Server serverLocal = Application.getBean(ServerService.class).get(21);// 84
            // 更新模板路径
            String path = CommonUtil.getPath(beanType, pathType) + "/";
            // 获取jar文件的md5值
            List<String> updateFileNameList = (List<String>) session.getAttribute(Global.UPDATE_FILE_NAME_LIST);
            if (updateFileNameList != null && updateFileNameList.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (String fileName : updateFileNameList) {
                    sb.append(path + fileName).append(" ");
                }
                Object[] fileMD5 = null;
                // System.out.println("sb=" + sb);
                if (sb.length() > 0) {
                    fileMD5 = getFileMD5(serverLocal, sb.toString());
                }
                if (fileMD5 != null && fileMD5.length > 0 && (Integer) fileMD5[0] == CommanConstant.RESULT_TRUE_STATE) {
                    String[] fileMD5s = fileMD5[1].toString().trim().split("\n");
                    for (int i = 0; i < updateFileNameList.size(); i++) {
                        properties.put(updateFileNameList.get(i), fileMD5s[i]);
                        // System.out.println(updateFileNameList.get(i) + "=" + properties.getProperty(updateFileNameList.get(i)));
                    }
                }
            }
            session.setAttribute(beanType + Global.LOCAL_FILE_MD5_VALUE, properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 獲取文件MD5值
     * 
     * @author:LKJ
     * @return
     * @throws Exception
     */
    public static Object[] getFileMD5(Server server, String path) {
        Object[] execResult = null;
        try {
            CustomTask customTasks = new ExecCommand(SshConstant.GET_FILE_MD5.replace("{0}", path));
            execResult = SSHExces.exce(server, customTasks);// 若是获取模板或更新文件时，server为本机Server
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execResult;
    }
    // /**
    // * 获取路径
    // *
    // * @param beanType
    // * bean类型
    // * @param pathType
    // * update或model
    // * @return
    // */
    // public static String getPath(String beanType, String pathType) {
    // if (StringUtils.isEmpty(beanType) || StringUtils.isEmpty(pathType)) {
    // return null;
    // }
    // String path = "";
    // BeanInterface beanInterface = null;
    // if (beanType.equals("AccountServer")) {
    // beanInterface = CacheService.getAccountServerAllList().get(0);
    // } else if (beanType.equals("IpdServer")) {
    // beanInterface = CacheService.getIpdServerAllList().get(0);
    // } else if (beanType.equals("WorldServer")) {
    // beanInterface = CacheService.getWorldServerAllList().get(0);
    // } else if (beanType.equals("DispatchServer")) {
    // beanInterface = CacheService.getDispatchServerAllList().get(0);
    // } else if (beanType.equals("serverLib")) {
    // beanInterface = CacheService.getServerAllList().get(0);
    // }
    // if (beanInterface != null) {
    // if (pathType.equals("update")) {
    // path = beanInterface.getUpdatePath();
    // } else if (pathType.equals("model")) {
    // path = beanInterface.getModelPath();
    // }
    // }
    // return path;
    // }
}
