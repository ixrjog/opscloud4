package com.baiyi.opscloud.sshserver.command.kubernetes;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.datasource.kubernetes.convert.PodAssetConvert;
import com.baiyi.opscloud.datasource.kubernetes.drive.KubernetesPodDrive;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
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
import com.baiyi.opscloud.sshserver.annotation.CheckTerminalSize;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.command.component.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.KubernetesDsInstance;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import com.baiyi.opscloud.sshserver.command.kubernetes.base.BaseKubernetesCommand;
import com.baiyi.opscloud.sshserver.command.kubernetes.base.PodContext;
import com.baiyi.opscloud.sshserver.command.util.ServerUtil;
import com.baiyi.opscloud.sshserver.config.SshServerArthasConfig;
import com.baiyi.opscloud.sshserver.util.TerminalUtil;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodCondition;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/7 4:26 下午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Kubernetes")
public class KubernetesPodCommand extends BaseKubernetesCommand implements InitializingBean {

    @Resource
    private SshServerArthasConfig arthasConfig;

    @Resource
    private EnvService envService;

    private static byte[] KUBERNETES_EXECUTE_ARTHAS = "curl -O https://arthas.aliyun.com/arthas-boot.jar && java -jar arthas-boot.jar \n".getBytes(StandardCharsets.UTF_8);

    @Resource
    private UserService userService;

    @Resource
    private PodCommandAudit auditPodCommandHandler;

    // ^C
    private static final int QUIT = 3;
    private static final int EOF = 4;

    private void listPodById(int id) {
        Map<Integer, Integer> idMapper = SessionCommandContext.getIdMapper();
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(idMapper.get(id));
        if (!asset.getAssetType().equals(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())) {
            helper.print("资产类型不符", PromptColor.RED);
            return;
        }
        KubernetesConfig kubernetesDsInstanceConfig = buildConfig(asset.getInstanceUuid());
        DatasourceInstance datasourceInstance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        List<Pod> pods = KubernetesPodDrive.listPod(kubernetesDsInstanceConfig.getKubernetes(), asset.getAssetKey2(), asset.getAssetKey());
        if (CollectionUtils.isEmpty(pods)) {
            helper.print(buildPagination(0));
            return;
        }
        PrettyTable pt = PrettyTable
                .fieldNames("ID",
                        "Kubernetes Instance Name",
                        "Namespace",
                        "Pod Name",
                        "Pod IP",
                        "Start Time",
                        "Status",  // Pod状态
                        "Restart Count", // Pod重启次数
                        "Container Name"
                );
        int seq = 1;
        for (Pod pod : pods) {
            idMapper.put(seq, asset.getId());
            List<String> names = pod.getSpec().getContainers().stream().map(Container::getName).collect(Collectors.toList());
            //  return cloudserverList.stream().collect(Collectors.toMap(OcCloudserver::getInstanceId, a -> a, (k1, k2) -> k1));
            Map<String, Boolean> podStatusMap = pod.getStatus().getConditions().stream().collect(Collectors.toMap(PodCondition::getType, a -> Boolean.valueOf(a.getStatus()), (k1, k2) -> k1));
            pt.addRow(seq,
                    datasourceInstance.getInstanceName(),
                    toNamespaceStr(pod.getMetadata().getNamespace()),
                    pod.getMetadata().getName(),
                    StringUtils.isEmpty(pod.getStatus().getPodIP()) ? "N/A" : pod.getStatus().getPodIP(),
                    com.baiyi.opscloud.common.util.TimeUtil.dateToStr(PodAssetConvert.toGmtDate(pod.getStatus().getStartTime())),
                    toPodStatusStr(pod.getStatus().getPhase(), podStatusMap),
                    pod.getStatus().getContainerStatuses().get(0).getRestartCount(), // Restart Count
                    Joiner.on(",").join(names)
            );
            seq++;
        }
        SessionCommandContext.setIdMapper(idMapper);
        helper.print(pt.toString());
        helper.print(buildPagination(pods.size()), PromptColor.GREEN);
    }

