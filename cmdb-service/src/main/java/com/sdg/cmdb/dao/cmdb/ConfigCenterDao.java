package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterDO;
import com.sdg.cmdb.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liangjian on 2017/5/26.
 */
@Component
public interface ConfigCenterDao {


    /**
     * 查询指定条件的属性组数目
     *
     * @param item
     * @param itemGroup
     * @param env
     * @return
     */
    long getConfigCenterSize(
            @Param("item") String item,
            @Param("itemGroup") String itemGroup,
            @Param("env") String env
    );

    /**
     * 查询指定条件的属性组分页数据
     *
     * @param item
     * @param itemGroup
     * @param env
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ConfigCenterDO> getConfigCenterPage(
            @Param("item") String item,
            @Param("itemGroup") String itemGroup,
            @Param("env") String env,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    /**
     * 按组查询
     *
     * @param itemGroup
     * @param env
     * @return
     */
    List<ConfigCenterDO> queryConfigCenterByItemGroup(@Param("itemGroup") String itemGroup,
                                                      @Param("env") String env);

    /**
     * 按配置项目查询
     *
     * @param item
     * @param env
     * @return
     */
    ConfigCenterDO queryConfigCenterByItem(@Param("item") String item,
                                           @Param("env") String env);


    /**
     * 新增
     *
     * @param configCenterDO
     * @return
     */
    int addConfigCenter(ConfigCenterDO configCenterDO);

    /**
     * 保存
     *
     * @param configCenterDO
     * @return
     */
    int updateConfigCenter(ConfigCenterDO configCenterDO);

    /**
     * 删除属性配置信息
     * @param id
     * @return
     */
    int delConfigCenter(@Param("id") long id);


}
