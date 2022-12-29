package com.baiyi.opscloud.leo.converter;

import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/25 17:00
 * @Version 1.0
 */
public class JenkinsPipelineConverter {

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

    public static List<LeoBuildVO.Node> toLeoBuildNodes(List<JenkinsPipeline.Node> nodes) {
        if (CollectionUtils.isEmpty(nodes))
            return Collections.emptyList();
        List<LeoBuildVO.Node> result = Lists.newArrayList();
        nodes.forEach(pn -> {
            LeoBuildVO.Node node = LeoBuildVO.Node.builder()
                    .firstParent(pn.getFirstParent())
                    .name(pn.getDisplayName())
                    .id(pn.getId())
                    .state(toState(pn))
                    .build();
            if ("PARALLEL".equalsIgnoreCase(pn.getType())) {
                addChildren(result, node);
            } else {
                result.add(node);
            }
        });
        return result;
    }

    private static void addChildren(List<LeoBuildVO.Node> result, LeoBuildVO.Node node) {
        result.stream().filter(n -> n.getId().equalsIgnoreCase(node.getFirstParent())).findFirst().ifPresent(n -> n.getChildren().add(node));
    }

    private static String toState(JenkinsPipeline.Node node) {
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

}
