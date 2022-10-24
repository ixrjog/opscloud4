package com.baiyi.opscloud.facade.auth.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatformLog;
import com.baiyi.opscloud.domain.param.auth.AuthPlatformParam;
import com.baiyi.opscloud.domain.vo.auth.AuthPlatformVO;
import com.baiyi.opscloud.facade.auth.AuthPlatformFacade;
import com.baiyi.opscloud.packer.auth.AuthPlatformLogPacker;
import com.baiyi.opscloud.service.auth.AuthPlatformLogService;
import com.baiyi.opscloud.service.auth.AuthPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/9/19 09:32
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthPlatformFacadeImpl implements AuthPlatformFacade {

    private final AuthPlatformService authPlatformService;

    private final AuthPlatformLogService authPlatformLogService;

    private final AuthPlatformLogPacker authPlatformLogPacker;

    @Override
    public List<AuthPlatformVO.Platform> getPlatformOptions() {
        return authPlatformService.queryAll().stream().map(
                e -> AuthPlatformVO.Platform.builder()
                        .platformId(e.getId())
                        .name(e.getName())
                        .comment(e.getComment())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public DataTable<AuthPlatformVO.AuthPlatformLog> queryAuthPlatformLog(AuthPlatformParam.AuthPlatformLogPageQuery pageQuery) {
        DataTable<AuthPlatformLog> table = authPlatformLogService.queryPageByParam(pageQuery);
        List<AuthPlatformVO.AuthPlatformLog> data = table.getData().stream().map(e -> {
            AuthPlatformVO.AuthPlatformLog authPlatformLog = BeanCopierUtil.copyProperties(e, AuthPlatformVO.AuthPlatformLog.class);
            authPlatformLogPacker.wrap(authPlatformLog, pageQuery);
            return authPlatformLog;
        }).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

}
