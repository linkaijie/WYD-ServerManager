package com.zhwyd.server.service;
import java.util.List;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.vo.DispatchVo;
public interface MonitorService extends BaseService {
    /**
     * 获取需监控的DispatchServer列表
     * 
     * @param errorSb
     *            TODO
     */
    public List<DispatchServer> getNeedMonitorList(StringBuffer errorSb);

    public List<String> getIpdMianList(StringBuffer errorSb) throws Exception;

    /**
     * 比较ipd列表 author:LKJ 2015-1-21
     * 
     * @param list1
     * @param list2
     * @param errorSb
     *            TODO
     */
    public String compareTwoList(List<String> list1, List<String> list2, StringBuffer errorSb);

    /**
     * 比较ipd列表
     * 
     * @author:LKJ 2015-1-21
     * @param errorSb
     *            TODO
     * @param list1
     * @param list2
     * @throws Exception
     */
    public String checkIpdMainList(List<DispatchServer> dispatchServerList, StringBuffer errorSb) throws Exception;

    /**
     * 发送邮件
     */
    public String sendEmail(String content) throws Exception;

    /**
     * 批量监控Dispatch服务
     * 
     * @author:LKJ 2014-12-17
     * @throws Exception
     */
    public void batchMonitor(List<DispatchServer> dispatchServerList) throws Exception;

    /**
     * 获取ipd列表
     */
    public List<DispatchVo> getIpdLoadList(StringBuffer errorSb);

    /**
     * 批量监控Dispatch服务
     * 
     * @author:LKJ 2014-12-17
     * @throws Exception
     */
    public void batchMonitorNew(List<DispatchVo> dispatchVoList) throws Exception;
    
    /**
     * 监控Dispatch服务
     */
    public void monitorInside(List<DispatchVo> dispatchVoList) throws Exception; 
}
