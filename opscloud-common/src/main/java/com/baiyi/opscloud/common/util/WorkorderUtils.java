package com.baiyi.opscloud.common.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.domain.vo.workorder.ApprovalOptionsVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2020/4/30 11:35 上午
 * @Version 1.0
 */
public class WorkorderUtils {

    public static ApprovalOptionsVO.ApprovalOptions convert(String approvalDetail) {
        Yaml yaml = new Yaml();
        Object result = yaml.load(approvalDetail);
        try {
            Gson gson = new GsonBuilder().create();
            ApprovalOptionsVO.ApprovalOptions options = gson.fromJson(JSON.toJSONString(result), ApprovalOptionsVO.ApprovalOptions.class);
            return options;
        } catch (Exception e) {
            return null;
        }
    }
}
