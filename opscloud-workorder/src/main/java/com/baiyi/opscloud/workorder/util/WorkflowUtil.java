package com.baiyi.opscloud.workorder.util;

import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/11 2:42 PM
 * @Version 1.0
 */
@Slf4j
public class WorkflowUtil {

    private WorkflowUtil() {
    }

    public static WorkflowVO.Workflow toView(String workflow) {
        if (StringUtils.isEmpty(workflow)) {
            return WorkflowVO.Workflow.EMPTY_WORKFLOW;
        }
        try {
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(new Constructor(WorkflowVO.Workflow.class), representer);
            return yaml.loadAs(workflow, WorkflowVO.Workflow.class);
        } catch (JsonSyntaxException e) {
            log.error(e.getMessage());
            return WorkflowVO.Workflow.EMPTY_WORKFLOW;
        }
    }

    public static Map<String, WorkflowVO.Node> toNodeMap(String workflow) {
        WorkflowVO.Workflow wf = toView(workflow);
        return wf.getNodes().stream().collect(Collectors.toMap(WorkflowVO.Node::getName, a -> a, (k1, k2) -> k1));
    }

}
