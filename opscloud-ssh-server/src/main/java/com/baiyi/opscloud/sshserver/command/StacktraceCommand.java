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

package com.baiyi.opscloud.sshserver.command;

import com.baiyi.opscloud.sshserver.annotation.SshShellComponent;
import com.baiyi.opscloud.sshserver.postprocess.TypePostProcessorResultHandler;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Stacktrace;

/**
 * Override stacktrace command to get error per thread
 */
@SshShellComponent
@ShellCommandGroup("Built-In Commands")
public class StacktraceCommand implements Stacktrace.Command {

    private Terminal terminal;

    @ShellMethod(key = {"stacktrace"}, value = "Display the full stacktrace of the last error.")
    public void stacktrace() {
        Throwable lastError = TypePostProcessorResultHandler.THREAD_CONTEXT.get();
        if (lastError != null) {
            lastError.printStackTrace(this.terminal.writer());
        }
    }

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
