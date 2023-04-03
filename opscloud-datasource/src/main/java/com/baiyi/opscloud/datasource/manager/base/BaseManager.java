package com.baiyi.opscloud.datasource.manager.base;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/11 11:18 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseManager {

    @Resource
    private InstanceHelper instanceHelper;

    abstract protected DsTypeEnum[] getFilterInstanceTypes();

    abstract protected String getTag();

    /**
     * 查询实例
     *
     * @return
     */
    protected List<DatasourceInstance> listInstance() {
        return instanceHelper.listInstance(getFilterInstanceTypes(), getTag());
    }

}
