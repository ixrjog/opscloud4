package com.baiyi.caesar.sshserver.command;

import com.baiyi.caesar.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.datasource.factory.DsConfigFactory;
import com.baiyi.caesar.datasource.kubernetes.handler.KubernetesPodHandler;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.param.datasource.DsAssetParam;
import com.baiyi.caesar.service.datasource.DsConfigService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import com.baiyi.caesar.sshserver.*;
import com.baiyi.caesar.sshserver.annotation.InvokeSessionUser;
import com.baiyi.caesar.sshserver.command.etc.ColorAligner;
import com.baiyi.caesar.sshserver.util.KubernetesTableUtil;
import com.baiyi.caesar.sshserver.util.SessionUtil;
import com.baiyi.caesar.sshserver.util.TerminalUtil;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Callback;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.utils.NonBlockingInputStreamPumper;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.baiyi.caesar.sshserver.util.KubernetesTableUtil.DIVIDING_LINE;

/**
 * @Author baiyi
 * @Date 2021/6/28 4:30 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Kubernetes")
public class KubernetesCommand {
    // ^C
    private static final int QUIT = 3;

    @Resource
    private SshShellHelper helper;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List kubernetes pods", key = {"list-k8s-pod"})
    public void listKubernetesPod(@ShellOption(help = "Name", defaultValue = "") String name) {
        // String sessionId = buildSessionId();

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
                .column("Pod IP")
                .column("Containers Name")
                .displayHeaders(false)
                .borderStyle(BorderStyle.fancy_light)
                .headerAligner(new ColorAligner(PromptColor.GREEN))
                .useFullBorder(false);

        table.getData().forEach(e -> {
            DatasourceInstance instance = dsInstanceService.getByUuid(e.getInstanceUuid());
            DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
            KubernetesDsInstanceConfig kubernetesDsInstanceConfig = (KubernetesDsInstanceConfig) dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);

            Pod pod = KubernetesPodHandler.getPod(kubernetesDsInstanceConfig.getKubernetes(), e.getAssetKey2(), e.getName());
            if (pod == null) return;
            List<Container> containers = pod.getSpec().getContainers();
            List<String> names = containers.stream().map(Container::getName).collect(Collectors.toList());
            builder.line(Arrays.asList(
                    String.format(" %-6s|", e.getId()),
                    String.format(" %-25s|", instance.getInstanceName()),
                    String.format(" %-50s|", e.getName()),
                    String.format(" %-16s|", e.getAssetKey()),
                    String.format(" %-50s", Joiner.on(",").join(names)))
            );
        });

        helper.print(helper.renderTable(builder.build()));
        helper.print(KubernetesTableUtil.buildPagination(table.getTotalNum(),
                pageQuery.getPage(),
                pageQuery.getLength()),
                PromptColor.GREEN);
    }

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "Show kubernetes (pod)container log [ press Ctrl+C quit ]", key = {"show-k8s-container-log"})
    public void showContainerLog(@ShellOption(help = "ID", defaultValue = "") Integer id, @ShellOption(help = "ContainerName", defaultValue = "") String name) {
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(id);
        DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);
        LogWatch logWatch;
        if (StringUtils.isEmpty(name)) {
            logWatch = KubernetesPodHandler.getPodLogWatch(kubernetesDsInstanceConfig.getKubernetes(), asset.getAssetKey2(), asset.getName());
        } else {
            logWatch = KubernetesPodHandler.getPodLogWatch(kubernetesDsInstanceConfig.getKubernetes(), asset.getAssetKey2(), asset.getName(), name);
        }
        Terminal terminal = getTerminal();
        TerminalUtil.enterRawMode(terminal);
        InputStream inputStream = logWatch.getOutput();
        try {
            InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            while ((br.read()) != -1) {
                terminal.writer().print(br.readLine() + "\n");
                terminal.flush();
                if (terminal.reader().read(25L) == QUIT)
                    break;
            }
            inputStream.close();
            isr.close();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "Show kubernetes (pod)container log [ press ctrl+c quit ]", key = {"exec-container-cmd"})
    public void execContainerCommand(@ShellOption(help = "ID", defaultValue = "") Integer id, @ShellOption(help = "ContainerName", defaultValue = "") String name
            , @ShellOption(help = "Exec Command") String command) {
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(id);
        DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        //  ExecWatch execWatch = KubernetesPodHandler.getPodExecWatch(kubernetesDsInstanceConfig.getKubernetes(), asset.getAssetKey2(), asset.getName(), name, stderr, command);


        Terminal terminal = getTerminal();
        TerminalUtil.enterRawMode(terminal);
        //execWatch.
        //InputStream inputStream = execWatch.getOutput();
        //  try {
        //  InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//            BufferedReader br = new BufferedReader(isr);
//            while ((br.read()) != -1) {
//                terminal.writer().print(br.readLine() + "\n");
//                terminal.flush();
//                if (terminal.reader().read(25L) == QUIT)
//                    break;
//            }
//            inputStream.close();
//            isr.close();
//        } catch (IOException e) {
//            // TODO
//            e.printStackTrace();
//        }
    }


    @InvokeSessionUser
    @ShellMethod(value = "Login container [ press ctrl+c quit ]", key = {"login-container"})
    public void loginContainer(@ShellOption(help = "ID", defaultValue = "") Integer id, @ShellOption(help = "ContainerName", defaultValue = "") String name) {
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(id);
        DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);

        Terminal terminal = getTerminal();
        ExecWatch execWatch = KubernetesPodHandler.loginPodContainer(
                kubernetesDsInstanceConfig.getKubernetes(),
                asset.getAssetKey2(),
                asset.getName(),
                name,
                terminal.output());

        Size size = terminal.getSize();
        execWatch.resize(size.getColumns(), size.getRows());

        TerminalUtil.enterRawMode(terminal); // 行模式
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        NonBlockingInputStreamPumper pump = new NonBlockingInputStreamPumper(execWatch.getOutput(), new OutCallback());
        executorService.submit(pump); // run
        try {
            while (true) {
                int ch = terminal.reader().read(25L);
                if (ch >= 0)
                    execWatch.getInput().write(ch);
                if (ch == QUIT) {
                    helper.print("\n用户正常退出容器！", PromptColor.GREEN);
                    break;
                }
                tryResize(size, terminal, execWatch);
            }
        } catch (Exception e) {
            helper.print("\n用户异常退出容器！", PromptColor.RED);
            e.printStackTrace();
        } finally {
            execWatch.close();
            executorService.shutdownNow();
        }
    }

    private void tryResize(Size size, Terminal terminal, ExecWatch execWatch) {
        if (!terminal.getSize().equals(size)) {
            size = terminal.getSize();
            execWatch.resize(size.getColumns(), size.getRows());
        }
    }

    private static class OutCallback implements Callback<byte[]> {
        @Override
        public void call(byte[] data) {
            // System.out.print(new String(data));
        }
    }

    private Terminal getTerminal() {
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        if (sshContext == null) {
            throw new IllegalStateException("Unable to find ssh context");
        } else {
            return sshContext.getTerminal();
        }
    }

    private String buildSessionId() {
        return SessionUtil.buildSessionId(helper.getSshSession().getIoSession());
    }

}
