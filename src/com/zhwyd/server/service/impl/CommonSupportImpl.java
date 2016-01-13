package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.CommonSupport;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.thread.BatchDeployServerThread;
import com.zhwyd.server.thread.BatchUpdateRemoteCfgThread;
import com.zhwyd.server.thread.BatchUpdateServerThread;
public abstract class CommonSupportImpl implements CommonSupport {
    /**
     * 通关SCP更新服务
     */
    public void updateServerByScp(HttpSession session, String ids, ServiceInterface serviceInterface) throws Exception {
        String[] beanIds = ids.split(",");
        session.removeAttribute(Global.BATCH_UPDATEVOLIST);
        session.removeAttribute(Global.UPDATE_BEAN_LIST);
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        for (int i = 0; i < beanIds.length; i++) {
            System.out.println("更新" + beanIds[i]);
            // 更新文件操作
            Application.getManager().getSimpleThreadPool().execute(this.createUpdateTask(Integer.valueOf(beanIds[i]), session, serviceInterface));
            beanInterfaceList.add(serviceInterface.getBean(Integer.valueOf(beanIds[i])));
        }
        session.setAttribute(Global.UPDATE_BEAN_LIST, beanInterfaceList);
    }

    /**
     * 创建新的批量更新任务
     */
    private Runnable createUpdateTask(int id, HttpSession session, ServiceInterface serviceInterface) {
        return new BatchUpdateServerThread(id, null, null, serviceInterface, session);
    }

    /**
     * 更新远程配置文件
     */
    public void updateRemoteCfg(String ids, String fileName, ServiceInterface serviceInterface, HttpSession session) throws Exception {
        String[] beanIds = ids.split(",");
        session.removeAttribute(Global.BATCH_UPDATE_REMOTE_CFG);
        session.removeAttribute(Global.UPDATE_CFG_BEAN_LIST);
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        for (int i = 0; i < beanIds.length; i++) {
            System.out.println("更新" + beanIds[i]);
            // 更新文件操作
            Application.getManager().getSimpleThreadPool().execute(this.createUpdateRemoteCfgTask(Integer.valueOf(beanIds[i]), fileName, session, serviceInterface));
            beanInterfaceList.add(serviceInterface.getBean(Integer.valueOf(beanIds[i])));
        }
        session.setAttribute(Global.UPDATE_CFG_BEAN_LIST, beanInterfaceList);
    }

    /**
     * 创建新的远程配置文件任务
     */
    private Runnable createUpdateRemoteCfgTask(int id, String fileName, HttpSession session, ServiceInterface serviceInterface) {
        return new BatchUpdateRemoteCfgThread(id, fileName, serviceInterface, session);
    }

    /**
     * 通关SCP部署服务
     */
    public void deployServerByScp(HttpSession session, String ids, ServiceInterface serviceInterface) throws Exception {
        String[] beanIds = ids.split(",");
        session.removeAttribute(Global.BATCH_DEPLOYVOLIST);
        session.removeAttribute(Global.DEPLOY_BEAN_LIST);
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        List<BatchOpearVo> batchOpearVoList = new ArrayList<BatchOpearVo>();
        for (int i = 0; i < beanIds.length; i++) {
            int beanId = Integer.valueOf(beanIds[i]);
            BatchOpearVo batchOpearVo = new BatchOpearVo();
            batchOpearVo.setId(beanId);
            batchOpearVo.setRemark("准备部署");
            batchOpearVoList.add(batchOpearVo);
            System.out.println("部署" + beanId);
            // 更新文件操作
            Application.getManager().getSimpleThreadPool().execute(this.createdeployTask(beanId, serviceInterface.getBean(beanId).getModelPath(), null, batchOpearVo, serviceInterface, session));
            beanInterfaceList.add(serviceInterface.getBean(Integer.valueOf(beanIds[i])));
        }
        session.setAttribute(Global.BATCH_DEPLOYVOLIST, batchOpearVoList);
        session.setAttribute(Global.DEPLOY_BEAN_LIST, beanInterfaceList);
    }

    /**
     * 创建新的批量更新任务
     * @param session TODO
     */
    private Runnable createdeployTask(int id, String remotePath, String targetPath, BatchOpearVo batchOpearVo, ServiceInterface serviceInterface, HttpSession session) {
        return new BatchDeployServerThread(id, remotePath, targetPath, batchOpearVo, serviceInterface, session);
    }
}
