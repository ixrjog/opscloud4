package com.baiyi.opscloud.sshserver.command;

import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.kubernetes.handler.KubernetesPodHandler;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.sshserver.*;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.command.component.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.KubernetesDsInstance;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.etc.ColorAligner;
import com.baiyi.opscloud.sshserver.util.KubernetesTableUtil;
import com.baiyi.opscloud.sshserver.util.SessionUtil;
import com.baiyi.opscloud.sshserver.util.TerminalUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Callback;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.utils.NonBlockingInputStreamPumper;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.sshserver.util.KubernetesTableUtil.DIVIDING_LINE;

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
    private static final int EOF = 4;

    protected static final int PAGE_FOOTER_SIZE = 5;

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

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List kubernetes pods", key = {"list-k8s-pod"})
    public void listKubernetesPod(@ShellOption(help = "Name", defaultValue = "") String name) {
        // String sessionId = buildSessionId();

        DsAssetParam.AssetPageQuery pageQuery = DsAssetParam.AssetPageQuery.builder()
                .assetType(DsAssetTypeEnum.KUBERNETES_POD.name())
                .queryName(name)
                .isActive(true)
                .build();
        pageQuery.setLength(terminal.getSize().getRows() - PAGE_FOOTER_SIZE);
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

        Map<Integer, Integer> idMapper = Maps.newHashMap();
        Map<String, KubernetesDsInstance> kubernetesDsInstanceMap = Maps.newHashMap();
        int id = 1;
        for (DatasourceInstanceAsset asset : table.getData()) {
            String instanceUuid = asset.getInstanceUuid();
            if(!kubernetesDsInstanceMap.containsKey(instanceUuid)){
                KubernetesDsInstance kubernetesDsInstance = KubernetesDsInstance.builder()
                        .dsInstance(dsInstanceService.getByUuid(instanceUuid))
                        .kubernetesDsInstanceConfig(buildConfig(instanceUuid))
                        .build();
                kubernetesDsInstanceMap.put(instanceUuid,kubernetesDsInstance);
            }
            Pod pod = KubernetesPodHandler.getPod(kubernetesDsInstanceMap.get(instanceUuid).getKubernetesDsInstanceConfig().getKubernetes(), asset.getAssetKey2(), asset.getName());
            if (pod == null) continue;
            List<Container> containers = pod.getSpec().getContainers();
            List<String> names = containers.stream().map(Container::getName).collect(Collectors.toList());
            idMapper.put(id, asset.getId());
            builder.line(Arrays.asList(
                    String.format(" %-6s|", id),
                    String.format(" %-25s|", kubernetesDsInstanceMap.get(instanceUuid).getDsInstance().getInstanceName()),
                    String.format(" %-50s|", asset.getName()),
                    String.format(" %-16s|", asset.getAssetKey()),
                    String.format(" %-50s", Joiner.on(",").join(names)))
            );
            id++;
        }
        SessionCommandContext.setIdMapper(idMapper);
        helper.print(helper.renderTable(builder.build()));
        helper.print(KubernetesTableUtil.buildPagination(table.getTotalNum(),
                pageQuery.getPage(),
                pageQuery.getLength()),
                PromptColor.GREEN);
    }

    private KubernetesDsInstanceConfig buildConfig(String instanceUuid) {
        DatasourceInstance instance = dsInstanceService.getByUuid(instanceUuid);
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
        return dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);
    }

    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "Show kubernetes (pod)container log [ press Ctrl+C quit ]", key = {"show-k8s-container-log"})
    public void showContainerLog(@ShellOption(help = "Pod Asset Id", defaultValue = "") Integer id,
                                 @ShellOption(help = "Container Name", defaultValue = "") String name,
                                 @ShellOption(help = "Tailing Lines", defaultValue = "100") Integer lines) {
        if (lines == null || lines <= 100)
            lines = 100;
        Map<Integer, Integer> idMapper = SessionCommandContext.getIdMapper();
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(idMapper.get(id));
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = buildConfig(asset.getInstanceUuid());
        LogWatch logWatch = KubernetesPodHandler.getPodLogWatch(kubernetesDsInstanceConfig.getKubernetes(),
                asset.getAssetKey2(),
                asset.getName(),
                name,
                lines);
        Terminal terminal = getTerminal();
        TerminalUtil.rawModeSupportVintr(terminal);
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

    // @InvokeSessionUser(invokeAdmin = true)
    //  @ShellMethod(value = "Show kubernetes (pod)container log [ press ctrl+c quit ]", key = {"exec-container-cmd"})
    public void execContainerCommand(@ShellOption(help = "ID", defaultValue = "") Integer id, @ShellOption(help = "ContainerName", defaultValue = "") String name
            , @ShellOption(help = "Exec Command") String command) {
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(id);
        DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        //  ExecWatch execWatch = KubernetesPodHandler.getPodExecWatch(kubernetesDsInstanceConfig.getKubernetes(), asset.getAssetKey2(), asset.getName(), name, stderr, command);

        Terminal terminal = getTerminal();
        TerminalUtil.rawModeSupportVintr(terminal);
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

    @ScreenClear
    @InvokeSessionUser
    @ShellMethod(value = "Login container [ press ctrl+d quit ]", key = {"login-container"})
    public void loginContainer(@ShellOption(help = "Pod Asset Id", defaultValue = "") Integer id, @ShellOption(help = "Container Name", defaultValue = "") String name) {
        Map<Integer, Integer> idMapper = SessionCommandContext.getIdMapper();
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(idMapper.get(id));
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = buildConfig(asset.getInstanceUuid());

        KubernetesPodHandler.SimpleListener listener = new KubernetesPodHandler.SimpleListener();
        Terminal terminal = getTerminal();
        ExecWatch execWatch = KubernetesPodHandler.loginPodContainer(
                kubernetesDsInstanceConfig.getKubernetes(),
                asset.getAssetKey2(),
                asset.getName(),
                name,
                listener,
                terminal.output());

        Size size = terminal.getSize();
        execWatch.resize(size.getColumns(), size.getRows());

        TerminalUtil.rawModeSupportVintr(terminal); // 行模式
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        NonBlockingInputStreamPumper pump = new NonBlockingInputStreamPumper(execWatch.getOutput(), new OutCallback());
        executorService.submit(pump); // run
        try {
            while (!listener.isClosed()) {
                int ch = terminal.reader().read(25L);
                if (ch >= 0) {
                    if (ch == EOF) {
                        execWatch.getInput().write(EOF);
                        Thread.sleep(200L); // 等待退出，避免sh残留
                        break;
                    } else {
                        execWatch.getInput().write(ch);
                    }
                }
                tryResize(size, terminal, execWatch);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            execWatch.close();
            pump.close();
            helper.print("\n用户退出容器！", PromptColor.GREEN);
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
