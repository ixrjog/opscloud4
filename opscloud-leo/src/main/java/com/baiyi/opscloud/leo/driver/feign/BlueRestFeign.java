package com.baiyi.opscloud.leo.driver.feign;

import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/25 17:37
 * @Version 1.0
 */
public interface BlueRestFeign {

    /**
     * /blue/rest/organizations/jenkins/pipelines/OUTWAY-DOWNLOAD_outway-download-server-dev/runs/16/nodes/21/steps/
     * @param authBasic
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @return
     */
    @RequestLine("GET /blue/rest/organizations/jenkins/pipelines/{jobName}/runs/{buildNumber}/nodes/{nodeId}/steps")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    List<JenkinsPipeline.Step> getPipelineRunNodeSteps(@Param("authBasic") String authBasic,
                                                       @Param("jobName") String jobName,
                                                       @Param("buildNumber") String buildNumber,
                                                       @Param("nodeId") String nodeId);

    /**
     * /blue/rest/organizations/jenkins/pipelines/OUTWAY-DOWNLOAD_outway-download-server-dev/runs/17/nodes/21/steps/26/log/
     * @param authBasic
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @param stepId
     * @return
     */
    @RequestLine("GET /blue/rest/organizations/jenkins/pipelines/{jobName}/runs/{buildNumber}/nodes/{nodeId}/steps/{stepId}/log")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    String getPipelineRunNodeStepLog(@Param("authBasic") String authBasic,
                                     @Param("jobName") String jobName,
                                     @Param("buildNumber") String buildNumber,
                                     @Param("nodeId") String nodeId,
                                     @Param("stepId") String stepId);

    /**
     * /blue/rest/organizations/jenkins/pipelines/ACCOUNT_account-server-build-prod/runs/1/nodes/?limit=10000
     * @param authBasic
     * @param jobName
     * @param buildNumber
     * @return
     */
    @RequestLine("GET /blue/rest/organizations/jenkins/pipelines/{jobName}/runs/{buildNumber}/nodes/?limit=10000")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    List<JenkinsPipeline.Node> getPipelineRunNodes(@Param("authBasic") String authBasic,
                                                   @Param("jobName") String jobName,
                                                   @Param("buildNumber") String buildNumber);

}
