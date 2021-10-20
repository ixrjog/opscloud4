package com.baiyi.opscloud.sshserver.ltable;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/10/20 3:45 下午
 * @Version 1.0
 */
@Data
@Builder
public class FieldElement {

    private PromptColor color; // 元素颜色

    private Object value;

    public int getLength() {
        int length = String.valueOf(value).length();
        if (color == null)
            return length;
        return length + 2;
    }

    @Override
    public String toString() {
        String v = String.valueOf(value);
        if (color == null)
            return v;
        return SshShellHelper.getColoredMessage(v, color);
    }

}
