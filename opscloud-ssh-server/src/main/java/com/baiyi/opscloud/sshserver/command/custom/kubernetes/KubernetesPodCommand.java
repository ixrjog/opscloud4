package com.baiyi.opscloud.sshserver.command.custom.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.converter.PodAssetConverter;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.sshcore.audit.PodCommandAudit;
import com.baiyi.opscloud.sshcore.builder.TerminalSessionInstanceBuilder;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.model.SessionIdMapper;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.table.PrettyTable;
import com.baiyi.opscloud.sshcore.task.ssh.WatchKubernetesSshOutputTask;
import com.baiyi.opscloud.sshcore.util.TerminalSessionUtil;
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
import com.baiyi.opscloud.sshserver.command.custom.kubernetes.base.PodContext;
import com.baiyi.opscloud.sshserver.util.ServerUtil;
import com.baiyi.opscloud.sshserver.util.TerminalUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodCondition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.common.channel.ChannelOutputStream;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.sshcore.util.ChannelShellUtil.DEF_UNICODE;
import static com.baiyi.opscloud.sshserver.constants.TableConstants.TABLE_KUBERNETES_POD_FIELD_NAMES;

/**
 * @Author baiyi
 * @Date 2021/7/7 4:26 下午
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@SshShellComponent
@ShellCommandGroup("Kubernetes")
@RequiredArgsConstructor
public class KubernetesPodCommand extends BaseKubernetesCommand {

    // private static byte[] KUBERNETES_EXECUTE_ARTHAS = "curl -O https://arthas.aliyun.com/arthas-boot.jar && java -jar arthas-boot.jar \n".getBytes(StandardCharsets.UTF_8);

    //private final SshServerArthasConfig arthasConfig;

    private final EnvService envService;

    private final UserService userService;

    private final PodCommandAudit podCommandAudit;

    /**
     * ^C
     */
    private static final int QUIT = 3;
    private static final int EOF = 4;

    private void listPodById(int id) {
        Map<Integer, Integer> idMapper = SessionCommandContext.getIdMapper();
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(idMapper.get(id));
        if (!asset.getAssetType().equals(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())) {
            sshShellHelper.print("资产类型不符", PromptColor.RED);
            return;
        }
        KubernetesConfig kubernetesDsInstanceConfig = buildConfig(asset.getInstanceUuid());
        DatasourceInstance datasourceInstance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        List<Pod> pods = KubernetesPodDriver.list(kubernetesDsInstanceConfig.getKubernetes(), asset.getAssetKey2(), asset.getAssetKey());
        if (CollectionUtils.isEmpty(pods)) {
            sshShellHelper.print(buildPagination(0));
            return;
        }
        PrettyTable pt = PrettyTable.fieldNames(TABLE_KUBERNETES_POD_FIELD_NAMES);
        int seq = 1;
        for (Pod pod : pods) {
            idMapper.put(seq, asset.getId());
            List<String> names = pod.getSpec().getContainers().stream().map(Container::getName).collect(Collectors.toList());
            Map<String, Boolean> podStatusMap = pod.getStatus().getConditions()
                    .stream()
                    .collect(Collectors.toMap(PodCondition::getType, a -> Boolean.valueOf(a.getStatus()), (k1, k2) -> k1));
            pt.addRow(seq,
                    datasourceInstance.getInstanceName(),
                    toNamespaceStr(pod.getMetadata().getNamespace()),
                    pod.getMetadata().getName(),
                    StringUtils.isEmpty(pod.getStatus().getPodIP()) ? "N/A" : pod.getStatus().getPodIP(),
                    NewTimeUtil.parse(PodAssetConverter.toUtcDate(pod.getStatus().getStartTime())),
                    toPodStatusStr(pod.getStatus().getPhase(), podStatusMap),
                    // Restart Count
                    pod.getStatus().getContainerStatuses().get(0).getRestartCount(),
                    Joiner.on(",").join(names));
            seq++;
        }
        SessionCommandContext.setIdMapper(idMapper);
        sshShellHelper.print(pt.toString());
        sshShellHelper.print(buildPagination(pods.size()), PromptColor.GREEN);
    }

    private String toNamespaceStr(String namespace) {
        Env env = envService.getByEnvName(namespace);
        if (env == null) {
            return namespace;
        }
        return ServerUtil.toDisplayEnv(env);
    }

    private String toPodStatusStr(String phase, Map<String, Boolean> podStatusMap) {
        if (StringUtils.isEmpty(phase)) {
            return sshShellHelper.getColored("N/A", PromptColor.YELLOW);
        }
        Optional<String> r = podStatusMap.keySet().stream().filter(k -> !podStatusMap.get(k)).findFirst();
        if (r.isPresent()) {
            return sshShellHelper.getColored(phase, PromptColor.YELLOW);
        } else {
            return sshShellHelper.getColored(phase, PromptColor.GREEN);
        }
    }

    private static boolean tryIgnoreName(KubernetesConfig.Kubernetes kubernetes, String containerName) {
        if (kubernetes.getContainer() == null || org.springframework.util.CollectionUtils.isEmpty(kubernetes.getContainer().getIgnore())) {
            return true;
        }
        for (String name : kubernetes.getContainer().getIgnore()) {
            if (containerName.startsWith(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param deploymentName
     * @ShellOption(help = "Deployment ID", defaultValue = "0") Integer id
     */
    @TerminalSize(cols = 130, rows = 10)
    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List kubernetes pod info.", key = {"list-k8s-pod"})
    public void list(@ShellOption(help = "Deployment", defaultValue = "") String deploymentName) {
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        Size size = terminal.getSize();
        final int maxSize = size.getRows() - 5;
        DsAssetParam.UserPermissionAssetPageQuery pageQuery = DsAssetParam.UserPermissionAssetPageQuery.builder()
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .queryName(deploymentName)
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .userId(userService.getByUsername(SessionHolder.getUsername()).getId())
                .page(1)
                .length(terminal.getSize().getRows() - PAGE_FOOTER_SIZE)
                .build();
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);

        Map<String, KubernetesDsInstance> kubernetesDsInstanceMap = Maps.newHashMap();
        PrettyTable pt = PrettyTable.fieldNames(TABLE_KUBERNETES_POD_FIELD_NAMES);
        Map<Integer, PodContext> podMapper = Maps.newHashMap();
        int seq = 1;
        for (DatasourceInstanceAsset datasourceAsset : table.getData()) {
            String instanceUuid = datasourceAsset.getInstanceUuid();
            if (!kubernetesDsInstanceMap.containsKey(instanceUuid)) {
                KubernetesDsInstance kubernetesDsInstance = KubernetesDsInstance.builder()
                        .dsInstance(dsInstanceService.getByUuid(instanceUuid))
                        .kubernetesDsInstanceConfig(buildConfig(instanceUuid))
                        .build();
                kubernetesDsInstanceMap.put(instanceUuid, kubernetesDsInstance);
            }
            try {
                List<Pod> pods = KubernetesPodDriver.list(kubernetesDsInstanceMap.get(instanceUuid).getKubernetesDsInstanceConfig().getKubernetes(), datasourceAsset.getAssetKey2(), datasourceAsset.getAssetKey());
                if (CollectionUtils.isEmpty(pods)) {
                    log.debug("查询Pods为空: namespace={}, deploymentName={}", datasourceAsset.getAssetKey2(), datasourceAsset.getAssetKey());
                    continue;
                }
                for (Pod pod : pods) {
                    Map<String, Boolean> podStatusMap = pod.getStatus().getConditions().stream().collect(Collectors.toMap(PodCondition::getType, a -> Boolean.valueOf(a.getStatus()), (k1, k2) -> k1));
                    String podName = pod.getMetadata().getName();

                    Set<String> names = pod.getSpec().getContainers()
                            .stream()
                            .map(Container::getName)
                            .toList()
                            .stream()
                            .filter(n -> tryIgnoreName(kubernetesDsInstanceMap.get(instanceUuid).getKubernetesDsInstanceConfig().getKubernetes(), n))
                            .collect(Collectors.toSet());
                    PodContext podContext = PodContext.builder()
                            .podName(podName)
                            .instanceUuid(instanceUuid)
                            .namespace(pod.getMetadata().getNamespace())
                            .podIp(pod.getStatus().getPodIP())
                            .containerNames(names).build();
                    podMapper.put(seq, podContext);
                    // Restart Count
                    pt.addRow(seq,
                            kubernetesDsInstanceMap.get(instanceUuid).getDsInstance().getInstanceName(),
                            toNamespaceStr(pod.getMetadata().getNamespace()),
                            podName,
                            StringUtils.isEmpty(pod.getStatus().getPodIP()) ? "N/A" : pod.getStatus().getPodIP(),
                            NewTimeUtil.parse(PodAssetConverter.toUtcDate(pod.getStatus().getStartTime())),
                            toPodStatusStr(pod.getStatus().getPhase(),
                                    podStatusMap),
                            pod.getStatus().getContainerStatuses().get(0).getRestartCount(),
                            Joiner.on(",").join(names));
                    seq++;
                    if (seq >= maxSize) {
                        break;
                    }
                }
            } catch (Exception ignored) {
            }
            if (seq >= maxSize) {
                break;
            }
        }
        SessionCommandContext.setPodMapper(podMapper);
        sshShellHelper.print(pt.toString());
    }

    /**
     * "登录容器组,通过参数可指定容器 [ 输入 ctrl+d 退出会话 ]
     *
     * @param id
     * @param name
     */
    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(value = "Login kubernetes pod container [Enter ctrl+d to exit the session]", key = {"login-k8s-pod"})
    public void login(@ShellOption(help = "ID", defaultValue = "1") Integer id,
                      @ShellOption(help = "Container", defaultValue = "") String name) {
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        Map<Integer, PodContext> podMapper = SessionCommandContext.getPodMapper();
        PodContext podContext = podMapper.get(id);
        KubernetesConfig kubernetesDsInstanceConfig = buildConfig(podContext.getInstanceUuid());
        if (StringUtils.isEmpty(name)) {
            List<String> names = podContext.getContainerNames().stream()
                    .filter(n -> tryIgnoreName(kubernetesDsInstanceConfig.getKubernetes(), n))
                    .toList();
            if (CollectionUtils.isEmpty(names)) {
                sshShellHelper.print("无效的容器名称！", PromptColor.RED);
                return;
            }
            if (names.size() > 1) {
                sshShellHelper.print("Pod中有多个Container必须指定ContainerName: --name ${containerName}", PromptColor.RED);
                return;
            }
            name = names.get(0);
        }

        KubernetesPodDriver.SimpleListener listener = new KubernetesPodDriver.SimpleListener();
        ServerSession serverSession = sshShellHelper.getSshSession();
        String sessionId = SessionIdMapper.getSessionId(serverSession.getIoSession());
        String instanceId = TerminalSessionUtil.toInstanceId(podContext.getPodName(), name);
        TerminalSessionInstance terminalSessionInstance = TerminalSessionInstanceBuilder
                .build(sessionId, podContext.getPodIp(), instanceId, InstanceSessionTypeEnum.CONTAINER_TERMINAL);
        simpleTerminalSessionFacade.recordTerminalSessionInstance(terminalSessionInstance);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Size size = terminal.getSize();
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetesDsInstanceConfig.getKubernetes());
             ExecWatch execWatch = kc.pods()
                     .inNamespace(podContext.getNamespace())
                     .withName(podContext.getPodName())
                     // 如果Pod中只有一个容器，不需要指定
                     .inContainer(name)
                     .redirectingInput()
                     //.redirectingOutput()
                     //.redirectingError()
                     //.redirectingErrorChannel()
                     .writingOutput(baos)
                     .withTTY()
                     .usingListener(listener)
                     // .exec("env", "TERM=xterm", "COLUMNS=" + columns, "LINES=" + lines, "sh", "-c", "ls -la");
                     .exec("env", "TERM=xterm", "LANG=" + DEF_UNICODE, "COLUMNS=" + size.getColumns(), "LINES=" + size.getRows(), "sh");
        ) {
            //execWatch.resize(size.getColumns(), size.getRows());
            SessionOutput sessionOutput = new SessionOutput(sessionId, instanceId);
            ChannelOutputStream out = (ChannelOutputStream) sshContext.getSshShellRunnable().getOs();
            out.setNoDelay(true);
            WatchKubernetesSshOutputTask run = new WatchKubernetesSshOutputTask(sessionOutput, baos, out);
            // JDK21 VirtualThreads
            Thread.ofVirtual().start(run);

            // 行模式
            TerminalUtil.enterRawMode(terminal);
            while (!listener.isClosed()) {
                int input = terminal.reader().read(5L);
                if (input >= 0) {
                    // execWatch.getInput().write(input);
                    execWatch.getInput().write(Character.toString((char) input).getBytes(StandardCharsets.UTF_8));
                    execWatch.getInput().flush();
                    if (input == EOF) {
                        // 等待退出，避免sh残留
                        NewTimeUtil.millisecondsSleep(200L);
                        break;
                    }
                }
                tryResize(size, terminal, execWatch);
            }
        } catch (Exception e) {
        } finally {
            simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSessionInstance);
            podCommandAudit.asyncRecordCommand(sessionId, instanceId);
            sshShellHelper.print("\n用户退出容器！", PromptColor.GREEN);
        }
    }

    @ScreenClear
    @SettingContextSessionUser(invokeAdmin = true)
    @ShellMethod(value = "Show kubernetes pod container log [Enter ctrl+c to exit the session]", key = {"show-k8s-pod-log"})
    public void showLog(@ShellOption(help = "ID", defaultValue = "") Integer id, @ShellOption(help = "Container", defaultValue = "") String name, @ShellOption(help = "Tailing Lines", defaultValue = "100") int lines) {
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        Map<Integer, PodContext> podMapper = SessionCommandContext.getPodMapper();
        PodContext podContext = podMapper.get(id);
        KubernetesConfig kubernetesDsInstanceConfig = buildConfig(podContext.getInstanceUuid());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (StringUtils.isEmpty(name)) {
            List<String> names = podContext.getContainerNames().stream()
                    .filter(n -> tryIgnoreName(kubernetesDsInstanceConfig.getKubernetes(), n))
                    .toList();
            if (CollectionUtils.isEmpty(names)) {
                sshShellHelper.print("无效的容器名称！", PromptColor.RED);
                return;
            }
            if (names.size() > 1) {
                sshShellHelper.print("Pod中有多个容器必须指定容器名称: --name ${containerName}", PromptColor.RED);
                return;
            }
            name = names.get(0);
        }
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetesDsInstanceConfig.getKubernetes());
             LogWatch logWatch = kc.pods()
                     .inNamespace(podContext.getNamespace())
                     .withName(podContext.getPodName())
                     .inContainer(name)
                     .tailingLines(lines)
                     .watchLog(byteArrayOutputStream);
        ) {
            TerminalUtil.enterRawMode(terminal);
            ServerSession serverSession = sshShellHelper.getSshSession();
            String sessionId = SessionIdMapper.getSessionId(serverSession.getIoSession());
            String instanceId = TerminalSessionUtil.toInstanceId(podContext.getPodName(), name);
            SessionOutput sessionOutput = new SessionOutput(sessionId, instanceId);
            // 高速输出日志流
            WatchKubernetesSshOutputTask run = new WatchKubernetesSshOutputTask(sessionOutput, byteArrayOutputStream, sshContext.getSshShellRunnable().getOs());

            // 低性能输出日志，为了能实现日志换行
            // WatchKubernetesSshOutputTask run = new WatchKubernetesSshOutputTask(sessionOutput, baos, terminal.writer());
            // JDK21 VirtualThreads
            Thread.ofVirtual().start(run);

            while (true) {
                int ch = terminal.reader().read(25L);
                if (ch != -2) {
                    if (ch == QUIT) {
                        run.close();
                        break;
                    } else {
                        terminal.writer().print(sshShellHelper.getColored("\n输入 [ ctrl+c ] 关闭日志!\n", PromptColor.RED));
                        terminal.writer().flush();
                        NewTimeUtil.millisecondsSleep(200L);
                    }
                }
                NewTimeUtil.millisecondsSleep(25L);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void tryResize(Size size, Terminal terminal, ExecWatch execWatch) {
        if (terminal.getSize().equals(size)) {
            return;
        }
        size = terminal.getSize();
        execWatch.resize(size.getColumns(), size.getRows());
    }

    public static String buildPagination(int podSize) {
        return Joiner.on(" ").join("容器组数量:", podSize);
    }

}