package com.baiyi.opscloud.decorator.aliyun;

import com.baiyi.opscloud.domain.vo.cloud.AliyunLogVO;
import com.baiyi.opscloud.service.log.OcAliyunLogMemberService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/13 3:51 下午
 * @Version 1.0
 */
@Component
public class AliyunLogDecorator {

    @Resource
    private OcAliyunLogMemberService ocAliyunLogMemberService;

    public AliyunLogVO.Log decorator(AliyunLogVO.Log log) {
        log.setMemberSize(ocAliyunLogMemberService.countOcAliyunLogMemberByLogId(log.getId()));
        return log;
    }
}
