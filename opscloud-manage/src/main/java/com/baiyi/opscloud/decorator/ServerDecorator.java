package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.env.OcEnvVO;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.OcServerVO;
import com.baiyi.opscloud.facade.TagFacade;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:46 上午
 * @Version 1.0
 */
@Component("ServerDecorator")
public class ServerDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private TagFacade tagFacade;

    @Resource
    private  ServerGroupDecorator serverGroupDecorator;

    public OcServerVO.Server decorator(OcServerVO.Server server) {
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(server.getEnvType());
        if (ocEnv != null) {
            OcEnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, OcEnvVO.Env.class);
            server.setEnv(env);
        }
        // 装饰 服务器组信息
        OcServerGroup ocServerGroup =  ocServerGroupService.queryOcServerGroupById(server.getServerGroupId());
        if (ocServerGroup != null) {
            OcServerGroupVO.ServerGroup serverGroup = BeanCopierUtils.copyProperties(ocServerGroup, OcServerGroupVO.ServerGroup.class);
            server.setServerGroup(serverGroupDecorator.decorator(serverGroup));
        }
        // 装饰 标签
        TagParam.BusinessQuery businessQuery = new TagParam.BusinessQuery();
        businessQuery.setBusinessType(BusinessType.SERVER.getType());
        businessQuery.setBusinessId(server.getId());
        server.setTags(tagFacade.queryBusinessTag(businessQuery));
        return server;
    }

}
