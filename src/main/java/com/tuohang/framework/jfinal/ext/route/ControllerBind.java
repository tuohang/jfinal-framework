package com.tuohang.framework.jfinal.ext.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定controller
 * 
 * @author 佚名
 * @date 2015年10月4日
 * @version 1.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ControllerBind {

	/**
	 * 表示ControllerKey
	 * 
	 * @return
	 */
	String value();

	String viewPath() default "";
}
