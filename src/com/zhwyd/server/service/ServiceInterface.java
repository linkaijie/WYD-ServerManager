package com.zhwyd.server.service;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.vo.BatchOpearVo;
public interface ServiceInterface {
    public abstract void updateState(int id, int state, int type);

    public abstract String getWildcard(int id);

    public abstract String startServer(BeanInterface beanInterface) throws Exception;

    public abstract String killServer(BeanInterface beanInterface) throws Exception;

    public abstract void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session);

    public abstract void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception;

    public abstract Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception;

    public abstract void writeBatchLog(HttpSession session, String logContent);

    public abstract void synchronizeConfig(String ids);

    // public abstract void updateServerByScp(HttpServletResponse response, String ids) throws Exception;
    public abstract BeanInterface getBean(Integer id);

    /**
     * @param object        对象
     * @param classType     Class类型
     * @param methodName    方法名
     * @param params        方法对于的参数
     * @return
     * @throws Exception
     */
    public Method getMethod(ServiceInterface serviceInterface, String classType, String methodName, String... params) throws Exception;
    
    /**
     * 同步远程配置
     * @param session TODO
     */
    public boolean synCfgByDeploy(BeanInterface beanInterface, BatchOpearVo batchOpearVo, HttpSession session) throws Exception;
    
    public void initCache();
}
