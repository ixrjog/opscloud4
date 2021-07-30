package com.baiyi.opscloud.domain.model.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/27 10:38 上午
 * @Since 1.0
 */


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "version",
        "kind",
        "metadata",
        "spec"
})
public class ServerProperty {


    @JsonProperty("version")
    private String version = "v1";

    @JsonProperty("kind")
    private String kind = "server";

    @JsonProperty("metadata")
    private Map<String, String> metadata;

    private Integer port;

    @JsonProperty("spec")
    private Map<String, Object> spec;
}
