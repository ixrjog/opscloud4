package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.packer.leo.LeoJobPacker;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:45
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoJobFacadeImpl implements LeoJobFacade {

    private final LeoJobService leoJobService;

    private final LeoJobPacker leoJobPacker;

    @Override
    public DataTable<LeoJobVO.Job> queryLeoJobPage(LeoJobParam.JobPageQuery pageQuery) {
        DataTable<LeoJob> table = leoJobService.queryJobPage(pageQuery);
        List<LeoJobVO.Job> data = BeanCopierUtil.copyListProperties(table.getData(), LeoJobVO.Job.class).stream()
                .peek(e -> leoJobPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }


}
