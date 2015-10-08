package com.tuohang.framework.jfinal.service.tx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * jfinal提供的事务是通过拦截器扩展的，而当项目中如果有业务层代码，想把事务控制在业务层边界时，如让定时器来调用，就不会使用控制层，
 * 也就没法使用事务拦截器
 * (论坛上有过这个问题讨论)，那我们可以通过jdk代理或cglib代理来实现我们需要的功能，以cglib为例(jdk代理是需要接口的)。<br>
 * 在业务层利用jfinal比较推荐的方法，使用单例类，不用IOC容器注入，生成事务代理类。<br>
 * 在业务层中添加方法，采用Db类和Model形式都支持，方法名上加上@TxBind注解才支持事务。
 * 
 * @author j67kryuy
 * @date 2015年1月19日
 * @version 1.0
 */
public class TxBindProxy implements MethodInterceptor {

	public final static Logger log = Logger.getLogger(TxBindProxy.class);

	private Object target = null;

	private TxBindProxy(Object target) {
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> targetClass) {
		if (null == targetClass)
			return null;
		Object proxy = null;
		try {
			T t = targetClass.newInstance();
			Enhancer en = new Enhancer();
			en.setSuperclass(targetClass);
			en.setCallback(new TxBindProxy(t));
			proxy = en.create();
			log.info("创建代理：" + targetClass.getName());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return (T) proxy;
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		Object result = null;
		if (method.isAnnotationPresent(TxBind.class)) {
			TxBindInvoke invoke = new TxBindInvoke(this.target, method, args);
			Db.tx(invoke);
			result = invoke.getResult();
		} else {
			result = method.invoke(this.target, args);
		}
		return result;
	}

	private class TxBindInvoke implements IAtom {
		private Object target = null;
		private Method method = null;
		private Object[] args = null;
		private Object result = null;

		public TxBindInvoke(Object target, Method method, Object[] args) {
			this.target = target;
			this.method = method;
			this.args = args;
		}

		@Override
		public boolean run() throws SQLException {
			boolean flag = false;
			try {
				this.result = this.method.invoke(this.target, args);
				flag = true;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return flag;
		}

		public Object getResult() {
			return this.result;
		}
	}
}
