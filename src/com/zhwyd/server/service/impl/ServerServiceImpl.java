package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.ServerType;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.ServerModel;
import com.zhwyd.server.dao.ServerDao;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.sshxcute.SSHExces;
public class ServerServiceImpl extends ServiceSupportImpl<Server> implements ServerService {
    protected ServerDao serverDao;

    public void setServerDao(ServerDao serverDao) {
        super.setDaoSupport(serverDao);
        this.serverDao = serverDao;
    }

    /**
     * 获取服务列表
     * author:LKJ 
     * 2014-9-4 
     * @return
     */
    public List<Server> getServerList(ServerModel serverModel) {
        List<Server> serverList = serverDao.getServerList(serverModel);
        return serverList;
    }

    /**
    * 通关SCP更新服务
    */
    public void updateServerByScp(HttpSession session, String ids, int type) throws Exception {
        String[] beanIds = ids.split(",");
        session.removeAttribute(Global.BATCH_UPDATEVOLIST);
        session.removeAttribute(Global.UPDATE_BEAN_LIST);
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        List<BatchOpearVo> batchOpearVoList = new ArrayList<BatchOpearVo>();
        for (int i = 0; i < beanIds.length; i++) {
            BatchOpearVo batchOpearVo = new BatchOpearVo();
            batchOpearVo.setId(Integer.valueOf(beanIds[i]));
            batchOpearVo.setRemark("准备更新");
            System.out.println("更新" + beanIds[i]);
            // 更新文件操作
            Application.getManager().getSimpleThreadPool().execute(this.createUpdateTask(batchOpearVo, Integer.valueOf(beanIds[i]), type, session));
            beanInterfaceList.add(this.get(Integer.valueOf(beanIds[i])));
            batchOpearVoList.add(batchOpearVo);
        }
        session.setAttribute(Global.UPDATE_BEAN_LIST, beanInterfaceList);
        session.setAttribute(Global.BATCH_UPDATEVOLIST, batchOpearVoList);
    }

    /**
     * 创建新的批量更新任务
     */
    private Runnable createUpdateTask(BatchOpearVo batchOpearVo, int id, int type, HttpSession session) {
        Server server = this.get(id);
        if (server == null) {
            batchOpearVo.isFail("ID為" + id + "的Server不存在");
            return null;
        }
        server.setType(type);
        String localPath = "";
        String remotePath = "";
        if (type == CommanConstant.SERVER_LIB) {
            localPath = server.getUpdatePath();
            remotePath = server.getPath();
        } else if (type == CommanConstant.LIB) {
            localPath = server.getLibUpdatePath();
            remotePath = server.getLibPath();
        }
        return new UpdateServerThread(batchOpearVo, id, localPath, remotePath, type, session);
    }
    /**
     * 更新Server内部类线程
     */
    public class UpdateServerThread implements Runnable {
        private int           id;
        private String        localPath;
        private String        targetPath;
        private ServerService serverService = null;
        private HttpSession   session;
        private BatchOpearVo  batchOpearVo;
        private int           type;

        /**
         * @param id                對象ID
         * @param remotePath        更新模板路徑（如果等于bean里面的更新路径（updatePath），该值为null）
         * @param targetPath        目標路徑（如果等于bean里面的目标路径（path），该值为null）
         * @param session           HttpSession
         */
        public UpdateServerThread(BatchOpearVo batchOpearVo, int id, String localPath, String targetPath, int type, HttpSession session) {
            this.id = id;
            this.localPath = localPath;
            this.targetPath = targetPath;
            this.serverService = Application.getBean(ServerService.class);
            this.session = session;
            this.batchOpearVo = batchOpearVo;
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                Server server = serverService.get(id);
                server.setType(type);
                // String className = server.getClass().getName();
                String beanName = targetPath.substring(targetPath.lastIndexOf("/") + 1);
                // 本地文件MD5信息
                Properties properties = (Properties) session.getAttribute(beanName + Global.LOCAL_FILE_MD5_VALUE);
                server.setUpdateTime(new Date());
                // SCP 操作
                boolean result = SshxcuteUtils.updateServersByScp(server, localPath, targetPath, CommanConstant.STATE_UPLOAD_ALL);
                if (result) {
                    List<String> updateFileNameList = (List<String>) session.getAttribute(Global.UPDATE_FILE_NAME_LIST);// 更新文件名稱列表
                    // 獲取被更新文件的MD5值
                    result = CommonUtil.compareMD5(session, server, properties, updateFileNameList);
                    // 比较MD5值
                    if (result) {
                        serverService.update(server);
                        batchOpearVo.isSUCCESS("更新成功");
                    } else {
                        batchOpearVo.isFail("MD5比较失败");
                    }
                } else {
                    batchOpearVo.isFail("更新失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通关SCP更新服务
     */
    public void runCommand(HttpSession session, String ids, String command) throws Exception {
        String[] beanIds = ids.split(",");
        session.removeAttribute(Global.BATCH_UPDATEVOLIST);
        session.removeAttribute(Global.UPDATE_BEAN_LIST);
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        List<BatchOpearVo> batchOpearVoList = new ArrayList<BatchOpearVo>();
        for (int i = 0; i < beanIds.length; i++) {
            BatchOpearVo batchOpearVo = new BatchOpearVo();
            batchOpearVo.setId(Integer.valueOf(beanIds[i]));
            batchOpearVo.setRemark("准备执行");
            System.out.println("执行" + beanIds[i]);
            // 更新文件操作
            Application.getManager().getSimpleThreadPool().execute(this.createCommandTask(batchOpearVo, Integer.valueOf(beanIds[i]), command));
            beanInterfaceList.add(this.get(Integer.valueOf(beanIds[i])));
            batchOpearVoList.add(batchOpearVo);
        }
        session.setAttribute(Global.UPDATE_BEAN_LIST, beanInterfaceList);
        session.setAttribute(Global.BATCH_UPDATEVOLIST, batchOpearVoList);
    }

    /**
     * 创建执行命令任务
     */
    private Runnable createCommandTask(BatchOpearVo batchOpearVo, int id, String command) {
        Server server = this.get(id);
        if (server == null) {
            batchOpearVo.isFail("ID為" + id + "的Server不存在");
            return null;
        }
        return new RunCommandThread(batchOpearVo, id, command);
    }
    /**
     * 执行命令任务内部类线程
     */
    public class RunCommandThread implements Runnable {
        private int           id;
        private String        command;
        private ServerService serverService = null;
        private BatchOpearVo  batchOpearVo;

        /**
         * @param id                對象ID
         * @param command           命令
         */
        public RunCommandThread(BatchOpearVo batchOpearVo, int id, String command) {
            this.id = id;
            this.command = command;
            this.serverService = Application.getBean(ServerService.class);
            this.batchOpearVo = batchOpearVo;
        }

        @Override
        public void run() {
            try {
                Server server = serverService.get(id);
                batchOpearVo.setRemark("开始执行");
                CustomTask customTasks = new ExecCommand(command);
                Object[] execResult = SSHExces.exce(server, customTasks);
                if (execResult == null || execResult.length <= 0 || (Integer) execResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                    batchOpearVo.isFail("执行失败：" + execResult[1].toString());
                } else {
                    batchOpearVo.isSUCCESS("执行成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 获取服务類型列表
     * author:LKJ 
     * 2014-9-4 
     * @return
     */
    public List<ServerType> getServerTypeList() {
        List<ServerType> serverTypeList = serverDao.getServerTypeList();
        return serverTypeList;
    }
}
