package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.copy.CopyDO;
import com.sdg.cmdb.domain.copy.CopyLogVO;
import com.sdg.cmdb.domain.copy.CopyVO;
import com.sdg.cmdb.service.CopyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/copy")
public class CopyController {

    @Resource
    private CopyService copyService;

    /**
     * 获取Copy分页数据
     *
     * @param businessKey
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryCopyPage(@RequestParam String businessKey, @RequestParam int page, @RequestParam int length) {
        TableVO<List<CopyVO>> tableVO = copyService.getCopyPage(businessKey, page, length);
        return new HttpResult(tableVO);
    }

    /**
     * 获取CopyLog分页数据
     *
     * @param serverName
     * @param envType
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/log/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryCopyLogPage(@RequestParam String serverName, @RequestParam int envType, @RequestParam int page, @RequestParam int length) {
        TableVO<List<CopyLogVO>> tableVO = copyService.getCopyLogPage(serverName, envType, page, length);
        return new HttpResult(tableVO);
    }

    @RequestMapping(value = "/log/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delCopyLog(@RequestParam long id) {
        return new HttpResult(copyService.delCopyLog(id));
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryCopyPage(@RequestParam long id) {
        return new HttpResult(copyService.getCopy(id));
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delCopy(@RequestParam long id) {
        return new HttpResult(copyService.delCopy(id));
    }

    /**
     * 保存Copy配置
     *
     * @param copyDO
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult queryCopyPage(@RequestBody CopyDO copyDO) {
        return new HttpResult(copyService.saveCopy(copyDO));
    }


    /**
     * 增加CopyServer
     *
     * @param copyId
     * @param serverId
     * @return
     */
    @RequestMapping(value = "/addServer", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult addCopyServer(@RequestParam long copyId, @RequestParam long serverId) {
        return new HttpResult(copyService.addCopyServer(copyId, serverId));
    }

    /**
     * 增加CopyServer
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delServer", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delCopyServer(@RequestParam long id) {
        return new HttpResult(copyService.delCopyServer(id));
    }

    /**
     * 执行Copy任务
     *
     * @param copyId
     * @return
     */
    @RequestMapping(value = "/doCopy", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult doCopy(@RequestParam long copyId) {
        return new HttpResult(copyService.doCopy(copyId));
    }

}
