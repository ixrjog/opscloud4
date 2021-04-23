package com.baiyi.opscloud.dingtalk.impl;

import com.baiyi.opscloud.dingtalk.DingtalkDeptApi;
import com.baiyi.opscloud.dingtalk.DingtalkTokenApi;
import com.baiyi.opscloud.dingtalk.handler.DingtalkDeptApiHandler;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 3:18 下午
 * @Since 1.0
 */

@Component
public class DingtalkDeptApiImpl implements DingtalkDeptApi {

    @Resource
    private DingtalkTokenApi dingtalkTokenApi;

    @Resource
    private DingtalkDeptApiHandler dingtalkDeptApiHandler;

    @Override
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 1.5))
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDeptList(DingtalkParam.QueryByDeptId param) throws RuntimeException {
        param.setAccessToken(dingtalkTokenApi.getDingtalkToken(param.getUid()));
        OapiV2DepartmentListsubResponse response = dingtalkDeptApiHandler.getDeptList(param);
        if (response == null)
            throw new RuntimeException("获取dingtalk dept 失败,retry");
        return response.getResult();
    }

    @Override
    public List<Long> getDeptIdList(DingtalkParam.QueryByDeptId param) {
        param.setAccessToken(dingtalkTokenApi.getDingtalkToken(param.getUid()));
        return dingtalkDeptApiHandler.getDeptIdList(param);
    }

}
