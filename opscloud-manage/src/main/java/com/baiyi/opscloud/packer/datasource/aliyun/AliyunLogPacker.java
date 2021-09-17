package com.baiyi.opscloud.packer.datasource.aliyun;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AliyunLog;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.aliyun.AliyunLogVO;
import com.baiyi.opscloud.packer.base.IPacker;
import com.baiyi.opscloud.service.datasource.AliyunLogMemberService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/17 10:57 上午
 * @Version 1.0
 */
@Component
public class AliyunLogPacker implements IPacker<AliyunLogVO.Log, AliyunLog> {

    @Resource
    private AliyunLogMemberService aliyunLogMemberService;

    public AliyunLogVO.Log toVO(AliyunLog aliyunLog) {
        return BeanCopierUtil.copyProperties(aliyunLog, AliyunLogVO.Log.class);
    }

    public AliyunLogVO.Log wrap(AliyunLog aliyunLog, IExtend iExtend) {
        AliyunLogVO.Log vo = toVO(aliyunLog);
        if (iExtend.getExtend()) {
            vo.setMemberSize(aliyunLogMemberService.countByAliyunLogId(aliyunLog.getId()));
        }
        return vo;
    }

}
