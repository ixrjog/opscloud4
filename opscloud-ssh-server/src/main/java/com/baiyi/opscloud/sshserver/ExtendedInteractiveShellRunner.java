package com.baiyi.opscloud.sshserver;

import org.jline.reader.LineReader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Primary;
import org.springframework.shell.Input;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.shell.context.InteractionMode;
import org.springframework.shell.context.ShellContext;
import org.springframework.shell.jline.InteractiveShellRunner;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.sshserver.SshShellCommandFactory.SSH_THREAD_CONTEXT;

/**
 * Used to clear thread context from post processors and also creates instance of InteractiveShellRunner
 * so that ThrowableResultHandler#shouldHandle() returns true
 */
@Component
@Primary
public class ExtendedInteractiveShellRunner extends InteractiveShellRunner {

    private final LineReader lineReader;
    private final PromptProvider promptProvider;
    private final Shell shell;
    private final ShellContext shellContext;

    public ExtendedInteractiveShellRunner(LineReader lineReader, PromptProvider promptProvider, Shell shell, ShellContext shellContext) {
        super(lineReader, promptProvider, shell, shellContext);
        this.lineReader = lineReader;
        this.promptProvider = promptProvider;
        this.shell = shell;
        this.shellContext = shellContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        shellContext.setInteractionMode(InteractionMode.INTERACTIVE);
        InputProvider inputProvider = new SshShellInputProvider(lineReader, promptProvider);
        shell.run(inputProvider);
    }

    @Override
    public boolean canRun(ApplicationArguments args) {
        return false;
    }

    public static class SshShellInputProvider
            extends InteractiveShellRunner.JLineInputProvider {

        public SshShellInputProvider(LineReader lineReader, PromptProvider promptProvider) {
            super(lineReader, promptProvider);
        }

        @Override
        public Input readInput() {
            SshContext ctx = SSH_THREAD_CONTEXT.get();
            if (ctx != null) {
                ctx.getPostProcessorsList().clear();
            }
            return super.readInput();
        }
    }

}