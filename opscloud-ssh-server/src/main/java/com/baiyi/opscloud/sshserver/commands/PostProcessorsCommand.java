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

import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Command to list available post processors
 */
@SshShellComponent
@ShellCommandGroup("Built-In Commands")
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + PostProcessorsCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class PostProcessorsCommand extends AbstractCommand {

    public static final String GROUP = "postprocessors";
    public static final String COMMAND_POST_PROCESSORS = "postprocessors";

    private final List<PostProcessor<?, ?>> postProcessors;

    public PostProcessorsCommand(SshShellHelper helper, SshShellProperties properties,
                                 List<PostProcessor<?, ?>> postProcessors) {
        super(helper, properties, properties.getCommands().getPostprocessors());
        this.postProcessors = new ArrayList<>(postProcessors);
        this.postProcessors.sort(Comparator.comparing(PostProcessor::getName));
    }

    @ShellMethod(key = COMMAND_POST_PROCESSORS, value = "Display the available post processors")
    @ShellMethodAvailability("postprocessorsAvailability")
    public CharSequence postprocessors() {
        AttributedStringBuilder result = new AttributedStringBuilder();
        result.append("Available Post-Processors\n\n", AttributedStyle.BOLD);
        for (PostProcessor<?, ?> postProcessor : postProcessors) {
            result.append("\t" + postProcessor.getName() + ":\n", AttributedStyle.BOLD);
            Class<?> input =
                    ((Class<?>) ((ParameterizedType) (postProcessor.getClass().getGenericInterfaces())[0]).getActualTypeArguments()[0]);
            Class<?> output =
                    ((Class<?>) ((ParameterizedType) (postProcessor.getClass().getGenericInterfaces())[0]).getActualTypeArguments()[1]);
            result.append("\t\thelp   : " + postProcessor.getDescription() + "\n", AttributedStyle.DEFAULT);
            result.append("\t\tinput  : " + input.getName() + "\n", AttributedStyle.DEFAULT);
            result.append("\t\toutput : " + output.getName() + "\n", AttributedStyle.DEFAULT);
        }

        return result;
    }

    private Availability postprocessorsAvailability() {
        return availability(GROUP, COMMAND_POST_PROCESSORS);
    }

}
