package com.baiyi.opscloud.packer.datasource.aliyun;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.datasource.AliyunLogMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/17 10:57 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunLogPacker implements IWrapper<AliyunLogVO.Log> {

    private final AliyunLogMemberService aliyunLogMemberService;

    @Override
    public void wrap(AliyunLogVO.Log aliyunLog, IExtend iExtend) {
        if (iExtend.getExtend()) {
            aliyunLog.setMemberSize(aliyunLogMemberService.countByAliyunLogId(aliyunLog.getId()));
        }
    }

}
