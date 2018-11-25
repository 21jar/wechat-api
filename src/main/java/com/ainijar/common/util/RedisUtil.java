package com.ainijar.common.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RedisUtil {

	@Autowired
	private RedisTemplate<String, ?> redisTemplate;

	public List<Object> getRange(final String key) {
		Assert.hasText(key, "Key is not empty.");

		List<Object> result = redisTemplate.execute(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				List<byte[]> res = connection.lRange(serializer.serialize(key), 0,
						connection.lLen(serializer.serialize(key)));
				List<Object> objList = new ArrayList<Object>();
				for (byte[] byt : res) {
					objList.add(serializer.deserialize(byt));
				}
				return objList;
			}
		});
		return result;
	}

	public Set<String> getKeySet(final String key) {
		Assert.hasText(key, "Key is not empty.");

		return redisTemplate.keys(key);
	}

	public long rpush(final String key, Object obj) {
		Assert.hasText(key, "Key is not empty.");

		final String value = JSONObject.toJSONString(obj);
		long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
				return count;
			}
		});
		return result;
	}

	public Object lpop(final String key) {
		Assert.hasText(key, "Key is not empty.");

		Object result = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] res = connection.lPop(serializer.serialize(key));
				return serializer.deserialize(res);
			}
		});
		return result;
	}

	public boolean exists(final String key) {
		Assert.hasText(key, "Key is not empty.");

		return redisTemplate.hasKey(key);
	}

	public long incr(final String key) {
		Assert.hasText(key, "Key is not empty.");

		long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Long res = connection.incr(serializer.serialize(key));
				return res;
			}
		});
		return result;
	}

	public long decr(final String key) {
		Assert.hasText(key, "Key is not empty.");

		long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Long res = connection.decr(serializer.serialize(key));
				return res;
			}
		});
		return result;
	}

	public long decrBy(final String key, final Long by) {
		Assert.hasText(key, "Key is not empty.");

		long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Long res = connection.decrBy(serializer.serialize(key), by);
				return res;
			}
		});
		return result;
	}

	public Object get(final String key) {
		Assert.hasText(key, "Key is not empty.");
		Object result = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] value = connection.get(serializer.serialize(key));
				return serializer.deserialize(value);
			}
		});
		return result;
	}

	public void set(final String key, final String value) {
		Assert.hasText(key, "Key is not empty.");
		redisTemplate.execute(new RedisCallback<Long>() {

			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.set(serializer.serialize(key), serializer.serialize(value));
				return null;
			}

		});
	}

	public void delByCodePattern(final String key) {
		Assert.hasText(key, "Key is not empty.");

		Set<String> keys = redisTemplate.keys(key);
		for (String k : keys) {
			redisTemplate.delete(k);
		}
	}

	public void deleteOne(final String key) {
		redisTemplate.delete(key);
	}

	public boolean isExistKey(final String allkey, final String key) {

		Set<String> keys = redisTemplate.keys(allkey);
		for (String k : keys) {
			if (k.equals(key)) {
				return true;
			}
		}
		return false;
	}

	public void lSet(final String key, final long index, final String setValue) {
		Assert.hasText(key, "Key is not empty.");

		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				connection.lSet(serializer.serialize(key), index, serializer.serialize(setValue));
				return null;
			}
		});
	}

	public long lRem(final String key, final String value) {
		Assert.hasText(key, "Key is not empty.");

		long result = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				Long res = connection.lRem(serializer.serialize(key), 0, serializer.serialize(value));
				return res;
			}
		});
		return result;
	}

	public void setEx(final String key, final String value, final Long day) {
		Assert.hasText(key, "key is not empty");
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection arg0) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				arg0.setEx(serializer.serialize(key), day, serializer.serialize(value));
				return null;
			}
		});
	}
}