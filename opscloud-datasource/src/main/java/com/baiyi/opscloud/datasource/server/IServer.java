package com.baiyi.opscloud.datasource.server;

import com.baiyi.opscloud.datasource.IManagerProvider;
import com.baiyi.opscloud.domain.base.IInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.Server;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 1:39 下午
 * @Since 1.0
 */
public interface IServer extends IManagerProvider<Server>, IInstanceType {

}
