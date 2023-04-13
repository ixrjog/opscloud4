package com.baiyi.opscloud.sshcore.audit;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/28 10:57 上午
 * @Version 1.0
 */
public class InstanceCommandBuilder {

    private final List<String> outputs;
    private final TerminalSessionInstanceCommand command;

    private InstanceCommandBuilder(TerminalSessionInstanceCommand command) {
        this.outputs = Lists.newArrayList();
        this.command = command;
    }

    static public InstanceCommandBuilder newBuilder(TerminalSessionInstanceCommand command) {
        return new InstanceCommandBuilder(command);
    }

    public InstanceCommandBuilder addOutput(String output) {
        if (outputs.size() < 10) {
            outputs.add(output);
        }
        return this;
    }

    public TerminalSessionInstanceCommand build() {
         command.setOutput(Joiner.on("\n").join(outputs));
         return command;
    }

}