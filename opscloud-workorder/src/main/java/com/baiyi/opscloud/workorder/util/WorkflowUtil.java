package com.baiyi.opscloud.workorder.util;

import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

    public static WorkflowVO.Workflow load(String workflow) {
        if (StringUtils.isEmpty(workflow)) {
            return WorkflowVO.Workflow.EMPTY_WORKFLOW;
        }
        try {
            return YamlUtil.loadAs(workflow, WorkflowVO.Workflow.class);
        } catch (JsonSyntaxException e) {
            log.error(e.getMessage());
            return WorkflowVO.Workflow.EMPTY_WORKFLOW;
        }
    }

    public static Map<String, WorkflowVO.Node> toNodeMap(String workflow) {
        WorkflowVO.Workflow wf = load(workflow);
        return wf.getNodes().stream().collect(Collectors.toMap(WorkflowVO.Node::getName, a -> a, (k1, k2) -> k1));
    }

}