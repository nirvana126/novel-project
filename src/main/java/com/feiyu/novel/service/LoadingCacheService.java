package com.feiyu.novel.service;

import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.RateLimiter;

public interface LoadingCacheService {

	boolean tryAcquire();

	RateLimiter getIPLimiter(String ipAddr) throws ExecutionException;

}
