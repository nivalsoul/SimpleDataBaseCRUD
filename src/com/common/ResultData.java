package com.common;

/**
 * <b>Ajax请求结果类</b>
 * <br>用来表示一般情况下的返回结果，具体参数根据接口定义进行设置
 * @author wlxu
 *
 */
public class ResultData {
	//返回代码：0,1,2等
	private int code;
	//提示信息，针对code的说明
	private String info;
	//具体的结果数据
	private Object data;
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
