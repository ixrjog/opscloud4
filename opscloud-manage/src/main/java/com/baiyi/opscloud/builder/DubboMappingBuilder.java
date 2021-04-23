package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMapping;
import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/9 3:51 下午
 * @Version 1.0
 */
public class DubboMappingBuilder {

    public static OcDubboMapping build(String env, List<NameValuePair> pairList) {
        OcDubboMapping ocDubboMapping = new OcDubboMapping();
        ocDubboMapping.setTcpMappingId(0);
        ocDubboMapping.setEnv(env);
        for (NameValuePair nameValuePair : pairList) {
            if (nameValuePair.getName().equals("interface")) {
                ocDubboMapping.setDubboInterface(nameValuePair.getValue());
                break;
            }
        }
        return ocDubboMapping;
    }

}
