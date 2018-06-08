package com.sdg.cmdb.service;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.esxi.EsxiHostDO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by liangjian on 2017/8/8.
 */
@Service
public class CacheEsxiHostService {

    private static final String perfKey = "esxiHost:";

    @Resource
    private RedisTemplate redisTemplate;

    public static final String esxi_cacha_status = "esxiHostCache:status";

    /**
     * 插入指定key-value    有效期无限制
     */
    public void insert(EsxiHostDO esxiHostDO) {
        String sp = JSON.toJSONString(esxiHostDO);
        redisTemplate.opsForValue().set(perfKey + esxiHostDO.getVmName(), sp);
    }

    /**
     * 查询指定key
     */
    public EsxiHostDO queryEsxiHost(String vmName) {
        Object value = redisTemplate.opsForValue().get(perfKey + vmName);
        if (value == null) {
            return null;
        } else {
            String obj = value.toString();
            EsxiHostDO esxiHostDO = JSON.parseObject(obj, EsxiHostDO.class);
            return esxiHostDO;
        }
    }

    public void set( String value) {
        redisTemplate.opsForValue().set(esxi_cacha_status, value);
    }

    public String getKeyByString() {
        Object value = redisTemplate.opsForValue().get(esxi_cacha_status);
        if (value == null) return null;
        String obj = value.toString();
        return obj;
    }

    /**
     * 插入指定key-value    有效期20分钟
     */
    private void demoInsertForTime() {
        redisTemplate.opsForValue().set("key", "value", 20, TimeUnit.MINUTES);
    }

    /**
     * 删除指定key
     */
    private void demoDel() {
        redisTemplate.delete("key");
    }
}
