package com.baiyi.opscloud.common.config;

import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/6 2:16 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "oc-server-attributes", ignoreInvalidFields = true)
public class ServerAttributeConfig {

    private List<AttributeGroup> groups;

}
