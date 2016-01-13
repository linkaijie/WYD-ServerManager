package com.zhwyd.server.bean.vo;
public class ThreadOpearVo {
    private int     num    = 0;
    private boolean result = true;
    private String  remark;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getResult() {
        synchronized(this){
            return result;
        }
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void addNum() {
        synchronized(this){
            this.num++;
        }
    }

    public void isFile(String remark) {
        synchronized(this){
            this.remark = remark;
            this.result = false;
        }
    }
}
