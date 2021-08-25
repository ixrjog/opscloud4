package com.baiyi.opscloud.datasource.accountGroup;

import com.baiyi.opscloud.datasource.provider.base.common.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;

/**
 * @Author baiyi
 * @Date 2021/8/24 10:44 上午
 * @Version 1.0
 */
public interface IAccountGroup extends IInstanceType {

    void create(DatasourceInstance dsInstance, UserGroup userGroup);

    void update(DatasourceInstance dsInstance, UserGroup userGroup);

    void delete(DatasourceInstance dsInstance, UserGroup userGroup);

    void grant(DatasourceInstance dsInstance, UserGroup userGroup, BaseBusiness.IBusiness businessResource);

    void revoke(DatasourceInstance dsInstance, UserGroup userGroup, BaseBusiness.IBusiness businessResource);
}