    private String toNamespaceStr(String namespace) {
        Env env = envService.getByEnvName(namespace);
        if (env == null)
            return namespace;
        return ServerUtil.toDisplayEnv(env);
    }

    private String toPodStatusStr(String phase, Map<String, Boolean> podStatusMap) {
        if (StringUtils.isEmpty(phase))
            return helper.getColored("N/A", PromptColor.YELLOW);
        Optional<String> r = podStatusMap.keySet().stream().filter(k -> !podStatusMap.get(k)).findFirst();
        if (r.isPresent()) {
            return helper.getColored(phase, PromptColor.YELLOW);
        } else {
            return helper.getColored(phase, PromptColor.GREEN);
        }
    }

    private void listPodByDeploymentName(String deploymentName) {
        Size size = terminal.getSize();
        final int maxSize = size.getRows() - 5;
        DsAssetParam.UserPermissionAssetPageQuery pageQuery = DsAssetParam.UserPermissionAssetPageQuery.builder()
                .assetType(DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .queryName(deploymentName)
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .userId(userService.getByUsername(SessionUtil.getUsername()).getId())
                .page(1)
                .length(terminal.getSize().getRows() - PAGE_FOOTER_SIZE)
                .build();
        DataTable<DatasourceInstanceAsset> table = dsInstanceAssetService.queryPageByParam(pageQuery);

        Map<String, KubernetesDsInstance> kubernetesDsInstanceMap = Maps.newHashMap();
        PrettyTable pt = PrettyTable
                .fieldNames("ID",
                        "Kubernetes Instance Name",
                        "Namespace",
                        "Pod Name",
                        "Pod IP",
                        "Start Time",
                        "Status",  // Pod状态
                        "Restart Count",
                        "Container Name"
                );
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
                List<Pod> pods = KubernetesPodDrive.listPod(kubernetesDsInstanceMap
                        .get(instanceUuid).getKubernetesDsInstanceConfig().getKubernetes(), datasourceAsset.getAssetKey2(), datasourceAsset.getAssetKey());
                if (CollectionUtils.isEmpty(pods)) {
                    log.info("查询Pods为空: namespace = {} , deploymentName = {}", datasourceAsset.getAssetKey2(), datasourceAsset.getAssetKey());
                    continue;
                }
                for (Pod pod : pods) {
                    Map<String, Boolean> podStatusMap = pod.getStatus().getConditions().stream().collect(Collectors.toMap(PodCondition::getType, a -> Boolean.valueOf(a.getStatus()), (k1, k2) -> k1));
                    String podName = pod.getMetadata().getName();
                    PodContext podContext = PodContext.builder()
                            .podName(podName)
                            .instanceUuid(instanceUuid)
                            .namespace(pod.getMetadata().getNamespace())
                            .podIp(pod.getStatus().getPodIP())
                            .build();
                    podMapper.put(seq, podContext);
                    List<String> names = pod.getSpec().getContainers().stream().map(Container::getName).collect(Collectors.toList());

                    pt.addRow(seq,
                            kubernetesDsInstanceMap
                                    .get(instanceUuid).getDsInstance().getInstanceName(),
                            toNamespaceStr(pod.getMetadata().getNamespace()),
                            podName,
                            StringUtils.isEmpty(pod.getStatus().getPodIP()) ? "N/A" : pod.getStatus().getPodIP(),
                            com.baiyi.opscloud.common.util.TimeUtil.dateToStr(PodAssetConvert.toGmtDate(pod.getStatus().getStartTime())),
                            toPodStatusStr(pod.getStatus().getPhase(), podStatusMap),
                            pod.getStatus().getContainerStatuses().get(0).getRestartCount(), // Restart Count
                            Joiner.on(",").join(names)
                    );
                    seq++;
                    if (seq >= maxSize) break;
                }
            } catch (Exception ignored) {
            }
            if (seq >= maxSize) break;
        }
        SessionCommandContext.setPodMapper(podMapper);
        helper.print(pt.toString());
    }

    @CheckTerminalSize(cols = 130, rows = 10)
    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "查询容器组列表信息", key = {"list-k8s-pod"})
    public void listKubernetesPod(@ShellOption(help = "Deployment Name", defaultValue = "") String deploymentName, @ShellOption(help = "Deployment ID", defaultValue = "0") int id) {
        if (id != 0) {
            listPodById(id);
        } else {
            listPodByDeploymentName(deploymentName);
        }
    }

    @ScreenClear
    @InvokeSessionUser
    @ShellMethod(value = "登录容器组,通过参数可指定容器 [ 输入 ctrl+d 退出会话 ]", key = {"login-k8s-pod"})
    public void loginPod(@ShellOption(help = "Pod ID", defaultValue = "") int id,
                         @ShellOption(help = "Container Name", defaultValue = "") String name,
                         @ShellOption(value = {"-R", "--arthas"}, help = "Arthas") boolean arthas) {
        Map<Integer, PodContext> podMapper = SessionCommandContext.getPodMapper();
        PodContext podContext = podMapper.get(id);
        KubernetesConfig kubernetesDsInstanceConfig = buildConfig(podContext.getInstanceUuid());
        KubernetesPodDrive.SimpleListener listener = new KubernetesPodDrive.SimpleListener();
        ServerSession serverSession = helper.getSshSession();
        String sessionId = SessionIdMapper.getSessionId(serverSession.getIoSession());
        String instanceId = TerminalSessionUtil.toInstanceId(podContext.getPodName(), name);
        TerminalSessionInstance terminalSessionInstance =
                TerminalSessionInstanceBuilder.build(sessionId, podContext.getPodIp(), instanceId, InstanceSessionTypeEnum.CONTAINER_TERMINAL);
        simpleTerminalSessionFacade.recordTerminalSessionInstance(
                terminalSessionInstance
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ExecWatch execWatch = KubernetesPodDrive.loginPodContainer(
                kubernetesDsInstanceConfig.getKubernetes(),
                podContext.getNamespace(),
                podContext.getPodName(),
                name,
                listener,
                baos);
        Size size = terminal.getSize();
        execWatch.resize(size.getColumns(), size.getRows());
        TerminalUtil.rawModeSupportVintr(terminal); // 行模式
        SessionOutput sessionOutput = new SessionOutput(sessionId, instanceId);

        SshContext sshContext = getSshContext();
        WatchKubernetesSshOutputTask run = new WatchKubernetesSshOutputTask(sessionOutput, baos, sshContext.getSshShellRunnable().getOs());
        // WatchKubernetesSshOutputTask run = new WatchKubernetesSshOutputTask(sessionOutput, baos, sshContext.getTerminal().writer());
        Thread thread = new Thread(run);
        thread.start();
        // ExecutorService executorService = Executors.newSingleThreadExecutor();
        // NonBlockingInputStreamPumper pump = new NonBlockingInputStreamPumper(sshIO.getIs(), new OutCallback());
        // executorService.submit(pump); // run
        // 执行arthas
        try {
            if (!listener.isClosed()) {
                // 清除输入缓冲区数据
                while (!listener.isClosed()) {
                    int ch = terminal.reader().read(1L);
                    if (ch < 0)
                        break;
                }
            }
            if (arthas) {
                TimeUnit.MILLISECONDS.sleep(200L);
                execWatch.getInput().write(KUBERNETES_EXECUTE_ARTHAS);
                TimeUnit.SECONDS.sleep(1L);
            }
        } catch (IOException | InterruptedException ie) {
            log.error("执行Arthas错误！");
        }
        try {
            while (!listener.isClosed()) {
                int ch = terminal.reader().read(25L);
                if (ch >= 0) {
                    if (ch == EOF) {
                        execWatch.getInput().write(EOF);
                        TimeUnit.MILLISECONDS.sleep(200L); // 等待退出，避免sh残留
                        break;
                    } else {
                        execWatch.getInput().write(ch);
                    }
                }
                tryResize(size, terminal, execWatch);
            }
        } catch (IOException | InterruptedException e) {
            log.error("用户关闭ssh-server: {}", e.getMessage());
        } finally {
            simpleTerminalSessionFacade.closeTerminalSessionInstance(terminalSessionInstance);
            auditPodCommandHandler.recordCommand(sessionId, instanceId);
            execWatch.close();
            helper.print("\n用户退出容器！", PromptColor.GREEN);
        }
    }

    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "显示容器组日志[ 输入 ctrl+c 关闭日志 ]", key = {"show-k8s-pod-log"})
    public void showPodLog(@ShellOption(help = "Pod Asset Id", defaultValue = "") Integer id,
                           @ShellOption(help = "Container Name", defaultValue = "") String name,
                           @ShellOption(help = "Tailing Lines", defaultValue = "100") int lines) {
        Map<Integer, PodContext> podMapper = SessionCommandContext.getPodMapper();
        PodContext podContext = podMapper.get(id);
        KubernetesConfig kubernetesDsInstanceConfig = buildConfig(podContext.getInstanceUuid());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LogWatch logWatch = KubernetesPodDrive.getPodLogWatch(kubernetesDsInstanceConfig.getKubernetes(),
                podContext.getNamespace(),
                podContext.getPodName(),
                name,
                lines, baos);

        TerminalUtil.rawModeSupportVintr(terminal);
        ServerSession serverSession = helper.getSshSession();
        String sessionId = SessionIdMapper.getSessionId(serverSession.getIoSession());
        String instanceId = TerminalSessionUtil.toInstanceId(podContext.getPodName(), name);
        SessionOutput sessionOutput = new SessionOutput(sessionId, instanceId);
        // 高速输出日志流
        SshContext sshContext = getSshContext();
        WatchKubernetesSshOutputTask run = new WatchKubernetesSshOutputTask(sessionOutput, baos, sshContext.getSshShellRunnable().getOs());
        // 低性能输出日志，为了能实现日志换行
        // WatchKubernetesSshOutputTask run = new WatchKubernetesSshOutputTask(sessionOutput, baos, terminal.writer());
        Thread thread = new Thread(run);
        thread.start();
        while (true) {
            try {
                int ch = terminal.reader().read(25L);
                if (ch != -2) {
                    if (ch == QUIT) {
                        run.close();
                        break;
                    } else {
                        terminal.writer().print(helper.getColored("\n输入 [ ctrl+c ] 关闭日志!\n", PromptColor.RED));
                        terminal.writer().flush();
                        TimeUnit.MILLISECONDS.sleep(200L);
                    }
                }
                TimeUnit.MILLISECONDS.sleep(200L);
            } catch (Exception ignored) {
            } finally {
                logWatch.close();
            }
        }

    }

    private SshContext getSshContext() {
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        if (sshContext == null) {
            throw new IllegalStateException("Unable to find ssh context");
        } else {
            return sshContext;
        }
    }

//    private static class OutCallback implements Callback<byte[]> {
//        @Override
//        public void call(byte[] data) {
//            // System.out.print(new String(data));
//        }
//    }

    private void tryResize(Size size, Terminal terminal, ExecWatch execWatch) {
        if (terminal.getSize().equals(size)) return;
        size = terminal.getSize();
        execWatch.resize(size.getColumns(), size.getRows());

    }

    public static String buildPagination(int podSize) {
        return Joiner.on(" ").join("容器组数量:", podSize);
    }


    private byte[] getBytes(char[] chars) {
        Charset cs = StandardCharsets.UTF_8;
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.isEmpty(arthasConfig.getKubernetes())) {
            KUBERNETES_EXECUTE_ARTHAS = arthasConfig.getKubernetes().getBytes(StandardCharsets.UTF_8);
            log.info("从配置文件加载KubernetesArthas命令: {}", arthasConfig.getKubernetes());
        }
    }
}

