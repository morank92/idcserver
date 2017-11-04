package com.baoidc.idcserver.core;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
@Component
public class RedisUtil {
	
	private JedisPool jedisPool;
	
	public RedisUtil() {
	}
	public RedisUtil(JedisPool jedisPool){
		this.jedisPool = jedisPool;
	}
	
	//String 类型的获取
	public String get(String key){
		
		
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
		return value;
	}
	/*
	 * String 类型的存储
	 * 参数
	 * 	  value：不需要覆盖原有值   输入 null
	 * 	  seconds 不需要设置生命周期  输入  0
	 */
	public void set(String key,String value,int seconds){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if(value!=null){
				jedis.set(key, value);
			}
			if(seconds>0){
				jedis.expire(key, seconds);
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}
	
	//获取一个redis链接
	public Jedis getJedis(){
		return jedisPool.getResource();
	}
	
}
