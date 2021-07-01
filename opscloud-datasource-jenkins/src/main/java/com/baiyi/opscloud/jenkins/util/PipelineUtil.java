package com.baiyi.caesar.jenkins.util;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsPipelineVO;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/30 5:59 下午
 * @Version 1.0
 */
public class PipelineUtil {


    public interface States {
        String FINISHED = "FINISHED";
        String RUNNING = "RUNNING";
        String SKIPPED = "SKIPPED";
        String PAUSED = "PAUSED";

    }

    public interface Results {
        String SUCCESS = "SUCCESS";
        String UNKNOWN = "UNKNOWN";
    }


    public static List<JenkinsPipelineVO.Node> convert(List<PipelineNode> nodes) {
        if (CollectionUtils.isEmpty(nodes))
            return Collections.EMPTY_LIST;

        List<JenkinsPipelineVO.Node> result = Lists.newArrayList();
        for (PipelineNode pn : nodes) {
            JenkinsPipelineVO.Node node = JenkinsPipelineVO.Node.builder()
                    .firstParent(pn.getFirstParent())
                    .name(pn.getDisplayName())
                    .id(pn.getId())
                    .state(convertState(pn))
                    .build();
            if ("PARALLEL".equalsIgnoreCase(pn.getType())) {
                addChildren(result, node);
            } else {
                result.add(node);
            }
        }
        return result;
    }

    private static void addChildren(List<JenkinsPipelineVO.Node> result, JenkinsPipelineVO.Node node) {
        for (JenkinsPipelineVO.Node n : result) {
            if (n.getId().equalsIgnoreCase(node.getFirstParent())) {
                n.getChildren().add(node);
                return;
            }
        }

    }


    private static String convertState(PipelineNode node) {
        if (StringUtils.isEmpty(node.getState()))
            return "not_built";
        switch (node.getState()) {
            case States.FINISHED:
                return node.getResult();
            case States.RUNNING:
                return States.RUNNING;
            case States.SKIPPED:
                return States.SKIPPED.toLowerCase();
            case States.PAUSED:
                return States.PAUSED;
            default:
                return "not_built";
        }

    }

    /**
     * e.g:
     * https://cc2.xinc818.com/blue/organizations/jenkins/CAESAR_caesar-server-build-prod/detail/CAESAR_caesar-server-build-prod
     * https://cc2.xinc818.com/blue/organizations/jenkins/MAGICIAN_magician-server-dev/activity
     *
     * @param jenkinsInstanceUrl
     * @param jobName
     * @return
     */
    public static String buildJobBaseUrl(String jenkinsInstanceUrl, String jobName) {
        return Joiner.on("/").skipNulls().join(jenkinsInstanceUrl,
                "blue/organizations/jenkins",
                jobName);

    }

    /**
     * eg
     * https://cc2.xinc818.com/blue/organizations/jenkins/CAESAR_caesar-server-build-prod/detail/CAESAR_caesar-server-build-prod/47/pipeline/
     * @param jobBaseUrl
     * @param jobName
     * @param buildNumber
     * @return
     */
    public static String buildJobBuildUrl(String jobBaseUrl,String jobName, Integer buildNumber) {
        return Joiner.on("/").skipNulls().join(jobBaseUrl,"detail", jobName,buildNumber, "pipeline");
    }
}
