package com.baiyi.opscloud.datasource.aws.iam.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/5/16 8:08 PM
 * @Since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IamPolicyDocument {

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Statement")
    private List<Statement> statementList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Statement {

        @JsonProperty("Sid")
        private String sid;

        @JsonProperty("Effect")
        private String effect;

        @JsonProperty("Principal")
        private String principal;

        @JsonProperty("NotPrincipal")
        private String notPrincipal;

        @JsonProperty("Action")
        private String action;

        @JsonProperty("NotAction")
        private String notAction;

        @JsonProperty("Resource")
        private String resource;

        @JsonProperty("NotResource")
        private String notResource;

        @JsonProperty("Condition")
        private String condition;

    }

}
