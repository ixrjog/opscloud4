package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionInstanceCommandVO;
import com.baiyi.opscloud.packer.IWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/3 10:02 上午
 * @Version 1.0
 */
@Component
public class TerminalSessionInstanceCommandPacker implements IWrapper<TerminalSessionInstanceCommandVO.Command> {

    @Override
    public void wrap(TerminalSessionInstanceCommandVO.Command command, IExtend iExtend) {
        if (iExtend.getExtend()) {
            command.setOutputRows(calcLines(command.getOutput()));
        }
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
