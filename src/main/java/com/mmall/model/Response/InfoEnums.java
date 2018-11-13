package com.mmall.model.Response;

public enum InfoEnums {
	SUCCESS(0,"success"),
	ERROR(10001,"请稍后重试！"),
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
	DATA_IS_NULL(10010,"没有数据"),

	KEY_EXISTENCE(10017,"已有相同关键字定价组！"),
	PLEASE_ADD_PRICING(10018,"请添加定价！"),
	ADD_PAYMENT_INSTITUTION(10015,"请先为该分支添加付款机构！"),

	//oss相关
	FILE_ERRO(100000,"文件不合法"),


	//定价相关
	PROCING_IS_NULL(110001,"定价组参数不齐"),
	COST_IS_NULL(110002,"成本组参数不齐"),

	//账单相关
	BILL_IS_NULL(120002,"账单不存在"),
	SEND_FAILURE(120003,"发送失败，清确认是否定价，或者已经发送过"),
	TOATL_EXISTS(120004,"账单已存在"),
	TOATL_IS_PRICING(120005,"订单已经发送"),
	NOT_UPDATE(120006,"账单已发送给客户，无法修改"),
	NO_PAYMENT(120007,"没有付款"),

	//导入用户相关
	TABLE_FORMAT_ERROR(130000,"表格错误"),
	TABLE_FORMAT_NULL(130001,"表格数据不能为空"),
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
