package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2022/1/11 2:42 PM
 * @Version 1.0
 */
public class WorkflowUtil {

    private WorkflowUtil() {
    }

    public static WorkflowVO.WorkflowView toWorkflowView(String workflow) {
        if (StringUtils.isEmpty(workflow))
            return WorkflowVO.WorkflowView.EMPTY;
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(workflow);
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(JSONUtil.writeValueAsString(result), WorkflowVO.WorkflowView.class);
        } catch (JsonSyntaxException e) {
            return WorkflowVO.WorkflowView.EMPTY;
        }
    }

}
