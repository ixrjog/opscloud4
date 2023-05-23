package com.baiyi.opscloud.factory.resource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.IProjectResType;
import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;

/**
 * @Author 修远
 * @Date 2023/5/19 2:07 PM
 * @Since 1.0
 */
public interface IProjectResQuery extends BaseBusiness.IBusinessType, IProjectResType {

    DataTable<ProjectResourceVO.Resource> queryResourcePage(ProjectResourceParam.ResourcePageQuery pageQuery);
}
