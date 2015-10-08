package com.tuohang.framework.jfinal.service.tx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事务注解
 * 
 * @author j67kryuy
 * @date 2015年1月19日
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface TxBind {
}
