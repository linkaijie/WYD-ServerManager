package com.zhwyd.server.thread;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchUpdateRemoteCfgThread implements Runnable {
    private int              id;
    private ServiceInterface serviceInterface = null;
    private HttpSession      session;
    private String           fileName;

    public BatchUpdateRemoteCfgThread(int id, String fileName, ServiceInterface serviceInterface, HttpSession session) {
        this.id = id;
        this.serviceInterface = serviceInterface;
        this.session = session;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            boolean result = false;
            BatchOpearVo batchOpearVo = new BatchOpearVo();
            BeanInterface beanInterface = serviceInterface.getBean(id);
            String filePath = session.getServletContext().getRealPath("cfgmodel");
            // filePath = "/data/apps/model";
            String remotePath = filePath + "/" + fileName;
            // SCP 操作
            boolean scpResult = SshxcuteUtils.updateServersByScp(beanInterface, remotePath, null, CommanConstant.STATE_UPLOAD_SINGLE);
            if (scpResult) {
                // 更新操作
                Object[] updateResult = SshxcuteUtils.updateRemoteConfig(beanInterface, serviceInterface, fileName);
                if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                    batchOpearVo.isFail("更新失败");// 失敗
                } else {
                    batchOpearVo.isSUCCESS("更新成功");// 成功
                    result = true;
                }
            } else {
                batchOpearVo.isFail("更新失败");// 失敗
            }
            batchOpearVo.setId(beanInterface.getId());
            batchOpearVo.setName(beanInterface.getName());
            batchOpearVo.setStatus(result);
            CacheService.updateSession(session, batchOpearVo, Global.BATCH_UPDATE_REMOTE_CFG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
