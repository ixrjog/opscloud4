package com.baiyi.opscloud.caesar;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.vo.gitlab.GitlabInstanceVO;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/1/11 1:42 下午
 * @Version 1.0
 */
class CaesarGitlabAPITest extends BaseUnit {

    @Resource
    private CaesarGitlabAPI caesarGitlabAPI;

    @Test
    void queryGitlabInstanceTest() {
        try {
            DataTable<GitlabInstanceVO.Instance> dataTable = caesarGitlabAPI.queryGitlabInstance("");
            System.err.println(JSON.toJSON(dataTable));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    @Test
//    void queryGitlabGroupPageTest() {
//        try {
//            DataTable<GitlabGroupVO.Group> dataTable = caesarGitlabAPI.queryGitlabGroupPage(1, "");
//            System.err.println(JSON.toJSON(dataTable));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}