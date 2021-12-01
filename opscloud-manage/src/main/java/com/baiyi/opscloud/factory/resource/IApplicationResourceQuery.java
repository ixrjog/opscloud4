package com.baiyi.opscloud.factory.resource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.IApplicationResourceType;

/**
 * @Author baiyi
 * @Date 2021/9/8 3:24 下午
 * @Version 1.0
 */
public interface IApplicationResourceQuery extends BaseBusiness.IBusinessType, IApplicationResourceType {

    DataTable<ApplicationResourceVO.Resource> queryResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery);

}
