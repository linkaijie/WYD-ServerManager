package com.zhwyd.server.bean.vo;
public class DispatchVo {
    private String dispatchIp;
    private String dispatchPort;
    private String dispatchId;
    private String dispatchCode;
    private String serviceId;

    public String getDispatchIp() {
        return dispatchIp;
    }

    public void setDispatchIp(String dispatchIp) {
        this.dispatchIp = dispatchIp;
    }

    public String getDispatchPort() {
        return dispatchPort;
    }

    public void setDispatchPort(String dispatchPort) {
        this.dispatchPort = dispatchPort;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        this.dispatchCode = dispatchCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DispatchVo)) {
            return false;
        }
        DispatchVo dispatchVo = (DispatchVo) o;
        if (dispatchVo.getDispatchPort() == null || dispatchVo.getDispatchIp() == null || dispatchVo.getDispatchId() == null || dispatchVo.getDispatchCode() == null) {
            return false;
        }
        return dispatchVo.getDispatchIp().equals(dispatchIp) && dispatchVo.getDispatchPort().equals(dispatchPort) && dispatchVo.getDispatchId().equals(dispatchId) && dispatchVo.getDispatchCode().equals(dispatchCode);
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
