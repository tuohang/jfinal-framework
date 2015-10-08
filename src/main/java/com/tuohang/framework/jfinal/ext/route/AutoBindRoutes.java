package com.tuohang.framework.jfinal.ext.route;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;

/**
 * 将Controller中的ControllerBind注解值加入到Routes配置中
 * 
 * @author 佚名
 * @date 2015年10月4日
 * @version 1.0
 */
public class AutoBindRoutes extends Routes {

	private boolean autoScan = true;

	private List<Class<? extends Controller>> excludeClasses = Lists
			.newArrayList();

	private boolean includeAllJarsInLib;

	private List<String> includeJars = Lists.newArrayList();

	protected final Logger logger = Logger.getLogger(getClass());

	private String suffix = "Controller";

	public AutoBindRoutes autoScan(boolean autoScan) {
		this.autoScan = autoScan;
		return this;
	}

	public AutoBindRoutes addExcludeClasses(
			Class<? extends Controller>... clazzes) {
		if (clazzes != null) {
			for (Class<? extends Controller> clazz : clazzes) {
				excludeClasses.add(clazz);
			}
		}
		return this;
	}

	public AutoBindRoutes addExcludeClasses(
			List<Class<? extends Controller>> clazzes) {
		excludeClasses.addAll(clazzes);
		return this;
	}

	public AutoBindRoutes addJars(String... jars) {
		if (jars != null) {
			for (String jar : jars) {
				includeJars.add(jar);
			}
		}
		return this;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void config() {
		// 寻找所有继承了Controller的class
		List<Class<? extends Controller>> controllerClasses = ClassSearcher
				.of(Controller.class).includeAllJarsInLib(includeAllJarsInLib)
				.injars(includeJars).search();
		ControllerBind controllerBind = null;
		for (Class controller : controllerClasses) {
			if (excludeClasses.contains(controller)) {
				continue;
			}
			controllerBind = (ControllerBind) controller
					.getAnnotation(ControllerBind.class);
			if (controllerBind == null) {
				// 如果不是自动绑定
				if (!autoScan) {
					continue;
				}
				this.add(controllerKey(controller), controller);
				logger.debug("routes.add(" + controllerKey(controller) + ", "
						+ controller.getName() + ")");
			} else if (StrKit.isBlank(controllerBind.viewPath())) {
				this.add(controllerBind.value(), controller);
				logger.debug("routes.add(" + controllerBind.value()
						+ ", " + controller.getName() + ")");
			} else {
				this.add(controllerBind.value(), controller,
						controllerBind.viewPath());
				logger.debug("routes.add(" + controllerBind.value()
						+ ", " + controller + "," + controllerBind.viewPath()
						+ ")");
			}
		}
	}

	/**
	 * 获取该controller种的controllerBind注解中的路径值
	 * 
	 * @param clazz
	 * @return
	 */
	private String controllerKey(Class<Controller> clazz) {
		Preconditions
				.checkArgument(
						clazz.getSimpleName().endsWith(suffix),
						clazz.getName()
								+ " is not annotated with @ControllerBind and not end with "
								+ suffix);
		String controllerKey = "/"
				+ StrKit.firstCharToLowerCase(clazz.getSimpleName());
		controllerKey = controllerKey.substring(0,
				controllerKey.indexOf(suffix));
		return controllerKey;
	}

	public AutoBindRoutes includeAllJarsInLib(boolean includeAllJarsInLib) {
		this.includeAllJarsInLib = includeAllJarsInLib;
		return this;
	}

	public AutoBindRoutes suffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

}
