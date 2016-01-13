package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.commons.SendMail;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.DispatchVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.util.HttpClientUtil;
import com.zhwyd.server.common.util.ParamUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.monitor.BathCreative;
import com.zhwyd.server.monitor.Monitor;
import com.zhwyd.server.service.MonitorService;
import com.zhwyd.server.thread.BatchMonitorNewThread;
import com.zhwyd.server.thread.BatchMonitorThread;
public class MonitorServiceImpl extends BaseServiceImpl implements MonitorService {
    private static Map<String, String> ipdMainListMap = new HashMap<String, String>();

    /**
     * 获取需监控的DispatchServer列表
     */
    public List<DispatchServer> getNeedMonitorList(StringBuffer errorSb) {
        WorldServer worldServer = null;
        List<DispatchServer> dispatchServerList = new ArrayList<DispatchServer>();
        List<DispatchServer> dispatchServerAllList = CacheService.getDispatchServerAllList();
        for (DispatchServer dispatchServer : dispatchServerAllList) {
            if (dispatchServer.getState() == CommanConstant.STATE_START) {
                worldServer = CacheService.getWorldServerById(dispatchServer.getWorldId());
                if (worldServer.getState() == CommanConstant.STATE_START) {
                    dispatchServerList.add(dispatchServer);
                } else {
                    errorSb.append("服务管理工具中ID为：<font color='red'>" + dispatchServer.getId());
                    errorSb.append("</font>,地址为：<font color='red'>[" + dispatchServer.getPublicserver() + "," + dispatchServer.getPublicport() + "]");
                    errorSb.append("</font>的Dispatch服务已开启，但对应的id为:");
                    errorSb.append(worldServer.getId() + ",名称为：<font color='red'>" + worldServer.getName() + "</font>的WorldServer服务未开启。");
                    errorSb.append("</br>");
                }
            }
        }
        if (errorSb.length() > 0 && !("").equals(errorSb)) {
            System.out.println(errorSb);
        }
        return dispatchServerList;
    }

    /**
     * 比较ipd列表
     */
    public String checkIpdMainList(List<DispatchServer> dispatchServerList, StringBuffer errorSb) throws Exception {
        String compareResult = "";
        List<String> dispatchAddressList = new ArrayList<String>();
        if (dispatchServerList != null && dispatchServerList.size() > 0) {
            for (DispatchServer dispatchServer : dispatchServerList) {
                dispatchAddressList.add(dispatchServer.getPublicserver().concat(",").concat(String.valueOf(dispatchServer.getPublicport())));
            }
        }
        compareResult = this.compareTwoList(this.getIpdMianList(errorSb), dispatchAddressList, errorSb);
        return compareResult;
    }

