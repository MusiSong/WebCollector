package com.Music.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * redis操作工具类
 * @author songshixin
 *
 */
public class RedisCacheUtils {
	
	private static JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	public static synchronized  Jedis getJedis(){
		try{
			if (jedisPool!=null) {
				return jedisPool.getResource();
			}else{
				return null;
			}
		}catch (Exception e) {
			return null;
		}
	}
	/**
	 * 添加key
	 * @param key
	 * @param value
	 * @param timestamp 超时时间（秒）
	 */
	public static void set(String key,String value,long timestamp){
		Jedis jedis=null;
		try{
		jedis=getJedis();
		String code=jedis.set(key, value);
		//设置超时时间
		jedis.expireAt(key, timestamp);
		if (code.equals("ok")) {
			Logger.getlogger("RedisCacheUtils").info("redis缓存成功,key={}"+key);
		}else{
			Logger.getlogger("RedisCacheUtils").error("redis缓存失败,key={}"+key);
		}
		}catch (Exception e) {
			throw e;
		}finally{
			if (jedis!=null) {
				jedis.disconnect();
				jedisPool.returnResource(jedis);
			}
		}
	}
	/**
	 * 可以删除多个key
	 * @param key
	 */
	public static void del(String... key){
		Jedis jedis=getJedis();
		jedis.del(key);
		jedisPool.returnResource(jedis);
	}
	
	/** 
     * 判断key是否存在 
     * 
     * @param String 
     *            key 
     * @return boolean 
     * */  
    public static boolean exists(String key) {  
        Jedis jedis=getJedis();   
        boolean exis = jedis.exists(key);  
        jedisPool.returnResource(jedis);
        return exis;  
    }  
    
    /** 
     * 清空所有key 
     */  
    public static String flushAll() {  
        Jedis jedis = getJedis();  
        String stata = jedis.flushAll();  
        jedisPool.returnResource(jedis); 
        return stata;  
    }  
    /**
     * 获取key
     * @param key
     * @return
     */
    public static String get(String key){
    	Jedis jedis=getJedis();
    	String result=jedis.get(key);
    	jedisPool.returnResource(jedis); 
    	return result;
    }
	 
}
