package com.tuohang.framework.jfinal.controller.interactive;

import java.io.Serializable;

/**
 * 向前台返回的结果json-bean（base）
 * 
 * @author Lims
 * @date 2015年10月5日
 * @version 1.0
 */
public class BaseResult implements Serializable {

	private static final long serialVersionUID = -4379245091714015914L;

	/**
	 * 结果状态码：200表示成功，404表示失败，302表示警告
	 */
	private int stateCode;

	/**
	 * 提示信息
	 */
	private String promptInfo;

	/**
	 * 返回内容
	 */
	private Object content;

	/**
	 * 结果类型，分为成功，失败，警告
	 */
	private String type;

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public String getPromptInfo() {
		return promptInfo;
	}

	public void setPromptInfo(String promptInfo) {
		this.promptInfo = promptInfo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
