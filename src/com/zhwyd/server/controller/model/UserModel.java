package com.zhwyd.server.controller.model;

public class UserModel extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
