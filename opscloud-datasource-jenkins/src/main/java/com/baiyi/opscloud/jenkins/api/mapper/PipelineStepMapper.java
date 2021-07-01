package com.baiyi.caesar.jenkins.api.mapper;

import com.baiyi.caesar.jenkins.api.model.PipelineStep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/4/13 11:28 上午
 * @Version 1.0
 */
public class PipelineStepMapper implements JenkinsMapper<PipelineStep>{

    @Override
    public List<PipelineStep> mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<List<PipelineStep>>() {
        });
    }
}
