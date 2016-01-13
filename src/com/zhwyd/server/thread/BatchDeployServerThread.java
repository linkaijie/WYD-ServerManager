package com.zhwyd.server.thread;
import java.lang.reflect.Method;
import java.util.Date;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchDeployServerThread implements Runnable {
    private int              id;
    private String           remotePath;
    private String           targetPath;
    private BatchOpearVo     batchOpearVo;
    private HttpSession      session;
    private ServiceInterface serviceInterface = null;

    /**
     * 
     * @param id                對象ID
     * @param remotePath        更新模板路徑（如果等于bean里面的更新路径（updatePath），该值为null）
     * @param targetPath        目標路徑（如果等于bean里面的目标路径（path），该值为null）
     * @param serviceInterface  對象的service
     * @param session TODO
     */
    public BatchDeployServerThread(int id, String remotePath, String targetPath, BatchOpearVo batchOpearVo, ServiceInterface serviceInterface, HttpSession session) {
        this.id = id;
        this.remotePath = remotePath;
        this.targetPath = targetPath;
        this.batchOpearVo = batchOpearVo;
        this.session = session;
        this.serviceInterface = serviceInterface;
    }

    @Override
    public void run() {
        try {
            BeanInterface beanInterface = serviceInterface.getBean(id);
            if (beanInterface == null || beanInterface.getIsDeploy() == CommanConstant.STATE_START) {
                batchOpearVo.isFail("beanInterface为空或已部署");
                return;
            }
            batchOpearVo.setRemark("正在部署" + beanInterface.getName());
            String classSuperName = beanInterface.getClass().getSuperclass().getName();
            beanInterface.setUpdateTime(new Date());
            // SCP 操作
            boolean result = SshxcuteUtils.updateServersByScp(beanInterface, remotePath, targetPath, CommanConstant.STATE_UPLOAD_SINGLE);
            if (!result) {
                batchOpearVo.isFail("SCP部署失败");
                return;
            }
            boolean synResult = serviceInterface.synCfgByDeploy(beanInterface, batchOpearVo, session);
            if (synResult) {
                beanInterface.setIsDeploy(CommanConstant.STATE_START);
                Method method = serviceInterface.getMethod(serviceInterface, "", "merge", classSuperName);// 获取方法
                method.invoke(serviceInterface, beanInterface);// 调用方法
                serviceInterface.initCache();
                batchOpearVo.isSUCCESS("部署成功");
            } else {
                batchOpearVo.isSUCCESS("部署失败");
            }
            batchOpearVo.setStatus(synResult);
        } catch (Exception e) {
            e.printStackTrace();
            batchOpearVo.isFail("部署失败");
        }
    }
}
