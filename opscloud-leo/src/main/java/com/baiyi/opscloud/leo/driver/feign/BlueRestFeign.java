package com.baiyi.opscloud.leo.driver.feign;

import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * https://github.com/jenkinsci/blueocean-plugin/blob/master/blueocean-rest/README.md
 *
 * @Author baiyi
 * @Date 2022/11/25 17:37
 * @Version 1.0
 */
public interface BlueRestFeign {

    /**
     * /blue/rest/organizations/jenkins/pipelines/OUTWAY-DOWNLOAD_outway-download-server-dev/runs/16/nodes/21/steps/
     *
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @return
     */
    @RequestLine("GET /blue/rest/organizations/jenkins/pipelines/{jobName}/runs/{buildNumber}/nodes/{nodeId}/steps/")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    List<JenkinsPipeline.Step> getPipelineNodeSteps(@Param("jobName") String jobName,
                                                    @Param("buildNumber") String buildNumber,
                                                    @Param("nodeId") String nodeId);

    /**
     * /blue/rest/organizations/jenkins/pipelines/OUTWAY-DOWNLOAD_outway-download-server-dev/runs/17/nodes/21/steps/26/log/
     *
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @param stepId
     * @return
     */
    @RequestLine("GET /blue/rest/organizations/jenkins/pipelines/{jobName}/runs/{buildNumber}/nodes/{nodeId}/steps/{stepId}/log/")
    @Headers("Content-Type: text/plain;charset=utf-8")
    String getPipelineNodeStepLog(@Param("jobName") String jobName,
                                  @Param("buildNumber") String buildNumber,
                                  @Param("nodeId") String nodeId,
                                  @Param("stepId") String stepId);

    /**
     * /blue/rest/organizations/jenkins/pipelines/ACCOUNT_account-server-build-prod/runs/1/nodes/?limit=10000
     *
     * @param jobName
     * @param buildNumber
     * @return
     */
    @RequestLine("GET /blue/rest/organizations/jenkins/pipelines/{jobName}/runs/{buildNumber}/nodes/?limit=10000")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    List<JenkinsPipeline.Node> getPipelineNodes(@Param("jobName") String jobName,
                                                @Param("buildNumber") String buildNumber);

    @RequestLine("PUT /blue/rest/organizations/jenkins/pipelines/{jobName}/runs/{buildNumber}/stop")
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    JenkinsPipeline.Step stopPipeline(@Param("jobName") String jobName,
                                      @Param("buildNumber") String buildNumber);

}