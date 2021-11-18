package com.baiyi.opscloud.datasource.account.util;

import com.baiyi.opscloud.common.util.RegexUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.zabbix.entity.ZabbixMedia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
                .mediatypeid(String.valueOf(ZabbixMedia.MediaType.MAIL))
                .sendto(toJsonNode(new String[] {mail}))
                .build();
    }

    private static ZabbixMedia buildPhoneMedia(String phone) throws JsonProcessingException{
        return ZabbixMedia.builder()
                .mediatypeid(String.valueOf(ZabbixMedia.MediaType.PHONE))
                .sendto(toJsonNode(phone))
                .build();
    }

    private static JsonNode toJsonNode(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(obj, JsonNode.class);
    }

}
