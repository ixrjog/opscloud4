package com.baiyi.opscloud.sshserver.command.custom.kubernetes;

import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.sshcore.table.PrettyTable;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshContext;
import com.baiyi.opscloud.sshserver.SshShellCommandFactory;
import com.baiyi.opscloud.sshserver.aop.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.aop.annotation.SettingContextSessionUser;
import com.baiyi.opscloud.sshserver.aop.annotation.TerminalSize;
import com.baiyi.opscloud.sshserver.command.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.custom.context.KubernetesDsInstance;
import com.baiyi.opscloud.sshserver.command.custom.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.custom.kubernetes.base.BaseKubernetesCommand;
import com.baiyi.opscloud.sshserver.pagination.TableFooter;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Map;

import static com.baiyi.opscloud.sshserver.constants.TableConstants.TABLE_KUBERNETES_DEPLOYMENT_FIELD_NAMES;

/**
 * @Author baiyi
 * @Date 2021/7/7 3:51 下午
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@SshShellComponent
@ShellCommandGroup("Kubernetes")
@RequiredArgsConstructor
public class KubernetesDeploymentCommand extends BaseKubernetesCommand {

    private final UserService userService;

    @TerminalSize(cols = 116, rows = 10)
    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List kubernetes deployment info.", key = {"list-k8s-deployment"})
    public void listDeployment(@ShellOption(help = "Name", defaultValue = "") String name) {
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        DsAssetParam.UserPermissionAssetPageQuery pageQuery = DsAssetParam.UserPermissionAssetPageQuery.builder()
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .queryName(name)
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .userId(userService.getByUsername(SessionHolder.getUsername()).getId())
                .page(1)
                .length(terminal.getSize().getRows() - PAGE_FOOTER_SIZE)
                .build();
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);
        PrettyTable pt = PrettyTable.fieldNames(TABLE_KUBERNETES_DEPLOYMENT_FIELD_NAMES);
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
        sshShellHelper.print(pt.toString());
        TableFooter.Pagination.builder()
                .totalNum(table.getTotalNum())
                .page(pageQuery.getPage())
                .length(pageQuery.getLength())
                .build().print(sshShellHelper, PromptColor.GREEN);
    }

}