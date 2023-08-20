package com.feiyu.novel.core.aspect;

import com.feiyu.novel.core.common.resp.RestResp;
import com.feiyu.novel.core.util.IPUtil;
import com.feiyu.novel.service.LoadingCacheService;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope
@Aspect
public class IPLimitAspect {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LoadingCacheService loadingCacheService;

	@Pointcut("@annotation(com.feiyu.novel.core.annotation.IPLimit)")
	public void ipLimit() {

	}

	@Around("ipLimit()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object obj = null;
		String ipAddr = IPUtil.getIpAddr(request);
		RateLimiter limiter = loadingCacheService.getIPLimiter(ipAddr);
		if (limiter.tryAcquire()) {
			// 获得令牌（不限制访问）
			obj = joinPoint.proceed();
		} else {
			// 未获得令牌（限制访问）
			obj = RestResp.ok("error");
		}
		return obj;
	}

}
