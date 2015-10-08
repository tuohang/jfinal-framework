package com.tuohang.framework.jfinal.exception;

/**
 * 自定义ServiceBind注解异常
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.0
 */
public class ServiceBindAnnotationException extends Exception {

	private static final long serialVersionUID = -8744272324260278380L;

	public ServiceBindAnnotationException() {
		super();
	}

	public ServiceBindAnnotationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceBindAnnotationException(String message) {
		super(message);
	}

	public ServiceBindAnnotationException(Throwable cause) {
		super(cause);
	}

}
