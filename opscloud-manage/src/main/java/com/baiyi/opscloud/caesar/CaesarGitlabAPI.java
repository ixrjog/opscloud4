package com.baiyi.opscloud.caesar;

import com.baiyi.opscloud.caesar.util.CaesarHttpUtils;
import com.baiyi.opscloud.caesar.util.PageUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.opscloud.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabGroupVO;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabInstanceVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.baiyi.opscloud.caesar.util.PageUtils.NOT_EXTEND;

/**
 * @Author baiyi
 * @Date 2021/1/11 11:17 上午
 * @Version 1.0
 */
@Component
public class CaesarGitlabAPI {

    private interface GitlabAPI {
        // 分页查Gitlab实例配置
        String QUERY_GITLAB_INSTANCE_API = "/gitlab/instance/page/query";
        String QUERY_GITLAB_GROUP_PAGE_API = "/gitlab/group/page/query";
        String ADD_GITLAB_GROUP_MEMBER_API = "/gitlab/group/member/add";
    }

    /**
     * 分页查Gitlab实例配置
     *
     * @param queryName
     * @return
     * @throws IOException
     */
    public DataTable<GitlabInstanceVO.Instance> queryGitlabInstance(String queryName) throws IOException {
        GitlabInstanceParam.PageQuery query = new GitlabInstanceParam.PageQuery();
        query.setQueryName(queryName);
        PageUtils.assemblePageParam(query);
        query.setExtend(NOT_EXTEND);
        JsonNode jsonNode = CaesarHttpUtils.httpPostExecutor(GitlabAPI.QUERY_GITLAB_INSTANCE_API, query);
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<GitlabInstanceVO.Instance>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }

    public DataTable<GitlabGroupVO.Group> queryGitlabGroupPage(GitlabGroupParam.GitlabGroupPageQuery query) throws IOException {
        PageUtils.assemblePageParam(query);
        query.setExtend(NOT_EXTEND);
        JsonNode jsonNode = CaesarHttpUtils.httpPostExecutor(GitlabAPI.QUERY_GITLAB_GROUP_PAGE_API, query);
        if (jsonNode.get("success").asBoolean()) {
            Type type = new TypeToken<DataTable<GitlabGroupVO.Group>>() {
            }.getType();
            String data = jsonNode.get("body").toString();
            return new GsonBuilder().create().fromJson(data, type);
        } else {
            return DataTable.EMPTY;
        }
    }

    public BusinessWrapper<Boolean> addGitlabGroupMember(GitlabGroupParam.AddMember addMember) throws IOException {
        JsonNode jsonNode = CaesarHttpUtils.httpPostExecutor(GitlabAPI.ADD_GITLAB_GROUP_MEMBER_API, addMember);
        Type type = new TypeToken<BusinessWrapper<Boolean>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(jsonNode.toString(), type);
    }
}
