package com.baiyi.caesar.jenkins.api.mapper;

import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/30 2:49 下午
 * @Version 1.0
 */
public class PipelineNodeMapper implements JenkinsMapper<PipelineNode>{

    @Override
    public List<PipelineNode> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<List<PipelineNode>>() {
        });
    }
}
