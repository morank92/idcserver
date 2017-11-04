package com.baoidc.idcserver.core;

public enum ErrorCode {
	//234  王华平   
	//567  蒋勇杰
	//189 尚康康
	
	NOT_USER("101","用户不存在"),
	USER_PASSWORD_WRONG("102","密码错误"),
	ADD_NEWUSER_SUCCESS("103","新增用户成功"),
	UPDATE_USERINFO_SUCCESS("104","用户信息更新成功"),
	USER_LOGIN_SUCCESS("105","登录成功"),
	ERROR_CODE("106","验证码错误"),
	OTHER_ERR("111","未知错误"),
	NOT_LOGIN("222","未登录"),
	CODECHECK_WRONG("107","验证码错误"),
	CODECHECK_EXPIRED("108","验证码无效"),
	PHONECODE_EXPIRED("109","手机验证码无效"),
	PHONECODE_WRONG("110","手机验证码错误"),
	BUSINESS_DEAL_SUCCESS("000","业务处理成功"),
	EXCEPTION("500","网络异常"),
	NOTLOGIN("404","登陆过期，重新登陆"),
	PAY_SUCCESS("300","支付成功"),
	BALANCE_NOT_ENOUGH("301","余额不足，去充值之后再支付"),
	PAY_FAIL("302","支付失败"),
	DEL_ORDER_FAIL("303","删除订单成功"),
	CHARGE_SUCCESS("200","充值成功"),
	CHARGE_FAIL("201","充值失败"),
	ENCASHMENT_SUCCESS("202","提现成功"),
	
	ADD_MANAGEUSER_SUCCESS("400","添加系统管理员成功"),
	DEL_MANAGEUSER_SUCCESS("401","删除系统管理员成功"),
	MODIFY_MANAGEUSER_SUCCESS("402","修改系统管理员成功"),
	ADD_ARTICLETYPE_SUCCESS("403","添加文章类别成功"),
	DEL_ARTICLETYPE_SUCCESS("405","删除文章类别成功"),
	ADD_ARTICLECONTENT_SUCCESS("406","添加文章成功"),
	DEL_ARTICLECONTENT_SUCCESS("407","删除文章成功"),
	EDIT_ARTICLECONTENT_SUCCESS("408","修改文章成功"),
	NO_AUTH("409","您目前没有权限访问相关资源"),
	GETOUT("410","该用户已在其他地方登陆，您已被迫下线"),
	
	START_SUCCESS_IPMI("601","开机成功"),
	END_SUCCESS_IPMI("602","关机成功"),
	
	EVENT_TRUE("701","true"),
	EVENT_FALSE("702","false"),
	
	GET_FALSE("703","未获取到信息"),

	DEAL_REORDER_TRUE("704","当前的续费订单，处理成功"),
	DEAL_REORDER_FALSE("705","当前订单已处理，无需处理"),
	
	SUBMIT_RENEWCONF_FALSE("703","选择的设备中已经存在自动续费"),
	
	
	SUBMIT_ORDER_TRUE("703","订单提交成功"),
	SUBMIT_ORDER_FALSE("704","订单提交失败"),
	
	START_FAILD_IPMI("501","机器目前处于开机状态"),
	END_FAILD_IPMI("502","机器目前处于开机状态");
	
	private String code;
	private String msg;
	
	ErrorCode(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("{\"");
		strBuilder.append("code\":\"");
		strBuilder.append(this.code);
		strBuilder.append("\",");
		strBuilder.append("\"msg\":\"");
		strBuilder.append(this.msg);
		strBuilder.append("\"}");
		return strBuilder.toString();
	}
}
