package com.baiyi.opscloud.server.decorator;

import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.baiyi.opscloud.common.config.serverAttribute.ServerAttribute;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/7 10:57 上午
 * @Version 1.0
 */
public class ServerAttributeDecorator {

    /**
     * 装饰属性组
     *
     * @param original 源属性组( 数据原型，若目标对象属性不存在就用原型数据覆盖)
     * @param dest     目标属性组
     * @return
     */
    public static AttributeGroup decorator(AttributeGroup original, AttributeGroup dest) {
        Map<String, ServerAttribute> originalAttributeMap = getServerAttributeMap(original.getAttributes());
        if (dest != null && dest.getAttributes() != null) {
            List<ServerAttribute> list = dest.getAttributes();
            for (ServerAttribute serverAttribute : list) {
                String key = serverAttribute.getName();
                // 源属性不存在
                if (!originalAttributeMap.containsKey(key)) continue;
                // 目标属性未配置，从源属性读取
                if (StringUtils.isEmpty(serverAttribute.getValue()))
                    serverAttribute.setValue(originalAttributeMap.get(key).getValue());
                originalAttributeMap.remove(key);
            }
            // 补全缺少的属性配置项
            if (!originalAttributeMap.isEmpty()) {
                for (String key : originalAttributeMap.keySet())
                    list.add(originalAttributeMap.get(key));
            }
            dest.setAttributes(list);
        }
        return dest;
    }

    /**
     * 转换 源属性组 为Map
     *
     * @param serverAttributeList
     * @return
     */
    private static Map<String, ServerAttribute> getServerAttributeMap(List<ServerAttribute> serverAttributeList) {
        return serverAttributeList.stream().collect(Collectors.toMap(ServerAttribute::getName, a -> a, (k1, k2) -> k1));
    }

}
