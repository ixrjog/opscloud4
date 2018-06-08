package com.sdg.cmdb.plugin.cache;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerPerfVO;
import com.sdg.cmdb.domain.server.ServerStatisticsDO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by zxxiao on 2017/3/1.
 */
@Service
public class CacheZabbixService {

    private static final String perfKey = "serverPerf:";

    private static final String perfStatisticsKey = "perfStatistics:";

    private static final String zabbixAuthKey = "zabbixAuth";

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * set zabbix auth token
     * @param auth
     */
    public void insertZabbixAuth(String auth) {
        redisTemplate.opsForValue().set(zabbixAuthKey, auth);
    }

    /**
     * get zabbix auth token
     * @return
     */
    public String getZabbixAuth() {
        Object value = redisTemplate.opsForValue().get(zabbixAuthKey);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    /**
     * 插入指定key-value    有效期无限制
     */
    public void insert(ServerPerfVO serverPerfVO) {
        String sp = JSON.toJSONString(serverPerfVO);
        redisTemplate.opsForValue().set(perfKey + serverPerfVO.getId(), sp);
    }


    public void insert(ServerStatisticsDO serverStatisticsDO) {
        String serverStatistics = JSON.toJSONString(serverStatisticsDO);
        redisTemplate.opsForValue().set(perfStatisticsKey, serverStatistics);
    }

    public ServerStatisticsDO queryServerStatistics() {
        Object value = redisTemplate.opsForValue().get(perfStatisticsKey);
        if (value == null) {
            return null;
        } else {
            String obj = value.toString();
            ServerStatisticsDO serverStatisticsDO = JSON.parseObject(obj, ServerStatisticsDO.class);
            return serverStatisticsDO;
        }
    }

    /**
     * 查询指定key
     */
    public ServerPerfVO queryServerPerfVO(long serverId) {
        Object value = redisTemplate.opsForValue().get(perfKey + serverId);
        if (value == null) {
            return null;
        } else {
            String obj = value.toString();
            ServerPerfVO serverPerfVO = JSON.parseObject(obj, ServerPerfVO.class);
            return serverPerfVO;
        }
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getKeyByString(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        String obj = value.toString();
        return obj;
    }

    /**
     * 插入指定key-value    有效期20分钟
     */
    public void demoInsertForTime() {
        redisTemplate.opsForValue().set("key", "value", 20, TimeUnit.MINUTES);
    }

    /**
     * 删除指定key
     */
    public void demoDel() {
        redisTemplate.delete("key");
    }
}
