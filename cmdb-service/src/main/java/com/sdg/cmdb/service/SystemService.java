package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.systems.SystemDO;

import java.util.List;

/**
 * Created by zxxiao on 16/10/8.
 */
public interface SystemService {

    /**
     * 保存 || 更新系统信息
     * @param systemDO
     * @return
     */
    BusinessWrapper<Boolean> saveSystem(SystemDO systemDO);

    /**
     * 删除指定的系统
     * @param systemId
     * @return
     */
    BusinessWrapper<Boolean> delSystem(long systemId);

    /**
     * 模糊查询匹配名称的系统列表
     * @param systemName
     * @param page
     * @param length
     * @return
     */
    TableVO<List<SystemDO>> querySystems(String systemName, int page, int length);
}
