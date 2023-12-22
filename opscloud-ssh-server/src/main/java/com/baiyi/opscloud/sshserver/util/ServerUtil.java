package com.baiyi.opscloud.sshserver.util;

import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/13 1:19 下午
 * @Version 1.0
 */
public class ServerUtil {

    private ServerUtil() {
    }

    public static String toDisplayEnv(Env env) {
        if (env.getPromptColor() == null) {
            return env.getEnvName();
        } else {
            return Joiner.on("").join("[", (new AttributedStringBuilder()).append(env.getEnvName(), AttributedStyle.DEFAULT.foreground(env.getPromptColor())).toAnsi(), "]");
        }
    }

    public static String toDisplayEnv(EnvVO.Env env) {
        if (env.getPromptColor() == null) {
            return env.getEnvName();
        } else {
            return Joiner.on("").join("[", (new AttributedStringBuilder()).append(env.getEnvName(), AttributedStyle.DEFAULT.foreground(env.getPromptColor())).toAnsi(), "]");
        }
    }

    public static String toDisplayTag(TagVO.ITags iTags) {
        if (CollectionUtils.isEmpty(iTags.getTags())) {
            return " ";
        }
        return Joiner.on(",").skipNulls().join(iTags.getTags().stream().map(TagVO.Tag::getTagKey).collect(Collectors.toList()));
    }

    public static String toDisplayIp(ServerVO.Server server) {
        if (!StringUtils.isEmpty(server.getPublicIp())) {
            return Joiner.on("/").join(server.getPublicIp(), server.getPrivateIp());
        } else {
            return server.getPrivateIp();
        }
    }

}