package com.baiyi.opscloud.sshserver.command.kubernetes;

import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.sshcore.table.PrettyTable;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.annotation.CheckTerminalSize;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.command.component.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.KubernetesDsInstance;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.kubernetes.base.BaseKubernetesCommand;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/7 3:51 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Kubernetes")
public class KubernetesDeploymentCommand extends BaseKubernetesCommand {

    @Resource
    private UserService userService;

    @CheckTerminalSize(cols = 116, rows = 10)
    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "查询无状态列表信息", key = {"list-k8s-deployment"})
    public void listKubernetesDeployment(@ShellOption(help = "Name", defaultValue = "") String name) {
        DsAssetParam.UserPermissionAssetPageQuery pageQuery = DsAssetParam.UserPermissionAssetPageQuery.builder()
                .assetType(DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .queryName(name)
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .userId(userService.getByUsername(SessionUtil.getUsername()).getId())
                .page(1)
                .length(terminal.getSize().getRows() - PAGE_FOOTER_SIZE)
                .build();
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);

//        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
//                .assetType(DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
//                .queryName(name)
//                .isActive(true)
//                .build();
//        pageQuery.setLength(terminal.getSize().getRows() - PAGE_FOOTER_SIZE);
//        pageQuery.setPage(1);
        // DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);

        PrettyTable pt = PrettyTable
                .fieldNames("ID",
                        "Kubernetes Instance Name",
                        "Namespace",
                        "Deployment Name"
                );

        Map<Integer, Integer> idMapper = Maps.newHashMap();
        Map<String, KubernetesDsInstance> kubernetesDsInstanceMap = Maps.newHashMap();
        int id = 1;
        for (DatasourceInstanceAsset asset : table.getData()) {
            String instanceUuid = asset.getInstanceUuid();
            if (!kubernetesDsInstanceMap.containsKey(instanceUuid)) {
                KubernetesDsInstance kubernetesDsInstance = KubernetesDsInstance.builder()
                        .dsInstance(dsInstanceService.getByUuid(instanceUuid))
                        .kubernetesDsInstanceConfig(buildConfig(instanceUuid))
                        .build();
                kubernetesDsInstanceMap.put(instanceUuid, kubernetesDsInstance);
            }
            idMapper.put(id, asset.getId());
            pt.addRow(id,
                    kubernetesDsInstanceMap.get(instanceUuid).getDsInstance().getInstanceName(),
                    asset.getAssetKey2(),
                    asset.getAssetKey()
            );
            id++;
        }
        SessionCommandContext.setIdMapper(idMapper);
        helper.print(pt.toString());
        helper.print(buildPagination(table.getTotalNum(),
                        pageQuery.getPage(),
                        pageQuery.getLength()),
                PromptColor.GREEN);
    }

    public static String buildPagination(long totalNum, int page, int length) {
        int tp = 0;
        try {
            tp = (int) (totalNum - 1) / length + 1;
        } catch (Exception ignored) {
        }
        return Joiner.on(" ,").join("页码: " + page, "分页长度: " + length, "总页数: " + tp, "总数量: " + totalNum);
    }
}
