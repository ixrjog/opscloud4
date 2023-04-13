package com.baiyi.opscloud.datasource.aws.iam.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2022/5/16 8:08 PM
 * @Since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IamPolicyDocument {

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Statement")
    private List<Statement> statement;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Statement {

        @JsonProperty("Sid")
        private String sid;

        @JsonProperty("Effect")
        private String effect;

        @JsonProperty("Principal")
        private Object principal;

        @JsonProperty("NotPrincipal")
        private Object notPrincipal;

        @JsonProperty("Action")
        private Object action;

        @JsonProperty("NotAction")
        private Object notAction;

        @JsonProperty("Resource")
        private Object resource;

        @JsonProperty("NotResource")
        private Object notResource;

        @JsonProperty("Condition")
        private Condition condition;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Condition {

        @JsonProperty("ArnLike")
        private Map<String, String> arnLike;

    }

}