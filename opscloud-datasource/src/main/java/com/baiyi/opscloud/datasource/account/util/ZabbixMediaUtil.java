package com.baiyi.opscloud.zabbix.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.util.JSONMapper;
import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.zabbix.entry.ZabbixMedia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/11 5:31 下午
 * @Version 1.0
 */
public class ZabbixMediaUtil {

    /**
     * 获取用户告警媒介
     *
     * @param user
     * @return
     */
    public static List<ZabbixMedia> buildMedias(User user) {
        List<ZabbixMedia> medias = Lists.newArrayList();
        try {
            if (RegexUtil.isEmail(user.getEmail())) {
                medias.add(buildMailMedia(user.getEmail()));
            }
            if(RegexUtil.isPhone(user.getPhone())){
                medias.add(buildPhoneMedia(user.getPhone()));
            }
        }catch (JsonProcessingException ignored){
        }
        return medias;
    }

    private static ZabbixMedia buildMailMedia(String mail) throws JsonProcessingException{
        return ZabbixMedia.builder()
                .mediaTypeId(String.valueOf(ZabbixMedia.MediaType.MAIL))
                .sendTo(readTree(JSON.toJSONString(Lists.newArrayList(mail))))
                .build();
    }

    private static ZabbixMedia buildPhoneMedia(String phone) throws JsonProcessingException{
        return ZabbixMedia.builder()
                .mediaTypeId(String.valueOf(ZabbixMedia.MediaType.PHONE))
                .sendTo(readTree(phone))
                .build();
    }

    private static JsonNode readTree(String data) throws JsonProcessingException {
        JSONMapper mapper = new JSONMapper();
        return mapper.readTree(data);
    }

}
