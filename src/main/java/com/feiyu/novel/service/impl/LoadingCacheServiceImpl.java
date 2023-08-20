package com.feiyu.novel.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.feiyu.novel.service.LoadingCacheService;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;


@Service
public class LoadingCacheServiceImpl implements LoadingCacheService {

	@Override
	public boolean tryAcquire() {
		return rateLimiter.tryAcquire();

	}

	// 每秒控制5个许可
	RateLimiter rateLimiter = RateLimiter.create(5.0);

	@Override
	public RateLimiter getIPLimiter(String ipAddr) throws ExecutionException {
		return ipRequestCaches.get(ipAddr);
	}

	LoadingCache<String, RateLimiter> ipRequestCaches = CacheBuilder.newBuilder()
			.maximumSize(1000)// 设置缓存个数
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build(new CacheLoader<String, RateLimiter>() {
				@Override
				public RateLimiter load(String s) throws Exception {
					return RateLimiter.create(0.1);// 新的IP初始化 (限流每秒0.1个令牌响应,即10s一个令牌)
				}
			});

}