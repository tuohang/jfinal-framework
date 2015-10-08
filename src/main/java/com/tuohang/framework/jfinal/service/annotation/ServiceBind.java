package com.tuohang.framework.jfinal.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tuohang.framework.jfinal.service.BaseService;

/**
 * 自定义ServiceBind注解，在service接口上绑定对应实现类
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ServiceBind {
	@SuppressWarnings("rawtypes")
	Class<? extends BaseService> value();
}
