package com.zhwyd.server.thread;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchUpdateServerThread implements Runnable {
    private int              id;
    private String           remotePath;
    private String           targetPath;
    private ServiceInterface serviceInterface = null;
    private HttpSession      session;

    /**
     * 
     * @param id                對象ID
     * @param remotePath        更新模板路徑（如果等于bean里面的更新路径（updatePath），该值为null）
     * @param targetPath        目標路徑（如果等于bean里面的目标路径（path），该值为null）
     * @param serviceInterface  對象的service
     * @param session           HttpSession
     */
    public BatchUpdateServerThread(int id, String remotePath, String targetPath, ServiceInterface serviceInterface, HttpSession session) {
        this.id = id;
        this.remotePath = remotePath;
        this.targetPath = targetPath;
        this.serviceInterface = serviceInterface;
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            BeanInterface beanInterface = serviceInterface.getBean(id);
            String className = beanInterface.getClass().getName();
            String beanName = className.substring(className.lastIndexOf(".") + 1);
            String classSuperName = beanInterface.getClass().getSuperclass().getName();
            // 本地文件MD5信息
            Properties properties = (Properties) session.getAttribute(beanName + Global.LOCAL_FILE_MD5_VALUE);
            beanInterface.setUpdateTime(new Date());
            // SCP 操作
            boolean result = SshxcuteUtils.updateServersByScp(beanInterface, remotePath, targetPath, CommanConstant.STATE_UPLOAD_ALL);
            BatchOpearVo batchOpearVo = new BatchOpearVo();
            if (result) {
                List<String> updateFileNameList = (List<String>) session.getAttribute(Global.UPDATE_FILE_NAME_LIST);// 更新文件名稱列表
                // 獲取被更新文件的MD5值
                result = CommonUtil.compareMD5(session, beanInterface, properties, updateFileNameList);
                // 比较MD5值
                if (result) {
                    Method method = serviceInterface.getMethod(serviceInterface, "", "merge", classSuperName);// 获取方法
                    method.invoke(serviceInterface, beanInterface);// 调用方法
                    batchOpearVo.isSUCCESS("更新成功");
                } else {
                    batchOpearVo.isFail("MD5比较失败");
                }
            } else {
                batchOpearVo.isFail("更新失败");
            }
            batchOpearVo.setId(beanInterface.getId());
            batchOpearVo.setName(beanInterface.getName());
            batchOpearVo.setStatus(result);
            CacheService.updateSession(session, batchOpearVo, Global.BATCH_UPDATEVOLIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
