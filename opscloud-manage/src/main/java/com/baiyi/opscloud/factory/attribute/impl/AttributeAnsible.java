package com.baiyi.opscloud.factory.attribute.impl;

import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.base.SettingName;
import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.PreviewAttributeVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.facade.SettingBaseFacade;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @Author baiyi
 * @Date 2020/4/7 10:28 上午
 * @Version 1.0
 */
@Component
public class AttributeAnsible extends AttributeBase {

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private SettingBaseFacade settingFacade;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    /**
     * 清空缓存
     */
    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_ATTRIBUTE_CACHE_REPO, key = "'preview_' + #serverGroupId", beforeInvocation = true)
    public void evictPreview(int serverGroupId) {
    }

    /**
     * 用于服务器组属性预览
     *
     * @param serverGroupId
     * @return
     */
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ATTRIBUTE_CACHE_REPO, key = "'preview_' + #serverGroupId")
    public List<PreviewAttributeVO.PreviewAttribute> preview(int serverGroupId) {
        List<PreviewAttributeVO.PreviewAttribute> previewAttributes = Lists.newArrayList();
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(serverGroupId);
        Map<String, List<OcServer>> serverMap = grouping(ocServerGroup, true);
        String content = format(ocServerGroup, serverMap);
        if (StringUtils.isEmpty(content))
            return previewAttributes;
        PreviewAttributeVO.PreviewAttribute previewAttribute = PreviewAttributeVO.PreviewAttribute.builder()
                .title("Ansible hosts")
                .content(content)
                .build();
        previewAttributes.add(previewAttribute);
        return previewAttributes;
    }

    /**
     * 清空缓存
     */
    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_ATTRIBUTE_CACHE_REPO, key = "'build_' + #ocServerGroup.id", beforeInvocation = true)
    public void evictBuild(OcServerGroup ocServerGroup) {
    }

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ATTRIBUTE_CACHE_REPO, key = "'build_' + #ocServerGroup.id")
    public PreviewAttributeVO.PreviewAttribute build(OcServerGroup ocServerGroup) {
        // 跳过空服务器组
        if (ocServerService.countByServerGroupId(ocServerGroup.getId()) == 0)
            return PreviewAttributeVO.PreviewAttribute.builder().build();
        Map<String, List<OcServer>> serverMap = grouping(ocServerGroup, true);
        String content = format(ocServerGroup, serverMap);
        if (StringUtils.isEmpty(content))
            return PreviewAttributeVO.PreviewAttribute.builder().build();
        return PreviewAttributeVO.PreviewAttribute.builder()
                .title("Ansible hosts")
                .content(content)
                .build();
    }

    private String build(List<OcServerGroup> serverGroups, boolean isSubgroup) {
        String result = getHeadInfo();
        for (OcServerGroup ocServerGroup : serverGroups) {
            List<OcServer> serverList = ocServerService.queryOcServerByServerGroupId(ocServerGroup.getId());
            if (CollectionUtils.isEmpty(serverList)) continue;
            Map<String, List<OcServer>> serverMap = grouping(ocServerGroup, isSubgroup);
            result += format(ocServerGroup, serverMap);
        }
        return result;
    }

    private String format(OcServerGroup ocServerGroup, Map<String, List<OcServer>> serverMap) {
        String comment = ocServerGroup.getComment();
        if (StringUtils.isEmpty(comment))
            comment = ocServerGroup.getName();
        StringBuilder result = new StringBuilder("# " + comment + "\n");

        serverMap.keySet().forEach(k -> {
            result.append(Joiner.on("").join("[", k, "]\n"));
            List<OcServer> serverList = serverMap.get(k);
            serverList.forEach(s -> result.append(acqHostLine(s)));
            result.append("\n");
        });
        return result.toString();
    }

    private String acqHostLine(OcServer ocServer) {
        String serverName = ServerBaseFacade.acqServerName(ocServer);
        // 支持自定义端口
        String port = serverAttributeFacade.getSSHPort(ocServer);
        return Joiner.on(" ").skipNulls().join(getManageIp(ocServer),
                link("ansible_ssh_user", settingFacade.querySetting(SettingName.SERVER_HIGH_AUTHORITY_ACCOUNT)),
                link("ansible_ssh_port", "22".equalsIgnoreCase(port) ? null : port),
                link("cloudServerType", getCloudServerType(ocServer)),
                link("hostname", serverName),
                "#", serverName, "\n");
    }

    private String link(String k, String v) {
        if (StringUtils.isEmpty(v)) return null;
        return Joiner.on("=").join(k, v);
    }

    private String getCloudServerType(OcServer ocServer) {
        for (CloudServerType cloudServerType : CloudServerType.values())
            if (cloudServerType.getType() == ocServer.getServerType()) return cloudServerType.name();
        return CloudServerType.PS.name();
    }

    private String getManageIp(OcServer ocServer) {
        return serverAttributeFacade.getManageIp(ocServer);
    }

    /**
     * 取服务器分组map (可单独对外提供分组配置)
     * 完整的包含 环境全组数据
     *
     * @param ocServerGroup
     * @param isSubgroup
     * @return
     */
    public Map<String, List<OcServer>> grouping(OcServerGroup ocServerGroup, boolean isSubgroup) {
        Map<String, List<OcServer>> serverMap = groupingByEnv(ocServerGroup);
        if (isSubgroup)
            groupingSubgroup(serverMap, getSubgroup(ocServerGroup));
        return serverMap;
    }

    /**
     * 清空缓存
     *
     * @param ocServerGroup
     */
    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_ANSIBLE_CACHE_REPO, key = "#ocServerGroup.id", beforeInvocation = true)
    public void evictGrouping(OcServerGroup ocServerGroup) {
    }

    /**
     * 取服务器分组map，不含重复的主机分组模式
     * server-pord-1
     * server-pord-2
     * server-pord(不包含)
     *
     * @param ocServerGroup
     * @return
     */
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ANSIBLE_CACHE_REPO, key = "#ocServerGroup.id")
    public Map<String, List<OcServer>> grouping(OcServerGroup ocServerGroup) {
        Map<String, List<OcServer>> serverMap = groupingByEnv(ocServerGroup);
        if (serverMap.isEmpty()) return serverMap;
        int subgroup = getSubgroup(ocServerGroup);

        Set<String> keSet = Sets.newHashSet(serverMap.keySet());
        keSet.forEach(k -> {
            List<OcServer> servers = serverMap.get(k);
            if (servers.size() >= 2) {
                groupingSubgroup(serverMap, servers, k, subgroup);
                serverMap.remove(k);
            }
        });
        return serverMap;
    }

    private void groupingSubgroup(Map<String, List<OcServer>> serverMap, int subgroup) {
        if (serverMap.isEmpty()) return;
        Set<String> keySet = Sets.newHashSet(serverMap.keySet());
        keySet.forEach(k -> {
            List<OcServer> servers = serverMap.get(k);
            if (servers.size() >= 2)
                groupingSubgroup(serverMap, servers, k, subgroup);
        });
    }

    private void groupingSubgroup(Map<String, List<OcServer>> serverMap, List<OcServer> serverList, String groupingName, int subgroup) {
        List<OcServer> preServerList = Lists.newArrayList(serverList);
        // 服务器数量少于分组数量也只分2组
        if (subgroup > preServerList.size())
            subgroup = 2;
        // 每组平均服务器数量
        int size = preServerList.size() / subgroup;
        int compensate = preServerList.size() % subgroup;
        int i = 1;
        while (!preServerList.isEmpty()) {
            List<OcServer> subServerList = acqSubgroup(preServerList, compensate >= 1 ? size + 1 : size);
            serverMap.put(groupingName + "-" + i, subServerList);
            compensate--;
            i++;
        }
    }

    private int getSubgroup(OcServerGroup ocServerGroup) {
        int defSubgroup = 2;
        Map<String, String> attributeMap = serverAttributeFacade.getServerGroupAttributeMap(ocServerGroup);
        if (!attributeMap.containsKey(Global.SERVER_ATTRIBUTE_ANSIBLE_SUBGROUP))
            return defSubgroup;
        String subgroup = attributeMap.get(Global.SERVER_ATTRIBUTE_ANSIBLE_SUBGROUP);
        try {
            int n = Integer.parseInt(subgroup);
            if (n > 2)
                return n;
        } catch (Exception ignored) {
        }
        return defSubgroup;
    }

    /**
     * 取子组
     *
     * @param serverList 服务器列表
     * @param size       数量
     * @return
     */
    private List<OcServer> acqSubgroup(List<OcServer> serverList, int size) {
        List<OcServer> subList = Lists.newArrayList(serverList.subList(0, size));
        serverList.subList(0, size).clear();
        return subList;
    }


}
