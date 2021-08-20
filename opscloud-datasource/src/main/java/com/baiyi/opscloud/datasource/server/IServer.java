package com.baiyi.opscloud.datasource.server;

import com.baiyi.opscloud.datasource.provider.base.common.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 1:39 下午
 * @Since 1.0
 */
public interface IServer extends IInstanceType {

    void create(DatasourceInstance dsInstance, Server server);

    void update(DatasourceInstance dsInstance, Server server);

    void delete(DatasourceInstance dsInstance, Server server);

    void add(DatasourceInstance dsInstance, Server server, BaseBusiness.IBusiness businessResource);

    void remove(DatasourceInstance dsInstance, Server server, BaseBusiness.IBusiness businessResource);

    void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource);

    void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource);

}
