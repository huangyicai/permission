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
	WEIGHT_NOT_WRITE(10010,"首重或续重未填写"),
	PHONE_ERROR(10011,"号码错误,请重新输入"),
	VERIFY_FAIL(10012, "验证码错误,请重新尝试"),
	PASSWORD_ATYPISM(10013, "两次密码不一致！"),
	PASSWORD_INCORRECT(10014, "密码不正确！"),
	//oss相关
	FILE_ERRO(100000,"文件不合法"),

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
