package com.baiyi.caesar.sshserver.command;

import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.param.datasource.DsAssetParam;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import com.baiyi.caesar.sshserver.PromptColor;
import com.baiyi.caesar.sshserver.SimpleTable;
import com.baiyi.caesar.sshserver.SshShellHelper;
import com.baiyi.caesar.sshserver.annotation.InvokeSessionUser;
import com.baiyi.caesar.sshserver.command.etc.ColorAligner;
import com.baiyi.caesar.sshserver.util.KubernetesTableUtil;
import com.baiyi.caesar.sshserver.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;

import javax.annotation.Resource;
import java.util.Arrays;

import static com.baiyi.caesar.sshserver.util.KubernetesTableUtil.DIVIDING_LINE;

/**
 * @Author baiyi
 * @Date 2021/6/28 4:30 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("List")
public class ListKubernetesCommand {

    @Resource
    private SshShellHelper helper;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    private Terminal terminal;


    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List Kubernetes Pod", key = {"ls-k8s", "list-k8s"})
    public void listKubernetes(@ShellOption(help = "Name", defaultValue = "") String name, @ShellOption(help = "IP", defaultValue = "") String ip) {
        String sessionId = buildSessionId();

        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .assetType(DsAssetTypeEnum.KUBERNETES_POD.name())
                .queryName(name)
                .isActive(true)
                .build();
        pageQuery.setLength(10);
        pageQuery.setPage(1);
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);

        helper.print(KubernetesTableUtil.TABLE_HEADERS
                , PromptColor.GREEN);
        helper.print(DIVIDING_LINE, PromptColor.GREEN);


        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("ID")
                .column("Kubernetes Instance Name")
                .column("Pod Name")
                .displayHeaders(false)
                .borderStyle(BorderStyle.fancy_light)
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .useFullBorder(false);


        table.getData().forEach(e -> {
            DatasourceInstance instance = dsInstanceService.getByUuid(e.getInstanceUuid());

            builder.line(Arrays.asList(
                    String.format(" %-6s|", e.getId()),
                    String.format(" %-35s|", instance.getInstanceName()),
                    String.format(" %-50s|", e.getName())));

        });

        helper.print(helper.renderTable(builder.build()));
        helper.print(KubernetesTableUtil.buildPagination(table.getTotalNum(),
                pageQuery.getPage(),
                pageQuery.getLength()),
                PromptColor.GREEN);
    }


    private String buildSessionId() {
        return SessionUtil.buildSessionId(helper.getSshSession().getIoSession());
    }

}
