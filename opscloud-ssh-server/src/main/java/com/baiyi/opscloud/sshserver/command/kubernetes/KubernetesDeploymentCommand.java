package com.baiyi.opscloud.sshserver.command.kubernetes;

import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SimpleTable;
import com.baiyi.opscloud.sshserver.annotation.CheckTerminalSize;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.command.component.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.KubernetesDsInstance;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.etc.ColorAligner;
import com.baiyi.opscloud.sshserver.command.kubernetes.base.BaseKubernetesCommand;
import com.baiyi.opscloud.sshserver.command.util.TableHeaderBuilder;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;

import java.util.Arrays;
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

    @CheckTerminalSize(cols = 116,rows = 10)
    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "查询无状态列表信息", key = {"list-k8s-deployment"})
    public void listKubernetesDeployment(@ShellOption(help = "Name", defaultValue = "") String name) {
        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .assetType(DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .queryName(name)
                .isActive(true)
                .build();
        pageQuery.setLength(terminal.getSize().getRows() - PAGE_FOOTER_SIZE);
        pageQuery.setPage(1);
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);

        helper.print(TABLE_HEADERS
                , PromptColor.GREEN);

        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("ID")
                .column("Kubernetes Instance Name")
                .column("Namespace")
                .column("Deployment Name")
                .displayHeaders(false)
                .borderStyle(BorderStyle.fancy_light)
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .useFullBorder(false);

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
            builder.line(Arrays.asList(
                    String.format(" %-5s|", id),
                    String.format(" %-25s|", kubernetesDsInstanceMap.get(instanceUuid).getDsInstance().getInstanceName()),
                    String.format(" %-15s|", asset.getAssetKey2()),
                    String.format(" %-50s", asset.getAssetKey()))
            );
            id++;
        }
        SessionCommandContext.setIdMapper(idMapper);
        helper.print(helper.renderTable(builder.build()));
        helper.print(buildPagination(table.getTotalNum(),
                pageQuery.getPage(),
                pageQuery.getLength()),
                PromptColor.GREEN);
    }

    public static final String TABLE_HEADERS = buildTableHeaders();

    public static String buildTableHeaders() {
        return TableHeaderBuilder.newBuilder()
                .addHeader("ID", 4)
                .addHeader("Kubernetes Instance Name", 25)
                .addHeader("Namespace", 15)
                .addHeader("Deployment Name", 50)
                .build();
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
