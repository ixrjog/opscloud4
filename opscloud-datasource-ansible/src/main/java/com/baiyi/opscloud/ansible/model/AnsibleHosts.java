package com.baiyi.opscloud.ansible.model;

import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.service.business.BusinessPropertyHelper;
import com.google.common.base.Joiner;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/8/16 5:58 下午
 * @Version 1.0
 */
public class AnsibleHosts {

    @Builder
    @Data
    public static class Hosts {

        private List<Group> groups;

        private String inventoryHost;

        public String toInventory() {
            StringBuilder sb = new StringBuilder(IOUtil.getHeadInfo(IOUtil.COMMENT_SIGN));
            groups.forEach(e -> sb.append(e.format()));
            return sb.toString();
        }
    }

    @Builder
    @Data
    public static class Group {

        private ServerGroup serverGroup;
        
        private Map<String, List<ServerPack>> serverMap;

        private String sshUser;

        public String format() {
            StringBuilder result = new StringBuilder(Joiner.on(" ").skipNulls().join("#", serverGroup.getName(), serverGroup.getComment(), "\n"));
            serverMap.forEach((k,v) -> {
                result.append("[").append(k).append("]\n");
                v.forEach(s -> result.append(toHostLine(s)));
                result.append("\n");
            });
            return result.toString();
        }

        private String toHostLine(ServerPack serverPack) {
            String serverName = SimpleServerNameFacade.toName(serverPack.getServer(), serverPack.getEnv());
            return Joiner.on(" ").skipNulls().join(
                    BusinessPropertyHelper.getManageIp(serverPack),
                    link("ansible_ssh_user", sshUser),
                    link("ansible_ssh_port", String.valueOf(BusinessPropertyHelper.getSshPort(serverPack))),
                    link("hostname", serverName),
                    "#", serverName, "\n");
        }

        private String link(String k, String v) {
            if (StringUtils.isEmpty(v)) return null;
            return Joiner.on("=").join(k, v);
        }

    }

}
