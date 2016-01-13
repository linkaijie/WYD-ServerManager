package com.zhwyd.server.bean.vo;
import com.zhwyd.server.common.constant.CommanConstant;
public class BatchOpearVo {
    private int     id;
    private String  areaId;
    private String  name;
    private Boolean status;
    private String  color;  // 颜色：1绿色，2红色
    private String  remark;
    private int     num = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.color = CommanConstant.COLOR_GREEN;
        this.remark = remark;
    }

    public void isNotExist(String name) {
        this.status = false;
        this.remark = name + "不存在";
        this.color = CommanConstant.COLOR_RED;
    }

    public void isNotDeploy(String name) {
        this.status = false;
        this.remark = name + "未部署";
        this.color = CommanConstant.COLOR_RED;
    }

    public void isSUCCESS(String remark) {
        this.status = true;
        this.remark = remark;
        this.color = CommanConstant.COLOR_GREEN;
    }

    public void isFail(String remark) {
        if (this.remark != null && this.status != null && !this.status) {
            this.remark += "\n" + remark;
        } else {
            this.remark = remark;
        }
        this.status = false;
        this.color = CommanConstant.COLOR_RED;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void updateNum() {
        synchronized (this) {
            this.num--;
            if(this.num == 0){
                this.notify();
            }
        }
    }
}
