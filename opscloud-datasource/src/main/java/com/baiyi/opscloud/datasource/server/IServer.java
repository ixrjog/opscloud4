package com.baiyi.opscloud.datasource.server;

import com.baiyi.opscloud.datasource.provider.base.asset.IAssetType;
import com.baiyi.opscloud.domain.generator.opscloud.Server;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 1:39 下午
 * @Since 1.0
 */
public interface IServer extends IAssetType {


    /**
     * 创建
     *
     * @param server
     */
    void create(Server server);

    /**
     * 更新
     *
     * @param server
     */
    void update(Server server);


    /**
     * 销毁
     *
     * @param id
     */
    void destroy(Integer id);


}
