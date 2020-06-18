package com.baiyi.opscloud.common.config.serverAttribute;

import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/6 2:17 下午
 * @Version 1.0
 */
@Data
public class AttributeGroup {
    private String name;
    private String comment;
    private List<ServerAttribute> attributes;
}
