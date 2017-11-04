package com.baoidc.idcserver.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import redis.clients.jedis.Jedis;

import com.baoidc.idcserver.core.GetBeanUtil;
import com.baoidc.idcserver.core.RedisUtil;
public class RedisCache implements Cache {
	
	private String id;
	private Jedis jedis = null;
	private ReadWriteLock lock = null;
	private RedisUtil redisUtil;
	
	public RedisCache(String id) {
		super();
		this.id = id;
		redisUtil = (RedisUtil) GetBeanUtil.getBean("redisUtil");
		this.jedis = redisUtil.getJedis();
		jedis.select(15);
		lock = new ReentrantReadWriteLock();
	}

	public void clear() {
		jedis.flushDB();
		
	}

	public String getId() {
		return id;
	}

	public Object getObject(Object arg0) {
		byte[] key =SerializerUtils.serialize(arg0);
		byte[] bs = jedis.get(key);
		Object object = SerializerUtils.unserialize(bs);
		return object;
	}

	public ReadWriteLock getReadWriteLock() {
		return lock;
	}

	public int getSize() {
		Long long1 = jedis.dbSize();
		return long1.intValue();
	}

	public void putObject(Object arg0, Object arg1) {
		byte[] key = SerializerUtils.serialize(arg0);
		byte[] value = SerializerUtils.serialize(arg1);
		jedis.set(key, value);
	}

	public Object removeObject(Object arg0) {
		byte[] key = SerializerUtils.serialize(arg0);
		Long long1 = jedis.del(key);
		return long1;
	}

}
