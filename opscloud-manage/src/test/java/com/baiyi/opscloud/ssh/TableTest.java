package com.baiyi.opscloud.ssh;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.sshcore.table.PrettyTable;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import org.junit.jupiter.api.Test;


/**
 * @Author baiyi
 * @Date 2021/7/12 2:05 下午
 * @Version 1.0
 */
public class TableTest extends BaseUnit {

    @Test
    void tableTest() {
        PrettyTable pt = PrettyTable
                .fieldNames("name", "age", "city");
        // [32mnew york[0m
        // [31mnew york[0m  8char
        String s = SshShellHelper.getColoredMessage("new york", PromptColor.RED);
        // System.out.println(s);
        pt.addRow(SshShellHelper.getColoredMessage("john", PromptColor.RED) + "," + SshShellHelper.getColoredMessage("b", PromptColor.GREEN), 22, SshShellHelper.getColoredMessage("new york", PromptColor.GREEN))
                .addRow("elizabeth", 43, "chicago")
                .addRow("bill", 31, "atlanta")
                .addRow("mary", 18, "los angeles");
        System.out.println(pt);


    }
}
