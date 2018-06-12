package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.dao.cmdb.ConfigCenterDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.GetwayItemEnum;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/5/26.
 */
@Service
public class ConfigCenterServiceImpl implements ConfigCenterService {

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    public final static String DEFAULT_ENV = "online";

    private static final String CONFIG_CENTER_ITEMGROUP_KEY = "ConfigCenterItemGroup:";

    private static final String CONFIG_CENTER_ITEM_KEY = "ConfigCenterItem:";

    @Resource
    private ConfigCenterDao configCenterDao;

    @Resource
    private ConfigService configService;


    /**
     * 插入指定key-value    有效期无限制
     */
    public void insert(HashMap<String, String> map, String name, String env) {
        String sp = JSON.toJSONString(map);
    }

    /**
     * 查询指定的itemGroup配置，并缓存
     */
    @Override
    public HashMap<String, String> queryItemGroup(String itemGroup, String env) {
        List<ConfigCenterDO> configList = configCenterDao.queryConfigCenterByItemGroup(itemGroup, env);
        HashMap<String, String> map = new HashMap<String, String>();
        if (configList.size() == 0) return queryItemGroup(itemGroup, DEFAULT_ENV);
        for (ConfigCenterDO configCenterDO : configList) {
            map.put(configCenterDO.getItem(), configCenterDO.getValue());
        }
        return map;

    }

    @Override
    public HashMap<String, String> getItemGroup(String itemGroup) {
        if (StringUtils.isEmpty(invokeEnv)) {
            return queryItemGroup(itemGroup, DEFAULT_ENV);
        }
        HashMap<String, String> map = queryItemGroup(itemGroup, invokeEnv);
        //System.err.println(itemGroup + invokeEnv);

        // 若没匹配到当你env配置，则读取DEFAULT_ENV配置
        if (map.isEmpty() && !invokeEnv.equalsIgnoreCase(DEFAULT_ENV)) {
            return queryItemGroup(itemGroup, DEFAULT_ENV);
        }
        return map;
    }

    @Override
    public String getItem(String itemGroup, String item) {
        HashMap<String, String> map = getItemGroup(itemGroup);
        if (map.containsKey(item)) {
            return map.get(item);
        } else {
            return "";
        }
    }

    @Override
    public TableVO<List<ConfigCenterDO>> getConfigCenterPage(String item, String itemGroup, String env, int page, int length) {
        long size = configCenterDao.getConfigCenterSize(item, itemGroup, env);
        List<ConfigCenterDO> configCenterDOList = configCenterDao.getConfigCenterPage(item, itemGroup, env, page * length, length);
        return new TableVO<>(size, configCenterDOList);
    }

    @Override
    public HashMap<String, ConfigCenterDO> getConfigCenterItemGroup(String itemGroup, String env) {
        //System.err.println(invokeEnv);
        if (StringUtils.isEmpty(env) && !StringUtils.isEmpty(invokeEnv)) {
            env = invokeEnv;
        } else {
            env = DEFAULT_ENV;
        }
        List<ConfigCenterDO> list = configCenterDao.getConfigCenterPage("", itemGroup, env, 0, 100);
        HashMap<String, ConfigCenterDO> map = new HashMap<>();
        for (ConfigCenterDO config : list)
            map.put(config.getItem(), config);
        return map;
    }


    @Override
    public BusinessWrapper<Boolean> refreshCache(String itemGroup) {
        try {
            //redisTemplate.delete(CONFIG_CENTER_ITEMGROUP_KEY + itemGroup + ":" + invokeEnv);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public boolean refreshCache(String itemGroup, String env) {
        try {
            //redisTemplate.delete(CONFIG_CENTER_ITEMGROUP_KEY + itemGroup + ":" + env);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 清理缓存
     *
     * @param configCenterDO
     * @return
     */
    private void updateCache(ConfigCenterDO configCenterDO) {
        refreshCache(configCenterDO.getItemGroup(), invokeEnv);
        if (!invokeEnv.equalsIgnoreCase(DEFAULT_ENV))
            refreshCache(configCenterDO.getItemGroup(), DEFAULT_ENV);
    }


    @Override
    public BusinessWrapper<Boolean> saveConfigCenter(ConfigCenterDO configCenterDO) {
        if (saveConfigCenterDO(configCenterDO)) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    private boolean saveConfigCenterDO(ConfigCenterDO configCenterDO) {
        try {
            if (configCenterDO.getId() == 0) {
                configCenterDao.addConfigCenter(configCenterDO);
            } else {
                configCenterDao.updateConfigCenter(configCenterDO);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public BusinessWrapper<Boolean> updateConfigCenter(HashMap<String, ConfigCenterDO> map) {
        try {
            for (String key : map.keySet()) {
                ConfigCenterDO configCenterDO = map.get(key);
                if (configCenterDO.getItemGroup().equalsIgnoreCase(ConfigCenterItemGroupEnum.GETWAY.getItemKey())) {
                    if (configCenterDO.getItem().equalsIgnoreCase(GetwayItemEnum.GETWAY_HOST_CONF_FILE.getItemKey()))
                        saveGetwayConfig(configCenterDO);
                }
                updateCache(configCenterDO);
                if (!saveConfigCenterDO(configCenterDO))
                    return new BusinessWrapper<>(ErrorCode.serverFailure);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
        return new BusinessWrapper<>(true);
    }

    // 重写配置文件
    private void saveGetwayConfig(ConfigCenterDO configCenterDO) {
        configService.saveGetwayHostFileConfigFile(configCenterDO.getValue());
    }

    @Override
    public BusinessWrapper<Boolean> delConfigCenter(long id) {
        try {
            configCenterDao.delConfigCenter(id);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

}
