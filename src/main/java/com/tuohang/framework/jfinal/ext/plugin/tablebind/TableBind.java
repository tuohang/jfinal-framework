package com.tuohang.framework.jfinal.ext.plugin.tablebind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果都为默认，tableName为对应的model名小写，pkName默认为id
 * 
 * @author Lims
 * @date 2015年10月4日
 * @version 1.0
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface TableBind {
	/**
	 * 对应的数据库表名：tableName
	 */
	String value() default "";

	/**
	 * 主键名
	 */
	String pkName() default "";

	String configName() default "";
}
