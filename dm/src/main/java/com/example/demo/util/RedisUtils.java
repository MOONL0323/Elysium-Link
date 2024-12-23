package com.example.demo.util;

import com.example.demo.countService.entity.UserInfoVo;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Redis工具类
 */
@Component
public class RedisUtils {

    //*****************************************String相关操作*****************************************/

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置指定 key 的值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    //*****************************************Set相关操作*****************************************/


    /**
     * 向集合添加元素
     *
     * @param key
     * @param values
     * @return
     */
    public Long sAdd(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取集合的成员数
     *
     * @param key
     * @return
     */
    public Long sCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 获取集合的所有成员
     *
     * @param key
     * @return
     */
    public Set<String> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 从集合中移除一个或多个成员
     *
     * @param key
     * @param values
     * @return
     */
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }


    //*****************************************ZSet相关操作*****************************************/

    /**
     * 添加元素到有序集合
     *
     * @param key
     * @param value
     * @param score
     */
    public void zAdd(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加元素到有序集合
     *
     * @param key
     * @param value
     * @param score
     */

    /**
     * 获取有序集合的成员数
     *
     * @param key
     * @return
     */
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 获取有序集合指定成员的分数
     *
     * @param key
     * @param value
     * @return
     */
    public Double zScore(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 从有序集合中移除一个或多个成员
     *
     * @param key
     * @param values
     * @return
     */
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 获取有序集合指定范围的成员
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取有序集合指定范围的成员和分数
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    /**
     * 获取有序集合中的所有成员的member，按照分数从大到小排序
     * @param key
     * @return List<Long>
     */
    public List<Long> zRevRange(String key) {
        Set<String> members = redisTemplate.opsForZSet().reverseRange(key, 0, -1);
        List<Long> result = new ArrayList<>();
        if (members != null) {
            for (String member : members) {
                result.add(Long.valueOf(member));
            }
        }
        return result;
    }

    //*****************************************Hash相关操作*****************************************/

    /**
     * 获取哈希表中指定字段的int值
     *
     * @param key
     * @param field
     * @return Integer
     */
    public Integer hGet(String key, String field) {
        return (Integer) redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 增加哈希表中指定字段的int值
     * @param key
     * @param field
     * @param n
     * @return boolean
     */
    public boolean hIncr(String key, String field, Integer n) {
        return redisTemplate.opsForHash().increment(key, field, n) != null;
    }

    /**
     * 批量设置哈希表中指定字段的值
     *
     * @param key
     * @param fieldsAndValues 可变参数，依次为 field1, value1, field2, value2, ...
     * @return boolean
     */
    public boolean hMSet(String key, Object... fieldsAndValues) {
        if (fieldsAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("fieldsAndValues length must be even");
        }
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            String field = (String) fieldsAndValues[i];
            Object value = fieldsAndValues[i + 1];
            redisTemplate.opsForHash().put(key, field, value);
        }
        return true;
    }

    /**
     * 设置哈希表中所有字段的值
     * @param key
     * @param userInfoVo
     * @return boolean
     */
    public boolean hMSet(String key, UserInfoVo userInfoVo) {
        return hMSet(key, "followingCount", userInfoVo.getFollowingCount(),
                "followerCount", userInfoVo.getFollowerCount(),
                "articleCount", userInfoVo.getArticleCount(),
                "userHeat", userInfoVo.getUserHeat());
    }


    /**
     * 使用HGHETALL获取哈希中所有值 hGetALL
     * @param key
     * @return Map<Object, Object>
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    //*****************************************Lua脚本相关操作*****************************************/

    public Object executeLuaScript(String scriptPath, String key, String field, String increment) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(loadScript(scriptPath));
        redisScript.setResultType(Long.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key), field, increment);
    }

    private String loadScript(String scriptPath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scriptPath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Script file not found: " + scriptPath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load script file: " + scriptPath, e);
        }
    }



}
