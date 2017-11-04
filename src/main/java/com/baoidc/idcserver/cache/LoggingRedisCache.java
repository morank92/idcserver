package com.baoidc.idcserver.cache;

import org.apache.ibatis.cache.decorators.LoggingCache;

public class LoggingRedisCache extends LoggingCache{
	
	public LoggingRedisCache(String id) {
		super(new RedisCache(id));
		System.out.println("in method loggingRedisCache");
	}

}
