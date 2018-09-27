package com.mmall.model.Response;

public enum InfoEnums {
	SUCCESS(0,"success"),
	ERROR(10001,"系统异常"),
	AUTHORIZATION(10002,"已授权"),
	PARAM_NOT(10003,"参数不正确！"),
	USERNAME_EXISTENCE(10004,"用户名已存在！"),
	USER_NOT_EXISTENCE(10005,"账号不存在"),
	USER_NOT_FROZEN(10006,"账号被冻结"),
	SignIn(10008,"请先登录！"),
	UNAUTHORIZATION(10009,"暂无无权限"),

	;

	private Integer code;
    private String info;
    
	
	private InfoEnums(Integer code, String info) {
		this.code = code;
		this.info = info;
	}


	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}


	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
