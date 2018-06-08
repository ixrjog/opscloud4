package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.explain.ExplainDTO;
import com.sdg.cmdb.service.ExplainService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by zxxiao on 2017/3/22.
 */
@Controller
@RequestMapping("/explain")
public class ExplainController {

    @Resource
    private ExplainService explainService;

    /**
     * 添加审核订阅
     * @param explainDTO
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult addExplain(@RequestBody ExplainDTO explainDTO) {
        explainService.addRepoExplainSub(explainDTO);
        return new HttpResult(true);
    }

    /**
     * 查询指定条件下的审核订阅
     * @param repo
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryExplain(@RequestParam String repo, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(explainService.queryRepoExplainSubList(repo, page, length));
    }

    /**
     * 删除指定id的审核订阅
     * @param id
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delExplain(@RequestParam long id) {
        explainService.delRepoExplainSub(id);
        return new HttpResult(true);
    }

    /**
     * 查询指定条件下的仓库列表
     * @param repo
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/repo/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryRepo(@RequestParam String repo, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(explainService.queryRepoList(repo, page, length));
    }

    /**
     * 扫描获取仓库分支集合
     * @param id
     * @return
     */
    @RequestMapping(value = "/repo/scan", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<Set<String>> invokeExplain(@RequestParam long id) {
        ExplainDTO explainDTO = explainService.getRepoSubById(id);
        if (explainDTO == null) {
            return new HttpResult(ErrorCode.explainNotExist);
        }

        return new HttpResult(explainService.doScanRepo(explainDTO));
    }
}
