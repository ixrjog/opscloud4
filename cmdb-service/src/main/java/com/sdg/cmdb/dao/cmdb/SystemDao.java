package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.systems.SystemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/10/8.
 */
@Component
public interface SystemDao {

    /**
     * 新增系统
     * @param systemDO
     * @return
     */
    int addSystem(SystemDO systemDO);

    /**
     * 更新系统
     * @param systemDO
     * @return
     */
    int updateSystem(SystemDO systemDO);

    /**
     * 删除指定系统
     * @param id
     * @return
     */
    int delSystemById(@Param("id")long id);

    /**
     * 模糊查询匹配名称的系统列表
     * @param systemName
     * @return
     */
    long querySystemsByNameSize(@Param("systemName") String systemName);

    /**
     * 模糊查询匹配名称的系统列表分页数据
     * @param systemName
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<SystemDO> querySystemByNamePage(@Param("systemName") String systemName, @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);
}
