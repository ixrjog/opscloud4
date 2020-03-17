package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.CloudServerStatus;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.RegexUtils;
import com.baiyi.opscloud.decorator.ServerDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcBusinessTag;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerAttribute;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.OcServerAttributeVO;
import com.baiyi.opscloud.domain.vo.server.OcServerVO;
import com.baiyi.opscloud.facade.CloudServerFacade;
import com.baiyi.opscloud.facade.ServerAttributeFacade;
import com.baiyi.opscloud.facade.ServerFacade;
import com.baiyi.opscloud.facade.TagFacade;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:47 下午
 * @Version 1.0
 */
@Service
public class ServerFacadeImpl implements ServerFacade {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ServerDecorator serverDecorator;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    @Resource
    private CloudServerFacade cloudServerFacade;

    @Resource
    private TagFacade tagFacade;

    @Override
    public DataTable<OcServerVO.Server> queryServerPage(ServerParam.PageQuery pageQuery) {
        DataTable<OcServer> table = ocServerService.queryOcServerByParam(pageQuery);
        return toServerDataTable(table);
    }

    @Override
    public DataTable<OcServerVO.Server> fuzzyQueryServerPage(ServerParam.PageQuery pageQuery) {
        DataTable<OcServer> table = ocServerService.queryOcServerByParam(pageQuery);
        return toServerDataTable(table);
    }

    @Override
    public List<OcServerAttributeVO.ServerAttribute> queryServerAttribute(int id) {
        OcServer ocServer = ocServerService.queryOcServerById(id);
        return serverAttributeFacade.queryServerAttribute(ocServer);
    }

    @Override
    public BusinessWrapper<Boolean> saveServerAttribute(OcServerAttributeVO.ServerAttribute serverAttribute) {
        return serverAttributeFacade.saveServerAttribute(serverAttribute);
    }

    private DataTable<OcServerVO.Server> toServerDataTable(DataTable<OcServer> table) {
        List<OcServerVO.Server> page = BeanCopierUtils.copyListProperties(table.getData(), OcServerVO.Server.class);
        DataTable<OcServerVO.Server> dataTable = new DataTable<>(page.stream().map(e -> serverDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addServer(OcServerVO.Server server) {
        if (StringUtils.isEmpty(server.getPrivateIp()))
            return new BusinessWrapper<>(ErrorEnum.SERVER_PRIVATE_IP_IS_NAME);
        if (ocServerService.queryOcServerByPrivateIp(server.getPrivateIp()) != null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_PRIVATE_IP_CONFLICT);
        if (StringUtils.isEmpty(server.getName()) || !RegexUtils.isServerNameRule(server.getName()))
            return new BusinessWrapper<>(ErrorEnum.SERVER_NAME_NON_COMPLIANCE_WITH_RULES);
        if (server.getServerGroupId() == null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_GROUP_NOT_SELECTED);
        if (ocServerGroupService.queryOcServerGroupById(server.getServerGroupId()) == null)
            return new BusinessWrapper<>(ErrorEnum.SERVERGROUP_NOT_EXIST);
        // 校验SN
        Integer serialNumber = 0;
        try {
            serialNumber = Integer.valueOf(server.getSerialNumber());
        } catch (Exception e) {
            // 序号错误
        }
        if (serialNumber == 0) {
            serialNumber = ocServerService.queryOcServerMaxSerialNumber(server.getServerGroupId());
            server.setSerialNumber(serialNumber + 1);
        }
        OcServer ocServer = BeanCopierUtils.copyProperties(server, OcServer.class);
        ocServerService.addOcServer(ocServer);
        // 云主机绑定
        if (server.getCloudServerId() != null && server.getCloudServerId() > 0)
            cloudServerFacade.updateCloudServerStatus(server.getServerGroupId(), ocServer.getId(), CloudServerStatus.REGISTER.getStatus());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateServer(OcServerVO.Server server) {
        // 校验服务器名称
        if (!RegexUtils.isServerNameRule(server.getName()))
            return new BusinessWrapper<>(ErrorEnum.SERVER_NAME_NON_COMPLIANCE_WITH_RULES);
        // 校验服务器组是否配置
        if (server.getServerGroupId() == null
                || server.getServerGroupId() <= 0
                || ocServerGroupService.queryOcServerGroupById(server.getServerGroupId()) == null) {
            return new BusinessWrapper<>(ErrorEnum.SERVER_GROUP_NOT_SELECTED);
        }
        OcServer ocServer = BeanCopierUtils.copyProperties(server, OcServer.class);
        ocServerService.updateOcServer(ocServer);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteServerById(int id) {
        OcServer ocServer = ocServerService.queryOcServerById(id);
        if (ocServer == null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_NOT_EXIST);

        // 删除server的Tag
        List<OcBusinessTag> ocBusinessTagList = tagFacade.queryOcBusinessTagByBusinessTypeAndBusinessId(BusinessType.SERVER.getType(), id);
        if (!ocBusinessTagList.isEmpty())
            tagFacade.deleteTagByList(ocBusinessTagList);

        // 删除server的属性
        List<OcServerAttribute> serverAttributeList = serverAttributeFacade.queryServerAttributeById(id);
        if(!serverAttributeList.isEmpty())
            serverAttributeFacade.deleteServerAttributeByList(serverAttributeList);

        ocServerService.deleteOcServerById(id);
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 带列号
     *
     * @return
     */
    @Override
    public String acqServerName(OcServer ocServer) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
            return Joiner.on("-").join(ocServer.getName(), ocServer.getSerialNumber());
        } else {
            return Joiner.on("-").join(ocServer.getName(), ocEnv.getEnvName(), ocServer.getSerialNumber());
        }
    }

    /**
     * 不带列号
     *
     * @return
     */
    @Override
    public String acqHostname(OcServer ocServer) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
            return ocServer.getName();
        } else {
            return Joiner.on("-").join(ocServer.getName(), ocEnv.getEnvName());
        }
    }
}
