package com.baiyi.opscloud.workorder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.WorkorderUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.vo.workorder.ApprovalOptionsVO;
import com.baiyi.opscloud.service.workorder.OcWorkorderService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/30 11:37 上午
 * @Version 1.0
 */
public class WorkorderTest extends BaseUnit {

    @Resource
    private OcWorkorderService ocWorkorderService;


    @Test
    void updateUsersUuid() {
        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(19);

        ApprovalOptionsVO.ApprovalOptions options = WorkorderUtils.convert(ocWorkorder.getApprovalDetail());

        System.err.println(JSON.toJSONString(options));

    }
}
