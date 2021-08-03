package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionInstanceCommandVO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/3 10:02 上午
 * @Version 1.0
 */
@Component
public class TerminalSessionInstanceCommandPacker {

    public List<TerminalSessionInstanceCommandVO.Command> wrapVOList(List<TerminalSessionInstanceCommand> commands, IExtend iExtend) {
        return commands.stream().map(e -> wrapVO(e, iExtend)).collect(Collectors.toList());
    }

    public TerminalSessionInstanceCommandVO.Command wrapVO(TerminalSessionInstanceCommand terminalSessionInstanceCommand, IExtend iExtend) {
        TerminalSessionInstanceCommandVO.Command command = BeanCopierUtil.copyProperties(terminalSessionInstanceCommand, TerminalSessionInstanceCommandVO.Command.class);
        if (iExtend.getExtend()) {
            command.setOutputRows(calcLines(command.getOutput()));
        }
        return command;
    }

    private Integer calcLines(String output) {
        if (StringUtils.isEmpty(output)) {
            return 0;
        } else {
            int lines = 1;
            int pos = 0;
            while ((pos = output.indexOf("\n", pos) + 1) != 0) {
                lines++;
            }
            return lines;
        }
    }

}
