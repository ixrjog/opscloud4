package com.baiyi.opscloud.facade.opscloud;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudInstanceParam;
import com.baiyi.opscloud.domain.vo.opscloud.HealthVO;
import com.baiyi.opscloud.domain.vo.opscloud.OpscloudVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 4:32 下午
 * @Since 1.0
 */
public interface OpscloudInstanceFacade {
    HealthVO.Health checkHealth();

    DataTable<OpscloudVO.Instance> queryOpscloudInstancePage(OpscloudInstanceParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> setOpscloudInstanceActive(int id);

    BusinessWrapper<Boolean> delOpscloudInstanceById(int id);
}
