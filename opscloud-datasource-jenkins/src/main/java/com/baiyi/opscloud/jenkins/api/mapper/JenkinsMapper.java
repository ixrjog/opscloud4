package com.baiyi.caesar.jenkins.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/2 2:49 下午
 * @Version 1.0
 */
public interface JenkinsMapper<T> {

    List<T> mapFromJson(JsonNode jsonNode)
            throws JsonProcessingException;

}