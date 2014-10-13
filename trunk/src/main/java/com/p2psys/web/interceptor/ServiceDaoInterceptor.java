package com.p2psys.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * 
 * 监控service和dao性能的拦截器
 * 
 
 * @version 1.0
 * @since 2013-11-5
 */
public class ServiceDaoInterceptor implements MethodInterceptor {
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(ServiceDaoInterceptor.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = invocation.proceed();
		long time = System.currentTimeMillis() - start;
		if (time >= 500) {
			logger.warn(String.format("class[%s] method[%s] time[%d]", invocation.getMethod().getDeclaringClass(),
					invocation.getMethod().getName(), time));
		}
//		if (result != null && result instanceof List) {
//			List dataList = (List) result;
//			if (dataList.size() >= 50) {
//				logger.warn(String.format("class[%s] method[%s] resultsize[%d]", invocation.getMethod()
//						.getDeclaringClass(), invocation.getMethod().getName(), dataList.size()));
//			}
//		}
		return result;
	}

}
