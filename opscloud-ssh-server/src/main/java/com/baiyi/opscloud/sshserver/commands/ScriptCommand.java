/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver.commands;

import com.baiyi.opscloud.sshserver.postprocess.PostProcessorObject;
import com.baiyi.opscloud.sshserver.ExtendedShell;
import com.baiyi.opscloud.sshserver.SshContext;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.interactive.Interactive;
import com.baiyi.opscloud.sshserver.interactive.InteractiveInputIO;
import com.baiyi.opscloud.sshserver.interactive.StoppableInteractiveInput;
import com.baiyi.opscloud.sshserver.postprocess.provided.SavePostProcessor;
import com.baiyi.opscloud.sshserver.providers.ExtendedFileValueProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.Parser;
import org.jline.utils.AttributedString;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.Availability;
import org.springframework.shell.jline.FileInputProvider;
import org.springframework.shell.standard.*;
import org.springframework.shell.standard.commands.Script;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.baiyi.opscloud.sshserver.SshShellCommandFactory.SSH_THREAD_CONTEXT;

/**
 * Override history command to get history per user if not shared
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Built-In Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + ScriptCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
@Lazy
public class ScriptCommand extends AbstractCommand
        implements Script.Command, DisposableBean {

    public static final String GROUP = "script";
    public static final String COMMAND_SCRIPT = GROUP;

    private final Parser parser;

    private ExecutorService executor;

    private ScriptStatus status;

    public ScriptCommand(Parser parser, SshShellHelper helper, SshShellProperties properties) {
        super(helper, properties, properties.getCommands().getScript());
        this.parser = parser;
    }

    @Override
    public void destroy() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @ShellMethod(key = COMMAND_SCRIPT, value = "Read and execute commands from a file.")
    @ShellMethodAvailability("scriptAvailability")
    public void script(
            @ShellOption(help = "File to run commands from", defaultValue = ShellOption.NULL, valueProvider = ExtendedFileValueProvider.class) File file,
            @ShellOption(help = "File to write results to", defaultValue = ShellOption.NULL, valueProvider = ExtendedFileValueProvider.class) File output,
            @ShellOption(help = "File to run commands from", defaultValue = "false") boolean background,
            @ShellOption(help = "Action : execute, stop, status (default is execute)", defaultValue = "execute",
                    valueProvider = EnumValueProvider.class) ScriptAction action,
            @ShellOption(help = "Do not launch status directly to get interactive process", defaultValue = "false") boolean notInteractive
    ) throws IOException {
        if (action == ScriptAction.execute) {
            if (file == null) {
                throw new IllegalArgumentException("File is mandatory");
            }
            if (!background) {
                run(file);
            } else {
                if (output == null) {
                    throw new IllegalArgumentException("Cannot use background option without output option for commands results");
                } else if (output.isDirectory()) {
                    throw new IllegalArgumentException("Cannot use given output : it is a directory [" + output.getAbsolutePath() + "]");
                } else if (!output.exists() && !output.createNewFile()) {
                    throw new IllegalArgumentException("Cannot use given output : unable to create file [" + output.getAbsolutePath() + "]");
                }
                if (status != null && !status.getFuture().isDone()) {
                    helper.printWarning("Script already running in background. Aborting.");
                    return;
                }
                try (Stream<String> lines = Files.lines(file.toPath())) {
                    long count = lines.count();
                    SshContext ctx = new SshContext();
                    ctx.setBackground(true);
                    ctx.getPostProcessorsList().add(new PostProcessorObject(SavePostProcessor.SAVE,
                            Collections.singletonList(output.getAbsolutePath())));
                    status = new ScriptStatus(executor().submit(() -> {
                        SSH_THREAD_CONTEXT.set(ctx);
                        try {
                            run(file);
                        } catch (IOException e) {
                            log.warn("Unable to run script command : {}", e.getMessage(), e);
                        }
                    }), output, count, ctx);
                    helper.print("Script from file starting un background. Please check results at " + output.getAbsolutePath() + ".");
                    if (!notInteractive) {
                        progress(status);
                    }
                }
            }
        } else if (action == ScriptAction.stop) {
            if (status != null && !status.getFuture().isDone()) {
                status.getFuture().cancel(true);
            }
            printStatus(status);
        } else {
            // script status
            if (status != null && !status.getFuture().isDone()) {
                progress(status);
            } else {
                printStatus(status);
            }
        }
    }

    private void printStatus(ScriptStatus status) {
        if (status == null) {
            helper.print("No script running in background.");
        } else if (status.getFuture().isDone()) {
            String doneOrCancelled = status.getFuture().isCancelled() ? "stopped" : "done";
            helper.print("Script " + doneOrCancelled + ". " + status.getCount() + " commands executed.");
        }
    }

    private void progress(ScriptStatus status) {
        helper.interactive(Interactive.builder().input((StoppableInteractiveInput) (size, currentDelay) -> {
            List<String> lines = new ArrayList<>();
            lines.add("Script still running. " + status.getCount() + "/" + status.getTotal() + " " +
                    "commands executed so far.");
            lines.add(helper.progress((int) status.getCount(), (int) status.getTotal()));
            lines.add(SshShellHelper.INTERACTIVE_LONG_MESSAGE + "\n");
            return new InteractiveInputIO(status.getFuture().isDone() || status.getFuture().isCancelled(),
                    lines.stream().map(AttributedString::new).collect(Collectors.toList()));

        }).fullScreen(false).refreshDelay(1000).build());
        if (status.getFuture().isDone() || status.getFuture().isCancelled()) {
            helper.print("Script done. " + status.getCount() + " commands executed.");
        }
    }

    private void run(File file) throws IOException {
        Reader reader = new FileReader(file);
        try (FileInputProvider inputProvider = new FileInputProvider(reader, parser)) {
            ((ExtendedShell) getShell()).run(inputProvider, () -> status != null && status.getFuture().isCancelled());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ExecutorService executor() {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
        return executor;
    }

    /**
     * Script action enum
     */
    public enum ScriptAction {
        execute, stop, status
    }

    /**
     * Script status POJO
     */
    @Data
    @AllArgsConstructor
    public static class ScriptStatus {

        private Future<?> future;

        private File result;

        private long total;

        private SshContext sshContext;

        public long getCount() {
            return sshContext.getBackgroundCount();
        }
    }

    private Availability scriptAvailability() {
        return availability(GROUP, COMMAND_SCRIPT);
    }
}
