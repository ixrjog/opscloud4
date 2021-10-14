package com.baiyi.opscloud.tencent.exmail.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailDeptList;
import com.baiyi.opscloud.tencent.exmail.entry.ExmailDept;
import com.baiyi.opscloud.tencent.exmail.http.TencentExmailHttpUtil;
import com.baiyi.opscloud.tencent.exmail.param.ExmailParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/13 10:31 上午
 * @Since 1.0
 */

@Slf4j
@Component
public class TencentExmailDeptHandler {

    @Resource
    private TencentExmailHttpUtil tencentExmailHttpUtil;

    @Resource
    private TencentExmailTokenHandler tencentExmailTokenHandler;

    private interface DeptApi {
        String GET = "/cgi-bin/department/search";
        String LIST = "/cgi-bin/department/list";
    }

    public List<ExmailDept> getDept(DsTencentExmailConfig.Tencent config, String name) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = tencentExmailHttpUtil.getWebHook(config, DeptApi.GET, token);
        try {
            ExmailParam.searchDept param = ExmailParam.searchDept.builder()
                    .name(name)
                    .build();
            JsonNode data = tencentExmailHttpUtil.httpPostExecutor(url, param);
            if (tencentExmailHttpUtil.checkResponse(data)) {
                return JSON.parseObject(data.toString(), ExmailDeptList.class).getDepartment();
            }
            log.error(data.get("errmsg").asText());
        } catch (IOException e) {
            log.error("TencentExmail搜索部门失败", e);
        }
        return Collections.emptyList();
    }

    public List<ExmailDept> listDept(DsTencentExmailConfig.Tencent config, Long deptId) {
        String token = tencentExmailTokenHandler.getToken(config);
        String url = Joiner.on("").join(tencentExmailHttpUtil.getWebHook(config, DeptApi.LIST, token)
                , "&id="
                , deptId);
        try {
            JsonNode data = tencentExmailHttpUtil.httpGetExecutor(url);
            if (tencentExmailHttpUtil.checkResponse(data)) {
                return JSON.parseObject(data.toString(), ExmailDeptList.class).getDepartment();
            }
            log.error(data.get("errmsg").asText());
        } catch (IOException e) {
            log.error("TencentExmail获取部门列表失败", e);
        }
        return null;
    }
}
