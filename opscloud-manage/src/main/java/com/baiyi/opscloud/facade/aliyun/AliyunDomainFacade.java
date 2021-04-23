package com.baiyi.opscloud.facade.aliyun;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDomainVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:59 下午
 * @Since 1.0
 */
public interface AliyunDomainFacade {

    BusinessWrapper<Boolean> syncAliyunDomain();

    BusinessWrapper<Boolean> setAliyunDomainActive(Integer id);

    DataTable<AliyunDomainVO.Domain> queryAliyunDomainPage(AliyunDomainParam.PageQuery pageQuery);
}