    /**
     * 获取ipd列表
     */
    @SuppressWarnings("unused")
    public List<String> getIpdMianList(StringBuffer errorSb) {
        List<String> dispatchAddresList = new ArrayList<String>();
        try {
            String url = Application.getConfig("system", "ipdurl");
            String value = HttpClientUtil.GetData(url);
            if (!StringUtils.isEmpty(value)) {
                String json = "{values:" + value + "}";
                JSONArray array = ParamUtil.getJSONArray("values", json);
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        String address = this.getDispatchAddress(object, dispatchAddresList);
                    }
                }
            } else {
                errorSb.append("主IPD列表为空,可能是IPD地址出错或者IPD服务未开启。</br>");
            }
        } catch (Exception e) {
            errorSb.append("获取主IPD列表出错。 </br>");
        }
        return dispatchAddresList;
    }

    /**
     * 解析json获取主Ipd中Dispatch地址
     */
    public String getDispatchAddress(JSONObject object, List<String> addressList) {
        String address = "";
        String machineName = "";
        try {
            JSONArray lineInfoList = JSONArray.fromObject(object.getString("lineInfoList"));
            if (lineInfoList != null && lineInfoList.size() > 0) {
                for (Object lineInfo : lineInfoList) {
                    JSONObject lineInfoObject = (JSONObject) lineInfo;
                    address = lineInfoObject.getString("address");
                    machineName = object.getString("machineName");
                    ipdMainListMap.put(address, machineName);
                    addressList.add(address);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * 比较ipd列表
     */
    public String compareTwoList(List<String> ipdMianList, List<String> dispatchAddressList, StringBuffer errorSb) {
        String compareResult = "";
        if (ipdMianList.size() != dispatchAddressList.size()) {
            errorSb.append("主IPD列表与服务管理工具中已开启的Dispatch列表不一致，请检查是否出错！</br>");
        }
        if (ipdMianList != null && ipdMianList.size() > 0) {
            for (String address : ipdMianList) {
                if (dispatchAddressList != null && dispatchAddressList.size() > 0) {
                    if (!dispatchAddressList.contains(address)) {
                        errorSb.append("名称为：<font color='red'>" + ipdMainListMap.get(address) + "</font>Dispatch地址为：<font color='red'>" + address + "</font>的主IPD列表与服务管理工具中已开启Dispatch不一致。</br>");
                    }
                } else {
                    errorSb.append("名称为：<font color='red'>" + ipdMainListMap.get(address) + "</font>Dispatch地址为：<font color='red'>" + address + "</font>的主IPD列表与服务管理工具中已开启Dispatch不一致。</br>");
                }
            }
        } else {
            errorSb.append("主IPD列表为空。</br>");
        }
        return compareResult;
    }

    /**
     * 发送邮件
     */
    public String sendEmail(String content) throws Exception {
        List<String> mailStr = CacheService.getMailSendList();
        for (String mail : mailStr) {
            System.out.println("mail=" + mail);
            if (!StringUtils.isEmpty(mail)) {
                try {
                    String smtp_host = "smtp.zhwyd.com";
                    String domain = "@zhwyd.com";
                    String smtp_user = "kaijie_lin" + domain;
                    String smtp_to = mail;
                    StringBuilder href = new StringBuilder();
                    href.append(content);
                    SendMail sendMail = new SendMail(smtp_host);
                    sendMail.setNeedAuth(true);
                    sendMail.setNamePass(smtp_user, "3543558");
                    sendMail.setFrom(smtp_user);
                    sendMail.setTo(smtp_to);
                    sendMail.setSubject("监控信息提醒");
                    sendMail.setBody(href.toString());
                    sendMail.sendout();
                } catch (Exception e) {
                    System.out.println("日志记录邮件发送错误信息");
                }
            }
        }
        return "";
    }

    /**
     * 批量监控Dispatch服务
     * 
     * @author:LKJ 2014-12-17
     * @throws Exception
     */
    public void batchMonitor(List<DispatchServer> dispatchServerList) throws Exception {
        if (dispatchServerList != null && dispatchServerList.size() > 0) {
            for (DispatchServer dispatchServer : dispatchServerList) {
                System.out.println("dispatchServerId=" + dispatchServer.getId());
                Application.getManager().getSimpleThreadPool().execute(this.createMonitorTask(dispatchServer));
            }
        }
    }

    /**
     * 创建新的开启任务
     * 
     * @return 开启任务
     */
    private Runnable createMonitorTask(DispatchServer dispatchServer) {
        MonitorService monitorService = Application.getBean(MonitorService.class);
        return new BatchMonitorThread(dispatchServer, monitorService);
    }

    /**
     * 获取ipd列表
     */
    public List<DispatchVo> getIpdLoadList(StringBuffer errorSb) {
        List<DispatchVo> dispatchVoList = new ArrayList<DispatchVo>();
        try {
            String url = Application.getConfig("system", "ipdurl");
            String value = HttpClientUtil.GetData(url);
            if (!StringUtils.isEmpty(value)) {
                String json = "{values:" + value + "}";
                JSONArray array = ParamUtil.getJSONArray("values", json);
                if (array != null && array.size() > 0) {
                    System.out.println("array.size()="+array.size());
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        this.getDispatchInfo(object, dispatchVoList);
                    }
                }
            } else {
                errorSb.append("主IPD列表为空,可能是IPD地址出错或者IPD服务未开启。</br>");
            }
        } catch (Exception e) {
            errorSb.append("获取主IPD列表出错。 </br>");
        }
        return dispatchVoList;
    }

    /**
     * 解析json获取主Ipd中Dispatch地址
     */
    public void getDispatchInfo(JSONObject object, List<DispatchVo> dispatchVoList) {
        String address = "";
        String machineCode = "";
        String dispatchId = "";
        String machineName = "";
        String serviceId = "";
        try {
            JSONArray lineInfoList = JSONArray.fromObject(object.getString("lineInfoList"));
            if (lineInfoList != null && lineInfoList.size() > 0) {
                DispatchVo dispatchVo = null;
                for (Object lineInfo : lineInfoList) {
                    dispatchVo = new DispatchVo();
                    JSONObject lineInfoObject = (JSONObject) lineInfo;
                    address = lineInfoObject.getString("address");
                    machineCode = object.getString("machineCode");
                    machineName = object.getString("machineName");
                    if (machineCode.contains("-")) {
                        serviceId = machineName.split("-")[0];
                        serviceId = serviceId.replaceAll("[^(0-9)]", "");
                    }else {
                        serviceId = machineCode;
                    }
                    dispatchId = lineInfoObject.getString("id");
                    dispatchVo.setDispatchIp(address.split(",")[0]);
                    dispatchVo.setDispatchPort(address.split(",")[1]);
                    dispatchVo.setDispatchCode(machineCode);
                    dispatchVo.setDispatchId(dispatchId);
                    dispatchVo.setServiceId(serviceId);
                    dispatchVoList.add(dispatchVo);
                    System.out.println("machineCode="+object.getString("machineCode")+"machineName="+object.getString("machineName"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量监控Dispatch服务
     * 
     * @author:LKJ 2014-12-17
     * @throws Exception
     */
    public void batchMonitorNew(List<DispatchVo> dispatchVoList) throws Exception {
        if (dispatchVoList != null && dispatchVoList.size() > 0) {
            for (DispatchVo dispatchVo : dispatchVoList) {
                 System.out.println("dispatchServerId=" + dispatchVo.getDispatchCode());
                Application.getManager().getSimpleThreadPool().execute(this.createMonitorNewTask(dispatchVo));
            }
        }
    }

    /**
     * 创建新的监控任务
     * 
     * @return 监控任务
     */
    private Runnable createMonitorNewTask(DispatchVo dispatchVo) {
        MonitorService monitorService = Application.getBean(MonitorService.class);
        return new BatchMonitorNewThread(dispatchVo, monitorService);
    }

    /**
     * 监控Dispatch服务(暂时未使用)
     */
    public void monitorInside(List<DispatchVo> dispatchVoList) throws Exception {
        if (dispatchVoList != null && dispatchVoList.size() > 0) {
            Application.getManager().getSimpleThreadPool().execute(this.createMonitorInsideTask(dispatchVoList));
        }
    }

    /**
     * 创建新的监控任务
     * 
     * @return 创建监控内部类线程
     */
    private Runnable createMonitorInsideTask(List<DispatchVo> dispatchVoList) {
        return new MonitorInsideThread(dispatchVoList);
    }
    /**
     * 监控线程内部类
     */
    public class MonitorInsideThread implements Runnable {
        private static final String loginName = "ML_";
        private static final String password  = "888888";
        private List<DispatchVo>    dispatchVoList;
        private MonitorService      monitorService;

        public MonitorInsideThread(List<DispatchVo> dispatchVoList) {
            this.dispatchVoList = dispatchVoList;
            this.monitorService = Application.getBean(MonitorService.class);
        }

        @Override
        public void run() {
            try {
                for (DispatchVo dispatchVo : dispatchVoList) {
                    StringBuffer errorSb = getHeadString(dispatchVo);
                    // System.out.println("message=" + errorSb);
                    String uuid = "";
                    User user = new User();
                    user.setUsername(loginName + dispatchVo.getDispatchPort() + "_" + dispatchVo.getDispatchCode());
                    // System.out.println("userName=" + user.getUsername());
                    user.setPassword(password);
                    JSONObject loginJson = JSONObject.fromObject(BathCreative.login(user));// 账号登录
                    uuid = loginJson.getString("uuid");
                    if (loginJson.get("code").toString().equals("0")) {
                        JSONObject registerJson = BathCreative.register(user);
                        // 账号注册：code返回0代表不成功,不成功则返回错误信息,发送邮件
                        if ("0".equals(registerJson.getString("code"))) {
                            errorSb.append("注册账号时" + user.getUsername() + "," + registerJson.getString("message"));
                        }
                        uuid = registerJson.getString("uuid");
                    }
                    user.setUuid(uuid);
                    Monitor monitor = new Monitor(dispatchVo.getDispatchIp(), Integer.valueOf(dispatchVo.getDispatchPort()), user);
                    String monitorResult = monitor.thirdLoginProcess(0);
                    // 成功返回空字符串，否则视为失败，发送邮件
                    if (monitorResult != null && !monitorResult.equals("")) {
                        errorSb.append(monitorResult);
                        System.out.println(monitorResult);
                        monitorService.sendEmail(errorSb.toString());
                        SystemLogService.monitorLog(errorSb);
                    }
                    errorSb = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public StringBuffer getHeadString(DispatchVo dispatchVo) {
        StringBuffer headSb = new StringBuffer();
        headSb.append("</br>");
        headSb.append("分区号（machineCode）为【" + dispatchVo.getDispatchCode() + "】,");
        headSb.append("Dispatch地址为【" + dispatchVo.getDispatchIp() + "," + dispatchVo.getDispatchPort() + "】,");
        headSb.append("</br>");
        return headSb;
    }
}
