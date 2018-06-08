package com.sdg.cmdb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zxxiao on 2016/12/9.
 */
public class PropertyUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    private static final Properties properties;

    public static final String keyStoreFile = "keystore.filePath";

    static {
        properties = new Properties();
        try {
            InputStream inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream("server.properties");
            properties.load(inputStream);
            logger.info("load properties file success!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取指定key对应的value
     * @param key
     * @return
     */
    public static String getProValueByKey(String key) {
        return properties.getProperty(key);
    }
}
